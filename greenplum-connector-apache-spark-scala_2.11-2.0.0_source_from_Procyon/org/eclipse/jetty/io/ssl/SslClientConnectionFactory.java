// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io.ssl;

import java.io.IOException;
import javax.net.ssl.SSLEngine;
import org.eclipse.jetty.io.Connection;
import java.util.Map;
import org.eclipse.jetty.io.EndPoint;
import java.util.concurrent.Executor;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.io.ClientConnectionFactory;

public class SslClientConnectionFactory implements ClientConnectionFactory
{
    public static final String SSL_PEER_HOST_CONTEXT_KEY = "ssl.peer.host";
    public static final String SSL_PEER_PORT_CONTEXT_KEY = "ssl.peer.port";
    public static final String SSL_ENGINE_CONTEXT_KEY = "ssl.engine";
    private final SslContextFactory sslContextFactory;
    private final ByteBufferPool byteBufferPool;
    private final Executor executor;
    private final ClientConnectionFactory connectionFactory;
    
    public SslClientConnectionFactory(final SslContextFactory sslContextFactory, final ByteBufferPool byteBufferPool, final Executor executor, final ClientConnectionFactory connectionFactory) {
        this.sslContextFactory = sslContextFactory;
        this.byteBufferPool = byteBufferPool;
        this.executor = executor;
        this.connectionFactory = connectionFactory;
    }
    
    @Override
    public Connection newConnection(final EndPoint endPoint, final Map<String, Object> context) throws IOException {
        final String host = context.get("ssl.peer.host");
        final int port = context.get("ssl.peer.port");
        final SSLEngine engine = this.sslContextFactory.newSSLEngine(host, port);
        engine.setUseClientMode(true);
        context.put("ssl.engine", engine);
        final SslConnection sslConnection = this.newSslConnection(this.byteBufferPool, this.executor, endPoint, engine);
        sslConnection.setRenegotiationAllowed(this.sslContextFactory.isRenegotiationAllowed());
        endPoint.setConnection(sslConnection);
        final EndPoint appEndPoint = sslConnection.getDecryptedEndPoint();
        appEndPoint.setConnection(this.connectionFactory.newConnection(appEndPoint, context));
        return sslConnection;
    }
    
    protected SslConnection newSslConnection(final ByteBufferPool byteBufferPool, final Executor executor, final EndPoint endPoint, final SSLEngine engine) {
        return new SslConnection(byteBufferPool, executor, endPoint, engine);
    }
}
