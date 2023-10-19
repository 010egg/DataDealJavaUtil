// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import com.alibaba.druid.support.profile.ProfileEntryStatValue;
import java.util.List;
import java.util.Date;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import com.alibaba.druid.support.monitor.annotation.MField;
import com.alibaba.druid.support.monitor.annotation.MTable;

@MTable(name = "druid_weburi")
public class WebURIStatValue
{
    @MField(groupBy = true, aggregate = AggregateType.None)
    protected String uri;
    @MField(aggregate = AggregateType.Last)
    protected int runningCount;
    @MField(aggregate = AggregateType.Max)
    protected int concurrentMax;
    @MField(aggregate = AggregateType.Sum)
    protected long requestCount;
    @MField(aggregate = AggregateType.Sum)
    protected long requestTimeNano;
    @MField(aggregate = AggregateType.Max)
    protected long requestTimeNanoMax;
    @MField(aggregate = AggregateType.Last)
    protected Date requestTimeNanoMaxOccurTime;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcFetchRowCount;
    @MField(aggregate = AggregateType.Max)
    protected long jdbcFetchRowPeak;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcUpdateCount;
    @MField(aggregate = AggregateType.Max)
    protected long jdbcUpdatePeak;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcExecuteCount;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcExecuteErrorCount;
    @MField(aggregate = AggregateType.Max)
    protected long jdbcExecutePeak;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcExecuteTimeNano;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcCommitCount;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcRollbackCount;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcPoolConnectionOpenCount;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcPoolConnectionCloseCount;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcResultSetOpenCount;
    @MField(aggregate = AggregateType.Sum)
    protected long jdbcResultSetCloseCount;
    @MField(aggregate = AggregateType.Sum)
    protected long errorCount;
    @MField(aggregate = AggregateType.Last)
    protected Date lastAccessTime;
    private List<ProfileEntryStatValue> profileEntryStatValueList;
    @MField(name = "h1", aggregate = AggregateType.Sum)
    protected long histogram_0_1;
    @MField(name = "h10", aggregate = AggregateType.Sum)
    protected long histogram_1_10;
    @MField(name = "h100", aggregate = AggregateType.Sum)
    protected long histogram_10_100;
    @MField(name = "h1000", aggregate = AggregateType.Sum)
    protected long histogram_100_1000;
    @MField(name = "h10000", aggregate = AggregateType.Sum)
    protected int histogram_1000_10000;
    @MField(name = "h100000", aggregate = AggregateType.Sum)
    protected int histogram_10000_100000;
    @MField(name = "h1000000", aggregate = AggregateType.Sum)
    protected int histogram_100000_1000000;
    @MField(name = "hmore", aggregate = AggregateType.Sum)
    protected int histogram_1000000_more;
    
    public WebURIStatValue() {
        this.lastAccessTime = null;
    }
    
    public long[] getHistogram() {
        return new long[] { this.histogram_0_1, this.histogram_1_10, this.histogram_10_100, this.histogram_100_1000, this.histogram_1000_10000, this.histogram_10000_100000, this.histogram_100000_1000000, this.histogram_1000000_more };
    }
    
    public List<ProfileEntryStatValue> getProfileEntryStatValueList() {
        return this.profileEntryStatValueList;
    }
    
    public void setProfileEntryStatValueList(final List<ProfileEntryStatValue> profileEntryStatValueList) {
        this.profileEntryStatValueList = profileEntryStatValueList;
    }
    
    public String getUri() {
        return this.uri;
    }
    
