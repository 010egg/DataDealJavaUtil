// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.function.ToFloatFunction;

final class FieldWriterFloatValueFunc extends FieldWriter
{
    final ToFloatFunction function;
    
    FieldWriterFloatValueFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final ToFloatFunction function) {
        super(fieldName, ordinal, features, format, label, Float.TYPE, Float.TYPE, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.applyAsFloat(object);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final Object object) {
        final float fieldValue = this.function.applyAsFloat(object);
        if (this.decimalFormat != null) {
            jsonWriter.writeDouble(fieldValue, this.decimalFormat);
        }
        else {
            jsonWriter.writeDouble(fieldValue);
        }
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        float value;
        try {
            value = this.function.applyAsFloat(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        this.writeFieldName(jsonWriter);
        if (this.decimalFormat != null) {
            jsonWriter.writeFloat(value, this.decimalFormat);
        }
        else {
            jsonWriter.writeFloat(value);
        }
        return true;
    }
}
