// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import sun.misc.Unsafe;
import com.alibaba.fastjson2.util.JDKUtils;

final class JSONWriterUTF16JDK9UF extends JSONWriterUTF16
{
    JSONWriterUTF16JDK9UF(final Context ctx) {
        super(ctx);
    }
    
    @Override
    public void writeString(final String str) {
        if (str == null) {
            this.writeStringNull();
            return;
        }
        final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
        final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
        boolean escape = false;
        final char quote = this.quote;
        final int strlen = str.length();
        final int minCapacity = this.off + strlen + 2;
        if (minCapacity >= this.chars.length) {
            this.ensureCapacity(minCapacity);
        }
        final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
        final byte[] value = JDKUtils.STRING_VALUE.apply(str);
        int off = this.off;
        final char[] chars = this.chars;
        chars[off++] = quote;
        for (int i = 0; i < strlen; ++i) {
            int c;
            if (coder == 0) {
                c = value[i];
            }
            else {
                c = JDKUtils.UNSAFE.getChar(str, Unsafe.ARRAY_CHAR_BASE_OFFSET + (long)(i * 2));
            }
            if (c == 92 || c == quote || c < 32 || (browserSecure && (c == 60 || c == 62 || c == 40 || c == 41)) || (escapeNoneAscii && c > 127)) {
                escape = true;
                break;
            }
            chars[off++] = (char)c;
        }
        if (!escape) {
            chars[off++] = quote;
            this.off = off;
            return;
        }
        this.writeStringEscape(str);
    }
}
