// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.ssl;

import java.net.UnknownHostException;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import java.net.Socket;
import java.net.InetAddress;
import javax.net.ssl.SSLSocketFactory;
import javax.net.SocketFactory;

public class ConfigurableSSLSocketFactory extends SocketFactory
{
    private final SSLParametersConfiguration parameters;
    private final SSLSocketFactory delegate;
    
    public ConfigurableSSLSocketFactory(final SSLParametersConfiguration parameters, final SSLSocketFactory delegate) {
        this.parameters = parameters;
        this.delegate = delegate;
    }
    
    @Override
    public Socket createSocket(final InetAddress address, final int port, final InetAddress localAddress, final int localPort) throws IOException {
        final SSLSocket socket = (SSLSocket)this.delegate.createSocket(address, port, localAddress, localPort);
        this.parameters.configure(new SSLConfigurableSocket(socket));
        return socket;
    }
    
    @Override
    public Socket createSocket(final InetAddress host, final int port) throws IOException {
        final SSLSocket socket = (SSLSocket)this.delegate.createSocket(host, port);
        this.parameters.configure(new SSLConfigurableSocket(socket));
        return socket;
    }
    
    @Override
    public Socket createSocket(final String host, final int port, final InetAddress localHost, final int localPort) throws IOException, UnknownHostException {
        final SSLSocket socket = (SSLSocket)this.delegate.createSocket(host, port, localHost, localPort);
        this.parameters.configure(new SSLConfigurableSocket(socket));
        return socket;
    }
    
    @Override
    public Socket createSocket(final String host, final int port) throws IOException, UnknownHostException {
        final SSLSocket socket = (SSLSocket)this.delegate.createSocket(host, port);
        this.parameters.configure(new SSLConfigurableSocket(socket));
        return socket;
    }
}
