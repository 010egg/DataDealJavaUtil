// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import com.alibaba.druid.support.logging.Log;

public class WebSessionStat
{
    private static final Log LOG;
    private final String sessionId;
    private volatile int runningCount;
    private volatile int concurrentMax;
    static final AtomicIntegerFieldUpdater<WebSessionStat> runningCountUpdater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> concurrentMaxUpdater;
    private volatile long requestCount;
    private volatile long requestErrorCount;
    private volatile long requestTimeNano;
    static final AtomicLongFieldUpdater<WebSessionStat> requestCountUpdater;
    static final AtomicLongFieldUpdater<WebSessionStat> requestErrorCountUpdater;
    static final AtomicLongFieldUpdater<WebSessionStat> requestTimeNanoUpdater;
    private volatile long jdbcFetchRowCount;
    private volatile long jdbcUpdateCount;
    private volatile long jdbcExecuteCount;
    private volatile long jdbcExecuteTimeNano;
    static final AtomicLongFieldUpdater<WebSessionStat> jdbcFetchRowCountUpdater;
    static final AtomicLongFieldUpdater<WebSessionStat> jdbcUpdateCountUpdater;
    static final AtomicLongFieldUpdater<WebSessionStat> jdbcExecuteCountUpdater;
    static final AtomicLongFieldUpdater<WebSessionStat> jdbcExecuteTimeNanoUpdater;
    private volatile long jdbcCommitCount;
    private volatile long jdbcRollbackCount;
    static final AtomicLongFieldUpdater<WebSessionStat> jdbcCommitCountUpdater;
    static final AtomicLongFieldUpdater<WebSessionStat> jdbcRollbackCountUpdater;
    private long createTimeMillis;
    private volatile long lastAccessTimeMillis;
    private String remoteAddresses;
    private String principal;
    private String userAgent;
    private volatile int requestIntervalHistogram_0_1;
    private volatile int requestIntervalHistogram_1_10;
    private volatile int requestIntervalHistogram_10_100;
    private volatile int requestIntervalHistogram_100_1000;
    private volatile int requestIntervalHistogram_1000_10000;
    private volatile int requestIntervalHistogram_10000_100000;
    private volatile int requestIntervalHistogram_100000_1000000;
    private volatile int requestIntervalHistogram_1000000_10000000;
    private volatile int requestIntervalHistogram_10000000_more;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_0_1_Updater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_1_10_Updater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_10_100_Updater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_10000_100000_Updater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_100000_1000000_Updater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_1000000_10000000_Updater;
    static final AtomicIntegerFieldUpdater<WebSessionStat> requestIntervalHistogram_10000000_more_Updater;
    
    public WebSessionStat(final String sessionId) {
        this.createTimeMillis = -1L;
        this.lastAccessTimeMillis = -1L;
        this.principal = null;
        this.sessionId = sessionId;
    }
    
    public void reset() {
        WebSessionStat.concurrentMaxUpdater.set(this, 0);
        WebSessionStat.requestCountUpdater.set(this, 0L);
        WebSessionStat.requestErrorCountUpdater.set(this, 0L);
        WebSessionStat.requestTimeNanoUpdater.set(this, 0L);
        WebSessionStat.jdbcFetchRowCountUpdater.set(this, 0L);
        WebSessionStat.jdbcUpdateCountUpdater.set(this, 0L);
        WebSessionStat.jdbcExecuteCountUpdater.set(this, 0L);
        WebSessionStat.jdbcExecuteTimeNanoUpdater.set(this, 0L);
        WebSessionStat.jdbcCommitCountUpdater.set(this, 0L);
        WebSessionStat.jdbcRollbackCountUpdater.set(this, 0L);
        this.remoteAddresses = null;
        this.principal = null;
        WebSessionStat.requestIntervalHistogram_0_1_Updater.set(this, 0);
        WebSessionStat.requestIntervalHistogram_1_10_Updater.set(this, 0);
        WebSessionStat.requestIntervalHistogram_10_100_Updater.set(this, 0);
        WebSessionStat.requestIntervalHistogram_100_1000_Updater.set(this, 0);
        WebSessionStat.requestIntervalHistogram_1000_10000_Updater.set(this, 0);
        WebSessionStat.requestIntervalHistogram_10000_100000_Updater.set(this, 0);
        WebSessionStat.requestIntervalHistogram_100000_1000000_Updater.set(this, 0);
        WebSessionStat.requestIntervalHistogram_1000000_10000000_Updater.set(this, 0);
        WebSessionStat.requestIntervalHistogram_10000000_more_Updater.set(this, 0);
    }
    
