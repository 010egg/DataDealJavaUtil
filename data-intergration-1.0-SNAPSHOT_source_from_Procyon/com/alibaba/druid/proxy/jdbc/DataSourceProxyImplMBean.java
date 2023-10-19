// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Date;

public interface DataSourceProxyImplMBean
{
    String getName();
    
    String getUrl();
    
    String getRawUrl();
    
    Date getCreatedTime();
    
    String getRawDriverClassName();
    
    String[] getFilterClasses();
    
    int getRawDriverMajorVersion();
    
    int getRawDriverMinorVersion();
    
    String getProperties();
}
