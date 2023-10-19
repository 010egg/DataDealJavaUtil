// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.selector;

import javax.sql.DataSource;

public class StickyDataSourceHolder
{
    private long retrievingTime;
    private DataSource dataSource;
    
    public StickyDataSourceHolder() {
        this.retrievingTime = System.currentTimeMillis();
    }
    
    public StickyDataSourceHolder(final DataSource dataSource) {
        this.retrievingTime = System.currentTimeMillis();
        this.dataSource = dataSource;
    }
    
    public boolean isValid() {
        return this.retrievingTime > 0L && this.dataSource != null;
    }
    
    public long getRetrievingTime() {
        return this.retrievingTime;
    }
    
    public void setRetrievingTime(final long retrievingTime) {
        this.retrievingTime = retrievingTime;
    }
    
    public DataSource getDataSource() {
        return this.dataSource;
    }
    
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
