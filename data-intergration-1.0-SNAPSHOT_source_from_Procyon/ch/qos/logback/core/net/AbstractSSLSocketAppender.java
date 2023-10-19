// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net;

import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
import javax.net.ssl.SSLContext;
import ch.qos.logback.core.net.ssl.ConfigurableSSLSocketFactory;
import ch.qos.logback.core.spi.ContextAware;
import javax.net.SocketFactory;
import ch.qos.logback.core.net.ssl.SSLConfiguration;
import ch.qos.logback.core.net.ssl.SSLComponent;

public abstract class AbstractSSLSocketAppender<E> extends AbstractSocketAppender<E> implements SSLComponent
{
    private SSLConfiguration ssl;
    private SocketFactory socketFactory;
    
    protected AbstractSSLSocketAppender() {
    }
    
    @Override
    protected SocketFactory getSocketFactory() {
        return this.socketFactory;
    }
    
    @Override
    public void start() {
        try {
            final SSLContext sslContext = this.getSsl().createContext(this);
            final SSLParametersConfiguration parameters = this.getSsl().getParameters();
            parameters.setContext(this.getContext());
            this.socketFactory = new ConfigurableSSLSocketFactory(parameters, sslContext.getSocketFactory());
            super.start();
        }
        catch (Exception ex) {
            this.addError(ex.getMessage(), ex);
        }
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
