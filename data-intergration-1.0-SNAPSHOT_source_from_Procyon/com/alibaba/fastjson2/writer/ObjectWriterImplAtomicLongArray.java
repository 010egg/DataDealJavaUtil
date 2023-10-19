// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.concurrent.atomic.AtomicLongArray;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplAtomicLongArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplAtomicLongArray INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        final AtomicLongArray array = (AtomicLongArray)object;
        jsonWriter.startArray(array.length());
        for (int i = 0; i < array.length(); ++i) {
            jsonWriter.writeInt64(array.get(i));
        }
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        final AtomicLongArray array = (AtomicLongArray)object;
        jsonWriter.startArray();
        for (int i = 0; i < array.length(); ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            jsonWriter.writeInt64(array.get(i));
        }
        jsonWriter.endArray();
    }
    
    static {
        INSTANCE = new ObjectWriterImplAtomicLongArray();
    }
}
