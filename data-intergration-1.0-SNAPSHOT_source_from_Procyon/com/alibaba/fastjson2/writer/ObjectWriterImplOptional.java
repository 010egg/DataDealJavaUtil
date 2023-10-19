// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.Optional;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Locale;
import java.lang.reflect.Type;

final class ObjectWriterImplOptional extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplOptional INSTANCE;
    Type valueType;
    long features;
    final String format;
    final Locale locale;
    
    public static ObjectWriterImplOptional of(final String format, final Locale locale) {
        if (format == null) {
            return ObjectWriterImplOptional.INSTANCE;
        }
        return new ObjectWriterImplOptional(format, locale);
    }
    
    public ObjectWriterImplOptional(final String format, final Locale locale) {
        this.format = format;
        this.locale = locale;
    }
    
    public ObjectWriterImplOptional(final Type valueType, final String format, final Locale locale) {
        this.valueType = valueType;
        this.format = format;
        this.locale = locale;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final Optional optional = (Optional)object;
        if (!optional.isPresent()) {
            jsonWriter.writeNull();
            return;
        }
        final Object value = optional.get();
        final ObjectWriter objectWriter = jsonWriter.getObjectWriter(value.getClass());
        objectWriter.writeJSONB(jsonWriter, value, fieldName, null, features);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final Optional optional = (Optional)object;
        if (!optional.isPresent()) {
            jsonWriter.writeNull();
            return;
        }
        final Object value = optional.get();
        final Class<?> valueClass = value.getClass();
        ObjectWriter valueWriter = null;
        if (this.format != null) {
            valueWriter = FieldWriter.getObjectWriter(null, null, this.format, this.locale, valueClass);
        }
        if (valueWriter == null) {
            valueWriter = jsonWriter.getObjectWriter(valueClass);
        }
        valueWriter.write(jsonWriter, value, fieldName, this.valueType, this.features);
    }
    
    static {
        INSTANCE = new ObjectWriterImplOptional(null, null);
    }
}
