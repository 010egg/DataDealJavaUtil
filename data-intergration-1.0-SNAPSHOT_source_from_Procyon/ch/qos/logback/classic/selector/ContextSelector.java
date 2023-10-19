// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.selector;

import java.util.List;
import ch.qos.logback.classic.LoggerContext;

public interface ContextSelector
{
    LoggerContext getLoggerContext();
    
    LoggerContext getLoggerContext(final String p0);
    
    LoggerContext getDefaultLoggerContext();
    
    LoggerContext detachLoggerContext(final String p0);
    
    List<String> getContextNames();
}
