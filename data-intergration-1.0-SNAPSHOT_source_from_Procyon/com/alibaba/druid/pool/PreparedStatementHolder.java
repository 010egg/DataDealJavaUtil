// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.PreparedStatement;

public final class PreparedStatementHolder
{
    public final DruidPooledPreparedStatement.PreparedStatementKey key;
    public final PreparedStatement statement;
    private int hitCount;
    private int fetchRowPeak;
    private int defaultRowPrefetch;
    private int rowPrefetch;
    private boolean enterOracleImplicitCache;
    private int inUseCount;
    private boolean pooling;
    
    public PreparedStatementHolder(final DruidPooledPreparedStatement.PreparedStatementKey key, final PreparedStatement stmt) {
        this.hitCount = 0;
        this.fetchRowPeak = -1;
        this.defaultRowPrefetch = -1;
        this.rowPrefetch = -1;
        this.enterOracleImplicitCache = false;
        this.inUseCount = 0;
        this.pooling = false;
        this.key = key;
        this.statement = stmt;
    }
    
    public boolean isEnterOracleImplicitCache() {
        return this.enterOracleImplicitCache;
    }
    
    public void setEnterOracleImplicitCache(final boolean enterOracleImplicitCache) {
        this.enterOracleImplicitCache = enterOracleImplicitCache;
    }
    
    public int getDefaultRowPrefetch() {
        return this.defaultRowPrefetch;
    }
    
    public void setDefaultRowPrefetch(final int defaultRowPrefetch) {
        this.defaultRowPrefetch = defaultRowPrefetch;
    }
    
    public int getRowPrefetch() {
        return this.rowPrefetch;
    }
    
    public void setRowPrefetch(final int rowPrefetch) {
        this.rowPrefetch = rowPrefetch;
    }
    
    public int getFetchRowPeak() {
        return this.fetchRowPeak;
    }
    
    public void setFetchRowPeak(final int fetchRowPeak) {
        if (fetchRowPeak > this.fetchRowPeak) {
            this.fetchRowPeak = fetchRowPeak;
        }
    }
    
    public void incrementHitCount() {
        ++this.hitCount;
    }
    
    public int getHitCount() {
        return this.hitCount;
    }
    
    public boolean isInUse() {
        return this.inUseCount > 0;
    }
    
    public void incrementInUseCount() {
        ++this.inUseCount;
    }
    
    public void decrementInUseCount() {
        --this.inUseCount;
    }
    
    public int getInUseCount() {
        return this.inUseCount;
    }
    
    public boolean isPooling() {
        return this.pooling;
    }
    
    public void setPooling(final boolean pooling) {
        this.pooling = pooling;
    }
}
