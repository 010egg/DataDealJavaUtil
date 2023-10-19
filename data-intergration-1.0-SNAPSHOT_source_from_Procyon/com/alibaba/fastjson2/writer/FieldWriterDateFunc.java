// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.function.Function;

final class FieldWriterDateFunc<T> extends FieldWriterDate<T>
{
    final Function<T, Date> function;
    
    FieldWriterDateFunc(final String fieldName, final int ordinal, final long features, final String dateTimeFormat, final String label, final Field field, final Method method, final Function<T, Date> function) {
        super(fieldName, ordinal, features, dateTimeFormat, label, Date.class, Date.class, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.apply(object);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Date value = this.function.apply(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        this.writeDate(jsonWriter, false, value.getTime());
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final Date value = this.function.apply(object);
        if (value != null) {
            this.writeDate(jsonWriter, value.getTime());
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
    
    @Override
    public Function getFunction() {
        return this.function;
    }
}
