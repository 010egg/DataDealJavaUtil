// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.ssl.jdbc4;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.PasswordCallback;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyFactory;
import java.io.RandomAccessFile;
import java.io.File;
import java.util.Collection;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.security.cert.CertificateException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.security.cert.CertificateFactory;
import javax.security.auth.x500.X500Principal;
import java.net.Socket;
import java.security.Principal;
import org.postgresql.util.PSQLException;
import javax.security.auth.callback.CallbackHandler;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509KeyManager;

public class LazyKeyManager implements X509KeyManager
{
    private X509Certificate[] cert;
    private PrivateKey key;
    private String certfile;
    private String keyfile;
    private CallbackHandler cbh;
    private boolean defaultfile;
    private PSQLException error;
    
    public LazyKeyManager(final String certfile, final String keyfile, final CallbackHandler cbh, final boolean defaultfile) {
        this.cert = null;
        this.key = null;
        this.error = null;
        this.certfile = certfile;
        this.keyfile = keyfile;
        this.cbh = cbh;
        this.defaultfile = defaultfile;
    }
    
    public void throwKeyManagerException() throws PSQLException {
        if (this.error != null) {
            throw this.error;
        }
    }
    
    @Override
    public String chooseClientAlias(final String[] keyType, final Principal[] issuers, final Socket socket) {
        if (this.certfile == null) {
            return null;
        }
        if (issuers == null || issuers.length == 0) {
            return "user";
        }
        final X509Certificate[] certchain = this.getCertificateChain("user");
        if (certchain == null) {
            return null;
        }
        final X500Principal ourissuer = certchain[certchain.length - 1].getIssuerX500Principal();
        boolean found = false;
        for (int i = 0; i < issuers.length; ++i) {
            if (ourissuer.equals(issuers[i])) {
                found = true;
            }
        }
        return found ? "user" : null;
    }
    
    @Override
    public String chooseServerAlias(final String keyType, final Principal[] issuers, final Socket socket) {
        return null;
    }
    
    @Override
    public X509Certificate[] getCertificateChain(final String alias) {
        if (this.cert == null && this.certfile != null) {
            CertificateFactory cf;
            try {
                cf = CertificateFactory.getInstance("X.509");
            }
            catch (CertificateException ex) {
                this.error = new PSQLException(GT.tr("Could not find a java cryptographic algorithm: X.509 CertificateFactory not available.", null), PSQLState.CONNECTION_FAILURE, ex);
                return null;
            }
            Collection certs;
            try {
                certs = cf.generateCertificates(new FileInputStream(this.certfile));
            }
            catch (FileNotFoundException ioex) {
                if (!this.defaultfile) {
                    this.error = new PSQLException(GT.tr("Could not open SSL certificate file {0}.", new Object[] { this.certfile }), PSQLState.CONNECTION_FAILURE, ioex);
                }
                return null;
            }
            catch (CertificateException gsex) {
                this.error = new PSQLException(GT.tr("Loading the SSL certificate {0} into a KeyManager failed.", new Object[] { this.certfile }), PSQLState.CONNECTION_FAILURE, gsex);
                return null;
            }
            this.cert = certs.toArray(new X509Certificate[certs.size()]);
        }
        return this.cert;
    }
    
    @Override
    public String[] getClientAliases(final String keyType, final Principal[] issuers) {
        final String alias = this.chooseClientAlias(new String[] { keyType }, issuers, null);
        return (alias == null) ? new String[0] : new String[] { alias };
    }
    
    @Override
    public PrivateKey getPrivateKey(final String alias) {
        RandomAccessFile raf = null;
        try {
            if (this.key == null && this.keyfile != null) {
                if (this.cert == null && this.getCertificateChain("user") == null) {
                    return null;
                }
                try {
                    raf = new RandomAccessFile(new File(this.keyfile), "r");
                }
                catch (FileNotFoundException ex) {
                    if (!this.defaultfile) {
                        throw ex;
                    }
                    return null;
                }
                final byte[] keydata = new byte[(int)raf.length()];
                raf.readFully(keydata);
                raf.close();
                raf = null;
                final KeyFactory kf = KeyFactory.getInstance(this.cert[0].getPublicKey().getAlgorithm());
                try {
                    final KeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keydata);
                    this.key = kf.generatePrivate(pkcs8KeySpec);
                }
                catch (InvalidKeySpecException ex3) {
                    final EncryptedPrivateKeyInfo ePKInfo = new EncryptedPrivateKeyInfo(keydata);
                    Cipher cipher;
                    try {
                        cipher = Cipher.getInstance(ePKInfo.getAlgName());
                    }
                    catch (NoSuchPaddingException npex) {
                        throw new NoSuchAlgorithmException(npex.getMessage(), npex);
                    }
                    final PasswordCallback pwdcb = new PasswordCallback(GT.tr("Enter SSL password: "), false);
                    try {
                        this.cbh.handle(new Callback[] { pwdcb });
                    }
                    catch (UnsupportedCallbackException ucex) {
                        if (this.cbh instanceof LibPQFactory.ConsoleCallbackHandler && "Console is not available".equals(ucex.getMessage())) {
                            this.error = new PSQLException(GT.tr("Could not read password for SSL key file, console is not available.", null), PSQLState.CONNECTION_FAILURE, ucex);
                        }
                        else {
                            this.error = new PSQLException(GT.tr("Could not read password for SSL key file by callbackhandler {0}.", new Object[] { this.cbh.getClass().getName() }), PSQLState.CONNECTION_FAILURE, ucex);
                        }
                        return null;
                    }
                    try {
                        final PBEKeySpec pbeKeySpec = new PBEKeySpec(pwdcb.getPassword());
                        final SecretKeyFactory skFac = SecretKeyFactory.getInstance(ePKInfo.getAlgName());
                        final Key pbeKey = skFac.generateSecret(pbeKeySpec);
                        final AlgorithmParameters algParams = ePKInfo.getAlgParameters();
                        cipher.init(2, pbeKey, algParams);
                        final KeySpec pkcs8KeySpec2 = ePKInfo.getKeySpec(cipher);
                        this.key = kf.generatePrivate(pkcs8KeySpec2);
                    }
                    catch (GeneralSecurityException ikex) {
                        this.error = new PSQLException(GT.tr("Could not decrypt SSL key file {0}.", new Object[] { this.keyfile }), PSQLState.CONNECTION_FAILURE, ikex);
                        return null;
                    }
                }
            }
        }
        catch (IOException ioex) {
            if (raf != null) {
                try {
                    raf.close();
                }
                catch (IOException ex4) {}
            }
            this.error = new PSQLException(GT.tr("Could not read SSL key file {0}.", new Object[] { this.keyfile }), PSQLState.CONNECTION_FAILURE, ioex);
        }
        catch (NoSuchAlgorithmException ex2) {
            this.error = new PSQLException(GT.tr("Could not find a java cryptographic algorithm: {0}.", new Object[] { ex2.getMessage() }), PSQLState.CONNECTION_FAILURE, ex2);
            return null;
        }
        return this.key;
    }
    
    @Override
    public String[] getServerAliases(final String keyType, final Principal[] issuers) {
        return new String[0];
    }
}
