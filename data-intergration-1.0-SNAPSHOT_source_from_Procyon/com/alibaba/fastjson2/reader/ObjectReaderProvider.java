// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import com.alibaba.fastjson2.function.FieldBiConsumer;
import com.alibaba.fastjson2.function.FieldConsumer;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import java.util.concurrent.atomic.AtomicReference;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import java.util.function.Supplier;
import java.lang.annotation.Annotation;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.codec.BeanInfo;
import java.util.Date;
import com.alibaba.fastjson2.util.JDKUtils;
import java.util.HashMap;
import java.util.ArrayList;
import com.alibaba.fastjson2.JSONFactory;
import java.lang.reflect.ParameterizedType;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.Iterator;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.JSONException;
import java.util.Arrays;
import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.modules.ObjectReaderModule;
import java.util.List;
import java.util.function.Function;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.modules.ObjectCodecProvider;

public class ObjectReaderProvider implements ObjectCodecProvider
{
    static final ClassLoader FASTJSON2_CLASS_LOADER;
    public static final boolean SAFE_MODE;
    static final String[] DENYS;
    static final String[] AUTO_TYPE_ACCEPT_LIST;
    static JSONReader.AutoTypeBeforeHandler DEFAULT_AUTO_TYPE_BEFORE_HANDLER;
    static Consumer<Class> DEFAULT_AUTO_TYPE_HANDLER;
    static boolean DEFAULT_AUTO_TYPE_HANDLER_INIT_ERROR;
    static ObjectReaderCachePair readerCache;
    final ConcurrentMap<Type, ObjectReader> cache;
    final ConcurrentMap<Type, ObjectReader> cacheFieldBased;
    final ConcurrentMap<Integer, ConcurrentHashMap<Long, ObjectReader>> tclHashCaches;
    final ConcurrentMap<Long, ObjectReader> hashCache;
    final ConcurrentMap<Class, Class> mixInCache;
    final LRUAutoTypeCache autoTypeList;
    private final ConcurrentMap<Type, Map<Type, Function>> typeConverts;
    final ObjectReaderCreator creator;
    final List<ObjectReaderModule> modules;
    private long[] acceptHashCodes;
    private JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler;
    private Consumer<Class> autoTypeHandler;
    
    public void registerIfAbsent(final long hashCode, final ObjectReader objectReader) {
        final ClassLoader tcl = Thread.currentThread().getContextClassLoader();
        if (tcl != null && tcl != JSON.class.getClassLoader()) {
            final int tclHash = System.identityHashCode(tcl);
            ConcurrentHashMap<Long, ObjectReader> tclHashCache = this.tclHashCaches.get(tclHash);
            if (tclHashCache == null) {
                this.tclHashCaches.putIfAbsent(tclHash, new ConcurrentHashMap<Long, ObjectReader>());
                tclHashCache = this.tclHashCaches.get(tclHash);
            }
            tclHashCache.putIfAbsent(hashCode, objectReader);
        }
        this.hashCache.putIfAbsent(hashCode, objectReader);
    }
    
    public void addAutoTypeAccept(final String name) {
        if (name != null && name.length() != 0) {
            final long hash = Fnv.hashCode64(name);
            if (Arrays.binarySearch(this.acceptHashCodes, hash) < 0) {
                final long[] hashCodes = new long[this.acceptHashCodes.length + 1];
                hashCodes[hashCodes.length - 1] = hash;
                System.arraycopy(this.acceptHashCodes, 0, hashCodes, 0, this.acceptHashCodes.length);
                Arrays.sort(hashCodes);
                this.acceptHashCodes = hashCodes;
            }
        }
    }
    
    @Deprecated
    public void addAutoTypeDeny(final String name) {
    }
    
    public Consumer<Class> getAutoTypeHandler() {
        return this.autoTypeHandler;
    }
    
    public void setAutoTypeHandler(final Consumer<Class> autoTypeHandler) {
        this.autoTypeHandler = autoTypeHandler;
    }
    
    @Override
    public Class getMixIn(final Class target) {
        return this.mixInCache.get(target);
    }
    
    public void cleanupMixIn() {
        this.mixInCache.clear();
    }
    
    public void mixIn(final Class target, final Class mixinSource) {
        if (mixinSource == null) {
            this.mixInCache.remove(target);
        }
        else {
            this.mixInCache.put(target, mixinSource);
        }
        this.cache.remove(target);
        this.cacheFieldBased.remove(target);
    }
    
    public void registerSeeAlsoSubType(final Class subTypeClass) {
        this.registerSeeAlsoSubType(subTypeClass, null);
    }
    
