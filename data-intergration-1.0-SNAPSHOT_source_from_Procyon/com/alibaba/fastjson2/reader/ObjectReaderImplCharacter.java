// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplCharacter extends ObjectReaderPrimitive
{
    static final ObjectReaderImplCharacter INSTANCE;
    
    ObjectReaderImplCharacter() {
        super(Character.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        return jsonReader.readCharValue();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final String str = jsonReader.readString();
        if (str == null) {
            return null;
        }
        return str.charAt(0);
    }
    
    static {
        INSTANCE = new ObjectReaderImplCharacter();
    }
}
