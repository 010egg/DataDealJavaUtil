// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplInt16 extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt16 INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final short shortValue = (short)object;
        jsonWriter.writeInt16(shortValue);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final short shortValue = (short)object;
        jsonWriter.writeInt32(shortValue);
        final long features2 = jsonWriter.getFeatures(features);
        if ((features2 & JSONWriter.Feature.WriteClassName.mask) != 0x0L && (features2 & JSONWriter.Feature.WriteNonStringKeyAsString.mask) == 0x0L && (features2 & JSONWriter.Feature.NotWriteNumberClassName.mask) == 0x0L && fieldType != Short.class && fieldType != Short.TYPE) {
            jsonWriter.writeRaw('S');
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt16();
    }
}
