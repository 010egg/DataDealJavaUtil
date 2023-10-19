// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class WallSqlStat
{
    private volatile long executeCount;
    private volatile long executeErrorCount;
    private volatile long fetchRowCount;
    private volatile long updateCount;
    static final AtomicLongFieldUpdater<WallSqlStat> executeCountUpdater;
    static final AtomicLongFieldUpdater<WallSqlStat> executeErrorCountUpdater;
    static final AtomicLongFieldUpdater<WallSqlStat> fetchRowCountUpdater;
    static final AtomicLongFieldUpdater<WallSqlStat> updateCountUpdater;
    private final Map<String, WallSqlTableStat> tableStats;
    private final List<Violation> violations;
    private final Map<String, WallSqlFunctionStat> functionStats;
    private final boolean syntaxError;
    private String sample;
    private long sqlHash;
    
    public WallSqlStat(final Map<String, WallSqlTableStat> tableStats, final Map<String, WallSqlFunctionStat> functionStats, final boolean syntaxError) {
        this(tableStats, functionStats, Collections.emptyList(), syntaxError);
    }
    
    public WallSqlStat(final Map<String, WallSqlTableStat> tableStats, final Map<String, WallSqlFunctionStat> functionStats, final List<Violation> violations, final boolean syntaxError) {
        this.violations = violations;
        this.tableStats = tableStats;
        this.functionStats = functionStats;
        this.syntaxError = syntaxError;
    }
    
    public long getSqlHash() {
        return this.sqlHash;
    }
    
    public void setSqlHash(final long sqlHash) {
        this.sqlHash = sqlHash;
    }
    
    public String getSample() {
        return this.sample;
    }
    
    public void setSample(final String sample) {
        this.sample = sample;
    }
    
    public long incrementAndGetExecuteCount() {
        return WallSqlStat.executeCountUpdater.incrementAndGet(this);
    }
    
    public long incrementAndGetExecuteErrorCount() {
        return WallSqlStat.executeErrorCountUpdater.incrementAndGet(this);
    }
    
    public long getExecuteCount() {
        return this.executeCount;
    }
    
    public long getExecuteErrorCount() {
        return this.executeErrorCount;
    }
    
    public long addAndFetchRowCount(final long delta) {
        return WallSqlStat.fetchRowCountUpdater.addAndGet(this, delta);
    }
    
    public long getEffectRowCount() {
        return this.fetchRowCount;
    }
    
    public long getUpdateCount() {
        return this.updateCount;
    }
    
    public void addUpdateCount(final long delta) {
        WallSqlStat.updateCountUpdater.addAndGet(this, delta);
    }
    
    public Map<String, WallSqlTableStat> getTableStats() {
        return this.tableStats;
    }
    
    public Map<String, WallSqlFunctionStat> getFunctionStats() {
        return this.functionStats;
    }
    
    public List<Violation> getViolations() {
        return this.violations;
    }
    
    public boolean isSyntaxError() {
        return this.syntaxError;
    }
    
    public WallSqlStatValue getStatValue(final boolean reset) {
        final WallSqlStatValue statValue = new WallSqlStatValue();
        statValue.setExecuteCount(JdbcSqlStatUtils.get(this, WallSqlStat.executeCountUpdater, reset));
        statValue.setExecuteErrorCount(JdbcSqlStatUtils.get(this, WallSqlStat.executeErrorCountUpdater, reset));
        statValue.setFetchRowCount(JdbcSqlStatUtils.get(this, WallSqlStat.fetchRowCountUpdater, reset));
        statValue.setUpdateCount(JdbcSqlStatUtils.get(this, WallSqlStat.updateCountUpdater, reset));
        statValue.setSyntaxError(this.syntaxError);
        statValue.setSqlSample(this.sample);
        if (this.violations.size() > 0) {
            final String violationMessage = this.violations.get(0).getMessage();
            statValue.setViolationMessage(violationMessage);
        }
        return statValue;
    }
    
    static {
        executeCountUpdater = AtomicLongFieldUpdater.newUpdater(WallSqlStat.class, "executeCount");
        executeErrorCountUpdater = AtomicLongFieldUpdater.newUpdater(WallSqlStat.class, "executeErrorCount");
        fetchRowCountUpdater = AtomicLongFieldUpdater.newUpdater(WallSqlStat.class, "fetchRowCount");
        updateCountUpdater = AtomicLongFieldUpdater.newUpdater(WallSqlStat.class, "updateCount");
    }
}
