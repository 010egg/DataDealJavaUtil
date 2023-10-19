// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.druid.VERSION;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import java.sql.DriverPropertyInfo;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.util.JdbcUtils;
import java.util.Iterator;
import com.alibaba.druid.proxy.jdbc.DataSourceProxyConfig;
import com.alibaba.druid.util.JMXUtils;
import com.alibaba.druid.stat.JdbcStatManager;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterManager;
import java.sql.Connection;
import java.util.Properties;
import java.sql.SQLException;
import javax.management.MBeanServer;
import com.alibaba.druid.support.logging.LogFactory;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.sql.DriverManager;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import com.alibaba.druid.proxy.jdbc.DataSourceProxyImpl;
import java.util.concurrent.ConcurrentMap;
import com.alibaba.druid.support.logging.Log;
import java.sql.Driver;

public class DruidDriver implements Driver, DruidDriverMBean
{
    private static Log LOG;
    private static final DruidDriver instance;
    private static final ConcurrentMap<String, DataSourceProxyImpl> proxyDataSources;
    private static final AtomicInteger dataSourceIdSeed;
    private static final AtomicInteger sqlStatIdSeed;
    public static final String DEFAULT_PREFIX = "jdbc:wrap-jdbc:";
    public static final String DRIVER_PREFIX = "driver=";
    public static final String PASSWORD_CALLBACK_PREFIX = "passwordCallback=";
    public static final String NAME_PREFIX = "name=";
    public static final String JMX_PREFIX = "jmx=";
    public static final String FILTERS_PREFIX = "filters=";
    private final AtomicLong connectCount;
    private String acceptPrefix;
    private int majorVersion;
    private int minorVersion;
    private static final String MBEAN_NAME = "com.alibaba.druid:type=DruidDriver";
    
    public static boolean registerDriver(final Driver driver) {
        try {
            DriverManager.registerDriver(driver);
            try {
                final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
                final ObjectName objectName = new ObjectName("com.alibaba.druid:type=DruidDriver");
                if (!mbeanServer.isRegistered(objectName)) {
                    mbeanServer.registerMBean(DruidDriver.instance, objectName);
                }
            }
            catch (Throwable ex) {
                if (DruidDriver.LOG == null) {
                    DruidDriver.LOG = LogFactory.getLog(DruidDriver.class);
                }
                DruidDriver.LOG.warn("register druid-driver mbean error", ex);
            }
            return true;
        }
        catch (Exception e) {
            if (DruidDriver.LOG == null) {
                DruidDriver.LOG = LogFactory.getLog(DruidDriver.class);
            }
            DruidDriver.LOG.error("registerDriver error", e);
            return false;
        }
    }
    
    public DruidDriver() {
        this.connectCount = new AtomicLong(0L);
        this.acceptPrefix = "jdbc:wrap-jdbc:";
        this.majorVersion = 4;
        this.minorVersion = 0;
    }
    
    public static DruidDriver getInstance() {
        return DruidDriver.instance;
    }
    
    public static int createDataSourceId() {
        return DruidDriver.dataSourceIdSeed.incrementAndGet();
    }
    
    public static int createSqlStatId() {
        return DruidDriver.sqlStatIdSeed.incrementAndGet();
    }
    
    @Override
    public boolean acceptsURL(final String url) throws SQLException {
        return url != null && url.startsWith(this.acceptPrefix);
    }
    
    @Override
    public Connection connect(final String url, final Properties info) throws SQLException {
        if (!this.acceptsURL(url)) {
            return null;
        }
        this.connectCount.incrementAndGet();
        final DataSourceProxyImpl dataSource = this.getDataSource(url, info);
        return dataSource.connect(info);
    }
    
    private DataSourceProxyImpl getDataSource(final String url, final Properties info) throws SQLException {
        DataSourceProxyImpl dataSource = DruidDriver.proxyDataSources.get(url);
        if (dataSource == null) {
            final DataSourceProxyConfig config = parseConfig(url, info);
            final Driver rawDriver = this.createDriver(config.getRawDriverClassName());
            final DataSourceProxyImpl newDataSource = new DataSourceProxyImpl(rawDriver, config);
            final String property = System.getProperty("druid.filters");
            if (property != null && property.length() > 0) {
                for (final String filterItem : property.split(",")) {
                    FilterManager.loadFilter(config.getFilters(), filterItem);
                }
            }
            final int dataSourceId = createDataSourceId();
            newDataSource.setId(dataSourceId);
            for (final Filter filter : config.getFilters()) {
                filter.init(newDataSource);
            }
            final DataSourceProxy oldDataSource = DruidDriver.proxyDataSources.putIfAbsent(url, newDataSource);
            if (oldDataSource == null && config.isJmxOption()) {
                JMXUtils.register("com.alibaba.druid:type=JdbcStat", JdbcStatManager.getInstance());
            }
            dataSource = DruidDriver.proxyDataSources.get(url);
        }
        return dataSource;
    }
    
