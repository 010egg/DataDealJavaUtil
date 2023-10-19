// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Date;

public class WebSessionStatValue
{
    String sessionId;
    int runningCount;
    int concurrentMax;
    long requestCount;
    long requestErrorCount;
    long requestTimeNano;
    long jdbcFetchRowCount;
    long jdbcUpdateCount;
    long jdbcExecuteCount;
    long jdbcExecuteTimeNano;
    long jdbcCommitCount;
    long jdbcRollbackCount;
    long createTimeMillis;
    long lastAccessTimeMillis;
    String remoteAddress;
    String principal;
    String userAgent;
    int requestIntervalHistogram_0_1;
    int requestIntervalHistogram_1_10;
    int requestIntervalHistogram_10_100;
    int requestIntervalHistogram_100_1000;
    int requestIntervalHistogram_1000_10000;
    int requestIntervalHistogram_10000_100000;
    int requestIntervalHistogram_100000_1000000;
    int requestIntervalHistogram_1000000_10000000;
    int requestIntervalHistogram_10000000_more;
    
    public String getSessionId() {
        return this.sessionId;
    }
    
    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
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
    
    public long getRequestCount() {
        return this.requestCount;
    }
    
    public void setRequestCount(final long requestCount) {
        this.requestCount = requestCount;
    }
    
    public long getRequestErrorCount() {
        return this.requestErrorCount;
    }
    
    public void setRequestErrorCount(final long requestErrorCount) {
        this.requestErrorCount = requestErrorCount;
    }
    
    public long getRequestTimeNano() {
        return this.requestTimeNano;
    }
    
    public void setRequestTimeNano(final long requestTimeNano) {
        this.requestTimeNano = requestTimeNano;
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
    
    public long getCreateTimeMillis() {
        return this.createTimeMillis;
    }
    
    public void setCreateTimeMillis(final long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }
    
    public long getLastAccessTimeMillis() {
        return this.lastAccessTimeMillis;
    }
    
    public void setLastAccessTimeMillis(final long lastAccessTimeMillis) {
        this.lastAccessTimeMillis = lastAccessTimeMillis;
    }
    
    public String getRemoteAddress() {
        return this.remoteAddress;
    }
    
    public void setRemoteAddress(final String remoteAddresses) {
        this.remoteAddress = remoteAddresses;
    }
    
    public String getPrincipal() {
        return this.principal;
    }
    
    public void setPrincipal(final String principal) {
        this.principal = principal;
    }
    
    public String getUserAgent() {
        return this.userAgent;
    }
    
    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }
    
    public long getRequestTimeMillis() {
        return this.getRequestTimeNano() / 1000000L;
    }
    
    public Date getCreateTime() {
        if (this.createTimeMillis == -1L) {
            return null;
        }
        return new Date(this.createTimeMillis);
    }
    
    public Date getLastAccessTime() {
        if (this.lastAccessTimeMillis < 0L) {
            return null;
        }
        return new Date(this.lastAccessTimeMillis);
    }
    
    public long getJdbcExecuteTimeMillis() {
        return this.getJdbcExecuteTimeNano() / 1000000L;
    }
    
    public long[] getRequestInterval() {
        return new long[] { this.requestIntervalHistogram_0_1, this.requestIntervalHistogram_1_10, this.requestIntervalHistogram_10_100, this.requestIntervalHistogram_100_1000, this.requestIntervalHistogram_1000_10000, this.requestIntervalHistogram_10000_100000, this.requestIntervalHistogram_100000_1000000, this.requestIntervalHistogram_1000000_10000000, this.requestIntervalHistogram_10000000_more };
    }
    
    public Map<String, Object> getStatData() {
        final Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("SESSIONID", this.getSessionId());
        data.put("Principal", this.getPrincipal());
        data.put("RunningCount", this.getRunningCount());
        data.put("ConcurrentMax", this.getConcurrentMax());
        data.put("RequestCount", this.getRequestCount());
        data.put("RequestTimeMillisTotal", this.getRequestTimeMillis());
        data.put("CreateTime", this.getCreateTime());
        data.put("LastAccessTime", this.getLastAccessTime());
        data.put("RemoteAddress", this.getRemoteAddress());
        data.put("Principal", this.getPrincipal());
        data.put("JdbcCommitCount", this.getJdbcCommitCount());
        data.put("JdbcRollbackCount", this.getJdbcRollbackCount());
        data.put("JdbcExecuteCount", this.getJdbcExecuteCount());
        data.put("JdbcExecuteTimeMillis", this.getJdbcExecuteTimeMillis());
        data.put("JdbcFetchRowCount", this.getJdbcFetchRowCount());
        data.put("JdbcUpdateCount", this.getJdbcUpdateCount());
        data.put("UserAgent", this.getUserAgent());
        data.put("RequestInterval", this.getRequestInterval());
        return data;
    }
}
