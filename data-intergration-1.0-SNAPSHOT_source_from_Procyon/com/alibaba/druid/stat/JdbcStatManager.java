// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import javax.management.openmbean.CompositeDataSupport;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import com.alibaba.druid.pool.DruidDataSource;
import javax.management.openmbean.CompositeData;
import com.alibaba.druid.proxy.jdbc.DataSourceProxyImpl;
import com.alibaba.druid.proxy.DruidDriver;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import javax.management.openmbean.TabularData;
import javax.management.JMException;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.CompositeType;
import java.util.concurrent.atomic.AtomicLong;

public final class JdbcStatManager implements JdbcStatManagerMBean
{
    private final AtomicLong sqlIdSeed;
    private static final JdbcStatManager instance;
    private final JdbcConnectionStat connectionStat;
    private final JdbcResultSetStat resultSetStat;
    private final JdbcStatementStat statementStat;
    private final AtomicLong resetCount;
    public final ThreadLocal<JdbcStatContext> contextLocal;
    private static CompositeType COMPOSITE_TYPE;
    
    private JdbcStatManager() {
        this.sqlIdSeed = new AtomicLong(1000L);
        this.connectionStat = new JdbcConnectionStat();
        this.resultSetStat = new JdbcResultSetStat();
        this.statementStat = new JdbcStatementStat();
        this.resetCount = new AtomicLong();
        this.contextLocal = new ThreadLocal<JdbcStatContext>();
    }
    
    public JdbcStatContext getStatContext() {
        return this.contextLocal.get();
    }
    
    public void setStatContext(final JdbcStatContext context) {
        this.contextLocal.set(context);
    }
    
    public JdbcStatContext createStatContext() {
        return new JdbcStatContext();
    }
    
    public long generateSqlId() {
        return this.sqlIdSeed.incrementAndGet();
    }
    
    public static JdbcStatManager getInstance() {
        return JdbcStatManager.instance;
    }
    
    public JdbcStatementStat getStatementStat() {
        return this.statementStat;
    }
    
    public JdbcResultSetStat getResultSetStat() {
        return this.resultSetStat;
    }
    
    public JdbcConnectionStat getConnectionStat() {
        return this.connectionStat;
    }
    
