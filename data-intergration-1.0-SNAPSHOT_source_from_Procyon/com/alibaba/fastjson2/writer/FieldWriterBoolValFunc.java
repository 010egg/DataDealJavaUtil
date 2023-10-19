// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.Predicate;

final class FieldWriterBoolValFunc extends FieldWriterBoolVal
{
    final Predicate function;
    
    FieldWriterBoolValFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Predicate function) {
        super(fieldName, ordinal, features, format, label, Boolean.class, Boolean.class, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.test(object);
    }
}
