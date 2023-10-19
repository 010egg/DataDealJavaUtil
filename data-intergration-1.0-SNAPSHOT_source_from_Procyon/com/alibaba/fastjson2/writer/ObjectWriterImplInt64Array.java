// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplInt64Array extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt64Array INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            if (jsonWriter.isEnabled(JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullListAsEmpty.mask)) {
                jsonWriter.startArray();
                jsonWriter.endArray();
            }
            else {
                jsonWriter.writeNull();
            }
            return;
        }
        final Long[] array = (Long[])object;
        jsonWriter.startArray();
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            final Long item = array[i];
            if (item == null) {
                jsonWriter.writeNull();
            }
            else {
                jsonWriter.writeInt64(item);
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
            jsonWriter.writeTypeName(ObjectWriterImplInt64Array.JSONB_TYPE_NAME_BYTES, ObjectWriterImplInt64Array.JSONB_TYPE_HASH);
        }
        final Long[] array = (Long[])object;
        jsonWriter.startArray(array.length);
        for (final Long item : array) {
            if (item == null) {
                jsonWriter.writeNull();
            }
            else {
                jsonWriter.writeInt64(item);
            }
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt64Array();
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[Long");
        JSONB_TYPE_HASH = Fnv.hashCode64("[Long");
    }
}
