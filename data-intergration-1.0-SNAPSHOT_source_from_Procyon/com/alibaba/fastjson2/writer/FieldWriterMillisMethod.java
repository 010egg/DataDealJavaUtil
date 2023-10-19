// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

final class FieldWriterMillisMethod<T> extends FieldWriterDate<T>
{
    FieldWriterMillisMethod(final String fieldName, final int ordinal, final long features, final String dateTimeFormat, final String label, final Class fieldClass, final Field field, final Method method) {
        super(fieldName, ordinal, features, dateTimeFormat, label, fieldClass, fieldClass, field, method);
    }
    
    @Override
    public Object getFieldValue(final T object) {
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
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final long millis = (long)this.getFieldValue(object);
        this.writeDate(jsonWriter, millis);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final long millis = (long)this.getFieldValue(object);
        this.writeDate(jsonWriter, false, millis);
    }
}
