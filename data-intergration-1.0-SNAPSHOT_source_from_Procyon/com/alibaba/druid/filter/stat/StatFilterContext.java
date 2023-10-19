// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.stat;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class StatFilterContext
{
    private List<StatFilterContextListener> listeners;
    private static final StatFilterContext instance;
    
    public StatFilterContext() {
        this.listeners = new CopyOnWriteArrayList<StatFilterContextListener>();
    }
    
    public static final StatFilterContext getInstance() {
        return StatFilterContext.instance;
    }
    
    public void addContextListener(final StatFilterContextListener listener) {
        this.listeners.add(listener);
    }
    
    public boolean removeContextListener(final StatFilterContextListener listener) {
        return this.listeners.remove(listener);
    }
    
    public List<StatFilterContextListener> getListeners() {
        return this.listeners;
    }
    
    public void addUpdateCount(final int updateCount) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.addUpdateCount(updateCount);
        }
    }
    
    public void addFetchRowCount(final int fetchRowCount) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.addFetchRowCount(fetchRowCount);
        }
    }
    
    public void executeBefore(final String sql, final boolean inTransaction) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.executeBefore(sql, inTransaction);
        }
    }
    
    public void executeAfter(final String sql, final long nanoSpan, final Throwable error) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.executeAfter(sql, nanoSpan, error);
        }
    }
    
    public void commit() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.commit();
        }
    }
    
    public void rollback() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.rollback();
        }
    }
    
    public void pool_connection_open() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.pool_connect();
        }
    }
    
    public void pool_connection_close(final long nanos) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.pool_close(nanos);
        }
    }
    
    public void physical_connection_connect() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.physical_connection_connect();
        }
    }
    
    public void physical_connection_close(final long nanos) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.physical_connection_close(nanos);
        }
    }
    
    public void resultSet_open() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.resultSet_open();
        }
    }
    
    public void resultSet_close(final long nanos) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.resultSet_close(nanos);
        }
    }
    
    public void clob_open() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.clob_open();
        }
    }
    
    public void blob_open() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            final StatFilterContextListener listener = this.listeners.get(i);
            listener.blob_open();
        }
    }
    
    static {
        instance = new StatFilterContext();
    }
}
