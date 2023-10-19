// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.clickhouse;

import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Properties;
import ru.yandex.clickhouse.BalancedClickhouseDataSource;
import java.sql.Driver;

public class BalancedClickhouseDriver implements Driver
{
    private final String url;
    private BalancedClickhouseDataSource dataSource;
    
    public BalancedClickhouseDriver(final String url, final Properties properties) {
        this.url = url;
        this.dataSource = new BalancedClickhouseDataSource(url, properties);
    }
    
    @Override
    public Connection connect(final String url, final Properties info) throws SQLException {
        if (!this.acceptsURL(url)) {
            throw new SQLException("TODO");
        }
        return (Connection)this.dataSource.getConnection();
    }
    
    @Override
    public boolean acceptsURL(final String url) throws SQLException {
        return this.url.equals(url);
    }
    
    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }
    
    @Override
    public int getMajorVersion() {
        return 0;
    }
    
    @Override
    public int getMinorVersion() {
        return 0;
    }
    
    @Override
    public boolean jdbcCompliant() {
        return false;
    }
    
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
