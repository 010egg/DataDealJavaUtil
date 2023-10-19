// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONException;
import java.util.Arrays;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Iterator;
import java.util.Collection;

public final class ObjectArrayReader extends ObjectReaderPrimitive
{
    public static final ObjectArrayReader INSTANCE;
    public static final long TYPE_HASH_CODE;
    
    public ObjectArrayReader() {
        super(Object[].class);
    }
    
    @Override
    public Object[] createInstance(final Collection collection) {
        final Object[] array = new Object[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            array[i++] = item;
        }
        return array;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNullOrEmptyString()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            Object[] values = new Object[16];
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
                final char ch = jsonReader.current();
                Object value = null;
                switch (ch) {
                    case '\"': {
                        value = jsonReader.readString();
                        break;
                    }
                    case '+':
                    case '-':
                    case '.':
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9': {
                        value = jsonReader.readNumber();
                        break;
                    }
                    case 'n': {
                        jsonReader.readNull();
                        value = null;
                        break;
                    }
                    case 'f':
                    case 't': {
                        value = jsonReader.readBoolValue();
                        break;
                    }
                    case '{': {
                        value = jsonReader.readObject();
                        break;
                    }
                    case '[': {
                        value = jsonReader.readArray();
                        break;
                    }
                    default: {
                        throw new JSONException(jsonReader.info());
                    }
                }
                values[size++] = value;
            }
            jsonReader.nextIfComma();
            return Arrays.copyOf(values, size);
        }
        throw new JSONException(jsonReader.info("TODO"));
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.getType() == -110) {
            final ObjectReader autoTypeObjectReader = jsonReader.checkAutoType(Object[].class, ObjectArrayReader.TYPE_HASH_CODE, features);
            if (autoTypeObjectReader != this) {
                return autoTypeObjectReader.readJSONBObject(jsonReader, fieldType, fieldName, features);
            }
        }
        final int itemCnt = jsonReader.startArray();
        if (itemCnt == -1) {
            return null;
        }
        final Object[] array = new Object[itemCnt];
        for (int i = 0; i < itemCnt; ++i) {
            final byte type = jsonReader.getType();
            Object value;
            if (type >= 73 && type <= 125) {
                value = jsonReader.readString();
            }
            else if (type == -110) {
                final ObjectReader autoTypeValueReader = jsonReader.checkAutoType(Object.class, 0L, features);
                if (autoTypeValueReader != null) {
                    value = autoTypeValueReader.readJSONBObject(jsonReader, null, null, features);
                }
                else {
                    value = jsonReader.readAny();
                }
            }
            else if (type == -81) {
                jsonReader.next();
                value = null;
            }
            else if (type == -79) {
                jsonReader.next();
                value = Boolean.TRUE;
            }
            else if (type == -80) {
                jsonReader.next();
                value = Boolean.FALSE;
            }
            else if (type == -66) {
                value = jsonReader.readInt64Value();
            }
            else {
                value = jsonReader.readAny();
            }
            array[i] = value;
        }
        return array;
    }
    
    static {
        INSTANCE = new ObjectArrayReader();
        TYPE_HASH_CODE = Fnv.hashCode64("[O");
    }
}
