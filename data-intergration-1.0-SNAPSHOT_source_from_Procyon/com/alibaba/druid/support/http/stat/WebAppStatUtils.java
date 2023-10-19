// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http.stat;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;
import com.alibaba.druid.support.logging.Log;

public class WebAppStatUtils
{
    private static final Log LOG;
    
    public static Map<String, Object> getStatData(final Object webStat) {
        if (webStat.getClass() == WebAppStat.class) {
            return ((WebAppStat)webStat).getStatData();
        }
        try {
            final Method method = webStat.getClass().getMethod("getStatData", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(webStat, new Object[0]);
            return (Map<String, Object>)obj;
        }
        catch (Exception e) {
            WebAppStatUtils.LOG.error("getStatData error", e);
            return null;
        }
    }
    
    public static List<Map<String, Object>> getURIStatDataList(final Object webStat) {
        if (webStat.getClass() == WebAppStat.class) {
            return ((WebAppStat)webStat).getURIStatDataList();
        }
        try {
            final Method method = webStat.getClass().getMethod("getURIStatDataList", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(webStat, new Object[0]);
            return (List<Map<String, Object>>)obj;
        }
        catch (Exception e) {
            WebAppStatUtils.LOG.error("getURIStatDataList error", e);
            return null;
        }
    }
    
    public static List<Map<String, Object>> getSessionStatDataList(final Object webStat) {
        if (webStat.getClass() == WebAppStat.class) {
            return ((WebAppStat)webStat).getSessionStatDataList();
        }
        try {
            final Method method = webStat.getClass().getMethod("getSessionStatDataList", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(webStat, new Object[0]);
            return (List<Map<String, Object>>)obj;
        }
        catch (Exception e) {
            WebAppStatUtils.LOG.error("getSessionStatDataList error", e);
            return null;
        }
    }
    
    public static Map<String, Object> getSessionStatData(final Object webStat, final String sessionId) {
        if (webStat.getClass() == WebAppStat.class) {
            return ((WebAppStat)webStat).getSessionStatData(sessionId);
        }
        try {
            final Method method = webStat.getClass().getMethod("getSessionStatData", String.class);
            final Object obj = method.invoke(webStat, sessionId);
            return (Map<String, Object>)obj;
        }
        catch (Exception e) {
            WebAppStatUtils.LOG.error("getSessionStatData error", e);
            return null;
        }
    }
    
    public static Map<String, Object> getURIStatData(final Object webStat, final String uri) {
        if (webStat.getClass() == WebAppStat.class) {
            return ((WebAppStat)webStat).getURIStatData(uri);
        }
        try {
            final Method method = webStat.getClass().getMethod("getURIStatData", String.class);
            final Object obj = method.invoke(webStat, uri);
            return (Map<String, Object>)obj;
        }
        catch (Exception e) {
            WebAppStatUtils.LOG.error("getURIStatData error", e);
            return null;
        }
    }
    
    public static void reset(final Object webStat) {
        if (webStat.getClass() == WebAppStat.class) {
            ((WebAppStat)webStat).reset();
            return;
        }
        try {
            final Method method = webStat.getClass().getMethod("reset", (Class<?>[])new Class[0]);
            method.invoke(webStat, new Object[0]);
        }
        catch (Exception e) {
            WebAppStatUtils.LOG.error("reset error", e);
        }
    }
    
    static {
        LOG = LogFactory.getLog(WebAppStatUtils.class);
    }
}
