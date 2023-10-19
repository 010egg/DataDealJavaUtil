// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.profile;

import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class ProfileEntryStat
{
    private volatile long executeCount;
    private volatile long executeTimeNanos;
    private static final AtomicLongFieldUpdater<ProfileEntryStat> executeCountUpdater;
    private static final AtomicLongFieldUpdater<ProfileEntryStat> executeTimeNanosUpdater;
    
    public ProfileEntryStat() {
        this.executeCount = 0L;
        this.executeTimeNanos = 0L;
    }
    
    public ProfileEntryStatValue getValue(final boolean reset) {
        final ProfileEntryStatValue val = new ProfileEntryStatValue();
        val.setExecuteCount(JdbcSqlStatUtils.get(this, ProfileEntryStat.executeCountUpdater, reset));
        val.setExecuteTimeNanos(JdbcSqlStatUtils.get(this, ProfileEntryStat.executeTimeNanosUpdater, reset));
        return val;
    }
    
    public long getExecuteCount() {
        return this.executeCount;
    }
    
    public void addExecuteCount(final long delta) {
        ProfileEntryStat.executeCountUpdater.addAndGet(this, delta);
    }
    
    public long getExecuteTimeNanos() {
        return this.executeTimeNanos;
    }
    
    public void addExecuteTimeNanos(final long delta) {
        ProfileEntryStat.executeTimeNanosUpdater.addAndGet(this, delta);
    }
    
    static {
        executeCountUpdater = AtomicLongFieldUpdater.newUpdater(ProfileEntryStat.class, "executeCount");
        executeTimeNanosUpdater = AtomicLongFieldUpdater.newUpdater(ProfileEntryStat.class, "executeTimeNanos");
    }
}
