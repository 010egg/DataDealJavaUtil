// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

public class CharTypes
{
    private static final boolean[] hexFlags;
    private static final boolean[] firstIdentifierFlags;
    private static final String[] stringCache;
    private static final boolean[] identifierFlags;
    private static final boolean[] whitespaceFlags;
    
    public static boolean isHex(final char c) {
        return c < '\u0100' && CharTypes.hexFlags[c];
    }
    
    public static boolean isDigit(final char c) {
        return c >= '0' && c <= '9';
    }
    
    public static boolean isFirstIdentifierChar(final char c) {
        if (c <= CharTypes.firstIdentifierFlags.length) {
            return CharTypes.firstIdentifierFlags[c];
        }
        return c != '\u3000' && c != '\uff0c';
    }
    
    public static boolean isIdentifierChar(final char c) {
        if (c <= CharTypes.identifierFlags.length) {
            return CharTypes.identifierFlags[c];
        }
        return c != '\u3000' && c != '\uff0c' && c != '\uff09' && c != '\uff08';
    }
    
    public static String valueOf(final char ch) {
        if (ch < CharTypes.stringCache.length) {
            return CharTypes.stringCache[ch];
        }
        return null;
    }
    
    public static boolean isWhitespace(final char c) {
        return (c <= CharTypes.whitespaceFlags.length && CharTypes.whitespaceFlags[c]) || c == '\u3000';
    }
    
    public static String trim(final String value) {
        int len;
        int st;
        for (len = value.length(), st = 0; st < len && isWhitespace(value.charAt(st)); ++st) {}
        while (st < len && isWhitespace(value.charAt(len - 1))) {
            --len;
        }
        return (st > 0 || len < value.length()) ? value.substring(st, len) : value;
    }
    
    static {
        hexFlags = new boolean[256];
        for (char c = '\0'; c < CharTypes.hexFlags.length; ++c) {
            if (c >= 'A' && c <= 'F') {
                CharTypes.hexFlags[c] = true;
            }
            else if (c >= 'a' && c <= 'f') {
                CharTypes.hexFlags[c] = true;
            }
            else if (c >= '0' && c <= '9') {
                CharTypes.hexFlags[c] = true;
            }
        }
        firstIdentifierFlags = new boolean[256];
        for (char c = '\0'; c < CharTypes.firstIdentifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                CharTypes.firstIdentifierFlags[c] = true;
            }
            else if (c >= 'a' && c <= 'z') {
                CharTypes.firstIdentifierFlags[c] = true;
            }
        }
        CharTypes.firstIdentifierFlags[96] = true;
        CharTypes.firstIdentifierFlags[95] = true;
        CharTypes.firstIdentifierFlags[36] = true;
        stringCache = new String[256];
        identifierFlags = new boolean[256];
        for (char c = '\0'; c < CharTypes.identifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                CharTypes.identifierFlags[c] = true;
            }
            else if (c >= 'a' && c <= 'z') {
                CharTypes.identifierFlags[c] = true;
            }
            else if (c >= '0' && c <= '9') {
                CharTypes.identifierFlags[c] = true;
            }
        }
        CharTypes.identifierFlags[95] = true;
        CharTypes.identifierFlags[36] = true;
        CharTypes.identifierFlags[35] = true;
        for (int i = 0; i < CharTypes.identifierFlags.length; ++i) {
            if (CharTypes.identifierFlags[i]) {
                final char ch = (char)i;
                CharTypes.stringCache[i] = Character.toString(ch);
            }
        }
        whitespaceFlags = new boolean[256];
        for (int i = 0; i <= 32; ++i) {
            CharTypes.whitespaceFlags[i] = true;
        }
        CharTypes.whitespaceFlags[26] = false;
        for (int i = 127; i <= 160; ++i) {
            CharTypes.whitespaceFlags[i] = true;
        }
        CharTypes.whitespaceFlags[160] = true;
    }
}
