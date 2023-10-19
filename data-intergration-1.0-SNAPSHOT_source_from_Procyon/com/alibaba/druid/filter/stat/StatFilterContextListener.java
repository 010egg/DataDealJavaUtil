// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.stat;

public interface StatFilterContextListener
{
    void addUpdateCount(final int p0);
    
    void addFetchRowCount(final int p0);
    
    void executeBefore(final String p0, final boolean p1);
    
    void executeAfter(final String p0, final long p1, final Throwable p2);
    
    void commit();
    
    void rollback();
    
    void pool_connect();
    
    void pool_close(final long p0);
    
    void physical_connection_connect();
    
    void physical_connection_close(final long p0);
    
    void resultSet_open();
    
    void resultSet_close(final long p0);
    
    void clob_open();
    
    void blob_open();
}
