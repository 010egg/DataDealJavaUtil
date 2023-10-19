// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.ArrayUtil;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import java.util.Iterator;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.io.AbstractConnection;
import org.eclipse.jetty.util.component.ContainerLifeCycle;

public abstract class AbstractConnectionFactory extends ContainerLifeCycle implements ConnectionFactory
{
    private final String _protocol;
    private int _inputbufferSize;
    
    protected AbstractConnectionFactory(final String protocol) {
        this._inputbufferSize = 8192;
        this._protocol = protocol;
    }
    
    @Override
    public String getProtocol() {
        return this._protocol;
    }
    
    public int getInputBufferSize() {
        return this._inputbufferSize;
    }
    
    public void setInputBufferSize(final int size) {
        this._inputbufferSize = size;
    }
    
    protected AbstractConnection configure(final AbstractConnection connection, final Connector connector, final EndPoint endPoint) {
        connection.setInputBufferSize(this.getInputBufferSize());
        if (connector instanceof ContainerLifeCycle) {
            final ContainerLifeCycle aggregate = (ContainerLifeCycle)connector;
            for (final Connection.Listener listener : aggregate.getBeans(Connection.Listener.class)) {
                connection.addListener(listener);
            }
        }
        return connection;
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x{%s}", this.getClass().getSimpleName(), this.hashCode(), this.getProtocol());
    }
    
    public static ConnectionFactory[] getFactories(final SslContextFactory sslContextFactory, ConnectionFactory... factories) {
        factories = ArrayUtil.removeNulls(factories);
        if (sslContextFactory == null) {
            return factories;
        }
        for (final ConnectionFactory factory : factories) {
            if (factory instanceof HttpConfiguration.ConnectionFactory) {
                final HttpConfiguration config = ((HttpConfiguration.ConnectionFactory)factory).getHttpConfiguration();
                if (config.getCustomizer(SecureRequestCustomizer.class) == null) {
                    config.addCustomizer(new SecureRequestCustomizer());
                }
            }
        }
        return ArrayUtil.prependToArray(new SslConnectionFactory(sslContextFactory, factories[0].getProtocol()), factories, ConnectionFactory.class);
    }
}
