// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.List;
import java.util.ArrayList;
import java.util.Base64;
import java.lang.reflect.Array;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

class ObjectReaderImplGenericArray implements ObjectReader
{
    final Type arrayType;
    final Class arrayClass;
    final Type itemType;
    final Class<?> componentClass;
    ObjectReader itemObjectReader;
    final String arrayClassName;
    final long arrayClassNameHash;
    
    public ObjectReaderImplGenericArray(final GenericArrayType genericType) {
        this.arrayType = genericType;
        this.arrayClass = TypeUtils.getClass(this.arrayType);
        this.itemType = genericType.getGenericComponentType();
        this.componentClass = TypeUtils.getMapping(this.itemType);
        this.arrayClassName = "[" + TypeUtils.getTypeName(this.componentClass);
        this.arrayClassNameHash = Fnv.hashCode64(this.arrayClassName);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfMatch((byte)(-110))) {
            final long typeHash = jsonReader.readTypeHashCode();
            if (typeHash != this.arrayClassNameHash) {
                final String typeName = jsonReader.getString();
                throw new JSONException("not support input typeName " + typeName);
            }
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt > 0 && this.itemObjectReader == null) {
            this.itemObjectReader = jsonReader.getContext().getObjectReader(this.itemType);
        }
        final Object array = Array.newInstance(this.componentClass, entryCnt);
        for (int i = 0; i < entryCnt; ++i) {
            final Object item = this.itemObjectReader.readJSONBObject(jsonReader, this.itemType, null, 0L);
            Array.set(array, i, item);
        }
        return array;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (this.itemObjectReader == null) {
            this.itemObjectReader = jsonReader.getContext().getObjectReader(this.itemType);
        }
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, 0L);
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        final char ch = jsonReader.current();
        if (ch == '\"') {
            if (fieldType instanceof GenericArrayType && ((GenericArrayType)fieldType).getGenericComponentType() == Byte.TYPE) {
                byte[] bytes;
                if ((jsonReader.features(features) & JSONReader.Feature.Base64StringAsByteArray.mask) != 0x0L) {
                    final String str = jsonReader.readString();
                    bytes = Base64.getDecoder().decode(str);
                }
                else {
                    bytes = jsonReader.readBinary();
                }
                return bytes;
            }
            final String str2 = jsonReader.readString();
            if (str2.isEmpty()) {
                return null;
            }
            throw new JSONException(jsonReader.info());
        }
        else {
            final List<Object> list = new ArrayList<Object>();
            if (ch != '[') {
                throw new JSONException(jsonReader.info());
            }
            jsonReader.next();
            while (!jsonReader.nextIfArrayEnd()) {
                Object item;
                if (this.itemObjectReader != null) {
                    item = this.itemObjectReader.readObject(jsonReader, this.itemType, null, 0L);
                }
                else {
                    if (this.itemType != String.class) {
                        throw new JSONException(jsonReader.info("TODO : " + this.itemType));
                    }
                    item = jsonReader.readString();
                }
                list.add(item);
                jsonReader.nextIfComma();
            }
            jsonReader.nextIfComma();
            final Object array = Array.newInstance(this.componentClass, list.size());
            for (int i = 0; i < list.size(); ++i) {
                Array.set(array, i, list.get(i));
            }
            return array;
        }
    }
}
