// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;

final class FieldReaderUUID extends FieldReaderObject
{
    public FieldReaderUUID(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final Field field, final BiConsumer function) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field, function);
        this.initReader = ObjectReaderImplUUID.INSTANCE;
    }
    
    @Override
    public ObjectReader getObjectReader(final JSONReader jsonReader) {
        return this.initReader;
    }
    
    @Override
    public ObjectReader getObjectReader(final JSONReader.Context context) {
        return this.initReader;
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final Object object) {
        this.accept(object, jsonReader.readUUID());
    }
}
