// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Calendar;

public enum SQLIntervalUnit
{
    YEAR, 
    YEAR_MONTH, 
    QUARTER, 
    MONTH, 
    WEEK, 
    DAY, 
    DAY_HOUR, 
    DAY_MINUTE, 
    DAY_SECOND, 
    DAY_MICROSECOND, 
    HOUR, 
    HOUR_MINUTE, 
    HOUR_SECOND, 
    HOUR_MICROSECOND, 
    MINUTE, 
    MINUTE_SECOND, 
    MINUTE_MICROSECOND, 
    SECOND, 
    SECOND_MICROSECOND, 
    MICROSECOND, 
    DAY_OF_WEEK, 
    DOW, 
    DAY_OF_MONTH, 
    DAY_OF_YEAR, 
    YEAR_OF_WEEK, 
    YOW, 
    TIMEZONE_HOUR, 
    TIMEZONE_MINUTE, 
    DOY, 
    YEAR_TO_MONTH("YEAR TO MONTH");
    
    public final String name;
    public final String name_lcase;
    
    private SQLIntervalUnit(final String name) {
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }
    
    private SQLIntervalUnit() {
        this.name = this.name();
        this.name_lcase = this.name.toLowerCase();
    }
    
    public static boolean add(final Calendar calendar, final int intervalInt, final SQLIntervalUnit unit) {
        switch (unit) {
            case YEAR: {
                calendar.add(1, intervalInt);
                return true;
            }
            case MONTH: {
                calendar.add(2, intervalInt);
                return true;
            }
            case WEEK: {
                calendar.add(4, intervalInt);
                return true;
            }
            case DAY: {
                calendar.add(5, intervalInt);
                return true;
            }
            case HOUR: {
                calendar.add(11, intervalInt);
                return true;
            }
            case MINUTE: {
                calendar.add(12, intervalInt);
                return true;
            }
            case SECOND: {
                calendar.add(13, intervalInt);
                return true;
            }
            case MICROSECOND: {
                calendar.add(14, intervalInt);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean isDateTime() {
        switch (this) {
            case HOUR:
            case MINUTE:
            case SECOND:
            case MICROSECOND: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
