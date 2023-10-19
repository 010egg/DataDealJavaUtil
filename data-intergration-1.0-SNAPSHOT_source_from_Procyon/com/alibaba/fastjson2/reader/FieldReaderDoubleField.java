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

final class FieldReaderDoubleField<T> extends FieldReaderObjectField<T>
{
    FieldReaderDoubleField(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final Double defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Double fieldValue = jsonReader.readDouble();
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        if (fieldValue == null && this.defaultValue != null) {
            return;
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
        return jsonReader.readDouble();
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Double doubleValue = TypeUtils.toDouble(value);
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        try {
            this.field.set(object, doubleValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
