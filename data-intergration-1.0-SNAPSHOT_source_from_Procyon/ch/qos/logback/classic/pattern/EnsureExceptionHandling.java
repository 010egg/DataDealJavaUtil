// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.Context;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PostCompileProcessor;

public class EnsureExceptionHandling implements PostCompileProcessor<ILoggingEvent>
{
    @Override
    public void process(final Context context, final Converter<ILoggingEvent> head) {
        if (head == null) {
            throw new IllegalArgumentException("cannot process empty chain");
        }
        if (!this.chainHandlesThrowable(head)) {
            final Converter<ILoggingEvent> tail = ConverterUtil.findTail(head);
            Converter<ILoggingEvent> exConverter = null;
            final LoggerContext loggerContext = (LoggerContext)context;
            if (loggerContext.isPackagingDataEnabled()) {
                exConverter = new ExtendedThrowableProxyConverter();
            }
            else {
                exConverter = new ThrowableProxyConverter();
            }
            tail.setNext(exConverter);
        }
    }
    
    public boolean chainHandlesThrowable(final Converter<ILoggingEvent> head) {
        for (Converter<ILoggingEvent> c = head; c != null; c = c.getNext()) {
            if (c instanceof ThrowableHandlingConverter) {
                return true;
            }
        }
        return false;
    }
}
