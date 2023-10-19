// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class ClassOfCallerConverter extends NamedConverter
{
    @Override
    protected String getFullyQualifiedName(final ILoggingEvent event) {
        final StackTraceElement[] cda = event.getCallerData();
        if (cda != null && cda.length > 0) {
            return cda[0].getClassName();
        }
        return "?";
    }
}
