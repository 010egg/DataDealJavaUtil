// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

class FieldWriterObjectMethod<T> extends FieldWriterObject<T>
{
    protected FieldWriterObjectMethod(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
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
