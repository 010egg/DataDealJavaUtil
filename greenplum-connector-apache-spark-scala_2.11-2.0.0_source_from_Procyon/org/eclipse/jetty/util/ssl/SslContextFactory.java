// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.ssl;

import org.eclipse.jetty.util.log.Log;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLParameters;
import java.net.InetSocketAddress;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.net.InetAddress;
import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.security.InvalidParameterException;
import org.eclipse.jetty.util.resource.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import org.eclipse.jetty.util.IO;
import java.io.ByteArrayOutputStream;
import javax.net.ssl.ManagerFactoryParameters;
import java.security.cert.CertPathParameters;
import javax.net.ssl.CertPathTrustManagerParameters;
import javax.net.ssl.TrustManagerFactory;
import java.security.Security;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.CertSelector;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509CertSelector;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.KeyManagerFactory;
import org.eclipse.jetty.util.security.CertificateUtils;
import javax.net.ssl.SSLEngine;
import java.security.cert.Certificate;
import java.util.List;
import java.security.cert.CRL;
import java.util.Collection;
import java.util.Arrays;
import org.eclipse.jetty.util.security.CertificateValidator;
import java.util.Collections;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import java.util.LinkedHashSet;
import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import org.eclipse.jetty.util.security.Password;
import java.io.InputStream;
import java.util.Set;
import org.eclipse.jetty.util.log.Logger;
import javax.net.ssl.TrustManager;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

public class SslContextFactory extends AbstractLifeCycle
{
    public static final TrustManager[] TRUST_ALL_CERTS;
    static final Logger LOG;
    public static final String DEFAULT_KEYMANAGERFACTORY_ALGORITHM;
    public static final String DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM;
    public static final String KEYPASSWORD_PROPERTY = "org.eclipse.jetty.ssl.keypassword";
    public static final String PASSWORD_PROPERTY = "org.eclipse.jetty.ssl.password";
    private final Set<String> _excludeProtocols;
    private final Set<String> _includeProtocols;
    private final Set<String> _excludeCipherSuites;
    private final Set<String> _includeCipherSuites;
    private String _keyStorePath;
    private String _keyStoreProvider;
    private String _keyStoreType;
    private InputStream _keyStoreInputStream;
    private String _certAlias;
    private String _trustStorePath;
    private String _trustStoreProvider;
    private String _trustStoreType;
    private InputStream _trustStoreInputStream;
    private boolean _needClientAuth;
    private boolean _wantClientAuth;
    private transient Password _keyStorePassword;
    private transient Password _keyManagerPassword;
    private transient Password _trustStorePassword;
    private String _sslProvider;
    private String _sslProtocol;
    private String _secureRandomAlgorithm;
    private String _keyManagerFactoryAlgorithm;
    private String _trustManagerFactoryAlgorithm;
    private boolean _validateCerts;
    private boolean _validatePeerCerts;
    private int _maxCertPathLength;
    private String _crlPath;
    private boolean _enableCRLDP;
    private boolean _enableOCSP;
    private String _ocspResponderURL;
    private KeyStore _keyStore;
    private KeyStore _trustStore;
    private boolean _sessionCachingEnabled;
    private int _sslSessionCacheSize;
    private int _sslSessionTimeout;
    private SSLContext _context;
    private String _endpointIdentificationAlgorithm;
    private boolean _trustAll;
    private boolean _renegotiationAllowed;
    
    public SslContextFactory() {
        this(false);
    }
    
    public SslContextFactory(final boolean trustAll) {
        this._excludeProtocols = new LinkedHashSet<String>();
        this._includeProtocols = new LinkedHashSet<String>();
        this._excludeCipherSuites = new LinkedHashSet<String>();
        this._includeCipherSuites = new LinkedHashSet<String>();
        this._keyStoreType = "JKS";
        this._trustStoreType = "JKS";
        this._needClientAuth = false;
        this._wantClientAuth = false;
        this._sslProtocol = "TLS";
        this._keyManagerFactoryAlgorithm = SslContextFactory.DEFAULT_KEYMANAGERFACTORY_ALGORITHM;
        this._trustManagerFactoryAlgorithm = SslContextFactory.DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM;
        this._maxCertPathLength = -1;
        this._enableCRLDP = false;
        this._enableOCSP = false;
        this._sessionCachingEnabled = true;
        this._endpointIdentificationAlgorithm = null;
        this._renegotiationAllowed = true;
        this.setTrustAll(trustAll);
        this.addExcludeProtocols("SSL", "SSLv2", "SSLv2Hello", "SSLv3");
    }
    
