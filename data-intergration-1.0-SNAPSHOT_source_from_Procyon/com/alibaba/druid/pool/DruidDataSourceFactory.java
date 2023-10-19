// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import com.alibaba.druid.support.logging.LogFactory;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Map;
import javax.sql.DataSource;
import javax.naming.RefAddr;
import java.util.Properties;
import javax.naming.Reference;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import com.alibaba.druid.support.logging.Log;
import javax.naming.spi.ObjectFactory;

public class DruidDataSourceFactory implements ObjectFactory
{
    private static final Log LOG;
    static final int UNKNOWN_TRANSACTIONISOLATION = -1;
    public static final String PROP_DEFAULTAUTOCOMMIT = "defaultAutoCommit";
    public static final String PROP_DEFAULTREADONLY = "defaultReadOnly";
    public static final String PROP_DEFAULTTRANSACTIONISOLATION = "defaultTransactionIsolation";
    public static final String PROP_DEFAULTCATALOG = "defaultCatalog";
    public static final String PROP_DRIVERCLASSNAME = "driverClassName";
    public static final String PROP_MAXACTIVE = "maxActive";
    public static final String PROP_MAXIDLE = "maxIdle";
    public static final String PROP_MINIDLE = "minIdle";
    public static final String PROP_INITIALSIZE = "initialSize";
    public static final String PROP_MAXWAIT = "maxWait";
    public static final String PROP_TESTONBORROW = "testOnBorrow";
    public static final String PROP_TESTONRETURN = "testOnReturn";
    public static final String PROP_TIMEBETWEENEVICTIONRUNSMILLIS = "timeBetweenEvictionRunsMillis";
    public static final String PROP_NUMTESTSPEREVICTIONRUN = "numTestsPerEvictionRun";
    public static final String PROP_MINEVICTABLEIDLETIMEMILLIS = "minEvictableIdleTimeMillis";
    public static final String PROP_PHY_TIMEOUT_MILLIS = "phyTimeoutMillis";
    public static final String PROP_TESTWHILEIDLE = "testWhileIdle";
    public static final String PROP_PASSWORD = "password";
    public static final String PROP_URL = "url";
    public static final String PROP_USERNAME = "username";
    public static final String PROP_VALIDATIONQUERY = "validationQuery";
    public static final String PROP_VALIDATIONQUERY_TIMEOUT = "validationQueryTimeout";
    public static final String PROP_INITCONNECTIONSQLS = "initConnectionSqls";
    public static final String PROP_ACCESSTOUNDERLYINGCONNECTIONALLOWED = "accessToUnderlyingConnectionAllowed";
    public static final String PROP_REMOVEABANDONED = "removeAbandoned";
    public static final String PROP_REMOVEABANDONEDTIMEOUT = "removeAbandonedTimeout";
    public static final String PROP_LOGABANDONED = "logAbandoned";
    public static final String PROP_POOLPREPAREDSTATEMENTS = "poolPreparedStatements";
    public static final String PROP_MAXOPENPREPAREDSTATEMENTS = "maxOpenPreparedStatements";
    public static final String PROP_CONNECTIONPROPERTIES = "connectionProperties";
    public static final String PROP_FILTERS = "filters";
    public static final String PROP_EXCEPTION_SORTER = "exceptionSorter";
    public static final String PROP_EXCEPTION_SORTER_CLASS_NAME = "exception-sorter-class-name";
    public static final String PROP_NAME = "name";
    public static final String PROP_INIT = "init";
    private static final String[] ALL_PROPERTIES;
    
    @Override
    public Object getObjectInstance(final Object obj, final Name name, final Context nameCtx, final Hashtable<?, ?> environment) throws Exception {
        if (obj == null || !(obj instanceof Reference)) {
            return null;
        }
        final Reference ref = (Reference)obj;
        if (!"javax.sql.DataSource".equals(ref.getClassName()) && !"com.alibaba.druid.pool.DruidDataSource".equals(ref.getClassName())) {
            return null;
        }
        final Properties properties = new Properties();
        for (int i = 0; i < DruidDataSourceFactory.ALL_PROPERTIES.length; ++i) {
            final String propertyName = DruidDataSourceFactory.ALL_PROPERTIES[i];
            final RefAddr ra = ref.get(propertyName);
            if (ra != null) {
                final String propertyValue = ra.getContent().toString();
                properties.setProperty(propertyName, propertyValue);
            }
        }
        return this.createDataSourceInternal(properties);
    }
    
