// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.Function;

final class FieldWriterInt16Func<T> extends FieldWriterInt16<T>
{
    final Function<T, Short> function;
    
    FieldWriterInt16Func(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Function<T, Short> function) {
        super(fieldName, ordinal, features, format, label, Short.class, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.apply(object);
    }
    
    @Override
    public Function getFunction() {
        return this.function;
    }
}
