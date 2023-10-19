// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari;

import org.slf4j.LoggerFactory;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import java.io.PrintWriter;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLException;
import java.sql.Connection;
import com.zaxxer.hikari.pool.HikariPool;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import java.io.Closeable;
import javax.sql.DataSource;

public class HikariDataSource extends HikariConfig implements DataSource, Closeable
{
    private static final Logger LOGGER;
    private final AtomicBoolean isShutdown;
    private final HikariPool fastPathPool;
    private volatile HikariPool pool;
    
    public HikariDataSource() {
        this.isShutdown = new AtomicBoolean();
        this.fastPathPool = null;
    }
    
    public HikariDataSource(final HikariConfig configuration) {
        this.isShutdown = new AtomicBoolean();
        configuration.validate();
        configuration.copyStateTo(this);
        this.seal();
        HikariDataSource.LOGGER.info("{} - Starting...", configuration.getPoolName());
        final HikariPool hikariPool = new HikariPool(this);
        this.fastPathPool = hikariPool;
        this.pool = hikariPool;
        HikariDataSource.LOGGER.info("{} - Start completed.", configuration.getPoolName());
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        if (this.isClosed()) {
            throw new SQLException("HikariDataSource " + this + " has been closed.");
        }
        if (this.fastPathPool != null) {
            return this.fastPathPool.getConnection();
        }
        HikariPool result = this.pool;
        if (result == null) {
            synchronized (this) {
                result = this.pool;
                if (result == null) {
                    this.validate();
                    HikariDataSource.LOGGER.info("{} - Starting...", this.getPoolName());
                    try {
                        result = (this.pool = new HikariPool(this));
                        this.seal();
                    }
                    catch (HikariPool.PoolInitializationException pie) {
                        if (pie.getCause() instanceof SQLException) {
                            throw (SQLException)pie.getCause();
                        }
                        throw pie;
                    }
                    HikariDataSource.LOGGER.info("{} - Start completed.", this.getPoolName());
                }
            }
        }
        return result.getConnection();
    }
    
    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        final HikariPool p = this.pool;
        return (p != null) ? p.getUnwrappedDataSource().getLogWriter() : null;
    }
    
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        final HikariPool p = this.pool;
        if (p != null) {
            p.getUnwrappedDataSource().setLogWriter(out);
        }
    }
    
    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        final HikariPool p = this.pool;
        if (p != null) {
            p.getUnwrappedDataSource().setLoginTimeout(seconds);
        }
    }
    
    @Override
    public int getLoginTimeout() throws SQLException {
        final HikariPool p = this.pool;
        return (p != null) ? p.getUnwrappedDataSource().getLoginTimeout() : 0;
    }
    
    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T)this;
        }
        final HikariPool p = this.pool;
        if (p != null) {
            final DataSource unwrappedDataSource = p.getUnwrappedDataSource();
            if (iface.isInstance(unwrappedDataSource)) {
                return (T)unwrappedDataSource;
            }
            if (unwrappedDataSource != null) {
                return unwrappedDataSource.unwrap(iface);
            }
        }
        throw new SQLException("Wrapped DataSource is not an instance of " + iface);
    }
    
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return true;
        }
        final HikariPool p = this.pool;
        if (p != null) {
            final DataSource unwrappedDataSource = p.getUnwrappedDataSource();
            if (iface.isInstance(unwrappedDataSource)) {
                return true;
            }
            if (unwrappedDataSource != null) {
                return unwrappedDataSource.isWrapperFor(iface);
            }
        }
        return false;
    }
    
    @Override
    public void setMetricRegistry(final Object metricRegistry) {
        final boolean isAlreadySet = this.getMetricRegistry() != null;
        super.setMetricRegistry(metricRegistry);
        final HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricRegistry can only be set one time");
            }
            p.setMetricRegistry(super.getMetricRegistry());
        }
    }
    
    @Override
    public void setMetricsTrackerFactory(final MetricsTrackerFactory metricsTrackerFactory) {
        final boolean isAlreadySet = this.getMetricsTrackerFactory() != null;
        super.setMetricsTrackerFactory(metricsTrackerFactory);
        final HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("MetricsTrackerFactory can only be set one time");
            }
            p.setMetricsTrackerFactory(super.getMetricsTrackerFactory());
        }
    }
    
    @Override
    public void setHealthCheckRegistry(final Object healthCheckRegistry) {
        final boolean isAlreadySet = this.getHealthCheckRegistry() != null;
        super.setHealthCheckRegistry(healthCheckRegistry);
        final HikariPool p = this.pool;
        if (p != null) {
            if (isAlreadySet) {
                throw new IllegalStateException("HealthCheckRegistry can only be set one time");
            }
            p.setHealthCheckRegistry(super.getHealthCheckRegistry());
        }
    }
    
    public HikariPoolMXBean getHikariPoolMXBean() {
        return this.pool;
    }
    
    public HikariConfigMXBean getHikariConfigMXBean() {
        return this;
    }
    
    public void evictConnection(final Connection connection) {
        final HikariPool p;
        if (!this.isClosed() && (p = this.pool) != null && connection.getClass().getName().startsWith("com.zaxxer.hikari")) {
            p.evictConnection(connection);
        }
    }
    
    @Deprecated
    public void suspendPool() {
        final HikariPool p;
        if (!this.isClosed() && (p = this.pool) != null) {
            p.suspendPool();
        }
    }
    
    @Deprecated
    public void resumePool() {
        final HikariPool p;
        if (!this.isClosed() && (p = this.pool) != null) {
            p.resumePool();
        }
    }
    
    @Override
    public void close() {
        if (this.isShutdown.getAndSet(true)) {
            return;
        }
        final HikariPool p = this.pool;
        if (p != null) {
            try {
                HikariDataSource.LOGGER.info("{} - Shutdown initiated...", this.getPoolName());
                p.shutdown();
                HikariDataSource.LOGGER.info("{} - Shutdown completed.", this.getPoolName());
            }
            catch (InterruptedException e) {
                HikariDataSource.LOGGER.warn("{} - Interrupted during closing", this.getPoolName(), e);
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public boolean isClosed() {
        return this.isShutdown.get();
    }
    
    @Deprecated
    public void shutdown() {
        HikariDataSource.LOGGER.warn("The shutdown() method has been deprecated, please use the close() method instead");
        this.close();
    }
    
    @Override
    public String toString() {
        return "HikariDataSource (" + this.pool + ")";
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(HikariDataSource.class);
    }
}
