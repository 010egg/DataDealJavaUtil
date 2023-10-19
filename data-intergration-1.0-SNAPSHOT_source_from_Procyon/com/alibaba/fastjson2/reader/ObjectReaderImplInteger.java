// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplInteger extends ObjectReaderPrimitive
{
    static final ObjectReaderImplInteger INSTANCE;
    
    public ObjectReaderImplInteger() {
        super(Integer.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readInt32();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readInt32();
    }
    
    static {
        INSTANCE = new ObjectReaderImplInteger();
    }
}
