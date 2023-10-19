// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.spi.ContextAwareBase;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.GregorianCalendar;

public class RollingCalendar extends GregorianCalendar
{
    private static final long serialVersionUID = -5937537740925066161L;
    static final TimeZone GMT_TIMEZONE;
    PeriodicityType periodicityType;
    String datePattern;
    
    public RollingCalendar(final String datePattern) {
        this.periodicityType = PeriodicityType.ERRONEOUS;
        this.datePattern = datePattern;
        this.periodicityType = this.computePeriodicityType();
    }
    
    public RollingCalendar(final String datePattern, final TimeZone tz, final Locale locale) {
        super(tz, locale);
        this.periodicityType = PeriodicityType.ERRONEOUS;
        this.datePattern = datePattern;
        this.periodicityType = this.computePeriodicityType();
    }
    
    public PeriodicityType getPeriodicityType() {
        return this.periodicityType;
    }
    
    public PeriodicityType computePeriodicityType() {
        final GregorianCalendar calendar = new GregorianCalendar(RollingCalendar.GMT_TIMEZONE, Locale.getDefault());
        final Date epoch = new Date(0L);
        if (this.datePattern != null) {
            for (final PeriodicityType i : PeriodicityType.VALID_ORDERED_LIST) {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.datePattern);
                simpleDateFormat.setTimeZone(RollingCalendar.GMT_TIMEZONE);
                final String r0 = simpleDateFormat.format(epoch);
                final Date next = innerGetEndOfThisPeriod(calendar, i, epoch);
                final String r2 = simpleDateFormat.format(next);
                if (r0 != null && r2 != null && !r0.equals(r2)) {
                    return i;
                }
            }
        }
        return PeriodicityType.ERRONEOUS;
    }
    
    public boolean isCollisionFree() {
        switch (this.periodicityType) {
            case TOP_OF_HOUR: {
                return !this.collision(43200000L);
            }
            case TOP_OF_DAY: {
                return !this.collision(604800000L) && !this.collision(2678400000L) && !this.collision(31536000000L);
            }
            case TOP_OF_WEEK: {
                return !this.collision(2937600000L) && !this.collision(31622400000L);
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean collision(final long delta) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.datePattern);
        simpleDateFormat.setTimeZone(RollingCalendar.GMT_TIMEZONE);
        final Date epoch0 = new Date(0L);
        final String r0 = simpleDateFormat.format(epoch0);
        final Date epoch2 = new Date(delta);
        final String r2 = simpleDateFormat.format(epoch2);
        return r0.equals(r2);
    }
    
    public void printPeriodicity(final ContextAwareBase cab) {
        switch (this.periodicityType) {
            case TOP_OF_MILLISECOND: {
                cab.addInfo("Roll-over every millisecond.");
                break;
            }
            case TOP_OF_SECOND: {
                cab.addInfo("Roll-over every second.");
                break;
            }
            case TOP_OF_MINUTE: {
                cab.addInfo("Roll-over every minute.");
                break;
            }
            case TOP_OF_HOUR: {
                cab.addInfo("Roll-over at the top of every hour.");
                break;
            }
            case HALF_DAY: {
                cab.addInfo("Roll-over at midday and midnight.");
                break;
            }
            case TOP_OF_DAY: {
                cab.addInfo("Roll-over at midnight.");
                break;
            }
            case TOP_OF_WEEK: {
                cab.addInfo("Rollover at the start of week.");
                break;
            }
            case TOP_OF_MONTH: {
                cab.addInfo("Rollover at start of every month.");
                break;
            }
            default: {
                cab.addInfo("Unknown periodicity.");
                break;
            }
        }
    }
    
    public long periodBarriersCrossed(final long start, final long end) {
        if (start > end) {
            throw new IllegalArgumentException("Start cannot come before end");
        }
        final Date startFloored = this.getsStartOfCurrentPeriod(start);
        final Date endFloored = this.getsStartOfCurrentPeriod(end);
        final long diff = endFloored.getTime() - startFloored.getTime();
        switch (this.periodicityType) {
            case TOP_OF_MILLISECOND: {
                return diff;
            }
            case TOP_OF_SECOND: {
                return diff / 1000L;
            }
            case TOP_OF_MINUTE: {
                return diff / 60000L;
            }
            case TOP_OF_HOUR: {
                return (int)diff / 3600000L;
            }
            case TOP_OF_DAY: {
                return diff / 86400000L;
            }
            case TOP_OF_WEEK: {
                return diff / 604800000L;
            }
            case TOP_OF_MONTH: {
                return diffInMonths(start, end);
            }
            default: {
                throw new IllegalStateException("Unknown periodicity type.");
            }
        }
    }
    
    public static int diffInMonths(final long startTime, final long endTime) {
        if (startTime > endTime) {
            throw new IllegalArgumentException("startTime cannot be larger than endTime");
        }
        final Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(startTime);
        final Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(endTime);
        final int yearDiff = endCal.get(1) - startCal.get(1);
        final int monthDiff = endCal.get(2) - startCal.get(2);
        return yearDiff * 12 + monthDiff;
    }
    
    private static Date innerGetEndOfThisPeriod(final Calendar cal, final PeriodicityType periodicityType, final Date now) {
        return innerGetEndOfNextNthPeriod(cal, periodicityType, now, 1);
    }
    
    private static Date innerGetEndOfNextNthPeriod(final Calendar cal, final PeriodicityType periodicityType, final Date now, final int numPeriods) {
        cal.setTime(now);
        switch (periodicityType) {
            case TOP_OF_MILLISECOND: {
                cal.add(14, numPeriods);
                break;
            }
            case TOP_OF_SECOND: {
                cal.set(14, 0);
                cal.add(13, numPeriods);
                break;
            }
            case TOP_OF_MINUTE: {
                cal.set(13, 0);
                cal.set(14, 0);
                cal.add(12, numPeriods);
                break;
            }
            case TOP_OF_HOUR: {
                cal.set(12, 0);
                cal.set(13, 0);
                cal.set(14, 0);
                cal.add(11, numPeriods);
                break;
            }
            case TOP_OF_DAY: {
                cal.set(11, 0);
                cal.set(12, 0);
                cal.set(13, 0);
                cal.set(14, 0);
                cal.add(5, numPeriods);
                break;
            }
            case TOP_OF_WEEK: {
                cal.set(7, cal.getFirstDayOfWeek());
                cal.set(11, 0);
                cal.set(12, 0);
                cal.set(13, 0);
                cal.set(14, 0);
                cal.add(3, numPeriods);
                break;
            }
            case TOP_OF_MONTH: {
                cal.set(5, 1);
                cal.set(11, 0);
                cal.set(12, 0);
                cal.set(13, 0);
                cal.set(14, 0);
                cal.add(2, numPeriods);
                break;
            }
            default: {
                throw new IllegalStateException("Unknown periodicity type.");
            }
        }
        return cal.getTime();
    }
    
    public Date getEndOfNextNthPeriod(final Date now, final int periods) {
        return innerGetEndOfNextNthPeriod(this, this.periodicityType, now, periods);
    }
    
    public Date getNextTriggeringDate(final Date now) {
        return this.getEndOfNextNthPeriod(now, 1);
    }
    
    public Date getsStartOfCurrentPeriod(final long now) {
        final Calendar aCal = Calendar.getInstance(this.getTimeZone());
        aCal.setTimeInMillis(now);
        return this.getEndOfNextNthPeriod(aCal.getTime(), 0);
    }
    
    static {
        GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
    }
}
