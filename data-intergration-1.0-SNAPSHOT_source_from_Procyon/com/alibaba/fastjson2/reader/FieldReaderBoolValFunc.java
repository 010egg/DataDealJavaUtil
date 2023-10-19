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
import com.alibaba.fastjson2.function.ObjBoolConsumer;

final class FieldReaderBoolValFunc<T> extends FieldReader<T>
{
    final ObjBoolConsumer<T> function;
    
    public FieldReaderBoolValFunc(final String fieldName, final int ordinal, final JSONSchema schema, final Method method, final ObjBoolConsumer<T> function) {
        super(fieldName, Boolean.TYPE, Boolean.TYPE, ordinal, 0L, null, null, null, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final boolean booleanValue = TypeUtils.toBooleanValue(value);
        this.function.accept(object, booleanValue);
    }
    
    @Override
    public void accept(final T object, final boolean value) {
        this.function.accept(object, value);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        this.function.accept(object, jsonReader.readBoolValue());
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readBool();
    }
}
