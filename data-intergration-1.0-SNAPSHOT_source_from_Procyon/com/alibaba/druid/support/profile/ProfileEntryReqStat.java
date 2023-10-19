// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.profile;

public class ProfileEntryReqStat
{
    private long executeCount;
    private long executeTimeNanos;
    
    public long getExecuteCount() {
        return this.executeCount;
    }
    
    public void incrementExecuteCount() {
        ++this.executeCount;
    }
    
    public long getExecuteTimeNanos() {
        return this.executeTimeNanos;
    }
    
    public void addExecuteTimeNanos(final long nanos) {
        this.executeTimeNanos += nanos;
    }
}
