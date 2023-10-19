// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.util.function.BiConsumer;

final class FieldReaderStringFunc<T, V> extends FieldReader<T>
{
    final BiConsumer<T, V> function;
    final String format;
    final boolean trim;
    
    FieldReaderStringFunc(final String fieldName, final Class<V> fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final BiConsumer<T, V> function) {
        super(fieldName, fieldClass, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, null);
        this.function = function;
        this.format = format;
        this.trim = ("trim".equals(format) || (features & JSONReader.Feature.TrimString.mask) != 0x0L);
    }
    
    @Override
    public void accept(final T object, final int value) {
        this.accept(object, Integer.toString(value));
    }
    
    @Override
    public void accept(final T object, final long value) {
        this.accept(object, Long.toString(value));
    }
    
    @Override
    public void accept(final T object, final Object value) {
        String fieldValue;
        if (value instanceof String || value == null) {
            fieldValue = (String)value;
        }
        else {
            fieldValue = value.toString();
        }
        if (this.trim && fieldValue != null) {
            fieldValue = fieldValue.trim();
        }
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        try {
            this.function.accept(object, (V)fieldValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        String fieldValue = jsonReader.readString();
        if (this.trim && fieldValue != null) {
            fieldValue = fieldValue.trim();
        }
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        this.function.accept(object, (V)fieldValue);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readString();
    }
    
    @Override
    public boolean supportAcceptType(final Class valueClass) {
        return true;
    }
    
    @Override
    public BiConsumer getFunction() {
        return this.function;
    }
}
