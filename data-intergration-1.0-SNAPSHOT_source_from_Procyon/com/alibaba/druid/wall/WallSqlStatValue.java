// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.LinkedHashMap;
import java.util.Map;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import com.alibaba.druid.support.monitor.annotation.MField;
import com.alibaba.druid.support.monitor.annotation.MTable;

@MTable(name = "druid_wall_sql")
public class WallSqlStatValue
{
    private String sql;
    @MField(groupBy = true, aggregate = AggregateType.None, hashFor = "sql", hashForType = "sql")
    private long sqlHash;
    private String sqlSample;
    @MField(groupBy = true, aggregate = AggregateType.None, hashFor = "sqlSample", hashForType = "sqlSample")
    private long sqlSampleHash;
    @MField(aggregate = AggregateType.Sum)
    private long executeCount;
    @MField(aggregate = AggregateType.Sum)
    private long executeErrorCount;
    @MField(aggregate = AggregateType.Sum)
    private long fetchRowCount;
    @MField(aggregate = AggregateType.Sum)
    private long updateCount;
    @MField(aggregate = AggregateType.Last)
    private boolean syntaxError;
    @MField(aggregate = AggregateType.Last)
    private String violationMessage;
    
    public String getSql() {
        return this.sql;
    }
    
    public void setSql(final String sql) {
        this.sql = sql;
    }
    
    public long getSqlHash() {
        return this.sqlHash;
    }
    
    public void setSqlHash(final long sqlHash) {
        this.sqlHash = sqlHash;
    }
    
    public String getSqlSample() {
        return this.sqlSample;
    }
    
    public void setSqlSample(final String sqlSample) {
        this.sqlSample = sqlSample;
    }
    
    public long getExecuteCount() {
        return this.executeCount;
    }
    
    public void setExecuteCount(final long executeCount) {
        this.executeCount = executeCount;
    }
    
    public long getFetchRowCount() {
        return this.fetchRowCount;
    }
    
    public void setFetchRowCount(final long fetchRowCount) {
        this.fetchRowCount = fetchRowCount;
    }
    
    public long getUpdateCount() {
        return this.updateCount;
    }
    
    public void setUpdateCount(final long updateCount) {
        this.updateCount = updateCount;
    }
    
    public boolean isSyntaxError() {
        return this.syntaxError;
    }
    
    public void setSyntaxError(final boolean syntaxError) {
        this.syntaxError = syntaxError;
    }
    
    public String getViolationMessage() {
        return this.violationMessage;
    }
    
    public void setViolationMessage(final String violationMessage) {
        this.violationMessage = violationMessage;
    }
    
    public long getExecuteErrorCount() {
        return this.executeErrorCount;
    }
    
    public void setExecuteErrorCount(final long executeErrorCount) {
        this.executeErrorCount = executeErrorCount;
    }
    
    public Map<String, Object> toMap() {
        final Map<String, Object> sqlStatMap = new LinkedHashMap<String, Object>();
        sqlStatMap.put("sql", this.sql);
        if (!this.sql.equals(this.sqlSample)) {
            sqlStatMap.put("sample", this.sqlSample);
        }
        sqlStatMap.put("executeCount", this.getExecuteCount());
        if (this.executeErrorCount > 0L) {
            sqlStatMap.put("executeErrorCount", this.executeErrorCount);
        }
        if (this.fetchRowCount > 0L) {
            sqlStatMap.put("fetchRowCount", this.fetchRowCount);
        }
        if (this.updateCount > 0L) {
            sqlStatMap.put("updateCount", this.updateCount);
        }
        if (this.violationMessage != null) {
            sqlStatMap.put("violationMessage", this.violationMessage);
        }
        return sqlStatMap;
    }
}
