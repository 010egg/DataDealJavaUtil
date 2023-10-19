// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;
import java.lang.reflect.Parameter;

class FieldReaderObjectParam<T> extends FieldReaderObject<T>
{
    final Parameter parameter;
    final String paramName;
    final long paramNameHash;
    
    FieldReaderObjectParam(final String fieldName, final Type fieldType, final Class fieldClass, final String paramName, final Parameter parameter, final int ordinal, final long features, final String format, final JSONSchema schema) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, null, null, schema, null, null, null);
        this.paramName = paramName;
        this.paramNameHash = Fnv.hashCode64(paramName);
        this.parameter = parameter;
    }
    
    @Override
    public void accept(final T object, final Object value) {
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        throw new JSONException("UnsupportedOperationException");
    }
}
