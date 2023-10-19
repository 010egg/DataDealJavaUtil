// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

public final class Fnv
{
    public static final long MAGIC_HASH_CODE = -3750763034362895579L;
    public static final long MAGIC_PRIME = 1099511628211L;
    
    public static long hashCode64LCase(final String name) {
        boolean ascii = true;
        long nameValue = 0L;
        int scoreCount = 0;
        for (int i = 0; i < name.length(); ++i) {
            final char ch = name.charAt(i);
            if (ch > '\u00ff' || (i == 0 && ch == '\0')) {
                ascii = false;
                break;
            }
            if (ch == '-' || ch == '_' || ch == ' ') {
                ++scoreCount;
            }
        }
        if (ascii && name.length() - scoreCount <= 8) {
            int i = name.length() - 1;
            int j = 0;
            while (i >= 0) {
                char ch2 = name.charAt(i);
                if (ch2 != '-' && ch2 != '_') {
                    if (ch2 != ' ') {
                        if (ch2 >= 'A' && ch2 <= 'Z') {
                            ch2 += ' ';
                        }
                        if (j == 0) {
                            nameValue = (byte)ch2;
                        }
                        else {
                            nameValue <<= 8;
                            nameValue += ch2;
                        }
                        ++j;
                    }
                }
                --i;
            }
            if (nameValue != 0L) {
                return nameValue;
            }
        }
        long hashCode = -3750763034362895579L;
        for (int k = 0; k < name.length(); ++k) {
            char ch3 = name.charAt(k);
            if (ch3 != '-' && ch3 != '_') {
                if (ch3 != ' ') {
                    if (ch3 >= 'A' && ch3 <= 'Z') {
                        ch3 += ' ';
                    }
                    hashCode ^= ch3;
                    hashCode *= 1099511628211L;
                }
            }
        }
        return hashCode;
    }
    
    public static long hashCode64(final String... names) {
        if (names.length == 1) {
            return hashCode64(names[0]);
        }
        long hashCode = -3750763034362895579L;
        for (final String name : names) {
            final long h = hashCode64(name);
            hashCode ^= h;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
    
    public static long hashCode64(final String name) {
        if (name.length() <= 8) {
            boolean ascii = true;
            long nameValue = 0L;
            for (int i = 0; i < name.length(); ++i) {
                final char ch = name.charAt(i);
                if (ch > '\u00ff' || (i == 0 && ch == '\0')) {
                    ascii = false;
                    break;
                }
            }
            if (ascii) {
                for (int i = name.length() - 1; i >= 0; --i) {
                    final char ch = name.charAt(i);
                    if (i == name.length() - 1) {
                        nameValue = (byte)ch;
                    }
                    else {
                        nameValue <<= 8;
                        nameValue += ch;
                    }
                }
                if (nameValue != 0L) {
                    return nameValue;
                }
            }
        }
        long hashCode = -3750763034362895579L;
        for (int j = 0; j < name.length(); ++j) {
            final char ch2 = name.charAt(j);
            hashCode ^= ch2;
            hashCode *= 1099511628211L;
        }
        return hashCode;
    }
}
