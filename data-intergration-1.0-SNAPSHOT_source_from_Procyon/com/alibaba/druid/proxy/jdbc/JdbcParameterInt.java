// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Calendar;

public final class JdbcParameterInt implements JdbcParameter
{
    private static JdbcParameterInt[] cache;
    private final int value;
    
    private JdbcParameterInt(final int value) {
        this.value = value;
    }
    
    public static JdbcParameterInt valueOf(final int value) {
        if (value >= 0 && value < JdbcParameterInt.cache.length) {
            return JdbcParameterInt.cache[value];
        }
        return new JdbcParameterInt(value);
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
        return 4;
    }
    
    static {
        final int cacheSize = 127;
        JdbcParameterInt.cache = new JdbcParameterInt[cacheSize];
        for (int i = 0; i < JdbcParameterInt.cache.length; ++i) {
            JdbcParameterInt.cache[i] = new JdbcParameterInt(i);
        }
    }
}
