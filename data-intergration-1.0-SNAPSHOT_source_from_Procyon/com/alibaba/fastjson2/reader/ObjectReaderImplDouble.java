// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplDouble extends ObjectReaderPrimitive
{
    static final ObjectReaderImplDouble INSTANCE;
    
    ObjectReaderImplDouble() {
        super(Double.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readDouble();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readDouble();
    }
    
    static {
        INSTANCE = new ObjectReaderImplDouble();
    }
}
