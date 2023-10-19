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

final class FieldReaderInt64Field<T> extends FieldReaderObjectField<T>
{
    FieldReaderInt64Field(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final Long defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Long fieldValue = jsonReader.readInt64();
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
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readInt64();
    }
    
    @Override
    public void accept(final T object, final float value) {
        this.accept(object, (Object)(long)value);
    }
    
    @Override
    public void accept(final T object, final double value) {
        this.accept(object, (Object)(long)value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Long longValue = TypeUtils.toLong(value);
        if (this.schema != null) {
            this.schema.assertValidate(longValue);
        }
        try {
            this.field.set(object, longValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
