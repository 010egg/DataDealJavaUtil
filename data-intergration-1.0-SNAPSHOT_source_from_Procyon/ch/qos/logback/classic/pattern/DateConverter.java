// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.List;
import java.util.TimeZone;
import ch.qos.logback.core.util.CachingDateFormatter;

public class DateConverter extends ClassicConverter
{
    long lastTimestamp;
    String timestampStrCache;
    CachingDateFormatter cachingDateFormatter;
    
    public DateConverter() {
        this.lastTimestamp = -1L;
        this.timestampStrCache = null;
        this.cachingDateFormatter = null;
    }
    
    @Override
    public void start() {
        String datePattern = this.getFirstOption();
        if (datePattern == null) {
            datePattern = "yyyy-MM-dd HH:mm:ss,SSS";
        }
        if (datePattern.equals("ISO8601")) {
            datePattern = "yyyy-MM-dd HH:mm:ss,SSS";
        }
        try {
            this.cachingDateFormatter = new CachingDateFormatter(datePattern);
        }
        catch (IllegalArgumentException e) {
            this.addWarn("Could not instantiate SimpleDateFormat with pattern " + datePattern, e);
            this.cachingDateFormatter = new CachingDateFormatter("yyyy-MM-dd HH:mm:ss,SSS");
        }
        final List optionList = this.getOptionList();
        if (optionList != null && optionList.size() > 1) {
            final TimeZone tz = TimeZone.getTimeZone(optionList.get(1));
            this.cachingDateFormatter.setTimeZone(tz);
        }
    }
    
    @Override
    public String convert(final ILoggingEvent le) {
        final long timestamp = le.getTimeStamp();
        return this.cachingDateFormatter.format(timestamp);
    }
}
