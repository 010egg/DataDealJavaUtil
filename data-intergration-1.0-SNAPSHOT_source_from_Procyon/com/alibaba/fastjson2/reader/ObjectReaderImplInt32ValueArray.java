// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import java.util.Arrays;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Function;

public final class ObjectReaderImplInt32ValueArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplInt32ValueArray INSTANCE;
    public static final long HASH_TYPE;
    final Function<int[], Object> builder;
    
    ObjectReaderImplInt32ValueArray(final Class objectClass, final Function<int[], Object> builder) {
        super(objectClass);
        this.builder = builder;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            int[] values = new int[16];
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
                values[size++] = jsonReader.readInt32Value();
            }
            jsonReader.nextIfComma();
            final int[] array = Arrays.copyOf(values, size);
            if (this.builder != null) {
                return this.builder.apply(array);
            }
            return array;
        }
        else {
            if (!jsonReader.isString()) {
                throw new JSONException(jsonReader.info("TODO"));
            }
            final String str = jsonReader.readString();
            if (str.isEmpty()) {
                return null;
            }
            throw new JSONException(jsonReader.info("not support input " + str));
        }
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfMatch((byte)(-110))) {
            final long typeHashCode = jsonReader.readTypeHashCode();
            if (typeHashCode != ObjectReaderImplInt32ValueArray.HASH_TYPE && typeHashCode != ObjectReaderImplInt32Array.HASH_TYPE) {
                throw new JSONException("not support autoType : " + jsonReader.getString());
            }
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final int[] array = new int[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = jsonReader.readInt32Value();
        }
        if (this.builder != null) {
            return this.builder.apply(array);
        }
        return array;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final int[] array = new int[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            int value;
            if (item == null) {
                value = 0;
            }
            else if (item instanceof Number) {
                value = ((Number)item).intValue();
            }
            else {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(item.getClass(), Integer.TYPE);
                if (typeConvert == null) {
                    throw new JSONException("can not cast to int " + item.getClass());
                }
                value = typeConvert.apply(item);
            }
            array[i++] = value;
        }
        if (this.builder != null) {
            return this.builder.apply(array);
        }
        return array;
    }
    
    static {
        INSTANCE = new ObjectReaderImplInt32ValueArray(int[].class, null);
        HASH_TYPE = Fnv.hashCode64("[I");
    }
}
