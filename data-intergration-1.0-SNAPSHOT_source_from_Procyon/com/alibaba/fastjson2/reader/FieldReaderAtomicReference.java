// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.ParameterizedType;
import java.util.Locale;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

public abstract class FieldReaderAtomicReference<T> extends FieldReader<T>
{
    final Type referenceType;
    
    public FieldReaderAtomicReference(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final JSONSchema schema, final Method method, final Field field) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, null, null, schema, method, field);
        Type referenceType = null;
        if (fieldType instanceof ParameterizedType) {
            final ParameterizedType paramType = (ParameterizedType)fieldType;
            final Type[] arguments = paramType.getActualTypeArguments();
            if (arguments.length == 1) {
                referenceType = arguments[0];
            }
        }
        this.referenceType = referenceType;
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        if (jsonReader.nextIfNull()) {
            return;
        }
        final Object refValue = jsonReader.read(this.referenceType);
        this.accept(object, refValue);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.read(this.referenceType);
    }
}
