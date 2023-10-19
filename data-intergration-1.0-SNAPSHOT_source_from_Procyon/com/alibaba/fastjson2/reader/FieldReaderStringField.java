// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;

class FieldReaderStringField<T> extends FieldReaderObjectField<T>
{
    final boolean trim;
    final long fieldOffset;
    
    FieldReaderStringField(final String fieldName, final Class fieldType, final int ordinal, final long features, final String format, final String defaultValue, final JSONSchema schema, final Field field) {
        super(fieldName, fieldType, fieldType, ordinal, features, format, defaultValue, schema, field);
        this.trim = ("trim".equals(format) || (features & JSONReader.Feature.TrimString.mask) != 0x0L);
        this.fieldOffset = JDKUtils.UNSAFE.objectFieldOffset(field);
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
        JDKUtils.UNSAFE.putObject(object, this.fieldOffset, fieldValue);
    }
    
    @Override
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        String fieldValue = jsonReader.readString();
        if (this.trim && fieldValue != null) {
            fieldValue = fieldValue.trim();
        }
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        this.accept(object, fieldValue);
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
    public boolean supportAcceptType(final Class valueClass) {
        return true;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        String fieldValue;
        if (value != null && !(value instanceof String)) {
            fieldValue = value.toString();
        }
        else {
            fieldValue = (String)value;
        }
        if (this.trim && fieldValue != null) {
            fieldValue = fieldValue.trim();
        }
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        JDKUtils.UNSAFE.putObject(object, this.fieldOffset, fieldValue);
    }
}
