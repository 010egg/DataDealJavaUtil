// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Date;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import com.alibaba.druid.support.monitor.annotation.MField;
import com.alibaba.druid.support.monitor.annotation.MTable;

@MTable(name = "druid_springmethod")
public class SpringMethodStatValue
{
    @MField(groupBy = true, aggregate = AggregateType.None)
    private String className;
    @MField(groupBy = true, aggregate = AggregateType.None)
    private String signature;
    @MField(aggregate = AggregateType.Last)
    private int runningCount;
    @MField(aggregate = AggregateType.Max)
    private int concurrentMax;
    @MField(aggregate = AggregateType.Sum)
    private long executeCount;
    @MField(aggregate = AggregateType.Sum)
    private long executeErrorCount;
    @MField(aggregate = AggregateType.Sum)
    private long executeTimeNano;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcFetchRowCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcUpdateCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcExecuteCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcExecuteErrorCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcExecuteTimeNano;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcCommitCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcRollbackCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcPoolConnectionOpenCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcPoolConnectionCloseCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcResultSetOpenCount;
    @MField(aggregate = AggregateType.Sum)
    private long jdbcResultSetCloseCount;
    @MField(aggregate = AggregateType.Last)
    private String lastErrorClass;
    @MField(aggregate = AggregateType.Last)
    private String lastErrorMessage;
    @MField(aggregate = AggregateType.Last)
    private String lastErrorStackTrace;
    @MField(aggregate = AggregateType.Last)
    private long lastErrorTimeMillis;
    @MField(name = "h1", aggregate = AggregateType.Sum)
    long histogram_0_1;
    @MField(name = "h10", aggregate = AggregateType.Sum)
    long histogram_1_10;
    @MField(name = "h100", aggregate = AggregateType.Sum)
    long histogram_10_100;
    @MField(name = "h1000", aggregate = AggregateType.Sum)
    long histogram_100_1000;
    @MField(name = "h10000", aggregate = AggregateType.Sum)
    int histogram_1000_10000;
    @MField(name = "h100000", aggregate = AggregateType.Sum)
    int histogram_10000_100000;
    @MField(name = "h1000000", aggregate = AggregateType.Sum)
    int histogram_100000_1000000;
    @MField(name = "hmore", aggregate = AggregateType.Sum)
    int histogram_1000000_more;
    
    public String getClassName() {
        return this.className;
    }
    
    public void setClassName(final String className) {
        this.className = className;
    }
    
    public String getSignature() {
        return this.signature;
    }
    
    public void setSignature(final String signature) {
        this.signature = signature;
    }
    
    public int getRunningCount() {
        return this.runningCount;
    }
    
    public void setRunningCount(final int runningCount) {
        this.runningCount = runningCount;
    }
    
    public int getConcurrentMax() {
        return this.concurrentMax;
    }
    
    public void setConcurrentMax(final int concurrentMax) {
        this.concurrentMax = concurrentMax;
    }
    
    public long getExecuteCount() {
        return this.executeCount;
    }
    
    public void setExecuteCount(final long executeCount) {
        this.executeCount = executeCount;
    }
    
    public long getExecuteErrorCount() {
        return this.executeErrorCount;
    }
    
    public void setExecuteErrorCount(final long executeErrorCount) {
        this.executeErrorCount = executeErrorCount;
    }
    
    public long getExecuteTimeNano() {
        return this.executeTimeNano;
    }
    
    public void setExecuteTimeNano(final long executeTimeNano) {
        this.executeTimeNano = executeTimeNano;
    }
    
    public long getJdbcFetchRowCount() {
        return this.jdbcFetchRowCount;
    }
    
    public void setJdbcFetchRowCount(final long jdbcFetchRowCount) {
        this.jdbcFetchRowCount = jdbcFetchRowCount;
    }
    
    public long getJdbcUpdateCount() {
        return this.jdbcUpdateCount;
    }
    
    public void setJdbcUpdateCount(final long jdbcUpdateCount) {
        this.jdbcUpdateCount = jdbcUpdateCount;
    }
    
    public long getJdbcExecuteCount() {
        return this.jdbcExecuteCount;
    }
    
    public void setJdbcExecuteCount(final long jdbcExecuteCount) {
        this.jdbcExecuteCount = jdbcExecuteCount;
    }
    
    public long getJdbcExecuteErrorCount() {
        return this.jdbcExecuteErrorCount;
    }
    
    public void setJdbcExecuteErrorCount(final long jdbcExecuteErrorCount) {
        this.jdbcExecuteErrorCount = jdbcExecuteErrorCount;
    }
    
    public long getJdbcExecuteTimeNano() {
        return this.jdbcExecuteTimeNano;
    }
    
    public void setJdbcExecuteTimeNano(final long jdbcExecuteTimeNano) {
        this.jdbcExecuteTimeNano = jdbcExecuteTimeNano;
    }
    
