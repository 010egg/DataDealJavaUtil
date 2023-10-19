// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.function.ToShortFunction;

final class FieldWriterInt16ValFunc extends FieldWriterInt16
{
    final ToShortFunction function;
    
    FieldWriterInt16ValFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final ToShortFunction function) {
        super(fieldName, ordinal, features, format, label, Short.TYPE, field, method);
        this.function = function;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final Object object) {
        final short value = this.function.applyAsShort(object);
        jsonWriter.writeInt32(value);
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.applyAsShort(object);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        short value;
        try {
            value = this.function.applyAsShort(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        this.writeInt16(jsonWriter, value);
        return true;
    }
}