    public SslContextFactory(final String keyStorePath) {
        this._excludeProtocols = new LinkedHashSet<String>();
        this._includeProtocols = new LinkedHashSet<String>();
        this._excludeCipherSuites = new LinkedHashSet<String>();
        this._includeCipherSuites = new LinkedHashSet<String>();
        this._keyStoreType = "JKS";
        this._trustStoreType = "JKS";
        this._needClientAuth = false;
        this._wantClientAuth = false;
        this._sslProtocol = "TLS";
        this._keyManagerFactoryAlgorithm = SslContextFactory.DEFAULT_KEYMANAGERFACTORY_ALGORITHM;
        this._trustManagerFactoryAlgorithm = SslContextFactory.DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM;
        this._maxCertPathLength = -1;
        this._enableCRLDP = false;
        this._enableOCSP = false;
        this._sessionCachingEnabled = true;
        this._endpointIdentificationAlgorithm = null;
        this._renegotiationAllowed = true;
        this._keyStorePath = keyStorePath;
    }
    
    @Override
    protected void doStart() throws Exception {
        if (this._context == null) {
            if (this._keyStore == null && this._keyStoreInputStream == null && this._keyStorePath == null && this._trustStore == null && this._trustStoreInputStream == null && this._trustStorePath == null) {
                TrustManager[] trust_managers = null;
                if (this._trustAll) {
                    if (SslContextFactory.LOG.isDebugEnabled()) {
                        SslContextFactory.LOG.debug("No keystore or trust store configured.  ACCEPTING UNTRUSTED CERTIFICATES!!!!!", new Object[0]);
                    }
                    trust_managers = SslContextFactory.TRUST_ALL_CERTS;
                }
                final SecureRandom secureRandom = (this._secureRandomAlgorithm == null) ? null : SecureRandom.getInstance(this._secureRandomAlgorithm);
                final SSLContext context = (this._sslProvider == null) ? SSLContext.getInstance(this._sslProtocol) : SSLContext.getInstance(this._sslProtocol, this._sslProvider);
                context.init(null, trust_managers, secureRandom);
                this._context = context;
            }
            else {
                this.checkKeyStore();
                final KeyStore keyStore = this.loadKeyStore();
                final KeyStore trustStore = this.loadTrustStore();
                final Collection<? extends CRL> crls = this.loadCRL(this._crlPath);
                if (this._validateCerts && keyStore != null) {
                    if (this._certAlias == null) {
                        final List<String> aliases = Collections.list(keyStore.aliases());
                        this._certAlias = ((aliases.size() == 1) ? aliases.get(0) : null);
                    }
                    final Certificate cert = (this._certAlias == null) ? null : keyStore.getCertificate(this._certAlias);
                    if (cert == null) {
                        throw new Exception("No certificate found in the keystore" + ((this._certAlias == null) ? "" : (" for alias " + this._certAlias)));
                    }
                    final CertificateValidator validator = new CertificateValidator(trustStore, crls);
                    validator.setMaxCertPathLength(this._maxCertPathLength);
                    validator.setEnableCRLDP(this._enableCRLDP);
                    validator.setEnableOCSP(this._enableOCSP);
                    validator.setOcspResponderURL(this._ocspResponderURL);
                    validator.validate(keyStore, cert);
                }
                final KeyManager[] keyManagers = this.getKeyManagers(keyStore);
                final TrustManager[] trustManagers = this.getTrustManagers(trustStore, crls);
                final SecureRandom secureRandom2 = (this._secureRandomAlgorithm == null) ? null : SecureRandom.getInstance(this._secureRandomAlgorithm);
                final SSLContext context2 = (this._sslProvider == null) ? SSLContext.getInstance(this._sslProtocol) : SSLContext.getInstance(this._sslProtocol, this._sslProvider);
                context2.init(keyManagers, trustManagers, secureRandom2);
                this._context = context2;
            }
            final SSLEngine engine = this.newSSLEngine();
            if (SslContextFactory.LOG.isDebugEnabled()) {
                SslContextFactory.LOG.debug("Enabled Protocols {} of {}", Arrays.asList(engine.getEnabledProtocols()), Arrays.asList(engine.getSupportedProtocols()));
                SslContextFactory.LOG.debug("Enabled Ciphers   {} of {}", Arrays.asList(engine.getEnabledCipherSuites()), Arrays.asList(engine.getSupportedCipherSuites()));
            }
        }
    }
    
