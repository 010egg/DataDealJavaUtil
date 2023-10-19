// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.BitSet;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplBitSet extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplBitSet INSTANCE;
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final BitSet bitSet = (BitSet)object;
        final byte[] bytes = bitSet.toByteArray();
        jsonWriter.writeBinary(bytes);
    }
    
    static {
        INSTANCE = new ObjectWriterImplBitSet();
    }
}