    public static DataSourceProxyConfig parseConfig(final String url, final Properties info) throws SQLException {
        String restUrl = url.substring("jdbc:wrap-jdbc:".length());
        final DataSourceProxyConfig config = new DataSourceProxyConfig();
        if (restUrl.startsWith("driver=")) {
            final int pos = restUrl.indexOf(58, "driver=".length());
            final String driverText = restUrl.substring("driver=".length(), pos);
            if (driverText.length() > 0) {
                config.setRawDriverClassName(driverText.trim());
            }
            restUrl = restUrl.substring(pos + 1);
        }
        if (restUrl.startsWith("filters=")) {
            final int pos = restUrl.indexOf(58, "filters=".length());
            final String filtersText = restUrl.substring("filters=".length(), pos);
            for (final String filterItem : filtersText.split(",")) {
                FilterManager.loadFilter(config.getFilters(), filterItem);
            }
            restUrl = restUrl.substring(pos + 1);
        }
        if (restUrl.startsWith("name=")) {
            final int pos = restUrl.indexOf(58, "name=".length());
            final String name = restUrl.substring("name=".length(), pos);
            config.setName(name);
            restUrl = restUrl.substring(pos + 1);
        }
        if (restUrl.startsWith("jmx=")) {
            final int pos = restUrl.indexOf(58, "jmx=".length());
            final String jmxOption = restUrl.substring("jmx=".length(), pos);
            config.setJmxOption(jmxOption);
            restUrl = restUrl.substring(pos + 1);
        }
        final String rawUrl = restUrl;
        config.setRawUrl(rawUrl);
        if (config.getRawDriverClassName() == null) {
            final String rawDriverClassname = JdbcUtils.getDriverClassName(rawUrl);
            config.setRawDriverClassName(rawDriverClassname);
        }
        config.setUrl(url);
        return config;
    }
    
    public Driver createDriver(final String className) throws SQLException {
        final Class<?> rawDriverClass = Utils.loadClass(className);
        if (rawDriverClass == null) {
            throw new SQLException("jdbc-driver's class not found. '" + className + "'");
        }
        Driver rawDriver;
        try {
            rawDriver = (Driver)rawDriverClass.newInstance();
        }
        catch (InstantiationException e) {
            throw new SQLException("create driver instance error, driver className '" + className + "'", e);
        }
        catch (IllegalAccessException e2) {
            throw new SQLException("create driver instance error, driver className '" + className + "'", e2);
        }
        return rawDriver;
    }
    
    @Override
    public int getMajorVersion() {
        return this.majorVersion;
    }
    
    @Override
    public int getMinorVersion() {
        return this.minorVersion;
    }
    
    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
        final DataSourceProxyImpl dataSource = this.getDataSource(url, info);
        return dataSource.getRawDriver().getPropertyInfo(dataSource.getConfig().getRawUrl(), info);
    }
    
    @Override
    public boolean jdbcCompliant() {
        return true;
    }
    
    @Override
    public long getConnectCount() {
        return this.connectCount.get();
    }
    
    @Override
    public String getAcceptPrefix() {
        return this.acceptPrefix;
    }
    
    @Override
    public String[] getDataSourceUrls() {
        return DruidDriver.proxyDataSources.keySet().toArray(new String[DruidDriver.proxyDataSources.size()]);
    }
    
    public static ConcurrentMap<String, DataSourceProxyImpl> getProxyDataSources() {
        return DruidDriver.proxyDataSources;
    }
    
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
    
    @Override
    public void resetStat() {
        this.connectCount.set(0L);
    }
    
    @Override
    public String getDruidVersion() {
        return VERSION.getVersionNumber();
    }
    
    static {
        instance = new DruidDriver();
        proxyDataSources = new ConcurrentHashMap<String, DataSourceProxyImpl>(16, 0.75f, 1);
        dataSourceIdSeed = new AtomicInteger(0);
        sqlStatIdSeed = new AtomicInteger(0);
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                DruidDriver.registerDriver(DruidDriver.instance);
                return null;
            }
        });
    }
}