    @Override
    protected void doStop() throws Exception {
        this._context = null;
        super.doStop();
    }
    
    public String[] getExcludeProtocols() {
        return this._excludeProtocols.toArray(new String[this._excludeProtocols.size()]);
    }
    
    public void setExcludeProtocols(final String... protocols) {
        this.checkNotStarted();
        this._excludeProtocols.clear();
        this._excludeProtocols.addAll(Arrays.asList(protocols));
    }
    
    public void addExcludeProtocols(final String... protocol) {
        this.checkNotStarted();
        this._excludeProtocols.addAll(Arrays.asList(protocol));
    }
    
    public String[] getIncludeProtocols() {
        return this._includeProtocols.toArray(new String[this._includeProtocols.size()]);
    }
    
    public void setIncludeProtocols(final String... protocols) {
        this.checkNotStarted();
        this._includeProtocols.clear();
        this._includeProtocols.addAll(Arrays.asList(protocols));
    }
    
    public String[] getExcludeCipherSuites() {
        return this._excludeCipherSuites.toArray(new String[this._excludeCipherSuites.size()]);
    }
    
    public void setExcludeCipherSuites(final String... cipherSuites) {
        this.checkNotStarted();
        this._excludeCipherSuites.clear();
        this._excludeCipherSuites.addAll(Arrays.asList(cipherSuites));
    }
    
    public void addExcludeCipherSuites(final String... cipher) {
        this.checkNotStarted();
        this._excludeCipherSuites.addAll(Arrays.asList(cipher));
    }
    
    public String[] getIncludeCipherSuites() {
        return this._includeCipherSuites.toArray(new String[this._includeCipherSuites.size()]);
    }
    
    public void setIncludeCipherSuites(final String... cipherSuites) {
        this.checkNotStarted();
        this._includeCipherSuites.clear();
        this._includeCipherSuites.addAll(Arrays.asList(cipherSuites));
    }
    
    public String getKeyStorePath() {
        return this._keyStorePath;
    }
    
    public void setKeyStorePath(final String keyStorePath) {
        this.checkNotStarted();
        this._keyStorePath = keyStorePath;
    }
    
    public String getKeyStoreProvider() {
        return this._keyStoreProvider;
    }
    
    public void setKeyStoreProvider(final String keyStoreProvider) {
        this.checkNotStarted();
        this._keyStoreProvider = keyStoreProvider;
    }
    
    public String getKeyStoreType() {
        return this._keyStoreType;
    }
    
    public void setKeyStoreType(final String keyStoreType) {
        this.checkNotStarted();
        this._keyStoreType = keyStoreType;
    }
    
    public String getCertAlias() {
        return this._certAlias;
    }
    
    public void setCertAlias(final String certAlias) {
        this.checkNotStarted();
        this._certAlias = certAlias;
    }
    
    public String getTrustStore() {
        return this._trustStorePath;
    }
    
    public void setTrustStorePath(final String trustStorePath) {
        this.checkNotStarted();
        this._trustStorePath = trustStorePath;
    }
    
