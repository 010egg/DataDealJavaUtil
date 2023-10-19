// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.Function;

final class ObjectWriterImplCharValueArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplCharValueArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    private final Function<Object, char[]> function;
    
    public ObjectWriterImplCharValueArray(final Function<Object, char[]> function) {
        this.function = function;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            jsonWriter.writeTypeName(ObjectWriterImplCharValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplCharValueArray.JSONB_TYPE_HASH);
        }
        char[] chars;
        if (this.function != null && object != null) {
            chars = this.function.apply(object);
        }
        else {
            chars = (char[])object;
        }
        jsonWriter.writeString(chars, 0, chars.length);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        char[] chars;
        if (this.function != null && object != null) {
            chars = this.function.apply(object);
        }
        else {
            chars = (char[])object;
        }
        if (jsonWriter.utf16) {
            jsonWriter.writeString(chars, 0, chars.length);
        }
        else {
            jsonWriter.writeString(new String(chars));
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplCharValueArray(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[C");
        JSONB_TYPE_HASH = Fnv.hashCode64("[C");
    }
}
