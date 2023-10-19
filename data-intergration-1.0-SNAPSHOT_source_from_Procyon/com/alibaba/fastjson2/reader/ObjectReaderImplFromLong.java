// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.LongFunction;

public final class ObjectReaderImplFromLong<T> extends ObjectReaderPrimitive<T>
{
    final LongFunction<T> creator;
    
    public ObjectReaderImplFromLong(final Class<T> objectClass, final LongFunction creator) {
        super(objectClass);
        this.creator = (LongFunction<T>)creator;
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
