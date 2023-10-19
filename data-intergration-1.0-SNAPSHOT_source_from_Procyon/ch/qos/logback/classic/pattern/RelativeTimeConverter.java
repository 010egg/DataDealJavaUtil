// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class RelativeTimeConverter extends ClassicConverter
{
    long lastTimestamp;
    String timesmapCache;
    
    public RelativeTimeConverter() {
        this.lastTimestamp = -1L;
        this.timesmapCache = null;
    }
    
    @Override
    public String convert(final ILoggingEvent event) {
        final long now = event.getTimeStamp();
        synchronized (this) {
            if (now != this.lastTimestamp) {
                this.lastTimestamp = now;
                this.timesmapCache = Long.toString(now - event.getLoggerContextVO().getBirthTime());
            }
            return this.timesmapCache;
        }
    }
}
