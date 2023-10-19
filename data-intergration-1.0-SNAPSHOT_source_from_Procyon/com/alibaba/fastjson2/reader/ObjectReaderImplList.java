// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.util.JDKUtils;
import java.util.Iterator;
import com.alibaba.fastjson2.JSONException;
import java.util.Map;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.util.GuavaSupport;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.NavigableSet;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.AbstractSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.AbstractSequentialList;
import java.util.Deque;
import java.util.Queue;
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.AbstractCollection;
import java.util.List;
import java.util.Collection;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.ParameterizedType;
import java.util.function.Function;
import java.lang.reflect.Type;

public final class ObjectReaderImplList implements ObjectReader
{
    static final Class CLASS_EMPTY_SET;
    static final Class CLASS_EMPTY_LIST;
    static final Class CLASS_SINGLETON;
    static final Class CLASS_SINGLETON_LIST;
    static final Class CLASS_ARRAYS_LIST;
    static final Class CLASS_UNMODIFIABLE_COLLECTION;
    static final Class CLASS_UNMODIFIABLE_LIST;
    static final Class CLASS_UNMODIFIABLE_SET;
    static final Class CLASS_UNMODIFIABLE_SORTED_SET;
    static final Class CLASS_UNMODIFIABLE_NAVIGABLE_SET;
    public static ObjectReaderImplList INSTANCE;
    final Type listType;
    final Class listClass;
    final Class instanceType;
    final long instanceTypeHash;
    final Type itemType;
    final Class itemClass;
    final String itemClassName;
    final long itemClassNameHash;
    final Function builder;
    Object listSingleton;
    ObjectReader itemObjectReader;
    volatile boolean instanceError;
    
