// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.OptionalInt;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplOptionalInt extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplOptionalInt INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final OptionalInt optionalInt = (OptionalInt)object;
        if (!optionalInt.isPresent()) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeInt32(optionalInt.getAsInt());
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final OptionalInt optionalInt = (OptionalInt)object;
        if (!optionalInt.isPresent()) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeInt32(optionalInt.getAsInt());
    }
    
    static {
        INSTANCE = new ObjectWriterImplOptionalInt();
    }
}
