// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ssl.jdbc4;

import java.io.Console;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.Callback;
import java.util.Iterator;
import javax.naming.ldap.Rdn;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLSession;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import org.postgresql.ssl.NonValidatingFactory;
import javax.net.ssl.TrustManager;
import java.security.GeneralSecurityException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStore;
import javax.net.ssl.TrustManagerFactory;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import javax.security.auth.callback.CallbackHandler;
import javax.net.ssl.SSLContext;
import org.postgresql.PGProperty;
import java.util.Properties;
import javax.net.ssl.HostnameVerifier;
import org.postgresql.ssl.WrappedFactory;

public class LibPQFactory extends WrappedFactory implements HostnameVerifier
{
    LazyKeyManager km;
    String sslmode;
    
    public LibPQFactory(final Properties info) throws PSQLException {
        this.km = null;
        try {
            this.sslmode = PGProperty.SSL_MODE.get(info);
            final SSLContext ctx = SSLContext.getInstance("TLS");
            final String pathsep = System.getProperty("file.separator");
            boolean defaultfile = false;
            String defaultdir;
            if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
                defaultdir = System.getenv("APPDATA") + pathsep + "postgresql" + pathsep;
            }
            else {
                defaultdir = System.getProperty("user.home") + pathsep + ".postgresql" + pathsep;
            }
            String sslcertfile = PGProperty.SSL_CERT.get(info);
            if (sslcertfile == null) {
                defaultfile = true;
                sslcertfile = defaultdir + "postgresql.crt";
            }
            String sslkeyfile = PGProperty.SSL_KEY.get(info);
            if (sslkeyfile == null) {
                defaultfile = true;
                sslkeyfile = defaultdir + "postgresql.pk8";
            }
            final String sslpasswordcallback = PGProperty.SSL_PASSWORD_CALLBACK.get(info);
            CallbackHandler cbh = null;
            Label_0267: {
                if (sslpasswordcallback != null) {
                    try {
                        cbh = (CallbackHandler)AbstractJdbc4MakeSSL.instantiate(sslpasswordcallback, info, false, null);
                        break Label_0267;
                    }
                    catch (Exception e) {
                        throw new PSQLException(GT.tr("The password callback class provided {0} could not be instantiated.", sslpasswordcallback), PSQLState.CONNECTION_FAILURE, e);
                    }
                }
                cbh = new ConsoleCallbackHandler(PGProperty.SSL_PASSWORD.get(info));
            }
            this.km = new LazyKeyManager("".equals(sslcertfile) ? null : sslcertfile, "".equals(sslkeyfile) ? null : sslkeyfile, cbh, defaultfile);
            TrustManager[] tm;
            if ("verify-ca".equals(this.sslmode) || "verify-full".equals(this.sslmode)) {
                final TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
                KeyStore ks;
                try {
                    ks = KeyStore.getInstance("jks");
                }
                catch (KeyStoreException e2) {
                    throw new NoSuchAlgorithmException("jks KeyStore not available");
                }
                String sslrootcertfile = PGProperty.SSL_ROOT_CERT.get(info);
                if (sslrootcertfile == null) {
                    sslrootcertfile = defaultdir + "root.crt";
                }
                FileInputStream fis;
                try {
                    fis = new FileInputStream(sslrootcertfile);
                }
                catch (FileNotFoundException ex) {
                    throw new PSQLException(GT.tr("Could not open SSL root certificate file {0}.", new Object[] { sslrootcertfile }), PSQLState.CONNECTION_FAILURE, ex);
                }
                try {
                    final CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    final Object[] certs = cf.generateCertificates(fis).toArray(new Certificate[0]);
                    fis.close();
                    ks.load(null, null);
                    for (int i = 0; i < certs.length; ++i) {
                        ks.setCertificateEntry("cert" + i, (Certificate)certs[i]);
                    }
                    tmf.init(ks);
                }
                catch (IOException ioex) {
                    throw new PSQLException(GT.tr("Could not read SSL root certificate file {0}.", new Object[] { sslrootcertfile }), PSQLState.CONNECTION_FAILURE, ioex);
                }
                catch (GeneralSecurityException gsex) {
                    throw new PSQLException(GT.tr("Loading the SSL root certificate {0} into a TrustManager failed.", new Object[] { sslrootcertfile }), PSQLState.CONNECTION_FAILURE, gsex);
                }
                tm = tmf.getTrustManagers();
            }
            else {
                tm = new TrustManager[] { new NonValidatingFactory.NonValidatingTM() };
            }
            try {
                ctx.init(new KeyManager[] { this.km }, tm, null);
            }
            catch (KeyManagementException ex2) {
                throw new PSQLException(GT.tr("Could not initialize SSL context.", null), PSQLState.CONNECTION_FAILURE, ex2);
            }
            this._factory = ctx.getSocketFactory();
        }
        catch (NoSuchAlgorithmException ex3) {
            throw new PSQLException(GT.tr("Could not find a java cryptographic algorithm: {0}.", new Object[] { ex3.getMessage() }), PSQLState.CONNECTION_FAILURE, ex3);
        }
    }
    
    public void throwKeyManagerException() throws PSQLException {
        if (this.km != null) {
            this.km.throwKeyManagerException();
        }
    }
    
    @Override
    public boolean verify(final String hostname, final SSLSession session) {
        X509Certificate[] peerCerts;
        try {
            peerCerts = (X509Certificate[])session.getPeerCertificates();
        }
        catch (SSLPeerUnverifiedException e) {
            return false;
        }
        if (peerCerts == null || peerCerts.length == 0) {
            return false;
        }
        final X509Certificate serverCert = peerCerts[0];
        LdapName DN;
        try {
            DN = new LdapName(serverCert.getSubjectX500Principal().getName("RFC2253"));
        }
        catch (InvalidNameException e2) {
            return false;
        }
        String CN = null;
        for (final Rdn rdn : DN.getRdns()) {
            if ("CN".equals(rdn.getType())) {
                CN = (String)rdn.getValue();
                break;
            }
        }
        if (CN == null) {
            return false;
        }
        if (CN.startsWith("*")) {
            return hostname.endsWith(CN.substring(1)) && !hostname.substring(0, hostname.length() - CN.length() + 1).contains(".");
        }
        return CN.equals(hostname);
    }
    
    static class ConsoleCallbackHandler implements CallbackHandler
    {
        private char[] password;
        
        public ConsoleCallbackHandler(final String password) {
            this.password = null;
            if (password != null) {
                this.password = password.toCharArray();
            }
        }
        
        @Override
        public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            final Console cons = System.console();
            if (cons == null && this.password == null) {
                throw new UnsupportedCallbackException(callbacks[0], "Console is not available");
            }
            for (int i = 0; i < callbacks.length; ++i) {
                if (!(callbacks[i] instanceof PasswordCallback)) {
                    throw new UnsupportedCallbackException(callbacks[i]);
                }
                if (this.password == null) {
                    ((PasswordCallback)callbacks[i]).setPassword(cons.readPassword("%s", ((PasswordCallback)callbacks[i]).getPrompt()));
                }
                else {
                    ((PasswordCallback)callbacks[i]).setPassword(this.password);
                }
            }
        }
    }
}
