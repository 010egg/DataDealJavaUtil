// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Iterator;
import java.sql.SQLException;
import com.alibaba.druid.util.Utils;
import java.util.List;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.io.Closeable;
import com.alibaba.druid.util.JdbcUtils;
import java.net.URL;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.druid.support.logging.Log;

public class FilterManager
{
    private static final Log LOG;
    private static final ConcurrentHashMap<String, String> aliasMap;
    
    public static final String getFilter(final String alias) {
        if (alias == null) {
            return null;
        }
        String filter = FilterManager.aliasMap.get(alias);
        if (filter == null && alias.length() < 128) {
            filter = alias;
        }
        return filter;
    }
    
    public static Properties loadFilterConfig() throws IOException {
        final Properties filterProperties = new Properties();
        loadFilterConfig(filterProperties, ClassLoader.getSystemClassLoader());
        loadFilterConfig(filterProperties, FilterManager.class.getClassLoader());
        loadFilterConfig(filterProperties, Thread.currentThread().getContextClassLoader());
        loadFilterConfig(filterProperties, FilterManager.class.getClassLoader());
        return filterProperties;
    }
    
    private static void loadFilterConfig(final Properties filterProperties, final ClassLoader classLoader) throws IOException {
        if (classLoader == null) {
            return;
        }
        final Enumeration<URL> e = classLoader.getResources("META-INF/druid-filter.properties");
        while (e.hasMoreElements()) {
            final URL url = e.nextElement();
            final Properties property = new Properties();
            InputStream is = null;
            try {
                is = url.openStream();
                property.load(is);
            }
            finally {
                JdbcUtils.close(is);
            }
            filterProperties.putAll(property);
        }
    }
    
    public static void loadFilter(final List<Filter> filters, final String filterName) throws SQLException {
        if (filterName.length() == 0) {
            return;
        }
        final String filterClassNames = getFilter(filterName);
        if (filterClassNames != null) {
            for (final String filterClassName : filterClassNames.split(",")) {
                Label_0231: {
                    if (!existsFilter(filters, filterClassName)) {
                        final Class<?> filterClass = Utils.loadClass(filterClassName);
                        if (filterClass == null) {
                            FilterManager.LOG.error("load filter error, filter not found : " + filterClassName);
                        }
                        else {
                            Filter filter;
                            try {
                                filter = (Filter)filterClass.newInstance();
                            }
                            catch (ClassCastException e) {
                                FilterManager.LOG.error("load filter error.", e);
                                break Label_0231;
                            }
                            catch (InstantiationException e2) {
                                throw new SQLException("load managed jdbc driver event listener error. " + filterName, e2);
                            }
                            catch (IllegalAccessException e3) {
                                throw new SQLException("load managed jdbc driver event listener error. " + filterName, e3);
                            }
                            catch (RuntimeException e4) {
                                throw new SQLException("load managed jdbc driver event listener error. " + filterName, e4);
                            }
                            filters.add(filter);
                        }
                    }
                }
            }
            return;
        }
        if (existsFilter(filters, filterName)) {
            return;
        }
        final Class<?> filterClass2 = Utils.loadClass(filterName);
        if (filterClass2 == null) {
            FilterManager.LOG.error("load filter error, filter not found : " + filterName);
            return;
        }
        try {
            final Filter filter2 = (Filter)filterClass2.newInstance();
            filters.add(filter2);
        }
        catch (Exception e5) {
            throw new SQLException("load managed jdbc driver event listener error. " + filterName, e5);
        }
    }
    
    private static boolean existsFilter(final List<Filter> filterList, final String filterClassName) {
        for (final Filter filter : filterList) {
            final String itemFilterClassName = filter.getClass().getName();
            if (itemFilterClassName.equalsIgnoreCase(filterClassName)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        LOG = LogFactory.getLog(FilterManager.class);
        aliasMap = new ConcurrentHashMap<String, String>(16, 0.75f, 1);
        try {
            final Properties filterProperties = loadFilterConfig();
            for (final Map.Entry<Object, Object> entry : filterProperties.entrySet()) {
                final String key = entry.getKey();
                if (key.startsWith("druid.filters.")) {
                    final String name = key.substring("druid.filters.".length());
                    FilterManager.aliasMap.put(name, entry.getValue());
                }
            }
        }
        catch (Throwable e) {
            FilterManager.LOG.error("load filter config error", e);
        }
    }
}
