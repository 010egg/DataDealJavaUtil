// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONException;
import java.util.Arrays;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplBoolValueArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplBoolValueArray INSTANCE;
    static final long TYPE_HASH;
    
    ObjectReaderImplBoolValueArray() {
        super(boolean[].class);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            boolean[] values = new boolean[16];
            int size = 0;
            while (!jsonReader.nextIfArrayEnd()) {
                final int minCapacity = size + 1;
                if (minCapacity - values.length > 0) {
                    final int oldCapacity = values.length;
                    int newCapacity = oldCapacity + (oldCapacity >> 1);
                    if (newCapacity - minCapacity < 0) {
                        newCapacity = minCapacity;
                    }
                    values = Arrays.copyOf(values, newCapacity);
                }
                values[size++] = jsonReader.readBoolValue();
            }
            jsonReader.nextIfComma();
            return Arrays.copyOf(values, size);
        }
        if (!jsonReader.isString()) {
            throw new JSONException(jsonReader.info("TODO"));
        }
        final String str = jsonReader.readString();
        if (str.isEmpty()) {
            return null;
        }
        throw new JSONException(jsonReader.info("not support input " + str));
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfMatch((byte)(-110))) {
            final long typeHashCode = jsonReader.readTypeHashCode();
            if (typeHashCode != ObjectReaderImplBoolValueArray.TYPE_HASH) {
                throw new JSONException("not support autoType : " + jsonReader.getString());
            }
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final boolean[] array = new boolean[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = jsonReader.readBoolValue();
        }
        return array;
    }
    
    static {
        INSTANCE = new ObjectReaderImplBoolValueArray();
        TYPE_HASH = Fnv.hashCode64("[Z");
    }
}
