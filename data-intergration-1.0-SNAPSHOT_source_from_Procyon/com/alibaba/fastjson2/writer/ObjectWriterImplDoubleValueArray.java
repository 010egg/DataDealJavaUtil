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

final class ObjectWriterImplDoubleValueArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplDoubleValueArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    final DecimalFormat format;
    private final Function<Object, double[]> function;
    
    public ObjectWriterImplDoubleValueArray(final DecimalFormat format) {
        this.format = format;
        this.function = null;
    }
    
    public ObjectWriterImplDoubleValueArray(final Function<Object, double[]> function, final DecimalFormat format) {
        this.function = function;
        this.format = format;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            jsonWriter.writeTypeName(ObjectWriterImplDoubleValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplDoubleValueArray.JSONB_TYPE_HASH);
        }
        double[] array;
        if (this.function != null && object != null) {
            array = this.function.apply(object);
        }
        else {
            array = (double[])object;
        }
        jsonWriter.writeDouble(array);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        double[] array;
        if (this.function != null && object != null) {
            array = this.function.apply(object);
        }
        else {
            array = (double[])object;
        }
        if (this.format == null) {
            jsonWriter.writeDouble(array);
        }
        else {
            jsonWriter.writeDouble(array, this.format);
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplDoubleValueArray(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[D");
        JSONB_TYPE_HASH = Fnv.hashCode64("[D");
    }
}
