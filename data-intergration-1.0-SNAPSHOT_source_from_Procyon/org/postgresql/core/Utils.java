// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core;

import java.text.ParsePosition;
import java.text.NumberFormat;
import java.io.IOException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import java.sql.SQLException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class Utils
{
    private static final Charset utf8Charset;
    
    public static String toHexString(final byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length * 2);
        for (int i = 0; i < data.length; ++i) {
            sb.append(Integer.toHexString(data[i] >> 4 & 0xF));
            sb.append(Integer.toHexString(data[i] & 0xF));
        }
        return sb.toString();
    }
    
    public static byte[] encodeUTF8(final String str) {
        final ByteBuffer buf = Utils.utf8Charset.encode(CharBuffer.wrap(str));
        final byte[] b = new byte[buf.limit()];
        buf.get(b, 0, buf.limit());
        return b;
    }
    
    @Deprecated
    public static StringBuffer appendEscapedLiteral(StringBuffer sbuf, final String value, final boolean standardConformingStrings) throws SQLException {
        if (sbuf == null) {
            sbuf = new StringBuffer(value.length() * 11 / 10);
        }
        doAppendEscapedLiteral(sbuf, value, standardConformingStrings);
        return sbuf;
    }
    
    public static StringBuilder escapeLiteral(StringBuilder sbuf, final String value, final boolean standardConformingStrings) throws SQLException {
        if (sbuf == null) {
            sbuf = new StringBuilder(value.length() * 11 / 10);
        }
        doAppendEscapedLiteral(sbuf, value, standardConformingStrings);
        return sbuf;
    }
    
    private static void doAppendEscapedLiteral(final Appendable sbuf, final String value, final boolean standardConformingStrings) throws SQLException {
        try {
            if (standardConformingStrings) {
                for (int i = 0; i < value.length(); ++i) {
                    final char ch = value.charAt(i);
                    if (ch == '\0') {
                        throw new PSQLException(GT.tr("Zero bytes may not occur in string parameters."), PSQLState.INVALID_PARAMETER_VALUE);
                    }
                    if (ch == '\'') {
                        sbuf.append('\'');
                    }
                    sbuf.append(ch);
                }
            }
            else {
                for (int i = 0; i < value.length(); ++i) {
                    final char ch = value.charAt(i);
                    if (ch == '\0') {
                        throw new PSQLException(GT.tr("Zero bytes may not occur in string parameters."), PSQLState.INVALID_PARAMETER_VALUE);
                    }
                    if (ch == '\\' || ch == '\'') {
                        sbuf.append(ch);
                    }
                    sbuf.append(ch);
                }
            }
        }
        catch (IOException e) {
            throw new PSQLException(GT.tr("No IOException expected from StringBuffer or StringBuilder"), PSQLState.UNEXPECTED_ERROR, e);
        }
    }
    
    @Deprecated
    public static StringBuffer appendEscapedIdentifier(StringBuffer sbuf, final String value) throws SQLException {
        if (sbuf == null) {
            sbuf = new StringBuffer(2 + value.length() * 11 / 10);
        }
        doAppendEscapedIdentifier(sbuf, value);
        return sbuf;
    }
    
    public static StringBuilder escapeIdentifier(StringBuilder sbuf, final String value) throws SQLException {
        if (sbuf == null) {
            sbuf = new StringBuilder(2 + value.length() * 11 / 10);
        }
        doAppendEscapedIdentifier(sbuf, value);
        return sbuf;
    }
    
    private static void doAppendEscapedIdentifier(final Appendable sbuf, final String value) throws SQLException {
        try {
            sbuf.append('\"');
            for (int i = 0; i < value.length(); ++i) {
                final char ch = value.charAt(i);
                if (ch == '\0') {
                    throw new PSQLException(GT.tr("Zero bytes may not occur in identifiers."), PSQLState.INVALID_PARAMETER_VALUE);
                }
                if (ch == '\"') {
                    sbuf.append(ch);
                }
                sbuf.append(ch);
            }
            sbuf.append('\"');
        }
        catch (IOException e) {
            throw new PSQLException(GT.tr("No IOException expected from StringBuffer or StringBuilder"), PSQLState.UNEXPECTED_ERROR, e);
        }
    }
    
    public static int parseServerVersionStr(final String serverVersion) throws NumberFormatException {
        final NumberFormat numformat = NumberFormat.getIntegerInstance();
        numformat.setGroupingUsed(false);
        final ParsePosition parsepos = new ParsePosition(0);
        if (serverVersion == null) {
            return 0;
        }
        Long parsed = (Long)numformat.parseObject(serverVersion, parsepos);
        if (parsed == null) {
            return 0;
        }
        if (parsed.intValue() >= 10000) {
            if (parsepos.getIndex() == serverVersion.length()) {
                return parsed.intValue();
            }
            throw new NumberFormatException("First major-version part equal to or greater than 10000 in invalid version string: " + serverVersion);
        }
        else {
            int vers = parsed.intValue() * 10000;
            if (parsepos.getIndex() == serverVersion.length()) {
                return 0;
            }
            if (serverVersion.charAt(parsepos.getIndex()) != '.') {
                return 0;
            }
            parsepos.setIndex(parsepos.getIndex() + 1);
            parsed = (Long)numformat.parseObject(serverVersion, parsepos);
            if (parsed == null) {
                return 0;
            }
            if (parsed.intValue() > 99) {
                throw new NumberFormatException("Unsupported second part of major version > 99 in invalid version string: " + serverVersion);
            }
            vers += parsed.intValue() * 100;
            if (parsepos.getIndex() == serverVersion.length()) {
                return vers;
            }
            if (serverVersion.charAt(parsepos.getIndex()) == '.') {
                parsepos.setIndex(parsepos.getIndex() + 1);
                parsed = (Long)numformat.parseObject(serverVersion, parsepos);
                if (parsed != null) {
                    if (parsed.intValue() > 99) {
                        throw new NumberFormatException("Unsupported minor version value > 99 in invalid version string: " + serverVersion);
                    }
                    vers += parsed.intValue();
                }
                return vers;
            }
            return vers;
        }
    }
    
    static {
        utf8Charset = Charset.forName("UTF-8");
    }
}
