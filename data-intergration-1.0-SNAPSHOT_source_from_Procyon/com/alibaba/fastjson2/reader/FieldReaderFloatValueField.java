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

final class FieldReaderFloatValueField<T> extends FieldReaderObjectField<T>
{
    FieldReaderFloatValueField(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final Float defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final float fieldFloat = jsonReader.readFloatValue();
        if (this.schema != null) {
            this.schema.assertValidate(fieldFloat);
        }
        try {
            this.field.setFloat(object, fieldFloat);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readFloatValue();
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final float floatValue = TypeUtils.toFloatValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(floatValue);
        }
        try {
            this.field.setFloat(object, floatValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
