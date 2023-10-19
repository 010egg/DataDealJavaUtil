// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Observer;
import com.alibaba.druid.pool.ha.node.FileNodeListener;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import java.sql.DriverManager;
import com.alibaba.druid.pool.ha.selector.DataSourceSelectorFactory;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Iterator;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ha.selector.DataSourceSelectorEnum;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentHashMap;
import java.io.OutputStream;
import java.util.concurrent.CopyOnWriteArrayList;
import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.ha.node.NodeListener;
import com.alibaba.druid.pool.ha.node.PoolUpdater;
import com.alibaba.druid.pool.ha.selector.DataSourceSelector;
import java.util.Set;
import java.util.Map;
import java.io.PrintWriter;
import com.alibaba.druid.filter.Filter;
import java.util.List;
import java.util.Properties;
import com.alibaba.druid.support.logging.Log;
import javax.sql.DataSource;
import com.alibaba.druid.pool.WrapperAdapter;

public class HighAvailableDataSource extends WrapperAdapter implements DataSource
{
    private static final Log LOG;
    private static final String DEFAULT_DATA_SOURCE_FILE = "ha-datasource.properties";
    private String driverClassName;
    private Properties connectProperties;
    private String connectionProperties;
    private int initialSize;
    private int maxActive;
    private int minIdle;
    private long maxWait;
    private String validationQuery;
    private int validationQueryTimeout;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean testWhileIdle;
    private boolean poolPreparedStatements;
    private boolean sharePreparedStatements;
    private int maxPoolPreparedStatementPerConnectionSize;
    private int queryTimeout;
    private int transactionQueryTimeout;
    private long timeBetweenEvictionRunsMillis;
    private long minEvictableIdleTimeMillis;
    private long maxEvictableIdleTimeMillis;
    private long phyTimeoutMillis;
    private long timeBetweenConnectErrorMillis;
    private boolean removeAbandoned;
    private long removeAbandonedTimeoutMillis;
    private boolean logAbandoned;
    private String filters;
    private List<Filter> proxyFilters;
    private PrintWriter logWriter;
    private Map<String, DataSource> dataSourceMap;
    private Set<String> blacklist;
    private DataSourceSelector selector;
    private String dataSourceFile;
    private String propertyPrefix;
    private int poolPurgeIntervalSeconds;
    private boolean allowEmptyPoolWhenUpdate;
    private volatile boolean inited;
    private PoolUpdater poolUpdater;
    private NodeListener nodeListener;
    
    public HighAvailableDataSource() {
        this.connectProperties = new Properties();
        this.connectionProperties = null;
        this.initialSize = 0;
        this.maxActive = 8;
        this.minIdle = 0;
        this.maxWait = -1L;
        this.validationQuery = DruidAbstractDataSource.DEFAULT_VALIDATION_QUERY;
        this.validationQueryTimeout = -1;
        this.testOnBorrow = false;
        this.testOnReturn = false;
        this.testWhileIdle = true;
        this.poolPreparedStatements = false;
        this.sharePreparedStatements = false;
        this.maxPoolPreparedStatementPerConnectionSize = 10;
        this.timeBetweenEvictionRunsMillis = 60000L;
        this.minEvictableIdleTimeMillis = 1800000L;
        this.maxEvictableIdleTimeMillis = 25200000L;
        this.phyTimeoutMillis = -1L;
        this.timeBetweenConnectErrorMillis = 500L;
        this.removeAbandonedTimeoutMillis = 300000L;
        this.proxyFilters = new CopyOnWriteArrayList<Filter>();
        this.logWriter = new PrintWriter(System.out);
        this.dataSourceMap = new ConcurrentHashMap<String, DataSource>();
        this.blacklist = new CopyOnWriteArraySet<String>();
        this.dataSourceFile = "ha-datasource.properties";
        this.propertyPrefix = "";
        this.poolPurgeIntervalSeconds = 60;
        this.allowEmptyPoolWhenUpdate = false;
        this.inited = false;
        this.poolUpdater = new PoolUpdater(this);
    }
    
    public void init() {
        if (this.inited) {
            return;
        }
        synchronized (this) {
            if (this.inited) {
                return;
            }
            if (this.dataSourceMap == null || this.dataSourceMap.isEmpty()) {
                this.poolUpdater.setIntervalSeconds(this.poolPurgeIntervalSeconds);
                this.poolUpdater.setAllowEmptyPool(this.allowEmptyPoolWhenUpdate);
                this.poolUpdater.init();
                this.createNodeMap();
            }
            if (this.selector == null) {
                this.setSelector(DataSourceSelectorEnum.RANDOM.getName());
            }
            if (this.dataSourceMap == null || this.dataSourceMap.isEmpty()) {
                HighAvailableDataSource.LOG.warn("There is NO DataSource available!!! Please check your configuration.");
            }
            this.inited = true;
        }
    }
    
    public void close() {
        this.destroy();
    }
    
