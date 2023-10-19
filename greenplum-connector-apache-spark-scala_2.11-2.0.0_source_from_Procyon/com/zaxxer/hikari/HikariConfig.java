// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari;

import org.slf4j.LoggerFactory;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.security.AccessControlException;
import java.util.concurrent.ThreadLocalRandom;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import com.zaxxer.hikari.util.UtilityElf;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.util.Map;
import com.zaxxer.hikari.util.PropertyElf;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;

public class HikariConfig implements HikariConfigMXBean
{
    private static final Logger LOGGER;
    private static final char[] ID_CHARACTERS;
    private static final long CONNECTION_TIMEOUT;
    private static final long VALIDATION_TIMEOUT;
    private static final long IDLE_TIMEOUT;
    private static final long MAX_LIFETIME;
    private static final int DEFAULT_POOL_SIZE = 10;
    private static boolean unitTest;
    private volatile long connectionTimeout;
    private volatile long validationTimeout;
    private volatile long idleTimeout;
    private volatile long leakDetectionThreshold;
    private volatile long maxLifetime;
    private volatile int maxPoolSize;
    private volatile int minIdle;
    private volatile String username;
    private volatile String password;
    private long initializationFailTimeout;
    private String catalog;
    private String connectionInitSql;
    private String connectionTestQuery;
    private String dataSourceClassName;
    private String dataSourceJndiName;
    private String driverClassName;
    private String jdbcUrl;
    private String poolName;
    private String schema;
    private String transactionIsolationName;
    private boolean isAutoCommit;
    private boolean isReadOnly;
    private boolean isIsolateInternalQueries;
    private boolean isRegisterMbeans;
    private boolean isAllowPoolSuspension;
    private DataSource dataSource;
    private Properties dataSourceProperties;
    private ThreadFactory threadFactory;
    private ScheduledExecutorService scheduledExecutor;
    private MetricsTrackerFactory metricsTrackerFactory;
    private Object metricRegistry;
    private Object healthCheckRegistry;
    private Properties healthCheckProperties;
    private volatile boolean sealed;
    
    public HikariConfig() {
        this.dataSourceProperties = new Properties();
        this.healthCheckProperties = new Properties();
        this.minIdle = -1;
        this.maxPoolSize = -1;
        this.maxLifetime = HikariConfig.MAX_LIFETIME;
        this.connectionTimeout = HikariConfig.CONNECTION_TIMEOUT;
        this.validationTimeout = HikariConfig.VALIDATION_TIMEOUT;
        this.idleTimeout = HikariConfig.IDLE_TIMEOUT;
        this.initializationFailTimeout = 1L;
        this.isAutoCommit = true;
        final String systemProp = System.getProperty("hikaricp.configurationFile");
        if (systemProp != null) {
            this.loadProperties(systemProp);
        }
    }
    
    public HikariConfig(final Properties properties) {
        this();
        PropertyElf.setTargetFromProperties(this, properties);
    }
    
    public HikariConfig(final String propertyFileName) {
        this();
        this.loadProperties(propertyFileName);
    }
    
    @Override
    public long getConnectionTimeout() {
        return this.connectionTimeout;
    }
    
