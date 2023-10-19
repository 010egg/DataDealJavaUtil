// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.io.Serializable;

public class PGInterval extends PGobject implements Serializable, Cloneable
{
    private int years;
    private int months;
    private int days;
    private int hours;
    private int minutes;
    private double seconds;
    private static final DecimalFormat secondsFormat;
    
    public PGInterval() {
        this.setType("interval");
    }
    
    public PGInterval(final String value) throws SQLException {
        this();
        this.setValue(value);
    }
    
    public PGInterval(final int years, final int months, final int days, final int hours, final int minutes, final double seconds) {
        this();
        this.setValue(years, months, days, hours, minutes, seconds);
    }
    
    @Override
    public void setValue(String value) throws SQLException {
        final boolean ISOFormat = !value.startsWith("@");
        if (!ISOFormat && value.length() == 3 && value.charAt(2) == '0') {
            this.setValue(0, 0, 0, 0, 0, 0.0);
            return;
        }
        int years = 0;
        int months = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        double seconds = 0.0;
        try {
            String valueToken = null;
            value = value.replace('+', ' ').replace('@', ' ');
            final StringTokenizer st = new StringTokenizer(value);
            int i = 1;
            while (st.hasMoreTokens()) {
                final String token = st.nextToken();
                if ((i & 0x1) == 0x1) {
                    final int endHours = token.indexOf(58);
                    if (endHours == -1) {
                        valueToken = token;
                    }
                    else {
                        final int offset = (token.charAt(0) == '-') ? 1 : 0;
                        hours = this.nullSafeIntGet(token.substring(offset + 0, endHours));
                        minutes = this.nullSafeIntGet(token.substring(endHours + 1, endHours + 3));
                        final int endMinutes = token.indexOf(58, endHours + 1);
                        if (endMinutes != -1) {
                            seconds = this.nullSafeDoubleGet(token.substring(endMinutes + 1));
                        }
                        if (offset == 1) {
                            hours = -hours;
                            minutes = -minutes;
                            seconds = -seconds;
                        }
                        valueToken = null;
                    }
                }
                else if (token.startsWith("year")) {
                    years = this.nullSafeIntGet(valueToken);
                }
                else if (token.startsWith("mon")) {
                    months = this.nullSafeIntGet(valueToken);
                }
                else if (token.startsWith("day")) {
                    days = this.nullSafeIntGet(valueToken);
                }
                else if (token.startsWith("hour")) {
                    hours = this.nullSafeIntGet(valueToken);
                }
                else if (token.startsWith("min")) {
                    minutes = this.nullSafeIntGet(valueToken);
                }
                else if (token.startsWith("sec")) {
                    seconds = this.nullSafeDoubleGet(valueToken);
                }
                ++i;
            }
        }
        catch (NumberFormatException e) {
            throw new PSQLException(GT.tr("Conversion of interval failed"), PSQLState.NUMERIC_CONSTANT_OUT_OF_RANGE, e);
        }
        if (!ISOFormat && value.endsWith("ago")) {
            this.setValue(-years, -months, -days, -hours, -minutes, -seconds);
        }
        else {
            this.setValue(years, months, days, hours, minutes, seconds);
        }
    }
    
    public void setValue(final int years, final int months, final int days, final int hours, final int minutes, final double seconds) {
        this.setYears(years);
        this.setMonths(months);
        this.setDays(days);
        this.setHours(hours);
        this.setMinutes(minutes);
        this.setSeconds(seconds);
    }
    
    @Override
    public String getValue() {
        return this.years + " years " + this.months + " mons " + this.days + " days " + this.hours + " hours " + this.minutes + " mins " + PGInterval.secondsFormat.format(this.seconds) + " secs";
    }
    
    public int getYears() {
        return this.years;
    }
    
    public void setYears(final int years) {
        this.years = years;
    }
    
    public int getMonths() {
        return this.months;
    }
    
    public void setMonths(final int months) {
        this.months = months;
    }
    
    public int getDays() {
        return this.days;
    }
    
    public void setDays(final int days) {
        this.days = days;
    }
    
    public int getHours() {
        return this.hours;
    }
    
    public void setHours(final int hours) {
        this.hours = hours;
    }
    
    public int getMinutes() {
        return this.minutes;
    }
    
    public void setMinutes(final int minutes) {
        this.minutes = minutes;
    }
    
    public double getSeconds() {
        return this.seconds;
    }
    
    public void setSeconds(final double seconds) {
        this.seconds = seconds;
    }
    
    public void add(final Calendar cal) {
        final int microseconds = (int)(this.getSeconds() * 1000000.0);
        final int milliseconds = (microseconds + ((microseconds < 0) ? -500 : 500)) / 1000;
        cal.add(14, milliseconds);
        cal.add(12, this.getMinutes());
        cal.add(10, this.getHours());
        cal.add(5, this.getDays());
        cal.add(2, this.getMonths());
        cal.add(1, this.getYears());
    }
    
    public void add(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.add(cal);
        date.setTime(cal.getTime().getTime());
    }
    
    public void add(final PGInterval interval) {
        interval.setYears(interval.getYears() + this.getYears());
        interval.setMonths(interval.getMonths() + this.getMonths());
        interval.setDays(interval.getDays() + this.getDays());
        interval.setHours(interval.getHours() + this.getHours());
        interval.setMinutes(interval.getMinutes() + this.getMinutes());
        interval.setSeconds(interval.getSeconds() + this.getSeconds());
    }
    
    public void scale(final int factor) {
        this.setYears(factor * this.getYears());
        this.setMonths(factor * this.getMonths());
        this.setDays(factor * this.getDays());
        this.setHours(factor * this.getHours());
        this.setMinutes(factor * this.getMinutes());
        this.setSeconds(factor * this.getSeconds());
    }
    
    private int nullSafeIntGet(final String value) throws NumberFormatException {
        return (value == null) ? 0 : Integer.parseInt(value);
    }
    
    private double nullSafeDoubleGet(final String value) throws NumberFormatException {
        return (value == null) ? 0.0 : Double.parseDouble(value);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PGInterval)) {
            return false;
        }
        final PGInterval pgi = (PGInterval)obj;
        return pgi.years == this.years && pgi.months == this.months && pgi.days == this.days && pgi.hours == this.hours && pgi.minutes == this.minutes && Double.doubleToLongBits(pgi.seconds) == Double.doubleToLongBits(this.seconds);
    }
    
    @Override
    public int hashCode() {
        return ((((((217 + (int)Double.doubleToLongBits(this.seconds)) * 31 + this.minutes) * 31 + this.hours) * 31 + this.days) * 31 + this.months) * 31 + this.years) * 31;
    }
    
    static {
        secondsFormat = new DecimalFormat("0.00####");
        final DecimalFormatSymbols dfs = PGInterval.secondsFormat.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        PGInterval.secondsFormat.setDecimalFormatSymbols(dfs);
    }
}
