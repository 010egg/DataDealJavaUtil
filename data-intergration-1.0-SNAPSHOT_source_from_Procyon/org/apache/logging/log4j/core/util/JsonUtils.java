// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import com.fasterxml.jackson.core.io.CharTypes;

public final class JsonUtils
{
    private static final char[] HC;
    private static final ThreadLocal<char[]> _qbufLocal;
    
    private static char[] getQBuf() {
        char[] _qbuf = JsonUtils._qbufLocal.get();
        if (_qbuf == null) {
            _qbuf = new char[] { '\\', '\0', '0', '0', '\0', '\0' };
            JsonUtils._qbufLocal.set(_qbuf);
        }
        return _qbuf;
    }
    
    public static void quoteAsString(final CharSequence input, final StringBuilder output) {
        final char[] qbuf = getQBuf();
        final int[] escCodes = CharTypes.get7BitOutputEscapes();
        final int escCodeCount = escCodes.length;
        int inPtr = 0;
        final int inputLen = input.length();
    Label_0133:
        while (inPtr < inputLen) {
            while (true) {
                final char c = input.charAt(inPtr);
                if (c < escCodeCount && escCodes[c] != 0) {
                    final char d = input.charAt(inPtr++);
                    final int escCode = escCodes[d];
                    final int length = (escCode < 0) ? _appendNumeric(d, qbuf) : _appendNamed(escCode, qbuf);
                    output.append(qbuf, 0, length);
                    break;
                }
                output.append(c);
                if (++inPtr >= inputLen) {
                    break Label_0133;
                }
            }
        }
    }
    
    private static int _appendNumeric(final int value, final char[] qbuf) {
        qbuf[1] = 'u';
        qbuf[4] = JsonUtils.HC[value >> 4];
        qbuf[5] = JsonUtils.HC[value & 0xF];
        return 6;
    }
    
    private static int _appendNamed(final int esc, final char[] qbuf) {
        qbuf[1] = (char)esc;
        return 2;
    }
    
    static {
        HC = CharTypes.copyHexChars();
        _qbufLocal = new ThreadLocal<char[]>();
    }
}
