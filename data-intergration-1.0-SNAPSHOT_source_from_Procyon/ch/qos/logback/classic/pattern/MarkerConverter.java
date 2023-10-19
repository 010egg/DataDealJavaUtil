// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import org.slf4j.Marker;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MarkerConverter extends ClassicConverter
{
    private static String EMPTY;
    
    static {
        MarkerConverter.EMPTY = "";
    }
    
    @Override
    public String convert(final ILoggingEvent le) {
        final Marker marker = le.getMarker();
        if (marker == null) {
            return MarkerConverter.EMPTY;
        }
        return marker.toString();
    }
}
