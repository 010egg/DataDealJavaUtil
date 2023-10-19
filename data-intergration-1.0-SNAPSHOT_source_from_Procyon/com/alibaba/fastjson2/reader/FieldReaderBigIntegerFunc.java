// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.math.BigInteger;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.util.function.BiConsumer;

final class FieldReaderBigIntegerFunc<T, V> extends FieldReader<T>
{
    final BiConsumer<T, V> function;
    
    public FieldReaderBigIntegerFunc(final String fieldName, final Class<V> fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final BiConsumer<T, V> function) {
        super(fieldName, fieldClass, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final BigInteger bigInteger = TypeUtils.toBigInteger(value);
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        try {
            this.function.accept(object, (V)bigInteger);
        }
        catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final int value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        try {
            this.function.accept(object, (V)BigInteger.valueOf(value));
        }
        catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final long value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        try {
            this.function.accept(object, (V)BigInteger.valueOf(value));
        }
        catch (Exception e) {
            throw new JSONException("set " + super.toString() + " error", e);
        }
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        BigInteger fieldValue;
        try {
            fieldValue = jsonReader.readBigInteger();
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
        return jsonReader.readBigInteger();
    }
    
    @Override
    public BiConsumer getFunction() {
        return this.function;
    }
}
