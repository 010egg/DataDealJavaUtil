// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import ch.qos.logback.core.Context;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.spi.ContextAwareBase;

public class LoggerContextAwareBase extends ContextAwareBase implements LoggerContextAware
{
    @Override
    public void setLoggerContext(final LoggerContext context) {
        super.setContext(context);
    }
    
    @Override
    public void setContext(final Context context) {
        if (context instanceof LoggerContext || context == null) {
            super.setContext(context);
            return;
        }
        throw new IllegalArgumentException("LoggerContextAwareBase only accepts contexts of type c.l.classic.LoggerContext");
    }
    
    public LoggerContext getLoggerContext() {
        return (LoggerContext)this.context;
    }
}
