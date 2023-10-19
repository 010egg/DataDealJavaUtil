// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import java.util.Base64;
import java.util.Arrays;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.Function;

class ObjectReaderImplInt8ValueArray extends ObjectReaderPrimitive
{
    static final ObjectReaderImplInt8ValueArray INSTANCE;
    static final long HASH_TYPE;
    final String format;
    final Function<byte[], Object> builder;
    final long features;
    
    ObjectReaderImplInt8ValueArray(final String format) {
        super(byte[].class);
        this.format = format;
        this.builder = null;
        this.features = 0L;
    }
    
    ObjectReaderImplInt8ValueArray(final Function<byte[], Object> builder, final String format) {
        super(byte[].class);
        this.format = format;
        this.features = ("base64".equals(format) ? JSONReader.Feature.Base64StringAsByteArray.mask : 0L);
        this.builder = builder;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            byte[] values = new byte[16];
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
                values[size++] = (byte)jsonReader.readInt32Value();
            }
            jsonReader.nextIfComma();
            final byte[] bytes = Arrays.copyOf(values, size);
            if (this.builder != null) {
                return this.builder.apply(bytes);
            }
            return bytes;
        }
        else {
            if (!jsonReader.isString()) {
                throw new JSONException(jsonReader.info("TODO"));
            }
            byte[] bytes2;
            if ((jsonReader.features(this.features | features) & JSONReader.Feature.Base64StringAsByteArray.mask) != 0x0L) {
                final String str = jsonReader.readString();
                bytes2 = Base64.getDecoder().decode(str);
            }
            else {
                bytes2 = jsonReader.readBinary();
            }
            if (this.builder != null) {
                return this.builder.apply(bytes2);
            }
            return bytes2;
        }
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfMatch((byte)(-110))) {
            final long typeHashCode = jsonReader.readTypeHashCode();
            if (typeHashCode != ObjectReaderImplInt8ValueArray.HASH_TYPE && typeHashCode != ObjectReaderImplInt8Array.HASH_TYPE) {
                throw new JSONException("not support autoType : " + jsonReader.getString());
            }
        }
        byte[] bytes;
        if (jsonReader.isBinary()) {
            bytes = jsonReader.readBinary();
        }
        else if (jsonReader.isString()) {
            final String str = jsonReader.readString();
            bytes = Base64.getDecoder().decode(str);
        }
        else {
            final int entryCnt = jsonReader.startArray();
            if (entryCnt == -1) {
                return null;
            }
            bytes = new byte[entryCnt];
            for (int i = 0; i < entryCnt; ++i) {
                bytes[i] = (byte)jsonReader.readInt32Value();
            }
        }
        if (this.builder != null) {
            return this.builder.apply(bytes);
        }
        return bytes;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final byte[] bytes = new byte[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            byte value;
            if (item == null) {
                value = 0;
            }
            else if (item instanceof Number) {
                value = ((Number)item).byteValue();
            }
            else {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(item.getClass(), Byte.TYPE);
                if (typeConvert == null) {
                    throw new JSONException("can not cast to byte " + item.getClass());
                }
                value = typeConvert.apply(item);
            }
            bytes[i++] = value;
        }
        if (this.builder != null) {
            return this.builder.apply(bytes);
        }
        return bytes;
    }
    
    static {
        INSTANCE = new ObjectReaderImplInt8ValueArray((String)null);
        HASH_TYPE = Fnv.hashCode64("[B");
    }
}
