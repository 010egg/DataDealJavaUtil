// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.profile;

public class ProfileEntry
{
    private final ProfileEntry parent;
    private final ProfileEntryKey key;
    private int executeCount;
    private long executeTimeNanos;
    
    public ProfileEntry(final ProfileEntry parent, final ProfileEntryKey key) {
        this.executeCount = 0;
        this.executeTimeNanos = 0L;
        this.parent = parent;
        this.key = key;
    }
    
    public ProfileEntry getParent() {
        return this.parent;
    }
    
    public ProfileEntryKey getKey() {
        return this.key;
    }
    
    public String getParentName() {
        return this.key.getParentName();
    }
    
    public String getName() {
        return this.key.getName();
    }
    
    public String getType() {
        return this.key.getType();
    }
    
    public int getExecuteCount() {
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
