// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import javax.management.JMException;
import javax.management.openmbean.CompositeData;
import java.util.Date;

public interface JdbcStatementStatMBean
{
    long getCreateCount();
    
    long getPrepareCount();
    
    long getPrepareCallCount();
    
    long getCloseCount();
    
    long getExecuteMillisTotal();
    
    long getExecuteSuccessCount();
    
    Date getLastErrorTime();
    
    CompositeData getLastError() throws JMException;
    
    Date getExecuteLastTime();
    
    int getRunningCount();
    
    int getConcurrentMax();
    
    long getExecuteCount();
    
    long getErrorCount();
}
