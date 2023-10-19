// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

public class Parser
{
    public static int parseSingleQuotes(final char[] query, int offset, boolean standardConformingStrings) {
        if (standardConformingStrings && offset >= 2 && (query[offset - 1] == 'e' || query[offset - 1] == 'E') && charTerminatesIdentifier(query[offset - 2])) {
            standardConformingStrings = false;
        }
        if (standardConformingStrings) {
            while (++offset < query.length) {
                switch (query[offset]) {
                    case '\'': {
                        return offset;
                    }
                    default: {
                        continue;
                    }
                }
            }
        }
        else {
            while (++offset < query.length) {
                switch (query[offset]) {
                    case '\\': {
                        ++offset;
                        continue;
                    }
                    case '\'': {
                        return offset;
                    }
                    default: {
                        continue;
                    }
                }
            }
        }
        return query.length;
    }
    
    public static int parseDoubleQuotes(final char[] query, int offset) {
        while (++offset < query.length && query[offset] != '\"') {}
        return offset;
    }
    
    public static int parseDollarQuotes(final char[] query, int offset) {
        if (offset + 1 < query.length && (offset == 0 || !isIdentifierContChar(query[offset - 1]))) {
            int endIdx = -1;
            if (query[offset + 1] == '$') {
                endIdx = offset + 1;
            }
            else if (isDollarQuoteStartChar(query[offset + 1])) {
                for (int d = offset + 2; d < query.length; ++d) {
                    if (query[d] == '$') {
                        endIdx = d;
                        break;
                    }
                    if (!isDollarQuoteContChar(query[d])) {
                        break;
                    }
                }
            }
            if (endIdx > 0) {
                final int tagIdx = offset;
                final int tagLen = endIdx - offset + 1;
                offset = endIdx;
                ++offset;
                while (offset < query.length) {
                    if (query[offset] == '$' && subArraysEqual(query, tagIdx, offset, tagLen)) {
                        offset += tagLen - 1;
                        break;
                    }
                    ++offset;
                }
            }
        }
        return offset;
    }
    
    public static int parseLineComment(final char[] query, int offset) {
        if (offset + 1 < query.length && query[offset + 1] == '-') {
            while (offset + 1 < query.length) {
                ++offset;
                if (query[offset] == '\r' || query[offset] == '\n') {
                    break;
                }
            }
        }
        return offset;
    }
    
    public static int parseBlockComment(final char[] query, int offset) {
        if (offset + 1 < query.length && query[offset + 1] == '*') {
            int level = 1;
            for (offset += 2; offset < query.length; ++offset) {
                switch (query[offset - 1]) {
                    case '*': {
                        if (query[offset] == '/') {
                            --level;
                            ++offset;
                            break;
                        }
                        break;
                    }
                    case '/': {
                        if (query[offset] == '*') {
                            ++level;
                            ++offset;
                            break;
                        }
                        break;
                    }
                }
                if (level == 0) {
                    --offset;
                    break;
                }
            }
        }
        return offset;
    }
    
    public static String unmarkDoubleQuestion(final String query, final boolean standardConformingStrings) {
        if (query == null) {
            return query;
        }
        final char[] aChars = query.toCharArray();
        final StringBuilder buf = new StringBuilder(aChars.length);
        int i = 0;
        int j = -1;
        while (i < aChars.length) {
            switch (aChars[i]) {
                case '\'': {
                    j = parseSingleQuotes(aChars, i, standardConformingStrings);
                    buf.append(aChars, i, j - i + 1);
                    i = j;
                    break;
                }
                case '\"': {
                    j = parseDoubleQuotes(aChars, i);
                    buf.append(aChars, i, j - i + 1);
                    i = j;
                    break;
                }
                case '-': {
                    j = parseLineComment(aChars, i);
                    buf.append(aChars, i, j - i + 1);
                    i = j;
                    break;
                }
                case '/': {
                    j = parseBlockComment(aChars, i);
                    buf.append(aChars, i, j - i + 1);
                    i = j;
                    break;
                }
                case '$': {
                    j = parseDollarQuotes(aChars, i);
                    buf.append(aChars, i, j - i + 1);
                    i = j;
                    break;
                }
                case '?': {
                    if (i + 1 < aChars.length && aChars[i + 1] == '?') {
                        buf.append("?");
                        ++i;
                        break;
                    }
                    buf.append("?");
                    break;
                }
                default: {
                    buf.append(aChars[i]);
                    break;
                }
            }
            ++i;
        }
        return buf.toString();
    }
    
    public static boolean isSpace(final char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f';
    }
    
    public static boolean isOperatorChar(final char c) {
        return ",()[].;:+-*/%^<>=~!@#&|`?".indexOf(c) != -1;
    }
    
    public static boolean isIdentifierStartChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c > '\u007f';
    }
    
    public static boolean isIdentifierContChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c > '\u007f' || (c >= '0' && c <= '9') || c == '$';
    }
    
    public static boolean charTerminatesIdentifier(final char c) {
        return c == '\"' || isSpace(c) || isOperatorChar(c);
    }
    
    public static boolean isDollarQuoteStartChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c > '\u007f';
    }
    
    public static boolean isDollarQuoteContChar(final char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c > '\u007f' || (c >= '0' && c <= '9');
    }
    
    private static boolean subArraysEqual(final char[] arr, final int offA, final int offB, final int len) {
        if (offA < 0 || offB < 0 || offA >= arr.length || offB >= arr.length || offA + len > arr.length || offB + len > arr.length) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            if (arr[offA + i] != arr[offB + i]) {
                return false;
            }
        }
        return true;
    }
}