    @Override
    public void setConnectionTimeout(final long connectionTimeoutMs) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        if (connectionTimeoutMs == 0L) {
            this.connectionTimeout = 2147483647L;
        }
        else {
            if (connectionTimeoutMs < 250L) {
                throw new IllegalArgumentException("connectionTimeout cannot be less than 250ms");
            }
            this.connectionTimeout = connectionTimeoutMs;
        }
    }
    
    @Override
    public long getIdleTimeout() {
        return this.idleTimeout;
    }
    
    @Override
    public void setIdleTimeout(final long idleTimeoutMs) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        if (idleTimeoutMs < 0L) {
            throw new IllegalArgumentException("idleTimeout cannot be negative");
        }
        this.idleTimeout = idleTimeoutMs;
    }
    
    @Override
    public long getLeakDetectionThreshold() {
        return this.leakDetectionThreshold;
    }
    
    @Override
    public void setLeakDetectionThreshold(final long leakDetectionThresholdMs) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.leakDetectionThreshold = leakDetectionThresholdMs;
    }
    
    @Override
    public long getMaxLifetime() {
        return this.maxLifetime;
    }
    
    @Override
    public void setMaxLifetime(final long maxLifetimeMs) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.maxLifetime = maxLifetimeMs;
    }
    
    @Override
    public int getMaximumPoolSize() {
        return this.maxPoolSize;
    }
    
    @Override
    public void setMaximumPoolSize(final int maxPoolSize) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        if (maxPoolSize < 1) {
            throw new IllegalArgumentException("maxPoolSize cannot be less than 1");
        }
        this.maxPoolSize = maxPoolSize;
    }
    
    @Override
    public int getMinimumIdle() {
        return this.minIdle;
    }
    
    @Override
    public void setMinimumIdle(final int minIdle) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        if (minIdle < 0) {
            throw new IllegalArgumentException("minimumIdle cannot be negative");
        }
        this.minIdle = minIdle;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public void setPassword(final String password) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.password = password;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    @Override
    public void setUsername(final String username) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.username = username;
    }
    
    @Override
    public long getValidationTimeout() {
        return this.validationTimeout;
    }
    
    @Override
    public void setValidationTimeout(final long validationTimeoutMs) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        if (validationTimeoutMs < 250L) {
            throw new IllegalArgumentException("validationTimeout cannot be less than 250ms");
        }
        this.validationTimeout = validationTimeoutMs;
    }
    
    public String getCatalog() {
        return this.catalog;
    }
    
    public void setCatalog(final String catalog) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.catalog = catalog;
    }
    
    public String getConnectionTestQuery() {
        return this.connectionTestQuery;
    }
    
    public void setConnectionTestQuery(final String connectionTestQuery) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.connectionTestQuery = connectionTestQuery;
    }
    
    public String getConnectionInitSql() {
        return this.connectionInitSql;
    }
    
    public void setConnectionInitSql(final String connectionInitSql) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.connectionInitSql = connectionInitSql;
    }
    
    public DataSource getDataSource() {
        return this.dataSource;
    }
    
    public void setDataSource(final DataSource dataSource) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.dataSource = dataSource;
    }
    
    public String getDataSourceClassName() {
        return this.dataSourceClassName;
    }
    
    public void setDataSourceClassName(final String className) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.dataSourceClassName = className;
    }
    
    public void addDataSourceProperty(final String propertyName, final Object value) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.dataSourceProperties.put(propertyName, value);
    }
    
    public String getDataSourceJNDI() {
        return this.dataSourceJndiName;
    }
    
    public void setDataSourceJNDI(final String jndiDataSource) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.dataSourceJndiName = jndiDataSource;
    }
    
    public Properties getDataSourceProperties() {
        return this.dataSourceProperties;
    }
    
    public void setDataSourceProperties(final Properties dsProperties) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.dataSourceProperties.putAll(dsProperties);
    }
    
    public String getDriverClassName() {
        return this.driverClassName;
    }
    
    public void setDriverClassName(final String driverClassName) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        Class<?> driverClass = null;
        final ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (threadContextClassLoader != null) {
                try {
                    driverClass = threadContextClassLoader.loadClass(driverClassName);
                    HikariConfig.LOGGER.debug("Driver class {} found in Thread context class loader {}", driverClassName, threadContextClassLoader);
                }
                catch (ClassNotFoundException e2) {
                    HikariConfig.LOGGER.debug("Driver class {} not found in Thread context class loader {}, trying classloader {}", driverClassName, threadContextClassLoader, this.getClass().getClassLoader());
                }
            }
            if (driverClass == null) {
                driverClass = this.getClass().getClassLoader().loadClass(driverClassName);
                HikariConfig.LOGGER.debug("Driver class {} found in the HikariConfig class classloader {}", driverClassName, this.getClass().getClassLoader());
            }
        }
        catch (ClassNotFoundException e2) {
            HikariConfig.LOGGER.error("Failed to load driver class {} from HikariConfig class classloader {}", driverClassName, this.getClass().getClassLoader());
        }
        if (driverClass == null) {
            throw new RuntimeException("Failed to load driver class " + driverClassName + " in either of HikariConfig class loader or Thread context classloader");
        }
        try {
            driverClass.newInstance();
            this.driverClassName = driverClassName;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to instantiate class " + driverClassName, e);
        }
    }
    
    public String getJdbcUrl() {
        return this.jdbcUrl;
    }
    
    public void setJdbcUrl(final String jdbcUrl) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.jdbcUrl = jdbcUrl;
    }
    
    public boolean isAutoCommit() {
        return this.isAutoCommit;
    }
    
    public void setAutoCommit(final boolean isAutoCommit) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.isAutoCommit = isAutoCommit;
    }
    
    public boolean isAllowPoolSuspension() {
        return this.isAllowPoolSuspension;
    }
    
    public void setAllowPoolSuspension(final boolean isAllowPoolSuspension) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.isAllowPoolSuspension = isAllowPoolSuspension;
    }
    
    public long getInitializationFailTimeout() {
        return this.initializationFailTimeout;
    }
    
    public void setInitializationFailTimeout(final long initializationFailTimeout) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.initializationFailTimeout = initializationFailTimeout;
    }
    
    @Deprecated
    public boolean isInitializationFailFast() {
        return this.initializationFailTimeout > 0L;
    }
    
    @Deprecated
    public void setInitializationFailFast(final boolean failFast) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        HikariConfig.LOGGER.warn("The initializationFailFast propery is deprecated, see initializationFailTimeout");
        this.initializationFailTimeout = (failFast ? 1 : -1);
    }
    
    public boolean isIsolateInternalQueries() {
        return this.isIsolateInternalQueries;
    }
    
    public void setIsolateInternalQueries(final boolean isolate) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.isIsolateInternalQueries = isolate;
    }
    
    @Deprecated
    public boolean isJdbc4ConnectionTest() {
        return false;
    }
    
    @Deprecated
    public void setJdbc4ConnectionTest(final boolean useIsValid) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        HikariConfig.LOGGER.warn("The jdbcConnectionTest property is now deprecated, see the documentation for connectionTestQuery");
    }
    
    public MetricsTrackerFactory getMetricsTrackerFactory() {
        return this.metricsTrackerFactory;
    }
    
    public void setMetricsTrackerFactory(final MetricsTrackerFactory metricsTrackerFactory) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        if (this.metricRegistry != null) {
            throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
        }
        this.metricsTrackerFactory = metricsTrackerFactory;
    }
    
    public Object getMetricRegistry() {
        return this.metricRegistry;
    }
    
    public void setMetricRegistry(Object metricRegistry) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        if (this.metricsTrackerFactory != null) {
            throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
        }
        if (metricRegistry != null) {
            metricRegistry = this.getObjectOrPerformJndiLookup(metricRegistry);
            if (!metricRegistry.getClass().getName().contains("MetricRegistry") && !metricRegistry.getClass().getName().contains("MeterRegistry")) {
                throw new IllegalArgumentException("Class must be instance of com.codahale.metrics.MetricRegistry or io.micrometer.core.instrument.MeterRegistry");
            }
        }
        this.metricRegistry = metricRegistry;
    }
    
    public Object getHealthCheckRegistry() {
        return this.healthCheckRegistry;
    }
    
    public void setHealthCheckRegistry(Object healthCheckRegistry) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        if (healthCheckRegistry != null) {
            healthCheckRegistry = this.getObjectOrPerformJndiLookup(healthCheckRegistry);
            if (!(healthCheckRegistry instanceof HealthCheckRegistry)) {
                throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.health.HealthCheckRegistry");
            }
        }
        this.healthCheckRegistry = healthCheckRegistry;
    }
    
    public Properties getHealthCheckProperties() {
        return this.healthCheckProperties;
    }
    
    public void setHealthCheckProperties(final Properties healthCheckProperties) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.healthCheckProperties.putAll(healthCheckProperties);
    }
    
    public void addHealthCheckProperty(final String key, final String value) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.healthCheckProperties.setProperty(key, value);
    }
    
    public boolean isReadOnly() {
        return this.isReadOnly;
    }
    
    public void setReadOnly(final boolean readOnly) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.isReadOnly = readOnly;
    }
    
    public boolean isRegisterMbeans() {
        return this.isRegisterMbeans;
    }
    
    public void setRegisterMbeans(final boolean register) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.isRegisterMbeans = register;
    }
    
    @Override
    public String getPoolName() {
        return this.poolName;
    }
    
    public void setPoolName(final String poolName) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.poolName = poolName;
    }
    
    @Deprecated
    public ScheduledThreadPoolExecutor getScheduledExecutorService() {
        return (ScheduledThreadPoolExecutor)this.scheduledExecutor;
    }
    
    @Deprecated
    public void setScheduledExecutorService(final ScheduledThreadPoolExecutor executor) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.scheduledExecutor = executor;
    }
    
    public ScheduledExecutorService getScheduledExecutor() {
        return this.scheduledExecutor;
    }
    
    public void setScheduledExecutor(final ScheduledExecutorService executor) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.scheduledExecutor = executor;
    }
    
    public String getTransactionIsolation() {
        return this.transactionIsolationName;
    }
    
    public String getSchema() {
        return this.schema;
    }
    
    public void setSchema(final String schema) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.schema = schema;
    }
    
    public void setTransactionIsolation(final String isolationLevel) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.transactionIsolationName = isolationLevel;
    }
    
    public ThreadFactory getThreadFactory() {
        return this.threadFactory;
    }
    
    public void setThreadFactory(final ThreadFactory threadFactory) {
        if (this.sealed) {
            throw new IllegalStateException("The configuration of the pool is sealed once started.  Use HikariConfigMXBean for runtime changes.");
        }
        this.threadFactory = threadFactory;
    }
    
    protected void seal() {
        this.sealed = true;
    }
    
    @Deprecated
    public void copyState(final HikariConfig other) {
        this.copyStateTo(other);
    }
    
    public void copyStateTo(final HikariConfig other) {
        for (final Field field : HikariConfig.class.getDeclaredFields()) {
            if (!Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    field.set(other, field.get(this));
                }
                catch (Exception e) {
                    throw new RuntimeException("Failed to copy HikariConfig state: " + e.getMessage(), e);
                }
            }
        }
        other.sealed = false;
    }
    
    public void validate() {
        if (this.poolName == null) {
            this.poolName = this.generatePoolName();
        }
        else if (this.isRegisterMbeans && this.poolName.contains(":")) {
            throw new IllegalArgumentException("poolName cannot contain ':' when used with JMX");
        }
        this.catalog = UtilityElf.getNullIfEmpty(this.catalog);
        this.connectionInitSql = UtilityElf.getNullIfEmpty(this.connectionInitSql);
        this.connectionTestQuery = UtilityElf.getNullIfEmpty(this.connectionTestQuery);
        this.transactionIsolationName = UtilityElf.getNullIfEmpty(this.transactionIsolationName);
        this.dataSourceClassName = UtilityElf.getNullIfEmpty(this.dataSourceClassName);
        this.dataSourceJndiName = UtilityElf.getNullIfEmpty(this.dataSourceJndiName);
        this.driverClassName = UtilityElf.getNullIfEmpty(this.driverClassName);
        this.jdbcUrl = UtilityElf.getNullIfEmpty(this.jdbcUrl);
        if (this.dataSource != null) {
            if (this.dataSourceClassName != null) {
                HikariConfig.LOGGER.warn("{} - using dataSource and ignoring dataSourceClassName.", this.poolName);
            }
        }
        else if (this.dataSourceClassName != null) {
            if (this.driverClassName != null) {
                HikariConfig.LOGGER.error("{} - cannot use driverClassName and dataSourceClassName together.", this.poolName);
                throw new IllegalStateException("cannot use driverClassName and dataSourceClassName together.");
            }
            if (this.jdbcUrl != null) {
                HikariConfig.LOGGER.warn("{} - using dataSourceClassName and ignoring jdbcUrl.", this.poolName);
            }
        }
        else if (this.jdbcUrl == null) {
            if (this.dataSourceJndiName == null) {
                if (this.driverClassName != null) {
                    HikariConfig.LOGGER.error("{} - jdbcUrl is required with driverClassName.", this.poolName);
                    throw new IllegalArgumentException("jdbcUrl is required with driverClassName.");
                }
                HikariConfig.LOGGER.error("{} - dataSource or dataSourceClassName or jdbcUrl is required.", this.poolName);
                throw new IllegalArgumentException("dataSource or dataSourceClassName or jdbcUrl is required.");
            }
        }
        this.validateNumerics();
        if (HikariConfig.LOGGER.isDebugEnabled() || HikariConfig.unitTest) {
            this.logConfiguration();
        }
    }
    
    private void validateNumerics() {
        if (this.maxLifetime != 0L && this.maxLifetime < TimeUnit.SECONDS.toMillis(30L)) {
            HikariConfig.LOGGER.warn("{} - maxLifetime is less than 30000ms, setting to default {}ms.", this.poolName, HikariConfig.MAX_LIFETIME);
            this.maxLifetime = HikariConfig.MAX_LIFETIME;
        }
        if (this.idleTimeout + TimeUnit.SECONDS.toMillis(1L) > this.maxLifetime && this.maxLifetime > 0L) {
            HikariConfig.LOGGER.warn("{} - idleTimeout is close to or more than maxLifetime, disabling it.", this.poolName);
            this.idleTimeout = 0L;
        }
        if (this.idleTimeout != 0L && this.idleTimeout < TimeUnit.SECONDS.toMillis(10L)) {
            HikariConfig.LOGGER.warn("{} - idleTimeout is less than 10000ms, setting to default {}ms.", this.poolName, HikariConfig.IDLE_TIMEOUT);
            this.idleTimeout = HikariConfig.IDLE_TIMEOUT;
        }
        if (this.leakDetectionThreshold > 0L && !HikariConfig.unitTest && (this.leakDetectionThreshold < TimeUnit.SECONDS.toMillis(2L) || (this.leakDetectionThreshold > this.maxLifetime && this.maxLifetime > 0L))) {
            HikariConfig.LOGGER.warn("{} - leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it.", this.poolName);
            this.leakDetectionThreshold = 0L;
        }
        if (this.connectionTimeout < 250L) {
            HikariConfig.LOGGER.warn("{} - connectionTimeout is less than 250ms, setting to {}ms.", this.poolName, HikariConfig.CONNECTION_TIMEOUT);
            this.connectionTimeout = HikariConfig.CONNECTION_TIMEOUT;
        }
        if (this.validationTimeout < 250L) {
            HikariConfig.LOGGER.warn("{} - validationTimeout is less than 250ms, setting to {}ms.", this.poolName, HikariConfig.VALIDATION_TIMEOUT);
            this.validationTimeout = HikariConfig.VALIDATION_TIMEOUT;
        }
        if (this.maxPoolSize < 1) {
            this.maxPoolSize = ((this.minIdle <= 0) ? 10 : this.minIdle);
        }
        if (this.minIdle < 0 || this.minIdle > this.maxPoolSize) {
            this.minIdle = this.maxPoolSize;
        }
    }
    
    private void logConfiguration() {
        HikariConfig.LOGGER.debug("{} - configuration:", this.poolName);
        final Set<String> propertyNames = new TreeSet<String>(PropertyElf.getPropertyNames(HikariConfig.class));
        for (final String prop : propertyNames) {
            try {
                Object value = PropertyElf.getProperty(prop, this);
                if ("dataSourceProperties".equals(prop)) {
                    final Properties dsProps = PropertyElf.copyProperties(this.dataSourceProperties);
                    dsProps.setProperty("password", "<masked>");
                    value = dsProps;
                }
                if ("initializationFailTimeout".equals(prop) && this.initializationFailTimeout == Long.MAX_VALUE) {
                    value = "infinite";
                }
                else if ("transactionIsolation".equals(prop) && this.transactionIsolationName == null) {
                    value = "default";
                }
                else if (prop.matches("scheduledExecutorService|threadFactory") && value == null) {
                    value = "internal";
                }
                else if (prop.contains("jdbcUrl") && value instanceof String) {
                    value = ((String)value).replaceAll("([?&;]password=)[^&#;]*(.*)", "$1<masked>$2");
                }
                else if (prop.contains("password")) {
                    value = "<masked>";
                }
                else if (value instanceof String) {
                    value = "\"" + value + "\"";
                }
                else if (value == null) {
                    value = "none";
                }
                HikariConfig.LOGGER.debug((prop + "................................................").substring(0, 32) + value);
            }
            catch (Exception ex) {}
        }
    }
    
    private void loadProperties(final String propertyFileName) {
        final File propFile = new File(propertyFileName);
        try (final InputStream is = propFile.isFile() ? new FileInputStream(propFile) : this.getClass().getResourceAsStream(propertyFileName)) {
            if (is == null) {
                throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
            }
            final Properties props = new Properties();
            props.load(is);
            PropertyElf.setTargetFromProperties(this, props);
        }
        catch (IOException io) {
            throw new RuntimeException("Failed to read property file", io);
        }
    }
    
    private String generatePoolName() {
        final String prefix = "HikariPool-";
        try {
            synchronized (System.getProperties()) {
                final String next = String.valueOf(Integer.getInteger("com.zaxxer.hikari.pool_number", 0) + 1);
                System.setProperty("com.zaxxer.hikari.pool_number", next);
                return "HikariPool-" + next;
            }
        }
        catch (AccessControlException e) {
            final ThreadLocalRandom random = ThreadLocalRandom.current();
            final StringBuilder buf = new StringBuilder("HikariPool-");
            for (int i = 0; i < 4; ++i) {
                buf.append(HikariConfig.ID_CHARACTERS[random.nextInt(62)]);
            }
            HikariConfig.LOGGER.info("assigned random pool name '{}' (security manager prevented access to system properties)", buf);
            return buf.toString();
        }
    }
    
    private Object getObjectOrPerformJndiLookup(final Object object) {
        if (object instanceof String) {
            try {
                final InitialContext initCtx = new InitialContext();
                return initCtx.lookup((String)object);
            }
            catch (NamingException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return object;
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(HikariConfig.class);
        ID_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30L);
        VALIDATION_TIMEOUT = TimeUnit.SECONDS.toMillis(5L);
        IDLE_TIMEOUT = TimeUnit.MINUTES.toMillis(10L);
        MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
        HikariConfig.unitTest = false;
    }
}
