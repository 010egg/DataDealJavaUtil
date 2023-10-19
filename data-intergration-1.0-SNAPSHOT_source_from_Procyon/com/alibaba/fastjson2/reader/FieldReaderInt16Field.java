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

final class FieldReaderInt16Field<T> extends FieldReaderObjectField<T>
{
    FieldReaderInt16Field(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final Short defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final int intValue = jsonReader.readInt32Value();
        Short fieldValue;
        if (jsonReader.wasNull()) {
            fieldValue = null;
        }
        else {
            fieldValue = (short)intValue;
        }
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
    public void accept(final T object, final float value) {
        this.accept(object, (Object)(short)value);
    }
    
    @Override
    public void accept(final T object, final double value) {
        this.accept(object, (Object)(short)value);
    }
    
    @Override
    public void accept(final T object, final int value) {
        this.accept(object, (Object)(short)value);
    }
    
    @Override
    public void accept(final T object, final long value) {
        this.accept(object, (Object)(short)value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Short shortValue = TypeUtils.toShort(value);
        if (this.schema != null) {
            this.schema.assertValidate(shortValue);
        }
        try {
            this.field.set(object, shortValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return (short)jsonReader.readInt32Value();
    }
}
