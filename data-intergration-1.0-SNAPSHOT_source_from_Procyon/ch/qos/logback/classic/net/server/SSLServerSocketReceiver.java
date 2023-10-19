// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net.server;

import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
import javax.net.ssl.SSLContext;
import ch.qos.logback.core.net.ssl.ConfigurableSSLServerSocketFactory;
import ch.qos.logback.core.spi.ContextAware;
import javax.net.ServerSocketFactory;
import ch.qos.logback.core.net.ssl.SSLConfiguration;
import ch.qos.logback.core.net.ssl.SSLComponent;

public class SSLServerSocketReceiver extends ServerSocketReceiver implements SSLComponent
{
    private SSLConfiguration ssl;
    private ServerSocketFactory socketFactory;
    
    @Override
    protected ServerSocketFactory getServerSocketFactory() throws Exception {
        if (this.socketFactory == null) {
            final SSLContext sslContext = this.getSsl().createContext(this);
            final SSLParametersConfiguration parameters = this.getSsl().getParameters();
            parameters.setContext(this.getContext());
            this.socketFactory = new ConfigurableSSLServerSocketFactory(parameters, sslContext.getServerSocketFactory());
        }
        return this.socketFactory;
    }
    
    @Override
    public SSLConfiguration getSsl() {
        if (this.ssl == null) {
            this.ssl = new SSLConfiguration();
        }
        return this.ssl;
    }
    
    @Override
    public void setSsl(final SSLConfiguration ssl) {
        this.ssl = ssl;
    }
}
