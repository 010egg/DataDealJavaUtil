// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.security;

import java.nio.charset.StandardCharsets;
import org.eclipse.jetty.util.TypeUtil;
import java.security.MessageDigest;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import java.io.Serializable;

public abstract class Credential implements Serializable
{
    private static final Logger LOG;
    private static final long serialVersionUID = -7760551052768181572L;
    
    public abstract boolean check(final Object p0);
    
    public static Credential getCredential(final String credential) {
        if (credential.startsWith("CRYPT:")) {
            return new Crypt(credential);
        }
        if (credential.startsWith("MD5:")) {
            return new MD5(credential);
        }
        return new Password(credential);
    }
    
    static {
        LOG = Log.getLogger(Credential.class);
    }
    
    public static class Crypt extends Credential
    {
        private static final long serialVersionUID = -2027792997664744210L;
        public static final String __TYPE = "CRYPT:";
        private final String _cooked;
        
        Crypt(final String cooked) {
            this._cooked = (cooked.startsWith("CRYPT:") ? cooked.substring("CRYPT:".length()) : cooked);
        }
        
        @Override
        public boolean check(Object credentials) {
            if (credentials instanceof char[]) {
                credentials = new String((char[])credentials);
            }
            if (!(credentials instanceof String) && !(credentials instanceof Password)) {
                Credential.LOG.warn("Can't check " + credentials.getClass() + " against CRYPT", new Object[0]);
            }
            final String passwd = credentials.toString();
            return this._cooked.equals(UnixCrypt.crypt(passwd, this._cooked));
        }
        
        public static String crypt(final String user, final String pw) {
            return "CRYPT:" + UnixCrypt.crypt(pw, user);
        }
    }
    
    public static class MD5 extends Credential
    {
        private static final long serialVersionUID = 5533846540822684240L;
        public static final String __TYPE = "MD5:";
        public static final Object __md5Lock;
        private static MessageDigest __md;
        private final byte[] _digest;
        
        MD5(String digest) {
            digest = (digest.startsWith("MD5:") ? digest.substring("MD5:".length()) : digest);
            this._digest = TypeUtil.parseBytes(digest, 16);
        }
        
        public byte[] getDigest() {
            return this._digest;
        }
        
        @Override
        public boolean check(Object credentials) {
            try {
                byte[] digest = null;
                if (credentials instanceof char[]) {
                    credentials = new String((char[])credentials);
                }
                if (credentials instanceof Password || credentials instanceof String) {
                    synchronized (MD5.__md5Lock) {
                        if (MD5.__md == null) {
                            MD5.__md = MessageDigest.getInstance("MD5");
                        }
                        MD5.__md.reset();
                        MD5.__md.update(credentials.toString().getBytes(StandardCharsets.ISO_8859_1));
                        digest = MD5.__md.digest();
                    }
                    if (digest == null || digest.length != this._digest.length) {
                        return false;
                    }
                    boolean digestMismatch = false;
                    for (int i = 0; i < digest.length; ++i) {
                        digestMismatch |= (digest[i] != this._digest[i]);
                    }
                    return !digestMismatch;
                }
                else if (credentials instanceof MD5) {
                    final MD5 md5 = (MD5)credentials;
                    if (this._digest.length != md5._digest.length) {
                        return false;
                    }
                    boolean digestMismatch2 = false;
                    for (int j = 0; j < this._digest.length; ++j) {
                        digestMismatch2 |= (this._digest[j] != md5._digest[j]);
                    }
                    return !digestMismatch2;
                }
                else {
                    if (credentials instanceof Credential) {
                        return ((Credential)credentials).check(this);
                    }
                    Credential.LOG.warn("Can't check " + credentials.getClass() + " against MD5", new Object[0]);
                    return false;
                }
            }
            catch (Exception e) {
                Credential.LOG.warn(e);
                return false;
            }
        }
        
        public static String digest(final String password) {
            try {
                final byte[] digest;
                synchronized (MD5.__md5Lock) {
                    if (MD5.__md == null) {
                        try {
                            MD5.__md = MessageDigest.getInstance("MD5");
                        }
                        catch (Exception e) {
                            Credential.LOG.warn(e);
                            return null;
                        }
                    }
                    MD5.__md.reset();
                    MD5.__md.update(password.getBytes(StandardCharsets.ISO_8859_1));
                    digest = MD5.__md.digest();
                }
                return "MD5:" + TypeUtil.toString(digest, 16);
            }
            catch (Exception e2) {
                Credential.LOG.warn(e2);
                return null;
            }
        }
        
        static {
            __md5Lock = new Object();
        }
    }
}
