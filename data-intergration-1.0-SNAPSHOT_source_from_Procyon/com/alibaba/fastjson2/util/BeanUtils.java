// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.util.LinkedHashSet;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Iterator;
import com.alibaba.fastjson2.codec.FieldInfo;
import java.util.Set;
import java.util.HashSet;
import java.lang.annotation.Inherited;
import java.lang.reflect.GenericDeclaration;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Array;
import java.lang.reflect.WildcardType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Parameter;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.JSONException;
import java.util.Arrays;
import java.lang.reflect.Proxy;
import java.lang.reflect.Member;
import com.alibaba.fastjson2.modules.ObjectCodecProvider;
import com.alibaba.fastjson2.codec.BeanInfo;
import com.alibaba.fastjson2.annotation.JSONType;
import java.lang.annotation.Annotation;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.annotation.JSONField;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;
import java.lang.reflect.Type;

public abstract class BeanUtils
{
    static final Type[] EMPTY_TYPE_ARRAY;
    static final ConcurrentMap<Class, Field[]> fieldCache;
    static final ConcurrentMap<Class, Map<String, Field>> fieldMapCache;
    static final ConcurrentMap<Class, Field[]> declaredFieldCache;
    static final ConcurrentMap<Class, Method[]> methodCache;
    static final ConcurrentMap<Class, Constructor[]> constructorCache;
    private static volatile Class RECORD_CLASS;
    private static volatile Method RECORD_GET_RECORD_COMPONENTS;
    private static volatile Method RECORD_COMPONENT_GET_NAME;
    public static final String SUPER = "$super$";
    static final long[] IGNORE_CLASS_HASH_CODES;
    
    public static String[] getRecordFieldNames(final Class<?> recordType) {
        if (JDKUtils.JVM_VERSION < 14) {
            return new String[0];
        }
        try {
            if (BeanUtils.RECORD_GET_RECORD_COMPONENTS == null) {
                BeanUtils.RECORD_GET_RECORD_COMPONENTS = Class.class.getMethod("getRecordComponents", (Class<?>[])new Class[0]);
            }
            if (BeanUtils.RECORD_COMPONENT_GET_NAME == null) {
                final Class<?> c = Class.forName("java.lang.reflect.RecordComponent");
                BeanUtils.RECORD_COMPONENT_GET_NAME = c.getMethod("getName", (Class<?>[])new Class[0]);
            }
            final Object[] components = (Object[])BeanUtils.RECORD_GET_RECORD_COMPONENTS.invoke(recordType, new Object[0]);
            final String[] names = new String[components.length];
            for (int i = 0; i < components.length; ++i) {
                names[i] = (String)BeanUtils.RECORD_COMPONENT_GET_NAME.invoke(components[i], new Object[0]);
            }
            return names;
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Failed to access Methods needed to support `java.lang.Record`: (%s) %s", e.getClass().getName(), e.getMessage()), e);
        }
    }
    
