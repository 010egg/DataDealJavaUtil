// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.Collection;
import com.alibaba.fastjson2.JSON;
import java.util.Map;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

final class ObjectReaderImplString extends ObjectReaderPrimitive
{
    static final ObjectReaderImplString INSTANCE;
    
    public ObjectReaderImplString() {
        super(String.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readString();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readString();
    }
    
    @Override
    public Object createInstance(final Map map, final long features) {
        if (map == null) {
            return null;
        }
        return JSON.toJSONString(map);
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        if (collection == null) {
            return null;
        }
        return JSON.toJSONString(collection);
    }
    
    static {
        INSTANCE = new ObjectReaderImplString();
    }
}
