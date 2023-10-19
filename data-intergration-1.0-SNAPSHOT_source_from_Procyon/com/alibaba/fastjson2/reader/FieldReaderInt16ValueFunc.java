// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import com.alibaba.fastjson2.function.ObjShortConsumer;

final class FieldReaderInt16ValueFunc<T> extends FieldReader<T>
{
    final ObjShortConsumer<T> function;
    
    public FieldReaderInt16ValueFunc(final String fieldName, final int ordinal, final long features, final String format, final Locale locale, final Short defaultValue, final JSONSchema schema, final Method method, final ObjShortConsumer<T> function) {
        super(fieldName, Short.TYPE, Short.TYPE, ordinal, features, format, locale, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final short value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final short shortValue = TypeUtils.toShortValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(shortValue);
        }
        this.function.accept(object, shortValue);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final short fieldInt = (short)jsonReader.readInt32Value();
        if (this.schema != null) {
            this.schema.assertValidate(fieldInt);
        }
        this.function.accept(object, fieldInt);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return (short)jsonReader.readInt32Value();
    }
}
