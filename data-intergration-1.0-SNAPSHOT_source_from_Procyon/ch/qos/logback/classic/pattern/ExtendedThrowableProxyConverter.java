// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

public class ExtendedThrowableProxyConverter extends ThrowableProxyConverter
{
    @Override
    protected void extraData(final StringBuilder builder, final StackTraceElementProxy step) {
        ThrowableProxyUtil.subjoinPackagingData(builder, step);
    }
    
    protected void prepareLoggingEvent(final ILoggingEvent event) {
    }
}
