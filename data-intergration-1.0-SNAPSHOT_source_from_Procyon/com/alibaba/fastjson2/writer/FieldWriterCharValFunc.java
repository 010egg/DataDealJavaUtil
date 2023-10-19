// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.function.ToCharFunction;

final class FieldWriterCharValFunc extends FieldWriter
{
    final ToCharFunction function;
    
    FieldWriterCharValFunc(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final Method method, final ToCharFunction function) {
        super(fieldName, ordinal, features, format, label, Character.TYPE, Character.TYPE, field, method);
        this.function = function;
    }
    
    @Override
    public Object getFieldValue(final Object object) {
        return this.function.applyAsChar(object);
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final Object object) {
        final char value = this.function.applyAsChar(object);
        jsonWriter.writeChar(value);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final Object object) {
        final char value = this.function.applyAsChar(object);
        this.writeFieldName(jsonWriter);
        jsonWriter.writeChar(value);
        return true;
    }
}
