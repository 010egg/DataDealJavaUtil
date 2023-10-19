// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import javax.management.JMException;
import com.alibaba.druid.util.JMXUtils;
import javax.management.openmbean.CompositeData;
import java.util.Date;
import com.alibaba.druid.util.Histogram;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class JdbcStatementStat implements JdbcStatementStatMBean
{
    private final AtomicLong createCount;
    private final AtomicLong prepareCount;
    private final AtomicLong prepareCallCount;
    private final AtomicLong closeCount;
    private final AtomicInteger runningCount;
    private final AtomicInteger concurrentMax;
    private final AtomicLong count;
    private final AtomicLong errorCount;
    private final AtomicLong nanoTotal;
    private Throwable lastError;
    private long lastErrorTime;
    private long lastSampleTime;
    private final Histogram histogram;
    
    public JdbcStatementStat() {
        this.createCount = new AtomicLong(0L);
        this.prepareCount = new AtomicLong(0L);
        this.prepareCallCount = new AtomicLong(0L);
        this.closeCount = new AtomicLong(0L);
        this.runningCount = new AtomicInteger();
        this.concurrentMax = new AtomicInteger();
        this.count = new AtomicLong();
        this.errorCount = new AtomicLong();
        this.nanoTotal = new AtomicLong();
        this.lastSampleTime = 0L;
        this.histogram = new Histogram(new long[] { 10L, 100L, 1000L, 10000L });
    }
    
    public long[] getHistogramRanges() {
        return this.histogram.getRanges();
    }
    
    public long[] getHistogramValues() {
        return this.histogram.toArray();
    }
    
    public void reset() {
        this.runningCount.set(0);
        this.concurrentMax.set(0);
        this.count.set(0L);
        this.errorCount.set(0L);
        this.nanoTotal.set(0L);
        this.lastError = null;
        this.lastErrorTime = 0L;
        this.lastSampleTime = 0L;
        this.createCount.set(0L);
        this.prepareCount.set(0L);
        this.prepareCallCount.set(0L);
        this.closeCount.set(0L);
        this.histogram.reset();
    }
    
    public void afterExecute(final long nanoSpan) {
        this.runningCount.decrementAndGet();
        this.nanoTotal.addAndGet(nanoSpan);
        final long millis = nanoSpan / 1000000L;
        this.histogram.record(millis);
    }
    
    public void beforeExecute() {
        final int invoking = this.runningCount.incrementAndGet();
        int max;
        do {
            max = this.concurrentMax.get();
        } while (invoking > max && !this.concurrentMax.compareAndSet(max, invoking));
        this.count.incrementAndGet();
        this.lastSampleTime = System.currentTimeMillis();
    }
    
    @Override
    public long getErrorCount() {
        return this.errorCount.get();
    }
    
    @Override
    public int getRunningCount() {
        return this.runningCount.get();
    }
    
    @Override
    public int getConcurrentMax() {
        return this.concurrentMax.get();
    }
    
    @Override
    public long getExecuteCount() {
        return this.count.get();
    }
    
    @Override
    public Date getExecuteLastTime() {
        if (this.lastSampleTime == 0L) {
            return null;
        }
        return new Date(this.lastSampleTime);
    }
    
    public long getNanoTotal() {
        return this.nanoTotal.get();
    }
    
    public long getMillisTotal() {
        return this.nanoTotal.get() / 1000000L;
    }
    
    public Throwable getLastException() {
        return this.lastError;
    }
    
    @Override
    public Date getLastErrorTime() {
        if (this.lastErrorTime <= 0L) {
            return null;
        }
        return new Date(this.lastErrorTime);
    }
    
    public void error(final Throwable error) {
        this.errorCount.incrementAndGet();
        this.lastError = error;
        this.lastErrorTime = System.currentTimeMillis();
    }
    
    @Override
    public long getCloseCount() {
        return this.closeCount.get();
    }
    
    @Override
    public long getCreateCount() {
        return this.createCount.get();
    }
    
    @Override
    public long getExecuteMillisTotal() {
        return this.getNanoTotal() / 1000000L;
    }
    
    @Override
    public long getPrepareCallCount() {
        return this.prepareCallCount.get();
    }
    
    @Override
    public long getPrepareCount() {
        return this.prepareCount.get();
    }
    
    @Override
    public long getExecuteSuccessCount() {
        return this.getExecuteCount() - this.getErrorCount() - this.getRunningCount();
    }
    
    @Override
    public CompositeData getLastError() throws JMException {
        return JMXUtils.getErrorCompositeData(this.getLastException());
    }
    
    public void incrementCreateCounter() {
        this.createCount.incrementAndGet();
    }
    
    public void incrementPrepareCallCount() {
        this.prepareCallCount.incrementAndGet();
    }
    
    public void incrementPrepareCounter() {
        this.prepareCount.incrementAndGet();
    }
    
    public void incrementStatementCloseCounter() {
        this.closeCount.incrementAndGet();
    }
}
