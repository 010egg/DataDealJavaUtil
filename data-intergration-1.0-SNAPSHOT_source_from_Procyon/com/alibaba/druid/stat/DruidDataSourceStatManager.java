// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.concurrent.locks.ReentrantLock;
import com.alibaba.druid.util.JMXUtils;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import javax.management.openmbean.TabularData;
import java.lang.reflect.Method;
import com.alibaba.druid.pool.DruidDataSource;
import java.util.Set;
import com.alibaba.druid.util.DruidDataSourceUtils;
import java.util.Properties;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import javax.management.MBeanServer;
import javax.management.JMException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import javax.management.openmbean.CompositeType;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import com.alibaba.druid.support.logging.Log;
import java.util.concurrent.locks.Lock;

public class DruidDataSourceStatManager implements DruidDataSourceStatManagerMBean
{
    private static final Lock staticLock;
    public static final String SYS_PROP_INSTANCES = "druid.dataSources";
    public static final String SYS_PROP_REGISTER_SYS_PROPERTY = "druid.registerToSysProperty";
    private static final Log LOG;
    private static final DruidDataSourceStatManager instance;
    private final AtomicLong resetCount;
    private static volatile Map dataSources;
    private static final String MBEAN_NAME = "com.alibaba.druid:type=DruidDataSourceStat";
    private static CompositeType COMPOSITE_TYPE;
    
    public DruidDataSourceStatManager() {
        this.resetCount = new AtomicLong();
    }
    
    public static DruidDataSourceStatManager getInstance() {
        return DruidDataSourceStatManager.instance;
    }
    
    public static void clear() {
        DruidDataSourceStatManager.staticLock.lock();
        try {
            final Map<Object, ObjectName> dataSources = getInstances();
            final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
            for (final Object item : dataSources.entrySet()) {
                final Map.Entry entry = (Map.Entry)item;
                final ObjectName objectName = entry.getValue();
                if (objectName == null) {
                    continue;
                }
                try {
                    mbeanServer.unregisterMBean(objectName);
                }
                catch (JMException e) {
                    DruidDataSourceStatManager.LOG.error(e.getMessage(), e);
                }
            }
        }
        finally {
            DruidDataSourceStatManager.staticLock.unlock();
        }
    }
    
    public static Map<Object, ObjectName> getInstances() {
        Map<Object, ObjectName> tmp = (Map<Object, ObjectName>)DruidDataSourceStatManager.dataSources;
        if (tmp == null) {
            DruidDataSourceStatManager.staticLock.lock();
            try {
                if (isRegisterToSystemProperty()) {
                    DruidDataSourceStatManager.dataSources = getInstances0();
                }
                else {
                    tmp = (Map<Object, ObjectName>)DruidDataSourceStatManager.dataSources;
                    if (null == tmp) {
                        tmp = (Map<Object, ObjectName>)(DruidDataSourceStatManager.dataSources = Collections.synchronizedMap(new IdentityHashMap<Object, ObjectName>()));
                    }
                }
            }
            finally {
                DruidDataSourceStatManager.staticLock.unlock();
            }
        }
        return (Map<Object, ObjectName>)DruidDataSourceStatManager.dataSources;
    }
    
    public static boolean isRegisterToSystemProperty() {
        final String value = System.getProperty("druid.registerToSysProperty");
        return "true".equals(value);
    }
    
    static Map<Object, ObjectName> getInstances0() {
        final Properties properties = System.getProperties();
        Map<Object, ObjectName> instances = (Map<Object, ObjectName>)properties.get("druid.dataSources");
        if (instances == null) {
            synchronized (properties) {
                instances = (Map<Object, ObjectName>)properties.get("druid.dataSources");
                if (instances == null) {
                    instances = Collections.synchronizedMap(new IdentityHashMap<Object, ObjectName>());
                    properties.put("druid.dataSources", instances);
                }
            }
        }
        return instances;
    }
    
    public static synchronized ObjectName addDataSource(final Object dataSource, final String name) {
        final Map<Object, ObjectName> instances = getInstances();
        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        synchronized (instances) {
            if (instances.size() == 0) {
                try {
                    final ObjectName objectName = new ObjectName("com.alibaba.druid:type=DruidDataSourceStat");
                    if (!mbeanServer.isRegistered(objectName)) {
                        mbeanServer.registerMBean(DruidDataSourceStatManager.instance, objectName);
                    }
                }
                catch (JMException ex) {
                    DruidDataSourceStatManager.LOG.error("register mbean error", ex);
                }
                DruidStatService.registerMBean();
            }
        }
        ObjectName objectName2 = null;
        if (name != null) {
            try {
                objectName2 = new ObjectName("com.alibaba.druid:type=DruidDataSource,id=" + name);
                mbeanServer.registerMBean(dataSource, objectName2);
            }
            catch (Throwable ex2) {
                DruidDataSourceStatManager.LOG.error("register mbean error", ex2);
                objectName2 = null;
            }
        }
        if (objectName2 == null) {
            try {
                final int id = System.identityHashCode(dataSource);
                objectName2 = new ObjectName("com.alibaba.druid:type=DruidDataSource,id=" + id);
                mbeanServer.registerMBean(dataSource, objectName2);
            }
            catch (Throwable ex2) {
                DruidDataSourceStatManager.LOG.error("register mbean error", ex2);
                objectName2 = null;
            }
        }
        instances.put(dataSource, objectName2);
        return objectName2;
    }
    
