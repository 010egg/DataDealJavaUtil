// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

final class FieldWriterBigDecimalMethod<T> extends FieldWriter<T>
{
    FieldWriterBigDecimalMethod(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method) {
        super(fieldName, ordinal, features, format, label, BigDecimal.class, BigDecimal.class, null, method);
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
        final BigDecimal value = (BigDecimal)this.getFieldValue(object);
        jsonWriter.writeDecimal(value, this.features, this.decimalFormat);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        BigDecimal value;
        try {
            value = (BigDecimal)this.getFieldValue(object);
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
}
