// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.sql.SQLException;

public class PGbytea
{
    private static final int MAX_3_BUFF_SIZE = 2097152;
    
    public static byte[] toBytes(final byte[] s) throws SQLException {
        if (s == null) {
            return null;
        }
        if (s.length < 2 || s[0] != 92 || s[1] != 120) {
            return toBytesOctalEscaped(s);
        }
        return toBytesHexEscaped(s);
    }
    
    private static byte[] toBytesHexEscaped(final byte[] s) {
        final byte[] output = new byte[(s.length - 2) / 2];
        for (int i = 0; i < output.length; ++i) {
            final byte b1 = gethex(s[2 + i * 2]);
            final byte b2 = gethex(s[2 + i * 2 + 1]);
            output[i] = (byte)(b1 << 4 | b2);
        }
        return output;
    }
    
    private static byte gethex(final byte b) {
        if (b <= 57) {
            return (byte)(b - 48);
        }
        if (b >= 97) {
            return (byte)(b - 97 + 10);
        }
        return (byte)(b - 65 + 10);
    }
    
    private static byte[] toBytesOctalEscaped(final byte[] s) {
        final int slength = s.length;
        byte[] buf = null;
        int correctSize = slength;
        if (slength > 2097152) {
            for (int i = 0; i < slength; ++i) {
                final byte current = s[i];
                if (current == 92) {
                    final byte next = s[++i];
                    if (next == 92) {
                        --correctSize;
                    }
                    else {
                        correctSize -= 3;
                    }
                }
            }
            buf = new byte[correctSize];
        }
        else {
            buf = new byte[slength];
        }
        int bufpos = 0;
        for (int j = 0; j < slength; ++j) {
            final byte nextbyte = s[j];
            if (nextbyte == 92) {
                final byte secondbyte = s[++j];
                if (secondbyte == 92) {
                    buf[bufpos++] = 92;
                }
                else {
                    int thebyte = (secondbyte - 48) * 64 + (s[++j] - 48) * 8 + (s[++j] - 48);
                    if (thebyte > 127) {
                        thebyte -= 256;
                    }
                    buf[bufpos++] = (byte)thebyte;
                }
            }
            else {
                buf[bufpos++] = nextbyte;
            }
        }
        if (bufpos == correctSize) {
            return buf;
        }
        final byte[] l_return = new byte[bufpos];
        System.arraycopy(buf, 0, l_return, 0, bufpos);
        return l_return;
    }
    
    public static String toPGString(final byte[] p_buf) throws SQLException {
        if (p_buf == null) {
            return null;
        }
        final StringBuilder l_strbuf = new StringBuilder(2 * p_buf.length);
        for (int i = 0; i < p_buf.length; ++i) {
            int l_int = p_buf[i];
            if (l_int < 0) {
                l_int += 256;
            }
            if (l_int < 32 || l_int > 126) {
                l_strbuf.append("\\");
                l_strbuf.append((char)((l_int >> 6 & 0x3) + 48));
                l_strbuf.append((char)((l_int >> 3 & 0x7) + 48));
                l_strbuf.append((char)((l_int & 0x7) + 48));
            }
            else if (p_buf[i] == 92) {
                l_strbuf.append("\\\\");
            }
            else {
                l_strbuf.append((char)p_buf[i]);
            }
        }
        return l_strbuf.toString();
    }
}
