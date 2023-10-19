// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.net.UnknownHostException;
import java.net.InetAddress;
import javax.net.ServerSocketFactory;
import ch.qos.logback.core.spi.PreSerializationTransformer;
import java.io.Serializable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import ch.qos.logback.core.AppenderBase;

public abstract class AbstractServerSocketAppender<E> extends AppenderBase<E>
{
    public static final int DEFAULT_BACKLOG = 50;
    public static final int DEFAULT_CLIENT_QUEUE_SIZE = 100;
    private int port;
    private int backlog;
    private int clientQueueSize;
    private String address;
    private ServerRunner<RemoteReceiverClient> runner;
    
    public AbstractServerSocketAppender() {
        this.port = 4560;
        this.backlog = 50;
        this.clientQueueSize = 100;
    }
    
    @Override
    public void start() {
        if (this.isStarted()) {
            return;
        }
        try {
            final ServerSocket socket = this.getServerSocketFactory().createServerSocket(this.getPort(), this.getBacklog(), this.getInetAddress());
            final ServerListener<RemoteReceiverClient> listener = this.createServerListener(socket);
            (this.runner = this.createServerRunner(listener, this.getContext().getExecutorService())).setContext(this.getContext());
            this.getContext().getExecutorService().execute(this.runner);
            super.start();
        }
        catch (Exception ex) {
            this.addError("server startup error: " + ex, ex);
        }
    }
    
    protected ServerListener<RemoteReceiverClient> createServerListener(final ServerSocket socket) {
        return new RemoteReceiverServerListener(socket);
    }
    
    protected ServerRunner<RemoteReceiverClient> createServerRunner(final ServerListener<RemoteReceiverClient> listener, final Executor executor) {
        return new RemoteReceiverServerRunner(listener, executor, this.getClientQueueSize());
    }
    
    @Override
    public void stop() {
        if (!this.isStarted()) {
            return;
        }
        try {
            this.runner.stop();
            super.stop();
        }
        catch (IOException ex) {
            this.addError("server shutdown error: " + ex, ex);
        }
    }
    
    @Override
    protected void append(final E event) {
        if (event == null) {
            return;
        }
        this.postProcessEvent(event);
        final Serializable serEvent = this.getPST().transform(event);
        this.runner.accept(new ClientVisitor<RemoteReceiverClient>() {
            @Override
            public void visit(final RemoteReceiverClient client) {
                client.offer(serEvent);
            }
        });
    }
    
    protected abstract void postProcessEvent(final E p0);
    
    protected abstract PreSerializationTransformer<E> getPST();
    
    protected ServerSocketFactory getServerSocketFactory() throws Exception {
        return ServerSocketFactory.getDefault();
    }
    
    protected InetAddress getInetAddress() throws UnknownHostException {
        if (this.getAddress() == null) {
            return null;
        }
        return InetAddress.getByName(this.getAddress());
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public int getBacklog() {
        return this.backlog;
    }
    
    public void setBacklog(final int backlog) {
        this.backlog = backlog;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(final String address) {
        this.address = address;
    }
    
    public int getClientQueueSize() {
        return this.clientQueueSize;
    }
    
    public void setClientQueueSize(final int clientQueueSize) {
        this.clientQueueSize = clientQueueSize;
    }
}
