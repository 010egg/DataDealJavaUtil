// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

abstract class FieldWriterObjectFinal<T> extends FieldWriterObject<T>
{
    final Type fieldType;
    final Class fieldClass;
    volatile ObjectWriter objectWriter;
    final boolean refDetect;
    
    protected FieldWriterObjectFinal(final String name, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class fieldClass, final Field field, final Method method) {
        super(name, ordinal, features, format, label, fieldType, fieldClass, field, method);
        this.fieldType = fieldType;
        this.fieldClass = fieldClass;
        this.refDetect = !ObjectWriterProvider.isNotReferenceDetect(fieldClass);
    }
    
    @Override
    public ObjectWriter getObjectWriter(final JSONWriter jsonWriter, final Class valueClass) {
        if (this.fieldClass != valueClass) {
            return super.getObjectWriter(jsonWriter, valueClass);
        }
        if (this.objectWriter != null) {
            return this.objectWriter;
        }
        return this.objectWriter = super.getObjectWriter(jsonWriter, valueClass);
    }
    
    @Override
    public boolean write(final JSONWriter jsonWriter, final T object) {
        Object value;
        try {
            value = this.getFieldValue(object);
        }
        catch (RuntimeException error) {
            if (jsonWriter.isIgnoreErrorGetter()) {
                return false;
            }
            throw error;
        }
        if (value != null) {
            final ObjectWriter valueWriter = this.getObjectWriter(jsonWriter, this.fieldClass);
            this.writeFieldName(jsonWriter);
            if (jsonWriter.jsonb) {
                valueWriter.writeJSONB(jsonWriter, value, this.fieldName, this.fieldType, this.features);
            }
            else {
                valueWriter.write(jsonWriter, value, this.fieldName, this.fieldType, this.features);
            }
            return true;
        }
        final long features = this.features | jsonWriter.getFeatures();
        if ((features & JSONWriter.Feature.WriteNulls.mask) != 0x0L) {
            this.writeFieldName(jsonWriter);
            if (this.fieldClass.isArray()) {
                jsonWriter.writeArrayNull();
            }
            else if (this.fieldClass == StringBuffer.class || this.fieldClass == StringBuilder.class) {
                jsonWriter.writeStringNull();
            }
            else {
                jsonWriter.writeNull();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void writeValue(final JSONWriter jsonWriter, final T object) {
        final Object value = this.getFieldValue(object);
        if (value == null) {
            jsonWriter.writeNull();
            return;
        }
        final boolean refDetect = this.refDetect && jsonWriter.isRefDetect();
        if (refDetect) {
            if (value == object) {
                jsonWriter.writeReference("..");
                return;
            }
            final String refPath = jsonWriter.setPath(this.fieldName, value);
            if (refPath != null) {
                jsonWriter.writeReference(refPath);
                jsonWriter.popPath(value);
                return;
            }
        }
        final ObjectWriter valueWriter = this.getObjectWriter(jsonWriter, this.fieldClass);
        final boolean beanToArray = (jsonWriter.getFeatures(this.features) & JSONWriter.Feature.BeanToArray.mask) != 0x0L;
        if (jsonWriter.jsonb) {
            if (beanToArray) {
                valueWriter.writeArrayMappingJSONB(jsonWriter, value, this.fieldName, this.fieldType, this.features);
            }
            else {
                valueWriter.writeJSONB(jsonWriter, value, this.fieldName, this.fieldType, this.features);
            }
        }
        else if (beanToArray) {
            valueWriter.writeArrayMapping(jsonWriter, value, this.fieldName, this.fieldType, this.features);
        }
        else {
            valueWriter.write(jsonWriter, value, this.fieldName, this.fieldType, this.features);
        }
        if (refDetect) {
            jsonWriter.popPath(value);
        }
    }
}
