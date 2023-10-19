// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.time.LocalTime;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.Closeable;

public class IOUtils
{
    public static final int NULL_32;
    public static final long NULL_64;
    public static final int TRUE;
    public static final long TRUE_64;
    public static final int ALSE;
    public static final long ALSE_64;
    public static final long DOT_X0;
    static final int[] sizeTable;
    public static final int[] DIGITS_K_32;
    public static final long[] DIGITS_K_64;
    private static final byte[] MIN_INT_BYTES;
    private static final char[] MIN_INT_CHARS;
    private static final byte[] MIN_LONG;
    public static final short[] PACKED_DIGITS;
    public static final int[] PACKED_DIGITS_UTF16;
    
    public static int stringSize(final int x) {
        int i;
        for (i = 0; x > IOUtils.sizeTable[i]; ++i) {}
        return i + 1;
    }
    
    public static int stringSize(final long x) {
        long p = 10L;
        for (int i = 1; i < 19; ++i) {
            if (x < p) {
                return i;
            }
            p *= 10L;
        }
        return 19;
    }
    
    public static void getChars(int i, final int index, final byte[] buf) {
        int charPos = index;
        final boolean negative = i < 0;
        if (!negative) {
            i = -i;
        }
        while (i <= -100) {
            final int q = i / 100;
            final int r = q * 100 - i;
            i = q;
            charPos -= 2;
            JDKUtils.UNSAFE.putShort(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + charPos, IOUtils.PACKED_DIGITS[r]);
        }
        if (i < -9) {
            charPos -= 2;
            JDKUtils.UNSAFE.putShort(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + charPos, IOUtils.PACKED_DIGITS[-i]);
        }
        else {
            buf[--charPos] = (byte)(48 - i);
        }
        if (negative) {
            buf[charPos - 1] = 45;
        }
    }
    
    public static void getChars(int i, final int index, final char[] buf) {
        int charPos = index;
        final boolean negative = i < 0;
        if (!negative) {
            i = -i;
        }
        while (i <= -100) {
            final int q = i / 100;
            final int r = q * 100 - i;
            i = q;
            charPos -= 2;
            JDKUtils.UNSAFE.putInt(buf, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (charPos << 1), IOUtils.PACKED_DIGITS_UTF16[r]);
        }
        if (i < -9) {
            charPos -= 2;
            JDKUtils.UNSAFE.putInt(buf, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (charPos << 1), IOUtils.PACKED_DIGITS_UTF16[-i]);
        }
        else {
            buf[--charPos] = (char)(48 - i);
        }
        if (negative) {
            buf[charPos - 1] = '-';
        }
    }
    
    public static void getChars(long i, final int index, final byte[] buf) {
        int charPos = index;
        final boolean negative = i < 0L;
        if (!negative) {
            i = -i;
        }
        while (i <= -2147483648L) {
            final long q = i / 100L;
            charPos -= 2;
            JDKUtils.UNSAFE.putShort(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + charPos, IOUtils.PACKED_DIGITS[(int)(q * 100L - i)]);
            i = q;
        }
        int i2;
        int q2;
        for (i2 = (int)i; i2 <= -100; i2 = q2) {
            q2 = i2 / 100;
            charPos -= 2;
            JDKUtils.UNSAFE.putShort(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + charPos, IOUtils.PACKED_DIGITS[q2 * 100 - i2]);
        }
        if (i2 < -9) {
            charPos -= 2;
            JDKUtils.UNSAFE.putShort(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + charPos, IOUtils.PACKED_DIGITS[-i2]);
        }
        else {
            buf[--charPos] = (byte)(48 - i2);
        }
        if (negative) {
            buf[charPos - 1] = 45;
        }
    }
    
    public static void getChars(long i, final int index, final char[] buf) {
        int charPos = index;
        final boolean negative = i < 0L;
        if (!negative) {
            i = -i;
        }
        while (i <= -2147483648L) {
            final long q = i / 100L;
            charPos -= 2;
            JDKUtils.UNSAFE.putInt(buf, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (charPos << 1), IOUtils.PACKED_DIGITS_UTF16[(int)(q * 100L - i)]);
            i = q;
        }
        int i2;
        int q2;
        for (i2 = (int)i; i2 <= -100; i2 = q2) {
            q2 = i2 / 100;
            charPos -= 2;
            JDKUtils.UNSAFE.putInt(buf, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (charPos << 1), IOUtils.PACKED_DIGITS_UTF16[q2 * 100 - i2]);
        }
        if (i2 < -9) {
            charPos -= 2;
            JDKUtils.UNSAFE.putInt(buf, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (charPos << 1), IOUtils.PACKED_DIGITS_UTF16[-i2]);
        }
        else {
            buf[--charPos] = (char)(48 - i2);
        }
        if (negative) {
            buf[--charPos] = '-';
        }
    }
    
    public static int writeDecimal(final byte[] buf, int off, long unscaledVal, final int scale) {
        if (unscaledVal < 0L) {
            buf[off++] = 45;
            unscaledVal = -unscaledVal;
        }
        if (scale == 0) {
            return writeInt64(buf, off, unscaledVal);
        }
        final int insertionPoint = stringSize(unscaledVal) - scale;
        if (insertionPoint == 0) {
            buf[off] = 48;
            buf[off + 1] = 46;
            off += 2;
        }
        else if (insertionPoint < 0) {
            buf[off] = 48;
            buf[off + 1] = 46;
            off += 2;
            for (int i = 0; i < -insertionPoint; ++i) {
                buf[off++] = 48;
            }
        }
        off = writeInt64(buf, off, unscaledVal);
        if (insertionPoint > 0) {
            final int insertPointOff = off - scale;
            System.arraycopy(buf, insertPointOff, buf, insertPointOff + 1, scale);
            buf[insertPointOff] = 46;
            ++off;
        }
        return off;
    }
    