    public String getTrustStoreProvider() {
        return this._trustStoreProvider;
    }
    
    public void setTrustStoreProvider(final String trustStoreProvider) {
        this.checkNotStarted();
        this._trustStoreProvider = trustStoreProvider;
    }
    
    public String getTrustStoreType() {
        return this._trustStoreType;
    }
    
    public void setTrustStoreType(final String trustStoreType) {
        this.checkNotStarted();
        this._trustStoreType = trustStoreType;
    }
    
    public boolean getNeedClientAuth() {
        return this._needClientAuth;
    }
    
    public void setNeedClientAuth(final boolean needClientAuth) {
        this.checkNotStarted();
        this._needClientAuth = needClientAuth;
    }
    
    public boolean getWantClientAuth() {
        return this._wantClientAuth;
    }
    
    public void setWantClientAuth(final boolean wantClientAuth) {
        this.checkNotStarted();
        this._wantClientAuth = wantClientAuth;
    }
    
    public boolean isValidateCerts() {
        return this._validateCerts;
    }
    
    public void setValidateCerts(final boolean validateCerts) {
        this.checkNotStarted();
        this._validateCerts = validateCerts;
    }
    
    public boolean isValidatePeerCerts() {
        return this._validatePeerCerts;
    }
    
    public void setValidatePeerCerts(final boolean validatePeerCerts) {
        this.checkNotStarted();
        this._validatePeerCerts = validatePeerCerts;
    }
    
    public void setKeyStorePassword(final String password) {
        this.checkNotStarted();
        this._keyStorePassword = Password.getPassword("org.eclipse.jetty.ssl.password", password, null);
    }
    
    public void setKeyManagerPassword(final String password) {
        this.checkNotStarted();
        this._keyManagerPassword = Password.getPassword("org.eclipse.jetty.ssl.keypassword", password, null);
    }
    
    public void setTrustStorePassword(final String password) {
        this.checkNotStarted();
        this._trustStorePassword = Password.getPassword("org.eclipse.jetty.ssl.password", password, null);
    }
    
    public String getProvider() {
        return this._sslProvider;
    }
    
    public void setProvider(final String provider) {
        this.checkNotStarted();
        this._sslProvider = provider;
    }
    
    public String getProtocol() {
        return this._sslProtocol;
    }
    
    public void setProtocol(final String protocol) {
        this.checkNotStarted();
        this._sslProtocol = protocol;
    }
    
    public String getSecureRandomAlgorithm() {
        return this._secureRandomAlgorithm;
    }
    
    public void setSecureRandomAlgorithm(final String algorithm) {
        this.checkNotStarted();
        this._secureRandomAlgorithm = algorithm;
    }
    
    public String getSslKeyManagerFactoryAlgorithm() {
        return this._keyManagerFactoryAlgorithm;
    }
    
    public void setSslKeyManagerFactoryAlgorithm(final String algorithm) {
        this.checkNotStarted();
        this._keyManagerFactoryAlgorithm = algorithm;
    }
    
    public String getTrustManagerFactoryAlgorithm() {
        return this._trustManagerFactoryAlgorithm;
    }
    
    public boolean isTrustAll() {
        return this._trustAll;
    }
    
    public void setTrustAll(final boolean trustAll) {
        this._trustAll = trustAll;
        if (trustAll) {
            this.setEndpointIdentificationAlgorithm(null);
        }
    }
    
    public void setTrustManagerFactoryAlgorithm(final String algorithm) {
        this.checkNotStarted();
        this._trustManagerFactoryAlgorithm = algorithm;
    }
    
    public boolean isRenegotiationAllowed() {
        return this._renegotiationAllowed;
    }
    
    public void setRenegotiationAllowed(final boolean renegotiationAllowed) {
        this._renegotiationAllowed = renegotiationAllowed;
    }
    
    public String getCrlPath() {
        return this._crlPath;
    }
    
    public void setCrlPath(final String crlPath) {
        this.checkNotStarted();
        this._crlPath = crlPath;
    }
    
