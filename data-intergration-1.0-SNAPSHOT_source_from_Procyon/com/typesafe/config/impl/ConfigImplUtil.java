// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.DataOutput;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.DataInput;
import com.typesafe.config.ConfigOrigin;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.net.URISyntaxException;
import java.io.File;
import java.net.URL;
import com.typesafe.config.ConfigException;

public final class ConfigImplUtil
{
    static boolean equalsHandlingNull(final Object a, final Object b) {
        return (a != null || b == null) && (a == null || b != null) && (a == b || a.equals(b));
    }
    
    static boolean isC0Control(final int codepoint) {
        return codepoint >= 0 && codepoint <= 31;
    }
    
    public static String renderJsonString(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append('\"');
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            switch (c) {
                case '\"': {
                    sb.append("\\\"");
                    break;
                }
                case '\\': {
                    sb.append("\\\\");
                    break;
                }
                case '\n': {
                    sb.append("\\n");
                    break;
                }
                case '\b': {
                    sb.append("\\b");
                    break;
                }
                case '\f': {
                    sb.append("\\f");
                    break;
                }
                case '\r': {
                    sb.append("\\r");
                    break;
                }
                case '\t': {
                    sb.append("\\t");
                    break;
                }
                default: {
                    if (isC0Control(c)) {
                        sb.append(String.format("\\u%04x", (int)c));
                        break;
                    }
                    sb.append(c);
                    break;
                }
            }
        }
        sb.append('\"');
        return sb.toString();
    }
    
    static String renderStringUnquotedIfPossible(final String s) {
        if (s.length() == 0) {
            return renderJsonString(s);
        }
        final int first = s.codePointAt(0);
        if (Character.isDigit(first) || first == 45) {
            return renderJsonString(s);
        }
        if (s.startsWith("include") || s.startsWith("true") || s.startsWith("false") || s.startsWith("null") || s.contains("//")) {
            return renderJsonString(s);
        }
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '-') {
                return renderJsonString(s);
            }
        }
        return s;
    }
    
    static boolean isWhitespace(final int codepoint) {
        switch (codepoint) {
            case 10:
            case 32:
            case 160:
            case 8199:
            case 8239:
            case 65279: {
                return true;
            }
            default: {
                return Character.isWhitespace(codepoint);
            }
        }
    }
    
    public static String unicodeTrim(final String s) {
        final int length = s.length();
        if (length == 0) {
            return s;
        }
        int start = 0;
        while (start < length) {
            final char c = s.charAt(start);
            if (c == ' ' || c == '\n') {
                ++start;
            }
            else {
                final int cp = s.codePointAt(start);
                if (!isWhitespace(cp)) {
                    break;
                }
                start += Character.charCount(cp);
            }
        }
        int end = length;
        while (end > start) {
            final char c2 = s.charAt(end - 1);
            if (c2 == ' ' || c2 == '\n') {
                --end;
            }
            else {
                int cp2;
                int delta;
                if (Character.isLowSurrogate(c2)) {
                    cp2 = s.codePointAt(end - 2);
                    delta = 2;
                }
                else {
                    cp2 = s.codePointAt(end - 1);
                    delta = 1;
                }
                if (!isWhitespace(cp2)) {
                    break;
                }
                end -= delta;
            }
        }
        return s.substring(start, end);
    }
    
    public static ConfigException extractInitializerError(final ExceptionInInitializerError e) {
        final Throwable cause = e.getCause();
        if (cause != null && cause instanceof ConfigException) {
            return (ConfigException)cause;
        }
        throw e;
    }
    
    static File urlToFile(final URL url) {
        try {
            return new File(url.toURI());
        }
        catch (URISyntaxException e) {
            return new File(url.getPath());
        }
        catch (IllegalArgumentException e2) {
            return new File(url.getPath());
        }
    }
    
    public static String joinPath(final String... elements) {
        return new Path(elements).render();
    }
    
    public static String joinPath(final List<String> elements) {
        return joinPath((String[])elements.toArray(new String[0]));
    }
    
    public static List<String> splitPath(final String path) {
        Path p = Path.newPath(path);
        final List<String> elements = new ArrayList<String>();
        while (p != null) {
            elements.add(p.first());
            p = p.remainder();
        }
        return elements;
    }
    
    public static ConfigOrigin readOrigin(final ObjectInputStream in) throws IOException {
        return SerializedConfigValue.readOrigin(in, null);
    }
    
    public static void writeOrigin(final ObjectOutputStream out, final ConfigOrigin origin) throws IOException {
        SerializedConfigValue.writeOrigin(new DataOutputStream(out), (SimpleConfigOrigin)origin, null);
    }
    
    static String toCamelCase(final String originalName) {
        final String[] words = originalName.split("-+");
        final StringBuilder nameBuilder = new StringBuilder(originalName.length());
        for (final String word : words) {
            if (nameBuilder.length() == 0) {
                nameBuilder.append(word);
            }
            else {
                nameBuilder.append(word.substring(0, 1).toUpperCase());
                nameBuilder.append(word.substring(1));
            }
        }
        return nameBuilder.toString();
    }
}
