// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.function.ToIntFunction;

final class FieldWriterInt32ValFunc extends FieldWriterInt32
{
    final ToIntFunction function;
    
    FieldWriterInt32ValFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final ToIntFunction function) {
        super(fieldName, ordinal, features, format, label, Integer.TYPE, Integer.TYPE, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.applyAsInt(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        int value;
        try {
            value = this.function.applyAsInt(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        this.writeInt32(jsonWriter, value);
        return true;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final Object object) {
        final int value = this.function.applyAsInt(object);
        jsonWriter.writeInt32(value);
    }
}
