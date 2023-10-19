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

final class FieldReaderDoubleFunc<T> extends FieldReader<T>
{
    final BiConsumer<T, Double> function;
    
    public FieldReaderDoubleFunc(final String fieldName, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Double defaultValue, final JSONSchema schema, final Method method, final BiConsumer<T, Double> function) {
        super(fieldName, fieldClass, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Double doubleValue = TypeUtils.toDouble(value);
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        this.function.accept(object, doubleValue);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        Double fieldValue;
        try {
            fieldValue = jsonReader.readDouble();
        }
        catch (Exception e) {
            if ((jsonReader.features(this.features) & JSONReader.Feature.NullOnError.mask) == 0x0L) {
                throw e;
            }
            fieldValue = null;
        }
        if (fieldValue == null && this.defaultValue != null) {
            return;
        }
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        this.function.accept(object, fieldValue);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readDouble();
    }
    
    @Override
    public BiConsumer getFunction() {
        return this.function;
    }
}
