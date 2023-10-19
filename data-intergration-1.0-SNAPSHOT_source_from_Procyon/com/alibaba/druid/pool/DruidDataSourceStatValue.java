// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.util.Date;
import com.alibaba.druid.stat.JdbcSqlStatValue;
import java.util.List;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import com.alibaba.druid.support.monitor.annotation.MField;
import com.alibaba.druid.support.monitor.annotation.MTable;

@MTable(name = "druid_datasource")
public class DruidDataSourceStatValue
{
    @MField(groupBy = true, aggregate = AggregateType.None)
    protected String name;
    @MField(aggregate = AggregateType.None)
    protected String dbType;
    @MField(aggregate = AggregateType.None)
    protected String driverClassName;
    protected String url;
    protected String userName;
    protected List<String> filterClassNames;
    protected boolean removeAbandoned;
    protected int initialSize;
    protected int minIdle;
    protected int maxActive;
    protected int queryTimeout;
    protected int transactionQueryTimeout;
    protected int loginTimeout;
    protected String validConnectionCheckerClassName;
    protected String exceptionSorterClassName;
    protected boolean testOnBorrow;
    protected boolean testOnReturn;
    protected boolean testWhileIdle;
    protected boolean defaultAutoCommit;
    protected boolean defaultReadOnly;
    protected Integer defaultTransactionIsolation;
    @MField(aggregate = AggregateType.Last)
    protected int activeCount;
    @MField(aggregate = AggregateType.Max)
    protected int activePeak;
    @MField(aggregate = AggregateType.Max)
    protected long activePeakTime;
    @MField(aggregate = AggregateType.Last)
    protected int poolingCount;
    @MField(aggregate = AggregateType.Max)
    protected int poolingPeak;
    @MField(aggregate = AggregateType.Max)
    protected long poolingPeakTime;
    @MField(aggregate = AggregateType.Sum)
    protected long connectCount;
    @MField(aggregate = AggregateType.Sum)
    protected long closeCount;
    @MField(aggregate = AggregateType.Sum)
    protected long waitThreadCount;
    @MField(aggregate = AggregateType.Sum)
    protected long notEmptyWaitCount;
    @MField(aggregate = AggregateType.Sum)
    protected long notEmptyWaitNanos;
    @MField(aggregate = AggregateType.Sum)
    protected long logicConnectErrorCount;
    @MField(aggregate = AggregateType.Sum)
    protected long physicalConnectCount;
    @MField(aggregate = AggregateType.Sum)
    protected long physicalCloseCount;
    @MField(aggregate = AggregateType.Sum)
    protected long physicalConnectErrorCount;
    @MField(aggregate = AggregateType.Sum)
    protected long executeCount;
    @MField(aggregate = AggregateType.Sum)
    protected long errorCount;
    @MField(aggregate = AggregateType.Sum)
    protected long commitCount;
    @MField(aggregate = AggregateType.Sum)
    protected long rollbackCount;
    @MField(aggregate = AggregateType.Sum)
    protected long pstmtCacheHitCount;
    @MField(aggregate = AggregateType.Sum)
    protected long pstmtCacheMissCount;
    @MField(aggregate = AggregateType.Sum)
    protected long startTransactionCount;
    @MField(aggregate = AggregateType.Sum)
    protected long keepAliveCheckCount;
    protected long[] connectionHoldTimeHistogram;
    @MField(aggregate = AggregateType.Sum)
    protected long txn_0_1;
    @MField(aggregate = AggregateType.Sum)
    protected long txn_1_10;
    @MField(aggregate = AggregateType.Sum)
    protected long txn_10_100;
    @MField(aggregate = AggregateType.Sum)
    protected long txn_100_1000;
    @MField(aggregate = AggregateType.Sum)
    protected long txn_1000_10000;
    @MField(aggregate = AggregateType.Sum)
    protected long txn_10000_100000;
    @MField(aggregate = AggregateType.Sum)
    protected long txn_more;
    @MField(aggregate = AggregateType.Sum)
    protected long clobOpenCount;
    @MField(aggregate = AggregateType.Sum)
    protected long blobOpenCount;
    @MField(aggregate = AggregateType.Sum)
    protected long sqlSkipCount;
    protected List<JdbcSqlStatValue> sqlList;
    
    public Date getPoolingPeakTime() {
        if (this.poolingPeakTime <= 0L) {
            return null;
        }
        return new Date(this.poolingPeakTime);
    }
    
    public long getSqlSkipCount() {
        return this.sqlSkipCount;
    }
    
    public void setSqlSkipCount(final long sqlSkipCount) {
        this.sqlSkipCount = sqlSkipCount;
    }
    
    public Date getActivePeakTime() {
        if (this.activePeakTime <= 0L) {
            return null;
        }
        return new Date(this.activePeakTime);
    }
    
