// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.ssl;

import java.io.IOException;
import javax.net.ssl.SSLServerSocket;
import java.net.ServerSocket;
import java.net.InetAddress;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ServerSocketFactory;

public class ConfigurableSSLServerSocketFactory extends ServerSocketFactory
{
    private final SSLParametersConfiguration parameters;
    private final SSLServerSocketFactory delegate;
    
    public ConfigurableSSLServerSocketFactory(final SSLParametersConfiguration parameters, final SSLServerSocketFactory delegate) {
        this.parameters = parameters;
        this.delegate = delegate;
    }
    
    @Override
    public ServerSocket createServerSocket(final int port, final int backlog, final InetAddress ifAddress) throws IOException {
        final SSLServerSocket socket = (SSLServerSocket)this.delegate.createServerSocket(port, backlog, ifAddress);
        this.parameters.configure(new SSLConfigurableServerSocket(socket));
        return socket;
    }
    
    @Override
    public ServerSocket createServerSocket(final int port, final int backlog) throws IOException {
        final SSLServerSocket socket = (SSLServerSocket)this.delegate.createServerSocket(port, backlog);
        this.parameters.configure(new SSLConfigurableServerSocket(socket));
        return socket;
    }
    
    @Override
    public ServerSocket createServerSocket(final int port) throws IOException {
        final SSLServerSocket socket = (SSLServerSocket)this.delegate.createServerSocket(port);
        this.parameters.configure(new SSLConfigurableServerSocket(socket));
        return socket;
    }
}
