// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplStringArray extends ObjectWriterPrimitiveImpl
{
    static final byte[] TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    static final ObjectWriterImplStringArray INSTANCE;
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        final String[] strings = (String[])object;
        jsonWriter.writeString(strings);
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            jsonWriter.writeTypeName(ObjectWriterImplStringArray.TYPE_NAME_BYTES, ObjectWriterImplStringArray.JSONB_TYPE_HASH);
        }
        final String[] list = (String[])object;
        jsonWriter.startArray(list.length);
        for (int i = 0; i < list.length; ++i) {
            final String item = list[i];
            if (item == null) {
                if (jsonWriter.isEnabled(JSONWriter.Feature.NullAsDefaultValue.mask | JSONWriter.Feature.WriteNullStringAsEmpty.mask)) {
                    jsonWriter.writeString("");
                }
                else {
                    jsonWriter.writeNull();
                }
            }
            else {
                jsonWriter.writeString(item);
            }
        }
    }
    
    static {
        TYPE_NAME_BYTES = JSONB.toBytes("[String");
        JSONB_TYPE_HASH = Fnv.hashCode64("[String");
        INSTANCE = new ObjectWriterImplStringArray();
    }
}
