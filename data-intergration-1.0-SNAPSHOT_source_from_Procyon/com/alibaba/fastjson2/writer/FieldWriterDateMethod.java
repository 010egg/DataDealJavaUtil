// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.Date;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

final class FieldWriterDateMethod<T> extends FieldWriterDate<T>
{
    FieldWriterDateMethod(final String fieldName, final int ordinal, final long features, final String format, final String label, final Class fieldClass, final Field field, final Method method) {
        super(fieldName, ordinal, features, format, label, fieldClass, fieldClass, field, method);
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        try {
            return this.method.invoke(object, new Object[0]);
        }
        catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new JSONException("invoke getter method error, " + this.fieldName, e);
        }
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Date value = (Date)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        this.writeDate(jsonWriter, false, value.getTime());
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final Date value = (Date)this.getFieldValue(object);
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
}
