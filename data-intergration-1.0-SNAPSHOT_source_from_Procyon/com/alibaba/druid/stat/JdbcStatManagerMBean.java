// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import javax.management.JMException;
import javax.management.openmbean.TabularData;

public interface JdbcStatManagerMBean
{
    TabularData getDataSourceList() throws JMException;
    
    TabularData getSqlList() throws JMException;
    
    TabularData getConnectionList() throws JMException;
    
    void reset();
    
    long getResetCount();
}
