// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.BitSet;

public final class ObjectReaderImplBitSet extends ObjectReaderPrimitive<BitSet>
{
    static final ObjectReaderImplBitSet INSTANCE;
    public static final long HASH_TYPE;
    
    public ObjectReaderImplBitSet() {
        super(BitSet.class);
    }
    
    @Override
    public BitSet readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        if (jsonReader.nextIfMatch((byte)(-110))) {
            final long typeHash = jsonReader.readTypeHashCode();
            if (typeHash != ObjectReaderImplBitSet.HASH_TYPE) {
                final String typeName = jsonReader.getString();
                throw new JSONException(jsonReader.info(typeName));
            }
        }
        final byte[] bytes = jsonReader.readBinary();
        return BitSet.valueOf(bytes);
    }
    
    @Override
    public BitSet readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        final byte[] bytes = jsonReader.readBinary();
        return BitSet.valueOf(bytes);
    }
    
    static {
        INSTANCE = new ObjectReaderImplBitSet();
        HASH_TYPE = Fnv.hashCode64("BitSet");
    }
}
