// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

final class FieldReaderInt32ValueMethod<T> extends FieldReaderObject<T>
{
    FieldReaderInt32ValueMethod(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Integer defaultValue, final JSONSchema schema, final Method setter) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, null, defaultValue, schema, setter, null, null);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final int fieldInt = jsonReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(fieldInt);
        }
        try {
            this.method.invoke(object, fieldInt);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        final int fieldInt = jsonReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(fieldInt);
        }
        try {
            this.method.invoke(object, fieldInt);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final int intValue = TypeUtils.toIntValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(intValue);
        }
        try {
            this.method.invoke(object, intValue);
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
            this.method.invoke(object, (int)value);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readInt32Value();
    }
}
