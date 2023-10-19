// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import java.sql.SQLTransientConnectionException;
import com.zaxxer.hikari.util.ClockSource;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import com.zaxxer.hikari.util.DriverDataSource;
import com.zaxxer.hikari.util.PropertyElf;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import com.zaxxer.hikari.util.UtilityElf;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicReference;
import javax.sql.DataSource;
import java.util.concurrent.Executor;
import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;

abstract class PoolBase
{
    private final Logger LOGGER;
    protected final HikariConfig config;
    protected final String poolName;
    long connectionTimeout;
    long validationTimeout;
    IMetricsTrackerDelegate metricsTracker;
    private static final String[] RESET_STATES;
    private static final int UNINITIALIZED = -1;
    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private int networkTimeout;
    private int isNetworkTimeoutSupported;
    private int isQueryTimeoutSupported;
    private int defaultTransactionIsolation;
    private int transactionIsolation;
    private Executor netTimeoutExecutor;
    private DataSource dataSource;
    private final String catalog;
    private final String schema;
    private final boolean isReadOnly;
    private final boolean isAutoCommit;
    private final boolean isUseJdbc4Validation;
    private final boolean isIsolateInternalQueries;
    private final AtomicReference<Throwable> lastConnectionFailure;
    private volatile boolean isValidChecked;
    
    PoolBase(final HikariConfig config) {
        this.LOGGER = LoggerFactory.getLogger(PoolBase.class);
        this.config = config;
        this.networkTimeout = -1;
        this.catalog = config.getCatalog();
        this.schema = config.getSchema();
        this.isReadOnly = config.isReadOnly();
        this.isAutoCommit = config.isAutoCommit();
        this.transactionIsolation = UtilityElf.getTransactionIsolation(config.getTransactionIsolation());
        this.isQueryTimeoutSupported = -1;
        this.isNetworkTimeoutSupported = -1;
        this.isUseJdbc4Validation = (config.getConnectionTestQuery() == null);
        this.isIsolateInternalQueries = config.isIsolateInternalQueries();
        this.poolName = config.getPoolName();
        this.connectionTimeout = config.getConnectionTimeout();
        this.validationTimeout = config.getValidationTimeout();
        this.lastConnectionFailure = new AtomicReference<Throwable>();
        this.initializeDataSource();
    }
    
    @Override
    public String toString() {
        return this.poolName;
    }
    
    abstract void recycle(final PoolEntry p0);
    
    void quietlyCloseConnection(final Connection connection, final String closureReason) {
        if (connection != null) {
            try {
                this.LOGGER.debug("{} - Closing connection {}: {}", this.poolName, connection, closureReason);
                try {
                    this.setNetworkTimeout(connection, TimeUnit.SECONDS.toMillis(15L));
                }
                finally {
                    connection.close();
                }
            }
            catch (Throwable e) {
                this.LOGGER.debug("{} - Closing connection {} failed", this.poolName, connection, e);
            }
        }
    }
    
