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

class ObjectReaderImplDoubleValueArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplDoubleValueArray INSTANCE;
    static final long TYPE_HASH;
    final Function<double[], Object> builder;
    
    ObjectReaderImplDoubleValueArray(final Function<double[], Object> builder) {
        super(double[].class);
        this.builder = builder;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            double[] values = new double[16];
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
                values[size++] = jsonReader.readDoubleValue();
            }
            jsonReader.nextIfComma();
            final double[] array = Arrays.copyOf(values, size);
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
            if (typeHashCode != ObjectReaderImplDoubleValueArray.TYPE_HASH) {
                throw new JSONException("not support autoType : " + jsonReader.getString());
            }
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final double[] array = new double[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = jsonReader.readDoubleValue();
        }
        if (this.builder != null) {
            return this.builder.apply(array);
        }
        return array;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final double[] array = new double[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            double value;
            if (item == null) {
                value = 0.0;
            }
            else if (item instanceof Number) {
                value = ((Number)item).doubleValue();
            }
            else {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(item.getClass(), Double.TYPE);
                if (typeConvert == null) {
                    throw new JSONException("can not cast to double " + item.getClass());
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
        INSTANCE = new ObjectReaderImplDoubleValueArray((Function<double[], Object>)null);
        TYPE_HASH = Fnv.hashCode64("[D");
    }
}
