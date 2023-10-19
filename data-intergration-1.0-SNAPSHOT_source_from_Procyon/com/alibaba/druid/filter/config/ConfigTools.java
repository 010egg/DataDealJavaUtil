// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.filter.config;

import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.InvalidKeyException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.interfaces.RSAPublicKey;
import java.security.Key;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import com.alibaba.druid.util.Base64;
import java.security.cert.Certificate;
import java.io.Closeable;
import com.alibaba.druid.util.JdbcUtils;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.io.FileInputStream;
import java.security.PublicKey;

public class ConfigTools
{
    private static final String DEFAULT_PRIVATE_KEY_STRING = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAocbCrurZGbC5GArEHKlAfDSZi7gFBnd4yxOt0rwTqKBFzGyhtQLu5PRKjEiOXVa95aeIIBJ6OhC2f8FjqFUpawIDAQABAkAPejKaBYHrwUqUEEOe8lpnB6lBAsQIUFnQI/vXU4MV+MhIzW0BLVZCiarIQqUXeOhThVWXKFt8GxCykrrUsQ6BAiEA4vMVxEHBovz1di3aozzFvSMdsjTcYRRo82hS5Ru2/OECIQC2fAPoXixVTVY7bNMeuxCP4954ZkXp7fEPDINCjcQDywIgcc8XLkkPcs3Jxk7uYofaXaPbg39wuJpEmzPIxi3k0OECIGubmdpOnin3HuCP/bbjbJLNNoUdGiEmFL5hDI4UdwAdAiEAtcAwbm08bKN7pwwvyqaCBC//VnEWaq39DCzxr+Z2EIk=";
    public static final String DEFAULT_PUBLIC_KEY_STRING = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHGwq7q2RmwuRgKxBypQHw0mYu4BQZ3eMsTrdK8E6igRcxsobUC7uT0SoxIjl1WveWniCASejoQtn/BY6hVKWsCAwEAAQ==";
    
    public static void main(final String[] args) throws Exception {
        final String password = args[0];
        final String[] arr = genKeyPair(512);
        System.out.println("privateKey:" + arr[0]);
        System.out.println("publicKey:" + arr[1]);
        System.out.println("password:" + encrypt(arr[0], password));
    }
    
    public static String decrypt(final String cipherText) throws Exception {
        return decrypt((String)null, cipherText);
    }
    
    public static String decrypt(final String publicKeyText, final String cipherText) throws Exception {
        final PublicKey publicKey = getPublicKey(publicKeyText);
        return decrypt(publicKey, cipherText);
    }
    
    public static PublicKey getPublicKeyByX509(final String x509File) {
        if (x509File == null || x509File.length() == 0) {
            return getPublicKey(null);
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(x509File);
            final CertificateFactory factory = CertificateFactory.getInstance("X.509");
            final Certificate cer = factory.generateCertificate(in);
            return cer.getPublicKey();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Failed to get public key", e);
        }
        finally {
            JdbcUtils.close(in);
        }
    }
    
    public static PublicKey getPublicKey(String publicKeyText) {
        if (publicKeyText == null || publicKeyText.length() == 0) {
            publicKeyText = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHGwq7q2RmwuRgKxBypQHw0mYu4BQZ3eMsTrdK8E6igRcxsobUC7uT0SoxIjl1WveWniCASejoQtn/BY6hVKWsCAwEAAQ==";
        }
        try {
            final byte[] publicKeyBytes = Base64.base64ToByteArray(publicKeyText);
            final X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
            return keyFactory.generatePublic(x509KeySpec);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Failed to get public key", e);
        }
    }
    
    public static PublicKey getPublicKeyByPublicKeyFile(final String publicKeyFile) {
        if (publicKeyFile == null || publicKeyFile.length() == 0) {
            return getPublicKey(null);
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(publicKeyFile);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len = 0;
            final byte[] b = new byte[64];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            final byte[] publicKeyBytes = out.toByteArray();
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            final KeyFactory factory = KeyFactory.getInstance("RSA", "SunRsaSign");
            return factory.generatePublic(spec);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Failed to get public key", e);
        }
        finally {
            JdbcUtils.close(in);
        }
    }
    
    public static String decrypt(final PublicKey publicKey, final String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        try {
            cipher.init(2, publicKey);
        }
        catch (InvalidKeyException e) {
            final RSAPublicKey rsaPublicKey = (RSAPublicKey)publicKey;
            final RSAPrivateKeySpec spec = new RSAPrivateKeySpec(rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent());
            final Key fakePrivateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(2, fakePrivateKey);
        }
        if (cipherText == null || cipherText.length() == 0) {
            return cipherText;
        }
        final byte[] cipherBytes = Base64.base64ToByteArray(cipherText);
        final byte[] plainBytes = cipher.doFinal(cipherBytes);
        return new String(plainBytes);
    }
    
    public static String encrypt(final String plainText) throws Exception {
        return encrypt((String)null, plainText);
    }
    
    public static String encrypt(String key, final String plainText) throws Exception {
        if (key == null) {
            key = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAocbCrurZGbC5GArEHKlAfDSZi7gFBnd4yxOt0rwTqKBFzGyhtQLu5PRKjEiOXVa95aeIIBJ6OhC2f8FjqFUpawIDAQABAkAPejKaBYHrwUqUEEOe8lpnB6lBAsQIUFnQI/vXU4MV+MhIzW0BLVZCiarIQqUXeOhThVWXKFt8GxCykrrUsQ6BAiEA4vMVxEHBovz1di3aozzFvSMdsjTcYRRo82hS5Ru2/OECIQC2fAPoXixVTVY7bNMeuxCP4954ZkXp7fEPDINCjcQDywIgcc8XLkkPcs3Jxk7uYofaXaPbg39wuJpEmzPIxi3k0OECIGubmdpOnin3HuCP/bbjbJLNNoUdGiEmFL5hDI4UdwAdAiEAtcAwbm08bKN7pwwvyqaCBC//VnEWaq39DCzxr+Z2EIk=";
        }
        final byte[] keyBytes = Base64.base64ToByteArray(key);
        return encrypt(keyBytes, plainText);
    }
    
    public static String encrypt(final byte[] keyBytes, final String plainText) throws Exception {
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        final KeyFactory factory = KeyFactory.getInstance("RSA", "SunRsaSign");
        final PrivateKey privateKey = factory.generatePrivate(spec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        try {
            cipher.init(1, privateKey);
        }
        catch (InvalidKeyException e) {
            final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)privateKey;
            final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
            final Key fakePublicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(1, fakePublicKey);
        }
        final byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        final String encryptedString = Base64.byteArrayToBase64(encryptedBytes);
        return encryptedString;
    }
    
    public static byte[][] genKeyPairBytes(final int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        final byte[][] keyPairBytes = new byte[2][];
        final KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
        gen.initialize(keySize, new SecureRandom());
        final KeyPair pair = gen.generateKeyPair();
        keyPairBytes[0] = pair.getPrivate().getEncoded();
        keyPairBytes[1] = pair.getPublic().getEncoded();
        return keyPairBytes;
    }
    
    public static String[] genKeyPair(final int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        final byte[][] keyPairBytes = genKeyPairBytes(keySize);
        final String[] keyPairs = { Base64.byteArrayToBase64(keyPairBytes[0]), Base64.byteArrayToBase64(keyPairBytes[1]) };
        return keyPairs;
    }
}