    public static CompositeType getDataSourceCompositeType() throws JMException {
        if (JdbcStatManager.COMPOSITE_TYPE != null) {
            return JdbcStatManager.COMPOSITE_TYPE;
        }
        final OpenType<?>[] indexTypes = (OpenType<?>[])new OpenType[] { SimpleType.LONG, SimpleType.STRING, SimpleType.STRING, new ArrayType(SimpleType.STRING, false), SimpleType.DATE, SimpleType.STRING, SimpleType.STRING, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.STRING, SimpleType.LONG, SimpleType.INTEGER, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.DATE, SimpleType.LONG, SimpleType.DATE, SimpleType.STRING, SimpleType.STRING, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.LONG, SimpleType.LONG, SimpleType.DATE, SimpleType.STRING, SimpleType.STRING, SimpleType.LONG, SimpleType.INTEGER, SimpleType.DATE, SimpleType.LONG, SimpleType.LONG, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.LONG, SimpleType.DATE, SimpleType.LONG, SimpleType.LONG, SimpleType.DATE, SimpleType.STRING, SimpleType.STRING, SimpleType.LONG, SimpleType.STRING, SimpleType.STRING, SimpleType.LONG, SimpleType.INTEGER, SimpleType.LONG, SimpleType.DATE, SimpleType.LONG, SimpleType.LONG, new ArrayType(SimpleType.LONG, true), new ArrayType(SimpleType.LONG, true) };
        final String[] indexNames = { "ID", "URL", "Name", "FilterClasses", "CreatedTime", "RawUrl", "RawDriverClassName", "RawDriverMajorVersion", "RawDriverMinorVersion", "Properties", "ConnectionActiveCount", "ConnectionActiveCountMax", "ConnectionCloseCount", "ConnectionCommitCount", "ConnectionRollbackCount", "ConnectionConnectLastTime", "ConnectionConnectErrorCount", "ConnectionConnectErrorLastTime", "ConnectionConnectErrorLastMessage", "ConnectionConnectErrorLastStackTrace", "StatementCreateCount", "StatementPrepareCount", "StatementPreCallCount", "StatementExecuteCount", "StatementRunningCount", "StatementConcurrentMax", "StatementCloseCount", "StatementErrorCount", "StatementLastErrorTime", "StatementLastErrorMessage", "StatementLastErrorStackTrace", "StatementExecuteMillisTotal", "ConnectionConnectingCount", "StatementExecuteLastTime", "ResultSetCloseCount", "ResultSetOpenCount", "ResultSetOpenningCount", "ResultSetOpenningMax", "ResultSetFetchRowCount", "ResultSetLastOpenTime", "ResultSetErrorCount", "ResultSetOpenningMillisTotal", "ResultSetLastErrorTime", "ResultSetLastErrorMessage", "ResultSetLastErrorStackTrace", "ConnectionConnectCount", "ConnectionErrorLastMessage", "ConnectionErrorLastStackTrace", "ConnectionConnectMillisTotal", "ConnectionConnectingCountMax", "ConnectionConnectMillisMax", "ConnectionErrorLastTime", "ConnectionAliveMillisMax", "ConnectionAliveMillisMin", "ConnectionHistogram", "StatementHistogram" };
        return JdbcStatManager.COMPOSITE_TYPE = new CompositeType("DataSourceStatistic", "DataSource Statistic", indexNames, indexNames, indexTypes);
    }
    
    @Override
    public TabularData getDataSourceList() throws JMException {
        final CompositeType rowType = getDataSourceCompositeType();
        final String[] indexNames = rowType.keySet().toArray(new String[rowType.keySet().size()]);
        final TabularType tabularType = new TabularType("DataSourceStat", "DataSourceStat", rowType, indexNames);
        final TabularData data = new TabularDataSupport(tabularType);
        final ConcurrentMap<String, DataSourceProxyImpl> dataSources = DruidDriver.getProxyDataSources();
        for (final DataSourceProxyImpl dataSource : dataSources.values()) {
            data.put(dataSource.getCompositeData());
        }
        final Set<DruidDataSource> dataSources2 = DruidDataSourceStatManager.getDruidDataSourceInstances();
        for (final DruidDataSource dataSource2 : dataSources2) {
            data.put(dataSource2.getCompositeData());
        }
        return data;
    }
    
