// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplBoolean extends ObjectReaderPrimitive
{
    static final ObjectReaderImplBoolean INSTANCE;
    
    ObjectReaderImplBoolean() {
        super(Boolean.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readBool();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readBool();
    }
    
    static {
        INSTANCE = new ObjectReaderImplBoolean();
    }
}
