// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.Field;

final class FieldWriterBoolValField extends FieldWriterBoolVal
{
    FieldWriterBoolValField(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Class fieldClass) {
        super(fieldName, ordinal, features, format, label, fieldClass, fieldClass, field, null);
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.getFieldValueBoolean(object);
    }
    
    public boolean getFieldValueBoolean(final Object object) {
        if (object == null) {
            throw new JSONException("field.get error, " + this.fieldName);
        }
        try {
            boolean value;
            if (this.fieldOffset != -1L) {
                value = JDKUtils.UNSAFE.getBoolean(object, this.fieldOffset);
            }
            else {
                value = this.field.getBoolean(object);
            }
            return value;
        }
        catch (IllegalArgumentException | IllegalAccessException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new JSONException("field.get error, " + this.fieldName, e);
        }
    }
}