    public static int writeDecimal(final char[] buf, int off, long unscaledVal, final int scale) {
        if (unscaledVal < 0L) {
            buf[off++] = '-';
            unscaledVal = -unscaledVal;
        }
        if (scale == 0) {
            return writeInt64(buf, off, unscaledVal);
        }
        final int insertionPoint = stringSize(unscaledVal) - scale;
        if (insertionPoint == 0) {
            buf[off] = '0';
            buf[off + 1] = '.';
            off += 2;
        }
        else if (insertionPoint < 0) {
            buf[off] = '0';
            buf[off + 1] = '.';
            off += 2;
            for (int i = 0; i < -insertionPoint; ++i) {
                buf[off++] = '0';
            }
        }
        off = writeInt64(buf, off, unscaledVal);
        if (insertionPoint > 0) {
            final int insertPointOff = off - scale;
            System.arraycopy(buf, insertPointOff, buf, insertPointOff + 1, scale);
            buf[insertPointOff] = '.';
            ++off;
        }
        return off;
    }
    
    public static int encodeUTF8(final byte[] src, int offset, final int len, final byte[] dst, int dp) {
        final int sl = offset + len;
        while (offset < sl) {
            byte b0 = src[offset];
            byte b2 = src[offset + 1];
            offset += 2;
            if (b2 == 0 && b0 >= 0) {
                dst[dp++] = b0;
            }
            else {
                final char c = (char)((b0 & 0xFF) | (b2 & 0xFF) << 8);
                if (c < '\u0800') {
                    dst[dp] = (byte)(0xC0 | c >> 6);
                    dst[dp + 1] = (byte)(0x80 | (c & '?'));
                    dp += 2;
                }
                else if (c >= '\ud800' && c < '\ue000') {
                    final int ip = offset - 1;
                    if (c >= '\udc00') {
                        return -1;
                    }
                    int uc;
                    if (sl - ip < 2) {
                        uc = -1;
                    }
                    else {
                        b0 = src[ip + 1];
                        b2 = src[ip + 2];
                        final char d = (char)((b0 & 0xFF) | (b2 & 0xFF) << 8);
                        if (d < '\udc00' || d >= '\ue000') {
                            return -1;
                        }
                        offset += 2;
                        uc = (c << 10) + d - 56613888;
                    }
                    if (uc < 0) {
                        dst[dp++] = 63;
                    }
                    else {
                        dst[dp] = (byte)(0xF0 | uc >> 18);
                        dst[dp + 1] = (byte)(0x80 | (uc >> 12 & 0x3F));
                        dst[dp + 2] = (byte)(0x80 | (uc >> 6 & 0x3F));
                        dst[dp + 3] = (byte)(0x80 | (uc & 0x3F));
                        dp += 4;
                    }
                }
                else {
                    dst[dp] = (byte)(0xE0 | c >> 12);
                    dst[dp + 1] = (byte)(0x80 | (c >> 6 & 0x3F));
                    dst[dp + 2] = (byte)(0x80 | (c & '?'));
                    dp += 3;
                }
            }
        }
        return dp;
    }
    
    public static int encodeUTF8(final char[] src, int offset, final int len, final byte[] dst, int dp) {
        final int sl = offset + len;
        for (int dlASCII = dp + Math.min(len, dst.length); dp < dlASCII && src[offset] < '\u0080'; dst[dp++] = (byte)src[offset++]) {}
        while (offset < sl) {
            final char c = src[offset++];
            if (c < '\u0080') {
                dst[dp++] = (byte)c;
            }
            else if (c < '\u0800') {
                dst[dp] = (byte)(0xC0 | c >> 6);
                dst[dp + 1] = (byte)(0x80 | (c & '?'));
                dp += 2;
            }
            else if (c >= '\ud800' && c < '\ue000') {
                final int ip = offset - 1;
                if (c < '\udc00') {
                    int uc;
                    if (sl - ip < 2) {
                        uc = -1;
                    }
                    else {
                        final char d = src[ip + 1];
                        if (d < '\udc00' || d >= '\ue000') {
                            dst[dp++] = 63;
                            continue;
                        }
                        uc = (c << 10) + d - 56613888;
                    }
                    if (uc < 0) {
                        dst[dp++] = 63;
                    }
                    else {
                        dst[dp] = (byte)(0xF0 | uc >> 18);
                        dst[dp + 1] = (byte)(0x80 | (uc >> 12 & 0x3F));
                        dst[dp + 2] = (byte)(0x80 | (uc >> 6 & 0x3F));
                        dst[dp + 3] = (byte)(0x80 | (uc & 0x3F));
                        dp += 4;
                        ++offset;
                    }
                }
                else {
                    dst[dp++] = 63;
                }
            }
            else {
                dst[dp] = (byte)(0xE0 | c >> 12);
                dst[dp + 1] = (byte)(0x80 | (c >> 6 & 0x3F));
                dst[dp + 2] = (byte)(0x80 | (c & '?'));
                dp += 3;
            }
        }
        return dp;
    }
    
