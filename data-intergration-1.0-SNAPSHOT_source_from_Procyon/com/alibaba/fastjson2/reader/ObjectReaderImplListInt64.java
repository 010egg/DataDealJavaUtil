// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.LinkedHashSet;
import java.util.function.Function;
import com.alibaba.fastjson2.JSONArray;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Iterator;
import java.util.Collection;
import com.alibaba.fastjson2.JSONException;
import java.util.LinkedList;
import java.util.ArrayList;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.util.TypeUtils;

public final class ObjectReaderImplListInt64 implements ObjectReader
{
    final Class listType;
    final Class instanceType;
    final long instanceTypeHash;
    
    public ObjectReaderImplListInt64(final Class listType, final Class instanceType) {
        this.listType = listType;
        this.instanceType = instanceType;
        this.instanceTypeHash = Fnv.hashCode64(TypeUtils.getTypeName(instanceType));
    }
    
    @Override
    public Object createInstance(final long features) {
        if (this.instanceType == ArrayList.class) {
            return new ArrayList();
        }
        if (this.instanceType == LinkedList.class) {
            return new LinkedList();
        }
        try {
            return this.instanceType.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            throw new JSONException("create list error, type " + this.instanceType);
        }
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final Collection list = (Collection)this.createInstance(0L);
        for (final Object item : collection) {
            list.add(TypeUtils.toLong(item));
        }
        return list;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        Class listType = this.listType;
        final ObjectReader objectReader = jsonReader.checkAutoType(listType, this.instanceTypeHash, features);
        if (objectReader != null) {
            listType = objectReader.getObjectClass();
        }
        Collection list;
        if (listType == ArrayList.class) {
            list = new ArrayList();
        }
        else if (listType == JSONArray.class) {
            list = new JSONArray();
        }
        else if (listType != null && listType != this.listType) {
            list = objectReader.createInstance(features);
        }
        else {
            list = (Collection)this.createInstance(jsonReader.getContext().getFeatures() | features);
        }
        for (int entryCnt = jsonReader.startArray(), i = 0; i < entryCnt; ++i) {
            list.add(jsonReader.readInt64());
        }
        if (objectReader != null) {
            final Function buildFunction = objectReader.getBuildFunction();
            if (buildFunction != null) {
                list = buildFunction.apply(list);
            }
        }
        return list;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, 0L);
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        if (jsonReader.isString()) {
            final Collection list = (Collection)this.createInstance(jsonReader.getContext().getFeatures() | features);
            final String str = jsonReader.readString();
            if (str.indexOf(44) != -1) {
                final String[] items = str.split(",");
                for (int i = 0; i < items.length; ++i) {
                    final String item = items[i];
                    list.add(Long.parseLong(item));
                }
            }
            else {
                list.add(Long.parseLong(str));
            }
            jsonReader.nextIfComma();
            return list;
        }
        final boolean set = jsonReader.nextIfSet();
        if (jsonReader.current() != '[') {
            throw new JSONException(jsonReader.info("format error"));
        }
        jsonReader.next();
        Collection list2;
        if (set && this.instanceType == Collection.class) {
            list2 = new LinkedHashSet();
        }
        else {
            list2 = (Collection)this.createInstance(jsonReader.getContext().getFeatures() | features);
        }
        while (!jsonReader.isEnd()) {
            if (jsonReader.nextIfArrayEnd()) {
                jsonReader.nextIfComma();
                return list2;
            }
            list2.add(jsonReader.readInt64());
        }
        throw new JSONException(jsonReader.info("illegal input error"));
    }
}