    public void registerSeeAlsoSubType(final Class subTypeClass, final String subTypeClassName) {
        final Class superClass = subTypeClass.getSuperclass();
        if (superClass == null) {
            throw new JSONException("superclass is null");
        }
        final ObjectReader objectReader = this.getObjectReader(superClass);
        if (objectReader instanceof ObjectReaderSeeAlso) {
            final ObjectReaderSeeAlso readerSeeAlso = (ObjectReaderSeeAlso)objectReader;
            final ObjectReaderSeeAlso readerSeeAlsoNew = readerSeeAlso.addSubType(subTypeClass, subTypeClassName);
            if (readerSeeAlsoNew != readerSeeAlso) {
                if (this.cache.containsKey(superClass)) {
                    this.cache.put(superClass, readerSeeAlsoNew);
                }
                else {
                    this.cacheFieldBased.put(subTypeClass, readerSeeAlsoNew);
                }
            }
        }
    }
    
    public ObjectReader register(final Type type, final ObjectReader objectReader, final boolean fieldBased) {
        final ConcurrentMap<Type, ObjectReader> cache = fieldBased ? this.cacheFieldBased : this.cache;
        if (objectReader == null) {
            return cache.remove(type);
        }
        return cache.put(type, objectReader);
    }
    
    public ObjectReader register(final Type type, final ObjectReader objectReader) {
        return this.register(type, objectReader, false);
    }
    
    public ObjectReader registerIfAbsent(final Type type, final ObjectReader objectReader) {
        return this.registerIfAbsent(type, objectReader, false);
    }
    
    public ObjectReader registerIfAbsent(final Type type, final ObjectReader objectReader, final boolean fieldBased) {
        final ConcurrentMap<Type, ObjectReader> cache = fieldBased ? this.cacheFieldBased : this.cache;
        return cache.putIfAbsent(type, objectReader);
    }
    
    public ObjectReader unregisterObjectReader(final Type type) {
        return this.unregisterObjectReader(type, false);
    }
    
    public ObjectReader unregisterObjectReader(final Type type, final boolean fieldBased) {
        final ConcurrentMap<Type, ObjectReader> cache = fieldBased ? this.cacheFieldBased : this.cache;
        return cache.remove(type);
    }
    
    public boolean unregisterObjectReader(final Type type, final ObjectReader reader) {
        return this.unregisterObjectReader(type, reader, false);
    }
    
    public boolean unregisterObjectReader(final Type type, final ObjectReader reader, final boolean fieldBased) {
        final ConcurrentMap<Type, ObjectReader> cache = fieldBased ? this.cacheFieldBased : this.cache;
        return cache.remove(type, reader);
    }
    
    public boolean register(final ObjectReaderModule module) {
        for (int i = this.modules.size() - 1; i >= 0; --i) {
            if (this.modules.get(i) == module) {
                return false;
            }
        }
        module.init(this);
        this.modules.add(0, module);
        return true;
    }
    
    public boolean unregister(final ObjectReaderModule module) {
        return this.modules.remove(module);
    }
    
    public void cleanup(final Class objectClass) {
        this.mixInCache.remove(objectClass);
        this.cache.remove(objectClass);
        this.cacheFieldBased.remove(objectClass);
        for (final ConcurrentHashMap<Long, ObjectReader> tlc : this.tclHashCaches.values()) {
            final Iterator<Map.Entry<Long, ObjectReader>> it = tlc.entrySet().iterator();
            while (it.hasNext()) {
                final Map.Entry<Long, ObjectReader> entry = it.next();
                final ObjectReader reader = entry.getValue();
                if (reader.getObjectClass() == objectClass) {
                    it.remove();
                }
            }
        }
        BeanUtils.cleanupCache(objectClass);
    }
    
