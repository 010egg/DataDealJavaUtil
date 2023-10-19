// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import java.util.Map;
import java.util.HashMap;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.JMException;
import com.alibaba.druid.util.JMXUtils;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.CompositeType;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import com.alibaba.druid.util.Histogram;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class JdbcConnectionStat implements JdbcConnectionStatMBean
{
    private final AtomicInteger activeCount;
    private final AtomicInteger activeCountMax;
    private final AtomicInteger connectingCount;
    private final AtomicInteger connectingMax;
    private final AtomicLong connectCount;
    private final AtomicLong connectErrorCount;
    private Throwable connectErrorLast;
    private final AtomicLong connectNanoTotal;
    private final AtomicLong connectNanoMax;
    private final AtomicLong errorCount;
    private final AtomicLong aliveNanoTotal;
    private Throwable lastError;
    private long lastErrorTime;
    private long connectLastTime;
    private final AtomicLong closeCount;
    private final AtomicLong transactionStartCount;
    private final AtomicLong commitCount;
    private final AtomicLong rollbackCount;
    private final AtomicLong aliveNanoMin;
    private final AtomicLong aliveNanoMax;
    private final Histogram histogram;
    
    public JdbcConnectionStat() {
        this.activeCount = new AtomicInteger();
        this.activeCountMax = new AtomicInteger();
        this.connectingCount = new AtomicInteger();
        this.connectingMax = new AtomicInteger();
        this.connectCount = new AtomicLong();
        this.connectErrorCount = new AtomicLong();
        this.connectNanoTotal = new AtomicLong(0L);
        this.connectNanoMax = new AtomicLong(0L);
        this.errorCount = new AtomicLong();
        this.aliveNanoTotal = new AtomicLong();
        this.connectLastTime = 0L;
        this.closeCount = new AtomicLong(0L);
        this.transactionStartCount = new AtomicLong(0L);
        this.commitCount = new AtomicLong(0L);
        this.rollbackCount = new AtomicLong(0L);
        this.aliveNanoMin = new AtomicLong();
        this.aliveNanoMax = new AtomicLong();
        this.histogram = new Histogram(TimeUnit.SECONDS, new long[] { 1L, 5L, 15L, 60L, 300L, 1800L });
    }
    
    public void reset() {
        this.connectingMax.set(0);
        this.connectErrorCount.set(0L);
        this.errorCount.set(0L);
        this.aliveNanoTotal.set(0L);
        this.aliveNanoMin.set(0L);
        this.aliveNanoMax.set(0L);
        this.lastError = null;
        this.lastErrorTime = 0L;
        this.connectLastTime = 0L;
        this.connectCount.set(0L);
        this.closeCount.set(0L);
        this.transactionStartCount.set(0L);
        this.commitCount.set(0L);
        this.rollbackCount.set(0L);
        this.connectNanoTotal.set(0L);
        this.connectNanoMax.set(0L);
        this.histogram.reset();
    }
    
    public void beforeConnect() {
        final int invoking = this.connectingCount.incrementAndGet();
        int max;
        do {
            max = this.connectingMax.get();
        } while (invoking > max && !this.connectingMax.compareAndSet(max, invoking));
        this.connectCount.incrementAndGet();
        this.connectLastTime = System.currentTimeMillis();
    }
    
    public void afterConnected(final long delta) {
        this.connectingCount.decrementAndGet();
        this.connectNanoTotal.addAndGet(delta);
        long max;
        do {
            max = this.connectNanoMax.get();
        } while (delta > max && !this.connectNanoMax.compareAndSet(max, delta));
        this.activeCount.incrementAndGet();
    }
    
    public long getConnectNanoMax() {
        return this.connectNanoMax.get();
    }
    
    public long getConnectMillisMax() {
        return this.connectNanoMax.get() / 1000000L;
    }
    
    public void setActiveCount(final int activeCount) {
        this.activeCount.set(activeCount);
        int max;
        do {
            max = this.activeCountMax.get();
        } while (activeCount > max && !this.activeCountMax.compareAndSet(max, activeCount));
    }
    
    public int getActiveCount() {
        return this.activeCount.get();
    }
    
    public int getAtiveCountMax() {
        return this.activeCount.get();
    }
    
    public long getErrorCount() {
        return this.errorCount.get();
    }
    
    public int getConnectingCount() {
        return this.connectingCount.get();
    }
    
    public int getConnectingMax() {
        return this.connectingMax.get();
    }
    
    public long getAliveTotal() {
        return this.aliveNanoTotal.get();
    }
    
    public long getAliveNanoMin() {
        return this.aliveNanoMin.get();
    }
    
    public long getAliveMillisMin() {
        return this.aliveNanoMin.get() / 1000000L;
    }
    
    public long getAliveNanoMax() {
        return this.aliveNanoMax.get();
    }
    
    public long getAliveMillisMax() {
        return this.aliveNanoMax.get() / 1000000L;
    }
    
    public void afterClose(final long aliveNano) {
        this.activeCount.decrementAndGet();
        this.aliveNanoTotal.addAndGet(aliveNano);
        long max;
        do {
            max = this.aliveNanoMax.get();
        } while (aliveNano > max && !this.aliveNanoMax.compareAndSet(max, aliveNano));
        long min;
        do {
            min = this.aliveNanoMin.get();
        } while ((min == 0L || aliveNano < min) && !this.aliveNanoMin.compareAndSet(min, aliveNano));
        final long aliveMillis = aliveNano / 1000000L;
        this.histogram.record(aliveMillis);
    }
    
    public Throwable getErrorLast() {
        return this.lastError;
    }
    
    public Throwable getConnectErrorLast() {
        return this.connectErrorLast;
    }
    
    public Date getErrorLastTime() {
        if (this.lastErrorTime <= 0L) {
            return null;
        }
        return new Date(this.lastErrorTime);
    }
    
    public void connectError(final Throwable error) {
        this.connectErrorCount.incrementAndGet();
        this.connectErrorLast = error;
        this.errorCount.incrementAndGet();
        this.lastError = error;
        this.lastErrorTime = System.currentTimeMillis();
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
    public long getCommitCount() {
        return this.commitCount.get();
    }
    
    @Override
    public long getConnectCount() {
        return this.connectCount.get();
    }
    
    @Override
    public long getConnectMillis() {
        return this.connectNanoTotal.get() / 1000000L;
    }
    
    @Override
    public int getActiveMax() {
        return this.activeCountMax.get();
    }
    
    @Override
    public long getRollbackCount() {
        return this.rollbackCount.get();
    }
    
    @Override
    public long getConnectErrorCount() {
        return this.connectErrorCount.get();
    }
    
    @Override
    public Date getConnectLastTime() {
        if (this.connectLastTime == 0L) {
            return null;
        }
        return new Date(this.connectLastTime);
    }
    
    public void incrementConnectionCloseCount() {
        this.closeCount.incrementAndGet();
    }
    
    public void incrementConnectionCommitCount() {
        this.commitCount.incrementAndGet();
    }
    
    public void incrementConnectionRollbackCount() {
        this.rollbackCount.incrementAndGet();
    }
    
    public void incrementTransactionStartCount() {
        this.transactionStartCount.incrementAndGet();
    }
    
    public long getTransactionStartCount() {
        return this.transactionStartCount.get();
    }
    
    public long[] getHistorgramValues() {
        return this.histogram.toArray();
    }
    
    public long[] getHistogramRanges() {
        return this.histogram.getRanges();
    }
    
    public static class Entry implements EntryMBean
    {
        private long id;
        private long establishTime;
        private long establishNano;
        private Date connectTime;
        private long connectTimespanNano;
        private Exception connectStackTraceException;
        private String lastSql;
        private Exception lastStatementStatckTraceException;
        protected Throwable lastError;
        protected long lastErrorTime;
        private final String dataSource;
        private static String[] indexNames;
        private static String[] indexDescriptions;
        
        public Entry(final String dataSource, final long connectionId) {
            this.id = connectionId;
            this.dataSource = dataSource;
        }
        
        @Override
        public void reset() {
            this.lastSql = null;
            this.lastStatementStatckTraceException = null;
            this.lastError = null;
            this.lastErrorTime = 0L;
        }
        
        @Override
        public Date getEstablishTime() {
            if (this.establishTime <= 0L) {
                return null;
            }
            return new Date(this.establishTime);
        }
        
        public void setEstablishTime(final long establishTime) {
            this.establishTime = establishTime;
        }
        
        @Override
        public long getEstablishNano() {
            return this.establishNano;
        }
        
        public void setEstablishNano(final long establishNano) {
            this.establishNano = establishNano;
        }
        
        @Override
        public Date getConnectTime() {
            return this.connectTime;
        }
        
        public void setConnectTime(final Date connectTime) {
            this.connectTime = connectTime;
        }
        
        @Override
        public long getConnectTimespanNano() {
            return this.connectTimespanNano;
        }
        
        public void setConnectTimespanNano(final long connectTimespanNano) {
            this.connectTimespanNano = connectTimespanNano;
        }
        
        @Override
        public String getLastSql() {
            return this.lastSql;
        }
        
        public void setLastSql(final String lastSql) {
            this.lastSql = lastSql;
        }
        
        @Override
        public String getConnectStackTrace() {
            if (this.connectStackTraceException == null) {
                return null;
            }
            final StringWriter buf = new StringWriter();
            this.connectStackTraceException.printStackTrace(new PrintWriter(buf));
            return buf.toString();
        }
        
        public void setConnectStackTrace(final Exception connectStackTraceException) {
            this.connectStackTraceException = connectStackTraceException;
        }
        
        @Override
        public String getLastStatementStatckTrace() {
            if (this.lastStatementStatckTraceException == null) {
                return null;
            }
            final StringWriter buf = new StringWriter();
            this.lastStatementStatckTraceException.printStackTrace(new PrintWriter(buf));
            return buf.toString();
        }
        
        public void setLastStatementStatckTrace(final Exception lastStatementStatckTrace) {
            this.lastStatementStatckTraceException = lastStatementStatckTrace;
        }
        
        public void error(final Throwable lastError) {
            this.lastError = lastError;
            this.lastErrorTime = System.currentTimeMillis();
        }
        
        @Override
        public Date getLastErrorTime() {
            if (this.lastErrorTime <= 0L) {
                return null;
            }
            return new Date(this.lastErrorTime);
        }
        
        public static CompositeType getCompositeType() throws JMException {
            final OpenType<?>[] indexTypes = (OpenType<?>[])new OpenType[] { SimpleType.LONG, SimpleType.DATE, SimpleType.LONG, SimpleType.DATE, SimpleType.LONG, SimpleType.STRING, JMXUtils.getThrowableCompositeType(), SimpleType.DATE, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING };
            return new CompositeType("ConnectionStatistic", "Connection Statistic", Entry.indexNames, Entry.indexDescriptions, indexTypes);
        }
        
        public String getDataSource() {
            return this.dataSource;
        }
        
        public CompositeDataSupport getCompositeData() throws JMException {
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID", this.id);
            map.put("ConnectTime", this.getConnectTime());
            map.put("ConnectTimespan", this.getConnectTimespanNano() / 1000000L);
            map.put("EstablishTime", this.getEstablishTime());
            map.put("AliveTimespan", (System.nanoTime() - this.getEstablishNano()) / 1000000L);
            map.put("LastSql", this.getLastSql());
            map.put("LastError", JMXUtils.getErrorCompositeData(this.lastError));
            map.put("LastErrorTime", this.getLastErrorTime());
            map.put("ConnectStatckTrace", this.getConnectStackTrace());
            map.put("LastStatementStackTrace", this.getLastStatementStatckTrace());
            map.put("DataSource", this.getDataSource());
            return new CompositeDataSupport(getCompositeType(), map);
        }
        
        static {
            Entry.indexNames = new String[] { "ID", "ConnectTime", "ConnectTimespan", "EstablishTime", "AliveTimespan", "LastSql", "LastError", "LastErrorTime", "ConnectStatckTrace", "LastStatementStackTrace", "DataSource" };
            Entry.indexDescriptions = Entry.indexNames;
        }
    }
    
    public interface EntryMBean
    {
        Date getEstablishTime();
        
        long getEstablishNano();
        
        Date getConnectTime();
        
        long getConnectTimespanNano();
        
        String getLastSql();
        
        String getConnectStackTrace();
        
        String getLastStatementStatckTrace();
        
        Date getLastErrorTime();
        
        void reset();
    }
}
