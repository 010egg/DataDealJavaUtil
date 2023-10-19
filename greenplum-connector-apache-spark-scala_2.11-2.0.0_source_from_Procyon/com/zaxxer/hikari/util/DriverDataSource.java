// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.util;

import org.slf4j.LoggerFactory;
import java.sql.SQLFeatureNotSupportedException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Iterator;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Map;
import java.sql.Driver;
import java.util.Properties;
import org.slf4j.Logger;
import javax.sql.DataSource;

public final class DriverDataSource implements DataSource
{
    private static final Logger LOGGER;
    private final String jdbcUrl;
    private final Properties driverProperties;
    private Driver driver;
    
    public DriverDataSource(final String jdbcUrl, final String driverClassName, final Properties properties, final String username, final String password) {
        this.jdbcUrl = jdbcUrl;
        this.driverProperties = new Properties();
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            this.driverProperties.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        if (username != null) {
            this.driverProperties.put("user", this.driverProperties.getProperty("user", username));
        }
        if (password != null) {
            this.driverProperties.put("password", this.driverProperties.getProperty("password", password));
        }
        if (driverClassName != null) {
            final Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                final Driver d = drivers.nextElement();
                if (d.getClass().getName().equals(driverClassName)) {
                    this.driver = d;
                    break;
                }
            }
            if (this.driver == null) {
                DriverDataSource.LOGGER.warn("Registered driver with driverClassName={} was not found, trying direct instantiation.", driverClassName);
                Class<?> driverClass = null;
                final ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    if (threadContextClassLoader != null) {
                        try {
                            driverClass = threadContextClassLoader.loadClass(driverClassName);
                            DriverDataSource.LOGGER.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
                        }
                        catch (ClassNotFoundException e3) {
                            DriverDataSource.LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", driverClassName, threadContextClassLoader, this.getClass().getClassLoader());
                        }
                    }
                    if (driverClass == null) {
                        driverClass = this.getClass().getClassLoader().loadClass(driverClassName);
                        DriverDataSource.LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", driverClassName, this.getClass().getClassLoader());
                    }
                }
                catch (ClassNotFoundException e3) {
                    DriverDataSource.LOGGER.debug("Failed to load driver class {} from HikariConfig class classloader {}", driverClassName, this.getClass().getClassLoader());
                }
                if (driverClass != null) {
                    try {
                        this.driver = (Driver)driverClass.newInstance();
                    }
                    catch (Exception e) {
                        DriverDataSource.LOGGER.warn("Failed to create instance of driver class {}, trying jdbcUrl resolution", driverClassName, e);
                    }
                }
            }
        }
        try {
            if (this.driver == null) {
                this.driver = DriverManager.getDriver(jdbcUrl);
            }
            else if (!this.driver.acceptsURL(jdbcUrl)) {
                throw new RuntimeException("Driver " + driverClassName + " claims to not accept jdbcUrl, " + jdbcUrl);
            }
        }
        catch (SQLException e2) {
            throw new RuntimeException("Failed to get driver instance for jdbcUrl=" + jdbcUrl, e2);
        }
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.driver.connect(this.jdbcUrl, this.driverProperties);
    }
    
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        return this.getConnection();
    }
    
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public void setLogWriter(final PrintWriter logWriter) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }
    
    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }
    
    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.driver.getParentLogger();
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return false;
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(DriverDataSource.class);
    }
}
