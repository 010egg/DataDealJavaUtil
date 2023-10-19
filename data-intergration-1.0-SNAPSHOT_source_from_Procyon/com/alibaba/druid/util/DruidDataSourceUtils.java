// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.List;
import java.util.Map;
import javax.management.ObjectName;
import java.lang.reflect.Method;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.logging.Log;

public class DruidDataSourceUtils
{
    private static final Log LOG;
    
    public static String getUrl(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getUrl();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getUrl", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (String)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getUrl error", e);
            return null;
        }
    }
    
    public static long getID(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getID();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getID", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (long)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getID error", e);
            return -1L;
        }
    }
    
    public static String getName(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getName();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getName", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (String)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getUrl error", e);
            return null;
        }
    }
    
    public static ObjectName getObjectName(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getObjectName();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getObjectName", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (ObjectName)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getObjectName error", e);
            return null;
        }
    }
    
    public static Object getSqlStat(final Object druidDataSource, final int sqlId) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getSqlStat(sqlId);
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getSqlStat", Integer.TYPE);
            return method.invoke(druidDataSource, sqlId);
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getSqlStat error", e);
            return null;
        }
    }
    
    public static boolean isRemoveAbandoned(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).isRemoveAbandoned();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("isRemoveAbandoned", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (boolean)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("isRemoveAbandoned error", e);
            return false;
        }
    }
    
    public static Map<String, Object> getStatDataForMBean(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getStatDataForMBean();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getStatDataForMBean", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (Map<String, Object>)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getStatDataForMBean error", e);
            return null;
        }
    }
    
    public static Map<String, Object> getStatData(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getStatData();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getStatData", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (Map<String, Object>)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getStatData error", e);
            return null;
        }
    }
    
    public static Map getSqlStatMap(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getSqlStatMap();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getSqlStatMap", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (Map)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getSqlStatMap error", e);
            return null;
        }
    }
    
    public static Map<String, Object> getWallStatMap(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getWallStatMap();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getWallStatMap", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (Map<String, Object>)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getWallStatMap error", e);
            return null;
        }
    }
    
    public static List<Map<String, Object>> getPoolingConnectionInfo(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getPoolingConnectionInfo();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getPoolingConnectionInfo", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (List<Map<String, Object>>)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getPoolingConnectionInfo error", e);
            return null;
        }
    }
    
    public static List<String> getActiveConnectionStackTrace(final Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getActiveConnectionStackTrace();
        }
        try {
            final Method method = druidDataSource.getClass().getMethod("getActiveConnectionStackTrace", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(druidDataSource, new Object[0]);
            return (List<String>)obj;
        }
        catch (Exception e) {
            DruidDataSourceUtils.LOG.error("getActiveConnectionStackTrace error", e);
            return null;
        }
    }
    
    static {
        LOG = LogFactory.getLog(DruidDataSourceUtils.class);
    }
}
