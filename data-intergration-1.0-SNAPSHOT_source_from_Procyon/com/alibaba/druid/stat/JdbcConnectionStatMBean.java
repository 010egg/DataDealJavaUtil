// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import java.util.Date;

public interface JdbcConnectionStatMBean
{
    long getConnectCount();
    
    long getCloseCount();
    
    int getActiveMax();
    
    long getCommitCount();
    
    long getRollbackCount();
    
    long getConnectMillis();
    
    long getConnectErrorCount();
    
    Date getConnectLastTime();
}
