// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.function.Function;

public class ObjectReaderImplValueString<T> implements ObjectReader<T>
{
    final long features;
    final Function<String, T> function;
    final JSONSchema schema;
    
    public ObjectReaderImplValueString(final Class<T> objectClass, final long features, final JSONSchema schema, final Function<String, T> function) {
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
        final String value = jsonReader.readString();
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
    
    public static <T> ObjectReaderImplValueString<T> of(final Class<T> objectClass, final Function<String, T> function) {
        return new ObjectReaderImplValueString<T>(objectClass, 0L, null, function);
    }
    
    public static <T> ObjectReaderImplValueString<T> of(final Class<T> objectClass, final long features, final JSONSchema schema, final Function<String, T> function) {
        return new ObjectReaderImplValueString<T>(objectClass, features, schema, function);
    }
}
