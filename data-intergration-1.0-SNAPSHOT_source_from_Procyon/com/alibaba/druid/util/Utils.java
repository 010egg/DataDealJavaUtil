// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.io.BufferedReader;
import java.util.Set;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.io.Writer;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.io.StringWriter;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Closeable;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Date;

public class Utils
{
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    private static Date startTime;
    
    public static String read(final InputStream in) {
        if (in == null) {
            return null;
        }
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return read(reader);
    }
    
    public static String readFromResource(final String resource) throws IOException {
        if (resource == null || resource.isEmpty() || resource.contains("..") || resource.contains("?") || resource.contains(":")) {
            return null;
        }
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                in = Utils.class.getResourceAsStream(resource);
            }
            if (in == null) {
                return null;
            }
            final String text = read(in);
            return text;
        }
        finally {
            JdbcUtils.close(in);
        }
    }
    
    public static byte[] readByteArrayFromResource(final String resource) throws IOException {
        if (resource == null || resource.isEmpty() || resource.contains("..") || resource.contains("?") || resource.contains(":")) {
            return null;
        }
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                return null;
            }
            return readByteArray(in);
        }
        finally {
            JdbcUtils.close(in);
        }
    }
    
    public static byte[] readByteArray(final InputStream input) throws IOException {
        if (input == null) {
            return null;
        }
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        final byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }
    
    public static long copy(final InputStream input, final OutputStream output) throws IOException {
        final int EOF = -1;
        final byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    public static String read(final Reader reader) {
        if (reader == null) {
            return null;
        }
        try {
            final StringWriter writer = new StringWriter();
            final char[] buffer = new char[4096];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            return writer.toString();
        }
        catch (IOException ex) {
            throw new IllegalStateException("read error", ex);
        }
    }
    
    public static String read(final Reader reader, final int length) {
        if (reader == null) {
            return null;
        }
        try {
            final char[] buffer = new char[length];
            int offset = 0;
            int rest = length;
            int len;
            while ((len = reader.read(buffer, offset, rest)) != -1) {
                rest -= len;
                offset += len;
                if (rest == 0) {
                    break;
                }
            }
            return new String(buffer, 0, length - rest);
        }
        catch (IOException ex) {
            throw new IllegalStateException("read error", ex);
        }
    }
    
    public static String toString(final Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    
    public static String getStackTrace(final Throwable ex) {
        final StringWriter buf = new StringWriter();
        ex.printStackTrace(new PrintWriter(buf));
        return buf.toString();
    }
    
    public static String toString(final StackTraceElement[] stackTrace) {
        final StringBuilder buf = new StringBuilder();
        for (final StackTraceElement item : stackTrace) {
            buf.append(item.toString());
            buf.append("\n");
        }
        return buf.toString();
    }
    
    public static Boolean getBoolean(final Properties properties, final String key) {
        final String property = properties.getProperty(key);
        if ("true".equals(property)) {
            return Boolean.TRUE;
        }
        if ("false".equals(property)) {
            return Boolean.FALSE;
        }
        return null;
    }
    
    public static Integer getInteger(final Properties properties, final String key) {
        final String property = properties.getProperty(key);
        if (property == null) {
            return null;
        }
        try {
            return Integer.parseInt(property);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public static Long getLong(final Properties properties, final String key) {
        final String property = properties.getProperty(key);
        if (property == null) {
            return null;
        }
        try {
            return Long.parseLong(property);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public static Class<?> loadClass(final String className) {
        Class<?> clazz = null;
        if (className == null) {
            return null;
        }
        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException ex) {
            final ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
            if (ctxClassLoader != null) {
                try {
                    clazz = ctxClassLoader.loadClass(className);
                }
                catch (ClassNotFoundException ex2) {}
            }
            return clazz;
        }
    }
    
    public static final Date getStartTime() {
        if (Utils.startTime == null) {
            Utils.startTime = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
        }
        return Utils.startTime;
    }
    
    public static long murmurhash2_64(final String text) {
        final byte[] bytes = text.getBytes();
        return murmurhash2_64(bytes, bytes.length, -512093083);
    }
    
    public static long murmurhash2_64(final byte[] data, final int length, final int seed) {
        final long m = -4132994306676758123L;
        final int r = 47;
        long h = ((long)seed & 0xFFFFFFFFL) ^ length * -4132994306676758123L;
        for (int length2 = length / 8, i = 0; i < length2; ++i) {
            final int i2 = i * 8;
            long k = ((long)data[i2 + 0] & 0xFFL) + (((long)data[i2 + 1] & 0xFFL) << 8) + (((long)data[i2 + 2] & 0xFFL) << 16) + (((long)data[i2 + 3] & 0xFFL) << 24) + (((long)data[i2 + 4] & 0xFFL) << 32) + (((long)data[i2 + 5] & 0xFFL) << 40) + (((long)data[i2 + 6] & 0xFFL) << 48) + (((long)data[i2 + 7] & 0xFFL) << 56);
            k *= -4132994306676758123L;
            k ^= k >>> 47;
            k *= -4132994306676758123L;
            h ^= k;
            h *= -4132994306676758123L;
        }
        switch (length % 8) {
            case 7: {
                h ^= (long)(data[(length & 0xFFFFFFF8) + 6] & 0xFF) << 48;
            }
            case 6: {
                h ^= (long)(data[(length & 0xFFFFFFF8) + 5] & 0xFF) << 40;
            }
            case 5: {
                h ^= (long)(data[(length & 0xFFFFFFF8) + 4] & 0xFF) << 32;
            }
            case 4: {
                h ^= (long)(data[(length & 0xFFFFFFF8) + 3] & 0xFF) << 24;
            }
            case 3: {
                h ^= (long)(data[(length & 0xFFFFFFF8) + 2] & 0xFF) << 16;
            }
            case 2: {
                h ^= (long)(data[(length & 0xFFFFFFF8) + 1] & 0xFF) << 8;
            }
            case 1: {
                h ^= (data[length & 0xFFFFFFF8] & 0xFF);
                h *= -4132994306676758123L;
                break;
            }
        }
        h ^= h >>> 47;
        h *= -4132994306676758123L;
        h ^= h >>> 47;
        return h;
    }
    
    public static byte[] md5Bytes(final String text) {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }
        msgDigest.update(text.getBytes());
        final byte[] bytes = msgDigest.digest();
        return bytes;
    }
    
    public static String md5(final String text) {
        final byte[] bytes = md5Bytes(text);
        return HexBin.encode(bytes, false);
    }
    
    public static void putLong(final byte[] b, final int off, final long val) {
        b[off + 7] = (byte)(val >>> 0);
        b[off + 6] = (byte)(val >>> 8);
        b[off + 5] = (byte)(val >>> 16);
        b[off + 4] = (byte)(val >>> 24);
        b[off + 3] = (byte)(val >>> 32);
        b[off + 2] = (byte)(val >>> 40);
        b[off + 1] = (byte)(val >>> 48);
        b[off + 0] = (byte)(val >>> 56);
    }
    
    public static boolean equals(final Object a, final Object b) {
        return a == b || (a != null && a.equals(b));
    }
    
    public static String hex(final int hash) {
        final byte[] bytes = { (byte)(hash >>> 24), (byte)(hash >>> 16), (byte)(hash >>> 8), (byte)hash };
        final char[] chars = new char[8];
        for (int i = 0; i < 4; ++i) {
            final byte b = bytes[i];
            final int a = b & 0xFF;
            final int b2 = a >> 4;
            final int b3 = a & 0xF;
            chars[i * 2] = (char)(b2 + ((b2 < 10) ? 48 : 55));
            chars[i * 2 + 1] = (char)(b3 + ((b3 < 10) ? 48 : 55));
        }
        return new String(chars);
    }
    
    public static String hex(final long hash) {
        final byte[] bytes = { (byte)(hash >>> 56), (byte)(hash >>> 48), (byte)(hash >>> 40), (byte)(hash >>> 32), (byte)(hash >>> 24), (byte)(hash >>> 16), (byte)(hash >>> 8), (byte)hash };
        final char[] chars = new char[16];
        for (int i = 0; i < 8; ++i) {
            final byte b = bytes[i];
            final int a = b & 0xFF;
            final int b2 = a >> 4;
            final int b3 = a & 0xF;
            chars[i * 2] = (char)(b2 + ((b2 < 10) ? 48 : 55));
            chars[i * 2 + 1] = (char)(b3 + ((b3 < 10) ? 48 : 55));
        }
        return new String(chars);
    }
    
    public static String hex_t(final long hash) {
        final byte[] bytes = { (byte)(hash >>> 56), (byte)(hash >>> 48), (byte)(hash >>> 40), (byte)(hash >>> 32), (byte)(hash >>> 24), (byte)(hash >>> 16), (byte)(hash >>> 8), (byte)hash };
        final char[] chars = new char[18];
        chars[0] = 'T';
        chars[1] = '_';
        for (int i = 0; i < 8; ++i) {
            final byte b = bytes[i];
            final int a = b & 0xFF;
            final int b2 = a >> 4;
            final int b3 = a & 0xF;
            chars[i * 2 + 2] = (char)(b2 + ((b2 < 10) ? 48 : 55));
            chars[i * 2 + 3] = (char)(b3 + ((b3 < 10) ? 48 : 55));
        }
        return new String(chars);
    }
    
    public static long fnv_64(final String input) {
        return FnvHash.fnv1a_64(input);
    }
    
    public static long fnv_64_lower(final String key) {
        return FnvHash.fnv1a_64_lower(key);
    }
    
    public static long fnv_32_lower(final String key) {
        return FnvHash.fnv_32_lower(key);
    }
    
    public static void loadFromFile(final String path, final Set<String> set) {
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim().toLowerCase();
                if (line.length() == 0) {
                    continue;
                }
                set.add(line);
            }
        }
        catch (Exception ex) {}
        finally {
            JdbcUtils.close(is);
            JdbcUtils.close(reader);
        }
    }
}
