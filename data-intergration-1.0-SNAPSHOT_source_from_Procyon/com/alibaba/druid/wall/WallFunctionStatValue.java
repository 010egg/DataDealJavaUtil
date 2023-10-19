// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.LinkedHashMap;
import java.util.Map;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import com.alibaba.druid.support.monitor.annotation.MField;
import com.alibaba.druid.support.monitor.annotation.MTable;

@MTable(name = "druid_wall_function")
public class WallFunctionStatValue
{
    @MField(groupBy = true, aggregate = AggregateType.None)
    private String name;
    @MField(aggregate = AggregateType.Sum)
    private long invokeCount;
    
    public WallFunctionStatValue() {
    }
    
    public WallFunctionStatValue(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public long getInvokeCount() {
        return this.invokeCount;
    }
    
    public void setInvokeCount(final long invokeCount) {
        this.invokeCount = invokeCount;
    }
    
    public Map<String, Object> toMap() {
        final Map<String, Object> map = new LinkedHashMap<String, Object>(2);
        map.put("name", this.name);
        map.put("invokeCount", this.invokeCount);
        return map;
    }
}
