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
import java.util.function.ObjLongConsumer;

final class FieldReaderInt64ValueFunc<T> extends FieldReader<T>
{
    final ObjLongConsumer<T> function;
    
    public FieldReaderInt64ValueFunc(final String fieldName, final int ordinal, final Long defaultValue, final JSONSchema schema, final Method method, final ObjLongConsumer<T> function) {
        super(fieldName, Long.TYPE, Long.TYPE, ordinal, 0L, null, null, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final long value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final long longValue = TypeUtils.toLongValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(longValue);
        }
        this.function.accept(object, longValue);
    }
    
    @Override
    public void accept(final T object, final int value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final long fieldValue = jsonReader.readInt64Value();
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        this.function.accept(object, fieldValue);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readInt64Value();
    }
}