    public int getMaxCertPathLength() {
        return this._maxCertPathLength;
    }
    
    public void setMaxCertPathLength(final int maxCertPathLength) {
        this.checkNotStarted();
        this._maxCertPathLength = maxCertPathLength;
    }
    
    public SSLContext getSslContext() {
        if (!this.isStarted()) {
            throw new IllegalStateException(this.getState());
        }
        return this._context;
    }
    
    public void setSslContext(final SSLContext sslContext) {
        this.checkNotStarted();
        this._context = sslContext;
    }
    
    public void setEndpointIdentificationAlgorithm(final String endpointIdentificationAlgorithm) {
        this._endpointIdentificationAlgorithm = endpointIdentificationAlgorithm;
    }
    
    protected KeyStore loadKeyStore() throws Exception {
        return (this._keyStore != null) ? this._keyStore : CertificateUtils.getKeyStore(this._keyStoreInputStream, this._keyStorePath, this._keyStoreType, this._keyStoreProvider, (this._keyStorePassword == null) ? null : this._keyStorePassword.toString());
    }
    
    protected KeyStore loadTrustStore() throws Exception {
        return (this._trustStore != null) ? this._trustStore : CertificateUtils.getKeyStore(this._trustStoreInputStream, this._trustStorePath, this._trustStoreType, this._trustStoreProvider, (this._trustStorePassword == null) ? null : this._trustStorePassword.toString());
    }
    
    protected Collection<? extends CRL> loadCRL(final String crlPath) throws Exception {
        return CertificateUtils.loadCRL(crlPath);
    }
    
    protected KeyManager[] getKeyManagers(final KeyStore keyStore) throws Exception {
        KeyManager[] managers = null;
        if (keyStore != null) {
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(this._keyManagerFactoryAlgorithm);
            keyManagerFactory.init(keyStore, (char[])((this._keyManagerPassword == null) ? ((this._keyStorePassword == null) ? null : this._keyStorePassword.toString().toCharArray()) : this._keyManagerPassword.toString().toCharArray()));
            managers = keyManagerFactory.getKeyManagers();
            if (this._certAlias != null) {
                for (int idx = 0; idx < managers.length; ++idx) {
                    if (managers[idx] instanceof X509KeyManager) {
                        managers[idx] = new AliasedX509ExtendedKeyManager(this._certAlias, (X509KeyManager)managers[idx]);
                    }
                }
            }
        }
        return managers;
    }
    
