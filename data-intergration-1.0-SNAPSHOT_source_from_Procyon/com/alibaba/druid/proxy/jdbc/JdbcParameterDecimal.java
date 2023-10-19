// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Calendar;
import java.math.BigDecimal;

public final class JdbcParameterDecimal implements JdbcParameter
{
    private final BigDecimal value;
    public static JdbcParameterDecimal NULL;
    public static JdbcParameterDecimal ZERO;
    public static JdbcParameterDecimal TEN;
    
    private JdbcParameterDecimal(final BigDecimal value) {
        this.value = value;
    }
    
    public static JdbcParameterDecimal valueOf(final BigDecimal x) {
        if (x == null) {
            return JdbcParameterDecimal.NULL;
        }
        if (0 == x.compareTo(BigDecimal.ZERO)) {
            return JdbcParameterDecimal.ZERO;
        }
        if (0 == x.compareTo(BigDecimal.TEN)) {
            return JdbcParameterDecimal.TEN;
        }
        return new JdbcParameterDecimal(x);
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
        return 3;
    }
    
    static {
        JdbcParameterDecimal.NULL = new JdbcParameterDecimal(null);
        JdbcParameterDecimal.ZERO = new JdbcParameterDecimal(BigDecimal.ZERO);
        JdbcParameterDecimal.TEN = new JdbcParameterDecimal(BigDecimal.TEN);
    }
}
