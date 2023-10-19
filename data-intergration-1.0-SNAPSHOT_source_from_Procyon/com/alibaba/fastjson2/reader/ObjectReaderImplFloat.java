// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplFloat extends ObjectReaderPrimitive
{
    static final ObjectReaderImplFloat INSTANCE;
    
    ObjectReaderImplFloat() {
        super(Float.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readFloat();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readFloat();
    }
    
    static {
        INSTANCE = new ObjectReaderImplFloat();
    }
}
