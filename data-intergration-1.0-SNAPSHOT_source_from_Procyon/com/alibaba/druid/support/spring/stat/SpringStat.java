// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SpringStat
{
    private ConcurrentMap<SpringMethodInfo, SpringMethodStat> methodStats;
    
    public SpringStat() {
        this.methodStats = new ConcurrentHashMap<SpringMethodInfo, SpringMethodStat>(16, 0.75f, 1);
    }
    
    public void reset() {
        for (final SpringMethodStat stat : this.methodStats.values()) {
            stat.reset();
        }
    }
    
    public SpringMethodStat getMethodStat(final SpringMethodInfo methodInfo, final boolean create) {
        SpringMethodStat methodStat = this.methodStats.get(methodInfo);
        if (methodStat != null) {
            return methodStat;
        }
        if (create) {
            this.methodStats.putIfAbsent(methodInfo, new SpringMethodStat(methodInfo));
            methodStat = this.methodStats.get(methodInfo);
        }
        return methodStat;
    }
    
    public List<SpringMethodStatValue> getStatList(final boolean reset) {
        final List<SpringMethodStatValue> statValueList = new ArrayList<SpringMethodStatValue>(this.methodStats.size());
        for (final SpringMethodStat methodStat : this.methodStats.values()) {
            final SpringMethodStatValue statValue = methodStat.getStatValue(reset);
            if (statValue.getRunningCount() == 0 && statValue.getExecuteCount() == 0L) {
                continue;
            }
            statValueList.add(statValue);
        }
        return statValueList;
    }
    
    public List<Map<String, Object>> getMethodStatDataList() {
        final List<Map<String, Object>> methodStatDataList = new ArrayList<Map<String, Object>>(this.methodStats.size());
        for (final SpringMethodStat methodStat : this.methodStats.values()) {
            final Map<String, Object> methodStatData = methodStat.getStatData();
            final int runningCount = methodStatData.get("RunningCount").intValue();
            final long executeCount = methodStatData.get("ExecuteCount");
            if (runningCount == 0 && executeCount == 0L) {
                continue;
            }
            methodStatDataList.add(methodStatData);
        }
        return methodStatDataList;
    }
    
    public Map<String, Object> getMethodStatData(final String clazz, final String method) {
        for (final SpringMethodStat methodStat : this.methodStats.values()) {
            final SpringMethodInfo methodInfo = methodStat.getMethodInfo();
            if (methodInfo.getClassName().equals(clazz) && methodInfo.getSignature().equals(method)) {
                return methodStat.getStatData();
            }
        }
        return null;
    }
}
