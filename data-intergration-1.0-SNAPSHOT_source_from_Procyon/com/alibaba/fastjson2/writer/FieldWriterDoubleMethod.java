// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

class FieldWriterDoubleMethod<T> extends FieldWriter<T>
{
    protected FieldWriterDoubleMethod(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
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
    public boolean write(final JSONWriter jsonWriter, final T object) {
        Double value;
        try {
            value = (Double)this.getFieldValue(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        this.writeFieldName(jsonWriter);
        if (value == null) {
            jsonWriter.writeNumberNull();
        }
        else {
            final double doubleValue = value;
            if (this.decimalFormat != null) {
                jsonWriter.writeDouble(doubleValue, this.decimalFormat);
            }
            else {
                jsonWriter.writeDouble(doubleValue);
            }
        }
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Double value = (Double)this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNumberNull();
        }
        else {
            final double doubleValue = value;
            if (this.decimalFormat != null) {
                jsonWriter.writeDouble(doubleValue, this.decimalFormat);
            }
            else {
                jsonWriter.writeDouble(doubleValue);
            }
        }
    }
}
