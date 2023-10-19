// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.function.Function;

final class FieldWriterBigDecimalFunc<T> extends FieldWriter<T>
{
    final Function<T, BigDecimal> function;
    
    FieldWriterBigDecimalFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Function<T, BigDecimal> function) {
        super(fieldName, ordinal, features, format, label, BigDecimal.class, BigDecimal.class, null, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.apply(object);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final BigDecimal value = this.function.apply(object);
        jsonWriter.writeDecimal(value, this.features, this.decimalFormat);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        BigDecimal value;
        try {
            value = this.function.apply(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value == null) {
            final long features = this.features | jsonWriter.getFeatures();
            if ((features & JSONWriter.Feature.WriteNulls.mask) == 0x0L) {
                return false;
            }
        }
        this.writeFieldName(jsonWriter);
        jsonWriter.writeDecimal(value, this.features, this.decimalFormat);
        return true;
    }
    
    @Override
    public Function getFunction() {
        return this.function;
    }
}
