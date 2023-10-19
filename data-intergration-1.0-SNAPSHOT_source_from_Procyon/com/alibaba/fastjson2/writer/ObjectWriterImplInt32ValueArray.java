// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.Function;

final class ObjectWriterImplInt32ValueArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt32ValueArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    private final Function<Object, int[]> function;
    
    public ObjectWriterImplInt32ValueArray(final Function<Object, int[]> function) {
        this.function = function;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            jsonWriter.writeTypeName(ObjectWriterImplInt32ValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplInt32ValueArray.JSONB_TYPE_HASH);
        }
        int[] array;
        if (this.function != null) {
            array = this.function.apply(object);
        }
        else {
            array = (int[])object;
        }
        jsonWriter.writeInt32(array);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        int[] array;
        if (this.function != null) {
            array = this.function.apply(object);
        }
        else {
            array = (int[])object;
        }
        jsonWriter.writeInt32(array);
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt32ValueArray(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[I");
        JSONB_TYPE_HASH = Fnv.hashCode64("[I");
    }
}
