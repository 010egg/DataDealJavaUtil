// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Function;

public final class ObjectReaderImplFromString<T> extends ObjectReaderPrimitive<T>
{
    final Function<String, T> creator;
    
    public ObjectReaderImplFromString(final Class<T> objectClass, final Function<String, T> creator) {
        super(objectClass);
        this.creator = creator;
    }
    
    @Override
    public T readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final String str = jsonReader.readString();
        if (str == null || str.isEmpty()) {
            return null;
        }
        return this.creator.apply(str);
    }
    
    @Override
    public T readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final String str = jsonReader.readString();
        if (str == null) {
            return null;
        }
        return this.creator.apply(str);
    }
}
