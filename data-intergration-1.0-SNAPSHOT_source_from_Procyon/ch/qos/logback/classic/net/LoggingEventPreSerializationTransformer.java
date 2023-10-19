// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net;

import ch.qos.logback.classic.spi.LoggingEventVO;
import ch.qos.logback.classic.spi.LoggingEvent;
import java.io.Serializable;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.PreSerializationTransformer;

public class LoggingEventPreSerializationTransformer implements PreSerializationTransformer<ILoggingEvent>
{
    @Override
    public Serializable transform(final ILoggingEvent event) {
        if (event == null) {
            return null;
        }
        if (event instanceof LoggingEvent) {
            return LoggingEventVO.build(event);
        }
        if (event instanceof LoggingEventVO) {
            return (LoggingEventVO)event;
        }
        throw new IllegalArgumentException("Unsupported type " + event.getClass().getName());
    }
}
