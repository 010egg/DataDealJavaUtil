// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.Field;

final class FieldWriterDoubleValField<T> extends FieldWriter<T>
{
    FieldWriterDoubleValField(final String name, final int ordinal, final String format, final String label, final Field field) {
        super(name, ordinal, 0L, format, label, Double.TYPE, Double.TYPE, field, null);
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.getFieldValueDouble(object);
    }
    
    public double getFieldValueDouble(final Object object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            double value;
            if (this.fieldOffset != -1L) {
                value = JDKUtils.UNSAFE.getDouble(object, this.fieldOffset);
            }
            else {
                value = this.field.getDouble(object);
            }
            return value;
        }
        catch (IllegalArgumentException | IllegalAccessException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final double value = this.getFieldValueDouble(object);
        this.writeDouble(jsonWriter, value);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final double value = this.getFieldValueDouble(object);
        if (this.decimalFormat != null) {
            jsonWriter.writeDouble(value, this.decimalFormat);
        }
        else {
            jsonWriter.writeDouble(value);
        }
    }
}
