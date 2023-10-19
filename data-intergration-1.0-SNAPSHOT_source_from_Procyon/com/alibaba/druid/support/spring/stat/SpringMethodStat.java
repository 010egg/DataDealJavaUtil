// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat;

import java.util.Map;
import java.util.Date;
import com.alibaba.druid.support.profile.Profiler;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class SpringMethodStat
{
    private static final ThreadLocal<SpringMethodStat> currentLocal;
    private final SpringMethodInfo methodInfo;
    private final AtomicInteger runningCount;
    private final AtomicInteger concurrentMax;
    private final AtomicLong executeCount;
    private final AtomicLong executeErrorCount;
    private final AtomicLong executeTimeNano;
    private final AtomicLong jdbcFetchRowCount;
    private final AtomicLong jdbcUpdateCount;
    private final AtomicLong jdbcExecuteCount;
    private final AtomicLong jdbcExecuteErrorCount;
    private final AtomicLong jdbcExecuteTimeNano;
    private final AtomicLong jdbcCommitCount;
    private final AtomicLong jdbcRollbackCount;
    private final AtomicLong jdbcPoolConnectionOpenCount;
    private final AtomicLong jdbcPoolConnectionCloseCount;
    private final AtomicLong jdbcResultSetOpenCount;
    private final AtomicLong jdbcResultSetCloseCount;
    private volatile Throwable lastError;
    private volatile long lastErrorTimeMillis;
    private volatile long histogram_0_1;
    private volatile long histogram_1_10;
    private volatile long histogram_10_100;
    private volatile long histogram_100_1000;
    private volatile int histogram_1000_10000;
    private volatile int histogram_10000_100000;
    private volatile int histogram_100000_1000000;
    private volatile int histogram_1000000_more;
    static final AtomicLongFieldUpdater<SpringMethodStat> histogram_0_1_Updater;
    static final AtomicLongFieldUpdater<SpringMethodStat> histogram_1_10_Updater;
    static final AtomicLongFieldUpdater<SpringMethodStat> histogram_10_100_Updater;
    static final AtomicLongFieldUpdater<SpringMethodStat> histogram_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<SpringMethodStat> histogram_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<SpringMethodStat> histogram_10000_100000_Updater;
    static final AtomicIntegerFieldUpdater<SpringMethodStat> histogram_100000_1000000_Updater;
    static final AtomicIntegerFieldUpdater<SpringMethodStat> histogram_1000000_more_Updater;
    
    public SpringMethodStat(final SpringMethodInfo methodInfo) {
        this.runningCount = new AtomicInteger();
        this.concurrentMax = new AtomicInteger();
        this.executeCount = new AtomicLong(0L);
        this.executeErrorCount = new AtomicLong(0L);
        this.executeTimeNano = new AtomicLong();
        this.jdbcFetchRowCount = new AtomicLong();
        this.jdbcUpdateCount = new AtomicLong();
        this.jdbcExecuteCount = new AtomicLong();
        this.jdbcExecuteErrorCount = new AtomicLong();
        this.jdbcExecuteTimeNano = new AtomicLong();
        this.jdbcCommitCount = new AtomicLong();
        this.jdbcRollbackCount = new AtomicLong();
        this.jdbcPoolConnectionOpenCount = new AtomicLong();
        this.jdbcPoolConnectionCloseCount = new AtomicLong();
        this.jdbcResultSetOpenCount = new AtomicLong();
        this.jdbcResultSetCloseCount = new AtomicLong();
        this.methodInfo = methodInfo;
    }
    
    public SpringMethodStatValue getStatValue(final boolean reset) {
        final SpringMethodStatValue val = new SpringMethodStatValue();
        val.setClassName(this.getMethodInfo().getClassName());
        val.setSignature(this.getMethodInfo().getSignature());
        val.setRunningCount(this.getRunningCount());
        val.setConcurrentMax(JdbcSqlStatUtils.get(this.concurrentMax, reset));
        val.setExecuteCount(JdbcSqlStatUtils.get(this.executeCount, reset));
        val.setExecuteErrorCount(JdbcSqlStatUtils.get(this.executeErrorCount, reset));
        val.setExecuteTimeNano(JdbcSqlStatUtils.get(this.executeTimeNano, reset));
        val.setJdbcFetchRowCount(JdbcSqlStatUtils.get(this.jdbcFetchRowCount, reset));
        val.setJdbcUpdateCount(JdbcSqlStatUtils.get(this.jdbcUpdateCount, reset));
        val.setJdbcExecuteCount(JdbcSqlStatUtils.get(this.jdbcExecuteCount, reset));
        val.setJdbcExecuteErrorCount(JdbcSqlStatUtils.get(this.jdbcExecuteErrorCount, reset));
        val.setJdbcExecuteTimeNano(JdbcSqlStatUtils.get(this.jdbcExecuteTimeNano, reset));
        val.setJdbcCommitCount(JdbcSqlStatUtils.get(this.jdbcCommitCount, reset));
        val.setJdbcRollbackCount(JdbcSqlStatUtils.get(this.jdbcRollbackCount, reset));
        val.setJdbcPoolConnectionOpenCount(JdbcSqlStatUtils.get(this.jdbcPoolConnectionOpenCount, reset));
        val.setJdbcPoolConnectionCloseCount(JdbcSqlStatUtils.get(this.jdbcPoolConnectionCloseCount, reset));
        val.setJdbcResultSetOpenCount(JdbcSqlStatUtils.get(this.jdbcResultSetOpenCount, reset));
        val.setJdbcResultSetCloseCount(JdbcSqlStatUtils.get(this.jdbcResultSetCloseCount, reset));
        val.setLastError(this.lastError);
        val.setLastErrorTimeMillis(this.lastErrorTimeMillis);
        if (reset) {
            this.lastError = null;
            this.lastErrorTimeMillis = 0L;
        }
        val.histogram_0_1 = JdbcSqlStatUtils.get(this, SpringMethodStat.histogram_0_1_Updater, reset);
        val.histogram_1_10 = JdbcSqlStatUtils.get(this, SpringMethodStat.histogram_1_10_Updater, reset);
        val.histogram_10_100 = JdbcSqlStatUtils.get(this, SpringMethodStat.histogram_10_100_Updater, reset);
        val.histogram_100_1000 = JdbcSqlStatUtils.get(this, SpringMethodStat.histogram_100_1000_Updater, reset);
        val.histogram_1000_10000 = JdbcSqlStatUtils.get(this, SpringMethodStat.histogram_1000_10000_Updater, reset);
        val.histogram_10000_100000 = JdbcSqlStatUtils.get(this, SpringMethodStat.histogram_10000_100000_Updater, reset);
        val.histogram_100000_1000000 = JdbcSqlStatUtils.get(this, SpringMethodStat.histogram_100000_1000000_Updater, reset);
        val.histogram_1000000_more = JdbcSqlStatUtils.get(this, SpringMethodStat.histogram_1000000_more_Updater, reset);
        return val;
    }
    
    public void reset() {
        this.concurrentMax.set(0);
        this.executeCount.set(0L);
        this.executeErrorCount.set(0L);
        this.executeTimeNano.set(0L);
        this.jdbcFetchRowCount.set(0L);
        this.jdbcUpdateCount.set(0L);
        this.jdbcExecuteCount.set(0L);
        this.jdbcExecuteErrorCount.set(0L);
        this.jdbcExecuteTimeNano.set(0L);
        this.jdbcCommitCount.set(0L);
        this.jdbcRollbackCount.set(0L);
        this.jdbcPoolConnectionOpenCount.set(0L);
        this.jdbcPoolConnectionCloseCount.set(0L);
        this.jdbcResultSetOpenCount.set(0L);
        this.jdbcResultSetCloseCount.set(0L);
        this.lastError = null;
        this.lastErrorTimeMillis = 0L;
        SpringMethodStat.histogram_0_1_Updater.set(this, 0L);
        SpringMethodStat.histogram_1_10_Updater.set(this, 0L);
        SpringMethodStat.histogram_10_100_Updater.set(this, 0L);
        SpringMethodStat.histogram_100_1000_Updater.set(this, 0L);
        SpringMethodStat.histogram_1000_10000_Updater.set(this, 0);
        SpringMethodStat.histogram_10000_100000_Updater.set(this, 0);
        SpringMethodStat.histogram_100000_1000000_Updater.set(this, 0);
        SpringMethodStat.histogram_1000000_more_Updater.set(this, 0);
    }
    
    public SpringMethodInfo getMethodInfo() {
        return this.methodInfo;
    }
    
    public static SpringMethodStat current() {
        return SpringMethodStat.currentLocal.get();
    }
    
    public static void setCurrent(final SpringMethodStat current) {
        SpringMethodStat.currentLocal.set(current);
    }
    
    public void beforeInvoke() {
        SpringMethodStat.currentLocal.set(this);
        final int running = this.runningCount.incrementAndGet();
        int max;
        do {
            max = this.concurrentMax.get();
        } while (running > max && !this.concurrentMax.compareAndSet(max, running));
        this.executeCount.incrementAndGet();
        Profiler.enter(this.methodInfo.getSignature(), "SPRING");
    }
    
    public void afterInvoke(final Throwable error, final long nanos) {
        this.runningCount.decrementAndGet();
        this.executeTimeNano.addAndGet(nanos);
        this.histogramRecord(nanos);
        if (error != null) {
            this.executeErrorCount.incrementAndGet();
            this.lastError = error;
            this.lastErrorTimeMillis = System.currentTimeMillis();
        }
        Profiler.release(nanos);
    }
    
    private void histogramRecord(final long nanos) {
        final long millis = nanos / 1000L / 1000L;
        if (millis < 1L) {
            SpringMethodStat.histogram_0_1_Updater.incrementAndGet(this);
        }
        else if (millis < 10L) {
            SpringMethodStat.histogram_1_10_Updater.incrementAndGet(this);
        }
        else if (millis < 100L) {
            SpringMethodStat.histogram_10_100_Updater.incrementAndGet(this);
        }
        else if (millis < 1000L) {
            SpringMethodStat.histogram_100_1000_Updater.incrementAndGet(this);
        }
        else if (millis < 10000L) {
            SpringMethodStat.histogram_1000_10000_Updater.incrementAndGet(this);
        }
        else if (millis < 100000L) {
            SpringMethodStat.histogram_10000_100000_Updater.incrementAndGet(this);
        }
        else if (millis < 1000000L) {
            SpringMethodStat.histogram_100000_1000000_Updater.incrementAndGet(this);
        }
        else {
            SpringMethodStat.histogram_1000000_more_Updater.incrementAndGet(this);
        }
    }
    
    public long[] getHistogramValues() {
        return new long[] { this.histogram_0_1, this.histogram_1_10, this.histogram_10_100, this.histogram_100_1000, this.histogram_1000_10000, this.histogram_10000_100000, this.histogram_100000_1000000, this.histogram_1000000_more };
    }
    
    public Throwable getLastError() {
        return this.lastError;
    }
    
    public Date getLastErrorTime() {
        if (this.lastErrorTimeMillis <= 0L) {
            return null;
        }
        return new Date(this.lastErrorTimeMillis);
    }
    
    public long getLastErrorTimeMillis() {
        return this.lastErrorTimeMillis;
    }
    
    public int getRunningCount() {
        return this.runningCount.get();
    }
    
    public int getConcurrentMax() {
        return this.concurrentMax.get();
    }
    
    public long getExecuteCount() {
        return this.executeCount.get();
    }
    
    public long getExecuteErrorCount() {
        return this.executeErrorCount.get();
    }
    
    public long getExecuteTimeNano() {
        return this.executeTimeNano.get();
    }
    
    public long getExecuteTimeMillis() {
        return this.getExecuteTimeNano() / 1000000L;
    }
    
    public void addJdbcFetchRowCount(final long delta) {
        this.jdbcFetchRowCount.addAndGet(delta);
    }
    
    public long getJdbcFetchRowCount() {
        return this.jdbcFetchRowCount.get();
    }
    
    public void addJdbcUpdateCount(final long updateCount) {
        this.jdbcUpdateCount.addAndGet(updateCount);
    }
    
    public long getJdbcUpdateCount() {
        return this.jdbcUpdateCount.get();
    }
    
    public void incrementJdbcExecuteCount() {
        this.jdbcExecuteCount.incrementAndGet();
    }
    
    public void addJdbcExecuteCount(final long executeCount) {
        this.jdbcExecuteCount.addAndGet(executeCount);
    }
    
    public long getJdbcExecuteCount() {
        return this.jdbcExecuteCount.get();
    }
    
    public long getJdbcExecuteErrorCount() {
        return this.jdbcExecuteErrorCount.get();
    }
    
    public void addJdbcExecuteErrorCount(final long executeCount) {
        this.jdbcExecuteErrorCount.addAndGet(executeCount);
    }
    
    public void incrementJdbcExecuteErrorCount() {
        this.jdbcExecuteErrorCount.incrementAndGet();
    }
    
    public long getJdbcExecuteTimeMillis() {
        return this.getJdbcExecuteTimeNano() / 1000000L;
    }
    
    public long getJdbcExecuteTimeNano() {
        return this.jdbcExecuteTimeNano.get();
    }
    
    public void addJdbcExecuteTimeNano(final long nano) {
        this.jdbcExecuteTimeNano.addAndGet(nano);
    }
    
    public void incrementJdbcCommitCount() {
        this.jdbcCommitCount.incrementAndGet();
    }
    
    public long getJdbcCommitCount() {
        return this.jdbcCommitCount.get();
    }
    
    public void addJdbcCommitCount(final long commitCount) {
        this.jdbcCommitCount.addAndGet(commitCount);
    }
    
    public void incrementJdbcRollbackCount() {
        this.jdbcRollbackCount.incrementAndGet();
    }
    
    public long getJdbcRollbackCount() {
        return this.jdbcRollbackCount.get();
    }
    
    public void addJdbcRollbackCount(final long rollbackCount) {
        this.jdbcRollbackCount.addAndGet(rollbackCount);
    }
    
    public long getJdbcPoolConnectionOpenCount() {
        return this.jdbcPoolConnectionOpenCount.get();
    }
    
    public void addJdbcPoolConnectionOpenCount(final long delta) {
        this.jdbcPoolConnectionOpenCount.addAndGet(delta);
    }
    
    public void incrementJdbcPoolConnectionOpenCount() {
        this.jdbcPoolConnectionOpenCount.incrementAndGet();
    }
    
    public long getJdbcPoolConnectionCloseCount() {
        return this.jdbcPoolConnectionCloseCount.get();
    }
    
    public void addJdbcPoolConnectionCloseCount(final long delta) {
        this.jdbcPoolConnectionCloseCount.addAndGet(delta);
    }
    
    public void incrementJdbcPoolConnectionCloseCount() {
        this.jdbcPoolConnectionCloseCount.incrementAndGet();
    }
    
    public long getJdbcResultSetOpenCount() {
        return this.jdbcResultSetOpenCount.get();
    }
    
    public void addJdbcResultSetOpenCount(final long delta) {
        this.jdbcResultSetOpenCount.addAndGet(delta);
    }
    
    public void incrementJdbcResultSetOpenCount() {
        this.jdbcResultSetOpenCount.incrementAndGet();
    }
    
    public long getJdbcResultSetCloseCount() {
        return this.jdbcResultSetCloseCount.get();
    }
    
    public void addJdbcResultSetCloseCount(final long delta) {
        this.jdbcResultSetCloseCount.addAndGet(delta);
    }
    
    public void incrementJdbcResultSetCloseCount() {
        this.jdbcResultSetCloseCount.incrementAndGet();
    }
    
    public Map<String, Object> getStatData() {
        return this.getStatValue(false).getData();
    }
    
    static {
        currentLocal = new ThreadLocal<SpringMethodStat>();
        histogram_0_1_Updater = AtomicLongFieldUpdater.newUpdater(SpringMethodStat.class, "histogram_0_1");
        histogram_1_10_Updater = AtomicLongFieldUpdater.newUpdater(SpringMethodStat.class, "histogram_1_10");
        histogram_10_100_Updater = AtomicLongFieldUpdater.newUpdater(SpringMethodStat.class, "histogram_10_100");
        histogram_100_1000_Updater = AtomicLongFieldUpdater.newUpdater(SpringMethodStat.class, "histogram_100_1000");
        histogram_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(SpringMethodStat.class, "histogram_1000_10000");
        histogram_10000_100000_Updater = AtomicIntegerFieldUpdater.newUpdater(SpringMethodStat.class, "histogram_10000_100000");
        histogram_100000_1000000_Updater = AtomicIntegerFieldUpdater.newUpdater(SpringMethodStat.class, "histogram_100000_1000000");
        histogram_1000000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(SpringMethodStat.class, "histogram_1000000_more");
    }
}
