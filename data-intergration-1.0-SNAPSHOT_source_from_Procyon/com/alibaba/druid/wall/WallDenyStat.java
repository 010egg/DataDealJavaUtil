// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class WallDenyStat
{
    private volatile long denyCount;
    private volatile long lastDenyTimeMillis;
    private volatile long resetCount;
    static final AtomicLongFieldUpdater<WallDenyStat> denyCountUpdater;
    static final AtomicLongFieldUpdater<WallDenyStat> resetCountUpdater;
    
    public long incrementAndGetDenyCount() {
        this.lastDenyTimeMillis = System.currentTimeMillis();
        return WallDenyStat.denyCountUpdater.incrementAndGet(this);
    }
    
    public long getDenyCount() {
        return this.denyCount;
    }
    
    public Date getLastDenyTime() {
        if (this.lastDenyTimeMillis <= 0L) {
            return null;
        }
        return new Date(this.lastDenyTimeMillis);
    }
    
    public void reset() {
        this.lastDenyTimeMillis = 0L;
        this.denyCount = 0L;
        WallDenyStat.resetCountUpdater.incrementAndGet(this);
    }
    
    public long getResetCount() {
        return this.resetCount;
    }
    
    static {
        denyCountUpdater = AtomicLongFieldUpdater.newUpdater(WallDenyStat.class, "denyCount");
        resetCountUpdater = AtomicLongFieldUpdater.newUpdater(WallDenyStat.class, "resetCount");
    }
}