    @Override
    public TabularData getSqlList() throws JMException {
        final CompositeType rowType = JdbcSqlStat.getCompositeType();
        final String[] indexNames = rowType.keySet().toArray(new String[rowType.keySet().size()]);
        final TabularType tabularType = new TabularType("SqlListStatistic", "SqlListStatistic", rowType, indexNames);
        final TabularData data = new TabularDataSupport(tabularType);
        final JdbcDataSourceStat globalStat = JdbcDataSourceStat.getGlobal();
        if (globalStat != null) {
            final Map<String, JdbcSqlStat> statMap = globalStat.getSqlStatMap();
            for (final Map.Entry<String, JdbcSqlStat> entry : statMap.entrySet()) {
                if (entry.getValue().getExecuteCount() == 0L && entry.getValue().getRunningCount() == 0L) {
                    continue;
                }
                final Map<String, Object> map = entry.getValue().getData();
                map.put("URL", globalStat.getUrl());
                data.put(new CompositeDataSupport(JdbcSqlStat.getCompositeType(), map));
            }
        }
        for (final DataSourceProxyImpl dataSource : DruidDriver.getProxyDataSources().values()) {
            final JdbcDataSourceStat druidDataSourceStat = dataSource.getDataSourceStat();
            if (druidDataSourceStat == globalStat) {
                continue;
            }
            final Map<String, JdbcSqlStat> statMap2 = druidDataSourceStat.getSqlStatMap();
            for (final Map.Entry<String, JdbcSqlStat> entry2 : statMap2.entrySet()) {
                if (entry2.getValue().getExecuteCount() == 0L && entry2.getValue().getRunningCount() == 0L) {
                    continue;
                }
                final Map<String, Object> map2 = entry2.getValue().getData();
                map2.put("URL", dataSource.getUrl());
                data.put(new CompositeDataSupport(JdbcSqlStat.getCompositeType(), map2));
            }
        }
        for (final DruidDataSource dataSource2 : DruidDataSourceStatManager.getDruidDataSourceInstances()) {
            final JdbcDataSourceStat druidDataSourceStat = dataSource2.getDataSourceStat();
            if (druidDataSourceStat == globalStat) {
                continue;
            }
            final Map<String, JdbcSqlStat> statMap2 = druidDataSourceStat.getSqlStatMap();
            for (final Map.Entry<String, JdbcSqlStat> entry2 : statMap2.entrySet()) {
                if (entry2.getValue().getExecuteCount() == 0L && entry2.getValue().getRunningCount() == 0L) {
                    continue;
                }
                final Map<String, Object> map2 = entry2.getValue().getData();
                map2.put("URL", dataSource2.getUrl());
                data.put(new CompositeDataSupport(JdbcSqlStat.getCompositeType(), map2));
            }
        }
        return data;
    }
    
    @Override
    public TabularData getConnectionList() throws JMException {
        final CompositeType rowType = JdbcConnectionStat.Entry.getCompositeType();
        final String[] indexNames = rowType.keySet().toArray(new String[rowType.keySet().size()]);
        final TabularType tabularType = new TabularType("ConnectionList", "ConnectionList", rowType, indexNames);
        final TabularData data = new TabularDataSupport(tabularType);
        final ConcurrentMap<String, DataSourceProxyImpl> dataSources = DruidDriver.getProxyDataSources();
        for (final DataSourceProxyImpl dataSource : dataSources.values()) {
            final JdbcDataSourceStat dataSourceStat = dataSource.getDataSourceStat();
            final ConcurrentMap<Long, JdbcConnectionStat.Entry> connections = dataSourceStat.getConnections();
            for (final Map.Entry<Long, JdbcConnectionStat.Entry> entry : connections.entrySet()) {
                data.put(entry.getValue().getCompositeData());
            }
        }
        for (final DruidDataSource instance : DruidDataSourceStatManager.getDruidDataSourceInstances()) {
            final JdbcDataSourceStat dataSourceStat = instance.getDataSourceStat();
            final ConcurrentMap<Long, JdbcConnectionStat.Entry> connections = dataSourceStat.getConnections();
            for (final Map.Entry<Long, JdbcConnectionStat.Entry> entry : connections.entrySet()) {
                data.put(entry.getValue().getCompositeData());
            }
        }
        return data;
    }
    
    @Override
    public void reset() {
        this.resetCount.incrementAndGet();
        this.connectionStat.reset();
        this.statementStat.reset();
        this.resultSetStat.reset();
        final ConcurrentMap<String, DataSourceProxyImpl> dataSources = DruidDriver.getProxyDataSources();
        for (final DataSourceProxyImpl dataSource : dataSources.values()) {
            dataSource.getDataSourceStat().reset();
        }
        for (final DruidDataSource instance : DruidDataSourceStatManager.getDruidDataSourceInstances()) {
            instance.getDataSourceStat().reset();
        }
    }
    
    @Override
    public long getResetCount() {
        return this.resetCount.get();
    }
    
    static {
        instance = new JdbcStatManager();
        JdbcStatManager.COMPOSITE_TYPE = null;
    }
}
