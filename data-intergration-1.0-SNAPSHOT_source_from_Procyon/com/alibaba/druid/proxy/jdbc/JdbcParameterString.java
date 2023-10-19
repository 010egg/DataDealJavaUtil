// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Calendar;

public final class JdbcParameterString implements JdbcParameter
{
    private final String value;
    public static final JdbcParameterString empty;
    
    public JdbcParameterString(final String value) {
        this.value = value;
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
        return 12;
    }
    
    static {
        empty = new JdbcParameterString("");
    }
}
