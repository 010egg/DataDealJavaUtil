// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.function.ObjByteConsumer;

final class FieldReaderInt8ValueFunc<T> extends FieldReader<T>
{
    final ObjByteConsumer<T> function;
    
    public FieldReaderInt8ValueFunc(final String fieldName, final int ordinal, final JSONSchema schema, final Method method, final ObjByteConsumer<T> function) {
        super(fieldName, Byte.TYPE, Byte.TYPE, ordinal, 0L, null, null, null, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final byte value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final byte byteValue = TypeUtils.toByteValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(byteValue);
        }
        this.function.accept(object, byteValue);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final byte value = (byte)jsonReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return (byte)jsonReader.readInt32Value();
    }
}