    public void destroy() {
        if (this.nodeListener != null) {
            this.nodeListener.destroy();
        }
        if (this.poolUpdater != null) {
            this.poolUpdater.destroy();
        }
        if (this.selector != null) {
            this.selector.destroy();
        }
        if (this.dataSourceMap == null || this.dataSourceMap.isEmpty()) {
            return;
        }
        for (final DataSource dataSource : this.dataSourceMap.values()) {
            if (dataSource instanceof DruidDataSource) {
                ((DruidDataSource)dataSource).close();
            }
        }
    }
    
    public void setTargetDataSource(final String targetName) {
        this.selector.setTarget(targetName);
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        this.init();
        final DataSource dataSource = this.selector.get();
        if (dataSource == null) {
            HighAvailableDataSource.LOG.warn("Can NOT obtain DataSource, return null.");
            return null;
        }
        return dataSource.getConnection();
    }
    
    public String getDataSourceFile() {
        return this.dataSourceFile;
    }
    
    public void setDataSourceFile(final String dataSourceFile) {
        this.dataSourceFile = dataSourceFile;
    }
    
    public String getPropertyPrefix() {
        return this.propertyPrefix;
    }
    
    public void setPropertyPrefix(final String propertyPrefix) {
        this.propertyPrefix = propertyPrefix;
    }
    
    public void setDataSourceMap(final Map<String, DataSource> dataSourceMap) {
        if (dataSourceMap != null) {
            this.dataSourceMap = dataSourceMap;
        }
    }
    
    public Map<String, DataSource> getDataSourceMap() {
        return this.dataSourceMap;
    }
    
    public Map<String, DataSource> getAvailableDataSourceMap() {
        final Map<String, DataSource> map = new ConcurrentHashMap<String, DataSource>(this.dataSourceMap);
        for (final String n : this.blacklist) {
            if (map.containsKey(n)) {
                map.remove(n);
            }
        }
        return map;
    }
    
    public void addBlackList(final String name) {
        if (this.dataSourceMap.containsKey(name)) {
            this.blacklist.add(name);
        }
        else {
            HighAvailableDataSource.LOG.info("Key " + name + " is NOT existed, ignore it.");
        }
    }
    
    public void removeBlackList(final String name) {
        this.blacklist.remove(name);
    }
    
    public boolean isInBlackList(final String name) {
        return this.blacklist.contains(name);
    }
    
    public void setSelector(final String name) {
        final DataSourceSelector selector = DataSourceSelectorFactory.getSelector(name, this);
        if (selector != null) {
            selector.init();
            this.setDataSourceSelector(selector);
        }
    }
    
