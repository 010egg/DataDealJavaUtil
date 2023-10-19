// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSON;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.IdentityHashMap;
import java.util.WeakHashMap;
import java.util.LinkedHashMap;
import java.util.Hashtable;
import java.util.SortedMap;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.EnumSet;
import java.util.BitSet;
import java.util.Currency;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Set;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.HashSet;
import java.util.Collection;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.alibaba.fastjson2.JSONReader;

public class ContextAutoTypeBeforeHandler implements JSONReader.AutoTypeBeforeHandler
{
    final long[] acceptHashCodes;
    final ConcurrentMap<Integer, ConcurrentHashMap<Long, Class>> tclHashCaches;
    final Map<Long, Class> classCache;
    
    public ContextAutoTypeBeforeHandler(final Class... types) {
        this(false, types);
    }
    
    public ContextAutoTypeBeforeHandler(final boolean includeBasic, final Class... types) {
        this(includeBasic, names((Collection<Class>)Arrays.asList((Class[])types)));
    }
    
    public ContextAutoTypeBeforeHandler(final String... acceptNames) {
        this(false, acceptNames);
    }
    
    public ContextAutoTypeBeforeHandler(final boolean includeBasic) {
        this(includeBasic, new String[0]);
    }
    
    static String[] names(final Collection<Class> types) {
        final Set<String> nameSet = new HashSet<String>();
        for (final Class type : types) {
            if (type == null) {
                continue;
            }
            final String name = TypeUtils.getTypeName(type);
            nameSet.add(name);
        }
        return nameSet.toArray(new String[nameSet.size()]);
    }
    
    public ContextAutoTypeBeforeHandler(final boolean includeBasic, final String... acceptNames) {
        this.tclHashCaches = new ConcurrentHashMap<Integer, ConcurrentHashMap<Long, Class>>();
        this.classCache = new ConcurrentHashMap<Long, Class>(16, 0.75f, 1);
        final Set<String> nameSet = new HashSet<String>();
        if (includeBasic) {
            final Class[] basicTypes = { Object.class, Byte.TYPE, Byte.class, Short.TYPE, Short.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Float.TYPE, Float.class, Double.TYPE, Double.class, Number.class, BigInteger.class, BigDecimal.class, AtomicInteger.class, AtomicLong.class, AtomicBoolean.class, AtomicIntegerArray.class, AtomicLongArray.class, AtomicReference.class, Boolean.TYPE, Boolean.class, Character.TYPE, Character.class, String.class, UUID.class, Currency.class, BitSet.class, EnumSet.class, EnumSet.noneOf(TimeUnit.class).getClass(), Date.class, Calendar.class, LocalTime.class, LocalDate.class, LocalDateTime.class, Instant.class, SimpleDateFormat.class, DateTimeFormatter.class, TimeUnit.class, Set.class, HashSet.class, LinkedHashSet.class, TreeSet.class, List.class, ArrayList.class, LinkedList.class, ConcurrentLinkedQueue.class, ConcurrentSkipListSet.class, CopyOnWriteArrayList.class, Collections.emptyList().getClass(), Collections.emptyMap().getClass(), TypeUtils.CLASS_SINGLE_SET, TypeUtils.CLASS_SINGLE_LIST, TypeUtils.CLASS_UNMODIFIABLE_COLLECTION, TypeUtils.CLASS_UNMODIFIABLE_LIST, TypeUtils.CLASS_UNMODIFIABLE_SET, TypeUtils.CLASS_UNMODIFIABLE_SORTED_SET, TypeUtils.CLASS_UNMODIFIABLE_NAVIGABLE_SET, Collections.unmodifiableMap((Map<?, ?>)new HashMap<Object, Object>()).getClass(), Collections.unmodifiableNavigableMap((NavigableMap<Object, ?>)new TreeMap<Object, Object>()).getClass(), Collections.unmodifiableSortedMap((SortedMap<Object, ?>)new TreeMap<Object, Object>()).getClass(), Arrays.asList(new Object[0]).getClass(), Map.class, HashMap.class, Hashtable.class, TreeMap.class, LinkedHashMap.class, WeakHashMap.class, IdentityHashMap.class, ConcurrentMap.class, ConcurrentHashMap.class, ConcurrentSkipListMap.class, Exception.class, IllegalAccessError.class, IllegalAccessException.class, IllegalArgumentException.class, IllegalMonitorStateException.class, IllegalStateException.class, IllegalThreadStateException.class, IndexOutOfBoundsException.class, InstantiationError.class, InstantiationException.class, InternalError.class, InterruptedException.class, LinkageError.class, NegativeArraySizeException.class, NoClassDefFoundError.class, NoSuchFieldError.class, NoSuchFieldException.class, NoSuchMethodError.class, NoSuchMethodException.class, NullPointerException.class, NumberFormatException.class, OutOfMemoryError.class, RuntimeException.class, SecurityException.class, StackOverflowError.class, StringIndexOutOfBoundsException.class, TypeNotPresentException.class, VerifyError.class, StackTraceElement.class };
            for (int i = 0; i < basicTypes.length; ++i) {
                final String name = TypeUtils.getTypeName(basicTypes[i]);
                nameSet.add(name);
            }
            final String[] basicTypeNames = { "javax.validation.ValidationException", "javax.validation.NoProviderFoundException" };
            nameSet.addAll(Arrays.asList(basicTypeNames));
        }
        for (int j = 0; j < acceptNames.length; ++j) {
            String name2 = acceptNames[j];
            if (name2 != null) {
                if (!name2.isEmpty()) {
                    final Class mapping = TypeUtils.getMapping(name2);
                    if (mapping != null) {
                        name2 = TypeUtils.getTypeName(mapping);
                    }
                    nameSet.add(name2);
                }
            }
        }
        long[] array = new long[nameSet.size()];
        int index = 0;
        for (final String name3 : nameSet) {
            long hashCode = -3750763034362895579L;
            for (int k = 0; k < name3.length(); ++k) {
                char ch = name3.charAt(k);
                if (ch == '$') {
                    ch = '.';
                }
                hashCode ^= ch;
                hashCode *= 1099511628211L;
            }
            array[index++] = hashCode;
        }
        if (index != array.length) {
            array = Arrays.copyOf(array, index);
        }
        Arrays.sort(array);
        this.acceptHashCodes = array;
    }
    
