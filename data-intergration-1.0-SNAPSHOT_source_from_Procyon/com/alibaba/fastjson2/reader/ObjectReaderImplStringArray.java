// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Iterator;
import java.util.Collection;

public final class ObjectReaderImplStringArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplStringArray INSTANCE;
    public static final long HASH_TYPE;
    
    ObjectReaderImplStringArray() {
        super(Long[].class);
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final String[] array = new String[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            String value;
            if (item == null) {
                value = null;
            }
            else if (item instanceof String) {
                value = (String)item;
            }
            else {
                value = item.toString();
            }
            array[i++] = value;
        }
        return array;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readStringArray();
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        return jsonReader.readStringArray();
    }
    
    static {
        INSTANCE = new ObjectReaderImplStringArray();
        HASH_TYPE = Fnv.hashCode64("[String");
    }
}
