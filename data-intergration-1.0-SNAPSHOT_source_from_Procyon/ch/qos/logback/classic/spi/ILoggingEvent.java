// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

import java.util.Map;
import org.slf4j.Marker;
import ch.qos.logback.classic.Level;
import ch.qos.logback.core.spi.DeferredProcessingAware;

public interface ILoggingEvent extends DeferredProcessingAware
{
    String getThreadName();
    
    Level getLevel();
    
    String getMessage();
    
    Object[] getArgumentArray();
    
    String getFormattedMessage();
    
    String getLoggerName();
    
    LoggerContextVO getLoggerContextVO();
    
    IThrowableProxy getThrowableProxy();
    
    StackTraceElement[] getCallerData();
    
    boolean hasCallerData();
    
    Marker getMarker();
    
    Map<String, String> getMDCPropertyMap();
    
    @Deprecated
    Map<String, String> getMdc();
    
    long getTimeStamp();
    
    void prepareForDeferredProcessing();
}