    public String getSelector() {
        return (this.selector == null) ? null : this.selector.getName();
    }
    
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by HighAvailableDataSource.");
    }
    
    @Override
    public void setLoginTimeout(final int seconds) {
        DriverManager.setLoginTimeout(seconds);
    }
    
    @Override
    public int getLoginTimeout() {
        return DriverManager.getLoginTimeout();
    }
    
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
    
    public void setConnectionProperties(final String connectionProperties) {
        this.connectionProperties = connectionProperties;
        if (connectionProperties == null || connectionProperties.trim().length() == 0) {
            this.setConnectProperties(null);
            return;
        }
        final String[] entries = connectionProperties.split(";");
        final Properties properties = new Properties();
        for (int i = 0; i < entries.length; ++i) {
            final String entry = entries[i];
            if (entry.length() > 0) {
                final int index = entry.indexOf(61);
                if (index > 0) {
                    final String name = entry.substring(0, index);
                    final String value = entry.substring(index + 1);
                    properties.setProperty(name, value);
                }
                else {
                    properties.setProperty(entry, "");
                }
            }
        }
        this.setConnectProperties(properties);
    }
    
    public void setConnectProperties(Properties connectProperties) {
        if (connectProperties == null) {
            connectProperties = new Properties();
        }
        if (this.connectProperties != null) {
            this.connectProperties.putAll(connectProperties);
        }
        else {
            this.connectProperties = connectProperties;
        }
    }
    
    private void createNodeMap() {
        if (this.nodeListener == null) {
            final FileNodeListener listener = new FileNodeListener();
            listener.setFile(this.dataSourceFile);
            listener.setPrefix(this.propertyPrefix);
            this.nodeListener = listener;
        }
        this.nodeListener.setObserver(this.poolUpdater);
        this.nodeListener.init();
        this.nodeListener.update();
    }
    
    public boolean isAllowEmptyPoolWhenUpdate() {
        return this.allowEmptyPoolWhenUpdate;
    }
    
    public void setAllowEmptyPoolWhenUpdate(final boolean allowEmptyPoolWhenUpdate) {
        this.allowEmptyPoolWhenUpdate = allowEmptyPoolWhenUpdate;
    }
    
    public int getPoolPurgeIntervalSeconds() {
        return this.poolPurgeIntervalSeconds;
    }
    
    public void setPoolPurgeIntervalSeconds(final int poolPurgeIntervalSeconds) {
        this.poolPurgeIntervalSeconds = poolPurgeIntervalSeconds;
    }
    
    public NodeListener getNodeListener() {
        return this.nodeListener;
    }
    
    public void setNodeListener(final NodeListener nodeListener) {
        this.nodeListener = nodeListener;
    }
    
    public DataSourceSelector getDataSourceSelector() {
        return this.selector;
    }
    
    public void setDataSourceSelector(final DataSourceSelector dataSourceSelector) {
        this.selector = dataSourceSelector;
    }
    
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.logWriter;
    }
    
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        this.logWriter = out;
    }
    
    public String getDriverClassName() {
        return this.driverClassName;
    }
    
    public void setDriverClassName(final String driverClassName) {
        this.driverClassName = driverClassName;
    }
    
    public Properties getConnectProperties() {
        return this.connectProperties;
    }
    
    public int getInitialSize() {
        return this.initialSize;
    }
    
    public void setInitialSize(final int initialSize) {
        this.initialSize = initialSize;
    }
    
    public int getMaxActive() {
        return this.maxActive;
    }
    
    public void setMaxActive(final int maxActive) {
        this.maxActive = maxActive;
    }
    
    public int getMinIdle() {
        return this.minIdle;
    }
    
    public void setMinIdle(final int minIdle) {
        this.minIdle = minIdle;
    }
    
    public long getMaxWait() {
        return this.maxWait;
    }
    
    public void setMaxWait(final long maxWait) {
        this.maxWait = maxWait;
    }
    
    public String getValidationQuery() {
        return this.validationQuery;
    }
    
    public void setValidationQuery(final String validationQuery) {
        this.validationQuery = validationQuery;
    }
    
    public int getValidationQueryTimeout() {
        return this.validationQueryTimeout;
    }
    
    public void setValidationQueryTimeout(final int validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
    }
    
    public boolean isTestOnBorrow() {
        return this.testOnBorrow;
    }
    
    public void setTestOnBorrow(final boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    
    public boolean isTestOnReturn() {
        return this.testOnReturn;
    }
    
    public void setTestOnReturn(final boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    
    public boolean isTestWhileIdle() {
        return this.testWhileIdle;
    }
    
    public void setTestWhileIdle(final boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    
    public boolean isPoolPreparedStatements() {
        return this.poolPreparedStatements;
    }
    
    public void setPoolPreparedStatements(final boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }
    
    public boolean isSharePreparedStatements() {
        return this.sharePreparedStatements;
    }
    
    public void setSharePreparedStatements(final boolean sharePreparedStatements) {
        this.sharePreparedStatements = sharePreparedStatements;
    }
    
    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return this.maxPoolPreparedStatementPerConnectionSize;
    }
    
    public void setMaxPoolPreparedStatementPerConnectionSize(final int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }
    
    public int getQueryTimeout() {
        return this.queryTimeout;
    }
    
    public void setQueryTimeout(final int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }
    
    public int getTransactionQueryTimeout() {
        return this.transactionQueryTimeout;
    }
    
    public void setTransactionQueryTimeout(final int transactionQueryTimeout) {
        this.transactionQueryTimeout = transactionQueryTimeout;
    }
    
    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }
    
    public void setTimeBetweenEvictionRunsMillis(final long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
    
    public long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }
    
    public void setMinEvictableIdleTimeMillis(final long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
    
    public long getMaxEvictableIdleTimeMillis() {
        return this.maxEvictableIdleTimeMillis;
    }
    
    public void setMaxEvictableIdleTimeMillis(final long maxEvictableIdleTimeMillis) {
        this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
    }
    
    public long getPhyTimeoutMillis() {
        return this.phyTimeoutMillis;
    }
    
    public void setPhyTimeoutMillis(final long phyTimeoutMillis) {
        this.phyTimeoutMillis = phyTimeoutMillis;
    }
    
    public long getTimeBetweenConnectErrorMillis() {
        return this.timeBetweenConnectErrorMillis;
    }
    
    public void setTimeBetweenConnectErrorMillis(final long timeBetweenConnectErrorMillis) {
        this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
    }
    
    public boolean isRemoveAbandoned() {
        return this.removeAbandoned;
    }
    
    public void setRemoveAbandoned(final boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }
    
    public long getRemoveAbandonedTimeoutMillis() {
        return this.removeAbandonedTimeoutMillis;
    }
    
    public void setRemoveAbandonedTimeoutMillis(final long removeAbandonedTimeoutMillis) {
        this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
    }
    
    public boolean isLogAbandoned() {
        return this.logAbandoned;
    }
    
    public void setLogAbandoned(final boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }
    
    public String getConnectionProperties() {
        return this.connectionProperties;
    }
    
    public String getFilters() {
        return this.filters;
    }
    
    public void setFilters(final String filters) {
        this.filters = filters;
    }
    
    public List<Filter> getProxyFilters() {
        return this.proxyFilters;
    }
    
    public void setProxyFilters(final List<Filter> proxyFilters) {
        this.proxyFilters = proxyFilters;
    }
    
    static {
        LOG = LogFactory.getLog(HighAvailableDataSource.class);
    }
}
