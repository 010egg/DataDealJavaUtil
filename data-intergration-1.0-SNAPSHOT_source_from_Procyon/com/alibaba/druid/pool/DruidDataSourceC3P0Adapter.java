// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import com.alibaba.druid.filter.Filter;
import java.util.List;
import java.util.Properties;
import java.sql.Driver;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.PrintWriter;
import javax.sql.DataSource;

public class DruidDataSourceC3P0Adapter implements DataSource, DruidDataSourceC3P0AdapterMBean
{
    private DruidDataSource dataSource;
    private String overrideDefaultUser;
    private String overrideDefaultPassword;
    private int propertyCycle;
    private boolean usesTraditionalReflectiveProxies;
    private String userOverridesAsString;
    private int maxAdministrativeTaskTime;
    private int maxIdleTimeExcessConnections;
    private int maxConnectionAge;
    private String connectionCustomizerClassName;
    private String factoryClassLocation;
    private int acquireIncrement;
    private String connectionTesterClassName;
    private String automaticTestTable;
    private boolean forceIgnoreUnresolvedTransactions;
    
    public DruidDataSourceC3P0Adapter() {
        this.acquireIncrement = 1;
        (this.dataSource = new DruidDataSource()).setInitialSize(3);
        this.acquireIncrement = 3;
        this.dataSource.setTimeBetweenConnectErrorMillis(1000L);
        this.dataSource.setDefaultAutoCommit(true);
        this.dataSource.setLogAbandoned(false);
        this.dataSource.setMaxActive(15);
        this.dataSource.setMinIdle(3);
        this.dataSource.setTestOnReturn(false);
        this.dataSource.setTestOnBorrow(false);
    }
    
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.dataSource.getLogWriter();
    }
    
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        this.dataSource.setLogWriter(out);
    }
    
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        this.dataSource.setLoginTimeout(seconds);
    }
    
    @Override
    public int getLoginTimeout() throws SQLException {
        return this.dataSource.getLoginTimeout();
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface == DruidDataSourceC3P0Adapter.class) {
            return (T)this;
        }
        return this.dataSource.unwrap(iface);
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface == DruidDataSourceC3P0Adapter.class || this.dataSource.isWrapperFor(iface);
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
    
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        return this.dataSource.getConnection(username, password);
    }
    
    @Override
    public String getDriverClass() {
        return this.dataSource.getDriverClassName();
    }
    
    public void setDriverClass(final String driverClass) {
        this.dataSource.setDriverClassName(driverClass);
    }
    
    @Override
    public String getJdbcUrl() {
        return this.dataSource.getUrl();
    }
    
    public Driver getDriver() {
        return this.dataSource.getDriver();
    }
    
    public void setDriver(final Driver driver) {
        this.dataSource.setDriver(driver);
    }
    
    public void setJdbcUrl(final String jdbcUrl) {
        this.dataSource.setUrl(jdbcUrl);
    }
    
    @Override
    public Properties getProperties() {
        return this.dataSource.getConnectProperties();
    }
    
    public void setProperties(final Properties properties) {
        this.dataSource.setConnectProperties(properties);
    }
    
    @Override
    public String getUser() {
        return this.dataSource.getUsername();
    }
    
    public void setUser(final String user) {
        this.dataSource.setUsername(user);
    }
    
    public String getPassword() {
        return this.dataSource.getPassword();
    }
    
    public void setPassword(final String password) {
        this.dataSource.setPassword(password);
    }
    
    @Override
    public int getCheckoutTimeout() {
        return (int)this.dataSource.getMaxWait();
    }
    
    public void setCheckoutTimeout(final int checkoutTimeout) {
        this.dataSource.setMaxWait(checkoutTimeout);
    }
    
    @Override
    public boolean isAutoCommitOnClose() {
        return this.dataSource.isDefaultAutoCommit();
    }
    
    public void setAutoCommitOnClose(final boolean autoCommitOnClose) {
        this.dataSource.setDefaultAutoCommit(autoCommitOnClose);
    }
    
    @Override
    public int getIdleConnectionTestPeriod() {
        return (int)(this.dataSource.getTimeBetweenEvictionRunsMillis() / 1000L);
    }
    
    public void setIdleConnectionTestPeriod(final int idleConnectionTestPeriod) {
        final long timeBetweenEvictionRunsMillis = idleConnectionTestPeriod * 1000L;
        this.dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        this.dataSource.setKeepAliveBetweenTimeMillis(timeBetweenEvictionRunsMillis * 2L);
    }
    
    @Override
    public int getInitialPoolSize() {
        return this.dataSource.getInitialSize();
    }
    
    public void setInitialPoolSize(final int initialPoolSize) {
        this.dataSource.setInitialSize(initialPoolSize);
    }
    
    @Override
    public int getMaxIdleTime() {
        return (int)this.dataSource.getMinEvictableIdleTimeMillis() / 1000;
    }
    
    public void setMaxIdleTime(final int maxIdleTime) {
        this.dataSource.setMinEvictableIdleTimeMillis(maxIdleTime * 1000L);
    }
    
    @Override
    public int getMaxPoolSize() {
        return this.dataSource.getMaxActive();
    }
    
    public void setMaxPoolSize(final int maxPoolSize) {
        this.dataSource.setMaxActive(maxPoolSize);
    }
    
    @Override
    public int getMinPoolSize() {
        return this.dataSource.getMinIdle();
    }
    
    public void setMinPoolSize(final int minPoolSize) {
        this.dataSource.setMinIdle(minPoolSize);
    }
    
    @Override
    public boolean isTestConnectionOnCheckout() {
        return this.dataSource.isTestOnBorrow();
    }
    
    public void setTestConnectionOnCheckout(final boolean testConnectionOnCheckout) {
        this.dataSource.setTestOnBorrow(testConnectionOnCheckout);
    }
    
    @Override
    public boolean isTestConnectionOnCheckin() {
        return this.dataSource.isTestOnReturn();
    }
    
    public void setTestConnectionOnCheckin(final boolean testConnectionOnCheckin) {
        this.dataSource.setTestOnReturn(testConnectionOnCheckin);
    }
    
    @Override
    public String getPreferredTestQuery() {
        return this.dataSource.getValidationQuery();
    }
    
    public void setPreferredTestQuery(final String preferredTestQuery) {
        this.dataSource.setValidationQuery(preferredTestQuery);
    }
    
    public void setFilters(final String filters) throws SQLException {
        this.dataSource.setFilters(filters);
    }
    
    public List<Filter> getProxyFilters() {
        return this.dataSource.getProxyFilters();
    }
    
    public void setProxyFilters(final List<Filter> filters) {
        this.dataSource.setProxyFilters(filters);
    }
    
    @Override
    public String getDataSourceName() {
        return this.dataSource.getName();
    }
    
    @Override
    public int getNumConnections() {
        return this.dataSource.getActiveCount() + this.dataSource.getPoolingCount();
    }
    
    @Override
    public int getNumIdleConnections() {
        return this.dataSource.getPoolingCount();
    }
    
    @Override
    public int getNumBusyConnections() {
        return this.dataSource.getActiveCount();
    }
    
    public int getNumUnclosedOrphanedConnections() {
        return 0;
    }
    
    public int getNumConnectionsDefaultUser() {
        return this.getNumConnections();
    }
    
    public int getNumIdleConnectionsDefaultUser() {
        return this.getNumIdleConnections();
    }
    
    public int getNumBusyConnectionsDefaultUser() {
        return this.getNumBusyConnections();
    }
    
    public int getMaxStatementsPerConnection() {
        return this.dataSource.getMaxPoolPreparedStatementPerConnectionSize();
    }
    
    public void setMaxStatementsPerConnection(final int maxStatementsPerConnection) {
        this.dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxStatementsPerConnection);
    }
    
    public int getMaxStatements() {
        return this.dataSource.getMaxOpenPreparedStatements();
    }
    
    public void setMaxStatements(final int maxStatements) {
        this.dataSource.setMaxOpenPreparedStatements(maxStatements);
    }
    
    public int getUnreturnedConnectionTimeout() {
        return this.dataSource.getRemoveAbandonedTimeout();
    }
    
    public void setUnreturnedConnectionTimeout(final int unreturnedConnectionTimeout) {
        this.dataSource.setRemoveAbandonedTimeout(unreturnedConnectionTimeout);
    }
    
    public boolean isDebugUnreturnedConnectionStackTraces() {
        return this.dataSource.isLogAbandoned();
    }
    
    public void setDebugUnreturnedConnectionStackTraces(final boolean debugUnreturnedConnectionStackTraces) {
        this.dataSource.setLogAbandoned(debugUnreturnedConnectionStackTraces);
    }
    
    public int getAcquireRetryAttempts() {
        return this.dataSource.getConnectionErrorRetryAttempts();
    }
    
    public void setAcquireRetryAttempts(final int acquireRetryAttempts) {
        this.dataSource.setConnectionErrorRetryAttempts(acquireRetryAttempts);
    }
    
    public int getAcquireRetryDelay() {
        return (int)this.dataSource.getTimeBetweenConnectErrorMillis();
    }
    
    public void setAcquireRetryDelay(final int acquireRetryDelay) {
        this.dataSource.setTimeBetweenConnectErrorMillis(acquireRetryDelay);
    }
    
    public boolean isBreakAfterAcquireFailure() {
        return this.dataSource.isBreakAfterAcquireFailure();
    }
    
    public void setBreakAfterAcquireFailure(final boolean breakAfterAcquireFailure) {
        this.dataSource.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
    }
    
    @Override
    public boolean isEnable() {
        return this.dataSource.isEnable();
    }
    
    @Override
    public void shrink() {
        this.dataSource.shrink();
    }
    
    @Override
    public int getWaitThreadCount() {
        return this.dataSource.getWaitThreadCount();
    }
    
    @Override
    public int getLockQueueLength() {
        return this.dataSource.getLockQueueLength();
    }
    
    @Override
    public void close() {
        this.dataSource.close();
    }
    
    public String getConnectionTesterClassName() {
        return this.connectionTesterClassName;
    }
    
    public void setConnectionTesterClassName(final String connectionTesterClassName) {
        this.connectionTesterClassName = connectionTesterClassName;
    }
    
    public String getAutomaticTestTable() {
        return this.automaticTestTable;
    }
    
    public void setAutomaticTestTable(final String automaticTestTable) {
        this.automaticTestTable = automaticTestTable;
    }
    
    public boolean isForceIgnoreUnresolvedTransactions() {
        return this.forceIgnoreUnresolvedTransactions;
    }
    
    public void setForceIgnoreUnresolvedTransactions(final boolean forceIgnoreUnresolvedTransactions) {
        this.forceIgnoreUnresolvedTransactions = forceIgnoreUnresolvedTransactions;
    }
    
    public boolean isUsesTraditionalReflectiveProxies() {
        return this.usesTraditionalReflectiveProxies;
    }
    
    public void setUsesTraditionalReflectiveProxies(final boolean usesTraditionalReflectiveProxies) {
        this.usesTraditionalReflectiveProxies = usesTraditionalReflectiveProxies;
    }
    
    public String getUserOverridesAsString() {
        return this.userOverridesAsString;
    }
    
    public void setUserOverridesAsString(final String userOverridesAsString) {
        this.userOverridesAsString = userOverridesAsString;
    }
    
    public int getMaxAdministrativeTaskTime() {
        return this.maxAdministrativeTaskTime;
    }
    
    public void setMaxAdministrativeTaskTime(final int maxAdministrativeTaskTime) {
        this.maxAdministrativeTaskTime = maxAdministrativeTaskTime;
    }
    
    public int getMaxIdleTimeExcessConnections() {
        return this.maxIdleTimeExcessConnections;
    }
    
    public void setMaxIdleTimeExcessConnections(final int maxIdleTimeExcessConnections) {
        this.maxIdleTimeExcessConnections = maxIdleTimeExcessConnections;
    }
    
    public int getMaxConnectionAge() {
        return this.maxConnectionAge;
    }
    
    public void setMaxConnectionAge(final int maxConnectionAge) {
        this.maxConnectionAge = maxConnectionAge;
    }
    
    public String getConnectionCustomizerClassName() {
        return this.connectionCustomizerClassName;
    }
    
    public void setConnectionCustomizerClassName(final String connectionCustomizerClassName) {
        this.connectionCustomizerClassName = connectionCustomizerClassName;
    }
    
    public String getFactoryClassLocation() {
        return this.factoryClassLocation;
    }
    
    public void setFactoryClassLocation(final String factoryClassLocation) {
        this.factoryClassLocation = factoryClassLocation;
    }
    
    public int getAcquireIncrement() {
        return this.acquireIncrement;
    }
    
    public void setAcquireIncrement(final int acquireIncrement) {
        this.acquireIncrement = acquireIncrement;
    }
    
    public String getOverrideDefaultUser() {
        return this.overrideDefaultUser;
    }
    
    public void setOverrideDefaultUser(final String overrideDefaultUser) {
        this.overrideDefaultUser = overrideDefaultUser;
    }
    
    public String getOverrideDefaultPassword() {
        return this.overrideDefaultPassword;
    }
    
    public void setOverrideDefaultPassword(final String overrideDefaultPassword) {
        this.overrideDefaultPassword = overrideDefaultPassword;
    }
    
    public int getPropertyCycle() {
        return this.propertyCycle;
    }
    
    public void setPropertyCycle(final int propertyCycle) {
        this.propertyCycle = propertyCycle;
    }
    
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.dataSource.getParentLogger();
    }
}
