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

final class FieldReaderBoolField<T> extends FieldReaderObjectField<T>
{
    FieldReaderBoolField(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final Boolean defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Boolean fieldValue = jsonReader.readBool();
        try {
            this.field.set(object, fieldValue);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final boolean value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, final int value) {
        this.accept(object, TypeUtils.toBoolean(value));
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Boolean booleanValue = TypeUtils.toBoolean(value);
        if (this.schema != null) {
            this.schema.assertValidate(booleanValue);
        }
        try {
            this.field.set(object, booleanValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readBool();
    }
}
