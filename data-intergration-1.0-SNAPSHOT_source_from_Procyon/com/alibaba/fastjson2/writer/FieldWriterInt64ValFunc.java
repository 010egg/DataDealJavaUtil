// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.ToLongFunction;

final class FieldWriterInt64ValFunc<T> extends FieldWriterInt64<T>
{
    final ToLongFunction function;
    
    FieldWriterInt64ValFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final ToLongFunction function) {
        super(fieldName, ordinal, features, format, label, Long.TYPE, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final T object) {
        return this.function.applyAsLong(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        long value;
        try {
            value = this.function.applyAsLong(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        this.writeInt64(jsonWriter, value);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final long value = this.function.applyAsLong(object);
        jsonWriter.writeInt64(value);
    }
}
