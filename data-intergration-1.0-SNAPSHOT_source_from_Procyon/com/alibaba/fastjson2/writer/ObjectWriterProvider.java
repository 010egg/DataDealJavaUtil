// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.OffsetTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;
import java.util.Calendar;
import java.util.Currency;
import java.math.BigInteger;
import java.util.IdentityHashMap;
import java.util.Arrays;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.util.BeanUtils;
import java.util.Map;
import com.alibaba.fastjson2.util.GuavaSupport;
import java.util.Collection;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.modules.ObjectWriterAnnotationProcessor;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.codec.BeanInfo;
import java.util.Date;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONFactory;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import java.util.List;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentMap;
import com.alibaba.fastjson2.modules.ObjectCodecProvider;

public class ObjectWriterProvider implements ObjectCodecProvider
{
    static final int TYPE_INT32_MASK = 2;
    static final int TYPE_INT64_MASK = 4;
    static final int TYPE_DECIMAL_MASK = 8;
    static final int TYPE_DATE_MASK = 16;
    static final int TYPE_ENUM_MASK = 32;
    static final int NAME_COMPATIBLE_WITH_FILED = 64;
    final ConcurrentMap<Type, ObjectWriter> cache;
    final ConcurrentMap<Type, ObjectWriter> cacheFieldBased;
    final ConcurrentMap<Class, Class> mixInCache;
    final ObjectWriterCreator creator;
    final List<ObjectWriterModule> modules;
    PropertyNamingStrategy namingStrategy;
    volatile long userDefineMask;
    static final int ENUM = 16384;
    static final int[] PRIMITIVE_HASH_CODES;
    static final int[] NOT_REFERENCES_TYPE_HASH_CODES;
    
    public ObjectWriterProvider() {
        this((PropertyNamingStrategy)null);
    }
    
    public ObjectWriterProvider(final PropertyNamingStrategy namingStrategy) {
        this.cache = new ConcurrentHashMap<Type, ObjectWriter>();
        this.cacheFieldBased = new ConcurrentHashMap<Type, ObjectWriter>();
        this.mixInCache = new ConcurrentHashMap<Class, Class>();
        this.modules = new ArrayList<ObjectWriterModule>();
        this.init();
        ObjectWriterCreator creator = null;
        final String creator2 = JSONFactory.CREATOR;
        switch (creator2) {
            case "reflect":
            case "lambda": {
                creator = ObjectWriterCreator.INSTANCE;
                break;
            }
            default: {
                try {
                    if (!JDKUtils.ANDROID && !JDKUtils.GRAAL) {
                        creator = ObjectWriterCreatorASM.INSTANCE;
                    }
                }
                catch (Throwable t) {}
                if (creator == null) {
                    creator = ObjectWriterCreator.INSTANCE;
                    break;
                }
                break;
            }
        }
        this.creator = creator;
        this.namingStrategy = namingStrategy;
    }
    
    public ObjectWriterProvider(final ObjectWriterCreator creator) {
        this.cache = new ConcurrentHashMap<Type, ObjectWriter>();
        this.cacheFieldBased = new ConcurrentHashMap<Type, ObjectWriter>();
        this.mixInCache = new ConcurrentHashMap<Class, Class>();
        this.modules = new ArrayList<ObjectWriterModule>();
        this.init();
        this.creator = creator;
    }
    
    public PropertyNamingStrategy getNamingStrategy() {
        return this.namingStrategy;
    }
    
    @Deprecated
    public void setCompatibleWithFieldName(final boolean stat) {
        if (stat) {
            this.userDefineMask |= 0x40L;
        }
        else {
            this.userDefineMask &= 0xFFFFFFFFFFFFFFBFL;
        }
    }
    
    public void setNamingStrategy(final PropertyNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }
    
    public void mixIn(final Class target, final Class mixinSource) {
        if (mixinSource == null) {
            this.mixInCache.remove(target);
        }
        else {
            this.mixInCache.put(target, mixinSource);
        }
        this.cache.remove(target);
    }
    