    public static ObjectReader of(Type type, Class listClass, final long features) {
        if (listClass == type && "".equals(listClass.getSimpleName())) {
            type = listClass.getGenericSuperclass();
            listClass = listClass.getSuperclass();
        }
        Type itemType = Object.class;
        Type rawType;
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            rawType = parameterizedType.getRawType();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                itemType = actualTypeArguments[0];
            }
        }
        else {
            rawType = type;
            if (listClass != null) {
                final Type superType = listClass.getGenericSuperclass();
                if (superType instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType2 = (ParameterizedType)superType;
                    rawType = parameterizedType2.getRawType();
                    final Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
                    if (actualTypeArguments2.length == 1) {
                        itemType = actualTypeArguments2[0];
                    }
                }
            }
        }
        if (listClass == null) {
            listClass = TypeUtils.getClass(rawType);
        }
        Function builder = null;
        Class instanceClass;
        if (listClass == Iterable.class || listClass == Collection.class || listClass == List.class || listClass == AbstractCollection.class || listClass == AbstractList.class) {
            instanceClass = ArrayList.class;
        }
        else if (listClass == Queue.class || listClass == Deque.class || listClass == AbstractSequentialList.class) {
            instanceClass = LinkedList.class;
        }
        else if (listClass == Set.class || listClass == AbstractSet.class) {
            instanceClass = HashSet.class;
        }
        else if (listClass == EnumSet.class) {
            instanceClass = HashSet.class;
            builder = (o -> EnumSet.copyOf(o));
        }
        else if (listClass == NavigableSet.class || listClass == SortedSet.class) {
            instanceClass = TreeSet.class;
        }
        else if (listClass == ObjectReaderImplList.CLASS_SINGLETON) {
            instanceClass = ArrayList.class;
            builder = (obj -> Collections.singleton((Object)obj.get(0)));
        }
        else if (listClass == ObjectReaderImplList.CLASS_SINGLETON_LIST) {
            instanceClass = ArrayList.class;
            builder = (obj -> Collections.singletonList((Object)obj.get(0)));
        }
        else if (listClass == ObjectReaderImplList.CLASS_ARRAYS_LIST) {
            instanceClass = ArrayList.class;
            builder = (obj -> Arrays.asList(obj.toArray()));
        }
        else if (listClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_COLLECTION) {
            instanceClass = ArrayList.class;
            builder = (obj -> Collections.unmodifiableCollection((Collection<?>)obj));
        }
        else if (listClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_LIST) {
            instanceClass = ArrayList.class;
            builder = (obj -> Collections.unmodifiableList((List<?>)obj));
        }
        else if (listClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_SET) {
            instanceClass = LinkedHashSet.class;
            builder = (obj -> Collections.unmodifiableSet((Set<?>)obj));
        }
        else if (listClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_SORTED_SET) {
            instanceClass = TreeSet.class;
            builder = (obj -> Collections.unmodifiableSortedSet(obj));
        }
        else if (listClass == ObjectReaderImplList.CLASS_UNMODIFIABLE_NAVIGABLE_SET) {
            instanceClass = TreeSet.class;
            builder = (obj -> Collections.unmodifiableNavigableSet(obj));
        }
        else {
            final String typeName2;
            final String typeName = typeName2 = listClass.getTypeName();
            switch (typeName2) {
                case "com.google.common.collect.ImmutableList":
                case "com.google.common.collect.SingletonImmutableList":
                case "com.google.common.collect.RegularImmutableList":
                case "com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList": {
                    instanceClass = ArrayList.class;
                    builder = GuavaSupport.immutableListConverter();
                    break;
                }
                case "com.google.common.collect.ImmutableSet":
                case "com.google.common.collect.SingletonImmutableSet":
                case "com.google.common.collect.RegularImmutableSet": {
                    instanceClass = ArrayList.class;
                    builder = GuavaSupport.immutableSetConverter();
                    break;
                }
                case "com.google.common.collect.Lists$TransformingRandomAccessList": {
                    instanceClass = ArrayList.class;
                    break;
                }
                case "com.google.common.collect.Lists.TransformingSequentialList": {
                    instanceClass = LinkedList.class;
                    break;
                }
                case "java.util.Collections$SynchronizedRandomAccessList": {
                    instanceClass = ArrayList.class;
                    builder = Collections::synchronizedList;
                    break;
                }
                case "java.util.Collections$SynchronizedCollection": {
                    instanceClass = ArrayList.class;
                    builder = Collections::synchronizedCollection;
                    break;
                }
                case "java.util.Collections$SynchronizedSet": {
                    instanceClass = HashSet.class;
                    builder = Collections::synchronizedSet;
                    break;
                }
                case "java.util.Collections$SynchronizedSortedSet": {
                    instanceClass = TreeSet.class;
                    builder = Collections::synchronizedSortedSet;
                    break;
                }
                case "java.util.Collections$SynchronizedNavigableSet": {
                    instanceClass = TreeSet.class;
                    builder = Collections::synchronizedNavigableSet;
                    break;
                }
                default: {
                    instanceClass = listClass;
                    break;
                }
            }
        }
        final String typeName3 = type.getTypeName();
        switch (typeName3) {
            case "kotlin.collections.EmptySet":
            case "kotlin.collections.EmptyList": {
                final Class<?> clazz = (Class<?>)type;
                Object empty;
                try {
                    final Field field = clazz.getField("INSTANCE");
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    empty = field.get(null);
                }
                catch (NoSuchFieldException | IllegalAccessException ex2) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException e = ex;
                    throw new IllegalStateException("Failed to get singleton of " + type, e);
                }
                return new ObjectReaderImplList(clazz, empty);
            }
            case "java.util.Collections$EmptySet": {
                return new ObjectReaderImplList((Class)type, Collections.emptySet());
            }
            case "java.util.Collections$EmptyList": {
                return new ObjectReaderImplList((Class)type, Collections.emptyList());
            }
            default: {
                if (itemType == String.class && builder == null) {
                    return new ObjectReaderImplListStr(listClass, instanceClass);
                }
                if (itemType == Long.class && builder == null) {
                    return new ObjectReaderImplListInt64(listClass, instanceClass);
                }
                return new ObjectReaderImplList(type, listClass, instanceClass, itemType, builder);
            }
        }
    }
    
    ObjectReaderImplList(final Class listClass, final Object listSingleton) {
        this(listClass, listClass, listClass, Object.class, null);
        this.listSingleton = listSingleton;
    }
    
    public ObjectReaderImplList(final Type listType, final Class listClass, final Class instanceType, final Type itemType, final Function builder) {
        this.listType = listType;
        this.listClass = listClass;
        this.instanceType = instanceType;
        this.instanceTypeHash = Fnv.hashCode64(TypeUtils.getTypeName(instanceType));
        this.itemType = itemType;
        this.itemClass = TypeUtils.getClass(itemType);
        this.builder = builder;
        this.itemClassName = ((this.itemClass != null) ? TypeUtils.getTypeName(this.itemClass) : null);
        this.itemClassNameHash = ((this.itemClassName != null) ? Fnv.hashCode64(this.itemClassName) : 0L);
    }
    
    @Override
    public Class getObjectClass() {
        return this.listClass;
    }
    
    @Override
    public Function getBuildFunction() {
        return this.builder;
    }
    
    @Override
    public Object createInstance(final Collection collection) {
        final int size = collection.size();
        if (size == 0 && this.listClass == List.class) {
            final Collection list = Collections.emptyList();
            if (this.builder != null) {
                return this.builder.apply(list);
            }
            return list;
        }
        else {
            final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
            Collection list2;
            if (this.instanceType == ArrayList.class) {
                list2 = new ArrayList(collection.size());
            }
            else {
                list2 = (Collection)this.createInstance(0L);
            }
            for (final Object item : collection) {
                if (item == null) {
                    list2.add(null);
                }
                else {
                    Object value = item;
                    final Class<?> valueClass = value.getClass();
                    if ((valueClass == JSONObject.class || valueClass == TypeUtils.CLASS_JSON_OBJECT_1x) && this.itemClass != valueClass) {
                        if (this.itemObjectReader == null) {
                            this.itemObjectReader = provider.getObjectReader(this.itemType);
                        }
                        value = this.itemObjectReader.createInstance((Map)value, 0L);
                    }
                    else if (valueClass != this.itemType) {
                        final Function typeConvert = provider.getTypeConvert(valueClass, this.itemType);
                        if (typeConvert != null) {
                            value = typeConvert.apply(value);
                        }
                        else if (item instanceof Map) {
                            final Map map = (Map)item;
                            if (this.itemObjectReader == null) {
                                this.itemObjectReader = provider.getObjectReader(this.itemType);
                            }
                            value = this.itemObjectReader.createInstance(map, 0L);
                        }
                        else if (value instanceof Collection) {
                            if (this.itemObjectReader == null) {
                                this.itemObjectReader = provider.getObjectReader(this.itemType);
                            }
                            value = this.itemObjectReader.createInstance((Collection)value);
                        }
                        else if (!this.itemClass.isInstance(value)) {
                            if (!Enum.class.isAssignableFrom(this.itemClass)) {
                                throw new JSONException("can not convert from " + valueClass + " to " + this.itemType);
                            }
                            if (this.itemObjectReader == null) {
                                this.itemObjectReader = provider.getObjectReader(this.itemType);
                            }
                            if (!(this.itemObjectReader instanceof ObjectReaderImplEnum)) {
                                throw new JSONException("can not convert from " + valueClass + " to " + this.itemType);
                            }
                            value = ((ObjectReaderImplEnum)this.itemObjectReader).getEnum((String)value);
                        }
                    }
                    list2.add(value);
                }
            }
            if (this.builder != null) {
                return this.builder.apply(list2);
            }
            return list2;
        }
    }
    
    @Override
    public Object createInstance(final long features) {
        if (this.instanceType == ArrayList.class) {
            return (JDKUtils.JVM_VERSION == 8) ? new ArrayList(10) : new ArrayList();
        }
        if (this.instanceType == LinkedList.class) {
            return new LinkedList();
        }
        if (this.instanceType == HashSet.class) {
            return new HashSet();
        }
        if (this.instanceType == LinkedHashSet.class) {
            return new LinkedHashSet();
        }
        if (this.instanceType == TreeSet.class) {
            return new TreeSet();
        }
        if (this.listSingleton != null) {
            return this.listSingleton;
        }
        if (this.instanceType != null) {
            JSONException error = null;
            if (!this.instanceError) {
                try {
                    return this.instanceType.newInstance();
                }
                catch (InstantiationException | IllegalAccessException ex3) {
                    final ReflectiveOperationException ex;
                    final ReflectiveOperationException e = ex;
                    this.instanceError = true;
                    error = new JSONException("create list error, type " + this.instanceType);
                }
            }
            if (this.instanceError && List.class.isAssignableFrom(this.instanceType.getSuperclass())) {
                try {
                    return this.instanceType.getSuperclass().newInstance();
                }
                catch (InstantiationException | IllegalAccessException ex4) {
                    final ReflectiveOperationException ex2;
                    final ReflectiveOperationException e = ex2;
                    this.instanceError = true;
                    error = new JSONException("create list error, type " + this.instanceType);
                }
            }
            if (error != null) {
                throw error;
            }
        }
        return new ArrayList();
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        final ObjectReader objectReader = jsonReader.checkAutoType(this.listClass, 0L, features);
        Function builder = this.builder;
        Class listType = this.instanceType;
        if (objectReader != null) {
            listType = objectReader.getObjectClass();
            if (listType == ObjectReaderImplList.CLASS_UNMODIFIABLE_COLLECTION) {
                listType = ArrayList.class;
                builder = Collections::unmodifiableCollection;
            }
            else if (listType == ObjectReaderImplList.CLASS_UNMODIFIABLE_LIST) {
                listType = ArrayList.class;
                builder = Collections::unmodifiableList;
            }
            else if (listType == ObjectReaderImplList.CLASS_UNMODIFIABLE_SET) {
                listType = LinkedHashSet.class;
                builder = Collections::unmodifiableSet;
            }
            else if (listType == ObjectReaderImplList.CLASS_UNMODIFIABLE_SORTED_SET) {
                listType = TreeSet.class;
                builder = Collections::unmodifiableSortedSet;
            }
            else if (listType == ObjectReaderImplList.CLASS_UNMODIFIABLE_NAVIGABLE_SET) {
                listType = TreeSet.class;
                builder = Collections::unmodifiableNavigableSet;
            }
            else if (listType == ObjectReaderImplList.CLASS_SINGLETON) {
                listType = ArrayList.class;
                builder = (list -> Collections.singleton((Object)list.iterator().next()));
            }
            else if (listType == ObjectReaderImplList.CLASS_SINGLETON_LIST) {
                listType = ArrayList.class;
                builder = (list -> Collections.singletonList((Object)list.get(0)));
            }
            else {
                final String typeName = listType.getTypeName();
                switch (typeName) {
                    case "kotlin.collections.EmptySet":
                    case "kotlin.collections.EmptyList": {
                        return objectReader.readObject(jsonReader, fieldType, fieldName, features);
                    }
                }
            }
        }
        final int entryCnt = jsonReader.startArray();
        if (entryCnt > 0 && this.itemObjectReader == null) {
            this.itemObjectReader = jsonReader.getContext().getObjectReader(this.itemType);
        }
        if (listType == ObjectReaderImplList.CLASS_ARRAYS_LIST) {
            final Object[] array = new Object[entryCnt];
            final List list2 = Arrays.asList(array);
            for (int i = 0; i < entryCnt; ++i) {
                Object item;
                if (jsonReader.isReference()) {
                    final String reference = jsonReader.readReference();
                    if ("..".equals(reference)) {
                        item = list2;
                    }
                    else {
                        item = null;
                        jsonReader.addResolveTask(list2, i, JSONPath.of(reference));
                    }
                }
                else {
                    item = this.itemObjectReader.readJSONBObject(jsonReader, this.itemType, i, features);
                }
                array[i] = item;
            }
            return list2;
        }
        Collection list3 = null;
        Label_0800: {
            if (listType == ArrayList.class) {
                list3 = ((entryCnt > 0) ? new ArrayList(entryCnt) : new ArrayList());
            }
            else if (listType == JSONArray.class) {
                list3 = ((entryCnt > 0) ? new JSONArray(entryCnt) : new JSONArray());
            }
            else if (listType == HashSet.class) {
                list3 = new HashSet();
            }
            else if (listType == LinkedHashSet.class) {
                list3 = new LinkedHashSet();
            }
            else if (listType == TreeSet.class) {
                list3 = new TreeSet();
            }
            else if (listType == ObjectReaderImplList.CLASS_EMPTY_SET) {
                list3 = Collections.emptySet();
            }
            else if (listType == ObjectReaderImplList.CLASS_EMPTY_LIST) {
                list3 = Collections.emptyList();
            }
            else if (listType == ObjectReaderImplList.CLASS_SINGLETON_LIST) {
                list3 = new ArrayList();
                builder = (items -> Collections.singletonList((Object)items.iterator().next()));
            }
            else if (listType == ObjectReaderImplList.CLASS_UNMODIFIABLE_LIST) {
                list3 = new ArrayList();
                builder = Collections::unmodifiableList;
            }
            else if (listType != null && EnumSet.class.isAssignableFrom(listType)) {
                list3 = new HashSet();
                builder = (o -> EnumSet.copyOf(o));
            }
            else {
                if (listType != null && listType != this.listType) {
                    try {
                        list3 = listType.newInstance();
                        break Label_0800;
                    }
                    catch (InstantiationException | IllegalAccessException ex2) {
                        final ReflectiveOperationException ex;
                        final ReflectiveOperationException e = ex;
                        throw new JSONException(jsonReader.info("create instance error " + listType), e);
                    }
                }
                list3 = (Collection)this.createInstance(jsonReader.getContext().getFeatures() | features);
            }
        }
        ObjectReader itemObjectReader = this.itemObjectReader;
        Type itemType = this.itemType;
        if (fieldType instanceof ParameterizedType) {
            final Type[] actualTypeArguments = ((ParameterizedType)fieldType).getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                itemType = actualTypeArguments[0];
                if (itemType != this.itemType) {
                    itemObjectReader = jsonReader.getObjectReader(itemType);
                }
            }
        }
        for (int j = 0; j < entryCnt; ++j) {
            Object item2;
            if (jsonReader.isReference()) {
                final String reference2 = jsonReader.readReference();
                if ("..".equals(reference2)) {
                    item2 = list3;
                }
                else {
                    jsonReader.addResolveTask(list3, j, JSONPath.of(reference2));
                    if (!(list3 instanceof List)) {
                        continue;
                    }
                    item2 = null;
                }
            }
            else {
                final ObjectReader autoTypeReader = jsonReader.checkAutoType(this.itemClass, this.itemClassNameHash, features);
                if (autoTypeReader != null) {
                    item2 = autoTypeReader.readJSONBObject(jsonReader, itemType, j, features);
                }
                else {
                    item2 = itemObjectReader.readJSONBObject(jsonReader, itemType, j, features);
                }
            }
            list3.add(item2);
        }
        if (builder != null) {
            return builder.apply(list3);
        }
        return list3;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final JSONReader.Context context = jsonReader.getContext();
        if (this.itemObjectReader == null) {
            this.itemObjectReader = context.getObjectReader(this.itemType);
        }
        if (jsonReader.jsonb) {
            return this.readJSONBObject(jsonReader, fieldType, fieldName, 0L);
        }
        if (jsonReader.readIfNull()) {
            return null;
        }
        Collection list;
        if (jsonReader.nextIfSet()) {
            list = new HashSet();
        }
        else {
            list = (Collection)this.createInstance(context.getFeatures() | features);
        }
        final char ch = jsonReader.current();
        if (ch == '\"') {
            final String str = jsonReader.readString();
            if (this.itemClass == String.class) {
                jsonReader.nextIfComma();
                list.add(str);
                return list;
            }
            if (str.isEmpty()) {
                jsonReader.nextIfComma();
                return null;
            }
            final Function typeConvert = context.getProvider().getTypeConvert(String.class, this.itemType);
            if (typeConvert != null) {
                final Object converted = typeConvert.apply(str);
                jsonReader.nextIfComma();
                list.add(converted);
                return list;
            }
            throw new JSONException(jsonReader.info());
        }
        else if (ch == '[') {
            jsonReader.next();
            ObjectReader itemObjectReader = this.itemObjectReader;
            Type itemType = this.itemType;
            if (fieldType != this.listType && fieldType instanceof ParameterizedType) {
                final Type[] actualTypeArguments = ((ParameterizedType)fieldType).getActualTypeArguments();
                if (actualTypeArguments.length == 1) {
                    itemType = actualTypeArguments[0];
                    if (itemType != this.itemType) {
                        itemObjectReader = jsonReader.getObjectReader(itemType);
                    }
                }
            }
            int i = 0;
            while (!jsonReader.nextIfArrayEnd()) {
                Label_0545: {
                    Object item;
                    if (itemType == String.class) {
                        item = jsonReader.readString();
                    }
                    else {
                        if (itemObjectReader == null) {
                            throw new JSONException(jsonReader.info("TODO : " + itemType));
                        }
                        if (jsonReader.isReference()) {
                            final String reference = jsonReader.readReference();
                            if (!"..".equals(reference)) {
                                jsonReader.addResolveTask(list, i, JSONPath.of(reference));
                                break Label_0545;
                            }
                            item = this;
                        }
                        else {
                            item = itemObjectReader.readObject(jsonReader, itemType, i, 0L);
                        }
                    }
                    list.add(item);
                    jsonReader.nextIfComma();
                }
                ++i;
            }
            jsonReader.nextIfComma();
            if (this.builder != null) {
                return this.builder.apply(list);
            }
            return list;
        }
        else {
            if ((this.itemClass != Object.class && this.itemObjectReader != null) || (this.itemClass == Object.class && jsonReader.isObject())) {
                final Object item2 = this.itemObjectReader.readObject(jsonReader, this.itemType, 0, 0L);
                list.add(item2);
                if (this.builder != null) {
                    list = this.builder.apply(list);
                }
                return list;
            }
            throw new JSONException(jsonReader.info());
        }
    }
    
    static {
        CLASS_EMPTY_SET = Collections.emptySet().getClass();
        CLASS_EMPTY_LIST = Collections.emptyList().getClass();
        CLASS_SINGLETON = Collections.singleton(0).getClass();
        CLASS_SINGLETON_LIST = Collections.singletonList(0).getClass();
        CLASS_ARRAYS_LIST = Arrays.asList(0).getClass();
        CLASS_UNMODIFIABLE_COLLECTION = Collections.unmodifiableCollection((Collection<?>)Collections.emptyList()).getClass();
        CLASS_UNMODIFIABLE_LIST = Collections.unmodifiableList(Collections.emptyList()).getClass();
        CLASS_UNMODIFIABLE_SET = Collections.unmodifiableSet(Collections.emptySet()).getClass();
        CLASS_UNMODIFIABLE_SORTED_SET = Collections.unmodifiableSortedSet(Collections.emptySortedSet()).getClass();
        CLASS_UNMODIFIABLE_NAVIGABLE_SET = Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet()).getClass();
        ObjectReaderImplList.INSTANCE = new ObjectReaderImplList(ArrayList.class, ArrayList.class, ArrayList.class, Object.class, null);
    }
}
