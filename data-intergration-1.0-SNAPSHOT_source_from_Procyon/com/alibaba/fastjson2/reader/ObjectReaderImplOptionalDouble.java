// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.OptionalDouble;

class ObjectReaderImplOptionalDouble extends ObjectReaderPrimitive
{
    static final ObjectReaderImplOptionalDouble INSTANCE;
    
    public ObjectReaderImplOptionalDouble() {
        super(OptionalDouble.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Double value = jsonReader.readDouble();
        if (value == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(value);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final Double value = jsonReader.readDouble();
        if (value == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(value);
    }
    
    static {
        INSTANCE = new ObjectReaderImplOptionalDouble();
    }
}