    public void cleanupMixIn() {
        this.mixInCache.clear();
    }
    
    public ObjectWriterCreator getCreator() {
        final ObjectWriterCreator contextCreator = JSONFactory.getContextWriterCreator();
        if (contextCreator != null) {
            return contextCreator;
        }
        return this.creator;
    }
    
    public ObjectWriter register(final Type type, final ObjectWriter objectWriter) {
        return this.register(type, objectWriter, false);
    }
    
    public ObjectWriter register(final Type type, final ObjectWriter objectWriter, final boolean fieldBased) {
        if (type == Integer.class) {
            if (objectWriter == null || objectWriter == ObjectWriterImplInt32.INSTANCE) {
                this.userDefineMask &= 0xFFFFFFFFFFFFFFFDL;
            }
            else {
                this.userDefineMask |= 0x2L;
            }
        }
        else if (type == Long.class || type == Long.TYPE) {
            if (objectWriter == null || objectWriter == ObjectWriterImplInt64.INSTANCE) {
                this.userDefineMask &= 0xFFFFFFFFFFFFFFFBL;
            }
            else {
                this.userDefineMask |= 0x4L;
            }
        }
        else if (type == BigDecimal.class) {
            if (objectWriter == null || objectWriter == ObjectWriterImplBigDecimal.INSTANCE) {
                this.userDefineMask &= 0xFFFFFFFFFFFFFFF7L;
            }
            else {
                this.userDefineMask |= 0x8L;
            }
        }
        else if (type == Date.class) {
            if (objectWriter == null || objectWriter == ObjectWriterImplDate.INSTANCE) {
                this.userDefineMask &= 0xFFFFFFFFFFFFFFEFL;
            }
            else {
                this.userDefineMask |= 0x10L;
            }
        }
        else if (type == Enum.class) {
            if (objectWriter == null) {
                this.userDefineMask &= 0xFFFFFFFFFFFFFFDFL;
            }
            else {
                this.userDefineMask |= 0x20L;
            }
        }
        final ConcurrentMap<Type, ObjectWriter> cache = fieldBased ? this.cacheFieldBased : this.cache;
        if (objectWriter == null) {
            return cache.remove(type);
        }
        return cache.put(type, objectWriter);
    }
    
    public ObjectWriter registerIfAbsent(final Type type, final ObjectWriter objectWriter) {
        return this.registerIfAbsent(type, objectWriter, false);
    }
    
    public ObjectWriter registerIfAbsent(final Type type, final ObjectWriter objectWriter, final boolean fieldBased) {
        final ConcurrentMap<Type, ObjectWriter> cache = fieldBased ? this.cacheFieldBased : this.cache;
        return cache.putIfAbsent(type, objectWriter);
    }
    
    public ObjectWriter unregister(final Type type) {
        return this.unregister(type, false);
    }
    
    public ObjectWriter unregister(final Type type, final boolean fieldBased) {
        final ConcurrentMap<Type, ObjectWriter> cache = fieldBased ? this.cacheFieldBased : this.cache;
        return cache.remove(type);
    }
    
    public boolean unregister(final Type type, final ObjectWriter objectWriter) {
        return this.unregister(type, objectWriter, false);
    }
    
    public boolean unregister(final Type type, final ObjectWriter objectWriter, final boolean fieldBased) {
        final ConcurrentMap<Type, ObjectWriter> cache = fieldBased ? this.cacheFieldBased : this.cache;
        return cache.remove(type, objectWriter);
    }
    
    public boolean register(final ObjectWriterModule module) {
        for (int i = this.modules.size() - 1; i >= 0; --i) {
            if (this.modules.get(i) == module) {
                return false;
            }
        }
        module.init(this);
        this.modules.add(0, module);
        return true;
    }
    
    public boolean unregister(final ObjectWriterModule module) {
        return this.modules.remove(module);
    }
    
    @Override
    public Class getMixIn(final Class target) {
        return this.mixInCache.get(target);
    }
    
