// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import javax.management.JMException;
import java.util.Map;
import com.alibaba.druid.stat.JdbcStatManager;
import com.alibaba.druid.util.Utils;
import java.util.HashMap;
import javax.management.openmbean.CompositeDataSupport;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import com.alibaba.druid.filter.Filter;
import java.util.List;
import java.sql.SQLException;
import com.alibaba.druid.filter.FilterChain;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Properties;
import java.sql.Driver;

public class DataSourceProxyImpl implements DataSourceProxy, DataSourceProxyImplMBean
{
    private final Driver rawDriver;
    private final DataSourceProxyConfig config;
    private long id;
    private final long createdTimeMillis;
    private Properties properties;
    private String dbType;
    private final AtomicLong connectionIdSeed;
    private final AtomicLong statementIdSeed;
    private final AtomicLong resultSetIdSeed;
    private final AtomicLong metaDataIdSeed;
    private final AtomicLong transactionIdSeed;
    private final JdbcDataSourceStat dataSourceStat;
    
    public DataSourceProxyImpl(final Driver rawDriver, final DataSourceProxyConfig config) {
        this.createdTimeMillis = System.currentTimeMillis();
        this.connectionIdSeed = new AtomicLong(10000L);
        this.statementIdSeed = new AtomicLong(20000L);
        this.resultSetIdSeed = new AtomicLong(50000L);
        this.metaDataIdSeed = new AtomicLong(100000L);
        this.transactionIdSeed = new AtomicLong(0L);
        this.rawDriver = rawDriver;
        this.config = config;
        this.dbType = JdbcUtils.getDbType(config.getRawUrl(), config.getRawDriverClassName());
        this.dataSourceStat = new JdbcDataSourceStat(config.getName(), config.getUrl(), this.dbType);
    }
    
    @Override
    public String getDbType() {
        return this.dbType;
    }
    
    @Override
    public Driver getRawDriver() {
        return this.rawDriver;
    }
    
    @Override
    public String getRawUrl() {
        return this.config.getRawUrl();
    }
    
    public ConnectionProxy connect(final Properties info) throws SQLException {
        this.properties = info;
        final PasswordCallback passwordCallback = this.config.getPasswordCallback();
        if (passwordCallback != null) {
            final char[] chars = passwordCallback.getPassword();
            final String password = new String(chars);
            info.put("password", password);
        }
        final NameCallback userCallback = this.config.getUserCallback();
        if (userCallback != null) {
            final String user = userCallback.getName();
            info.put("user", user);
        }
        final FilterChain chain = new FilterChainImpl(this);
        return chain.connection_connect(info);
    }
    
    public DataSourceProxyConfig getConfig() {
        return this.config;
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return this.config.getName();
    }
    
    @Override
    public String getUrl() {
        return this.config.getUrl();
    }
    
    @Override
    public List<Filter> getProxyFilters() {
        return this.config.getFilters();
    }
    
    @Override
    public String[] getFilterClasses() {
        final List<Filter> filterConfigList = this.config.getFilters();
        final List<String> classes = new ArrayList<String>();
        for (final Filter filter : filterConfigList) {
            classes.add(filter.getClass().getName());
        }
        return classes.toArray(new String[classes.size()]);
    }
    
    @Override
    public String getRawDriverClassName() {
        return this.config.getRawDriverClassName();
    }
    
    @Override
    public Date getCreatedTime() {
        return new Date(this.createdTimeMillis);
    }
    
    @Override
    public int getRawDriverMajorVersion() {
        return this.rawDriver.getMajorVersion();
    }
    
    @Override
    public int getRawDriverMinorVersion() {
        return this.rawDriver.getMinorVersion();
    }
    
    public String getDataSourceMBeanDomain() {
        final String name = this.config.getName();
        if (name != null && name.length() != 0) {
            return name;
        }
        return "java.sql.dataSource_" + System.identityHashCode(this);
    }
    
    @Override
    public String getProperties() {
        if (this.properties == null) {
            return null;
        }
        final Properties properties = new Properties(this.properties);
        if (properties.contains("password")) {
            properties.put("password", "******");
        }
        return properties.toString();
    }
    
    @Override
    public Properties getConnectProperties() {
        return this.properties;
    }
    
