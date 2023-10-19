// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Calendar;

public final class JdbcParameterNull implements JdbcParameter
{
    private final int sqlType;
    public static final JdbcParameterNull CHAR;
    public static final JdbcParameterNull VARCHAR;
    public static final JdbcParameterNull NVARCHAR;
    public static final JdbcParameterNull BINARY;
    public static final JdbcParameterNull VARBINARY;
    public static final JdbcParameterNull TINYINT;
    public static final JdbcParameterNull SMALLINT;
    public static final JdbcParameterNull INTEGER;
    public static final JdbcParameterNull BIGINT;
    public static final JdbcParameterNull DECIMAL;
    public static final JdbcParameterNull NUMERIC;
    public static final JdbcParameterNull FLOAT;
    public static final JdbcParameterNull DOUBLE;
    public static final JdbcParameterNull NULL;
    public static final JdbcParameterNull DATE;
    public static final JdbcParameterNull TIME;
    public static final JdbcParameterNull TIMESTAMP;
    
    private JdbcParameterNull(final int sqlType) {
        this.sqlType = sqlType;
    }
    
    @Override
    public Object getValue() {
        return null;
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
        return this.sqlType;
    }
    
    public static JdbcParameterNull valueOf(final int sqlType) {
        switch (sqlType) {
            case -6: {
                return JdbcParameterNull.INTEGER;
            }
            case 5: {
                return JdbcParameterNull.SMALLINT;
            }
            case 4: {
                return JdbcParameterNull.INTEGER;
            }
            case -5: {
                return JdbcParameterNull.BIGINT;
            }
            case 3: {
                return JdbcParameterNull.DECIMAL;
            }
            case 2: {
                return JdbcParameterNull.NUMERIC;
            }
            case 6: {
                return JdbcParameterNull.FLOAT;
            }
            case 8: {
                return JdbcParameterNull.DOUBLE;
            }
            case 1: {
                return JdbcParameterNull.CHAR;
            }
            case 12: {
                return JdbcParameterNull.VARCHAR;
            }
            case -9: {
                return JdbcParameterNull.NVARCHAR;
            }
            case -2: {
                return JdbcParameterNull.BINARY;
            }
            case -3: {
                return JdbcParameterNull.VARBINARY;
            }
            case 92: {
                return JdbcParameterNull.TIME;
            }
            case 91: {
                return JdbcParameterNull.DATE;
            }
            case 93: {
                return JdbcParameterNull.TIMESTAMP;
            }
            case 0: {
                return JdbcParameterNull.NULL;
            }
            default: {
                return new JdbcParameterNull(sqlType);
            }
        }
    }
    
    static {
        CHAR = new JdbcParameterNull(1);
        VARCHAR = new JdbcParameterNull(12);
        NVARCHAR = new JdbcParameterNull(-9);
        BINARY = new JdbcParameterNull(-2);
        VARBINARY = new JdbcParameterNull(-3);
        TINYINT = new JdbcParameterNull(-6);
        SMALLINT = new JdbcParameterNull(5);
        INTEGER = new JdbcParameterNull(4);
        BIGINT = new JdbcParameterNull(-5);
        DECIMAL = new JdbcParameterNull(3);
        NUMERIC = new JdbcParameterNull(2);
        FLOAT = new JdbcParameterNull(6);
        DOUBLE = new JdbcParameterNull(8);
        NULL = new JdbcParameterNull(0);
        DATE = new JdbcParameterNull(91);
        TIME = new JdbcParameterNull(92);
        TIMESTAMP = new JdbcParameterNull(93);
    }
}