    public void init() {
        this.modules.add(new ObjectWriterBaseModule(this));
    }
    
    public List<ObjectWriterModule> getModules() {
        return this.modules;
    }
    
    public void getFieldInfo(final BeanInfo beanInfo, final FieldInfo fieldInfo, final Class objectClass, final Field field) {
        for (int i = 0; i < this.modules.size(); ++i) {
            final ObjectWriterModule module = this.modules.get(i);
            final ObjectWriterAnnotationProcessor annotationProcessor = module.getAnnotationProcessor();
            if (annotationProcessor != null) {
                annotationProcessor.getFieldInfo(beanInfo, fieldInfo, objectClass, field);
            }
        }
    }
    
    public void getFieldInfo(final BeanInfo beanInfo, final FieldInfo fieldInfo, final Class objectClass, final Method method) {
        for (int i = 0; i < this.modules.size(); ++i) {
            final ObjectWriterModule module = this.modules.get(i);
            final ObjectWriterAnnotationProcessor annotationProcessor = module.getAnnotationProcessor();
            if (annotationProcessor != null) {
                annotationProcessor.getFieldInfo(beanInfo, fieldInfo, objectClass, method);
            }
        }
    }
    
    public void getBeanInfo(final BeanInfo beanInfo, final Class objectClass) {
        if (this.namingStrategy != null && this.namingStrategy != PropertyNamingStrategy.NeverUseThisValueExceptDefaultValue) {
            beanInfo.namingStrategy = this.namingStrategy.name();
        }
        for (int i = 0; i < this.modules.size(); ++i) {
            final ObjectWriterModule module = this.modules.get(i);
            final ObjectWriterAnnotationProcessor annotationProcessor = module.getAnnotationProcessor();
            if (annotationProcessor != null) {
                annotationProcessor.getBeanInfo(beanInfo, objectClass);
            }
        }
    }
    
    public ObjectWriter getObjectWriter(final Class objectClass) {
        return this.getObjectWriter(objectClass, objectClass, false);
    }
    
    public ObjectWriter getObjectWriter(final Type objectType, final Class objectClass) {
        return this.getObjectWriter(objectType, objectClass, false);
    }
    
    public ObjectWriter getObjectWriter(final Type objectType) {
        final Class objectClass = TypeUtils.getClass(objectType);
        return this.getObjectWriter(objectType, objectClass, false);
    }
    
    public ObjectWriter getObjectWriterFromCache(final Type objectType, final Class objectClass, final boolean fieldBased) {
        return fieldBased ? this.cacheFieldBased.get(objectType) : this.cache.get(objectType);
    }
    
    public ObjectWriter getObjectWriter(final Type objectType, final Class objectClass, final boolean fieldBased) {
        final ObjectWriter objectWriter = fieldBased ? this.cacheFieldBased.get(objectType) : this.cache.get(objectType);
        return (objectWriter != null) ? objectWriter : this.getObjectWriterInternal(objectType, objectClass, fieldBased);
    }
    
