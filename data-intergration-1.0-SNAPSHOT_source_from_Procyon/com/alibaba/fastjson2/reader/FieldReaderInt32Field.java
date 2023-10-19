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

final class FieldReaderInt32Field<T> extends FieldReaderObjectField<T>
{
    FieldReaderInt32Field(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final Integer defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Integer fieldValue = jsonReader.readInt32();
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
        return jsonReader.readInt32();
    }
    
    @Override
    public void accept(final T object, final double value) {
        this.accept(object, (Object)(int)value);
    }
    
    @Override
    public void accept(final T object, final float value) {
        this.accept(object, (Object)(int)value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Integer integer = TypeUtils.toInteger(value);
        if (this.schema != null) {
            this.schema.assertValidate(integer);
        }
        if (value == null && (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0x0L) {
            return;
        }
        try {
            this.field.set(object, integer);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
