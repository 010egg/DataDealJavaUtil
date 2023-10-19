// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.math.BigInteger;
import java.util.Locale;
import java.lang.reflect.Type;

final class FieldReaderBigIntegerMethod<T> extends FieldReaderObject<T>
{
    FieldReaderBigIntegerMethod(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final BigInteger defaultValue, final JSONSchema schema, final Method method) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, null, null);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final BigInteger fieldValue = jsonReader.readBigInteger();
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        try {
            this.method.invoke(object, fieldValue);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final BigInteger bigInteger = TypeUtils.toBigInteger(value);
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        try {
            this.method.invoke(object, bigInteger);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final int value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        try {
            this.method.invoke(object, BigInteger.valueOf(value));
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final long value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        try {
            this.method.invoke(object, BigInteger.valueOf(value));
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