    protected DataSource createDataSourceInternal(final Properties properties) throws Exception {
        final DruidDataSource dataSource = new DruidDataSource();
        config(dataSource, properties);
        return dataSource;
    }
    
    public static DataSource createDataSource(final Properties properties) throws Exception {
        return createDataSource((Map)properties);
    }
    
    public static DataSource createDataSource(final Map properties) throws Exception {
        final DruidDataSource dataSource = new DruidDataSource();
        config(dataSource, properties);
        return dataSource;
    }
    
    public static void config(final DruidDataSource dataSource, final Map<?, ?> properties) throws SQLException {
        String value = null;
        value = (String)properties.get("defaultAutoCommit");
        if (value != null) {
            dataSource.setDefaultAutoCommit(Boolean.valueOf(value));
        }
        value = (String)properties.get("defaultReadOnly");
        if (value != null) {
            dataSource.setDefaultReadOnly((boolean)Boolean.valueOf(value));
        }
        value = (String)properties.get("defaultTransactionIsolation");
        if (value != null) {
            int level = -1;
            if ("NONE".equalsIgnoreCase(value)) {
                level = 0;
            }
            else if ("READ_COMMITTED".equalsIgnoreCase(value)) {
                level = 2;
            }
            else if ("READ_UNCOMMITTED".equalsIgnoreCase(value)) {
                level = 1;
            }
            else if ("REPEATABLE_READ".equalsIgnoreCase(value)) {
                level = 4;
            }
            else if ("SERIALIZABLE".equalsIgnoreCase(value)) {
                level = 8;
            }
            else {
                try {
                    level = Integer.parseInt(value);
                }
                catch (NumberFormatException e) {
                    DruidDataSourceFactory.LOG.error("Could not parse defaultTransactionIsolation: " + value);
                    DruidDataSourceFactory.LOG.error("WARNING: defaultTransactionIsolation not set");
                    DruidDataSourceFactory.LOG.error("using default value of database driver");
                    level = -1;
                }
            }
            dataSource.setDefaultTransactionIsolation(level);
        }
        value = (String)properties.get("defaultCatalog");
        if (value != null) {
            dataSource.setDefaultCatalog(value);
        }
        value = (String)properties.get("driverClassName");
        if (value != null) {
            dataSource.setDriverClassName(value);
        }
        value = (String)properties.get("maxActive");
        if (value != null) {
            dataSource.setMaxActive(Integer.parseInt(value));
        }
        value = (String)properties.get("maxIdle");
        if (value != null) {
            dataSource.setMaxIdle(Integer.parseInt(value));
        }
        value = (String)properties.get("minIdle");
        if (value != null) {
            dataSource.setMinIdle(Integer.parseInt(value));
        }
        value = (String)properties.get("initialSize");
        if (value != null) {
            dataSource.setInitialSize(Integer.parseInt(value));
        }
        value = (String)properties.get("maxWait");
        if (value != null) {
            dataSource.setMaxWait(Long.parseLong(value));
        }
        value = (String)properties.get("testOnBorrow");
        if (value != null) {
            dataSource.setTestOnBorrow(Boolean.valueOf(value));
        }
        value = (String)properties.get("testOnReturn");
        if (value != null) {
            dataSource.setTestOnReturn(Boolean.valueOf(value));
        }
        value = (String)properties.get("timeBetweenEvictionRunsMillis");
        if (value != null) {
            dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(value));
        }
        value = (String)properties.get("numTestsPerEvictionRun");
        if (value != null) {
            dataSource.setNumTestsPerEvictionRun(Integer.parseInt(value));
        }
        value = (String)properties.get("minEvictableIdleTimeMillis");
        if (value != null) {
            dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(value));
        }
        value = (String)properties.get("phyTimeoutMillis");
        if (value != null) {
            dataSource.setPhyTimeoutMillis(Long.parseLong(value));
        }
        value = (String)properties.get("testWhileIdle");
        if (value != null) {
            dataSource.setTestWhileIdle(Boolean.valueOf(value));
        }
        value = (String)properties.get("password");
        if (value != null) {
            dataSource.setPassword(value);
        }
        value = (String)properties.get("url");
        if (value != null) {
            dataSource.setUrl(value);
        }
        value = (String)properties.get("username");
        if (value != null) {
            dataSource.setUsername(value);
        }
        value = (String)properties.get("validationQuery");
        if (value != null) {
            dataSource.setValidationQuery(value);
        }
        value = (String)properties.get("validationQueryTimeout");
        if (value != null) {
            dataSource.setValidationQueryTimeout(Integer.parseInt(value));
        }
        value = (String)properties.get("accessToUnderlyingConnectionAllowed");
        if (value != null) {
            dataSource.setAccessToUnderlyingConnectionAllowed(Boolean.valueOf(value));
        }
        value = (String)properties.get("removeAbandoned");
        if (value != null) {
            dataSource.setRemoveAbandoned(Boolean.valueOf(value));
        }
        value = (String)properties.get("removeAbandonedTimeout");
        if (value != null) {
            dataSource.setRemoveAbandonedTimeout(Integer.parseInt(value));
        }
        value = (String)properties.get("logAbandoned");
        if (value != null) {
            dataSource.setLogAbandoned(Boolean.valueOf(value));
        }
        value = (String)properties.get("poolPreparedStatements");
        if (value != null) {
            final boolean poolPreparedStatements = Boolean.valueOf(value);
            dataSource.setPoolPreparedStatements(poolPreparedStatements);
            if (poolPreparedStatements) {
                value = (String)properties.get("maxOpenPreparedStatements");
                if (value != null) {
                    dataSource.setMaxOpenPreparedStatements(Integer.parseInt(value));
                }
            }
        }
        value = (String)properties.get("filters");
        if (value != null) {
            dataSource.setFilters(value);
        }
        value = (String)properties.get("exceptionSorter");
        if (value != null) {
            dataSource.setExceptionSorter(value);
        }
        value = (String)properties.get("exception-sorter-class-name");
        if (value != null) {
            dataSource.setExceptionSorter(value);
        }
        value = (String)properties.get("initConnectionSqls");
        if (value != null) {
            final StringTokenizer tokenizer = new StringTokenizer(value, ";");
            dataSource.setConnectionInitSqls(Collections.list((Enumeration<Object>)tokenizer));
        }
        value = (String)properties.get("connectionProperties");
        if (value != null) {
            dataSource.setConnectionProperties(value);
        }
        Properties dataSourceProperties = null;
        for (final Map.Entry entry : properties.entrySet()) {
            final String entryKey = entry.getKey();
            if (entryKey.startsWith("druid.")) {
                if (dataSourceProperties == null) {
                    dataSourceProperties = new Properties();
                }
                final String entryValue = entry.getValue();
                dataSourceProperties.put(entryKey, entryValue);
            }
        }
        if (dataSourceProperties != null) {
            dataSource.configFromPropety(dataSourceProperties);
        }
        value = (String)properties.get("init");
        if ("true".equals(value)) {
            dataSource.init();
        }
    }
    
    static {
        LOG = LogFactory.getLog(DruidDataSourceFactory.class);
        ALL_PROPERTIES = new String[] { "defaultAutoCommit", "defaultReadOnly", "defaultTransactionIsolation", "defaultCatalog", "driverClassName", "maxActive", "maxIdle", "minIdle", "initialSize", "maxWait", "testOnBorrow", "testOnReturn", "timeBetweenEvictionRunsMillis", "numTestsPerEvictionRun", "minEvictableIdleTimeMillis", "testWhileIdle", "password", "filters", "url", "username", "validationQuery", "validationQueryTimeout", "initConnectionSqls", "accessToUnderlyingConnectionAllowed", "removeAbandoned", "removeAbandonedTimeout", "logAbandoned", "poolPreparedStatements", "maxOpenPreparedStatements", "connectionProperties", "exceptionSorter", "exception-sorter-class-name", "init", "name", "druid.timeBetweenLogStatsMillis", "druid.stat.sql.MaxSize", "druid.clearFiltersEnable", "druid.resetStatEnable", "druid.notFullTimeoutRetryCount", "druid.maxWaitThreadCount", "druid.failFast", "druid.phyTimeoutMillis", "druid.wall.tenantColumn", "druid.wall.updateAllow", "druid.wall.deleteAllow", "druid.wall.insertAllow", "druid.wall.selelctAllow", "druid.wall.multiStatementAllow" };
    }
}
