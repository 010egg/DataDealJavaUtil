// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Parameter;

final class FieldReaderInt8Param<T> extends FieldReaderObjectParam<T>
{
    FieldReaderInt8Param(final String fieldName, final Class fieldType, final String paramName, final Parameter parameter, final int ordinal, final long features, final String format, final JSONSchema schema) {
        super(fieldName, fieldType, fieldType, paramName, parameter, ordinal, features, format, schema);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        final Integer integer = jsonReader.readInt32();
        return (integer == null) ? null : Byte.valueOf(integer.byteValue());
    }
}
