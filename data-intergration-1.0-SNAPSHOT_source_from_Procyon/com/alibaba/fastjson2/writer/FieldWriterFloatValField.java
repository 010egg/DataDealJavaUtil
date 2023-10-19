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

final class FieldWriterFloatValField<T> extends FieldWriter<T>
{
    FieldWriterFloatValField(final String name, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(name, ordinal, features, format, label, Float.TYPE, Float.TYPE, field, null);
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.getFieldValueFloat(object);
    }
    
    public float getFieldValueFloat(final T object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            float value;
            if (this.fieldOffset != -1L) {
                value = JDKUtils.UNSAFE.getFloat(object, this.fieldOffset);
            }
            else {
                value = this.field.getFloat(object);
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
        final float value = this.getFieldValueFloat(object);
        this.writeFloat(jsonWriter, value);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final float value = this.getFieldValueFloat(object);
        if (this.decimalFormat != null) {
            jsonWriter.writeFloat(value, this.decimalFormat);
        }
        else {
            jsonWriter.writeFloat(value);
        }
    }
}
