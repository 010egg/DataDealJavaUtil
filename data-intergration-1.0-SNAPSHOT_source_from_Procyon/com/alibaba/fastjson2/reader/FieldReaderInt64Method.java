// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;

final class FieldReaderInt64Method<T> extends FieldReaderObject<T>
{
    FieldReaderInt64Method(final String fieldName, final int ordinal, final long features, final String format, final Locale locale, final Long defaultValue, final JSONSchema schema, final Method setter) {
        super(fieldName, Long.class, Long.class, ordinal, features, format, locale, defaultValue, schema, setter, null, null);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Long fieldValue = jsonReader.readInt64();
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
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        final Long fieldValue = jsonReader.readInt64();
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
    public void accept(final T object, final Object value) {
        final Long longValue = TypeUtils.toLong(value);
        if (this.schema != null) {
            this.schema.assertValidate(longValue);
        }
        try {
            this.method.invoke(object, longValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
