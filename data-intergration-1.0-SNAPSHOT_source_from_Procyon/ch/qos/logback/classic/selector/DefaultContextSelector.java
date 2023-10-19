// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.selector;

import java.util.Arrays;
import java.util.List;
import ch.qos.logback.classic.LoggerContext;

public class DefaultContextSelector implements ContextSelector
{
    private LoggerContext defaultLoggerContext;
    
    public DefaultContextSelector(final LoggerContext context) {
        this.defaultLoggerContext = context;
    }
    
    @Override
    public LoggerContext getLoggerContext() {
        return this.getDefaultLoggerContext();
    }
    
    @Override
    public LoggerContext getDefaultLoggerContext() {
        return this.defaultLoggerContext;
    }
    
    @Override
    public LoggerContext detachLoggerContext(final String loggerContextName) {
        return this.defaultLoggerContext;
    }
    
    @Override
    public List<String> getContextNames() {
        return Arrays.asList(this.defaultLoggerContext.getName());
    }
    
    @Override
    public LoggerContext getLoggerContext(final String name) {
        if (this.defaultLoggerContext.getName().equals(name)) {
            return this.defaultLoggerContext;
        }
        return null;
    }
}
