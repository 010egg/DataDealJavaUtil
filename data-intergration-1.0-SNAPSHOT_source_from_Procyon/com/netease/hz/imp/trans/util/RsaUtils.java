// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import java.security.NoSuchAlgorithmException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.PublicKey;
import java.security.Key;
import javax.crypto.Cipher;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;

public class RsaUtils
{
    public static String spsEsbPublicKey;
    
    public static String decryptByPrivateKey(final String text) throws Exception {
        return decryptByPrivateKey(RsaUtils.spsEsbPublicKey, text);
    }
    
    public static String decryptByPublicKey(final String publicKeyString, final String text) throws Exception {
        final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, publicKey);
        final byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }
    
    public static String encryptByPrivateKey(final String privateKeyString, final String text) throws Exception {
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, privateKey);
        final byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }
    
    public static String decryptByPrivateKey(final String privateKeyString, final String text) throws Exception {
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, privateKey);
        final byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }
    
    public static String encryptByPublicKey(final String publicKeyString, final String text) throws Exception {
        final X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString));
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, publicKey);
        final byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }
    
    public static RsaKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        final RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
        final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
        final String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        final String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        return new RsaKeyPair(publicKeyString, privateKeyString);
    }
    
    static {
        RsaUtils.spsEsbPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwd8tihM2/p42LBnJYAy/xJ3DiSCNw3Bwqh2+2aojTwnsdvdxqePYnV95KJwVFjnlh+JrcFLpCenNB12KhGJjAc5LvU5eW6KdtGUw1U4bS42ICywooWD6H+J314lCtE1KM8XpCqpQLEXi2N8I31g5mJSxeDEdiDRA6TzA8edbcWEYc9xT7YWh1yrAcbKqZ/aTJ94jwufmMPLRdwajJtsRcgZcWb/oPu7zzCrbjVqhkra/CTnqqyQuWKN6yPfNiXiGCjejYWOb+KAW4qXbK5NX44qOTb2uVQtVWimC8pNLRkClwdXAgPdxwYnTxKk87frCrb/DfLW/fZgLmEoIT6fQbQIDAQAB";
    }
    
    public static class RsaKeyPair
    {
        private final String publicKey;
        private final String privateKey;
        
        public RsaKeyPair(final String publicKey, final String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
        
        public String getPublicKey() {
            return this.publicKey;
        }
        
        public String getPrivateKey() {
            return this.privateKey;
        }
    }
}
