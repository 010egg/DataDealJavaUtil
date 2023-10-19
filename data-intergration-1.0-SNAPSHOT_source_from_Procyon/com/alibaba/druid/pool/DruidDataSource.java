// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import com.alibaba.druid.support.logging.LogFactory;
import javax.management.MBeanServer;
import java.util.concurrent.locks.Lock;
import com.alibaba.druid.wall.WallFilter;
import com.alibaba.druid.wall.WallProviderStatValue;
import com.alibaba.druid.stat.JdbcSqlStat;
import javax.management.JMException;
import com.alibaba.druid.util.JMXUtils;
import java.util.HashMap;
import com.alibaba.druid.VERSION;
import com.alibaba.druid.TransactionTimeoutException;
import com.alibaba.druid.proxy.jdbc.TransactionInfo;
import java.util.LinkedHashMap;
import java.util.ConcurrentModificationException;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.StringRefAddr;
import javax.naming.Reference;
import java.security.AccessController;
import javax.management.ObjectName;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import java.security.PrivilegedAction;
import java.sql.Statement;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionEvent;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.alibaba.druid.util.StringUtils;
import java.util.TimeZone;
import java.sql.Connection;
import javax.sql.PooledConnection;
import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.pool.vendor.DB2ExceptionSorter;
import com.alibaba.druid.pool.vendor.MockExceptionSorter;
import com.alibaba.druid.pool.vendor.PGExceptionSorter;
import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;
import com.alibaba.druid.pool.vendor.InformixExceptionSorter;
import com.alibaba.druid.pool.vendor.OceanBaseOracleExceptionSorter;
import com.alibaba.druid.pool.vendor.OracleExceptionSorter;
import com.alibaba.druid.pool.vendor.MySqlExceptionSorter;
import com.alibaba.druid.pool.vendor.NullExceptionSorter;
import com.alibaba.druid.pool.vendor.PGValidConnectionChecker;
import com.alibaba.druid.pool.vendor.MSSQLValidConnectionChecker;
import com.alibaba.druid.pool.vendor.OracleValidConnectionChecker;
import com.alibaba.druid.pool.vendor.MySqlValidConnectionChecker;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.support.clickhouse.BalancedClickhouseDriver;
import com.alibaba.druid.mock.MockDriver;
import com.alibaba.druid.proxy.jdbc.DataSourceProxyConfig;
import com.alibaba.druid.filter.AutoLoad;
import java.util.ServiceLoader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Date;
import com.alibaba.druid.DbType;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.proxy.DruidDriver;
import java.util.Map;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Collections;
import java.util.StringTokenizer;
import java.sql.SQLException;
import com.alibaba.druid.util.Utils;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import com.alibaba.druid.filter.Filter;
import java.util.List;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import com.alibaba.druid.support.logging.Log;
import javax.management.MBeanRegistration;
import javax.sql.ConnectionPoolDataSource;
import java.io.Closeable;
import javax.naming.Referenceable;

public class DruidDataSource extends DruidAbstractDataSource implements DruidDataSourceMBean, ManagedDataSource, Referenceable, Closeable, Cloneable, ConnectionPoolDataSource, MBeanRegistration
{
    private static final Log LOG;
    private static final long serialVersionUID = 1L;
    private volatile long recycleErrorCount;
    private long connectCount;
    private long closeCount;
    private volatile long connectErrorCount;
    private long recycleCount;
    private long removeAbandonedCount;
    private long notEmptyWaitCount;
    private long notEmptySignalCount;
    private long notEmptyWaitNanos;
    private int keepAliveCheckCount;
    private int activePeak;
    private long activePeakTime;
    private int poolingPeak;
    private long poolingPeakTime;
    private volatile DruidConnectionHolder[] connections;
    private int poolingCount;
    private int activeCount;
    private volatile long discardCount;
    private int notEmptyWaitThreadCount;
    private int notEmptyWaitThreadPeak;
    private DruidConnectionHolder[] evictConnections;
    private DruidConnectionHolder[] keepAliveConnections;
    private volatile ScheduledFuture<?> destroySchedulerFuture;
    private DestroyTask destroyTask;
    private volatile Future<?> createSchedulerFuture;
    private CreateConnectionThread createConnectionThread;
    private DestroyConnectionThread destroyConnectionThread;
    private LogStatsThread logStatsThread;
    private int createTaskCount;
    private volatile long createTaskIdSeed;
    private long[] createTasks;
    private final CountDownLatch initedLatch;
    private volatile boolean enable;
    private boolean resetStatEnable;
    private volatile long resetCount;
    private String initStackTrace;
    private volatile boolean closing;
    private volatile boolean closed;
    private long closeTimeMillis;
    protected JdbcDataSourceStat dataSourceStat;
    private boolean useGlobalDataSourceStat;
    private boolean mbeanRegistered;
    public static ThreadLocal<Long> waitNanosLocal;
    private boolean logDifferentThread;
    private volatile boolean keepAlive;
    private boolean asyncInit;
    protected boolean killWhenSocketReadTimeout;
    protected boolean checkExecuteTime;
    private static List<Filter> autoFilters;
    private boolean loadSpifilterSkip;
    private volatile DataSourceDisableException disableException;
    protected static final AtomicLongFieldUpdater<DruidDataSource> recycleErrorCountUpdater;
    protected static final AtomicLongFieldUpdater<DruidDataSource> connectErrorCountUpdater;
    protected static final AtomicLongFieldUpdater<DruidDataSource> resetCountUpdater;
    protected static final AtomicLongFieldUpdater<DruidDataSource> createTaskIdSeedUpdater;
    protected String instanceKey;
    
    public DruidDataSource() {
        this(false);
    }
    
    public DruidDataSource(final boolean fairLock) {
        super(fairLock);
        this.recycleErrorCount = 0L;
        this.connectCount = 0L;
        this.closeCount = 0L;
        this.connectErrorCount = 0L;
        this.recycleCount = 0L;
        this.removeAbandonedCount = 0L;
        this.notEmptyWaitCount = 0L;
        this.notEmptySignalCount = 0L;
        this.notEmptyWaitNanos = 0L;
        this.keepAliveCheckCount = 0;
        this.activePeak = 0;
        this.activePeakTime = 0L;
        this.poolingPeak = 0;
        this.poolingPeakTime = 0L;
        this.poolingCount = 0;
        this.activeCount = 0;
        this.discardCount = 0L;
        this.notEmptyWaitThreadCount = 0;
        this.notEmptyWaitThreadPeak = 0;
        this.createTaskIdSeed = 1L;
        this.initedLatch = new CountDownLatch(2);
        this.enable = true;
        this.resetStatEnable = true;
        this.resetCount = 0L;
        this.closing = false;
        this.closed = false;
        this.closeTimeMillis = -1L;
        this.useGlobalDataSourceStat = false;
        this.mbeanRegistered = false;
        this.logDifferentThread = true;
        this.keepAlive = false;
        this.asyncInit = false;
        this.killWhenSocketReadTimeout = false;
        this.checkExecuteTime = false;
        this.loadSpifilterSkip = false;
        this.disableException = null;
        this.instanceKey = null;
        this.configFromPropety(System.getProperties());
    }
    
    public boolean isAsyncInit() {
        return this.asyncInit;
    }
    
    public void setAsyncInit(final boolean asyncInit) {
        this.asyncInit = asyncInit;
    }
    
    public void configFromPropety(final Properties properties) {
        String property = properties.getProperty("druid.name");
        if (property != null) {
            this.setName(property);
        }
        property = properties.getProperty("druid.url");
        if (property != null) {
            this.setUrl(property);
        }
        property = properties.getProperty("druid.username");
        if (property != null) {
            this.setUsername(property);
        }
        property = properties.getProperty("druid.password");
        if (property != null) {
            this.setPassword(property);
        }
        Boolean value = Utils.getBoolean(properties, "druid.testWhileIdle");
        if (value != null) {
            this.testWhileIdle = value;
        }
        value = Utils.getBoolean(properties, "druid.testOnBorrow");
        if (value != null) {
            this.testOnBorrow = value;
        }
        property = properties.getProperty("druid.validationQuery");
        if (property != null && property.length() > 0) {
            this.setValidationQuery(property);
        }
        value = Utils.getBoolean(properties, "druid.useGlobalDataSourceStat");
        if (value != null) {
            this.setUseGlobalDataSourceStat(value);
        }
        value = Utils.getBoolean(properties, "druid.useGloalDataSourceStat");
        if (value != null) {
            this.setUseGlobalDataSourceStat(value);
        }
        value = Utils.getBoolean(properties, "druid.asyncInit");
        if (value != null) {
            this.setAsyncInit(value);
        }
        property = properties.getProperty("druid.filters");
        if (property != null && property.length() > 0) {
            try {
                this.setFilters(property);
            }
            catch (SQLException e) {
                DruidDataSource.LOG.error("setFilters error", e);
            }
        }
        property = properties.getProperty("druid.timeBetweenLogStatsMillis");
        if (property != null && property.length() > 0) {
            try {
                final long value2 = Long.parseLong(property);
                this.setTimeBetweenLogStatsMillis(value2);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.timeBetweenLogStatsMillis'", e2);
            }
        }
        property = properties.getProperty("druid.stat.sql.MaxSize");
        if (property != null && property.length() > 0) {
            try {
                final int value3 = Integer.parseInt(property);
                if (this.dataSourceStat != null) {
                    this.dataSourceStat.setMaxSqlSize(value3);
                }
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.stat.sql.MaxSize'", e2);
            }
        }
        value = Utils.getBoolean(properties, "druid.clearFiltersEnable");
        if (value != null) {
            this.setClearFiltersEnable(value);
        }
        value = Utils.getBoolean(properties, "druid.resetStatEnable");
        if (value != null) {
            this.setResetStatEnable(value);
        }
        property = properties.getProperty("druid.notFullTimeoutRetryCount");
        if (property != null && property.length() > 0) {
            try {
                final int value3 = Integer.parseInt(property);
                this.setNotFullTimeoutRetryCount(value3);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.notFullTimeoutRetryCount'", e2);
            }
        }
        property = properties.getProperty("druid.timeBetweenEvictionRunsMillis");
        if (property != null && property.length() > 0) {
            try {
                final long value2 = Long.parseLong(property);
                this.setTimeBetweenEvictionRunsMillis(value2);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.timeBetweenEvictionRunsMillis'", e2);
            }
        }
        property = properties.getProperty("druid.maxWaitThreadCount");
        if (property != null && property.length() > 0) {
            try {
                final int value3 = Integer.parseInt(property);
                this.setMaxWaitThreadCount(value3);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.maxWaitThreadCount'", e2);
            }
        }
        property = properties.getProperty("druid.maxWait");
        if (property != null && property.length() > 0) {
            try {
                final int value3 = Integer.parseInt(property);
                this.setMaxWait(value3);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.maxWait'", e2);
            }
        }
        value = Utils.getBoolean(properties, "druid.failFast");
        if (value != null) {
            this.setFailFast(value);
        }
        property = properties.getProperty("druid.phyTimeoutMillis");
        if (property != null && property.length() > 0) {
            try {
                final long value2 = Long.parseLong(property);
                this.setPhyTimeoutMillis(value2);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.phyTimeoutMillis'", e2);
            }
        }
        property = properties.getProperty("druid.phyMaxUseCount");
        if (property != null && property.length() > 0) {
            try {
                final long value2 = Long.parseLong(property);
                this.setPhyMaxUseCount(value2);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.phyMaxUseCount'", e2);
            }
        }
        property = properties.getProperty("druid.minEvictableIdleTimeMillis");
        if (property != null && property.length() > 0) {
            try {
                final long value2 = Long.parseLong(property);
                this.setMinEvictableIdleTimeMillis(value2);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.minEvictableIdleTimeMillis'", e2);
            }
        }
        property = properties.getProperty("druid.maxEvictableIdleTimeMillis");
        if (property != null && property.length() > 0) {
            try {
                final long value2 = Long.parseLong(property);
                this.setMaxEvictableIdleTimeMillis(value2);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.maxEvictableIdleTimeMillis'", e2);
            }
        }
        value = Utils.getBoolean(properties, "druid.keepAlive");
        if (value != null) {
            this.setKeepAlive(value);
        }
        property = properties.getProperty("druid.keepAliveBetweenTimeMillis");
        if (property != null && property.length() > 0) {
            try {
                final long value2 = Long.parseLong(property);
                this.setKeepAliveBetweenTimeMillis(value2);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.keepAliveBetweenTimeMillis'", e2);
            }
        }
        value = Utils.getBoolean(properties, "druid.poolPreparedStatements");
        if (value != null) {
            this.setPoolPreparedStatements0(value);
        }
        value = Utils.getBoolean(properties, "druid.initVariants");
        if (value != null) {
            this.setInitVariants(value);
        }
        value = Utils.getBoolean(properties, "druid.initGlobalVariants");
        if (value != null) {
            this.setInitGlobalVariants(value);
        }
        value = Utils.getBoolean(properties, "druid.useUnfairLock");
        if (value != null) {
            this.setUseUnfairLock(value);
        }
        property = properties.getProperty("druid.driverClassName");
        if (property != null) {
            this.setDriverClassName(property);
        }
        property = properties.getProperty("druid.initialSize");
        if (property != null && property.length() > 0) {
            try {
                final int value3 = Integer.parseInt(property);
                this.setInitialSize(value3);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.initialSize'", e2);
            }
        }
        property = properties.getProperty("druid.minIdle");
        if (property != null && property.length() > 0) {
            try {
                final int value3 = Integer.parseInt(property);
                this.setMinIdle(value3);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.minIdle'", e2);
            }
        }
        property = properties.getProperty("druid.maxActive");
        if (property != null && property.length() > 0) {
            try {
                final int value3 = Integer.parseInt(property);
                this.setMaxActive(value3);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.maxActive'", e2);
            }
        }
        value = Utils.getBoolean(properties, "druid.killWhenSocketReadTimeout");
        if (value != null) {
            this.setKillWhenSocketReadTimeout(value);
        }
        property = properties.getProperty("druid.connectProperties");
        if (property != null) {
            this.setConnectionProperties(property);
        }
        property = properties.getProperty("druid.maxPoolPreparedStatementPerConnectionSize");
        if (property != null && property.length() > 0) {
            try {
                final int value3 = Integer.parseInt(property);
                this.setMaxPoolPreparedStatementPerConnectionSize(value3);
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.maxPoolPreparedStatementPerConnectionSize'", e2);
            }
        }
        property = properties.getProperty("druid.initConnectionSqls");
        if (property != null && property.length() > 0) {
            try {
                final StringTokenizer tokenizer = new StringTokenizer(property, ";");
                this.setConnectionInitSqls(Collections.list((Enumeration<Object>)tokenizer));
            }
            catch (NumberFormatException e2) {
                DruidDataSource.LOG.error("illegal property 'druid.initConnectionSqls'", e2);
            }
        }
        property = System.getProperty("druid.load.spifilter.skip");
        if (property != null && !"false".equals(property)) {
            this.loadSpifilterSkip = true;
        }
        property = System.getProperty("druid.checkExecuteTime");
        if (property != null && !"false".equals(property)) {
            this.checkExecuteTime = true;
        }
    }
    
    public boolean isKillWhenSocketReadTimeout() {
        return this.killWhenSocketReadTimeout;
    }
    
    public void setKillWhenSocketReadTimeout(final boolean killWhenSocketTimeOut) {
        this.killWhenSocketReadTimeout = killWhenSocketTimeOut;
    }
    
    @Override
    public boolean isUseGlobalDataSourceStat() {
        return this.useGlobalDataSourceStat;
    }
    
    public void setUseGlobalDataSourceStat(final boolean useGlobalDataSourceStat) {
        this.useGlobalDataSourceStat = useGlobalDataSourceStat;
    }
    
    public boolean isKeepAlive() {
        return this.keepAlive;
    }
    
