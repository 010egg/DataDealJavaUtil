// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.Function;

class ObjectWriterImplBoolValueArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplBoolValueArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    private final Function<Object, boolean[]> function;
    
    public ObjectWriterImplBoolValueArray(final Function<Object, boolean[]> function) {
        this.function = function;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
            jsonWriter.writeTypeName(ObjectWriterImplBoolValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplBoolValueArray.JSONB_TYPE_HASH);
        }
        boolean[] array;
        if (this.function != null && object != null) {
            array = this.function.apply(object);
        }
        else {
            array = (boolean[])object;
        }
        jsonWriter.writeBool(array);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        boolean[] array;
        if (this.function != null && object != null) {
            array = this.function.apply(object);
        }
        else {
            array = (boolean[])object;
        }
        jsonWriter.writeBool(array);
    }
    
    static {
        INSTANCE = new ObjectWriterImplBoolValueArray(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[Z");
        JSONB_TYPE_HASH = Fnv.hashCode64("[Z");
    }
}
