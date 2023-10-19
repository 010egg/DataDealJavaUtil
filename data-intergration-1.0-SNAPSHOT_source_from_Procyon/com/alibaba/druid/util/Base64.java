// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

public class Base64
{
    private static final char[] intToBase64;
    private static final char[] intToAltBase64;
    private static final byte[] base64ToInt;
    private static final byte[] altBase64ToInt;
    
    public static String byteArrayToBase64(final byte[] a) {
        return byteArrayToBase64(a, false);
    }
    
    public static String byteArrayToAltBase64(final byte[] a) {
        return byteArrayToBase64(a, true);
    }
    
    private static String byteArrayToBase64(final byte[] a, final boolean alternate) {
        final int aLen = a.length;
        final int numFullGroups = aLen / 3;
        final int numBytesInPartialGroup = aLen - 3 * numFullGroups;
        final int resultLen = 4 * ((aLen + 2) / 3);
        final StringBuilder result = new StringBuilder(resultLen);
        final char[] intToAlpha = alternate ? Base64.intToAltBase64 : Base64.intToBase64;
        int inCursor = 0;
        for (int i = 0; i < numFullGroups; ++i) {
            final int byte0 = a[inCursor++] & 0xFF;
            final int byte2 = a[inCursor++] & 0xFF;
            final int byte3 = a[inCursor++] & 0xFF;
            result.append(intToAlpha[byte0 >> 2]);
            result.append(intToAlpha[(byte0 << 4 & 0x3F) | byte2 >> 4]);
            result.append(intToAlpha[(byte2 << 2 & 0x3F) | byte3 >> 6]);
            result.append(intToAlpha[byte3 & 0x3F]);
        }
        if (numBytesInPartialGroup != 0) {
            final int byte4 = a[inCursor++] & 0xFF;
            result.append(intToAlpha[byte4 >> 2]);
            if (numBytesInPartialGroup == 1) {
                result.append(intToAlpha[byte4 << 4 & 0x3F]);
                result.append("==");
            }
            else {
                final int byte5 = a[inCursor++] & 0xFF;
                result.append(intToAlpha[(byte4 << 4 & 0x3F) | byte5 >> 4]);
                result.append(intToAlpha[byte5 << 2 & 0x3F]);
                result.append('=');
            }
        }
        return result.toString();
    }
    
    public static byte[] base64ToByteArray(final String s) {
        return base64ToByteArray(s, false);
    }
    
    public static byte[] altBase64ToByteArray(final String s) {
        return base64ToByteArray(s, true);
    }
    
    private static byte[] base64ToByteArray(final String s, final boolean alternate) {
        final byte[] alphaToInt = alternate ? Base64.altBase64ToInt : Base64.base64ToInt;
        final int sLen = s.length();
        final int numGroups = sLen / 4;
        if (4 * numGroups != sLen) {
            throw new IllegalArgumentException("String length must be a multiple of four.");
        }
        int missingBytesInLastGroup = 0;
        int numFullGroups = numGroups;
        if (sLen != 0) {
            if (s.charAt(sLen - 1) == '=') {
                ++missingBytesInLastGroup;
                --numFullGroups;
            }
            if (s.charAt(sLen - 2) == '=') {
                ++missingBytesInLastGroup;
            }
        }
        final byte[] result = new byte[3 * numGroups - missingBytesInLastGroup];
        int inCursor = 0;
        int outCursor = 0;
        for (int i = 0; i < numFullGroups; ++i) {
            final int ch0 = base64toInt(s.charAt(inCursor++), alphaToInt);
            final int ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
            final int ch3 = base64toInt(s.charAt(inCursor++), alphaToInt);
            final int ch4 = base64toInt(s.charAt(inCursor++), alphaToInt);
            result[outCursor++] = (byte)(ch0 << 2 | ch2 >> 4);
            result[outCursor++] = (byte)(ch2 << 4 | ch3 >> 2);
            result[outCursor++] = (byte)(ch3 << 6 | ch4);
        }
        if (missingBytesInLastGroup != 0) {
            final int ch5 = base64toInt(s.charAt(inCursor++), alphaToInt);
            final int ch6 = base64toInt(s.charAt(inCursor++), alphaToInt);
            result[outCursor++] = (byte)(ch5 << 2 | ch6 >> 4);
            if (missingBytesInLastGroup == 1) {
                final int ch7 = base64toInt(s.charAt(inCursor++), alphaToInt);
                result[outCursor++] = (byte)(ch6 << 4 | ch7 >> 2);
            }
        }
        return result;
    }
    
    private static int base64toInt(final char c, final byte[] alphaToInt) {
        final int result = alphaToInt[c];
        if (result < 0) {
            throw new IllegalArgumentException("Illegal character " + c);
        }
        return result;
    }
    
    static {
        intToBase64 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
        intToAltBase64 = new char[] { '!', '\"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?' };
        base64ToInt = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
        altBase64ToInt = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25 };
    }
}
