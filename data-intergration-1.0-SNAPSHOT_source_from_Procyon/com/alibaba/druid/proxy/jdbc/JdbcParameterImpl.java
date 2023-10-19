// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Calendar;

public final class JdbcParameterImpl implements JdbcParameter
{
    private final int sqlType;
    private final Object value;
    private final long length;
    private final Calendar calendar;
    private final int scaleOrLength;
    
    public JdbcParameterImpl(final int sqlType, final Object value, final long length, final Calendar calendar, final int scaleOrLength) {
        this.sqlType = sqlType;
        this.value = value;
        this.length = length;
        this.calendar = calendar;
        this.scaleOrLength = scaleOrLength;
    }
    
    public JdbcParameterImpl(final int sqlType, final Object value, final long length, final Calendar calendar) {
        this(sqlType, value, -1L, null, -1);
    }
    
    public JdbcParameterImpl(final int sqlType, final Object value) {
        this(sqlType, value, -1L, null);
    }
    
    public JdbcParameterImpl(final int sqlType, final Object value, final long length) {
        this(sqlType, value, length, null);
    }
    
    public JdbcParameterImpl(final int sqlType, final Object value, final Calendar calendar) {
        this(sqlType, value, -1L, calendar);
    }
    
    public int getScaleOrLength() {
        return this.scaleOrLength;
    }
    
    @Override
    public Object getValue() {
        return this.value;
    }
    
    @Override
    public long getLength() {
        return this.length;
    }
    
    @Override
    public Calendar getCalendar() {
        return this.calendar;
    }
    
    @Override
    public int getSqlType() {
        return this.sqlType;
    }
}
