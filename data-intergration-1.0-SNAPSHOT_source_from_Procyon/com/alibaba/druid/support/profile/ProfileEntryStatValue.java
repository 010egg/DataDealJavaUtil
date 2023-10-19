// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.profile;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProfileEntryStatValue
{
    private String parentName;
    private String name;
    private String type;
    private long executeCount;
    private long executeTimeNanos;
    
    public ProfileEntryStatValue() {
        this.executeCount = 0L;
        this.executeTimeNanos = 0L;
    }
    
    public String getParentName() {
        return this.parentName;
    }
    
    public void setParentName(final String parentName) {
        this.parentName = parentName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public long getExecuteCount() {
        return this.executeCount;
    }
    
    public void setExecuteCount(final long executeCount) {
        this.executeCount = executeCount;
    }
    
    public long getExecuteTimeNanos() {
        return this.executeTimeNanos;
    }
    
    public void setExecuteTimeNanos(final long executeTimeNanos) {
        this.executeTimeNanos = executeTimeNanos;
    }
    
    public Map<String, Object> getData() {
        final Map<String, Object> entryData = new LinkedHashMap<String, Object>();
        entryData.put("Name", this.getName());
        entryData.put("Parent", this.getParentName());
        entryData.put("Type", this.getType());
        entryData.put("ExecuteCount", this.getExecuteCount());
        entryData.put("ExecuteTimeMillis", this.getExecuteTimeNanos() / 1000L / 1000L);
        return entryData;
    }
}
