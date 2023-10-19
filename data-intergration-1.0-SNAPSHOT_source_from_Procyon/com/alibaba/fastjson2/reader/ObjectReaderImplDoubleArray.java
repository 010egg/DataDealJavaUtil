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

final class ObjectReaderImplDoubleArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplDoubleArray INSTANCE;
    static final long HASH_TYPE;
    
    ObjectReaderImplDoubleArray() {
        super(Double[].class);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            Double[] values = new Double[16];
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
                values[size++] = jsonReader.readDouble();
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
            if (typeHashCode != ObjectReaderImplDoubleArray.HASH_TYPE) {
                throw new JSONException("not support autoType : " + jsonReader.getString());
            }
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final Double[] array = new Double[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = jsonReader.readDouble();
        }
        return array;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final Double[] array = new Double[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            Double value;
            if (item == null) {
                value = null;
            }
            else if (item instanceof Number) {
                value = ((Number)item).doubleValue();
            }
            else {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(item.getClass(), Double.class);
                if (typeConvert == null) {
                    throw new JSONException("can not cast to Double " + item.getClass());
                }
                value = typeConvert.apply(item);
            }
            array[i++] = value;
        }
        return array;
    }
    
    static {
        INSTANCE = new ObjectReaderImplDoubleArray();
        HASH_TYPE = Fnv.hashCode64("[Double");
    }
}
