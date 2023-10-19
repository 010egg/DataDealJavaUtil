// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.function.IntFunction;

public class ObjectReaderImplValueInt<T> implements ObjectReader<T>
{
    final long features;
    final IntFunction<T> function;
    final JSONSchema schema;
    
    public ObjectReaderImplValueInt(final Class<T> objectClass, final long features, final JSONSchema schema, final IntFunction<T> function) {
        this.features = features;
        this.schema = schema;
        this.function = function;
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return this.readObject(jsonReader, fieldType, fieldName, features);
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNullOrEmptyString()) {
            return null;
        }
        final int value = jsonReader.readInt32Value();
        if (this.schema != null) {
            this.schema.validate(value);
        }
        T object;
        try {
            object = this.function.apply(value);
        }
        catch (Exception ex) {
            throw new JSONException(jsonReader.info("create object error"), ex);
        }
        return object;
    }
    
    public static <T> ObjectReaderImplValueInt<T> of(final Class<T> objectClass, final IntFunction<T> function) {
        return new ObjectReaderImplValueInt<T>(objectClass, 0L, null, function);
    }
    
    public static <T> ObjectReaderImplValueInt<T> of(final Class<T> objectClass, final long features, final JSONSchema schema, final IntFunction<T> function) {
        return new ObjectReaderImplValueInt<T>(objectClass, features, schema, function);
    }
}
