// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplInt64 extends ObjectReaderPrimitive<Long>
{
    static final ObjectReaderImplInt64 INSTANCE;
    
    ObjectReaderImplInt64() {
        super(Long.class);
    }
    
    @Override
    public Long readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readInt64();
    }
    
    @Override
    public Long readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readInt64();
    }
    
    static {
        INSTANCE = new ObjectReaderImplInt64();
    }
}
