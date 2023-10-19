// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplInt8 extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt8 INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final byte byteValue = (byte)object;
        jsonWriter.writeInt8(byteValue);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        jsonWriter.writeInt32(((Number)object).intValue());
        final long features2 = jsonWriter.getFeatures(features);
        if ((features2 & JSONWriter.Feature.WriteClassName.mask) != 0x0L && (features2 & JSONWriter.Feature.WriteNonStringKeyAsString.mask) == 0x0L && (features2 & JSONWriter.Feature.NotWriteNumberClassName.mask) == 0x0L && fieldType != Byte.class && fieldType != Byte.TYPE) {
            jsonWriter.writeRaw('B');
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt8();
    }
}
