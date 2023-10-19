// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net;

import java.io.IOException;
import java.net.Socket;
import ch.qos.logback.core.util.FixedDelay;
import javax.net.SocketFactory;
import ch.qos.logback.core.util.DelayStrategy;
import java.net.InetAddress;

public class DefaultSocketConnector implements SocketConnector
{
    private final InetAddress address;
    private final int port;
    private final DelayStrategy delayStrategy;
    private ExceptionHandler exceptionHandler;
    private SocketFactory socketFactory;
    
    public DefaultSocketConnector(final InetAddress address, final int port, final long initialDelay, final long retryDelay) {
        this(address, port, new FixedDelay(initialDelay, retryDelay));
    }
    
    public DefaultSocketConnector(final InetAddress address, final int port, final DelayStrategy delayStrategy) {
        this.address = address;
        this.port = port;
        this.delayStrategy = delayStrategy;
    }
    
    @Override
    public Socket call() throws InterruptedException {
        this.useDefaultsForMissingFields();
        Socket socket;
        for (socket = this.createSocket(); socket == null && !Thread.currentThread().isInterrupted(); socket = this.createSocket()) {
            Thread.sleep(this.delayStrategy.nextDelay());
        }
        return socket;
    }
    
    private Socket createSocket() {
        Socket newSocket = null;
        try {
            newSocket = this.socketFactory.createSocket(this.address, this.port);
        }
        catch (IOException ioex) {
            this.exceptionHandler.connectionFailed(this, ioex);
        }
        return newSocket;
    }
    
    private void useDefaultsForMissingFields() {
        if (this.exceptionHandler == null) {
            this.exceptionHandler = new ConsoleExceptionHandler();
        }
        if (this.socketFactory == null) {
            this.socketFactory = SocketFactory.getDefault();
        }
    }
    
    @Override
    public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
    
    @Override
    public void setSocketFactory(final SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }
    
    private static class ConsoleExceptionHandler implements ExceptionHandler
    {
        @Override
        public void connectionFailed(final SocketConnector connector, final Exception ex) {
            System.out.println(ex);
        }
    }
}
