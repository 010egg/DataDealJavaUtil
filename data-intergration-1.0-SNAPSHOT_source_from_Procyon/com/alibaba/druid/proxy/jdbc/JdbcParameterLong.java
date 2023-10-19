// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Calendar;

public final class JdbcParameterLong implements JdbcParameter
{
    private static JdbcParameterLong[] cache;
    private final long value;
    
    private JdbcParameterLong(final long value) {
        this.value = value;
    }
    
    public static JdbcParameterLong valueOf(final long value) {
        if (value >= 0L && value < JdbcParameterLong.cache.length) {
            return JdbcParameterLong.cache[(int)value];
        }
        return new JdbcParameterLong(value);
    }
    
    @Override
    public Object getValue() {
        return this.value;
    }
    
    @Override
    public long getLength() {
        return 0L;
    }
    
    @Override
    public Calendar getCalendar() {
        return null;
    }
    
    @Override
    public int getSqlType() {
        return -5;
    }
    
    static {
        final int cacheSize = 127;
        JdbcParameterLong.cache = new JdbcParameterLong[cacheSize];
        for (int i = 0; i < JdbcParameterLong.cache.length; ++i) {
            JdbcParameterLong.cache[i] = new JdbcParameterLong(i);
        }
    }
}
