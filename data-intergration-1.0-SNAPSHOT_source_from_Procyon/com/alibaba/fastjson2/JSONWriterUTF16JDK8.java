// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.util.JDKUtils;

final class JSONWriterUTF16JDK8 extends JSONWriterUTF16
{
    JSONWriterUTF16JDK8(final Context ctx) {
        super(ctx);
    }
    
    @Override
    public void writeString(final String str) {
        if (str == null) {
            if (this.isEnabled(Feature.NullAsDefaultValue.mask | Feature.WriteNullStringAsEmpty.mask)) {
                this.writeString("");
                return;
            }
            this.writeNull();
        }
        else {
            final boolean browserSecure = (this.context.features & Feature.BrowserSecure.mask) != 0x0L;
            final boolean escapeNoneAscii = (this.context.features & Feature.EscapeNoneAscii.mask) != 0x0L;
            final char[] value = JDKUtils.getCharArray(str);
            final int strlen = value.length;
            boolean escape = false;
            for (int i = 0; i < value.length; ++i) {
                final char ch = value[i];
                if (ch == this.quote || ch == '\\' || ch < ' ' || (browserSecure && (ch == '<' || ch == '>' || ch == '(' || ch == ')')) || (escapeNoneAscii && ch > '\u007f')) {
                    escape = true;
                    break;
                }
            }
            if (!escape) {
                final int minCapacity = this.off + strlen + 2;
                if (minCapacity >= this.chars.length) {
                    this.ensureCapacity(minCapacity);
                }
                this.chars[this.off++] = this.quote;
                System.arraycopy(value, 0, this.chars, this.off, value.length);
                this.off += strlen;
                this.chars[this.off++] = this.quote;
                return;
            }
            this.writeStringEscape(str);
        }
    }
}
