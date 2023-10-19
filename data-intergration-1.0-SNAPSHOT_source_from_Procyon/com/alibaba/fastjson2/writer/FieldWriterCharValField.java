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

final class FieldWriterCharValField<T> extends FieldWriter<T>
{
    FieldWriterCharValField(final String name, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(name, ordinal, features, format, label, Character.TYPE, Character.TYPE, field, null);
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.getFieldValueChar(object);
    }
    
    public char getFieldValueChar(final Object object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            char value;
            if (this.fieldOffset != -1L) {
                value = JDKUtils.UNSAFE.getChar(object, this.fieldOffset);
            }
            else {
                value = this.field.getChar(object);
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
        final char value = this.getFieldValueChar(object);
        this.writeFieldName(jsonWriter);
        jsonWriter.writeChar(value);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final Object object) {
        final char value = this.getFieldValueChar(object);
        jsonWriter.writeChar(value);
    }
}
