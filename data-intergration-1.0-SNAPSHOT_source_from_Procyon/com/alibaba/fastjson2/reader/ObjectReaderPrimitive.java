// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;

abstract class ObjectReaderPrimitive<T> implements ObjectReader<T>
{
    protected final Class objectClass;
    
    public ObjectReaderPrimitive(final Class objectClass) {
        this.objectClass = objectClass;
    }
    
    @Override
    public Class getObjectClass() {
        return this.objectClass;
    }
    
    @Override
    public T createInstance(final long features) {
        throw new JSONException("UnsupportedOperation");
    }
    
    @Override
    public abstract T readJSONBObject(final JSONReader p0, final Type p1, final Object p2, final long p3);
}
