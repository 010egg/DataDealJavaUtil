// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.Supplier;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.util.ReferenceKey;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Field;
import java.util.EnumMap;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.util.JSONObject1O;
import java.lang.reflect.TypeVariable;
import com.alibaba.fastjson2.JSONObject;
import java.lang.reflect.ParameterizedType;
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
import java.lang.reflect.Type;
import java.util.function.Function;

public final class ObjectReaderImplMap implements ObjectReader
{
    static final Function ENUM_MAP_BUILDER;
    static Function UNSAFE_OBJECT_CREATOR;
    static final Class CLASS_SINGLETON_MAP;
    static final Class CLASS_EMPTY_MAP;
    static final Class CLASS_EMPTY_SORTED_MAP;
    static final Class CLASS_EMPTY_NAVIGABLE_MAP;
    static final Class CLASS_UNMODIFIABLE_MAP;
    static final Class CLASS_UNMODIFIABLE_SORTED_MAP;
    static final Class CLASS_UNMODIFIABLE_NAVIGABLE_MAP;
    public static final ObjectReaderImplMap INSTANCE;
    public static final ObjectReaderImplMap INSTANCE_OBJECT;
    final Type fieldType;
    final Class mapType;
    final long mapTypeHash;
    final Class instanceType;
    final long features;
    final Function builder;
    Object mapSingleton;
    volatile boolean instanceError;
    
