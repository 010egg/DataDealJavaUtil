// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

class ObjectWriterImplBoolValueArrayLambda extends ObjectWriterPrimitiveImpl
{
    private final ToIntFunction functionSize;
    private final BiFunction<Object, Integer, Boolean> functionGet;
    
    public ObjectWriterImplBoolValueArrayLambda(final ToIntFunction functionSize, final BiFunction<Object, Integer, Boolean> functionGet) {
        this.functionSize = functionSize;
        this.functionGet = functionGet;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            jsonWriter.writeTypeName(ObjectWriterImplBoolValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplBoolValueArray.JSONB_TYPE_HASH);
        }
        final int size = this.functionSize.applyAsInt(object);
        jsonWriter.startArray(size);
        for (int i = 0; i < size; ++i) {
            final boolean value = this.functionGet.apply(object, i);
            jsonWriter.writeBool(value);
        }
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        final int size = this.functionSize.applyAsInt(object);
        jsonWriter.startArray();
        for (int i = 0; i < size; ++i) {
            final boolean value = this.functionGet.apply(object, i);
            if (i != 0) {
                jsonWriter.writeComma();
            }
            jsonWriter.writeBool(value);
        }
        jsonWriter.endArray();
    }
}
