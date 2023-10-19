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

final class FieldWriterInt32Val<T> extends FieldWriterInt32<T>
{
    FieldWriterInt32Val(final String name, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(name, ordinal, features, format, label, Integer.TYPE, Integer.TYPE, field, null);
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.getFieldValueInt(object);
    }
    
    public int getFieldValueInt(final T object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            int value;
            if (this.fieldOffset != -1L) {
                value = JDKUtils.UNSAFE.getInt(object, this.fieldOffset);
            }
            else {
                value = this.field.getInt(object);
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
        final int value = this.getFieldValueInt(object);
        if (value == 0 && jsonWriter.isEnabled(JSONWriter.Feature.NotWriteDefaultValue)) {
            return false;
        }
        this.writeInt32(jsonWriter, value);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final int value = this.getFieldValueInt(object);
        jsonWriter.writeInt32(value);
    }
}
