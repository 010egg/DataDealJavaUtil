// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.util.Arrays;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Function;

final class ObjectReaderImplCharValueArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplCharValueArray INSTANCE;
    static final long TYPE_HASH;
    final Function<char[], Object> builder;
    
    public ObjectReaderImplCharValueArray(final Function<char[], Object> builder) {
        super(char[].class);
        this.builder = builder;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.current() == '\"') {
            final String str = jsonReader.readString();
            final char[] chars = str.toCharArray();
            if (this.builder != null) {
                return this.builder.apply(chars);
            }
            return chars;
        }
        else {
            if (!jsonReader.nextIfArrayStart()) {
                throw new JSONException(jsonReader.info("TODO"));
            }
            char[] values = new char[16];
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
                if (jsonReader.isInt()) {
                    values[size++] = (char)jsonReader.readInt32Value();
                }
                else {
                    final String str2 = jsonReader.readString();
                    values[size++] = ((str2 == null) ? '\0' : str2.charAt(0));
                }
            }
            jsonReader.nextIfComma();
            final char[] chars2 = Arrays.copyOf(values, size);
            if (this.builder != null) {
                return this.builder.apply(chars2);
            }
            return chars2;
        }
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfMatch((byte)(-110))) {
            final long typeHashCode = jsonReader.readTypeHashCode();
            if (typeHashCode != ObjectReaderImplCharValueArray.TYPE_HASH) {
                throw new JSONException("not support autoType : " + jsonReader.getString());
            }
        }
        if (jsonReader.isString()) {
            final String str = jsonReader.readString();
            return str.toCharArray();
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final char[] chars = new char[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            if (jsonReader.isInt()) {
                chars[i] = (char)jsonReader.readInt32Value();
            }
            else {
                chars[i] = jsonReader.readString().charAt(0);
            }
        }
        if (this.builder != null) {
            return this.builder.apply(chars);
        }
        return chars;
    }
    
    static {
        INSTANCE = new ObjectReaderImplCharValueArray((Function<char[], Object>)null);
        TYPE_HASH = Fnv.hashCode64("[C");
    }
}
