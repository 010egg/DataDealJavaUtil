// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.math.BigInteger;

final class FieldReaderBigIntegerField<T> extends FieldReaderObjectField<T>
{
    FieldReaderBigIntegerField(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final BigInteger defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final BigInteger fieldValue = jsonReader.readBigInteger();
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        try {
            this.field.set(object, fieldValue);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final int value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        try {
            this.field.set(object, BigInteger.valueOf(value));
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
            this.field.set(object, BigInteger.valueOf(value));
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final BigInteger bigInteger = TypeUtils.toBigInteger(value);
        if (this.schema != null) {
            this.schema.assertValidate(bigInteger);
        }
        try {
            this.field.set(object, bigInteger);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
