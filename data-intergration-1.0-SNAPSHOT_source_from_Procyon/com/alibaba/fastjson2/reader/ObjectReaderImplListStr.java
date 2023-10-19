// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.HashSet;
import java.util.function.Function;
import com.alibaba.fastjson2.util.GuavaSupport;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.Collections;
import com.alibaba.fastjson2.JSONArray;
import java.util.Arrays;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Iterator;
import com.alibaba.fastjson2.JSON;
import java.util.Collection;
import com.alibaba.fastjson2.JSONException;
import java.util.LinkedList;
import java.util.ArrayList;

public final class ObjectReaderImplListStr implements ObjectReader
{
    final Class listType;
    final Class instanceType;
    
    public ObjectReaderImplListStr(final Class listType, final Class instanceType) {
        this.listType = listType;
        this.instanceType = instanceType;
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
        if (this.listType.isInstance(collection)) {
            boolean typeMatch = true;
            for (final Object item : collection) {
                if (!(item instanceof String)) {
                    typeMatch = false;
                    break;
                }
            }
            if (typeMatch) {
                return collection;
            }
        }
        final Collection typedList = (Collection)this.createInstance(0L);
        for (final Object item : collection) {
            if (item == null || item instanceof String) {
                typedList.add(item);
            }
            else {
                typedList.add(JSON.toJSONString(item));
            }
        }
        return typedList;
    }
    
    @Override
    public Class getObjectClass() {
        return this.listType;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        Class instanceType = this.instanceType;
        if (jsonReader.nextIfNull()) {
            return null;
        }
        final ObjectReader objectReader = jsonReader.checkAutoType(this.listType, 0L, features);
        if (objectReader != null) {
            instanceType = objectReader.getObjectClass();
        }
        if (instanceType == ObjectReaderImplList.CLASS_ARRAYS_LIST) {
            final int entryCnt = jsonReader.startArray();
            final String[] array = new String[entryCnt];
            for (int i = 0; i < entryCnt; ++i) {
                array[i] = jsonReader.readString();
            }
            return Arrays.asList(array);
        }
        final int entryCnt = jsonReader.startArray();
        Function builder = null;
        Collection list;
        if (instanceType == ArrayList.class) {
            list = ((entryCnt > 0) ? new ArrayList(entryCnt) : new ArrayList());
        }
        else if (instanceType == JSONArray.class) {
            list = ((entryCnt > 0) ? new JSONArray(entryCnt) : new JSONArray());
        }
        else if (instanceType == ObjectReaderImplList.CLASS_UNMODIFIABLE_COLLECTION) {
            list = new ArrayList();
            builder = Collections::unmodifiableCollection;
        }
        else if (instanceType == ObjectReaderImplList.CLASS_UNMODIFIABLE_LIST) {
            list = new ArrayList();
            builder = Collections::unmodifiableList;
        }
        else if (instanceType == ObjectReaderImplList.CLASS_UNMODIFIABLE_SET) {
            list = new LinkedHashSet();
            builder = Collections::unmodifiableSet;
        }
        else if (instanceType == ObjectReaderImplList.CLASS_UNMODIFIABLE_SORTED_SET) {
            list = new TreeSet();
            builder = Collections::unmodifiableSortedSet;
        }
        else if (instanceType == ObjectReaderImplList.CLASS_UNMODIFIABLE_NAVIGABLE_SET) {
            list = new TreeSet();
            builder = Collections::unmodifiableNavigableSet;
        }
        else if (instanceType == ObjectReaderImplList.CLASS_SINGLETON) {
            list = new ArrayList();
            builder = (collection -> Collections.singleton((Object)collection.iterator().next()));
        }
        else if (instanceType == ObjectReaderImplList.CLASS_SINGLETON_LIST) {
            list = new ArrayList();
            builder = (collection -> Collections.singletonList((Object)collection.iterator().next()));
        }
        else if (instanceType != null && instanceType != this.listType) {
            final String typeName2;
            final String typeName = typeName2 = instanceType.getTypeName();
            switch (typeName2) {
                case "com.google.common.collect.ImmutableList": {
                    list = new ArrayList();
                    builder = GuavaSupport.immutableListConverter();
                    break;
                }
                case "com.google.common.collect.ImmutableSet": {
                    list = new ArrayList();
                    builder = GuavaSupport.immutableSetConverter();
                    break;
                }
                case "com.google.common.collect.Lists$TransformingRandomAccessList": {
                    list = new ArrayList();
                    break;
                }
                case "com.google.common.collect.Lists.TransformingSequentialList": {
                    list = new LinkedList();
                    break;
                }
                default: {
                    try {
                        list = instanceType.newInstance();
                    }
                    catch (InstantiationException | IllegalAccessException ex2) {
                        final ReflectiveOperationException ex;
                        final ReflectiveOperationException e = ex;
                        throw new JSONException(jsonReader.info("create instance error " + instanceType), e);
                    }
                    break;
                }
            }
        }
        else {
            list = (Collection)this.createInstance(jsonReader.getContext().getFeatures() | features);
        }
        for (int j = 0; j < entryCnt; ++j) {
            list.add(jsonReader.readString());
        }
        if (builder != null) {
            list = builder.apply(list);
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
        final boolean set = jsonReader.nextIfSet();
        final Collection list = set ? new HashSet() : ((Collection)this.createInstance(jsonReader.getContext().getFeatures() | features));
        final char ch = jsonReader.current();
        if (ch == '[') {
            jsonReader.next();
            while (!jsonReader.nextIfArrayEnd()) {
                list.add(jsonReader.readString());
            }
        }
        else {
            if (ch != '\"' && ch != '\'' && ch != '{') {
                throw new JSONException(jsonReader.info());
            }
            final String str = jsonReader.readString();
            if (!str.isEmpty()) {
                list.add(str);
            }
        }
        jsonReader.nextIfComma();
        return list;
    }
}