    public static synchronized void removeDataSource(final Object dataSource) {
        final Map<Object, ObjectName> instances = getInstances();
        ObjectName objectName = instances.remove(dataSource);
        if (objectName == null) {
            objectName = DruidDataSourceUtils.getObjectName(dataSource);
        }
        if (objectName == null) {
            DruidDataSourceStatManager.LOG.error("unregister mbean failed. url " + DruidDataSourceUtils.getUrl(dataSource));
            return;
        }
        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            mbeanServer.unregisterMBean(objectName);
        }
        catch (Throwable ex) {
            DruidDataSourceStatManager.LOG.error("unregister mbean error", ex);
        }
        if (instances.size() == 0) {
            try {
                mbeanServer.unregisterMBean(new ObjectName("com.alibaba.druid:type=DruidDataSourceStat"));
            }
            catch (Throwable ex) {
                DruidDataSourceStatManager.LOG.error("unregister mbean error", ex);
            }
            DruidStatService.unregisterMBean();
        }
    }
    
    public static Set<DruidDataSource> getDruidDataSourceInstances() {
        getInstances();
        return DruidDataSourceStatManager.dataSources.keySet();
    }
    
    @Override
    public void reset() {
        final Map<Object, ObjectName> dataSources = getInstances();
        for (final Object item : dataSources.keySet()) {
            try {
                final Method method = item.getClass().getMethod("resetStat", (Class<?>[])new Class[0]);
                method.invoke(item, new Object[0]);
            }
            catch (Exception e) {
                DruidDataSourceStatManager.LOG.error("resetStat error", e);
            }
        }
        this.resetCount.incrementAndGet();
    }
    
    public void logAndResetDataSource() {
        final Map<Object, ObjectName> dataSources = getInstances();
        for (final Object item : dataSources.keySet()) {
            try {
                final Method method = item.getClass().getMethod("logStats", (Class<?>[])new Class[0]);
                method.invoke(item, new Object[0]);
            }
            catch (Exception e) {
                DruidDataSourceStatManager.LOG.error("resetStat error", e);
            }
        }
        this.resetCount.incrementAndGet();
    }
    
    @Override
    public long getResetCount() {
        return this.resetCount.get();
    }
    
    @Override
    public TabularData getDataSourceList() throws JMException {
        final CompositeType rowType = getDruidDataSourceCompositeType();
        final String[] indexNames = rowType.keySet().toArray(new String[rowType.keySet().size()]);
        final TabularType tabularType = new TabularType("DruidDataSourceStat", "DruidDataSourceStat", rowType, indexNames);
        final TabularData data = new TabularDataSupport(tabularType);
        final Set<Object> dataSources = getInstances().keySet();
        for (final Object dataSource : dataSources) {
            data.put(this.getCompositeData(dataSource));
        }
        return data;
    }
    
    public CompositeDataSupport getCompositeData(final Object dataSource) throws JMException {
        final CompositeType rowType = getDruidDataSourceCompositeType();
        final Map<String, Object> map = DruidDataSourceUtils.getStatDataForMBean(dataSource);
        return new CompositeDataSupport(rowType, map);
    }
    
    public static CompositeType getDruidDataSourceCompositeType() throws JMException {
        if (DruidDataSourceStatManager.COMPOSITE_TYPE != null) {
            return DruidDataSourceStatManager.COMPOSITE_TYPE;
        }
        final OpenType<?>[] indexTypes = (OpenType<?>[])new OpenType[] { SimpleType.STRING, SimpleType.STRING, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.BOOLEAN, SimpleType.BOOLEAN, SimpleType.BOOLEAN, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.STRING, SimpleType.STRING, SimpleType.INTEGER, SimpleType.STRING, SimpleType.STRING, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, JMXUtils.getThrowableCompositeType(), JMXUtils.getThrowableCompositeType(), SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.STRING, SimpleType.DATE, SimpleType.DATE, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG };
        final String[] indexDescriptions;
        final String[] indexNames = indexDescriptions = new String[] { "Name", "URL", "CreateCount", "DestroyCount", "ConnectCount", "CloseCount", "ActiveCount", "PoolingCount", "LockQueueLength", "WaitThreadCount", "InitialSize", "MaxActive", "MinIdle", "PoolPreparedStatements", "TestOnBorrow", "TestOnReturn", "MinEvictableIdleTimeMillis", "ConnectErrorCount", "CreateTimespanMillis", "DbType", "ValidationQuery", "ValidationQueryTimeout", "DriverClassName", "Username", "RemoveAbandonedCount", "NotEmptyWaitCount", "NotEmptyWaitNanos", "ErrorCount", "ReusePreparedStatementCount", "StartTransactionCount", "CommitCount", "RollbackCount", "LastError", "LastCreateError", "PreparedStatementCacheDeleteCount", "PreparedStatementCacheAccessCount", "PreparedStatementCacheMissCount", "PreparedStatementCacheHitCount", "PreparedStatementCacheCurrentCount", "Version", "LastErrorTime", "LastCreateErrorTime", "CreateErrorCount", "DiscardCount", "ExecuteQueryCount", "ExecuteUpdateCount" };
        return DruidDataSourceStatManager.COMPOSITE_TYPE = new CompositeType("DataSourceStatistic", "DataSource Statistic", indexNames, indexDescriptions, indexTypes);
    }
    
    static {
        staticLock = new ReentrantLock();
        LOG = LogFactory.getLog(DruidDataSourceStatManager.class);
        instance = new DruidDataSourceStatManager();
        DruidDataSourceStatManager.COMPOSITE_TYPE = null;
    }
}
