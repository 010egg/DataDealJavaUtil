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

final class FieldWriterCharMethod<T> extends FieldWriter<T>
{
    FieldWriterCharMethod(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Class fieldClass) {
        super(fieldName, ordinal, features, format, label, fieldClass, fieldClass, field, method);
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
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Character value = (Character)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        jsonWriter.writeChar(value);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final Character value = (Character)this.getFieldValue(object);
        if (value == null) {
            return false;
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeChar(value);
        return true;
    }
}
