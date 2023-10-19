// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.function.Function;

final class FieldWriterEnumFunc extends FieldWriterEnum
{
    final Function function;
    
    FieldWriterEnumFunc(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method, final Function function) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.apply(object);
    }
    
    @Override
    public Function getFunction() {
        return this.function;
    }
}
