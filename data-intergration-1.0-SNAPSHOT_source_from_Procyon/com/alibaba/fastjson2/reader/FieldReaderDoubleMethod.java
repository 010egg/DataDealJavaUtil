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
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;

final class FieldReaderDoubleMethod<T> extends FieldReaderObject<T>
{
    FieldReaderDoubleMethod(final String fieldName, final int ordinal, final long features, final String format, final Double defaultValue, final JSONSchema schema, final Method setter) {
        super(fieldName, Double.class, Double.class, ordinal, features, format, null, defaultValue, schema, setter, null, null);
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
            this.method.invoke(object, fieldValue);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Double doubleValue = TypeUtils.toDouble(value);
        if (this.schema != null) {
            this.schema.assertValidate(doubleValue);
        }
        try {
            this.method.invoke(object, doubleValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
}
