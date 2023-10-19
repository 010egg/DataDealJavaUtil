// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import java.util.List;
import javax.sql.DataSource;
import java.sql.SQLTransientConnectionException;
import com.zaxxer.hikari.metrics.PoolStats;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.Optional;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Callable;
import com.zaxxer.hikari.metrics.dropwizard.CodahaleHealthChecker;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.ExecutorService;
import com.zaxxer.hikari.util.ClockSource;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.BlockingQueue;
import com.zaxxer.hikari.util.UtilityElf;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.HikariConfig;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import com.zaxxer.hikari.util.SuspendResumeLock;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.Collection;
import org.slf4j.Logger;
import com.zaxxer.hikari.util.ConcurrentBag;
import com.zaxxer.hikari.HikariPoolMXBean;

public final class HikariPool extends PoolBase implements HikariPoolMXBean, ConcurrentBag.IBagStateListener
{
    private final Logger LOGGER;
    private static final int POOL_NORMAL = 0;
    private static final int POOL_SUSPENDED = 1;
    private static final int POOL_SHUTDOWN = 2;
    private volatile int poolState;
    private final long ALIVE_BYPASS_WINDOW_MS;
    private final long HOUSEKEEPING_PERIOD_MS;
    private static final String EVICTED_CONNECTION_MESSAGE = "(connection was evicted)";
    private static final String DEAD_CONNECTION_MESSAGE = "(connection is dead)";
    private final PoolEntryCreator POOL_ENTRY_CREATOR;
    private final PoolEntryCreator POST_FILL_POOL_ENTRY_CREATOR;
    private final Collection<Runnable> addConnectionQueue;
    private final ThreadPoolExecutor addConnectionExecutor;
    private final ThreadPoolExecutor closeConnectionExecutor;
    private final ConcurrentBag<PoolEntry> connectionBag;
    private final ProxyLeakTaskFactory leakTaskFactory;
    private final SuspendResumeLock suspendResumeLock;
    private final ScheduledExecutorService houseKeepingExecutorService;
    private ScheduledFuture<?> houseKeeperTask;
    
