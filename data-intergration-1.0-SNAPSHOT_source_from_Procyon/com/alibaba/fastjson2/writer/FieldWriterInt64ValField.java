// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

final class FieldWriterInt64ValField<T> extends FieldWriterInt64<T>
{
    FieldWriterInt64ValField(final String name, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(name, ordinal, features, format, label, Long.TYPE, field, null);
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
    public boolean write(final JSONWriter jsonWriter, final T o) {
        final long value = this.getFieldLong(o);
        if (value == 0L && jsonWriter.isEnabled(JSONWriter.Feature.NotWriteDefaultValue)) {
            return false;
        }
        this.writeInt64(jsonWriter, value);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final long value = this.getFieldLong(object);
        jsonWriter.writeInt64(value);
    }
}