    private ObjectWriter getObjectWriterInternal(Type objectType, Class objectClass, boolean fieldBased) {
        final Class superclass = objectClass.getSuperclass();
        if (!objectClass.isEnum() && superclass != null && superclass.isEnum()) {
            return this.getObjectWriter(superclass, superclass, fieldBased);
        }
        if (fieldBased) {
            if (superclass != null && superclass != Object.class && superclass.getName().equals("com.google.protobuf.GeneratedMessageV3")) {
                fieldBased = false;
            }
            if (objectClass.getName().equals("springfox.documentation.spring.web.json.Json")) {
                fieldBased = false;
            }
        }
        ObjectWriter objectWriter = fieldBased ? this.cacheFieldBased.get(objectType) : this.cache.get(objectType);
        if (objectWriter != null) {
            return objectWriter;
        }
        if (TypeUtils.isProxy(objectClass)) {
            if (objectClass == objectType) {
                objectType = superclass;
            }
            objectClass = superclass;
            if (fieldBased) {
                fieldBased = false;
                objectWriter = this.cacheFieldBased.get(objectType);
                if (objectWriter != null) {
                    return objectWriter;
                }
            }
        }
        boolean useModules = true;
        if (fieldBased && Iterable.class.isAssignableFrom(objectClass) && !Collection.class.isAssignableFrom(objectClass)) {
            useModules = false;
        }
        if (useModules) {
            for (int i = 0; i < this.modules.size(); ++i) {
                final ObjectWriterModule module = this.modules.get(i);
                objectWriter = module.getObjectWriter(objectType, objectClass);
                if (objectWriter != null) {
                    final ObjectWriter previous = fieldBased ? this.cacheFieldBased.putIfAbsent(objectType, objectWriter) : this.cache.putIfAbsent(objectType, objectWriter);
                    if (previous != null) {
                        objectWriter = previous;
                    }
                    return objectWriter;
                }
            }
        }
        if (objectClass != null) {
            final String name;
            final String className = name = objectClass.getName();
            switch (name) {
                case "com.google.common.collect.HashMultimap":
                case "com.google.common.collect.LinkedListMultimap":
                case "com.google.common.collect.LinkedHashMultimap":
                case "com.google.common.collect.ArrayListMultimap":
                case "com.google.common.collect.TreeMultimap": {
                    objectWriter = GuavaSupport.createAsMapWriter(objectClass);
                    break;
                }
                case "com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList": {
                    objectWriter = ObjectWriterImplList.INSTANCE;
                    break;
                }
                case "com.alibaba.fastjson.JSONObject": {
                    objectWriter = ObjectWriterImplMap.of(objectClass);
                    break;
                }
                case "android.net.Uri$OpaqueUri":
                case "android.net.Uri$HierarchicalUri":
                case "android.net.Uri$StringUri": {
                    objectWriter = ObjectWriterImplToString.INSTANCE;
                    break;
                }
            }
        }
        if (objectWriter == null && !fieldBased && Map.class.isAssignableFrom(objectClass) && BeanUtils.isExtendedMap(objectClass)) {
            return ObjectWriterImplMap.of(objectClass);
        }
        if (objectWriter == null) {
            final ObjectWriterCreator creator = this.getCreator();
            objectWriter = creator.createObjectWriter(objectClass, fieldBased ? JSONWriter.Feature.FieldBased.mask : 0L, this);
            final ObjectWriter previous2 = fieldBased ? this.cacheFieldBased.putIfAbsent(objectType, objectWriter) : this.cache.putIfAbsent(objectType, objectWriter);
            if (previous2 != null) {
                objectWriter = previous2;
            }
        }
        return objectWriter;
    }
    
    public static boolean isPrimitiveOrEnum(final Class<?> clazz) {
        return Arrays.binarySearch(ObjectWriterProvider.PRIMITIVE_HASH_CODES, System.identityHashCode(clazz)) >= 0 || ((clazz.getModifiers() & 0x4000) != 0x0 && clazz.getSuperclass() == Enum.class);
    }
    
    public static boolean isNotReferenceDetect(final Class<?> clazz) {
        return Arrays.binarySearch(ObjectWriterProvider.NOT_REFERENCES_TYPE_HASH_CODES, System.identityHashCode(clazz)) >= 0 || ((clazz.getModifiers() & 0x4000) != 0x0 && clazz.getSuperclass() == Enum.class);
    }
    
    public void cleanup(final Class objectClass) {
        this.mixInCache.remove(objectClass);
        this.cache.remove(objectClass);
        this.cacheFieldBased.remove(objectClass);
        BeanUtils.cleanupCache(objectClass);
    }
    
