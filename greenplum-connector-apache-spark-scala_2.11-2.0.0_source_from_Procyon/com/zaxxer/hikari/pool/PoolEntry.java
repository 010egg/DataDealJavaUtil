// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import com.zaxxer.hikari.util.ClockSource;
import java.sql.Statement;
import com.zaxxer.hikari.util.FastList;
import java.util.concurrent.ScheduledFuture;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import org.slf4j.Logger;
import com.zaxxer.hikari.util.ConcurrentBag;

final class PoolEntry implements ConcurrentBag.IConcurrentBagEntry
{
    private static final Logger LOGGER;
    private static final AtomicIntegerFieldUpdater<PoolEntry> stateUpdater;
    Connection connection;
    long lastAccessed;
    long lastBorrowed;
    private volatile int state;
    private volatile boolean evict;
    private volatile ScheduledFuture<?> endOfLife;
    private final FastList<Statement> openStatements;
    private final HikariPool hikariPool;
    private final boolean isReadOnly;
    private final boolean isAutoCommit;
    
    PoolEntry(final Connection connection, final PoolBase pool, final boolean isReadOnly, final boolean isAutoCommit) {
        this.state = 0;
        this.connection = connection;
        this.hikariPool = (HikariPool)pool;
        this.isReadOnly = isReadOnly;
        this.isAutoCommit = isAutoCommit;
        this.lastAccessed = ClockSource.currentTime();
        this.openStatements = new FastList<Statement>(Statement.class, 16);
    }
    
    void recycle(final long lastAccessed) {
        if (this.connection != null) {
            this.lastAccessed = lastAccessed;
            this.hikariPool.recycle(this);
        }
    }
    
    void setFutureEol(final ScheduledFuture<?> endOfLife) {
        this.endOfLife = endOfLife;
    }
    
    Connection createProxyConnection(final ProxyLeakTask leakTask, final long now) {
        return ProxyFactory.getProxyConnection(this, this.connection, this.openStatements, leakTask, now, this.isReadOnly, this.isAutoCommit);
    }
    
    void resetConnectionState(final ProxyConnection proxyConnection, final int dirtyBits) throws SQLException {
        this.hikariPool.resetConnectionState(this.connection, proxyConnection, dirtyBits);
    }
    
    String getPoolName() {
        return this.hikariPool.toString();
    }
    
    boolean isMarkedEvicted() {
        return this.evict;
    }
    
    void markEvicted() {
        this.evict = true;
    }
    
    void evict(final String closureReason) {
        this.hikariPool.closeConnection(this, closureReason);
    }
    
    long getMillisSinceBorrowed() {
        return ClockSource.elapsedMillis(this.lastBorrowed);
    }
    
    @Override
    public String toString() {
        final long now = ClockSource.currentTime();
        return this.connection + ", accessed " + ClockSource.elapsedDisplayString(this.lastAccessed, now) + " ago, " + this.stateToString();
    }
    
    @Override
    public int getState() {
        return PoolEntry.stateUpdater.get(this);
    }
    
    @Override
    public boolean compareAndSet(final int expect, final int update) {
        return PoolEntry.stateUpdater.compareAndSet(this, expect, update);
    }
    
    @Override
    public void setState(final int update) {
        PoolEntry.stateUpdater.set(this, update);
    }
    
    Connection close() {
        final ScheduledFuture<?> eol = this.endOfLife;
        if (eol != null && !eol.isDone() && !eol.cancel(false)) {
            PoolEntry.LOGGER.warn("{} - maxLifeTime expiration task cancellation unexpectedly returned false for connection {}", this.getPoolName(), this.connection);
        }
        final Connection con = this.connection;
        this.connection = null;
        this.endOfLife = null;
        return con;
    }
    
    private String stateToString() {
        switch (this.state) {
            case 1: {
                return "IN_USE";
            }
            case 0: {
                return "NOT_IN_USE";
            }
            case -1: {
                return "REMOVED";
            }
            case -2: {
                return "RESERVED";
            }
            default: {
                return "Invalid";
            }
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(PoolEntry.class);
        stateUpdater = AtomicIntegerFieldUpdater.newUpdater(PoolEntry.class, "state");
    }
}
