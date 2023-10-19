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

final class FieldReaderInt8Field<T> extends FieldReaderObjectField<T>
{
    FieldReaderInt8Field(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final Byte defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Integer fieldInt = jsonReader.readInt32();
        if (this.schema != null) {
            this.schema.assertValidate(fieldInt);
        }
        try {
            this.field.set(object, (fieldInt == null) ? null : Byte.valueOf(fieldInt.byteValue()));
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final short value) {
        this.accept(object, (Object)(byte)value);
    }
    
    @Override
    public void accept(final T object, final float value) {
        this.accept(object, (Object)(byte)value);
    }
    
    @Override
    public void accept(final T object, final double value) {
        this.accept(object, (Object)(byte)value);
    }
    
    @Override
    public void accept(final T object, final int value) {
        this.accept(object, (Object)(byte)value);
    }
    
    @Override
    public void accept(final T object, final long value) {
        this.accept(object, (Object)(byte)value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Byte byteValue = TypeUtils.toByte(value);
        if (this.schema != null) {
            this.schema.assertValidate(byteValue);
        }
        try {
            this.field.set(object, byteValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return (byte)jsonReader.readInt32Value();
    }
}
