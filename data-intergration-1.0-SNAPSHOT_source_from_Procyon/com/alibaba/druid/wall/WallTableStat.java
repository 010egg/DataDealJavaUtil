// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import com.alibaba.druid.support.json.JSONUtils;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class WallTableStat
{
    private volatile long selectCount;
    private volatile long selectIntoCount;
    private volatile long insertCount;
    private volatile long updateCount;
    private volatile long deleteCount;
    private volatile long truncateCount;
    private volatile long createCount;
    private volatile long alterCount;
    private volatile long dropCount;
    private volatile long replaceCount;
    private volatile long deleteDataCount;
    private volatile long updateDataCount;
    private volatile long insertDataCount;
    private volatile long fetchRowCount;
    static final AtomicLongFieldUpdater<WallTableStat> selectCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> selectIntoCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> insertCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> updateCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> deleteCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> truncateCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> createCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> alterCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> dropCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> replaceCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> deleteDataCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> insertDataCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> updateDataCountUpdater;
    static final AtomicLongFieldUpdater<WallTableStat> fetchRowCountUpdater;
    private volatile long fetchRowCount_0_1;
    private volatile long fetchRowCount_1_10;
    private volatile long fetchRowCount_10_100;
    private volatile int fetchRowCount_100_1000;
    private volatile int fetchRowCount_1000_10000;
    private volatile int fetchRowCount_10000_more;
    static final AtomicLongFieldUpdater<WallTableStat> fetchRowCount_0_1_Updater;
    static final AtomicLongFieldUpdater<WallTableStat> fetchRowCount_1_10_Updater;
    static final AtomicLongFieldUpdater<WallTableStat> fetchRowCount_10_100_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> fetchRowCount_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> fetchRowCount_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> fetchRowCount_10000_more_Updater;
    private volatile long updateDataCount_0_1;
    private volatile long updateDataCount_1_10;
    private volatile long updateDataCount_10_100;
    private volatile int updateDataCount_100_1000;
    private volatile int updateDataCount_1000_10000;
    private volatile int updateDataCount_10000_more;
    static final AtomicLongFieldUpdater<WallTableStat> updateDataCount_0_1_Updater;
    static final AtomicLongFieldUpdater<WallTableStat> updateDataCount_1_10_Updater;
    static final AtomicLongFieldUpdater<WallTableStat> updateDataCount_10_100_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> updateDataCount_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> updateDataCount_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> updateDataCount_10000_more_Updater;
    private volatile long deleteDataCount_0_1;
    private volatile long deleteDataCount_1_10;
    private volatile long deleteDataCount_10_100;
    private volatile int deleteDataCount_100_1000;
    private volatile int deleteDataCount_1000_10000;
    private volatile int deleteDataCount_10000_more;
    static final AtomicLongFieldUpdater<WallTableStat> deleteDataCount_0_1_Updater;
    static final AtomicLongFieldUpdater<WallTableStat> deleteDataCount_1_10_Updater;
    static final AtomicLongFieldUpdater<WallTableStat> deleteDataCount_10_100_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> deleteDataCount_100_1000_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> deleteDataCount_1000_10000_Updater;
    static final AtomicIntegerFieldUpdater<WallTableStat> deleteDataCount_10000_more_Updater;
    
    public long getSelectCount() {
        return this.selectCount;
    }
    
    public long getSelectIntoCount() {
        return this.selectIntoCount;
    }
    
    public long getInsertCount() {
        return this.insertCount;
    }
    
    public long getUpdateCount() {
        return this.updateCount;
    }
    
    public long getDeleteCount() {
        return this.deleteCount;
    }
    
    public long getTruncateCount() {
        return this.truncateCount;
    }
    
    public long getCreateCount() {
        return this.createCount;
    }
    
    public long getAlterCount() {
        return this.alterCount;
    }
    
    public long getDropCount() {
        return this.dropCount;
    }
    
    public long getReplaceCount() {
        return this.replaceCount;
    }
    
    public long getDeleteDataCount() {
        return this.deleteDataCount;
    }
    
    public long[] getDeleteDataCountHistogramValues() {
        return new long[] { this.deleteDataCount_0_1, this.deleteDataCount_1_10, this.deleteDataCount_10_100, this.deleteDataCount_100_1000, this.deleteDataCount_1000_10000, this.deleteDataCount_10000_more };
    }
    
    public void addDeleteDataCount(final long delta) {
        WallTableStat.deleteDataCountUpdater.addAndGet(this, delta);
        if (delta < 1L) {
            WallTableStat.deleteDataCount_0_1_Updater.incrementAndGet(this);
        }
        else if (delta < 10L) {
            WallTableStat.deleteDataCount_1_10_Updater.incrementAndGet(this);
        }
        else if (delta < 100L) {
            WallTableStat.deleteDataCount_10_100_Updater.incrementAndGet(this);
        }
        else if (delta < 1000L) {
            WallTableStat.deleteDataCount_100_1000_Updater.incrementAndGet(this);
        }
        else if (delta < 10000L) {
            WallTableStat.deleteDataCount_1000_10000_Updater.incrementAndGet(this);
        }
        else {
            WallTableStat.deleteDataCount_10000_more_Updater.incrementAndGet(this);
        }
    }
    
    public long getUpdateDataCount() {
        return this.updateDataCount;
    }
    
    public long[] getUpdateDataCountHistogramValues() {
        return new long[] { this.updateDataCount_0_1, this.updateDataCount_1_10, this.updateDataCount_10_100, this.updateDataCount_100_1000, this.updateDataCount_1000_10000, this.updateDataCount_10000_more };
    }
    
    public long getInsertDataCount() {
        return this.insertDataCount;
    }
    
    public void addInsertDataCount(final long delta) {
        WallTableStat.insertDataCountUpdater.addAndGet(this, delta);
    }
    
    public void addUpdateDataCount(final long delta) {
        WallTableStat.updateDataCountUpdater.addAndGet(this, delta);
        if (delta < 1L) {
            WallTableStat.updateDataCount_0_1_Updater.incrementAndGet(this);
        }
        else if (delta < 10L) {
            WallTableStat.updateDataCount_1_10_Updater.incrementAndGet(this);
        }
        else if (delta < 100L) {
            WallTableStat.updateDataCount_10_100_Updater.incrementAndGet(this);
        }
        else if (delta < 1000L) {
            WallTableStat.updateDataCount_100_1000_Updater.incrementAndGet(this);
        }
        else if (delta < 10000L) {
            WallTableStat.updateDataCount_1000_10000_Updater.incrementAndGet(this);
        }
        else {
            WallTableStat.updateDataCount_10000_more_Updater.incrementAndGet(this);
        }
    }
    
    public long getFetchRowCount() {
        return this.fetchRowCount;
    }
    
    public long[] getFetchRowCountHistogramValues() {
        return new long[] { this.fetchRowCount_0_1, this.fetchRowCount_1_10, this.fetchRowCount_10_100, this.fetchRowCount_100_1000, this.fetchRowCount_1000_10000, this.fetchRowCount_10000_more };
    }
    
    public void addFetchRowCount(final long delta) {
        WallTableStat.fetchRowCountUpdater.addAndGet(this, delta);
        if (delta < 1L) {
            WallTableStat.fetchRowCount_0_1_Updater.incrementAndGet(this);
        }
        else if (delta < 10L) {
            WallTableStat.fetchRowCount_1_10_Updater.incrementAndGet(this);
        }
        else if (delta < 100L) {
            WallTableStat.fetchRowCount_10_100_Updater.incrementAndGet(this);
        }
        else if (delta < 1000L) {
            WallTableStat.fetchRowCount_100_1000_Updater.incrementAndGet(this);
        }
        else if (delta < 10000L) {
            WallTableStat.fetchRowCount_1000_10000_Updater.incrementAndGet(this);
        }
        else {
            WallTableStat.fetchRowCount_10000_more_Updater.incrementAndGet(this);
        }
    }
    
    public void addSqlTableStat(final WallSqlTableStat stat) {
        long val = stat.getSelectCount();
        if (val > 0L) {
            WallTableStat.selectCountUpdater.addAndGet(this, val);
        }
        val = stat.getSelectIntoCount();
        if (val > 0L) {
            WallTableStat.selectIntoCountUpdater.addAndGet(this, val);
        }
        val = stat.getInsertCount();
        if (val > 0L) {
            WallTableStat.insertCountUpdater.addAndGet(this, val);
        }
        val = stat.getUpdateCount();
        if (val > 0L) {
            WallTableStat.updateCountUpdater.addAndGet(this, val);
        }
        val = stat.getDeleteCount();
        if (val > 0L) {
            WallTableStat.deleteCountUpdater.addAndGet(this, val);
        }
        val = stat.getAlterCount();
        if (val > 0L) {
            WallTableStat.alterCountUpdater.addAndGet(this, val);
        }
        val = stat.getTruncateCount();
        if (val > 0L) {
            WallTableStat.truncateCountUpdater.addAndGet(this, val);
        }
        val = stat.getCreateCount();
        if (val > 0L) {
            WallTableStat.createCountUpdater.addAndGet(this, val);
        }
        val = stat.getDropCount();
        if (val > 0L) {
            WallTableStat.dropCountUpdater.addAndGet(this, val);
        }
        val = stat.getReplaceCount();
        if (val > 0L) {
            WallTableStat.replaceCountUpdater.addAndGet(this, val);
        }
    }
    
    @Override
    public String toString() {
        final Map<String, Object> map = this.toMap();
        return JSONUtils.toJSONString(map);
    }
    
    public Map<String, Object> toMap() {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        return this.toMap(map);
    }
    
    public WallTableStatValue getStatValue(final boolean reset) {
        final WallTableStatValue statValue = new WallTableStatValue();
        statValue.setSelectCount(JdbcSqlStatUtils.get(this, WallTableStat.selectCountUpdater, reset));
        statValue.setDeleteCount(JdbcSqlStatUtils.get(this, WallTableStat.deleteCountUpdater, reset));
        statValue.setInsertCount(JdbcSqlStatUtils.get(this, WallTableStat.insertCountUpdater, reset));
        statValue.setUpdateCount(JdbcSqlStatUtils.get(this, WallTableStat.updateCountUpdater, reset));
        statValue.setAlterCount(JdbcSqlStatUtils.get(this, WallTableStat.alterCountUpdater, reset));
        statValue.setDropCount(JdbcSqlStatUtils.get(this, WallTableStat.dropCountUpdater, reset));
        statValue.setCreateCount(JdbcSqlStatUtils.get(this, WallTableStat.createCountUpdater, reset));
        statValue.setTruncateCount(JdbcSqlStatUtils.get(this, WallTableStat.truncateCountUpdater, reset));
        statValue.setReplaceCount(JdbcSqlStatUtils.get(this, WallTableStat.replaceCountUpdater, reset));
        statValue.setDeleteDataCount(JdbcSqlStatUtils.get(this, WallTableStat.deleteDataCountUpdater, reset));
        statValue.setFetchRowCount(JdbcSqlStatUtils.get(this, WallTableStat.fetchRowCountUpdater, reset));
        statValue.setUpdateDataCount(JdbcSqlStatUtils.get(this, WallTableStat.updateDataCountUpdater, reset));
        statValue.fetchRowCount_0_1 = JdbcSqlStatUtils.get(this, WallTableStat.fetchRowCount_0_1_Updater, reset);
        statValue.fetchRowCount_1_10 = JdbcSqlStatUtils.get(this, WallTableStat.fetchRowCount_1_10_Updater, reset);
        statValue.fetchRowCount_10_100 = JdbcSqlStatUtils.get(this, WallTableStat.fetchRowCount_10_100_Updater, reset);
        statValue.fetchRowCount_100_1000 = JdbcSqlStatUtils.get(this, WallTableStat.fetchRowCount_100_1000_Updater, reset);
        statValue.fetchRowCount_1000_10000 = JdbcSqlStatUtils.get(this, WallTableStat.fetchRowCount_1000_10000_Updater, reset);
        statValue.fetchRowCount_10000_more = JdbcSqlStatUtils.get(this, WallTableStat.fetchRowCount_10000_more_Updater, reset);
        statValue.updateDataCount_0_1 = JdbcSqlStatUtils.get(this, WallTableStat.updateDataCount_0_1_Updater, reset);
        statValue.updateDataCount_1_10 = JdbcSqlStatUtils.get(this, WallTableStat.updateDataCount_1_10_Updater, reset);
        statValue.updateDataCount_10_100 = JdbcSqlStatUtils.get(this, WallTableStat.updateDataCount_10_100_Updater, reset);
        statValue.updateDataCount_100_1000 = JdbcSqlStatUtils.get(this, WallTableStat.updateDataCount_100_1000_Updater, reset);
        statValue.updateDataCount_1000_10000 = JdbcSqlStatUtils.get(this, WallTableStat.updateDataCount_1000_10000_Updater, reset);
        statValue.updateDataCount_10000_more = JdbcSqlStatUtils.get(this, WallTableStat.updateDataCount_10000_more_Updater, reset);
        statValue.deleteDataCount_0_1 = JdbcSqlStatUtils.get(this, WallTableStat.deleteDataCount_0_1_Updater, reset);
        statValue.deleteDataCount_1_10 = JdbcSqlStatUtils.get(this, WallTableStat.deleteDataCount_1_10_Updater, reset);
        statValue.deleteDataCount_10_100 = JdbcSqlStatUtils.get(this, WallTableStat.deleteDataCount_10_100_Updater, reset);
        statValue.deleteDataCount_100_1000 = JdbcSqlStatUtils.get(this, WallTableStat.deleteDataCount_100_1000_Updater, reset);
        statValue.deleteDataCount_1000_10000 = JdbcSqlStatUtils.get(this, WallTableStat.deleteDataCount_1000_10000_Updater, reset);
        statValue.deleteDataCount_10000_more = JdbcSqlStatUtils.get(this, WallTableStat.deleteDataCount_10000_more_Updater, reset);
        return statValue;
    }
    
    public Map<String, Object> toMap(final Map<String, Object> map) {
        return this.getStatValue(false).toMap(map);
    }
    
    static {
        selectCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "selectCount");
        selectIntoCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "selectIntoCount");
        insertCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "insertCount");
        updateCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateCount");
        deleteCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteCount");
        truncateCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "truncateCount");
        createCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "createCount");
        alterCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "alterCount");
        dropCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "dropCount");
        replaceCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "replaceCount");
        deleteDataCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount");
        insertDataCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "insertDataCount");
        updateDataCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount");
        fetchRowCountUpdater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount");
        fetchRowCount_0_1_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_0_1");
        fetchRowCount_1_10_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_1_10");
        fetchRowCount_10_100_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_10_100");
        fetchRowCount_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_100_1000");
        fetchRowCount_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_1000_10000");
        fetchRowCount_10000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "fetchRowCount_10000_more");
        updateDataCount_0_1_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_0_1");
        updateDataCount_1_10_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_1_10");
        updateDataCount_10_100_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_10_100");
        updateDataCount_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_100_1000");
        updateDataCount_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_1000_10000");
        updateDataCount_10000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "updateDataCount_10000_more");
        deleteDataCount_0_1_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_0_1");
        deleteDataCount_1_10_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_1_10");
        deleteDataCount_10_100_Updater = AtomicLongFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_10_100");
        deleteDataCount_100_1000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_100_1000");
        deleteDataCount_1000_10000_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_1000_10000");
        deleteDataCount_10000_more_Updater = AtomicIntegerFieldUpdater.newUpdater(WallTableStat.class, "deleteDataCount_10000_more");
    }
}
