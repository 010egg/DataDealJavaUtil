// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import com.alibaba.druid.support.monitor.annotation.MField;
import com.alibaba.druid.support.monitor.annotation.MTable;

@MTable(name = "druid_wall")
public class WallProviderStatValue
{
    @MField(aggregate = AggregateType.None)
    private String name;
    @MField(aggregate = AggregateType.Sum)
    private long checkCount;
    @MField(aggregate = AggregateType.Sum)
    private long hardCheckCount;
    @MField(aggregate = AggregateType.Sum)
    private long violationCount;
    @MField(aggregate = AggregateType.Sum)
    private long whiteListHitCount;
    @MField(aggregate = AggregateType.Sum)
    private long blackListHitCount;
    @MField(aggregate = AggregateType.Sum)
    private long syntaxErrorCount;
    @MField(aggregate = AggregateType.Sum)
    private long violationEffectRowCount;
    private final List<WallTableStatValue> tables;
    private final List<WallFunctionStatValue> functions;
    private final List<WallSqlStatValue> whiteList;
    private final List<WallSqlStatValue> blackList;
    
    public WallProviderStatValue() {
        this.tables = new ArrayList<WallTableStatValue>();
        this.functions = new ArrayList<WallFunctionStatValue>();
        this.whiteList = new ArrayList<WallSqlStatValue>();
        this.blackList = new ArrayList<WallSqlStatValue>();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public long getCheckCount() {
        return this.checkCount;
    }
    
    public void setCheckCount(final long checkCount) {
        this.checkCount = checkCount;
    }
    
    public long getHardCheckCount() {
        return this.hardCheckCount;
    }
    
    public void setHardCheckCount(final long hardCheckCount) {
        this.hardCheckCount = hardCheckCount;
    }
    
    public long getViolationCount() {
        return this.violationCount;
    }
    
    public void setViolationCount(final long violationCount) {
        this.violationCount = violationCount;
    }
    
    public long getWhiteListHitCount() {
        return this.whiteListHitCount;
    }
    
    public void setWhiteListHitCount(final long whiteListHitCount) {
        this.whiteListHitCount = whiteListHitCount;
    }
    
    public long getBlackListHitCount() {
        return this.blackListHitCount;
    }
    
    public void setBlackListHitCount(final long blackListHitCount) {
        this.blackListHitCount = blackListHitCount;
    }
    
    public long getSyntaxErrorCount() {
        return this.syntaxErrorCount;
    }
    
    public void setSyntaxErrorCount(final long syntaxErrorCount) {
        this.syntaxErrorCount = syntaxErrorCount;
    }
    
    public long getViolationEffectRowCount() {
        return this.violationEffectRowCount;
    }
    
    public void setViolationEffectRowCount(final long violationEffectRowCount) {
        this.violationEffectRowCount = violationEffectRowCount;
    }
    
    public List<WallTableStatValue> getTables() {
        return this.tables;
    }
    
    public List<WallFunctionStatValue> getFunctions() {
        return this.functions;
    }
    
    public List<WallSqlStatValue> getWhiteList() {
        return this.whiteList;
    }
    
    public List<WallSqlStatValue> getBlackList() {
        return this.blackList;
    }
    
    public Map<String, Object> toMap() {
        final Map<String, Object> info = new LinkedHashMap<String, Object>();
        info.put("checkCount", this.getCheckCount());
        info.put("hardCheckCount", this.getHardCheckCount());
        info.put("violationCount", this.getViolationCount());
        info.put("violationEffectRowCount", this.getViolationEffectRowCount());
        info.put("blackListHitCount", this.getBlackListHitCount());
        info.put("blackListSize", this.getBlackList().size());
        info.put("whiteListHitCount", this.getWhiteListHitCount());
        info.put("whiteListSize", this.getWhiteList().size());
        info.put("syntaxErrorCount", this.getSyntaxErrorCount());
        final List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>(this.tables.size());
        for (final WallTableStatValue tableStatValue : this.tables) {
            final Map<String, Object> statMap = tableStatValue.toMap();
            tables.add(statMap);
        }
        info.put("tables", tables);
        final List<Map<String, Object>> functions = new ArrayList<Map<String, Object>>();
        for (final WallFunctionStatValue funStatValue : this.functions) {
            final Map<String, Object> statMap = funStatValue.toMap();
            functions.add(statMap);
        }
        info.put("functions", functions);
        final List<Map<String, Object>> blackList = new ArrayList<Map<String, Object>>(this.blackList.size());
        for (final WallSqlStatValue sqlStatValue : this.blackList) {
            blackList.add(sqlStatValue.toMap());
        }
        info.put("blackList", blackList);
        final List<Map<String, Object>> whiteList = new ArrayList<Map<String, Object>>(this.whiteList.size());
        for (final WallSqlStatValue sqlStatValue : this.whiteList) {
            whiteList.add(sqlStatValue.toMap());
        }
        info.put("whiteList", whiteList);
        return info;
    }
}
