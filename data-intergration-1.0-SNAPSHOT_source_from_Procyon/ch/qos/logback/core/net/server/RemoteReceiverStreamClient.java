// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.io.ObjectOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;
import ch.qos.logback.core.util.CloseUtil;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.io.OutputStream;
import java.net.Socket;
import ch.qos.logback.core.spi.ContextAwareBase;

class RemoteReceiverStreamClient extends ContextAwareBase implements RemoteReceiverClient
{
    private final String clientId;
    private final Socket socket;
    private final OutputStream outputStream;
    private BlockingQueue<Serializable> queue;
    
    public RemoteReceiverStreamClient(final String id, final Socket socket) {
        this.clientId = "client " + id + ": ";
        this.socket = socket;
        this.outputStream = null;
    }
    
    RemoteReceiverStreamClient(final String id, final OutputStream outputStream) {
        this.clientId = "client " + id + ": ";
        this.socket = null;
        this.outputStream = outputStream;
    }
    
    @Override
    public void setQueue(final BlockingQueue<Serializable> queue) {
        this.queue = queue;
    }
    
    @Override
    public boolean offer(final Serializable event) {
        if (this.queue == null) {
            throw new IllegalStateException("client has no event queue");
        }
        return this.queue.offer(event);
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
        this.addInfo(this.clientId + "connected");
        ObjectOutputStream oos = null;
        try {
            int counter = 0;
            oos = this.createObjectOutputStream();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    final Serializable event = this.queue.take();
                    oos.writeObject(event);
                    oos.flush();
                    if (++counter < 70) {
                        continue;
                    }
                    counter = 0;
                    oos.reset();
                }
                catch (InterruptedException ex4) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        catch (SocketException ex) {
            this.addInfo(this.clientId + ex);
        }
        catch (IOException ex2) {
            this.addError(this.clientId + ex2);
        }
        catch (RuntimeException ex3) {
            this.addError(this.clientId + ex3);
        }
        finally {
            if (oos != null) {
                CloseUtil.closeQuietly(oos);
            }
            this.close();
            this.addInfo(this.clientId + "connection closed");
        }
    }
    
    private ObjectOutputStream createObjectOutputStream() throws IOException {
        if (this.socket == null) {
            return new ObjectOutputStream(this.outputStream);
        }
        return new ObjectOutputStream(this.socket.getOutputStream());
    }
}
