// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat;

import com.alibaba.druid.support.logging.LogFactory;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.List;
import com.alibaba.druid.support.logging.Log;

public class SpringStatUtils
{
    private static final Log LOG;
    
    public static List<Map<String, Object>> getMethodStatDataList(final Object methodStat) {
        if (methodStat.getClass() == SpringStat.class) {
            return ((SpringStat)methodStat).getMethodStatDataList();
        }
        try {
            final Method method = methodStat.getClass().getMethod("getMethodStatDataList", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(methodStat, new Object[0]);
            return (List<Map<String, Object>>)obj;
        }
        catch (Exception e) {
            SpringStatUtils.LOG.error("getMethodStatDataList error", e);
            return null;
        }
    }
    
    public static Map<String, Object> getMethodStatData(final Object methodStat, final String clazz, final String methodSignature) {
        if (methodStat.getClass() == SpringStat.class) {
            return ((SpringStat)methodStat).getMethodStatData(clazz, methodSignature);
        }
        try {
            final Method method = methodStat.getClass().getMethod("getMethodStatData", String.class, String.class);
            final Object obj = method.invoke(methodStat, clazz, methodSignature);
            return (Map<String, Object>)obj;
        }
        catch (Exception e) {
            SpringStatUtils.LOG.error("getMethodStatDataList error", e);
            return null;
        }
    }
    
    public static void reset(final Object webStat) {
        if (webStat.getClass() == SpringStat.class) {
            ((SpringStat)webStat).reset();
            return;
        }
        try {
            final Method method = webStat.getClass().getMethod("reset", (Class<?>[])new Class[0]);
            method.invoke(webStat, new Object[0]);
        }
        catch (Exception e) {
            SpringStatUtils.LOG.error("reset error", e);
        }
    }
    
    static {
        LOG = LogFactory.getLog(SpringStatUtils.class);
    }
}
