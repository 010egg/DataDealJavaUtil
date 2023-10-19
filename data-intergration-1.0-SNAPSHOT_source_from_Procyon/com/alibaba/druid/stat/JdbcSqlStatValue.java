// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import javax.management.JMException;
import com.alibaba.druid.util.JMXUtils;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.druid.util.Utils;
import java.util.Date;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import com.alibaba.druid.support.monitor.annotation.MField;
import com.alibaba.druid.support.monitor.annotation.MTable;

@MTable(name = "druid_sql")
public class JdbcSqlStatValue
{
    protected long id;
    protected String sql;
    @MField(groupBy = true, aggregate = AggregateType.None, hashFor = "sql", hashForType = "sql")
    private long sqlHash;
    @MField(aggregate = AggregateType.None)
    protected String dataSource;
    @MField(name = "lastStartTime", aggregate = AggregateType.Max)
    protected long executeLastStartTime;
    @MField(name = "batchTotal", aggregate = AggregateType.Sum)
    protected long executeBatchSizeTotal;
    @MField(name = "batchToMax", aggregate = AggregateType.Max)
    protected int executeBatchSizeMax;
    @MField(name = "execSuccessCount", aggregate = AggregateType.Sum)
    protected long executeSuccessCount;
    @MField(name = "execNanoTotal", aggregate = AggregateType.Sum)
    protected long executeSpanNanoTotal;
    @MField(name = "execNanoMax", aggregate = AggregateType.Sum)
    protected long executeSpanNanoMax;
    @MField(name = "running", aggregate = AggregateType.Last)
    protected int runningCount;
    @MField(aggregate = AggregateType.Max)
    protected int concurrentMax;
    @MField(name = "rsHoldTime", aggregate = AggregateType.Sum)
    protected long resultSetHoldTimeNano;
    @MField(name = "execRsHoldTime", aggregate = AggregateType.Sum)
    protected long executeAndResultSetHoldTime;
    @MField(aggregate = AggregateType.None)
    protected String name;
    @MField(aggregate = AggregateType.None)
    protected String file;
    @MField(aggregate = AggregateType.None)
    protected String dbType;
    @MField(name = "execNanoMaxOccurTime", aggregate = AggregateType.Max)
    protected long executeNanoSpanMaxOccurTime;
    @MField(name = "errorCount", aggregate = AggregateType.Sum)
    protected long executeErrorCount;
    protected Throwable executeErrorLast;
    @MField(name = "errorLastMsg", aggregate = AggregateType.Last)
    protected String executeErrorLastMessage;
    @MField(name = "errorLastClass", aggregate = AggregateType.Last)
    protected String executeErrorLastClass;
    @MField(name = "errorLastStackTrace", aggregate = AggregateType.Last)
    protected String executeErrorLastStackTrace;
    @MField(name = "errorLastTime", aggregate = AggregateType.Last)
    protected long executeErrorLastTime;
    @MField(aggregate = AggregateType.Sum)
    protected long updateCount;
    @MField(aggregate = AggregateType.Sum)
    protected long updateCountMax;
    @MField(aggregate = AggregateType.Sum)
    protected long fetchRowCount;
    @MField(aggregate = AggregateType.Sum)
    protected long fetchRowCountMax;
    @MField(name = "inTxnCount", aggregate = AggregateType.Sum)
    protected long inTransactionCount;
    @MField(aggregate = AggregateType.Last)
    protected String lastSlowParameters;
    @MField(aggregate = AggregateType.Sum)
    protected long clobOpenCount;
    @MField(aggregate = AggregateType.Sum)
    protected long blobOpenCount;
    @MField(aggregate = AggregateType.Sum)
    protected long readStringLength;
    @MField(aggregate = AggregateType.Sum)
    protected long readBytesLength;
    @MField(aggregate = AggregateType.Sum)
    protected long inputStreamOpenCount;
    @MField(aggregate = AggregateType.Sum)
    protected long readerOpenCount;
    @MField(name = "h1", aggregate = AggregateType.Sum)
    protected long histogram_0_1;
    @MField(name = "h10", aggregate = AggregateType.Sum)
    protected long histogram_1_10;
    @MField(name = "h100", aggregate = AggregateType.Sum)
    protected int histogram_10_100;
    @MField(name = "h1000", aggregate = AggregateType.Sum)
    protected int histogram_100_1000;
    @MField(name = "h10000", aggregate = AggregateType.Sum)
    protected int histogram_1000_10000;
    @MField(name = "h100000", aggregate = AggregateType.Sum)
    protected int histogram_10000_100000;
    @MField(name = "h1000000", aggregate = AggregateType.Sum)
    protected int histogram_100000_1000000;
    @MField(name = "hmore", aggregate = AggregateType.Sum)
    protected int histogram_1000000_more;
    @MField(name = "eh1", aggregate = AggregateType.Sum)
    protected long executeAndResultHoldTime_0_1;
    @MField(name = "eh10", aggregate = AggregateType.Sum)
    protected long executeAndResultHoldTime_1_10;
    @MField(name = "eh100", aggregate = AggregateType.Sum)
    protected int executeAndResultHoldTime_10_100;
    @MField(name = "eh1000", aggregate = AggregateType.Sum)
    protected int executeAndResultHoldTime_100_1000;
    @MField(name = "eh10000", aggregate = AggregateType.Sum)
    protected int executeAndResultHoldTime_1000_10000;
    @MField(name = "eh100000", aggregate = AggregateType.Sum)
    protected int executeAndResultHoldTime_10000_100000;
    @MField(name = "eh1000000", aggregate = AggregateType.Sum)
    protected int executeAndResultHoldTime_100000_1000000;
    @MField(name = "ehmore", aggregate = AggregateType.Sum)
    protected int executeAndResultHoldTime_1000000_more;
    @MField(name = "f1", aggregate = AggregateType.Sum)
    protected long fetchRowCount_0_1;
    @MField(name = "f10", aggregate = AggregateType.Sum)
    protected long fetchRowCount_1_10;
    @MField(name = "f100", aggregate = AggregateType.Sum)
    protected long fetchRowCount_10_100;
    @MField(name = "f1000", aggregate = AggregateType.Sum)
    protected int fetchRowCount_100_1000;
    @MField(name = "f10000", aggregate = AggregateType.Sum)
    protected int fetchRowCount_1000_10000;
    @MField(name = "fmore", aggregate = AggregateType.Sum)
    protected int fetchRowCount_10000_more;
    @MField(name = "u1", aggregate = AggregateType.Sum)
    protected long updateCount_0_1;
    @MField(name = "u10", aggregate = AggregateType.Sum)
    protected long updateCount_1_10;
    @MField(name = "u100", aggregate = AggregateType.Sum)
    protected long updateCount_10_100;
    @MField(name = "u1000", aggregate = AggregateType.Sum)
    protected int updateCount_100_1000;
    @MField(name = "u10000", aggregate = AggregateType.Sum)
    protected int updateCount_1000_10000;
    @MField(name = "umore", aggregate = AggregateType.Sum)
    protected int updateCount_10000_more;
    
