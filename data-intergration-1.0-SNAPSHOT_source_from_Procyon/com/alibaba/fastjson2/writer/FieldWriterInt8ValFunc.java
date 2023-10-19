// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.function.ToByteFunction;

final class FieldWriterInt8ValFunc extends FieldWriterInt8
{
    final ToByteFunction function;
    
    FieldWriterInt8ValFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final ToByteFunction function) {
        super(fieldName, ordinal, features, format, label, Byte.TYPE, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.applyAsByte(object);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final Object object) {
        final byte value = this.function.applyAsByte(object);
        jsonWriter.writeInt32(value);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        byte value;
        try {
            value = this.function.applyAsByte(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        this.writeInt8(jsonWriter, value);
        return true;
    }
}