    protected TrustManager[] getTrustManagers(final KeyStore trustStore, final Collection<? extends CRL> crls) throws Exception {
        TrustManager[] managers = null;
        if (trustStore != null) {
            if (this._validatePeerCerts && this._trustManagerFactoryAlgorithm.equalsIgnoreCase("PKIX")) {
                final PKIXBuilderParameters pbParams = new PKIXBuilderParameters(trustStore, new X509CertSelector());
                pbParams.setMaxPathLength(this._maxCertPathLength);
                pbParams.setRevocationEnabled(true);
                if (crls != null && !crls.isEmpty()) {
                    pbParams.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters(crls)));
                }
                if (this._enableCRLDP) {
                    System.setProperty("com.sun.security.enableCRLDP", "true");
                }
                if (this._enableOCSP) {
                    Security.setProperty("ocsp.enable", "true");
                    if (this._ocspResponderURL != null) {
                        Security.setProperty("ocsp.responderURL", this._ocspResponderURL);
                    }
                }
                final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(this._trustManagerFactoryAlgorithm);
                trustManagerFactory.init(new CertPathTrustManagerParameters(pbParams));
                managers = trustManagerFactory.getTrustManagers();
            }
            else {
                final TrustManagerFactory trustManagerFactory2 = TrustManagerFactory.getInstance(this._trustManagerFactoryAlgorithm);
                trustManagerFactory2.init(trustStore);
                managers = trustManagerFactory2.getTrustManagers();
            }
        }
        return managers;
    }
    
    public void checkKeyStore() {
        if (this._context != null) {
            return;
        }
        if (this._keyStore == null && this._keyStoreInputStream == null && this._keyStorePath == null) {
            throw new IllegalStateException("SSL doesn't have a valid keystore");
        }
        if (this._trustStore == null && this._trustStoreInputStream == null && this._trustStorePath == null) {
            this._trustStore = this._keyStore;
            this._trustStorePath = this._keyStorePath;
            this._trustStoreInputStream = this._keyStoreInputStream;
            this._trustStoreType = this._keyStoreType;
            this._trustStoreProvider = this._keyStoreProvider;
            this._trustStorePassword = this._keyStorePassword;
            this._trustManagerFactoryAlgorithm = this._keyManagerFactoryAlgorithm;
        }
        if (this._keyStoreInputStream != null && this._keyStoreInputStream == this._trustStoreInputStream) {
            try {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IO.copy(this._keyStoreInputStream, baos);
                this._keyStoreInputStream.close();
                this._keyStoreInputStream = new ByteArrayInputStream(baos.toByteArray());
                this._trustStoreInputStream = new ByteArrayInputStream(baos.toByteArray());
            }
            catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }
    
    public String[] selectProtocols(final String[] enabledProtocols, final String[] supportedProtocols) {
        final Set<String> selected_protocols = new LinkedHashSet<String>();
        if (!this._includeProtocols.isEmpty()) {
            for (final String protocol : this._includeProtocols) {
                if (Arrays.asList(supportedProtocols).contains(protocol)) {
                    selected_protocols.add(protocol);
                }
            }
        }
        else {
            selected_protocols.addAll(Arrays.asList(enabledProtocols));
        }
        selected_protocols.removeAll(this._excludeProtocols);
        return selected_protocols.toArray(new String[selected_protocols.size()]);
    }
    
    public String[] selectCipherSuites(final String[] enabledCipherSuites, final String[] supportedCipherSuites) {
        final Set<String> selected_ciphers = new CopyOnWriteArraySet<String>();
        if (this._includeCipherSuites.isEmpty()) {
            selected_ciphers.addAll(Arrays.asList(enabledCipherSuites));
        }
        else {
            this.processIncludeCipherSuites(supportedCipherSuites, selected_ciphers);
        }
        this.removeExcludedCipherSuites(selected_ciphers);
        return selected_ciphers.toArray(new String[selected_ciphers.size()]);
    }
    
    protected void processIncludeCipherSuites(final String[] supportedCipherSuites, final Set<String> selected_ciphers) {
        for (final String cipherSuite : this._includeCipherSuites) {
            final Pattern p = Pattern.compile(cipherSuite);
            for (final String supportedCipherSuite : supportedCipherSuites) {
                final Matcher m = p.matcher(supportedCipherSuite);
                if (m.matches()) {
                    selected_ciphers.add(supportedCipherSuite);
                }
            }
        }
    }
    
    protected void removeExcludedCipherSuites(final Set<String> selected_ciphers) {
        for (final String excludeCipherSuite : this._excludeCipherSuites) {
            final Pattern excludeCipherPattern = Pattern.compile(excludeCipherSuite);
            for (final String selectedCipherSuite : selected_ciphers) {
                final Matcher m = excludeCipherPattern.matcher(selectedCipherSuite);
                if (m.matches()) {
                    selected_ciphers.remove(selectedCipherSuite);
                }
            }
        }
    }
    
    protected void checkNotStarted() {
        if (this.isStarted()) {
            throw new IllegalStateException("Cannot modify configuration when " + this.getState());
        }
    }
    
    public boolean isEnableCRLDP() {
        return this._enableCRLDP;
    }
    
    public void setEnableCRLDP(final boolean enableCRLDP) {
        this.checkNotStarted();
        this._enableCRLDP = enableCRLDP;
    }
    
    public boolean isEnableOCSP() {
        return this._enableOCSP;
    }
    
    public void setEnableOCSP(final boolean enableOCSP) {
        this.checkNotStarted();
        this._enableOCSP = enableOCSP;
    }
    
    public String getOcspResponderURL() {
        return this._ocspResponderURL;
    }
    
    public void setOcspResponderURL(final String ocspResponderURL) {
        this.checkNotStarted();
        this._ocspResponderURL = ocspResponderURL;
    }
    
    public void setKeyStore(final KeyStore keyStore) {
        this.checkNotStarted();
        this._keyStore = keyStore;
    }
    
    public void setTrustStore(final KeyStore trustStore) {
        this.checkNotStarted();
        this._trustStore = trustStore;
    }
    
    public void setKeyStoreResource(final Resource resource) {
        this.checkNotStarted();
        try {
            this._keyStoreInputStream = resource.getInputStream();
        }
        catch (IOException e) {
            throw new InvalidParameterException("Unable to get resource input stream for resource " + resource.toString());
        }
    }
    
    public void setTrustStoreResource(final Resource resource) {
        this.checkNotStarted();
        try {
            this._trustStoreInputStream = resource.getInputStream();
        }
        catch (IOException e) {
            throw new InvalidParameterException("Unable to get resource input stream for resource " + resource.toString());
        }
    }
    
    public boolean isSessionCachingEnabled() {
        return this._sessionCachingEnabled;
    }
    
    public void setSessionCachingEnabled(final boolean enableSessionCaching) {
        this._sessionCachingEnabled = enableSessionCaching;
    }
    
    public int getSslSessionCacheSize() {
        return this._sslSessionCacheSize;
    }
    
    public void setSslSessionCacheSize(final int sslSessionCacheSize) {
        this._sslSessionCacheSize = sslSessionCacheSize;
    }
    
    public int getSslSessionTimeout() {
        return this._sslSessionTimeout;
    }
    
    public void setSslSessionTimeout(final int sslSessionTimeout) {
        this._sslSessionTimeout = sslSessionTimeout;
    }
    
    public SSLServerSocket newSslServerSocket(final String host, final int port, final int backlog) throws IOException {
        final SSLServerSocketFactory factory = this._context.getServerSocketFactory();
        final SSLServerSocket socket = (SSLServerSocket)((host == null) ? factory.createServerSocket(port, backlog) : factory.createServerSocket(port, backlog, InetAddress.getByName(host)));
        if (this.getWantClientAuth()) {
            socket.setWantClientAuth(this.getWantClientAuth());
        }
        if (this.getNeedClientAuth()) {
            socket.setNeedClientAuth(this.getNeedClientAuth());
        }
        socket.setEnabledCipherSuites(this.selectCipherSuites(socket.getEnabledCipherSuites(), socket.getSupportedCipherSuites()));
        socket.setEnabledProtocols(this.selectProtocols(socket.getEnabledProtocols(), socket.getSupportedProtocols()));
        return socket;
    }
    
    public SSLSocket newSslSocket() throws IOException {
        final SSLSocketFactory factory = this._context.getSocketFactory();
        final SSLSocket socket = (SSLSocket)factory.createSocket();
        if (this.getWantClientAuth()) {
            socket.setWantClientAuth(this.getWantClientAuth());
        }
        if (this.getNeedClientAuth()) {
            socket.setNeedClientAuth(this.getNeedClientAuth());
        }
        socket.setEnabledCipherSuites(this.selectCipherSuites(socket.getEnabledCipherSuites(), socket.getSupportedCipherSuites()));
        socket.setEnabledProtocols(this.selectProtocols(socket.getEnabledProtocols(), socket.getSupportedProtocols()));
        return socket;
    }
    
    public SSLEngine newSSLEngine() {
        if (!this.isRunning()) {
            throw new IllegalStateException("!STARTED");
        }
        final SSLEngine sslEngine = this._context.createSSLEngine();
        this.customize(sslEngine);
        return sslEngine;
    }
    
    public SSLEngine newSSLEngine(final String host, final int port) {
        if (!this.isRunning()) {
            throw new IllegalStateException("!STARTED");
        }
        final SSLEngine sslEngine = this.isSessionCachingEnabled() ? this._context.createSSLEngine(host, port) : this._context.createSSLEngine();
        this.customize(sslEngine);
        return sslEngine;
    }
    
    public SSLEngine newSSLEngine(final InetSocketAddress address) {
        if (address == null) {
            return this.newSSLEngine();
        }
        final boolean useHostName = this.getNeedClientAuth();
        final String hostName = useHostName ? address.getHostName() : address.getAddress().getHostAddress();
        return this.newSSLEngine(hostName, address.getPort());
    }
    
    public void customize(final SSLEngine sslEngine) {
        final SSLParameters sslParams = sslEngine.getSSLParameters();
        sslParams.setEndpointIdentificationAlgorithm(this._endpointIdentificationAlgorithm);
        sslEngine.setSSLParameters(sslParams);
        if (this.getWantClientAuth()) {
            sslEngine.setWantClientAuth(this.getWantClientAuth());
        }
        if (this.getNeedClientAuth()) {
            sslEngine.setNeedClientAuth(this.getNeedClientAuth());
        }
        sslEngine.setEnabledCipherSuites(this.selectCipherSuites(sslEngine.getEnabledCipherSuites(), sslEngine.getSupportedCipherSuites()));
        sslEngine.setEnabledProtocols(this.selectProtocols(sslEngine.getEnabledProtocols(), sslEngine.getSupportedProtocols()));
    }
    
    public static X509Certificate[] getCertChain(final SSLSession sslSession) {
        try {
            final Certificate[] javaxCerts = sslSession.getPeerCertificates();
            if (javaxCerts == null || javaxCerts.length == 0) {
                return null;
            }
            final int length = javaxCerts.length;
            final X509Certificate[] javaCerts = new X509Certificate[length];
            final CertificateFactory cf = CertificateFactory.getInstance("X.509");
            for (int i = 0; i < length; ++i) {
                final byte[] bytes = javaxCerts[i].getEncoded();
                final ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                javaCerts[i] = (X509Certificate)cf.generateCertificate(stream);
            }
            return javaCerts;
        }
        catch (SSLPeerUnverifiedException pue) {
            return null;
        }
        catch (Exception e) {
            SslContextFactory.LOG.warn("EXCEPTION ", e);
            return null;
        }
    }
    
    public static int deduceKeyLength(final String cipherSuite) {
        if (cipherSuite == null) {
            return 0;
        }
        if (cipherSuite.contains("WITH_AES_256_")) {
            return 256;
        }
        if (cipherSuite.contains("WITH_RC4_128_")) {
            return 128;
        }
        if (cipherSuite.contains("WITH_AES_128_")) {
            return 128;
        }
        if (cipherSuite.contains("WITH_RC4_40_")) {
            return 40;
        }
        if (cipherSuite.contains("WITH_3DES_EDE_CBC_")) {
            return 168;
        }
        if (cipherSuite.contains("WITH_IDEA_CBC_")) {
            return 128;
        }
        if (cipherSuite.contains("WITH_RC2_CBC_40_")) {
            return 40;
        }
        if (cipherSuite.contains("WITH_DES40_CBC_")) {
            return 40;
        }
        if (cipherSuite.contains("WITH_DES_CBC_")) {
            return 56;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x(%s,%s)", this.getClass().getSimpleName(), this.hashCode(), this._keyStorePath, this._trustStorePath);
    }
    
    static {
        TRUST_ALL_CERTS = new X509TrustManager[] { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
                
                @Override
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                }
            } };
        LOG = Log.getLogger(SslContextFactory.class);
        DEFAULT_KEYMANAGERFACTORY_ALGORITHM = ((Security.getProperty("ssl.KeyManagerFactory.algorithm") == null) ? KeyManagerFactory.getDefaultAlgorithm() : Security.getProperty("ssl.KeyManagerFactory.algorithm"));
        DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM = ((Security.getProperty("ssl.TrustManagerFactory.algorithm") == null) ? TrustManagerFactory.getDefaultAlgorithm() : Security.getProperty("ssl.TrustManagerFactory.algorithm"));
    }
}
