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

final class FieldReaderBoolValueMethod<T> extends FieldReaderObject<T>
{
    FieldReaderBoolValueMethod(final String fieldName, final int ordinal, final long features, final String format, final Boolean defaultValue, final JSONSchema schema, final Method method) {
        super(fieldName, Boolean.TYPE, Boolean.TYPE, ordinal, features, format, null, defaultValue, schema, method, null, null);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final boolean fieldValue = jsonReader.readBoolValue();
        try {
            this.method.invoke(object, fieldValue);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        final boolean fieldValue = jsonReader.readBoolValue();
        try {
            this.method.invoke(object, fieldValue);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final boolean booleanValue = TypeUtils.toBooleanValue(value);
        try {
            this.method.invoke(object, booleanValue);
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
