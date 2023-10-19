// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import javax.management.JMException;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import javax.management.openmbean.TabularData;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;
import com.alibaba.druid.util.Histogram;
import java.util.concurrent.atomic.AtomicLong;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.alibaba.druid.support.logging.Log;

public class JdbcDataSourceStat implements JdbcDataSourceStatMBean
{
    private static final Log LOG;
    private final String name;
    private final String url;
    private String dbType;
    private final JdbcConnectionStat connectionStat;
    private final JdbcResultSetStat resultSetStat;
    private final JdbcStatementStat statementStat;
    private int maxSqlSize;
    private ReentrantReadWriteLock lock;
    private final LinkedHashMap<String, JdbcSqlStat> sqlStatMap;
    private final AtomicLong skipSqlCount;
    private final Histogram connectionHoldHistogram;
    private final ConcurrentMap<Long, JdbcConnectionStat.Entry> connections;
    private final AtomicLong clobOpenCount;
    private final AtomicLong blobOpenCount;
    private final AtomicLong keepAliveCheckCount;
    private boolean resetStatEnable;
    private static JdbcDataSourceStat global;
    
    public static JdbcDataSourceStat getGlobal() {
        return JdbcDataSourceStat.global;
    }
    
    public static void setGlobal(final JdbcDataSourceStat value) {
        JdbcDataSourceStat.global = value;
    }
    
    public void configFromProperties(final Properties properties) {
    }
    
    public boolean isResetStatEnable() {
        return this.resetStatEnable;
    }
    
    public void setResetStatEnable(final boolean resetStatEnable) {
        this.resetStatEnable = resetStatEnable;
    }
    
    public JdbcDataSourceStat(final String name, final String url) {
        this(name, url, null);
    }
    
    public JdbcDataSourceStat(final String name, final String url, final String dbType) {
        this(name, url, dbType, null);
    }
    
