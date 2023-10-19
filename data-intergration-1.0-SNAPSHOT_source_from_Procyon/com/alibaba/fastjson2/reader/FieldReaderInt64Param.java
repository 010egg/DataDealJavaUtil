// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Parameter;

final class FieldReaderInt64Param<T> extends FieldReaderObjectParam<T>
{
    FieldReaderInt64Param(final String fieldName, final Class fieldType, final String paramName, final Parameter parameter, final int ordinal, final long features, final String format, final JSONSchema schema) {
        super(fieldName, fieldType, fieldType, paramName, parameter, ordinal, features, format, schema);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readInt64();
    }
}
