// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.OffsetDateTime;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Function;

final class FieldWriterOffsetDateTimeFunc<T> extends FieldWriterObjectFinal<T>
{
    final Function function;
    
    FieldWriterOffsetDateTimeFunc(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method, final Function function) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.apply(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final OffsetDateTime dateTime = this.function.apply(object);
        if (dateTime != null) {
            this.writeFieldName(jsonWriter);
            if (this.objectWriter == null) {
                this.objectWriter = this.getObjectWriter(jsonWriter, OffsetDateTime.class);
            }
            if (this.objectWriter != ObjectWriterImplOffsetDateTime.INSTANCE) {
                this.objectWriter.write(jsonWriter, dateTime, this.fieldName, this.fieldClass, this.features);
            }
            else {
                jsonWriter.writeOffsetDateTime(dateTime);
            }
            return true;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if ((features & JSONWriter.Feature.WriteNulls.mask) != 0x0L) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeNull();
            return true;
        }
        return false;
    }
}
