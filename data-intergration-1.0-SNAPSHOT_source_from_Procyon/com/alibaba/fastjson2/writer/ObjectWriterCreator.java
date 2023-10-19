// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.HashMap;
import com.alibaba.fastjson2.function.ToCharFunction;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodType;
import com.alibaba.fastjson2.util.JDKUtils;
import java.util.Calendar;
import java.util.UUID;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import com.alibaba.fastjson2.function.ToFloatFunction;
import com.alibaba.fastjson2.function.ToByteFunction;
import com.alibaba.fastjson2.function.ToShortFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.lang.reflect.Member;
import java.lang.reflect.ParameterizedType;
import com.alibaba.fastjson2.modules.ObjectCodecProvider;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.alibaba.fastjson2.filter.Filter;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.TreeMap;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Iterator;
import com.alibaba.fastjson2.modules.ObjectWriterModule;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.BeanUtils;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.JSONFactory;
import java.util.function.Function;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.function.FieldSupplierFunction;
import com.alibaba.fastjson2.function.FieldSupplier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

public class ObjectWriterCreator
{
    public static final ObjectWriterCreator INSTANCE;
    static final Map<Class, LambdaInfo> lambdaMapping;
    protected final AtomicInteger jitErrorCount;
    protected volatile Throwable jitErrorLast;
    
    public ObjectWriterCreator() {
        this.jitErrorCount = new AtomicInteger();
    }
    
    public ObjectWriter createObjectWriter(final List<FieldWriter> fieldWriters) {
        return new ObjectWriterAdapter(null, null, null, 0L, fieldWriters);
    }
    
    public ObjectWriter createObjectWriter(final FieldWriter... fieldWriters) {
        return this.createObjectWriter((List<FieldWriter>)Arrays.asList((FieldWriter[])fieldWriters));
    }
    
    public <T> ObjectWriter<T> createObjectWriter(final String[] names, final Type[] types, final FieldSupplier<T> supplier) {
        final FieldWriter[] fieldWriters = new FieldWriter[names.length];
        for (int i = 0; i < names.length; ++i) {
            final String fieldName = names[i];
            final Type fieldType = types[i];
            final int fieldIndex = i;
            final Function<T, Object> function = new FieldSupplierFunction<T>(supplier, fieldIndex);
            fieldWriters[i] = this.createFieldWriter(fieldName, fieldType, TypeUtils.getClass(fieldType), function);
        }
        return (ObjectWriter<T>)this.createObjectWriter(fieldWriters);
    }
    
    public ObjectWriter createObjectWriter(final Class objectType) {
        return this.createObjectWriter(objectType, 0L, JSONFactory.getDefaultObjectWriterProvider());
    }
    
    public ObjectWriter createObjectWriter(final Class objectType, final FieldWriter... fieldWriters) {
        return this.createObjectWriter(objectType, 0L, fieldWriters);
    }
    
