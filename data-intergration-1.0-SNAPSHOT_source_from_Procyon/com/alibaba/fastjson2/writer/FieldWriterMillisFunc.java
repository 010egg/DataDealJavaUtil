// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.ToLongFunction;

final class FieldWriterMillisFunc<T> extends FieldWriterDate<T>
{
    final ToLongFunction function;
    
    FieldWriterMillisFunc(final String fieldName, final int ordinal, final long features, final String dateTimeFormat, final String label, final Field field, final Method method, final ToLongFunction function) {
        super(fieldName, ordinal, features, dateTimeFormat, label, Long.TYPE, Long.TYPE, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.applyAsLong(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        final long millis = this.function.applyAsLong(object);
        this.writeDate(jsonWriter, millis);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final long millis = this.function.applyAsLong(object);
        this.writeDate(jsonWriter, false, millis);
    }
}
