// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

public final class ObjectReaderImplByte extends ObjectReaderPrimitive<Byte>
{
    static final ObjectReaderImplByte INSTANCE;
    public static final long HASH_TYPE;
    
    ObjectReaderImplByte() {
        super(Byte.class);
    }
    
    @Override
    public Byte readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Integer i = jsonReader.readInt32();
        if (i == null) {
            return null;
        }
        return i.byteValue();
    }
    
    @Override
    public Byte readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Integer i = jsonReader.readInt32();
        if (i == null) {
            return null;
        }
        return i.byteValue();
    }
    
    static {
        INSTANCE = new ObjectReaderImplByte();
        HASH_TYPE = Fnv.hashCode64("B");
    }
}