    public HikariPool(final HikariConfig config) {
        super(config);
        this.LOGGER = LoggerFactory.getLogger(HikariPool.class);
        this.ALIVE_BYPASS_WINDOW_MS = Long.getLong("com.zaxxer.hikari.aliveBypassWindowMs", TimeUnit.MILLISECONDS.toMillis(500L));
        this.HOUSEKEEPING_PERIOD_MS = Long.getLong("com.zaxxer.hikari.housekeeping.periodMs", TimeUnit.SECONDS.toMillis(30L));
        this.POOL_ENTRY_CREATOR = new PoolEntryCreator(null);
        this.POST_FILL_POOL_ENTRY_CREATOR = new PoolEntryCreator("After adding ");
        this.connectionBag = new ConcurrentBag<PoolEntry>(this);
        this.suspendResumeLock = (config.isAllowPoolSuspension() ? new SuspendResumeLock() : SuspendResumeLock.FAUX_LOCK);
        this.houseKeepingExecutorService = this.initializeHouseKeepingExecutorService();
        this.checkFailFast();
        if (config.getMetricsTrackerFactory() != null) {
            this.setMetricsTrackerFactory(config.getMetricsTrackerFactory());
        }
        else {
            this.setMetricRegistry(config.getMetricRegistry());
        }
        this.setHealthCheckRegistry(config.getHealthCheckRegistry());
        this.registerMBeans(this);
        final ThreadFactory threadFactory = config.getThreadFactory();
        final LinkedBlockingQueue<Runnable> addConnectionQueue = new LinkedBlockingQueue<Runnable>(config.getMaximumPoolSize());
        this.addConnectionQueue = Collections.unmodifiableCollection((Collection<? extends Runnable>)addConnectionQueue);
        this.addConnectionExecutor = UtilityElf.createThreadPoolExecutor(addConnectionQueue, this.poolName + " connection adder", threadFactory, new ThreadPoolExecutor.DiscardPolicy());
        this.closeConnectionExecutor = UtilityElf.createThreadPoolExecutor(config.getMaximumPoolSize(), this.poolName + " connection closer", threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        this.leakTaskFactory = new ProxyLeakTaskFactory(config.getLeakDetectionThreshold(), this.houseKeepingExecutorService);
        this.houseKeeperTask = this.houseKeepingExecutorService.scheduleWithFixedDelay(new HouseKeeper(), 100L, this.HOUSEKEEPING_PERIOD_MS, TimeUnit.MILLISECONDS);
    }
    
    public Connection getConnection() throws SQLException {
        return this.getConnection(this.connectionTimeout);
    }
    
    public Connection getConnection(final long hardTimeout) throws SQLException {
        this.suspendResumeLock.acquire();
        final long startTime = ClockSource.currentTime();
        try {
            long timeout = hardTimeout;
            do {
                final PoolEntry poolEntry = this.connectionBag.borrow(timeout, TimeUnit.MILLISECONDS);
                if (poolEntry == null) {
                    break;
                }
                final long now = ClockSource.currentTime();
                if (!poolEntry.isMarkedEvicted() && (ClockSource.elapsedMillis(poolEntry.lastAccessed, now) <= this.ALIVE_BYPASS_WINDOW_MS || this.isConnectionAlive(poolEntry.connection))) {
                    this.metricsTracker.recordBorrowStats(poolEntry, startTime);
                    return poolEntry.createProxyConnection(this.leakTaskFactory.schedule(poolEntry), now);
                }
                this.closeConnection(poolEntry, poolEntry.isMarkedEvicted() ? "(connection was evicted)" : "(connection is dead)");
                timeout = hardTimeout - ClockSource.elapsedMillis(startTime);
            } while (timeout > 0L);
            this.metricsTracker.recordBorrowTimeoutStats(startTime);
            throw this.createTimeoutException(startTime);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException(this.poolName + " - Interrupted during connection acquisition", e);
        }
        finally {
            this.suspendResumeLock.release();
        }
    }
    
    public synchronized void shutdown() throws InterruptedException {
        try {
            this.poolState = 2;
            if (this.addConnectionExecutor == null) {
                return;
            }
            this.logPoolState("Before shutdown ");
            if (this.houseKeeperTask != null) {
                this.houseKeeperTask.cancel(false);
                this.houseKeeperTask = null;
            }
            this.softEvictConnections();
            this.addConnectionExecutor.shutdown();
            this.addConnectionExecutor.awaitTermination(this.getLoginTimeout(), TimeUnit.SECONDS);
            this.destroyHouseKeepingExecutorService();
            this.connectionBag.close();
            final ExecutorService assassinExecutor = UtilityElf.createThreadPoolExecutor(this.config.getMaximumPoolSize(), this.poolName + " connection assassinator", this.config.getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
            try {
                final long start = ClockSource.currentTime();
                do {
                    this.abortActiveConnections(assassinExecutor);
                    this.softEvictConnections();
                } while (this.getTotalConnections() > 0 && ClockSource.elapsedMillis(start) < TimeUnit.SECONDS.toMillis(10L));
            }
            finally {
                assassinExecutor.shutdown();
                assassinExecutor.awaitTermination(10L, TimeUnit.SECONDS);
            }
            this.shutdownNetworkTimeoutExecutor();
            this.closeConnectionExecutor.shutdown();
            this.closeConnectionExecutor.awaitTermination(10L, TimeUnit.SECONDS);
        }
        finally {
            this.logPoolState("After shutdown ");
            this.unregisterMBeans();
            this.metricsTracker.close();
        }
    }
    
    public void evictConnection(final Connection connection) {
        final ProxyConnection proxyConnection = (ProxyConnection)connection;
        proxyConnection.cancelLeakTask();
        try {
            this.softEvictConnection(proxyConnection.getPoolEntry(), "(connection evicted by user)", !connection.isClosed());
        }
        catch (SQLException ex) {}
    }
    
    public void setMetricRegistry(final Object metricRegistry) {
        if (metricRegistry != null && metricRegistry.getClass().getName().contains("MetricRegistry")) {
            this.setMetricsTrackerFactory(new CodahaleMetricsTrackerFactory((MetricRegistry)metricRegistry));
        }
        else if (metricRegistry != null && metricRegistry.getClass().getName().contains("MeterRegistry")) {
            this.setMetricsTrackerFactory(new MicrometerMetricsTrackerFactory((MeterRegistry)metricRegistry));
        }
        else {
            this.setMetricsTrackerFactory(null);
        }
    }
    
    public void setMetricsTrackerFactory(final MetricsTrackerFactory metricsTrackerFactory) {
        if (metricsTrackerFactory != null) {
            this.metricsTracker = new MetricsTrackerDelegate(metricsTrackerFactory.create(this.config.getPoolName(), this.getPoolStats()));
        }
        else {
            this.metricsTracker = new NopMetricsTrackerDelegate();
        }
    }
    
    public void setHealthCheckRegistry(final Object healthCheckRegistry) {
        if (healthCheckRegistry != null) {
            CodahaleHealthChecker.registerHealthChecks(this, this.config, (HealthCheckRegistry)healthCheckRegistry);
        }
    }
    
    @Override
    public void addBagItem(final int waiting) {
        final boolean shouldAdd = waiting - this.addConnectionQueue.size() >= 0;
        if (shouldAdd) {
            this.addConnectionExecutor.submit((Callable<Object>)this.POOL_ENTRY_CREATOR);
        }
    }
    
    @Override
    public int getActiveConnections() {
        return this.connectionBag.getCount(1);
    }
    
    @Override
    public int getIdleConnections() {
        return this.connectionBag.getCount(0);
    }
    
    @Override
    public int getTotalConnections() {
        return this.connectionBag.size();
    }
    
    @Override
    public int getThreadsAwaitingConnection() {
        return this.connectionBag.getWaitingThreadCount();
    }
    
    @Override
    public void softEvictConnections() {
        this.connectionBag.values().forEach(poolEntry -> this.softEvictConnection(poolEntry, "(connection evicted)", false));
    }
    
    @Override
    public synchronized void suspendPool() {
        if (this.suspendResumeLock == SuspendResumeLock.FAUX_LOCK) {
            throw new IllegalStateException(this.poolName + " - is not suspendable");
        }
        if (this.poolState != 1) {
            this.suspendResumeLock.suspend();
            this.poolState = 1;
        }
    }
    
    @Override
    public synchronized void resumePool() {
        if (this.poolState == 1) {
            this.poolState = 0;
            this.fillPool();
            this.suspendResumeLock.resume();
        }
    }
    
    void logPoolState(final String... prefix) {
        if (this.LOGGER.isDebugEnabled()) {
            this.LOGGER.debug("{} - {}stats (total={}, active={}, idle={}, waiting={})", this.poolName, (prefix.length > 0) ? prefix[0] : "", this.getTotalConnections(), this.getActiveConnections(), this.getIdleConnections(), this.getThreadsAwaitingConnection());
        }
    }
    
    @Override
    void recycle(final PoolEntry poolEntry) {
        this.metricsTracker.recordConnectionUsage(poolEntry);
        this.connectionBag.requite(poolEntry);
    }
    
    void closeConnection(final PoolEntry poolEntry, final String closureReason) {
        if (this.connectionBag.remove(poolEntry)) {
            final Connection connection = poolEntry.close();
            this.closeConnectionExecutor.execute(() -> {
                this.quietlyCloseConnection(connection, closureReason);
                if (this.poolState == 0) {
                    this.fillPool();
                }
            });
        }
    }
    
    int[] getPoolStateCounts() {
        return this.connectionBag.getStateCounts();
    }
    
    private PoolEntry createPoolEntry() {
        try {
            final PoolEntry poolEntry = this.newPoolEntry();
            final long maxLifetime = this.config.getMaxLifetime();
            if (maxLifetime > 0L) {
                final long variance = (maxLifetime > 10000L) ? ThreadLocalRandom.current().nextLong(maxLifetime / 40L) : 0L;
                final long lifetime = maxLifetime - variance;
                poolEntry.setFutureEol(this.houseKeepingExecutorService.schedule(() -> {
                    if (this.softEvictConnection(poolEntry, "(connection has passed maxLifetime)", false)) {
                        this.addBagItem(this.connectionBag.getWaitingThreadCount());
                    }
                    return;
                }, lifetime, TimeUnit.MILLISECONDS));
            }
            return poolEntry;
        }
        catch (Exception e) {
            if (this.poolState == 0) {
                this.LOGGER.debug("{} - Cannot acquire connection from data source", this.poolName, (e instanceof ConnectionSetupException) ? e.getCause() : e);
            }
            return null;
        }
    }
    
    private synchronized void fillPool() {
        for (int connectionsToAdd = Math.min(this.config.getMaximumPoolSize() - this.getTotalConnections(), this.config.getMinimumIdle() - this.getIdleConnections()) - this.addConnectionQueue.size(), i = 0; i < connectionsToAdd; ++i) {
            this.addConnectionExecutor.submit((Callable<Object>)((i < connectionsToAdd - 1) ? this.POOL_ENTRY_CREATOR : this.POST_FILL_POOL_ENTRY_CREATOR));
        }
    }
    
    private void abortActiveConnections(final ExecutorService assassinExecutor) {
        for (final PoolEntry poolEntry : this.connectionBag.values(1)) {
            final Connection connection = poolEntry.close();
            try {
                connection.abort(assassinExecutor);
            }
            catch (Throwable e) {
                this.quietlyCloseConnection(connection, "(connection aborted during shutdown)");
            }
            finally {
                this.connectionBag.remove(poolEntry);
            }
        }
    }
    
    private void checkFailFast() {
        final long initializationTimeout = this.config.getInitializationFailTimeout();
        if (initializationTimeout < 0L) {
            return;
        }
        final long startTime = ClockSource.currentTime();
        do {
            final PoolEntry poolEntry = this.createPoolEntry();
            if (poolEntry != null) {
                if (this.config.getMinimumIdle() > 0) {
                    this.connectionBag.add(poolEntry);
                    this.LOGGER.debug("{} - Added connection {}", this.poolName, poolEntry.connection);
                }
                else {
                    this.quietlyCloseConnection(poolEntry.close(), "(initialization check complete and minimumIdle is zero)");
                }
                return;
            }
            if (this.getLastConnectionFailure() instanceof ConnectionSetupException) {
                this.throwPoolInitializationException(this.getLastConnectionFailure().getCause());
            }
            UtilityElf.quietlySleep(TimeUnit.SECONDS.toMillis(1L));
        } while (ClockSource.elapsedMillis(startTime) < initializationTimeout);
        if (initializationTimeout > 0L) {
            this.throwPoolInitializationException(this.getLastConnectionFailure());
        }
    }
    
    private void throwPoolInitializationException(final Throwable t) {
        this.LOGGER.error("{} - Exception during pool initialization.", this.poolName, t);
        this.destroyHouseKeepingExecutorService();
        throw new PoolInitializationException(t);
    }
    
    private boolean softEvictConnection(final PoolEntry poolEntry, final String reason, final boolean owner) {
        poolEntry.markEvicted();
        if (owner || this.connectionBag.reserve(poolEntry)) {
            this.closeConnection(poolEntry, reason);
            return true;
        }
        return false;
    }
    
    private ScheduledExecutorService initializeHouseKeepingExecutorService() {
        if (this.config.getScheduledExecutor() == null) {
            final ThreadFactory threadFactory = Optional.ofNullable(this.config.getThreadFactory()).orElse(new UtilityElf.DefaultThreadFactory(this.poolName + " housekeeper", true));
            final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory, new ThreadPoolExecutor.DiscardPolicy());
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            executor.setRemoveOnCancelPolicy(true);
            return executor;
        }
        return this.config.getScheduledExecutor();
    }
    
    private void destroyHouseKeepingExecutorService() {
        if (this.config.getScheduledExecutor() == null) {
            this.houseKeepingExecutorService.shutdownNow();
        }
    }
    
    private PoolStats getPoolStats() {
        return new PoolStats(TimeUnit.SECONDS.toMillis(1L)) {
            @Override
            protected void update() {
                this.pendingThreads = HikariPool.this.getThreadsAwaitingConnection();
                this.idleConnections = HikariPool.this.getIdleConnections();
                this.totalConnections = HikariPool.this.getTotalConnections();
                this.activeConnections = HikariPool.this.getActiveConnections();
            }
        };
    }
    
    private SQLException createTimeoutException(final long startTime) {
        this.logPoolState("Timeout failure ");
        this.metricsTracker.recordConnectionTimeout();
        String sqlState = null;
        final Throwable originalException = this.getLastConnectionFailure();
        if (originalException instanceof SQLException) {
            sqlState = ((SQLException)originalException).getSQLState();
        }
        final SQLException connectionException = new SQLTransientConnectionException(this.poolName + " - Connection is not available, request timed out after " + ClockSource.elapsedMillis(startTime) + "ms.", sqlState, originalException);
        if (originalException instanceof SQLException) {
            connectionException.setNextException((SQLException)originalException);
        }
        return connectionException;
    }
    
    private final class PoolEntryCreator implements Callable<Boolean>
    {
        private final String loggingPrefix;
        
        PoolEntryCreator(final String loggingPrefix) {
            this.loggingPrefix = loggingPrefix;
        }
        
        @Override
        public Boolean call() throws Exception {
            long sleepBackoff = 250L;
            while (HikariPool.this.poolState == 0 && this.shouldCreateAnotherConnection()) {
                final PoolEntry poolEntry = HikariPool.this.createPoolEntry();
                if (poolEntry != null) {
                    HikariPool.this.connectionBag.add(poolEntry);
                    HikariPool.this.LOGGER.debug("{} - Added connection {}", HikariPool.this.poolName, poolEntry.connection);
                    if (this.loggingPrefix != null) {
                        HikariPool.this.logPoolState(this.loggingPrefix);
                    }
                    return Boolean.TRUE;
                }
                UtilityElf.quietlySleep(sleepBackoff);
                sleepBackoff = Math.min(TimeUnit.SECONDS.toMillis(10L), Math.min(HikariPool.this.connectionTimeout, (long)(sleepBackoff * 1.5)));
            }
            return Boolean.FALSE;
        }
        
        private boolean shouldCreateAnotherConnection() {
            return HikariPool.this.getTotalConnections() < HikariPool.this.config.getMaximumPoolSize() && (HikariPool.this.connectionBag.getWaitingThreadCount() > 0 || HikariPool.this.getIdleConnections() < HikariPool.this.config.getMinimumIdle());
        }
    }
    
    private final class HouseKeeper implements Runnable
    {
        private volatile long previous;
        
        private HouseKeeper() {
            this.previous = ClockSource.plusMillis(ClockSource.currentTime(), -HikariPool.this.HOUSEKEEPING_PERIOD_MS);
        }
        
        @Override
        public void run() {
            try {
                HikariPool.this.connectionTimeout = HikariPool.this.config.getConnectionTimeout();
                HikariPool.this.validationTimeout = HikariPool.this.config.getValidationTimeout();
                HikariPool.this.leakTaskFactory.updateLeakDetectionThreshold(HikariPool.this.config.getLeakDetectionThreshold());
                final long idleTimeout = HikariPool.this.config.getIdleTimeout();
                final long now = ClockSource.currentTime();
                if (ClockSource.plusMillis(now, 128L) < ClockSource.plusMillis(this.previous, HikariPool.this.HOUSEKEEPING_PERIOD_MS)) {
                    HikariPool.this.LOGGER.warn("{} - Retrograde clock change detected (housekeeper delta={}), soft-evicting connections from pool.", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, now));
                    this.previous = now;
                    HikariPool.this.softEvictConnections();
                    return;
                }
                if (now > ClockSource.plusMillis(this.previous, 3L * HikariPool.this.HOUSEKEEPING_PERIOD_MS / 2L)) {
                    HikariPool.this.LOGGER.warn("{} - Thread starvation or clock leap detected (housekeeper delta={}).", HikariPool.this.poolName, ClockSource.elapsedDisplayString(this.previous, now));
                }
                this.previous = now;
                String afterPrefix = "Pool ";
                if (idleTimeout > 0L && HikariPool.this.config.getMinimumIdle() < HikariPool.this.config.getMaximumPoolSize()) {
                    HikariPool.this.logPoolState("Before cleanup ");
                    afterPrefix = "After cleanup  ";
                    final List<PoolEntry> notInUse = HikariPool.this.connectionBag.values(0);
                    int toRemove = notInUse.size() - HikariPool.this.config.getMinimumIdle();
                    for (final PoolEntry entry : notInUse) {
                        if (toRemove > 0 && ClockSource.elapsedMillis(entry.lastAccessed, now) > idleTimeout && HikariPool.this.connectionBag.reserve(entry)) {
                            HikariPool.this.closeConnection(entry, "(connection has passed idleTimeout)");
                            --toRemove;
                        }
                    }
                }
                HikariPool.this.logPoolState(afterPrefix);
                HikariPool.this.fillPool();
            }
            catch (Exception e) {
                HikariPool.this.LOGGER.error("Unexpected exception in housekeeping task", e);
            }
        }
    }
    
    public static class PoolInitializationException extends RuntimeException
    {
        private static final long serialVersionUID = 929872118275916520L;
        
        public PoolInitializationException(final Throwable t) {
            super("Failed to initialize pool: " + t.getMessage(), t);
        }
    }
}
