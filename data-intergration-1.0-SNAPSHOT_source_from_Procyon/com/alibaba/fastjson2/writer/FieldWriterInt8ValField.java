// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

final class FieldWriterInt8ValField<T> extends FieldWriterInt8<T>
{
    FieldWriterInt8ValField(final String name, final int ordinal, final long features, final String format, final String label, final Field field) {
        super(name, ordinal, features, format, label, Byte.TYPE, field, null);
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.getFieldValueByte(object);
    }
    
    public byte getFieldValueByte(final T object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            byte value;
            if (this.fieldOffset != -1L) {
                value = JDKUtils.UNSAFE.getByte(object, this.fieldOffset);
            }
            else {
                value = this.field.getByte(object);
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
        final byte value = this.getFieldValueByte(object);
        this.writeInt8(jsonWriter, value);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final byte value = this.getFieldValueByte(object);
        jsonWriter.writeInt32(value);
    }
}
