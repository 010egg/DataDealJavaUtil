// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.DruidRuntimeException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.management.JMException;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import com.alibaba.druid.stat.JdbcStatManager;
import javax.management.openmbean.CompositeDataSupport;
import java.util.HashMap;
import com.alibaba.druid.util.DruidPasswordCallback;
import com.alibaba.druid.filter.FilterChainImpl;
import java.util.HashSet;
import java.util.Set;
import com.alibaba.druid.VERSION;
import com.alibaba.druid.util.MySqlUtils;
import com.alibaba.druid.stat.JdbcSqlStat;
import java.sql.ResultSet;
import java.sql.Statement;
import com.alibaba.druid.util.JdbcUtils;
import java.sql.Connection;
import com.alibaba.druid.filter.FilterManager;
import com.alibaba.druid.pool.vendor.NullExceptionSorter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.proxy.jdbc.TransactionInfo;
import java.util.IdentityHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.OutputStream;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.management.ObjectName;
import com.alibaba.druid.util.Histogram;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.Date;
import java.util.Map;
import java.sql.Driver;
import com.alibaba.druid.filter.Filter;
import java.util.List;
import java.io.PrintWriter;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import java.util.Properties;
import com.alibaba.druid.support.logging.Log;
import java.io.Serializable;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import javax.sql.DataSource;

