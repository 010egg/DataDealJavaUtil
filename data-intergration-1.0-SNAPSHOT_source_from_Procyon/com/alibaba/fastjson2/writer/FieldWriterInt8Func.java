// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.Function;

final class FieldWriterInt8Func<T> extends FieldWriterInt8<T>
{
    final Function<T, Byte> function;
    
    FieldWriterInt8Func(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final Function<T, Byte> function) {
        super(fieldName, ordinal, features, format, label, Byte.class, field, method);
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