    public ObjectWriter createObjectWriter(final Class objectClass, final long features, final FieldWriter... fieldWriters) {
        if (fieldWriters.length == 0) {
            return this.createObjectWriter(objectClass, features, JSONFactory.getDefaultObjectWriterProvider());
        }
        boolean googleCollection = false;
        if (objectClass != null) {
            final String typeName = objectClass.getName();
            googleCollection = ("com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList".equals(typeName) || "com.google.common.collect.AbstractMapBasedMultimap$WrappedSet".equals(typeName));
        }
        if (googleCollection) {
            return new ObjectWriterAdapter(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
        }
        switch (fieldWriters.length) {
            case 1: {
                if ((fieldWriters[0].features & 0x1000000000000L) == 0x0L) {
                    return new ObjectWriter1(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
                }
                return new ObjectWriterAdapter(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 2: {
                return new ObjectWriter2(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 3: {
                return new ObjectWriter3(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 4: {
                return new ObjectWriter4(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 5: {
                return new ObjectWriter5(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 6: {
                return new ObjectWriter6(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 7: {
                return new ObjectWriter7(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 8: {
                return new ObjectWriter8(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 9: {
                return new ObjectWriter9(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 10: {
                return new ObjectWriter10(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 11: {
                return new ObjectWriter11(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            case 12: {
                return new ObjectWriter12(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
            default: {
                return new ObjectWriterAdapter(objectClass, null, null, features, Arrays.asList((FieldWriter[])fieldWriters));
            }
        }
    }
    
    protected FieldWriter creteFieldWriter(final Class objectClass, final long writerFeatures, final ObjectWriterProvider provider, final BeanInfo beanInfo, final FieldInfo fieldInfo, final Field field) {
        fieldInfo.features = writerFeatures;
        provider.getFieldInfo(beanInfo, fieldInfo, objectClass, field);
        if (fieldInfo.ignore || TypeUtils.isFunction(field.getType())) {
            return null;
        }
        String fieldName;
        if (fieldInfo.fieldName == null || fieldInfo.fieldName.isEmpty()) {
            fieldName = field.getName();
            if (beanInfo.namingStrategy != null) {
                fieldName = BeanUtils.fieldName(fieldName, beanInfo.namingStrategy);
            }
        }
        else {
            fieldName = fieldInfo.fieldName;
        }
        if (beanInfo.orders != null) {
            boolean match = false;
            for (int i = 0; i < beanInfo.orders.length; ++i) {
                if (fieldName.equals(beanInfo.orders[i])) {
                    fieldInfo.ordinal = i;
                    match = true;
                }
            }
            if (!match && fieldInfo.ordinal == 0) {
                fieldInfo.ordinal = beanInfo.orders.length;
            }
        }
        if (beanInfo.includes != null && beanInfo.includes.length > 0) {
            boolean match = false;
            for (final String include : beanInfo.includes) {
                if (include.equals(fieldName)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return null;
            }
        }
        ObjectWriter writeUsingWriter = null;
        if (fieldInfo.writeUsing != null) {
            try {
                final Constructor<?> constructor = fieldInfo.writeUsing.getDeclaredConstructor((Class<?>[])new Class[0]);
                constructor.setAccessible(true);
                writeUsingWriter = (ObjectWriter)constructor.newInstance(new Object[0]);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("create writeUsing Writer error", e);
            }
        }
        try {
            field.setAccessible(true);
        }
        catch (Throwable t) {}
        if (writeUsingWriter == null && fieldInfo.fieldClassMixIn) {
            writeUsingWriter = ObjectWriterBaseModule.VoidObjectWriter.INSTANCE;
        }
        if (writeUsingWriter == null) {
            final Class<?> fieldClass = field.getType();
            if (fieldClass == Date.class) {
                final ObjectWriter objectWriter = provider.cache.get(fieldClass);
                if (objectWriter != ObjectWriterImplDate.INSTANCE) {
                    writeUsingWriter = objectWriter;
                }
            }
            else if (Map.class.isAssignableFrom(fieldClass) && (fieldInfo.keyUsing != null || fieldInfo.valueUsing != null)) {
                ObjectWriter keyWriter = null;
                ObjectWriter valueWriter = null;
                if (fieldInfo.keyUsing != null) {
                    try {
                        final Constructor<?> constructor2 = fieldInfo.keyUsing.getDeclaredConstructor((Class<?>[])new Class[0]);
                        constructor2.setAccessible(true);
                        keyWriter = (ObjectWriter)constructor2.newInstance(new Object[0]);
                    }
                    catch (Exception ex3) {}
                }
                if (fieldInfo.valueUsing != null) {
                    try {
                        final Constructor<?> constructor2 = fieldInfo.valueUsing.getDeclaredConstructor((Class<?>[])new Class[0]);
                        constructor2.setAccessible(true);
                        valueWriter = (ObjectWriter)constructor2.newInstance(new Object[0]);
                    }
                    catch (Exception ex4) {}
                }
                if (keyWriter != null || valueWriter != null) {
                    final ObjectWriterImplMap mapWriter = ObjectWriterImplMap.of(field.getType(), fieldClass);
                    mapWriter.keyWriter = keyWriter;
                    mapWriter.valueWriter = valueWriter;
                    writeUsingWriter = mapWriter;
                }
            }
        }
        String format = fieldInfo.format;
        if (format == null && beanInfo.format != null) {
            format = beanInfo.format;
        }
        return this.createFieldWriter(provider, fieldName, fieldInfo.ordinal, fieldInfo.features, format, fieldInfo.label, field, writeUsingWriter);
    }
    
    public ObjectWriter createObjectWriter(final Class objectClass, final long features, final List<ObjectWriterModule> modules) {
        ObjectWriterProvider provider = null;
        for (final ObjectWriterModule module : modules) {
            if (provider == null) {
                provider = module.getProvider();
            }
        }
        return this.createObjectWriter(objectClass, features, provider);
    }
    
    protected void setDefaultValue(final List<FieldWriter> fieldWriters, final Class objectClass) {
        final Constructor constructor = BeanUtils.getDefaultConstructor(objectClass, true);
        if (constructor == null) {
            return;
        }
        final int parameterCount = constructor.getParameterCount();
        Object object;
        try {
            constructor.setAccessible(true);
            if (parameterCount == 0) {
                object = constructor.newInstance(new Object[0]);
            }
            else {
                if (parameterCount != 1) {
                    return;
                }
                object = constructor.newInstance(true);
            }
        }
        catch (Exception ignored) {
            return;
        }
        for (final FieldWriter fieldWriter : fieldWriters) {
            fieldWriter.setDefaultValue(object);
        }
    }
    
    public ObjectWriter createObjectWriter(final Class objectClass, final long features, final ObjectWriterProvider provider) {
        final BeanInfo beanInfo2;
        final BeanInfo beanInfo = beanInfo2 = new BeanInfo();
        beanInfo2.readerFeatures |= 0x40000000000000L;
        provider.getBeanInfo(beanInfo, objectClass);
        if (beanInfo.serializer != null && ObjectWriter.class.isAssignableFrom(beanInfo.serializer)) {
            try {
                return beanInfo.serializer.newInstance();
            }
            catch (InstantiationException | IllegalAccessException ex3) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("create serializer error", e);
            }
        }
        final boolean record = BeanUtils.isRecord(objectClass);
        long beanFeatures = beanInfo.writerFeatures;
        if (beanInfo.seeAlso != null) {
            beanFeatures &= ~JSONWriter.Feature.WriteClassName.mask;
        }
        final long writerFieldFeatures = features | beanFeatures;
        boolean fieldBased = (writerFieldFeatures & JSONWriter.Feature.FieldBased.mask) != 0x0L;
        if (fieldBased && (record || objectClass.isInterface())) {
            fieldBased = false;
        }
        final FieldInfo fieldInfo = new FieldInfo();
        List<FieldWriter> fieldWriters;
        if (fieldBased) {
            final Map<String, FieldWriter> fieldWriterMap = new TreeMap<String, FieldWriter>();
            final FieldInfo fieldInfo2;
            final long writerFeatures2;
            final BeanInfo beanInfo3;
            final FieldWriter fieldWriter;
            final Map<String, FieldWriter> map;
            BeanUtils.declaredFields(objectClass, field -> {
                fieldInfo2.init();
                fieldWriter = this.creteFieldWriter(objectClass, writerFeatures2, provider, beanInfo3, fieldInfo2, field);
                if (fieldWriter != null) {
                    map.put(fieldWriter.fieldName, fieldWriter);
                }
                return;
            });
            fieldWriters = new ArrayList<FieldWriter>(fieldWriterMap.values());
        }
        else {
            boolean fieldWritersCreated = false;
            fieldWriters = new ArrayList<FieldWriter>();
            for (final ObjectWriterModule module : provider.modules) {
                if (module.createFieldWriters(this, objectClass, fieldWriters)) {
                    fieldWritersCreated = true;
                    break;
                }
            }
            if (!fieldWritersCreated) {
                final Map<String, FieldWriter> fieldWriterMap2 = new TreeMap<String, FieldWriter>();
                if (!record) {
                    final FieldInfo fieldInfo3;
                    final long writerFeatures3;
                    final BeanInfo beanInfo4;
                    final FieldWriter fieldWriter2;
                    final Map<String, FieldWriter> map2;
                    FieldWriter origin;
                    BeanUtils.declaredFields(objectClass, field -> {
                        fieldInfo3.init();
                        fieldInfo3.ignore = ((field.getModifiers() & 0x1) == 0x0);
                        fieldWriter2 = this.creteFieldWriter(objectClass, writerFeatures3, provider, beanInfo4, fieldInfo3, field);
                        if (fieldWriter2 != null) {
                            origin = map2.putIfAbsent(fieldWriter2.fieldName, fieldWriter2);
                            if (origin != null && origin.compareTo(fieldWriter2) > 0) {
                                map2.put(fieldWriter2.fieldName, fieldWriter2);
                            }
                        }
                        return;
                    });
                }
                final Class mixIn = provider.getMixIn(objectClass);
                final FieldInfo fieldInfo4;
                final long features2;
                final BeanInfo beanInfo5;
                final boolean record2;
                String fieldName;
                boolean match;
                String[] includes;
                int length;
                int j = 0;
                String include;
                boolean match2;
                int i;
                Class<?> returnType;
                ObjectWriter writeUsingWriter;
                Constructor<?> constructor;
                final ReflectiveOperationException ex2;
                ReflectiveOperationException e2;
                FieldWriter fieldWriter3;
                final Map<String, FieldWriter> map3;
                FieldWriter origin2;
                BeanUtils.getters(objectClass, mixIn, method -> {
                    fieldInfo4.init();
                    fieldInfo4.features = features2;
                    fieldInfo4.format = beanInfo5.format;
                    provider.getFieldInfo(beanInfo5, fieldInfo4, objectClass, method);
                    if (fieldInfo4.ignore) {
                        return;
                    }
                    else {
                        fieldName = getFieldName(objectClass, provider, beanInfo5, record2, fieldInfo4, method);
                        if (beanInfo5.includes != null && beanInfo5.includes.length > 0) {
                            match = false;
                            includes = beanInfo5.includes;
                            length = includes.length;
                            while (j < length) {
                                include = includes[j];
                                if (include.equals(fieldName)) {
                                    match = true;
                                    break;
                                }
                                else {
                                    ++j;
                                }
                            }
                            if (!match) {
                                return;
                            }
                        }
                        if ((beanInfo5.writerFeatures & JSONWriter.Feature.WriteClassName.mask) != 0x0L && fieldName.equals(beanInfo5.typeKey)) {
                            return;
                        }
                        else {
                            if (beanInfo5.orders != null) {
                                match2 = false;
                                for (i = 0; i < beanInfo5.orders.length; ++i) {
                                    if (fieldName.equals(beanInfo5.orders[i])) {
                                        fieldInfo4.ordinal = i;
                                        match2 = true;
                                    }
                                }
                                if (!match2 && fieldInfo4.ordinal == 0) {
                                    fieldInfo4.ordinal = beanInfo5.orders.length;
                                }
                            }
                            returnType = method.getReturnType();
                            if (TypeUtils.isFunction(returnType)) {
                                return;
                            }
                            else {
                                writeUsingWriter = null;
                                if (fieldInfo4.writeUsing != null) {
                                    try {
                                        constructor = fieldInfo4.writeUsing.getDeclaredConstructor((Class<?>[])new Class[0]);
                                        constructor.setAccessible(true);
                                        writeUsingWriter = (ObjectWriter)constructor.newInstance(new Object[0]);
                                    }
                                    catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex4) {
                                        e2 = ex2;
                                        throw new JSONException("create writeUsing Writer error", e2);
                                    }
                                }
                                if (writeUsingWriter == null && fieldInfo4.fieldClassMixIn) {
                                    writeUsingWriter = ObjectWriterBaseModule.VoidObjectWriter.INSTANCE;
                                }
                                fieldWriter3 = null;
                                if ((beanInfo5.readerFeatures & 0x40000000000000L) != 0x0L) {
                                    try {
                                        fieldWriter3 = this.createFieldWriterLambda(provider, (Class<Object>)objectClass, fieldName, fieldInfo4.ordinal, fieldInfo4.features, fieldInfo4.format, fieldInfo4.label, method, writeUsingWriter);
                                    }
                                    catch (Throwable ignored) {
                                        this.jitErrorCount.incrementAndGet();
                                        this.jitErrorLast = ignored;
                                    }
                                }
                                if (fieldWriter3 == null) {
                                    fieldWriter3 = this.createFieldWriter(provider, (Class<Object>)objectClass, fieldName, fieldInfo4.ordinal, fieldInfo4.features, fieldInfo4.format, fieldInfo4.label, method, writeUsingWriter);
                                }
                                origin2 = map3.putIfAbsent(fieldWriter3.fieldName, fieldWriter3);
                                if (origin2 != null && origin2.compareTo(fieldWriter3) > 0) {
                                    map3.put(fieldName, fieldWriter3);
                                }
                                return;
                            }
                        }
                    }
                });
                fieldWriters = new ArrayList<FieldWriter>(fieldWriterMap2.values());
            }
        }
        final long writerFeatures = features | beanInfo.writerFeatures;
        if (!fieldBased && Throwable.class.isAssignableFrom(objectClass)) {
            return new ObjectWriterException(objectClass, writerFeatures, fieldWriters);
        }
        this.handleIgnores(beanInfo, fieldWriters);
        if (beanInfo.alphabetic) {
            Collections.sort(fieldWriters);
        }
        if (BeanUtils.isExtendedMap(objectClass)) {
            final Type superType = objectClass.getGenericSuperclass();
            final FieldWriter superWriter = ObjectWriters.fieldWriter("$super$", superType, (Class<? super T>)objectClass.getSuperclass(), o -> o);
            fieldWriters.add(superWriter);
        }
        this.setDefaultValue(fieldWriters, objectClass);
        ObjectWriterAdapter writerAdapter = null;
        final String typeName = objectClass.getName();
        final boolean googleCollection = "com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList".equals(typeName) || "com.google.common.collect.AbstractMapBasedMultimap$WrappedSet".equals(typeName);
        if (!googleCollection) {
            switch (fieldWriters.size()) {
                case 1: {
                    if ((fieldWriters.get(0).features & 0x1000000000000L) == 0x0L) {
                        writerAdapter = new ObjectWriter1(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                        break;
                    }
                    break;
                }
                case 2: {
                    writerAdapter = new ObjectWriter2(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 3: {
                    writerAdapter = new ObjectWriter3(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 4: {
                    writerAdapter = new ObjectWriter4(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 5: {
                    writerAdapter = new ObjectWriter5(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 6: {
                    writerAdapter = new ObjectWriter6(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 7: {
                    writerAdapter = new ObjectWriter7(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 8: {
                    writerAdapter = new ObjectWriter8(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 9: {
                    writerAdapter = new ObjectWriter9(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 10: {
                    writerAdapter = new ObjectWriter10(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 11: {
                    writerAdapter = new ObjectWriter11(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
                case 12: {
                    writerAdapter = new ObjectWriter12(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
                    break;
                }
            }
        }
        if (writerAdapter == null) {
            writerAdapter = new ObjectWriterAdapter(objectClass, beanInfo.typeKey, beanInfo.typeName, writerFeatures, fieldWriters);
        }
        if (beanInfo.serializeFilters != null) {
            configSerializeFilters(beanInfo, writerAdapter);
        }
        return writerAdapter;
    }
    
    protected static String getFieldName(final Class objectClass, final ObjectWriterProvider provider, final BeanInfo beanInfo, final boolean record, final FieldInfo fieldInfo, final Method method) {
        String fieldName;
        if (fieldInfo.fieldName == null || fieldInfo.fieldName.isEmpty()) {
            if (record) {
                fieldName = method.getName();
            }
            else {
                fieldName = BeanUtils.getterName(method, beanInfo.namingStrategy);
                Field field = null;
                if ((provider.userDefineMask & 0x40L) != 0x0L && (field = BeanUtils.getField(objectClass, method)) != null) {
                    fieldName = field.getName();
                }
                else {
                    char c0 = '\0';
                    final int len = fieldName.length();
                    if (len > 0) {
                        c0 = fieldName.charAt(0);
                    }
                    final char c2;
                    if ((len == 1 && c0 >= 'a' && c0 <= 'z') || (len > 2 && c0 >= 'A' && c0 <= 'Z' && (c2 = fieldName.charAt(1)) >= 'A' && c2 <= 'Z')) {
                        final char[] chars = fieldName.toCharArray();
                        if (c0 >= 'a') {
                            chars[0] -= ' ';
                        }
                        else {
                            chars[0] += ' ';
                        }
                        final String fieldName2 = new String(chars);
                        field = BeanUtils.getDeclaredField(objectClass, fieldName2);
                        if (field != null && (len == 1 || Modifier.isPublic(field.getModifiers()))) {
                            fieldName = field.getName();
                        }
                    }
                }
            }
        }
        else {
            fieldName = fieldInfo.fieldName;
        }
        return fieldName;
    }
    
    protected static void configSerializeFilters(final BeanInfo beanInfo, final ObjectWriterAdapter writerAdapter) {
        for (final Class<? extends Filter> filterClass : beanInfo.serializeFilters) {
            if (Filter.class.isAssignableFrom(filterClass)) {
                try {
                    final Filter filter = (Filter)filterClass.newInstance();
                    writerAdapter.setFilter(filter);
                }
                catch (InstantiationException ex) {}
                catch (IllegalAccessException ex2) {}
            }
        }
    }
    
    protected void handleIgnores(final BeanInfo beanInfo, final List<FieldWriter> fieldWriters) {
        if (beanInfo.ignores == null || beanInfo.ignores.length == 0) {
            return;
        }
        for (int i = fieldWriters.size() - 1; i >= 0; --i) {
            final FieldWriter fieldWriter = fieldWriters.get(i);
            for (final String ignore : beanInfo.ignores) {
                if (ignore.equals(fieldWriter.fieldName)) {
                    fieldWriters.remove(i);
                    break;
                }
            }
        }
    }
    
    public <T> FieldWriter<T> createFieldWriter(final String fieldName, final String format, final Field field) {
        return this.createFieldWriter(JSONFactory.getDefaultObjectWriterProvider(), fieldName, 0, 0L, format, null, field, null);
    }
    
    public <T> FieldWriter<T> createFieldWriter(final String fieldName, final int ordinal, final long features, final String format, final Field field) {
        return this.createFieldWriter(JSONFactory.getDefaultObjectWriterProvider(), fieldName, ordinal, features, format, null, field, null);
    }
    
    public <T> FieldWriter<T> createFieldWriter(final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final ObjectWriter initObjectWriter) {
        return this.createFieldWriter(JSONFactory.getDefaultObjectWriterProvider(), fieldName, ordinal, features, format, label, field, initObjectWriter);
    }
    
    public <T> FieldWriter<T> createFieldWriter(final ObjectWriterProvider provider, final String fieldName, final int ordinal, final long features, final String format, final String label, final Field field, final ObjectWriter initObjectWriter) {
        final Class<?> declaringClass = field.getDeclaringClass();
        Method method = null;
        if (declaringClass == Throwable.class && field.getName().equals("stackTrace")) {
            method = BeanUtils.getMethod(Throwable.class, "getStackTrace");
        }
        if (method != null) {
            return this.createFieldWriter(provider, (Class<T>)Throwable.class, fieldName, ordinal, features, format, label, method, initObjectWriter);
        }
        final Class<?> fieldClass = field.getType();
        final Type fieldType = field.getGenericType();
        if (initObjectWriter != null) {
            final FieldWriterObject objImp = new FieldWriterObject(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, null);
            objImp.initValueClass = fieldClass;
            if (initObjectWriter != ObjectWriterBaseModule.VoidObjectWriter.INSTANCE) {
                objImp.initObjectWriter = initObjectWriter;
            }
            return (FieldWriter<T>)objImp;
        }
        if (fieldClass == Boolean.TYPE) {
            return (FieldWriter<T>)new FieldWriterBoolValField(fieldName, ordinal, features, format, label, field, fieldClass);
        }
        if (fieldClass == Byte.TYPE) {
            return new FieldWriterInt8ValField<T>(fieldName, ordinal, features, format, label, field);
        }
        if (fieldClass == Short.TYPE) {
            return new FieldWriterInt16ValField<T>(fieldName, ordinal, features, format, label, field);
        }
        if (fieldClass == Integer.TYPE) {
            return new FieldWriterInt32Val<T>(fieldName, ordinal, features, format, label, field);
        }
        if (fieldClass == Long.TYPE) {
            if (format == null || format.isEmpty() || "string".equals(format)) {
                return new FieldWriterInt64ValField<T>(fieldName, ordinal, features, format, label, field);
            }
            return new FieldWriterMillisField<T>(fieldName, ordinal, features, format, label, field);
        }
        else {
            if (fieldClass == Float.TYPE) {
                return new FieldWriterFloatValField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == Float.class) {
                return new FieldWriterFloatField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == Double.TYPE) {
                return new FieldWriterDoubleValField<T>(fieldName, ordinal, format, label, field);
            }
            if (fieldClass == Double.class) {
                return new FieldWriterDoubleField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == Character.TYPE) {
                return new FieldWriterCharValField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == BigInteger.class) {
                return new FieldWriterBigIntField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == BigDecimal.class) {
                return new FieldWriterBigDecimalField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == Date.class) {
                return new FieldWriterDateField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass == String.class) {
                return new FieldWriterStringField<T>(fieldName, ordinal, features, format, label, field);
            }
            if (fieldClass.isEnum()) {
                final BeanInfo beanInfo = new BeanInfo();
                provider.getBeanInfo(beanInfo, fieldClass);
                boolean writeEnumAsJavaBean = beanInfo.writeEnumAsJavaBean;
                if (!writeEnumAsJavaBean) {
                    final ObjectWriter objectWriter = provider.cache.get(fieldClass);
                    if (objectWriter != null && !(objectWriter instanceof ObjectWriterImplEnum)) {
                        writeEnumAsJavaBean = true;
                    }
                }
                final Member enumValueField = BeanUtils.getEnumValueField(fieldClass, provider);
                if (enumValueField == null && !writeEnumAsJavaBean) {
                    final String[] enumAnnotationNames = BeanUtils.getEnumAnnotationNames(fieldClass);
                    if (enumAnnotationNames == null) {
                        return (FieldWriter<T>)new FieldWriterEnum(fieldName, ordinal, features, format, label, fieldType, (Class<? extends Enum>)fieldClass, field, null);
                    }
                }
            }
            if (fieldClass == List.class || fieldClass == ArrayList.class) {
                Type itemType = null;
                if (fieldType instanceof ParameterizedType) {
                    itemType = ((ParameterizedType)fieldType).getActualTypeArguments()[0];
                }
                return new FieldWriterListField<T>(fieldName, itemType, ordinal, features, format, label, fieldType, fieldClass, field);
            }
            if (fieldClass.isArray() && !fieldClass.getComponentType().isPrimitive()) {
                final Class<?> itemClass = fieldClass.getComponentType();
                return new FieldWriterObjectArrayField<T>(fieldName, itemClass, ordinal, features, format, label, itemClass, fieldClass, field);
            }
            return new FieldWriterObject<T>(fieldName, ordinal, features, format, label, field.getGenericType(), fieldClass, field, null);
        }
    }
    
    public <T> FieldWriter<T> createFieldWriter(final Class<T> objectType, final String fieldName, final String dateFormat, final Method method) {
        return this.createFieldWriter(objectType, fieldName, 0, 0L, dateFormat, method);
    }
    
    public <T> FieldWriter<T> createFieldWriter(final Class<T> objectType, final String fieldName, final int ordinal, final long features, final String format, final Method method) {
        return this.createFieldWriter(null, objectType, fieldName, ordinal, features, format, null, method, null);
    }
    
    public <T> FieldWriter<T> createFieldWriter(final ObjectWriterProvider provider, final Class<T> objectType, String fieldName, final int ordinal, final long features, String format, final String label, final Method method, ObjectWriter initObjectWriter) {
        method.setAccessible(true);
        final Class<?> fieldClass = method.getReturnType();
        final Type fieldType = method.getGenericReturnType();
        if (initObjectWriter == null && provider != null) {
            initObjectWriter = this.getInitWriter(provider, fieldClass);
        }
        if (initObjectWriter != null) {
            final FieldWriterObjectMethod objMethod = new FieldWriterObjectMethod(fieldName, ordinal, features, format, label, fieldType, fieldClass, null, method);
            objMethod.initValueClass = fieldClass;
            if (initObjectWriter != ObjectWriterBaseModule.VoidObjectWriter.INSTANCE) {
                objMethod.initObjectWriter = initObjectWriter;
            }
            return (FieldWriter<T>)objMethod;
        }
        if (fieldName == null) {
            fieldName = BeanUtils.getterName(method, null);
        }
        final Field field = BeanUtils.getField(objectType, method);
        if (fieldClass == Boolean.TYPE || fieldClass == Boolean.class) {
            return (FieldWriter<T>)new FieldWriterBoolMethod(fieldName, ordinal, features, format, label, field, method, fieldClass);
        }
        if (fieldClass == Integer.TYPE || fieldClass == Integer.class) {
            return new FieldWriterInt32Method<T>(fieldName, ordinal, features, format, label, field, method, fieldClass);
        }
        if (fieldClass == Float.TYPE || fieldClass == Float.class) {
            return new FieldWriterFloatMethod<T>(fieldName, ordinal, features, format, label, fieldClass, fieldClass, field, method);
        }
        if (fieldClass == Double.TYPE || fieldClass == Double.class) {
            return new FieldWriterDoubleMethod<T>(fieldName, ordinal, features, format, label, fieldClass, fieldClass, field, method);
        }
        if (fieldClass == Long.TYPE || fieldClass == Long.class) {
            if (format == null || format.isEmpty() || "string".equals(format)) {
                return new FieldWriterInt64Method<T>(fieldName, ordinal, features, format, label, field, method, fieldClass);
            }
            return new FieldWriterMillisMethod<T>(fieldName, ordinal, features, format, label, fieldClass, field, method);
        }
        else {
            if (fieldClass == Short.TYPE || fieldClass == Short.class) {
                return new FieldWriterInt16Method<T>(fieldName, ordinal, features, format, label, field, method, fieldClass);
            }
            if (fieldClass == Byte.TYPE || fieldClass == Byte.class) {
                return (FieldWriter<T>)new FieldWriterInt8Method(fieldName, ordinal, features, format, label, field, method, fieldClass);
            }
            if (fieldClass == Character.TYPE || fieldClass == Character.class) {
                return new FieldWriterCharMethod<T>(fieldName, ordinal, features, format, label, field, method, fieldClass);
            }
            if (fieldClass == BigDecimal.class) {
                return new FieldWriterBigDecimalMethod<T>(fieldName, ordinal, features, format, label, field, method);
            }
            if (fieldClass.isEnum() && BeanUtils.getEnumValueField(fieldClass, provider) == null && !BeanUtils.isWriteEnumAsJavaBean(fieldClass)) {
                final String[] enumAnnotationNames = BeanUtils.getEnumAnnotationNames(fieldClass);
                if (enumAnnotationNames == null) {
                    return (FieldWriter<T>)new FieldWriterEnumMethod(fieldName, ordinal, features, format, label, fieldClass, field, method);
                }
            }
            if (fieldClass == Date.class) {
                if (format != null) {
                    format = format.trim();
                    if (format.isEmpty()) {
                        format = null;
                    }
                }
                return new FieldWriterDateMethod<T>(fieldName, ordinal, features, format, label, fieldClass, field, method);
            }
            if (fieldClass == String.class) {
                return new FieldWriterStringMethod<T>(fieldName, ordinal, format, label, features, field, method);
            }
            if (fieldClass == List.class || fieldClass == Iterable.class) {
                Type itemType;
                if (fieldType instanceof ParameterizedType) {
                    itemType = ((ParameterizedType)fieldType).getActualTypeArguments()[0];
                }
                else {
                    itemType = Object.class;
                }
                return new FieldWriterListMethod<T>(fieldName, itemType, ordinal, features, format, label, null, method, fieldType, fieldClass);
            }
            if (fieldClass == Float[].class || fieldClass == Double[].class || fieldClass == BigDecimal[].class) {
                return new FieldWriterObjectArrayMethod<T>(fieldName, fieldClass.getComponentType(), ordinal, features, format, label, fieldType, fieldClass, field, method);
            }
            return new FieldWriterObjectMethod<T>(fieldName, ordinal, features, format, label, fieldType, fieldClass, null, method);
        }
    }
    
    public <T> FieldWriter createFieldWriter(final String fieldName, final ToLongFunction<T> function) {
        return new FieldWriterInt64ValFunc(fieldName, 0, 0L, null, null, null, null, function);
    }
    
    public <T> FieldWriter createFieldWriter(final String fieldName, final ToIntFunction<T> function) {
        return new FieldWriterInt32ValFunc(fieldName, 0, 0L, null, null, null, null, function);
    }
    
    public <T> FieldWriter createFieldWriter(final String fieldName, final Field field, final Method method, final ToIntFunction<T> function) {
        return new FieldWriterInt32ValFunc(fieldName, 0, 0L, null, null, field, method, function);
    }
    
    public <T> FieldWriter createFieldWriter(final String fieldName, final ToShortFunction<T> function) {
        return new FieldWriterInt16ValFunc(fieldName, 0, 0L, null, null, null, null, function);
    }
    
    public <T> FieldWriter createFieldWriter(final String fieldName, final ToByteFunction<T> function) {
        return new FieldWriterInt8ValFunc(fieldName, 0, 0L, null, null, null, null, function);
    }
    
    public <T> FieldWriter createFieldWriter(final String fieldName, final ToFloatFunction<T> function) {
        return new FieldWriterFloatValueFunc(fieldName, 0, 0L, null, null, null, null, function);
    }
    
    public <T> FieldWriter createFieldWriter(final String fieldName, final ToDoubleFunction<T> function) {
        return new FieldWriterDoubleValueFunc(fieldName, 0, 0L, null, null, null, null, function);
    }
    
    public <T> FieldWriter createFieldWriter(final String fieldName, final Predicate<T> function) {
        return new FieldWriterBoolValFunc(fieldName, 0, 0L, null, null, null, null, function);
    }
    
    public <T, V> FieldWriter createFieldWriter(final String fieldName, final Class fieldClass, final Function<T, V> function) {
        return this.createFieldWriter(null, null, fieldName, 0, 0L, null, null, fieldClass, fieldClass, null, function);
    }
    
    public <T, V> FieldWriter createFieldWriter(final String fieldName, final Class fieldClass, final Field field, final Method method, final Function<T, V> function) {
        return this.createFieldWriter(null, null, fieldName, 0, 0L, null, null, fieldClass, fieldClass, field, method, function);
    }
    
    public <T, V> FieldWriter createFieldWriter(final String fieldName, final Type fieldType, final Class fieldClass, final Function<T, V> function) {
        return this.createFieldWriter(null, null, fieldName, 0, 0L, null, null, fieldType, fieldClass, null, function);
    }
    
    public <T, V> FieldWriter createFieldWriter(final String fieldName, final long features, final String format, final Class fieldClass, final Function<T, V> function) {
        return this.createFieldWriter(null, null, fieldName, 0, features, format, null, fieldClass, fieldClass, null, function);
    }
    
    public <T, V> FieldWriter<T> createFieldWriter(final ObjectWriterProvider provider, final Class<T> objectClass, final String fieldName, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class<V> fieldClass, final Method method, final Function<T, V> function) {
        return this.createFieldWriter(provider, objectClass, fieldName, ordinal, features, format, label, fieldType, fieldClass, null, method, function);
    }
    
    public <T, V> FieldWriter<T> createFieldWriter(ObjectWriterProvider provider, final Class<T> objectClass, final String fieldName, final int ordinal, final long features, final String format, final String label, final Type fieldType, final Class<V> fieldClass, final Field field, final Method method, final Function<T, V> function) {
        if (fieldClass == Byte.class) {
            return new FieldWriterInt8Func<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, Byte>)function);
        }
        if (fieldClass == Short.class) {
            return new FieldWriterInt16Func<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, Short>)function);
        }
        if (fieldClass == Integer.class) {
            return new FieldWriterInt32Func<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, Integer>)function);
        }
        if (fieldClass == Long.class) {
            return new FieldWriterInt64Func<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, Long>)function);
        }
        if (fieldClass == BigInteger.class) {
            return new FieldWriterBigIntFunc<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, BigInteger>)function);
        }
        if (fieldClass == BigDecimal.class) {
            return new FieldWriterBigDecimalFunc<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, BigDecimal>)function);
        }
        if (fieldClass == String.class) {
            return new FieldWriterStringFunc<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, String>)function);
        }
        if (fieldClass == Date.class) {
            return new FieldWriterDateFunc<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, Date>)function);
        }
        if (fieldClass == LocalDate.class) {
            return new FieldWriterLocalDateFunc<T>(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method, function);
        }
        if (fieldClass == OffsetDateTime.class) {
            return new FieldWriterOffsetDateTimeFunc<T>(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method, function);
        }
        if (fieldClass == UUID.class) {
            return new FieldWriterUUIDFunc<T>(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method, function);
        }
        if (Calendar.class.isAssignableFrom(fieldClass)) {
            return new FieldWriterCalendarFunc<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, Calendar>)function);
        }
        if (fieldClass.isEnum()) {
            final BeanInfo beanInfo = new BeanInfo();
            if (provider == null) {
                provider = JSONFactory.getDefaultObjectWriterProvider();
            }
            provider.getBeanInfo(beanInfo, fieldClass);
            boolean writeEnumAsJavaBean = beanInfo.writeEnumAsJavaBean;
            if (!writeEnumAsJavaBean) {
                final ObjectWriter objectWriter = provider.cache.get(fieldClass);
                if (objectWriter != null && !(objectWriter instanceof ObjectWriterImplEnum)) {
                    writeEnumAsJavaBean = true;
                }
            }
            if (!writeEnumAsJavaBean && BeanUtils.getEnumValueField(fieldClass, provider) == null) {
                final String[] enumAnnotationNames = BeanUtils.getEnumAnnotationNames(fieldClass);
                if (enumAnnotationNames == null) {
                    return (FieldWriter<T>)new FieldWriterEnumFunc(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method, function);
                }
            }
        }
        if (fieldType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldType;
            final Type rawType = parameterizedType.getRawType();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if ((rawType == List.class || rawType == ArrayList.class) && actualTypeArguments.length == 1) {
                final Type itemType = actualTypeArguments[0];
                if (itemType == String.class) {
                    return new FieldWriterListStrFunc<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, List>)function, fieldType, fieldClass);
                }
                return new FieldWriterListFunc<T>(fieldName, ordinal, features, format, label, itemType, field, method, (Function<T, List>)function, fieldType, fieldClass);
            }
        }
        if (Modifier.isFinal(fieldClass.getModifiers())) {
            return new FieldWriterObjectFuncFinal<T>(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method, function);
        }
        return new FieldWriterObjectFunc<T>(fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method, function);
    }
    
    Object lambdaGetter(final Class objectClass, final Class fieldClass, final Method method) {
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(objectClass);
        final LambdaInfo buildInfo = ObjectWriterCreator.lambdaMapping.get(fieldClass);
        MethodType methodType;
        MethodType invokedType;
        String methodName;
        MethodType samMethodType;
        if (buildInfo != null) {
            methodType = buildInfo.methodType;
            invokedType = buildInfo.invokedType;
            methodName = buildInfo.methodName;
            samMethodType = buildInfo.samMethodType;
        }
        else {
            methodType = MethodType.methodType(fieldClass);
            invokedType = TypeUtils.METHOD_TYPE_FUNCTION;
            methodName = "apply";
            samMethodType = TypeUtils.METHOD_TYPE_OBJECT_OBJECT;
        }
        try {
            final MethodHandle target = lookup.findVirtual(objectClass, method.getName(), methodType);
            final MethodType instantiatedMethodType = target.type();
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, methodName, invokedType, samMethodType, target, instantiatedMethodType);
            return callSite.getTarget().invoke();
        }
        catch (Throwable e) {
            throw new JSONException("create fieldLambdaGetter error, method : " + method, e);
        }
    }
    
    protected ObjectWriter getInitWriter(final ObjectWriterProvider provider, final Class fieldClass) {
        if (fieldClass == Date.class) {
            if ((provider.userDefineMask & 0x10L) != 0x0L) {
                final ObjectWriter objectWriter = provider.cache.get(fieldClass);
                if (objectWriter != ObjectWriterImplDate.INSTANCE) {
                    return objectWriter;
                }
            }
        }
        else if (fieldClass == Long.TYPE || fieldClass == Long.class) {
            if ((provider.userDefineMask & 0x4L) != 0x0L) {
                final ObjectWriter objectWriter = provider.cache.get(Long.class);
                if (objectWriter != ObjectWriterImplInt64.INSTANCE) {
                    return objectWriter;
                }
            }
        }
        else if (fieldClass == BigDecimal.class) {
            if ((provider.userDefineMask & 0x8L) != 0x0L) {
                final ObjectWriter objectWriter = provider.cache.get(fieldClass);
                if (objectWriter != ObjectWriterImplBigDecimal.INSTANCE) {
                    return objectWriter;
                }
            }
        }
        else if (Enum.class.isAssignableFrom(fieldClass)) {
            final ObjectWriter objectWriter = provider.cache.get(fieldClass);
            if (!(objectWriter instanceof ObjectWriterImplEnum)) {
                return objectWriter;
            }
        }
        return null;
    }
    
     <T> FieldWriter<T> createFieldWriterLambda(final ObjectWriterProvider provider, final Class<T> objectClass, final String fieldName, final int ordinal, final long features, final String format, final String label, final Method method, ObjectWriter initObjectWriter) {
        final Class<?> fieldClass = method.getReturnType();
        final Type fieldType = method.getGenericReturnType();
        if (initObjectWriter == null && provider != null) {
            initObjectWriter = this.getInitWriter(provider, fieldClass);
        }
        if (initObjectWriter != null) {
            return null;
        }
        final String objectClassName = objectClass.getName();
        if (objectClassName.indexOf(36) != -1 && objectClassName.contains("$$")) {
            return null;
        }
        final Object lambda = this.lambdaGetter(objectClass, fieldClass, method);
        final Field field = BeanUtils.getField(objectClass, method);
        if (fieldClass == Integer.TYPE) {
            return (FieldWriter<T>)new FieldWriterInt32ValFunc(fieldName, ordinal, features, format, label, null, method, (ToIntFunction)lambda);
        }
        if (fieldClass == Long.TYPE) {
            if (format == null || format.isEmpty() || "string".equals(format)) {
                return new FieldWriterInt64ValFunc<T>(fieldName, ordinal, features, format, label, field, method, (ToLongFunction)lambda);
            }
            return new FieldWriterMillisFunc<T>(fieldName, ordinal, features, format, label, field, method, (ToLongFunction)lambda);
        }
        else {
            if (fieldClass == Boolean.TYPE) {
                return (FieldWriter<T>)new FieldWriterBoolValFunc(fieldName, ordinal, features, format, label, field, method, (Predicate)lambda);
            }
            if (fieldClass == Boolean.class) {
                return (FieldWriter<T>)new FieldWriterBooleanFunc(fieldName, ordinal, features, format, label, field, method, (Function)lambda);
            }
            if (fieldClass == Short.TYPE) {
                return (FieldWriter<T>)new FieldWriterInt16ValFunc(fieldName, ordinal, features, format, label, field, method, (ToShortFunction)lambda);
            }
            if (fieldClass == Byte.TYPE) {
                return (FieldWriter<T>)new FieldWriterInt8ValFunc(fieldName, ordinal, features, format, label, field, method, (ToByteFunction)lambda);
            }
            if (fieldClass == Float.TYPE) {
                return (FieldWriter<T>)new FieldWriterFloatValueFunc(fieldName, ordinal, features, format, label, field, method, (ToFloatFunction)lambda);
            }
            if (fieldClass == Float.class) {
                return new FieldWriterFloatFunc<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, Float>)lambda);
            }
            if (fieldClass == Double.TYPE) {
                return (FieldWriter<T>)new FieldWriterDoubleValueFunc(fieldName, ordinal, features, format, label, field, method, (ToDoubleFunction)lambda);
            }
            if (fieldClass == Double.class) {
                return new FieldWriterDoubleFunc<T>(fieldName, ordinal, features, format, label, field, method, (Function<T, Double>)lambda);
            }
            if (fieldClass == Character.TYPE) {
                return (FieldWriter<T>)new FieldWriterCharValFunc(fieldName, ordinal, features, format, label, field, method, (ToCharFunction)lambda);
            }
            final Function function = (Function)lambda;
            return this.createFieldWriter(provider, objectClass, fieldName, ordinal, features, format, label, fieldType, fieldClass, field, method, function);
        }
    }
    
    static {
        INSTANCE = new ObjectWriterCreator();
        (lambdaMapping = new HashMap<Class, LambdaInfo>()).put(Boolean.TYPE, new LambdaInfo(Boolean.TYPE, Predicate.class, "test"));
        ObjectWriterCreator.lambdaMapping.put(Character.TYPE, new LambdaInfo(Character.TYPE, ToCharFunction.class, "applyAsChar"));
        ObjectWriterCreator.lambdaMapping.put(Byte.TYPE, new LambdaInfo(Byte.TYPE, ToByteFunction.class, "applyAsByte"));
        ObjectWriterCreator.lambdaMapping.put(Short.TYPE, new LambdaInfo(Short.TYPE, ToShortFunction.class, "applyAsShort"));
        ObjectWriterCreator.lambdaMapping.put(Integer.TYPE, new LambdaInfo(Integer.TYPE, ToIntFunction.class, "applyAsInt"));
        ObjectWriterCreator.lambdaMapping.put(Long.TYPE, new LambdaInfo(Long.TYPE, ToLongFunction.class, "applyAsLong"));
        ObjectWriterCreator.lambdaMapping.put(Float.TYPE, new LambdaInfo(Float.TYPE, ToFloatFunction.class, "applyAsFloat"));
        ObjectWriterCreator.lambdaMapping.put(Double.TYPE, new LambdaInfo(Double.TYPE, ToDoubleFunction.class, "applyAsDouble"));
    }
    
    static class LambdaInfo
    {
        final Class fieldClass;
        final Class supplierClass;
        final String methodName;
        final MethodType methodType;
        final MethodType invokedType;
        final MethodType samMethodType;
        
        LambdaInfo(final Class fieldClass, final Class supplierClass, final String methodName) {
            this.fieldClass = fieldClass;
            this.supplierClass = supplierClass;
            this.methodName = methodName;
            this.methodType = MethodType.methodType(fieldClass);
            this.invokedType = MethodType.methodType(supplierClass);
            this.samMethodType = MethodType.methodType(fieldClass, Object.class);
        }
    }
}
