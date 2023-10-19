// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

final class FieldWriterBoolMethod extends FieldWriterBoolean
{
    FieldWriterBoolMethod(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Class fieldClass) {
        super(fieldName, ordinal, features, format, label, fieldClass, fieldClass, field, method);
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        try {
            return this.method.invoke(object, new Object[0]);
        }
        catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new JSONException("invoke getter method error, " + this.fieldName, e);
        }
    }
}