    public long getNotEmptyWaitMillis() {
        return this.notEmptyWaitNanos / 1000000L;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final String dbType) {
        this.dbType = dbType;
    }
    
    public String getDriverClassName() {
        return this.driverClassName;
    }
    
    public void setDriverClassName(final String driverClassName) {
        this.driverClassName = driverClassName;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public List<String> getFilterClassNames() {
        return this.filterClassNames;
    }
    
    public void setFilterClassNames(final List<String> filterClassNames) {
        this.filterClassNames = filterClassNames;
    }
    
    public boolean isRemoveAbandoned() {
        return this.removeAbandoned;
    }
    
    public void setRemoveAbandoned(final boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }
    
    public int getInitialSize() {
        return this.initialSize;
    }
    
    public void setInitialSize(final int initialSize) {
        this.initialSize = initialSize;
    }
    
    public int getMinIdle() {
        return this.minIdle;
    }
    
    public void setMinIdle(final int minIdle) {
        this.minIdle = minIdle;
    }
    
    public int getMaxActive() {
        return this.maxActive;
    }
    
    public void setMaxActive(final int maxActive) {
        this.maxActive = maxActive;
    }
    
    public int getQueryTimeout() {
        return this.queryTimeout;
    }
    
    public void setQueryTimeout(final int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }
    
    public int getTransactionQueryTimeout() {
        return this.transactionQueryTimeout;
    }
    
    public void setTransactionQueryTimeout(final int transactionQueryTimeout) {
        this.transactionQueryTimeout = transactionQueryTimeout;
    }
    
    public int getLoginTimeout() {
        return this.loginTimeout;
    }
    
    public void setLoginTimeout(final int loginTimeout) {
        this.loginTimeout = loginTimeout;
    }
    
    public String getValidConnectionCheckerClassName() {
        return this.validConnectionCheckerClassName;
    }
    
    public void setValidConnectionCheckerClassName(final String validConnectionCheckerClassName) {
        this.validConnectionCheckerClassName = validConnectionCheckerClassName;
    }
    
    public String getExceptionSorterClassName() {
        return this.exceptionSorterClassName;
    }
    
    public void setExceptionSorterClassName(final String exceptionSorterClassName) {
        this.exceptionSorterClassName = exceptionSorterClassName;
    }
    
    public boolean isTestOnBorrow() {
        return this.testOnBorrow;
    }
    
    public void setTestOnBorrow(final boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    
    public boolean isTestOnReturn() {
        return this.testOnReturn;
    }
    
    public void setTestOnReturn(final boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    
    public boolean isTestWhileIdle() {
        return this.testWhileIdle;
    }
    
    public void setTestWhileIdle(final boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    
    public boolean isDefaultAutoCommit() {
        return this.defaultAutoCommit;
    }
    
    public void setDefaultAutoCommit(final boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }
    
    public boolean isDefaultReadOnly() {
        return this.defaultReadOnly;
    }
    
    public void setDefaultReadOnly(final boolean defaultReadOnly) {
        this.defaultReadOnly = defaultReadOnly;
    }
    
    public Integer getDefaultTransactionIsolation() {
        return this.defaultTransactionIsolation;
    }
    
    public void setDefaultTransactionIsolation(final Integer defaultTransactionIsolation) {
        this.defaultTransactionIsolation = defaultTransactionIsolation;
    }
    
    public int getActiveCount() {
        return this.activeCount;
    }
    
    public void setActiveCount(final int activeCount) {
        this.activeCount = activeCount;
    }
    
    public int getActivePeak() {
        return this.activePeak;
    }
    
    public void setActivePeak(final int activePeak) {
        this.activePeak = activePeak;
    }
    
    public int getPoolingCount() {
        return this.poolingCount;
    }
    
    public void setPoolingCount(final int poolingCount) {
        this.poolingCount = poolingCount;
    }
    
    public int getPoolingPeak() {
        return this.poolingPeak;
    }
    
    public void setPoolingPeak(final int poolingPeak) {
        this.poolingPeak = poolingPeak;
    }
    
    public long getConnectCount() {
        return this.connectCount;
    }
    
    public void setConnectCount(final long connectCount) {
        this.connectCount = connectCount;
    }
    
    public long getCloseCount() {
        return this.closeCount;
    }
    
    public void setCloseCount(final long closeCount) {
        this.closeCount = closeCount;
    }
    
    public long getWaitThreadCount() {
        return this.waitThreadCount;
    }
    
    public void setWaitThreadCount(final long waitThreadCount) {
        this.waitThreadCount = waitThreadCount;
    }
    
    public long getNotEmptyWaitCount() {
        return this.notEmptyWaitCount;
    }
    
    public void setNotEmptyWaitCount(final long notEmptyWaitCount) {
        this.notEmptyWaitCount = notEmptyWaitCount;
    }
    
    public long getNotEmptyWaitNanos() {
        return this.notEmptyWaitNanos;
    }
    
    public void setNotEmptyWaitNanos(final long notEmptyWaitNanos) {
        this.notEmptyWaitNanos = notEmptyWaitNanos;
    }
    
    public long getLogicConnectErrorCount() {
        return this.logicConnectErrorCount;
    }
    
    public void setLogicConnectErrorCount(final long logicConnectErrorCount) {
        this.logicConnectErrorCount = logicConnectErrorCount;
    }
    
    public long getPhysicalConnectCount() {
        return this.physicalConnectCount;
    }
    
    public void setPhysicalConnectCount(final long physicalConnectCount) {
        this.physicalConnectCount = physicalConnectCount;
    }
    
    public long getPhysicalCloseCount() {
        return this.physicalCloseCount;
    }
    
    public void setPhysicalCloseCount(final long physicalCloseCount) {
        this.physicalCloseCount = physicalCloseCount;
    }
    
    public long getPhysicalConnectErrorCount() {
        return this.physicalConnectErrorCount;
    }
    
    public void setPhysicalConnectErrorCount(final long physicalConnectErrorCount) {
        this.physicalConnectErrorCount = physicalConnectErrorCount;
    }
    
    public long getExecuteCount() {
        return this.executeCount;
    }
    
    public void setExecuteCount(final long executeCount) {
        this.executeCount = executeCount;
    }
    
    public long getErrorCount() {
        return this.errorCount;
    }
    
    public void setErrorCount(final long errorCount) {
        this.errorCount = errorCount;
    }
    
    public long getCommitCount() {
        return this.commitCount;
    }
    
    public void setCommitCount(final long commitCount) {
        this.commitCount = commitCount;
    }
    
    public long getRollbackCount() {
        return this.rollbackCount;
    }
    
    public void setRollbackCount(final long rollbackCount) {
        this.rollbackCount = rollbackCount;
    }
    
    public long getPstmtCacheHitCount() {
        return this.pstmtCacheHitCount;
    }
    
    public void setPstmtCacheHitCount(final long pstmtCacheHitCount) {
        this.pstmtCacheHitCount = pstmtCacheHitCount;
    }
    
    public long getPstmtCacheMissCount() {
        return this.pstmtCacheMissCount;
    }
    
    public void setPstmtCacheMissCount(final long pstmtCacheMissCount) {
        this.pstmtCacheMissCount = pstmtCacheMissCount;
    }
    
    public long getStartTransactionCount() {
        return this.startTransactionCount;
    }
    
    public void setStartTransactionCount(final long startTransactionCount) {
        this.startTransactionCount = startTransactionCount;
    }
    
    public long[] getTransactionHistogram() {
        return new long[] { this.txn_0_1, this.txn_1_10, this.txn_10_100, this.txn_100_1000, this.txn_1000_10000, this.txn_10000_100000, this.txn_more };
    }
    
    public void setTransactionHistogram(final long[] values) {
        this.txn_0_1 = values[0];
        this.txn_1_10 = values[1];
        this.txn_10_100 = values[2];
        this.txn_100_1000 = values[3];
        this.txn_1000_10000 = values[4];
        this.txn_10000_100000 = values[5];
        this.txn_more = values[6];
    }
    
    public long[] getConnectionHoldTimeHistogram() {
        return this.connectionHoldTimeHistogram;
    }
    
    public void setConnectionHoldTimeHistogram(final long[] connectionHoldTimeHistogram) {
        this.connectionHoldTimeHistogram = connectionHoldTimeHistogram;
    }
    
    public long getClobOpenCount() {
        return this.clobOpenCount;
    }
    
    public void setClobOpenCount(final long clobOpenCount) {
        this.clobOpenCount = clobOpenCount;
    }
    
    public long getBlobOpenCount() {
        return this.blobOpenCount;
    }
    
    public void setBlobOpenCount(final long blobOpenCount) {
        this.blobOpenCount = blobOpenCount;
    }
    
    public List<JdbcSqlStatValue> getSqlList() {
        return this.sqlList;
    }
    
    public void setSqlList(final List<JdbcSqlStatValue> sqlList) {
        this.sqlList = sqlList;
    }
    
    public void setActivePeakTime(final long activePeakTime) {
        this.activePeakTime = activePeakTime;
    }
    
    public void setPoolingPeakTime(final long poolingPeakTime) {
        this.poolingPeakTime = poolingPeakTime;
    }
    
    public long getKeepAliveCheckCount() {
        return this.keepAliveCheckCount;
    }
    
    public void setKeepAliveCheckCount(final long keepAliveCheckCount) {
        this.keepAliveCheckCount = keepAliveCheckCount;
    }
}
