// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha;

import java.sql.SQLException;
import com.alibaba.druid.pool.DruidDataSource;

public class DataSourceCreator
{
    public static DruidDataSource create(final String name, final String url, final String username, final String password, final HighAvailableDataSource haDataSource) throws SQLException {
        final DruidDataSource dataSource = new DruidDataSource();
        dataSource.setName(name + "-" + System.identityHashCode(dataSource));
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(haDataSource.getDriverClassName());
        dataSource.setConnectProperties(haDataSource.getConnectProperties());
        dataSource.setConnectionProperties(haDataSource.getConnectionProperties());
        dataSource.setInitialSize(haDataSource.getInitialSize());
        dataSource.setMaxActive(haDataSource.getMaxActive());
        dataSource.setMinIdle(haDataSource.getMinIdle());
        dataSource.setMaxWait(haDataSource.getMaxWait());
        dataSource.setValidationQuery(haDataSource.getValidationQuery());
        dataSource.setValidationQueryTimeout(haDataSource.getValidationQueryTimeout());
        dataSource.setTestOnBorrow(haDataSource.isTestOnBorrow());
        dataSource.setTestOnReturn(haDataSource.isTestOnReturn());
        dataSource.setTestWhileIdle(haDataSource.isTestWhileIdle());
        dataSource.setPoolPreparedStatements(haDataSource.isPoolPreparedStatements());
        dataSource.setSharePreparedStatements(haDataSource.isSharePreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(haDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        dataSource.setQueryTimeout(haDataSource.getQueryTimeout());
        dataSource.setTransactionQueryTimeout(haDataSource.getTransactionQueryTimeout());
        dataSource.setTimeBetweenEvictionRunsMillis(haDataSource.getTimeBetweenEvictionRunsMillis());
        dataSource.setMinEvictableIdleTimeMillis(haDataSource.getMinEvictableIdleTimeMillis());
        dataSource.setMaxEvictableIdleTimeMillis(haDataSource.getMaxEvictableIdleTimeMillis());
        dataSource.setPhyTimeoutMillis(haDataSource.getPhyTimeoutMillis());
        dataSource.setTimeBetweenConnectErrorMillis(haDataSource.getTimeBetweenConnectErrorMillis());
        dataSource.setRemoveAbandoned(haDataSource.isRemoveAbandoned());
        dataSource.setRemoveAbandonedTimeoutMillis(haDataSource.getRemoveAbandonedTimeoutMillis());
        dataSource.setLogAbandoned(haDataSource.isLogAbandoned());
        dataSource.setProxyFilters(haDataSource.getProxyFilters());
        dataSource.setFilters(haDataSource.getFilters());
        dataSource.setLogWriter(haDataSource.getLogWriter());
        dataSource.init();
        return dataSource;
    }
}
