// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

import java.util.Map;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.Date;
import com.alibaba.druid.support.profile.ProfileStat;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class WebURIStat
{
    private final String uri;
    private volatile int runningCount;
    private volatile int concurrentMax;
    private volatile long requestCount;
    private volatile long requestTimeNano;
    private volatile long requestTimeNanoMax;
    private volatile long requestTimeNanoMaxOccurTime;
    static final AtomicIntegerFieldUpdater<WebURIStat> runningCountUpdater;
    static final AtomicIntegerFieldUpdater<WebURIStat> concurrentMaxUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> requestCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> requestTimeNanoUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> requestTimeNanoMaxUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> requestTimeNanoMaxOccurTimeUpdater;
    private volatile long jdbcFetchRowCount;
    private volatile long jdbcFetchRowPeak;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcFetchRowCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcFetchRowPeakUpdater;
    private volatile long jdbcUpdateCount;
    private volatile long jdbcUpdatePeak;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcUpdateCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcUpdatePeakUpdater;
    private volatile long jdbcExecuteCount;
    private volatile long jdbcExecuteErrorCount;
    private volatile long jdbcExecutePeak;
    private volatile long jdbcExecuteTimeNano;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcExecuteCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcExecuteErrorCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcExecutePeakUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcExecuteTimeNanoUpdater;
    private volatile long jdbcCommitCount;
    private volatile long jdbcRollbackCount;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcCommitCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcRollbackCountUpdater;
    private volatile long jdbcPoolConnectionOpenCount;
    private volatile long jdbcPoolConnectionCloseCount;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcPoolConnectionOpenCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcPoolConnectionCloseCountUpdater;
    private volatile long jdbcResultSetOpenCount;
    private volatile long jdbcResultSetCloseCount;
    private volatile long errorCount;
    private volatile long lastAccessTimeMillis;
    private volatile ProfileStat profiletat;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcResultSetOpenCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> jdbcResultSetCloseCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> errorCountUpdater;
    static final AtomicLongFieldUpdater<WebURIStat> lastAccessTimeMillisUpdater;
    private static final ThreadLocal<WebURIStat> currentLocal;
    private volatile long histogram_0_1;
    private volatile long histogram_1_10;
    private volatile long histogram_10_100;
    private volatile long histogram_100_1000;
    private volatile int histogram_1000_10000;
    private volatile int histogram_10000_100000;
    private volatile int histogram_100000_1000000;
    private volatile int histogram_1000000_more;
    static final AtomicLongFieldUpdater<WebURIStat> histogram_0_1_Updater;
    static final AtomicLongFieldUpdater<WebURIStat> histogram_1_10_Updater;
    static final AtomicLongFieldUpdater<WebURIStat> histogram_10_100_Updater;
    static final AtomicLongFieldUpdater<WebURIStat> histogram_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<WebURIStat> histogram_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<WebURIStat> histogram_10000_100000_Updater;
    static final AtomicIntegerFieldUpdater<WebURIStat> histogram_100000_1000000_Updater;
    static final AtomicIntegerFieldUpdater<WebURIStat> histogram_1000000_more_Updater;
    
    public WebURIStat(final String uri) {
        this.lastAccessTimeMillis = -1L;
        this.profiletat = new ProfileStat();
        this.uri = uri;
    }
    
    public static WebURIStat current() {
        return WebURIStat.currentLocal.get();
    }
    
    public String getUri() {
        return this.uri;
    }
    
    public void beforeInvoke() {
        WebURIStat.currentLocal.set(this);
        final int running = WebURIStat.runningCountUpdater.incrementAndGet(this);
        int max;
        do {
            max = WebURIStat.concurrentMaxUpdater.get(this);
        } while (running > max && !WebURIStat.concurrentMaxUpdater.compareAndSet(this, max, running));
        WebURIStat.requestCountUpdater.incrementAndGet(this);
        final WebRequestStat requestStat = WebRequestStat.current();
        if (requestStat != null) {
            this.setLastAccessTimeMillis(requestStat.getStartMillis());
        }
    }
    
    public void afterInvoke(final Throwable error, final long nanos) {
        WebURIStat.runningCountUpdater.decrementAndGet(this);
        WebURIStat.requestTimeNanoUpdater.addAndGet(this, nanos);
        while (true) {
            long current;
            do {
                current = WebURIStat.requestTimeNanoMaxUpdater.get(this);
                if (current < nanos) {
                    continue;
                }
                this.histogramRecord(nanos);
                if (error != null) {
                    WebURIStat.errorCountUpdater.incrementAndGet(this);
                }
                final WebRequestStat localStat = WebRequestStat.current();
                if (localStat != null) {
                    final long fetchRowCount = localStat.getJdbcFetchRowCount();
                    this.addJdbcFetchRowCount(fetchRowCount);
                    long peak;
                    do {
                        peak = WebURIStat.jdbcFetchRowPeakUpdater.get(this);
                        if (fetchRowCount <= peak) {
                            break;
                        }
                    } while (!WebURIStat.jdbcFetchRowPeakUpdater.compareAndSet(this, peak, fetchRowCount));
                    final long executeCount = localStat.getJdbcExecuteCount();
                    this.addJdbcExecuteCount(executeCount);
                    do {
                        peak = WebURIStat.jdbcExecutePeakUpdater.get(this);
                        if (executeCount <= peak) {
                            break;
                        }
                    } while (!WebURIStat.jdbcExecutePeakUpdater.compareAndSet(this, peak, executeCount));
                    final long updateCount = localStat.getJdbcUpdateCount();
                    this.addJdbcUpdateCount(updateCount);
                    do {
                        peak = WebURIStat.jdbcUpdatePeakUpdater.get(this);
                        if (updateCount <= peak) {
                            break;
                        }
                    } while (!WebURIStat.jdbcUpdatePeakUpdater.compareAndSet(this, peak, updateCount));
                    WebURIStat.jdbcExecuteErrorCountUpdater.addAndGet(this, localStat.getJdbcExecuteErrorCount());
                    WebURIStat.jdbcExecuteTimeNanoUpdater.addAndGet(this, localStat.getJdbcExecuteTimeNano());
                    this.addJdbcPoolConnectionOpenCount(localStat.getJdbcPoolConnectionOpenCount());
                    this.addJdbcPoolConnectionCloseCount(localStat.getJdbcPoolConnectionCloseCount());
                    this.addJdbcResultSetOpenCount(localStat.getJdbcResultSetOpenCount());
                    this.addJdbcResultSetCloseCount(localStat.getJdbcResultSetCloseCount());
                }
                WebURIStat.currentLocal.set(null);
                return;
            } while (!WebURIStat.requestTimeNanoMaxUpdater.compareAndSet(this, current, nanos));
            this.requestTimeNanoMaxOccurTime = System.currentTimeMillis();
            continue;
        }
    }
    
    private void histogramRecord(final long nanos) {
        final long millis = nanos / 1000L / 1000L;
        if (millis < 1L) {
            WebURIStat.histogram_0_1_Updater.incrementAndGet(this);
        }
        else if (millis < 10L) {
            WebURIStat.histogram_1_10_Updater.incrementAndGet(this);
        }
        else if (millis < 100L) {
            WebURIStat.histogram_10_100_Updater.incrementAndGet(this);
        }
        else if (millis < 1000L) {
            WebURIStat.histogram_100_1000_Updater.incrementAndGet(this);
        }
        else if (millis < 10000L) {
            WebURIStat.histogram_1000_10000_Updater.incrementAndGet(this);
        }
        else if (millis < 100000L) {
            WebURIStat.histogram_10000_100000_Updater.incrementAndGet(this);
        }
        else if (millis < 1000000L) {
            WebURIStat.histogram_100000_1000000_Updater.incrementAndGet(this);
        }
        else {
            WebURIStat.histogram_1000000_more_Updater.incrementAndGet(this);
        }
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
    
    public long getRequestTimeNano() {
        return this.requestTimeNano;
    }
    
    public long getRequestTimeMillis() {
        return this.getRequestTimeNano() / 1000000L;
    }
    
    public void addJdbcFetchRowCount(final long delta) {
        WebURIStat.jdbcFetchRowCountUpdater.addAndGet(this, delta);
    }
    
    public long getJdbcFetchRowCount() {
        return this.jdbcFetchRowCount;
    }
    
    public long getJdbcFetchRowPeak() {
        return this.jdbcFetchRowPeak;
    }
    
    public void addJdbcUpdateCount(final long updateCount) {
        WebURIStat.jdbcUpdateCountUpdater.addAndGet(this, updateCount);
    }
    
    public long getJdbcUpdateCount() {
        return this.jdbcUpdateCount;
    }
    
    public long getJdbcUpdatePeak() {
        return this.jdbcUpdatePeak;
    }
    
    public void incrementJdbcExecuteCount() {
        WebURIStat.jdbcExecuteCountUpdater.incrementAndGet(this);
    }
    
    public void addJdbcExecuteCount(final long executeCount) {
        WebURIStat.jdbcExecuteCountUpdater.addAndGet(this, executeCount);
    }
    
    public long getJdbcExecuteCount() {
        return this.jdbcExecuteCount;
    }
    
    public long getJdbcExecuteErrorCount() {
        return this.jdbcExecuteErrorCount;
    }
    
    public long getJdbcExecutePeak() {
        return this.jdbcExecutePeak;
    }
    
    public long getJdbcExecuteTimeMillis() {
        return this.getJdbcExecuteTimeNano() / 1000000L;
    }
    
    public long getJdbcExecuteTimeNano() {
        return this.jdbcExecuteTimeNano;
    }
    
    public void incrementJdbcCommitCount() {
        WebURIStat.jdbcCommitCountUpdater.incrementAndGet(this);
    }
    
    public long getJdbcCommitCount() {
        return this.jdbcCommitCount;
    }
    
    public void incrementJdbcRollbackCount() {
        WebURIStat.jdbcRollbackCountUpdater.incrementAndGet(this);
    }
    
    public long getJdbcRollbackCount() {
        return this.jdbcRollbackCount;
    }
    
    public void setLastAccessTimeMillis(final long lastAccessTimeMillis) {
        this.lastAccessTimeMillis = lastAccessTimeMillis;
    }
    
    public Date getLastAccessTime() {
        if (this.lastAccessTimeMillis < 0L) {
            return null;
        }
        return new Date(this.lastAccessTimeMillis);
    }
    
    public long getLastAccessTimeMillis() {
        return this.lastAccessTimeMillis;
    }
    
    public long getErrorCount() {
        return this.errorCount;
    }
    
    public long getJdbcPoolConnectionOpenCount() {
        return this.jdbcPoolConnectionOpenCount;
    }
    
    public void addJdbcPoolConnectionOpenCount(final long delta) {
        WebURIStat.jdbcPoolConnectionOpenCountUpdater.addAndGet(this, delta);
    }
    
    public void incrementJdbcPoolConnectionOpenCount() {
        WebURIStat.jdbcPoolConnectionOpenCountUpdater.incrementAndGet(this);
    }
    
    public long getJdbcPoolConnectionCloseCount() {
        return this.jdbcPoolConnectionCloseCount;
    }
    
    public void addJdbcPoolConnectionCloseCount(final long delta) {
        WebURIStat.jdbcPoolConnectionCloseCountUpdater.addAndGet(this, delta);
    }
    
    public void incrementJdbcPoolConnectionCloseCount() {
        WebURIStat.jdbcPoolConnectionCloseCountUpdater.incrementAndGet(this);
    }
    
    public long getJdbcResultSetOpenCount() {
        return this.jdbcResultSetOpenCount;
    }
    
    public void addJdbcResultSetOpenCount(final long delta) {
        WebURIStat.jdbcResultSetOpenCountUpdater.addAndGet(this, delta);
    }
    
    public long getJdbcResultSetCloseCount() {
        return this.jdbcResultSetCloseCount;
    }
    
    public void addJdbcResultSetCloseCount(final long delta) {
        WebURIStat.jdbcResultSetCloseCountUpdater.addAndGet(this, delta);
    }
    
    public ProfileStat getProfiletat() {
        return this.profiletat;
    }
    
    public long[] getHistogramValues() {
        return new long[] { this.histogram_0_1, this.histogram_1_10, this.histogram_10_100, this.histogram_100_1000, this.histogram_1000_10000, this.histogram_10000_100000, this.histogram_100000_1000000, this.histogram_1000000_more };
    }
    
    public WebURIStatValue getValue(final boolean reset) {
        final WebURIStatValue val = new WebURIStatValue();
        val.setUri(this.uri);
        val.setRunningCount(this.runningCount);
        val.setConcurrentMax(JdbcSqlStatUtils.get(this, WebURIStat.concurrentMaxUpdater, reset));
        val.setRequestCount(JdbcSqlStatUtils.get(this, WebURIStat.requestCountUpdater, reset));
        val.setRequestTimeNano(JdbcSqlStatUtils.get(this, WebURIStat.requestTimeNanoUpdater, reset));
        val.setRequestTimeNanoMax(JdbcSqlStatUtils.get(this, WebURIStat.requestTimeNanoMaxUpdater, reset));
        val.setRequestTimeNanoMaxOccurTime(JdbcSqlStatUtils.get(this, WebURIStat.requestTimeNanoMaxOccurTimeUpdater, reset));
        val.setJdbcFetchRowCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcFetchRowCountUpdater, reset));
        val.setJdbcFetchRowPeak(JdbcSqlStatUtils.get(this, WebURIStat.jdbcFetchRowPeakUpdater, reset));
        val.setJdbcUpdateCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcUpdateCountUpdater, reset));
        val.setJdbcUpdatePeak(JdbcSqlStatUtils.get(this, WebURIStat.jdbcUpdatePeakUpdater, reset));
        val.setJdbcExecuteCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcExecuteCountUpdater, reset));
        val.setJdbcExecuteErrorCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcExecuteErrorCountUpdater, reset));
        val.setJdbcExecutePeak(JdbcSqlStatUtils.get(this, WebURIStat.jdbcExecutePeakUpdater, reset));
        val.setJdbcExecuteTimeNano(JdbcSqlStatUtils.get(this, WebURIStat.jdbcExecuteTimeNanoUpdater, reset));
        val.setJdbcCommitCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcCommitCountUpdater, reset));
        val.setJdbcRollbackCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcRollbackCountUpdater, reset));
        val.setJdbcPoolConnectionOpenCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcPoolConnectionOpenCountUpdater, reset));
        val.setJdbcPoolConnectionCloseCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcPoolConnectionCloseCountUpdater, reset));
        val.setJdbcResultSetOpenCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcResultSetOpenCountUpdater, reset));
        val.setJdbcResultSetCloseCount(JdbcSqlStatUtils.get(this, WebURIStat.jdbcResultSetCloseCountUpdater, reset));
        val.setErrorCount(JdbcSqlStatUtils.get(this, WebURIStat.errorCountUpdater, reset));
        val.setLastAccessTimeMillis(JdbcSqlStatUtils.get(this, WebURIStat.lastAccessTimeMillisUpdater, reset));
        val.setProfileEntryStatValueList(this.getProfiletat().getStatValue(reset));
        val.histogram_0_1 = JdbcSqlStatUtils.get(this, WebURIStat.histogram_0_1_Updater, reset);
        val.histogram_1_10 = JdbcSqlStatUtils.get(this, WebURIStat.histogram_1_10_Updater, reset);
        val.histogram_10_100 = JdbcSqlStatUtils.get(this, WebURIStat.histogram_10_100_Updater, reset);
        val.histogram_100_1000 = JdbcSqlStatUtils.get(this, WebURIStat.histogram_100_1000_Updater, reset);
        val.histogram_1000_10000 = JdbcSqlStatUtils.get(this, WebURIStat.histogram_1000_10000_Updater, reset);
        val.histogram_10000_100000 = JdbcSqlStatUtils.get(this, WebURIStat.histogram_10000_100000_Updater, reset);
        val.histogram_100000_1000000 = JdbcSqlStatUtils.get(this, WebURIStat.histogram_100000_1000000_Updater, reset);
        val.histogram_1000000_more = JdbcSqlStatUtils.get(this, WebURIStat.histogram_1000000_more_Updater, reset);
        return val;
    }
    
    public Map<String, Object> getStatData() {
        return this.getValue(false).getStatData();
    }
    
    static {
        runningCountUpdater = AtomicIntegerFieldUpdater.newUpdater(WebURIStat.class, "runningCount");
        concurrentMaxUpdater = AtomicIntegerFieldUpdater.newUpdater(WebURIStat.class, "concurrentMax");
        requestCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "requestCount");
        requestTimeNanoUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "requestTimeNano");
        requestTimeNanoMaxUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "requestTimeNanoMax");
        requestTimeNanoMaxOccurTimeUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "requestTimeNanoMaxOccurTime");
        jdbcFetchRowCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcFetchRowCount");
        jdbcFetchRowPeakUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcFetchRowPeak");
        jdbcUpdateCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcUpdateCount");
        jdbcUpdatePeakUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcUpdatePeak");
        jdbcExecuteCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcExecuteCount");
        jdbcExecuteErrorCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcExecuteErrorCount");
        jdbcExecutePeakUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcExecutePeak");
        jdbcExecuteTimeNanoUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcExecuteTimeNano");
        jdbcCommitCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcCommitCount");
        jdbcRollbackCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcRollbackCount");
        jdbcPoolConnectionOpenCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcPoolConnectionOpenCount");
        jdbcPoolConnectionCloseCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcPoolConnectionCloseCount");
        jdbcResultSetOpenCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcResultSetOpenCount");
        jdbcResultSetCloseCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "jdbcResultSetCloseCount");
        errorCountUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "errorCount");
        lastAccessTimeMillisUpdater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "lastAccessTimeMillis");
        currentLocal = new ThreadLocal<WebURIStat>();
        histogram_0_1_Updater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "histogram_0_1");
        histogram_1_10_Updater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "histogram_1_10");
        histogram_10_100_Updater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "histogram_10_100");
        histogram_100_1000_Updater = AtomicLongFieldUpdater.newUpdater(WebURIStat.class, "histogram_100_1000");
        histogram_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(WebURIStat.class, "histogram_1000_10000");
        histogram_10000_100000_Updater = AtomicIntegerFieldUpdater.newUpdater(WebURIStat.class, "histogram_10000_100000");
        histogram_100000_1000000_Updater = AtomicIntegerFieldUpdater.newUpdater(WebURIStat.class, "histogram_100000_1000000");
        histogram_1000000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(WebURIStat.class, "histogram_1000000_more");
    }
}
