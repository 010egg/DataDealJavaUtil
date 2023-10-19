// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class JdbcResultSetStat implements JdbcResultSetStatMBean
{
    private final AtomicInteger openingCount;
    private final AtomicInteger openingMax;
    private final AtomicLong openCount;
    private final AtomicLong errorCount;
    private final AtomicLong aliveNanoTotal;
    private final AtomicLong aliveNanoMax;
    private final AtomicLong aliveNanoMin;
    private Throwable lastError;
    private long lastErrorTime;
    private long lastOpenTime;
    private final AtomicLong fetchRowCount;
    private final AtomicLong closeCount;
    
    public JdbcResultSetStat() {
        this.openingCount = new AtomicInteger();
        this.openingMax = new AtomicInteger();
        this.openCount = new AtomicLong();
        this.errorCount = new AtomicLong();
        this.aliveNanoTotal = new AtomicLong();
        this.aliveNanoMax = new AtomicLong();
        this.aliveNanoMin = new AtomicLong();
        this.lastOpenTime = 0L;
        this.fetchRowCount = new AtomicLong(0L);
        this.closeCount = new AtomicLong(0L);
    }
    
    public void reset() {
        this.openingMax.set(0);
        this.openCount.set(0L);
        this.errorCount.set(0L);
        this.aliveNanoTotal.set(0L);
        this.aliveNanoMax.set(0L);
        this.aliveNanoMin.set(0L);
        this.lastError = null;
        this.lastErrorTime = 0L;
        this.lastOpenTime = 0L;
        this.fetchRowCount.set(0L);
        this.closeCount.set(0L);
    }
    
    public void beforeOpen() {
        final int invoking = this.openingCount.incrementAndGet();
        int max;
        do {
            max = this.openingMax.get();
        } while (invoking > max && !this.openingMax.compareAndSet(max, invoking));
        this.openCount.incrementAndGet();
        this.lastOpenTime = System.currentTimeMillis();
    }
    
    @Override
    public long getErrorCount() {
        return this.errorCount.get();
    }
    
    @Override
    public int getOpeningCount() {
        return this.openingCount.get();
    }
    
    @Override
    public int getOpeningMax() {
        return this.openingMax.get();
    }
    
    @Override
    public long getOpenCount() {
        return this.openCount.get();
    }
    
    public Date getLastOpenTime() {
        if (this.lastOpenTime == 0L) {
            return null;
        }
        return new Date(this.lastOpenTime);
    }
    
    public long getAliveNanoTotal() {
        return this.aliveNanoTotal.get();
    }
    
    public long getAliveMillisTotal() {
        return this.aliveNanoTotal.get() / 1000000L;
    }
    
    public long getAliveMilisMin() {
        return this.aliveNanoMin.get() / 1000000L;
    }
    
    public long getAliveMilisMax() {
        return this.aliveNanoMax.get() / 1000000L;
    }
    
    public void afterClose(final long aliveNano) {
        this.openingCount.decrementAndGet();
        this.aliveNanoTotal.addAndGet(aliveNano);
        long max;
        do {
            max = this.aliveNanoMax.get();
            if (aliveNano <= max) {
                break;
            }
        } while (!this.aliveNanoMax.compareAndSet(max, aliveNano));
        long min;
        do {
            min = this.aliveNanoMin.get();
        } while (aliveNano < min && !this.aliveNanoMin.compareAndSet(min, aliveNano));
    }
    
    public Throwable getLastError() {
        return this.lastError;
    }
    
    public Date getLastErrorTime() {
        if (this.lastErrorTime <= 0L) {
            return null;
        }
        return new Date(this.lastErrorTime);
    }
    
    public void error(final Throwable error) {
        this.errorCount.incrementAndGet();
        this.lastError = error;
        this.lastErrorTime = System.currentTimeMillis();
    }
    
    @Override
    public long getHoldMillisTotal() {
        return this.getAliveNanoTotal() / 1000000L;
    }
    
    @Override
    public long getFetchRowCount() {
        return this.fetchRowCount.get();
    }
    
    @Override
    public long getCloseCount() {
        return this.closeCount.get();
    }
    
    public void addFetchRowCount(final long fetchCount) {
        this.fetchRowCount.addAndGet(fetchCount);
    }
    
    public void incrementCloseCounter() {
        this.closeCount.incrementAndGet();
    }
}
