// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONReader;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.reflect.Type;

final class ObjectReaderImplAtomicReference extends ObjectReaderPrimitive
{
    static final ObjectReaderImplAtomicReference INSTANCE;
    final Type referenceType;
    
    public ObjectReaderImplAtomicReference(final Type referenceType) {
        super(AtomicReference.class);
        this.referenceType = referenceType;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        final Object value = jsonReader.read(this.referenceType);
        return new AtomicReference(value);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        final Object value = jsonReader.read(this.referenceType);
        return new AtomicReference(value);
    }
    
    static {
        INSTANCE = new ObjectReaderImplAtomicReference((Type)Object.class);
    }
}
