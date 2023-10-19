// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.IntFunction;

public final class ObjectReaderImplFromInt<T> extends ObjectReaderPrimitive<T>
{
    final IntFunction<T> creator;
    
    public ObjectReaderImplFromInt(final Class<T> objectClass, final IntFunction creator) {
        super(objectClass);
        this.creator = (IntFunction<T>)creator;
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        return this.creator.apply(jsonReader.readInt32Value());
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        return this.creator.apply(jsonReader.readInt32Value());
    }
}
