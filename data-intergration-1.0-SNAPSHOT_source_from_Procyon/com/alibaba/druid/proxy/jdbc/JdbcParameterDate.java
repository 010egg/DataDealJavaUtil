// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Calendar;
import java.util.Date;

public final class JdbcParameterDate implements JdbcParameter
{
    private final Date value;
    
    public JdbcParameterDate(final Date value) {
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
        return 91;
    }
}