    public void setKeepAlive(final boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
    
    public String getInitStackTrace() {
        return this.initStackTrace;
    }
    
    @Override
    public boolean isResetStatEnable() {
        return this.resetStatEnable;
    }
    
    @Override
    public void setResetStatEnable(final boolean resetStatEnable) {
        this.resetStatEnable = resetStatEnable;
        if (this.dataSourceStat != null) {
            this.dataSourceStat.setResetStatEnable(resetStatEnable);
        }
    }
    
    @Override
    public long getDiscardCount() {
        return this.discardCount;
    }
    
    public void restart() throws SQLException {
        this.restart(null);
    }
    
    public void restart(final Properties properties) throws SQLException {
        this.lock.lock();
        try {
            if (this.activeCount > 0) {
                throw new SQLException("can not restart, activeCount not zero. " + this.activeCount);
            }
            if (DruidDataSource.LOG.isInfoEnabled()) {
                DruidDataSource.LOG.info("{dataSource-" + this.getID() + "} restart");
            }
            this.close();
            this.resetStat();
            this.inited = false;
            this.enable = true;
            this.closed = false;
            if (properties != null) {
                this.configFromPropety(properties);
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public void resetStat() {
        if (!this.isResetStatEnable()) {
            return;
        }
        this.lock.lock();
        try {
            this.connectCount = 0L;
            this.closeCount = 0L;
            this.discardCount = 0L;
            this.recycleCount = 0L;
            this.createCount = 0L;
            this.directCreateCount = 0;
            this.destroyCount = 0L;
            this.removeAbandonedCount = 0L;
            this.notEmptyWaitCount = 0L;
            this.notEmptySignalCount = 0L;
            this.notEmptyWaitNanos = 0L;
            this.activePeak = this.activeCount;
            this.activePeakTime = 0L;
            this.poolingPeak = 0;
            this.createTimespan = 0L;
            this.lastError = null;
            this.lastErrorTimeMillis = 0L;
            this.lastCreateError = null;
            this.lastCreateErrorTimeMillis = 0L;
        }
        finally {
            this.lock.unlock();
        }
        DruidDataSource.connectErrorCountUpdater.set(this, 0L);
        DruidDataSource.errorCountUpdater.set(this, 0L);
        DruidDataSource.commitCountUpdater.set(this, 0L);
        DruidDataSource.rollbackCountUpdater.set(this, 0L);
        DruidDataSource.startTransactionCountUpdater.set(this, 0L);
        DruidDataSource.cachedPreparedStatementHitCountUpdater.set(this, 0L);
        DruidDataSource.closedPreparedStatementCountUpdater.set(this, 0L);
        DruidDataSource.preparedStatementCountUpdater.set(this, 0L);
        this.transactionHistogram.reset();
        DruidDataSource.cachedPreparedStatementDeleteCountUpdater.set(this, 0L);
        DruidDataSource.recycleErrorCountUpdater.set(this, 0L);
        DruidDataSource.resetCountUpdater.incrementAndGet(this);
    }
    
    @Override
    public long getResetCount() {
        return this.resetCount;
    }
    
    @Override
    public boolean isEnable() {
        return this.enable;
    }
    
    @Override
    public void setEnable(final boolean enable) {
        this.lock.lock();
        try {
            if (!(this.enable = enable)) {
                this.notEmpty.signalAll();
                ++this.notEmptySignalCount;
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public void setPoolPreparedStatements(final boolean value) {
        this.setPoolPreparedStatements0(value);
    }
    
    private void setPoolPreparedStatements0(final boolean value) {
        if (this.poolPreparedStatements == value) {
            return;
        }
        this.poolPreparedStatements = value;
        if (!this.inited) {
            return;
        }
        if (DruidDataSource.LOG.isInfoEnabled()) {
            DruidDataSource.LOG.info("set poolPreparedStatements " + this.poolPreparedStatements + " -> " + value);
        }
        if (!value) {
            this.lock.lock();
            try {
                for (int i = 0; i < this.poolingCount; ++i) {
                    final DruidConnectionHolder connection = this.connections[i];
                    for (final PreparedStatementHolder holder : connection.getStatementPool().getMap().values()) {
                        this.closePreapredStatement(holder);
                    }
                    connection.getStatementPool().getMap().clear();
                }
            }
            finally {
                this.lock.unlock();
            }
        }
    }
    
    @Override
    public void setMaxActive(final int maxActive) {
        if (this.maxActive == maxActive) {
            return;
        }
        if (maxActive == 0) {
            throw new IllegalArgumentException("maxActive can't not set zero");
        }
        if (!this.inited) {
            this.maxActive = maxActive;
            return;
        }
        if (maxActive < this.minIdle) {
            throw new IllegalArgumentException("maxActive less than minIdle, " + maxActive + " < " + this.minIdle);
        }
        if (DruidDataSource.LOG.isInfoEnabled()) {
            DruidDataSource.LOG.info("maxActive changed : " + this.maxActive + " -> " + maxActive);
        }
        this.lock.lock();
        try {
            final int allCount = this.poolingCount + this.activeCount;
            if (maxActive > allCount) {
                this.connections = Arrays.copyOf(this.connections, maxActive);
                this.evictConnections = new DruidConnectionHolder[maxActive];
                this.keepAliveConnections = new DruidConnectionHolder[maxActive];
            }
            else {
                this.connections = Arrays.copyOf(this.connections, allCount);
                this.evictConnections = new DruidConnectionHolder[allCount];
                this.keepAliveConnections = new DruidConnectionHolder[allCount];
            }
            this.maxActive = maxActive;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public void setConnectProperties(Properties properties) {
        if (properties == null) {
            properties = new Properties();
        }
        boolean equals;
        if (properties.size() == this.connectProperties.size()) {
            equals = true;
            for (final Map.Entry entry : properties.entrySet()) {
                final Object value = this.connectProperties.get(entry.getKey());
                final Object entryValue = entry.getValue();
                if (value == null && entryValue != null) {
                    equals = false;
                    break;
                }
                if (!value.equals(entry.getValue())) {
                    equals = false;
                    break;
                }
            }
        }
        else {
            equals = false;
        }
        if (!equals) {
            if (this.inited && DruidDataSource.LOG.isInfoEnabled()) {
                DruidDataSource.LOG.info("connectProperties changed : " + this.connectProperties + " -> " + properties);
            }
            this.configFromPropety(properties);
            for (final Filter filter : this.filters) {
                filter.configFromProperties(properties);
            }
            if (this.exceptionSorter != null) {
                this.exceptionSorter.configFromProperties(properties);
            }
            if (this.validConnectionChecker != null) {
                this.validConnectionChecker.configFromProperties(properties);
            }
            if (this.statLogger != null) {
                this.statLogger.configFromProperties(properties);
            }
        }
        this.connectProperties = properties;
    }
    
    public void init() throws SQLException {
        if (this.inited) {
            return;
        }
        DruidDriver.getInstance();
        final ReentrantLock lock = this.lock;
        try {
            lock.lockInterruptibly();
        }
        catch (InterruptedException e) {
            throw new SQLException("interrupt", e);
        }
        boolean init = false;
        try {
            if (this.inited) {
                return;
            }
            this.initStackTrace = Utils.toString(Thread.currentThread().getStackTrace());
            this.id = DruidDriver.createDataSourceId();
            if (this.id > 1L) {
                final long delta = (this.id - 1L) * 100000L;
                DruidDataSource.connectionIdSeedUpdater.addAndGet(this, delta);
                DruidDataSource.statementIdSeedUpdater.addAndGet(this, delta);
                DruidDataSource.resultSetIdSeedUpdater.addAndGet(this, delta);
                DruidDataSource.transactionIdSeedUpdater.addAndGet(this, delta);
            }
            if (this.jdbcUrl != null) {
                this.jdbcUrl = this.jdbcUrl.trim();
                this.initFromWrapDriverUrl();
            }
            for (final Filter filter : this.filters) {
                filter.init(this);
            }
            if (this.dbTypeName == null || this.dbTypeName.length() == 0) {
                this.dbTypeName = JdbcUtils.getDbType(this.jdbcUrl, null);
            }
            final DbType dbType = DbType.of(this.dbTypeName);
            if (dbType == DbType.mysql || dbType == DbType.mariadb || dbType == DbType.oceanbase || dbType == DbType.ads) {
                boolean cacheServerConfigurationSet = false;
                if (this.connectProperties.containsKey("cacheServerConfiguration")) {
                    cacheServerConfigurationSet = true;
                }
                else if (this.jdbcUrl.indexOf("cacheServerConfiguration") != -1) {
                    cacheServerConfigurationSet = true;
                }
                if (cacheServerConfigurationSet) {
                    this.connectProperties.put("cacheServerConfiguration", "true");
                }
            }
            if (this.maxActive <= 0) {
                throw new IllegalArgumentException("illegal maxActive " + this.maxActive);
            }
            if (this.maxActive < this.minIdle) {
                throw new IllegalArgumentException("illegal maxActive " + this.maxActive);
            }
            if (this.getInitialSize() > this.maxActive) {
                throw new IllegalArgumentException("illegal initialSize " + this.initialSize + ", maxActive " + this.maxActive);
            }
            if (this.timeBetweenLogStatsMillis > 0L && this.useGlobalDataSourceStat) {
                throw new IllegalArgumentException("timeBetweenLogStatsMillis not support useGlobalDataSourceStat=true");
            }
            if (this.maxEvictableIdleTimeMillis < this.minEvictableIdleTimeMillis) {
                throw new SQLException("maxEvictableIdleTimeMillis must be grater than minEvictableIdleTimeMillis");
            }
            if (this.keepAlive && this.keepAliveBetweenTimeMillis <= this.timeBetweenEvictionRunsMillis) {
                throw new SQLException("keepAliveBetweenTimeMillis must be grater than timeBetweenEvictionRunsMillis");
            }
            if (this.driverClass != null) {
                this.driverClass = this.driverClass.trim();
            }
            this.initFromSPIServiceLoader();
            this.resolveDriver();
            this.initCheck();
            this.initExceptionSorter();
            this.initValidConnectionChecker();
            this.validationQueryCheck();
            if (this.isUseGlobalDataSourceStat()) {
                this.dataSourceStat = JdbcDataSourceStat.getGlobal();
                if (this.dataSourceStat == null) {
                    JdbcDataSourceStat.setGlobal(this.dataSourceStat = new JdbcDataSourceStat("Global", "Global", this.dbTypeName));
                }
                if (this.dataSourceStat.getDbType() == null) {
                    this.dataSourceStat.setDbType(this.dbTypeName);
                }
            }
            else {
                this.dataSourceStat = new JdbcDataSourceStat(this.name, this.jdbcUrl, this.dbTypeName, this.connectProperties);
            }
            this.dataSourceStat.setResetStatEnable(this.resetStatEnable);
            this.connections = new DruidConnectionHolder[this.maxActive];
            this.evictConnections = new DruidConnectionHolder[this.maxActive];
            this.keepAliveConnections = new DruidConnectionHolder[this.maxActive];
            SQLException connectError = null;
            if (this.createScheduler != null && this.asyncInit) {
                for (int i = 0; i < this.initialSize; ++i) {
                    this.submitCreateTask(true);
                }
            }
            else if (!this.asyncInit) {
                while (this.poolingCount < this.initialSize) {
                    try {
                        final PhysicalConnectionInfo pyConnectInfo = this.createPhysicalConnection();
                        final DruidConnectionHolder holder = new DruidConnectionHolder(this, pyConnectInfo);
                        this.connections[this.poolingCount++] = holder;
                    }
                    catch (SQLException ex) {
                        DruidDataSource.LOG.error("init datasource error, url: " + this.getUrl(), ex);
                        if (this.initExceptionThrow) {
                            connectError = ex;
                            break;
                        }
                        Thread.sleep(3000L);
                    }
                }
                if (this.poolingCount > 0) {
                    this.poolingPeak = this.poolingCount;
                    this.poolingPeakTime = System.currentTimeMillis();
                }
            }
            this.createAndLogThread();
            this.createAndStartCreatorThread();
            this.createAndStartDestroyThread();
            this.initedLatch.await();
            init = true;
            this.initedTime = new Date();
            this.registerMbean();
            if (connectError != null && this.poolingCount == 0) {
                throw connectError;
            }
            if (this.keepAlive) {
                if (this.createScheduler != null) {
                    for (int i = 0; i < this.minIdle; ++i) {
                        this.submitCreateTask(true);
                    }
                }
                else {
                    this.emptySignal();
                }
            }
        }
        catch (SQLException e2) {
            DruidDataSource.LOG.error("{dataSource-" + this.getID() + "} init error", e2);
            throw e2;
        }
        catch (InterruptedException e3) {
            throw new SQLException(e3.getMessage(), e3);
        }
        catch (RuntimeException e4) {
            DruidDataSource.LOG.error("{dataSource-" + this.getID() + "} init error", e4);
            throw e4;
        }
        catch (Error e5) {
            DruidDataSource.LOG.error("{dataSource-" + this.getID() + "} init error", e5);
            throw e5;
        }
        finally {
            this.inited = true;
            lock.unlock();
            if (init && DruidDataSource.LOG.isInfoEnabled()) {
                String msg = "{dataSource-" + this.getID();
                if (this.name != null && !this.name.isEmpty()) {
                    msg += ",";
                    msg += this.name;
                }
                msg += "} inited";
                DruidDataSource.LOG.info(msg);
            }
        }
    }
    
    private void submitCreateTask(final boolean initTask) {
        ++this.createTaskCount;
        final CreateConnectionTask task = new CreateConnectionTask(initTask);
        if (this.createTasks == null) {
            this.createTasks = new long[8];
        }
        boolean putted = false;
        for (int i = 0; i < this.createTasks.length; ++i) {
            if (this.createTasks[i] == 0L) {
                this.createTasks[i] = task.taskId;
                putted = true;
                break;
            }
        }
        if (!putted) {
            final long[] array = new long[this.createTasks.length * 3 / 2];
            System.arraycopy(this.createTasks, 0, array, 0, this.createTasks.length);
            array[this.createTasks.length] = task.taskId;
            this.createTasks = array;
        }
        this.createSchedulerFuture = this.createScheduler.submit(task);
    }
    
    private boolean clearCreateTask(final long taskId) {
        if (this.createTasks == null) {
            return false;
        }
        if (taskId == 0L) {
            return false;
        }
        for (int i = 0; i < this.createTasks.length; ++i) {
            if (this.createTasks[i] == taskId) {
                this.createTasks[i] = 0L;
                --this.createTaskCount;
                if (this.createTaskCount < 0) {
                    this.createTaskCount = 0;
                }
                if (this.createTaskCount == 0 && this.createTasks.length > 8) {
                    this.createTasks = new long[8];
                }
                return true;
            }
        }
        if (DruidDataSource.LOG.isWarnEnabled()) {
            DruidDataSource.LOG.warn("clear create task failed : " + taskId);
        }
        return false;
    }
    
    private void createAndLogThread() {
        if (this.timeBetweenLogStatsMillis <= 0L) {
            return;
        }
        final String threadName = "Druid-ConnectionPool-Log-" + System.identityHashCode(this);
        (this.logStatsThread = new LogStatsThread(threadName)).start();
        this.resetStatEnable = false;
    }
    
    protected void createAndStartDestroyThread() {
        this.destroyTask = new DestroyTask();
        if (this.destroyScheduler != null) {
            long period = this.timeBetweenEvictionRunsMillis;
            if (period <= 0L) {
                period = 1000L;
            }
            this.destroySchedulerFuture = this.destroyScheduler.scheduleAtFixedRate(this.destroyTask, period, period, TimeUnit.MILLISECONDS);
            this.initedLatch.countDown();
            return;
        }
        final String threadName = "Druid-ConnectionPool-Destroy-" + System.identityHashCode(this);
        (this.destroyConnectionThread = new DestroyConnectionThread(threadName)).start();
    }
    
    protected void createAndStartCreatorThread() {
        if (this.createScheduler == null) {
            final String threadName = "Druid-ConnectionPool-Create-" + System.identityHashCode(this);
            (this.createConnectionThread = new CreateConnectionThread(threadName)).start();
            return;
        }
        this.initedLatch.countDown();
    }
    
    private void initFromSPIServiceLoader() {
        if (this.loadSpifilterSkip) {
            return;
        }
        if (DruidDataSource.autoFilters == null) {
            final List<Filter> filters = new ArrayList<Filter>();
            final ServiceLoader<Filter> autoFilterLoader = ServiceLoader.load(Filter.class);
            for (final Filter filter : autoFilterLoader) {
                final AutoLoad autoLoad = filter.getClass().getAnnotation(AutoLoad.class);
                if (autoLoad != null && autoLoad.value()) {
                    filters.add(filter);
                }
            }
            DruidDataSource.autoFilters = filters;
        }
        for (final Filter filter2 : DruidDataSource.autoFilters) {
            if (DruidDataSource.LOG.isInfoEnabled()) {
                DruidDataSource.LOG.info("load filter from spi :" + filter2.getClass().getName());
            }
            this.addFilter(filter2);
        }
    }
    
    private void initFromWrapDriverUrl() throws SQLException {
        if (!this.jdbcUrl.startsWith("jdbc:wrap-jdbc:")) {
            return;
        }
        final DataSourceProxyConfig config = DruidDriver.parseConfig(this.jdbcUrl, null);
        this.driverClass = config.getRawDriverClassName();
        DruidDataSource.LOG.error("error url : '" + this.jdbcUrl + "', it should be : '" + config.getRawUrl() + "'");
        this.jdbcUrl = config.getRawUrl();
        if (this.name == null) {
            this.name = config.getName();
        }
        for (final Filter filter : config.getFilters()) {
            this.addFilter(filter);
        }
    }
    
    private void addFilter(final Filter filter) {
        boolean exists = false;
        for (final Filter initedFilter : this.filters) {
            if (initedFilter.getClass() == filter.getClass()) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            filter.init(this);
            this.filters.add(filter);
        }
    }
    
    private void validationQueryCheck() {
        if (!this.testOnBorrow && !this.testOnReturn && !this.testWhileIdle) {
            return;
        }
        if (this.validConnectionChecker != null) {
            return;
        }
        if (this.validationQuery != null && this.validationQuery.length() > 0) {
            return;
        }
        String errorMessage = "";
        if (this.testOnBorrow) {
            errorMessage += "testOnBorrow is true, ";
        }
        if (this.testOnReturn) {
            errorMessage += "testOnReturn is true, ";
        }
        if (this.testWhileIdle) {
            errorMessage += "testWhileIdle is true, ";
        }
        DruidDataSource.LOG.error(errorMessage + "validationQuery not set");
    }
    
    protected void resolveDriver() throws SQLException {
        if (this.driver == null) {
            if (this.driverClass == null || this.driverClass.isEmpty()) {
                this.driverClass = JdbcUtils.getDriverClassName(this.jdbcUrl);
            }
            if (MockDriver.class.getName().equals(this.driverClass)) {
                this.driver = MockDriver.instance;
            }
            else if ("com.alibaba.druid.support.clickhouse.BalancedClickhouseDriver".equals(this.driverClass)) {
                final Properties info = new Properties();
                info.put("user", this.username);
                info.put("password", this.password);
                info.putAll(this.connectProperties);
                this.driver = new BalancedClickhouseDriver(this.jdbcUrl, info);
            }
            else {
                if (this.jdbcUrl == null && (this.driverClass == null || this.driverClass.length() == 0)) {
                    throw new SQLException("url not set");
                }
                this.driver = JdbcUtils.createDriver(this.driverClassLoader, this.driverClass);
            }
        }
        else if (this.driverClass == null) {
            this.driverClass = this.driver.getClass().getName();
        }
    }
    
    protected void initCheck() throws SQLException {
        final DbType dbType = DbType.of(this.dbTypeName);
        if (dbType == DbType.oracle) {
            this.isOracle = true;
            if (this.driver.getMajorVersion() < 10) {
                throw new SQLException("not support oracle driver " + this.driver.getMajorVersion() + "." + this.driver.getMinorVersion());
            }
            if (this.driver.getMajorVersion() == 10 && this.isUseOracleImplicitCache()) {
                this.getConnectProperties().setProperty("oracle.jdbc.FreeMemoryOnEnterImplicitCache", "true");
            }
            this.oracleValidationQueryCheck();
        }
        else if (dbType == DbType.db2) {
            this.db2ValidationQueryCheck();
        }
        else if (dbType == DbType.mysql || "com.mysql.cj.jdbc.Driver".equals(this.driverClass)) {
            this.isMySql = true;
        }
        if (this.removeAbandoned) {
            DruidDataSource.LOG.warn("removeAbandoned is true, not use in production.");
        }
    }
    
    private void oracleValidationQueryCheck() {
        if (this.validationQuery == null) {
            return;
        }
        if (this.validationQuery.length() == 0) {
            return;
        }
        final SQLStatementParser sqlStmtParser = SQLParserUtils.createSQLStatementParser(this.validationQuery, this.dbTypeName, new SQLParserFeature[0]);
        final List<SQLStatement> stmtList = sqlStmtParser.parseStatementList();
        if (stmtList.size() != 1) {
            return;
        }
        final SQLStatement stmt = stmtList.get(0);
        if (!(stmt instanceof SQLSelectStatement)) {
            return;
        }
        final SQLSelectQuery query = ((SQLSelectStatement)stmt).getSelect().getQuery();
        if (query instanceof SQLSelectQueryBlock && ((SQLSelectQueryBlock)query).getFrom() == null) {
            DruidDataSource.LOG.error("invalid oracle validationQuery. " + this.validationQuery + ", may should be : " + this.validationQuery + " FROM DUAL");
        }
    }
    
    private void db2ValidationQueryCheck() {
        if (this.validationQuery == null) {
            return;
        }
        if (this.validationQuery.length() == 0) {
            return;
        }
        final SQLStatementParser sqlStmtParser = SQLParserUtils.createSQLStatementParser(this.validationQuery, this.dbTypeName, new SQLParserFeature[0]);
        final List<SQLStatement> stmtList = sqlStmtParser.parseStatementList();
        if (stmtList.size() != 1) {
            return;
        }
        final SQLStatement stmt = stmtList.get(0);
        if (!(stmt instanceof SQLSelectStatement)) {
            return;
        }
        final SQLSelectQuery query = ((SQLSelectStatement)stmt).getSelect().getQuery();
        if (query instanceof SQLSelectQueryBlock && ((SQLSelectQueryBlock)query).getFrom() == null) {
            DruidDataSource.LOG.error("invalid db2 validationQuery. " + this.validationQuery + ", may should be : " + this.validationQuery + " FROM SYSDUMMY");
        }
    }
    
    private void initValidConnectionChecker() {
        if (this.validConnectionChecker != null) {
            return;
        }
        final String realDriverClassName = this.driver.getClass().getName();
        if (JdbcUtils.isMySqlDriver(realDriverClassName)) {
            this.validConnectionChecker = new MySqlValidConnectionChecker();
        }
        else if (realDriverClassName.equals("oracle.jdbc.OracleDriver") || realDriverClassName.equals("oracle.jdbc.driver.OracleDriver")) {
            this.validConnectionChecker = new OracleValidConnectionChecker();
        }
        else if (realDriverClassName.equals("com.microsoft.jdbc.sqlserver.SQLServerDriver") || realDriverClassName.equals("com.microsoft.sqlserver.jdbc.SQLServerDriver") || realDriverClassName.equals("net.sourceforge.jtds.jdbc.Driver")) {
            this.validConnectionChecker = new MSSQLValidConnectionChecker();
        }
        else if (realDriverClassName.equals("org.postgresql.Driver") || realDriverClassName.equals("com.edb.Driver") || realDriverClassName.equals("com.aliyun.polardb.Driver")) {
            this.validConnectionChecker = new PGValidConnectionChecker();
        }
    }
    
    private void initExceptionSorter() {
        if (this.exceptionSorter instanceof NullExceptionSorter) {
            if (this.driver instanceof MockDriver) {
                return;
            }
        }
        else if (this.exceptionSorter != null) {
            return;
        }
        Class<?> driverClass = this.driver.getClass();
        while (true) {
            final String realDriverClassName = driverClass.getName();
            if (realDriverClassName.equals("com.mysql.jdbc.Driver") || realDriverClassName.equals("com.mysql.cj.jdbc.Driver")) {
                this.exceptionSorter = new MySqlExceptionSorter();
                this.isMySql = true;
                break;
            }
            if (realDriverClassName.equals("oracle.jdbc.OracleDriver") || realDriverClassName.equals("oracle.jdbc.driver.OracleDriver")) {
                this.exceptionSorter = new OracleExceptionSorter();
                break;
            }
            if (realDriverClassName.equals("com.alipay.oceanbase.jdbc.Driver")) {
                if (JdbcUtils.OCEANBASE_ORACLE.name().equalsIgnoreCase(this.dbTypeName)) {
                    this.exceptionSorter = new OceanBaseOracleExceptionSorter();
                    break;
                }
                this.exceptionSorter = new MySqlExceptionSorter();
                break;
            }
            else {
                if (realDriverClassName.equals("com.informix.jdbc.IfxDriver")) {
                    this.exceptionSorter = new InformixExceptionSorter();
                    break;
                }
                if (realDriverClassName.equals("com.sybase.jdbc2.jdbc.SybDriver")) {
                    this.exceptionSorter = new SybaseExceptionSorter();
                    break;
                }
                if (realDriverClassName.equals("org.postgresql.Driver") || realDriverClassName.equals("com.edb.Driver") || realDriverClassName.equals("com.aliyun.polardb.Driver")) {
                    this.exceptionSorter = new PGExceptionSorter();
                    break;
                }
                if (realDriverClassName.equals("com.alibaba.druid.mock.MockDriver")) {
                    this.exceptionSorter = new MockExceptionSorter();
                    break;
                }
                if (realDriverClassName.contains("DB2")) {
                    this.exceptionSorter = new DB2ExceptionSorter();
                    break;
                }
                final Class<?> superClass = driverClass.getSuperclass();
                if (superClass == null || superClass == Object.class) {
                    break;
                }
                driverClass = superClass;
            }
        }
    }
    
    @Override
    public DruidPooledConnection getConnection() throws SQLException {
        return this.getConnection(this.maxWait);
    }
    
    public DruidPooledConnection getConnection(final long maxWaitMillis) throws SQLException {
        this.init();
        if (this.filters.size() > 0) {
            final FilterChainImpl filterChain = new FilterChainImpl(this);
            return filterChain.dataSource_connect(this, maxWaitMillis);
        }
        return this.getConnectionDirect(maxWaitMillis);
    }
    
    @Override
    public PooledConnection getPooledConnection() throws SQLException {
        return this.getConnection(this.maxWait);
    }
    
    @Override
    public PooledConnection getPooledConnection(final String user, final String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by DruidDataSource");
    }
    
    public DruidPooledConnection getConnectionDirect(final long maxWaitMillis) throws SQLException {
        int notFullTimeoutRetryCnt = 0;
        DruidPooledConnection poolableConnection;
        while (true) {
            try {
                poolableConnection = this.getConnectionInternal(maxWaitMillis);
            }
            catch (GetConnectionTimeoutException ex) {
                if (notFullTimeoutRetryCnt <= this.notFullTimeoutRetryCount && !this.isFull()) {
                    ++notFullTimeoutRetryCnt;
                    if (!DruidDataSource.LOG.isWarnEnabled()) {
                        continue;
                    }
                    DruidDataSource.LOG.warn("get connection timeout retry : " + notFullTimeoutRetryCnt);
                    continue;
                }
                throw ex;
            }
            if (this.testOnBorrow) {
                final boolean validate = this.testConnectionInternal(poolableConnection.holder, poolableConnection.conn);
                if (validate) {
                    break;
                }
                if (DruidDataSource.LOG.isDebugEnabled()) {
                    DruidDataSource.LOG.debug("skip not validate connection.");
                }
                this.discardConnection(poolableConnection.holder);
            }
            else if (poolableConnection.conn.isClosed()) {
                this.discardConnection(poolableConnection.holder);
            }
            else {
                if (!this.testWhileIdle) {
                    break;
                }
                final DruidConnectionHolder holder = poolableConnection.holder;
                final long currentTimeMillis = System.currentTimeMillis();
                long lastActiveTimeMillis = holder.lastActiveTimeMillis;
                final long lastExecTimeMillis = holder.lastExecTimeMillis;
                final long lastKeepTimeMillis = holder.lastKeepTimeMillis;
                if (this.checkExecuteTime && lastExecTimeMillis != lastActiveTimeMillis) {
                    lastActiveTimeMillis = lastExecTimeMillis;
                }
                if (lastKeepTimeMillis > lastActiveTimeMillis) {
                    lastActiveTimeMillis = lastKeepTimeMillis;
                }
                final long idleMillis = currentTimeMillis - lastActiveTimeMillis;
                long timeBetweenEvictionRunsMillis = this.timeBetweenEvictionRunsMillis;
                if (timeBetweenEvictionRunsMillis <= 0L) {
                    timeBetweenEvictionRunsMillis = 60000L;
                }
                if (idleMillis < timeBetweenEvictionRunsMillis && idleMillis >= 0L) {
                    break;
                }
                final boolean validate2 = this.testConnectionInternal(poolableConnection.holder, poolableConnection.conn);
                if (validate2) {
                    break;
                }
                if (DruidDataSource.LOG.isDebugEnabled()) {
                    DruidDataSource.LOG.debug("skip not validate connection.");
                }
                this.discardConnection(poolableConnection.holder);
            }
        }
        if (this.removeAbandoned) {
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            poolableConnection.connectStackTrace = stackTrace;
            poolableConnection.setConnectedTimeNano();
            poolableConnection.traceEnable = true;
            this.activeConnectionLock.lock();
            try {
                this.activeConnections.put(poolableConnection, DruidDataSource.PRESENT);
            }
            finally {
                this.activeConnectionLock.unlock();
            }
        }
        if (!this.defaultAutoCommit) {
            poolableConnection.setAutoCommit(false);
        }
        return poolableConnection;
    }
    
    @Override
    @Deprecated
    public void discardConnection(final Connection realConnection) {
        JdbcUtils.close(realConnection);
        this.lock.lock();
        try {
            --this.activeCount;
            ++this.discardCount;
            if (this.activeCount <= this.minIdle) {
                this.emptySignal();
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public void discardConnection(final DruidConnectionHolder holder) {
        if (holder == null) {
            return;
        }
        final Connection conn = holder.getConnection();
        if (conn != null) {
            JdbcUtils.close(conn);
        }
        this.lock.lock();
        try {
            if (holder.discard) {
                return;
            }
            if (holder.active) {
                --this.activeCount;
                holder.active = false;
            }
            ++this.discardCount;
            holder.discard = true;
            if (this.activeCount <= this.minIdle) {
                this.emptySignal();
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private DruidPooledConnection getConnectionInternal(final long maxWait) throws SQLException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/alibaba/druid/pool/DruidDataSource.closed:Z
        //     4: ifeq            53
        //     7: getstatic       com/alibaba/druid/pool/DruidDataSource.connectErrorCountUpdater:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //    10: aload_0         /* this */
        //    11: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.incrementAndGet:(Ljava/lang/Object;)J
        //    14: pop2           
        //    15: new             Lcom/alibaba/druid/pool/DataSourceClosedException;
        //    18: dup            
        //    19: new             Ljava/lang/StringBuilder;
        //    22: dup            
        //    23: invokespecial   java/lang/StringBuilder.<init>:()V
        //    26: ldc_w           "dataSource already closed at "
        //    29: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    32: new             Ljava/util/Date;
        //    35: dup            
        //    36: aload_0         /* this */
        //    37: getfield        com/alibaba/druid/pool/DruidDataSource.closeTimeMillis:J
        //    40: invokespecial   java/util/Date.<init>:(J)V
        //    43: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    46: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    49: invokespecial   com/alibaba/druid/pool/DataSourceClosedException.<init>:(Ljava/lang/String;)V
        //    52: athrow         
        //    53: aload_0         /* this */
        //    54: getfield        com/alibaba/druid/pool/DruidDataSource.enable:Z
        //    57: ifne            88
        //    60: getstatic       com/alibaba/druid/pool/DruidDataSource.connectErrorCountUpdater:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //    63: aload_0         /* this */
        //    64: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.incrementAndGet:(Ljava/lang/Object;)J
        //    67: pop2           
        //    68: aload_0         /* this */
        //    69: getfield        com/alibaba/druid/pool/DruidDataSource.disableException:Lcom/alibaba/druid/pool/DataSourceDisableException;
        //    72: ifnull          80
        //    75: aload_0         /* this */
        //    76: getfield        com/alibaba/druid/pool/DruidDataSource.disableException:Lcom/alibaba/druid/pool/DataSourceDisableException;
        //    79: athrow         
        //    80: new             Lcom/alibaba/druid/pool/DataSourceDisableException;
        //    83: dup            
        //    84: invokespecial   com/alibaba/druid/pool/DataSourceDisableException.<init>:()V
        //    87: athrow         
        //    88: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //    91: lload_1         /* maxWait */
        //    92: invokevirtual   java/util/concurrent/TimeUnit.toNanos:(J)J
        //    95: lstore_3        /* nanos */
        //    96: aload_0         /* this */
        //    97: getfield        com/alibaba/druid/pool/DruidDataSource.maxWaitThreadCount:I
        //   100: istore          maxWaitThreadCount
        //   102: iconst_0       
        //   103: istore          createDirect
        //   105: iload           createDirect
        //   107: ifeq            307
        //   110: getstatic       com/alibaba/druid/pool/DruidDataSource.createStartNanosUpdater:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //   113: aload_0         /* this */
        //   114: invokestatic    java/lang/System.nanoTime:()J
        //   117: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.set:(Ljava/lang/Object;J)V
        //   120: getstatic       com/alibaba/druid/pool/DruidDataSource.creatingCountUpdater:Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
        //   123: aload_0         /* this */
        //   124: iconst_0       
        //   125: iconst_1       
        //   126: invokevirtual   java/util/concurrent/atomic/AtomicIntegerFieldUpdater.compareAndSet:(Ljava/lang/Object;II)Z
        //   129: ifeq            307
        //   132: aload_0         /* this */
        //   133: invokevirtual   com/alibaba/druid/pool/DruidDataSource.createPhysicalConnection:()Lcom/alibaba/druid/pool/DruidAbstractDataSource$PhysicalConnectionInfo;
        //   136: astore          pyConnInfo
        //   138: new             Lcom/alibaba/druid/pool/DruidConnectionHolder;
        //   141: dup            
        //   142: aload_0         /* this */
        //   143: aload           pyConnInfo
        //   145: invokespecial   com/alibaba/druid/pool/DruidConnectionHolder.<init>:(Lcom/alibaba/druid/pool/DruidAbstractDataSource;Lcom/alibaba/druid/pool/DruidAbstractDataSource$PhysicalConnectionInfo;)V
        //   148: astore          holder
        //   150: aload           holder
        //   152: invokestatic    java/lang/System.currentTimeMillis:()J
        //   155: putfield        com/alibaba/druid/pool/DruidConnectionHolder.lastActiveTimeMillis:J
        //   158: getstatic       com/alibaba/druid/pool/DruidDataSource.creatingCountUpdater:Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
        //   161: aload_0         /* this */
        //   162: invokevirtual   java/util/concurrent/atomic/AtomicIntegerFieldUpdater.decrementAndGet:(Ljava/lang/Object;)I
        //   165: pop            
        //   166: getstatic       com/alibaba/druid/pool/DruidDataSource.directCreateCountUpdater:Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
        //   169: aload_0         /* this */
        //   170: invokevirtual   java/util/concurrent/atomic/AtomicIntegerFieldUpdater.incrementAndGet:(Ljava/lang/Object;)I
        //   173: pop            
        //   174: getstatic       com/alibaba/druid/pool/DruidDataSource.LOG:Lcom/alibaba/druid/support/logging/Log;
        //   177: invokeinterface com/alibaba/druid/support/logging/Log.isDebugEnabled:()Z
        //   182: ifeq            196
        //   185: getstatic       com/alibaba/druid/pool/DruidDataSource.LOG:Lcom/alibaba/druid/support/logging/Log;
        //   188: ldc_w           "conn-direct_create "
        //   191: invokeinterface com/alibaba/druid/support/logging/Log.debug:(Ljava/lang/String;)V
        //   196: iconst_0       
        //   197: istore          discard
        //   199: aload_0         /* this */
        //   200: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   203: invokevirtual   java/util/concurrent/locks/ReentrantLock.lock:()V
        //   206: aload_0         /* this */
        //   207: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   210: aload_0         /* this */
        //   211: getfield        com/alibaba/druid/pool/DruidDataSource.maxActive:I
        //   214: if_icmpge       269
        //   217: aload_0         /* this */
        //   218: dup            
        //   219: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   222: iconst_1       
        //   223: iadd           
        //   224: putfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   227: aload           holder
        //   229: iconst_1       
        //   230: putfield        com/alibaba/druid/pool/DruidConnectionHolder.active:Z
        //   233: aload_0         /* this */
        //   234: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   237: aload_0         /* this */
        //   238: getfield        com/alibaba/druid/pool/DruidDataSource.activePeak:I
        //   241: if_icmple       259
        //   244: aload_0         /* this */
        //   245: aload_0         /* this */
        //   246: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   249: putfield        com/alibaba/druid/pool/DruidDataSource.activePeak:I
        //   252: aload_0         /* this */
        //   253: invokestatic    java/lang/System.currentTimeMillis:()J
        //   256: putfield        com/alibaba/druid/pool/DruidDataSource.activePeakTime:J
        //   259: aload_0         /* this */
        //   260: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   263: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   266: goto            795
        //   269: iconst_1       
        //   270: istore          discard
        //   272: aload_0         /* this */
        //   273: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   276: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   279: goto            294
        //   282: astore          10
        //   284: aload_0         /* this */
        //   285: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   288: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   291: aload           10
        //   293: athrow         
        //   294: iload           discard
        //   296: ifeq            307
        //   299: aload           pyConnInfo
        //   301: invokevirtual   com/alibaba/druid/pool/DruidAbstractDataSource$PhysicalConnectionInfo.getPhysicalConnection:()Ljava/sql/Connection;
        //   304: invokestatic    com/alibaba/druid/util/JdbcUtils.close:(Ljava/sql/Connection;)V
        //   307: aload_0         /* this */
        //   308: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   311: invokevirtual   java/util/concurrent/locks/ReentrantLock.lockInterruptibly:()V
        //   314: goto            340
        //   317: astore          e
        //   319: getstatic       com/alibaba/druid/pool/DruidDataSource.connectErrorCountUpdater:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //   322: aload_0         /* this */
        //   323: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.incrementAndGet:(Ljava/lang/Object;)J
        //   326: pop2           
        //   327: new             Ljava/sql/SQLException;
        //   330: dup            
        //   331: ldc_w           "interrupt"
        //   334: aload           e
        //   336: invokespecial   java/sql/SQLException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   339: athrow         
        //   340: iload           maxWaitThreadCount
        //   342: ifle            407
        //   345: aload_0         /* this */
        //   346: getfield        com/alibaba/druid/pool/DruidDataSource.notEmptyWaitThreadCount:I
        //   349: iload           maxWaitThreadCount
        //   351: if_icmplt       407
        //   354: getstatic       com/alibaba/druid/pool/DruidDataSource.connectErrorCountUpdater:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //   357: aload_0         /* this */
        //   358: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.incrementAndGet:(Ljava/lang/Object;)J
        //   361: pop2           
        //   362: new             Ljava/sql/SQLException;
        //   365: dup            
        //   366: new             Ljava/lang/StringBuilder;
        //   369: dup            
        //   370: invokespecial   java/lang/StringBuilder.<init>:()V
        //   373: ldc_w           "maxWaitThreadCount "
        //   376: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   379: iload           maxWaitThreadCount
        //   381: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   384: ldc_w           ", current wait Thread count "
        //   387: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   390: aload_0         /* this */
        //   391: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   394: invokevirtual   java/util/concurrent/locks/ReentrantLock.getQueueLength:()I
        //   397: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   400: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   403: invokespecial   java/sql/SQLException.<init>:(Ljava/lang/String;)V
        //   406: athrow         
        //   407: aload_0         /* this */
        //   408: getfield        com/alibaba/druid/pool/DruidDataSource.onFatalError:Z
        //   411: ifeq            555
        //   414: aload_0         /* this */
        //   415: getfield        com/alibaba/druid/pool/DruidDataSource.onFatalErrorMaxActive:I
        //   418: ifle            555
        //   421: aload_0         /* this */
        //   422: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   425: aload_0         /* this */
        //   426: getfield        com/alibaba/druid/pool/DruidDataSource.onFatalErrorMaxActive:I
        //   429: if_icmplt       555
        //   432: getstatic       com/alibaba/druid/pool/DruidDataSource.connectErrorCountUpdater:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //   435: aload_0         /* this */
        //   436: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.incrementAndGet:(Ljava/lang/Object;)J
        //   439: pop2           
        //   440: new             Ljava/lang/StringBuilder;
        //   443: dup            
        //   444: invokespecial   java/lang/StringBuilder.<init>:()V
        //   447: astore          errorMsg
        //   449: aload           errorMsg
        //   451: ldc_w           "onFatalError, activeCount "
        //   454: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   457: aload_0         /* this */
        //   458: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   461: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   464: ldc_w           ", onFatalErrorMaxActive "
        //   467: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   470: aload_0         /* this */
        //   471: getfield        com/alibaba/druid/pool/DruidDataSource.onFatalErrorMaxActive:I
        //   474: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   477: pop            
        //   478: aload_0         /* this */
        //   479: getfield        com/alibaba/druid/pool/DruidDataSource.lastFatalErrorTimeMillis:J
        //   482: lconst_0       
        //   483: lcmp           
        //   484: ifle            515
        //   487: aload           errorMsg
        //   489: ldc_w           ", time '"
        //   492: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   495: aload_0         /* this */
        //   496: getfield        com/alibaba/druid/pool/DruidDataSource.lastFatalErrorTimeMillis:J
        //   499: invokestatic    java/util/TimeZone.getDefault:()Ljava/util/TimeZone;
        //   502: invokestatic    com/alibaba/druid/util/StringUtils.formatDateTime19:(JLjava/util/TimeZone;)Ljava/lang/String;
        //   505: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   508: ldc_w           "'"
        //   511: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   514: pop            
        //   515: aload_0         /* this */
        //   516: getfield        com/alibaba/druid/pool/DruidDataSource.lastFatalErrorSql:Ljava/lang/String;
        //   519: ifnull          538
        //   522: aload           errorMsg
        //   524: ldc_w           ", sql \n"
        //   527: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   530: aload_0         /* this */
        //   531: getfield        com/alibaba/druid/pool/DruidDataSource.lastFatalErrorSql:Ljava/lang/String;
        //   534: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   537: pop            
        //   538: new             Ljava/sql/SQLException;
        //   541: dup            
        //   542: aload           errorMsg
        //   544: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   547: aload_0         /* this */
        //   548: getfield        com/alibaba/druid/pool/DruidDataSource.lastFatalError:Ljava/lang/Throwable;
        //   551: invokespecial   java/sql/SQLException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   554: athrow         
        //   555: aload_0         /* this */
        //   556: dup            
        //   557: getfield        com/alibaba/druid/pool/DruidDataSource.connectCount:J
        //   560: lconst_1       
        //   561: ladd           
        //   562: putfield        com/alibaba/druid/pool/DruidDataSource.connectCount:J
        //   565: aload_0         /* this */
        //   566: getfield        com/alibaba/druid/pool/DruidDataSource.createScheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   569: ifnull          645
        //   572: aload_0         /* this */
        //   573: getfield        com/alibaba/druid/pool/DruidDataSource.poolingCount:I
        //   576: ifne            645
        //   579: aload_0         /* this */
        //   580: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   583: aload_0         /* this */
        //   584: getfield        com/alibaba/druid/pool/DruidDataSource.maxActive:I
        //   587: if_icmpge       645
        //   590: getstatic       com/alibaba/druid/pool/DruidDataSource.creatingCountUpdater:Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;
        //   593: aload_0         /* this */
        //   594: invokevirtual   java/util/concurrent/atomic/AtomicIntegerFieldUpdater.get:(Ljava/lang/Object;)I
        //   597: ifne            645
        //   600: aload_0         /* this */
        //   601: getfield        com/alibaba/druid/pool/DruidDataSource.createScheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   604: instanceof      Ljava/util/concurrent/ScheduledThreadPoolExecutor;
        //   607: ifeq            645
        //   610: aload_0         /* this */
        //   611: getfield        com/alibaba/druid/pool/DruidDataSource.createScheduler:Ljava/util/concurrent/ScheduledExecutorService;
        //   614: checkcast       Ljava/util/concurrent/ScheduledThreadPoolExecutor;
        //   617: astore          executor
        //   619: aload           executor
        //   621: invokevirtual   java/util/concurrent/ScheduledThreadPoolExecutor.getQueue:()Ljava/util/concurrent/BlockingQueue;
        //   624: invokeinterface java/util/concurrent/BlockingQueue.size:()I
        //   629: ifle            645
        //   632: iconst_1       
        //   633: istore          createDirect
        //   635: aload_0         /* this */
        //   636: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   639: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   642: goto            105
        //   645: lload_1         /* maxWait */
        //   646: lconst_0       
        //   647: lcmp           
        //   648: ifle            661
        //   651: aload_0         /* this */
        //   652: lload_3         /* nanos */
        //   653: invokespecial   com/alibaba/druid/pool/DruidDataSource.pollLast:(J)Lcom/alibaba/druid/pool/DruidConnectionHolder;
        //   656: astore          holder
        //   658: goto            667
        //   661: aload_0         /* this */
        //   662: invokevirtual   com/alibaba/druid/pool/DruidDataSource.takeLast:()Lcom/alibaba/druid/pool/DruidConnectionHolder;
        //   665: astore          holder
        //   667: aload           holder
        //   669: ifnull          732
        //   672: aload           holder
        //   674: getfield        com/alibaba/druid/pool/DruidConnectionHolder.discard:Z
        //   677: ifeq            690
        //   680: aload_0         /* this */
        //   681: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   684: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   687: goto            105
        //   690: aload_0         /* this */
        //   691: dup            
        //   692: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   695: iconst_1       
        //   696: iadd           
        //   697: putfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   700: aload           holder
        //   702: iconst_1       
        //   703: putfield        com/alibaba/druid/pool/DruidConnectionHolder.active:Z
        //   706: aload_0         /* this */
        //   707: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   710: aload_0         /* this */
        //   711: getfield        com/alibaba/druid/pool/DruidDataSource.activePeak:I
        //   714: if_icmple       732
        //   717: aload_0         /* this */
        //   718: aload_0         /* this */
        //   719: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   722: putfield        com/alibaba/druid/pool/DruidDataSource.activePeak:I
        //   725: aload_0         /* this */
        //   726: invokestatic    java/lang/System.currentTimeMillis:()J
        //   729: putfield        com/alibaba/druid/pool/DruidDataSource.activePeakTime:J
        //   732: aload_0         /* this */
        //   733: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   736: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   739: goto            792
        //   742: astore          e
        //   744: getstatic       com/alibaba/druid/pool/DruidDataSource.connectErrorCountUpdater:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //   747: aload_0         /* this */
        //   748: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.incrementAndGet:(Ljava/lang/Object;)J
        //   751: pop2           
        //   752: new             Ljava/sql/SQLException;
        //   755: dup            
        //   756: aload           e
        //   758: invokevirtual   java/lang/InterruptedException.getMessage:()Ljava/lang/String;
        //   761: aload           e
        //   763: invokespecial   java/sql/SQLException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   766: athrow         
        //   767: astore          e
        //   769: getstatic       com/alibaba/druid/pool/DruidDataSource.connectErrorCountUpdater:Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;
        //   772: aload_0         /* this */
        //   773: invokevirtual   java/util/concurrent/atomic/AtomicLongFieldUpdater.incrementAndGet:(Ljava/lang/Object;)J
        //   776: pop2           
        //   777: aload           e
        //   779: athrow         
        //   780: astore          11
        //   782: aload_0         /* this */
        //   783: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   786: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   789: aload           11
        //   791: athrow         
        //   792: goto            795
        //   795: aload           holder
        //   797: ifnonnull       1154
        //   800: getstatic       com/alibaba/druid/pool/DruidDataSource.waitNanosLocal:Ljava/lang/ThreadLocal;
        //   803: invokevirtual   java/lang/ThreadLocal.get:()Ljava/lang/Object;
        //   806: checkcast       Ljava/lang/Long;
        //   809: invokevirtual   java/lang/Long.longValue:()J
        //   812: lstore          waitNanos
        //   814: aload_0         /* this */
        //   815: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   818: invokevirtual   java/util/concurrent/locks/ReentrantLock.lock:()V
        //   821: aload_0         /* this */
        //   822: getfield        com/alibaba/druid/pool/DruidDataSource.activeCount:I
        //   825: i2l            
        //   826: lstore          activeCount
        //   828: aload_0         /* this */
        //   829: getfield        com/alibaba/druid/pool/DruidDataSource.maxActive:I
        //   832: i2l            
        //   833: lstore          maxActive
        //   835: aload_0         /* this */
        //   836: getfield        com/alibaba/druid/pool/DruidDataSource.creatingCount:I
        //   839: i2l            
        //   840: lstore          creatingCount
        //   842: aload_0         /* this */
        //   843: getfield        com/alibaba/druid/pool/DruidDataSource.createStartNanos:J
        //   846: lstore          createStartNanos
        //   848: aload_0         /* this */
        //   849: getfield        com/alibaba/druid/pool/DruidDataSource.createErrorCount:I
        //   852: i2l            
        //   853: lstore          createErrorCount
        //   855: aload_0         /* this */
        //   856: getfield        com/alibaba/druid/pool/DruidDataSource.createError:Ljava/lang/Throwable;
        //   859: astore          createError
        //   861: aload_0         /* this */
        //   862: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   865: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   868: goto            883
        //   871: astore          20
        //   873: aload_0         /* this */
        //   874: getfield        com/alibaba/druid/pool/DruidDataSource.lock:Ljava/util/concurrent/locks/ReentrantLock;
        //   877: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
        //   880: aload           20
        //   882: athrow         
        //   883: new             Ljava/lang/StringBuilder;
        //   886: dup            
        //   887: sipush          128
        //   890: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //   893: astore          buf
        //   895: aload           buf
        //   897: ldc_w           "wait millis "
        //   900: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   903: lload           waitNanos
        //   905: ldc2_w          1000000
        //   908: ldiv           
        //   909: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   912: ldc_w           ", active "
        //   915: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   918: lload           activeCount
        //   920: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   923: ldc_w           ", maxActive "
        //   926: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   929: lload           maxActive
        //   931: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   934: ldc_w           ", creating "
        //   937: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   940: lload           creatingCount
        //   942: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   945: pop            
        //   946: lload           creatingCount
        //   948: lconst_0       
        //   949: lcmp           
        //   950: ifle            993
        //   953: lload           createStartNanos
        //   955: lconst_0       
        //   956: lcmp           
        //   957: ifle            993
        //   960: invokestatic    java/lang/System.nanoTime:()J
        //   963: lload           createStartNanos
        //   965: lsub           
        //   966: ldc2_w          1000000
        //   969: ldiv           
        //   970: lstore          createElapseMillis
        //   972: lload           createElapseMillis
        //   974: lconst_0       
        //   975: lcmp           
        //   976: ifle            993
        //   979: aload           buf
        //   981: ldc_w           ", createElapseMillis "
        //   984: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   987: lload           createElapseMillis
        //   989: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   992: pop            
        //   993: lload           createErrorCount
        //   995: lconst_0       
        //   996: lcmp           
        //   997: ifle            1014
        //  1000: aload           buf
        //  1002: ldc_w           ", createErrorCount "
        //  1005: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1008: lload           createErrorCount
        //  1010: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  1013: pop            
        //  1014: aload_0         /* this */
        //  1015: invokevirtual   com/alibaba/druid/pool/DruidDataSource.getDataSourceStat:()Lcom/alibaba/druid/stat/JdbcDataSourceStat;
        //  1018: invokevirtual   com/alibaba/druid/stat/JdbcDataSourceStat.getRuningSqlList:()Ljava/util/List;
        //  1021: astore          sqlList
        //  1023: iconst_0       
        //  1024: istore          i
        //  1026: iload           i
        //  1028: aload           sqlList
        //  1030: invokeinterface java/util/List.size:()I
        //  1035: if_icmpge       1120
        //  1038: iload           i
        //  1040: ifeq            1054
        //  1043: aload           buf
        //  1045: bipush          10
        //  1047: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //  1050: pop            
        //  1051: goto            1063
        //  1054: aload           buf
        //  1056: ldc_w           ", "
        //  1059: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1062: pop            
        //  1063: aload           sqlList
        //  1065: iload           i
        //  1067: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //  1072: checkcast       Lcom/alibaba/druid/stat/JdbcSqlStatValue;
        //  1075: astore          sql
        //  1077: aload           buf
        //  1079: ldc_w           "runningSqlCount "
        //  1082: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1085: aload           sql
        //  1087: invokevirtual   com/alibaba/druid/stat/JdbcSqlStatValue.getRunningCount:()I
        //  1090: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1093: pop            
        //  1094: aload           buf
        //  1096: ldc_w           " : "
        //  1099: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1102: pop            
        //  1103: aload           buf
        //  1105: aload           sql
        //  1107: invokevirtual   com/alibaba/druid/stat/JdbcSqlStatValue.getSql:()Ljava/lang/String;
        //  1110: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1113: pop            
        //  1114: iinc            i, 1
        //  1117: goto            1026
        //  1120: aload           buf
        //  1122: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1125: astore          errorMessage
        //  1127: aload           createError
        //  1129: ifnull          1144
        //  1132: new             Lcom/alibaba/druid/pool/GetConnectionTimeoutException;
        //  1135: dup            
        //  1136: aload           errorMessage
        //  1138: aload           createError
        //  1140: invokespecial   com/alibaba/druid/pool/GetConnectionTimeoutException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  1143: athrow         
        //  1144: new             Lcom/alibaba/druid/pool/GetConnectionTimeoutException;
        //  1147: dup            
        //  1148: aload           errorMessage
        //  1150: invokespecial   com/alibaba/druid/pool/GetConnectionTimeoutException.<init>:(Ljava/lang/String;)V
        //  1153: athrow         
        //  1154: aload           holder
        //  1156: invokevirtual   com/alibaba/druid/pool/DruidConnectionHolder.incrementUseCount:()V
        //  1159: new             Lcom/alibaba/druid/pool/DruidPooledConnection;
        //  1162: dup            
        //  1163: aload           holder
        //  1165: invokespecial   com/alibaba/druid/pool/DruidPooledConnection.<init>:(Lcom/alibaba/druid/pool/DruidConnectionHolder;)V
        //  1168: astore          poolalbeConnection
        //  1170: aload           poolalbeConnection
        //  1172: areturn        
        //    Exceptions:
        //  throws java.sql.SQLException
        //    StackMapTable: 00 24 35 1A 07 FF 00 10 00 06 07 04 CD 04 04 01 00 01 00 00 FF 00 5A 00 07 07 04 CD 04 04 01 07 04 ED 01 07 05 8C 00 00 FC 00 3E 01 09 4C 07 04 E1 0B FF 00 0C 00 06 07 04 CD 04 04 01 00 01 00 00 49 07 05 10 16 FB 00 42 FC 00 6B 07 05 8D 16 FA 00 10 FB 00 59 0F FF 00 05 00 06 07 04 CD 04 04 01 07 04 ED 01 00 00 16 29 FF 00 09 00 06 07 04 CD 04 04 01 00 01 00 01 07 05 10 58 07 04 D0 4C 07 04 E1 FF 00 0B 00 06 07 04 CD 04 04 01 07 04 ED 01 00 00 FA 00 02 FF 00 4B 00 06 07 04 CD 04 04 01 07 04 ED 04 00 01 07 04 E1 FF 00 0B 00 0C 07 04 CD 04 04 01 07 04 ED 04 04 04 04 04 04 07 04 E1 00 00 FC 00 6D 07 05 8D 14 FD 00 0B 07 05 2B 01 1B 08 FA 00 38 FC 00 17 07 04 CF FF 00 09 00 05 07 04 CD 04 04 01 07 04 ED 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  206    259    282    294    Any
        //  269    272    282    294    Any
        //  282    284    282    294    Any
        //  307    314    317    340    Ljava/lang/InterruptedException;
        //  340    635    742    767    Ljava/lang/InterruptedException;
        //  645    680    742    767    Ljava/lang/InterruptedException;
        //  690    732    742    767    Ljava/lang/InterruptedException;
        //  340    635    767    780    Ljava/sql/SQLException;
        //  645    680    767    780    Ljava/sql/SQLException;
        //  690    732    767    780    Ljava/sql/SQLException;
        //  340    635    780    792    Any
        //  645    680    780    792    Any
        //  690    732    780    792    Any
        //  742    782    780    792    Any
        //  814    861    871    883    Any
        //  871    873    871    883    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:458)
        //     at com.strobel.assembler.Collection.get(Collection.java:43)
        //     at java.base/java.util.Collections$UnmodifiableList.get(Collections.java:1308)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCallCore(AstMethodBodyBuilder.java:1313)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.adjustArgumentsForMethodCall(AstMethodBodyBuilder.java:1286)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1197)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:718)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:441)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void handleConnectionException(final DruidPooledConnection pooledConnection, final Throwable t, final String sql) throws SQLException {
        final DruidConnectionHolder holder = pooledConnection.getConnectionHolder();
        if (holder == null) {
            return;
        }
        DruidDataSource.errorCountUpdater.incrementAndGet(this);
        this.lastError = t;
        this.lastErrorTimeMillis = System.currentTimeMillis();
        if (t instanceof SQLException) {
            final SQLException sqlEx = (SQLException)t;
            final ConnectionEvent event = new ConnectionEvent(pooledConnection, sqlEx);
            for (final ConnectionEventListener eventListener : holder.getConnectionEventListeners()) {
                eventListener.connectionErrorOccurred(event);
            }
            if (this.exceptionSorter != null && this.exceptionSorter.isExceptionFatal(sqlEx)) {
                this.handleFatalError(pooledConnection, sqlEx, sql);
            }
            throw sqlEx;
        }
        throw new SQLException("Error", t);
    }
    
    protected final void handleFatalError(final DruidPooledConnection conn, final SQLException error, String sql) throws SQLException {
        final DruidConnectionHolder holder = conn.holder;
        if (conn.isTraceEnable()) {
            this.activeConnectionLock.lock();
            try {
                if (conn.isTraceEnable()) {
                    this.activeConnections.remove(conn);
                    conn.setTraceEnable(false);
                }
            }
            finally {
                this.activeConnectionLock.unlock();
            }
        }
        long lastErrorTimeMillis = this.lastErrorTimeMillis;
        if (lastErrorTimeMillis == 0L) {
            lastErrorTimeMillis = System.currentTimeMillis();
        }
        if (sql != null && sql.length() > 1024) {
            sql = sql.substring(0, 1024);
        }
        boolean requireDiscard = false;
        final ReentrantLock lock = conn.lock;
        lock.lock();
        try {
            if (!conn.isClosed() || !conn.isDisable()) {
                conn.disable(error);
                requireDiscard = true;
            }
            this.lastFatalErrorTimeMillis = lastErrorTimeMillis;
            ++this.fatalErrorCount;
            if (this.fatalErrorCount - this.fatalErrorCountLastShrink > this.onFatalErrorMaxActive) {
                this.onFatalError = true;
            }
            this.lastFatalError = error;
            this.lastFatalErrorSql = sql;
        }
        finally {
            lock.unlock();
        }
        if (this.onFatalError && holder != null && holder.getDataSource() != null) {
            final ReentrantLock dataSourceLock = holder.getDataSource().lock;
            dataSourceLock.lock();
            try {
                this.emptySignal();
            }
            finally {
                dataSourceLock.unlock();
            }
        }
        if (requireDiscard) {
            if (holder.statementTrace != null) {
                holder.lock.lock();
                try {
                    for (final Statement stmt : holder.statementTrace) {
                        JdbcUtils.close(stmt);
                    }
                }
                finally {
                    holder.lock.unlock();
                }
            }
            this.discardConnection(holder);
        }
        DruidDataSource.LOG.error("{conn-" + holder.getConnectionId() + "} discard", error);
    }
    
    @Override
    protected void recycle(final DruidPooledConnection pooledConnection) throws SQLException {
        final DruidConnectionHolder holder = pooledConnection.holder;
        if (holder == null) {
            DruidDataSource.LOG.warn("connectionHolder is null");
            return;
        }
        if (this.logDifferentThread && !this.isAsyncCloseConnectionEnable() && pooledConnection.ownerThread != Thread.currentThread()) {
            DruidDataSource.LOG.warn("get/close not same thread");
        }
        final Connection physicalConnection = holder.conn;
        if (pooledConnection.traceEnable) {
            Object oldInfo = null;
            this.activeConnectionLock.lock();
            try {
                if (pooledConnection.traceEnable) {
                    oldInfo = this.activeConnections.remove(pooledConnection);
                    pooledConnection.traceEnable = false;
                }
            }
            finally {
                this.activeConnectionLock.unlock();
            }
            if (oldInfo == null && DruidDataSource.LOG.isWarnEnabled()) {
                DruidDataSource.LOG.warn("remove abandonded failed. activeConnections.size " + this.activeConnections.size());
            }
        }
        final boolean isAutoCommit = holder.underlyingAutoCommit;
        final boolean isReadOnly = holder.underlyingReadOnly;
        final boolean testOnReturn = this.testOnReturn;
        try {
            if (!isAutoCommit && !isReadOnly) {
                pooledConnection.rollback();
            }
            final boolean isSameThread = pooledConnection.ownerThread == Thread.currentThread();
            if (!isSameThread) {
                final ReentrantLock lock = pooledConnection.lock;
                lock.lock();
                try {
                    holder.reset();
                }
                finally {
                    lock.unlock();
                }
            }
            else {
                holder.reset();
            }
            if (holder.discard) {
                return;
            }
            if (this.phyMaxUseCount > 0L && holder.useCount >= this.phyMaxUseCount) {
                this.discardConnection(holder);
                return;
            }
            if (physicalConnection.isClosed()) {
                this.lock.lock();
                try {
                    if (holder.active) {
                        --this.activeCount;
                        holder.active = false;
                    }
                    ++this.closeCount;
                }
                finally {
                    this.lock.unlock();
                }
                return;
            }
            if (testOnReturn) {
                final boolean validate = this.testConnectionInternal(holder, physicalConnection);
                if (!validate) {
                    JdbcUtils.close(physicalConnection);
                    DruidDataSource.destroyCountUpdater.incrementAndGet(this);
                    this.lock.lock();
                    try {
                        if (holder.active) {
                            --this.activeCount;
                            holder.active = false;
                        }
                        ++this.closeCount;
                    }
                    finally {
                        this.lock.unlock();
                    }
                    return;
                }
            }
            if (holder.initSchema != null) {
                holder.conn.setSchema(holder.initSchema);
                holder.initSchema = null;
            }
            if (!this.enable) {
                this.discardConnection(holder);
                return;
            }
            final long currentTimeMillis = System.currentTimeMillis();
            if (this.phyTimeoutMillis > 0L) {
                final long phyConnectTimeMillis = currentTimeMillis - holder.connectTimeMillis;
                if (phyConnectTimeMillis > this.phyTimeoutMillis) {
                    this.discardConnection(holder);
                    return;
                }
            }
            this.lock.lock();
            boolean result;
            try {
                if (holder.active) {
                    --this.activeCount;
                    holder.active = false;
                }
                ++this.closeCount;
                result = this.putLast(holder, currentTimeMillis);
                ++this.recycleCount;
            }
            finally {
                this.lock.unlock();
            }
            if (!result) {
                JdbcUtils.close(holder.conn);
                DruidDataSource.LOG.info("connection recyle failed.");
            }
        }
        catch (Throwable e) {
            holder.clearStatementCache();
            if (!holder.discard) {
                this.discardConnection(holder);
                holder.discard = true;
            }
            DruidDataSource.LOG.error("recyle error", e);
            DruidDataSource.recycleErrorCountUpdater.incrementAndGet(this);
        }
    }
    
    public long getRecycleErrorCount() {
        return this.recycleErrorCount;
    }
    
    @Override
    public void clearStatementCache() throws SQLException {
        this.lock.lock();
        try {
            for (int i = 0; i < this.poolingCount; ++i) {
                final DruidConnectionHolder conn = this.connections[i];
                if (conn.statementPool != null) {
                    conn.statementPool.clear();
                }
            }
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public void close() {
        if (DruidDataSource.LOG.isInfoEnabled()) {
            DruidDataSource.LOG.info("{dataSource-" + this.getID() + "} closing ...");
        }
        this.lock.lock();
        try {
            if (this.closed) {
                return;
            }
            if (!this.inited) {
                return;
            }
            this.closing = true;
            if (this.logStatsThread != null) {
                this.logStatsThread.interrupt();
            }
            if (this.createConnectionThread != null) {
                this.createConnectionThread.interrupt();
            }
            if (this.destroyConnectionThread != null) {
                this.destroyConnectionThread.interrupt();
            }
            if (this.createSchedulerFuture != null) {
                this.createSchedulerFuture.cancel(true);
            }
            if (this.destroySchedulerFuture != null) {
                this.destroySchedulerFuture.cancel(true);
            }
            for (int i = 0; i < this.poolingCount; ++i) {
                final DruidConnectionHolder connHolder = this.connections[i];
                for (final PreparedStatementHolder stmtHolder : connHolder.getStatementPool().getMap().values()) {
                    connHolder.getStatementPool().closeRemovedStatement(stmtHolder);
                }
                connHolder.getStatementPool().getMap().clear();
                final Connection physicalConnection = connHolder.getConnection();
                try {
                    physicalConnection.close();
                }
                catch (Exception ex) {
                    DruidDataSource.LOG.warn("close connection error", ex);
                }
                this.connections[i] = null;
                DruidDataSource.destroyCountUpdater.incrementAndGet(this);
            }
            this.poolingCount = 0;
            this.unregisterMbean();
            this.enable = false;
            this.notEmpty.signalAll();
            ++this.notEmptySignalCount;
            this.closed = true;
            this.closeTimeMillis = System.currentTimeMillis();
            this.disableException = new DataSourceDisableException();
            for (final Filter filter : this.filters) {
                filter.destroy();
            }
        }
        finally {
            this.closing = false;
            this.lock.unlock();
        }
        if (DruidDataSource.LOG.isInfoEnabled()) {
            DruidDataSource.LOG.info("{dataSource-" + this.getID() + "} closed");
        }
    }
    
    public void registerMbean() {
        if (!this.mbeanRegistered) {
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    final ObjectName objectName = DruidDataSourceStatManager.addDataSource(DruidDataSource.this, DruidDataSource.this.name);
                    DruidDataSource.this.setObjectName(objectName);
                    DruidDataSource.this.mbeanRegistered = true;
                    return null;
                }
            });
        }
    }
    
    public void unregisterMbean() {
        if (this.mbeanRegistered) {
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                    DruidDataSourceStatManager.removeDataSource(DruidDataSource.this);
                    DruidDataSource.this.mbeanRegistered = false;
                    return null;
                }
            });
        }
    }
    
    public boolean isMbeanRegistered() {
        return this.mbeanRegistered;
    }
    
    boolean putLast(final DruidConnectionHolder e, final long lastActiveTimeMillis) {
        if (this.poolingCount >= this.maxActive || e.discard || this.closed) {
            return false;
        }
        e.lastActiveTimeMillis = lastActiveTimeMillis;
        this.connections[this.poolingCount] = e;
        this.incrementPoolingCount();
        if (this.poolingCount > this.poolingPeak) {
            this.poolingPeak = this.poolingCount;
            this.poolingPeakTime = lastActiveTimeMillis;
        }
        this.notEmpty.signal();
        ++this.notEmptySignalCount;
        return true;
    }
    
    DruidConnectionHolder takeLast() throws InterruptedException, SQLException {
        try {
            while (this.poolingCount == 0) {
                this.emptySignal();
                if (this.failFast && this.isFailContinuous()) {
                    throw new DataSourceNotAvailableException(this.createError);
                }
                ++this.notEmptyWaitThreadCount;
                if (this.notEmptyWaitThreadCount > this.notEmptyWaitThreadPeak) {
                    this.notEmptyWaitThreadPeak = this.notEmptyWaitThreadCount;
                }
                try {
                    this.notEmpty.await();
                }
                finally {
                    --this.notEmptyWaitThreadCount;
                }
                ++this.notEmptyWaitCount;
                if (this.enable) {
                    continue;
                }
                DruidDataSource.connectErrorCountUpdater.incrementAndGet(this);
                if (this.disableException != null) {
                    throw this.disableException;
                }
                throw new DataSourceDisableException();
            }
        }
        catch (InterruptedException ie) {
            this.notEmpty.signal();
            ++this.notEmptySignalCount;
            throw ie;
        }
        this.decrementPoolingCount();
        final DruidConnectionHolder last = this.connections[this.poolingCount];
        this.connections[this.poolingCount] = null;
        return last;
    }
    
    private DruidConnectionHolder pollLast(final long nanos) throws InterruptedException, SQLException {
        long estimate = nanos;
        while (this.poolingCount == 0) {
            this.emptySignal();
            if (this.failFast && this.isFailContinuous()) {
                throw new DataSourceNotAvailableException(this.createError);
            }
            if (estimate <= 0L) {
                DruidDataSource.waitNanosLocal.set(nanos - estimate);
                return null;
            }
            ++this.notEmptyWaitThreadCount;
            if (this.notEmptyWaitThreadCount > this.notEmptyWaitThreadPeak) {
                this.notEmptyWaitThreadPeak = this.notEmptyWaitThreadCount;
            }
            try {
                final long startEstimate = estimate;
                estimate = this.notEmpty.awaitNanos(estimate);
                ++this.notEmptyWaitCount;
                this.notEmptyWaitNanos += startEstimate - estimate;
                if (!this.enable) {
                    DruidDataSource.connectErrorCountUpdater.incrementAndGet(this);
                    if (this.disableException != null) {
                        throw this.disableException;
                    }
                    throw new DataSourceDisableException();
                }
            }
            catch (InterruptedException ie) {
                this.notEmpty.signal();
                ++this.notEmptySignalCount;
                throw ie;
            }
            finally {
                --this.notEmptyWaitThreadCount;
            }
            if (this.poolingCount != 0) {
                break;
            }
            if (estimate > 0L) {
                continue;
            }
            DruidDataSource.waitNanosLocal.set(nanos - estimate);
            return null;
        }
        this.decrementPoolingCount();
        final DruidConnectionHolder last = this.connections[this.poolingCount];
        this.connections[this.poolingCount] = null;
        final long waitNanos = nanos - estimate;
        last.setLastNotEmptyWaitNanos(waitNanos);
        return last;
    }
    
    private final void decrementPoolingCount() {
        --this.poolingCount;
    }
    
    private final void incrementPoolingCount() {
        ++this.poolingCount;
    }
    
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        if (this.username == null && this.password == null && username != null && password != null) {
            this.username = username;
            this.password = password;
            return this.getConnection();
        }
        if (!StringUtils.equals(username, this.username)) {
            throw new UnsupportedOperationException("Not supported by DruidDataSource");
        }
        if (!StringUtils.equals(password, this.password)) {
            throw new UnsupportedOperationException("Not supported by DruidDataSource");
        }
        return this.getConnection();
    }
    
    @Override
    public long getCreateCount() {
        return this.createCount;
    }
    
    @Override
    public long getDestroyCount() {
        return this.destroyCount;
    }
    
    @Override
    public long getConnectCount() {
        this.lock.lock();
        try {
            return this.connectCount;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public long getCloseCount() {
        return this.closeCount;
    }
    
    @Override
    public long getConnectErrorCount() {
        return DruidDataSource.connectErrorCountUpdater.get(this);
    }
    
    @Override
    public int getPoolingCount() {
        this.lock.lock();
        try {
            return this.poolingCount;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public int getPoolingPeak() {
        this.lock.lock();
        try {
            return this.poolingPeak;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public Date getPoolingPeakTime() {
        if (this.poolingPeakTime <= 0L) {
            return null;
        }
        return new Date(this.poolingPeakTime);
    }
    
    @Override
    public long getRecycleCount() {
        return this.recycleCount;
    }
    
    @Override
    public int getActiveCount() {
        this.lock.lock();
        try {
            return this.activeCount;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void logStats() {
        final DruidDataSourceStatLogger statLogger = this.statLogger;
        if (statLogger == null) {
            return;
        }
        final DruidDataSourceStatValue statValue = this.getStatValueAndReset();
        statLogger.log(statValue);
    }
    
    public DruidDataSourceStatValue getStatValueAndReset() {
        final DruidDataSourceStatValue value = new DruidDataSourceStatValue();
        this.lock.lock();
        try {
            value.setPoolingCount(this.poolingCount);
            value.setPoolingPeak(this.poolingPeak);
            value.setPoolingPeakTime(this.poolingPeakTime);
            value.setActiveCount(this.activeCount);
            value.setActivePeak(this.activePeak);
            value.setActivePeakTime(this.activePeakTime);
            value.setConnectCount(this.connectCount);
            value.setCloseCount(this.closeCount);
            value.setWaitThreadCount(this.lock.getWaitQueueLength(this.notEmpty));
            value.setNotEmptyWaitCount(this.notEmptyWaitCount);
            value.setNotEmptyWaitNanos(this.notEmptyWaitNanos);
            value.setKeepAliveCheckCount(this.keepAliveCheckCount);
            this.poolingPeak = 0;
            this.poolingPeakTime = 0L;
            this.activePeak = 0;
            this.activePeakTime = 0L;
            this.connectCount = 0L;
            this.closeCount = 0L;
            this.keepAliveCheckCount = 0;
            this.notEmptyWaitCount = 0L;
            this.notEmptyWaitNanos = 0L;
        }
        finally {
            this.lock.unlock();
        }
        value.setName(this.getName());
        value.setDbType(this.dbTypeName);
        value.setDriverClassName(this.getDriverClassName());
        value.setUrl(this.getUrl());
        value.setUserName(this.getUsername());
        value.setFilterClassNames(this.getFilterClassNames());
        value.setInitialSize(this.getInitialSize());
        value.setMinIdle(this.getMinIdle());
        value.setMaxActive(this.getMaxActive());
        value.setQueryTimeout(this.getQueryTimeout());
        value.setTransactionQueryTimeout(this.getTransactionQueryTimeout());
        value.setLoginTimeout(this.getLoginTimeout());
        value.setValidConnectionCheckerClassName(this.getValidConnectionCheckerClassName());
        value.setExceptionSorterClassName(this.getExceptionSorterClassName());
        value.setTestOnBorrow(this.testOnBorrow);
        value.setTestOnReturn(this.testOnReturn);
        value.setTestWhileIdle(this.testWhileIdle);
        value.setDefaultAutoCommit(this.isDefaultAutoCommit());
        if (this.defaultReadOnly != null) {
            value.setDefaultReadOnly(this.defaultReadOnly);
        }
        value.setDefaultTransactionIsolation(this.getDefaultTransactionIsolation());
        value.setLogicConnectErrorCount(DruidDataSource.connectErrorCountUpdater.getAndSet(this, 0L));
        value.setPhysicalConnectCount(DruidDataSource.createCountUpdater.getAndSet(this, 0L));
        value.setPhysicalCloseCount(DruidDataSource.destroyCountUpdater.getAndSet(this, 0L));
        value.setPhysicalConnectErrorCount(DruidDataSource.createErrorCountUpdater.getAndSet(this, 0));
        value.setExecuteCount(this.getAndResetExecuteCount());
        value.setErrorCount(DruidDataSource.errorCountUpdater.getAndSet(this, 0L));
        value.setCommitCount(DruidDataSource.commitCountUpdater.getAndSet(this, 0L));
        value.setRollbackCount(DruidDataSource.rollbackCountUpdater.getAndSet(this, 0L));
        value.setPstmtCacheHitCount(DruidDataSource.cachedPreparedStatementHitCountUpdater.getAndSet(this, 0L));
        value.setPstmtCacheMissCount(DruidDataSource.cachedPreparedStatementMissCountUpdater.getAndSet(this, 0L));
        value.setStartTransactionCount(DruidDataSource.startTransactionCountUpdater.getAndSet(this, 0L));
        value.setTransactionHistogram(this.getTransactionHistogram().toArrayAndReset());
        value.setConnectionHoldTimeHistogram(this.getDataSourceStat().getConnectionHoldHistogram().toArrayAndReset());
        value.setRemoveAbandoned(this.isRemoveAbandoned());
        value.setClobOpenCount(this.getDataSourceStat().getClobOpenCountAndReset());
        value.setBlobOpenCount(this.getDataSourceStat().getBlobOpenCountAndReset());
        value.setSqlSkipCount(this.getDataSourceStat().getSkipSqlCountAndReset());
        value.setSqlList(this.getDataSourceStat().getSqlStatMapAndReset());
        return value;
    }
    
    @Override
    public long getRemoveAbandonedCount() {
        return this.removeAbandonedCount;
    }
    
    protected boolean put(final PhysicalConnectionInfo physicalConnectionInfo) {
        DruidConnectionHolder holder = null;
        try {
            holder = new DruidConnectionHolder(this, physicalConnectionInfo);
        }
        catch (SQLException ex) {
            this.lock.lock();
            try {
                if (this.createScheduler != null) {
                    this.clearCreateTask(physicalConnectionInfo.createTaskId);
                }
            }
            finally {
                this.lock.unlock();
            }
            DruidDataSource.LOG.error("create connection holder error", ex);
            return false;
        }
        return this.put(holder, physicalConnectionInfo.createTaskId, false);
    }
    
    private boolean put(final DruidConnectionHolder holder, final long createTaskId, final boolean checkExists) {
        this.lock.lock();
        try {
            if (this.closing || this.closed) {
                return false;
            }
            if (this.poolingCount >= this.maxActive) {
                if (this.createScheduler != null) {
                    this.clearCreateTask(createTaskId);
                }
                return false;
            }
            if (checkExists) {
                for (int i = 0; i < this.poolingCount; ++i) {
                    if (this.connections[i] == holder) {
                        return false;
                    }
                }
            }
            this.connections[this.poolingCount] = holder;
            this.incrementPoolingCount();
            if (this.poolingCount > this.poolingPeak) {
                this.poolingPeak = this.poolingCount;
                this.poolingPeakTime = System.currentTimeMillis();
            }
            this.notEmpty.signal();
            ++this.notEmptySignalCount;
            if (this.createScheduler != null) {
                this.clearCreateTask(createTaskId);
                if (this.poolingCount + this.createTaskCount < this.notEmptyWaitThreadCount && this.activeCount + this.poolingCount + this.createTaskCount < this.maxActive) {
                    this.emptySignal();
                }
            }
        }
        finally {
            this.lock.unlock();
        }
        return true;
    }
    
    @Override
    public int removeAbandoned() {
        int removeCount = 0;
        final long currrentNanos = System.nanoTime();
        final List<DruidPooledConnection> abandonedList = new ArrayList<DruidPooledConnection>();
        this.activeConnectionLock.lock();
        try {
            final Iterator<DruidPooledConnection> iter = this.activeConnections.keySet().iterator();
            while (iter.hasNext()) {
                final DruidPooledConnection pooledConnection = iter.next();
                if (pooledConnection.isRunning()) {
                    continue;
                }
                final long timeMillis = (currrentNanos - pooledConnection.getConnectedTimeNano()) / 1000000L;
                if (timeMillis < this.removeAbandonedTimeoutMillis) {
                    continue;
                }
                iter.remove();
                pooledConnection.setTraceEnable(false);
                abandonedList.add(pooledConnection);
            }
        }
        finally {
            this.activeConnectionLock.unlock();
        }
        if (abandonedList.size() > 0) {
            for (final DruidPooledConnection pooledConnection : abandonedList) {
                final ReentrantLock lock = pooledConnection.lock;
                lock.lock();
                try {
                    if (pooledConnection.isDisable()) {
                        continue;
                    }
                }
                finally {
                    lock.unlock();
                }
                JdbcUtils.close(pooledConnection);
                pooledConnection.abandond();
                ++this.removeAbandonedCount;
                ++removeCount;
                if (this.isLogAbandoned()) {
                    final StringBuilder buf = new StringBuilder();
                    buf.append("abandon connection, owner thread: ");
                    buf.append(pooledConnection.getOwnerThread().getName());
                    buf.append(", connected at : ");
                    buf.append(pooledConnection.getConnectedTimeMillis());
                    buf.append(", open stackTrace\n");
                    StackTraceElement[] trace = pooledConnection.getConnectStackTrace();
                    for (int i = 0; i < trace.length; ++i) {
                        buf.append("\tat ");
                        buf.append(trace[i].toString());
                        buf.append("\n");
                    }
                    buf.append("ownerThread current state is " + pooledConnection.getOwnerThread().getState() + ", current stackTrace\n");
                    trace = pooledConnection.getOwnerThread().getStackTrace();
                    for (int i = 0; i < trace.length; ++i) {
                        buf.append("\tat ");
                        buf.append(trace[i].toString());
                        buf.append("\n");
                    }
                    DruidDataSource.LOG.error(buf.toString());
                }
            }
        }
        return removeCount;
    }
    
    @Override
    public Reference getReference() throws NamingException {
        final String className = this.getClass().getName();
        final String factoryName = className + "Factory";
        final Reference ref = new Reference(className, factoryName, null);
        ref.add(new StringRefAddr("instanceKey", this.instanceKey));
        ref.add(new StringRefAddr("url", this.getUrl()));
        ref.add(new StringRefAddr("username", this.getUsername()));
        ref.add(new StringRefAddr("password", this.getPassword()));
        return ref;
    }
    
    @Override
    public List<String> getFilterClassNames() {
        final List<String> names = new ArrayList<String>();
        for (final Filter filter : this.filters) {
            names.add(filter.getClass().getName());
        }
        return names;
    }
    
    @Override
    public int getRawDriverMajorVersion() {
        int version = -1;
        if (this.driver != null) {
            version = this.driver.getMajorVersion();
        }
        return version;
    }
    
    @Override
    public int getRawDriverMinorVersion() {
        int version = -1;
        if (this.driver != null) {
            version = this.driver.getMinorVersion();
        }
        return version;
    }
    
    @Override
    public String getProperties() {
        final Properties properties = new Properties();
        properties.putAll(this.connectProperties);
        if (properties.containsKey("password")) {
            properties.put("password", "******");
        }
        return properties.toString();
    }
    
    @Override
    public void shrink() {
        this.shrink(false, false);
    }
    
    public void shrink(final boolean checkTime) {
        this.shrink(checkTime, this.keepAlive);
    }
    
    public void shrink(final boolean checkTime, final boolean keepAlive) {
        try {
            this.lock.lockInterruptibly();
        }
        catch (InterruptedException e) {
            return;
        }
        boolean needFill = false;
        int evictCount = 0;
        int keepAliveCount = 0;
        final int fatalErrorIncrement = this.fatalErrorCount - this.fatalErrorCountLastShrink;
        this.fatalErrorCountLastShrink = this.fatalErrorCount;
        try {
            if (!this.inited) {
                return;
            }
            final int checkCount = this.poolingCount - this.minIdle;
            final long currentTimeMillis = System.currentTimeMillis();
            for (int i = 0; i < this.poolingCount; ++i) {
                final DruidConnectionHolder connection = this.connections[i];
                if ((this.onFatalError || fatalErrorIncrement > 0) && this.lastFatalErrorTimeMillis > connection.connectTimeMillis) {
                    this.keepAliveConnections[keepAliveCount++] = connection;
                }
                else if (checkTime) {
                    if (this.phyTimeoutMillis > 0L) {
                        final long phyConnectTimeMillis = currentTimeMillis - connection.connectTimeMillis;
                        if (phyConnectTimeMillis > this.phyTimeoutMillis) {
                            this.evictConnections[evictCount++] = connection;
                            continue;
                        }
                    }
                    final long idleMillis = currentTimeMillis - connection.lastActiveTimeMillis;
                    if (idleMillis < this.minEvictableIdleTimeMillis && idleMillis < this.keepAliveBetweenTimeMillis) {
                        break;
                    }
                    if (idleMillis >= this.minEvictableIdleTimeMillis) {
                        if (checkTime && i < checkCount) {
                            this.evictConnections[evictCount++] = connection;
                            continue;
                        }
                        if (idleMillis > this.maxEvictableIdleTimeMillis) {
                            this.evictConnections[evictCount++] = connection;
                            continue;
                        }
                    }
                    if (keepAlive && idleMillis >= this.keepAliveBetweenTimeMillis) {
                        this.keepAliveConnections[keepAliveCount++] = connection;
                    }
                }
                else {
                    if (i >= checkCount) {
                        break;
                    }
                    this.evictConnections[evictCount++] = connection;
                }
            }
            final int removeCount = evictCount + keepAliveCount;
            if (removeCount > 0) {
                System.arraycopy(this.connections, removeCount, this.connections, 0, this.poolingCount - removeCount);
                Arrays.fill(this.connections, this.poolingCount - removeCount, this.poolingCount, null);
                this.poolingCount -= removeCount;
            }
            this.keepAliveCheckCount += keepAliveCount;
            if (keepAlive && this.poolingCount + this.activeCount < this.minIdle) {
                needFill = true;
            }
        }
        finally {
            this.lock.unlock();
        }
        if (evictCount > 0) {
            for (int j = 0; j < evictCount; ++j) {
                final DruidConnectionHolder item = this.evictConnections[j];
                final Connection connection2 = item.getConnection();
                JdbcUtils.close(connection2);
                DruidDataSource.destroyCountUpdater.incrementAndGet(this);
            }
            Arrays.fill(this.evictConnections, null);
        }
        if (keepAliveCount > 0) {
            for (int j = keepAliveCount - 1; j >= 0; --j) {
                final DruidConnectionHolder holer = this.keepAliveConnections[j];
                final Connection connection2 = holer.getConnection();
                holer.incrementKeepAliveCheckCount();
                boolean validate = false;
                try {
                    this.validateConnection(connection2);
                    validate = true;
                }
                catch (Throwable error) {
                    if (DruidDataSource.LOG.isDebugEnabled()) {
                        DruidDataSource.LOG.debug("keepAliveErr", error);
                    }
                }
                boolean discard = !validate;
                if (validate) {
                    holer.lastKeepTimeMillis = System.currentTimeMillis();
                    final boolean putOk = this.put(holer, 0L, true);
                    if (!putOk) {
                        discard = true;
                    }
                }
                if (discard) {
                    try {
                        connection2.close();
                    }
                    catch (Exception ex) {}
                    this.lock.lock();
                    try {
                        ++this.discardCount;
                        if (this.activeCount + this.poolingCount <= this.minIdle) {
                            this.emptySignal();
                        }
                    }
                    finally {
                        this.lock.unlock();
                    }
                }
            }
            this.getDataSourceStat().addKeepAliveCheckCount(keepAliveCount);
            Arrays.fill(this.keepAliveConnections, null);
        }
        if (needFill) {
            this.lock.lock();
            try {
                for (int fillCount = this.minIdle - (this.activeCount + this.poolingCount + this.createTaskCount), k = 0; k < fillCount; ++k) {
                    this.emptySignal();
                }
            }
            finally {
                this.lock.unlock();
            }
        }
        else if (this.onFatalError || fatalErrorIncrement > 0) {
            this.lock.lock();
            try {
                this.emptySignal();
            }
            finally {
                this.lock.unlock();
            }
        }
    }
    
    @Override
    public int getWaitThreadCount() {
        this.lock.lock();
        try {
            return this.lock.getWaitQueueLength(this.notEmpty);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public long getNotEmptyWaitCount() {
        return this.notEmptyWaitCount;
    }
    
    @Override
    public int getNotEmptyWaitThreadCount() {
        this.lock.lock();
        try {
            return this.notEmptyWaitThreadCount;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public int getNotEmptyWaitThreadPeak() {
        this.lock.lock();
        try {
            return this.notEmptyWaitThreadPeak;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public long getNotEmptySignalCount() {
        return this.notEmptySignalCount;
    }
    
    @Override
    public long getNotEmptyWaitMillis() {
        return this.notEmptyWaitNanos / 1000000L;
    }
    
    @Override
    public long getNotEmptyWaitNanos() {
        return this.notEmptyWaitNanos;
    }
    
    @Override
    public int getLockQueueLength() {
        return this.lock.getQueueLength();
    }
    
    @Override
    public int getActivePeak() {
        return this.activePeak;
    }
    
    @Override
    public Date getActivePeakTime() {
        if (this.activePeakTime <= 0L) {
            return null;
        }
        return new Date(this.activePeakTime);
    }
    
    @Override
    public String dump() {
        this.lock.lock();
        try {
            return this.toString();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    @Override
    public long getErrorCount() {
        return this.errorCount;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("{");
        buf.append("\n\tCreateTime:\"");
        buf.append(Utils.toString(this.getCreatedTime()));
        buf.append("\"");
        buf.append(",\n\tActiveCount:");
        buf.append(this.getActiveCount());
        buf.append(",\n\tPoolingCount:");
        buf.append(this.getPoolingCount());
        buf.append(",\n\tCreateCount:");
        buf.append(this.getCreateCount());
        buf.append(",\n\tDestroyCount:");
        buf.append(this.getDestroyCount());
        buf.append(",\n\tCloseCount:");
        buf.append(this.getCloseCount());
        buf.append(",\n\tConnectCount:");
        buf.append(this.getConnectCount());
        buf.append(",\n\tConnections:[");
        for (int i = 0; i < this.poolingCount; ++i) {
            final DruidConnectionHolder conn = this.connections[i];
            if (conn != null) {
                if (i != 0) {
                    buf.append(",");
                }
                buf.append("\n\t\t");
                buf.append(conn.toString());
            }
        }
        buf.append("\n\t]");
        buf.append("\n}");
        if (this.isPoolPreparedStatements()) {
            buf.append("\n\n[");
            for (int i = 0; i < this.poolingCount; ++i) {
                final DruidConnectionHolder conn = this.connections[i];
                if (conn != null) {
                    if (i != 0) {
                        buf.append(",");
                    }
                    buf.append("\n\t{\n\tID:");
                    buf.append(System.identityHashCode(conn.getConnection()));
                    final PreparedStatementPool pool = conn.getStatementPool();
                    buf.append(", \n\tpoolStatements:[");
                    int entryIndex = 0;
                    try {
                        for (final Map.Entry<DruidPooledPreparedStatement.PreparedStatementKey, PreparedStatementHolder> entry : pool.getMap().entrySet()) {
                            if (entryIndex != 0) {
                                buf.append(",");
                            }
                            buf.append("\n\t\t{hitCount:");
                            buf.append(entry.getValue().getHitCount());
                            buf.append(",sql:\"");
                            buf.append(entry.getKey().getSql());
                            buf.append("\"");
                            buf.append("\t}");
                            ++entryIndex;
                        }
                    }
                    catch (ConcurrentModificationException ex) {}
                    buf.append("\n\t\t]");
                    buf.append("\n\t}");
                }
            }
            buf.append("\n]");
        }
        return buf.toString();
    }
    
    public List<Map<String, Object>> getPoolingConnectionInfo() {
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        this.lock.lock();
        try {
            for (int i = 0; i < this.poolingCount; ++i) {
                final DruidConnectionHolder connHolder = this.connections[i];
                final Connection conn = connHolder.getConnection();
                final Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("id", System.identityHashCode(conn));
                map.put("connectionId", connHolder.getConnectionId());
                map.put("useCount", connHolder.getUseCount());
                if (connHolder.lastActiveTimeMillis > 0L) {
                    map.put("lastActiveTime", new Date(connHolder.lastActiveTimeMillis));
                }
                if (connHolder.lastKeepTimeMillis > 0L) {
                    map.put("lastKeepTimeMillis", new Date(connHolder.lastKeepTimeMillis));
                }
                map.put("connectTime", new Date(connHolder.getTimeMillis()));
                map.put("holdability", connHolder.getUnderlyingHoldability());
                map.put("transactionIsolation", connHolder.getUnderlyingTransactionIsolation());
                map.put("autoCommit", connHolder.underlyingAutoCommit);
                map.put("readoOnly", connHolder.isUnderlyingReadOnly());
                if (connHolder.isPoolPreparedStatements()) {
                    final List<Map<String, Object>> stmtCache = new ArrayList<Map<String, Object>>();
                    final PreparedStatementPool stmtPool = connHolder.getStatementPool();
                    for (final PreparedStatementHolder stmtHolder : stmtPool.getMap().values()) {
                        final Map<String, Object> stmtInfo = new LinkedHashMap<String, Object>();
                        stmtInfo.put("sql", stmtHolder.key.getSql());
                        stmtInfo.put("defaultRowPrefetch", stmtHolder.getDefaultRowPrefetch());
                        stmtInfo.put("rowPrefetch", stmtHolder.getRowPrefetch());
                        stmtInfo.put("hitCount", stmtHolder.getHitCount());
                        stmtCache.add(stmtInfo);
                    }
                    map.put("pscache", stmtCache);
                }
                map.put("keepAliveCheckCount", connHolder.getKeepAliveCheckCount());
                list.add(map);
            }
        }
        finally {
            this.lock.unlock();
        }
        return list;
    }
    
    @Override
    public void logTransaction(final TransactionInfo info) {
        final long transactionMillis = info.getEndTimeMillis() - info.getStartTimeMillis();
        if (this.transactionThresholdMillis > 0L && transactionMillis > this.transactionThresholdMillis) {
            final StringBuilder buf = new StringBuilder();
            buf.append("long time transaction, take ");
            buf.append(transactionMillis);
            buf.append(" ms : ");
            for (final String sql : info.getSqlList()) {
                buf.append(sql);
                buf.append(";");
            }
            DruidDataSource.LOG.error(buf.toString(), new TransactionTimeoutException());
        }
    }
    
    @Override
    public String getVersion() {
        return VERSION.getVersionNumber();
    }
    
    @Override
    public JdbcDataSourceStat getDataSourceStat() {
        return this.dataSourceStat;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return this.cloneDruidDataSource();
    }
    
    public DruidDataSource cloneDruidDataSource() {
        final DruidDataSource x = new DruidDataSource();
        this.cloneTo(x);
        return x;
    }
    
    public Map<String, Object> getStatDataForMBean() {
        try {
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("Name", this.getName());
            map.put("URL", this.getUrl());
            map.put("CreateCount", this.getCreateCount());
            map.put("DestroyCount", this.getDestroyCount());
            map.put("ConnectCount", this.getConnectCount());
            map.put("CloseCount", this.getCloseCount());
            map.put("ActiveCount", this.getActiveCount());
            map.put("PoolingCount", this.getPoolingCount());
            map.put("LockQueueLength", this.getLockQueueLength());
            map.put("WaitThreadCount", this.getNotEmptyWaitThreadCount());
            map.put("InitialSize", this.getInitialSize());
            map.put("MaxActive", this.getMaxActive());
            map.put("MinIdle", this.getMinIdle());
            map.put("PoolPreparedStatements", this.isPoolPreparedStatements());
            map.put("TestOnBorrow", this.isTestOnBorrow());
            map.put("TestOnReturn", this.isTestOnReturn());
            map.put("MinEvictableIdleTimeMillis", this.minEvictableIdleTimeMillis);
            map.put("ConnectErrorCount", this.getConnectErrorCount());
            map.put("CreateTimespanMillis", this.getCreateTimespanMillis());
            map.put("DbType", this.dbTypeName);
            map.put("ValidationQuery", this.getValidationQuery());
            map.put("ValidationQueryTimeout", this.getValidationQueryTimeout());
            map.put("DriverClassName", this.getDriverClassName());
            map.put("Username", this.getUsername());
            map.put("RemoveAbandonedCount", this.getRemoveAbandonedCount());
            map.put("NotEmptyWaitCount", this.getNotEmptyWaitCount());
            map.put("NotEmptyWaitNanos", this.getNotEmptyWaitNanos());
            map.put("ErrorCount", this.getErrorCount());
            map.put("ReusePreparedStatementCount", this.getCachedPreparedStatementHitCount());
            map.put("StartTransactionCount", this.getStartTransactionCount());
            map.put("CommitCount", this.getCommitCount());
            map.put("RollbackCount", this.getRollbackCount());
            map.put("LastError", JMXUtils.getErrorCompositeData(this.getLastError()));
            map.put("LastCreateError", JMXUtils.getErrorCompositeData(this.getLastCreateError()));
            map.put("PreparedStatementCacheDeleteCount", this.getCachedPreparedStatementDeleteCount());
            map.put("PreparedStatementCacheAccessCount", this.getCachedPreparedStatementAccessCount());
            map.put("PreparedStatementCacheMissCount", this.getCachedPreparedStatementMissCount());
            map.put("PreparedStatementCacheHitCount", this.getCachedPreparedStatementHitCount());
            map.put("PreparedStatementCacheCurrentCount", this.getCachedPreparedStatementCount());
            map.put("Version", this.getVersion());
            map.put("LastErrorTime", this.getLastErrorTime());
            map.put("LastCreateErrorTime", this.getLastCreateErrorTime());
            map.put("CreateErrorCount", this.getCreateErrorCount());
            map.put("DiscardCount", this.getDiscardCount());
            map.put("ExecuteQueryCount", this.getExecuteQueryCount());
            map.put("ExecuteUpdateCount", this.getExecuteUpdateCount());
            return map;
        }
        catch (JMException ex) {
            throw new IllegalStateException("getStatData error", ex);
        }
    }
    
    public Map<String, Object> getStatData() {
        this.lock.lock();
        int poolingCount;
        int poolingPeak;
        Date poolingPeakTime;
        int activeCount;
        int activePeak;
        Date activePeakTime;
        long connectCount;
        long closeCount;
        try {
            poolingCount = this.poolingCount;
            poolingPeak = this.poolingPeak;
            poolingPeakTime = this.getPoolingPeakTime();
            activeCount = this.activeCount;
            activePeak = this.activePeak;
            activePeakTime = this.getActivePeakTime();
            connectCount = this.connectCount;
            closeCount = this.closeCount;
        }
        finally {
            this.lock.unlock();
        }
        final Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Identity", System.identityHashCode(this));
        dataMap.put("Name", this.getName());
        dataMap.put("DbType", this.dbTypeName);
        dataMap.put("DriverClassName", this.getDriverClassName());
        dataMap.put("URL", this.getUrl());
        dataMap.put("UserName", this.getUsername());
        dataMap.put("FilterClassNames", this.getFilterClassNames());
        dataMap.put("WaitThreadCount", this.getWaitThreadCount());
        dataMap.put("NotEmptyWaitCount", this.getNotEmptyWaitCount());
        dataMap.put("NotEmptyWaitMillis", this.getNotEmptyWaitMillis());
        dataMap.put("PoolingCount", poolingCount);
        dataMap.put("PoolingPeak", poolingPeak);
        dataMap.put("PoolingPeakTime", poolingPeakTime);
        dataMap.put("ActiveCount", activeCount);
        dataMap.put("ActivePeak", activePeak);
        dataMap.put("ActivePeakTime", activePeakTime);
        dataMap.put("InitialSize", this.getInitialSize());
        dataMap.put("MinIdle", this.getMinIdle());
        dataMap.put("MaxActive", this.getMaxActive());
        dataMap.put("QueryTimeout", this.getQueryTimeout());
        dataMap.put("TransactionQueryTimeout", this.getTransactionQueryTimeout());
        dataMap.put("LoginTimeout", this.getLoginTimeout());
        dataMap.put("ValidConnectionCheckerClassName", this.getValidConnectionCheckerClassName());
        dataMap.put("ExceptionSorterClassName", this.getExceptionSorterClassName());
        dataMap.put("TestOnBorrow", this.isTestOnBorrow());
        dataMap.put("TestOnReturn", this.isTestOnReturn());
        dataMap.put("TestWhileIdle", this.isTestWhileIdle());
        dataMap.put("DefaultAutoCommit", this.isDefaultAutoCommit());
        dataMap.put("DefaultReadOnly", this.getDefaultReadOnly());
        dataMap.put("DefaultTransactionIsolation", this.getDefaultTransactionIsolation());
        dataMap.put("LogicConnectCount", connectCount);
        dataMap.put("LogicCloseCount", closeCount);
        dataMap.put("LogicConnectErrorCount", this.getConnectErrorCount());
        dataMap.put("PhysicalConnectCount", this.getCreateCount());
        dataMap.put("PhysicalCloseCount", this.getDestroyCount());
        dataMap.put("PhysicalConnectErrorCount", this.getCreateErrorCount());
        dataMap.put("DiscardCount", this.getDiscardCount());
        dataMap.put("ExecuteCount", this.getExecuteCount());
        dataMap.put("ExecuteUpdateCount", this.getExecuteUpdateCount());
        dataMap.put("ExecuteQueryCount", this.getExecuteQueryCount());
        dataMap.put("ExecuteBatchCount", this.getExecuteBatchCount());
        dataMap.put("ErrorCount", this.getErrorCount());
        dataMap.put("CommitCount", this.getCommitCount());
        dataMap.put("RollbackCount", this.getRollbackCount());
        dataMap.put("PSCacheAccessCount", this.getCachedPreparedStatementAccessCount());
        dataMap.put("PSCacheHitCount", this.getCachedPreparedStatementHitCount());
        dataMap.put("PSCacheMissCount", this.getCachedPreparedStatementMissCount());
        dataMap.put("StartTransactionCount", this.getStartTransactionCount());
        dataMap.put("TransactionHistogram", this.getTransactionHistogramValues());
        dataMap.put("ConnectionHoldTimeHistogram", this.getDataSourceStat().getConnectionHoldHistogram().toArray());
        dataMap.put("RemoveAbandoned", this.isRemoveAbandoned());
        dataMap.put("ClobOpenCount", this.getDataSourceStat().getClobOpenCount());
        dataMap.put("BlobOpenCount", this.getDataSourceStat().getBlobOpenCount());
        dataMap.put("KeepAliveCheckCount", this.getDataSourceStat().getKeepAliveCheckCount());
        dataMap.put("KeepAlive", this.isKeepAlive());
        dataMap.put("FailFast", this.isFailFast());
        dataMap.put("MaxWait", this.getMaxWait());
        dataMap.put("MaxWaitThreadCount", this.getMaxWaitThreadCount());
        dataMap.put("PoolPreparedStatements", this.isPoolPreparedStatements());
        dataMap.put("MaxPoolPreparedStatementPerConnectionSize", this.getMaxPoolPreparedStatementPerConnectionSize());
        dataMap.put("MinEvictableIdleTimeMillis", this.minEvictableIdleTimeMillis);
        dataMap.put("MaxEvictableIdleTimeMillis", this.maxEvictableIdleTimeMillis);
        dataMap.put("LogDifferentThread", this.isLogDifferentThread());
        dataMap.put("RecycleErrorCount", this.getRecycleErrorCount());
        dataMap.put("PreparedStatementOpenCount", this.getPreparedStatementCount());
        dataMap.put("PreparedStatementClosedCount", this.getClosedPreparedStatementCount());
        dataMap.put("UseUnfairLock", this.isUseUnfairLock());
        dataMap.put("InitGlobalVariants", this.isInitGlobalVariants());
        dataMap.put("InitVariants", this.isInitVariants());
        return dataMap;
    }
    
    public JdbcSqlStat getSqlStat(final int sqlId) {
        return this.getDataSourceStat().getSqlStat(sqlId);
    }
    
    public JdbcSqlStat getSqlStat(final long sqlId) {
        return this.getDataSourceStat().getSqlStat(sqlId);
    }
    
    public Map<String, JdbcSqlStat> getSqlStatMap() {
        return this.getDataSourceStat().getSqlStatMap();
    }
    
    public Map<String, Object> getWallStatMap() {
        final WallProviderStatValue wallStatValue = this.getWallStatValue(false);
        if (wallStatValue != null) {
            return wallStatValue.toMap();
        }
        return null;
    }
    
    public WallProviderStatValue getWallStatValue(final boolean reset) {
        for (final Filter filter : this.filters) {
            if (filter instanceof WallFilter) {
                final WallFilter wallFilter = (WallFilter)filter;
                return wallFilter.getProvider().getStatValue(reset);
            }
        }
        return null;
    }
    
    public Lock getLock() {
        return this.lock;
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        for (final Filter filter : this.filters) {
            if (filter.isWrapperFor(iface)) {
                return true;
            }
        }
        return (this.statLogger != null && (this.statLogger.getClass() == iface || DruidDataSourceStatLogger.class == iface)) || super.isWrapperFor(iface);
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) {
        for (final Filter filter : this.filters) {
            if (filter.isWrapperFor(iface)) {
                return (T)filter;
            }
        }
        if (this.statLogger != null && (this.statLogger.getClass() == iface || DruidDataSourceStatLogger.class == iface)) {
            return (T)this.statLogger;
        }
        return super.unwrap(iface);
    }
    
    public boolean isLogDifferentThread() {
        return this.logDifferentThread;
    }
    
    public void setLogDifferentThread(final boolean logDifferentThread) {
        this.logDifferentThread = logDifferentThread;
    }
    
    public DruidPooledConnection tryGetConnection() throws SQLException {
        if (this.poolingCount == 0) {
            return null;
        }
        return this.getConnection();
    }
    
    @Override
    public int fill() throws SQLException {
        return this.fill(this.maxActive);
    }
    
    @Override
    public int fill(int toCount) throws SQLException {
        if (this.closed) {
            throw new DataSourceClosedException("dataSource already closed at " + new Date(this.closeTimeMillis));
        }
        if (toCount < 0) {
            throw new IllegalArgumentException("toCount can't not be less than zero");
        }
        this.init();
        if (toCount > this.maxActive) {
            toCount = this.maxActive;
        }
        int fillCount = 0;
        while (true) {
            try {
                this.lock.lockInterruptibly();
            }
            catch (InterruptedException e) {
                DruidDataSource.connectErrorCountUpdater.incrementAndGet(this);
                throw new SQLException("interrupt", e);
            }
            final boolean fillable = this.isFillable(toCount);
            this.lock.unlock();
            if (!fillable) {
                break;
            }
            DruidConnectionHolder holder;
            try {
                final PhysicalConnectionInfo pyConnInfo = this.createPhysicalConnection();
                holder = new DruidConnectionHolder(this, pyConnInfo);
            }
            catch (SQLException e2) {
                DruidDataSource.LOG.error("fill connection error, url: " + this.jdbcUrl, e2);
                DruidDataSource.connectErrorCountUpdater.incrementAndGet(this);
                throw e2;
            }
            try {
                this.lock.lockInterruptibly();
            }
            catch (InterruptedException e3) {
                DruidDataSource.connectErrorCountUpdater.incrementAndGet(this);
                throw new SQLException("interrupt", e3);
            }
            try {
                if (!this.isFillable(toCount)) {
                    JdbcUtils.close(holder.getConnection());
                    DruidDataSource.LOG.info("fill connections skip.");
                    break;
                }
                this.putLast(holder, System.currentTimeMillis());
                ++fillCount;
            }
            finally {
                this.lock.unlock();
            }
        }
        if (DruidDataSource.LOG.isInfoEnabled()) {
            DruidDataSource.LOG.info("fill " + fillCount + " connections");
        }
        return fillCount;
    }
    
    private boolean isFillable(final int toCount) {
        final int currentCount = this.poolingCount + this.activeCount;
        return currentCount < toCount && currentCount < this.maxActive;
    }
    
    public boolean isFull() {
        this.lock.lock();
        try {
            return this.poolingCount + this.activeCount >= this.maxActive;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private void emptySignal() {
        if (this.createScheduler == null) {
            this.empty.signal();
            return;
        }
        if (this.createTaskCount >= this.maxCreateTaskCount) {
            return;
        }
        if (this.activeCount + this.poolingCount + this.createTaskCount >= this.maxActive) {
            return;
        }
        this.submitCreateTask(false);
    }
    
    @Override
    public ObjectName preRegister(final MBeanServer server, final ObjectName name) throws Exception {
        if (server != null) {
            try {
                if (server.isRegistered(name)) {
                    server.unregisterMBean(name);
                }
            }
            catch (Exception ex) {
                DruidDataSource.LOG.warn("DruidDataSource preRegister error", ex);
            }
        }
        return name;
    }
    
    @Override
    public void postRegister(final Boolean registrationDone) {
    }
    
    @Override
    public void preDeregister() throws Exception {
    }
    
    @Override
    public void postDeregister() {
    }
    
    public boolean isClosed() {
        return this.closed;
    }
    
    public boolean isCheckExecuteTime() {
        return this.checkExecuteTime;
    }
    
    public void setCheckExecuteTime(final boolean checkExecuteTime) {
        this.checkExecuteTime = checkExecuteTime;
    }
    
    public void forEach(final Connection conn) {
    }
    
    static {
        LOG = LogFactory.getLog(DruidDataSource.class);
        DruidDataSource.waitNanosLocal = new ThreadLocal<Long>();
        DruidDataSource.autoFilters = null;
        recycleErrorCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidDataSource.class, "recycleErrorCount");
        connectErrorCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidDataSource.class, "connectErrorCount");
        resetCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidDataSource.class, "resetCount");
        createTaskIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidDataSource.class, "createTaskIdSeed");
    }
    
    public class CreateConnectionTask implements Runnable
    {
        private int errorCount;
        private boolean initTask;
        private final long taskId;
        
        public CreateConnectionTask() {
            this.errorCount = 0;
            this.initTask = false;
            this.taskId = DruidDataSource.createTaskIdSeedUpdater.getAndIncrement(DruidDataSource.this);
        }
        
        public CreateConnectionTask(final boolean initTask) {
            this.errorCount = 0;
            this.initTask = false;
            this.taskId = DruidDataSource.createTaskIdSeedUpdater.getAndIncrement(DruidDataSource.this);
            this.initTask = initTask;
        }
        
        @Override
        public void run() {
            this.runInternal();
        }
        
        private void runInternal() {
            while (true) {
                DruidDataSource.this.lock.lock();
                try {
                    if (DruidDataSource.this.closed || DruidDataSource.this.closing) {
                        DruidDataSource.this.clearCreateTask(this.taskId);
                        return;
                    }
                    boolean emptyWait = true;
                    if (DruidDataSource.this.createError != null && DruidDataSource.this.poolingCount == 0) {
                        emptyWait = false;
                    }
                    if (emptyWait) {
                        if (DruidDataSource.this.poolingCount >= DruidDataSource.this.notEmptyWaitThreadCount && (!DruidDataSource.this.keepAlive || DruidDataSource.this.activeCount + DruidDataSource.this.poolingCount >= DruidDataSource.this.minIdle) && !this.initTask && !DruidDataSource.this.isFailContinuous() && !DruidDataSource.this.isOnFatalError()) {
                            DruidDataSource.this.clearCreateTask(this.taskId);
                            return;
                        }
                        if (DruidDataSource.this.activeCount + DruidDataSource.this.poolingCount >= DruidDataSource.this.maxActive) {
                            DruidDataSource.this.clearCreateTask(this.taskId);
                            return;
                        }
                    }
                }
                finally {
                    DruidDataSource.this.lock.unlock();
                }
                PhysicalConnectionInfo physicalConnection = null;
                try {
                    physicalConnection = DruidDataSource.this.createPhysicalConnection();
                }
                catch (OutOfMemoryError e) {
                    DruidDataSource.LOG.error("create connection OutOfMemoryError, out memory. ", e);
                    ++this.errorCount;
                    if (this.errorCount > DruidDataSource.this.connectionErrorRetryAttempts && DruidDataSource.this.timeBetweenConnectErrorMillis > 0L) {
                        DruidDataSource.this.setFailContinuous(true);
                        if (DruidDataSource.this.failFast) {
                            DruidDataSource.this.lock.lock();
                            try {
                                DruidDataSource.this.notEmpty.signalAll();
                            }
                            finally {
                                DruidDataSource.this.lock.unlock();
                            }
                        }
                        if (DruidDataSource.this.breakAfterAcquireFailure) {
                            DruidDataSource.this.lock.lock();
                            try {
                                DruidDataSource.this.clearCreateTask(this.taskId);
                            }
                            finally {
                                DruidDataSource.this.lock.unlock();
                            }
                            return;
                        }
                        this.errorCount = 0;
                        if (DruidDataSource.this.closing || DruidDataSource.this.closed) {
                            DruidDataSource.this.lock.lock();
                            try {
                                DruidDataSource.this.clearCreateTask(this.taskId);
                            }
                            finally {
                                DruidDataSource.this.lock.unlock();
                            }
                            return;
                        }
                        DruidDataSource.this.createSchedulerFuture = DruidDataSource.this.createScheduler.schedule(this, DruidDataSource.this.timeBetweenConnectErrorMillis, TimeUnit.MILLISECONDS);
                        return;
                    }
                }
                catch (SQLException e2) {
                    DruidDataSource.LOG.error("create connection SQLException, url: " + DruidDataSource.this.jdbcUrl, e2);
                    ++this.errorCount;
                    if (this.errorCount > DruidDataSource.this.connectionErrorRetryAttempts && DruidDataSource.this.timeBetweenConnectErrorMillis > 0L) {
                        DruidDataSource.this.setFailContinuous(true);
                        if (DruidDataSource.this.failFast) {
                            DruidDataSource.this.lock.lock();
                            try {
                                DruidDataSource.this.notEmpty.signalAll();
                            }
                            finally {
                                DruidDataSource.this.lock.unlock();
                            }
                        }
                        if (DruidDataSource.this.breakAfterAcquireFailure) {
                            DruidDataSource.this.lock.lock();
                            try {
                                DruidDataSource.this.clearCreateTask(this.taskId);
                            }
                            finally {
                                DruidDataSource.this.lock.unlock();
                            }
                            return;
                        }
                        this.errorCount = 0;
                        if (DruidDataSource.this.closing || DruidDataSource.this.closed) {
                            DruidDataSource.this.lock.lock();
                            try {
                                DruidDataSource.this.clearCreateTask(this.taskId);
                            }
                            finally {
                                DruidDataSource.this.lock.unlock();
                            }
                            return;
                        }
                        DruidDataSource.this.createSchedulerFuture = DruidDataSource.this.createScheduler.schedule(this, DruidDataSource.this.timeBetweenConnectErrorMillis, TimeUnit.MILLISECONDS);
                        return;
                    }
                }
                catch (RuntimeException e3) {
                    DruidDataSource.LOG.error("create connection RuntimeException", e3);
                    DruidDataSource.this.setFailContinuous(true);
                    continue;
                }
                catch (Error e4) {
                    DruidDataSource.this.lock.lock();
                    try {
                        DruidDataSource.this.clearCreateTask(this.taskId);
                    }
                    finally {
                        DruidDataSource.this.lock.unlock();
                    }
                    DruidDataSource.LOG.error("create connection Error", e4);
                    DruidDataSource.this.setFailContinuous(true);
                    break;
                }
                catch (Throwable e5) {
                    DruidDataSource.this.lock.lock();
                    try {
                        DruidDataSource.this.clearCreateTask(this.taskId);
                    }
                    finally {
                        DruidDataSource.this.lock.unlock();
                    }
                    DruidDataSource.LOG.error("create connection unexecpted error.", e5);
                    break;
                }
                if (physicalConnection == null) {
                    continue;
                }
                physicalConnection.createTaskId = this.taskId;
                final boolean result = DruidDataSource.this.put(physicalConnection);
                if (!result) {
                    JdbcUtils.close(physicalConnection.getPhysicalConnection());
                    DruidDataSource.LOG.info("put physical connection to pool failed.");
                    break;
                }
                break;
            }
        }
    }
    
    public class CreateConnectionThread extends Thread
    {
        public CreateConnectionThread(final String name) {
            super(name);
            this.setDaemon(true);
        }
        
        @Override
        public void run() {
            DruidDataSource.this.initedLatch.countDown();
            long lastDiscardCount = 0L;
            int errorCount = 0;
            while (true) {
                try {
                    DruidDataSource.this.lock.lockInterruptibly();
                }
                catch (InterruptedException e5) {
                    break;
                }
                final long discardCount = DruidDataSource.this.discardCount;
                final boolean discardChanged = discardCount - lastDiscardCount > 0L;
                lastDiscardCount = discardCount;
                try {
                    boolean emptyWait = true;
                    if (DruidDataSource.this.createError != null && DruidDataSource.this.poolingCount == 0 && !discardChanged) {
                        emptyWait = false;
                    }
                    if (emptyWait && DruidDataSource.this.asyncInit && DruidDataSource.this.createCount < DruidDataSource.this.initialSize) {
                        emptyWait = false;
                    }
                    if (emptyWait) {
                        if (DruidDataSource.this.poolingCount >= DruidDataSource.this.notEmptyWaitThreadCount && (!DruidDataSource.this.keepAlive || DruidDataSource.this.activeCount + DruidDataSource.this.poolingCount >= DruidDataSource.this.minIdle) && !DruidDataSource.this.isFailContinuous()) {
                            DruidDataSource.this.empty.await();
                        }
                        if (DruidDataSource.this.activeCount + DruidDataSource.this.poolingCount >= DruidDataSource.this.maxActive) {
                            DruidDataSource.this.empty.await();
                            continue;
                        }
                    }
                }
                catch (InterruptedException e) {
                    DruidDataSource.this.lastCreateError = e;
                    DruidDataSource.this.lastErrorTimeMillis = System.currentTimeMillis();
                    if (!DruidDataSource.this.closing && !DruidDataSource.this.closed) {
                        DruidDataSource.LOG.error("create connection Thread Interrupted, url: " + DruidDataSource.this.jdbcUrl, e);
                    }
                    break;
                }
                finally {
                    DruidDataSource.this.lock.unlock();
                }
                PhysicalConnectionInfo connection = null;
                try {
                    connection = DruidDataSource.this.createPhysicalConnection();
                }
                catch (SQLException e2) {
                    DruidDataSource.LOG.error("create connection SQLException, url: " + DruidDataSource.this.jdbcUrl + ", errorCode " + e2.getErrorCode() + ", state " + e2.getSQLState(), e2);
                    if (++errorCount > DruidDataSource.this.connectionErrorRetryAttempts && DruidDataSource.this.timeBetweenConnectErrorMillis > 0L) {
                        DruidDataSource.this.setFailContinuous(true);
                        if (DruidDataSource.this.failFast) {
                            DruidDataSource.this.lock.lock();
                            try {
                                DruidDataSource.this.notEmpty.signalAll();
                            }
                            finally {
                                DruidDataSource.this.lock.unlock();
                            }
                        }
                        if (DruidDataSource.this.breakAfterAcquireFailure) {
                            break;
                        }
                        try {
                            Thread.sleep(DruidDataSource.this.timeBetweenConnectErrorMillis);
                        }
                        catch (InterruptedException interruptEx) {
                            break;
                        }
                    }
                }
                catch (RuntimeException e3) {
                    DruidDataSource.LOG.error("create connection RuntimeException", e3);
                    DruidDataSource.this.setFailContinuous(true);
                    continue;
                }
                catch (Error e4) {
                    DruidDataSource.LOG.error("create connection Error", e4);
                    DruidDataSource.this.setFailContinuous(true);
                    break;
                }
                if (connection == null) {
                    continue;
                }
                final boolean result = DruidDataSource.this.put(connection);
                if (!result) {
                    JdbcUtils.close(connection.getPhysicalConnection());
                    DruidDataSource.LOG.info("put physical connection to pool failed.");
                }
                errorCount = 0;
                if (DruidDataSource.this.closing) {
                    break;
                }
                if (DruidDataSource.this.closed) {
                    break;
                }
            }
        }
    }
    
    public class DestroyConnectionThread extends Thread
    {
        public DestroyConnectionThread(final String name) {
            super(name);
            this.setDaemon(true);
        }
        
        @Override
        public void run() {
            DruidDataSource.this.initedLatch.countDown();
            try {
                while (!DruidDataSource.this.closed && !DruidDataSource.this.closing) {
                    if (DruidDataSource.this.timeBetweenEvictionRunsMillis > 0L) {
                        Thread.sleep(DruidDataSource.this.timeBetweenEvictionRunsMillis);
                    }
                    else {
                        Thread.sleep(1000L);
                    }
                    if (Thread.interrupted()) {
                        break;
                    }
                    DruidDataSource.this.destroyTask.run();
                }
            }
            catch (InterruptedException e) {}
        }
    }
    
    public class DestroyTask implements Runnable
    {
        @Override
        public void run() {
            DruidDataSource.this.shrink(true, DruidDataSource.this.keepAlive);
            if (DruidDataSource.this.isRemoveAbandoned()) {
                DruidDataSource.this.removeAbandoned();
            }
        }
    }
    
    public class LogStatsThread extends Thread
    {
        public LogStatsThread(final String name) {
            super(name);
            this.setDaemon(true);
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        DruidDataSource.this.logStats();
                    }
                    catch (Exception e) {
                        DruidDataSource.LOG.error("logStats error", e);
                    }
                    Thread.sleep(DruidDataSource.this.timeBetweenLogStatsMillis);
                }
            }
            catch (InterruptedException ex) {}
        }
    }
}