    public String getUserAgent() {
        return this.userAgent;
    }
    
    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getSessionId() {
        return this.sessionId;
    }
    
    public long getCreateTimeMillis() {
        return this.createTimeMillis;
    }
    
    public String getPrincipal() {
        return this.principal;
    }
    
    public void setPrincipal(final String principal) {
        this.principal = principal;
    }
    
    public void setCreateTimeMillis(final long createTimeMillis) {
        this.createTimeMillis = createTimeMillis;
    }
    
    public long getLastAccessTimeMillis() {
        return this.lastAccessTimeMillis;
    }
    
    public String getRemoteAddress() {
        return this.remoteAddresses;
    }
    
    public void setLastAccessTimeMillis(final long lastAccessTimeMillis) {
        if (this.lastAccessTimeMillis > 0L) {
            final long interval = lastAccessTimeMillis - this.lastAccessTimeMillis;
            this.requestIntervalHistogramRecord(interval);
        }
        this.lastAccessTimeMillis = lastAccessTimeMillis;
    }
    
    private void requestIntervalHistogramRecord(final long nanoSpan) {
        final long millis = nanoSpan / 1000L / 1000L;
        if (millis < 1L) {
            WebSessionStat.requestIntervalHistogram_0_1_Updater.incrementAndGet(this);
        }
        else if (millis < 10L) {
            WebSessionStat.requestIntervalHistogram_1_10_Updater.incrementAndGet(this);
        }
        else if (millis < 100L) {
            WebSessionStat.requestIntervalHistogram_10_100_Updater.incrementAndGet(this);
        }
        else if (millis < 1000L) {
            WebSessionStat.requestIntervalHistogram_100_1000_Updater.incrementAndGet(this);
        }
        else if (millis < 10000L) {
            WebSessionStat.requestIntervalHistogram_1000_10000_Updater.incrementAndGet(this);
        }
        else if (millis < 100000L) {
            WebSessionStat.requestIntervalHistogram_10000_100000_Updater.incrementAndGet(this);
        }
        else if (millis < 1000000L) {
            WebSessionStat.requestIntervalHistogram_100000_1000000_Updater.incrementAndGet(this);
        }
        else if (millis < 10000000L) {
            WebSessionStat.requestIntervalHistogram_1000000_10000000_Updater.incrementAndGet(this);
        }
        else {
            WebSessionStat.requestIntervalHistogram_10000000_more_Updater.incrementAndGet(this);
        }
    }
    
    public void beforeInvoke() {
        final int running = WebSessionStat.runningCountUpdater.incrementAndGet(this);
        int max;
        do {
            max = WebSessionStat.concurrentMaxUpdater.get(this);
        } while (running > max && !WebSessionStat.concurrentMaxUpdater.compareAndSet(this, max, running));
        this.incrementRequestCount();
        final WebRequestStat requestStat = WebRequestStat.current();
        if (requestStat != null) {
            this.setLastAccessTimeMillis(requestStat.getStartMillis());
        }
    }
    
    public void incrementRequestCount() {
        WebSessionStat.requestCountUpdater.incrementAndGet(this);
    }
    
    public void afterInvoke(final Throwable error, final long nanos) {
        WebSessionStat.runningCountUpdater.decrementAndGet(this);
        this.reacord(nanos);
    }
    
