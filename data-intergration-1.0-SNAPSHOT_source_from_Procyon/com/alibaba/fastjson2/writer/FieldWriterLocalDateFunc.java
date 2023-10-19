// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.time.LocalDate;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Function;

final class FieldWriterLocalDateFunc<T> extends FieldWriterObjectFinal<T>
{
    final Function function;
    
    FieldWriterLocalDateFunc(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method, final Function function) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.apply(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final LocalDate localDate = this.function.apply(object);
        if (localDate != null) {
            this.writeFieldName(jsonWriter);
            if (this.objectWriter == null) {
                this.objectWriter = this.getObjectWriter(jsonWriter, LocalDate.class);
            }
            if (this.objectWriter != ObjectWriterImplLocalDate.INSTANCE) {
                this.objectWriter.write(jsonWriter, localDate, this.fieldName, this.fieldClass, this.features);
            }
            else {
                jsonWriter.writeLocalDate(localDate);
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
