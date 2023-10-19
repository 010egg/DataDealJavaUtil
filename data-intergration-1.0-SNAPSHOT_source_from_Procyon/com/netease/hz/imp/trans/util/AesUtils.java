// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.util.Base64;
import java.io.UnsupportedEncodingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

public class AesUtils
{
    private static final String IV_STRING = "A-16-Byte-String";
    private static final String charset = "UTF-8";
    private static final String SEED = "sps!@Esb!";
    
    public static String encrypt(final String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return encrypt(content, "sps!@Esb!");
    }
    
    public static String decrypt(final String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return decrypt(content, "sps!@Esb!");
    }
    
    public static String encrypt(final String content, final String seed) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        final byte[] contentBytes = content.getBytes("UTF-8");
        final KeyGenerator keyGenerator = getKeyGenerator(seed);
        final SecretKey originalKey = keyGenerator.generateKey();
        final byte[] encryptedBytes = aesEncryptBytes(contentBytes, originalKey.getEncoded());
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encryptedBytes);
    }
    
    public static String decrypt(final String content, final String seed) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final byte[] encryptedBytes = decoder.decode(content);
        final KeyGenerator keyGenerator = getKeyGenerator(seed);
        final SecretKey originalKey = keyGenerator.generateKey();
        final byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, originalKey.getEncoded());
        return new String(decryptedBytes, "UTF-8");
    }
    
    public static byte[] aesEncryptBytes(final byte[] contentBytes, final byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, 1);
    }
    
    public static byte[] aesDecryptBytes(final byte[] contentBytes, final byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        return cipherOperation(contentBytes, keyBytes, 2);
    }
    
    private static byte[] cipherOperation(final byte[] contentBytes, final byte[] keyBytes, final int mode) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        final SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        final byte[] initParam = "A-16-Byte-String".getBytes("UTF-8");
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, secretKey, ivParameterSpec);
        return cipher.doFinal(contentBytes);
    }
    
    private static KeyGenerator getKeyGenerator(final String seed) throws NoSuchAlgorithmException {
        final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed.getBytes());
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, random);
        return keyGenerator;
    }
}
