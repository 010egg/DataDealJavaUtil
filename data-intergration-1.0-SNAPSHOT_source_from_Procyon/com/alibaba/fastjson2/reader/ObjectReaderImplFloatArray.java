// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.util.function.Function;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import java.util.Arrays;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

final class ObjectReaderImplFloatArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplFloatArray INSTANCE;
    static final long HASH_TYPE;
    
    ObjectReaderImplFloatArray() {
        super(Float[].class);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            Float[] values = new Float[16];
            int size = 0;
            while (!jsonReader.nextIfArrayEnd()) {
                if (jsonReader.isEnd()) {
                    throw new JSONException(jsonReader.info("input end"));
                }
                final int minCapacity = size + 1;
                if (minCapacity - values.length > 0) {
                    final int oldCapacity = values.length;
                    int newCapacity = oldCapacity + (oldCapacity >> 1);
                    if (newCapacity - minCapacity < 0) {
                        newCapacity = minCapacity;
                    }
                    values = Arrays.copyOf(values, newCapacity);
                }
                values[size++] = jsonReader.readFloat();
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
            if (typeHashCode != ObjectReaderImplFloatArray.HASH_TYPE) {
                throw new JSONException("not support autoType : " + jsonReader.getString());
            }
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final Float[] array = new Float[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = jsonReader.readFloat();
        }
        return array;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final Float[] array = new Float[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            Float value;
            if (item == null) {
                value = null;
            }
            else if (item instanceof Number) {
                value = ((Number)item).floatValue();
            }
            else {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(item.getClass(), Float.class);
                if (typeConvert == null) {
                    throw new JSONException("can not cast to Float " + item.getClass());
                }
                value = typeConvert.apply(item);
            }
            array[i++] = value;
        }
        return array;
    }
    
    static {
        INSTANCE = new ObjectReaderImplFloatArray();
        HASH_TYPE = Fnv.hashCode64("[Float");
    }
}
