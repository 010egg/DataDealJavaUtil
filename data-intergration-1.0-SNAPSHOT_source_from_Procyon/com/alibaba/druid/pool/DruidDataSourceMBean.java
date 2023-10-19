// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.SQLException;
import javax.management.ObjectName;
import java.util.Date;

public interface DruidDataSourceMBean extends DruidAbstractDataSourceMBean
{
    long getResetCount();
    
    boolean isEnable();
    
    String getUrl();
    
    void shrink();
    
    int removeAbandoned();
    
    String dump();
    
    int getWaitThreadCount();
    
    int getLockQueueLength();
    
    long getNotEmptyWaitCount();
    
    int getNotEmptyWaitThreadCount();
    
    long getNotEmptySignalCount();
    
    long getNotEmptyWaitMillis();
    
    long getNotEmptyWaitNanos();
    
    void resetStat();
    
    boolean isResetStatEnable();
    
    void setResetStatEnable(final boolean p0);
    
    String getVersion();
    
    void setPoolPreparedStatements(final boolean p0);
    
    int getActivePeak();
    
    int getPoolingPeak();
    
    Date getActivePeakTime();
    
    Date getPoolingPeakTime();
    
    long getErrorCount();
    
    ObjectName getObjectName();
    
    void clearStatementCache() throws SQLException;
    
    long getDiscardCount();
    
    void setStatLoggerClassName(final String p0);
    
    long getTimeBetweenLogStatsMillis();
    
    void setTimeBetweenLogStatsMillis(final long p0);
    
    void setConnectionProperties(final String p0);
    
    int fill() throws SQLException;
    
    int fill(final int p0) throws SQLException;
    
    boolean isUseGlobalDataSourceStat();
}
