// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net.server;

import ch.qos.logback.classic.net.LoggingEventPreSerializationTransformer;
import ch.qos.logback.core.spi.PreSerializationTransformer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.server.SSLServerSocketAppenderBase;

public class SSLServerSocketAppender extends SSLServerSocketAppenderBase<ILoggingEvent>
{
    private static final PreSerializationTransformer<ILoggingEvent> pst;
    private boolean includeCallerData;
    
    static {
        pst = new LoggingEventPreSerializationTransformer();
    }
    
    @Override
    protected void postProcessEvent(final ILoggingEvent event) {
        if (this.isIncludeCallerData()) {
            event.getCallerData();
        }
    }
    
    @Override
    protected PreSerializationTransformer<ILoggingEvent> getPST() {
        return SSLServerSocketAppender.pst;
    }
    
    public boolean isIncludeCallerData() {
        return this.includeCallerData;
    }
    
    public void setIncludeCallerData(final boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
    }
}
