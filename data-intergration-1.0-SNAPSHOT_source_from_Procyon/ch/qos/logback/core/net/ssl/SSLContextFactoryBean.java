// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.ssl;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import ch.qos.logback.core.spi.ContextAware;

public class SSLContextFactoryBean
{
    private static final String JSSE_KEY_STORE_PROPERTY = "javax.net.ssl.keyStore";
    private static final String JSSE_TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore";
    private KeyStoreFactoryBean keyStore;
    private KeyStoreFactoryBean trustStore;
    private SecureRandomFactoryBean secureRandom;
    private KeyManagerFactoryFactoryBean keyManagerFactory;
    private TrustManagerFactoryFactoryBean trustManagerFactory;
    private String protocol;
    private String provider;
    
    public SSLContext createContext(final ContextAware context) throws NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, CertificateException {
        final SSLContext sslContext = (this.getProvider() != null) ? SSLContext.getInstance(this.getProtocol(), this.getProvider()) : SSLContext.getInstance(this.getProtocol());
        context.addInfo("SSL protocol '" + sslContext.getProtocol() + "' provider '" + sslContext.getProvider() + "'");
        final KeyManager[] keyManagers = this.createKeyManagers(context);
        final TrustManager[] trustManagers = this.createTrustManagers(context);
        final SecureRandom secureRandom = this.createSecureRandom(context);
        sslContext.init(keyManagers, trustManagers, secureRandom);
        return sslContext;
    }
    
    private KeyManager[] createKeyManagers(final ContextAware context) throws NoSuchProviderException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        if (this.getKeyStore() == null) {
            return null;
        }
        final KeyStore keyStore = this.getKeyStore().createKeyStore();
        context.addInfo("key store of type '" + keyStore.getType() + "' provider '" + keyStore.getProvider() + "': " + this.getKeyStore().getLocation());
        final KeyManagerFactory kmf = this.getKeyManagerFactory().createKeyManagerFactory();
        context.addInfo("key manager algorithm '" + kmf.getAlgorithm() + "' provider '" + kmf.getProvider() + "'");
        final char[] passphrase = this.getKeyStore().getPassword().toCharArray();
        kmf.init(keyStore, passphrase);
        return kmf.getKeyManagers();
    }
    
    private TrustManager[] createTrustManagers(final ContextAware context) throws NoSuchProviderException, NoSuchAlgorithmException, KeyStoreException {
        if (this.getTrustStore() == null) {
            return null;
        }
        final KeyStore trustStore = this.getTrustStore().createKeyStore();
        context.addInfo("trust store of type '" + trustStore.getType() + "' provider '" + trustStore.getProvider() + "': " + this.getTrustStore().getLocation());
        final TrustManagerFactory tmf = this.getTrustManagerFactory().createTrustManagerFactory();
        context.addInfo("trust manager algorithm '" + tmf.getAlgorithm() + "' provider '" + tmf.getProvider() + "'");
        tmf.init(trustStore);
        return tmf.getTrustManagers();
    }
    
    private SecureRandom createSecureRandom(final ContextAware context) throws NoSuchProviderException, NoSuchAlgorithmException {
        final SecureRandom secureRandom = this.getSecureRandom().createSecureRandom();
        context.addInfo("secure random algorithm '" + secureRandom.getAlgorithm() + "' provider '" + secureRandom.getProvider() + "'");
        return secureRandom;
    }
    
    public KeyStoreFactoryBean getKeyStore() {
        if (this.keyStore == null) {
            this.keyStore = this.keyStoreFromSystemProperties("javax.net.ssl.keyStore");
        }
        return this.keyStore;
    }
    
    public void setKeyStore(final KeyStoreFactoryBean keyStore) {
        this.keyStore = keyStore;
    }
    
    public KeyStoreFactoryBean getTrustStore() {
        if (this.trustStore == null) {
            this.trustStore = this.keyStoreFromSystemProperties("javax.net.ssl.trustStore");
        }
        return this.trustStore;
    }
    
    public void setTrustStore(final KeyStoreFactoryBean trustStore) {
        this.trustStore = trustStore;
    }
    
    private KeyStoreFactoryBean keyStoreFromSystemProperties(final String property) {
        if (System.getProperty(property) == null) {
            return null;
        }
        final KeyStoreFactoryBean keyStore = new KeyStoreFactoryBean();
        keyStore.setLocation(this.locationFromSystemProperty(property));
        keyStore.setProvider(System.getProperty(property + "Provider"));
        keyStore.setPassword(System.getProperty(property + "Password"));
        keyStore.setType(System.getProperty(property + "Type"));
        return keyStore;
    }
    
    private String locationFromSystemProperty(final String name) {
        String location = System.getProperty(name);
        if (location != null && !location.startsWith("file:")) {
            location = "file:" + location;
        }
        return location;
    }
    
    public SecureRandomFactoryBean getSecureRandom() {
        if (this.secureRandom == null) {
            return new SecureRandomFactoryBean();
        }
        return this.secureRandom;
    }
    
    public void setSecureRandom(final SecureRandomFactoryBean secureRandom) {
        this.secureRandom = secureRandom;
    }
    
    public KeyManagerFactoryFactoryBean getKeyManagerFactory() {
        if (this.keyManagerFactory == null) {
            return new KeyManagerFactoryFactoryBean();
        }
        return this.keyManagerFactory;
    }
    
    public void setKeyManagerFactory(final KeyManagerFactoryFactoryBean keyManagerFactory) {
        this.keyManagerFactory = keyManagerFactory;
    }
    
    public TrustManagerFactoryFactoryBean getTrustManagerFactory() {
        if (this.trustManagerFactory == null) {
            return new TrustManagerFactoryFactoryBean();
        }
        return this.trustManagerFactory;
    }
    
    public void setTrustManagerFactory(final TrustManagerFactoryFactoryBean trustManagerFactory) {
        this.trustManagerFactory = trustManagerFactory;
    }
    
    public String getProtocol() {
        if (this.protocol == null) {
            return "SSL";
        }
        return this.protocol;
    }
    
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }
    
    public String getProvider() {
        return this.provider;
    }
    
    public void setProvider(final String provider) {
        this.provider = provider;
    }
}
