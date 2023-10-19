// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.log.Log;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSession;
import org.eclipse.jetty.util.TypeUtil;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import javax.net.ssl.SSLEngine;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.io.ssl.SslConnection;
import org.eclipse.jetty.util.log.Logger;

public class SecureRequestCustomizer implements HttpConfiguration.Customizer
{
    private static final Logger LOG;
    public static final String CACHED_INFO_ATTR;
    private String sslSessionAttribute;
    
    public SecureRequestCustomizer() {
        this.sslSessionAttribute = "org.eclipse.jetty.servlet.request.ssl_session";
    }
    
    @Override
    public void customize(final Connector connector, final HttpConfiguration channelConfig, final Request request) {
        if (request.getHttpChannel().getEndPoint() instanceof SslConnection.DecryptedEndPoint) {
            request.setScheme(HttpScheme.HTTPS.asString());
            request.setSecure(true);
            final SslConnection.DecryptedEndPoint ssl_endp = (SslConnection.DecryptedEndPoint)request.getHttpChannel().getEndPoint();
            final SslConnection sslConnection = ssl_endp.getSslConnection();
            final SSLEngine sslEngine = sslConnection.getSSLEngine();
            this.customize(sslEngine, request);
        }
    }
    
    public void customize(final SSLEngine sslEngine, final Request request) {
        request.setScheme(HttpScheme.HTTPS.asString());
        final SSLSession sslSession = sslEngine.getSession();
        try {
            final String cipherSuite = sslSession.getCipherSuite();
            CachedInfo cachedInfo = (CachedInfo)sslSession.getValue(SecureRequestCustomizer.CACHED_INFO_ATTR);
            Integer keySize;
            X509Certificate[] certs;
            String idStr;
            if (cachedInfo != null) {
                keySize = cachedInfo.getKeySize();
                certs = cachedInfo.getCerts();
                idStr = cachedInfo.getIdStr();
            }
            else {
                keySize = new Integer(SslContextFactory.deduceKeyLength(cipherSuite));
                certs = SslContextFactory.getCertChain(sslSession);
                final byte[] bytes = sslSession.getId();
                idStr = TypeUtil.toHexString(bytes);
                cachedInfo = new CachedInfo(keySize, certs, idStr);
                sslSession.putValue(SecureRequestCustomizer.CACHED_INFO_ATTR, cachedInfo);
            }
            if (certs != null) {
                request.setAttribute("javax.servlet.request.X509Certificate", certs);
            }
            request.setAttribute("javax.servlet.request.cipher_suite", cipherSuite);
            request.setAttribute("javax.servlet.request.key_size", keySize);
            request.setAttribute("javax.servlet.request.ssl_session_id", idStr);
            request.setAttribute(this.getSslSessionAttribute(), sslSession);
        }
        catch (Exception e) {
            SecureRequestCustomizer.LOG.warn("EXCEPTION ", e);
        }
    }
    
    public void setSslSessionAttribute(final String attribute) {
        this.sslSessionAttribute = attribute;
    }
    
    public String getSslSessionAttribute() {
        return this.sslSessionAttribute;
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x", this.getClass().getSimpleName(), this.hashCode());
    }
    
    static {
        LOG = Log.getLogger(SecureRequestCustomizer.class);
        CACHED_INFO_ATTR = CachedInfo.class.getName();
    }
    
    private static class CachedInfo
    {
        private final X509Certificate[] _certs;
        private final Integer _keySize;
        private final String _idStr;
        
        CachedInfo(final Integer keySize, final X509Certificate[] certs, final String idStr) {
            this._keySize = keySize;
            this._certs = certs;
            this._idStr = idStr;
        }
        
        X509Certificate[] getCerts() {
            return this._certs;
        }
        
        Integer getKeySize() {
            return this._keySize;
        }
        
        String getIdStr() {
            return this._idStr;
        }
    }
}
