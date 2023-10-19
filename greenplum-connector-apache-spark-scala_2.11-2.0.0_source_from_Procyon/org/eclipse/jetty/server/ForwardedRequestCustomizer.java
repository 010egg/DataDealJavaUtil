// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.http.HttpFields;
import java.net.InetSocketAddress;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpHeader;

public class ForwardedRequestCustomizer implements HttpConfiguration.Customizer
{
    private String _hostHeader;
    private String _forwardedHostHeader;
    private String _forwardedServerHeader;
    private String _forwardedForHeader;
    private String _forwardedProtoHeader;
    private String _forwardedCipherSuiteHeader;
    private String _forwardedSslSessionIdHeader;
    
    public ForwardedRequestCustomizer() {
        this._forwardedHostHeader = HttpHeader.X_FORWARDED_HOST.toString();
        this._forwardedServerHeader = HttpHeader.X_FORWARDED_SERVER.toString();
        this._forwardedForHeader = HttpHeader.X_FORWARDED_FOR.toString();
        this._forwardedProtoHeader = HttpHeader.X_FORWARDED_PROTO.toString();
    }
    
    public String getHostHeader() {
        return this._hostHeader;
    }
    
    public void setHostHeader(final String hostHeader) {
        this._hostHeader = hostHeader;
    }
    
    public String getForwardedHostHeader() {
        return this._forwardedHostHeader;
    }
    
    public void setForwardedHostHeader(final String forwardedHostHeader) {
        this._forwardedHostHeader = forwardedHostHeader;
    }
    
    public String getForwardedServerHeader() {
        return this._forwardedServerHeader;
    }
    
    public void setForwardedServerHeader(final String forwardedServerHeader) {
        this._forwardedServerHeader = forwardedServerHeader;
    }
    
    public String getForwardedForHeader() {
        return this._forwardedForHeader;
    }
    
    public void setForwardedForHeader(final String forwardedRemoteAddressHeader) {
        this._forwardedForHeader = forwardedRemoteAddressHeader;
    }
    
    public String getForwardedProtoHeader() {
        return this._forwardedProtoHeader;
    }
    
    public void setForwardedProtoHeader(final String forwardedProtoHeader) {
        this._forwardedProtoHeader = forwardedProtoHeader;
    }
    
    public String getForwardedCipherSuiteHeader() {
        return this._forwardedCipherSuiteHeader;
    }
    
    public void setForwardedCipherSuiteHeader(final String forwardedCipherSuite) {
        this._forwardedCipherSuiteHeader = forwardedCipherSuite;
    }
    
    public String getForwardedSslSessionIdHeader() {
        return this._forwardedSslSessionIdHeader;
    }
    
    public void setForwardedSslSessionIdHeader(final String forwardedSslSessionId) {
        this._forwardedSslSessionIdHeader = forwardedSslSessionId;
    }
    
    @Override
    public void customize(final Connector connector, final HttpConfiguration config, final Request request) {
        final HttpFields httpFields = request.getHttpFields();
        if (this.getForwardedCipherSuiteHeader() != null) {
            final String cipher_suite = httpFields.getStringField(this.getForwardedCipherSuiteHeader());
            if (cipher_suite != null) {
                request.setAttribute("javax.servlet.request.cipher_suite", cipher_suite);
            }
        }
        if (this.getForwardedSslSessionIdHeader() != null) {
            final String ssl_session_id = httpFields.getStringField(this.getForwardedSslSessionIdHeader());
            if (ssl_session_id != null) {
                request.setAttribute("javax.servlet.request.ssl_session_id", ssl_session_id);
                request.setScheme(HttpScheme.HTTPS.asString());
            }
        }
        final String forwardedHost = this.getLeftMostFieldValue(httpFields, this.getForwardedHostHeader());
        final String forwardedServer = this.getLeftMostFieldValue(httpFields, this.getForwardedServerHeader());
        final String forwardedFor = this.getLeftMostFieldValue(httpFields, this.getForwardedForHeader());
        final String forwardedProto = this.getLeftMostFieldValue(httpFields, this.getForwardedProtoHeader());
        if (this._hostHeader != null) {
            httpFields.put(HttpHeader.HOST.toString(), this._hostHeader);
            request.setServerName(null);
            request.setServerPort(-1);
            request.getServerName();
        }
        else if (forwardedHost != null) {
            httpFields.put(HttpHeader.HOST.toString(), forwardedHost);
            request.setServerName(null);
            request.setServerPort(-1);
            request.getServerName();
        }
        else if (forwardedServer != null) {
            request.setServerName(forwardedServer);
        }
        if (forwardedFor != null) {
            request.setRemoteAddr(InetSocketAddress.createUnresolved(forwardedFor, request.getRemotePort()));
        }
        if (forwardedProto != null) {
            request.setScheme(forwardedProto);
            if (forwardedProto.equals(config.getSecureScheme())) {
                request.setSecure(true);
            }
        }
    }
    
    protected String getLeftMostFieldValue(final HttpFields fields, final String header) {
        if (header == null) {
            return null;
        }
        final String headerValue = fields.getStringField(header);
        if (headerValue == null) {
            return null;
        }
        final int commaIndex = headerValue.indexOf(44);
        if (commaIndex == -1) {
            return headerValue;
        }
        return headerValue.substring(0, commaIndex);
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x", this.getClass().getSimpleName(), this.hashCode());
    }
}
