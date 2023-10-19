// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.ToDoubleFunction;

final class FieldWriterDoubleValueFunc extends FieldWriter
{
    final ToDoubleFunction function;
    
    FieldWriterDoubleValueFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final ToDoubleFunction function) {
        super(fieldName, ordinal, features, format, label, Double.TYPE, Double.TYPE, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.applyAsDouble(object);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final Object object) {
        final double value = this.function.applyAsDouble(object);
        if (this.decimalFormat != null) {
            jsonWriter.writeDouble(value, this.decimalFormat);
        }
        else {
            jsonWriter.writeDouble(value);
        }
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        double value;
        try {
            value = this.function.applyAsDouble(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        this.writeFieldName(jsonWriter);
        if (this.decimalFormat != null) {
            jsonWriter.writeDouble(value, this.decimalFormat);
        }
        else {
            jsonWriter.writeDouble(value);
        }
        return true;
    }
}
