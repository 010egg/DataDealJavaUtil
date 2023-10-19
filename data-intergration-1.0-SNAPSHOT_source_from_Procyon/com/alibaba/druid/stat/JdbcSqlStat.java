// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import javax.management.openmbean.CompositeDataSupport;
import java.util.Map;
import javax.management.JMException;
import javax.management.openmbean.ArrayType;
import com.alibaba.druid.util.JMXUtils;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.OpenType;
import com.alibaba.druid.proxy.jdbc.StatementExecuteType;
import java.util.Date;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import com.alibaba.druid.proxy.DruidDriver;
import javax.management.openmbean.CompositeType;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public final class JdbcSqlStat implements JdbcSqlStatMBean, Comparable<JdbcSqlStat>
{
    private final String sql;
    private long sqlHash;
    private long id;
    private String dataSource;
    private long executeLastStartTime;
    private volatile long executeBatchSizeTotal;
    private volatile int executeBatchSizeMax;
    private volatile long executeSuccessCount;
    private volatile long executeSpanNanoTotal;
    private volatile long executeSpanNanoMax;
    private volatile int runningCount;
    private volatile int concurrentMax;
    private volatile long resultSetHoldTimeNano;
    private volatile long executeAndResultSetHoldTime;
    static final AtomicLongFieldUpdater<JdbcSqlStat> executeBatchSizeTotalUpdater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> executeBatchSizeMaxUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> executeSuccessCountUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> executeSpanNanoTotalUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> executeSpanNanoMaxUpdater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> runningCountUpdater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> concurrentMaxUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> resultSetHoldTimeNanoUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> executeAndResultSetHoldTimeUpdater;
    private String name;
    private String file;
    private String dbType;
    private volatile long executeNanoSpanMaxOccurTime;
    private volatile long executeErrorCount;
    private volatile Throwable executeErrorLast;
    private volatile long executeErrorLastTime;
    private volatile long updateCount;
    private volatile long updateCountMax;
    private volatile long fetchRowCount;
    private volatile long fetchRowCountMax;
    private volatile long inTransactionCount;
    private volatile String lastSlowParameters;
    private boolean removed;
    private volatile long clobOpenCount;
    private volatile long blobOpenCount;
    private volatile long readStringLength;
    private volatile long readBytesLength;
    private volatile long inputStreamOpenCount;
    private volatile long readerOpenCount;
    static final AtomicLongFieldUpdater<JdbcSqlStat> executeErrorCountUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> updateCountUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> updateCountMaxUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> fetchRowCountUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> fetchRowCountMaxUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> inTransactionCountUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> clobOpenCountUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> blobOpenCountUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> readStringLengthUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> readBytesLengthUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> inputStreamOpenCountUpdater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> readerOpenCountUpdater;
    private volatile long histogram_0_1;
    private volatile long histogram_1_10;
    private volatile int histogram_10_100;
    private volatile int histogram_100_1000;
    private volatile int histogram_1000_10000;
    private volatile int histogram_10000_100000;
    private volatile int histogram_100000_1000000;
    private volatile int histogram_1000000_more;
    static final AtomicLongFieldUpdater<JdbcSqlStat> histogram_0_1_Updater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> histogram_1_10_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> histogram_10_100_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> histogram_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> histogram_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> histogram_10000_100000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> histogram_100000_1000000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> histogram_1000000_more_Updater;
    private volatile long executeAndResultHoldTime_0_1;
    private volatile long executeAndResultHoldTime_1_10;
    private volatile int executeAndResultHoldTime_10_100;
    private volatile int executeAndResultHoldTime_100_1000;
    private volatile int executeAndResultHoldTime_1000_10000;
    private volatile int executeAndResultHoldTime_10000_100000;
    private volatile int executeAndResultHoldTime_100000_1000000;
    private volatile int executeAndResultHoldTime_1000000_more;
    static final AtomicLongFieldUpdater<JdbcSqlStat> executeAndResultHoldTime_0_1_Updater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> executeAndResultHoldTime_1_10_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> executeAndResultHoldTime_10_100_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> executeAndResultHoldTime_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> executeAndResultHoldTime_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> executeAndResultHoldTime_10000_100000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> executeAndResultHoldTime_100000_1000000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> executeAndResultHoldTime_1000000_more_Updater;
    private volatile long fetchRowCount_0_1;
    private volatile long fetchRowCount_1_10;
    private volatile long fetchRowCount_10_100;
    private volatile int fetchRowCount_100_1000;
    private volatile int fetchRowCount_1000_10000;
    private volatile int fetchRowCount_10000_more;
    static final AtomicLongFieldUpdater<JdbcSqlStat> fetchRowCount_0_1_Updater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> fetchRowCount_1_10_Updater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> fetchRowCount_10_100_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> fetchRowCount_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> fetchRowCount_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> fetchRowCount_10000_more_Updater;
    private volatile long updateCount_0_1;
    private volatile long updateCount_1_10;
    private volatile long updateCount_10_100;
    private volatile int updateCount_100_1000;
    private volatile int updateCount_1000_10000;
    private volatile int updateCount_10000_more;
    static final AtomicLongFieldUpdater<JdbcSqlStat> updateCount_0_1_Updater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> updateCount_1_10_Updater;
    static final AtomicLongFieldUpdater<JdbcSqlStat> updateCount_10_100_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> updateCount_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> updateCount_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<JdbcSqlStat> updateCount_10000_more_Updater;
    private static CompositeType COMPOSITE_TYPE;
    
    public JdbcSqlStat(final String sql) {
        this.removed = false;
        this.sql = sql;
        this.id = DruidDriver.createSqlStatId();
    }
    
    public String getLastSlowParameters() {
        return this.lastSlowParameters;
    }
    
    public void setLastSlowParameters(final String lastSlowParameters) {
        this.lastSlowParameters = lastSlowParameters;
    }
    
    public String getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final String dbType) {
        this.dbType = dbType;
    }
    
    public String getDataSource() {
        return this.dataSource;
    }
    
    public void setDataSource(final String dataSource) {
        this.dataSource = dataSource;
    }
    
    public static final String getContextSqlName() {
        final JdbcStatContext context = JdbcStatManager.getInstance().getStatContext();
        if (context == null) {
            return null;
        }
        return context.getName();
    }
    
    public static final void setContextSqlName(final String val) {
        JdbcStatContext context = JdbcStatManager.getInstance().getStatContext();
        if (context == null) {
            context = JdbcStatManager.getInstance().createStatContext();
            JdbcStatManager.getInstance().setStatContext(context);
        }
        context.setName(val);
    }
    
    public static final String getContextSqlFile() {
        final JdbcStatContext context = JdbcStatManager.getInstance().getStatContext();
        if (context == null) {
            return null;
        }
        return context.getFile();
    }
    
    public static final void setContextSqlFile(final String val) {
        JdbcStatContext context = JdbcStatManager.getInstance().getStatContext();
        if (context == null) {
            context = JdbcStatManager.getInstance().createStatContext();
            JdbcStatManager.getInstance().setStatContext(context);
        }
        context.setFile(val);
    }
    
    public static final void setContextSql(final String val) {
        JdbcStatContext context = JdbcStatManager.getInstance().getStatContext();
        if (context == null) {
            context = JdbcStatManager.getInstance().createStatContext();
            JdbcStatManager.getInstance().setStatContext(context);
        }
        context.setSql(val);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String getFile() {
        return this.file;
    }
    
    public void setFile(final String file) {
        this.file = file;
    }
    
    @Override
    public void reset() {
        this.executeLastStartTime = 0L;
        JdbcSqlStat.executeBatchSizeTotalUpdater.set(this, 0L);
        JdbcSqlStat.executeBatchSizeMaxUpdater.set(this, 0);
        JdbcSqlStat.executeSuccessCountUpdater.set(this, 0L);
        JdbcSqlStat.executeSpanNanoTotalUpdater.set(this, 0L);
        JdbcSqlStat.executeSpanNanoMaxUpdater.set(this, 0L);
        this.executeNanoSpanMaxOccurTime = 0L;
        JdbcSqlStat.concurrentMaxUpdater.set(this, 0);
        JdbcSqlStat.executeErrorCountUpdater.set(this, 0L);
        this.executeErrorLast = null;
        this.executeErrorLastTime = 0L;
        JdbcSqlStat.updateCountUpdater.set(this, 0L);
        JdbcSqlStat.updateCountMaxUpdater.set(this, 0L);
        JdbcSqlStat.fetchRowCountUpdater.set(this, 0L);
        JdbcSqlStat.fetchRowCountMaxUpdater.set(this, 0L);
        JdbcSqlStat.histogram_0_1_Updater.set(this, 0L);
        JdbcSqlStat.histogram_1_10_Updater.set(this, 0L);
        JdbcSqlStat.histogram_10_100_Updater.set(this, 0);
        JdbcSqlStat.histogram_100_1000_Updater.set(this, 0);
        JdbcSqlStat.histogram_1000_10000_Updater.set(this, 0);
        JdbcSqlStat.histogram_10000_100000_Updater.set(this, 0);
        JdbcSqlStat.histogram_100000_1000000_Updater.set(this, 0);
        JdbcSqlStat.histogram_1000000_more_Updater.set(this, 0);
        this.lastSlowParameters = null;
        JdbcSqlStat.inTransactionCountUpdater.set(this, 0L);
        JdbcSqlStat.resultSetHoldTimeNanoUpdater.set(this, 0L);
        JdbcSqlStat.executeAndResultSetHoldTimeUpdater.set(this, 0L);
        JdbcSqlStat.fetchRowCount_0_1_Updater.set(this, 0L);
        JdbcSqlStat.fetchRowCount_1_10_Updater.set(this, 0L);
        JdbcSqlStat.fetchRowCount_10_100_Updater.set(this, 0L);
        JdbcSqlStat.fetchRowCount_100_1000_Updater.set(this, 0);
        JdbcSqlStat.fetchRowCount_1000_10000_Updater.set(this, 0);
        JdbcSqlStat.fetchRowCount_10000_more_Updater.set(this, 0);
        JdbcSqlStat.updateCount_0_1_Updater.set(this, 0L);
        JdbcSqlStat.updateCount_1_10_Updater.set(this, 0L);
        JdbcSqlStat.updateCount_10_100_Updater.set(this, 0L);
        JdbcSqlStat.updateCount_100_1000_Updater.set(this, 0);
        JdbcSqlStat.updateCount_1000_10000_Updater.set(this, 0);
        JdbcSqlStat.updateCount_10000_more_Updater.set(this, 0);
        JdbcSqlStat.executeAndResultHoldTime_0_1_Updater.set(this, 0L);
        JdbcSqlStat.executeAndResultHoldTime_1_10_Updater.set(this, 0L);
        JdbcSqlStat.executeAndResultHoldTime_10_100_Updater.set(this, 0);
        JdbcSqlStat.executeAndResultHoldTime_100_1000_Updater.set(this, 0);
        JdbcSqlStat.executeAndResultHoldTime_1000_10000_Updater.set(this, 0);
        JdbcSqlStat.executeAndResultHoldTime_10000_100000_Updater.set(this, 0);
        JdbcSqlStat.executeAndResultHoldTime_100000_1000000_Updater.set(this, 0);
        JdbcSqlStat.executeAndResultHoldTime_1000000_more_Updater.set(this, 0);
        JdbcSqlStat.blobOpenCountUpdater.set(this, 0L);
        JdbcSqlStat.clobOpenCountUpdater.set(this, 0L);
        JdbcSqlStat.readStringLengthUpdater.set(this, 0L);
        JdbcSqlStat.readBytesLengthUpdater.set(this, 0L);
        JdbcSqlStat.inputStreamOpenCountUpdater.set(this, 0L);
        JdbcSqlStat.readerOpenCountUpdater.set(this, 0L);
    }
    
    public JdbcSqlStatValue getValueAndReset() {
        return this.getValue(true);
    }
    
    public JdbcSqlStatValue getValue(final boolean reset) {
        final JdbcSqlStatValue val = new JdbcSqlStatValue();
        val.setDbType(this.dbType);
        val.setSql(this.sql);
        val.setSqlHash(this.getSqlHash());
        val.setId(this.id);
        val.setName(this.name);
        val.setFile(this.file);
        val.setExecuteLastStartTime(this.executeLastStartTime);
        if (reset) {
            this.executeLastStartTime = 0L;
        }
        val.setExecuteBatchSizeTotal(JdbcSqlStatUtils.get(this, JdbcSqlStat.executeBatchSizeTotalUpdater, reset));
        val.setExecuteBatchSizeMax(JdbcSqlStatUtils.get(this, JdbcSqlStat.executeBatchSizeMaxUpdater, reset));
        val.setExecuteSuccessCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.executeSuccessCountUpdater, reset));
        val.setExecuteSpanNanoTotal(JdbcSqlStatUtils.get(this, JdbcSqlStat.executeSpanNanoTotalUpdater, reset));
        val.setExecuteSpanNanoMax(JdbcSqlStatUtils.get(this, JdbcSqlStat.executeSpanNanoMaxUpdater, reset));
        val.setExecuteNanoSpanMaxOccurTime(this.executeNanoSpanMaxOccurTime);
        if (reset) {
            this.executeNanoSpanMaxOccurTime = 0L;
        }
        val.setRunningCount(this.runningCount);
        val.setConcurrentMax(JdbcSqlStatUtils.get(this, JdbcSqlStat.concurrentMaxUpdater, reset));
        val.setExecuteErrorCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.executeErrorCountUpdater, reset));
        val.setExecuteErrorLast(this.executeErrorLast);
        if (reset) {
            this.executeErrorLast = null;
        }
        val.setExecuteErrorLastTime(this.executeErrorLastTime);
        if (reset) {
            this.executeErrorLastTime = 0L;
        }
        val.setUpdateCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.updateCountUpdater, reset));
        val.setUpdateCountMax(JdbcSqlStatUtils.get(this, JdbcSqlStat.updateCountMaxUpdater, reset));
        val.setFetchRowCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.fetchRowCountUpdater, reset));
        val.setFetchRowCountMax(JdbcSqlStatUtils.get(this, JdbcSqlStat.fetchRowCountMaxUpdater, reset));
        val.histogram_0_1 = JdbcSqlStatUtils.get(this, JdbcSqlStat.histogram_0_1_Updater, reset);
        val.histogram_1_10 = JdbcSqlStatUtils.get(this, JdbcSqlStat.histogram_1_10_Updater, reset);
        val.histogram_10_100 = JdbcSqlStatUtils.get(this, JdbcSqlStat.histogram_10_100_Updater, reset);
        val.histogram_100_1000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.histogram_100_1000_Updater, reset);
        val.histogram_1000_10000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.histogram_1000_10000_Updater, reset);
        val.histogram_10000_100000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.histogram_10000_100000_Updater, reset);
        val.histogram_100000_1000000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.histogram_100000_1000000_Updater, reset);
        val.histogram_1000000_more = JdbcSqlStatUtils.get(this, JdbcSqlStat.histogram_1000000_more_Updater, reset);
        val.setLastSlowParameters(this.lastSlowParameters);
        if (reset) {
            this.lastSlowParameters = null;
        }
        val.setInTransactionCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.inTransactionCountUpdater, reset));
        val.setResultSetHoldTimeNano(JdbcSqlStatUtils.get(this, JdbcSqlStat.resultSetHoldTimeNanoUpdater, reset));
        val.setExecuteAndResultSetHoldTime(JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultSetHoldTimeUpdater, reset));
        val.fetchRowCount_0_1 = JdbcSqlStatUtils.get(this, JdbcSqlStat.fetchRowCount_0_1_Updater, reset);
        val.fetchRowCount_1_10 = JdbcSqlStatUtils.get(this, JdbcSqlStat.fetchRowCount_1_10_Updater, reset);
        val.fetchRowCount_10_100 = JdbcSqlStatUtils.get(this, JdbcSqlStat.fetchRowCount_10_100_Updater, reset);
        val.fetchRowCount_100_1000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.fetchRowCount_100_1000_Updater, reset);
        val.fetchRowCount_1000_10000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.fetchRowCount_1000_10000_Updater, reset);
        val.fetchRowCount_10000_more = JdbcSqlStatUtils.get(this, JdbcSqlStat.fetchRowCount_10000_more_Updater, reset);
        val.updateCount_0_1 = JdbcSqlStatUtils.get(this, JdbcSqlStat.updateCount_0_1_Updater, reset);
        val.updateCount_1_10 = JdbcSqlStatUtils.get(this, JdbcSqlStat.updateCount_1_10_Updater, reset);
        val.updateCount_10_100 = JdbcSqlStatUtils.get(this, JdbcSqlStat.updateCount_10_100_Updater, reset);
        val.updateCount_100_1000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.updateCount_100_1000_Updater, reset);
        val.updateCount_1000_10000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.updateCount_1000_10000_Updater, reset);
        val.updateCount_10000_more = JdbcSqlStatUtils.get(this, JdbcSqlStat.updateCount_10000_more_Updater, reset);
        val.executeAndResultHoldTime_0_1 = JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultHoldTime_0_1_Updater, reset);
        val.executeAndResultHoldTime_1_10 = JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultHoldTime_1_10_Updater, reset);
        val.executeAndResultHoldTime_10_100 = JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultHoldTime_10_100_Updater, reset);
        val.executeAndResultHoldTime_100_1000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultHoldTime_100_1000_Updater, reset);
        val.executeAndResultHoldTime_1000_10000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultHoldTime_1000_10000_Updater, reset);
        val.executeAndResultHoldTime_10000_100000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultHoldTime_10000_100000_Updater, reset);
        val.executeAndResultHoldTime_100000_1000000 = JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultHoldTime_100000_1000000_Updater, reset);
        val.executeAndResultHoldTime_1000000_more = JdbcSqlStatUtils.get(this, JdbcSqlStat.executeAndResultHoldTime_1000000_more_Updater, reset);
        val.setBlobOpenCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.blobOpenCountUpdater, reset));
        val.setClobOpenCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.clobOpenCountUpdater, reset));
        val.setReadStringLength(JdbcSqlStatUtils.get(this, JdbcSqlStat.readStringLengthUpdater, reset));
        val.setReadBytesLength(JdbcSqlStatUtils.get(this, JdbcSqlStat.readBytesLengthUpdater, reset));
        val.setInputStreamOpenCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.inputStreamOpenCountUpdater, reset));
        val.setReaderOpenCount(JdbcSqlStatUtils.get(this, JdbcSqlStat.readerOpenCountUpdater, reset));
        return val;
    }
    
    @Override
    public long getConcurrentMax() {
        return this.concurrentMax;
    }
    
    @Override
    public long getRunningCount() {
        return this.runningCount;
    }
    
    public void addUpdateCount(final int delta) {
        if (delta > 0) {
            JdbcSqlStat.updateCountUpdater.addAndGet(this, delta);
        }
        long max;
        do {
            max = JdbcSqlStat.updateCountMaxUpdater.get(this);
            if (delta <= max) {
                break;
            }
        } while (!JdbcSqlStat.updateCountMaxUpdater.compareAndSet(this, max, delta));
        if (delta < 1) {
            JdbcSqlStat.updateCount_0_1_Updater.incrementAndGet(this);
        }
        else if (delta < 10) {
            JdbcSqlStat.updateCount_1_10_Updater.incrementAndGet(this);
        }
        else if (delta < 100) {
            JdbcSqlStat.updateCount_10_100_Updater.incrementAndGet(this);
        }
        else if (delta < 1000) {
            JdbcSqlStat.updateCount_100_1000_Updater.incrementAndGet(this);
        }
        else if (delta < 10000) {
            JdbcSqlStat.updateCount_1000_10000_Updater.incrementAndGet(this);
        }
        else {
            JdbcSqlStat.updateCount_10000_more_Updater.incrementAndGet(this);
        }
    }
    
    @Override
    public long getUpdateCount() {
        return this.updateCount;
    }
    
    public long getUpdateCountMax() {
        return this.updateCountMax;
    }
    
    @Override
    public long getFetchRowCount() {
        return this.fetchRowCount;
    }
    
    public long getFetchRowCountMax() {
        return this.fetchRowCountMax;
    }
    
    public long getClobOpenCount() {
        return this.clobOpenCount;
    }
    
    public void incrementClobOpenCount() {
        JdbcSqlStat.clobOpenCountUpdater.incrementAndGet(this);
    }
    
    public long getBlobOpenCount() {
        return this.blobOpenCount;
    }
    
    public void incrementBlobOpenCount() {
        JdbcSqlStat.blobOpenCountUpdater.incrementAndGet(this);
    }
    
    public long getReadStringLength() {
        return this.readStringLength;
    }
    
    public void addStringReadLength(final long length) {
        JdbcSqlStat.readStringLengthUpdater.addAndGet(this, length);
    }
    
    public long getReadBytesLength() {
        return this.readBytesLength;
    }
    
    public void addReadBytesLength(final long length) {
        JdbcSqlStat.readBytesLengthUpdater.addAndGet(this, length);
    }
    
    public long getReaderOpenCount() {
        return this.readerOpenCount;
    }
    
    public void addReaderOpenCount(final int count) {
        JdbcSqlStat.readerOpenCountUpdater.addAndGet(this, count);
    }
    
    public long getInputStreamOpenCount() {
        return this.inputStreamOpenCount;
    }
    
    public void addInputStreamOpenCount(final int count) {
        JdbcSqlStat.inputStreamOpenCountUpdater.addAndGet(this, count);
    }
    
    @Override
    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    @Override
    public String getSql() {
        return this.sql;
    }
    
    public long getSqlHash() {
        if (this.sqlHash == 0L) {
            this.sqlHash = Utils.fnv_64(this.sql);
        }
        return this.sqlHash;
    }
    
    @Override
    public Date getExecuteLastStartTime() {
        if (this.executeLastStartTime <= 0L) {
            return null;
        }
        return new Date(this.executeLastStartTime);
    }
    
    public void setExecuteLastStartTime(final long executeLastStartTime) {
        this.executeLastStartTime = executeLastStartTime;
    }
    
    @Override
    public Date getExecuteNanoSpanMaxOccurTime() {
        if (this.executeNanoSpanMaxOccurTime <= 0L) {
            return null;
        }
        return new Date(this.executeNanoSpanMaxOccurTime);
    }
    
    @Override
    public Date getExecuteErrorLastTime() {
        if (this.executeErrorLastTime <= 0L) {
            return null;
        }
        return new Date(this.executeErrorLastTime);
    }
    
    public void addFetchRowCount(final long delta) {
        JdbcSqlStat.fetchRowCountUpdater.addAndGet(this, delta);
        long max;
        do {
            max = JdbcSqlStat.fetchRowCountMaxUpdater.get(this);
            if (delta <= max) {
                break;
            }
        } while (!JdbcSqlStat.fetchRowCountMaxUpdater.compareAndSet(this, max, delta));
        if (delta < 1L) {
            JdbcSqlStat.fetchRowCount_0_1_Updater.incrementAndGet(this);
        }
        else if (delta < 10L) {
            JdbcSqlStat.fetchRowCount_1_10_Updater.incrementAndGet(this);
        }
        else if (delta < 100L) {
            JdbcSqlStat.fetchRowCount_10_100_Updater.incrementAndGet(this);
        }
        else if (delta < 1000L) {
            JdbcSqlStat.fetchRowCount_100_1000_Updater.incrementAndGet(this);
        }
        else if (delta < 10000L) {
            JdbcSqlStat.fetchRowCount_1000_10000_Updater.incrementAndGet(this);
        }
        else {
            JdbcSqlStat.fetchRowCount_10000_more_Updater.incrementAndGet(this);
        }
    }
    
    public void addExecuteBatchCount(final long batchSize) {
        JdbcSqlStat.executeBatchSizeTotalUpdater.addAndGet(this, batchSize);
        int current;
        do {
            current = JdbcSqlStat.executeBatchSizeMaxUpdater.get(this);
            if (current >= batchSize) {
                break;
            }
        } while (!JdbcSqlStat.executeBatchSizeMaxUpdater.compareAndSet(this, current, (int)batchSize));
    }
    
    @Override
    public long getExecuteBatchSizeTotal() {
        return this.executeBatchSizeTotal;
    }
    
    public void incrementExecuteSuccessCount() {
        JdbcSqlStat.executeSuccessCountUpdater.incrementAndGet(this);
    }
    
    public void incrementRunningCount() {
        final int val = JdbcSqlStat.runningCountUpdater.incrementAndGet(this);
        int max;
        do {
            max = JdbcSqlStat.concurrentMaxUpdater.get(this);
            if (val <= max) {
                break;
            }
        } while (!JdbcSqlStat.concurrentMaxUpdater.compareAndSet(this, max, val));
    }
    
    public void decrementRunningCount() {
        JdbcSqlStat.runningCountUpdater.decrementAndGet(this);
    }
    
    public void decrementExecutingCount() {
        JdbcSqlStat.runningCountUpdater.decrementAndGet(this);
    }
    
    @Override
    public long getExecuteSuccessCount() {
        return this.executeSuccessCount;
    }
    
    public void addExecuteTime(final StatementExecuteType executeType, final boolean firstResultSet, final long nanoSpan) {
        this.addExecuteTime(nanoSpan);
        if (StatementExecuteType.ExecuteQuery != executeType && !firstResultSet) {
            this.executeAndResultHoldTimeHistogramRecord(nanoSpan);
        }
    }
    
    private void executeAndResultHoldTimeHistogramRecord(final long nanoSpan) {
        final long millis = nanoSpan / 1000L / 1000L;
        if (millis < 1L) {
            JdbcSqlStat.executeAndResultHoldTime_0_1_Updater.incrementAndGet(this);
        }
        else if (millis < 10L) {
            JdbcSqlStat.executeAndResultHoldTime_1_10_Updater.incrementAndGet(this);
        }
        else if (millis < 100L) {
            JdbcSqlStat.executeAndResultHoldTime_10_100_Updater.incrementAndGet(this);
        }
        else if (millis < 1000L) {
            JdbcSqlStat.executeAndResultHoldTime_100_1000_Updater.incrementAndGet(this);
        }
        else if (millis < 10000L) {
            JdbcSqlStat.executeAndResultHoldTime_1000_10000_Updater.incrementAndGet(this);
        }
        else if (millis < 100000L) {
            JdbcSqlStat.executeAndResultHoldTime_10000_100000_Updater.incrementAndGet(this);
        }
        else if (millis < 1000000L) {
            JdbcSqlStat.executeAndResultHoldTime_100000_1000000_Updater.incrementAndGet(this);
        }
        else {
            JdbcSqlStat.executeAndResultHoldTime_1000000_more_Updater.incrementAndGet(this);
        }
    }
    
    private void histogramRecord(final long nanoSpan) {
        final long millis = nanoSpan / 1000L / 1000L;
        if (millis < 1L) {
            JdbcSqlStat.histogram_0_1_Updater.incrementAndGet(this);
        }
        else if (millis < 10L) {
            JdbcSqlStat.histogram_1_10_Updater.incrementAndGet(this);
        }
        else if (millis < 100L) {
            JdbcSqlStat.histogram_10_100_Updater.incrementAndGet(this);
        }
        else if (millis < 1000L) {
            JdbcSqlStat.histogram_100_1000_Updater.incrementAndGet(this);
        }
        else if (millis < 10000L) {
            JdbcSqlStat.histogram_1000_10000_Updater.incrementAndGet(this);
        }
        else if (millis < 100000L) {
            JdbcSqlStat.histogram_10000_100000_Updater.incrementAndGet(this);
        }
        else if (millis < 1000000L) {
            JdbcSqlStat.histogram_100000_1000000_Updater.incrementAndGet(this);
        }
        else {
            JdbcSqlStat.histogram_1000000_more_Updater.incrementAndGet(this);
        }
    }
    
    public void addExecuteTime(final long nanoSpan) {
        JdbcSqlStat.executeSpanNanoTotalUpdater.addAndGet(this, nanoSpan);
        while (true) {
            final long current = JdbcSqlStat.executeSpanNanoMaxUpdater.get(this);
            if (current >= nanoSpan) {
                break;
            }
            if (JdbcSqlStat.executeSpanNanoMaxUpdater.compareAndSet(this, current, nanoSpan)) {
                this.executeNanoSpanMaxOccurTime = System.currentTimeMillis();
                break;
            }
        }
        this.histogramRecord(nanoSpan);
    }
    
    @Override
    public long getExecuteMillisTotal() {
        return this.executeSpanNanoTotal / 1000000L;
    }
    
    @Override
    public long getExecuteMillisMax() {
        return this.executeSpanNanoMax / 1000000L;
    }
    
    @Override
    public long getErrorCount() {
        return this.executeErrorCount;
    }
    
    @Override
    public long getExecuteBatchSizeMax() {
        return this.executeBatchSizeMax;
    }
    
    public long getInTransactionCount() {
        return this.inTransactionCount;
    }
    
    public void incrementInTransactionCount() {
        JdbcSqlStat.inTransactionCountUpdater.incrementAndGet(this);
    }
    
    public static CompositeType getCompositeType() throws JMException {
        if (JdbcSqlStat.COMPOSITE_TYPE != null) {
            return JdbcSqlStat.COMPOSITE_TYPE;
        }
        final OpenType<?>[] indexTypes = (OpenType<?>[])new OpenType[] { SimpleType.LONG, SimpleType.STRING, SimpleType.STRING, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.DATE, SimpleType.LONG, JMXUtils.getThrowableCompositeType(), SimpleType.LONG, SimpleType.LONG, SimpleType.DATE, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, SimpleType.DATE, SimpleType.STRING, SimpleType.LONG, SimpleType.STRING, new ArrayType(SimpleType.LONG, true), SimpleType.STRING, SimpleType.LONG, SimpleType.LONG, new ArrayType(SimpleType.LONG, true), new ArrayType(SimpleType.LONG, true), new ArrayType(SimpleType.LONG, true), SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG };
        final String[] indexDescriptions;
        final String[] indexNames = indexDescriptions = new String[] { "ID", "DataSource", "SQL", "ExecuteCount", "ErrorCount", "TotalTime", "LastTime", "MaxTimespan", "LastError", "EffectedRowCount", "FetchRowCount", "MaxTimespanOccurTime", "BatchSizeMax", "BatchSizeTotal", "ConcurrentMax", "RunningCount", "Name", "File", "LastErrorMessage", "LastErrorClass", "LastErrorStackTrace", "LastErrorTime", "DbType", "InTransactionCount", "URL", "Histogram", "LastSlowParameters", "ResultSetHoldTime", "ExecuteAndResultSetHoldTime", "FetchRowCountHistogram", "EffectedRowCountHistogram", "ExecuteAndResultHoldTimeHistogram", "EffectedRowCountMax", "FetchRowCountMax", "ClobOpenCount", "BlobOpenCount", "ReadStringLength", "ReadBytesLength", "InputStreamOpenCount", "ReaderOpenCount", "HASH" };
        return JdbcSqlStat.COMPOSITE_TYPE = new CompositeType("SqlStatistic", "Sql Statistic", indexNames, indexDescriptions, indexTypes);
    }
    
    @Override
    public long getExecuteCount() {
        return this.getErrorCount() + this.getExecuteSuccessCount();
    }
    
    public Map<String, Object> getData() throws JMException {
        return this.getValue(false).getData();
    }
    
    public long[] getHistogramValues() {
        return new long[] { this.histogram_0_1, this.histogram_1_10, this.histogram_10_100, this.histogram_100_1000, this.histogram_1000_10000, this.histogram_10000_100000, this.histogram_100000_1000000, this.histogram_1000000_more };
    }
    
    public long getHistogramSum() {
        final long[] values = this.getHistogramValues();
        long sum = 0L;
        for (int i = 0; i < values.length; ++i) {
            sum += values[i];
        }
        return sum;
    }
    
    public CompositeDataSupport getCompositeData() throws JMException {
        return new CompositeDataSupport(getCompositeType(), this.getData());
    }
    
    public Throwable getExecuteErrorLast() {
        return this.executeErrorLast;
    }
    
    public void error(final Throwable error) {
        JdbcSqlStat.executeErrorCountUpdater.incrementAndGet(this);
        this.executeErrorLastTime = System.currentTimeMillis();
        this.executeErrorLast = error;
    }
    
    public long getResultSetHoldTimeMilis() {
        return this.getResultSetHoldTimeNano() / 1000000L;
    }
    
    public long getExecuteAndResultSetHoldTimeMilis() {
        return this.getExecuteAndResultSetHoldTimeNano() / 1000000L;
    }
    
    public long[] getFetchRowCountHistogramValues() {
        return new long[] { this.fetchRowCount_0_1, this.fetchRowCount_1_10, this.fetchRowCount_10_100, this.fetchRowCount_100_1000, this.fetchRowCount_1000_10000, this.fetchRowCount_10000_more };
    }
    
    public long[] getUpdateCountHistogramValues() {
        return new long[] { this.updateCount_0_1, this.updateCount_1_10, this.updateCount_10_100, this.updateCount_100_1000, this.updateCount_1000_10000, this.updateCount_10000_more };
    }
    
    public long[] getExecuteAndResultHoldTimeHistogramValues() {
        return new long[] { this.executeAndResultHoldTime_0_1, this.executeAndResultHoldTime_1_10, this.executeAndResultHoldTime_10_100, this.executeAndResultHoldTime_100_1000, this.executeAndResultHoldTime_1000_10000, this.executeAndResultHoldTime_10000_100000, this.executeAndResultHoldTime_100000_1000000, this.executeAndResultHoldTime_1000000_more };
    }
    
    public long getExecuteAndResultHoldTimeHistogramSum() {
        final long[] values = this.getExecuteAndResultHoldTimeHistogramValues();
        long sum = 0L;
        for (int i = 0; i < values.length; ++i) {
            sum += values[i];
        }
        return sum;
    }
    
    public long getResultSetHoldTimeNano() {
        return this.resultSetHoldTimeNano;
    }
    
    public long getExecuteAndResultSetHoldTimeNano() {
        return this.executeAndResultSetHoldTime;
    }
    
    public void addResultSetHoldTimeNano(final long nano) {
        JdbcSqlStat.resultSetHoldTimeNanoUpdater.addAndGet(this, nano);
    }
    
    public void addResultSetHoldTimeNano(final long statementExecuteNano, final long resultHoldTimeNano) {
        JdbcSqlStat.resultSetHoldTimeNanoUpdater.addAndGet(this, resultHoldTimeNano);
        JdbcSqlStat.executeAndResultSetHoldTimeUpdater.addAndGet(this, statementExecuteNano + resultHoldTimeNano);
        this.executeAndResultHoldTimeHistogramRecord(statementExecuteNano + resultHoldTimeNano);
        JdbcSqlStat.updateCount_0_1_Updater.incrementAndGet(this);
    }
    
    public boolean isRemoved() {
        return this.removed;
    }
    
    public void setRemoved(final boolean removed) {
        this.removed = removed;
    }
    
    @Override
    public int compareTo(final JdbcSqlStat o) {
        if (o.sqlHash == this.sqlHash) {
            return 0;
        }
        return (this.id < o.id) ? -1 : 1;
    }
    
    static {
        executeBatchSizeTotalUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "executeBatchSizeTotal");
        executeBatchSizeMaxUpdater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "executeBatchSizeMax");
        executeSuccessCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "executeSuccessCount");
        executeSpanNanoTotalUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "executeSpanNanoTotal");
        executeSpanNanoMaxUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "executeSpanNanoMax");
        runningCountUpdater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "runningCount");
        concurrentMaxUpdater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "concurrentMax");
        resultSetHoldTimeNanoUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "resultSetHoldTimeNano");
        executeAndResultSetHoldTimeUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultSetHoldTime");
        executeErrorCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "executeErrorCount");
        updateCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "updateCount");
        updateCountMaxUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "updateCountMax");
        fetchRowCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "fetchRowCount");
        fetchRowCountMaxUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "fetchRowCountMax");
        inTransactionCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "inTransactionCount");
        clobOpenCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "clobOpenCount");
        blobOpenCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "blobOpenCount");
        readStringLengthUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "readStringLength");
        readBytesLengthUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "readBytesLength");
        inputStreamOpenCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "inputStreamOpenCount");
        readerOpenCountUpdater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "readerOpenCount");
        histogram_0_1_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "histogram_0_1");
        histogram_1_10_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "histogram_1_10");
        histogram_10_100_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "histogram_10_100");
        histogram_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "histogram_100_1000");
        histogram_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "histogram_1000_10000");
        histogram_10000_100000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "histogram_10000_100000");
        histogram_100000_1000000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "histogram_100000_1000000");
        histogram_1000000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "histogram_1000000_more");
        executeAndResultHoldTime_0_1_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultHoldTime_0_1");
        executeAndResultHoldTime_1_10_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultHoldTime_1_10");
        executeAndResultHoldTime_10_100_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultHoldTime_10_100");
        executeAndResultHoldTime_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultHoldTime_100_1000");
        executeAndResultHoldTime_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultHoldTime_1000_10000");
        executeAndResultHoldTime_10000_100000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultHoldTime_10000_100000");
        executeAndResultHoldTime_100000_1000000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultHoldTime_100000_1000000");
        executeAndResultHoldTime_1000000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "executeAndResultHoldTime_1000000_more");
        fetchRowCount_0_1_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "fetchRowCount_0_1");
        fetchRowCount_1_10_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "fetchRowCount_1_10");
        fetchRowCount_10_100_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "fetchRowCount_10_100");
        fetchRowCount_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "fetchRowCount_100_1000");
        fetchRowCount_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "fetchRowCount_1000_10000");
        fetchRowCount_10000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "fetchRowCount_10000_more");
        updateCount_0_1_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "updateCount_0_1");
        updateCount_1_10_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "updateCount_1_10");
        updateCount_10_100_Updater = AtomicLongFieldUpdater.newUpdater(JdbcSqlStat.class, "updateCount_10_100");
        updateCount_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "updateCount_100_1000");
        updateCount_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "updateCount_1000_10000");
        updateCount_10000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(JdbcSqlStat.class, "updateCount_10000_more");
        JdbcSqlStat.COMPOSITE_TYPE = null;
    }
}
