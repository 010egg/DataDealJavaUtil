// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.OptionalLong;

class ObjectReaderImplOptionalLong extends ObjectReaderPrimitive
{
    static final ObjectReaderImplOptionalLong INSTANCE;
    
    public ObjectReaderImplOptionalLong() {
        super(OptionalLong.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Long integer = jsonReader.readInt64();
        if (integer == null) {
            return OptionalLong.empty();
        }
        return OptionalLong.of(integer);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Long integer = jsonReader.readInt64();
        if (integer == null) {
            return OptionalLong.empty();
        }
        return OptionalLong.of(integer);
    }
    
    static {
        INSTANCE = new ObjectReaderImplOptionalLong();
    }
}