    public static boolean isNumber(final String str) {
        for (int i = 0; i < str.length(); ++i) {
            final char ch = str.charAt(i);
            if (ch == '+' || ch == '-') {
                if (i != 0) {
                    return false;
                }
            }
            else if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNumber(final char[] chars, final int off, final int len) {
        for (int i = off, end = off + len; i < end; ++i) {
            final char ch = chars[i];
            if (ch == '+' || ch == '-') {
                if (i != 0) {
                    return false;
                }
            }
            else if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNumber(final byte[] chars, final int off, final int len) {
        for (int i = off, end = off + len; i < end; ++i) {
            final char ch = (char)chars[i];
            if (ch == '+' || ch == '-') {
                if (i != 0) {
                    return false;
                }
            }
            else if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }
    
    public static void close(final Closeable x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        }
        catch (Exception ex) {}
    }
    
    public static int decodeUTF8(final byte[] src, int off, final int len, final byte[] dst) {
        final int sl = off + len;
        int dp = 0;
        while (off < sl) {
            final int b0 = src[off++];
            if (b0 >= 0) {
                dst[dp] = (byte)b0;
                dst[dp + 1] = 0;
                dp += 2;
            }
            else if (b0 >> 5 == -2 && (b0 & 0x1E) != 0x0) {
                if (off >= sl) {
                    dst[dp] = (byte)b0;
                    dst[dp + 1] = 0;
                    dp += 2;
                    break;
                }
                final int b2 = src[off++];
                if ((b2 & 0xC0) != 0x80) {
                    return -1;
                }
                final char c = (char)(b0 << 6 ^ b2 ^ 0xF80);
                dst[dp] = (byte)c;
                dst[dp + 1] = (byte)(c >> 8);
                dp += 2;
            }
            else if (b0 >> 4 == -2) {
                if (off + 1 >= sl) {
                    return -1;
                }
                final int b2 = src[off];
                final int b3 = src[off + 1];
                off += 2;
                if ((b0 == -32 && (b2 & 0xE0) == 0x80) || (b2 & 0xC0) != 0x80 || (b3 & 0xC0) != 0x80) {
                    return -1;
                }
                final char c2 = (char)(b0 << 12 ^ b2 << 6 ^ (b3 ^ 0xFFFE1F80));
                final boolean isSurrogate = c2 >= '\ud800' && c2 < '\ue000';
                if (isSurrogate) {
                    return -1;
                }
                dst[dp] = (byte)c2;
                dst[dp + 1] = (byte)(c2 >> 8);
                dp += 2;
            }
            else {
                if (b0 >> 3 != -2) {
                    return -1;
                }
                if (off + 2 >= sl) {
                    return -1;
                }
                final int b4 = src[off];
                final int b5 = src[off + 1];
                final int b6 = src[off + 2];
                off += 3;
                final int uc = b0 << 18 ^ b4 << 12 ^ b5 << 6 ^ (b6 ^ 0x381F80);
                if ((b4 & 0xC0) != 0x80 || (b5 & 0xC0) != 0x80 || (b6 & 0xC0) != 0x80 || uc < 65536 || uc >= 1114112) {
                    return -1;
                }
                char c3 = (char)((uc >>> 10) + 55232);
                dst[dp] = (byte)c3;
                dst[dp + 1] = (byte)(c3 >> 8);
                dp += 2;
                c3 = (char)((uc & 0x3FF) + 56320);
                dst[dp] = (byte)c3;
                dst[dp + 1] = (byte)(c3 >> 8);
                dp += 2;
            }
        }
        return dp;
    }
    
    public static int decodeUTF8(final byte[] src, int off, final int len, final char[] dst) {
        final int sl = off + len;
        int dp = 0;
        for (int dlASCII = Math.min(len, dst.length); dp < dlASCII && src[off] >= 0; dst[dp++] = (char)src[off++]) {}
        while (off < sl) {
            final int b1 = src[off++];
            if (b1 >= 0) {
                dst[dp++] = (char)b1;
            }
            else if (b1 >> 5 == -2 && (b1 & 0x1E) != 0x0) {
                if (off >= sl) {
                    return -1;
                }
                final int b2 = src[off++];
                if ((b2 & 0xC0) != 0x80) {
                    return -1;
                }
                dst[dp++] = (char)(b1 << 6 ^ b2 ^ 0xF80);
            }
            else if (b1 >> 4 == -2) {
                if (off + 1 >= sl) {
                    return -1;
                }
                final int b2 = src[off];
                final int b3 = src[off + 1];
                off += 2;
                if ((b1 == -32 && (b2 & 0xE0) == 0x80) || (b2 & 0xC0) != 0x80 || (b3 & 0xC0) != 0x80) {
                    return -1;
                }
                final char c = (char)(b1 << 12 ^ b2 << 6 ^ (b3 ^ 0xFFFE1F80));
                final boolean isSurrogate = c >= '\ud800' && c < '\ue000';
                if (isSurrogate) {
                    return -1;
                }
                dst[dp++] = c;
            }
            else {
                if (b1 >> 3 != -2) {
                    return -1;
                }
                if (off + 2 >= sl) {
                    return -1;
                }
                final int b2 = src[off];
                final int b3 = src[off + 1];
                final int b4 = src[off + 2];
                off += 3;
                final int uc = b1 << 18 ^ b2 << 12 ^ b3 << 6 ^ (b4 ^ 0x381F80);
                if ((b2 & 0xC0) != 0x80 || (b3 & 0xC0) != 0x80 || (b4 & 0xC0) != 0x80 || uc < 65536 || uc >= 1114112) {
                    return -1;
                }
                dst[dp] = (char)((uc >>> 10) + 55232);
                dst[dp + 1] = (char)((uc & 0x3FF) + 56320);
                dp += 2;
            }
        }
        return dp;
    }
    
    public static long lines(final File file) throws Exception {
        final FileInputStream in = new FileInputStream(file);
        try {
            final long lines = lines(in);
            in.close();
            return lines;
        }
        catch (Throwable t) {
            try {
                in.close();
            }
            catch (Throwable exception) {
                t.addSuppressed(exception);
            }
            throw t;
        }
    }
    
    public static long lines(final InputStream in) throws Exception {
        long lines = 0L;
        final byte[] buf = new byte[8192];
        while (true) {
            final int len = in.read(buf, 0, buf.length);
            if (len == -1) {
                break;
            }
            for (final byte b : buf) {
                if (b == 10) {
                    ++lines;
                }
            }
        }
        return lines;
    }
    
    public static int writeLocalDate(final byte[] bytes, int off, int year, final int month, final int dayOfMonth) {
        if (year < 0) {
            bytes[off++] = 45;
            year = -year;
        }
        else if (year > 9999) {
            bytes[off++] = 43;
        }
        if (year < 10000) {
            final int y01 = year / 100;
            final int y2 = year - y01 * 100;
            JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, IOUtils.PACKED_DIGITS[y01]);
            JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 2L, IOUtils.PACKED_DIGITS[y2]);
            off += 4;
        }
        else {
            off = writeInt32(bytes, off, year);
        }
        bytes[off] = 45;
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 1L, IOUtils.PACKED_DIGITS[month]);
        bytes[off + 3] = 45;
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 4L, IOUtils.PACKED_DIGITS[dayOfMonth]);
        return off + 6;
    }
    
