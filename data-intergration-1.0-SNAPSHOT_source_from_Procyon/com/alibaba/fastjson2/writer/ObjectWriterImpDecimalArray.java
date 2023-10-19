// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONB;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImpDecimalArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImpDecimalArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    
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
        final BigDecimal[] array = (BigDecimal[])object;
        jsonWriter.startArray();
        for (int i = 0; i < array.length; ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            jsonWriter.writeDecimal(array[i], 0L, null);
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
            final long JSONB_TYPE_HASH = -2138534155605614069L;
            jsonWriter.writeTypeName(ObjectWriterImpDecimalArray.JSONB_TYPE_NAME_BYTES, -2138534155605614069L);
        }
        final BigDecimal[] array = (BigDecimal[])object;
        jsonWriter.startArray(array.length);
        for (final BigDecimal bigDecimal : array) {
            jsonWriter.writeDecimal(bigDecimal, 0L, null);
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImpDecimalArray();
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[BigDecimal");
    }
}
