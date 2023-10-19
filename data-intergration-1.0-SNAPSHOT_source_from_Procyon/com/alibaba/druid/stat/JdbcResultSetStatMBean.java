// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

public interface JdbcResultSetStatMBean
{
    long getHoldMillisTotal();
    
    long getFetchRowCount();
    
    long getOpenCount();
    
    long getCloseCount();
    
    int getOpeningCount();
    
    int getOpeningMax();
    
    long getErrorCount();
}
