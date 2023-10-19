// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplInt64 extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt64 INSTANCE;
    final Class defineClass;
    
    public ObjectWriterImplInt64(final Class defineClass) {
        this.defineClass = defineClass;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final long longValue = (long)object;
        jsonWriter.writeInt64(longValue);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNumberNull();
            return;
        }
        final long i = ((Number)object).longValue();
        jsonWriter.writeInt64(i);
        if (i >= -2147483648L && i <= 2147483647L && (features & JSONWriter.Feature.WriteClassName.mask) != 0x0L) {
            final long contextFeatures = jsonWriter.getFeatures();
            if ((contextFeatures & JSONWriter.Feature.WriteClassName.mask) == 0x0L) {
                final boolean writeAsString = (contextFeatures & (JSONWriter.Feature.WriteNonStringValueAsString.mask | JSONWriter.Feature.WriteLongAsString.mask)) != 0x0L;
                if (!writeAsString) {
                    jsonWriter.writeRaw('L');
                }
            }
        }
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt64(null);
    }
}
