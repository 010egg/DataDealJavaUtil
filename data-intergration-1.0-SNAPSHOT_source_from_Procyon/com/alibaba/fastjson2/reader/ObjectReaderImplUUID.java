// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.UUID;

class ObjectReaderImplUUID extends ObjectReaderPrimitive
{
    static final ObjectReaderImplUUID INSTANCE;
    
    public ObjectReaderImplUUID() {
        super(UUID.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readUUID();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readUUID();
    }
    
    static {
        INSTANCE = new ObjectReaderImplUUID();
    }
}
