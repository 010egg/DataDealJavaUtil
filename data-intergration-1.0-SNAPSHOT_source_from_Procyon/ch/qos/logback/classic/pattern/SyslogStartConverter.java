// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import java.util.Date;
import java.net.UnknownHostException;
import java.net.InetAddress;
import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.SyslogAppenderBase;
import java.util.Locale;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class SyslogStartConverter extends ClassicConverter
{
    long lastTimestamp;
    String timesmapStr;
    SimpleDateFormat simpleMonthFormat;
    SimpleDateFormat simpleTimeFormat;
    private final Calendar calendar;
    String localHostName;
    int facility;
    
    public SyslogStartConverter() {
        this.lastTimestamp = -1L;
        this.timesmapStr = null;
        this.calendar = Calendar.getInstance(Locale.US);
    }
    
    @Override
    public void start() {
        int errorCount = 0;
        final String facilityStr = this.getFirstOption();
        if (facilityStr == null) {
            this.addError("was expecting a facility string as an option");
            return;
        }
        this.facility = SyslogAppenderBase.facilityStringToint(facilityStr);
        this.localHostName = this.getLocalHostname();
        try {
            this.simpleMonthFormat = new SimpleDateFormat("MMM", Locale.US);
            this.simpleTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        }
        catch (IllegalArgumentException e) {
            this.addError("Could not instantiate SimpleDateFormat", e);
            ++errorCount;
        }
        if (errorCount == 0) {
            super.start();
        }
    }
    
    @Override
    public String convert(final ILoggingEvent event) {
        final StringBuilder sb = new StringBuilder();
        final int pri = this.facility + LevelToSyslogSeverity.convert(event);
        sb.append("<");
        sb.append(pri);
        sb.append(">");
        sb.append(this.computeTimeStampString(event.getTimeStamp()));
        sb.append(' ');
        sb.append(this.localHostName);
        sb.append(' ');
        return sb.toString();
    }
    
    public String getLocalHostname() {
        try {
            final InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        }
        catch (UnknownHostException uhe) {
            this.addError("Could not determine local host name", uhe);
            return "UNKNOWN_LOCALHOST";
        }
    }
    
    String computeTimeStampString(final long now) {
        synchronized (this) {
            if (now / 1000L != this.lastTimestamp) {
                this.lastTimestamp = now / 1000L;
                final Date nowDate = new Date(now);
                this.calendar.setTime(nowDate);
                this.timesmapStr = String.format("%s %2d %s", this.simpleMonthFormat.format(nowDate), this.calendar.get(5), this.simpleTimeFormat.format(nowDate));
            }
            return this.timesmapStr;
        }
    }
}
