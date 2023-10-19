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
import java.util.function.ObjDoubleConsumer;

final class FieldReaderDoubleValueFunc<T> extends FieldReader<T>
{
    final ObjDoubleConsumer<T> function;
    
    public FieldReaderDoubleValueFunc(final String fieldName, final int ordinal, final Double defaultValue, final JSONSchema schema, final Method method, final ObjDoubleConsumer<T> function) {
        super(fieldName, Double.TYPE, Double.TYPE, ordinal, 0L, null, null, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final double value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final double doubleValue = TypeUtils.toDoubleValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        this.function.accept(object, doubleValue);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final double value = jsonReader.readDoubleValue();
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readDoubleValue();
    }
}
