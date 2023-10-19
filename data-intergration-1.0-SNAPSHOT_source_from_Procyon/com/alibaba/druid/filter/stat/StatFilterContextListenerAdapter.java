// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.stat;

public class StatFilterContextListenerAdapter implements StatFilterContextListener
{
    @Override
    public void addUpdateCount(final int updateCount) {
    }
    
    @Override
    public void addFetchRowCount(final int fetchRowCount) {
    }
    
    @Override
    public void executeBefore(final String sql, final boolean inTransaction) {
    }
    
    @Override
    public void executeAfter(final String sql, final long nanoSpan, final Throwable error) {
    }
    
    @Override
    public void commit() {
    }
    
    @Override
    public void rollback() {
    }
    
    @Override
    public void pool_connect() {
    }
    
    @Override
    public void pool_close(final long nanos) {
    }
    
    @Override
    public void physical_connection_connect() {
    }
    
    @Override
    public void physical_connection_close(final long nanos) {
    }
    
    @Override
    public void resultSet_open() {
    }
    
    @Override
    public void resultSet_close(final long nanos) {
    }
    
    @Override
    public void clob_open() {
    }
    
    @Override
    public void blob_open() {
    }
}
