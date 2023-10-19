// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.HashMap;
import java.util.function.ObjDoubleConsumer;
import com.alibaba.fastjson2.function.ObjFloatConsumer;
import com.alibaba.fastjson2.function.ObjCharConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ObjIntConsumer;
import com.alibaba.fastjson2.function.ObjShortConsumer;
import com.alibaba.fastjson2.function.ObjByteConsumer;
import com.alibaba.fastjson2.function.ObjBoolConsumer;
import com.alibaba.fastjson2.modules.ObjectCodecProvider;
import com.alibaba.fastjson2.util.Fnv;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.Date;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.UUID;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.function.BiConsumer;
import com.alibaba.fastjson2.TypeReference;
import java.util.Locale;
import java.lang.invoke.MethodType;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.LambdaMetafactory;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor;
import java.util.Iterator;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.modules.ObjectReaderModule;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Modifier;
import com.alibaba.fastjson2.JSONObject;
import java.util.function.IntFunction;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import java.lang.reflect.AccessibleObject;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import java.util.Arrays;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.annotation.JSONField;
import java.util.LinkedHashMap;
import com.alibaba.fastjson2.codec.BeanInfo;
import java.util.function.Supplier;
import java.util.function.BiFunction;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.codec.FieldInfo;
import java.lang.reflect.Parameter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.lang.reflect.Executable;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONFactory;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectReaderCreator
{
    public static final boolean JIT;
    public static final ObjectReaderCreator INSTANCE;
    protected final AtomicInteger jitErrorCount;
    protected volatile Throwable jitErrorLast;
    protected static final Map<Class, LambdaSetterInfo> methodTypeMapping;
    
    public ObjectReaderCreator() {
        this.jitErrorCount = new AtomicInteger();
    }
    
    public <T> ObjectReader<T> createObjectReaderNoneDefaultConstructor(final Constructor constructor, final String... paramNames) {
        final Function<Map<Long, Object>, T> function = this.createFunction(constructor, paramNames);
        final Class declaringClass = constructor.getDeclaringClass();
        final FieldReader[] fieldReaders = this.createFieldReaders(JSONFactory.getDefaultObjectReaderProvider(), declaringClass, declaringClass, constructor, constructor.getParameters(), paramNames);
        return this.createObjectReaderNoneDefaultConstructor(declaringClass, function, fieldReaders);
    }
    
    public <T> ObjectReader<T> createObjectReaderNoneDefaultConstructor(final Class objectClass, final Function<Map<Long, Object>, T> creator, final FieldReader... fieldReaders) {
        return new ObjectReaderNoneDefaultConstructor<T>(objectClass, null, null, 0L, creator, null, null, fieldReaders, null, null, null);
    }
    
    public <T> ObjectReader<T> createObjectReaderFactoryMethod(final Method factoryMethod, final String... paramNames) {
        final Function<Map<Long, Object>, Object> factoryFunction = this.createFactoryFunction(factoryMethod, paramNames);
        final FieldReader[] fieldReaders = this.createFieldReaders(JSONFactory.getDefaultObjectReaderProvider(), null, null, factoryMethod, factoryMethod.getParameters(), paramNames);
        return new ObjectReaderNoneDefaultConstructor<T>(null, null, null, 0L, (Function<Map<Long, Object>, T>)factoryFunction, null, paramNames, fieldReaders, null, null, null);
    }
    
    public FieldReader[] createFieldReaders(final ObjectReaderProvider provider, final Class objectClass, final Type objectType, final Executable owner, final Parameter[] parameters, final String... paramNames) {
        Class<?> declaringClass = null;
        if (owner != null) {
            declaringClass = owner.getDeclaringClass();
        }
        final FieldReader[] fieldReaders = new FieldReader[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            final FieldInfo fieldInfo = new FieldInfo();
            final Parameter parameter = parameters[i];
            String paramName;
            if (i < paramNames.length) {
                paramName = paramNames[i];
            }
            else {
                paramName = parameter.getName();
            }
            if (owner instanceof Constructor) {
                provider.getFieldInfo(fieldInfo, declaringClass, (Constructor)owner, i, parameter);
            }
            if (owner instanceof Constructor) {
                final Field field = BeanUtils.getDeclaredField(declaringClass, paramName);
                if (field != null) {
                    provider.getFieldInfo(fieldInfo, declaringClass, field);
                }
            }
            String fieldName;
            if (fieldInfo.fieldName == null || fieldInfo.fieldName.isEmpty()) {
                fieldName = paramName;
            }
            else {
                fieldName = fieldInfo.fieldName;
            }
            if (fieldName == null) {
                fieldName = "arg" + i;
            }
            if (paramName == null) {
                paramName = "arg" + i;
            }
            final ObjectReader initReader = getInitReader(provider, parameter.getParameterizedType(), parameter.getType(), fieldInfo);
            final Type paramType = parameter.getParameterizedType();
            fieldReaders[i] = this.createFieldReaderParam((Class<Object>)null, null, fieldName, i, fieldInfo.features, fieldInfo.format, paramType, parameter.getType(), paramName, declaringClass, parameter, null, initReader);
        }
        return fieldReaders;
    }
    
    public <T> Function<Map<Long, Object>, T> createFactoryFunction(final Method factoryMethod, final String... paramNames) {
        factoryMethod.setAccessible(true);
        return new FactoryFunction<T>(factoryMethod, paramNames);
    }
    
    public <T> Function<Map<Long, Object>, T> createFunction(final Constructor constructor, final String... paramNames) {
        constructor.setAccessible(true);
        return new ConstructorFunction<T>(null, constructor, null, null, null, paramNames);
    }
    
    public <T> Function<Map<Long, Object>, T> createFunction(final Constructor constructor, final Constructor markerConstructor, final String... paramNames) {
        if (markerConstructor == null) {
            constructor.setAccessible(true);
        }
        else {
            markerConstructor.setAccessible(true);
        }
        return new ConstructorFunction<T>(null, constructor, null, null, markerConstructor, paramNames);
    }
    
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectClass, final FieldReader... fieldReaders) {
        return this.createObjectReader(objectClass, null, 0L, null, (Supplier<T>)this.createSupplier((Class<T>)objectClass), null, fieldReaders);
    }
    
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectClass, final Supplier<T> defaultCreator, final FieldReader... fieldReaders) {
        return this.createObjectReader(objectClass, null, 0L, null, defaultCreator, null, fieldReaders);
    }
    
    public <T> ObjectReader<T> createObjectReaderSeeAlso(final Class<T> objectType, final Class[] seeAlso, final FieldReader... fieldReaders) {
        final Supplier<T> instanceSupplier = this.createSupplier(objectType);
        return new ObjectReaderSeeAlso<T>(objectType, instanceSupplier, "@type", seeAlso, null, null, fieldReaders);
    }
    
    public <T> ObjectReader<T> createObjectReaderSeeAlso(final Class<T> objectClass, final String typeKey, final Class[] seeAlso, final String[] seeAlsoNames, final FieldReader... fieldReaders) {
        final Supplier<T> creator = this.createSupplier(objectClass);
        return new ObjectReaderSeeAlso<T>(objectClass, creator, typeKey, seeAlso, seeAlsoNames, null, fieldReaders);
    }
    
    public <T> ObjectReader<T> createObjectReaderSeeAlso(final Class<T> objectClass, final String typeKey, final Class[] seeAlso, final String[] seeAlsoNames, final Class seeAlsoDefault, final FieldReader... fieldReaders) {
        final Supplier<T> creator = this.createSupplier(objectClass);
        return new ObjectReaderSeeAlso<T>(objectClass, creator, typeKey, seeAlso, seeAlsoNames, seeAlsoDefault, fieldReaders);
    }
    
    public <T> ObjectReader<T> createObjectReaderSeeAlso(final Class<T> objectType, final Supplier<T> defaultCreator, final String typeKey, final Class[] seeAlso, final String[] seeAlsoNames, final FieldReader... fieldReaders) {
        return new ObjectReaderSeeAlso<T>(objectType, defaultCreator, typeKey, seeAlso, seeAlsoNames, null, fieldReaders);
    }
    
    protected <T> ObjectReader<T> createObjectReaderWithBuilder(final Class<T> objectClass, final Type objectType, final ObjectReaderProvider provider, final BeanInfo beanInfo) {
        Function<Object, Object> builderFunction = null;
        if (beanInfo.buildMethod != null) {
            builderFunction = this.createBuildFunction(beanInfo.buildMethod);
        }
        final Class builderClass = beanInfo.builder;
        String builderWithPrefix = beanInfo.builderWithPrefix;
        if (builderWithPrefix == null || builderWithPrefix.isEmpty()) {
            builderWithPrefix = "with";
        }
        final int builderWithPrefixLenth = builderWithPrefix.length();
        final Map<String, FieldReader> fieldReaders = new LinkedHashMap<String, FieldReader>();
        final String prefix = builderWithPrefix;
        final FieldInfo fieldInfo = new FieldInfo();
        final FieldInfo fieldInfo2;
        String methodName;
        int methodNameLength;
        final String prefix2;
        boolean prefixNotMach;
        final Class<Object> clazz;
        String fieldName;
        final int prefixLength;
        FieldReader fieldReader;
        final Map<String, FieldReader> map;
        FieldReader origin;
        Type fieldType;
        Class fieldClass;
        FieldReader fieldReader2;
        FieldReader origin2;
        String[] alternateNames;
        int length;
        int i = 0;
        String alternateName;
        BeanUtils.setters(builderClass, false, method -> {
            fieldInfo2.init();
            provider.getFieldInfo(fieldInfo2, objectClass, method);
            if (fieldInfo2.ignore) {
                return;
            }
            else {
                methodName = method.getName();
                if (fieldInfo2.fieldName == null || fieldInfo2.fieldName.isEmpty()) {
                    methodNameLength = methodName.length();
                    prefixNotMach = (methodNameLength <= prefix2.length() || !methodName.startsWith(prefix2));
                    if (prefixNotMach) {
                        if (method.getDeclaringClass() != Object.class && method.getReturnType() == clazz && (method.getAnnotation(JSONField.class) != null || (beanInfo.readerFeatures & JSONReader.Feature.SupportSmartMatch.mask) != 0x0L)) {
                            fieldName = methodName;
                        }
                        else {
                            return;
                        }
                    }
                    else {
                        fieldName = BeanUtils.setterName(methodName, prefixLength);
                    }
                }
                else {
                    fieldName = fieldInfo2.fieldName;
                }
                if (method.getParameterCount() == 0) {
                    fieldReader = this.createFieldReaderMethod(clazz, clazz, fieldName, fieldInfo2.ordinal, fieldInfo2.features, fieldInfo2.format, fieldInfo2.locale, fieldInfo2.defaultValue, fieldInfo2.schema, method.getGenericReturnType(), method.getReturnType(), method, null);
                    origin = map.putIfAbsent(fieldName, fieldReader);
                    if (origin != null && origin.compareTo(fieldReader) > 0) {
                        map.put(fieldName, fieldReader);
                    }
                    return;
                }
                else {
                    fieldType = method.getGenericParameterTypes()[0];
                    fieldClass = method.getParameterTypes()[0];
                    method.setAccessible(true);
                    fieldReader2 = this.createFieldReaderMethod(clazz, objectType, fieldName, fieldInfo2.ordinal, fieldInfo2.features, fieldInfo2.format, fieldInfo2.locale, fieldInfo2.defaultValue, fieldInfo2.schema, fieldType, fieldClass, method, null);
                    origin2 = map.putIfAbsent(fieldName, fieldReader2);
                    if (origin2 != null && origin2.compareTo(fieldReader2) > 0) {
                        map.put(fieldName, fieldReader2);
                    }
                    if (fieldInfo2.alternateNames != null) {
                        alternateNames = fieldInfo2.alternateNames;
                        for (length = alternateNames.length; i < length; ++i) {
                            alternateName = alternateNames[i];
                            if (!fieldName.equals(alternateName)) {
                                map.putIfAbsent(alternateName, this.createFieldReaderMethod(clazz, objectType, alternateName, fieldInfo2.ordinal, fieldInfo2.features, fieldInfo2.format, fieldInfo2.locale, fieldInfo2.defaultValue, fieldInfo2.schema, fieldType, fieldClass, method, null));
                            }
                        }
                    }
                    return;
                }
            }
        });
        final FieldReader[] fieldReaderArray = new FieldReader[fieldReaders.size()];
        fieldReaders.values().toArray(fieldReaderArray);
        Arrays.sort(fieldReaderArray);
        final Supplier instanceSupplier = this.createSupplier((Class<Object>)builderClass);
        return this.createObjectReader(builderClass, 0L, (Supplier<T>)instanceSupplier, builderFunction, fieldReaderArray);
    }
    
    protected <T> ObjectReader<T> createObjectReaderWithCreator(final Class<T> objectClass, final Type objectType, final ObjectReaderProvider provider, final BeanInfo beanInfo) {
        final FieldInfo fieldInfo = new FieldInfo();
        final Map<String, FieldReader> fieldReaders = new LinkedHashMap<String, FieldReader>();
        Class declaringClass = null;
        Parameter[] parameters;
        String[] paramNames;
        if (beanInfo.creatorConstructor != null) {
            parameters = beanInfo.creatorConstructor.getParameters();
            declaringClass = beanInfo.creatorConstructor.getDeclaringClass();
            paramNames = ASMUtils.lookupParameterNames(beanInfo.creatorConstructor);
        }
        else {
            parameters = beanInfo.createMethod.getParameters();
            declaringClass = beanInfo.createMethod.getDeclaringClass();
            paramNames = ASMUtils.lookupParameterNames(beanInfo.createMethod);
        }
        for (int i = 0; i < parameters.length; ++i) {
            fieldInfo.init();
            final Parameter parameter = parameters[i];
            if (beanInfo.creatorConstructor != null) {
                provider.getFieldInfo(fieldInfo, objectClass, beanInfo.creatorConstructor, i, parameter);
            }
            else {
                provider.getFieldInfo(fieldInfo, objectClass, beanInfo.createMethod, i, parameter);
            }
            if (parameters.length == 1 && (fieldInfo.features & 0x1000000000000L) != 0x0L) {
                break;
            }
            String fieldName = fieldInfo.fieldName;
            if (fieldName == null || fieldName.isEmpty()) {
                if (beanInfo.createParameterNames != null && i < beanInfo.createParameterNames.length) {
                    fieldName = beanInfo.createParameterNames[i];
                }
                if (fieldName == null || fieldName.isEmpty()) {
                    fieldName = parameter.getName();
                }
            }
            if (fieldName == null || fieldName.isEmpty()) {
                fieldName = paramNames[i];
            }
            else if (fieldName.startsWith("arg")) {
                if (paramNames != null && paramNames.length > i) {
                    fieldName = paramNames[i];
                }
            }
            else {
                paramNames[i] = fieldName;
            }
            final String finalFieldName = fieldName;
            final Class<?> paramClass = parameter.getType();
            final Class<?> clazz;
            FieldInfo methodFieldInfo;
            String methodFieldName;
            final String s;
            final FieldInfo fieldInfo2;
            BeanUtils.getters(objectClass, method -> {
                if (method.getReturnType() != clazz) {
                    return;
                }
                else {
                    methodFieldInfo = new FieldInfo();
                    provider.getFieldInfo(methodFieldInfo, objectClass, method);
                    methodFieldName = methodFieldInfo.fieldName;
                    if (methodFieldName == null) {
                        methodFieldName = BeanUtils.getterName(method, PropertyNamingStrategy.CamelCase.name());
                    }
                    if (methodFieldInfo.readUsing != null && s.equals(methodFieldName)) {
                        fieldInfo2.readUsing = methodFieldInfo.readUsing;
                    }
                    return;
                }
            });
            if (fieldName == null || fieldName.isEmpty()) {
                fieldName = "arg" + i;
            }
            final Type paramType = parameter.getParameterizedType();
            final ObjectReader initReader = getInitReader(provider, paramType, paramClass, fieldInfo);
            final FieldReader fieldReaderParam = this.createFieldReaderParam(objectClass, objectType, fieldName, i, fieldInfo.features, fieldInfo.format, paramType, paramClass, fieldName, declaringClass, parameter, null, initReader);
            fieldReaders.put(fieldName, fieldReaderParam);
            if (fieldInfo.alternateNames != null) {
                for (final String alternateName : fieldInfo.alternateNames) {
                    if (!fieldName.equals(alternateName)) {
                        fieldReaders.putIfAbsent(alternateName, this.createFieldReaderParam(objectClass, objectType, alternateName, i, fieldInfo.features, fieldInfo.format, paramType, paramClass, fieldName, declaringClass, parameter, null));
                    }
                }
            }
        }
        if (parameters.length == 1 && (fieldInfo.features & 0x1000000000000L) != 0x0L) {
            final Type valueType = (beanInfo.creatorConstructor == null) ? beanInfo.createMethod.getGenericParameterTypes()[0] : beanInfo.creatorConstructor.getGenericParameterTypes()[0];
            final Class valueClass = (beanInfo.creatorConstructor == null) ? beanInfo.createMethod.getParameterTypes()[0] : beanInfo.creatorConstructor.getParameterTypes()[0];
            JSONSchema jsonSchema = null;
            if (fieldInfo.schema != null && !fieldInfo.schema.isEmpty()) {
                final JSONObject object = JSON.parseObject(fieldInfo.schema);
                if (!object.isEmpty()) {
                    jsonSchema = JSONSchema.of(object, valueClass);
                }
            }
            Object defaultValue = fieldInfo.defaultValue;
            if (defaultValue != null && defaultValue.getClass() != valueClass) {
                final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(defaultValue.getClass(), valueType);
                if (typeConvert == null) {
                    throw new JSONException("illegal defaultValue : " + defaultValue + ", class " + valueClass.getName());
                }
                defaultValue = typeConvert.apply(defaultValue);
            }
            final boolean jit = ObjectReaderCreator.JIT || (fieldInfo.features & 0x40000000000000L) != 0x0L || (beanInfo.readerFeatures & 0x40000000000000L) != 0x0L;
            Function function = null;
            if (defaultValue == null && jit) {
                if (valueClass == Integer.TYPE) {
                    IntFunction intFunction = null;
                    if (beanInfo.creatorConstructor != null) {
                        intFunction = this.createIntFunction(beanInfo.creatorConstructor);
                    }
                    else if (beanInfo.createMethod != null) {
                        intFunction = this.createIntFunction(beanInfo.createMethod);
                    }
                    if (intFunction != null) {
                        return ObjectReaderImplValueInt.of(objectClass, fieldInfo.features, jsonSchema, intFunction);
                    }
                }
                else if (valueClass == String.class) {
                    if (beanInfo.creatorConstructor != null) {
                        function = this.createStringFunction(beanInfo.creatorConstructor);
                    }
                    else if (beanInfo.createMethod != null) {
                        function = this.createStringFunction(beanInfo.createMethod);
                    }
                    if (function != null) {
                        return ObjectReaderImplValueString.of(objectClass, fieldInfo.features, jsonSchema, function);
                    }
                }
            }
            if (jit && !valueClass.isPrimitive()) {
                if (beanInfo.creatorConstructor != null) {
                    function = this.createValueFunction((Constructor<Object>)beanInfo.creatorConstructor, (Class<Object>)valueClass);
                }
                else if (beanInfo.createMethod != null) {
                    function = this.createValueFunction(beanInfo.createMethod, valueClass);
                }
            }
            return new ObjectReaderImplValue<Object, T>(objectClass, valueType, valueClass, fieldInfo.features, fieldInfo.format, defaultValue, jsonSchema, beanInfo.creatorConstructor, beanInfo.createMethod, function);
        }
        Function<Map<Long, Object>, Object> function2;
        if (beanInfo.creatorConstructor != null) {
            function2 = this.createFunction(beanInfo.creatorConstructor, beanInfo.markerConstructor, paramNames);
        }
        else {
            function2 = this.createFactoryFunction(beanInfo.createMethod, paramNames);
        }
        final FieldReader[] fieldReaderArray = new FieldReader[fieldReaders.size()];
        fieldReaders.values().toArray(fieldReaderArray);
        Arrays.sort(fieldReaderArray);
        FieldReader[] setterFieldReaders = this.createFieldReaders(objectClass, objectType);
        Arrays.sort(setterFieldReaders);
        boolean[] flags = null;
        int maskCount = 0;
        for (int j = 0; j < setterFieldReaders.length; ++j) {
            final FieldReader setterFieldReader = setterFieldReaders[j];
            if (fieldReaders.containsKey(setterFieldReader.fieldName)) {
                if (flags == null) {
                    flags = new boolean[setterFieldReaders.length];
                }
                flags[j] = true;
                ++maskCount;
            }
        }
        if (maskCount > 0) {
            final FieldReader[] array = new FieldReader[setterFieldReaders.length - maskCount];
            int index = 0;
            for (int k = 0; k < setterFieldReaders.length; ++k) {
                if (!flags[k]) {
                    array[index++] = setterFieldReaders[k];
                }
            }
            setterFieldReaders = array;
        }
        return new ObjectReaderNoneDefaultConstructor<T>(objectClass, beanInfo.typeKey, beanInfo.typeName, beanInfo.readerFeatures, (Function<Map<Long, Object>, T>)function2, null, paramNames, fieldReaderArray, setterFieldReaders, beanInfo.seeAlso, beanInfo.seeAlsoNames);
    }
    
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectClass, final long features, final Supplier<T> defaultCreator, final Function buildFunction, final FieldReader... fieldReaders) {
        return this.createObjectReader(objectClass, null, features, null, defaultCreator, buildFunction, fieldReaders);
    }
    
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectClass, final String typeKey, final long features, final JSONSchema schema, final Supplier<T> defaultCreator, final Function buildFunction, final FieldReader... fieldReaders) {
        if (objectClass != null) {
            final int modifiers = objectClass.getModifiers();
            if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
                return new ObjectReaderAdapter<T>(objectClass, typeKey, null, features, schema, defaultCreator, buildFunction, fieldReaders);
            }
        }
        switch (fieldReaders.length) {
            case 1: {
                return new ObjectReader1<T>(objectClass, features, schema, defaultCreator, buildFunction, fieldReaders[0]);
            }
            case 2: {
                return new ObjectReader2<T>(objectClass, features, schema, defaultCreator, buildFunction, fieldReaders[0], fieldReaders[1]);
            }
            case 3: {
                return new ObjectReader3<T>(objectClass, defaultCreator, features, schema, buildFunction, fieldReaders[0], fieldReaders[1], fieldReaders[2]);
            }
            case 4: {
                return new ObjectReader4<T>(objectClass, features, schema, defaultCreator, buildFunction, fieldReaders[0], fieldReaders[1], fieldReaders[2], fieldReaders[3]);
            }
            case 5: {
                return new ObjectReader5<T>(objectClass, defaultCreator, features, schema, buildFunction, fieldReaders[0], fieldReaders[1], fieldReaders[2], fieldReaders[3], fieldReaders[4]);
            }
            case 6: {
                return new ObjectReader6<T>(objectClass, defaultCreator, features, schema, buildFunction, fieldReaders[0], fieldReaders[1], fieldReaders[2], fieldReaders[3], fieldReaders[4], fieldReaders[5]);
            }
            default: {
                return new ObjectReaderAdapter<T>(objectClass, typeKey, null, features, schema, defaultCreator, buildFunction, fieldReaders);
            }
        }
    }
    
    public <T> ObjectReader<T> createObjectReader(final Type objectType) {
        if (objectType instanceof Class) {
            return this.createObjectReader((Class<T>)objectType);
        }
        final Class<T> objectClass = (Class<T>)TypeUtils.getMapping(objectType);
        final FieldReader[] fieldReaderArray = this.createFieldReaders(objectClass, objectType);
        return this.createObjectReader(objectClass, (Supplier<T>)this.createSupplier((Class<T>)objectClass), fieldReaderArray);
    }
    
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectType) {
        return this.createObjectReader(objectType, objectType, false, JSONFactory.getDefaultObjectReaderProvider());
    }
    
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectType, final boolean fieldBased) {
        return this.createObjectReader(objectType, objectType, fieldBased, JSONFactory.getDefaultObjectReaderProvider());
    }
    
    public <T> ObjectReader<T> createObjectReader(final Class<T> objectClass, final Type objectType, boolean fieldBased, final ObjectReaderProvider provider) {
        final BeanInfo beanInfo = new BeanInfo();
        if (fieldBased) {
            final BeanInfo beanInfo2 = beanInfo;
            beanInfo2.readerFeatures |= JSONReader.Feature.FieldBased.mask;
        }
        for (final ObjectReaderModule module : provider.modules) {
            final ObjectReaderAnnotationProcessor annotationProcessor = module.getAnnotationProcessor();
            if (annotationProcessor != null) {
                annotationProcessor.getBeanInfo(beanInfo, objectClass);
            }
        }
        if (beanInfo.deserializer != null && ObjectReader.class.isAssignableFrom(beanInfo.deserializer)) {
            try {
                final Constructor constructor = beanInfo.deserializer.getDeclaredConstructor((Class[])new Class[0]);
                constructor.setAccessible(true);
                return constructor.newInstance(new Object[0]);
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("create deserializer error", e);
            }
        }
        if (fieldBased) {
            final BeanInfo beanInfo3 = beanInfo;
            beanInfo3.readerFeatures |= JSONReader.Feature.FieldBased.mask;
        }
        if (Enum.class.isAssignableFrom(objectClass) && (beanInfo.createMethod == null || beanInfo.createMethod.getParameterCount() == 1)) {
            return (ObjectReader<T>)this.createEnumReader(objectClass, beanInfo.createMethod, provider);
        }
        if (Throwable.class.isAssignableFrom(objectClass)) {
            fieldBased = false;
            final BeanInfo beanInfo4 = beanInfo;
            beanInfo4.readerFeatures |= JSONReader.Feature.IgnoreSetNullValue.mask;
        }
        if (fieldBased && objectClass.isInterface()) {
            fieldBased = false;
        }
        final FieldReader[] fieldReaderArray = this.createFieldReaders(objectClass, objectType, beanInfo, fieldBased, provider);
        boolean allReadOnlyOrZero = true;
        for (int i = 0; i < fieldReaderArray.length; ++i) {
            final FieldReader fieldReader = fieldReaderArray[i];
            if (!fieldReader.isReadOnly()) {
                allReadOnlyOrZero = false;
                break;
            }
        }
        if (beanInfo.creatorConstructor != null || beanInfo.createMethod != null) {
            return this.createObjectReaderWithCreator(objectClass, objectType, provider, beanInfo);
        }
        if (beanInfo.builder != null) {
            return this.createObjectReaderWithBuilder(objectClass, objectType, provider, beanInfo);
        }
        Constructor creatorConstructor = null;
        final ArrayList<Constructor> obj;
        final List<Constructor> alternateConstructors = (List<Constructor>)(obj = new ArrayList<Constructor>());
        Objects.requireNonNull((List<Constructor>)obj);
        BeanUtils.constructor(objectClass, (Consumer<Constructor>)obj::add);
        if (Throwable.class.isAssignableFrom(objectClass)) {
            return new ObjectReaderException<T>(objectClass, alternateConstructors, fieldReaderArray);
        }
        Constructor defaultConstructor = null;
        final Class<?> declaringClass = objectClass.getDeclaringClass();
        int index = -1;
        for (int j = 0; j < alternateConstructors.size(); ++j) {
            final Constructor constructor2 = alternateConstructors.get(j);
            if (constructor2.getParameterCount() == 0) {
                defaultConstructor = constructor2;
            }
            if (declaringClass != null && constructor2.getParameterCount() == 1 && declaringClass.equals(constructor2.getParameterTypes()[0])) {
                creatorConstructor = constructor2;
                index = j;
                break;
            }
            if (creatorConstructor == null) {
                creatorConstructor = constructor2;
                index = j;
            }
            else if (constructor2.getParameterCount() == 0) {
                creatorConstructor = constructor2;
                index = j;
            }
            else if (creatorConstructor.getParameterCount() < constructor2.getParameterCount()) {
                creatorConstructor = constructor2;
                index = j;
            }
        }
        if (index != -1) {
            alternateConstructors.remove(index);
        }
        if (creatorConstructor != null && creatorConstructor.getParameterCount() != 0 && beanInfo.seeAlso == null) {
            final boolean record = BeanUtils.isRecord(objectClass);
            creatorConstructor.setAccessible(true);
            String[] parameterNames = beanInfo.createParameterNames;
            if (record && parameterNames == null) {
                parameterNames = BeanUtils.getRecordFieldNames(objectClass);
            }
            if (parameterNames == null || parameterNames.length == 0) {
                parameterNames = ASMUtils.lookupParameterNames(creatorConstructor);
                final Parameter[] parameters = creatorConstructor.getParameters();
                final FieldInfo fieldInfo = new FieldInfo();
                for (int k = 0; k < parameters.length && k < parameterNames.length; ++k) {
                    fieldInfo.init();
                    final Parameter parameter = parameters[k];
                    provider.getFieldInfo(fieldInfo, objectClass, creatorConstructor, k, parameter);
                    if (fieldInfo.fieldName != null) {
                        parameterNames[k] = fieldInfo.fieldName;
                    }
                }
            }
            int matchCount = 0;
            if (defaultConstructor != null) {
                for (int l = 0; l < parameterNames.length; ++l) {
                    final String parameterName = parameterNames[l];
                    if (parameterName != null) {
                        for (int m = 0; m < fieldReaderArray.length; ++m) {
                            final FieldReader fieldReader2 = fieldReaderArray[m];
                            if (fieldReader2 != null && parameterName.equals(fieldReader2.fieldName)) {
                                ++matchCount;
                                break;
                            }
                        }
                    }
                }
            }
            if (!fieldBased && !Throwable.class.isAssignableFrom(objectClass) && defaultConstructor == null && matchCount != parameterNames.length) {
                if (creatorConstructor.getParameterCount() == 1) {
                    final FieldInfo fieldInfo = new FieldInfo();
                    provider.getFieldInfo(fieldInfo, objectClass, creatorConstructor, 0, creatorConstructor.getParameters()[0]);
                    if ((fieldInfo.features & 0x1000000000000L) != 0x0L) {
                        final Type valueType = creatorConstructor.getGenericParameterTypes()[0];
                        final Class valueClass = creatorConstructor.getParameterTypes()[0];
                        JSONSchema jsonSchema = null;
                        if (fieldInfo.schema != null && !fieldInfo.schema.isEmpty()) {
                            final JSONObject object = JSON.parseObject(fieldInfo.schema);
                            if (!object.isEmpty()) {
                                jsonSchema = JSONSchema.of(object, valueClass);
                            }
                        }
                        Object defaultValue = fieldInfo.defaultValue;
                        if (defaultValue != null && defaultValue.getClass() != valueClass) {
                            final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(defaultValue.getClass(), valueType);
                            if (typeConvert == null) {
                                throw new JSONException("illegal defaultValue : " + defaultValue + ", class " + valueClass.getName());
                            }
                            defaultValue = typeConvert.apply(defaultValue);
                        }
                        return new ObjectReaderImplValue<Object, T>(objectClass, valueType, valueClass, fieldInfo.features, fieldInfo.format, defaultValue, jsonSchema, creatorConstructor, null, null);
                    }
                }
                if (allReadOnlyOrZero && fieldReaderArray.length != 0 && alternateConstructors.isEmpty()) {
                    for (int l = 0; l < parameterNames.length; ++l) {
                        final String paramName = parameterNames[l];
                        for (final FieldReader fieldReader3 : fieldReaderArray) {
                            if (fieldReader3.field != null && fieldReader3.field.getName().equals(paramName) && !fieldReader3.fieldName.equals(paramName)) {
                                parameterNames[l] = fieldReader3.fieldName;
                                break;
                            }
                        }
                    }
                }
                Function function = null;
                BiFunction biFunction = null;
                if (ObjectReaderCreator.JIT) {
                    if (creatorConstructor.getParameterCount() == 1) {
                        function = LambdaMiscCodec.createFunction(creatorConstructor);
                    }
                    else if (creatorConstructor.getParameterCount() == 2) {
                        biFunction = LambdaMiscCodec.createBiFunction(creatorConstructor);
                    }
                }
                final Function<Map<Long, Object>, T> constructorFunction = new ConstructorFunction<T>(alternateConstructors, creatorConstructor, function, biFunction, null, parameterNames);
                final FieldReader[] paramFieldReaders = this.createFieldReaders(provider, objectClass, objectType, creatorConstructor, creatorConstructor.getParameters(), parameterNames);
                return new ObjectReaderNoneDefaultConstructor<T>(objectClass, beanInfo.typeKey, beanInfo.typeName, beanInfo.readerFeatures, constructorFunction, alternateConstructors, parameterNames, paramFieldReaders, fieldReaderArray, null, null);
            }
        }
        if (beanInfo.seeAlso != null && beanInfo.seeAlso.length != 0) {
            return this.createObjectReaderSeeAlso(objectClass, beanInfo.typeKey, beanInfo.seeAlso, beanInfo.seeAlsoNames, beanInfo.seeAlsoDefault, fieldReaderArray);
        }
        if (objectClass.isInterface()) {
            return new ObjectReaderInterface<T>(objectClass, null, null, 0L, null, null, fieldReaderArray);
        }
        final Supplier<T> creator = this.createSupplier(objectClass);
        final JSONSchema jsonSchema2 = JSONSchema.of(JSON.parseObject(beanInfo.schema), objectClass);
        final ObjectReader<T> objectReader = this.createObjectReader(objectClass, beanInfo.typeKey, beanInfo.readerFeatures, jsonSchema2, creator, null, fieldReaderArray);
        if (objectReader instanceof ObjectReaderBean) {
            JSONReader.AutoTypeBeforeHandler beforeHandler = null;
            if (beanInfo.autoTypeBeforeHandler != null) {
                try {
                    final Constructor constructor3 = beanInfo.autoTypeBeforeHandler.getDeclaredConstructor((Class<?>[])new Class[0]);
                    constructor3.setAccessible(true);
                    beforeHandler = constructor3.newInstance(new Object[0]);
                }
                catch (Exception ex3) {}
            }
            if (beforeHandler != null) {
                ((ObjectReaderBean)objectReader).setAutoTypeBeforeHandler(beforeHandler);
            }
        }
        return objectReader;
    }
    
    public <T> FieldReader[] createFieldReaders(final Class<T> objectClass) {
        return this.createFieldReaders(objectClass, objectClass, null, false, JSONFactory.getDefaultObjectReaderProvider());
    }
    
    public <T> FieldReader[] createFieldReaders(final Class<T> objectClass, final Type objectType) {
        return this.createFieldReaders(objectClass, objectType, null, false, JSONFactory.getDefaultObjectReaderProvider());
    }
    
    protected void createFieldReader(final Class objectClass, final Type objectType, final String namingStrategy, final FieldInfo fieldInfo, final Field field, final Map<String, FieldReader> fieldReaders, final ObjectReaderProvider provider) {
        provider.getFieldInfo(fieldInfo, objectClass, field);
        if (fieldInfo.ignore) {
            final boolean unwrap = (fieldInfo.features & 0x2000000000000L) != 0x0L && Map.class.isAssignableFrom(field.getType());
            if (!unwrap) {
                return;
            }
        }
        String fieldName;
        if (fieldInfo.fieldName == null || fieldInfo.fieldName.isEmpty()) {
            fieldName = field.getName();
            if (namingStrategy != null) {
                fieldName = BeanUtils.fieldName(fieldName, namingStrategy);
            }
        }
        else {
            fieldName = fieldInfo.fieldName;
        }
        final Type fieldType = field.getGenericType();
        final Class<?> fieldClass = field.getType();
        final ObjectReader initReader = getInitReader(provider, fieldType, fieldClass, fieldInfo);
        String schema = fieldInfo.schema;
        if (fieldInfo.required && schema == null) {
            schema = "{\"required\":true}";
        }
        final FieldReader<Object> fieldReader = this.createFieldReader(objectClass, objectType, fieldName, fieldInfo.ordinal, fieldInfo.features, fieldInfo.format, fieldInfo.locale, fieldInfo.defaultValue, schema, fieldType, fieldClass, field, initReader);
        final FieldReader previous = fieldReaders.putIfAbsent(fieldName, fieldReader);
        if (previous != null) {
            final int cmp = fieldReader.compareTo((FieldReader<Object>)previous);
            if (cmp > 0) {
                fieldReaders.put(fieldName, fieldReader);
            }
        }
        if (fieldInfo.alternateNames != null) {
            for (final String alternateName : fieldInfo.alternateNames) {
                if (!fieldName.equals(alternateName)) {
                    final FieldReader<Object> fieldReader2 = this.createFieldReader(objectClass, objectType, alternateName, fieldInfo.ordinal, fieldInfo.features, null, fieldInfo.locale, fieldInfo.defaultValue, schema, fieldType, fieldClass, field, null);
                    fieldReaders.putIfAbsent(alternateName, fieldReader2);
                }
            }
        }
    }
    
    protected void createFieldReader(final Class objectClass, final Type objectType, final String namingStrategy, final String[] orders, final BeanInfo beanInfo, final FieldInfo fieldInfo, final Method method, final Map<String, FieldReader> fieldReaders, final ObjectReaderProvider provider) {
        provider.getFieldInfo(fieldInfo, objectClass, method);
        if (fieldInfo.ignore) {
            return;
        }
        String fieldName;
        if (fieldInfo.fieldName == null || fieldInfo.fieldName.isEmpty()) {
            final String methodName = method.getName();
            if (methodName.startsWith("set")) {
                fieldName = BeanUtils.setterName(methodName, namingStrategy);
            }
            else {
                fieldName = BeanUtils.getterName(method, namingStrategy);
            }
            char c0 = '\0';
            final int len = fieldName.length();
            if (len > 0) {
                c0 = fieldName.charAt(0);
            }
            final char c2;
            if ((len == 1 && c0 >= 'a' && c0 <= 'z') || (len > 2 && c0 >= 'A' && c0 <= 'Z' && (c2 = fieldName.charAt(1)) >= 'A' && c2 <= 'Z')) {
                final char[] chars = fieldName.toCharArray();
                if (len == 1) {
                    chars[0] -= ' ';
                }
                else {
                    chars[0] += ' ';
                }
                final String fieldName2 = new String(chars);
                final Field field = BeanUtils.getDeclaredField(objectClass, fieldName2);
                if (field != null) {
                    if (Modifier.isPublic(field.getModifiers())) {
                        fieldName = field.getName();
                    }
                    else if (len == 1) {
                        fieldInfo.alternateNames = new String[] { fieldName };
                        fieldName = field.getName();
                    }
                }
            }
        }
        else {
            fieldName = fieldInfo.fieldName;
        }
        if (orders != null && orders.length > 0) {
            boolean match = false;
            for (int i = 0; i < orders.length; ++i) {
                if (fieldName.equals(orders[i])) {
                    fieldInfo.ordinal = i;
                    match = true;
                    break;
                }
            }
            if (!match && fieldInfo.ordinal == 0) {
                fieldInfo.ordinal = orders.length;
            }
        }
        final int parameterCount = method.getParameterCount();
        if (parameterCount == 0) {
            final FieldReader fieldReader = this.createFieldReaderMethod((Class<Object>)objectClass, objectType, fieldName, fieldInfo.ordinal, fieldInfo.features, fieldInfo.format, fieldInfo.locale, fieldInfo.defaultValue, fieldInfo.schema, method.getGenericReturnType(), method.getReturnType(), method, fieldInfo.getInitReader());
            final FieldReader origin = fieldReaders.putIfAbsent(fieldName, fieldReader);
            if (origin != null && origin.compareTo(fieldReader) > 0) {
                fieldReaders.put(fieldName, fieldReader);
            }
            return;
        }
        if (parameterCount == 2) {
            final Class<?> fieldClass = method.getParameterTypes()[1];
            final Type fieldType = method.getGenericParameterTypes()[1];
            method.setAccessible(true);
            final FieldReaderAnySetter anySetter = new FieldReaderAnySetter(fieldType, fieldClass, fieldInfo.ordinal, fieldInfo.features, fieldInfo.format, null, method);
            fieldReaders.put(anySetter.fieldName, anySetter);
            return;
        }
        final Type fieldType2 = method.getGenericParameterTypes()[0];
        final Class fieldClass2 = method.getParameterTypes()[0];
        if (TypeUtils.isFunction(fieldClass2)) {
            return;
        }
        final ObjectReader initReader = getInitReader(provider, fieldType2, fieldClass2, fieldInfo);
        FieldReader fieldReader2 = null;
        final boolean jit = (fieldInfo.features & 0x40000000000000L) != 0x0L;
        if (jit) {
            try {
                fieldReader2 = this.createFieldReaderLambda((Class<Object>)objectClass, objectType, fieldName, fieldInfo.ordinal, fieldInfo.features, fieldInfo.format, fieldInfo.locale, fieldInfo.defaultValue, fieldInfo.schema, fieldType2, fieldClass2, method, initReader);
            }
            catch (Throwable ignored) {
                this.jitErrorCount.incrementAndGet();
                this.jitErrorLast = ignored;
            }
        }
        if (fieldReader2 == null) {
            fieldReader2 = this.createFieldReaderMethod((Class<Object>)objectClass, objectType, fieldName, fieldInfo.ordinal, fieldInfo.features, fieldInfo.format, fieldInfo.locale, fieldInfo.defaultValue, fieldInfo.schema, fieldType2, fieldClass2, method, initReader);
        }
        final FieldReader origin2 = fieldReaders.putIfAbsent(fieldName, fieldReader2);
        if (origin2 != null && origin2.compareTo(fieldReader2) > 0) {
            fieldReaders.put(fieldName, fieldReader2);
        }
        if (fieldInfo.alternateNames != null) {
            for (final String alternateName : fieldInfo.alternateNames) {
                if (!fieldName.equals(alternateName)) {
                    fieldReaders.putIfAbsent(alternateName, this.createFieldReaderMethod((Class<Object>)objectClass, objectType, alternateName, fieldInfo.ordinal, fieldInfo.features, fieldInfo.format, fieldInfo.locale, fieldInfo.defaultValue, fieldInfo.schema, fieldType2, fieldClass2, method, initReader));
                }
            }
        }
    }
    
    protected <T> FieldReader[] createFieldReaders(final Class<T> objectClass, final Type objectType, BeanInfo beanInfo, final boolean fieldBased, final ObjectReaderProvider provider) {
        if (beanInfo == null) {
            beanInfo = new BeanInfo();
            for (final ObjectReaderModule module : provider.modules) {
                final ObjectReaderAnnotationProcessor annotationProcessor = module.getAnnotationProcessor();
                if (annotationProcessor != null) {
                    annotationProcessor.getBeanInfo(beanInfo, objectClass);
                }
            }
        }
        final boolean recoard = BeanUtils.isRecord(objectClass);
        final String namingStrategy = beanInfo.namingStrategy;
        final Map<String, FieldReader> fieldReaders = new LinkedHashMap<String, FieldReader>();
        final BeanInfo finalBeanInfo = beanInfo;
        final long beanFeatures = beanInfo.readerFeatures;
        final String beanFormat = beanInfo.format;
        final FieldInfo fieldInfo = new FieldInfo();
        final String[] orders = beanInfo.orders;
        if (fieldBased) {
            final FieldInfo fieldInfo2;
            final long n;
            final String format;
            final String namingStrategy2;
            final Map<String, FieldReader> fieldReaders2;
            BeanUtils.declaredFields(objectClass, field -> {
                fieldInfo2.init();
                fieldInfo2.features |= JSONReader.Feature.FieldBased.mask;
                fieldInfo2.features |= n;
                fieldInfo2.format = format;
                this.createFieldReader(objectClass, objectType, namingStrategy2, fieldInfo2, field, fieldReaders2, provider);
                return;
            });
        }
        else {
            if (!recoard) {
                final FieldInfo fieldInfo3;
                final long n2;
                final String format2;
                final String namingStrategy3;
                final Map<String, FieldReader> fieldReaders3;
                String fieldName;
                final BeanInfo beanInfo2;
                BeanUtils.declaredFields(objectClass, field -> {
                    fieldInfo3.init();
                    fieldInfo3.ignore = ((field.getModifiers() & 0x1) == 0x0);
                    fieldInfo3.features |= n2;
                    fieldInfo3.format = format2;
                    this.createFieldReader(objectClass, objectType, namingStrategy3, fieldInfo3, field, fieldReaders3, provider);
                    if (fieldInfo3.required) {
                        fieldName = fieldInfo3.fieldName;
                        if (fieldName == null || fieldName.isEmpty()) {
                            fieldName = field.getName();
                        }
                        beanInfo2.required(fieldName);
                    }
                    return;
                });
            }
            final Class mixIn = provider.getMixIn(objectClass);
            final FieldInfo fieldInfo4;
            final long n3;
            final String format3;
            final String namingStrategy4;
            final String[] orders2;
            final BeanInfo beanInfo3;
            final Map<String, FieldReader> fieldReaders4;
            BeanUtils.setters(objectClass, mixIn, method -> {
                fieldInfo4.init();
                fieldInfo4.features |= n3;
                fieldInfo4.format = format3;
                this.createFieldReader(objectClass, objectType, namingStrategy4, orders2, beanInfo3, fieldInfo4, method, fieldReaders4, provider);
                return;
            });
            if (objectClass.isInterface()) {
                final FieldInfo fieldInfo5;
                final long n4;
                final String namingStrategy5;
                final String[] orders3;
                final BeanInfo beanInfo4;
                final Map<String, FieldReader> fieldReaders5;
                BeanUtils.getters(objectClass, method -> {
                    fieldInfo5.init();
                    fieldInfo5.features |= n4;
                    this.createFieldReader(objectClass, objectType, namingStrategy5, orders3, beanInfo4, fieldInfo5, method, fieldReaders5, provider);
                    return;
                });
            }
        }
        final Class<? super T> superclass = objectClass.getSuperclass();
        if (BeanUtils.isExtendedMap(objectClass)) {
            final Type superType = objectClass.getGenericSuperclass();
            final Map thisMap;
            final Map superMap;
            final Iterator<Object> iterator2;
            Object value;
            Map.Entry entry;
            final FieldReader fieldReader = ObjectReaders.fieldReader("$super$", superType, superclass, (o, f) -> {
                thisMap = o;
                superMap = f;
                superMap.entrySet().iterator();
                while (iterator2.hasNext()) {
                    value = iterator2.next();
                    entry = (Map.Entry)value;
                    thisMap.put(entry.getKey(), entry.getValue());
                }
                return;
            });
            fieldReaders.put("$super$", fieldReader);
        }
        final FieldReader[] fieldReaderArray = new FieldReader[fieldReaders.size()];
        fieldReaders.values().toArray(fieldReaderArray);
        Arrays.sort(fieldReaderArray);
        return fieldReaderArray;
    }
    
    public <T> Supplier<T> createSupplier(final Class<T> objectClass) {
        if (objectClass.isInterface()) {
            return null;
        }
        final int modifiers = objectClass.getModifiers();
        if (Modifier.isAbstract(modifiers)) {
            return null;
        }
        Constructor<T> constructor;
        try {
            constructor = objectClass.getDeclaredConstructor((Class<?>[])new Class[0]);
            constructor.setAccessible(true);
        }
        catch (NoSuchMethodException ignored) {
            return null;
        }
        catch (Throwable e) {
            throw new JSONException("get constructor error, class " + objectClass.getName(), e);
        }
        return this.createSupplier(constructor, true);
    }
    
    public <T> Supplier<T> createSupplier(final Constructor constructor, boolean jit) {
        jit &= ObjectReaderCreator.JIT;
        if (jit) {
            final Class declaringClass = constructor.getDeclaringClass();
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
            try {
                if (constructor.getParameterCount() == 0) {
                    final MethodHandle handle = lookup.findConstructor(declaringClass, TypeUtils.METHOD_TYPE_VOID);
                    final CallSite callSite = LambdaMetafactory.metafactory(lookup, "get", TypeUtils.METHOD_TYPE_SUPPLIER, TypeUtils.METHOD_TYPE_OBJECT, handle, TypeUtils.METHOD_TYPE_OBJECT);
                    return (Supplier<T>)callSite.getTarget().invokeExact();
                }
            }
            catch (Throwable e) {
                this.jitErrorCount.incrementAndGet();
                this.jitErrorLast = e;
            }
        }
        return (Supplier<T>)new ConstructorSupplier(constructor);
    }
    
    protected <T> IntFunction<T> createIntFunction(final Constructor constructor) {
        final Class declaringClass = constructor.getDeclaringClass();
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
        try {
            final MethodHandle handle = lookup.findConstructor(declaringClass, TypeUtils.METHOD_TYPE_VOID_INT);
            final MethodType instantiatedMethodType = MethodType.methodType(declaringClass, Integer.TYPE);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_INT_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_INT, handle, instantiatedMethodType);
            return (IntFunction<T>)callSite.getTarget().invokeExact();
        }
        catch (Throwable e) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = e;
            return null;
        }
    }
    
    protected <T> IntFunction<T> createIntFunction(final Method factoryMethod) {
        final Class declaringClass = factoryMethod.getDeclaringClass();
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
        try {
            final MethodType methodType = MethodType.methodType(factoryMethod.getReturnType(), Integer.TYPE);
            final MethodHandle handle = lookup.findStatic(declaringClass, factoryMethod.getName(), methodType);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_INT_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_INT, handle, methodType);
            return (IntFunction<T>)callSite.getTarget().invokeExact();
        }
        catch (Throwable e) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = e;
            return null;
        }
    }
    
    protected <T> Function<String, T> createStringFunction(final Constructor constructor) {
        final Class declaringClass = constructor.getDeclaringClass();
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
        try {
            final MethodHandle handle = lookup.findConstructor(declaringClass, TypeUtils.METHOD_TYPE_VOID_STRING);
            final MethodType instantiatedMethodType = MethodType.methodType(declaringClass, String.class);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, handle, instantiatedMethodType);
            return (Function<String, T>)callSite.getTarget().invokeExact();
        }
        catch (Throwable e) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = e;
            return null;
        }
    }
    
    protected <T> Function<String, T> createStringFunction(final Method factoryMethod) {
        final Class declaringClass = factoryMethod.getDeclaringClass();
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
        try {
            final MethodType methodType = MethodType.methodType(factoryMethod.getReturnType(), String.class);
            final MethodHandle handle = lookup.findStatic(declaringClass, factoryMethod.getName(), methodType);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, handle, methodType);
            return (Function<String, T>)callSite.getTarget().invokeExact();
        }
        catch (Throwable e) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = e;
            return null;
        }
    }
    
    protected <I, T> Function<I, T> createValueFunction(final Constructor<T> constructor, final Class<I> valueClass) {
        final Class declaringClass = constructor.getDeclaringClass();
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
        try {
            final MethodType methodType = MethodType.methodType(Void.TYPE, valueClass);
            final MethodHandle handle = lookup.findConstructor(declaringClass, methodType);
            final MethodType instantiatedMethodType = MethodType.methodType(declaringClass, valueClass);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, handle, instantiatedMethodType);
            return (Function<I, T>)callSite.getTarget().invokeExact();
        }
        catch (Throwable e) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = e;
            return null;
        }
    }
    
    protected <I, T> Function<I, T> createValueFunction(final Method factoryMethod, final Class valueClass) {
        final Class declaringClass = factoryMethod.getDeclaringClass();
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
        try {
            final MethodType methodType = MethodType.methodType(factoryMethod.getReturnType(), valueClass);
            final MethodHandle handle = lookup.findStatic(declaringClass, factoryMethod.getName(), methodType);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, handle, methodType);
            return (Function<I, T>)callSite.getTarget().invokeExact();
        }
        catch (Throwable e) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = e;
            return null;
        }
    }
    
    public <T, R> Function<T, R> createBuildFunction(final Method builderMethod) {
        try {
            return (Function<T, R>)this.createBuildFunctionLambda(builderMethod);
        }
        catch (Throwable e) {
            this.jitErrorCount.incrementAndGet();
            this.jitErrorLast = e;
            builderMethod.setAccessible(true);
            return (Function<T, R>)(o -> {
                try {
                    return builderMethod.invoke(o, new Object[0]);
                }
                catch (Throwable e2) {
                    throw new JSONException("create instance error", e2);
                }
            });
        }
    }
    
     <T, R> Function<T, R> createBuildFunctionLambda(final Method builderMethod) {
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(builderMethod.getDeclaringClass());
        try {
            final MethodHandle target = lookup.findVirtual(builderMethod.getDeclaringClass(), builderMethod.getName(), MethodType.methodType(builderMethod.getReturnType()));
            final MethodType func = target.type();
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, func.erase(), target, func);
            final Object object = callSite.getTarget().invoke();
            return (Function<T, R>)object;
        }
        catch (Throwable e) {
            throw new JSONException("create fieldReader error", e);
        }
    }
    
    public <T> FieldReader createFieldReader(final Class<T> objectType, final String fieldName, final Type fieldType, final Class fieldClass, final Method method) {
        return this.createFieldReaderMethod(objectType, objectType, fieldName, 0, 0L, null, null, null, null, fieldType, fieldClass, method, null);
    }
    
    public <T> FieldReader createFieldReader(final Class<T> objectType, final String fieldName, final String format, final Type fieldType, final Class fieldClass, final Method method) {
        return this.createFieldReaderMethod((Class<Object>)objectType, fieldName, format, fieldType, fieldClass, method);
    }
    
    public <T> FieldReader createFieldReaderMethod(final Class<T> objectClass, final String fieldName, final String format, final Type fieldType, final Class fieldClass, final Method method) {
        return this.createFieldReaderMethod(objectClass, objectClass, fieldName, 0, 0L, format, null, null, null, fieldType, fieldClass, method, null);
    }
    
    public <T> FieldReader createFieldReaderParam(final Class<T> objectClass, final Type objectType, final String fieldName, final int ordinal, final long features, final String format, final Type fieldType, final Class fieldClass, final String paramName, final Class declaringClass, final Parameter parameter, final JSONSchema schema) {
        return this.createFieldReaderParam(objectClass, objectType, fieldName, ordinal, features, format, fieldType, fieldClass, paramName, declaringClass, parameter, schema, null);
    }
    
    public <T> FieldReader createFieldReaderParam(final Class<T> objectClass, final Type objectType, final String fieldName, final int ordinal, final long features, final String format, final Type fieldType, final Class fieldClass, final String paramName, final Class declaringClass, final Parameter parameter, final JSONSchema schema, final ObjectReader initReader) {
        if (initReader != null) {
            final FieldReaderObjectParam paramReader = new FieldReaderObjectParam(fieldName, fieldType, fieldClass, paramName, parameter, ordinal, features, format, schema);
            paramReader.initReader = initReader;
            return paramReader;
        }
        if (fieldType == Byte.TYPE || fieldType == Byte.class) {
            return new FieldReaderInt8Param(fieldName, fieldClass, paramName, parameter, ordinal, features, format, schema);
        }
        if (fieldType == Short.TYPE || fieldType == Short.class) {
            return new FieldReaderInt16Param(fieldName, fieldClass, paramName, parameter, ordinal, features, format, schema);
        }
        if (fieldType == Integer.TYPE || fieldType == Integer.class) {
            return new FieldReaderInt32Param(fieldName, fieldClass, paramName, parameter, ordinal, features, format, schema);
        }
        if (fieldType == Long.TYPE || fieldType == Long.class) {
            return new FieldReaderInt64Param(fieldName, fieldClass, paramName, parameter, ordinal, features, format, schema);
        }
        Type fieldTypeResolved = null;
        Class fieldClassResolved = null;
        if (!(fieldType instanceof Class) && objectType != null) {
            fieldTypeResolved = BeanUtils.getParamType(TypeReference.get(objectType), objectClass, declaringClass, parameter, fieldType);
            if (fieldTypeResolved != null) {
                fieldClassResolved = TypeUtils.getMapping(fieldTypeResolved);
            }
        }
        if (fieldTypeResolved == null) {
            fieldTypeResolved = fieldType;
        }
        if (fieldClassResolved == null) {
            fieldClassResolved = fieldClass;
        }
        return new FieldReaderObjectParam(fieldName, fieldTypeResolved, fieldClassResolved, paramName, parameter, ordinal, features, format, schema);
    }
    
    public <T> FieldReader createFieldReaderMethod(final Class<T> objectClass, final Type objectType, final String fieldName, final int ordinal, final long features, final String format, final Locale locale, Object defaultValue, final String schema, final Type fieldType, final Class fieldClass, final Method method, final ObjectReader initReader) {
        if (method != null) {
            method.setAccessible(true);
        }
        if (defaultValue != null && defaultValue.getClass() != fieldClass) {
            final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(defaultValue.getClass(), fieldType);
            if (typeConvert == null) {
                throw new JSONException("illegal defaultValue : " + defaultValue + ", class " + fieldClass.getName());
            }
            defaultValue = typeConvert.apply(defaultValue);
        }
        JSONSchema jsonSchema = null;
        if (schema != null && !schema.isEmpty()) {
            final JSONObject object = JSON.parseObject(schema);
            if (!object.isEmpty()) {
                jsonSchema = JSONSchema.of(object, fieldClass);
            }
        }
        if (initReader != null) {
            final FieldReaderObject fieldReaderObjectMethod = new FieldReaderObject(fieldName, fieldType, fieldClass, ordinal, features | 0x8000000000000L, format, locale, defaultValue, jsonSchema, method, null, null);
            fieldReaderObjectMethod.initReader = initReader;
            return fieldReaderObjectMethod;
        }
        if (fieldType == Boolean.TYPE) {
            return new FieldReaderBoolValueMethod(fieldName, ordinal, features, format, (Boolean)defaultValue, jsonSchema, method);
        }
        if (fieldType == Boolean.class) {
            return new FieldReaderBoolMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (Boolean)defaultValue, jsonSchema, method);
        }
        if (fieldType == Byte.TYPE) {
            return new FieldReaderInt8ValueMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (Byte)defaultValue, jsonSchema, method);
        }
        if (fieldType == Short.TYPE) {
            return new FieldReaderInt16ValueMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (Short)defaultValue, jsonSchema, method);
        }
        if (fieldType == Integer.TYPE) {
            return new FieldReaderInt32ValueMethod(fieldName, fieldType, fieldClass, ordinal, features, format, (Integer)defaultValue, jsonSchema, method);
        }
        if (fieldType == Long.TYPE) {
            return new FieldReaderInt64ValueMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (Long)defaultValue, jsonSchema, method);
        }
        if (fieldType == Float.TYPE) {
            return new FieldReaderFloatValueMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (Float)defaultValue, jsonSchema, method);
        }
        if (fieldType == Double.TYPE) {
            return new FieldReaderDoubleValueMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (Double)defaultValue, jsonSchema, method);
        }
        if (fieldType == Byte.class) {
            return new FieldReaderInt8Method(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (Byte)defaultValue, jsonSchema, method);
        }
        if (fieldType == Short.class) {
            return new FieldReaderInt16Method(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (Short)defaultValue, jsonSchema, method);
        }
        if (fieldType == Integer.class) {
            return new FieldReaderInt32Method(fieldName, ordinal, features, format, locale, (Integer)defaultValue, jsonSchema, method);
        }
        if (fieldType == Long.class) {
            return new FieldReaderInt64Method(fieldName, ordinal, features, format, locale, (Long)defaultValue, jsonSchema, method);
        }
        if (fieldType == Float.class) {
            return new FieldReaderFloatMethod(fieldName, ordinal, features, format, locale, (Float)defaultValue, jsonSchema, method);
        }
        if (fieldType == Double.class) {
            return new FieldReaderDoubleMethod(fieldName, ordinal, features, format, (Double)defaultValue, jsonSchema, method);
        }
        if (fieldClass == BigDecimal.class) {
            return new FieldReaderBigDecimalMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (BigDecimal)defaultValue, jsonSchema, method);
        }
        if (fieldClass == BigInteger.class) {
            return new FieldReaderBigIntegerMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (BigInteger)defaultValue, jsonSchema, method);
        }
        if (fieldType == String.class) {
            return new FieldReaderStringMethod(fieldName, fieldType, fieldClass, ordinal, features, format, locale, (String)defaultValue, jsonSchema, method);
        }
        if (fieldType == LocalDate.class) {
            return new FieldReaderLocalDate(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, method, null, null);
        }
        if (fieldType == OffsetDateTime.class) {
            return new FieldReaderOffsetDateTime(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, method, null, null);
        }
        if (fieldType == UUID.class) {
            return new FieldReaderUUID(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, method, null, null);
        }
        if (fieldType == String[].class) {
            return new FieldReaderStringArray(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, method, null, null);
        }
        if (method.getParameterCount() == 0) {
            if (fieldClass == AtomicInteger.class) {
                return new FieldReaderAtomicIntegerMethodReadOnly(fieldName, fieldClass, ordinal, jsonSchema, method);
            }
            if (fieldClass == AtomicLong.class) {
                return new FieldReaderAtomicLongReadOnly(fieldName, fieldClass, ordinal, jsonSchema, method);
            }
            if (fieldClass == AtomicIntegerArray.class) {
                return new FieldReaderAtomicIntegerArrayReadOnly(fieldName, fieldClass, ordinal, jsonSchema, method);
            }
            if (fieldClass == AtomicLongArray.class) {
                return new FieldReaderAtomicLongArrayReadOnly(fieldName, fieldClass, ordinal, jsonSchema, method);
            }
            if (fieldClass == AtomicBoolean.class) {
                return new FieldReaderAtomicBooleanMethodReadOnly(fieldName, fieldClass, ordinal, jsonSchema, method);
            }
            if (fieldClass == AtomicReference.class) {
                return new FieldReaderAtomicReferenceMethodReadOnly(fieldName, fieldType, fieldClass, ordinal, jsonSchema, method);
            }
            if (Collection.class.isAssignableFrom(fieldClass)) {
                Field field = null;
                final String methodName = method.getName();
                if (methodName.startsWith("get")) {
                    final String getterName = BeanUtils.getterName(methodName, PropertyNamingStrategy.CamelCase.name());
                    field = BeanUtils.getDeclaredField(method.getDeclaringClass(), getterName);
                }
                return new FieldReaderCollectionMethodReadOnly(fieldName, fieldType, fieldClass, ordinal, features, format, jsonSchema, method, field);
            }
            if (Map.class.isAssignableFrom(fieldClass)) {
                Field field = null;
                final String methodName = method.getName();
                if (methodName.startsWith("get")) {
                    final String getterName = BeanUtils.getterName(methodName, PropertyNamingStrategy.CamelCase.name());
                    field = BeanUtils.getDeclaredField(method.getDeclaringClass(), getterName);
                }
                return new FieldReaderMapMethodReadOnly(fieldName, fieldType, fieldClass, ordinal, features, format, jsonSchema, method, field);
            }
            if (!objectClass.isInterface()) {
                return null;
            }
        }
        Type fieldTypeResolved = null;
        Class fieldClassResolved = null;
        if (!(fieldType instanceof Class)) {
            fieldTypeResolved = BeanUtils.getFieldType(TypeReference.get(objectType), objectClass, method, fieldType);
            fieldClassResolved = TypeUtils.getMapping(fieldTypeResolved);
        }
        if (fieldClass == List.class || fieldClass == ArrayList.class || fieldClass == LinkedList.class) {
            if (fieldTypeResolved instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)fieldTypeResolved;
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length == 1) {
                    final Type itemType = actualTypeArguments[0];
                    final Class itemClass = TypeUtils.getMapping(itemType);
                    if (itemClass == String.class) {
                        return new FieldReaderList(fieldName, fieldTypeResolved, fieldClass, String.class, String.class, ordinal, features, format, locale, null, jsonSchema, method, null, null);
                    }
                    return new FieldReaderList(fieldName, fieldTypeResolved, fieldClassResolved, itemType, itemClass, ordinal, features, format, locale, null, jsonSchema, method, null, null);
                }
            }
            return new FieldReaderList(fieldName, fieldType, fieldClass, Object.class, Object.class, ordinal, features, format, locale, null, jsonSchema, method, null, null);
        }
        if (fieldClass == Date.class) {
            return new FieldReaderDate(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, null, method, null);
        }
        if (fieldClass == StackTraceElement[].class && method.getDeclaringClass() == Throwable.class) {
            return new FieldReaderStackTrace(fieldName, (fieldTypeResolved != null) ? fieldTypeResolved : fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, method, null, Throwable::setStackTrace);
        }
        Field field2 = null;
        if ((features & 0x2000000000000L) != 0x0L) {
            final String methodName2 = method.getName();
            if (methodName2.startsWith("set")) {
                final String setterName = BeanUtils.setterName(methodName2, PropertyNamingStrategy.CamelCase.name());
                field2 = BeanUtils.getDeclaredField(method.getDeclaringClass(), setterName);
                try {
                    field2.setAccessible(true);
                }
                catch (Throwable t) {}
            }
        }
        return new FieldReaderObject(fieldName, (fieldTypeResolved != null) ? fieldTypeResolved : fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, method, field2, null);
    }
    
    public <T> FieldReader<T> createFieldReader(final String fieldName, final Type fieldType, final Field field) {
        return this.createFieldReader(fieldName, null, fieldType, field);
    }
    
    public <T> FieldReader<T> createFieldReader(final String fieldName, final Field field) {
        return this.createFieldReader(fieldName, null, field.getGenericType(), field);
    }
    
    public <T> FieldReader createFieldReader(final String fieldName, final Method method) {
        final Class<?> declaringClass = method.getDeclaringClass();
        final int parameterCount = method.getParameterCount();
        Class fieldClass;
        Type fieldType;
        if (parameterCount == 0) {
            fieldClass = method.getReturnType();
            fieldType = method.getGenericReturnType();
        }
        else {
            if (parameterCount != 1) {
                throw new JSONException("illegal setter method " + method);
            }
            fieldClass = method.getParameterTypes()[0];
            fieldType = method.getGenericParameterTypes()[0];
        }
        return this.createFieldReaderMethod(declaringClass, declaringClass, fieldName, 0, 0L, null, null, null, null, fieldType, fieldClass, method, null);
    }
    
    public <T> FieldReader<T> createFieldReader(final String fieldName, final String format, final Type fieldType, final Field field) {
        final Class objectClass = field.getDeclaringClass();
        return this.createFieldReader(objectClass, objectClass, fieldName, 0L, format, fieldType, field.getType(), field);
    }
    
    public <T> FieldReader<T> createFieldReader(final Class objectClass, final Type objectType, final String fieldName, final long features, final String format, final Type fieldType, final Class fieldClass, final Field field) {
        return this.createFieldReader(objectClass, objectType, fieldName, 0, features, format, null, null, null, fieldType, field.getType(), field, null);
    }
    
    public <T> FieldReader<T> createFieldReader(final Class objectClass, final Type objectType, final String fieldName, final int ordinal, final long features, final String format, final Locale locale, Object defaultValue, final String schema, final Type fieldType, final Class fieldClass, final Field field, final ObjectReader initReader) {
        if (defaultValue != null && defaultValue.getClass() != fieldClass) {
            final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
            final Function typeConvert = provider.getTypeConvert(defaultValue.getClass(), fieldType);
            if (typeConvert == null) {
                throw new JSONException("illegal defaultValue : " + defaultValue + ", class " + fieldClass.getName());
            }
            defaultValue = typeConvert.apply(defaultValue);
        }
        JSONSchema jsonSchema = null;
        if (schema != null && !schema.isEmpty()) {
            final JSONObject object = JSON.parseObject(schema);
            if (!object.isEmpty()) {
                jsonSchema = JSONSchema.of(object, fieldClass);
            }
        }
        if (field != null) {
            final String objectClassName = objectClass.getName();
            if (!objectClassName.startsWith("java.lang") && !objectClassName.startsWith("java.time")) {
                field.setAccessible(true);
            }
        }
        if (initReader != null) {
            final FieldReaderObjectField fieldReader = new FieldReaderObjectField(fieldName, fieldType, fieldClass, ordinal, features | 0x8000000000000L, format, defaultValue, jsonSchema, field);
            fieldReader.initReader = initReader;
            return (FieldReader<T>)fieldReader;
        }
        if (fieldClass == Integer.TYPE) {
            return new FieldReaderInt32ValueField<T>(fieldName, fieldClass, ordinal, format, (Integer)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Integer.class) {
            return new FieldReaderInt32Field<T>(fieldName, fieldClass, ordinal, features, format, (Integer)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Long.TYPE) {
            return new FieldReaderInt64ValueField<T>(fieldName, fieldClass, ordinal, features, format, (Long)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Long.class) {
            return new FieldReaderInt64Field<T>(fieldName, fieldClass, ordinal, features, format, (Long)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Short.TYPE) {
            return new FieldReaderInt16ValueField<T>(fieldName, fieldClass, ordinal, features, format, (Short)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Short.class) {
            return new FieldReaderInt16Field<T>(fieldName, fieldClass, ordinal, features, format, (Short)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Boolean.TYPE) {
            return new FieldReaderBoolValueField<T>(fieldName, ordinal, features, format, (Boolean)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Boolean.class) {
            return new FieldReaderBoolField<T>(fieldName, fieldClass, ordinal, features, format, (Boolean)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Byte.TYPE) {
            return new FieldReaderInt8ValueField<T>(fieldName, fieldClass, ordinal, features, format, (Byte)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Byte.class) {
            return new FieldReaderInt8Field<T>(fieldName, fieldClass, ordinal, features, format, (Byte)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Float.TYPE) {
            return new FieldReaderFloatValueField<T>(fieldName, fieldClass, ordinal, features, format, (Float)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Float.class) {
            return new FieldReaderFloatField<T>(fieldName, fieldClass, ordinal, features, format, (Float)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Double.TYPE) {
            return new FieldReaderDoubleValueField<T>(fieldName, fieldClass, ordinal, features, format, (Double)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Double.class) {
            return new FieldReaderDoubleField<T>(fieldName, fieldClass, ordinal, features, format, (Double)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Character.TYPE) {
            return new FieldReaderCharValueField<T>(fieldName, ordinal, features, format, (Character)defaultValue, jsonSchema, field);
        }
        if (fieldClass == BigDecimal.class) {
            return new FieldReaderBigDecimalField<T>(fieldName, fieldClass, ordinal, features, format, (BigDecimal)defaultValue, jsonSchema, field);
        }
        if (fieldClass == BigInteger.class) {
            return new FieldReaderBigIntegerField<T>(fieldName, fieldClass, ordinal, features, format, (BigInteger)defaultValue, jsonSchema, field);
        }
        if (fieldClass == String.class) {
            return new FieldReaderStringField<T>(fieldName, fieldClass, ordinal, features, format, (String)defaultValue, jsonSchema, field);
        }
        if (fieldClass == Date.class) {
            return new FieldReaderDate<T>(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, field, null, null);
        }
        if (fieldClass == AtomicBoolean.class) {
            return new FieldReaderAtomicBooleanFieldReadOnly<T>(fieldName, fieldClass, ordinal, format, (AtomicBoolean)defaultValue, jsonSchema, field);
        }
        if (fieldClass == AtomicReference.class) {
            return new FieldReaderAtomicReferenceField<T>(fieldName, fieldType, fieldClass, ordinal, format, jsonSchema, field);
        }
        Type fieldTypeResolved = null;
        Class fieldClassResolved = null;
        if (!(fieldType instanceof Class)) {
            fieldTypeResolved = BeanUtils.getFieldType(TypeReference.get(objectType), objectClass, field, fieldType);
            fieldClassResolved = TypeUtils.getMapping(fieldTypeResolved);
        }
        final boolean finalField = Modifier.isFinal(field.getModifiers());
        if (Collection.class.isAssignableFrom(fieldClass)) {
            if (fieldTypeResolved instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)fieldTypeResolved;
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length == 1) {
                    final Type itemType = actualTypeArguments[0];
                    final Class itemClass = TypeUtils.getMapping(itemType);
                    if (itemClass != String.class) {
                        return (FieldReader<T>)new FieldReaderListField(fieldName, fieldTypeResolved, fieldClassResolved, itemType, itemClass, ordinal, features, format, locale, (Collection)defaultValue, jsonSchema, field);
                    }
                    if (!finalField) {
                        return (FieldReader<T>)new FieldReaderListField(fieldName, fieldTypeResolved, fieldClassResolved, String.class, String.class, ordinal, features, format, locale, null, jsonSchema, field);
                    }
                    if ((features & JSONReader.Feature.FieldBased.mask) != 0x0L) {
                        return (FieldReader<T>)new FieldReaderListField(fieldName, fieldTypeResolved, fieldClassResolved, String.class, String.class, ordinal, features, format, locale, null, jsonSchema, field);
                    }
                    return new FieldReaderCollectionFieldReadOnly<T>(fieldName, fieldTypeResolved, fieldClassResolved, ordinal, features, format, jsonSchema, field);
                }
            }
            Type itemType2 = null;
            if (fieldType instanceof ParameterizedType) {
                final Type[] actualTypeArguments = ((ParameterizedType)fieldType).getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    itemType2 = actualTypeArguments[0];
                }
            }
            if (itemType2 == null) {
                itemType2 = Object.class;
            }
            final Class itemClass2 = TypeUtils.getClass(itemType2);
            return (FieldReader<T>)new FieldReaderListField(fieldName, fieldType, fieldClass, itemType2, itemClass2, ordinal, features, format, locale, (Collection)defaultValue, jsonSchema, field);
        }
        if (Map.class.isAssignableFrom(fieldClass) && fieldTypeResolved instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)fieldTypeResolved;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 2 && finalField && (features & JSONReader.Feature.FieldBased.mask) == 0x0L) {
                return new FieldReaderMapFieldReadOnly<T>(fieldName, fieldTypeResolved, fieldClassResolved, ordinal, features, format, jsonSchema, field);
            }
        }
        if (finalField) {
            if (fieldClass == int[].class) {
                return new FieldReaderInt32ValueArrayFinalField<T>(fieldName, fieldClass, ordinal, features, format, (int[])defaultValue, jsonSchema, field);
            }
            if (fieldClass == long[].class) {
                return new FieldReaderInt64ValueArrayFinalField<T>(fieldName, fieldClass, ordinal, features, format, (long[])defaultValue, jsonSchema, field);
            }
        }
        if (fieldClassResolved != null) {
            if ((features & 0x2000000000000L) != 0x0L && Map.class.isAssignableFrom(fieldClassResolved)) {
                return new FieldReaderMapFieldReadOnly<T>(fieldName, fieldTypeResolved, fieldClass, ordinal, features, format, jsonSchema, field);
            }
            return new FieldReaderObjectField<T>(fieldName, fieldTypeResolved, fieldClass, ordinal, features, format, defaultValue, jsonSchema, field);
        }
        else {
            if (fieldClass == LocalDateTime.class) {
                return new FieldReaderLocalDateTime<T>(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, field, null, null);
            }
            if (fieldClass == ZonedDateTime.class) {
                return new FieldReaderZonedDateTime<T>(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, field, null, null);
            }
            if (fieldClass == Instant.class) {
                return new FieldReaderInstant<T>(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, field, null, null);
            }
            return new FieldReaderObjectField<T>(fieldName, fieldType, fieldClass, ordinal, features, format, defaultValue, jsonSchema, field);
        }
    }
    
    public <T, V> FieldReader createFieldReader(final String fieldName, final Type fieldType, final Class<V> fieldClass, final long features, final BiConsumer<T, V> function) {
        return this.createFieldReader(null, null, fieldName, fieldType, fieldClass, 0, features, null, null, null, null, null, function, null);
    }
    
    public <T, V> FieldReader createFieldReader(final String fieldName, final Type fieldType, final Class<V> fieldClass, final Method method, final BiConsumer<T, V> function) {
        return this.createFieldReader(null, null, fieldName, fieldType, fieldClass, 0, 0L, null, null, null, null, method, function, null);
    }
    
    public <T, V> FieldReader createFieldReader(final Class objectClass, final Type objectType, final String fieldName, final Type fieldType, final Class<V> fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final BiConsumer<T, V> function, final ObjectReader initReader) {
        if (initReader != null) {
            final FieldReaderObject fieldReaderObjectMethod = new FieldReaderObject(fieldName, fieldType, fieldClass, ordinal, features | 0x8000000000000L, format, locale, defaultValue, schema, method, null, function);
            fieldReaderObjectMethod.initReader = initReader;
            return fieldReaderObjectMethod;
        }
        if (fieldClass == Integer.class) {
            return new FieldReaderInt32Func(fieldName, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function);
        }
        if (fieldClass == Long.class) {
            return new FieldReaderInt64Func(fieldName, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function);
        }
        if (fieldClass == String.class) {
            return new FieldReaderStringFunc(fieldName, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function);
        }
        if (fieldClass == Boolean.class) {
            return new FieldReaderBoolFunc(fieldName, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function);
        }
        if (fieldClass == Short.class) {
            return new FieldReaderInt16Func(fieldName, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function);
        }
        if (fieldClass == Byte.class) {
            return new FieldReaderInt8Func(fieldName, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function);
        }
        if (fieldClass == BigDecimal.class) {
            return new FieldReaderBigDecimalFunc(fieldName, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function);
        }
        if (fieldClass == BigInteger.class) {
            return new FieldReaderBigIntegerFunc(fieldName, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function);
        }
        if (fieldClass == Float.class) {
            return new FieldReaderFloatFunc(fieldName, fieldClass, ordinal, features, format, locale, (Float)defaultValue, schema, method, function);
        }
        if (fieldClass == Double.class) {
            return new FieldReaderDoubleFunc(fieldName, fieldClass, ordinal, features, format, locale, (Double)defaultValue, schema, method, function);
        }
        if (fieldClass == Number.class) {
            return new FieldReaderNumberFunc(fieldName, fieldClass, ordinal, features, format, locale, (Number)defaultValue, schema, method, function);
        }
        if (fieldClass == Date.class) {
            return new FieldReaderDate(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, null, method, function);
        }
        Type fieldTypeResolved = null;
        Class fieldClassResolved = null;
        if (!(fieldType instanceof Class)) {
            TypeReference<?> objectTypeReference;
            if (objectType == null) {
                objectTypeReference = null;
            }
            else {
                objectTypeReference = TypeReference.get(objectType);
            }
            fieldTypeResolved = BeanUtils.getFieldType(objectTypeReference, objectClass, method, fieldType);
            fieldClassResolved = TypeUtils.getMapping(fieldTypeResolved);
        }
        if (fieldClass == List.class || fieldClass == ArrayList.class) {
            Type itemType = Object.class;
            Class itemClass = Object.class;
            if (fieldTypeResolved instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)fieldTypeResolved;
                final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length == 1) {
                    itemType = actualTypeArguments[0];
                    itemClass = TypeUtils.getMapping(itemType);
                    if (itemClass == String.class) {
                        return new FieldReaderList(fieldName, fieldTypeResolved, fieldClassResolved, String.class, String.class, ordinal, features, format, locale, defaultValue, schema, method, null, function);
                    }
                }
            }
            return new FieldReaderList(fieldName, fieldTypeResolved, fieldClassResolved, itemType, itemClass, ordinal, features, format, locale, defaultValue, schema, method, null, function);
        }
        if (fieldTypeResolved != null) {
            return new FieldReaderObjectFunc(fieldName, fieldTypeResolved, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function, null);
        }
        return new FieldReaderObjectFunc(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, function, null);
    }
    
    protected ObjectReader createEnumReader(final Class objectClass, final Method createMethod, final ObjectReaderProvider provider) {
        final FieldInfo fieldInfo = new FieldInfo();
        final Enum[] ordinalEnums = objectClass.getEnumConstants();
        final Map<Long, Enum> enumMap = new LinkedHashMap<Long, Enum>();
        for (int i = 0; ordinalEnums != null && i < ordinalEnums.length; ++i) {
            final Enum e = ordinalEnums[i];
            final String name = e.name();
            final long hash = Fnv.hashCode64(name);
            enumMap.put(hash, e);
            try {
                fieldInfo.init();
                final Field field = objectClass.getField(name);
                provider.getFieldInfo(fieldInfo, objectClass, field);
                final String jsonFieldName = fieldInfo.fieldName;
                if (jsonFieldName != null && !jsonFieldName.isEmpty() && !jsonFieldName.equals(name)) {
                    final long jsonFieldNameHash = Fnv.hashCode64(jsonFieldName);
                    enumMap.putIfAbsent(jsonFieldNameHash, e);
                }
                if (fieldInfo.alternateNames != null) {
                    for (final String alternateName : fieldInfo.alternateNames) {
                        if (alternateName != null) {
                            if (!alternateName.isEmpty()) {
                                final long alternateNameHash = Fnv.hashCode64(alternateName);
                                enumMap.putIfAbsent(alternateNameHash, e);
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {}
        }
        for (int i = 0; ordinalEnums != null && i < ordinalEnums.length; ++i) {
            final Enum e = ordinalEnums[i];
            final String name = e.name();
            final long hashLCase = Fnv.hashCode64LCase(name);
            enumMap.putIfAbsent(hashLCase, e);
        }
        final long[] enumNameHashCodes = new long[enumMap.size()];
        int j = 0;
        for (final Long h : enumMap.keySet()) {
            enumNameHashCodes[j++] = h;
        }
        Arrays.sort(enumNameHashCodes);
        Member enumValueField = BeanUtils.getEnumValueField(objectClass, provider);
        if (enumValueField == null && provider.modules.size() > 0) {
            final Class fieldClassMixInSource = provider.getMixIn(objectClass);
            if (fieldClassMixInSource != null) {
                final Member mixedValueField = BeanUtils.getEnumValueField(fieldClassMixInSource, provider);
                if (mixedValueField instanceof Field) {
                    try {
                        enumValueField = objectClass.getField(mixedValueField.getName());
                    }
                    catch (NoSuchFieldException ex2) {}
                }
                else if (mixedValueField instanceof Method) {
                    try {
                        enumValueField = objectClass.getMethod(mixedValueField.getName(), (Class[])new Class[0]);
                    }
                    catch (NoSuchMethodException ex3) {}
                }
            }
        }
        final Enum[] enums = new Enum[enumNameHashCodes.length];
        for (int k = 0; k < enumNameHashCodes.length; ++k) {
            final long hash2 = enumNameHashCodes[k];
            final Enum e2 = enumMap.get(hash2);
            enums[k] = e2;
        }
        return new ObjectReaderImplEnum(objectClass, createMethod, enumValueField, enums, ordinalEnums, enumNameHashCodes);
    }
    
    static ObjectReader getInitReader(final ObjectReaderProvider provider, final Type fieldType, final Class fieldClass, final FieldInfo fieldInfo) {
        ObjectReader initReader = fieldInfo.getInitReader();
        if (initReader == null && Map.class.isAssignableFrom(fieldClass) && (fieldInfo.keyUsing != null || fieldInfo.valueUsing != null)) {
            ObjectReader keyReader = null;
            if (fieldInfo.keyUsing != null) {
                try {
                    final Constructor<?> constructor = fieldInfo.keyUsing.getDeclaredConstructor((Class<?>[])new Class[0]);
                    constructor.setAccessible(true);
                    keyReader = (ObjectReader)constructor.newInstance(new Object[0]);
                }
                catch (Exception ex) {}
            }
            ObjectReader valueReader = null;
            if (fieldInfo.valueUsing != null) {
                try {
                    final Constructor<?> constructor2 = fieldInfo.valueUsing.getDeclaredConstructor((Class<?>[])new Class[0]);
                    constructor2.setAccessible(true);
                    valueReader = (ObjectReader)constructor2.newInstance(new Object[0]);
                }
                catch (Exception ex2) {}
            }
            if (keyReader != null || valueReader != null) {
                final ObjectReader reader = ObjectReaderImplMap.of(fieldType, fieldClass, fieldInfo.features);
                if (reader instanceof ObjectReaderImplMapTyped) {
                    final ObjectReaderImplMapTyped mapReader = (ObjectReaderImplMapTyped)reader;
                    if (keyReader != null) {
                        mapReader.keyObjectReader = keyReader;
                    }
                    if (valueReader != null) {
                        mapReader.valueObjectReader = valueReader;
                    }
                    return mapReader;
                }
            }
        }
        if (initReader == null) {
            if (fieldClass == Long.TYPE || fieldClass == Long.class) {
                final ObjectReader objectReader = provider.getObjectReader(Long.class);
                if (objectReader != ObjectReaderImplInt64.INSTANCE) {
                    initReader = objectReader;
                }
            }
            else if (fieldClass == BigDecimal.class) {
                final ObjectReader objectReader = provider.getObjectReader(BigDecimal.class);
                if (objectReader != ObjectReaderImplBigDecimal.INSTANCE) {
                    initReader = objectReader;
                }
            }
            else if (fieldClass == BigInteger.class) {
                final ObjectReader objectReader = provider.getObjectReader(BigInteger.class);
                if (objectReader != ObjectReaderImplBigInteger.INSTANCE) {
                    initReader = objectReader;
                }
            }
            else if (fieldClass == Date.class) {
                final ObjectReader objectReader = provider.getObjectReader(Date.class);
                if (objectReader != ObjectReaderImplDate.INSTANCE) {
                    initReader = objectReader;
                }
            }
        }
        return initReader;
    }
    
    protected <T> FieldReader createFieldReaderLambda(final Class<T> objectClass, final Type objectType, final String fieldName, final int ordinal, final long features, final String format, final Locale locale, Object defaultValue, final String schema, final Type fieldType, final Class fieldClass, final Method method, final ObjectReader initReader) {
        if (defaultValue != null && defaultValue.getClass() != fieldClass) {
            final Function typeConvert = JSONFactory.getDefaultObjectReaderProvider().getTypeConvert(defaultValue.getClass(), fieldType);
            if (typeConvert == null) {
                throw new JSONException("illegal defaultValue : " + defaultValue + ", class " + fieldClass.getName());
            }
            defaultValue = typeConvert.apply(defaultValue);
        }
        JSONSchema jsonSchema = null;
        if (schema != null && !schema.isEmpty()) {
            final JSONObject object = JSON.parseObject(schema);
            if (!object.isEmpty()) {
                jsonSchema = JSONSchema.of(object, fieldClass);
            }
        }
        if (initReader != null) {
            final BiConsumer function = (BiConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return this.createFieldReader(objectClass, objectType, fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, method, (BiConsumer<Object, Object>)function, initReader);
        }
        if (fieldType == Boolean.TYPE) {
            final ObjBoolConsumer function2 = (ObjBoolConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return new FieldReaderBoolValFunc(fieldName, ordinal, jsonSchema, method, function2);
        }
        if (fieldType == Byte.TYPE) {
            final ObjByteConsumer function3 = (ObjByteConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return new FieldReaderInt8ValueFunc(fieldName, ordinal, jsonSchema, method, function3);
        }
        if (fieldType == Short.TYPE) {
            final ObjShortConsumer function4 = (ObjShortConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return new FieldReaderInt16ValueFunc(fieldName, ordinal, features, format, locale, (Short)defaultValue, jsonSchema, method, function4);
        }
        if (fieldType == Integer.TYPE) {
            final ObjIntConsumer function5 = (ObjIntConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return new FieldReaderInt32ValueFunc(fieldName, ordinal, (Integer)defaultValue, jsonSchema, method, function5);
        }
        if (fieldType == Long.TYPE) {
            final ObjLongConsumer function6 = (ObjLongConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return new FieldReaderInt64ValueFunc(fieldName, ordinal, (Long)defaultValue, jsonSchema, method, function6);
        }
        if (fieldType == Character.TYPE) {
            final ObjCharConsumer function7 = (ObjCharConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return new FieldReaderCharValueFunc(fieldName, ordinal, format, (Character)defaultValue, jsonSchema, method, function7);
        }
        if (fieldType == Float.TYPE) {
            final ObjFloatConsumer function8 = (ObjFloatConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return new FieldReaderFloatValueFunc(fieldName, ordinal, (Float)defaultValue, jsonSchema, method, function8);
        }
        if (fieldType == Double.TYPE) {
            final ObjDoubleConsumer function9 = (ObjDoubleConsumer)this.lambdaSetter(objectClass, fieldClass, method);
            return new FieldReaderDoubleValueFunc(fieldName, ordinal, (Double)defaultValue, jsonSchema, method, function9);
        }
        final BiConsumer consumer = (BiConsumer)this.lambdaSetter(objectClass, fieldClass, method);
        return this.createFieldReader(objectClass, objectType, fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, jsonSchema, method, (BiConsumer<Object, Object>)consumer, null);
    }
    
    protected Object lambdaSetter(final Class objectClass, final Class fieldClass, final Method method) {
        final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(objectClass);
        final Class<?> returnType = method.getReturnType();
        final LambdaSetterInfo lambdaInfo = ObjectReaderCreator.methodTypeMapping.get(fieldClass);
        MethodType methodType = null;
        MethodType samMethodType;
        MethodType invokedType;
        if (lambdaInfo != null) {
            samMethodType = lambdaInfo.sameMethodMethod;
            invokedType = lambdaInfo.invokedType;
            if (returnType == Void.TYPE) {
                methodType = lambdaInfo.methodType;
            }
        }
        else {
            samMethodType = TypeUtils.METHOD_TYPE_VOO;
            invokedType = TypeUtils.METHOD_TYPE_BI_CONSUMER;
        }
        if (methodType == null) {
            methodType = MethodType.methodType(returnType, fieldClass);
        }
        try {
            final MethodHandle target = lookup.findVirtual(objectClass, method.getName(), methodType);
            final MethodType instantiatedMethodType = MethodType.methodType(Void.TYPE, objectClass, fieldClass);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "accept", invokedType, samMethodType, target, instantiatedMethodType);
            return callSite.getTarget().invoke();
        }
        catch (Throwable e) {
            throw new JSONException("create fieldReader error", e);
        }
    }
    
    public Function<Consumer, ByteArrayValueConsumer> createByteArrayValueConsumerCreator(final Class objectClass, final FieldReader[] fieldReaderArray) {
        return null;
    }
    
    public Function<Consumer, CharArrayValueConsumer> createCharArrayValueConsumerCreator(final Class objectClass, final FieldReader[] fieldReaderArray) {
        return null;
    }
    
    static {
        JIT = (!JDKUtils.ANDROID && !JDKUtils.GRAAL);
        INSTANCE = new ObjectReaderCreator();
        (methodTypeMapping = new HashMap<Class, LambdaSetterInfo>()).put(Boolean.TYPE, new LambdaSetterInfo(Boolean.TYPE, ObjBoolConsumer.class));
        ObjectReaderCreator.methodTypeMapping.put(Byte.TYPE, new LambdaSetterInfo(Byte.TYPE, ObjByteConsumer.class));
        ObjectReaderCreator.methodTypeMapping.put(Short.TYPE, new LambdaSetterInfo(Short.TYPE, ObjShortConsumer.class));
        ObjectReaderCreator.methodTypeMapping.put(Integer.TYPE, new LambdaSetterInfo(Integer.TYPE, ObjIntConsumer.class));
        ObjectReaderCreator.methodTypeMapping.put(Long.TYPE, new LambdaSetterInfo(Long.TYPE, ObjLongConsumer.class));
        ObjectReaderCreator.methodTypeMapping.put(Character.TYPE, new LambdaSetterInfo(Character.TYPE, ObjCharConsumer.class));
        ObjectReaderCreator.methodTypeMapping.put(Float.TYPE, new LambdaSetterInfo(Float.TYPE, ObjFloatConsumer.class));
        ObjectReaderCreator.methodTypeMapping.put(Double.TYPE, new LambdaSetterInfo(Double.TYPE, ObjDoubleConsumer.class));
    }
    
    static class LambdaSetterInfo
    {
        final Class fieldClass;
        final MethodType sameMethodMethod;
        final MethodType methodType;
        final MethodType invokedType;
        
        LambdaSetterInfo(final Class fieldClass, final Class functionClass) {
            this.fieldClass = fieldClass;
            this.sameMethodMethod = MethodType.methodType(Void.TYPE, Object.class, fieldClass);
            this.methodType = MethodType.methodType(Void.TYPE, fieldClass);
            this.invokedType = MethodType.methodType(functionClass);
        }
    }
}
