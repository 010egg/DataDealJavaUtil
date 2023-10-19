// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class WallFunctionStat
{
    private volatile long invokeCount;
    static final AtomicLongFieldUpdater<WallFunctionStat> invokeCountUpdater;
    
    public long getInvokeCount() {
        return this.invokeCount;
    }
    
    public void incrementInvokeCount() {
        WallFunctionStat.invokeCountUpdater.incrementAndGet(this);
    }
    
    public void addSqlFunctionStat(final WallSqlFunctionStat sqlFunctionStat) {
        this.invokeCount += sqlFunctionStat.getInvokeCount();
    }
    
    @Override
    public String toString() {
        return "{\"invokeCount\":" + this.invokeCount + "}";
    }
    
    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("invokeCount", this.invokeCount);
        return map;
    }
    
    public WallFunctionStatValue getStatValue(final boolean reset) {
        final WallFunctionStatValue statValue = new WallFunctionStatValue();
        statValue.setInvokeCount(JdbcSqlStatUtils.get(this, WallFunctionStat.invokeCountUpdater, reset));
        return statValue;
    }
    
    static {
        invokeCountUpdater = AtomicLongFieldUpdater.newUpdater(WallFunctionStat.class, "invokeCount");
    }
}
