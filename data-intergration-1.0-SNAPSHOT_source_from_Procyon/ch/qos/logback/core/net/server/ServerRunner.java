// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.io.IOException;
import ch.qos.logback.core.spi.ContextAware;

public interface ServerRunner<T extends Client> extends ContextAware, Runnable
{
    boolean isRunning();
    
    void stop() throws IOException;
    
    void accept(final ClientVisitor<T> p0);
}
