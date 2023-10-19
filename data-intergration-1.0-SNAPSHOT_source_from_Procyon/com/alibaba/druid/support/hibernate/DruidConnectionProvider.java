// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.hibernate;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import java.util.Map;
import java.sql.SQLException;
import java.sql.Connection;
import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.service.spi.Stoppable;
import org.hibernate.service.spi.Configurable;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

public class DruidConnectionProvider implements ConnectionProvider, Configurable, Stoppable
{
    private static final long serialVersionUID = 1026193803901107651L;
    private DruidDataSource dataSource;
    
    public DruidConnectionProvider() {
        this.dataSource = new DruidDataSource();
    }
    
    public boolean isUnwrappableAs(final Class unwrapType) {
        return this.dataSource.isWrapperFor(unwrapType);
    }
    
    public <T> T unwrap(final Class<T> unwrapType) {
        return this.dataSource.unwrap(unwrapType);
    }
    
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
    
    public void closeConnection(final Connection conn) throws SQLException {
        conn.close();
    }
    
    public boolean supportsAggressiveRelease() {
        return false;
    }
    
    public void configure(final Map configurationValues) {
        try {
            DruidDataSourceFactory.config(this.dataSource, configurationValues);
        }
        catch (SQLException e) {
            throw new IllegalArgumentException("config error", e);
        }
    }
    
    public void stop() {
        this.dataSource.close();
    }
}
