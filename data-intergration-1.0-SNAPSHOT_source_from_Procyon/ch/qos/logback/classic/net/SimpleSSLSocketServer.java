// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net;

import ch.qos.logback.core.net.ssl.ConfigurableSSLServerSocketFactory;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import ch.qos.logback.classic.LoggerContext;
import javax.net.ServerSocketFactory;

public class SimpleSSLSocketServer extends SimpleSocketServer
{
    private final ServerSocketFactory socketFactory;
    
    public static void main(final String[] argv) throws Exception {
        SimpleSocketServer.doMain(SimpleSSLSocketServer.class, argv);
    }
    
    public SimpleSSLSocketServer(final LoggerContext lc, final int port) throws NoSuchAlgorithmException {
        this(lc, port, SSLContext.getDefault());
    }
    
    public SimpleSSLSocketServer(final LoggerContext lc, final int port, final SSLContext sslContext) {
        super(lc, port);
        if (sslContext == null) {
            throw new NullPointerException("SSL context required");
        }
        final SSLParametersConfiguration parameters = new SSLParametersConfiguration();
        parameters.setContext(lc);
        this.socketFactory = new ConfigurableSSLServerSocketFactory(parameters, sslContext.getServerSocketFactory());
    }
    
    @Override
    protected ServerSocketFactory getServerSocketFactory() {
        return this.socketFactory;
    }
}
