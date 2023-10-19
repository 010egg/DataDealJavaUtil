// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplInt8Array extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt8Array INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final Byte[] array = (Byte[])object;
        jsonWriter.startArray();
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            final Byte value = array[i];
            if (value == null) {
                jsonWriter.writeNull();
            }
            else {
                jsonWriter.writeInt32(value);
            }
        }
        jsonWriter.endArray();
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            jsonWriter.writeTypeName(ObjectWriterImplInt8Array.JSONB_TYPE_NAME_BYTES, ObjectWriterImplInt8Array.JSONB_TYPE_HASH);
        }
        final Byte[] array = (Byte[])object;
        jsonWriter.startArray(array.length);
        for (int i = 0; i < array.length; ++i) {
            final Byte value = array[i];
            if (value == null) {
                jsonWriter.writeNull();
            }
            else {
                jsonWriter.writeInt32(value);
            }
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt8Array();
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[Byte");
        JSONB_TYPE_HASH = Fnv.hashCode64("[Byte");
    }
}
