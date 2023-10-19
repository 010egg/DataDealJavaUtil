// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.Function;
import java.text.DecimalFormat;

final class ObjectWriterImplFloatValueArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplFloatValueArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    private final DecimalFormat format;
    private final Function<Object, float[]> function;
    
    public ObjectWriterImplFloatValueArray(final DecimalFormat format) {
        this.format = format;
        this.function = null;
    }
    
    public ObjectWriterImplFloatValueArray(final Function<Object, float[]> function, final DecimalFormat format) {
        this.function = function;
        this.format = format;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            jsonWriter.writeTypeName(ObjectWriterImplFloatValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplFloatValueArray.JSONB_TYPE_HASH);
        }
        float[] array;
        if (this.function != null && object != null) {
            array = this.function.apply(object);
        }
        else {
            array = (float[])object;
        }
        jsonWriter.writeFloat(array);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        float[] array;
        if (this.function != null && object != null) {
            array = this.function.apply(object);
        }
        else {
            array = (float[])object;
        }
        if (this.format == null) {
            jsonWriter.writeFloat(array);
        }
        else {
            jsonWriter.writeFloat(array, this.format);
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplFloatValueArray(null, null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[F");
        JSONB_TYPE_HASH = Fnv.hashCode64("[F");
    }
}
