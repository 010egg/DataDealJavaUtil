// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

public final class HexBin
{
    private static final int BASE_LENGTH = 128;
    private static final int LOOKUP_LENGTH = 16;
    private static final byte[] HEX_NUMBER_TABLE;
    private static final char[] UPPER_CHARS;
    private static final char[] LOWER_CHARS;
    
    public static String encode(final byte[] bytes) {
        return encode(bytes, true);
    }
    
    public static String encode(final byte[] bytes, final boolean upperCase) {
        if (bytes == null) {
            return null;
        }
        final char[] chars = upperCase ? HexBin.UPPER_CHARS : HexBin.LOWER_CHARS;
        final char[] hex = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; ++i) {
            final int b = bytes[i] & 0xFF;
            hex[i * 2] = chars[b >> 4];
            hex[i * 2 + 1] = chars[b & 0xF];
        }
        return new String(hex);
    }
    
    public static byte[] decode(final String encoded) {
        if (encoded == null) {
            return null;
        }
        final int lengthData = encoded.length();
        if (lengthData % 2 != 0) {
            return null;
        }
        final char[] binaryData = encoded.toCharArray();
        final int lengthDecode = lengthData / 2;
        final byte[] decodedData = new byte[lengthDecode];
        for (int i = 0; i < lengthDecode; ++i) {
            char tempChar = binaryData[i * 2];
            final byte temp1 = (byte)((tempChar < '\u0080') ? HexBin.HEX_NUMBER_TABLE[tempChar] : -1);
            if (temp1 == -1) {
                return null;
            }
            tempChar = binaryData[i * 2 + 1];
            final byte temp2 = (byte)((tempChar < '\u0080') ? HexBin.HEX_NUMBER_TABLE[tempChar] : -1);
            if (temp2 == -1) {
                return null;
            }
            decodedData[i] = (byte)(temp1 << 4 | temp2);
        }
        return decodedData;
    }
    
    static {
        HEX_NUMBER_TABLE = new byte[128];
        UPPER_CHARS = new char[16];
        LOWER_CHARS = new char[16];
        for (int i = 0; i < 128; ++i) {
            HexBin.HEX_NUMBER_TABLE[i] = -1;
        }
        for (int i = 57; i >= 48; --i) {
            HexBin.HEX_NUMBER_TABLE[i] = (byte)(i - 48);
        }
        for (int i = 70; i >= 65; --i) {
            HexBin.HEX_NUMBER_TABLE[i] = (byte)(i - 65 + 10);
        }
        for (int i = 102; i >= 97; --i) {
            HexBin.HEX_NUMBER_TABLE[i] = (byte)(i - 97 + 10);
        }
        for (int i = 0; i < 10; ++i) {
            HexBin.UPPER_CHARS[i] = (char)(48 + i);
            HexBin.LOWER_CHARS[i] = (char)(48 + i);
        }
        for (int i = 10; i <= 15; ++i) {
            HexBin.UPPER_CHARS[i] = (char)(65 + i - 10);
            HexBin.LOWER_CHARS[i] = (char)(97 + i - 10);
        }
    }
}