public abstract class DruidAbstractDataSource extends WrapperAdapter implements DruidAbstractDataSourceMBean, DataSource, DataSourceProxy, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Log LOG;
    public static final int DEFAULT_INITIAL_SIZE = 0;
    public static final int DEFAULT_MAX_ACTIVE_SIZE = 8;
    public static final int DEFAULT_MAX_IDLE = 8;
    public static final int DEFAULT_MIN_IDLE = 0;
    public static final int DEFAULT_MAX_WAIT = -1;
    public static final String DEFAULT_VALIDATION_QUERY;
    public static final boolean DEFAULT_TEST_ON_BORROW = false;
    public static final boolean DEFAULT_TEST_ON_RETURN = false;
    public static final boolean DEFAULT_WHILE_IDLE = true;
    public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 60000L;
    public static final long DEFAULT_TIME_BETWEEN_CONNECT_ERROR_MILLIS = 500L;
    public static final int DEFAULT_NUM_TESTS_PER_EVICTION_RUN = 3;
    public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1800000L;
    public static final long DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS = 25200000L;
    public static final long DEFAULT_PHY_TIMEOUT_MILLIS = -1L;
    protected volatile boolean defaultAutoCommit;
    protected volatile Boolean defaultReadOnly;
    protected volatile Integer defaultTransactionIsolation;
    protected volatile String defaultCatalog;
    protected String name;
    protected volatile String username;
    protected volatile String password;
    protected volatile String jdbcUrl;
    protected volatile String driverClass;
    protected volatile ClassLoader driverClassLoader;
    protected volatile Properties connectProperties;
    protected volatile PasswordCallback passwordCallback;
    protected volatile NameCallback userCallback;
    protected volatile int initialSize;
    protected volatile int maxActive;
    protected volatile int minIdle;
    protected volatile int maxIdle;
    protected volatile long maxWait;
    protected int notFullTimeoutRetryCount;
    protected volatile String validationQuery;
    protected volatile int validationQueryTimeout;
    protected volatile boolean testOnBorrow;
    protected volatile boolean testOnReturn;
    protected volatile boolean testWhileIdle;
    protected volatile boolean poolPreparedStatements;
    protected volatile boolean sharePreparedStatements;
    protected volatile int maxPoolPreparedStatementPerConnectionSize;
    protected volatile boolean inited;
    protected volatile boolean initExceptionThrow;
    protected PrintWriter logWriter;
    protected List<Filter> filters;
    private boolean clearFiltersEnable;
    protected volatile ExceptionSorter exceptionSorter;
    protected Driver driver;
    protected volatile int queryTimeout;
    protected volatile int transactionQueryTimeout;
    protected long createTimespan;
    protected volatile int maxWaitThreadCount;
    protected volatile boolean accessToUnderlyingConnectionAllowed;
    protected volatile long timeBetweenEvictionRunsMillis;
    protected volatile int numTestsPerEvictionRun;
    protected volatile long minEvictableIdleTimeMillis;
    protected volatile long maxEvictableIdleTimeMillis;
    protected volatile long keepAliveBetweenTimeMillis;
    protected volatile long phyTimeoutMillis;
    protected volatile long phyMaxUseCount;
    protected volatile boolean removeAbandoned;
    protected volatile long removeAbandonedTimeoutMillis;
    protected volatile boolean logAbandoned;
    protected volatile int maxOpenPreparedStatements;
    protected volatile List<String> connectionInitSqls;
    protected volatile String dbTypeName;
    protected volatile long timeBetweenConnectErrorMillis;
    protected volatile ValidConnectionChecker validConnectionChecker;
    protected final Map<DruidPooledConnection, Object> activeConnections;
    protected static final Object PRESENT;
    protected long id;
    protected int connectionErrorRetryAttempts;
    protected boolean breakAfterAcquireFailure;
    protected long transactionThresholdMillis;
    protected final Date createdTime;
    protected Date initedTime;
    protected volatile long errorCount;
    protected volatile long dupCloseCount;
    protected volatile long startTransactionCount;
    protected volatile long commitCount;
    protected volatile long rollbackCount;
    protected volatile long cachedPreparedStatementHitCount;
    protected volatile long preparedStatementCount;
    protected volatile long closedPreparedStatementCount;
    protected volatile long cachedPreparedStatementCount;
    protected volatile long cachedPreparedStatementDeleteCount;
    protected volatile long cachedPreparedStatementMissCount;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> errorCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> dupCloseCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> startTransactionCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> commitCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> rollbackCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> cachedPreparedStatementHitCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> preparedStatementCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> closedPreparedStatementCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> cachedPreparedStatementCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> cachedPreparedStatementDeleteCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> cachedPreparedStatementMissCountUpdater;
    protected final Histogram transactionHistogram;
    private boolean dupCloseLogEnable;
    private ObjectName objectName;
    protected volatile long executeCount;
    protected volatile long executeQueryCount;
    protected volatile long executeUpdateCount;
    protected volatile long executeBatchCount;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> executeQueryCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> executeUpdateCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> executeBatchCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> executeCountUpdater;
    protected volatile Throwable createError;
    protected volatile Throwable lastError;
    protected volatile long lastErrorTimeMillis;
    protected volatile Throwable lastCreateError;
    protected volatile long lastCreateErrorTimeMillis;
    protected volatile long lastCreateStartTimeMillis;
    protected boolean isOracle;
    protected boolean isMySql;
    protected boolean useOracleImplicitCache;
    protected ReentrantLock lock;
    protected Condition notEmpty;
    protected Condition empty;
    protected ReentrantLock activeConnectionLock;
    protected volatile int createErrorCount;
    protected volatile int creatingCount;
    protected volatile int directCreateCount;
    protected volatile long createCount;
    protected volatile long destroyCount;
    protected volatile long createStartNanos;
    static final AtomicIntegerFieldUpdater<DruidAbstractDataSource> createErrorCountUpdater;
    static final AtomicIntegerFieldUpdater<DruidAbstractDataSource> creatingCountUpdater;
    static final AtomicIntegerFieldUpdater<DruidAbstractDataSource> directCreateCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> createCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> destroyCountUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> createStartNanosUpdater;
    private Boolean useUnfairLock;
    private boolean useLocalSessionState;
    protected long timeBetweenLogStatsMillis;
    protected DruidDataSourceStatLogger statLogger;
    private boolean asyncCloseConnectionEnable;
    protected int maxCreateTaskCount;
    protected boolean failFast;
    protected volatile int failContinuous;
    protected volatile long failContinuousTimeMillis;
    protected ScheduledExecutorService destroyScheduler;
    protected ScheduledExecutorService createScheduler;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> failContinuousTimeMillisUpdater;
    static final AtomicIntegerFieldUpdater<DruidAbstractDataSource> failContinuousUpdater;
    protected boolean initVariants;
    protected boolean initGlobalVariants;
    protected volatile boolean onFatalError;
    protected volatile int onFatalErrorMaxActive;
    protected volatile int fatalErrorCount;
    protected volatile int fatalErrorCountLastShrink;
    protected volatile long lastFatalErrorTimeMillis;
    protected volatile String lastFatalErrorSql;
    protected volatile Throwable lastFatalError;
    protected volatile long connectionIdSeed;
    protected volatile long statementIdSeed;
    protected volatile long resultSetIdSeed;
    protected volatile long transactionIdSeed;
    protected volatile long metaDataIdSeed;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> connectionIdSeedUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> statementIdSeedUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> resultSetIdSeedUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> transactionIdSeedUpdater;
    static final AtomicLongFieldUpdater<DruidAbstractDataSource> metaDataIdSeedUpdater;
    
    public DruidAbstractDataSource(final boolean lockFair) {
        this.defaultAutoCommit = true;
        this.defaultCatalog = null;
        this.connectProperties = new Properties();
        this.initialSize = 0;
        this.maxActive = 8;
        this.minIdle = 0;
        this.maxIdle = 8;
        this.maxWait = -1L;
        this.notFullTimeoutRetryCount = 0;
        this.validationQuery = DruidAbstractDataSource.DEFAULT_VALIDATION_QUERY;
        this.validationQueryTimeout = -1;
        this.testOnBorrow = false;
        this.testOnReturn = false;
        this.testWhileIdle = true;
        this.poolPreparedStatements = false;
        this.sharePreparedStatements = false;
        this.maxPoolPreparedStatementPerConnectionSize = 10;
        this.inited = false;
        this.initExceptionThrow = true;
        this.logWriter = new PrintWriter(System.out);
        this.filters = new CopyOnWriteArrayList<Filter>();
        this.clearFiltersEnable = true;
        this.exceptionSorter = null;
        this.maxWaitThreadCount = -1;
        this.accessToUnderlyingConnectionAllowed = true;
        this.timeBetweenEvictionRunsMillis = 60000L;
        this.numTestsPerEvictionRun = 3;
        this.minEvictableIdleTimeMillis = 1800000L;
        this.maxEvictableIdleTimeMillis = 25200000L;
        this.keepAliveBetweenTimeMillis = 120000L;
        this.phyTimeoutMillis = -1L;
        this.phyMaxUseCount = -1L;
        this.removeAbandonedTimeoutMillis = 300000L;
        this.maxOpenPreparedStatements = -1;
        this.timeBetweenConnectErrorMillis = 500L;
        this.validConnectionChecker = null;
        this.activeConnections = new IdentityHashMap<DruidPooledConnection, Object>();
        this.connectionErrorRetryAttempts = 1;
        this.breakAfterAcquireFailure = false;
        this.transactionThresholdMillis = 0L;
        this.createdTime = new Date();
        this.errorCount = 0L;
        this.dupCloseCount = 0L;
        this.startTransactionCount = 0L;
        this.commitCount = 0L;
        this.rollbackCount = 0L;
        this.cachedPreparedStatementHitCount = 0L;
        this.preparedStatementCount = 0L;
        this.closedPreparedStatementCount = 0L;
        this.cachedPreparedStatementCount = 0L;
        this.cachedPreparedStatementDeleteCount = 0L;
        this.cachedPreparedStatementMissCount = 0L;
        this.transactionHistogram = new Histogram(new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L });
        this.dupCloseLogEnable = false;
        this.executeCount = 0L;
        this.executeQueryCount = 0L;
        this.executeUpdateCount = 0L;
        this.executeBatchCount = 0L;
        this.isOracle = false;
        this.isMySql = false;
        this.useOracleImplicitCache = true;
        this.activeConnectionLock = new ReentrantLock();
        this.createErrorCount = 0;
        this.creatingCount = 0;
        this.directCreateCount = 0;
        this.createCount = 0L;
        this.destroyCount = 0L;
        this.createStartNanos = 0L;
        this.useUnfairLock = null;
        this.useLocalSessionState = true;
        this.statLogger = new DruidDataSourceStatLoggerImpl();
        this.asyncCloseConnectionEnable = false;
        this.maxCreateTaskCount = 3;
        this.failFast = false;
        this.failContinuous = 0;
        this.failContinuousTimeMillis = 0L;
        this.initVariants = false;
        this.initGlobalVariants = false;
        this.onFatalError = false;
        this.onFatalErrorMaxActive = 0;
        this.fatalErrorCount = 0;
        this.fatalErrorCountLastShrink = 0;
        this.lastFatalErrorTimeMillis = 0L;
        this.lastFatalErrorSql = null;
        this.lastFatalError = null;
        this.connectionIdSeed = 10000L;
        this.statementIdSeed = 20000L;
        this.resultSetIdSeed = 50000L;
        this.transactionIdSeed = 60000L;
        this.metaDataIdSeed = 80000L;
        this.lock = new ReentrantLock(lockFair);
        this.notEmpty = this.lock.newCondition();
        this.empty = this.lock.newCondition();
    }
    
    public boolean isUseLocalSessionState() {
        return this.useLocalSessionState;
    }
    
    public void setUseLocalSessionState(final boolean useLocalSessionState) {
        this.useLocalSessionState = useLocalSessionState;
    }
    
    public DruidDataSourceStatLogger getStatLogger() {
        return this.statLogger;
    }
    
    public void setStatLoggerClassName(final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            final DruidDataSourceStatLogger statLogger = (DruidDataSourceStatLogger)clazz.newInstance();
            this.setStatLogger(statLogger);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(className, e);
        }
    }
    
    public void setStatLogger(final DruidDataSourceStatLogger statLogger) {
        this.statLogger = statLogger;
    }
    
    public long getTimeBetweenLogStatsMillis() {
        return this.timeBetweenLogStatsMillis;
    }
    
    public void setTimeBetweenLogStatsMillis(final long timeBetweenLogStatsMillis) {
        this.timeBetweenLogStatsMillis = timeBetweenLogStatsMillis;
    }
    
    public boolean isOracle() {
        return this.isOracle;
    }
    
    public void setOracle(final boolean isOracle) {
        if (this.inited) {
            throw new IllegalStateException();
        }
        this.isOracle = isOracle;
    }
    
    public boolean isUseUnfairLock() {
        return !this.lock.isFair();
    }
    
    public void setUseUnfairLock(final boolean useUnfairLock) {
        if (this.lock.isFair() == !useUnfairLock && this.useUnfairLock != null) {
            return;
        }
        if (!this.inited) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                if (!this.inited) {
                    this.lock = new ReentrantLock(!useUnfairLock);
                    this.notEmpty = this.lock.newCondition();
                    this.empty = this.lock.newCondition();
                    this.useUnfairLock = useUnfairLock;
                }
            }
            finally {
                lock.unlock();
            }
        }
    }
    
    @Override
    public boolean isUseOracleImplicitCache() {
        return this.useOracleImplicitCache;
    }
    
    @Override
    public void setUseOracleImplicitCache(final boolean useOracleImplicitCache) {
        if (this.useOracleImplicitCache != useOracleImplicitCache) {
            this.useOracleImplicitCache = useOracleImplicitCache;
            final boolean isOracleDriver10 = this.isOracle() && this.driver != null && this.driver.getMajorVersion() == 10;
            if (isOracleDriver10 && useOracleImplicitCache) {
                this.getConnectProperties().setProperty("oracle.jdbc.FreeMemoryOnEnterImplicitCache", "true");
            }
            else {
                this.getConnectProperties().remove("oracle.jdbc.FreeMemoryOnEnterImplicitCache");
            }
        }
    }
    
    public Throwable getLastCreateError() {
        return this.lastCreateError;
    }
    
    public Throwable getLastError() {
        return this.lastError;
    }
    
    public long getLastErrorTimeMillis() {
        return this.lastErrorTimeMillis;
    }
    
    public Date getLastErrorTime() {
        if (this.lastErrorTimeMillis <= 0L) {
            return null;
        }
        return new Date(this.lastErrorTimeMillis);
    }
    
    public long getLastCreateErrorTimeMillis() {
        return this.lastCreateErrorTimeMillis;
    }
    
    public Date getLastCreateErrorTime() {
        if (this.lastCreateErrorTimeMillis <= 0L) {
            return null;
        }
        return new Date(this.lastCreateErrorTimeMillis);
    }
    
    @Override
    public int getTransactionQueryTimeout() {
        if (this.transactionQueryTimeout <= 0) {
            return this.queryTimeout;
        }
        return this.transactionQueryTimeout;
    }
    
    public void setTransactionQueryTimeout(final int transactionQueryTimeout) {
        this.transactionQueryTimeout = transactionQueryTimeout;
    }
    
    public long getExecuteCount() {
        return this.executeCount + this.executeQueryCount + this.executeUpdateCount + this.executeBatchCount;
    }
    
    public long getExecuteUpdateCount() {
        return this.executeUpdateCount;
    }
    
    public long getExecuteQueryCount() {
        return this.executeQueryCount;
    }
    
    public long getExecuteBatchCount() {
        return this.executeBatchCount;
    }
    
    public long getAndResetExecuteCount() {
        return DruidAbstractDataSource.executeCountUpdater.getAndSet(this, 0L) + DruidAbstractDataSource.executeQueryCountUpdater.getAndSet(this, 0L) + DruidAbstractDataSource.executeUpdateCountUpdater.getAndSet(this, 0L) + DruidAbstractDataSource.executeBatchCountUpdater.getAndSet(this, 0L);
    }
    
    public long getExecuteCount2() {
        return this.executeCount;
    }
    
    public void incrementExecuteCount() {
        DruidAbstractDataSource.executeCountUpdater.incrementAndGet(this);
    }
    
    public void incrementExecuteUpdateCount() {
        ++this.executeUpdateCount;
    }
    
    public void incrementExecuteQueryCount() {
        ++this.executeQueryCount;
    }
    
    public void incrementExecuteBatchCount() {
        ++this.executeBatchCount;
    }
    
    public boolean isDupCloseLogEnable() {
        return this.dupCloseLogEnable;
    }
    
    public void setDupCloseLogEnable(final boolean dupCloseLogEnable) {
        this.dupCloseLogEnable = dupCloseLogEnable;
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    public void setObjectName(final ObjectName objectName) {
        this.objectName = objectName;
    }
    
    public Histogram getTransactionHistogram() {
        return this.transactionHistogram;
    }
    
    public void incrementCachedPreparedStatementCount() {
        DruidAbstractDataSource.cachedPreparedStatementCountUpdater.incrementAndGet(this);
    }
    
    public void decrementCachedPreparedStatementCount() {
        DruidAbstractDataSource.cachedPreparedStatementCountUpdater.decrementAndGet(this);
    }
    
    public void incrementCachedPreparedStatementDeleteCount() {
        DruidAbstractDataSource.cachedPreparedStatementDeleteCountUpdater.incrementAndGet(this);
    }
    
    public void incrementCachedPreparedStatementMissCount() {
        DruidAbstractDataSource.cachedPreparedStatementMissCountUpdater.incrementAndGet(this);
    }
    
    @Override
    public long getCachedPreparedStatementMissCount() {
        return this.cachedPreparedStatementMissCount;
    }
    
    @Override
    public long getCachedPreparedStatementAccessCount() {
        return this.cachedPreparedStatementMissCount + this.cachedPreparedStatementHitCount;
    }
    
    @Override
    public long getCachedPreparedStatementDeleteCount() {
        return this.cachedPreparedStatementDeleteCount;
    }
    
    @Override
    public long getCachedPreparedStatementCount() {
        return this.cachedPreparedStatementCount;
    }
    
    public void incrementClosedPreparedStatementCount() {
        DruidAbstractDataSource.closedPreparedStatementCountUpdater.incrementAndGet(this);
    }
    
    @Override
    public long getClosedPreparedStatementCount() {
        return this.closedPreparedStatementCount;
    }
    
    public void incrementPreparedStatementCount() {
        DruidAbstractDataSource.preparedStatementCountUpdater.incrementAndGet(this);
    }
    
    @Override
    public long getPreparedStatementCount() {
        return this.preparedStatementCount;
    }
    
    public void incrementCachedPreparedStatementHitCount() {
        DruidAbstractDataSource.cachedPreparedStatementHitCountUpdater.incrementAndGet(this);
    }
    
    @Override
    public long getCachedPreparedStatementHitCount() {
        return this.cachedPreparedStatementHitCount;
    }
    
    @Override
    public long getTransactionThresholdMillis() {
        return this.transactionThresholdMillis;
    }
    
    @Override
    public void setTransactionThresholdMillis(final long transactionThresholdMillis) {
        this.transactionThresholdMillis = transactionThresholdMillis;
    }
    
    public abstract void logTransaction(final TransactionInfo p0);
    
    @Override
    public long[] getTransactionHistogramValues() {
        return this.transactionHistogram.toArray();
    }
    
    public long[] getTransactionHistogramRanges() {
        return this.transactionHistogram.getRanges();
    }
    
    @Override
    public long getCommitCount() {
        return this.commitCount;
    }
    
    public void incrementCommitCount() {
        DruidAbstractDataSource.commitCountUpdater.incrementAndGet(this);
    }
    
    @Override
    public long getRollbackCount() {
        return this.rollbackCount;
    }
    
    public void incrementRollbackCount() {
        DruidAbstractDataSource.rollbackCountUpdater.incrementAndGet(this);
    }
    
    @Override
    public long getStartTransactionCount() {
        return this.startTransactionCount;
    }
    
    public void incrementStartTransactionCount() {
        DruidAbstractDataSource.startTransactionCountUpdater.incrementAndGet(this);
    }
    
    @Override
    public boolean isBreakAfterAcquireFailure() {
        return this.breakAfterAcquireFailure;
    }
    
    public void setBreakAfterAcquireFailure(final boolean breakAfterAcquireFailure) {
        this.breakAfterAcquireFailure = breakAfterAcquireFailure;
    }
    
    @Override
    public int getConnectionErrorRetryAttempts() {
        return this.connectionErrorRetryAttempts;
    }
    
    public void setConnectionErrorRetryAttempts(final int connectionErrorRetryAttempts) {
        this.connectionErrorRetryAttempts = connectionErrorRetryAttempts;
    }
    
    @Override
    public long getDupCloseCount() {
        return this.dupCloseCount;
    }
    
    @Override
    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return this.maxPoolPreparedStatementPerConnectionSize;
    }
    
    @Override
    public void setMaxPoolPreparedStatementPerConnectionSize(final int maxPoolPreparedStatementPerConnectionSize) {
        if (maxPoolPreparedStatementPerConnectionSize > 0) {
            this.poolPreparedStatements = true;
        }
        else {
            this.poolPreparedStatements = false;
        }
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }
    
    @Override
    public boolean isSharePreparedStatements() {
        return this.sharePreparedStatements;
    }
    
    public void setSharePreparedStatements(final boolean sharePreparedStatements) {
        this.sharePreparedStatements = sharePreparedStatements;
    }
    
    public void incrementDupCloseCount() {
        DruidAbstractDataSource.dupCloseCountUpdater.incrementAndGet(this);
    }
    
    public ValidConnectionChecker getValidConnectionChecker() {
        return this.validConnectionChecker;
    }
    
    public void setValidConnectionChecker(final ValidConnectionChecker validConnectionChecker) {
        this.validConnectionChecker = validConnectionChecker;
    }
    
    @Override
    public String getValidConnectionCheckerClassName() {
        if (this.validConnectionChecker == null) {
            return null;
        }
        return this.validConnectionChecker.getClass().getName();
    }
    
    public void setValidConnectionCheckerClassName(final String validConnectionCheckerClass) throws Exception {
        final Class<?> clazz = Utils.loadClass(validConnectionCheckerClass);
        ValidConnectionChecker validConnectionChecker = null;
        if (clazz != null) {
            validConnectionChecker = (ValidConnectionChecker)clazz.newInstance();
            this.validConnectionChecker = validConnectionChecker;
        }
        else {
            DruidAbstractDataSource.LOG.error("load validConnectionCheckerClass error : " + validConnectionCheckerClass);
        }
    }
    
    @Override
    public String getDbType() {
        return this.dbTypeName;
    }
    
    public void setDbType(final DbType dbType) {
        if (dbType == null) {
            this.dbTypeName = null;
        }
        else {
            this.dbTypeName = dbType.name();
        }
    }
    
    public void setDbType(final String dbType) {
        this.dbTypeName = dbType;
    }
    
    public void addConnectionProperty(final String name, final String value) {
        if (StringUtils.equals(this.connectProperties.getProperty(name), value)) {
            return;
        }
        if (this.inited) {
            throw new UnsupportedOperationException();
        }
        this.connectProperties.put(name, value);
    }
    
    public Collection<String> getConnectionInitSqls() {
        final Collection<String> result = this.connectionInitSqls;
        if (result == null) {
            return (Collection<String>)Collections.emptyList();
        }
        return result;
    }
    
    public void setConnectionInitSqls(final Collection<?> connectionInitSqls) {
        if (connectionInitSqls != null && connectionInitSqls.size() > 0) {
            ArrayList<String> newVal = null;
            for (final Object o : connectionInitSqls) {
                if (o == null) {
                    continue;
                }
                String s = o.toString();
                s = s.trim();
                if (s.length() == 0) {
                    continue;
                }
                if (newVal == null) {
                    newVal = new ArrayList<String>();
                }
                newVal.add(s);
            }
            this.connectionInitSqls = newVal;
        }
        else {
            this.connectionInitSqls = null;
        }
    }
    
    @Override
    public long getTimeBetweenConnectErrorMillis() {
        return this.timeBetweenConnectErrorMillis;
    }
    
    public void setTimeBetweenConnectErrorMillis(final long timeBetweenConnectErrorMillis) {
        this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
    }
    
    @Override
    public int getMaxOpenPreparedStatements() {
        return this.maxPoolPreparedStatementPerConnectionSize;
    }
    
    public void setMaxOpenPreparedStatements(final int maxOpenPreparedStatements) {
        this.setMaxPoolPreparedStatementPerConnectionSize(maxOpenPreparedStatements);
    }
    
    @Override
    public boolean isLogAbandoned() {
        return this.logAbandoned;
    }
    
    @Override
    public void setLogAbandoned(final boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }
    
    public int getRemoveAbandonedTimeout() {
        return (int)(this.removeAbandonedTimeoutMillis / 1000L);
    }
    
    public void setRemoveAbandonedTimeout(final int removeAbandonedTimeout) {
        this.removeAbandonedTimeoutMillis = removeAbandonedTimeout * 1000L;
    }
    
    public void setRemoveAbandonedTimeoutMillis(final long removeAbandonedTimeoutMillis) {
        this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
    }
    
    @Override
    public long getRemoveAbandonedTimeoutMillis() {
        return this.removeAbandonedTimeoutMillis;
    }
    
    @Override
    public boolean isRemoveAbandoned() {
        return this.removeAbandoned;
    }
    
    public void setRemoveAbandoned(final boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }
    
    @Override
    public long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }
    
    public void setMinEvictableIdleTimeMillis(final long minEvictableIdleTimeMillis) {
        if (minEvictableIdleTimeMillis < 30000L) {
            DruidAbstractDataSource.LOG.error("minEvictableIdleTimeMillis should be greater than 30000");
        }
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
    
    public long getKeepAliveBetweenTimeMillis() {
        return this.keepAliveBetweenTimeMillis;
    }
    
    public void setKeepAliveBetweenTimeMillis(final long keepAliveBetweenTimeMillis) {
        if (keepAliveBetweenTimeMillis < 30000L) {
            DruidAbstractDataSource.LOG.error("keepAliveBetweenTimeMillis should be greater than 30000");
        }
        if (keepAliveBetweenTimeMillis <= this.timeBetweenEvictionRunsMillis) {
            DruidAbstractDataSource.LOG.warn("keepAliveBetweenTimeMillis should be greater than timeBetweenEvictionRunsMillis");
        }
        this.keepAliveBetweenTimeMillis = keepAliveBetweenTimeMillis;
    }
    
    public long getMaxEvictableIdleTimeMillis() {
        return this.maxEvictableIdleTimeMillis;
    }
    
    public void setMaxEvictableIdleTimeMillis(final long maxEvictableIdleTimeMillis) {
        if (maxEvictableIdleTimeMillis < 30000L) {
            DruidAbstractDataSource.LOG.error("maxEvictableIdleTimeMillis should be greater than 30000");
        }
        if (this.inited && maxEvictableIdleTimeMillis < this.minEvictableIdleTimeMillis) {
            throw new IllegalArgumentException("maxEvictableIdleTimeMillis must be grater than minEvictableIdleTimeMillis");
        }
        this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
    }
    
    public long getPhyTimeoutMillis() {
        return this.phyTimeoutMillis;
    }
    
    public void setPhyTimeoutMillis(final long phyTimeoutMillis) {
        this.phyTimeoutMillis = phyTimeoutMillis;
    }
    
    public long getPhyMaxUseCount() {
        return this.phyMaxUseCount;
    }
    
    public void setPhyMaxUseCount(final long phyMaxUseCount) {
        this.phyMaxUseCount = phyMaxUseCount;
    }
    
    public int getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }
    
    @Deprecated
    public void setNumTestsPerEvictionRun(final int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }
    
    @Override
    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }
    
    public void setTimeBetweenEvictionRunsMillis(final long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
    
    @Override
    public int getMaxWaitThreadCount() {
        return this.maxWaitThreadCount;
    }
    
    public void setMaxWaitThreadCount(final int maxWaithThreadCount) {
        this.maxWaitThreadCount = maxWaithThreadCount;
    }
    
    @Override
    public String getValidationQuery() {
        return this.validationQuery;
    }
    
    public void setValidationQuery(final String validationQuery) {
        this.validationQuery = validationQuery;
    }
    
    @Override
    public int getValidationQueryTimeout() {
        return this.validationQueryTimeout;
    }
    
    public void setValidationQueryTimeout(final int validationQueryTimeout) {
        if (validationQueryTimeout < 0 && DbType.of(this.dbTypeName) == DbType.sqlserver) {
            DruidAbstractDataSource.LOG.error("validationQueryTimeout should be >= 0");
        }
        this.validationQueryTimeout = validationQueryTimeout;
    }
    
    public boolean isAccessToUnderlyingConnectionAllowed() {
        return this.accessToUnderlyingConnectionAllowed;
    }
    
    public void setAccessToUnderlyingConnectionAllowed(final boolean accessToUnderlyingConnectionAllowed) {
        this.accessToUnderlyingConnectionAllowed = accessToUnderlyingConnectionAllowed;
    }
    
    @Override
    public boolean isTestOnBorrow() {
        return this.testOnBorrow;
    }
    
    @Override
    public void setTestOnBorrow(final boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    
    @Override
    public boolean isTestOnReturn() {
        return this.testOnReturn;
    }
    
    public void setTestOnReturn(final boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    
    @Override
    public boolean isTestWhileIdle() {
        return this.testWhileIdle;
    }
    
    @Override
    public void setTestWhileIdle(final boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    
    @Override
    public boolean isDefaultAutoCommit() {
        return this.defaultAutoCommit;
    }
    
    public void setDefaultAutoCommit(final boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }
    
    @Override
    public Boolean getDefaultReadOnly() {
        return this.defaultReadOnly;
    }
    
    public void setDefaultReadOnly(final Boolean defaultReadOnly) {
        this.defaultReadOnly = defaultReadOnly;
    }
    
    @Override
    public Integer getDefaultTransactionIsolation() {
        return this.defaultTransactionIsolation;
    }
    
    public void setDefaultTransactionIsolation(final Integer defaultTransactionIsolation) {
        this.defaultTransactionIsolation = defaultTransactionIsolation;
    }
    
    @Override
    public String getDefaultCatalog() {
        return this.defaultCatalog;
    }
    
    public void setDefaultCatalog(final String defaultCatalog) {
        this.defaultCatalog = defaultCatalog;
    }
    
    public PasswordCallback getPasswordCallback() {
        return this.passwordCallback;
    }
    
    public void setPasswordCallback(final PasswordCallback passwordCallback) {
        this.passwordCallback = passwordCallback;
    }
    
    public void setPasswordCallbackClassName(final String passwordCallbackClassName) throws Exception {
        final Class<?> clazz = Utils.loadClass(passwordCallbackClassName);
        if (clazz != null) {
            this.passwordCallback = (PasswordCallback)clazz.newInstance();
        }
        else {
            DruidAbstractDataSource.LOG.error("load passwordCallback error : " + passwordCallbackClassName);
            this.passwordCallback = null;
        }
    }
    
    public NameCallback getUserCallback() {
        return this.userCallback;
    }
    
    public void setUserCallback(final NameCallback userCallback) {
        this.userCallback = userCallback;
    }
    
    public boolean isInitVariants() {
        return this.initVariants;
    }
    
    public void setInitVariants(final boolean initVariants) {
        this.initVariants = initVariants;
    }
    
    public boolean isInitGlobalVariants() {
        return this.initGlobalVariants;
    }
    
    public void setInitGlobalVariants(final boolean initGlobalVariants) {
        this.initGlobalVariants = initGlobalVariants;
    }
    
    @Override
    public int getQueryTimeout() {
        return this.queryTimeout;
    }
    
    public void setQueryTimeout(final int seconds) {
        this.queryTimeout = seconds;
    }
    
    @Override
    public String getName() {
        if (this.name != null) {
            return this.name;
        }
        return "DataSource-" + System.identityHashCode(this);
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean isPoolPreparedStatements() {
        return this.poolPreparedStatements;
    }
    
    public abstract void setPoolPreparedStatements(final boolean p0);
    
    @Override
    public long getMaxWait() {
        return this.maxWait;
    }
    
    public void setMaxWait(final long maxWaitMillis) {
        if (maxWaitMillis == this.maxWait) {
            return;
        }
        if (maxWaitMillis > 0L && this.useUnfairLock == null && !this.inited) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                if (!this.inited && !lock.isFair()) {
                    this.lock = new ReentrantLock(true);
                    this.notEmpty = this.lock.newCondition();
                    this.empty = this.lock.newCondition();
                }
            }
            finally {
                lock.unlock();
            }
        }
        if (this.inited) {
            DruidAbstractDataSource.LOG.error("maxWait changed : " + this.maxWait + " -> " + maxWaitMillis);
        }
        this.maxWait = maxWaitMillis;
    }
    
    public int getNotFullTimeoutRetryCount() {
        return this.notFullTimeoutRetryCount;
    }
    
    public void setNotFullTimeoutRetryCount(final int notFullTimeoutRetryCount) {
        this.notFullTimeoutRetryCount = notFullTimeoutRetryCount;
    }
    
    @Override
    public int getMinIdle() {
        return this.minIdle;
    }
    
    public void setMinIdle(final int value) {
        if (value == this.minIdle) {
            return;
        }
        if (this.inited && value > this.maxActive) {
            throw new IllegalArgumentException("minIdle greater than maxActive, " + this.maxActive + " < " + this.minIdle);
        }
        if (this.minIdle < 0) {
            throw new IllegalArgumentException("minIdle must > 0");
        }
        this.minIdle = value;
    }
    
    @Override
    public int getMaxIdle() {
        return this.maxIdle;
    }
    
    @Deprecated
    public void setMaxIdle(final int maxIdle) {
        DruidAbstractDataSource.LOG.error("maxIdle is deprecated");
        this.maxIdle = maxIdle;
    }
    
    @Override
    public int getInitialSize() {
        return this.initialSize;
    }
    
    public void setInitialSize(final int initialSize) {
        if (this.initialSize == initialSize) {
            return;
        }
        if (this.inited) {
            throw new UnsupportedOperationException();
        }
        this.initialSize = initialSize;
    }
    
    @Override
    public long getCreateErrorCount() {
        return this.createErrorCount;
    }
    
    @Override
    public int getMaxActive() {
        return this.maxActive;
    }
    
    @Override
    public abstract void setMaxActive(final int p0);
    
    @Override
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        if (StringUtils.equals(this.username, username)) {
            return;
        }
        if (this.inited) {
            throw new UnsupportedOperationException();
        }
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        if (StringUtils.equals(this.password, password)) {
            return;
        }
        if (this.inited) {
            DruidAbstractDataSource.LOG.info("password changed");
        }
        this.password = password;
    }
    
    @Override
    public Properties getConnectProperties() {
        return this.connectProperties;
    }
    
    public abstract void setConnectProperties(final Properties p0);
    
    public void setConnectionProperties(final String connectionProperties) {
        if (connectionProperties == null || connectionProperties.trim().length() == 0) {
            this.setConnectProperties(null);
            return;
        }
        final String[] entries = connectionProperties.split(";");
        final Properties properties = new Properties();
        for (int i = 0; i < entries.length; ++i) {
            final String entry = entries[i];
            if (entry.length() > 0) {
                final int index = entry.indexOf(61);
                if (index > 0) {
                    final String name = entry.substring(0, index);
                    final String value = entry.substring(index + 1);
                    properties.setProperty(name, value);
                }
                else {
                    properties.setProperty(entry, "");
                }
            }
        }
        this.setConnectProperties(properties);
    }
    
    @Override
    public String getUrl() {
        return this.jdbcUrl;
    }
    
    @Override
    public String getRawJdbcUrl() {
        return this.jdbcUrl;
    }
    
    public void setUrl(String jdbcUrl) {
        if (StringUtils.equals(this.jdbcUrl, jdbcUrl)) {
            return;
        }
        if (this.inited) {
            throw new UnsupportedOperationException();
        }
        if (jdbcUrl != null) {
            jdbcUrl = jdbcUrl.trim();
        }
        this.jdbcUrl = jdbcUrl;
    }
    
    @Override
    public String getDriverClassName() {
        return this.driverClass;
    }
    
    public void setDriverClassName(String driverClass) {
        if (driverClass != null && driverClass.length() > 256) {
            throw new IllegalArgumentException("driverClassName length > 256.");
        }
        if ("oracle.jdbc.driver.OracleDriver".equalsIgnoreCase(driverClass)) {
            driverClass = "oracle.jdbc.OracleDriver";
            DruidAbstractDataSource.LOG.warn("oracle.jdbc.driver.OracleDriver is deprecated.Having use oracle.jdbc.OracleDriver.");
        }
        if (!this.inited) {
            this.driverClass = driverClass;
            return;
        }
        if (StringUtils.equals(this.driverClass, driverClass)) {
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    public ClassLoader getDriverClassLoader() {
        return this.driverClassLoader;
    }
    
    public void setDriverClassLoader(final ClassLoader driverClassLoader) {
        this.driverClassLoader = driverClassLoader;
    }
    
    @Override
    public PrintWriter getLogWriter() {
        return this.logWriter;
    }
    
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        this.logWriter = out;
    }
    
    @Override
    public void setLoginTimeout(final int seconds) {
        DriverManager.setLoginTimeout(seconds);
    }
    
    @Override
    public int getLoginTimeout() {
        return DriverManager.getLoginTimeout();
    }
    
    public Driver getDriver() {
        return this.driver;
    }
    
    public void setDriver(final Driver driver) {
        this.driver = driver;
    }
    
    @Override
    public int getDriverMajorVersion() {
        if (this.driver == null) {
            return -1;
        }
        return this.driver.getMajorVersion();
    }
    
    @Override
    public int getDriverMinorVersion() {
        if (this.driver == null) {
            return -1;
        }
        return this.driver.getMinorVersion();
    }
    
    public ExceptionSorter getExceptionSorter() {
        return this.exceptionSorter;
    }
    
    @Override
    public String getExceptionSorterClassName() {
        if (this.exceptionSorter == null) {
            return null;
        }
        return this.exceptionSorter.getClass().getName();
    }
    
    public void setExceptionSorter(final ExceptionSorter exceptionSoter) {
        this.exceptionSorter = exceptionSoter;
    }
    
    public void setExceptionSorterClassName(final String exceptionSorter) throws Exception {
        this.setExceptionSorter(exceptionSorter);
    }
    
    public void setExceptionSorter(String exceptionSorter) throws SQLException {
        if (exceptionSorter == null) {
            this.exceptionSorter = NullExceptionSorter.getInstance();
            return;
        }
        exceptionSorter = exceptionSorter.trim();
        if (exceptionSorter.length() == 0) {
            this.exceptionSorter = NullExceptionSorter.getInstance();
            return;
        }
        final Class<?> clazz = Utils.loadClass(exceptionSorter);
        if (clazz == null) {
            DruidAbstractDataSource.LOG.error("load exceptionSorter error : " + exceptionSorter);
        }
        else {
            try {
                this.exceptionSorter = (ExceptionSorter)clazz.newInstance();
            }
            catch (Exception ex) {
                throw new SQLException("create exceptionSorter error", ex);
            }
        }
    }
    
    @Override
    public List<Filter> getProxyFilters() {
        return this.filters;
    }
    
    public void setProxyFilters(final List<Filter> filters) {
        if (filters != null) {
            this.filters.addAll(filters);
        }
    }
    
    public String[] getFilterClasses() {
        final List<Filter> filterConfigList = this.getProxyFilters();
        final List<String> classes = new ArrayList<String>();
        for (final Filter filter : filterConfigList) {
            classes.add(filter.getClass().getName());
        }
        return classes.toArray(new String[classes.size()]);
    }
    
    public void setFilters(String filters) throws SQLException {
        if (filters != null && filters.startsWith("!")) {
            filters = filters.substring(1);
            this.clearFilters();
        }
        this.addFilters(filters);
    }
    
    public void addFilters(final String filters) throws SQLException {
        if (filters == null || filters.length() == 0) {
            return;
        }
        final String[] split;
        final String[] filterArray = split = filters.split("\\,");
        for (final String item : split) {
            FilterManager.loadFilter(this.filters, item.trim());
        }
    }
    
    public void clearFilters() {
        if (!this.isClearFiltersEnable()) {
            return;
        }
        this.filters.clear();
    }
    
    public void validateConnection(final Connection conn) throws SQLException {
        final String query = this.getValidationQuery();
        if (conn.isClosed()) {
            throw new SQLException("validateConnection: connection closed");
        }
        if (this.validConnectionChecker == null) {
            if (null != query) {
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    stmt = conn.createStatement();
                    if (this.getValidationQueryTimeout() > 0) {
                        stmt.setQueryTimeout(this.getValidationQueryTimeout());
                    }
                    rs = stmt.executeQuery(query);
                    if (!rs.next()) {
                        throw new SQLException("validationQuery didn't return a row");
                    }
                    if (this.onFatalError) {
                        this.lock.lock();
                        try {
                            if (this.onFatalError) {
                                this.onFatalError = false;
                            }
                        }
                        finally {
                            this.lock.unlock();
                        }
                    }
                }
                finally {
                    JdbcUtils.close(rs);
                    JdbcUtils.close(stmt);
                }
            }
            return;
        }
        Exception error = null;
        boolean result;
        try {
            result = this.validConnectionChecker.isValidConnection(conn, this.validationQuery, this.validationQueryTimeout);
            if (result && this.onFatalError) {
                this.lock.lock();
                try {
                    if (this.onFatalError) {
                        this.onFatalError = false;
                    }
                }
                finally {
                    this.lock.unlock();
                }
            }
        }
        catch (SQLException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            result = false;
            error = ex2;
        }
        if (!result) {
            final SQLException sqlError = (error != null) ? new SQLException("validateConnection false", error) : new SQLException("validateConnection false");
            throw sqlError;
        }
    }
    
    @Deprecated
    protected boolean testConnectionInternal(final Connection conn) {
        return this.testConnectionInternal(null, conn);
    }
    
    protected boolean testConnectionInternal(final DruidConnectionHolder holder, final Connection conn) {
        final String sqlFile = JdbcSqlStat.getContextSqlFile();
        final String sqlName = JdbcSqlStat.getContextSqlName();
        if (sqlFile != null) {
            JdbcSqlStat.setContextSqlFile(null);
        }
        if (sqlName != null) {
            JdbcSqlStat.setContextSqlName(null);
        }
        try {
            if (this.validConnectionChecker != null) {
                final boolean valid = this.validConnectionChecker.isValidConnection(conn, this.validationQuery, this.validationQueryTimeout);
                final long currentTimeMillis = System.currentTimeMillis();
                if (holder != null) {
                    holder.lastValidTimeMillis = currentTimeMillis;
                    holder.lastExecTimeMillis = currentTimeMillis;
                }
                if (valid && this.isMySql) {
                    final long lastPacketReceivedTimeMs = MySqlUtils.getLastPacketReceivedTimeMs(conn);
                    if (lastPacketReceivedTimeMs > 0L) {
                        final long mysqlIdleMillis = currentTimeMillis - lastPacketReceivedTimeMs;
                        if (lastPacketReceivedTimeMs > 0L && mysqlIdleMillis >= this.timeBetweenEvictionRunsMillis) {
                            this.discardConnection(holder);
                            final String errorMsg = "discard long time none received connection. , jdbcUrl : " + this.jdbcUrl + ", version : " + VERSION.getVersionNumber() + ", lastPacketReceivedIdleMillis : " + mysqlIdleMillis;
                            DruidAbstractDataSource.LOG.warn(errorMsg);
                            return false;
                        }
                    }
                }
                if (valid && this.onFatalError) {
                    this.lock.lock();
                    try {
                        if (this.onFatalError) {
                            this.onFatalError = false;
                        }
                    }
                    finally {
                        this.lock.unlock();
                    }
                }
                return valid;
            }
            if (conn.isClosed()) {
                return false;
            }
            if (null == this.validationQuery) {
                return true;
            }
            Statement stmt = null;
            ResultSet rset = null;
            try {
                stmt = conn.createStatement();
                if (this.getValidationQueryTimeout() > 0) {
                    stmt.setQueryTimeout(this.validationQueryTimeout);
                }
                rset = stmt.executeQuery(this.validationQuery);
                if (!rset.next()) {
                    return false;
                }
            }
            finally {
                JdbcUtils.close(rset);
                JdbcUtils.close(stmt);
            }
            if (this.onFatalError) {
                this.lock.lock();
                try {
                    if (this.onFatalError) {
                        this.onFatalError = false;
                    }
                }
                finally {
                    this.lock.unlock();
                }
            }
            return true;
        }
        catch (Throwable ex) {
            return false;
        }
        finally {
            if (sqlFile != null) {
                JdbcSqlStat.setContextSqlFile(sqlFile);
            }
            if (sqlName != null) {
                JdbcSqlStat.setContextSqlName(sqlName);
            }
        }
    }
    
    public Set<DruidPooledConnection> getActiveConnections() {
        this.activeConnectionLock.lock();
        try {
            return new HashSet<DruidPooledConnection>(this.activeConnections.keySet());
        }
        finally {
            this.activeConnectionLock.unlock();
        }
    }
    
    @Override
    public List<String> getActiveConnectionStackTrace() {
        final List<String> list = new ArrayList<String>();
        for (final DruidPooledConnection conn : this.getActiveConnections()) {
            list.add(Utils.toString(conn.getConnectStackTrace()));
        }
        return list;
    }
    
    public long getCreateTimespanNano() {
        return this.createTimespan;
    }
    
    @Override
    public long getCreateTimespanMillis() {
        return this.createTimespan / 1000000L;
    }
    
    @Override
    public Driver getRawDriver() {
        return this.driver;
    }
    
    public boolean isClearFiltersEnable() {
        return this.clearFiltersEnable;
    }
    
    public void setClearFiltersEnable(final boolean clearFiltersEnable) {
        this.clearFiltersEnable = clearFiltersEnable;
    }
    
    @Override
    public long createConnectionId() {
        return DruidAbstractDataSource.connectionIdSeedUpdater.incrementAndGet(this);
    }
    
    @Override
    public long createStatementId() {
        return DruidAbstractDataSource.statementIdSeedUpdater.getAndIncrement(this);
    }
    
    @Override
    public long createMetaDataId() {
        return DruidAbstractDataSource.metaDataIdSeedUpdater.getAndIncrement(this);
    }
    
    @Override
    public long createResultSetId() {
        return DruidAbstractDataSource.resultSetIdSeedUpdater.getAndIncrement(this);
    }
    
    @Override
    public long createTransactionId() {
        return DruidAbstractDataSource.transactionIdSeedUpdater.getAndIncrement(this);
    }
    
    void initStatement(final DruidPooledConnection conn, final Statement stmt) throws SQLException {
        final boolean transaction = !conn.getConnectionHolder().underlyingAutoCommit;
        final int queryTimeout = transaction ? this.getTransactionQueryTimeout() : this.getQueryTimeout();
        if (queryTimeout > 0) {
            stmt.setQueryTimeout(queryTimeout);
        }
    }
    
    public void handleConnectionException(final DruidPooledConnection conn, final Throwable t) throws SQLException {
        this.handleConnectionException(conn, t, null);
    }
    
    public abstract void handleConnectionException(final DruidPooledConnection p0, final Throwable p1, final String p2) throws SQLException;
    
    protected abstract void recycle(final DruidPooledConnection p0) throws SQLException;
    
    public Connection createPhysicalConnection(final String url, final Properties info) throws SQLException {
        Connection conn;
        if (this.getProxyFilters().size() == 0) {
            conn = this.getDriver().connect(url, info);
        }
        else {
            conn = new FilterChainImpl(this).connection_connect(info);
        }
        DruidAbstractDataSource.createCountUpdater.incrementAndGet(this);
        return conn;
    }
    
    public PhysicalConnectionInfo createPhysicalConnection() throws SQLException {
        final String url = this.getUrl();
        final Properties connectProperties = this.getConnectProperties();
        String user;
        if (this.getUserCallback() != null) {
            user = this.getUserCallback().getName();
        }
        else {
            user = this.getUsername();
        }
        String password = this.getPassword();
        final PasswordCallback passwordCallback = this.getPasswordCallback();
        if (passwordCallback != null) {
            if (passwordCallback instanceof DruidPasswordCallback) {
                final DruidPasswordCallback druidPasswordCallback = (DruidPasswordCallback)passwordCallback;
                druidPasswordCallback.setUrl(url);
                druidPasswordCallback.setProperties(connectProperties);
            }
            final char[] chars = passwordCallback.getPassword();
            if (chars != null) {
                password = new String(chars);
            }
        }
        final Properties physicalConnectProperties = new Properties();
        if (connectProperties != null) {
            physicalConnectProperties.putAll(connectProperties);
        }
        if (user != null && user.length() != 0) {
            physicalConnectProperties.put("user", user);
        }
        if (password != null && password.length() != 0) {
            physicalConnectProperties.put("password", password);
        }
        Connection conn = null;
        final long connectStartNanos = System.nanoTime();
        final Map<String, Object> variables = this.initVariants ? new HashMap<String, Object>() : null;
        final Map<String, Object> globalVariables = this.initGlobalVariants ? new HashMap<String, Object>() : null;
        DruidAbstractDataSource.createStartNanosUpdater.set(this, connectStartNanos);
        DruidAbstractDataSource.creatingCountUpdater.incrementAndGet(this);
        long connectedNanos;
        long initedNanos;
        long validatedNanos;
        try {
            conn = this.createPhysicalConnection(url, physicalConnectProperties);
            connectedNanos = System.nanoTime();
            if (conn == null) {
                throw new SQLException("connect error, url " + url + ", driverClass " + this.driverClass);
            }
            this.initPhysicalConnection(conn, variables, globalVariables);
            initedNanos = System.nanoTime();
            this.validateConnection(conn);
            validatedNanos = System.nanoTime();
            this.setFailContinuous(false);
            this.setCreateError(null);
            return new PhysicalConnectionInfo(conn, connectStartNanos, connectedNanos, initedNanos, validatedNanos, variables, globalVariables);
        }
        catch (SQLException ex) {
            this.setCreateError(ex);
            JdbcUtils.close(conn);
            throw ex;
        }
        catch (RuntimeException ex2) {
            this.setCreateError(ex2);
            JdbcUtils.close(conn);
            throw ex2;
        }
        catch (Error ex3) {
            DruidAbstractDataSource.createErrorCountUpdater.incrementAndGet(this);
            this.setCreateError(ex3);
            JdbcUtils.close(conn);
            throw ex3;
        }
        finally {
            final long nano = System.nanoTime() - connectStartNanos;
            this.createTimespan += nano;
            DruidAbstractDataSource.creatingCountUpdater.decrementAndGet(this);
        }
        return new PhysicalConnectionInfo(conn, connectStartNanos, connectedNanos, initedNanos, validatedNanos, variables, globalVariables);
    }
    
    protected void setCreateError(final Throwable ex) {
        if (ex == null) {
            this.lock.lock();
            try {
                if (this.createError != null) {
                    this.createError = null;
                }
            }
            finally {
                this.lock.unlock();
            }
            return;
        }
        DruidAbstractDataSource.createErrorCountUpdater.incrementAndGet(this);
        final long now = System.currentTimeMillis();
        this.lock.lock();
        try {
            this.createError = ex;
            this.lastCreateError = ex;
            this.lastCreateErrorTimeMillis = now;
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public boolean isFailContinuous() {
        return DruidAbstractDataSource.failContinuousUpdater.get(this) == 1;
    }
    
    protected void setFailContinuous(final boolean fail) {
        if (fail) {
            DruidAbstractDataSource.failContinuousTimeMillisUpdater.set(this, System.currentTimeMillis());
        }
        else {
            DruidAbstractDataSource.failContinuousTimeMillisUpdater.set(this, 0L);
        }
        final boolean currentState = DruidAbstractDataSource.failContinuousUpdater.get(this) == 1;
        if (currentState == fail) {
            return;
        }
        if (fail) {
            DruidAbstractDataSource.failContinuousUpdater.set(this, 1);
            if (DruidAbstractDataSource.LOG.isInfoEnabled()) {
                DruidAbstractDataSource.LOG.info("{dataSource-" + this.getID() + "} failContinuous is true");
            }
        }
        else {
            DruidAbstractDataSource.failContinuousUpdater.set(this, 0);
            if (DruidAbstractDataSource.LOG.isInfoEnabled()) {
                DruidAbstractDataSource.LOG.info("{dataSource-" + this.getID() + "} failContinuous is false");
            }
        }
    }
    
    public void initPhysicalConnection(final Connection conn) throws SQLException {
        this.initPhysicalConnection(conn, null, null);
    }
    
    public void initPhysicalConnection(final Connection conn, final Map<String, Object> variables, final Map<String, Object> globalVariables) throws SQLException {
        if (conn.getAutoCommit() != this.defaultAutoCommit) {
            conn.setAutoCommit(this.defaultAutoCommit);
        }
        if (this.defaultReadOnly != null && conn.isReadOnly() != this.defaultReadOnly) {
            conn.setReadOnly(this.defaultReadOnly);
        }
        if (this.getDefaultTransactionIsolation() != null && conn.getTransactionIsolation() != this.getDefaultTransactionIsolation()) {
            conn.setTransactionIsolation(this.getDefaultTransactionIsolation());
        }
        if (this.getDefaultCatalog() != null && this.getDefaultCatalog().length() != 0) {
            conn.setCatalog(this.getDefaultCatalog());
        }
        final Collection<String> initSqls = this.getConnectionInitSqls();
        if (initSqls.size() == 0 && variables == null && globalVariables == null) {
            return;
        }
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for (final String sql : initSqls) {
                if (sql == null) {
                    continue;
                }
                stmt.execute(sql);
            }
            final DbType dbType = DbType.of(this.dbTypeName);
            if (dbType == DbType.mysql || dbType == DbType.ads) {
                if (variables != null) {
                    ResultSet rs = null;
                    try {
                        rs = stmt.executeQuery("show variables");
                        while (rs.next()) {
                            final String name = rs.getString(1);
                            final Object value = rs.getObject(2);
                            variables.put(name, value);
                        }
                    }
                    finally {
                        JdbcUtils.close(rs);
                    }
                }
                if (globalVariables != null) {
                    ResultSet rs = null;
                    try {
                        rs = stmt.executeQuery("show global variables");
                        while (rs.next()) {
                            final String name = rs.getString(1);
                            final Object value = rs.getObject(2);
                            globalVariables.put(name, value);
                        }
                    }
                    finally {
                        JdbcUtils.close(rs);
                    }
                }
            }
        }
        finally {
            JdbcUtils.close(stmt);
        }
    }
    
    public abstract int getActivePeak();
    
    public CompositeDataSupport getCompositeData() throws JMException {
        final JdbcDataSourceStat stat = this.getDataSourceStat();
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", this.getID());
        map.put("URL", this.getUrl());
        map.put("Name", this.getName());
        map.put("FilterClasses", this.getFilterClasses());
        map.put("CreatedTime", this.getCreatedTime());
        map.put("RawDriverClassName", this.getDriverClassName());
        map.put("RawUrl", this.getUrl());
        map.put("RawDriverMajorVersion", this.getRawDriverMajorVersion());
        map.put("RawDriverMinorVersion", this.getRawDriverMinorVersion());
        map.put("Properties", this.getProperties());
        map.put("ConnectionActiveCount", (long)this.getActiveCount());
        map.put("ConnectionActiveCountMax", this.getActivePeak());
        map.put("ConnectionCloseCount", this.getCloseCount());
        map.put("ConnectionCommitCount", this.getCommitCount());
        map.put("ConnectionRollbackCount", this.getRollbackCount());
        map.put("ConnectionConnectLastTime", stat.getConnectionStat().getConnectLastTime());
        map.put("ConnectionConnectErrorCount", this.getCreateCount());
        if (this.createError != null) {
            map.put("ConnectionConnectErrorLastTime", this.getLastCreateErrorTime());
            map.put("ConnectionConnectErrorLastMessage", this.createError.getMessage());
            map.put("ConnectionConnectErrorLastStackTrace", Utils.getStackTrace(this.createError));
        }
        else {
            map.put("ConnectionConnectErrorLastTime", null);
            map.put("ConnectionConnectErrorLastMessage", null);
            map.put("ConnectionConnectErrorLastStackTrace", null);
        }
        map.put("StatementCreateCount", stat.getStatementStat().getCreateCount());
        map.put("StatementPrepareCount", stat.getStatementStat().getPrepareCount());
        map.put("StatementPreCallCount", stat.getStatementStat().getPrepareCallCount());
        map.put("StatementExecuteCount", stat.getStatementStat().getExecuteCount());
        map.put("StatementRunningCount", stat.getStatementStat().getRunningCount());
        map.put("StatementConcurrentMax", stat.getStatementStat().getConcurrentMax());
        map.put("StatementCloseCount", stat.getStatementStat().getCloseCount());
        map.put("StatementErrorCount", stat.getStatementStat().getErrorCount());
        map.put("StatementLastErrorTime", null);
        map.put("StatementLastErrorMessage", null);
        map.put("StatementLastErrorStackTrace", null);
        map.put("StatementExecuteMillisTotal", stat.getStatementStat().getMillisTotal());
        map.put("StatementExecuteLastTime", stat.getStatementStat().getExecuteLastTime());
        map.put("ConnectionConnectingCount", stat.getConnectionStat().getConnectingCount());
        map.put("ResultSetCloseCount", stat.getResultSetStat().getCloseCount());
        map.put("ResultSetOpenCount", stat.getResultSetStat().getOpenCount());
        map.put("ResultSetOpenningCount", stat.getResultSetStat().getOpeningCount());
        map.put("ResultSetOpenningMax", stat.getResultSetStat().getOpeningMax());
        map.put("ResultSetFetchRowCount", stat.getResultSetStat().getFetchRowCount());
        map.put("ResultSetLastOpenTime", stat.getResultSetStat().getLastOpenTime());
        map.put("ResultSetErrorCount", stat.getResultSetStat().getErrorCount());
        map.put("ResultSetOpenningMillisTotal", stat.getResultSetStat().getAliveMillisTotal());
        map.put("ResultSetLastErrorTime", stat.getResultSetStat().getLastErrorTime());
        map.put("ResultSetLastErrorMessage", null);
        map.put("ResultSetLastErrorStackTrace", null);
        map.put("ConnectionConnectCount", this.getConnectCount());
        if (this.createError != null) {
            map.put("ConnectionErrorLastMessage", this.createError.getMessage());
            map.put("ConnectionErrorLastStackTrace", Utils.getStackTrace(this.createError));
        }
        else {
            map.put("ConnectionErrorLastMessage", null);
            map.put("ConnectionErrorLastStackTrace", null);
        }
        map.put("ConnectionConnectMillisTotal", stat.getConnectionStat().getConnectMillis());
        map.put("ConnectionConnectingCountMax", stat.getConnectionStat().getConnectingMax());
        map.put("ConnectionConnectMillisMax", stat.getConnectionStat().getConnectMillisMax());
        map.put("ConnectionErrorLastTime", stat.getConnectionStat().getErrorLastTime());
        map.put("ConnectionAliveMillisMax", stat.getConnectionConnectAliveMillisMax());
        map.put("ConnectionAliveMillisMin", stat.getConnectionConnectAliveMillisMin());
        map.put("ConnectionHistogram", stat.getConnectionHistogramValues());
        map.put("StatementHistogram", stat.getStatementStat().getHistogramValues());
        return new CompositeDataSupport(JdbcStatManager.getDataSourceCompositeType(), map);
    }
    
    public long getID() {
        return this.id;
    }
    
    @Override
    public Date getCreatedTime() {
        return this.createdTime;
    }
    
    @Override
    public abstract int getRawDriverMajorVersion();
    
    @Override
    public abstract int getRawDriverMinorVersion();
    
    @Override
    public abstract String getProperties();
    
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
    
    public void closePreapredStatement(final PreparedStatementHolder stmtHolder) {
        if (stmtHolder == null) {
            return;
        }
        DruidAbstractDataSource.closedPreparedStatementCountUpdater.incrementAndGet(this);
        this.decrementCachedPreparedStatementCount();
        this.incrementCachedPreparedStatementDeleteCount();
        JdbcUtils.close(stmtHolder.statement);
    }
    
    protected void cloneTo(final DruidAbstractDataSource to) {
        to.defaultAutoCommit = this.defaultAutoCommit;
        to.defaultReadOnly = this.defaultReadOnly;
        to.defaultTransactionIsolation = this.defaultTransactionIsolation;
        to.defaultCatalog = this.defaultCatalog;
        to.name = this.name;
        to.username = this.username;
        to.password = this.password;
        to.jdbcUrl = this.jdbcUrl;
        to.driverClass = this.driverClass;
        to.connectProperties = this.connectProperties;
        to.passwordCallback = this.passwordCallback;
        to.userCallback = this.userCallback;
        to.initialSize = this.initialSize;
        to.maxActive = this.maxActive;
        to.minIdle = this.minIdle;
        to.maxIdle = this.maxIdle;
        to.maxWait = this.maxWait;
        to.validationQuery = this.validationQuery;
        to.validationQueryTimeout = this.validationQueryTimeout;
        to.testOnBorrow = this.testOnBorrow;
        to.testOnReturn = this.testOnReturn;
        to.testWhileIdle = this.testWhileIdle;
        to.poolPreparedStatements = this.poolPreparedStatements;
        to.sharePreparedStatements = this.sharePreparedStatements;
        to.maxPoolPreparedStatementPerConnectionSize = this.maxPoolPreparedStatementPerConnectionSize;
        to.logWriter = this.logWriter;
        if (this.filters != null) {
            to.filters = new ArrayList<Filter>(this.filters);
        }
        to.exceptionSorter = this.exceptionSorter;
        to.driver = this.driver;
        to.queryTimeout = this.queryTimeout;
        to.transactionQueryTimeout = this.transactionQueryTimeout;
        to.accessToUnderlyingConnectionAllowed = this.accessToUnderlyingConnectionAllowed;
        to.timeBetweenEvictionRunsMillis = this.timeBetweenEvictionRunsMillis;
        to.numTestsPerEvictionRun = this.numTestsPerEvictionRun;
        to.minEvictableIdleTimeMillis = this.minEvictableIdleTimeMillis;
        to.removeAbandoned = this.removeAbandoned;
        to.removeAbandonedTimeoutMillis = this.removeAbandonedTimeoutMillis;
        to.logAbandoned = this.logAbandoned;
        to.maxOpenPreparedStatements = this.maxOpenPreparedStatements;
        if (this.connectionInitSqls != null) {
            to.connectionInitSqls = new ArrayList<String>(this.connectionInitSqls);
        }
        to.dbTypeName = this.dbTypeName;
        to.timeBetweenConnectErrorMillis = this.timeBetweenConnectErrorMillis;
        to.validConnectionChecker = this.validConnectionChecker;
        to.connectionErrorRetryAttempts = this.connectionErrorRetryAttempts;
        to.breakAfterAcquireFailure = this.breakAfterAcquireFailure;
        to.transactionThresholdMillis = this.transactionThresholdMillis;
        to.dupCloseLogEnable = this.dupCloseLogEnable;
        to.isOracle = this.isOracle;
        to.useOracleImplicitCache = this.useOracleImplicitCache;
        to.asyncCloseConnectionEnable = this.asyncCloseConnectionEnable;
        to.createScheduler = this.createScheduler;
        to.destroyScheduler = this.destroyScheduler;
    }
    
    public abstract void discardConnection(final Connection p0);
    
    public void discardConnection(final DruidConnectionHolder holder) {
        this.discardConnection(holder.getConnection());
    }
    
    public boolean isAsyncCloseConnectionEnable() {
        return this.isRemoveAbandoned() || this.asyncCloseConnectionEnable;
    }
    
    public void setAsyncCloseConnectionEnable(final boolean asyncCloseConnectionEnable) {
        this.asyncCloseConnectionEnable = asyncCloseConnectionEnable;
    }
    
    public ScheduledExecutorService getCreateScheduler() {
        return this.createScheduler;
    }
    
    public void setCreateScheduler(final ScheduledExecutorService createScheduler) {
        if (this.isInited()) {
            throw new DruidRuntimeException("dataSource inited.");
        }
        this.createScheduler = createScheduler;
    }
    
    public ScheduledExecutorService getDestroyScheduler() {
        return this.destroyScheduler;
    }
    
    public void setDestroyScheduler(final ScheduledExecutorService destroyScheduler) {
        if (this.isInited()) {
            throw new DruidRuntimeException("dataSource inited.");
        }
        this.destroyScheduler = destroyScheduler;
    }
    
    public boolean isInited() {
        return this.inited;
    }
    
    public int getMaxCreateTaskCount() {
        return this.maxCreateTaskCount;
    }
    
    public void setMaxCreateTaskCount(final int maxCreateTaskCount) {
        if (maxCreateTaskCount < 1) {
            throw new IllegalArgumentException();
        }
        this.maxCreateTaskCount = maxCreateTaskCount;
    }
    
    public boolean isFailFast() {
        return this.failFast;
    }
    
    public void setFailFast(final boolean failFast) {
        this.failFast = failFast;
    }
    
    public int getOnFatalErrorMaxActive() {
        return this.onFatalErrorMaxActive;
    }
    
    public void setOnFatalErrorMaxActive(final int onFatalErrorMaxActive) {
        this.onFatalErrorMaxActive = onFatalErrorMaxActive;
    }
    
    public boolean isOnFatalError() {
        return this.onFatalError;
    }
    
    public boolean isInitExceptionThrow() {
        return this.initExceptionThrow;
    }
    
    public void setInitExceptionThrow(final boolean initExceptionThrow) {
        this.initExceptionThrow = initExceptionThrow;
    }
    
    static {
        LOG = LogFactory.getLog(DruidAbstractDataSource.class);
        DEFAULT_VALIDATION_QUERY = null;
        PRESENT = new Object();
        errorCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "errorCount");
        dupCloseCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "dupCloseCount");
        startTransactionCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "startTransactionCount");
        commitCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "commitCount");
        rollbackCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "rollbackCount");
        cachedPreparedStatementHitCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "cachedPreparedStatementHitCount");
        preparedStatementCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "preparedStatementCount");
        closedPreparedStatementCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "closedPreparedStatementCount");
        cachedPreparedStatementCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "cachedPreparedStatementCount");
        cachedPreparedStatementDeleteCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "cachedPreparedStatementDeleteCount");
        cachedPreparedStatementMissCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "cachedPreparedStatementMissCount");
        executeQueryCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "executeQueryCount");
        executeUpdateCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "executeUpdateCount");
        executeBatchCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "executeBatchCount");
        executeCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "executeCount");
        createErrorCountUpdater = AtomicIntegerFieldUpdater.newUpdater(DruidAbstractDataSource.class, "createErrorCount");
        creatingCountUpdater = AtomicIntegerFieldUpdater.newUpdater(DruidAbstractDataSource.class, "creatingCount");
        directCreateCountUpdater = AtomicIntegerFieldUpdater.newUpdater(DruidAbstractDataSource.class, "directCreateCount");
        createCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "createCount");
        destroyCountUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "destroyCount");
        createStartNanosUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "createStartNanos");
        failContinuousTimeMillisUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "failContinuousTimeMillis");
        failContinuousUpdater = AtomicIntegerFieldUpdater.newUpdater(DruidAbstractDataSource.class, "failContinuous");
        connectionIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "connectionIdSeed");
        statementIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "statementIdSeed");
        resultSetIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "resultSetIdSeed");
        transactionIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "transactionIdSeed");
        metaDataIdSeedUpdater = AtomicLongFieldUpdater.newUpdater(DruidAbstractDataSource.class, "metaDataIdSeed");
    }
    
    public static class PhysicalConnectionInfo
    {
        private Connection connection;
        private long connectStartNanos;
        private long connectedNanos;
        private long initedNanos;
        private long validatedNanos;
        private Map<String, Object> vairiables;
        private Map<String, Object> globalVairiables;
        long createTaskId;
        
        public PhysicalConnectionInfo(final Connection connection, final long connectStartNanos, final long connectedNanos, final long initedNanos, final long validatedNanos) {
            this(connection, connectStartNanos, connectedNanos, initedNanos, validatedNanos, null, null);
        }
        
        public PhysicalConnectionInfo(final Connection connection, final long connectStartNanos, final long connectedNanos, final long initedNanos, final long validatedNanos, final Map<String, Object> vairiables, final Map<String, Object> globalVairiables) {
            this.connection = connection;
            this.connectStartNanos = connectStartNanos;
            this.connectedNanos = connectedNanos;
            this.initedNanos = initedNanos;
            this.validatedNanos = validatedNanos;
            this.vairiables = vairiables;
            this.globalVairiables = globalVairiables;
        }
        
        public Connection getPhysicalConnection() {
            return this.connection;
        }
        
        public long getConnectStartNanos() {
            return this.connectStartNanos;
        }
        
        public long getConnectedNanos() {
            return this.connectedNanos;
        }
        
        public long getInitedNanos() {
            return this.initedNanos;
        }
        
        public long getValidatedNanos() {
            return this.validatedNanos;
        }
        
        public long getConnectNanoSpan() {
            return this.connectedNanos - this.connectStartNanos;
        }
        
        public Map<String, Object> getVairiables() {
            return this.vairiables;
        }
        
        public Map<String, Object> getGlobalVairiables() {
            return this.globalVairiables;
        }
    }
}
