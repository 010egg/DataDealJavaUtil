// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.io.AbstractConnection;
import javax.net.ssl.SSLEngine;
import java.util.Iterator;
import org.eclipse.jetty.io.ssl.SslConnection;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import java.util.Arrays;
import java.util.List;

public abstract class NegotiatingServerConnectionFactory extends AbstractConnectionFactory
{
    private final List<String> protocols;
    private String defaultProtocol;
    
    public NegotiatingServerConnectionFactory(final String protocol, final String... protocols) {
        super(protocol);
        this.protocols = Arrays.asList(protocols);
    }
    
    public String getDefaultProtocol() {
        return this.defaultProtocol;
    }
    
    public void setDefaultProtocol(final String defaultProtocol) {
        this.defaultProtocol = defaultProtocol;
    }
    
    public List<String> getProtocols() {
        return this.protocols;
    }
    
    @Override
    public Connection newConnection(final Connector connector, final EndPoint endPoint) {
        List<String> protocols = this.protocols;
        if (protocols.isEmpty()) {
            protocols = connector.getProtocols();
            final Iterator<String> i = protocols.iterator();
            while (i.hasNext()) {
                final String protocol = i.next();
                final String prefix = "ssl-";
                if (protocol.regionMatches(true, 0, prefix, 0, prefix.length()) || protocol.equalsIgnoreCase("alpn")) {
                    i.remove();
                }
            }
        }
        String dft = this.defaultProtocol;
        if (dft == null && !protocols.isEmpty()) {
            dft = protocols.get(0);
        }
        SSLEngine engine = null;
        EndPoint ep = endPoint;
        while (engine == null && ep != null) {
            if (ep instanceof SslConnection.DecryptedEndPoint) {
                engine = ((SslConnection.DecryptedEndPoint)ep).getSslConnection().getSSLEngine();
            }
            else {
                ep = null;
            }
        }
        return this.configure(this.newServerConnection(connector, endPoint, engine, protocols, dft), connector, endPoint);
    }
    
    protected abstract AbstractConnection newServerConnection(final Connector p0, final EndPoint p1, final SSLEngine p2, final List<String> p3, final String p4);
    
    @Override
    public String toString() {
        return String.format("%s@%x{%s,%s,%s}", this.getClass().getSimpleName(), this.hashCode(), this.getProtocol(), this.getDefaultProtocol(), this.getProtocols());
    }
}