    @Override
    public Class<?> apply(final long typeNameHash, final Class<?> expectClass, final long features) {
        final ClassLoader tcl = Thread.currentThread().getContextClassLoader();
        if (tcl != null && tcl != JSON.class.getClassLoader()) {
            final int tclHash = System.identityHashCode(tcl);
            final ConcurrentHashMap<Long, Class> tclHashCache = this.tclHashCaches.get(tclHash);
            if (tclHashCache != null) {
                return tclHashCache.get(typeNameHash);
            }
        }
        return this.classCache.get(typeNameHash);
    }
    
    @Override
    public Class<?> apply(String typeName, final Class<?> expectClass, final long features) {
        if ("O".equals(typeName)) {
            typeName = "Object";
        }
        long hash = -3750763034362895579L;
        for (int i = 0, typeNameLength = typeName.length(); i < typeNameLength; ++i) {
            char ch = typeName.charAt(i);
            if (ch == '$') {
                ch = '.';
            }
            hash ^= ch;
            hash *= 1099511628211L;
            if (Arrays.binarySearch(this.acceptHashCodes, hash) >= 0) {
                final long typeNameHash = Fnv.hashCode64(typeName);
                Class clazz = this.apply(typeNameHash, expectClass, features);
                if (clazz == null) {
                    clazz = TypeUtils.loadClass(typeName);
                    if (clazz != null) {
                        final Class origin = this.putCacheIfAbsent(typeNameHash, clazz);
                        if (origin != null) {
                            clazz = origin;
                        }
                    }
                }
                if (clazz != null) {
                    return (Class<?>)clazz;
                }
            }
        }
        final long typeNameHash2 = Fnv.hashCode64(typeName);
        if (typeName.length() > 0 && typeName.charAt(0) == '[') {
            final Class clazz2 = this.apply(typeNameHash2, expectClass, features);
            if (clazz2 != null) {
                return (Class<?>)clazz2;
            }
            final String itemTypeName = typeName.substring(1);
            Class itemExpectClass = null;
            if (expectClass != null) {
                itemExpectClass = expectClass.getComponentType();
            }
            final Class itemType = this.apply(itemTypeName, itemExpectClass, features);
            if (itemType != null) {
                Class arrayType;
                if (itemType == itemExpectClass) {
                    arrayType = expectClass;
                }
                else {
                    arrayType = TypeUtils.getArrayClass(itemType);
                }
                final Class origin2 = this.putCacheIfAbsent(typeNameHash2, arrayType);
                if (origin2 != null) {
                    arrayType = origin2;
                }
                return (Class<?>)arrayType;
            }
        }
        final Class mapping = TypeUtils.getMapping(typeName);
        if (mapping != null) {
            final String mappingTypeName = TypeUtils.getTypeName(mapping);
            if (!typeName.equals(mappingTypeName)) {
                final Class<?> mappingClass = this.apply(mappingTypeName, expectClass, features);
                if (mappingClass != null) {
                    this.putCacheIfAbsent(typeNameHash2, mappingClass);
                }
                return mappingClass;
            }
        }
        return null;
    }
    
    private Class putCacheIfAbsent(final long typeNameHash, final Class type) {
        final ClassLoader tcl = Thread.currentThread().getContextClassLoader();
        if (tcl != null && tcl != JSON.class.getClassLoader()) {
            final int tclHash = System.identityHashCode(tcl);
            ConcurrentHashMap<Long, Class> tclHashCache = this.tclHashCaches.get(tclHash);
            if (tclHashCache == null) {
                this.tclHashCaches.putIfAbsent(tclHash, new ConcurrentHashMap<Long, Class>());
                tclHashCache = this.tclHashCaches.get(tclHash);
            }
            return tclHashCache.putIfAbsent(typeNameHash, type);
        }
        return this.classCache.putIfAbsent(typeNameHash, type);
    }
}