    public static int writeLocalDate(final char[] chars, int off, int year, final int month, final int dayOfMonth) {
        if (year < 0) {
            chars[off++] = '-';
            year = -year;
        }
        else if (year > 9999) {
            chars[off++] = '+';
        }
        if (year < 10000) {
            final int y01 = year / 100;
            final int y2 = year - y01 * 100;
            JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off << 1), IOUtils.PACKED_DIGITS_UTF16[y01]);
            JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 2 << 1), IOUtils.PACKED_DIGITS_UTF16[y2]);
            off += 4;
        }
        else {
            off = writeInt32(chars, off, year);
        }
        chars[off] = '-';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 1 << 1), IOUtils.PACKED_DIGITS_UTF16[month]);
        chars[off + 3] = '-';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 4 << 1), IOUtils.PACKED_DIGITS_UTF16[dayOfMonth]);
        return off + 6;
    }
    
    public static void writeLocalTime(final byte[] bytes, final int off, final int hour, final int minute, final int second) {
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off, IOUtils.PACKED_DIGITS[hour]);
        bytes[off + 2] = 58;
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 3L, IOUtils.PACKED_DIGITS[minute]);
        bytes[off + 5] = 58;
        JDKUtils.UNSAFE.putShort(bytes, JDKUtils.ARRAY_BYTE_BASE_OFFSET + off + 6L, IOUtils.PACKED_DIGITS[second]);
    }
    
    public static int writeLocalTime(final byte[] bytes, int off, final LocalTime time) {
        writeLocalTime(bytes, off, time.getHour(), time.getMinute(), time.getSecond());
        off += 8;
        final int nano = time.getNano();
        return (nano != 0) ? writeNano(bytes, off, nano) : off;
    }
    
    public static int writeNano(final byte[] bytes, int off, final int nano) {
        final int div = nano / 1000;
        final int div2 = div / 1000;
        final int rem1 = nano - div * 1000;
        putInt(bytes, off, (IOUtils.DIGITS_K_32[div2] & 0xFFFFFF00) | 0x2E);
        off += 4;
        int v;
        if (rem1 == 0) {
            final int rem2 = div - div2 * 1000;
            if (rem2 == 0) {
                return off;
            }
            v = IOUtils.DIGITS_K_32[rem2];
        }
        else {
            v = IOUtils.DIGITS_K_32[div - div2 * 1000];
        }
        bytes[off] = (byte)(v >> 8);
        bytes[off + 1] = (byte)(v >> 16);
        off += 2;
        if (rem1 == 0) {
            bytes[off] = (byte)(v >> 24);
            return off + 1;
        }
        putInt(bytes, off, (IOUtils.DIGITS_K_32[rem1] & 0xFFFFFF00) | v >> 24);
        return off + 4;
    }
    
    public static int writeNano(final char[] chars, int off, final int nano) {
        final int div = nano / 1000;
        final int div2 = div / 1000;
        final int rem1 = nano - div * 1000;
        putLong(chars, off, (IOUtils.DIGITS_K_64[div2] & 0xFFFFFFFFFFFF0000L) | IOUtils.DOT_X0);
        off += 4;
        long v;
        if (rem1 == 0) {
            final int rem2 = div - div2 * 1000;
            if (rem2 == 0) {
                return off;
            }
            v = IOUtils.DIGITS_K_64[rem2];
        }
        else {
            v = IOUtils.DIGITS_K_64[div - div2 * 1000];
        }
        chars[off] = (char)(v >> 16);
        chars[off + 1] = (char)(v >> 32);
        off += 2;
        if (rem1 == 0) {
            chars[off] = (char)(v >> 48);
            return off + 1;
        }
        putLong(chars, off, (IOUtils.DIGITS_K_64[rem1] & 0xFFFFFFFFFFFF0000L) | v >> 48);
        return off + 4;
    }
    
    public static void writeLocalTime(final char[] chars, final int off, final int hour, final int minute, final int second) {
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off << 1), IOUtils.PACKED_DIGITS_UTF16[hour]);
        chars[off + 2] = ':';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 3 << 1), IOUtils.PACKED_DIGITS_UTF16[minute]);
        chars[off + 5] = ':';
        JDKUtils.UNSAFE.putInt(chars, JDKUtils.ARRAY_CHAR_BASE_OFFSET + (off + 6 << 1), IOUtils.PACKED_DIGITS_UTF16[second]);
    }
    
    public static int writeLocalTime(final char[] chars, int off, final LocalTime time) {
        writeLocalTime(chars, off, time.getHour(), time.getMinute(), time.getSecond());
        off += 8;
        final int nano = time.getNano();
        return (nano != 0) ? writeNano(chars, off, nano) : off;
    }
    
    public static int writeInt64(final byte[] buf, int pos, final long value) {
        long i;
        if (value < 0L) {
            if (value == Long.MIN_VALUE) {
                System.arraycopy(IOUtils.MIN_LONG, 0, buf, pos, IOUtils.MIN_LONG.length);
                return pos + IOUtils.MIN_LONG.length;
            }
            i = -value;
            buf[pos++] = 45;
        }
        else {
            i = value;
        }
        if (i < 1000L) {
            final int v = IOUtils.DIGITS_K_32[(int)i];
            final int start = v & 0xFF;
            if (start == 0) {
                buf[pos] = (byte)(v >> 8);
                buf[pos + 1] = (byte)(v >> 16);
                pos += 2;
            }
            else if (start == 1) {
                buf[pos++] = (byte)(v >> 16);
            }
            buf[pos++] = (byte)(v >> 24);
            return pos;
        }
        final long q1 = i / 1000L;
        final int r1 = (int)(i - q1 * 1000L);
        final int v2 = IOUtils.DIGITS_K_32[r1];
        if (i < 1000000L) {
            final int v3 = IOUtils.DIGITS_K_32[(int)q1];
            final int start2 = v3 & 0xFF;
            if (start2 == 0) {
                buf[pos] = (byte)(v3 >> 8);
                buf[pos + 1] = (byte)(v3 >> 16);
                pos += 2;
            }
            else if (start2 == 1) {
                buf[pos++] = (byte)(v3 >> 16);
            }
            putInt(buf, pos, (v2 & 0xFFFFFF00) | v3 >> 24);
            return pos + 4;
        }
        final long q2 = q1 / 1000L;
        final int r2 = (int)(q1 - q2 * 1000L);
        final long q3 = q2 / 1000L;
        final int v4 = IOUtils.DIGITS_K_32[r2];
        if (q3 == 0L) {
            final int v5 = IOUtils.DIGITS_K_32[(int)q2];
            final int start3 = v5 & 0xFF;
            if (start3 == 0) {
                buf[pos] = (byte)(v5 >> 8);
                buf[pos + 1] = (byte)(v5 >> 16);
                pos += 2;
            }
            else if (start3 == 1) {
                buf[pos++] = (byte)(v5 >> 16);
            }
            buf[pos] = (byte)(v5 >> 24);
            buf[pos + 1] = (byte)(v4 >> 8);
            buf[pos + 2] = (byte)(v4 >> 16);
            putInt(buf, pos + 3, (v2 & 0xFFFFFF00) | v4 >> 24);
            return pos + 7;
        }
        final int r3 = (int)(q2 - q3 * 1000L);
        final int q4 = (int)(q3 / 1000L);
        final int v6 = IOUtils.DIGITS_K_32[r3];
        if (q4 == 0) {
            final int v7 = IOUtils.DIGITS_K_32[(int)q3];
            final int start4 = v7 & 0xFF;
            if (start4 == 0) {
                buf[pos] = (byte)(v7 >> 8);
                buf[pos + 1] = (byte)(v7 >> 16);
                pos += 2;
            }
            else if (start4 == 1) {
                buf[pos++] = (byte)(v7 >> 16);
            }
            buf[pos] = (byte)(v7 >> 24);
            buf[pos + 1] = (byte)(v6 >> 8);
            putInt(buf, pos + 2, (v4 & 0xFFFF00) << 8 | v6 >> 16);
            putInt(buf, pos + 6, (v2 & 0xFFFFFF00) | v4 >> 24);
            return pos + 10;
        }
        final int r4 = (int)(q3 - q4 * 1000);
        final int q5 = q4 / 1000;
        final int v8 = IOUtils.DIGITS_K_32[r4];
        if (q5 == 0) {
            final int v9 = IOUtils.DIGITS_K_32[q4];
            final int start5 = v9 & 0xFF;
            if (start5 == 0) {
                buf[pos] = (byte)(v9 >> 8);
                buf[pos + 1] = (byte)(v9 >> 16);
                pos += 2;
            }
            else if (start5 == 1) {
                buf[pos++] = (byte)(v9 >> 16);
            }
            putInt(buf, pos, (v8 & 0xFFFFFF00) | v9 >> 24);
            buf[pos + 4] = (byte)(v6 >> 8);
            putInt(buf, pos + 5, (v4 & 0xFFFF00) << 8 | v6 >> 16);
            putInt(buf, pos + 9, (v2 & 0xFFFFFF00) | v4 >> 24);
            return pos + 13;
        }
        final int r5 = q4 - q5 * 1000;
        final int q6 = q5 / 1000;
        final int v10 = IOUtils.DIGITS_K_32[r5];
        if (q6 == 0) {
            final int v11 = IOUtils.DIGITS_K_32[q5];
            final int start6 = v11 & 0xFF;
            if (start6 == 0) {
                buf[pos] = (byte)(v11 >> 8);
                buf[pos + 1] = (byte)(v11 >> 16);
                pos += 2;
            }
            else if (start6 == 1) {
                buf[pos++] = (byte)(v11 >> 16);
            }
            buf[pos++] = (byte)(v11 >> 24);
        }
        else {
            putInt(buf, pos, (IOUtils.DIGITS_K_32[q5 - q6 * 1000] & 0xFFFFFF00) | q6 + 48);
            pos += 4;
        }
        buf[pos] = (byte)(v10 >> 8);
        putInt(buf, pos + 1, (v8 & 0xFFFF00) << 8 | v10 >> 16);
        putInt(buf, pos + 5, (v6 & 0xFFFFFF00) | v8 >> 24);
        buf[pos + 9] = (byte)(v4 >> 8);
        buf[pos + 10] = (byte)(v4 >> 16);
        putInt(buf, pos + 11, (v2 & 0xFFFFFF00) | v4 >> 24);
        return pos + 15;
    }
    
    public static int writeInt64(final char[] buf, int pos, final long value) {
        long i;
        if (value < 0L) {
            if (value == Long.MIN_VALUE) {
                for (int x = 0; x < IOUtils.MIN_LONG.length; ++x) {
                    buf[pos + x] = (char)IOUtils.MIN_LONG[x];
                }
                return pos + IOUtils.MIN_LONG.length;
            }
            i = -value;
            buf[pos++] = '-';
        }
        else {
            i = value;
        }
        if (i < 1000L) {
            final long v = IOUtils.DIGITS_K_64[(int)i];
            final int start = (byte)v;
            if (start == 0) {
                putInt(buf, pos, (int)(v >> 16));
                pos += 2;
            }
            else if (start == 1) {
                buf[pos++] = (char)(v >> 32);
            }
            buf[pos++] = (char)(v >> 48);
            return pos;
        }
        final long q1 = i / 1000L;
        final int r1 = (int)(i - q1 * 1000L);
        final long v2 = IOUtils.DIGITS_K_64[r1];
        if (i < 1000000L) {
            final long v3 = IOUtils.DIGITS_K_64[(int)q1];
            final int start2 = (byte)v3;
            if (start2 == 0) {
                putInt(buf, pos, (int)(v3 >> 16));
                pos += 2;
            }
            else if (start2 == 1) {
                buf[pos++] = (char)(v3 >> 32);
            }
            putLong(buf, pos, (v2 & 0xFFFFFFFFFFFF0000L) | v3 >> 48);
            return pos + 4;
        }
        final long q2 = q1 / 1000L;
        final int r2 = (int)(q1 - q2 * 1000L);
        final long q3 = q2 / 1000L;
        final long v4 = IOUtils.DIGITS_K_64[r2];
        if (q3 == 0L) {
            final long v5 = IOUtils.DIGITS_K_64[(int)q2];
            final int start3 = (byte)v5;
            if (start3 == 0) {
                putInt(buf, pos, (int)(v5 >> 16));
                pos += 2;
            }
            else if (start3 == 1) {
                buf[pos++] = (char)(v5 >> 32);
            }
            buf[pos] = (char)(v5 >> 48);
            putInt(buf, pos + 1, (int)(v4 >> 16));
            putLong(buf, pos + 3, (v2 & 0xFFFFFFFFFFFF0000L) | v4 >> 48);
            return pos + 7;
        }
        final int r3 = (int)(q2 - q3 * 1000L);
        final int q4 = (int)(q3 / 1000L);
        final long v6 = IOUtils.DIGITS_K_64[r3];
        if (q4 == 0) {
            final long v7 = IOUtils.DIGITS_K_64[(int)q3];
            final int start4 = (byte)v7;
            if (start4 == 0) {
                putInt(buf, pos, (int)(v7 >> 16));
                pos += 2;
            }
            else if (start4 == 1) {
                buf[pos++] = (char)(v7 >> 32);
            }
            buf[pos] = (char)(v7 >> 48);
            buf[pos + 1] = (char)(v6 >> 16);
            putLong(buf, pos + 2, (v4 & 0xFFFFFFFF0000L) << 16 | v6 >> 32);
            putLong(buf, pos + 6, (v2 & 0xFFFFFFFFFFFF0000L) | v4 >> 48);
            return pos + 10;
        }
        final int r4 = (int)(q3 - q4 * 1000);
        final int q5 = q4 / 1000;
        final long v8 = IOUtils.DIGITS_K_64[r4];
        if (q5 == 0) {
            final long v9 = IOUtils.DIGITS_K_64[q4];
            final int start5 = (byte)v9;
            if (start5 == 0) {
                putInt(buf, pos, (int)(v9 >> 16));
                pos += 2;
            }
            else if (start5 == 1) {
                buf[pos++] = (char)(v9 >> 32);
            }
            buf[pos] = (char)(v9 >> 48);
            putInt(buf, pos + 1, (int)(v8 >> 16));
            putLong(buf, pos + 3, (v6 & 0xFFFFFFFFFFFF0000L) | v8 >> 48);
            putInt(buf, pos + 7, (int)(v4 >> 16));
            putLong(buf, pos + 9, (v2 & 0xFFFFFFFFFFFF0000L) | v4 >> 48);
            return pos + 13;
        }
        final int r5 = q4 - q5 * 1000;
        final int q6 = q5 / 1000;
        final long v10 = IOUtils.DIGITS_K_64[r5];
        if (q6 == 0) {
            final int v11 = IOUtils.DIGITS_K_32[q5];
            final int start6 = (byte)v11;
            if (start6 == 0) {
                buf[pos] = (char)(byte)(v11 >> 8);
                buf[pos + 1] = (char)(byte)(v11 >> 16);
                pos += 2;
            }
            else if (start6 == 1) {
                buf[pos++] = (char)(byte)(v11 >> 16);
            }
            buf[pos++] = (char)(v11 >> 24);
        }
        else {
            putLong(buf, pos, IOUtils.DIGITS_K_64[q5 - q6 * 1000]);
            buf[pos] = (char)(q6 + 48);
            pos += 4;
        }
        putInt(buf, pos, (int)(v10 >> 16));
        putLong(buf, pos + 2, (v8 & 0xFFFFFFFFFFFF0000L) | v10 >> 48);
        buf[pos + 6] = (char)(v6 >> 16);
        putLong(buf, pos + 7, (v4 & 0xFFFFFFFF0000L) << 16 | v6 >> 32);
        putLong(buf, pos + 11, (v2 & 0xFFFFFFFFFFFF0000L) | v4 >> 48);
        return pos + 15;
    }
    
    public static int writeInt32(final byte[] buf, int pos, final int value) {
        int i;
        if (value < 0) {
            if (value == Integer.MIN_VALUE) {
                System.arraycopy(IOUtils.MIN_INT_BYTES, 0, buf, pos, IOUtils.MIN_INT_BYTES.length);
                return pos + IOUtils.MIN_INT_BYTES.length;
            }
            i = -value;
            buf[pos++] = 45;
        }
        else {
            i = value;
        }
        if (i < 1000) {
            final int v = IOUtils.DIGITS_K_32[i];
            final int start = (byte)v;
            if (start == 0) {
                putShort(buf, pos, (short)(v >> 8));
                pos += 2;
            }
            else if (start == 1) {
                buf[pos++] = (byte)(v >> 16);
            }
            buf[pos] = (byte)(v >> 24);
            return pos + 1;
        }
        final int q1 = i / 1000;
        final int r1 = i - q1 * 1000;
        final int v2 = IOUtils.DIGITS_K_32[r1];
        if (i < 1000000) {
            final int v3 = IOUtils.DIGITS_K_32[q1];
            final int start2 = (byte)v3;
            if (start2 == 0) {
                putShort(buf, pos, (short)(v3 >> 8));
                pos += 2;
            }
            else if (start2 == 1) {
                buf[pos++] = (byte)(v3 >> 16);
            }
            putInt(buf, pos, (v2 & 0xFFFFFF00) | v3 >> 24);
            return pos + 4;
        }
        final int q2 = q1 / 1000;
        final int r2 = q1 - q2 * 1000;
        final int q3 = q2 / 1000;
        final int v4 = IOUtils.DIGITS_K_32[r2];
        if (q3 == 0) {
            final int v5 = IOUtils.DIGITS_K_32[q2];
            final int start3 = (byte)v5;
            if (start3 == 0) {
                putShort(buf, pos, (short)(v5 >> 8));
                pos += 2;
            }
            else if (start3 == 1) {
                buf[pos++] = (byte)(v5 >> 16);
            }
            buf[pos++] = (byte)(v5 >> 24);
        }
        else {
            putInt(buf, pos, (IOUtils.DIGITS_K_32[q2 - q3 * 1000] & 0xFFFFFF00) | q3 + 48);
            pos += 4;
        }
        putShort(buf, pos, (short)(v4 >> 8));
        putInt(buf, pos + 2, (v2 & 0xFFFFFF00) | v4 >> 24);
        return pos + 6;
    }
    
    public static int writeInt32(final char[] buf, int pos, final int value) {
        int i;
        if (value < 0) {
            if (value == Integer.MIN_VALUE) {
                System.arraycopy(IOUtils.MIN_INT_CHARS, 0, buf, pos, IOUtils.MIN_INT_CHARS.length);
                return pos + IOUtils.MIN_INT_CHARS.length;
            }
            i = -value;
            buf[pos++] = '-';
        }
        else {
            i = value;
        }
        if (i < 1000) {
            final long v = IOUtils.DIGITS_K_64[i];
            final int start = (byte)v;
            if (start == 0) {
                putInt(buf, pos, (int)(v >> 16));
                pos += 2;
            }
            else if (start == 1) {
                buf[pos++] = (char)(v >> 32);
            }
            buf[pos] = (char)(v >> 48);
            return pos + 1;
        }
        final int q1 = i / 1000;
        final int r1 = i - q1 * 1000;
        final long v2 = IOUtils.DIGITS_K_64[r1];
        if (i < 1000000) {
            final long v3 = IOUtils.DIGITS_K_64[q1];
            final int start2 = (byte)v3;
            if (start2 == 0) {
                putInt(buf, pos, (int)(v3 >> 16));
                pos += 2;
            }
            else if (start2 == 1) {
                buf[pos++] = (char)(v3 >> 32);
            }
            putLong(buf, pos, (v2 & 0xFFFFFFFFFFFF0000L) | v3 >> 48);
            return pos + 4;
        }
        final int q2 = q1 / 1000;
        final int r2 = q1 - q2 * 1000;
        final int q3 = q2 / 1000;
        final long v4 = IOUtils.DIGITS_K_64[r2];
        if (q3 == 0) {
            final long v5 = IOUtils.DIGITS_K_64[q2];
            final int start3 = (byte)v5;
            if (start3 == 0) {
                putInt(buf, pos, (int)(v5 >> 16));
                pos += 2;
            }
            else if (start3 == 1) {
                buf[pos++] = (char)(v5 >> 32);
            }
            buf[pos++] = (char)(v5 >> 48);
        }
        else {
            putLong(buf, pos, IOUtils.DIGITS_K_64[q2 - q3 * 1000]);
            buf[pos] = (char)(q3 + 48);
            pos += 4;
        }
        putInt(buf, pos, (int)(v4 >> 16));
        putLong(buf, pos + 2, (v2 & 0xFFFFFFFFFFFF0000L) | v4 >> 48);
        return pos + 6;
    }
    
    public static void putShort(final byte[] buf, final int pos, final short v) {
        JDKUtils.UNSAFE.putShort(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + pos, JDKUtils.BIG_ENDIAN ? Short.reverseBytes(v) : v);
    }
    
    public static void putInt(final byte[] buf, final int pos, final int v) {
        JDKUtils.UNSAFE.putInt(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + pos, JDKUtils.BIG_ENDIAN ? Integer.reverseBytes(v) : v);
    }
    
    public static void putInt(final char[] buf, final int pos, final int v) {
        JDKUtils.UNSAFE.putInt(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + (pos << 1), JDKUtils.BIG_ENDIAN ? Integer.reverseBytes(v) : v);
    }
    
    public static void putLong(final char[] buf, final int pos, final long v) {
        JDKUtils.UNSAFE.putLong(buf, JDKUtils.ARRAY_BYTE_BASE_OFFSET + (pos << 1), JDKUtils.BIG_ENDIAN ? Long.reverseBytes(v) : v);
    }
    
    static {
        NULL_32 = (JDKUtils.BIG_ENDIAN ? 1853189228 : 1819047278);
        NULL_64 = (JDKUtils.BIG_ENDIAN ? 30962749956423788L : 30399761348886638L);
        TRUE = (JDKUtils.BIG_ENDIAN ? 1953658213 : 1702195828);
        TRUE_64 = (JDKUtils.BIG_ENDIAN ? 32651586932375653L : 28429475166421108L);
        ALSE = (JDKUtils.BIG_ENDIAN ? 1634497381 : 1702063201);
        ALSE_64 = (JDKUtils.BIG_ENDIAN ? 27303536604938341L : 28429466576093281L);
        DOT_X0 = (JDKUtils.BIG_ENDIAN ? 11776L : 46L);
        sizeTable = new int[] { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };
        DIGITS_K_32 = new int[1000];
        DIGITS_K_64 = new long[1000];
        MIN_INT_BYTES = "-2147483648".getBytes();
        MIN_INT_CHARS = "-2147483648".toCharArray();
        MIN_LONG = "-9223372036854775808".getBytes();
        final short[] shorts = { 12336, 12592, 12848, 13104, 13360, 13616, 13872, 14128, 14384, 14640, 12337, 12593, 12849, 13105, 13361, 13617, 13873, 14129, 14385, 14641, 12338, 12594, 12850, 13106, 13362, 13618, 13874, 14130, 14386, 14642, 12339, 12595, 12851, 13107, 13363, 13619, 13875, 14131, 14387, 14643, 12340, 12596, 12852, 13108, 13364, 13620, 13876, 14132, 14388, 14644, 12341, 12597, 12853, 13109, 13365, 13621, 13877, 14133, 14389, 14645, 12342, 12598, 12854, 13110, 13366, 13622, 13878, 14134, 14390, 14646, 12343, 12599, 12855, 13111, 13367, 13623, 13879, 14135, 14391, 14647, 12344, 12600, 12856, 13112, 13368, 13624, 13880, 14136, 14392, 14648, 12345, 12601, 12857, 13113, 13369, 13625, 13881, 14137, 14393, 14649 };
        final int[] digits = { 3145776, 3211312, 3276848, 3342384, 3407920, 3473456, 3538992, 3604528, 3670064, 3735600, 3145777, 3211313, 3276849, 3342385, 3407921, 3473457, 3538993, 3604529, 3670065, 3735601, 3145778, 3211314, 3276850, 3342386, 3407922, 3473458, 3538994, 3604530, 3670066, 3735602, 3145779, 3211315, 3276851, 3342387, 3407923, 3473459, 3538995, 3604531, 3670067, 3735603, 3145780, 3211316, 3276852, 3342388, 3407924, 3473460, 3538996, 3604532, 3670068, 3735604, 3145781, 3211317, 3276853, 3342389, 3407925, 3473461, 3538997, 3604533, 3670069, 3735605, 3145782, 3211318, 3276854, 3342390, 3407926, 3473462, 3538998, 3604534, 3670070, 3735606, 3145783, 3211319, 3276855, 3342391, 3407927, 3473463, 3538999, 3604535, 3670071, 3735607, 3145784, 3211320, 3276856, 3342392, 3407928, 3473464, 3539000, 3604536, 3670072, 3735608, 3145785, 3211321, 3276857, 3342393, 3407929, 3473465, 3539001, 3604537, 3670073, 3735609 };
        if (JDKUtils.BIG_ENDIAN) {
            for (int i = 0; i < shorts.length; ++i) {
                shorts[i] = Short.reverseBytes(shorts[i]);
            }
            for (int i = 0; i < digits.length; ++i) {
                digits[i] = Integer.reverseBytes(digits[i] << 8);
            }
        }
        PACKED_DIGITS = shorts;
        PACKED_DIGITS_UTF16 = digits;
        for (int i = 0; i < 1000; ++i) {
            final int c0 = (i < 10) ? 2 : ((i < 100) ? 1 : 0);
            final int c2 = i / 100 + 48;
            final int c3 = i / 10 % 10 + 48;
            final int c4 = i % 10 + 48;
            IOUtils.DIGITS_K_32[i] = c0 + (c2 << 8) + (c3 << 16) + (c4 << 24);
            long v = (c2 << 16) + ((long)c3 << 32) + ((long)c4 << 48);
            if (JDKUtils.BIG_ENDIAN) {
                v <<= 8;
            }
            IOUtils.DIGITS_K_64[i] = c0 + v;
        }
    }
}