    public CompositeDataSupport getCompositeData() throws JMException {
        final JdbcDataSourceStat stat = this.getDataSourceStat();
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("ID", this.id);
        map.put("URL", this.getUrl());
        map.put("Name", this.getName());
        map.put("FilterClasses", this.getFilterClasses());
        map.put("CreatedTime", this.getCreatedTime());
        map.put("RawDriverClassName", this.getRawDriverClassName());
        map.put("RawUrl", this.getRawUrl());
        map.put("RawDriverMajorVersion", this.getRawDriverMajorVersion());
        map.put("RawDriverMinorVersion", this.getRawDriverMinorVersion());
        map.put("Properties", this.getProperties());
        if (stat != null) {
            map.put("ConnectionActiveCount", stat.getConnectionActiveCount());
            map.put("ConnectionActiveCountMax", stat.getConnectionStat().getActiveMax());
            map.put("ConnectionCloseCount", stat.getConnectionStat().getCloseCount());
            map.put("ConnectionCommitCount", stat.getConnectionStat().getCommitCount());
            map.put("ConnectionRollbackCount", stat.getConnectionStat().getRollbackCount());
            map.put("ConnectionConnectLastTime", stat.getConnectionStat().getConnectLastTime());
            map.put("ConnectionConnectErrorCount", stat.getConnectionStat().getConnectErrorCount());
            final Throwable lastConnectionConnectError = stat.getConnectionStat().getConnectErrorLast();
            if (lastConnectionConnectError != null) {
                map.put("ConnectionConnectErrorLastTime", stat.getConnectionStat().getErrorLastTime());
                map.put("ConnectionConnectErrorLastMessage", lastConnectionConnectError.getMessage());
                map.put("ConnectionConnectErrorLastStackTrace", Utils.getStackTrace(lastConnectionConnectError));
            }
            else {
                map.put("ConnectionConnectErrorLastTime", null);
                map.put("ConnectionConnectErrorLastMessage", null);
                map.put("ConnectionConnectErrorLastStackTrace", null);
            }
            map.put("StatementCreateCount", stat.getStatementStat().getCreateCount());
            map.put("StatementPrepareCount", stat.getStatementStat().getPrepareCount());
            map.put("StatementPreCallCount", stat.getStatementStat().getPrepareCallCount());
            map.put("StatementExecuteCount", stat.getStatementStat().getExecuteCount());
            map.put("StatementRunningCount", stat.getStatementStat().getRunningCount());
            map.put("StatementConcurrentMax", stat.getStatementStat().getConcurrentMax());
            map.put("StatementCloseCount", stat.getStatementStat().getCloseCount());
            map.put("StatementErrorCount", stat.getStatementStat().getErrorCount());
            final Throwable lastStatementError = stat.getStatementStat().getLastException();
            if (lastStatementError != null) {
                map.put("StatementLastErrorTime", stat.getStatementStat().getLastErrorTime());
                map.put("StatementLastErrorMessage", lastStatementError.getMessage());
                map.put("StatementLastErrorStackTrace", Utils.getStackTrace(lastStatementError));
            }
            else {
                map.put("StatementLastErrorTime", null);
                map.put("StatementLastErrorMessage", null);
                map.put("StatementLastErrorStackTrace", null);
            }
            map.put("StatementExecuteMillisTotal", stat.getStatementStat().getMillisTotal());
            map.put("StatementExecuteLastTime", stat.getStatementStat().getExecuteLastTime());
            map.put("ConnectionConnectingCount", stat.getConnectionStat().getConnectingCount());
            map.put("ResultSetCloseCount", stat.getResultSetStat().getCloseCount());
            map.put("ResultSetOpenCount", stat.getResultSetStat().getOpenCount());
            map.put("ResultSetOpenningCount", stat.getResultSetStat().getOpeningCount());
            map.put("ResultSetOpenningMax", stat.getResultSetStat().getOpeningMax());
            map.put("ResultSetFetchRowCount", stat.getResultSetStat().getFetchRowCount());
            map.put("ResultSetLastOpenTime", stat.getResultSetStat().getLastOpenTime());
            map.put("ResultSetErrorCount", stat.getResultSetStat().getErrorCount());
            map.put("ResultSetOpenningMillisTotal", stat.getResultSetStat().getAliveMillisTotal());
            map.put("ResultSetLastErrorTime", stat.getResultSetStat().getLastErrorTime());
            final Throwable lastResultSetError = stat.getResultSetStat().getLastError();
            if (lastResultSetError != null) {
                map.put("ResultSetLastErrorMessage", lastResultSetError.getMessage());
                map.put("ResultSetLastErrorStackTrace", Utils.getStackTrace(lastResultSetError));
            }
            else {
                map.put("ResultSetLastErrorMessage", null);
                map.put("ResultSetLastErrorStackTrace", null);
            }
            map.put("ConnectionConnectCount", stat.getConnectionStat().getConnectCount());
            final Throwable lastConnectionError = stat.getConnectionStat().getErrorLast();
            if (lastConnectionError != null) {
                map.put("ConnectionErrorLastMessage", lastConnectionError.getMessage());
                map.put("ConnectionErrorLastStackTrace", Utils.getStackTrace(lastConnectionError));
            }
            else {
                map.put("ConnectionErrorLastMessage", null);
                map.put("ConnectionErrorLastStackTrace", null);
            }
            map.put("ConnectionConnectMillisTotal", stat.getConnectionStat().getConnectMillis());
            map.put("ConnectionConnectingCountMax", stat.getConnectionStat().getConnectingMax());
            map.put("ConnectionConnectMillisMax", stat.getConnectionStat().getConnectMillisMax());
            map.put("ConnectionErrorLastTime", stat.getConnectionStat().getErrorLastTime());
            map.put("ConnectionAliveMillisMax", stat.getConnectionConnectAliveMillisMax());
            map.put("ConnectionAliveMillisMin", stat.getConnectionConnectAliveMillisMin());
            map.put("ConnectionHistogram", stat.getConnectionHistogramValues());
            map.put("StatementHistogram", stat.getStatementStat().getHistogramValues());
        }
        else {
            map.put("ConnectionActiveCount", null);
            map.put("ConnectionActiveCountMax", null);
            map.put("ConnectionCloseCount", null);
            map.put("ConnectionCommitCount", null);
            map.put("ConnectionRollbackCount", null);
            map.put("ConnectionConnectLastTime", null);
            map.put("ConnectionConnectErrorCount", null);
            map.put("ConnectionConnectErrorLastTime", null);
            map.put("ConnectionConnectErrorLastMessage", null);
            map.put("ConnectionConnectErrorLastStackTrace", null);
            map.put("StatementCreateCount", null);
            map.put("StatementPrepareCount", null);
            map.put("StatementPreCallCount", null);
            map.put("StatementExecuteCount", null);
            map.put("StatementRunningCount", null);
            map.put("StatementConcurrentMax", null);
            map.put("StatementCloseCount", null);
            map.put("StatementErrorCount", null);
            map.put("StatementLastErrorTime", null);
            map.put("StatementLastErrorMessage", null);
            map.put("StatementLastErrorStackTrace", null);
            map.put("StatementExecuteMillisTotal", null);
            map.put("ConnectionConnectingCount", null);
            map.put("StatementExecuteLastTime", null);
            map.put("ResultSetCloseCount", null);
            map.put("ResultSetOpenCount", null);
            map.put("ResultSetOpenningCount", null);
            map.put("ResultSetOpenningMax", null);
            map.put("ResultSetFetchRowCount", null);
            map.put("ResultSetLastOpenTime", null);
            map.put("ResultSetErrorCount", null);
            map.put("ResultSetOpenningMillisTotal", null);
            map.put("ResultSetLastErrorTime", null);
            map.put("ResultSetLastErrorMessage", null);
            map.put("ResultSetLastErrorStackTrace", null);
            map.put("ConnectionConnectCount", null);
            map.put("ConnectionErrorLastMessage", null);
            map.put("ConnectionErrorLastStackTrace", null);
            map.put("ConnectionConnectMillisTotal", null);
            map.put("ConnectionConnectingCountMax", null);
            map.put("ConnectionConnectMillisMax", null);
            map.put("ConnectionErrorLastTime", null);
            map.put("ConnectionAliveMillisMax", null);
            map.put("ConnectionAliveMillisMin", null);
            map.put("ConnectionHistogram", new long[0]);
            map.put("StatementHistogram", new long[0]);
        }
        return new CompositeDataSupport(JdbcStatManager.getDataSourceCompositeType(), map);
    }
    
    @Override
    public String getRawJdbcUrl() {
        return this.config.getRawUrl();
    }
    
    @Override
    public long createConnectionId() {
        return this.connectionIdSeed.incrementAndGet();
    }
    
    @Override
    public long createStatementId() {
        return this.statementIdSeed.getAndIncrement();
    }
    
    @Override
    public long createResultSetId() {
        return this.resultSetIdSeed.getAndIncrement();
    }
    
    @Override
    public long createMetaDataId() {
        return this.metaDataIdSeed.getAndIncrement();
    }
    
    @Override
    public long createTransactionId() {
        return this.transactionIdSeed.getAndIncrement();
    }
    
    @Override
    public JdbcDataSourceStat getDataSourceStat() {
        return this.dataSourceStat;
    }
}
