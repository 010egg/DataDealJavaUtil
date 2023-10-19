// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.OptionalInt;

final class ObjectReaderImplOptionalInt extends ObjectReaderPrimitive
{
    static final ObjectReaderImplOptionalInt INSTANCE;
    
    public ObjectReaderImplOptionalInt() {
        super(OptionalInt.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Integer integer = jsonReader.readInt32();
        if (integer == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(integer);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Integer integer = jsonReader.readInt32();
        if (integer == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(integer);
    }
    
    static {
        INSTANCE = new ObjectReaderImplOptionalInt();
    }
}