    public JdbcDataSourceStat(final String name, final String url, final String dbType, final Properties connectProperties) {
        this.connectionStat = new JdbcConnectionStat();
        this.resultSetStat = new JdbcResultSetStat();
        this.statementStat = new JdbcStatementStat();
        this.maxSqlSize = 1000;
        this.lock = new ReentrantReadWriteLock();
        this.skipSqlCount = new AtomicLong();
        this.connectionHoldHistogram = new Histogram(new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L });
        this.connections = new ConcurrentHashMap<Long, JdbcConnectionStat.Entry>(16, 0.75f, 1);
        this.clobOpenCount = new AtomicLong();
        this.blobOpenCount = new AtomicLong();
        this.keepAliveCheckCount = new AtomicLong();
        this.resetStatEnable = true;
        this.name = name;
        this.url = url;
        this.dbType = dbType;
        if (connectProperties != null) {
            Object arg = connectProperties.get("druid.stat.sql.MaxSize");
            if (arg == null) {
                arg = System.getProperty("druid.stat.sql.MaxSize");
            }
            if (arg != null) {
                try {
                    this.maxSqlSize = Integer.parseInt(arg.toString());
                }
                catch (NumberFormatException ex) {
                    JdbcDataSourceStat.LOG.error("maxSize parse error", ex);
                }
            }
        }
        this.sqlStatMap = new LinkedHashMap<String, JdbcSqlStat>(16, 0.75f, false) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<String, JdbcSqlStat> eldest) {
                final boolean remove = this.size() > JdbcDataSourceStat.this.maxSqlSize;
                if (remove) {
                    final JdbcSqlStat sqlStat = eldest.getValue();
                    if (sqlStat.getRunningCount() > 0L || sqlStat.getExecuteCount() > 0L) {
                        JdbcDataSourceStat.this.skipSqlCount.incrementAndGet();
                    }
                }
                return remove;
            }
        };
    }
    
    public int getMaxSqlSize() {
        return this.maxSqlSize;
    }
    
    public void setMaxSqlSize(final int value) {
        if (value == this.maxSqlSize) {
            return;
        }
        this.lock.writeLock().lock();
        try {
            if (value < this.maxSqlSize) {
                int removeCount = this.maxSqlSize - value;
                final Iterator<Map.Entry<String, JdbcSqlStat>> iter = this.sqlStatMap.entrySet().iterator();
                while (iter.hasNext()) {
                    iter.next();
                    if (removeCount <= 0) {
                        break;
                    }
                    iter.remove();
                    --removeCount;
                }
            }
            this.maxSqlSize = value;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public String getDbType() {
        return this.dbType;
    }
    
    public void setDbType(final String dbType) {
        this.dbType = dbType;
    }
    
    public long getSkipSqlCount() {
        return this.skipSqlCount.get();
    }
    
    public long getSkipSqlCountAndReset() {
        return this.skipSqlCount.getAndSet(0L);
    }
    
    @Override
    public void reset() {
        if (!this.isResetStatEnable()) {
            return;
        }
        this.blobOpenCount.set(0L);
        this.clobOpenCount.set(0L);
        this.connectionStat.reset();
        this.statementStat.reset();
        this.resultSetStat.reset();
        this.connectionHoldHistogram.reset();
        this.skipSqlCount.set(0L);
        this.lock.writeLock().lock();
        try {
            final Iterator<Map.Entry<String, JdbcSqlStat>> iter = this.sqlStatMap.entrySet().iterator();
            while (iter.hasNext()) {
                final Map.Entry<String, JdbcSqlStat> entry = iter.next();
                final JdbcSqlStat stat = entry.getValue();
                if (stat.getExecuteCount() == 0L && stat.getRunningCount() == 0L) {
                    stat.setRemoved(true);
                    iter.remove();
                }
                else {
                    stat.reset();
                }
            }
        }
        finally {
            this.lock.writeLock().unlock();
        }
        for (final JdbcConnectionStat.Entry connectionStat : this.connections.values()) {
            connectionStat.reset();
        }
    }
    
    public Histogram getConnectionHoldHistogram() {
        return this.connectionHoldHistogram;
    }
    
    public JdbcConnectionStat getConnectionStat() {
        return this.connectionStat;
    }
    
    public JdbcResultSetStat getResultSetStat() {
        return this.resultSetStat;
    }
    
    public JdbcStatementStat getStatementStat() {
        return this.statementStat;
    }
    
    @Override
    public String getConnectionUrl() {
        return this.url;
    }
    
    @Override
    public TabularData getSqlList() throws JMException {
        final Map<String, JdbcSqlStat> sqlStatMap = this.getSqlStatMap();
        final CompositeType rowType = JdbcSqlStat.getCompositeType();
        final String[] indexNames = rowType.keySet().toArray(new String[rowType.keySet().size()]);
        final TabularType tabularType = new TabularType("SqlListStatistic", "SqlListStatistic", rowType, indexNames);
        final TabularData data = new TabularDataSupport(tabularType);
        for (final Map.Entry<String, JdbcSqlStat> entry : sqlStatMap.entrySet()) {
            data.put(entry.getValue().getCompositeData());
        }
        return data;
    }
    
    public static StatFilter getStatFilter(final DataSourceProxy dataSource) {
        for (final Filter filter : dataSource.getProxyFilters()) {
            if (filter instanceof StatFilter) {
                return (StatFilter)filter;
            }
        }
        return null;
    }
    
    public JdbcSqlStat getSqlStat(final int id) {
        return this.getSqlStat((long)id);
    }
    
    public JdbcSqlStat getSqlStat(final long id) {
        this.lock.readLock().lock();
        try {
            for (final Map.Entry<String, JdbcSqlStat> entry : this.sqlStatMap.entrySet()) {
                if (entry.getValue().getId() == id) {
                    return entry.getValue();
                }
            }
            return null;
        }
        finally {
            this.lock.readLock().unlock();
        }
    }
    
    public final ConcurrentMap<Long, JdbcConnectionStat.Entry> getConnections() {
        return this.connections;
    }
    
    @Override
    public TabularData getConnectionList() throws JMException {
        final CompositeType rowType = JdbcConnectionStat.Entry.getCompositeType();
        final String[] indexNames = rowType.keySet().toArray(new String[rowType.keySet().size()]);
        final TabularType tabularType = new TabularType("ConnectionListStatistic", "ConnectionListStatistic", rowType, indexNames);
        final TabularData data = new TabularDataSupport(tabularType);
        for (final Map.Entry<Long, JdbcConnectionStat.Entry> entry : this.getConnections().entrySet()) {
            data.put(entry.getValue().getCompositeData());
        }
        return data;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public Map<String, JdbcSqlStat> getSqlStatMap() {
        final Map<String, JdbcSqlStat> map = new LinkedHashMap<String, JdbcSqlStat>(this.sqlStatMap.size());
        this.lock.readLock().lock();
        try {
            map.putAll(this.sqlStatMap);
        }
        finally {
            this.lock.readLock().unlock();
        }
        return map;
    }
    
    public List<JdbcSqlStatValue> getSqlStatMapAndReset() {
        final List<JdbcSqlStat> stats = new ArrayList<JdbcSqlStat>(this.sqlStatMap.size());
        this.lock.writeLock().lock();
        try {
            final Iterator<Map.Entry<String, JdbcSqlStat>> iter = this.sqlStatMap.entrySet().iterator();
            while (iter.hasNext()) {
                final Map.Entry<String, JdbcSqlStat> entry = iter.next();
                final JdbcSqlStat stat = entry.getValue();
                if (stat.getExecuteCount() == 0L && stat.getRunningCount() == 0L) {
                    stat.setRemoved(true);
                    iter.remove();
                }
                else {
                    stats.add(entry.getValue());
                }
            }
        }
        finally {
            this.lock.writeLock().unlock();
        }
        final List<JdbcSqlStatValue> values = new ArrayList<JdbcSqlStatValue>(stats.size());
        final Iterator<JdbcSqlStat> iterator = stats.iterator();
        while (iterator.hasNext()) {
            final JdbcSqlStat stat = iterator.next();
            final JdbcSqlStatValue value = stat.getValueAndReset();
            if (value.getExecuteCount() == 0L && value.getRunningCount() == 0) {
                continue;
            }
            values.add(value);
        }
        return values;
    }
    
    public List<JdbcSqlStatValue> getRuningSqlList() {
        final List<JdbcSqlStat> stats = new ArrayList<JdbcSqlStat>(this.sqlStatMap.size());
        this.lock.readLock().lock();
        try {
            for (final Map.Entry<String, JdbcSqlStat> entry : this.sqlStatMap.entrySet()) {
                final JdbcSqlStat stat = entry.getValue();
                if (stat.getRunningCount() >= 0L) {
                    stats.add(entry.getValue());
                }
            }
        }
        finally {
            this.lock.readLock().unlock();
        }
        final List<JdbcSqlStatValue> values = new ArrayList<JdbcSqlStatValue>(stats.size());
        final Iterator<JdbcSqlStat> iterator2 = stats.iterator();
        while (iterator2.hasNext()) {
            final JdbcSqlStat stat = iterator2.next();
            final JdbcSqlStatValue value = stat.getValue(false);
            if (value.getRunningCount() > 0) {
                values.add(value);
            }
        }
        return values;
    }
    
    public JdbcSqlStat getSqlStat(final String sql) {
        this.lock.readLock().lock();
        try {
            return this.sqlStatMap.get(sql);
        }
        finally {
            this.lock.readLock().unlock();
        }
    }
    
    public JdbcSqlStat createSqlStat(final String sql) {
        this.lock.writeLock().lock();
        try {
            JdbcSqlStat sqlStat = this.sqlStatMap.get(sql);
            if (sqlStat == null) {
                sqlStat = new JdbcSqlStat(sql);
                sqlStat.setDbType(this.dbType);
                sqlStat.setName(this.name);
                this.sqlStatMap.put(sql, sqlStat);
            }
            return sqlStat;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    @Override
    public long getConnectionActiveCount() {
        return this.connections.size();
    }
    
    @Override
    public long getConnectionConnectAliveMillis() {
        final long nowNano = System.nanoTime();
        long aliveNanoSpan = this.getConnectionStat().getAliveTotal();
        for (final JdbcConnectionStat.Entry connection : this.connections.values()) {
            aliveNanoSpan += nowNano - connection.getEstablishNano();
        }
        return aliveNanoSpan / 1000000L;
    }
    
    public long getConnectionConnectAliveMillisMax() {
        long max = this.getConnectionStat().getAliveNanoMax();
        final long nowNano = System.nanoTime();
        for (final JdbcConnectionStat.Entry connection : this.connections.values()) {
            final long connectionAliveNano = nowNano - connection.getEstablishNano();
            if (connectionAliveNano > max) {
                max = connectionAliveNano;
            }
        }
        return max / 1000000L;
    }
    
    public long getConnectionConnectAliveMillisMin() {
        long min = this.getConnectionStat().getAliveNanoMin();
        final long nowNano = System.nanoTime();
        for (final JdbcConnectionStat.Entry connection : this.connections.values()) {
            final long connectionAliveNano = nowNano - connection.getEstablishNano();
            if (connectionAliveNano < min || min == 0L) {
                min = connectionAliveNano;
            }
        }
        return min / 1000000L;
    }
    
    @Override
    public long[] getConnectionHistogramRanges() {
        return this.connectionStat.getHistogramRanges();
    }
    
    @Override
    public long[] getConnectionHistogramValues() {
        return this.connectionStat.getHistorgramValues();
    }
    
    public long getClobOpenCount() {
        return this.clobOpenCount.get();
    }
    
    public long getClobOpenCountAndReset() {
        return this.clobOpenCount.getAndSet(0L);
    }
    
    public void incrementClobOpenCount() {
        this.clobOpenCount.incrementAndGet();
    }
    
    public long getBlobOpenCount() {
        return this.blobOpenCount.get();
    }
    
    public long getBlobOpenCountAndReset() {
        return this.blobOpenCount.getAndSet(0L);
    }
    
    public void incrementBlobOpenCount() {
        this.blobOpenCount.incrementAndGet();
    }
    
    public long getKeepAliveCheckCount() {
        return this.keepAliveCheckCount.get();
    }
    
    public long getKeepAliveCheckCountAndReset() {
        return this.keepAliveCheckCount.getAndSet(0L);
    }
    
    public void addKeepAliveCheckCount(final long delta) {
        this.keepAliveCheckCount.addAndGet(delta);
    }
    
    static {
        LOG = LogFactory.getLog(JdbcDataSourceStat.class);
        String dbType = null;
        final String property = System.getProperty("druid.globalDbType");
        if (property != null && property.length() > 0) {
            dbType = property;
        }
        JdbcDataSourceStat.global = new JdbcDataSourceStat("Global", "Global", dbType);
    }
}
