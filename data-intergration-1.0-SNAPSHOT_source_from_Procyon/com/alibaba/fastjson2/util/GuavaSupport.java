// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONB;
import java.util.Map;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Collection;
import java.util.List;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GuavaSupport
{
    static Class CLASS_IMMUTABLE_MAP;
    static Class CLASS_IMMUTABLE_LIST;
    static Class CLASS_IMMUTABLE_SET;
    static Class CLASS_ARRAYLIST_MULTI_MAP;
    static Supplier FUNC_IMMUTABLE_MAP_OF_0;
    static BiFunction FUNC_IMMUTABLE_MAP_OF_1;
    static Function FUNC_IMMUTABLE_MAP_COPY_OF;
    static Supplier FUNC_IMMUTABLE_LIST_OF_0;
    static Function FUNC_IMMUTABLE_LIST_OF_1;
    static Function FUNC_IMMUTABLE_LIST_COPY_OF;
    static Supplier FUNC_IMMUTABLE_SET_OF_0;
    static Function FUNC_IMMUTABLE_SET_OF_1;
    static Function FUNC_IMMUTABLE_SET_COPY_OF;
    static Supplier FUNC_ARRAYLIST_MULTI_MAP_CREATE;
    static Method METHOD_ARRAYLIST_MULTI_MAP_PUT_ALL;
    static volatile boolean METHOD_ARRAYLIST_MULTI_MAP_ERROR;
    static BiFunction FUNC_SINGLETON_IMMUTABLE_BIMAP;
    
    public static Function immutableListConverter() {
        return new ImmutableListConvertFunction();
    }
    
    public static Function immutableSetConverter() {
        return new ImmutableSetConvertFunction();
    }
    
    public static Function immutableMapConverter() {
        return new ImmutableSingletonMapConvertFunction();
    }
    
    public static Function singletonBiMapConverter() {
        return new SingletonImmutableBiMapConvertFunction();
    }
    
    public static ObjectWriter createAsMapWriter(final Class objectClass) {
        return new AsMapWriter(objectClass);
    }
    
    public static Function createConvertFunction(final Class objectClass) {
        final String instanceTypeName = objectClass.getName();
        if (instanceTypeName.equals("com.google.common.collect.ArrayListMultimap")) {
            if (GuavaSupport.CLASS_ARRAYLIST_MULTI_MAP == null) {
                GuavaSupport.CLASS_ARRAYLIST_MULTI_MAP = objectClass;
            }
            if (!GuavaSupport.METHOD_ARRAYLIST_MULTI_MAP_ERROR && GuavaSupport.FUNC_ARRAYLIST_MULTI_MAP_CREATE == null) {
                try {
                    final Method method = GuavaSupport.CLASS_ARRAYLIST_MULTI_MAP.getMethod("create", (Class[])new Class[0]);
                    GuavaSupport.FUNC_ARRAYLIST_MULTI_MAP_CREATE = LambdaMiscCodec.createSupplier(method);
                }
                catch (Throwable ignored) {
                    GuavaSupport.METHOD_ARRAYLIST_MULTI_MAP_ERROR = true;
                }
            }
            if (!GuavaSupport.METHOD_ARRAYLIST_MULTI_MAP_ERROR && GuavaSupport.METHOD_ARRAYLIST_MULTI_MAP_PUT_ALL == null) {
                try {
                    GuavaSupport.METHOD_ARRAYLIST_MULTI_MAP_PUT_ALL = GuavaSupport.CLASS_ARRAYLIST_MULTI_MAP.getMethod("putAll", Object.class, Iterable.class);
                }
                catch (Throwable ignored) {
                    GuavaSupport.METHOD_ARRAYLIST_MULTI_MAP_ERROR = true;
                }
            }
            if (GuavaSupport.FUNC_ARRAYLIST_MULTI_MAP_CREATE != null && GuavaSupport.METHOD_ARRAYLIST_MULTI_MAP_PUT_ALL != null) {
                return new ArrayListMultimapConvertFunction(GuavaSupport.FUNC_ARRAYLIST_MULTI_MAP_CREATE, GuavaSupport.METHOD_ARRAYLIST_MULTI_MAP_PUT_ALL);
            }
        }
        throw new JSONException("create map error : " + objectClass);
    }
    
    static class ImmutableSetConvertFunction implements Function
    {
        @Override
        public Object apply(final Object object) {
            if (GuavaSupport.CLASS_IMMUTABLE_SET == null) {
                GuavaSupport.CLASS_IMMUTABLE_SET = TypeUtils.loadClass("com.google.common.collect.ImmutableSet");
            }
            if (GuavaSupport.CLASS_IMMUTABLE_SET == null) {
                throw new JSONException("class not found : com.google.common.collect.ImmutableSet");
            }
            final List list = (List)object;
            if (list.isEmpty()) {
                if (GuavaSupport.FUNC_IMMUTABLE_SET_OF_0 == null) {
                    try {
                        final Method method = GuavaSupport.CLASS_IMMUTABLE_SET.getMethod("of", (Class[])new Class[0]);
                        GuavaSupport.FUNC_IMMUTABLE_SET_OF_0 = LambdaMiscCodec.createSupplier(method);
                    }
                    catch (NoSuchMethodException e) {
                        throw new JSONException("method not found : com.google.common.collect.ImmutableSet.of", e);
                    }
                }
                return GuavaSupport.FUNC_IMMUTABLE_SET_OF_0.get();
            }
            if (list.size() == 1) {
                if (GuavaSupport.FUNC_IMMUTABLE_SET_OF_1 == null) {
                    try {
                        final Method method = GuavaSupport.CLASS_IMMUTABLE_SET.getMethod("of", Object.class);
                        GuavaSupport.FUNC_IMMUTABLE_SET_OF_1 = LambdaMiscCodec.createFunction(method);
                    }
                    catch (NoSuchMethodException e) {
                        throw new JSONException("method not found : com.google.common.collect.ImmutableSet.of", e);
                    }
                }
                final Object first = list.get(0);
                return GuavaSupport.FUNC_IMMUTABLE_SET_OF_1.apply(first);
            }
            if (GuavaSupport.FUNC_IMMUTABLE_SET_COPY_OF == null) {
                try {
                    final Method method = GuavaSupport.CLASS_IMMUTABLE_SET.getMethod("copyOf", Collection.class);
                    GuavaSupport.FUNC_IMMUTABLE_SET_COPY_OF = LambdaMiscCodec.createFunction(method);
                }
                catch (NoSuchMethodException e) {
                    throw new JSONException("method not found : com.google.common.collect.ImmutableSet.copyOf", e);
                }
            }
            return GuavaSupport.FUNC_IMMUTABLE_SET_COPY_OF.apply(list);
        }
    }
    
    static class ImmutableListConvertFunction implements Function
    {
        @Override
        public Object apply(final Object object) {
            if (GuavaSupport.CLASS_IMMUTABLE_LIST == null) {
                GuavaSupport.CLASS_IMMUTABLE_LIST = TypeUtils.loadClass("com.google.common.collect.ImmutableList");
            }
            if (GuavaSupport.CLASS_IMMUTABLE_LIST == null) {
                throw new JSONException("class not found : com.google.common.collect.ImmutableList");
            }
            final List list = (List)object;
            if (list.isEmpty()) {
                if (GuavaSupport.FUNC_IMMUTABLE_LIST_OF_0 == null) {
                    try {
                        final Method method = GuavaSupport.CLASS_IMMUTABLE_LIST.getMethod("of", (Class[])new Class[0]);
                        GuavaSupport.FUNC_IMMUTABLE_LIST_OF_0 = LambdaMiscCodec.createSupplier(method);
                    }
                    catch (NoSuchMethodException e) {
                        throw new JSONException("method not found : com.google.common.collect.ImmutableList.of", e);
                    }
                }
                return GuavaSupport.FUNC_IMMUTABLE_LIST_OF_0.get();
            }
            if (list.size() == 1) {
                if (GuavaSupport.FUNC_IMMUTABLE_LIST_OF_1 == null) {
                    try {
                        final Method method = GuavaSupport.CLASS_IMMUTABLE_LIST.getMethod("of", Object.class);
                        GuavaSupport.FUNC_IMMUTABLE_LIST_OF_1 = LambdaMiscCodec.createFunction(method);
                    }
                    catch (NoSuchMethodException e) {
                        throw new JSONException("method not found : com.google.common.collect.ImmutableList.of", e);
                    }
                }
                final Object first = list.get(0);
                return GuavaSupport.FUNC_IMMUTABLE_LIST_OF_1.apply(first);
            }
            if (GuavaSupport.FUNC_IMMUTABLE_LIST_COPY_OF == null) {
                try {
                    final Method method = GuavaSupport.CLASS_IMMUTABLE_LIST.getMethod("copyOf", Collection.class);
                    GuavaSupport.FUNC_IMMUTABLE_LIST_COPY_OF = LambdaMiscCodec.createFunction(method);
                }
                catch (NoSuchMethodException e) {
                    throw new JSONException("method not found : com.google.common.collect.ImmutableList.copyOf", e);
                }
            }
            return GuavaSupport.FUNC_IMMUTABLE_LIST_COPY_OF.apply(list);
        }
    }
    
    static class AsMapWriter implements ObjectWriter
    {
        final Class objectClass;
        final String typeName;
        final long typeNameHash;
        final Function asMap;
        protected byte[] typeNameJSONB;
        
        public AsMapWriter(final Class objectClass) {
            this.objectClass = objectClass;
            this.typeName = TypeUtils.getTypeName(objectClass);
            this.typeNameHash = Fnv.hashCode64(this.typeName);
            try {
                final Method method = objectClass.getMethod("asMap", (Class[])new Class[0]);
                this.asMap = LambdaMiscCodec.createFunction(method);
            }
            catch (NoSuchMethodException e) {
                throw new JSONException("create Guava AsMapWriter error", e);
            }
        }
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            final Map map = this.asMap.apply(object);
            jsonWriter.write(map);
        }
        
        @Override
        public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
            if (jsonWriter.isWriteTypeInfo(object, fieldType, features)) {
                if (this.typeNameJSONB == null) {
                    this.typeNameJSONB = JSONB.toBytes(this.typeName);
                }
                jsonWriter.writeTypeName(this.typeNameJSONB, this.typeNameHash);
            }
            final Map map = this.asMap.apply(object);
            jsonWriter.write(map);
        }
    }
    
    static class ArrayListMultimapConvertFunction implements Function
    {
        final Supplier method;
        final Method putAllMethod;
        
        public ArrayListMultimapConvertFunction(final Supplier method, final Method putAllMethod) {
            this.method = method;
            this.putAllMethod = putAllMethod;
        }
        
        @Override
        public Object apply(final Object o) {
            final Map map = (Map)o;
            final Object multiMap = this.method.get();
            for (final Map.Entry entry : map.entrySet()) {
                final Object key = entry.getKey();
                final Iterable item = entry.getValue();
                try {
                    this.putAllMethod.invoke(multiMap, key, item);
                }
                catch (Throwable e) {
                    throw new JSONException("putAll ArrayListMultimap error", e);
                }
            }
            return multiMap;
        }
    }
    
    static class SingletonImmutableBiMapConvertFunction implements Function
    {
        @Override
        public Object apply(final Object object) {
            if (GuavaSupport.FUNC_SINGLETON_IMMUTABLE_BIMAP == null) {
                try {
                    final Constructor constructor = TypeUtils.loadClass("com.google.common.collect.SingletonImmutableBiMap").getDeclaredConstructor(Object.class, Object.class);
                    GuavaSupport.FUNC_SINGLETON_IMMUTABLE_BIMAP = LambdaMiscCodec.createBiFunction(constructor);
                }
                catch (NoSuchMethodException | SecurityException ex2) {
                    final Exception ex;
                    final Exception e = ex;
                    throw new JSONException("method not found : com.google.common.collect.SingletonImmutableBiMap(Object, Object)", e);
                }
            }
            final Map map = (Map)object;
            final Map.Entry entry = (Map.Entry)map.entrySet().iterator().next();
            return GuavaSupport.FUNC_SINGLETON_IMMUTABLE_BIMAP.apply(entry.getKey(), entry.getValue());
        }
    }
    
    static class ImmutableSingletonMapConvertFunction implements Function
    {
        @Override
        public Object apply(final Object object) {
            if (GuavaSupport.CLASS_IMMUTABLE_MAP == null) {
                GuavaSupport.CLASS_IMMUTABLE_MAP = TypeUtils.loadClass("com.google.common.collect.ImmutableMap");
            }
            if (GuavaSupport.CLASS_IMMUTABLE_MAP == null) {
                throw new JSONException("class not found : com.google.common.collect.ImmutableMap");
            }
            final Map map = (Map)object;
            if (map.size() == 0) {
                if (GuavaSupport.FUNC_IMMUTABLE_MAP_OF_0 == null) {
                    try {
                        final Method method = GuavaSupport.CLASS_IMMUTABLE_MAP.getMethod("of", (Class[])new Class[0]);
                        GuavaSupport.FUNC_IMMUTABLE_MAP_OF_0 = LambdaMiscCodec.createSupplier(method);
                    }
                    catch (NoSuchMethodException e) {
                        throw new JSONException("method not found : com.google.common.collect.ImmutableMap.of", e);
                    }
                }
                return GuavaSupport.FUNC_IMMUTABLE_MAP_OF_0.get();
            }
            if (map.size() == 1) {
                if (GuavaSupport.FUNC_IMMUTABLE_MAP_OF_1 == null) {
                    try {
                        final Method method = GuavaSupport.CLASS_IMMUTABLE_MAP.getMethod("of", Object.class, Object.class);
                        method.setAccessible(true);
                        GuavaSupport.FUNC_IMMUTABLE_MAP_OF_1 = LambdaMiscCodec.createBiFunction(method);
                    }
                    catch (NoSuchMethodException e) {
                        throw new JSONException("method not found : com.google.common.collect.ImmutableBiMap.of", e);
                    }
                }
                final Map.Entry entry = (Map.Entry)map.entrySet().iterator().next();
                return GuavaSupport.FUNC_IMMUTABLE_MAP_OF_1.apply(entry.getKey(), entry.getValue());
            }
            if (GuavaSupport.FUNC_IMMUTABLE_MAP_COPY_OF == null) {
                try {
                    final Method method = GuavaSupport.CLASS_IMMUTABLE_MAP.getMethod("copyOf", Map.class);
                    GuavaSupport.FUNC_IMMUTABLE_MAP_COPY_OF = LambdaMiscCodec.createFunction(method);
                }
                catch (NoSuchMethodException e) {
                    throw new JSONException("method not found : com.google.common.collect.ImmutableBiMap.copyOf", e);
                }
            }
            return GuavaSupport.FUNC_IMMUTABLE_MAP_COPY_OF.apply(map);
        }
    }
}