    public long[] getExecuteHistogram() {
        return new long[] { this.histogram_0_1, this.histogram_1_10, this.histogram_10_100, this.histogram_100_1000, this.histogram_1000_10000, this.histogram_10000_100000, this.histogram_100000_1000000, this.histogram_1000000_more };
    }
    
    public long[] getExecuteAndResultHoldHistogram() {
        return new long[] { this.executeAndResultHoldTime_0_1, this.executeAndResultHoldTime_1_10, this.executeAndResultHoldTime_10_100, this.executeAndResultHoldTime_100_1000, this.executeAndResultHoldTime_1000_10000, this.executeAndResultHoldTime_10000_100000, this.executeAndResultHoldTime_100000_1000000, this.executeAndResultHoldTime_1000000_more };
    }
    
    public long[] getFetchRowHistogram() {
        return new long[] { this.fetchRowCount_0_1, this.fetchRowCount_1_10, this.fetchRowCount_10_100, this.fetchRowCount_100_1000, this.fetchRowCount_1000_10000, this.fetchRowCount_10000_more };
    }
    
    public long[] getUpdateHistogram() {
        return new long[] { this.updateCount_0_1, this.updateCount_1_10, this.updateCount_10_100, this.updateCount_100_1000, this.updateCount_1000_10000, this.updateCount_10000_more };
    }
    
    public long getExecuteCount() {
        return this.executeErrorCount + this.executeSuccessCount;
    }
    
