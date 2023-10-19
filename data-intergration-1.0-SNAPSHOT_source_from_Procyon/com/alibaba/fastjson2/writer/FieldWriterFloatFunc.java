// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.Function;

final class FieldWriterFloatFunc<T> extends FieldWriter<T>
{
    final Function<T, Float> function;
    
    FieldWriterFloatFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Function<T, Float> function) {
        super(fieldName, ordinal, features, format, label, Float.class, Float.class, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.apply(object);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Float value = this.function.apply(object);
        if (value == null) {
            jsonWriter.writeNumberNull();
        }
        else {
            final float floatValue = value;
            if (this.decimalFormat != null) {
                jsonWriter.writeFloat(floatValue, this.decimalFormat);
            }
            else {
                jsonWriter.writeFloat(floatValue);
            }
        }
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        Float value;
        try {
            value = this.function.apply(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value != null) {
            this.writeFieldName(jsonWriter);
            final float floatValue = value;
            if (this.decimalFormat != null) {
                jsonWriter.writeFloat(floatValue, this.decimalFormat);
            }
            else {
                jsonWriter.writeFloat(floatValue);
            }
            return true;
        }
        final long features = jsonWriter.getFeatures(this.features);
        if ((features & JSONWriter.Feature.WriteNulls.mask) != 0x0L && (features & JSONWriter.Feature.NotWriteDefaultValue.mask) == 0x0L) {
            this.writeFieldName(jsonWriter);
            jsonWriter.writeNumberNull();
            return true;
        }
        return false;
    }
    
    @Override
    public Function getFunction() {
        return this.function;
    }
}
