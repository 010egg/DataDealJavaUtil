// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import com.alibaba.fastjson2.util.JDKUtils;

class JSONWriterUTF8JDK9 extends JSONWriterUTF8
{
    JSONWriterUTF8JDK9(final Context ctx) {
        super(ctx);
    }
    
    @Override
    public void writeString(final String str) {
        if (str == null) {
            this.writeStringNull();
            return;
        }
        final int coder = JDKUtils.STRING_CODER.applyAsInt(str);
        final byte[] value = JDKUtils.STRING_VALUE.apply(str);
        if (coder == 0) {
            this.writeStringLatin1(value);
        }
        else {
            this.writeStringUTF16(value);
        }
    }
}
