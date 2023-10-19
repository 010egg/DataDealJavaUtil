// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.math.BigInteger;

final class ObjectReaderImplBigInteger extends ObjectReaderPrimitive<BigInteger>
{
    static final ObjectReaderImplBigInteger INSTANCE;
    
    public ObjectReaderImplBigInteger() {
        super(BigInteger.class);
    }
    
    @Override
    public BigInteger readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readBigInteger();
    }
    
    @Override
    public BigInteger readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readBigInteger();
    }
    
    static {
        INSTANCE = new ObjectReaderImplBigInteger();
    }
}
