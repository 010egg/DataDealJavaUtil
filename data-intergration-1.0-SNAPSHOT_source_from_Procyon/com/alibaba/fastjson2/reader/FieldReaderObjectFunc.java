// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;

final class FieldReaderObjectFunc<T, V> extends FieldReaderObject<T>
{
    FieldReaderObjectFunc(final String fieldName, final Type fieldType, final Class<V> fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final BiConsumer<T, V> function, final ObjectReader fieldObjectReader) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, null, function);
        this.initReader = fieldObjectReader;
    }
    
    @Override
    public void accept(final T object, Object value) {
        if (this.fieldType == Float.class) {
            value = TypeUtils.toFloat(value);
        }
        else if (this.fieldType == Double.class) {
            value = TypeUtils.toDouble(value);
        }
        if (value == null && this.fieldClass == StackTraceElement[].class) {
            return;
        }
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
}
