// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.repository.function;

import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.lang.reflect.Type;

public class SignatureUtils
{
    public static Type getJavaType(final String signature) {
        if (signature == null || signature.length() == 0) {
            return null;
        }
        final char c0 = signature.charAt(0);
        if (signature.length() == 1) {
            switch (c0) {
                case 'b': {
                    return Byte.TYPE;
                }
                case 's': {
                    return Short.TYPE;
                }
                case 'i': {
                    return Integer.TYPE;
                }
                case 'j': {
                    return Long.TYPE;
                }
                case 'f': {
                    return Float.TYPE;
                }
                case 'd': {
                    return Double.TYPE;
                }
                case 'c': {
                    return Character.TYPE;
                }
                case 'g': {
                    return String.class;
                }
                case 'a': {
                    return Date.class;
                }
                case 't': {
                    return Time.class;
                }
                case 'p': {
                    return Timestamp.class;
                }
                case 'B': {
                    return Byte.class;
                }
                case 'S': {
                    return Short.class;
                }
                case 'I': {
                    return Integer.class;
                }
                case 'J': {
                    return Long.class;
                }
                case 'F': {
                    return Float.class;
                }
                case 'D': {
                    return Double.class;
                }
                case 'C': {
                    return Character.class;
                }
                case 'G': {
                    return String.class;
                }
                case 'A': {
                    return Date.class;
                }
                case 'T': {
                    return Time.class;
                }
                case 'P': {
                    return Timestamp.class;
                }
            }
        }
        throw new UnsupportedOperationException("type : " + signature + " is not support.");
    }
}