    static boolean match(final Type objectType, final ObjectWriter objectWriter, final ClassLoader classLoader, final IdentityHashMap<ObjectWriter, Object> checkedMap) {
        final Class<?> objectClass = TypeUtils.getClass(objectType);
        if (objectClass != null && objectClass.getClassLoader() == classLoader) {
            return true;
        }
        if (checkedMap.containsKey(objectWriter)) {
            return false;
        }
        if (objectWriter instanceof ObjectWriterImplMap) {
            final ObjectWriterImplMap mapTyped = (ObjectWriterImplMap)objectWriter;
            final Class valueClass = TypeUtils.getClass(mapTyped.valueType);
            if (valueClass != null && valueClass.getClassLoader() == classLoader) {
                return true;
            }
            final Class keyClass = TypeUtils.getClass(mapTyped.keyType);
            return keyClass != null && keyClass.getClassLoader() == classLoader;
        }
        else {
            if (objectWriter instanceof ObjectWriterImplCollection) {
                final Class itemClass = TypeUtils.getClass(((ObjectWriterImplCollection)objectWriter).itemType);
                return itemClass != null && itemClass.getClassLoader() == classLoader;
            }
            if (objectWriter instanceof ObjectWriterImplOptional) {
                final Class itemClass = TypeUtils.getClass(((ObjectWriterImplOptional)objectWriter).valueType);
                return itemClass != null && itemClass.getClassLoader() == classLoader;
            }
            if (objectWriter instanceof ObjectWriterAdapter) {
                checkedMap.put(objectWriter, null);
                final List<FieldWriter> fieldWriters = ((ObjectWriterAdapter)objectWriter).fieldWriters;
                for (int i = 0; i < fieldWriters.size(); ++i) {
                    final FieldWriter fieldWriter = fieldWriters.get(i);
                    if (fieldWriter instanceof FieldWriterObject) {
                        final ObjectWriter initObjectWriter = ((FieldWriterObject)fieldWriter).initObjectWriter;
                        if (match(null, initObjectWriter, classLoader, checkedMap)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
    
    public void cleanup(final ClassLoader classLoader) {
        this.mixInCache.entrySet().removeIf(entry -> entry.getKey().getClassLoader() == classLoader);
        final IdentityHashMap<ObjectWriter, Object> checkedMap = new IdentityHashMap<ObjectWriter, Object>();
        this.cache.entrySet().removeIf(entry -> match(entry.getKey(), (ObjectWriter)entry.getValue(), classLoader, checkedMap));
        this.cacheFieldBased.entrySet().removeIf(entry -> match(entry.getKey(), (ObjectWriter)entry.getValue(), classLoader, checkedMap));
        BeanUtils.cleanupCache(classLoader);
    }
    
    static {
        final Class<?>[] classes = (Class<?>[])new Class[] { Boolean.TYPE, Boolean.class, Character.class, Character.TYPE, Byte.class, Byte.TYPE, Short.class, Short.TYPE, Integer.class, Integer.TYPE, Long.class, Long.TYPE, Float.class, Float.TYPE, Double.class, Double.TYPE, BigInteger.class, BigDecimal.class, String.class, Currency.class, Date.class, Calendar.class, UUID.class, Locale.class, LocalTime.class, LocalDate.class, LocalDateTime.class, Instant.class, ZoneId.class, ZonedDateTime.class, OffsetDateTime.class, OffsetTime.class, AtomicInteger.class, AtomicLong.class, String.class, StackTraceElement.class, Collections.emptyList().getClass(), Collections.emptyMap().getClass(), Collections.emptySet().getClass() };
        final int[] codes = new int[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            codes[i] = System.identityHashCode(classes[i]);
        }
        Arrays.sort(codes);
        PRIMITIVE_HASH_CODES = codes;
        final int[] codes2 = Arrays.copyOf(codes, codes.length + 3);
        codes2[codes2.length - 1] = System.identityHashCode(Class.class);
        codes2[codes2.length - 2] = System.identityHashCode(int[].class);
        codes2[codes2.length - 3] = System.identityHashCode(long[].class);
        Arrays.sort(codes2);
        NOT_REFERENCES_TYPE_HASH_CODES = codes2;
    }
}
