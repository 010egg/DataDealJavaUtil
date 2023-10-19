// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

public class WebRequestStat
{
    private long startNano;
    private long startMillis;
    private long endNano;
    private long jdbcCommitCount;
    private long jdbcRollbackCount;
    private long jdbcExecuteCount;
    private long jdbcUpdateCount;
    private long jdbcFetchRowCount;
    private long jdbcExecuteNano;
    private long jdbcExecuteErrorCount;
    private long jdbcPoolConnectCount;
    private long jdbcPoolCloseCount;
    private long jdbcResultSetOpenCount;
    private long jdbcResultSetCloseCount;
    private static ThreadLocal<WebRequestStat> localRequestStat;
    
    public WebRequestStat() {
    }
    
    public WebRequestStat(final long startNano) {
        this.startNano = startNano;
    }
    
    public WebRequestStat(final long startNano, final long startMillis) {
        this.startNano = startNano;
        this.startMillis = startMillis;
    }
    
    public static WebRequestStat current() {
        return WebRequestStat.localRequestStat.get();
    }
    
    public static void set(final WebRequestStat requestStat) {
        WebRequestStat.localRequestStat.set(requestStat);
    }
    
    public long getStartNano() {
        return this.startNano;
    }
    
    public void setStartNano(final long startNano) {
        this.startNano = startNano;
    }
    
    public long getStartMillis() {
        return this.startMillis;
    }
    
    public void setStartMillis(final long startMillis) {
        this.startMillis = startMillis;
    }
    
    public long getEndNano() {
        return this.endNano;
    }
    
    public void setEndNano(final long endNano) {
        this.endNano = endNano;
    }
    
    public void addJdbcUpdateCount(final long count) {
        this.jdbcUpdateCount += count;
    }
    
    public void addJdbcFetchRowCount(final long count) {
        this.jdbcFetchRowCount += count;
    }
    
    public void incrementJdbcExecuteCount() {
        ++this.jdbcExecuteCount;
    }
    
    public long getJdbcExecuteCount() {
        return this.jdbcExecuteCount;
    }
    
    public long getJdbcExecuteTimeNano() {
        return this.jdbcExecuteNano;
    }
    
    public void addJdbcExecuteTimeNano(final long nano) {
        this.jdbcExecuteNano += nano;
    }
    
    public long getJdbcUpdateCount() {
        return this.jdbcUpdateCount;
    }
    
    public long getJdbcFetchRowCount() {
        return this.jdbcFetchRowCount;
    }
    
    public long getJdbcCommitCount() {
        return this.jdbcCommitCount;
    }
    
    public void incrementJdbcCommitCount() {
        ++this.jdbcCommitCount;
    }
    
    public long getJdbcRollbackCount() {
        return this.jdbcRollbackCount;
    }
    
    public void incrementJdbcRollbackCount() {
        ++this.jdbcRollbackCount;
    }
    
    public long getJdbcExecuteErrorCount() {
        return this.jdbcExecuteErrorCount;
    }
    
    public void incrementJdbcExecuteErrorCount() {
        ++this.jdbcExecuteErrorCount;
    }
    
    public long getJdbcPoolConnectionOpenCount() {
        return this.jdbcPoolConnectCount;
    }
    
    public void incrementJdbcPoolConnectCount() {
        ++this.jdbcPoolConnectCount;
    }
    
    public long getJdbcPoolConnectionCloseCount() {
        return this.jdbcPoolCloseCount;
    }
    
    public void incrementJdbcPoolCloseCount() {
        ++this.jdbcPoolCloseCount;
    }
    
    public long getJdbcResultSetOpenCount() {
        return this.jdbcResultSetOpenCount;
    }
    
    public void incrementJdbcResultSetOpenCount() {
        ++this.jdbcResultSetOpenCount;
    }
    
    public long getJdbcResultSetCloseCount() {
        return this.jdbcResultSetCloseCount;
    }
    
    public void incrementJdbcResultSetCloseCount() {
        ++this.jdbcResultSetCloseCount;
    }
    
    static {
        WebRequestStat.localRequestStat = new ThreadLocal<WebRequestStat>();
    }
}
