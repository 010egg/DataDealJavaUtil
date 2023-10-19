// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;

final class FieldReaderInt8Method<T> extends FieldReaderObject<T>
{
    FieldReaderInt8Method(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Byte defaultValue, final JSONSchema schema, final Method setter) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, setter, null, null);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final Integer fieldValue = jsonReader.readInt32();
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        try {
            this.method.invoke(object, (fieldValue == null) ? null : Byte.valueOf(fieldValue.byteValue()));
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final Byte byteValue = TypeUtils.toByte(value);
        if (this.schema != null) {
            this.schema.assertValidate(byteValue);
        }
        try {
            this.method.invoke(object, byteValue);
        }
        catch (Exception e) {
            throw new JSONException("set " + this.fieldName + " error", e);
        }
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readInt32();
    }
}
