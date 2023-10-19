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

class FieldWriterFloatField<T> extends FieldWriter<T>
{
    protected FieldWriterFloatField(final String name, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(name, ordinal, features, format, label, Float.class, Float.class, field, null);
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            Object value;
            if (this.fieldOffset != -1L && !this.fieldClass.isPrimitive()) {
                value = JDKUtils.UNSAFE.getObject(object, this.fieldOffset);
            }
            else {
                value = this.field.get(object);
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
        final Float value = (Float)this.getFieldValue(object);
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
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Float value = (Float)this.getFieldValue(object);
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
}