    public static void fields(final Class objectClass, final Consumer<Field> fieldReaders) {
        if (TypeUtils.isProxy(objectClass)) {
            final Class superclass = objectClass.getSuperclass();
            fields(superclass, fieldReaders);
            return;
        }
        Field[] fields = BeanUtils.fieldCache.get(objectClass);
        if (fields == null) {
            fields = objectClass.getFields();
            BeanUtils.fieldCache.putIfAbsent(objectClass, fields);
        }
        final boolean enumClass = Enum.class.isAssignableFrom(objectClass);
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) || enumClass) {
                fieldReaders.accept(field);
            }
        }
    }
    
    public static Method getMethod(final Class objectClass, final String methodName) {
        Method[] methods = BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        for (final Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
    
    public static Method fluentSetter(final Class objectClass, final String methodName, final Class paramType) {
        Method[] methods = (Method[])BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        for (final Method method : methods) {
            if (method.getName().equals(methodName) && method.getReturnType() == objectClass && method.getParameterCount() == 1 && method.getParameterTypes()[0] == paramType) {
                return method;
            }
        }
        return null;
    }
    
    public static Method getMethod(final Class objectClass, final Method signature) {
        if (objectClass == null || objectClass == Object.class || objectClass == Serializable.class) {
            return null;
        }
        Method[] methods = BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        for (final Method method : methods) {
            if (method.getName().equals(signature.getName())) {
                if (method.getParameterCount() == signature.getParameterCount()) {
                    final Class<?>[] parameterTypes0 = method.getParameterTypes();
                    final Class<?>[] parameterTypes2 = signature.getParameterTypes();
                    boolean paramMatch = true;
                    for (int i = 0; i < parameterTypes0.length; ++i) {
                        if (!parameterTypes0[i].equals(parameterTypes2[i])) {
                            paramMatch = false;
                            break;
                        }
                    }
                    if (paramMatch) {
                        return method;
                    }
                }
            }
        }
        return null;
    }
    
    public static Field getDeclaredField(final Class objectClass, final String fieldName) {
        Map<String, Field> fieldMap = BeanUtils.fieldMapCache.get(objectClass);
        if (fieldMap == null) {
            final Map<String, Field> map = new HashMap<String, Field>();
            declaredFields(objectClass, field -> map.put(field.getName(), field));
            BeanUtils.fieldMapCache.putIfAbsent(objectClass, map);
            fieldMap = BeanUtils.fieldMapCache.get(objectClass);
        }
        return fieldMap.get(fieldName);
    }
    
    public static Method getSetter(final Class objectClass, final String methodName) {
        final Method[] methods = { null };
        final Object o;
        setters(objectClass, e -> {
            if (!methodName.equals(e.getName())) {
                return;
            }
            else {
                o[0] = e;
                return;
            }
        });
        return methods[0];
    }
    
    public static void declaredFields(final Class objectClass, final Consumer<Field> fieldConsumer) {
        if (objectClass == null || fieldConsumer == null) {
            return;
        }
        if (ignore(objectClass)) {
            return;
        }
        if (TypeUtils.isProxy(objectClass)) {
            final Class superclass = objectClass.getSuperclass();
            declaredFields(superclass, fieldConsumer);
            return;
        }
        final Class superClass = objectClass.getSuperclass();
        boolean protobufMessageV3 = false;
        if (superClass != null && superClass != Object.class) {
            protobufMessageV3 = superClass.getName().equals("com.google.protobuf.GeneratedMessageV3");
            if (!protobufMessageV3) {
                declaredFields(superClass, fieldConsumer);
            }
        }
        Field[] fields = BeanUtils.declaredFieldCache.get(objectClass);
        if (fields == null) {
            Field[] declaredFields = null;
            try {
                declaredFields = objectClass.getDeclaredFields();
                BeanUtils.declaredFieldCache.put(objectClass, declaredFields);
            }
            catch (Throwable ignored) {
                declaredFields = new Field[0];
            }
            boolean allMatch = true;
            for (final Field field : declaredFields) {
                final int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                fields = declaredFields;
            }
            else {
                final List<Field> list = new ArrayList<Field>(declaredFields.length);
                for (final Field field2 : declaredFields) {
                    final int modifiers2 = field2.getModifiers();
                    if (!Modifier.isStatic(modifiers2)) {
                        list.add(field2);
                    }
                }
                fields = list.toArray(new Field[list.size()]);
            }
            BeanUtils.fieldCache.putIfAbsent(objectClass, fields);
        }
        for (final Field field3 : fields) {
            final int modifiers3 = field3.getModifiers();
            if ((modifiers3 & 0x8) == 0x0) {
                if (protobufMessageV3) {
                    final String fieldName = field3.getName();
                    final Class<?> fieldClass = field3.getType();
                    if ("cardsmap_".equals(fieldName) && fieldClass.getName().equals("com.google.protobuf.MapField")) {
                        return;
                    }
                }
                final Class<?> declaringClass = field3.getDeclaringClass();
                if (declaringClass != AbstractMap.class && declaringClass != HashMap.class && declaringClass != LinkedHashMap.class && declaringClass != TreeMap.class) {
                    if (declaringClass != ConcurrentHashMap.class) {
                        fieldConsumer.accept(field3);
                    }
                }
            }
        }
    }
    
    public static void staticMethod(final Class objectClass, final Consumer<Method> methodConsumer) {
        Method[] methods = BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        for (final Method method : methods) {
            final int modifiers = method.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                methodConsumer.accept(method);
            }
        }
    }
    
    public static Method buildMethod(final Class objectClass, final String methodName) {
        Method[] methods = BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        for (final Method method : methods) {
            final int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                if (method.getParameterCount() == 0) {
                    if (method.getName().equals(methodName)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }
    
    public static void constructor(final Class objectClass, final Consumer<Constructor> constructorConsumer) {
        Constructor[] constructors = BeanUtils.constructorCache.get(objectClass);
        if (constructors == null) {
            constructors = objectClass.getDeclaredConstructors();
            BeanUtils.constructorCache.putIfAbsent(objectClass, constructors);
        }
        final boolean record = isRecord(objectClass);
        for (final Constructor constructor : constructors) {
            if (!record || constructor.getParameterCount() != 0) {
                constructorConsumer.accept(constructor);
            }
        }
    }
    
    public static Constructor[] getConstructor(final Class objectClass) {
        Constructor[] constructors = BeanUtils.constructorCache.get(objectClass);
        if (constructors == null) {
            constructors = objectClass.getDeclaredConstructors();
            BeanUtils.constructorCache.putIfAbsent(objectClass, constructors);
        }
        return constructors;
    }
    
    public static Constructor getDefaultConstructor(final Class objectClass, final boolean includeNoneStaticMember) {
        if ((objectClass == StackTraceElement.class && JDKUtils.JVM_VERSION >= 9) || isRecord(objectClass)) {
            return null;
        }
        Constructor[] constructors = BeanUtils.constructorCache.get(objectClass);
        if (constructors == null) {
            constructors = objectClass.getDeclaredConstructors();
            BeanUtils.constructorCache.putIfAbsent(objectClass, constructors);
        }
        for (final Constructor constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        if (!includeNoneStaticMember) {
            return null;
        }
        final Class declaringClass = objectClass.getDeclaringClass();
        if (declaringClass != null) {
            for (final Constructor constructor2 : constructors) {
                if (constructor2.getParameterCount() == 1) {
                    final Class firstParamType = constructor2.getParameterTypes()[0];
                    if (declaringClass.equals(firstParamType)) {
                        return constructor2;
                    }
                }
            }
        }
        return null;
    }
    
    public static void setters(final Class objectClass, final Consumer<Method> methodConsumer) {
        setters(objectClass, null, methodConsumer);
    }
    
    public static void setters(final Class objectClass, final Class mixin, final Consumer<Method> methodConsumer) {
        if (ignore(objectClass)) {
            return;
        }
        Method[] methods = BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        for (final Method method : methods) {
            final int mods = method.getModifiers();
            Label_0845: {
                if (!Modifier.isStatic(mods)) {
                    if (method.getDeclaringClass() != Object.class) {
                        final String methodName = method.getName();
                        boolean methodSkip = false;
                        final String s = methodName;
                        switch (s) {
                            case "equals":
                            case "hashCode":
                            case "toString": {
                                methodSkip = true;
                                break;
                            }
                        }
                        if (!methodSkip) {
                            final int paramCount = method.getParameterCount();
                            final Class<?> returnType = method.getReturnType();
                            if (paramCount == 0) {
                                if (methodName.length() <= 3) {
                                    break Label_0845;
                                }
                                if (!methodName.startsWith("get")) {
                                    break Label_0845;
                                }
                                if (returnType == AtomicInteger.class || returnType == AtomicLong.class || returnType == AtomicBoolean.class || returnType == AtomicIntegerArray.class || returnType == AtomicLongArray.class || returnType == AtomicReference.class || Collection.class.isAssignableFrom(returnType) || Map.class.isAssignableFrom(returnType)) {
                                    methodConsumer.accept(method);
                                    break Label_0845;
                                }
                            }
                            if (paramCount == 2 && method.getReturnType() == Void.TYPE && method.getParameterTypes()[0] == String.class) {
                                final Annotation[] annotations = getAnnotations(method);
                                boolean unwrapped = false;
                                for (final Annotation annotation : annotations) {
                                    final Class<? extends Annotation> annotationType = annotation.annotationType();
                                    final JSONField jsonField = findAnnotation(annotation, JSONField.class);
                                    if (jsonField != null) {
                                        if (jsonField.unwrapped()) {
                                            unwrapped = true;
                                            break;
                                        }
                                    }
                                    else {
                                        final String name = annotationType.getName();
                                        switch (name) {
                                            case "com.fasterxml.jackson.annotation.JsonAnySetter":
                                            case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonAnySetter": {
                                                if (JSONFactory.isUseJacksonAnnotation()) {
                                                    unwrapped = true;
                                                    break;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (unwrapped) {
                                    methodConsumer.accept(method);
                                }
                            }
                            else if (paramCount == 1) {
                                final int methodNameLength = methodName.length();
                                boolean nameMatch = methodNameLength > 3 && (methodName.startsWith("set") || returnType == objectClass);
                                if (!nameMatch && mixin != null) {
                                    final Method mixinMethod = getMethod(mixin, method);
                                    if (mixinMethod != null) {
                                        final Annotation[] annotations4;
                                        final Annotation[] annotations2 = annotations4 = getAnnotations(mixinMethod);
                                        final int length3 = annotations4.length;
                                        int k = 0;
                                        while (k < length3) {
                                            final Annotation annotation2 = annotations4[k];
                                            if (annotation2.annotationType() == JSONField.class) {
                                                final JSONField jsonField2 = (JSONField)annotation2;
                                                if (!jsonField2.unwrapped()) {
                                                    nameMatch = true;
                                                    break;
                                                }
                                                break;
                                            }
                                            else {
                                                ++k;
                                            }
                                        }
                                    }
                                }
                                if (!nameMatch) {
                                    final Annotation[] annotations5;
                                    final Annotation[] annotations3 = annotations5 = getAnnotations(method);
                                    final int length4 = annotations5.length;
                                    int l = 0;
                                    while (l < length4) {
                                        final Annotation annotation3 = annotations5[l];
                                        if (annotation3.annotationType() == JSONField.class) {
                                            final JSONField jsonField = (JSONField)annotation3;
                                            if (!jsonField.unwrapped()) {
                                                nameMatch = true;
                                                break;
                                            }
                                            break;
                                        }
                                        else {
                                            ++l;
                                        }
                                    }
                                }
                                if (nameMatch) {
                                    methodConsumer.accept(method);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void setters(final Class objectClass, final boolean checkPrefix, final Consumer<Method> methodConsumer) {
        if (ignore(objectClass)) {
            return;
        }
        Method[] methods = BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        for (final Method method : methods) {
            final int paramType = method.getParameterCount();
            Label_0254: {
                if (paramType == 0) {
                    final String methodName = method.getName();
                    if (checkPrefix) {
                        if (methodName.length() <= 3) {
                            break Label_0254;
                        }
                        if (!methodName.startsWith("get")) {
                            break Label_0254;
                        }
                    }
                    final Class<?> returnType = method.getReturnType();
                    if (returnType == AtomicInteger.class || returnType == AtomicLong.class || returnType == AtomicBoolean.class || returnType == AtomicIntegerArray.class || returnType == AtomicLongArray.class || Collection.class.isAssignableFrom(returnType)) {
                        methodConsumer.accept(method);
                        break Label_0254;
                    }
                }
                if (paramType == 1) {
                    final int mods = method.getModifiers();
                    if (!Modifier.isStatic(mods)) {
                        final String methodName2 = method.getName();
                        final int methodNameLength = methodName2.length();
                        if (checkPrefix) {
                            if (methodNameLength <= 3) {
                                break Label_0254;
                            }
                            if (!methodName2.startsWith("set")) {
                                break Label_0254;
                            }
                        }
                        methodConsumer.accept(method);
                    }
                }
            }
        }
    }
    
    public static void annotationMethods(final Class objectClass, final Consumer<Method> methodConsumer) {
        Method[] methods = BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        for (final Method method : methods) {
            if (method.getParameterCount() == 0) {
                final Class<?> declaringClass = method.getDeclaringClass();
                if (declaringClass != Object.class) {
                    final String name = method.getName();
                    switch (name) {
                        case "toString":
                        case "hashCode":
                        case "annotationType": {
                            break;
                        }
                        default: {
                            methodConsumer.accept(method);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public static boolean isWriteEnumAsJavaBean(final Class clazz) {
        final Annotation[] annotations2;
        final Annotation[] annotations = annotations2 = getAnnotations(clazz);
        for (final Annotation annotation : annotations2) {
            final JSONType jsonType = findAnnotation(annotation, JSONType.class);
            if (jsonType != null) {
                return jsonType.writeEnumAsJavaBean();
            }
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            final String name = annotationType.getName();
            if ("com.alibaba.fastjson.annotation.JSONType".equals(name)) {
                final BeanInfo beanInfo = new BeanInfo();
                annotationMethods(annotationType, method -> processJSONType1x(beanInfo, annotation, method));
                if (beanInfo.writeEnumAsJavaBean) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static String[] getEnumAnnotationNames(final Class enumClass) {
        final Enum[] enumConstants = enumClass.getEnumConstants();
        final String[] annotationNames = new String[enumConstants.length];
        final String fieldName;
        int i;
        final Object o;
        Enum e;
        String enumName;
        JSONField annotation;
        String annotationName;
        final Object o2;
        fields(enumClass, field -> {
            fieldName = field.getName();
            i = 0;
            while (i < o.length) {
                e = o[i];
                enumName = e.name();
                if (fieldName.equals(enumName)) {
                    annotation = field.getAnnotation(JSONField.class);
                    if (annotation != null) {
                        annotationName = annotation.name();
                        if (annotationName.length() != 0 && !annotationName.equals(enumName)) {
                            o2[i] = annotationName;
                        }
                        break;
                    }
                    else {
                        break;
                    }
                }
                else {
                    ++i;
                }
            }
            return;
        });
        int nulls = 0;
        for (final String annotationName2 : annotationNames) {
            if (annotationName2 == null) {
                ++nulls;
            }
        }
        if (nulls == annotationNames.length) {
            return null;
        }
        return annotationNames;
    }
    
    public static Member getEnumValueField(final Class enumClass, final ObjectCodecProvider mixinProvider) {
        if (enumClass == null) {
            return null;
        }
        final Class[] interfaces = enumClass.getInterfaces();
        Method[] methods = BeanUtils.methodCache.get(enumClass);
        if (methods == null) {
            methods = enumClass.getMethods();
            BeanUtils.methodCache.putIfAbsent(enumClass, methods);
        }
        for (final Method method : methods) {
            if (method.getReturnType() != Void.class) {
                if (method.getParameterCount() == 0) {
                    final Class<?> declaringClass = method.getDeclaringClass();
                    if (declaringClass != Enum.class) {
                        if (declaringClass != Object.class) {
                            final String methodName = method.getName();
                            if (!methodName.equals("values")) {
                                if (isJSONField(method)) {
                                    return method;
                                }
                                if (methodName.startsWith("get")) {
                                    final String fieldName = getterName(methodName, null);
                                    final Field field = getDeclaredField(enumClass, fieldName);
                                    if (field != null && isJSONField(field)) {
                                        return method;
                                    }
                                }
                                final AtomicReference<Member> memberRef = new AtomicReference<Member>();
                                for (final Class enumInterface : interfaces) {
                                    final Object anObject;
                                    final AtomicReference<Method> atomicReference;
                                    final Method newValue;
                                    getters(enumInterface, e -> {
                                        if (e.getName().equals(anObject) && isJSONField(e)) {
                                            atomicReference.set(newValue);
                                        }
                                        return;
                                    });
                                    Class mixIn;
                                    if (mixinProvider != null) {
                                        mixIn = mixinProvider.getMixIn(enumInterface);
                                    }
                                    else {
                                        mixIn = JSONFactory.getDefaultObjectWriterProvider().getMixIn(enumInterface);
                                    }
                                    if (mixIn != null) {
                                        final Object anObject2;
                                        final AtomicReference<Method> atomicReference2;
                                        final Method newValue2;
                                        getters(mixIn, e -> {
                                            if (e.getName().equals(anObject2) && isJSONField(e)) {
                                                atomicReference2.set(newValue2);
                                            }
                                            return;
                                        });
                                    }
                                }
                                final Member refMember = memberRef.get();
                                if (refMember != null) {
                                    return refMember;
                                }
                            }
                        }
                    }
                }
            }
        }
        Field[] fields = BeanUtils.fieldCache.get(enumClass);
        if (fields == null) {
            fields = enumClass.getFields();
            BeanUtils.fieldCache.putIfAbsent(enumClass, fields);
        }
        Member member = null;
        final Enum[] enumConstants = enumClass.getEnumConstants();
        for (final Field field2 : fields) {
            boolean found = false;
            if (enumConstants != null) {
                final String fieldName2 = field2.getName();
                for (final Enum e2 : enumConstants) {
                    if (fieldName2.equals(e2.name())) {
                        found = true;
                        break;
                    }
                }
            }
            if (isJSONField(field2) && !found) {
                member = field2;
                break;
            }
        }
        return member;
    }
    
    public static void getters(final Class objectClass, final Consumer<Method> methodConsumer) {
        getters(objectClass, null, methodConsumer);
    }
    
    public static void getters(final Class objectClass, final Class mixinSource, final Consumer<Method> methodConsumer) {
        if (objectClass == null) {
            return;
        }
        if (Proxy.isProxyClass(objectClass)) {
            final Class[] interfaces = objectClass.getInterfaces();
            if (interfaces.length == 1) {
                getters(interfaces[0], methodConsumer);
                return;
            }
        }
        if (ignore(objectClass)) {
            return;
        }
        final Class superClass = objectClass.getSuperclass();
        if (TypeUtils.isProxy(objectClass)) {
            getters(superClass, methodConsumer);
            return;
        }
        final boolean record = isRecord(objectClass);
        String[] recordFieldNames = null;
        if (record) {
            recordFieldNames = getRecordFieldNames(objectClass);
        }
        Method[] methods = BeanUtils.methodCache.get(objectClass);
        if (methods == null) {
            methods = getMethods(objectClass);
            BeanUtils.methodCache.putIfAbsent(objectClass, methods);
        }
        final boolean protobufMessageV3 = superClass != null && superClass.getName().equals("com.google.protobuf.GeneratedMessageV3");
        for (final Method method : methods) {
            final int paramType = method.getParameterCount();
            Label_1322: {
                if (paramType == 0) {
                    final int mods = method.getModifiers();
                    if (!Modifier.isStatic(mods)) {
                        final Class<?> returnClass = method.getReturnType();
                        if (returnClass != Void.class) {
                            final Class<?> declaringClass = method.getDeclaringClass();
                            if (declaringClass != Enum.class) {
                                if (declaringClass != Object.class) {
                                    final String methodName = method.getName();
                                    boolean methodSkip = false;
                                    final String s = methodName;
                                    switch (s) {
                                        case "isInitialized":
                                        case "getInitializationErrorString":
                                        case "getSerializedSize": {
                                            if (protobufMessageV3) {
                                                methodSkip = true;
                                                break;
                                            }
                                            break;
                                        }
                                        case "equals":
                                        case "hashCode":
                                        case "toString": {
                                            methodSkip = true;
                                            break;
                                        }
                                    }
                                    if (!methodSkip) {
                                        if (!protobufMessageV3 || (!methodName.endsWith("Type") && !methodName.endsWith("Bytes")) || !returnClass.getName().equals("com.google.protobuf.ByteString")) {
                                            if (methodName.startsWith("isSet") && returnClass == Boolean.TYPE) {
                                                boolean setterFound = false;
                                                boolean unsetFound = false;
                                                boolean getterFound = false;
                                                final String setterName = getterName(methodName, null);
                                                final String getterName = "g" + setterName.substring(1);
                                                final String unsetName = "un" + setterName;
                                                for (final Method m : methods) {
                                                    if (m.getName().equals(setterName) && m.getParameterCount() == 1 && m.getReturnType() == Void.TYPE) {
                                                        setterFound = true;
                                                    }
                                                    else if (m.getName().equals(getterName) && m.getParameterCount() == 0) {
                                                        getterFound = true;
                                                    }
                                                    else if (m.getName().equals(unsetName) && m.getParameterCount() == 0 && m.getReturnType() == Void.TYPE) {
                                                        unsetFound = true;
                                                    }
                                                }
                                                if (setterFound && unsetFound && getterFound && findAnnotation(method, JSONField.class) == null) {
                                                    break Label_1322;
                                                }
                                            }
                                            if (record) {
                                                boolean match = false;
                                                for (final String recordFieldName : recordFieldNames) {
                                                    if (methodName.equals(recordFieldName)) {
                                                        match = true;
                                                        break;
                                                    }
                                                }
                                                if (match) {
                                                    methodConsumer.accept(method);
                                                    break Label_1322;
                                                }
                                            }
                                            final int methodNameLength = methodName.length();
                                            boolean nameMatch = methodNameLength > 3 && methodName.startsWith("get");
                                            if (nameMatch) {
                                                final char firstChar = methodName.charAt(3);
                                                if (firstChar >= 'a' && firstChar <= 'z' && methodNameLength == 4) {
                                                    nameMatch = false;
                                                }
                                            }
                                            else if (returnClass == Boolean.TYPE || returnClass == Boolean.class) {
                                                nameMatch = (methodNameLength > 2 && methodName.startsWith("is"));
                                                if (nameMatch) {
                                                    final char firstChar = methodName.charAt(2);
                                                    if (firstChar >= 'a' && firstChar <= 'z' && methodNameLength == 3) {
                                                        nameMatch = false;
                                                    }
                                                }
                                            }
                                            if (!nameMatch && isJSONField(method)) {
                                                nameMatch = true;
                                            }
                                            if (!nameMatch && mixinSource != null) {
                                                final Method mixinMethod = getMethod(mixinSource, method);
                                                if (mixinMethod != null && isJSONField(mixinMethod)) {
                                                    nameMatch = true;
                                                }
                                            }
                                            if (!nameMatch && fluentSetter(objectClass, methodName, returnClass) != null) {
                                                nameMatch = true;
                                            }
                                            if (nameMatch) {
                                                if (protobufMessageV3) {
                                                    if (method.getDeclaringClass() == superClass) {
                                                        break Label_1322;
                                                    }
                                                    final Class<?> returnType = method.getReturnType();
                                                    boolean ignore = false;
                                                    final String s2 = methodName;
                                                    switch (s2) {
                                                        case "getUnknownFields":
                                                        case "getSerializedSize":
                                                        case "getParserForType":
                                                        case "getMessageBytes":
                                                        case "getDefaultInstanceForType": {
                                                            ignore = (returnType.getName().startsWith("com.google.protobuf.") || returnType == objectClass);
                                                            break;
                                                        }
                                                    }
                                                    if (ignore) {
                                                        break Label_1322;
                                                    }
                                                }
                                                methodConsumer.accept(method);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static Method[] getMethods(final Class objectClass) {
        Method[] methods;
        try {
            methods = objectClass.getMethods();
        }
        catch (NoClassDefFoundError ignored) {
            methods = new Method[0];
        }
        return methods;
    }
    
    private static boolean isJSONField(final AnnotatedElement element) {
        final Annotation[] annotations2;
        final Annotation[] annotations = annotations2 = element.getAnnotations();
        for (final Annotation annotation : annotations2) {
            final String name;
            final String annotationTypeName = name = annotation.annotationType().getName();
            switch (name) {
                case "com.alibaba.fastjson.annotation.JSONField":
                case "com.alibaba.fastjson2.annotation.JSONField": {
                    return true;
                }
                case "com.fasterxml.jackson.annotation.JsonValue":
                case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonValue":
                case "com.fasterxml.jackson.annotation.JsonRawValue":
                case "com.fasterxml.jackson.annotation.JsonProperty":
                case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonProperty": {
                    if (JSONFactory.isUseJacksonAnnotation()) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }
    
    static boolean ignore(final Class objectClass) {
        return objectClass == null || Arrays.binarySearch(BeanUtils.IGNORE_CLASS_HASH_CODES, Fnv.hashCode64(objectClass.getName())) >= 0;
    }
    
    public static boolean isRecord(final Class objectClass) {
        final Class superclass = objectClass.getSuperclass();
        if (superclass == null) {
            return false;
        }
        if (BeanUtils.RECORD_CLASS != null) {
            return superclass == BeanUtils.RECORD_CLASS;
        }
        final String superclassName = superclass.getName();
        if ("java.lang.Record".equals(superclassName)) {
            BeanUtils.RECORD_CLASS = superclass;
            return true;
        }
        return false;
    }
    
    public static String setterName(final String methodName, String namingStrategy) {
        if (namingStrategy == null) {
            namingStrategy = "CamelCase";
        }
        final int methodNameLength = methodName.length();
        if (methodNameLength <= 3) {
            return methodName;
        }
        final int prefixLength = methodName.startsWith("set") ? 3 : 0;
        final String s = namingStrategy;
        switch (s) {
            case "NeverUseThisValueExceptDefaultValue":
            case "CamelCase": {
                final char[] chars = new char[methodNameLength - prefixLength];
                methodName.getChars(prefixLength, methodNameLength, chars, 0);
                final char c0 = chars[0];
                final boolean c1UCase = chars.length > 1 && chars[1] >= 'A' && chars[1] <= 'Z';
                if (c0 >= 'A' && c0 <= 'Z' && !c1UCase) {
                    chars[0] = (char)(c0 + ' ');
                }
                return new String(chars);
            }
            case "PascalCase": {
                return pascal(methodName, methodNameLength, prefixLength);
            }
            case "SnakeCase": {
                return snakeCase(methodName, prefixLength);
            }
            case "UpperCaseWithUnderScores": {
                return underScores(methodName, prefixLength, true);
            }
            case "UpperCase": {
                final char[] chars = new char[methodNameLength - prefixLength];
                methodName.getChars(prefixLength, methodNameLength, chars, 0);
                final char c0 = chars[0];
                for (int i = 0; i < chars.length; ++i) {
                    final char ch = chars[i];
                    if (ch >= 'a' && c0 <= 'z') {
                        chars[i] = (char)(ch - ' ');
                    }
                }
                return new String(chars);
            }
            default: {
                throw new JSONException("TODO : " + namingStrategy);
            }
        }
    }
    
    public static String setterName(final String methodName, final int prefixLength) {
        final int methodNameLength = methodName.length();
        final char[] chars = new char[methodNameLength - prefixLength];
        methodName.getChars(prefixLength, methodNameLength, chars, 0);
        final char c0 = chars[0];
        final boolean c1UCase = chars.length > 1 && chars[1] >= 'A' && chars[1] <= 'Z';
        if (c0 >= 'A' && c0 <= 'Z' && !c1UCase) {
            chars[0] = (char)(c0 + ' ');
        }
        return new String(chars);
    }
    
    public static String getterName(final Method method, final String namingStrategy) {
        String fieldName = getterName(method.getName(), namingStrategy);
        if (fieldName.length() > 2 && fieldName.charAt(0) >= 'A' && fieldName.charAt(0) <= 'Z' && fieldName.charAt(1) >= 'A' && fieldName.charAt(1) <= 'Z') {
            final char[] chars = fieldName.toCharArray();
            chars[0] += ' ';
            final String fieldName2 = new String(chars);
            final Field field = getDeclaredField(method.getDeclaringClass(), fieldName2);
            if (field != null && Modifier.isPublic(field.getModifiers())) {
                fieldName = field.getName();
            }
        }
        return fieldName;
    }
    
    public static Field getField(final Class objectClass, final Method method) {
        final String methodName = method.getName();
        final int len = methodName.length();
        final Class<?> returnType = method.getReturnType();
        boolean is = false;
        boolean get = false;
        boolean set = false;
        if (len > 2) {
            final char c0 = methodName.charAt(0);
            final char c2 = methodName.charAt(1);
            final char c3 = methodName.charAt(2);
            if (c0 == 'i' && c2 == 's') {
                is = (returnType == Boolean.class || returnType == Boolean.TYPE);
            }
            else if (c0 == 'g' && c2 == 'e' && c3 == 't') {
                get = (len > 3);
            }
            else if (c0 == 's' && c2 == 'e' && c3 == 't') {
                set = (len > 3 && method.getParameterCount() == 1);
            }
        }
        final Field[] fields = new Field[2];
        if (is || get || set) {
            final Class type = (is || get) ? returnType : method.getParameterTypes()[0];
            final int prefix = is ? 2 : 3;
            final char[] chars = new char[len - prefix];
            methodName.getChars(prefix, len, chars, 0);
            final char c4 = chars[0];
            String fieldName;
            int fieldNameLength;
            final Object o;
            final Object ooffset;
            final Class<?> clazz;
            final int n;
            final String s;
            final Object o2;
            declaredFields(objectClass, field -> {
                if (field.getDeclaringClass() != method.getDeclaringClass()) {
                    return;
                }
                else {
                    fieldName = field.getName();
                    fieldNameLength = fieldName.length();
                    if (fieldNameLength == o - ooffset && (field.getType() == clazz || clazz.isAssignableFrom(field.getType()))) {
                        if (n >= 65 && n <= 90 && n + 32 == fieldName.charAt(0) && fieldName.regionMatches(1, s, (int)(ooffset + 1), fieldNameLength - 1)) {
                            o2[0] = field;
                        }
                        else if (fieldName.regionMatches(0, s, (int)ooffset, fieldNameLength)) {
                            o2[1] = field;
                        }
                    }
                    return;
                }
            });
        }
        Field field2 = (fields[0] != null) ? fields[0] : fields[1];
        if (Throwable.class.isAssignableFrom(objectClass)) {
            if (returnType == String.class && ((field2 == null && methodName.equals("getMessage")) || (field2 == null && methodName.equals("getLocalizedMessage")))) {
                field2 = getDeclaredField(objectClass, "detailMessage");
            }
            else if (returnType == Throwable[].class && methodName.equals("getSuppressed")) {
                field2 = getDeclaredField(objectClass, "suppressedExceptions");
            }
        }
        return field2;
    }
    
    public static String getterName(final String methodName, String namingStrategy) {
        if (namingStrategy == null) {
            namingStrategy = "CamelCase";
        }
        final int methodNameLength = methodName.length();
        final boolean is = methodName.startsWith("is");
        final boolean get = methodName.startsWith("get");
        int prefixLength;
        if (is) {
            prefixLength = 2;
        }
        else if (get) {
            prefixLength = 3;
        }
        else {
            prefixLength = 0;
        }
        if (methodNameLength == prefixLength) {
            return methodName;
        }
        final String s = namingStrategy;
        switch (s) {
            case "NeverUseThisValueExceptDefaultValue":
            case "CamelCase": {
                final char[] chars = new char[methodNameLength - prefixLength];
                methodName.getChars(prefixLength, methodNameLength, chars, 0);
                final char c0 = chars[0];
                final boolean c1UCase = chars.length > 1 && chars[1] >= 'A' && chars[1] <= 'Z';
                if (c0 >= 'A' && c0 <= 'Z' && !c1UCase) {
                    chars[0] = (char)(c0 + ' ');
                }
                return new String(chars);
            }
            case "CamelCase1x": {
                final char[] chars = new char[methodNameLength - prefixLength];
                methodName.getChars(prefixLength, methodNameLength, chars, 0);
                final char c0 = chars[0];
                if (c0 >= 'A' && c0 <= 'Z') {
                    chars[0] = (char)(c0 + ' ');
                }
                return new String(chars);
            }
            case "PascalCase": {
                return pascal(methodName, methodNameLength, prefixLength);
            }
            case "SnakeCase": {
                return snakeCase(methodName, prefixLength);
            }
            case "UpperCaseWithUnderScores": {
                return underScores(methodName, prefixLength, true);
            }
            case "UpperCamelCaseWithSpaces": {
                return upperCamelWith(methodName, prefixLength, ' ');
            }
            case "UpperCase": {
                return methodName.substring(prefixLength).toUpperCase();
            }
            case "UpperCaseWithDashes": {
                return dashes(methodName, prefixLength, true);
            }
            case "UpperCaseWithDots": {
                return dots(methodName, prefixLength, true);
            }
            case "KebabCase": {
                final StringBuilder buf = new StringBuilder();
                int firstIndex;
                if (is) {
                    firstIndex = 2;
                }
                else if (get) {
                    firstIndex = 3;
                }
                else {
                    firstIndex = 0;
                }
                for (int i = firstIndex; i < methodName.length(); ++i) {
                    final char ch = methodName.charAt(i);
                    if (ch >= 'A' && ch <= 'Z') {
                        final char u = (char)(ch + ' ');
                        if (i > firstIndex) {
                            buf.append('-');
                        }
                        buf.append(u);
                    }
                    else {
                        buf.append(ch);
                    }
                }
                return buf.toString();
            }
            default: {
                throw new JSONException("TODO : " + namingStrategy);
            }
        }
    }
    
    private static String pascal(final String methodName, final int methodNameLength, final int prefixLength) {
        final char[] chars = new char[methodNameLength - prefixLength];
        methodName.getChars(prefixLength, methodNameLength, chars, 0);
        final char c0 = chars[0];
        if (c0 >= 'a' && c0 <= 'z' && chars.length > 1) {
            chars[0] = (char)(c0 - ' ');
        }
        else if (c0 == '_' && chars.length > 2) {
            final char c2 = chars[1];
            if (c2 >= 'a' && c2 <= 'z' && chars[2] >= 'a' && chars[2] <= 'z') {
                chars[1] = (char)(c2 - ' ');
            }
        }
        return new String(chars);
    }
    
    public static String fieldName(final String methodName, String namingStrategy) {
        if (namingStrategy == null) {
            namingStrategy = "CamelCase";
        }
        if (methodName == null || methodName.isEmpty()) {
            return methodName;
        }
        final String s = namingStrategy;
        switch (s) {
            case "NoChange":
            case "NeverUseThisValueExceptDefaultValue":
            case "CamelCase": {
                final char c0 = methodName.charAt(0);
                final char c2 = (methodName.length() > 1) ? methodName.charAt(1) : '\0';
                if (c0 >= 'A' && c0 <= 'Z' && methodName.length() > 1 && (c2 < 'A' || c2 > 'Z')) {
                    final char[] chars = methodName.toCharArray();
                    chars[0] = (char)(c0 + ' ');
                    return new String(chars);
                }
                return methodName;
            }
            case "CamelCase1x": {
                final char c0 = methodName.charAt(0);
                if (c0 >= 'A' && c0 <= 'Z' && methodName.length() > 1) {
                    final char[] chars2 = methodName.toCharArray();
                    chars2[0] = (char)(c0 + ' ');
                    return new String(chars2);
                }
                return methodName;
            }
            case "PascalCase": {
                final char c0 = methodName.charAt(0);
                char c2;
                if (c0 >= 'a' && c0 <= 'z' && methodName.length() > 1 && (c2 = methodName.charAt(1)) >= 'a' && c2 <= 'z') {
                    final char[] chars = methodName.toCharArray();
                    chars[0] = (char)(c0 - ' ');
                    return new String(chars);
                }
                if (c0 == '_' && methodName.length() > 1 && (c2 = methodName.charAt(1)) >= 'a' && c2 <= 'z') {
                    final char[] chars = methodName.toCharArray();
                    chars[1] = (char)(c2 - ' ');
                    return new String(chars);
                }
                return methodName;
            }
            case "SnakeCase": {
                return snakeCase(methodName, 0);
            }
            case "UpperCaseWithUnderScores": {
                return underScores(methodName, 0, true);
            }
            case "LowerCaseWithUnderScores": {
                return underScores(methodName, 0, false);
            }
            case "UpperCaseWithDashes": {
                return dashes(methodName, 0, true);
            }
            case "LowerCaseWithDashes": {
                return dashes(methodName, 0, false);
            }
            case "UpperCaseWithDots": {
                return dots(methodName, 0, true);
            }
            case "LowerCaseWithDots": {
                return dots(methodName, 0, false);
            }
            case "UpperCase": {
                return methodName.toUpperCase();
            }
            case "LowerCase": {
                return methodName.toLowerCase();
            }
            case "UpperCamelCaseWithSpaces": {
                return upperCamelWith(methodName, 0, ' ');
            }
            case "UpperCamelCaseWithUnderScores": {
                return upperCamelWith(methodName, 0, '_');
            }
            case "UpperCamelCaseWithDashes": {
                return upperCamelWith(methodName, 0, '-');
            }
            case "UpperCamelCaseWithDots": {
                return upperCamelWith(methodName, 0, '.');
            }
            case "KebabCase": {
                final StringBuilder buf = new StringBuilder();
                for (int i = 0; i < methodName.length(); ++i) {
                    final char ch = methodName.charAt(i);
                    if (ch >= 'A' && ch <= 'Z') {
                        final char u = (char)(ch + ' ');
                        if (i > 0) {
                            buf.append('-');
                        }
                        buf.append(u);
                    }
                    else {
                        buf.append(ch);
                    }
                }
                return buf.toString();
            }
            default: {
                throw new JSONException("TODO : " + namingStrategy);
            }
        }
    }
    
    static String snakeCase(final String methodName, final int prefixLength) {
        final int methodNameLength = methodName.length();
        char[] buf = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (buf == null) {
            buf = new char[128];
        }
        try {
            int off = 0;
            for (int i = prefixLength; i < methodNameLength; ++i) {
                final char ch = methodName.charAt(i);
                if (ch >= 'A' && ch <= 'Z') {
                    final char u = (char)(ch + ' ');
                    if (i > prefixLength) {
                        buf[off++] = '_';
                    }
                    buf[off++] = u;
                }
                else {
                    buf[off++] = ch;
                }
            }
            return new String(buf, 0, off);
        }
        finally {
            TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, buf);
        }
    }
    
    static String upperCamelWith(final String methodName, final int prefixLength, final char separator) {
        final int methodNameLength = methodName.length();
        char[] buf = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (buf == null) {
            buf = new char[128];
        }
        try {
            int off = 0;
            for (int i = prefixLength; i < methodNameLength; ++i) {
                final char ch = methodName.charAt(i);
                if (i == prefixLength) {
                    char c1;
                    if (ch >= 'a' && ch <= 'z' && i + 1 < methodNameLength && (c1 = methodName.charAt(i + 1)) >= 'a' && c1 <= 'z') {
                        buf[off++] = (char)(ch - ' ');
                    }
                    else if (ch == '_' && i + 1 < methodNameLength && (c1 = methodName.charAt(i + 1)) >= 'a' && c1 <= 'z') {
                        buf[off] = ch;
                        buf[off + 1] = (char)(c1 - ' ');
                        off += 2;
                        ++i;
                    }
                    else {
                        buf[off++] = ch;
                    }
                }
                else {
                    char c1;
                    if (ch >= 'A' && ch <= 'Z' && i + 1 < methodNameLength && ((c1 = methodName.charAt(i + 1)) < 'A' || c1 > 'Z')) {
                        if (i > prefixLength) {
                            buf[off++] = separator;
                        }
                        buf[off++] = ch;
                    }
                    else if (ch >= 'A' && ch <= 'Z' && i > prefixLength && i + 1 < methodNameLength && (c1 = methodName.charAt(i + 1)) >= 'A' && c1 <= 'Z' && (c1 = methodName.charAt(i - 1)) >= 'a' && c1 <= 'z') {
                        buf[off++] = separator;
                        buf[off++] = ch;
                    }
                    else {
                        buf[off++] = ch;
                    }
                }
            }
            return new String(buf, 0, off);
        }
        finally {
            TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, buf);
        }
    }
    
    static String underScores(final String methodName, final int prefixLength, final boolean upper) {
        final int methodNameLength = methodName.length();
        char[] buf = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (buf == null) {
            buf = new char[128];
        }
        try {
            int off = 0;
            for (int i = prefixLength; i < methodNameLength; ++i) {
                char ch = methodName.charAt(i);
                if (upper) {
                    if (ch < 'A' || ch > 'Z') {
                        if (ch >= 'a' && ch <= 'z') {
                            ch -= ' ';
                        }
                    }
                    else if (i > prefixLength) {
                        buf[off++] = '_';
                    }
                    buf[off++] = ch;
                }
                else if (ch >= 'A' && ch <= 'Z') {
                    if (i > prefixLength) {
                        buf[off++] = '_';
                    }
                    buf[off++] = (char)(ch + ' ');
                }
                else {
                    buf[off++] = ch;
                }
            }
            return new String(buf, 0, off);
        }
        finally {
            TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, buf);
        }
    }
    
    static String dashes(final String methodName, final int prefixLength, final boolean upper) {
        final int methodNameLength = methodName.length();
        char[] buf = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (buf == null) {
            buf = new char[128];
        }
        try {
            int off = 0;
            for (int i = prefixLength; i < methodNameLength; ++i) {
                char ch = methodName.charAt(i);
                if (upper) {
                    if (ch >= 'A' && ch <= 'Z') {
                        if (i > prefixLength) {
                            buf[off++] = '-';
                        }
                    }
                    else if (ch >= 'a' && ch <= 'z') {
                        ch -= ' ';
                    }
                    buf[off++] = ch;
                }
                else if (ch >= 'A' && ch <= 'Z') {
                    if (i > prefixLength) {
                        buf[off++] = '-';
                    }
                    buf[off++] = (char)(ch + ' ');
                }
                else {
                    buf[off++] = ch;
                }
            }
            return new String(buf, 0, off);
        }
        finally {
            TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, buf);
        }
    }
    
    static String dots(final String methodName, final int prefixLength, final boolean upper) {
        final int methodNameLength = methodName.length();
        char[] buf = TypeUtils.CHARS_UPDATER.getAndSet(TypeUtils.CACHE, null);
        if (buf == null) {
            buf = new char[128];
        }
        try {
            int off = 0;
            for (int i = prefixLength; i < methodNameLength; ++i) {
                char ch = methodName.charAt(i);
                if (upper) {
                    if (ch >= 'A' && ch <= 'Z') {
                        if (i > prefixLength) {
                            buf[off++] = '.';
                        }
                    }
                    else if (ch >= 'a' && ch <= 'z') {
                        ch -= ' ';
                    }
                    buf[off++] = ch;
                }
                else if (ch >= 'A' && ch <= 'Z') {
                    if (i > prefixLength) {
                        buf[off++] = '.';
                    }
                    buf[off++] = (char)(ch + ' ');
                }
                else {
                    buf[off++] = ch;
                }
            }
            return new String(buf, 0, off);
        }
        finally {
            TypeUtils.CHARS_UPDATER.set(TypeUtils.CACHE, buf);
        }
    }
    
    public static Type getFieldType(TypeReference typeReference, Class<?> raw, final Member field, final Type fieldType) {
        Class<?> declaringClass;
        if (field == null) {
            declaringClass = null;
        }
        else {
            declaringClass = field.getDeclaringClass();
        }
        while (raw != Object.class) {
            final Type type = (typeReference == null) ? null : typeReference.getType();
            if (declaringClass == raw) {
                return resolve(type, declaringClass, fieldType);
            }
            final Type superType = raw.getGenericSuperclass();
            if (superType == null) {
                break;
            }
            typeReference = TypeReference.get(resolve(type, raw, superType));
            raw = (Class<?>)typeReference.getRawType();
        }
        return null;
    }
    
    public static Type getParamType(TypeReference type, Class<?> raw, final Class declaringClass, final Parameter field, final Type fieldType) {
        while (raw != Object.class) {
            if (declaringClass == raw) {
                return resolve(type.getType(), declaringClass, fieldType);
            }
            type = TypeReference.get(resolve(type.getType(), raw, raw.getGenericSuperclass()));
            raw = (Class<?>)type.getRawType();
        }
        return null;
    }
    
    public static ParameterizedType newParameterizedTypeWithOwner(final Type ownerType, final Type rawType, final Type... typeArguments) {
        return new ParameterizedTypeImpl(ownerType, rawType, typeArguments);
    }
    
    public static GenericArrayType arrayOf(final Type componentType) {
        return new GenericArrayTypeImpl(componentType);
    }
    
    public static WildcardType subtypeOf(final Type bound) {
        Type[] upperBounds;
        if (bound instanceof WildcardType) {
            upperBounds = ((WildcardType)bound).getUpperBounds();
        }
        else {
            upperBounds = new Type[] { bound };
        }
        return new WildcardTypeImpl(upperBounds, BeanUtils.EMPTY_TYPE_ARRAY);
    }
    
    public static WildcardType supertypeOf(final Type bound) {
        Type[] lowerBounds;
        if (bound instanceof WildcardType) {
            lowerBounds = ((WildcardType)bound).getLowerBounds();
        }
        else {
            lowerBounds = new Type[] { bound };
        }
        return new WildcardTypeImpl(new Type[] { Object.class }, lowerBounds);
    }
    
    public static Type canonicalize(final Type type) {
        if (type instanceof Class) {
            final Class<?> c = (Class<?>)type;
            return (Type)(c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c);
        }
        if (type instanceof ParameterizedType) {
            final ParameterizedType p = (ParameterizedType)type;
            return new ParameterizedTypeImpl(p.getOwnerType(), p.getRawType(), p.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            final GenericArrayType g = (GenericArrayType)type;
            return new GenericArrayTypeImpl(g.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            final WildcardType w = (WildcardType)type;
            return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());
        }
        return type;
    }
    
    public static Class<?> getRawType(final Type type) {
        if (type instanceof Class) {
            return (Class<?>)type;
        }
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final Type rawType = parameterizedType.getRawType();
            checkArgument(rawType instanceof Class);
            return (Class<?>)rawType;
        }
        if (type instanceof GenericArrayType) {
            final Type componentType = ((GenericArrayType)type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType)type).getUpperBounds()[0]);
        }
        final String className = (type == null) ? "null" : type.getClass().getName();
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + className);
    }
    
    static boolean equal(final Object a, final Object b) {
        return Objects.equals(a, b);
    }
    
    public static boolean equals(final Type a, final Type b) {
        if (a == b) {
            return true;
        }
        if (a instanceof Class) {
            return a.equals(b);
        }
        if (a instanceof ParameterizedType) {
            if (!(b instanceof ParameterizedType)) {
                return false;
            }
            final ParameterizedType pa = (ParameterizedType)a;
            final ParameterizedType pb = (ParameterizedType)b;
            return equal(pa.getOwnerType(), pb.getOwnerType()) && pa.getRawType().equals(pb.getRawType()) && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());
        }
        else if (a instanceof GenericArrayType) {
            if (!(b instanceof GenericArrayType)) {
                return false;
            }
            final GenericArrayType ga = (GenericArrayType)a;
            final GenericArrayType gb = (GenericArrayType)b;
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType());
        }
        else if (a instanceof WildcardType) {
            if (!(b instanceof WildcardType)) {
                return false;
            }
            final WildcardType wa = (WildcardType)a;
            final WildcardType wb = (WildcardType)b;
            return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds()) && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());
        }
        else {
            if (!(a instanceof TypeVariable)) {
                return false;
            }
            if (!(b instanceof TypeVariable)) {
                return false;
            }
            final TypeVariable<?> va = (TypeVariable<?>)a;
            final TypeVariable<?> vb = (TypeVariable<?>)b;
            return va.getGenericDeclaration() == vb.getGenericDeclaration() && va.getName().equals(vb.getName());
        }
    }
    
    static int hashCodeOrZero(final Object o) {
        return (o != null) ? o.hashCode() : 0;
    }
    
    public static String typeToString(final Type type) {
        return (type instanceof Class) ? ((Class)type).getName() : type.toString();
    }
    
    static Type getGenericSupertype(final Type context, Class<?> rawType, final Class<?> toResolve) {
        if (toResolve == rawType) {
            return context;
        }
        if (toResolve.isInterface()) {
            final Class<?>[] interfaces = rawType.getInterfaces();
            for (int i = 0, length = interfaces.length; i < length; ++i) {
                if (interfaces[i] == toResolve) {
                    return rawType.getGenericInterfaces()[i];
                }
                if (toResolve.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(rawType.getGenericInterfaces()[i], interfaces[i], toResolve);
                }
            }
        }
        if (rawType != null && !rawType.isInterface()) {
            while (rawType != Object.class) {
                final Class<?> rawSupertype = rawType.getSuperclass();
                if (rawSupertype == toResolve) {
                    return rawType.getGenericSuperclass();
                }
                if (toResolve.isAssignableFrom(rawSupertype)) {
                    return getGenericSupertype(rawType.getGenericSuperclass(), rawSupertype, toResolve);
                }
                rawType = rawSupertype;
            }
        }
        return toResolve;
    }
    
    public static Type resolve(final Type context, final Class<?> contextRawType, final Type toResolve) {
        return resolve(context, contextRawType, toResolve, new HashMap<TypeVariable<?>, Type>());
    }
    
    private static Type resolve(final Type context, final Class<?> contextRawType, Type toResolve, final Map<TypeVariable<?>, Type> visitedTypeVariables) {
        TypeVariable<?> resolving = null;
        while (true) {
            while (toResolve instanceof TypeVariable) {
                final TypeVariable<?> typeVariable = (TypeVariable<?>)toResolve;
                final Type previouslyResolved = visitedTypeVariables.get(typeVariable);
                if (previouslyResolved != null) {
                    return (previouslyResolved == Void.TYPE) ? toResolve : previouslyResolved;
                }
                visitedTypeVariables.put(typeVariable, Void.TYPE);
                if (resolving == null) {
                    resolving = typeVariable;
                }
                toResolve = resolveTypeVariable(context, contextRawType, typeVariable);
                if (toResolve == typeVariable) {
                    if (resolving != null) {
                        visitedTypeVariables.put(resolving, toResolve);
                    }
                    return toResolve;
                }
            }
            if (toResolve instanceof Class && ((Class)toResolve).isArray()) {
                final Class<?> original = (Class<?>)toResolve;
                final Type componentType = original.getComponentType();
                final Type newComponentType = resolve(context, contextRawType, componentType, visitedTypeVariables);
                toResolve = (Type)(equal(componentType, newComponentType) ? original : arrayOf(newComponentType));
                continue;
            }
            if (toResolve instanceof GenericArrayType) {
                final GenericArrayType original2 = (GenericArrayType)toResolve;
                final Type componentType = original2.getGenericComponentType();
                final Type newComponentType = resolve(context, contextRawType, componentType, visitedTypeVariables);
                toResolve = (equal(componentType, newComponentType) ? original2 : arrayOf(newComponentType));
                continue;
            }
            if (toResolve instanceof ParameterizedType) {
                final ParameterizedType original3 = (ParameterizedType)toResolve;
                final Type ownerType = original3.getOwnerType();
                final Type newOwnerType = resolve(context, contextRawType, ownerType, visitedTypeVariables);
                boolean changed = !equal(newOwnerType, ownerType);
                Type[] args = original3.getActualTypeArguments();
                for (int t = 0, length = args.length; t < length; ++t) {
                    final Type resolvedTypeArgument = resolve(context, contextRawType, args[t], visitedTypeVariables);
                    if (!equal(resolvedTypeArgument, args[t])) {
                        if (!changed) {
                            args = args.clone();
                            changed = true;
                        }
                        args[t] = resolvedTypeArgument;
                    }
                }
                toResolve = (changed ? newParameterizedTypeWithOwner(newOwnerType, original3.getRawType(), args) : original3);
                continue;
            }
            if (!(toResolve instanceof WildcardType)) {
                continue;
            }
            final WildcardType original4 = (WildcardType)toResolve;
            final Type[] originalLowerBound = original4.getLowerBounds();
            final Type[] originalUpperBound = original4.getUpperBounds();
            if (originalLowerBound.length == 1) {
                final Type lowerBound = resolve(context, contextRawType, originalLowerBound[0], visitedTypeVariables);
                if (lowerBound != originalLowerBound[0]) {
                    toResolve = supertypeOf(lowerBound);
                }
                continue;
            }
            else {
                if (originalUpperBound.length != 1) {
                    continue;
                }
                final Type upperBound = resolve(context, contextRawType, originalUpperBound[0], visitedTypeVariables);
                if (upperBound != originalUpperBound[0]) {
                    toResolve = subtypeOf(upperBound);
                }
                continue;
            }
            break;
        }
    }
    
    static Type resolveTypeVariable(final Type context, final Class<?> contextRawType, final TypeVariable<?> unknown) {
        final Class<?> declaredByRaw = declaringClassOf(unknown);
        if (declaredByRaw == null) {
            return unknown;
        }
        final Type declaredBy = getGenericSupertype(context, contextRawType, declaredByRaw);
        if (declaredBy instanceof ParameterizedType) {
            final int index = indexOf(declaredByRaw.getTypeParameters(), unknown);
            return ((ParameterizedType)declaredBy).getActualTypeArguments()[index];
        }
        return unknown;
    }
    
    private static int indexOf(final Object[] array, final Object toFind) {
        for (int i = 0, length = array.length; i < length; ++i) {
            if (toFind.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }
    
    private static Class<?> declaringClassOf(final TypeVariable<?> typeVariable) {
        final GenericDeclaration genericDeclaration = (GenericDeclaration)typeVariable.getGenericDeclaration();
        return (Class<?>)((genericDeclaration instanceof Class) ? ((Class)genericDeclaration) : null);
    }
    
    static void checkNotPrimitive(final Type type) {
        checkArgument(!(type instanceof Class) || !((Class)type).isPrimitive());
    }
    
    public static <A extends Annotation> A findAnnotation(final AnnotatedElement element, final Class<A> annotationType) {
        if (annotationType == null) {
            throw new NullPointerException("annotationType must not be null");
        }
        final boolean inherited = annotationType.isAnnotationPresent(Inherited.class);
        return findAnnotation(element, annotationType, inherited, new HashSet<Annotation>());
    }
    
    public static <A extends Annotation> A findAnnotation(final Annotation annotation, final Class<A> annotationType) {
        if (annotation == null) {
            throw new NullPointerException("annotation must not be null");
        }
        if (annotationType == null) {
            throw new NullPointerException("annotationType must not be null");
        }
        final Class<? extends Annotation> annotationTypeClass = annotation.annotationType();
        if (annotationTypeClass == annotationType) {
            return (A)annotation;
        }
        final boolean inherited = annotationType.isAnnotationPresent(Inherited.class);
        return findAnnotation(annotationTypeClass, annotationType, inherited, new HashSet<Annotation>());
    }
    
    private static <A extends Annotation> A findAnnotation(final AnnotatedElement element, final Class<A> annotationType, final boolean inherited, final Set<Annotation> visited) {
        if (element == null || annotationType == null) {
            return null;
        }
        final A annotation = element.getDeclaredAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        final Annotation[] declaredAnnotations = element.getDeclaredAnnotations();
        final A directMetaAnnotation = findMetaAnnotation(annotationType, declaredAnnotations, inherited, visited);
        if (directMetaAnnotation != null) {
            return directMetaAnnotation;
        }
        if (element instanceof Class) {
            final Class<?> clazz = (Class<?>)element;
            for (final Class<?> ifc : clazz.getInterfaces()) {
                if (ifc != Annotation.class) {
                    final A annotationOnInterface = (A)findAnnotation(ifc, (Class<Annotation>)annotationType, inherited, visited);
                    if (annotationOnInterface != null) {
                        return annotationOnInterface;
                    }
                }
            }
            if (inherited) {
                final Class<?> superclass = clazz.getSuperclass();
                if (superclass != null && superclass != Object.class) {
                    final A annotationOnSuperclass = (A)findAnnotation(superclass, (Class<Annotation>)annotationType, true, visited);
                    if (annotationOnSuperclass != null) {
                        return annotationOnSuperclass;
                    }
                }
            }
        }
        return findMetaAnnotation(annotationType, getAnnotations(element), inherited, visited);
    }
    
    private static <A extends Annotation> A findMetaAnnotation(final Class<A> annotationType, final Annotation[] candidates, final boolean inherited, final Set<Annotation> visited) {
        for (final Annotation candidateAnnotation : candidates) {
            final Class<? extends Annotation> candidateAnnotationType = candidateAnnotation.annotationType();
            final String name = candidateAnnotationType.getName();
            final boolean isInJavaLangAnnotationPackage = name.startsWith("java.lang.annotation") || name.startsWith("kotlin.");
            if (!isInJavaLangAnnotationPackage && visited.add(candidateAnnotation)) {
                final A metaAnnotation = findAnnotation(candidateAnnotationType, annotationType, inherited, visited);
                if (metaAnnotation != null) {
                    return metaAnnotation;
                }
            }
        }
        return null;
    }
    
    public static Annotation[] getAnnotations(final AnnotatedElement element) {
        try {
            return element.getDeclaredAnnotations();
        }
        catch (Throwable ignored) {
            return new Annotation[0];
        }
    }
    
    static void checkArgument(final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }
    
    public static void processJacksonJsonIgnore(final FieldInfo fieldInfo, final Annotation annotation) {
        fieldInfo.ignore = true;
        final Class<? extends Annotation> annotationClass = annotation.getClass();
        final String name;
        Object result;
        annotationMethods(annotationClass, m -> {
            name = m.getName();
            try {
                result = m.invoke(annotation, new Object[0]);
                if (name.equals("value")) {
                    fieldInfo.ignore = (boolean)result;
                }
            }
            catch (Throwable t) {}
        });
    }
    
    public static boolean isNoneStaticMemberClass(final Class objectClass, final Class memberClass) {
        if (memberClass == null || memberClass.isPrimitive() || memberClass == String.class || memberClass == List.class) {
            return false;
        }
        final Class enclosingClass = memberClass.getEnclosingClass();
        if (enclosingClass == null) {
            return false;
        }
        if (objectClass != null && !objectClass.equals(enclosingClass)) {
            return false;
        }
        Constructor[] constructors = BeanUtils.constructorCache.get(memberClass);
        if (constructors == null) {
            constructors = memberClass.getDeclaredConstructors();
            BeanUtils.constructorCache.putIfAbsent(memberClass, constructors);
        }
        if (constructors.length == 0) {
            return false;
        }
        final Constructor firstConstructor = constructors[0];
        if (firstConstructor.getParameterCount() == 0) {
            return false;
        }
        final Class[] parameterTypes = firstConstructor.getParameterTypes();
        return enclosingClass.equals(parameterTypes[0]);
    }
    
    public static void setNoneStaticMemberClassParent(final Object object, final Object parent) {
        final Class objectClass = object.getClass();
        Field[] fields = BeanUtils.declaredFieldCache.get(objectClass);
        if (fields == null) {
            final Field[] declaredFields = objectClass.getDeclaredFields();
            boolean allMatch = true;
            for (final Field field : declaredFields) {
                final int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                fields = declaredFields;
            }
            else {
                final List<Field> list = new ArrayList<Field>(declaredFields.length);
                for (final Field field2 : declaredFields) {
                    final int modifiers2 = field2.getModifiers();
                    if (!Modifier.isStatic(modifiers2)) {
                        list.add(field2);
                    }
                }
                fields = list.toArray(new Field[list.size()]);
            }
            BeanUtils.fieldCache.putIfAbsent(objectClass, fields);
        }
        Field this0 = null;
        for (final Field field3 : fields) {
            if ("this$0".equals(field3.getName())) {
                this0 = field3;
            }
        }
        if (this0 != null) {
            this0.setAccessible(true);
            try {
                this0.set(object, parent);
            }
            catch (IllegalAccessException e) {
                throw new JSONException("setNoneStaticMemberClassParent error, class " + objectClass);
            }
        }
    }
    
    public static void cleanupCache(final Class objectClass) {
        if (objectClass == null) {
            return;
        }
        BeanUtils.fieldCache.remove(objectClass);
        BeanUtils.fieldMapCache.remove(objectClass);
        BeanUtils.declaredFieldCache.remove(objectClass);
        BeanUtils.methodCache.remove(objectClass);
        BeanUtils.constructorCache.remove(objectClass);
    }
    
    public static void cleanupCache(final ClassLoader classLoader) {
        Iterator<Map.Entry<Class, Field[]>> it = (Iterator<Map.Entry<Class, Field[]>>)BeanUtils.fieldCache.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<Class, Field[]> entry = it.next();
            final Class entryKey = entry.getKey();
            if (entryKey.getClassLoader() == classLoader) {
                it.remove();
            }
        }
        final Iterator<Map.Entry<Class, Map<String, Field>>> it2 = (Iterator<Map.Entry<Class, Map<String, Field>>>)BeanUtils.fieldMapCache.entrySet().iterator();
        while (it2.hasNext()) {
            final Map.Entry<Class, Map<String, Field>> entry2 = it2.next();
            final Class entryKey = entry2.getKey();
            if (entryKey.getClassLoader() == classLoader) {
                it2.remove();
            }
        }
        it = (Iterator<Map.Entry<Class, Field[]>>)BeanUtils.declaredFieldCache.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<Class, Field[]> entry = it.next();
            final Class entryKey = entry.getKey();
            if (entryKey.getClassLoader() == classLoader) {
                it.remove();
            }
        }
        final Iterator<Map.Entry<Class, Method[]>> it3 = (Iterator<Map.Entry<Class, Method[]>>)BeanUtils.methodCache.entrySet().iterator();
        while (it3.hasNext()) {
            final Map.Entry<Class, Method[]> entry3 = it3.next();
            final Class entryKey = entry3.getKey();
            if (entryKey.getClassLoader() == classLoader) {
                it3.remove();
            }
        }
        final Iterator<Map.Entry<Class, Constructor[]>> it4 = (Iterator<Map.Entry<Class, Constructor[]>>)BeanUtils.constructorCache.entrySet().iterator();
        while (it4.hasNext()) {
            final Map.Entry<Class, Constructor[]> entry4 = it4.next();
            final Class entryKey = entry4.getKey();
            if (entryKey.getClassLoader() == classLoader) {
                it4.remove();
            }
        }
    }
    
    public static void processJSONType1x(final BeanInfo beanInfo, final Annotation jsonType1x, final Method method) {
        try {
            final Object result = method.invoke(jsonType1x, new Object[0]);
            final String name = method.getName();
            switch (name) {
                case "seeAlso": {
                    final Class<?>[] classes = (Class<?>[])result;
                    if (classes.length != 0) {
                        beanInfo.seeAlso = classes;
                        break;
                    }
                    break;
                }
                case "typeName": {
                    final String typeName = (String)result;
                    if (!typeName.isEmpty()) {
                        beanInfo.typeName = typeName;
                        break;
                    }
                    break;
                }
                case "typeKey": {
                    final String typeKey = (String)result;
                    if (!typeKey.isEmpty()) {
                        beanInfo.typeKey = typeKey;
                        break;
                    }
                    break;
                }
                case "alphabetic": {
                    final Boolean alphabetic = (Boolean)result;
                    if (!alphabetic) {
                        beanInfo.alphabetic = false;
                        break;
                    }
                    break;
                }
                case "serializeFeatures":
                case "serialzeFeatures": {
                    final Enum[] array;
                    final Enum[] serializeFeatures = array = (Enum[])result;
                    for (final Enum feature : array) {
                        final String name2 = feature.name();
                        switch (name2) {
                            case "WriteMapNullValue": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.WriteNulls.mask;
                                break;
                            }
                            case "WriteNullListAsEmpty": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.WriteNullListAsEmpty.mask;
                                break;
                            }
                            case "WriteNullStringAsEmpty": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.WriteNullStringAsEmpty.mask;
                                break;
                            }
                            case "WriteNullNumberAsZero": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.WriteNullNumberAsZero.mask;
                                break;
                            }
                            case "WriteNullBooleanAsFalse": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.WriteNullBooleanAsFalse.mask;
                                break;
                            }
                            case "BrowserCompatible": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.BrowserCompatible.mask;
                                break;
                            }
                            case "WriteClassName": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.WriteClassName.mask;
                                break;
                            }
                            case "WriteNonStringValueAsString": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.WriteNonStringValueAsString.mask;
                                break;
                            }
                            case "WriteEnumUsingToString": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.WriteEnumUsingToString.mask;
                                break;
                            }
                            case "NotWriteRootClassName": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.NotWriteRootClassName.mask;
                                break;
                            }
                            case "IgnoreErrorGetter": {
                                beanInfo.writerFeatures |= JSONWriter.Feature.IgnoreErrorGetter.mask;
                                break;
                            }
                        }
                    }
                    break;
                }
                case "serializeEnumAsJavaBean": {
                    final boolean serializeEnumAsJavaBean = (boolean)result;
                    if (serializeEnumAsJavaBean) {
                        beanInfo.writeEnumAsJavaBean = true;
                        break;
                    }
                    break;
                }
                case "naming": {
                    final Enum naming = (Enum)result;
                    beanInfo.namingStrategy = naming.name();
                    break;
                }
                case "ignores": {
                    final String[] fields = (String[])result;
                    if (fields.length == 0) {
                        break;
                    }
                    if (beanInfo.ignores == null) {
                        beanInfo.ignores = fields;
                        break;
                    }
                    final LinkedHashSet<String> ignoresSet = new LinkedHashSet<String>();
                    ignoresSet.addAll((Collection<?>)Arrays.asList(beanInfo.ignores));
                    ignoresSet.addAll((Collection<?>)Arrays.asList(fields));
                    beanInfo.ignores = ignoresSet.toArray(new String[ignoresSet.size()]);
                    break;
                }
                case "includes": {
                    final String[] fields = (String[])result;
                    if (fields.length != 0) {
                        beanInfo.includes = fields;
                        break;
                    }
                    break;
                }
                case "orders": {
                    final String[] fields = (String[])result;
                    if (fields.length != 0) {
                        beanInfo.orders = fields;
                        break;
                    }
                    break;
                }
            }
        }
        catch (Throwable t) {}
    }
    
    public static void processJacksonJsonFormat(final FieldInfo fieldInfo, final Annotation annotation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //     4: astore_2        /* annotationClass */
        //     5: aload_2         /* annotationClass */
        //     6: aload_1         /* annotation */
        //     7: aload_0         /* fieldInfo */
        //     8: invokedynamic   BootstrapMethod #8, accept:(Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/FieldInfo;)Ljava/util/function/Consumer;
        //    13: invokestatic    com/alibaba/fastjson2/util/BeanUtils.annotationMethods:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
        //    16: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static void processJacksonJsonFormat(final BeanInfo beanInfo, final Annotation annotation) {
        final Class<? extends Annotation> annotationClass = annotation.getClass();
        final String name;
        Object result;
        String pattern;
        annotationMethods(annotationClass, m -> {
            name = m.getName();
            try {
                result = m.invoke(annotation, new Object[0]);
                if (name.equals("pattern")) {
                    pattern = (String)result;
                    if (pattern.length() != 0) {
                        beanInfo.format = pattern;
                    }
                }
            }
            catch (Throwable t) {}
        });
    }
    
    public static void processJacksonJsonInclude(final BeanInfo beanInfo, final Annotation annotation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //     4: astore_2        /* annotationClass */
        //     5: aload_2         /* annotationClass */
        //     6: aload_1         /* annotation */
        //     7: aload_0         /* beanInfo */
        //     8: invokedynamic   BootstrapMethod #10, accept:(Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/BeanInfo;)Ljava/util/function/Consumer;
        //    13: invokestatic    com/alibaba/fastjson2/util/BeanUtils.annotationMethods:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
        //    16: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static void processJacksonJsonTypeName(final BeanInfo beanInfo, final Annotation annotation) {
        final Class<? extends Annotation> annotationClass = annotation.getClass();
        final String name;
        Object result;
        String value;
        annotationMethods(annotationClass, m -> {
            name = m.getName();
            try {
                result = m.invoke(annotation, new Object[0]);
                if (name.equals("value")) {
                    value = (String)result;
                    if (!value.isEmpty()) {
                        beanInfo.typeName = value;
                    }
                }
            }
            catch (Throwable t) {}
        });
    }
    
    public static void processJacksonJsonSubTypesType(final BeanInfo beanInfo, final int index, final Annotation annotation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //     4: astore_3        /* annotationClass */
        //     5: aload_3         /* annotationClass */
        //     6: aload_2         /* annotation */
        //     7: aload_0         /* beanInfo */
        //     8: iload_1         /* index */
        //     9: invokedynamic   BootstrapMethod #12, accept:(Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/BeanInfo;I)Ljava/util/function/Consumer;
        //    14: invokestatic    com/alibaba/fastjson2/util/BeanUtils.annotationMethods:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
        //    17: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static void processGsonSerializedName(final FieldInfo fieldInfo, final Annotation annotation) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //     4: astore_2        /* annotationClass */
        //     5: aload_2         /* annotationClass */
        //     6: aload_1         /* annotation */
        //     7: aload_0         /* fieldInfo */
        //     8: invokedynamic   BootstrapMethod #13, accept:(Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/FieldInfo;)Ljava/util/function/Consumer;
        //    13: invokestatic    com/alibaba/fastjson2/util/BeanUtils.annotationMethods:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
        //    16: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static boolean isExtendedMap(final Class objectClass) {
        if (objectClass == HashMap.class || objectClass == LinkedHashMap.class || objectClass == TreeMap.class || "".equals(objectClass.getSimpleName())) {
            return false;
        }
        final Class superclass = objectClass.getSuperclass();
        if (superclass != HashMap.class && superclass != LinkedHashMap.class && superclass != TreeMap.class) {
            return false;
        }
        final Constructor defaultConstructor = getDefaultConstructor(objectClass, false);
        if (defaultConstructor != null) {
            return false;
        }
        final List<Field> fields = new ArrayList<Field>();
        final int modifiers;
        final Class<?> clazz;
        final List<Field> list;
        declaredFields(objectClass, field -> {
            modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || field.getDeclaringClass().isAssignableFrom(clazz) || field.getName().equals("this$0")) {
                return;
            }
            else {
                list.add(field);
                return;
            }
        });
        return !fields.isEmpty();
    }
    
    static {
        EMPTY_TYPE_ARRAY = new Type[0];
        fieldCache = new ConcurrentHashMap<Class, Field[]>();
        fieldMapCache = new ConcurrentHashMap<Class, Map<String, Field>>();
        declaredFieldCache = new ConcurrentHashMap<Class, Field[]>();
        methodCache = new ConcurrentHashMap<Class, Method[]>();
        constructorCache = new ConcurrentHashMap<Class, Constructor[]>();
        IGNORE_CLASS_HASH_CODES = new long[] { -9214723784238596577L, -9030616758866828325L, -8335274122997354104L, -6963030519018899258L, -4863137578837233966L, -3653547262287832698L, -2819277587813726773L, -2291619803571459675L, -1811306045128064037L, -864440709753525476L, 8731803887940231L, 1616814008855344660L, 2164749833121980361L, 3724195282986200606L, 3977020351318456359L, 4882459834864833642L, 7981148566008458638L, 8344106065386396833L };
    }
    
    static final class ParameterizedTypeImpl implements ParameterizedType, Serializable
    {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;
        private static final long serialVersionUID = 0L;
        
        public ParameterizedTypeImpl(final Type ownerType, final Type rawType, final Type... typeArguments) {
            if (rawType instanceof Class) {
                final Class<?> rawTypeAsClass = (Class<?>)rawType;
                final boolean isStaticOrTopLevelClass = Modifier.isStatic(rawTypeAsClass.getModifiers()) || rawTypeAsClass.getEnclosingClass() == null;
                BeanUtils.checkArgument(ownerType != null || isStaticOrTopLevelClass);
            }
            this.ownerType = ((ownerType == null) ? null : BeanUtils.canonicalize(ownerType));
            this.rawType = BeanUtils.canonicalize(rawType);
            this.typeArguments = typeArguments.clone();
            for (int t = 0, length = this.typeArguments.length; t < length; ++t) {
                BeanUtils.checkNotPrimitive(this.typeArguments[t]);
                this.typeArguments[t] = BeanUtils.canonicalize(this.typeArguments[t]);
            }
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return this.typeArguments.clone();
        }
        
        @Override
        public Type getRawType() {
            return this.rawType;
        }
        
        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }
        
        @Override
        public boolean equals(final Object other) {
            return other instanceof ParameterizedType && BeanUtils.equals(this, (Type)other);
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ BeanUtils.hashCodeOrZero(this.ownerType);
        }
        
        @Override
        public String toString() {
            final int length = this.typeArguments.length;
            if (length == 0) {
                return BeanUtils.typeToString(this.rawType);
            }
            final StringBuilder stringBuilder = new StringBuilder(30 * (length + 1));
            stringBuilder.append(BeanUtils.typeToString(this.rawType)).append("<").append(BeanUtils.typeToString(this.typeArguments[0]));
            for (int i = 1; i < length; ++i) {
                stringBuilder.append(", ").append(BeanUtils.typeToString(this.typeArguments[i]));
            }
            return stringBuilder.append(">").toString();
        }
    }
    
    public static final class GenericArrayTypeImpl implements GenericArrayType, Serializable
    {
        private final Type componentType;
        private static final long serialVersionUID = 0L;
        
        public GenericArrayTypeImpl(final Type componentType) {
            this.componentType = BeanUtils.canonicalize(componentType);
        }
        
        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof GenericArrayType && BeanUtils.equals(this, (Type)o);
        }
        
        @Override
        public int hashCode() {
            return this.componentType.hashCode();
        }
        
        @Override
        public String toString() {
            return BeanUtils.typeToString(this.componentType) + "[]";
        }
    }
    
    static final class WildcardTypeImpl implements WildcardType, Serializable
    {
        private final Type upperBound;
        private final Type lowerBound;
        private static final long serialVersionUID = 0L;
        
        public WildcardTypeImpl(final Type[] upperBounds, final Type[] lowerBounds) {
            BeanUtils.checkArgument(lowerBounds.length <= 1);
            BeanUtils.checkArgument(upperBounds.length == 1);
            if (lowerBounds.length == 1) {
                BeanUtils.checkNotPrimitive(lowerBounds[0]);
                BeanUtils.checkArgument(upperBounds[0] == Object.class);
                this.lowerBound = BeanUtils.canonicalize(lowerBounds[0]);
                this.upperBound = Object.class;
            }
            else {
                BeanUtils.checkNotPrimitive(upperBounds[0]);
                this.lowerBound = null;
                this.upperBound = BeanUtils.canonicalize(upperBounds[0]);
            }
        }
        
        @Override
        public Type[] getUpperBounds() {
            return new Type[] { this.upperBound };
        }
        
        @Override
        public Type[] getLowerBounds() {
            return (this.lowerBound != null) ? new Type[] { this.lowerBound } : BeanUtils.EMPTY_TYPE_ARRAY;
        }
        
        @Override
        public boolean equals(final Object other) {
            return other instanceof WildcardType && BeanUtils.equals(this, (Type)other);
        }
        
        @Override
        public int hashCode() {
            return ((this.lowerBound != null) ? (31 + this.lowerBound.hashCode()) : 1) ^ 31 + this.upperBound.hashCode();
        }
        
        @Override
        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + BeanUtils.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + BeanUtils.typeToString(this.upperBound);
        }
    }
}
