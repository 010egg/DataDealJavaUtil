// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.Supplier;
import com.alibaba.fastjson2.JSONArray;
import java.util.Collection;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import java.util.Collections;
import com.alibaba.fastjson2.util.GuavaSupport;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.AbstractMap;
import java.util.Map;
import com.alibaba.fastjson2.util.MapMultiValueType;
import java.util.function.Function;

public class ObjectReaderImplMapMultiValueType implements ObjectReader
{
    final Class mapType;
    final Class instanceType;
    final Function builder;
    final MapMultiValueType multiValueType;
    
    public ObjectReaderImplMapMultiValueType(final MapMultiValueType multiValueType) {
        this.multiValueType = multiValueType;
        this.mapType = multiValueType.getMapType();
        Class instanceType = this.mapType;
        Function builder = null;
        if (this.mapType == Map.class || this.mapType == AbstractMap.class || this.mapType == ObjectReaderImplMap.CLASS_SINGLETON_MAP) {
            instanceType = HashMap.class;
        }
        else if (this.mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_MAP) {
            instanceType = LinkedHashMap.class;
        }
        else if (this.mapType == SortedMap.class || this.mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_SORTED_MAP || this.mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_NAVIGABLE_MAP) {
            instanceType = TreeMap.class;
        }
        else if (this.mapType == ConcurrentMap.class) {
            instanceType = ConcurrentHashMap.class;
        }
        else if (this.mapType == ConcurrentNavigableMap.class) {
            instanceType = ConcurrentSkipListMap.class;
        }
        else {
            final String typeName = this.mapType.getTypeName();
            switch (typeName) {
                case "com.google.common.collect.ImmutableMap":
                case "com.google.common.collect.RegularImmutableMap": {
                    instanceType = HashMap.class;
                    builder = GuavaSupport.immutableMapConverter();
                    break;
                }
                case "com.google.common.collect.SingletonImmutableBiMap": {
                    instanceType = HashMap.class;
                    builder = GuavaSupport.singletonBiMapConverter();
                    break;
                }
                case "java.util.Collections$SynchronizedMap": {
                    instanceType = HashMap.class;
                    builder = Collections::synchronizedMap;
                    break;
                }
                case "java.util.Collections$SynchronizedNavigableMap": {
                    instanceType = TreeMap.class;
                    builder = Collections::synchronizedNavigableMap;
                    break;
                }
                case "java.util.Collections$SynchronizedSortedMap": {
                    instanceType = TreeMap.class;
                    builder = Collections::synchronizedSortedMap;
                    break;
                }
            }
        }
        this.instanceType = instanceType;
        this.builder = builder;
    }
    
    @Override
    public Object createInstance(final long features) {
        if (this.instanceType != null && !this.instanceType.isInterface()) {
            try {
                return this.instanceType.newInstance();
            }
            catch (Exception e) {
                throw new JSONException("create map error", e);
            }
        }
        return new HashMap();
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (!jsonReader.nextIfObjectStart()) {
            if (jsonReader.nextIfNullOrEmptyString()) {
                return null;
            }
            throw new JSONException(jsonReader.info("expect '{', but '" + jsonReader.current() + "'"));
        }
        else {
            final JSONReader.Context context = jsonReader.getContext();
            final long contextFeatures = context.getFeatures() | features;
            Map innerMap = null;
            Map object;
            if (this.instanceType == HashMap.class) {
                final Supplier<Map> objectSupplier = context.getObjectSupplier();
                if (this.mapType == Map.class && objectSupplier != null) {
                    object = objectSupplier.get();
                    innerMap = TypeUtils.getInnerMap(object);
                }
                else {
                    object = new HashMap();
                }
            }
            else if (this.instanceType == JSONObject.class) {
                object = new JSONObject();
            }
            else {
                object = (Map)this.createInstance(contextFeatures);
            }
            Type valueType = null;
            int i = 0;
            while (!jsonReader.nextIfObjectEnd() && !jsonReader.isEnd()) {
                String name;
                if (jsonReader.nextIfNull()) {
                    if (!jsonReader.nextIfMatch(':')) {
                        throw new JSONException(jsonReader.info("illegal json"));
                    }
                    name = null;
                }
                else {
                    name = jsonReader.readFieldName();
                    valueType = this.multiValueType.getType(name);
                }
                Object value;
                if (valueType == null) {
                    value = jsonReader.readAny();
                }
                else {
                    final ObjectReader valueObjectReader = jsonReader.getObjectReader(valueType);
                    value = valueObjectReader.readObject(jsonReader, valueType, fieldName, 0L);
                }
                Object origin;
                if (innerMap != null) {
                    origin = innerMap.put(name, value);
                }
                else {
                    origin = object.put(name, value);
                }
                if (origin != null && (contextFeatures & JSONReader.Feature.DuplicateKeyValueAsArray.mask) != 0x0L) {
                    if (origin instanceof Collection) {
                        ((Collection)origin).add(value);
                        object.put(name, origin);
                    }
                    else {
                        final JSONArray array = JSONArray.of(origin, value);
                        object.put(name, array);
                    }
                }
                ++i;
            }
            jsonReader.nextIfMatch(',');
            if (this.builder != null) {
                return this.builder.apply(object);
            }
            return object;
        }
    }
}