    public void setUri(final String uri) {
        this.uri = uri;
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
    
    public long getRequestTimeNano() {
        return this.requestTimeNano;
    }
    
    public void setRequestTimeNano(final long requestTimeNano) {
        this.requestTimeNano = requestTimeNano;
    }
    
    public long getRequestTimeNanoMax() {
        return this.requestTimeNanoMax;
    }
    
    public void setRequestTimeNanoMax(final long requestTimeNanoMax) {
        this.requestTimeNanoMax = requestTimeNanoMax;
    }
    
    public Date getRequestTimeNanoMaxOccurTime() {
        return this.requestTimeNanoMaxOccurTime;
    }
    
    public void setRequestTimeNanoMaxOccurTime(final long requestTimeNanoMaxOccurTime) {
        if (requestTimeNanoMaxOccurTime > 0L) {
            this.requestTimeNanoMaxOccurTime = new Date(requestTimeNanoMaxOccurTime);
        }
        else {
            this.requestTimeNanoMaxOccurTime = null;
        }
    }
    
    public long getJdbcFetchRowCount() {
        return this.jdbcFetchRowCount;
    }
    
    public void setJdbcFetchRowCount(final long jdbcFetchRowCount) {
        this.jdbcFetchRowCount = jdbcFetchRowCount;
    }
    
    public long getJdbcFetchRowPeak() {
        return this.jdbcFetchRowPeak;
    }
    
    public void setJdbcFetchRowPeak(final long jdbcFetchRowPeak) {
        this.jdbcFetchRowPeak = jdbcFetchRowPeak;
    }
    
    public long getJdbcUpdateCount() {
        return this.jdbcUpdateCount;
    }
    
    public void setJdbcUpdateCount(final long jdbcUpdateCount) {
        this.jdbcUpdateCount = jdbcUpdateCount;
    }
    
    public long getJdbcUpdatePeak() {
        return this.jdbcUpdatePeak;
    }
    
    public void setJdbcUpdatePeak(final long jdbcUpdatePeak) {
        this.jdbcUpdatePeak = jdbcUpdatePeak;
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
    
    public long getJdbcExecutePeak() {
        return this.jdbcExecutePeak;
    }
    
    public void setJdbcExecutePeak(final long jdbcExecutePeak) {
        this.jdbcExecutePeak = jdbcExecutePeak;
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
    
    public long getErrorCount() {
        return this.errorCount;
    }
    
    public void setErrorCount(final long errorCount) {
        this.errorCount = errorCount;
    }
    
    public void setLastAccessTimeMillis(final long lastAccessTimeMillis) {
        if (lastAccessTimeMillis > 0L) {
            this.lastAccessTime = new Date(lastAccessTimeMillis);
        }
        else {
            this.lastAccessTime = null;
        }
    }
    
    public long getRequestTimeMillis() {
        return this.getRequestTimeNano() / 1000000L;
    }
    
    public long getRequestTimeMillisMax() {
        return this.getRequestTimeNanoMax() / 1000000L;
    }
    
    public Date getLastAccessTime() {
        return this.lastAccessTime;
    }
    
    public long getJdbcExecuteTimeMillis() {
        return this.getJdbcExecuteTimeNano() / 1000000L;
    }
    
    public Map<String, Object> getStatData() {
        final Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("URI", this.getUri());
        data.put("RunningCount", this.getRunningCount());
        data.put("ConcurrentMax", this.getConcurrentMax());
        data.put("RequestCount", this.getRequestCount());
        data.put("RequestTimeMillis", this.getRequestTimeMillis());
        data.put("ErrorCount", this.getErrorCount());
        data.put("LastAccessTime", this.getLastAccessTime());
        data.put("JdbcCommitCount", this.getJdbcCommitCount());
        data.put("JdbcRollbackCount", this.getJdbcRollbackCount());
        data.put("JdbcExecuteCount", this.getJdbcExecuteCount());
        data.put("JdbcExecuteErrorCount", this.getJdbcExecuteErrorCount());
        data.put("JdbcExecutePeak", this.getJdbcExecutePeak());
        data.put("JdbcExecuteTimeMillis", this.getJdbcExecuteTimeMillis());
        data.put("JdbcFetchRowCount", this.getJdbcFetchRowCount());
        data.put("JdbcFetchRowPeak", this.getJdbcFetchRowPeak());
        data.put("JdbcUpdateCount", this.getJdbcUpdateCount());
        data.put("JdbcUpdatePeak", this.getJdbcUpdatePeak());
        data.put("JdbcPoolConnectionOpenCount", this.getJdbcPoolConnectionOpenCount());
        data.put("JdbcPoolConnectionCloseCount", this.getJdbcPoolConnectionCloseCount());
        data.put("JdbcResultSetOpenCount", this.getJdbcResultSetOpenCount());
        data.put("JdbcResultSetCloseCount", this.getJdbcResultSetCloseCount());
        data.put("Histogram", this.getHistogram());
        if (this.profileEntryStatValueList != null) {
            final int size = this.profileEntryStatValueList.size();
            final List<Map<String, Object>> profileDataList = new ArrayList<Map<String, Object>>(size);
            for (final ProfileEntryStatValue profileEntryStatValue : this.profileEntryStatValueList) {
                profileDataList.add(profileEntryStatValue.getData());
            }
            data.put("Profiles", profileDataList);
        }
        data.put("RequestTimeMillisMax", this.getRequestTimeMillisMax());
        data.put("RequestTimeMillisMaxOccurTime", this.getRequestTimeNanoMaxOccurTime());
        return data;
    }
}