    public long getJdbcCommitCount() {
        return this.jdbcCommitCount;
    }
    
    public void setJdbcCommitCount(final long jdbcCommitCount) {
        this.jdbcCommitCount = jdbcCommitCount;
    }
    
    public long getJdbcRollbackCount() {
        return this.jdbcRollbackCount;
    }
    
    public void setJdbcRollbackCount(final long jdbcRollbackCount) {
        this.jdbcRollbackCount = jdbcRollbackCount;
    }
    
    public long getJdbcPoolConnectionOpenCount() {
        return this.jdbcPoolConnectionOpenCount;
    }
    
    public void setJdbcPoolConnectionOpenCount(final long jdbcPoolConnectionOpenCount) {
        this.jdbcPoolConnectionOpenCount = jdbcPoolConnectionOpenCount;
    }
    
    public long getJdbcPoolConnectionCloseCount() {
        return this.jdbcPoolConnectionCloseCount;
    }
    
    public void setJdbcPoolConnectionCloseCount(final long jdbcPoolConnectionCloseCount) {
        this.jdbcPoolConnectionCloseCount = jdbcPoolConnectionCloseCount;
    }
    
    public long getJdbcResultSetOpenCount() {
        return this.jdbcResultSetOpenCount;
    }
    
    public void setJdbcResultSetOpenCount(final long jdbcResultSetOpenCount) {
        this.jdbcResultSetOpenCount = jdbcResultSetOpenCount;
    }
    
    public long getJdbcResultSetCloseCount() {
        return this.jdbcResultSetCloseCount;
    }
    
    public void setJdbcResultSetCloseCount(final long jdbcResultSetCloseCount) {
        this.jdbcResultSetCloseCount = jdbcResultSetCloseCount;
    }
    
    public void setLastError(final Throwable lastError) {
        if (lastError != null) {
            this.lastErrorClass = lastError.getClass().getName();
            this.lastErrorMessage = lastError.getMessage();
            this.lastErrorStackTrace = Utils.toString(lastError.getStackTrace());
        }
    }
    
    public long getLastErrorTimeMillis() {
        return this.lastErrorTimeMillis;
    }
    
    public void setLastErrorTimeMillis(final long lastErrorTimeMillis) {
        this.lastErrorTimeMillis = lastErrorTimeMillis;
    }
    
    public long getExecuteTimeMillis() {
        return this.getExecuteTimeNano() / 1000000L;
    }
    
    public long getJdbcExecuteTimeMillis() {
        return this.getJdbcExecuteTimeNano() / 1000000L;
    }
    
    public Date getLastErrorTime() {
        if (this.lastErrorTimeMillis <= 0L) {
            return null;
        }
        return new Date(this.lastErrorTimeMillis);
    }
    
    public long[] getHistogram() {
        return new long[] { this.histogram_0_1, this.histogram_1_10, this.histogram_10_100, this.histogram_100_1000, this.histogram_1000_10000, this.histogram_10000_100000, this.histogram_100000_1000000, this.histogram_1000000_more };
    }
    
    public Map<String, Object> getData() {
        final Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("Class", this.getClassName());
        data.put("Method", this.getSignature());
        data.put("RunningCount", this.getRunningCount());
        data.put("ConcurrentMax", this.getConcurrentMax());
        data.put("ExecuteCount", this.getExecuteCount());
        data.put("ExecuteErrorCount", this.getExecuteErrorCount());
        data.put("ExecuteTimeMillis", this.getExecuteTimeMillis());
        data.put("JdbcCommitCount", this.getJdbcCommitCount());
        data.put("JdbcRollbackCount", this.getJdbcRollbackCount());
        data.put("JdbcPoolConnectionOpenCount", this.getJdbcPoolConnectionOpenCount());
        data.put("JdbcPoolConnectionCloseCount", this.getJdbcPoolConnectionCloseCount());
        data.put("JdbcResultSetOpenCount", this.getJdbcResultSetOpenCount());
        data.put("JdbcResultSetCloseCount", this.getJdbcResultSetCloseCount());
        data.put("JdbcExecuteCount", this.getJdbcExecuteCount());
        data.put("JdbcExecuteErrorCount", this.getJdbcExecuteErrorCount());
        data.put("JdbcExecuteTimeMillis", this.getJdbcExecuteTimeMillis());
        data.put("JdbcFetchRowCount", this.getJdbcFetchRowCount());
        data.put("JdbcUpdateCount", this.getJdbcUpdateCount());
        if (this.lastErrorClass == null) {
            data.put("LastError", null);
        }
        else {
            final Map<String, Object> map = new LinkedHashMap<String, Object>(3);
            map.put("Class", this.lastErrorClass);
            map.put("Message", this.lastErrorMessage);
            map.put("StackTrace", this.lastErrorStackTrace);
            data.put("LastError", map);
        }
        data.put("LastErrorTime", this.getLastErrorTime());
        data.put("Histogram", this.getHistogram());
        return data;
    }
}
