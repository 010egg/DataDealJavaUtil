// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.Arrays;
import java.lang.reflect.Method;
import com.alibaba.druid.stat.JdbcSqlStat;
import java.util.Map;
import com.alibaba.druid.support.logging.Log;

public class JdbcSqlStatUtils
{
    private static final Log LOG;
    
    public static Map<String, Object> getData(final Object jdbcSqlStat) {
        try {
            if (jdbcSqlStat.getClass() == JdbcSqlStat.class) {
                return ((JdbcSqlStat)jdbcSqlStat).getData();
            }
            final Method method = jdbcSqlStat.getClass().getMethod("getData", (Class<?>[])new Class[0]);
            final Object obj = method.invoke(jdbcSqlStat, new Object[0]);
            return (Map<String, Object>)obj;
        }
        catch (Exception e) {
            JdbcSqlStatUtils.LOG.error("getData error", e);
            return null;
        }
    }
    
    public static long[] rtrim(long[] array) {
        int notZeroLen = array.length;
        for (int i = array.length - 1; i >= 0 && array[i] == 0L; --i, --notZeroLen) {}
        if (notZeroLen != array.length) {
            array = Arrays.copyOf(array, notZeroLen);
        }
        return array;
    }
    
    public static <T> int get(final T stat, final AtomicIntegerFieldUpdater<T> updater, final boolean reset) {
        if (reset) {
            return updater.getAndSet(stat, 0);
        }
        return updater.get(stat);
    }
    
    public static <T> long get(final T stat, final AtomicLongFieldUpdater<T> updater, final boolean reset) {
        if (reset) {
            return updater.getAndSet(stat, 0L);
        }
        return updater.get(stat);
    }
    
    public static long get(final AtomicLong counter, final boolean reset) {
        if (reset) {
            return counter.getAndSet(0L);
        }
        return counter.get();
    }
    
    public static int get(final AtomicInteger counter, final boolean reset) {
        if (reset) {
            return counter.getAndSet(0);
        }
        return counter.get();
    }
    
    static {
        LOG = LogFactory.getLog(JdbcSqlStatUtils.class);
    }
}
