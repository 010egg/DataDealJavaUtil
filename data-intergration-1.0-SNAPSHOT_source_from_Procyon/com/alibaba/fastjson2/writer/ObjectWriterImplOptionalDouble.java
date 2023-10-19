// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.OptionalDouble;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplOptionalDouble extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplOptionalDouble INSTANCE;
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final OptionalDouble optionalDouble = (OptionalDouble)object;
        if (!optionalDouble.isPresent()) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeDouble(optionalDouble.getAsDouble());
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final OptionalDouble optionalDouble = (OptionalDouble)object;
        if (!optionalDouble.isPresent()) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeDouble(optionalDouble.getAsDouble());
    }
    
    static {
        INSTANCE = new ObjectWriterImplOptionalDouble();
    }
}
