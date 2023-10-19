// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net.server;

import java.io.ObjectInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.EOFException;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.CloseUtil;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import java.io.InputStream;
import java.net.Socket;

class RemoteAppenderStreamClient implements RemoteAppenderClient
{
    private final String id;
    private final Socket socket;
    private final InputStream inputStream;
    private LoggerContext lc;
    private Logger logger;
    
    public RemoteAppenderStreamClient(final String id, final Socket socket) {
        this.id = id;
        this.socket = socket;
        this.inputStream = null;
    }
    
    public RemoteAppenderStreamClient(final String id, final InputStream inputStream) {
        this.id = id;
        this.socket = null;
        this.inputStream = inputStream;
    }
    
    @Override
    public void setLoggerContext(final LoggerContext lc) {
        this.lc = lc;
        this.logger = lc.getLogger(this.getClass().getPackage().getName());
    }
    
    @Override
    public void close() {
        if (this.socket == null) {
            return;
        }
        CloseUtil.closeQuietly(this.socket);
    }
    
    @Override
    public void run() {
        this.logger.info(this + ": connected");
        ObjectInputStream ois = null;
        try {
            ois = this.createObjectInputStream();
            while (true) {
                final ILoggingEvent event = (ILoggingEvent)ois.readObject();
                final Logger remoteLogger = this.lc.getLogger(event.getLoggerName());
                if (remoteLogger.isEnabledFor(event.getLevel())) {
                    remoteLogger.callAppenders(event);
                }
            }
        }
        catch (EOFException ex3) {}
        catch (IOException ex) {
            this.logger.info(this + ": " + ex);
        }
        catch (ClassNotFoundException ex4) {
            this.logger.error(this + ": unknown event class");
        }
        catch (RuntimeException ex2) {
            this.logger.error(this + ": " + ex2);
        }
        finally {
            if (ois != null) {
                CloseUtil.closeQuietly(ois);
            }
            this.close();
            this.logger.info(this + ": connection closed");
        }
    }
    
    private ObjectInputStream createObjectInputStream() throws IOException {
        if (this.inputStream != null) {
            return new ObjectInputStream(this.inputStream);
        }
        return new ObjectInputStream(this.socket.getInputStream());
    }
    
    @Override
    public String toString() {
        return "client " + this.id;
    }
}