    public long getExecuteMillisMax() {
        return this.executeSpanNanoMax / 1000000L;
    }
    
    public long getExecuteMillisTotal() {
        return this.executeSpanNanoTotal / 1000000L;
    }
    
    public String getSql() {
        return this.sql;
    }
    
    public void setSql(final String sql) {
        this.sql = sql;
    }
    
    public long getSqlHash() {
        return this.sqlHash;
    }
    
    public void setSqlHash(final long sqlHash) {
        this.sqlHash = sqlHash;
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public String getDataSource() {
        return this.dataSource;
    }
    
    public void setDataSource(final String dataSource) {
        this.dataSource = dataSource;
    }
    
    public long getExecuteLastStartTimeMillis() {
        return this.executeLastStartTime;
    }
    
    public Date getExecuteLastStartTime() {
        if (this.executeLastStartTime <= 0L) {
            return null;
        }
        return new Date(this.executeLastStartTime);
    }
    
    public void setExecuteLastStartTime(final long executeLastStartTime) {
        this.executeLastStartTime = executeLastStartTime;
    }
    
    public long getExecuteBatchSizeTotal() {
        return this.executeBatchSizeTotal;
    }
    
    public void setExecuteBatchSizeTotal(final long executeBatchSizeTotal) {
        this.executeBatchSizeTotal = executeBatchSizeTotal;
    }
    
    public int getExecuteBatchSizeMax() {
        return this.executeBatchSizeMax;
    }
    
    public void setExecuteBatchSizeMax(final int executeBatchSizeMax) {
        this.executeBatchSizeMax = executeBatchSizeMax;
    }
    
    public long getExecuteSuccessCount() {
        return this.executeSuccessCount;
    }
    
    public void setExecuteSuccessCount(final long executeSuccessCount) {
        this.executeSuccessCount = executeSuccessCount;
    }
    
    public long getExecuteSpanNanoTotal() {
        return this.executeSpanNanoTotal;
    }
    
    public void setExecuteSpanNanoTotal(final long executeSpanNanoTotal) {
        this.executeSpanNanoTotal = executeSpanNanoTotal;
    }
    
    public long getExecuteSpanNanoMax() {
        return this.executeSpanNanoMax;
    }
    
    public void setExecuteSpanNanoMax(final long executeSpanNanoMax) {
        this.executeSpanNanoMax = executeSpanNanoMax;
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
    
    public long getResultSetHoldTimeNano() {
        return this.resultSetHoldTimeNano;
    }
    
    public void setResultSetHoldTimeNano(final long resultSetHoldTimeNano) {
        this.resultSetHoldTimeNano = resultSetHoldTimeNano;
    }
    
    public long getExecuteAndResultSetHoldTimeNano() {
        return this.executeAndResultSetHoldTime;
    }
    
    public void setExecuteAndResultSetHoldTime(final long executeAndResultSetHoldTime) {
        this.executeAndResultSetHoldTime = executeAndResultSetHoldTime;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getFile() {
        return this.file;
    }
    
    public void setFile(final String file) {
        this.file = file;
    }
    
    public String getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final String dbType) {
        this.dbType = dbType;
    }
    
    public long getExecuteNanoSpanMaxOccurTimeMillis() {
        return this.executeNanoSpanMaxOccurTime;
    }
    
    public Date getExecuteNanoSpanMaxOccurTime() {
        if (this.executeNanoSpanMaxOccurTime <= 0L) {
            return null;
        }
        return new Date(this.executeNanoSpanMaxOccurTime);
    }
    
    public Date getExecuteErrorLastTime() {
        if (this.executeErrorLastTime <= 0L) {
            return null;
        }
        return new Date(this.executeErrorLastTime);
    }
    
    public void setExecuteNanoSpanMaxOccurTime(final long executeNanoSpanMaxOccurTime) {
        this.executeNanoSpanMaxOccurTime = executeNanoSpanMaxOccurTime;
    }
    
    public long getExecuteErrorCount() {
        return this.executeErrorCount;
    }
    
    public void setExecuteErrorCount(final long executeErrorCount) {
        this.executeErrorCount = executeErrorCount;
    }
    
    public Throwable getExecuteErrorLast() {
        return this.executeErrorLast;
    }
    
    public void setExecuteErrorLast(final Throwable executeErrorLast) {
        this.executeErrorLast = executeErrorLast;
        if (executeErrorLast != null) {
            this.executeErrorLastMessage = executeErrorLast.getMessage();
            this.executeErrorLastClass = executeErrorLast.getClass().getName();
            this.executeErrorLastStackTrace = Utils.toString(executeErrorLast.getStackTrace());
        }
    }
    
    public long getExecuteErrorLastTimeMillis() {
        return this.executeErrorLastTime;
    }
    
    public void setExecuteErrorLastTime(final long executeErrorLastTime) {
        this.executeErrorLastTime = executeErrorLastTime;
    }
    
    public long getUpdateCount() {
        return this.updateCount;
    }
    
    public void setUpdateCount(final long updateCount) {
        this.updateCount = updateCount;
    }
    
    public long getUpdateCountMax() {
        return this.updateCountMax;
    }
    
    public void setUpdateCountMax(final long updateCountMax) {
        this.updateCountMax = updateCountMax;
    }
    
    public long getFetchRowCount() {
        return this.fetchRowCount;
    }
    
    public void setFetchRowCount(final long fetchRowCount) {
        this.fetchRowCount = fetchRowCount;
    }
    
    public long getFetchRowCountMax() {
        return this.fetchRowCountMax;
    }
    
    public void setFetchRowCountMax(final long fetchRowCountMax) {
        this.fetchRowCountMax = fetchRowCountMax;
    }
    
    public long getInTransactionCount() {
        return this.inTransactionCount;
    }
    
    public void setInTransactionCount(final long inTransactionCount) {
        this.inTransactionCount = inTransactionCount;
    }
    
    public String getLastSlowParameters() {
        return this.lastSlowParameters;
    }
    
    public void setLastSlowParameters(final String lastSlowParameters) {
        this.lastSlowParameters = lastSlowParameters;
    }
    
    public long getClobOpenCount() {
        return this.clobOpenCount;
    }
    
    public void setClobOpenCount(final long clobOpenCount) {
        this.clobOpenCount = clobOpenCount;
    }
    
    public long getBlobOpenCount() {
        return this.blobOpenCount;
    }
    
    public void setBlobOpenCount(final long blobOpenCount) {
        this.blobOpenCount = blobOpenCount;
    }
    
    public long getReadStringLength() {
        return this.readStringLength;
    }
    
    public void setReadStringLength(final long readStringLength) {
        this.readStringLength = readStringLength;
    }
    
    public long getReadBytesLength() {
        return this.readBytesLength;
    }
    
    public void setReadBytesLength(final long readBytesLength) {
        this.readBytesLength = readBytesLength;
    }
    
    public long getInputStreamOpenCount() {
        return this.inputStreamOpenCount;
    }
    
    public void setInputStreamOpenCount(final long inputStreamOpenCount) {
        this.inputStreamOpenCount = inputStreamOpenCount;
    }
    
    public long getReaderOpenCount() {
        return this.readerOpenCount;
    }
    
    public void setReaderOpenCount(final long readerOpenCount) {
        this.readerOpenCount = readerOpenCount;
    }
    
    public long[] getHistogramValues() {
        return new long[] { this.histogram_0_1, this.histogram_1_10, this.histogram_10_100, this.histogram_100_1000, this.histogram_1000_10000, this.histogram_10000_100000, this.histogram_100000_1000000, this.histogram_1000000_more };
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
    
    public long getResultSetHoldTimeMilis() {
        return this.getResultSetHoldTimeNano() / 1000000L;
    }
    
    public long getExecuteAndResultSetHoldTimeMilis() {
        return this.getExecuteAndResultSetHoldTimeNano() / 1000000L;
    }
    
    public Map<String, Object> getData() throws JMException {
        final Map<String, Object> map = new HashMap<String, Object>(48);
        map.put("ID", this.id);
        map.put("DataSource", this.dataSource);
        map.put("SQL", this.sql);
        map.put("ExecuteCount", this.getExecuteCount());
        map.put("ErrorCount", this.getExecuteErrorCount());
        map.put("TotalTime", this.getExecuteMillisTotal());
        map.put("LastTime", this.getExecuteLastStartTime());
        map.put("MaxTimespan", this.getExecuteMillisMax());
        map.put("LastError", JMXUtils.getErrorCompositeData(this.getExecuteErrorLast()));
        map.put("EffectedRowCount", this.getUpdateCount());
        map.put("FetchRowCount", this.getFetchRowCount());
        map.put("MaxTimespanOccurTime", this.getExecuteNanoSpanMaxOccurTime());
        map.put("BatchSizeMax", (long)this.getExecuteBatchSizeMax());
        map.put("BatchSizeTotal", this.getExecuteBatchSizeTotal());
        map.put("ConcurrentMax", (long)this.getConcurrentMax());
        map.put("RunningCount", (long)this.getRunningCount());
        map.put("Name", this.getName());
        map.put("File", this.getFile());
        if (this.executeErrorLastMessage != null) {
            map.put("LastErrorMessage", this.executeErrorLastMessage);
            map.put("LastErrorClass", this.executeErrorLastClass);
            map.put("LastErrorStackTrace", this.executeErrorLastStackTrace);
            map.put("LastErrorTime", new Date(this.executeErrorLastTime));
        }
        else {
            map.put("LastErrorMessage", null);
            map.put("LastErrorClass", null);
            map.put("LastErrorStackTrace", null);
            map.put("LastErrorTime", null);
        }
        map.put("DbType", this.dbType);
        map.put("URL", null);
        map.put("InTransactionCount", this.getInTransactionCount());
        map.put("Histogram", this.getHistogramValues());
        map.put("LastSlowParameters", this.lastSlowParameters);
        map.put("ResultSetHoldTime", this.getResultSetHoldTimeMilis());
        map.put("ExecuteAndResultSetHoldTime", this.getExecuteAndResultSetHoldTimeMilis());
        map.put("FetchRowCountHistogram", this.getFetchRowCountHistogramValues());
        map.put("EffectedRowCountHistogram", this.getUpdateCountHistogramValues());
        map.put("ExecuteAndResultHoldTimeHistogram", this.getExecuteAndResultHoldTimeHistogramValues());
        map.put("EffectedRowCountMax", this.getUpdateCountMax());
        map.put("FetchRowCountMax", this.getFetchRowCountMax());
        map.put("ClobOpenCount", this.getClobOpenCount());
        map.put("BlobOpenCount", this.getBlobOpenCount());
        map.put("ReadStringLength", this.getReadStringLength());
        map.put("ReadBytesLength", this.getReadBytesLength());
        map.put("InputStreamOpenCount", this.getInputStreamOpenCount());
        map.put("ReaderOpenCount", this.getReaderOpenCount());
        map.put("HASH", this.getSqlHash());
        return map;
    }
    
    public long getHistogram_0_1() {
        return this.histogram_0_1;
    }
    
    public void setHistogram_0_1(final long histogram_0_1) {
        this.histogram_0_1 = histogram_0_1;
    }
    
    public long getHistogram_1_10() {
        return this.histogram_1_10;
    }
    
    public void setHistogram_1_10(final long histogram_1_10) {
        this.histogram_1_10 = histogram_1_10;
    }
    
    public int getHistogram_10_100() {
        return this.histogram_10_100;
    }
    
    public void setHistogram_10_100(final int histogram_10_100) {
        this.histogram_10_100 = histogram_10_100;
    }
    
    public int getHistogram_100_1000() {
        return this.histogram_100_1000;
    }
    
    public void setHistogram_100_1000(final int histogram_100_1000) {
        this.histogram_100_1000 = histogram_100_1000;
    }
    
    public int getHistogram_1000_10000() {
        return this.histogram_1000_10000;
    }
    
    public void setHistogram_1000_10000(final int histogram_1000_10000) {
        this.histogram_1000_10000 = histogram_1000_10000;
    }
    
    public int getHistogram_10000_100000() {
        return this.histogram_10000_100000;
    }
    
    public void setHistogram_10000_100000(final int histogram_10000_100000) {
        this.histogram_10000_100000 = histogram_10000_100000;
    }
    
    public int getHistogram_100000_1000000() {
        return this.histogram_100000_1000000;
    }
    
    public void setHistogram_100000_1000000(final int histogram_100000_1000000) {
        this.histogram_100000_1000000 = histogram_100000_1000000;
    }
    
    public int getHistogram_1000000_more() {
        return this.histogram_1000000_more;
    }
    
    public void setHistogram_1000000_more(final int histogram_1000000_more) {
        this.histogram_1000000_more = histogram_1000000_more;
    }
}
