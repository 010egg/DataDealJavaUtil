// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import java.util.Date;

public interface JdbcSqlStatMBean
{
    String getSql();
    
    Date getExecuteLastStartTime();
    
    Date getExecuteNanoSpanMaxOccurTime();
    
    Date getExecuteErrorLastTime();
    
    long getExecuteBatchSizeTotal();
    
    long getExecuteBatchSizeMax();
    
    long getExecuteSuccessCount();
    
    long getExecuteMillisTotal();
    
    long getExecuteMillisMax();
    
    long getErrorCount();
    
    long getConcurrentMax();
    
    long getRunningCount();
    
    String getName();
    
    String getFile();
    
    void reset();
    
    long getFetchRowCount();
    
    long getUpdateCount();
    
    long getExecuteCount();
    
    long getId();
}
