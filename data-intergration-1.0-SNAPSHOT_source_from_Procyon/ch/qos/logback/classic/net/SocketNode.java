// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net;

import java.io.IOException;
import java.net.SocketException;
import java.io.EOFException;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.io.InputStream;
import java.io.BufferedInputStream;
import ch.qos.logback.classic.Logger;
import java.net.SocketAddress;
import java.io.ObjectInputStream;
import ch.qos.logback.classic.LoggerContext;
import java.net.Socket;

public class SocketNode implements Runnable
{
    Socket socket;
    LoggerContext context;
    ObjectInputStream ois;
    SocketAddress remoteSocketAddress;
    Logger logger;
    boolean closed;
    SimpleSocketServer socketServer;
    
    public SocketNode(final SimpleSocketServer socketServer, final Socket socket, final LoggerContext context) {
        this.closed = false;
        this.socketServer = socketServer;
        this.socket = socket;
        this.remoteSocketAddress = socket.getRemoteSocketAddress();
        this.context = context;
        this.logger = context.getLogger(SocketNode.class);
    }
    
    @Override
    public void run() {
        try {
            this.ois = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
        }
        catch (Exception e) {
            this.logger.error("Could not open ObjectInputStream to " + this.socket, e);
            this.closed = true;
        }
        try {
            while (!this.closed) {
                final ILoggingEvent event = (ILoggingEvent)this.ois.readObject();
                final Logger remoteLogger = this.context.getLogger(event.getLoggerName());
                if (remoteLogger.isEnabledFor(event.getLevel())) {
                    remoteLogger.callAppenders(event);
                }
            }
        }
        catch (EOFException ex) {
            this.logger.info("Caught java.io.EOFException closing connection.");
        }
        catch (SocketException ex2) {
            this.logger.info("Caught java.net.SocketException closing connection.");
        }
        catch (IOException e2) {
            this.logger.info("Caught java.io.IOException: " + e2);
            this.logger.info("Closing connection.");
        }
        catch (Exception e3) {
            this.logger.error("Unexpected exception. Closing connection.", e3);
        }
        this.socketServer.socketNodeClosing(this);
        this.close();
    }
    
    void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.ois != null) {
            try {
                this.ois.close();
            }
            catch (IOException e) {
                this.logger.warn("Could not close connection.", e);
                return;
            }
            finally {
                this.ois = null;
            }
            this.ois = null;
        }
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getClass().getName()) + this.remoteSocketAddress.toString();
    }
}