    public void reacord(final long nanos) {
        WebSessionStat.requestTimeNanoUpdater.addAndGet(this, nanos);
        final WebRequestStat requestStat = WebRequestStat.current();
        if (requestStat != null) {
            this.addJdbcExecuteCount(requestStat.getJdbcExecuteCount());
            this.addJdbcFetchRowCount(requestStat.getJdbcFetchRowCount());
            this.addJdbcUpdateCount(requestStat.getJdbcUpdateCount());
            this.addJdbcCommitCount(requestStat.getJdbcCommitCount());
            this.addJdbcRollbackCount(requestStat.getJdbcRollbackCount());
            this.addJdbcExecuteTimeNano(requestStat.getJdbcExecuteTimeNano());
        }
    }
    
    public void addRemoteAddress(final String ip) {
        if (this.remoteAddresses == null) {
            this.remoteAddresses = ip;
            return;
        }
        if (this.remoteAddresses.contains(ip)) {
            return;
        }
        if (this.remoteAddresses.length() > 256) {
            return;
        }
        this.remoteAddresses = this.remoteAddresses + ';' + ip;
    }
    
    public int getRunningCount() {
        return this.runningCount;
    }
    
    public long getConcurrentMax() {
        return this.concurrentMax;
    }
    
    public long getRequestCount() {
        return this.requestCount;
    }
    
    public long getRequestErrorCount() {
        return this.requestErrorCount;
    }
    
    public long getRequestTimeNano() {
        return this.requestTimeNano;
    }
    
    public void addJdbcFetchRowCount(final long delta) {
        WebSessionStat.jdbcFetchRowCountUpdater.addAndGet(this, delta);
    }
    
    public long getJdbcFetchRowCount() {
        return this.jdbcFetchRowCount;
    }
    
    public void addJdbcUpdateCount(final long updateCount) {
        WebSessionStat.jdbcUpdateCountUpdater.addAndGet(this, updateCount);
    }
    
    public long getJdbcUpdateCount() {
        return this.jdbcUpdateCount;
    }
    
    public void incrementJdbcExecuteCount() {
        WebSessionStat.jdbcExecuteCountUpdater.incrementAndGet(this);
    }
    
    public void addJdbcExecuteCount(final long executeCount) {
        WebSessionStat.jdbcExecuteCountUpdater.addAndGet(this, executeCount);
    }
    
    public long getJdbcExecuteCount() {
        return this.jdbcExecuteCount;
    }
    
    public long getJdbcExecuteTimeNano() {
        return this.jdbcExecuteTimeNano;
    }
    
    public void addJdbcExecuteTimeNano(final long nano) {
        WebSessionStat.jdbcExecuteTimeNanoUpdater.addAndGet(this, nano);
    }
    
    public void incrementJdbcCommitCount() {
        WebSessionStat.jdbcCommitCountUpdater.incrementAndGet(this);
    }
    
    public long getJdbcCommitCount() {
        return this.jdbcCommitCount;
    }
    
    public void addJdbcCommitCount(final long commitCount) {
        WebSessionStat.jdbcCommitCountUpdater.addAndGet(this, commitCount);
    }
    
    public void incrementJdbcRollbackCount() {
        WebSessionStat.jdbcRollbackCountUpdater.incrementAndGet(this);
    }
    
    public long getJdbcRollbackCount() {
        return this.jdbcRollbackCount;
    }
    
    public void addJdbcRollbackCount(final long rollbackCount) {
        WebSessionStat.jdbcRollbackCountUpdater.addAndGet(this, rollbackCount);
    }
    
    public long[] getRequestInterval() {
        return new long[] { this.requestIntervalHistogram_0_1, this.requestIntervalHistogram_1_10, this.requestIntervalHistogram_10_100, this.requestIntervalHistogram_100_1000, this.requestIntervalHistogram_1000_10000, this.requestIntervalHistogram_10000_100000, this.requestIntervalHistogram_100000_1000000, this.requestIntervalHistogram_1000000_10000000, this.requestIntervalHistogram_10000000_more };
    }
    
    public Map<String, Object> getStatData() {
        return this.getValue(false).getStatData();
    }
    
