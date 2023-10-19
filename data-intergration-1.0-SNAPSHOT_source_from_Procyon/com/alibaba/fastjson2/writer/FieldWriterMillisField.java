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

final class FieldWriterMillisField<T> extends FieldWriterDate<T>
{
    FieldWriterMillisField(final String fieldName, final int ordinal, final long features, final String dateTimeFormat, final String label, final Field field) {
        super(fieldName, ordinal, features, dateTimeFormat, label, Long.TYPE, Long.TYPE, field, null);
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.getFieldLong(object);
    }
    
    public long getFieldLong(final T object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            long value;
            if (this.fieldOffset != -1L) {
                value = JDKUtils.UNSAFE.getLong(object, this.fieldOffset);
            }
            else {
                value = this.field.getLong(object);
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
        final long millis = this.getFieldLong(object);
        this.writeDate(jsonWriter, millis);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final long millis = this.getFieldLong(object);
        this.writeDate(jsonWriter, false, millis);
    }
}
