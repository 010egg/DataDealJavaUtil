// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net;

import javax.net.SocketFactory;
import java.net.Socket;
import java.util.concurrent.Callable;

public interface SocketConnector extends Callable<Socket>
{
    Socket call() throws InterruptedException;
    
    void setExceptionHandler(final ExceptionHandler p0);
    
    void setSocketFactory(final SocketFactory p0);
    
    public interface ExceptionHandler
    {
        void connectionFailed(final SocketConnector p0, final Exception p1);
    }
}