    static boolean match(final Type objectType, final ObjectReader objectReader, final ClassLoader classLoader) {
        final Class<?> objectClass = TypeUtils.getClass(objectType);
        if (objectClass != null && objectClass.getClassLoader() == classLoader) {
            return true;
        }
        if (objectType instanceof ParameterizedType) {
            final ParameterizedType paramType = (ParameterizedType)objectType;
            final Type rawType = paramType.getRawType();
            if (match(rawType, objectReader, classLoader)) {
                return true;
            }
            for (final Type argType : paramType.getActualTypeArguments()) {
                if (match(argType, objectReader, classLoader)) {
                    return true;
                }
            }
        }
        if (objectReader instanceof ObjectReaderImplMapTyped) {
            final ObjectReaderImplMapTyped mapTyped = (ObjectReaderImplMapTyped)objectReader;
            final Class valueClass = mapTyped.valueClass;
            if (valueClass != null && valueClass.getClassLoader() == classLoader) {
                return true;
            }
            final Class keyClass = TypeUtils.getClass(mapTyped.keyType);
            return keyClass != null && keyClass.getClassLoader() == classLoader;
        }
        else {
            if (objectReader instanceof ObjectReaderImplList) {
                final ObjectReaderImplList list = (ObjectReaderImplList)objectReader;
                return list.itemClass != null && list.itemClass.getClassLoader() == classLoader;
            }
            if (objectReader instanceof ObjectReaderImplOptional) {
                final Class itemClass = ((ObjectReaderImplOptional)objectReader).itemClass;
                return itemClass != null && itemClass.getClassLoader() == classLoader;
            }
            if (objectReader instanceof ObjectReaderAdapter) {
                final FieldReader[] fieldReaders2;
                final FieldReader[] fieldReaders = fieldReaders2 = ((ObjectReaderAdapter)objectReader).fieldReaders;
                for (final FieldReader fieldReader : fieldReaders2) {
                    if (fieldReader.fieldClass != null && fieldReader.fieldClass.getClassLoader() == classLoader) {
                        return true;
                    }
                    final Type fieldType = fieldReader.fieldType;
                    if (fieldType instanceof ParameterizedType && match(fieldType, null, classLoader)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    public void cleanup(final ClassLoader classLoader) {
        this.mixInCache.entrySet().removeIf(entry -> entry.getKey().getClassLoader() == classLoader);
        this.cache.entrySet().removeIf(entry -> match(entry.getKey(), (ObjectReader)entry.getValue(), classLoader));
        this.cacheFieldBased.entrySet().removeIf(entry -> match(entry.getKey(), (ObjectReader)entry.getValue(), classLoader));
        final int tclHash = System.identityHashCode(classLoader);
        this.tclHashCaches.remove(tclHash);
        BeanUtils.cleanupCache(classLoader);
    }
    
    public ObjectReaderCreator getCreator() {
        final ObjectReaderCreator contextCreator = JSONFactory.getContextReaderCreator();
        if (contextCreator != null) {
            return contextCreator;
        }
        return this.creator;
    }
    
    public ObjectReaderProvider() {
        this.cache = new ConcurrentHashMap<Type, ObjectReader>();
        this.cacheFieldBased = new ConcurrentHashMap<Type, ObjectReader>();
        this.tclHashCaches = new ConcurrentHashMap<Integer, ConcurrentHashMap<Long, ObjectReader>>();
        this.hashCache = new ConcurrentHashMap<Long, ObjectReader>();
        this.mixInCache = new ConcurrentHashMap<Class, Class>();
        this.autoTypeList = new LRUAutoTypeCache(1024);
        this.typeConverts = new ConcurrentHashMap<Type, Map<Type, Function>>();
        this.modules = new ArrayList<ObjectReaderModule>();
        this.autoTypeBeforeHandler = ObjectReaderProvider.DEFAULT_AUTO_TYPE_BEFORE_HANDLER;
        this.autoTypeHandler = ObjectReaderProvider.DEFAULT_AUTO_TYPE_HANDLER;
        long[] hashCodes;
        if (ObjectReaderProvider.AUTO_TYPE_ACCEPT_LIST == null) {
            hashCodes = new long[] { 0L };
        }
        else {
            hashCodes = new long[ObjectReaderProvider.AUTO_TYPE_ACCEPT_LIST.length + 1];
            for (int i = 0; i < ObjectReaderProvider.AUTO_TYPE_ACCEPT_LIST.length; ++i) {
                hashCodes[i] = Fnv.hashCode64(ObjectReaderProvider.AUTO_TYPE_ACCEPT_LIST[i]);
            }
        }
        hashCodes[hashCodes.length - 1] = -6293031534589903644L;
        Arrays.sort(hashCodes);
        this.acceptHashCodes = hashCodes;
        this.hashCache.put(ObjectArrayReader.TYPE_HASH_CODE, ObjectArrayReader.INSTANCE);
        final long STRING_CLASS_NAME_HASH = -4834614249632438472L;
        this.hashCache.put(-4834614249632438472L, ObjectReaderImplString.INSTANCE);
        this.hashCache.put(Fnv.hashCode64(TypeUtils.getTypeName(HashMap.class)), ObjectReaderImplMap.INSTANCE);
        ObjectReaderCreator creator = null;
        final String creator2 = JSONFactory.CREATOR;
        switch (creator2) {
            case "reflect":
            case "lambda": {
                creator = ObjectReaderCreator.INSTANCE;
                break;
            }
            default: {
                try {
                    if (!JDKUtils.ANDROID && !JDKUtils.GRAAL) {
                        creator = ObjectReaderCreatorASM.INSTANCE;
                    }
                }
                catch (Throwable t) {}
                if (creator == null) {
                    creator = ObjectReaderCreator.INSTANCE;
                    break;
                }
                break;
            }
        }
        this.creator = creator;
        this.modules.add(new ObjectReaderBaseModule(this));
        this.init();
    }
    
    public ObjectReaderProvider(final ObjectReaderCreator creator) {
        this.cache = new ConcurrentHashMap<Type, ObjectReader>();
        this.cacheFieldBased = new ConcurrentHashMap<Type, ObjectReader>();
        this.tclHashCaches = new ConcurrentHashMap<Integer, ConcurrentHashMap<Long, ObjectReader>>();
        this.hashCache = new ConcurrentHashMap<Long, ObjectReader>();
        this.mixInCache = new ConcurrentHashMap<Class, Class>();
        this.autoTypeList = new LRUAutoTypeCache(1024);
        this.typeConverts = new ConcurrentHashMap<Type, Map<Type, Function>>();
        this.modules = new ArrayList<ObjectReaderModule>();
        this.autoTypeBeforeHandler = ObjectReaderProvider.DEFAULT_AUTO_TYPE_BEFORE_HANDLER;
        this.autoTypeHandler = ObjectReaderProvider.DEFAULT_AUTO_TYPE_HANDLER;
        long[] hashCodes;
        if (ObjectReaderProvider.AUTO_TYPE_ACCEPT_LIST == null) {
            hashCodes = new long[] { 0L };
        }
        else {
            hashCodes = new long[ObjectReaderProvider.AUTO_TYPE_ACCEPT_LIST.length + 1];
            for (int i = 0; i < ObjectReaderProvider.AUTO_TYPE_ACCEPT_LIST.length; ++i) {
                hashCodes[i] = Fnv.hashCode64(ObjectReaderProvider.AUTO_TYPE_ACCEPT_LIST[i]);
            }
        }
        hashCodes[hashCodes.length - 1] = -6293031534589903644L;
        Arrays.sort(hashCodes);
        this.acceptHashCodes = hashCodes;
        this.hashCache.put(ObjectArrayReader.TYPE_HASH_CODE, ObjectArrayReader.INSTANCE);
        final long STRING_CLASS_NAME_HASH = -4834614249632438472L;
        this.hashCache.put(-4834614249632438472L, ObjectReaderImplString.INSTANCE);
        this.hashCache.put(Fnv.hashCode64(TypeUtils.getTypeName(HashMap.class)), ObjectReaderImplMap.INSTANCE);
        this.creator = creator;
        this.modules.add(new ObjectReaderBaseModule(this));
        this.init();
    }
    
    void init() {
        for (final ObjectReaderModule module : this.modules) {
            module.init(this);
        }
    }
    
    public Function getTypeConvert(final Type from, final Type to) {
        final Map<Type, Function> map = this.typeConverts.get(from);
        if (map == null) {
            return null;
        }
        return map.get(to);
    }
    
    public Function registerTypeConvert(final Type from, final Type to, final Function typeConvert) {
        Map<Type, Function> map = this.typeConverts.get(from);
        if (map == null) {
            this.typeConverts.putIfAbsent(from, new ConcurrentHashMap<Type, Function>());
            map = this.typeConverts.get(from);
        }
        return map.put(to, typeConvert);
    }
    
    public ObjectReader getObjectReader(final long hashCode) {
        final ObjectReaderCachePair pair = ObjectReaderProvider.readerCache;
        if (pair != null) {
            if (pair.hashCode == hashCode) {
                return pair.reader;
            }
            if (pair.missCount++ > 16) {
                ObjectReaderProvider.readerCache = null;
            }
        }
        final Long hashCodeObj = new Long(hashCode);
        ObjectReader objectReader = null;
        final ClassLoader tcl = Thread.currentThread().getContextClassLoader();
        if (tcl != null && tcl != ObjectReaderProvider.FASTJSON2_CLASS_LOADER) {
            final int tclHash = System.identityHashCode(tcl);
            final ConcurrentHashMap<Long, ObjectReader> tclHashCache = this.tclHashCaches.get(tclHash);
            if (tclHashCache != null) {
                objectReader = tclHashCache.get(hashCodeObj);
            }
        }
        if (objectReader == null) {
            objectReader = this.hashCache.get(hashCodeObj);
        }
        if (objectReader != null && ObjectReaderProvider.readerCache == null) {
            ObjectReaderProvider.readerCache = new ObjectReaderCachePair(hashCode, objectReader);
        }
        return objectReader;
    }
    
    public ObjectReader getObjectReader(final String typeName, final Class<?> expectClass, final long features) {
        final Class<?> autoTypeClass = this.checkAutoType(typeName, expectClass, features);
        if (autoTypeClass == null) {
            return null;
        }
        final boolean fieldBased = (features & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = this.getObjectReader(autoTypeClass, fieldBased);
        if (autoTypeClass != expectClass) {
            this.registerIfAbsent(Fnv.hashCode64(typeName), objectReader);
        }
        return objectReader;
    }
    
    final void afterAutoType(final String typeName, final Class type) {
        if (this.autoTypeHandler != null) {
            this.autoTypeHandler.accept(type);
        }
        synchronized (this.autoTypeList) {
            this.autoTypeList.putIfAbsent(typeName, new Date());
        }
    }
    
    public Class<?> checkAutoType(final String typeName, final Class<?> expectClass, final long features) {
        if (typeName == null || typeName.isEmpty()) {
            return null;
        }
        if (this.autoTypeBeforeHandler != null) {
            final Class<?> resolvedClass = this.autoTypeBeforeHandler.apply(typeName, expectClass, features);
            if (resolvedClass != null) {
                this.afterAutoType(typeName, resolvedClass);
                return resolvedClass;
            }
        }
        if (ObjectReaderProvider.SAFE_MODE) {
            return null;
        }
        final int typeNameLength = typeName.length();
        if (typeNameLength >= 192) {
            throw new JSONException("autoType is not support. " + typeName);
        }
        if (typeName.charAt(0) == '[') {
            final String componentTypeName = typeName.substring(1);
            this.checkAutoType(componentTypeName, null, features);
        }
        if (expectClass != null && expectClass.getName().equals(typeName)) {
            this.afterAutoType(typeName, expectClass);
            return expectClass;
        }
        final boolean autoTypeSupport = (features & JSONReader.Feature.SupportAutoType.mask) != 0x0L;
        if (autoTypeSupport) {
            long hash = -3750763034362895579L;
            for (int i = 0; i < typeNameLength; ++i) {
                char ch = typeName.charAt(i);
                if (ch == '$') {
                    ch = '.';
                }
                hash ^= ch;
                hash *= 1099511628211L;
                if (Arrays.binarySearch(this.acceptHashCodes, hash) >= 0) {
                    final Class<?> clazz = (Class<?>)TypeUtils.loadClass(typeName);
                    if (clazz != null) {
                        if (expectClass != null && !expectClass.isAssignableFrom(clazz)) {
                            throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
                        }
                        this.afterAutoType(typeName, clazz);
                        return clazz;
                    }
                }
            }
        }
        if (!autoTypeSupport) {
            long hash = -3750763034362895579L;
            int i = 0;
            while (i < typeNameLength) {
                char ch = typeName.charAt(i);
                if (ch == '$') {
                    ch = '.';
                }
                hash ^= ch;
                hash *= 1099511628211L;
                if (Arrays.binarySearch(this.acceptHashCodes, hash) >= 0) {
                    final Class<?> clazz = (Class<?>)TypeUtils.loadClass(typeName);
                    if (clazz != null && expectClass != null && !expectClass.isAssignableFrom(clazz)) {
                        throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
                    }
                    this.afterAutoType(typeName, clazz);
                    return clazz;
                }
                else {
                    ++i;
                }
            }
        }
        if (!autoTypeSupport) {
            return null;
        }
        Class<?> clazz = (Class<?>)TypeUtils.getMapping(typeName);
        if (clazz == null) {
            clazz = (Class<?>)TypeUtils.loadClass(typeName);
            if (clazz != null) {
                if (ClassLoader.class.isAssignableFrom(clazz) || JDKUtils.isSQLDataSourceOrRowSet(clazz)) {
                    throw new JSONException("autoType is not support. " + typeName);
                }
                if (expectClass != null) {
                    if (expectClass.isAssignableFrom(clazz)) {
                        this.afterAutoType(typeName, clazz);
                        return clazz;
                    }
                    if ((features & JSONReader.Feature.IgnoreAutoTypeNotMatch.mask) != 0x0L) {
                        return expectClass;
                    }
                    throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
                }
            }
            this.afterAutoType(typeName, clazz);
            return clazz;
        }
        if (expectClass != null && expectClass != Object.class && clazz != HashMap.class && !expectClass.isAssignableFrom(clazz)) {
            throw new JSONException("type not match. " + typeName + " -> " + expectClass.getName());
        }
        this.afterAutoType(typeName, clazz);
        return clazz;
    }
    
    public List<ObjectReaderModule> getModules() {
        return this.modules;
    }
    
    public void getBeanInfo(final BeanInfo beanInfo, final Class objectClass) {
        for (final ObjectReaderModule module : this.modules) {
            module.getBeanInfo(beanInfo, objectClass);
        }
    }
    
    public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Field field) {
        for (final ObjectReaderModule module : this.modules) {
            module.getFieldInfo(fieldInfo, objectClass, field);
        }
    }
    
    public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Constructor constructor, final int paramIndex, final Parameter parameter) {
        for (final ObjectReaderModule module : this.modules) {
            final ObjectReaderAnnotationProcessor annotationProcessor = module.getAnnotationProcessor();
            if (annotationProcessor != null) {
                annotationProcessor.getFieldInfo(fieldInfo, objectClass, constructor, paramIndex, parameter);
            }
        }
    }
    
    public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Method method, final int paramIndex, final Parameter parameter) {
        for (final ObjectReaderModule module : this.modules) {
            final ObjectReaderAnnotationProcessor annotationProcessor = module.getAnnotationProcessor();
            if (annotationProcessor != null) {
                annotationProcessor.getFieldInfo(fieldInfo, objectClass, method, paramIndex, parameter);
            }
        }
    }
    
    public ObjectReader getObjectReader(final Type objectType) {
        return this.getObjectReader(objectType, false);
    }
    
    public Function<Consumer, ByteArrayValueConsumer> createValueConsumerCreator(final Class objectClass, final FieldReader[] fieldReaderArray) {
        return this.creator.createByteArrayValueConsumerCreator(objectClass, fieldReaderArray);
    }
    
    public Function<Consumer, CharArrayValueConsumer> createCharArrayValueConsumerCreator(final Class objectClass, final FieldReader[] fieldReaderArray) {
        return this.creator.createCharArrayValueConsumerCreator(objectClass, fieldReaderArray);
    }
    
    public ObjectReader getObjectReader(Type objectType, final boolean fieldBased) {
        if (objectType == null) {
            objectType = Object.class;
        }
        final ObjectReader objectReader = fieldBased ? this.cacheFieldBased.get(objectType) : this.cache.get(objectType);
        return (objectReader != null) ? objectReader : this.getObjectReaderInternal(objectType, fieldBased);
    }
    
    private ObjectReader getObjectReaderInternal(final Type objectType, final boolean fieldBased) {
        ObjectReader objectReader = null;
        for (final ObjectReaderModule module : this.modules) {
            objectReader = module.getObjectReader(this, objectType);
            if (objectReader != null) {
                final ObjectReader previous = fieldBased ? this.cacheFieldBased.putIfAbsent(objectType, objectReader) : this.cache.putIfAbsent(objectType, objectReader);
                if (previous != null) {
                    objectReader = previous;
                }
                return objectReader;
            }
        }
        if (objectType instanceof TypeVariable) {
            final Type[] bounds = ((TypeVariable)objectType).getBounds();
            if (bounds.length > 0) {
                final Type bound = bounds[0];
                if (bound instanceof Class) {
                    ObjectReader boundObjectReader = this.getObjectReader(bound, fieldBased);
                    if (boundObjectReader != null) {
                        final ObjectReader previous2 = this.getPreviousObjectReader(fieldBased, objectType, boundObjectReader);
                        if (previous2 != null) {
                            boundObjectReader = previous2;
                        }
                        return boundObjectReader;
                    }
                }
            }
        }
        if (objectType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)objectType;
            final Type rawType = parameterizedType.getRawType();
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (rawType instanceof Class) {
                final Class rawClass = (Class)rawType;
                boolean generic = false;
                for (Class clazz = rawClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
                    if (clazz.getTypeParameters().length > 0) {
                        generic = true;
                        break;
                    }
                }
                if (typeArguments.length == 0 || !generic) {
                    ObjectReader rawClassReader = this.getObjectReader(rawClass, fieldBased);
                    if (rawClassReader != null) {
                        final ObjectReader previous3 = this.getPreviousObjectReader(fieldBased, objectType, rawClassReader);
                        if (previous3 != null) {
                            rawClassReader = previous3;
                        }
                        return rawClassReader;
                    }
                }
            }
        }
        final Class<?> objectClass = TypeUtils.getMapping(objectType);
        final String className = objectClass.getName();
        if (!fieldBased && className.equals("com.google.common.collect.ArrayListMultimap")) {
            objectReader = ObjectReaderImplMap.of(null, objectClass, 0L);
        }
        if (objectReader == null) {
            boolean jsonCompiled = false;
            final Annotation[] annotations2;
            final Annotation[] annotations = annotations2 = objectClass.getAnnotations();
            for (final Annotation annotation : annotations2) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                jsonCompiled = annotationType.getName().equals("com.alibaba.fastjson2.annotation.JSONCompiled");
            }
            if (jsonCompiled) {
                final String codeGenClassName = objectClass.getName() + "_FASTJOSNReader";
                ClassLoader classLoader = objectClass.getClassLoader();
                if (classLoader == null) {
                    classLoader = Thread.currentThread().getContextClassLoader();
                }
                if (classLoader == null) {
                    classLoader = this.getClass().getClassLoader();
                }
                try {
                    final Class<?> loadedClass = classLoader.loadClass(codeGenClassName);
                    if (ObjectReader.class.isAssignableFrom(loadedClass)) {
                        objectReader = (ObjectReader)loadedClass.newInstance();
                    }
                }
                catch (Exception ex) {}
            }
        }
        if (objectReader == null) {
            final ObjectReaderCreator creator = this.getCreator();
            objectReader = creator.createObjectReader(objectClass, objectType, fieldBased, this);
        }
        final ObjectReader previous = this.getPreviousObjectReader(fieldBased, objectType, objectReader);
        if (previous != null) {
            objectReader = previous;
        }
        return objectReader;
    }
    
    private ObjectReader getPreviousObjectReader(final boolean fieldBased, final Type objectType, final ObjectReader boundObjectReader) {
        return fieldBased ? this.cacheFieldBased.putIfAbsent(objectType, boundObjectReader) : this.cache.putIfAbsent(objectType, boundObjectReader);
    }
    
    public JSONReader.AutoTypeBeforeHandler getAutoTypeBeforeHandler() {
        return this.autoTypeBeforeHandler;
    }
    
    public Map<String, Date> getAutoTypeList() {
        return this.autoTypeList;
    }
    
    public void setAutoTypeBeforeHandler(final JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler) {
        this.autoTypeBeforeHandler = autoTypeBeforeHandler;
    }
    
    public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Method method) {
        for (final ObjectReaderModule module : this.modules) {
            final ObjectReaderAnnotationProcessor annotationProcessor = module.getAnnotationProcessor();
            if (annotationProcessor == null) {
                continue;
            }
            annotationProcessor.getFieldInfo(fieldInfo, objectClass, method);
        }
        if (fieldInfo.fieldName == null && fieldInfo.alternateNames == null) {
            final String methodName = method.getName();
            if (methodName.startsWith("set")) {
                final String findName = methodName.substring(3);
                final Field field = BeanUtils.getDeclaredField(objectClass, findName);
                if (field != null) {
                    fieldInfo.alternateNames = new String[] { findName };
                }
            }
        }
    }
    
