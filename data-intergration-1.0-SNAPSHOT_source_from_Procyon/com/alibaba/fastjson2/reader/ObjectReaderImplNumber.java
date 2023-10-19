// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplNumber extends ObjectReaderPrimitive
{
    static final ObjectReaderImplNumber INSTANCE;
    
    public ObjectReaderImplNumber() {
        super(Number.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readNumber();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readNumber();
    }
    
    static {
        INSTANCE = new ObjectReaderImplNumber();
    }
}
