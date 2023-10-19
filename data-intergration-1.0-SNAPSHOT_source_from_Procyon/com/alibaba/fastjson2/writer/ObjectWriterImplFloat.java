// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.text.DecimalFormat;

final class ObjectWriterImplFloat extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplFloat INSTANCE;
    private final DecimalFormat format;
    
    public ObjectWriterImplFloat(final DecimalFormat format) {
        this.format = format;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeFloat((float)object);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        if (this.format != null) {
            final String str = this.format.format(object);
            jsonWriter.writeRaw(str);
            return;
        }
        jsonWriter.writeFloat((float)object);
        final long features2 = jsonWriter.getFeatures(features);
        if ((features2 & JSONWriter.Feature.WriteClassName.mask) != 0x0L && (features2 & JSONWriter.Feature.WriteNonStringKeyAsString.mask) == 0x0L && (features2 & JSONWriter.Feature.NotWriteNumberClassName.mask) == 0x0L && fieldType != Float.class && fieldType != Float.TYPE) {
            jsonWriter.writeRaw('F');
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplFloat(null);
    }
}
