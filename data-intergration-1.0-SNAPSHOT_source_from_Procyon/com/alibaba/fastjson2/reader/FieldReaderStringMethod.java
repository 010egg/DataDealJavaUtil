// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;

final class FieldReaderStringMethod<T> extends FieldReaderObject<T>
{
    final boolean trim;
    
    FieldReaderStringMethod(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final String defaultValue, final JSONSchema schema, final Method setter) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, setter, null, null);
        this.trim = ("trim".equals(format) || (features & JSONReader.Feature.TrimString.mask) != 0x0L);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        String fieldValue = jsonReader.readString();
        if (this.trim && fieldValue != null) {
            fieldValue = fieldValue.trim();
        }
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
    public String readFieldValue(final JSONReader jsonReader) {
        String fieldValue = jsonReader.readString();
        if (this.trim && fieldValue != null) {
            fieldValue = fieldValue.trim();
        }
        return fieldValue;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        String fieldValue;
        if (value instanceof String || value == null) {
            fieldValue = (String)value;
        }
        else {
            fieldValue = value.toString();
        }
        if (this.trim && fieldValue != null) {
            fieldValue = fieldValue.trim();
        }
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        try {
            this.method.invoke(object, fieldValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public boolean supportAcceptType(final Class valueClass) {
        return true;
    }
}
