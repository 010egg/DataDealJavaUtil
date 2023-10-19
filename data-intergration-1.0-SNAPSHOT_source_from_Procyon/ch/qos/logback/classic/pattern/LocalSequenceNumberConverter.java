// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.concurrent.atomic.AtomicLong;

public class LocalSequenceNumberConverter extends ClassicConverter
{
    AtomicLong sequenceNumber;
    
    public LocalSequenceNumberConverter() {
        this.sequenceNumber = new AtomicLong(System.currentTimeMillis());
    }
    
    @Override
    public String convert(final ILoggingEvent event) {
        return Long.toString(this.sequenceNumber.getAndIncrement());
    }
}