    boolean isConnectionAlive(final Connection connection) {
        try {
            try {
                this.setNetworkTimeout(connection, this.validationTimeout);
                final int validationSeconds = (int)Math.max(1000L, this.validationTimeout) / 1000;
                if (this.isUseJdbc4Validation) {
                    return connection.isValid(validationSeconds);
                }
                try (final Statement statement = connection.createStatement()) {
                    if (this.isNetworkTimeoutSupported != 1) {
                        this.setQueryTimeout(statement, validationSeconds);
                    }
                    statement.execute(this.config.getConnectionTestQuery());
                }
            }
            finally {
                this.setNetworkTimeout(connection, this.networkTimeout);
                if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                    connection.rollback();
                }
            }
            return true;
        }
        catch (Exception e) {
            this.lastConnectionFailure.set(e);
            this.LOGGER.warn("{} - Failed to validate connection {} ({})", this.poolName, connection, e.getMessage());
            return false;
        }
    }
    
    Throwable getLastConnectionFailure() {
        return this.lastConnectionFailure.get();
    }
    
    public DataSource getUnwrappedDataSource() {
        return this.dataSource;
    }
    
    PoolEntry newPoolEntry() throws Exception {
        return new PoolEntry(this.newConnection(), this, this.isReadOnly, this.isAutoCommit);
    }
    
    void resetConnectionState(final Connection connection, final ProxyConnection proxyConnection, final int dirtyBits) throws SQLException {
        int resetBits = 0;
        if ((dirtyBits & 0x1) != 0x0 && proxyConnection.getReadOnlyState() != this.isReadOnly) {
            connection.setReadOnly(this.isReadOnly);
            resetBits |= 0x1;
        }
        if ((dirtyBits & 0x2) != 0x0 && proxyConnection.getAutoCommitState() != this.isAutoCommit) {
            connection.setAutoCommit(this.isAutoCommit);
            resetBits |= 0x2;
        }
        if ((dirtyBits & 0x4) != 0x0 && proxyConnection.getTransactionIsolationState() != this.transactionIsolation) {
            connection.setTransactionIsolation(this.transactionIsolation);
            resetBits |= 0x4;
        }
        if ((dirtyBits & 0x8) != 0x0 && this.catalog != null && !this.catalog.equals(proxyConnection.getCatalogState())) {
            connection.setCatalog(this.catalog);
            resetBits |= 0x8;
        }
        if ((dirtyBits & 0x10) != 0x0 && proxyConnection.getNetworkTimeoutState() != this.networkTimeout) {
            this.setNetworkTimeout(connection, this.networkTimeout);
            resetBits |= 0x10;
        }
        if ((dirtyBits & 0x20) != 0x0 && this.schema != null && !this.schema.equals(proxyConnection.getSchemaState())) {
            connection.setSchema(this.schema);
            resetBits |= 0x20;
        }
        if (resetBits != 0 && this.LOGGER.isDebugEnabled()) {
            this.LOGGER.debug("{} - Reset ({}) on connection {}", this.poolName, this.stringFromResetBits(resetBits), connection);
        }
    }
    
    void shutdownNetworkTimeoutExecutor() {
        if (this.netTimeoutExecutor instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor)this.netTimeoutExecutor).shutdownNow();
        }
    }
    
    long getLoginTimeout() {
        try {
            return (this.dataSource != null) ? this.dataSource.getLoginTimeout() : TimeUnit.SECONDS.toSeconds(5L);
        }
        catch (SQLException e) {
            return TimeUnit.SECONDS.toSeconds(5L);
        }
    }
    
    void registerMBeans(final HikariPool hikariPool) {
        if (!this.config.isRegisterMbeans()) {
            return;
        }
        try {
            final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            final ObjectName beanConfigName = new ObjectName("com.zaxxer.hikari:type=PoolConfig (" + this.poolName + ")");
            final ObjectName beanPoolName = new ObjectName("com.zaxxer.hikari:type=Pool (" + this.poolName + ")");
            if (!mBeanServer.isRegistered(beanConfigName)) {
                mBeanServer.registerMBean(this.config, beanConfigName);
                mBeanServer.registerMBean(hikariPool, beanPoolName);
            }
            else {
                this.LOGGER.error("{} - JMX name ({}) is already registered.", this.poolName, this.poolName);
            }
        }
        catch (Exception e) {
            this.LOGGER.warn("{} - Failed to register management beans.", this.poolName, e);
        }
    }
    
    void unregisterMBeans() {
        if (!this.config.isRegisterMbeans()) {
            return;
        }
        try {
            final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            final ObjectName beanConfigName = new ObjectName("com.zaxxer.hikari:type=PoolConfig (" + this.poolName + ")");
            final ObjectName beanPoolName = new ObjectName("com.zaxxer.hikari:type=Pool (" + this.poolName + ")");
            if (mBeanServer.isRegistered(beanConfigName)) {
                mBeanServer.unregisterMBean(beanConfigName);
                mBeanServer.unregisterMBean(beanPoolName);
            }
        }
        catch (Exception e) {
            this.LOGGER.warn("{} - Failed to unregister management beans.", this.poolName, e);
        }
    }
    
    private void initializeDataSource() {
        final String jdbcUrl = this.config.getJdbcUrl();
        final String username = this.config.getUsername();
        final String password = this.config.getPassword();
        final String dsClassName = this.config.getDataSourceClassName();
        final String driverClassName = this.config.getDriverClassName();
        final String dataSourceJNDI = this.config.getDataSourceJNDI();
        final Properties dataSourceProperties = this.config.getDataSourceProperties();
        DataSource dataSource = this.config.getDataSource();
        if (dsClassName != null && dataSource == null) {
            dataSource = UtilityElf.createInstance(dsClassName, DataSource.class, new Object[0]);
            PropertyElf.setTargetFromProperties(dataSource, dataSourceProperties);
        }
        else if (jdbcUrl != null && dataSource == null) {
            dataSource = new DriverDataSource(jdbcUrl, driverClassName, dataSourceProperties, username, password);
        }
        else if (dataSourceJNDI != null && dataSource == null) {
            try {
                final InitialContext ic = new InitialContext();
                dataSource = (DataSource)ic.lookup(dataSourceJNDI);
            }
            catch (NamingException e) {
                throw new HikariPool.PoolInitializationException(e);
            }
        }
        if (dataSource != null) {
            this.setLoginTimeout(dataSource);
            this.createNetworkTimeoutExecutor(dataSource, dsClassName, jdbcUrl);
        }
        this.dataSource = dataSource;
    }
    
    private Connection newConnection() throws Exception {
        final long start = ClockSource.currentTime();
        Connection connection = null;
        try {
            final String username = this.config.getUsername();
            final String password = this.config.getPassword();
            connection = ((username == null) ? this.dataSource.getConnection() : this.dataSource.getConnection(username, password));
            if (connection == null) {
                throw new SQLTransientConnectionException("DataSource returned null unexpectedly");
            }
            this.setupConnection(connection);
            this.lastConnectionFailure.set(null);
            return connection;
        }
        catch (Exception e) {
            if (connection != null) {
                this.quietlyCloseConnection(connection, "(Failed to create/setup connection)");
            }
            else if (this.getLastConnectionFailure() == null) {
                this.LOGGER.debug("{} - Failed to create/setup connection: {}", this.poolName, e.getMessage());
            }
            this.lastConnectionFailure.set(e);
            throw e;
        }
        finally {
            if (this.metricsTracker != null) {
                this.metricsTracker.recordConnectionCreated(ClockSource.elapsedMillis(start));
            }
        }
    }
    
    private void setupConnection(final Connection connection) throws ConnectionSetupException {
        try {
            if (this.networkTimeout == -1) {
                this.networkTimeout = this.getAndSetNetworkTimeout(connection, this.validationTimeout);
            }
            else {
                this.setNetworkTimeout(connection, this.validationTimeout);
            }
            connection.setReadOnly(this.isReadOnly);
            connection.setAutoCommit(this.isAutoCommit);
            this.checkDriverSupport(connection);
            if (this.transactionIsolation != this.defaultTransactionIsolation) {
                connection.setTransactionIsolation(this.transactionIsolation);
            }
            if (this.catalog != null) {
                connection.setCatalog(this.catalog);
            }
            if (this.schema != null) {
                connection.setSchema(this.schema);
            }
            this.executeSql(connection, this.config.getConnectionInitSql(), true);
            this.setNetworkTimeout(connection, this.networkTimeout);
        }
        catch (SQLException e) {
            throw new ConnectionSetupException(e);
        }
    }
    
    private void checkDriverSupport(final Connection connection) throws SQLException {
        if (!this.isValidChecked) {
            try {
                if (this.isUseJdbc4Validation) {
                    connection.isValid(1);
                }
                else {
                    this.executeSql(connection, this.config.getConnectionTestQuery(), false);
                }
            }
            catch (Throwable e) {
                this.LOGGER.error("{} - Failed to execute" + (this.isUseJdbc4Validation ? " isValid() for connection, configure" : "") + " connection test query ({}).", this.poolName, e.getMessage());
                throw e;
            }
            try {
                this.defaultTransactionIsolation = connection.getTransactionIsolation();
                if (this.transactionIsolation == -1) {
                    this.transactionIsolation = this.defaultTransactionIsolation;
                }
            }
            catch (SQLException e2) {
                this.LOGGER.warn("{} - Default transaction isolation level detection failed ({}).", this.poolName, e2.getMessage());
                if (e2.getSQLState() != null && !e2.getSQLState().startsWith("08")) {
                    throw e2;
                }
            }
            this.isValidChecked = true;
        }
    }
    
    private void setQueryTimeout(final Statement statement, final int timeoutSec) {
        if (this.isQueryTimeoutSupported != 0) {
            try {
                statement.setQueryTimeout(timeoutSec);
                this.isQueryTimeoutSupported = 1;
            }
            catch (Throwable e) {
                if (this.isQueryTimeoutSupported == -1) {
                    this.isQueryTimeoutSupported = 0;
                    this.LOGGER.info("{} - Failed to set query timeout for statement. ({})", this.poolName, e.getMessage());
                }
            }
        }
    }
    
    private int getAndSetNetworkTimeout(final Connection connection, final long timeoutMs) {
        if (this.isNetworkTimeoutSupported != 0) {
            try {
                final int originalTimeout = connection.getNetworkTimeout();
                connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
                this.isNetworkTimeoutSupported = 1;
                return originalTimeout;
            }
            catch (Throwable e) {
                if (this.isNetworkTimeoutSupported == -1) {
                    this.isNetworkTimeoutSupported = 0;
                    this.LOGGER.info("{} - Driver does not support get/set network timeout for connections. ({})", this.poolName, e.getMessage());
                    if (this.validationTimeout < TimeUnit.SECONDS.toMillis(1L)) {
                        this.LOGGER.warn("{} - A validationTimeout of less than 1 second cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
                    }
                    else if (this.validationTimeout % TimeUnit.SECONDS.toMillis(1L) != 0L) {
                        this.LOGGER.warn("{} - A validationTimeout with fractional second granularity cannot be honored on drivers without setNetworkTimeout() support.", this.poolName);
                    }
                }
            }
        }
        return 0;
    }
    
    private void setNetworkTimeout(final Connection connection, final long timeoutMs) throws SQLException {
        if (this.isNetworkTimeoutSupported == 1) {
            connection.setNetworkTimeout(this.netTimeoutExecutor, (int)timeoutMs);
        }
    }
    
    private void executeSql(final Connection connection, final String sql, final boolean isCommit) throws SQLException {
        if (sql != null) {
            try (final Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
            if (this.isIsolateInternalQueries && !this.isAutoCommit) {
                if (isCommit) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
            }
        }
    }
    
    private void createNetworkTimeoutExecutor(final DataSource dataSource, final String dsClassName, final String jdbcUrl) {
        if ((dsClassName != null && dsClassName.contains("Mysql")) || (jdbcUrl != null && jdbcUrl.contains("mysql")) || (dataSource != null && dataSource.getClass().getName().contains("Mysql"))) {
            this.netTimeoutExecutor = new SynchronousExecutor();
        }
        else {
            ThreadFactory threadFactory = this.config.getThreadFactory();
            threadFactory = ((threadFactory != null) ? threadFactory : new UtilityElf.DefaultThreadFactory(this.poolName + " network timeout executor", true));
            final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool(threadFactory);
            executor.setKeepAliveTime(15L, TimeUnit.SECONDS);
            executor.allowCoreThreadTimeOut(true);
            this.netTimeoutExecutor = executor;
        }
    }
    
    private void setLoginTimeout(final DataSource dataSource) {
        if (this.connectionTimeout != 2147483647L) {
            try {
                dataSource.setLoginTimeout(Math.max(1, (int)TimeUnit.MILLISECONDS.toSeconds(500L + this.connectionTimeout)));
            }
            catch (Throwable e) {
                this.LOGGER.info("{} - Failed to set login timeout for data source. ({})", this.poolName, e.getMessage());
            }
        }
    }
    
    private String stringFromResetBits(final int bits) {
        final StringBuilder sb = new StringBuilder();
        for (int ndx = 0; ndx < PoolBase.RESET_STATES.length; ++ndx) {
            if ((bits & 1 << ndx) != 0x0) {
                sb.append(PoolBase.RESET_STATES[ndx]).append(", ");
            }
        }
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
    
    static {
        RESET_STATES = new String[] { "readOnly", "autoCommit", "isolation", "catalog", "netTimeout", "schema" };
    }
    
    static class ConnectionSetupException extends Exception
    {
        private static final long serialVersionUID = 929872118275916521L;
        
        ConnectionSetupException(final Throwable t) {
            super(t);
        }
    }
    
    private static class SynchronousExecutor implements Executor
    {
        @Override
        public void execute(final Runnable command) {
            try {
                command.run();
            }
            catch (Throwable t) {
                LoggerFactory.getLogger(PoolBase.class).debug("Failed to execute: {}", command, t);
            }
        }
    }
    
    interface IMetricsTrackerDelegate extends AutoCloseable
    {
        default void recordConnectionUsage(final PoolEntry poolEntry) {
        }
        
        default void recordConnectionCreated(final long connectionCreatedMillis) {
        }
        
        default void recordBorrowTimeoutStats(final long startTime) {
        }
        
        default void recordBorrowStats(final PoolEntry poolEntry, final long startTime) {
        }
        
        default void recordConnectionTimeout() {
        }
        
        default void close() {
        }
    }
    
    static class MetricsTrackerDelegate implements IMetricsTrackerDelegate
    {
        final IMetricsTracker tracker;
        
        MetricsTrackerDelegate(final IMetricsTracker tracker) {
            this.tracker = tracker;
        }
        
        @Override
        public void recordConnectionUsage(final PoolEntry poolEntry) {
            this.tracker.recordConnectionUsageMillis(poolEntry.getMillisSinceBorrowed());
        }
        
        @Override
        public void recordConnectionCreated(final long connectionCreatedMillis) {
            this.tracker.recordConnectionCreatedMillis(connectionCreatedMillis);
        }
        
        @Override
        public void recordBorrowTimeoutStats(final long startTime) {
            this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime));
        }
        
        @Override
        public void recordBorrowStats(final PoolEntry poolEntry, final long startTime) {
            final long now = ClockSource.currentTime();
            poolEntry.lastBorrowed = now;
            this.tracker.recordConnectionAcquiredNanos(ClockSource.elapsedNanos(startTime, now));
        }
        
        @Override
        public void recordConnectionTimeout() {
            this.tracker.recordConnectionTimeout();
        }
        
        @Override
        public void close() {
            this.tracker.close();
        }
    }
    
    static final class NopMetricsTrackerDelegate implements IMetricsTrackerDelegate
    {
    }
}
