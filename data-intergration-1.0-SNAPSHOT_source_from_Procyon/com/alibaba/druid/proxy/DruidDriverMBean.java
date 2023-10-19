// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy;

public interface DruidDriverMBean
{
    String getDruidVersion();
    
    long getConnectCount();
    
    void resetStat();
    
    String getAcceptPrefix();
    
    boolean jdbcCompliant();
    
    int getMinorVersion();
    
    int getMajorVersion();
    
    String[] getDataSourceUrls();
}