    public <T> Supplier<T> createObjectCreator(final Class<T> objectClass, final long readerFeatures) {
        final boolean fieldBased = (readerFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = fieldBased ? this.cacheFieldBased.get(objectClass) : this.cache.get(objectClass);
        if (objectReader != null) {
            return (Supplier<T>)(() -> objectReader.createInstance(0L));
        }
        final Constructor constructor = BeanUtils.getDefaultConstructor(objectClass, false);
        if (constructor == null) {
            throw new JSONException("default constructor not found : " + objectClass.getName());
        }
        return (Supplier<T>)LambdaMiscCodec.createSupplier(constructor);
    }
    
    public FieldReader createFieldReader(final Class objectClass, final String fieldName, final long readerFeatures) {
        final boolean fieldBased = (readerFeatures & JSONReader.Feature.FieldBased.mask) != 0x0L;
        final ObjectReader objectReader = fieldBased ? this.cacheFieldBased.get(objectClass) : this.cache.get(objectClass);
        if (objectReader != null) {
            return objectReader.getFieldReader(fieldName);
        }
        final AtomicReference<Field> fieldRef = new AtomicReference<Field>();
        final long nameHashLCase = Fnv.hashCode64LCase(fieldName);
        final long n;
        final AtomicReference<Field> atomicReference;
        BeanUtils.fields(objectClass, field -> {
            if (n == Fnv.hashCode64LCase(field.getName())) {
                atomicReference.set(field);
            }
            return;
        });
        final Field field2 = fieldRef.get();
        if (field2 != null) {
            return this.creator.createFieldReader(fieldName, null, field2.getType(), field2);
        }
        final AtomicReference<Method> methodRef = new AtomicReference<Method>();
        final String setterName;
        final long n2;
        final AtomicReference<Method> atomicReference2;
        BeanUtils.setters(objectClass, method -> {
            setterName = BeanUtils.setterName(method.getName(), PropertyNamingStrategy.CamelCase.name());
            if (n2 == Fnv.hashCode64LCase(setterName)) {
                atomicReference2.set(method);
            }
            return;
        });
        final Method method2 = methodRef.get();
        if (method2 != null) {
            final Class<?>[] params = method2.getParameterTypes();
            final Class fieldClass = params[0];
            return this.creator.createFieldReaderMethod((Class<Object>)objectClass, fieldName, null, fieldClass, fieldClass, method2);
        }
        return null;
    }
    
    public <T> ObjectReader<T> createObjectReader(final String[] names, final Type[] types, final Supplier<T> supplier, final FieldConsumer<T> c) {
        return this.createObjectReader(names, types, null, supplier, c);
    }
    
    public <T> ObjectReader<T> createObjectReader(final String[] names, final Type[] types, final long[] features, final Supplier<T> supplier, final FieldConsumer<T> c) {
        final FieldReader[] fieldReaders = new FieldReader[names.length];
        for (int i = 0; i < names.length; ++i) {
            final Type fieldType = types[i];
            final Class fieldClass = TypeUtils.getClass(fieldType);
            final long feature = (features != null && i < features.length) ? features[i] : 0L;
            fieldReaders[i] = this.creator.createFieldReader(names[i], fieldType, fieldClass, feature, (BiConsumer<Object, Object>)new FieldBiConsumer<Object>(i, (FieldConsumer<Object>)c));
        }
        return this.creator.createObjectReader(null, supplier, fieldReaders);
    }
    
    static {
        FASTJSON2_CLASS_LOADER = JSON.class.getClassLoader();
        String property = System.getProperty("fastjson2.parser.deny");
        if (property == null) {
            property = JSONFactory.getProperty("fastjson2.parser.deny");
        }
        if (property != null && property.length() > 0) {
            DENYS = property.split(",");
        }
        else {
            DENYS = new String[0];
        }
        property = System.getProperty("fastjson2.autoTypeAccept");
        if (property == null) {
            property = JSONFactory.getProperty("fastjson2.autoTypeAccept");
        }
        if (property != null && property.length() > 0) {
            AUTO_TYPE_ACCEPT_LIST = property.split(",");
        }
        else {
            AUTO_TYPE_ACCEPT_LIST = new String[0];
        }
        property = System.getProperty("fastjson2.autoTypeBeforeHandler");
        if (property == null || property.isEmpty()) {
            property = JSONFactory.getProperty("fastjson2.autoTypeBeforeHandler");
        }
        if (property != null) {
            property = property.trim();
        }
        if (property != null && !property.isEmpty()) {
            final Class handlerClass = TypeUtils.loadClass(property);
            if (handlerClass != null) {
                try {
                    ObjectReaderProvider.DEFAULT_AUTO_TYPE_BEFORE_HANDLER = handlerClass.newInstance();
                }
                catch (Exception ignored) {
                    ObjectReaderProvider.DEFAULT_AUTO_TYPE_HANDLER_INIT_ERROR = true;
                }
            }
        }
        property = System.getProperty("fastjson2.autoTypeHandler");
        if (property == null || property.isEmpty()) {
            property = JSONFactory.getProperty("fastjson2.autoTypeHandler");
        }
        if (property != null) {
            property = property.trim();
        }
        if (property != null && !property.isEmpty()) {
            final Class handlerClass = TypeUtils.loadClass(property);
            if (handlerClass != null) {
                try {
                    ObjectReaderProvider.DEFAULT_AUTO_TYPE_HANDLER = (Consumer<Class>)handlerClass.newInstance();
                }
                catch (Exception ignored) {
                    ObjectReaderProvider.DEFAULT_AUTO_TYPE_HANDLER_INIT_ERROR = true;
                }
            }
        }
        property = System.getProperty("fastjson.parser.safeMode");
        if (property == null || property.isEmpty()) {
            property = JSONFactory.getProperty("fastjson.parser.safeMode");
        }
        if (property == null || property.isEmpty()) {
            property = System.getProperty("fastjson2.parser.safeMode");
        }
        if (property == null || property.isEmpty()) {
            property = JSONFactory.getProperty("fastjson2.parser.safeMode");
        }
        if (property != null) {
            property = property.trim();
        }
        SAFE_MODE = "true".equals(property);
    }
    
    static class ObjectReaderCachePair
    {
        final long hashCode;
        final ObjectReader reader;
        volatile int missCount;
        
        public ObjectReaderCachePair(final long hashCode, final ObjectReader reader) {
            this.hashCode = hashCode;
            this.reader = reader;
        }
    }
    
    static class LRUAutoTypeCache extends LinkedHashMap<String, Date>
    {
        private final int maxSize;
        
        public LRUAutoTypeCache(final int maxSize) {
            super(16, 0.75f, false);
            this.maxSize = maxSize;
        }
        
        @Override
        protected boolean removeEldestEntry(final Map.Entry<String, Date> eldest) {
            return this.size() > this.maxSize;
        }
    }
}
