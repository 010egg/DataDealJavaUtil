// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net.server;

import java.net.UnknownHostException;
import java.net.InetAddress;
import javax.net.ServerSocketFactory;
import java.io.IOException;
import ch.qos.logback.core.net.server.ServerListener;
import ch.qos.logback.core.util.CloseUtil;
import java.util.concurrent.Executor;
import ch.qos.logback.core.net.server.ServerRunner;
import java.net.ServerSocket;
import ch.qos.logback.classic.net.ReceiverBase;

public class ServerSocketReceiver extends ReceiverBase
{
    public static final int DEFAULT_BACKLOG = 50;
    private int port;
    private int backlog;
    private String address;
    private ServerSocket serverSocket;
    private ServerRunner runner;
    
    public ServerSocketReceiver() {
        this.port = 4560;
        this.backlog = 50;
    }
    
    @Override
    protected boolean shouldStart() {
        try {
            final ServerSocket serverSocket = this.getServerSocketFactory().createServerSocket(this.getPort(), this.getBacklog(), this.getInetAddress());
            final ServerListener<RemoteAppenderClient> listener = this.createServerListener(serverSocket);
            (this.runner = this.createServerRunner(listener, this.getContext().getExecutorService())).setContext(this.getContext());
            return true;
        }
        catch (Exception ex) {
            this.addError("server startup error: " + ex, ex);
            CloseUtil.closeQuietly(this.serverSocket);
            return false;
        }
    }
    
    protected ServerListener<RemoteAppenderClient> createServerListener(final ServerSocket socket) {
        return new RemoteAppenderServerListener(socket);
    }
    
    protected ServerRunner createServerRunner(final ServerListener<RemoteAppenderClient> listener, final Executor executor) {
        return new RemoteAppenderServerRunner(listener, executor);
    }
    
    @Override
    protected Runnable getRunnableTask() {
        return this.runner;
    }
    
    @Override
    protected void onStop() {
        try {
            if (this.runner == null) {
                return;
            }
            this.runner.stop();
        }
        catch (IOException ex) {
            this.addError("server shutdown error: " + ex, ex);
        }
    }
    
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
}
