// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.util.function.BiConsumer;

final class FieldReaderInt16Func<T, V> extends FieldReader<T>
{
    final BiConsumer<T, V> function;
    
    public FieldReaderInt16Func(final String fieldName, final Class<V> fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final BiConsumer<T, V> function) {
        super(fieldName, fieldClass, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Short shortValue = TypeUtils.toShort(value);
        if (this.schema != null) {
            this.schema.assertValidate(shortValue);
        }
        this.function.accept(object, (V)shortValue);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        Short fieldValue;
        try {
            final Integer value = jsonReader.readInt32();
            fieldValue = ((value == null) ? null : Short.valueOf(value.shortValue()));
        }
        catch (Exception e) {
            if ((jsonReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0x0L) {
                throw e;
            }
            fieldValue = null;
        }
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        this.function.accept(object, (V)fieldValue);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return (short)jsonReader.readInt32Value();
    }
    
    @Override
    public BiConsumer getFunction() {
        return this.function;
    }
}
