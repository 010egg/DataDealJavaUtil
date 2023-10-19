// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net;

import ch.qos.logback.core.spi.PreSerializationTransformer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.AbstractSocketAppender;

public class SocketAppender extends AbstractSocketAppender<ILoggingEvent>
{
    private static final PreSerializationTransformer<ILoggingEvent> pst;
    private boolean includeCallerData;
    
    static {
        pst = new LoggingEventPreSerializationTransformer();
    }
    
    public SocketAppender() {
        this.includeCallerData = false;
    }
    
    @Override
    protected void postProcessEvent(final ILoggingEvent event) {
        if (this.includeCallerData) {
            event.getCallerData();
        }
    }
    
    public void setIncludeCallerData(final boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
    }
    
    public PreSerializationTransformer<ILoggingEvent> getPST() {
        return SocketAppender.pst;
    }
}
