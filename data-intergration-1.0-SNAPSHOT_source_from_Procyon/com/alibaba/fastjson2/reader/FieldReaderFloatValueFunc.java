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
import com.alibaba.fastjson2.function.ObjFloatConsumer;

final class FieldReaderFloatValueFunc<T> extends FieldReader<T>
{
    final ObjFloatConsumer<T> function;
    
    public FieldReaderFloatValueFunc(final String fieldName, final int ordinal, final Float defaultValue, final JSONSchema schema, final Method method, final ObjFloatConsumer<T> function) {
        super(fieldName, Float.TYPE, Float.TYPE, ordinal, 0L, null, null, defaultValue, schema, method, null);
        this.function = function;
    }
    
    @Override
    public void accept(final T object, final float value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        this.function.accept(object, value);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        final float floatValue = TypeUtils.toFloatValue(value);
        if (this.schema != null) {
            this.schema.assertValidate(floatValue);
        }
        this.function.accept(object, floatValue);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        final float fieldValue = jsonReader.readFloatValue();
        if (this.schema != null) {
            this.schema.assertValidate(fieldValue);
        }
        this.function.accept(object, fieldValue);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        return jsonReader.readFloatValue();
    }
}
