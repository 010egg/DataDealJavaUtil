// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.util.Calendar;
import java.sql.Time;

public class PGTime extends Time
{
    private static final long serialVersionUID = 3592492258676494276L;
    private Calendar calendar;
    
    public PGTime(final long time) {
        this(time, null);
    }
    
    public PGTime(final long time, final Calendar calendar) {
        super(time);
        this.setCalendar(calendar);
    }
    
    public void setCalendar(final Calendar calendar) {
        this.calendar = calendar;
    }
    
    public Calendar getCalendar() {
        return this.calendar;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + ((this.calendar == null) ? 0 : this.calendar.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof PGTime)) {
            return false;
        }
        final PGTime other = (PGTime)obj;
        if (this.calendar == null) {
            if (other.calendar != null) {
                return false;
            }
        }
        else if (!this.calendar.equals(other.calendar)) {
            return false;
        }
        return true;
    }
    
    @Override
    public Object clone() {
        final PGTime clone = (PGTime)super.clone();
        if (this.getCalendar() != null) {
            clone.setCalendar((Calendar)this.getCalendar().clone());
        }
        return clone;
    }
}