    public WebSessionStatValue getValue(final boolean reset) {
        final WebSessionStatValue val = new WebSessionStatValue();
        val.sessionId = this.sessionId;
        val.runningCount = this.getRunningCount();
        val.concurrentMax = JdbcSqlStatUtils.get(this, WebSessionStat.concurrentMaxUpdater, reset);
        val.requestCount = JdbcSqlStatUtils.get(this, WebSessionStat.requestCountUpdater, reset);
        val.requestErrorCount = JdbcSqlStatUtils.get(this, WebSessionStat.requestErrorCountUpdater, reset);
        val.requestTimeNano = JdbcSqlStatUtils.get(this, WebSessionStat.requestTimeNanoUpdater, reset);
        val.jdbcFetchRowCount = JdbcSqlStatUtils.get(this, WebSessionStat.jdbcFetchRowCountUpdater, reset);
        val.jdbcUpdateCount = JdbcSqlStatUtils.get(this, WebSessionStat.jdbcUpdateCountUpdater, reset);
        val.jdbcExecuteCount = JdbcSqlStatUtils.get(this, WebSessionStat.jdbcExecuteCountUpdater, reset);
        val.jdbcExecuteTimeNano = JdbcSqlStatUtils.get(this, WebSessionStat.jdbcExecuteTimeNanoUpdater, reset);
        val.jdbcCommitCount = JdbcSqlStatUtils.get(this, WebSessionStat.jdbcCommitCountUpdater, reset);
        val.jdbcRollbackCount = JdbcSqlStatUtils.get(this, WebSessionStat.jdbcRollbackCountUpdater, reset);
        val.createTimeMillis = this.createTimeMillis;
        val.lastAccessTimeMillis = this.lastAccessTimeMillis;
        val.remoteAddress = this.remoteAddresses;
        val.principal = this.principal;
        val.userAgent = this.userAgent;
        val.requestIntervalHistogram_0_1 = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_0_1_Updater, reset);
        val.requestIntervalHistogram_1_10 = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_1_10_Updater, reset);
        val.requestIntervalHistogram_10_100 = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_10_100_Updater, reset);
        val.requestIntervalHistogram_100_1000 = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_100_1000_Updater, reset);
        val.requestIntervalHistogram_1000_10000 = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_1000_10000_Updater, reset);
        val.requestIntervalHistogram_10000_100000 = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_10000_100000_Updater, reset);
        val.requestIntervalHistogram_100000_1000000 = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_100000_1000000_Updater, reset);
        val.requestIntervalHistogram_1000000_10000000 = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_1000000_10000000_Updater, reset);
        val.requestIntervalHistogram_10000000_more = JdbcSqlStatUtils.get(this, WebSessionStat.requestIntervalHistogram_10000000_more_Updater, reset);
        return val;
    }
    
    static {
        LOG = LogFactory.getLog(WebSessionStat.class);
        runningCountUpdater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "runningCount");
        concurrentMaxUpdater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "concurrentMax");
        requestCountUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "requestCount");
        requestErrorCountUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "requestErrorCount");
        requestTimeNanoUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "requestTimeNano");
        jdbcFetchRowCountUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "jdbcFetchRowCount");
        jdbcUpdateCountUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "jdbcUpdateCount");
        jdbcExecuteCountUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "jdbcExecuteCount");
        jdbcExecuteTimeNanoUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "jdbcExecuteTimeNano");
        jdbcCommitCountUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "jdbcCommitCount");
        jdbcRollbackCountUpdater = AtomicLongFieldUpdater.newUpdater(WebSessionStat.class, "jdbcRollbackCount");
        requestIntervalHistogram_0_1_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_0_1");
        requestIntervalHistogram_1_10_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_1_10");
        requestIntervalHistogram_10_100_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_10_100");
        requestIntervalHistogram_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_100_1000");
        requestIntervalHistogram_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_1000_10000");
        requestIntervalHistogram_10000_100000_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_10000_100000");
        requestIntervalHistogram_100000_1000000_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_100000_1000000");
        requestIntervalHistogram_1000000_10000000_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_1000000_10000000");
        requestIntervalHistogram_10000000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(WebSessionStat.class, "requestIntervalHistogram_10000000_more");
    }
}
