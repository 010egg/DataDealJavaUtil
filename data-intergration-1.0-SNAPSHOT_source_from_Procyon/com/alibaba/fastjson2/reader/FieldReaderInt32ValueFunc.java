// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.function.ObjIntConsumer;

final class FieldReaderInt32ValueFunc<T> extends FieldReader<T>
{
    final ObjIntConsumer<T> function;
    
    public FieldReaderInt32ValueFunc(final String fieldName, final int ordinal, final Integer defaultValue, final JSONSchema schema, final Method method, final ObjIntConsumer<T> function) {
        super(fieldName, Integer.TYPE, Integer.TYPE, ordinal, 0L, null, null, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final int value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public void accept(final T object, final long value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, (int)value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final int intValue = TypeUtils.toIntValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(intValue);
        }
        this.function.accept(object, intValue);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final int value = jsonReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readInt32Value();
    }
}
