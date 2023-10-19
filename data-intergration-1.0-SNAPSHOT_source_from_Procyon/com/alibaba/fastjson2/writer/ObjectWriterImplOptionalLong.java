// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.OptionalLong;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplOptionalLong extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplOptionalLong INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final OptionalLong optionalLong = (OptionalLong)object;
        if (!optionalLong.isPresent()) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeInt64(optionalLong.getAsLong());
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final OptionalLong optionalLong = (OptionalLong)object;
        if (!optionalLong.isPresent()) {
            jsonWriter.writeNull();
            return;
        }
        final long value = optionalLong.getAsLong();
        jsonWriter.writeInt64(value);
    }
    
    static {
        INSTANCE = new ObjectWriterImplOptionalLong();
    }
}
