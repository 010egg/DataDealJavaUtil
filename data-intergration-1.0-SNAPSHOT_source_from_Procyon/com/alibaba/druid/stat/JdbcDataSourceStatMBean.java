// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import javax.management.JMException;
import javax.management.openmbean.TabularData;

public interface JdbcDataSourceStatMBean
{
    void reset();
    
    TabularData getSqlList() throws JMException;
    
    TabularData getConnectionList() throws JMException;
    
    String getConnectionUrl();
    
    long getConnectionActiveCount();
    
    long getConnectionConnectAliveMillis();
    
    long[] getConnectionHistogramValues();
    
    long[] getConnectionHistogramRanges();
}
