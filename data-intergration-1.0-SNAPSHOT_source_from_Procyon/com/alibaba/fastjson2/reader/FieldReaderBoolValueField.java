// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;

final class FieldReaderBoolValueField<T> extends FieldReaderObjectField<T>
{
    FieldReaderBoolValueField(final String fieldName, final int ordinal, final long features, final String format, final Boolean defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, Boolean.TYPE, Boolean.TYPE, ordinal, features, format, defaultValue, schema, field);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final boolean fieldValue = jsonReader.readBoolValue();
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        try {
            this.field.setBoolean(object, fieldValue);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("set " + this.fieldName + " error"), e);
        }
    }
    
    @Override
    public void accept(final T object, final int value) {
        this.accept(object, TypeUtils.toBooleanValue(value));
    }
    
    @Override
    public void accept(final T object, final Object value) {
        if (value == null) {
            if ((this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0x0L) {
                return;
            }
            this.accept(object, false);
        }
        else {
            if (value instanceof Boolean) {
                this.accept(object, (boolean)value);
                return;
            }
            throw new JSONException("set " + this.fieldName + " error, type not support " + value.getClass());
        }
    }
    
    @Override
    public void accept(final T object, final boolean value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (this.fieldOffset != -1L) {
            JDKUtils.UNSAFE.putBoolean(object, this.fieldOffset, value);
            return;
        }
        try {
            this.field.setBoolean(object, value);
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