    public static ObjectReader of(Type fieldType, final Class mapType, final long features) {
        Function builder = null;
        Class instanceType = mapType;
        if ("".equals(instanceType.getSimpleName())) {
            instanceType = mapType.getSuperclass();
            if (fieldType == null) {
                fieldType = mapType.getGenericSuperclass();
            }
        }
        if (mapType == Map.class || mapType == AbstractMap.class || mapType == ObjectReaderImplMap.CLASS_SINGLETON_MAP) {
            instanceType = HashMap.class;
        }
        else if (mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_MAP) {
            instanceType = LinkedHashMap.class;
        }
        else if (mapType == SortedMap.class || mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_SORTED_MAP || mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_NAVIGABLE_MAP) {
            instanceType = TreeMap.class;
        }
        else if (mapType == ConcurrentMap.class) {
            instanceType = ConcurrentHashMap.class;
        }
        else if (mapType == ConcurrentNavigableMap.class) {
            instanceType = ConcurrentSkipListMap.class;
        }
        else {
            final String typeName = mapType.getTypeName();
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
        if (fieldType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldType;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 2 && !"org.springframework.util.LinkedMultiValueMap".equals(instanceType.getName())) {
                final Type keyType = actualTypeArguments[0];
                final Type valueType = actualTypeArguments[1];
                if (keyType == String.class && valueType == String.class && builder == null) {
                    return new ObjectReaderImplMapString(mapType, instanceType, features);
                }
                return new ObjectReaderImplMapTyped(mapType, instanceType, keyType, valueType, 0L, builder);
            }
        }
        if (fieldType == null && features == 0L) {
            if (mapType == HashMap.class && instanceType == HashMap.class) {
                return ObjectReaderImplMap.INSTANCE;
            }
            if (mapType == JSONObject.class && instanceType == JSONObject.class) {
                return ObjectReaderImplMap.INSTANCE_OBJECT;
            }
        }
        final String name;
        final String instanceTypeName = name = instanceType.getName();
        switch (name) {
            case "com.alibaba.fastjson.JSONObject": {
                builder = createObjectSupplier(instanceType);
                instanceType = HashMap.class;
                break;
            }
            case "com.google.common.collect.RegularImmutableMap": {
                builder = GuavaSupport.immutableMapConverter();
                instanceType = HashMap.class;
                break;
            }
            case "com.google.common.collect.SingletonImmutableBiMap": {
                builder = GuavaSupport.singletonBiMapConverter();
                instanceType = HashMap.class;
                break;
            }
            case "com.google.common.collect.ArrayListMultimap": {
                builder = GuavaSupport.createConvertFunction(instanceType);
                instanceType = HashMap.class;
                break;
            }
            case "kotlin.collections.EmptyMap": {
                Object mapSingleton;
                try {
                    final Field field = instanceType.getField("INSTANCE");
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    mapSingleton = field.get(null);
                }
                catch (NoSuchFieldException | IllegalAccessException ex2) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException e = ex;
                    throw new IllegalStateException("Failed to get singleton of " + instanceType, e);
                }
                return new ObjectReaderImplMap(instanceType, features, mapSingleton);
            }
            case "java.util.Collections$EmptyMap": {
                return new ObjectReaderImplMap(instanceType, features, Collections.EMPTY_MAP);
            }
            default: {
                final Type genericSuperclass = instanceType.getGenericSuperclass();
                if (mapType != JSONObject.class && genericSuperclass instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType2 = (ParameterizedType)genericSuperclass;
                    final Type[] arguments = parameterizedType2.getActualTypeArguments();
                    if (arguments.length == 2) {
                        final Type arg0 = arguments[0];
                        final Type arg2 = arguments[1];
                        final boolean typed = !(arg0 instanceof TypeVariable) && !(arg2 instanceof TypeVariable);
                        if (typed) {
                            return new ObjectReaderImplMapTyped(mapType, instanceType, arg0, arg2, 0L, builder);
                        }
                    }
                }
                if (instanceType == JSONObject1O.class) {
                    builder = createObjectSupplier(TypeUtils.CLASS_JSON_OBJECT_1x);
                    instanceType = LinkedHashMap.class;
                    break;
                }
                if (mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_MAP) {
                    builder = Collections::unmodifiableMap;
                    break;
                }
                if (mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_SORTED_MAP) {
                    builder = Collections::unmodifiableSortedMap;
                    break;
                }
                if (mapType == ObjectReaderImplMap.CLASS_UNMODIFIABLE_NAVIGABLE_MAP) {
                    builder = Collections::unmodifiableNavigableMap;
                    break;
                }
                if (mapType == ObjectReaderImplMap.CLASS_SINGLETON_MAP) {
                    final Map.Entry entry;
                    builder = (map -> {
                        entry = (Map.Entry)map.entrySet().iterator().next();
                        return Collections.singletonMap(entry.getKey(), entry.getValue());
                    });
                    break;
                }
                if (mapType == EnumMap.class) {
                    instanceType = LinkedHashMap.class;
                    builder = ObjectReaderImplMap.ENUM_MAP_BUILDER;
                    break;
                }
                break;
            }
        }
        return new ObjectReaderImplMap(fieldType, mapType, instanceType, features, builder);
    }
    
    ObjectReaderImplMap(final Class mapClass, final long features, final Object mapSingleton) {
        this(mapClass, mapClass, mapClass, features, null);
        this.mapSingleton = mapSingleton;
    }
    
    ObjectReaderImplMap(final Type fieldType, final Class mapType, final Class instanceType, final long features, final Function builder) {
        this.fieldType = fieldType;
        this.mapType = mapType;
        this.mapTypeHash = Fnv.hashCode64(TypeUtils.getTypeName(mapType));
        this.instanceType = instanceType;
        this.features = features;
        this.builder = builder;
    }
    
    @Override
    public Class getObjectClass() {
        return this.mapType;
    }
    
    @Override
    public Function getBuildFunction() {
        return this.builder;
    }
    
    @Override
    public Object createInstance(final long features) {
        if (this.instanceType == HashMap.class) {
            return new HashMap();
        }
        if (this.instanceType == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (this.instanceType == JSONObject.class) {
            return new JSONObject();
        }
        if (this.mapSingleton != null) {
            return this.mapSingleton;
        }
        if (this.instanceType == ObjectReaderImplMap.CLASS_EMPTY_SORTED_MAP) {
            return Collections.emptySortedMap();
        }
        if (this.instanceType == ObjectReaderImplMap.CLASS_EMPTY_NAVIGABLE_MAP) {
            return Collections.emptyNavigableMap();
        }
        final String name;
        final String instanceTypeName = name = this.instanceType.getName();
        switch (name) {
            case "com.ali.com.google.common.collect.EmptyImmutableBiMap": {
                final JSONException ex;
                return ((Supplier<Object>)(() -> {
                    try {
                        return JDKUtils.UNSAFE.allocateInstance(this.instanceType);
                    }
                    catch (InstantiationException e2) {
                        new JSONException("create map error : " + this.instanceType);
                        throw ex;
                    }
                })).get();
            }
            case "java.util.ImmutableCollections$Map1": {
                return new HashMap();
            }
            case "java.util.ImmutableCollections$MapN": {
                return new LinkedHashMap();
            }
            default: {
                try {
                    return this.instanceType.newInstance();
                }
                catch (InstantiationException | IllegalAccessException ex3) {
                    final ReflectiveOperationException ex2;
                    final ReflectiveOperationException e = ex2;
                    throw new JSONException("create map error : " + this.instanceType);
                }
                break;
            }
        }
    }
    
    @Override
    public Object createInstance(final Map map, final long features) {
        if (this.mapType.isInstance(map)) {
            return map;
        }
        if (this.mapType == JSONObject.class) {
            return new JSONObject(map);
        }
        final Map instance = (Map)this.createInstance(features);
        instance.putAll(map);
        if (this.builder != null) {
            return this.builder.apply(instance);
        }
        return instance;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName1, final long features) {
        final ObjectReader objectReader = jsonReader.checkAutoType(this.mapType, this.mapTypeHash, this.features | features);
        if (objectReader != null && objectReader != this) {
            return objectReader.readJSONBObject(jsonReader, fieldType, fieldName1, features);
        }
        if (jsonReader.nextIfNull()) {
            return null;
        }
        final boolean emptyObject = false;
        jsonReader.nextIfMatch((byte)(-90));
        final Supplier<Map> objectSupplier = jsonReader.getContext().getObjectSupplier();
        Map map = null;
        if (this.mapType == null && objectSupplier != null) {
            map = objectSupplier.get();
        }
        else if (this.instanceType == HashMap.class) {
            map = new HashMap();
        }
        else if (this.instanceType == LinkedHashMap.class) {
            map = new LinkedHashMap();
        }
        else if (this.instanceType == JSONObject.class) {
            map = new JSONObject();
        }
        else if (this.instanceType == ObjectReaderImplMap.CLASS_EMPTY_MAP) {
            map = Collections.EMPTY_MAP;
        }
        else {
            JSONException error = null;
            if (!this.instanceError) {
                try {
                    map = this.instanceType.newInstance();
                }
                catch (InstantiationException | IllegalAccessException ex3) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException e = ex;
                    this.instanceError = true;
                    error = new JSONException(jsonReader.info("create map error " + this.instanceType));
                }
            }
            if (this.instanceError && Map.class.isAssignableFrom(this.instanceType.getSuperclass())) {
                try {
                    map = (Map)this.instanceType.getSuperclass().newInstance();
                    error = null;
                }
                catch (InstantiationException | IllegalAccessException ex4) {
                    final ReflectiveOperationException ex2;
                    final ReflectiveOperationException e = ex2;
                    if (error == null) {
                        error = new JSONException(jsonReader.info("create map error " + this.instanceType));
                    }
                }
            }
            if (error != null) {
                throw error;
            }
        }
        if (!emptyObject) {
            int i = 0;
            while (true) {
                byte type = jsonReader.getType();
                if (type == -91) {
                    break;
                }
                Object fieldName2;
                if (type >= 73) {
                    fieldName2 = jsonReader.readFieldName();
                }
                else if (jsonReader.nextIfMatch((byte)(-109))) {
                    final String reference = jsonReader.readString();
                    fieldName2 = new ReferenceKey(i);
                    jsonReader.addResolveTask(map, fieldName2, JSONPath.of(reference));
                }
                else {
                    fieldName2 = jsonReader.readAny();
                }
                if (jsonReader.isReference()) {
                    final String reference = jsonReader.readReference();
                    if ("..".equals(reference)) {
                        map.put(fieldName2, map);
                    }
                    else {
                        jsonReader.addResolveTask(map, fieldName2, JSONPath.of(reference));
                        map.put(fieldName2, null);
                    }
                }
                else {
                    type = jsonReader.getType();
                    Object value;
                    if (type >= 73 && type <= 125) {
                        value = jsonReader.readString();
                    }
                    else if (type == -110) {
                        final ObjectReader autoTypeObjectReader = jsonReader.checkAutoType(Object.class, 0L, this.features | features);
                        if (autoTypeObjectReader != null) {
                            value = autoTypeObjectReader.readJSONBObject(jsonReader, null, fieldName2, features);
                        }
                        else {
                            value = jsonReader.readAny();
                        }
                    }
                    else if (type == -79) {
                        value = Boolean.TRUE;
                        jsonReader.next();
                    }
                    else if (type == -80) {
                        value = Boolean.FALSE;
                        jsonReader.next();
                    }
                    else if (type == -109) {
                        final String reference2 = jsonReader.readReference();
                        if ("..".equals(reference2)) {
                            value = map;
                        }
                        else {
                            value = null;
                            jsonReader.addResolveTask(map, fieldName2, JSONPath.of(reference2));
                        }
                    }
                    else if (type == -90) {
                        value = jsonReader.readObject();
                    }
                    else if (type >= -108 && type <= -92) {
                        value = jsonReader.readArray();
                    }
                    else {
                        value = jsonReader.readAny();
                    }
                    map.put(fieldName2, value);
                }
                ++i;
            }
            jsonReader.next();
        }
        if (this.builder != null) {
            return this.builder.apply(map);
        }
        return map;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, features);
        }
        final JSONReader.Context context = jsonReader.getContext();
        final Supplier<Map> objectSupplier = jsonReader.getContext().getObjectSupplier();
        Map object;
        if (objectSupplier != null && (this.mapType == null || this.mapType == JSONObject.class || "com.alibaba.fastjson.JSONObject".equals(this.mapType.getName()))) {
            object = objectSupplier.get();
        }
        else {
            object = (Map)this.createInstance(context.getFeatures() | features);
        }
        jsonReader.read(object, features);
        jsonReader.nextIfComma();
        if (this.builder != null) {
            return this.builder.apply(object);
        }
        return object;
    }
    
    static Function createObjectSupplier(final Class objectClass) {
        if (ObjectReaderImplMap.UNSAFE_OBJECT_CREATOR != null) {
            return ObjectReaderImplMap.UNSAFE_OBJECT_CREATOR;
        }
        return ObjectReaderImplMap.UNSAFE_OBJECT_CREATOR = new ObjectCreatorUF(objectClass);
    }
    
    static {
        ENUM_MAP_BUILDER = (e -> new EnumMap(e));
        CLASS_SINGLETON_MAP = Collections.singletonMap(1, 1).getClass();
        CLASS_EMPTY_MAP = Collections.EMPTY_MAP.getClass();
        CLASS_EMPTY_SORTED_MAP = Collections.emptySortedMap().getClass();
        CLASS_EMPTY_NAVIGABLE_MAP = Collections.emptyNavigableMap().getClass();
        CLASS_UNMODIFIABLE_MAP = Collections.unmodifiableMap(Collections.emptyMap()).getClass();
        CLASS_UNMODIFIABLE_SORTED_MAP = Collections.unmodifiableSortedMap(Collections.emptySortedMap()).getClass();
        CLASS_UNMODIFIABLE_NAVIGABLE_MAP = Collections.unmodifiableNavigableMap(Collections.emptyNavigableMap()).getClass();
        INSTANCE = new ObjectReaderImplMap(null, HashMap.class, HashMap.class, 0L, null);
        INSTANCE_OBJECT = new ObjectReaderImplMap(null, JSONObject.class, JSONObject.class, 0L, null);
    }
    
    static class ObjectCreatorUF implements Function
    {
        final Class objectClass;
        final Field map;
        final long mapOffset;
        
        ObjectCreatorUF(final Class objectClass) {
            this.objectClass = objectClass;
            try {
                this.map = objectClass.getDeclaredField("map");
            }
            catch (NoSuchFieldException e) {
                throw new JSONException("field map not found", e);
            }
            this.mapOffset = JDKUtils.UNSAFE.objectFieldOffset(this.map);
        }
        
        @Override
        public Object apply(Object map) {
            if (map == null) {
                map = new HashMap();
            }
            Object object;
            try {
                object = JDKUtils.UNSAFE.allocateInstance(this.objectClass);
                JDKUtils.UNSAFE.putObject(object, this.mapOffset, map);
            }
            catch (InstantiationException e) {
                throw new JSONException("create " + this.objectClass.getName() + " error", e);
            }
            return object;
        }
    }
}
