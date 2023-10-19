// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.Function;

final class FieldWriterInt64Func<T> extends FieldWriterInt64<T>
{
    final Function<T, Long> function;
    
    FieldWriterInt64Func(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Function<T, Long> function) {
        super(fieldName, ordinal, features, format, label, Long.class, field, method);
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
