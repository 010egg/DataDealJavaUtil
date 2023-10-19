// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.io.AbstractConnection;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.util.annotation.Name;

public class HttpConnectionFactory extends AbstractConnectionFactory implements HttpConfiguration.ConnectionFactory
{
    private final HttpConfiguration _config;
    
    public HttpConnectionFactory() {
        this(new HttpConfiguration());
        this.setInputBufferSize(16384);
    }
    
    public HttpConnectionFactory(@Name("config") final HttpConfiguration config) {
        super(HttpVersion.HTTP_1_1.toString());
        this.addBean(this._config = config);
    }
    
    @Override
    public HttpConfiguration getHttpConfiguration() {
        return this._config;
    }
    
    @Override
    public Connection newConnection(final Connector connector, final EndPoint endPoint) {
        return this.configure(new HttpConnection(this._config, connector, endPoint), connector, endPoint);
    }
}
