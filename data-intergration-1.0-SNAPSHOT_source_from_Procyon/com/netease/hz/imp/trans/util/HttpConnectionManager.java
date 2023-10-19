// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.io.FileInputStream;
import java.io.File;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.SSLContexts;
import java.security.KeyStore;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

public class HttpConnectionManager
{
    private static final Logger LOGGER;
    private static CloseableHttpClient httpClient;
    
    public static synchronized CloseableHttpClient init(final HttpConnectionConfig connectionCfg) {
        try {
            if (HttpConnectionManager.httpClient != null) {
                return HttpConnectionManager.httpClient;
            }
            final ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {
                @Override
                public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
                    long keepAlive = super.getKeepAliveDuration(response, context);
                    if (keepAlive == -1L) {
                        keepAlive = connectionCfg.getDefaultKeepliveTimeout();
                    }
                    return keepAlive;
                }
            };
            final RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
            final ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
            registryBuilder.register("http", plainSF);
            try {
                final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                final SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
                        return true;
                    }
                }).build();
                final LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                registryBuilder.register("https", sslSF);
            }
            catch (KeyStoreException e) {
                throw new RuntimeException(e);
            }
            catch (KeyManagementException e2) {
                throw new RuntimeException(e2);
            }
            catch (NoSuchAlgorithmException e3) {
                throw new RuntimeException(e3);
            }
            final Registry<ConnectionSocketFactory> registry = registryBuilder.build();
            final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(connectionCfg.getMaxConnection());
            cm.setDefaultMaxPerRoute(connectionCfg.getMaxConnection());
            SocketConfig.custom().setSoTimeout(connectionCfg.getConnectTimeout()).build();
            final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionCfg.getConnectTimeout()).setSocketTimeout(connectionCfg.getConnectTimeout()).setCookieSpec("ignoreCookies").build();
            return HttpConnectionManager.httpClient = HttpClients.custom().setKeepAliveStrategy(keepAliveStrat).setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
        }
        catch (Exception e4) {
            HttpConnectionManager.LOGGER.error("init http connection manager failed.", e4);
            return null;
        }
    }
    
    public static HttpClient getHttpClient() {
        if (HttpConnectionManager.httpClient == null) {
            final HttpConnectionConfig connectionConfig = HttpConnectionConfig.getInstance();
            HttpConnectionManager.httpClient = init(connectionConfig);
        }
        return HttpConnectionManager.httpClient;
    }
    
    public static void main(final String[] args) throws CertificateException, FileNotFoundException {
        final String f = HttpConnectionManager.class.getClassLoader().getResource("crt").getPath();
        final File file = new File(f);
        final InputStream in = new FileInputStream(file);
        final CertificateFactory cf = CertificateFactory.getInstance("X.509");
        final X509Certificate cert = (X509Certificate)cf.generateCertificate(in);
        cert.checkValidity();
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(HttpConnectionConfig.class);
    }
}
