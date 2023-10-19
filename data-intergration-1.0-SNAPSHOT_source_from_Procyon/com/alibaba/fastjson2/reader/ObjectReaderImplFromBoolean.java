// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Function;

public final class ObjectReaderImplFromBoolean<T> extends ObjectReaderPrimitive<T>
{
    final Function<Boolean, T> creator;
    
    public ObjectReaderImplFromBoolean(final Class<T> objectClass, final Function<Boolean, T> creator) {
        super(objectClass);
        this.creator = creator;
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        return this.creator.apply(jsonReader.readBoolValue());
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        return this.creator.apply(jsonReader.readBoolValue());
    }
}
