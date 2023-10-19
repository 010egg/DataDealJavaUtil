// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.Function;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import com.alibaba.fastjson2.JSONException;
import java.util.Arrays;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplNumberArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplNumberArray INSTANCE;
    
    public ObjectReaderImplNumberArray() {
        super(Number[].class);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            Number[] values = new Number[16];
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
                values[size++] = jsonReader.readNumber();
            }
            jsonReader.nextIfComma();
            return Arrays.copyOf(values, size);
        }
        throw new JSONException(jsonReader.info("TODO"));
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final Number[] array = new Number[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            array[i] = jsonReader.readNumber();
        }
        return array;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final Number[] array = new Number[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            Number value;
            if (item == null || item instanceof Number) {
                value = (Number)item;
            }
            else {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(item.getClass(), Number.class);
                if (typeConvert == null) {
                    throw new JSONException("can not cast to Number " + item.getClass());
                }
                value = typeConvert.apply(item);
            }
            array[i++] = value;
        }
        return array;
    }
    
    static {
        INSTANCE = new ObjectReaderImplNumberArray();
    }
}
