// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

public final class ObjectReaderImplShort extends ObjectReaderPrimitive
{
    static final ObjectReaderImplShort INSTANCE;
    public static final long HASH_TYPE;
    
    public ObjectReaderImplShort() {
        super(Short.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Integer i = jsonReader.readInt32();
        if (i == null) {
            return null;
        }
        return i.shortValue();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Integer i = jsonReader.readInt32();
        if (i == null) {
            return null;
        }
        return i.shortValue();
    }
    
    static {
        INSTANCE = new ObjectReaderImplShort();
        HASH_TYPE = Fnv.hashCode64("S");
    }
}
