// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class LoggerConverter extends NamedConverter
{
    @Override
    protected String getFullyQualifiedName(final ILoggingEvent event) {
        return event.getLoggerName();
    }
}
