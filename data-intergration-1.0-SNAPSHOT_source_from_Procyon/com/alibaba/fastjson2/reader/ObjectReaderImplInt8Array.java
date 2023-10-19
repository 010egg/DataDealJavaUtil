// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import java.util.function.Function;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONFactory;
import java.util.Collection;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Arrays;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

class ObjectReaderImplInt8Array extends ObjectReaderPrimitive
{
    static final ObjectReaderImplInt8Array INSTANCE;
    static final long HASH_TYPE;
    final String format;
    
    public ObjectReaderImplInt8Array(final String format) {
        super(Byte[].class);
        this.format = format;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.nextIfArrayStart()) {
            Byte[] values = new Byte[16];
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
                final Integer i = jsonReader.readInt32();
                values[size++] = ((i == null) ? null : Byte.valueOf(i.byteValue()));
            }
            jsonReader.nextIfComma();
            return Arrays.copyOf(values, size);
        }
        if (jsonReader.current() == 'x') {
            return jsonReader.readBinary();
        }
        if (jsonReader.isString()) {
            if ("hex".equals(this.format)) {
                return jsonReader.readHex();
            }
            final String strVal = jsonReader.readString();
            if (strVal.isEmpty()) {
                return null;
            }
            if ("base64".equals(this.format)) {
                return Base64.getDecoder().decode(strVal);
            }
            if ("gzip,base64".equals(this.format) || "gzip".equals(this.format)) {
                final byte[] bytes = Base64.getDecoder().decode(strVal);
                GZIPInputStream gzipIn = null;
                try {
                    gzipIn = new GZIPInputStream(new ByteArrayInputStream(bytes));
                    final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    while (true) {
                        final byte[] buf = new byte[1024];
                        final int len = gzipIn.read(buf);
                        if (len == -1) {
                            break;
                        }
                        if (len <= 0) {
                            continue;
                        }
                        byteOut.write(buf, 0, len);
                    }
                    return byteOut.toByteArray();
                }
                catch (IOException ex) {
                    throw new JSONException(jsonReader.info("unzip bytes error."), ex);
                }
            }
        }
        throw new JSONException(jsonReader.info("TODO"));
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfMatch((byte)(-110))) {
            final long typeHashCode = jsonReader.readTypeHashCode();
            if (typeHashCode != ObjectReaderImplInt8Array.HASH_TYPE) {
                throw new JSONException("not support autoType : " + jsonReader.getString());
            }
        }
        if (jsonReader.isString() && "hex".equals(this.format)) {
            return jsonReader.readHex();
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt == -1) {
            return null;
        }
        final Byte[] array = new Byte[entryCnt];
        for (int i = 0; i < entryCnt; ++i) {
            final Integer integer = jsonReader.readInt32();
            array[i] = ((integer == null) ? null : Byte.valueOf(integer.byteValue()));
        }
        return array;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final Byte[] array = new Byte[collection.size()];
        int i = 0;
        for (final Object item : collection) {
            Byte value;
            if (item == null) {
                value = null;
            }
            else if (item instanceof Number) {
                value = ((Number)item).byteValue();
            }
            else {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(item.getClass(), Byte.class);
                if (typeConvert == null) {
                    throw new JSONException("can not cast to Byte " + item.getClass());
                }
                value = typeConvert.apply(item);
            }
            array[i++] = value;
        }
        return array;
    }
    
    static {
        INSTANCE = new ObjectReaderImplInt8Array((String)null);
        HASH_TYPE = Fnv.hashCode64("[Byte");
    }
}
