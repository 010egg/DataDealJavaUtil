// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.Function;

final class ObjectWriterImplInt16ValueArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt16ValueArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    private final Function<Object, short[]> function;
    
    public ObjectWriterImplInt16ValueArray(final Function<Object, short[]> function) {
        this.function = function;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            jsonWriter.writeTypeName(ObjectWriterImplInt16ValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplInt16ValueArray.JSONB_TYPE_HASH);
        }
        short[] shorts;
        if (this.function != null && object != null) {
            shorts = this.function.apply(object);
        }
        else {
            shorts = (short[])object;
        }
        jsonWriter.startArray(shorts.length);
        for (int i = 0; i < shorts.length; ++i) {
            jsonWriter.writeInt32(shorts[i]);
        }
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        short[] shorts;
        if (this.function != null) {
            shorts = this.function.apply(object);
        }
        else {
            shorts = (short[])object;
        }
        jsonWriter.startArray();
        for (int i = 0; i < shorts.length; ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            jsonWriter.writeInt32(shorts[i]);
        }
        jsonWriter.endArray();
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt16ValueArray(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[S");
        JSONB_TYPE_HASH = Fnv.hashCode64("[S");
    }
}
