// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net;

import javax.net.SocketFactory;
import ch.qos.logback.core.net.DefaultSocketConnector;
import java.net.ConnectException;
import ch.qos.logback.classic.Logger;
import java.io.IOException;
import java.io.EOFException;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Callable;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.CloseUtil;
import java.net.UnknownHostException;
import java.util.concurrent.Future;
import java.net.Socket;
import java.net.InetAddress;
import ch.qos.logback.core.net.SocketConnector;

public class SocketReceiver extends ReceiverBase implements Runnable, SocketConnector.ExceptionHandler
{
    private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
    private String remoteHost;
    private InetAddress address;
    private int port;
    private int reconnectionDelay;
    private int acceptConnectionTimeout;
    private String receiverId;
    private volatile Socket socket;
    private Future<Socket> connectorTask;
    
    public SocketReceiver() {
        this.acceptConnectionTimeout = 5000;
    }
    
    @Override
    protected boolean shouldStart() {
        int errorCount = 0;
        if (this.port == 0) {
            ++errorCount;
            this.addError("No port was configured for receiver. For more information, please visit http://logback.qos.ch/codes.html#receiver_no_port");
        }
        if (this.remoteHost == null) {
            ++errorCount;
            this.addError("No host name or address was configured for receiver. For more information, please visit http://logback.qos.ch/codes.html#receiver_no_host");
        }
        if (this.reconnectionDelay == 0) {
            this.reconnectionDelay = 30000;
        }
        if (errorCount == 0) {
            try {
                this.address = InetAddress.getByName(this.remoteHost);
            }
            catch (UnknownHostException ex) {
                this.addError("unknown host: " + this.remoteHost);
                ++errorCount;
            }
        }
        if (errorCount == 0) {
            this.receiverId = "receiver " + this.remoteHost + ":" + this.port + ": ";
        }
        return errorCount == 0;
    }
    
    @Override
    protected void onStop() {
        if (this.socket != null) {
            CloseUtil.closeQuietly(this.socket);
        }
    }
    
    @Override
    protected Runnable getRunnableTask() {
        return this;
    }
    
    @Override
    public void run() {
        try {
            final LoggerContext lc = (LoggerContext)this.getContext();
            while (!Thread.currentThread().isInterrupted()) {
                final SocketConnector connector = this.createConnector(this.address, this.port, 0, this.reconnectionDelay);
                this.connectorTask = this.activateConnector(connector);
                if (this.connectorTask == null) {
                    break;
                }
                this.socket = this.waitForConnectorToReturnASocket();
                if (this.socket == null) {
                    break;
                }
                this.dispatchEvents(lc);
            }
        }
        catch (InterruptedException ex) {}
        this.addInfo("shutting down");
    }
    
    private SocketConnector createConnector(final InetAddress address, final int port, final int initialDelay, final int retryDelay) {
        final SocketConnector connector = this.newConnector(address, port, initialDelay, retryDelay);
        connector.setExceptionHandler(this);
        connector.setSocketFactory(this.getSocketFactory());
        return connector;
    }
    
    private Future<Socket> activateConnector(final SocketConnector connector) {
        try {
            return this.getContext().getScheduledExecutorService().submit((Callable<Socket>)connector);
        }
        catch (RejectedExecutionException ex) {
            return null;
        }
    }
    
    private Socket waitForConnectorToReturnASocket() throws InterruptedException {
        try {
            final Socket s = this.connectorTask.get();
            this.connectorTask = null;
            return s;
        }
        catch (ExecutionException ex) {
            return null;
        }
    }
    
    private void dispatchEvents(final LoggerContext lc) {
        try {
            this.socket.setSoTimeout(this.acceptConnectionTimeout);
            final ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            this.socket.setSoTimeout(0);
            this.addInfo(String.valueOf(this.receiverId) + "connection established");
            while (true) {
                final ILoggingEvent event = (ILoggingEvent)ois.readObject();
                final Logger remoteLogger = lc.getLogger(event.getLoggerName());
                if (remoteLogger.isEnabledFor(event.getLevel())) {
                    remoteLogger.callAppenders(event);
                }
            }
        }
        catch (EOFException ex3) {
            this.addInfo(String.valueOf(this.receiverId) + "end-of-stream detected");
        }
        catch (IOException ex) {
            this.addInfo(String.valueOf(this.receiverId) + "connection failed: " + ex);
        }
        catch (ClassNotFoundException ex2) {
            this.addInfo(String.valueOf(this.receiverId) + "unknown event class: " + ex2);
        }
        finally {
            CloseUtil.closeQuietly(this.socket);
            this.socket = null;
            this.addInfo(String.valueOf(this.receiverId) + "connection closed");
        }
    }
    
    @Override
    public void connectionFailed(final SocketConnector connector, final Exception ex) {
        if (ex instanceof InterruptedException) {
            this.addInfo("connector interrupted");
        }
        else if (ex instanceof ConnectException) {
            this.addInfo(String.valueOf(this.receiverId) + "connection refused");
        }
        else {
            this.addInfo(String.valueOf(this.receiverId) + ex);
        }
    }
    
    protected SocketConnector newConnector(final InetAddress address, final int port, final int initialDelay, final int retryDelay) {
        return new DefaultSocketConnector(address, port, initialDelay, retryDelay);
    }
    
    protected SocketFactory getSocketFactory() {
        return SocketFactory.getDefault();
    }
    
    public void setRemoteHost(final String remoteHost) {
        this.remoteHost = remoteHost;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public void setReconnectionDelay(final int reconnectionDelay) {
        this.reconnectionDelay = reconnectionDelay;
    }
    
    public void setAcceptConnectionTimeout(final int acceptConnectionTimeout) {
        this.acceptConnectionTimeout = acceptConnectionTimeout;
    }
}
