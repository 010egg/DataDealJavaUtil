// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.sift;

import org.slf4j.Marker;
import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.core.joran.spi.DefaultClass;
import ch.qos.logback.core.sift.Discriminator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.SiftingAppenderBase;

public class SiftingAppender extends SiftingAppenderBase<ILoggingEvent>
{
    @Override
    protected long getTimestamp(final ILoggingEvent event) {
        return event.getTimeStamp();
    }
    
    @DefaultClass(MDCBasedDiscriminator.class)
    @Override
    public void setDiscriminator(final Discriminator<ILoggingEvent> discriminator) {
        super.setDiscriminator(discriminator);
    }
    
    @Override
    protected boolean eventMarksEndOfLife(final ILoggingEvent event) {
        final Marker marker = event.getMarker();
        return marker != null && marker.contains(ClassicConstants.FINALIZE_SESSION_MARKER);
    }
}
