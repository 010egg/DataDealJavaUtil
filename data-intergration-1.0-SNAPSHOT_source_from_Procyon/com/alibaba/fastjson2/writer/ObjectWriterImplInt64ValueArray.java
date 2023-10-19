// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.Function;

final class ObjectWriterImplInt64ValueArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt64ValueArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    private final Function<Object, long[]> function;
    
    public ObjectWriterImplInt64ValueArray(final Function<Object, long[]> function) {
        this.function = function;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            jsonWriter.writeTypeName(ObjectWriterImplInt64ValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplInt64ValueArray.JSONB_TYPE_HASH);
        }
        long[] array;
        if (this.function != null) {
            array = this.function.apply(object);
        }
        else {
            array = (long[])object;
        }
        jsonWriter.writeInt64(array);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final ObjectWriterProvider provider = jsonWriter.context.provider;
        ObjectWriter objectWriter = null;
        if ((provider.userDefineMask & 0x4L) != 0x0L) {
            objectWriter = jsonWriter.context.getObjectWriter(Long.class);
        }
        long[] array;
        if (this.function != null) {
            array = this.function.apply(object);
        }
        else {
            array = (long[])object;
        }
        if (objectWriter == null || objectWriter == ObjectWriterImplInt32.INSTANCE) {
            jsonWriter.writeInt64(array);
            return;
        }
        jsonWriter.startArray();
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            objectWriter.write(jsonWriter, array[i], i, Long.TYPE, features);
        }
        jsonWriter.endArray();
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt64ValueArray(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[J");
        JSONB_TYPE_HASH = Fnv.hashCode64("[J");
    }
}
