// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.Objects;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.codec.FieldInfo;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.JSONWriter;
import java.lang.annotation.Annotation;
import com.alibaba.fastjson2.util.KotlinUtils;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.annotation.JSONCompiler;
import com.alibaba.fastjson2.annotation.JSONType;
import java.lang.reflect.AnnotatedElement;
import com.alibaba.fastjson2.modules.ObjectCodecProvider;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import com.alibaba.fastjson2.codec.BeanInfo;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.io.File;
import java.net.URL;
import java.net.URI;
import com.alibaba.fastjson2.JSONPObject;
import java.util.TimeZone;
import java.util.Currency;
import java.util.UUID;
import java.text.DecimalFormat;
import java.lang.reflect.Modifier;
import com.alibaba.fastjson2.JSONPath;
import java.lang.reflect.Member;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.OptionalInt;
import java.util.BitSet;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.file.Path;
import com.alibaba.fastjson2.util.BeanUtils;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import com.alibaba.fastjson2.util.ApacheLang3Support;
import com.alibaba.fastjson2.support.money.MoneySupport;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.Locale;
import com.alibaba.fastjson2.util.JodaSupport;
import com.alibaba.fastjson2.util.JdbcSupport;
import com.alibaba.fastjson2.modules.ObjectWriterAnnotationProcessor;
import com.alibaba.fastjson2.modules.ObjectWriterModule;

public class ObjectWriterBaseModule implements ObjectWriterModule
{
    static ObjectWriterAdapter STACK_TRACE_ELEMENT_WRITER;
    final ObjectWriterProvider provider;
    final WriterAnnotationProcessor annotationProcessor;
    
    public ObjectWriterBaseModule(final ObjectWriterProvider provider) {
        this.provider = provider;
        this.annotationProcessor = new WriterAnnotationProcessor();
    }
    
    @Override
    public ObjectWriterProvider getProvider() {
        return this.provider;
    }
    
    @Override
    public ObjectWriterAnnotationProcessor getAnnotationProcessor() {
        return this.annotationProcessor;
    }
    
    ObjectWriter getExternalObjectWriter(final String className, final Class objectClass) {
        switch (className) {
            case "java.sql.Time": {
                return JdbcSupport.createTimeWriter(null);
            }
            case "java.sql.Timestamp": {
                return JdbcSupport.createTimestampWriter(objectClass, null);
            }
            case "org.joda.time.chrono.GregorianChronology": {
                return JodaSupport.createGregorianChronologyWriter(objectClass);
            }
            case "org.joda.time.chrono.ISOChronology": {
                return JodaSupport.createISOChronologyWriter(objectClass);
            }
            case "org.joda.time.LocalDate": {
                return JodaSupport.createLocalDateWriter(objectClass, null);
            }
            case "org.joda.time.LocalDateTime": {
                return JodaSupport.createLocalDateTimeWriter(objectClass, null);
            }
            case "org.joda.time.DateTime": {
                return new ObjectWriterImplZonedDateTime(null, null, new JodaSupport.DateTime2ZDT());
            }
            default: {
                if (JdbcSupport.isClob(objectClass)) {
                    return JdbcSupport.createClobWriter(objectClass);
                }
                return null;
            }
        }
    }
    
    @Override
    public ObjectWriter getObjectWriter(Type objectType, Class objectClass) {
        if (objectType == String.class) {
            return ObjectWriterImplString.INSTANCE;
        }
        if (objectClass == null) {
            if (objectType instanceof Class) {
                objectClass = (Class<?>)objectType;
            }
            else {
                objectClass = TypeUtils.getMapping(objectType);
            }
        }
        final String className = objectClass.getName();
        ObjectWriter externalObjectWriter = this.getExternalObjectWriter(className, objectClass);
        if (externalObjectWriter != null) {
            return externalObjectWriter;
        }
        final String s = className;
        switch (s) {
            case "com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList":
            case "com.google.common.collect.AbstractMapBasedMultimap$WrappedSet": {
                return null;
            }
            case "org.javamoney.moneta.internal.JDKCurrencyAdapter": {
                return ObjectWriterImplToString.INSTANCE;
            }
            case "com.fasterxml.jackson.databind.node.ObjectNode": {
                return ObjectWriterImplToString.DIRECT;
            }
            case "org.javamoney.moneta.Money": {
                return MoneySupport.createMonetaryAmountWriter();
            }
            case "org.javamoney.moneta.spi.DefaultNumberValue": {
                return MoneySupport.createNumberValueWriter();
            }
            case "net.sf.json.JSONNull":
            case "java.net.Inet4Address":
            case "java.net.Inet6Address":
            case "java.net.InetSocketAddress":
            case "java.text.SimpleDateFormat":
            case "java.util.regex.Pattern":
            case "com.fasterxml.jackson.databind.node.ArrayNode": {
                return ObjectWriterMisc.INSTANCE;
            }
            case "org.apache.commons.lang3.tuple.Pair":
            case "org.apache.commons.lang3.tuple.MutablePair":
            case "org.apache.commons.lang3.tuple.ImmutablePair": {
                return new ApacheLang3Support.PairWriter(objectClass);
            }
            case "com.carrotsearch.hppc.ByteArrayList":
            case "com.carrotsearch.hppc.ShortArrayList":
            case "com.carrotsearch.hppc.IntArrayList":
            case "com.carrotsearch.hppc.IntHashSet":
            case "com.carrotsearch.hppc.LongArrayList":
            case "com.carrotsearch.hppc.LongHashSet":
            case "com.carrotsearch.hppc.CharArrayList":
            case "com.carrotsearch.hppc.CharHashSet":
            case "com.carrotsearch.hppc.FloatArrayList":
            case "com.carrotsearch.hppc.DoubleArrayList":
            case "com.carrotsearch.hppc.BitSet":
            case "gnu.trove.list.array.TByteArrayList":
            case "gnu.trove.list.array.TCharArrayList":
            case "gnu.trove.list.array.TShortArrayList":
            case "gnu.trove.list.array.TIntArrayList":
            case "gnu.trove.list.array.TLongArrayList":
            case "gnu.trove.list.array.TFloatArrayList":
            case "gnu.trove.list.array.TDoubleArrayList":
            case "gnu.trove.set.hash.TByteHashSet":
            case "gnu.trove.set.hash.TShortHashSet":
            case "gnu.trove.set.hash.TIntHashSet":
            case "gnu.trove.set.hash.TLongHashSet":
            case "gnu.trove.stack.array.TByteArrayStack":
            case "org.bson.types.Decimal128": {
                return LambdaMiscCodec.getObjectWriter(objectType, objectClass);
            }
            case "java.nio.HeapByteBuffer":
            case "java.nio.DirectByteBuffer": {
                return new ObjectWriterImplInt8ValueArray(o -> o.array());
            }
            default: {
                if (objectType instanceof ParameterizedType) {
                    final ParameterizedType parameterizedType = (ParameterizedType)objectType;
                    final Type rawType = parameterizedType.getRawType();
                    final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (rawType == List.class || rawType == ArrayList.class) {
                        if (actualTypeArguments.length == 1 && actualTypeArguments[0] == String.class) {
                            return ObjectWriterImplListStr.INSTANCE;
                        }
                        objectType = rawType;
                    }
                    if (Map.class.isAssignableFrom(objectClass)) {
                        return ObjectWriterImplMap.of(objectType, objectClass);
                    }
                    if (objectClass == Optional.class && actualTypeArguments.length == 1) {
                        return new ObjectWriterImplOptional(actualTypeArguments[0], null, null);
                    }
                }
                if (objectType == LinkedList.class) {
                    return ObjectWriterImplList.INSTANCE;
                }
                if (objectType == ArrayList.class || objectType == List.class || List.class.isAssignableFrom(objectClass)) {
                    return ObjectWriterImplList.INSTANCE;
                }
                if (Collection.class.isAssignableFrom(objectClass)) {
                    return ObjectWriterImplCollection.INSTANCE;
                }
                if (BeanUtils.isExtendedMap(objectClass)) {
                    return null;
                }
                if (Map.class.isAssignableFrom(objectClass)) {
                    return ObjectWriterImplMap.of(objectClass);
                }
                if (Map.Entry.class.isAssignableFrom(objectClass)) {
                    return ObjectWriterImplMapEntry.INSTANCE;
                }
                if (Path.class.isAssignableFrom(objectClass)) {
                    return ObjectWriterImplToString.INSTANCE;
                }
                if (objectType == Integer.class) {
                    return ObjectWriterImplInt32.INSTANCE;
                }
                if (objectType == AtomicInteger.class) {
                    return ObjectWriterImplAtomicInteger.INSTANCE;
                }
                if (objectType == Byte.class) {
                    return ObjectWriterImplInt8.INSTANCE;
                }
                if (objectType == Short.class) {
                    return ObjectWriterImplInt16.INSTANCE;
                }
                if (objectType == Long.class) {
                    return ObjectWriterImplInt64.INSTANCE;
                }
                if (objectType == AtomicLong.class) {
                    return ObjectWriterImplAtomicLong.INSTANCE;
                }
                if (objectType == AtomicReference.class) {
                    return ObjectWriterImplAtomicReference.INSTANCE;
                }
                if (objectType == Float.class) {
                    return ObjectWriterImplFloat.INSTANCE;
                }
                if (objectType == Double.class) {
                    return ObjectWriterImplDouble.INSTANCE;
                }
                if (objectType == BigInteger.class) {
                    return ObjectWriterBigInteger.INSTANCE;
                }
                if (objectType == BigDecimal.class) {
                    return ObjectWriterImplBigDecimal.INSTANCE;
                }
                if (objectType == BitSet.class) {
                    return ObjectWriterImplBitSet.INSTANCE;
                }
                if (objectType == OptionalInt.class) {
                    return ObjectWriterImplOptionalInt.INSTANCE;
                }
                if (objectType == OptionalLong.class) {
                    return ObjectWriterImplOptionalLong.INSTANCE;
                }
                if (objectType == OptionalDouble.class) {
                    return ObjectWriterImplOptionalDouble.INSTANCE;
                }
                if (objectType == Optional.class) {
                    return ObjectWriterImplOptional.INSTANCE;
                }
                if (objectType == Boolean.class) {
                    return ObjectWriterImplBoolean.INSTANCE;
                }
                if (objectType == AtomicBoolean.class) {
                    return ObjectWriterImplAtomicBoolean.INSTANCE;
                }
                if (objectType == AtomicIntegerArray.class) {
                    return ObjectWriterImplAtomicIntegerArray.INSTANCE;
                }
                if (objectType == AtomicLongArray.class) {
                    return ObjectWriterImplAtomicLongArray.INSTANCE;
                }
                if (objectType == Character.class) {
                    return ObjectWriterImplCharacter.INSTANCE;
                }
                if (objectType instanceof Class) {
                    final Class clazz = (Class)objectType;
                    if (TimeUnit.class.isAssignableFrom(clazz)) {
                        return new ObjectWriterImplEnum(null, TimeUnit.class, null, null, 0L);
                    }
                    if (Enum.class.isAssignableFrom(clazz)) {
                        final ObjectWriter enumWriter = this.createEnumWriter(clazz);
                        if (enumWriter != null) {
                            return enumWriter;
                        }
                    }
                    if (JSONPath.class.isAssignableFrom(clazz)) {
                        return ObjectWriterImplToString.INSTANCE;
                    }
                    if (clazz == boolean[].class) {
                        return ObjectWriterImplBoolValueArray.INSTANCE;
                    }
                    if (clazz == char[].class) {
                        return ObjectWriterImplCharValueArray.INSTANCE;
                    }
                    if (clazz == StringBuffer.class || clazz == StringBuilder.class) {
                        return ObjectWriterImplToString.INSTANCE;
                    }
                    if (clazz == byte[].class) {
                        return ObjectWriterImplInt8ValueArray.INSTANCE;
                    }
                    if (clazz == short[].class) {
                        return ObjectWriterImplInt16ValueArray.INSTANCE;
                    }
                    if (clazz == int[].class) {
                        return ObjectWriterImplInt32ValueArray.INSTANCE;
                    }
                    if (clazz == long[].class) {
                        return ObjectWriterImplInt64ValueArray.INSTANCE;
                    }
                    if (clazz == float[].class) {
                        return ObjectWriterImplFloatValueArray.INSTANCE;
                    }
                    if (clazz == double[].class) {
                        return ObjectWriterImplDoubleValueArray.INSTANCE;
                    }
                    if (clazz == Byte[].class) {
                        return ObjectWriterImplInt8Array.INSTANCE;
                    }
                    if (clazz == Integer[].class) {
                        return ObjectWriterImplInt32Array.INSTANCE;
                    }
                    if (clazz == Long[].class) {
                        return ObjectWriterImplInt64Array.INSTANCE;
                    }
                    if (String[].class == clazz) {
                        return ObjectWriterImplStringArray.INSTANCE;
                    }
                    if (BigDecimal[].class == clazz) {
                        return ObjectWriterImpDecimalArray.INSTANCE;
                    }
                    if (Object[].class.isAssignableFrom(clazz)) {
                        if (clazz == Object[].class) {
                            return ObjectWriterArray.INSTANCE;
                        }
                        final Class componentType = clazz.getComponentType();
                        if (Modifier.isFinal(componentType.getModifiers())) {
                            return new ObjectWriterArrayFinal(componentType, null);
                        }
                        return new ObjectWriterArray(componentType);
                    }
                    else {
                        if (clazz == UUID.class) {
                            return ObjectWriterImplUUID.INSTANCE;
                        }
                        if (clazz == Locale.class) {
                            return ObjectWriterImplLocale.INSTANCE;
                        }
                        if (clazz == Currency.class) {
                            return ObjectWriterImplCurrency.INSTANCE;
                        }
                        if (TimeZone.class.isAssignableFrom(clazz)) {
                            return ObjectWriterImplTimeZone.INSTANCE;
                        }
                        if (JSONPObject.class.isAssignableFrom(clazz)) {
                            return new ObjectWriterImplJSONP();
                        }
                        if (clazz == URI.class || clazz == URL.class || clazz == File.class || ZoneId.class.isAssignableFrom(clazz) || Charset.class.isAssignableFrom(clazz)) {
                            return ObjectWriterImplToString.INSTANCE;
                        }
                        externalObjectWriter = this.getExternalObjectWriter(clazz.getName(), clazz);
                        if (externalObjectWriter != null) {
                            return externalObjectWriter;
                        }
                        final BeanInfo beanInfo = new BeanInfo();
                        final Class mixIn = this.provider.getMixIn(clazz);
                        if (mixIn != null) {
                            this.annotationProcessor.getBeanInfo(beanInfo, mixIn);
                        }
                        if (Date.class.isAssignableFrom(clazz)) {
                            if (beanInfo.format != null || beanInfo.locale != null) {
                                return new ObjectWriterImplDate(beanInfo.format, beanInfo.locale);
                            }
                            return ObjectWriterImplDate.INSTANCE;
                        }
                        else if (Calendar.class.isAssignableFrom(clazz)) {
                            if (beanInfo.format != null || beanInfo.locale != null) {
                                return new ObjectWriterImplCalendar(beanInfo.format, beanInfo.locale);
                            }
                            return ObjectWriterImplCalendar.INSTANCE;
                        }
                        else if (ZonedDateTime.class == clazz) {
                            if (beanInfo.format != null || beanInfo.locale != null) {
                                return new ObjectWriterImplZonedDateTime(beanInfo.format, beanInfo.locale);
                            }
                            return ObjectWriterImplZonedDateTime.INSTANCE;
                        }
                        else {
                            if (OffsetDateTime.class == clazz) {
                                return ObjectWriterImplOffsetDateTime.of(beanInfo.format, beanInfo.locale);
                            }
                            if (LocalDateTime.class == clazz) {
                                if (beanInfo.format != null || beanInfo.locale != null) {
                                    return new ObjectWriterImplLocalDateTime(beanInfo.format, beanInfo.locale);
                                }
                                return ObjectWriterImplLocalDateTime.INSTANCE;
                            }
                            else {
                                if (LocalDate.class == clazz) {
                                    return ObjectWriterImplLocalDate.of(beanInfo.format, beanInfo.locale);
                                }
                                if (LocalTime.class == clazz) {
                                    if (beanInfo.format != null || beanInfo.locale != null) {
                                        return new ObjectWriterImplLocalTime(beanInfo.format, beanInfo.locale);
                                    }
                                    return ObjectWriterImplLocalTime.INSTANCE;
                                }
                                else if (OffsetTime.class == clazz) {
                                    if (beanInfo.format != null || beanInfo.locale != null) {
                                        return new ObjectWriterImplOffsetTime(beanInfo.format, beanInfo.locale);
                                    }
                                    return ObjectWriterImplOffsetTime.INSTANCE;
                                }
                                else if (Instant.class == clazz) {
                                    if (beanInfo.format != null || beanInfo.locale != null) {
                                        return new ObjectWriterImplInstant(beanInfo.format, beanInfo.locale);
                                    }
                                    return ObjectWriterImplInstant.INSTANCE;
                                }
                                else {
                                    if (Duration.class == clazz) {
                                        return ObjectWriterImplToString.INSTANCE;
                                    }
                                    if (StackTraceElement.class == clazz) {
                                        if (ObjectWriterBaseModule.STACK_TRACE_ELEMENT_WRITER == null) {
                                            final ObjectWriterCreator creator = this.provider.getCreator();
                                            ObjectWriterBaseModule.STACK_TRACE_ELEMENT_WRITER = new ObjectWriterAdapter((Class<T>)StackTraceElement.class, null, null, 0L, (List<FieldWriter>)Arrays.asList(creator.createFieldWriter("fileName", String.class, BeanUtils.getDeclaredField(StackTraceElement.class, "fileName"), BeanUtils.getMethod(StackTraceElement.class, "getFileName"), StackTraceElement::getFileName), creator.createFieldWriter("lineNumber", BeanUtils.getDeclaredField(StackTraceElement.class, "lineNumber"), BeanUtils.getMethod(StackTraceElement.class, "getLineNumber"), StackTraceElement::getLineNumber), creator.createFieldWriter("className", String.class, BeanUtils.getDeclaredField(StackTraceElement.class, "declaringClass"), BeanUtils.getMethod(StackTraceElement.class, "getClassName"), StackTraceElement::getClassName), creator.createFieldWriter("methodName", String.class, BeanUtils.getDeclaredField(StackTraceElement.class, "methodName"), BeanUtils.getMethod(StackTraceElement.class, "getMethodName"), StackTraceElement::getMethodName)));
                                        }
                                        return ObjectWriterBaseModule.STACK_TRACE_ELEMENT_WRITER;
                                    }
                                    if (Class.class == clazz) {
                                        return ObjectWriterImplClass.INSTANCE;
                                    }
                                    if (Method.class == clazz) {
                                        return new ObjectWriterAdapter(Method.class, null, null, 0L, Arrays.asList(ObjectWriters.fieldWriter("declaringClass", Class.class, Method::getDeclaringClass), ObjectWriters.fieldWriter("name", String.class, Method::getName), ObjectWriters.fieldWriter("parameterTypes", (Class<Class[]>)Class[].class, Method::getParameterTypes)));
                                    }
                                    if (Field.class == clazz) {
                                        return new ObjectWriterAdapter(Method.class, null, null, 0L, Arrays.asList(ObjectWriters.fieldWriter("declaringClass", Class.class, Field::getDeclaringClass), ObjectWriters.fieldWriter("name", String.class, Field::getName)));
                                    }
                                    if (ParameterizedType.class.isAssignableFrom(clazz)) {
                                        return ObjectWriters.objectWriter(ParameterizedType.class, ObjectWriters.fieldWriter("actualTypeArguments", Type[].class, ParameterizedType::getActualTypeArguments), ObjectWriters.fieldWriter("ownerType", Type.class, ParameterizedType::getOwnerType), ObjectWriters.fieldWriter("rawType", Type.class, ParameterizedType::getRawType));
                                    }
                                }
                            }
                        }
                    }
                }
                return null;
            }
        }
    }
    
    private ObjectWriter createEnumWriter(Class enumClass) {
        if (!enumClass.isEnum()) {
            final Class superclass = enumClass.getSuperclass();
            if (superclass.isEnum()) {
                enumClass = superclass;
            }
        }
        Member valueField = BeanUtils.getEnumValueField(enumClass, this.provider);
        if (valueField == null) {
            final Class mixInSource = (Class)this.provider.mixInCache.get(enumClass);
            final Member mixedValueField = BeanUtils.getEnumValueField(mixInSource, this.provider);
            if (mixedValueField instanceof Field) {
                try {
                    valueField = enumClass.getField(mixedValueField.getName());
                }
                catch (NoSuchFieldException ex) {}
            }
            else if (mixedValueField instanceof Method) {
                try {
                    valueField = enumClass.getMethod(mixedValueField.getName(), (Class[])new Class[0]);
                }
                catch (NoSuchMethodException ex2) {}
            }
        }
        final BeanInfo beanInfo = new BeanInfo();
        final Class[] interfaces = enumClass.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            this.annotationProcessor.getBeanInfo(beanInfo, interfaces[i]);
        }
        this.annotationProcessor.getBeanInfo(beanInfo, enumClass);
        if (beanInfo.writeEnumAsJavaBean) {
            return null;
        }
        final String[] annotationNames = BeanUtils.getEnumAnnotationNames(enumClass);
        return new ObjectWriterImplEnum(null, enumClass, valueField, annotationNames, 0L);
    }
    
    public class WriterAnnotationProcessor implements ObjectWriterAnnotationProcessor
    {
        @Override
        public void getBeanInfo(final BeanInfo beanInfo, final Class objectClass) {
            if (objectClass != null) {
                final Class superclass = objectClass.getSuperclass();
                if (superclass != Object.class && superclass != null && superclass != Enum.class) {
                    this.getBeanInfo(beanInfo, superclass);
                    if (beanInfo.seeAlso != null && beanInfo.seeAlsoNames != null) {
                        for (int i = 0; i < beanInfo.seeAlso.length; ++i) {
                            final Class seeAlso = beanInfo.seeAlso[i];
                            if (seeAlso == objectClass && i < beanInfo.seeAlsoNames.length) {
                                final String seeAlsoName = beanInfo.seeAlsoNames[i];
                                if (seeAlsoName != null && seeAlsoName.length() != 0) {
                                    beanInfo.typeName = seeAlsoName;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            Annotation jsonType1x = null;
            JSONType jsonType = null;
            final Annotation[] annotations = BeanUtils.getAnnotations(objectClass);
            for (int j = 0; j < annotations.length; ++j) {
                final Annotation annotation = annotations[j];
                final Class annotationType = annotation.annotationType();
                jsonType = BeanUtils.findAnnotation(annotation, JSONType.class);
                if (jsonType != annotation) {
                    if (annotationType == JSONCompiler.class) {
                        final JSONCompiler compiler = (JSONCompiler)annotation;
                        if (compiler.value() == JSONCompiler.CompilerOption.LAMBDA) {
                            beanInfo.writerFeatures |= 0x40000000000000L;
                        }
                    }
                    final boolean useJacksonAnnotation = JSONFactory.isUseJacksonAnnotation();
                    final String name = annotationType.getName();
                    switch (name) {
                        case "com.alibaba.fastjson.annotation.JSONType": {
                            jsonType1x = annotation;
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonIgnoreProperties":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonIgnoreProperties": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonIgnoreProperties(beanInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonPropertyOrder":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonPropertyOrder": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonPropertyOrder(beanInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonFormat":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonFormat": {
                            if (useJacksonAnnotation) {
                                BeanUtils.processJacksonJsonFormat(beanInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonInclude":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonInclude": {
                            if (useJacksonAnnotation) {
                                BeanUtils.processJacksonJsonInclude(beanInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonTypeInfo":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonTypeInfo": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonTypeInfo(beanInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.databind.annotation.JsonSerialize":
                        case "com.alibaba.fastjson2.adapter.jackson.databind.annotation.JsonSerialize": {
                            if (!useJacksonAnnotation) {
                                break;
                            }
                            this.processJacksonJsonSerialize(beanInfo, annotation);
                            if (beanInfo.serializer != null && Enum.class.isAssignableFrom(objectClass)) {
                                beanInfo.writeEnumAsJavaBean = true;
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonTypeName":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonTypeName": {
                            if (useJacksonAnnotation) {
                                BeanUtils.processJacksonJsonTypeName(beanInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonSubTypes":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonSubTypes": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonSubTypes(beanInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "kotlin.Metadata": {
                            beanInfo.kotlin = true;
                            KotlinUtils.getConstructor(objectClass, beanInfo);
                            break;
                        }
                    }
                }
            }
            if (jsonType == null) {
                final Class mixInSource = ObjectWriterBaseModule.this.provider.mixInCache.get(objectClass);
                if (mixInSource != null) {
                    beanInfo.mixIn = true;
                    final Annotation[] mixInAnnotations = BeanUtils.getAnnotations(mixInSource);
                    for (int k = 0; k < mixInAnnotations.length; ++k) {
                        final Annotation annotation2 = mixInAnnotations[k];
                        final Class<? extends Annotation> annotationType2 = annotation2.annotationType();
                        jsonType = BeanUtils.findAnnotation(annotation2, JSONType.class);
                        if (jsonType != annotation2) {
                            final String annotationTypeName = annotationType2.getName();
                            if (annotationTypeName.equals("com.alibaba.fastjson.annotation.JSONType")) {
                                jsonType1x = annotation2;
                            }
                        }
                    }
                }
            }
            if (jsonType != null) {
                final Class<?>[] classes = jsonType.seeAlso();
                if (classes.length != 0) {
                    beanInfo.seeAlso = classes;
                }
                final String typeKey = jsonType.typeKey();
                if (!typeKey.isEmpty()) {
                    beanInfo.typeKey = typeKey;
                }
                final String typeName = jsonType.typeName();
                if (!typeName.isEmpty()) {
                    beanInfo.typeName = typeName;
                }
                for (final JSONWriter.Feature feature : jsonType.serializeFeatures()) {
                    beanInfo.writerFeatures |= feature.mask;
                }
                beanInfo.namingStrategy = jsonType.naming().name();
                final String[] ignores = jsonType.ignores();
                if (ignores.length > 0) {
                    beanInfo.ignores = ignores;
                }
                final String[] includes = jsonType.includes();
                if (includes.length > 0) {
                    beanInfo.includes = includes;
                }
                final String[] orders = jsonType.orders();
                if (orders.length > 0) {
                    beanInfo.orders = orders;
                }
                final Class<?> serializer = jsonType.serializer();
                if (ObjectWriter.class.isAssignableFrom(serializer)) {
                    beanInfo.serializer = serializer;
                }
                final Class<? extends Filter>[] serializeFilters = jsonType.serializeFilters();
                if (serializeFilters.length != 0) {
                    beanInfo.serializeFilters = serializeFilters;
                }
                final String format = jsonType.format();
                if (!format.isEmpty()) {
                    beanInfo.format = format;
                }
                final String locale = jsonType.locale();
                if (!locale.isEmpty()) {
                    final String[] parts = locale.split("_");
                    if (parts.length == 2) {
                        beanInfo.locale = new Locale(parts[0], parts[1]);
                    }
                }
                if (!jsonType.alphabetic()) {
                    beanInfo.alphabetic = false;
                }
                if (jsonType.writeEnumAsJavaBean()) {
                    beanInfo.writeEnumAsJavaBean = true;
                }
            }
            else if (jsonType1x != null) {
                final Annotation annotation3 = jsonType1x;
                BeanUtils.annotationMethods(jsonType1x.annotationType(), method -> BeanUtils.processJSONType1x(beanInfo, annotation3, method));
            }
            if (beanInfo.seeAlso != null && beanInfo.seeAlso.length != 0 && (beanInfo.typeName == null || beanInfo.typeName.length() == 0)) {
                for (final Class seeAlsoClass : beanInfo.seeAlso) {
                    if (seeAlsoClass == objectClass) {
                        beanInfo.typeName = objectClass.getSimpleName();
                        break;
                    }
                }
            }
        }
        
        @Override
        public void getFieldInfo(final BeanInfo beanInfo, final FieldInfo fieldInfo, final Class objectClass, final Field field) {
            if (objectClass != null) {
                final Class mixInSource = ObjectWriterBaseModule.this.provider.mixInCache.get(objectClass);
                if (mixInSource != null && mixInSource != objectClass) {
                    Field mixInField = null;
                    try {
                        mixInField = mixInSource.getDeclaredField(field.getName());
                    }
                    catch (Exception ex) {}
                    if (mixInField != null) {
                        this.getFieldInfo(beanInfo, fieldInfo, mixInSource, mixInField);
                    }
                }
            }
            final Class fieldClassMixInSource = ObjectWriterBaseModule.this.provider.mixInCache.get(field.getType());
            if (fieldClassMixInSource != null) {
                fieldInfo.fieldClassMixIn = true;
            }
            final int modifiers = field.getModifiers();
            final boolean isTransient = Modifier.isTransient(modifiers);
            if (isTransient) {
                fieldInfo.ignore = true;
            }
            JSONField jsonField = null;
            final Annotation[] annotations2;
            final Annotation[] annotations = annotations2 = BeanUtils.getAnnotations(field);
            for (final Annotation annotation : annotations2) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                Label_0927: {
                    if (jsonField == null) {
                        jsonField = BeanUtils.findAnnotation(annotation, JSONField.class);
                        if (jsonField == annotation) {
                            break Label_0927;
                        }
                    }
                    final String annotationTypeName = annotationType.getName();
                    final boolean useJacksonAnnotation = JSONFactory.isUseJacksonAnnotation();
                    final String s = annotationTypeName;
                    switch (s) {
                        case "com.fasterxml.jackson.annotation.JsonIgnore":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonIgnore": {
                            if (useJacksonAnnotation) {
                                BeanUtils.processJacksonJsonIgnore(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonAnyGetter":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonAnyGetter": {
                            if (useJacksonAnnotation) {
                                fieldInfo.features |= 0x2000000000000L;
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonValue":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonValue": {
                            if (useJacksonAnnotation) {
                                fieldInfo.features |= 0x1000000000000L;
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonRawValue":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonRawValue": {
                            if (useJacksonAnnotation) {
                                fieldInfo.features |= 0x4000000000000L;
                                break;
                            }
                            break;
                        }
                        case "com.alibaba.fastjson.annotation.JSONField": {
                            this.processJSONField1x(fieldInfo, annotation);
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonProperty":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonProperty": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonProperty(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonFormat":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonFormat": {
                            if (useJacksonAnnotation) {
                                BeanUtils.processJacksonJsonFormat(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonInclude":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonInclude": {
                            if (useJacksonAnnotation) {
                                BeanUtils.processJacksonJsonInclude(beanInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.alibaba.fastjson2.adapter.jackson.databind.annotation.JsonSerialize":
                        case "com.fasterxml.jackson.databind.annotation.JsonSerialize": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonSerialize(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.google.gson.annotations.SerializedName": {
                            BeanUtils.processGsonSerializedName(fieldInfo, annotation);
                            break;
                        }
                    }
                }
            }
            if (jsonField == null) {
                return;
            }
            this.loadFieldInfo(fieldInfo, jsonField);
            final Class writeUsing = jsonField.writeUsing();
            if (ObjectWriter.class.isAssignableFrom(writeUsing)) {
                fieldInfo.writeUsing = (Class<?>)writeUsing;
            }
            final Class serializeUsing = jsonField.serializeUsing();
            if (ObjectWriter.class.isAssignableFrom(serializeUsing)) {
                fieldInfo.writeUsing = (Class<?>)serializeUsing;
            }
            if (jsonField.jsonDirect()) {
                fieldInfo.features |= 0x4000000000000L;
            }
            if ((fieldInfo.features & JSONWriter.Feature.WriteNonStringValueAsString.mask) != 0x0L && !String.class.equals(field.getType()) && fieldInfo.writeUsing == null) {
                fieldInfo.writeUsing = ObjectWriterImplToString.class;
            }
        }
        
        private void processJacksonJsonSubTypes(final BeanInfo beanInfo, final Annotation annotation) {
            final Class<? extends Annotation> annotationClass = annotation.getClass();
            final String name;
            Object result;
            Annotation[] value;
            int i;
            Annotation item;
            BeanUtils.annotationMethods(annotationClass, m -> {
                name = m.getName();
                try {
                    result = m.invoke(annotation, new Object[0]);
                    if (name.equals("value")) {
                        value = (Annotation[])result;
                        if (value.length != 0) {
                            beanInfo.seeAlso = new Class[value.length];
                            beanInfo.seeAlsoNames = new String[value.length];
                            for (i = 0; i < value.length; ++i) {
                                item = value[i];
                                BeanUtils.processJacksonJsonSubTypesType(beanInfo, i, item);
                            }
                        }
                    }
                }
                catch (Throwable t) {}
            });
        }
        
        private void processJacksonJsonSerialize(final BeanInfo beanInfo, final Annotation annotation) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
            //     4: astore_3        /* annotationClass */
            //     5: aload_3         /* annotationClass */
            //     6: aload_0         /* this */
            //     7: aload_2         /* annotation */
            //     8: aload_1         /* beanInfo */
            //     9: invokedynamic   BootstrapMethod #2, accept:(Lcom/alibaba/fastjson2/writer/ObjectWriterBaseModule$WriterAnnotationProcessor;Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/BeanInfo;)Ljava/util/function/Consumer;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        private Class processUsing(final Class result) {
            final String usingName = result.getName();
            final String noneClassName0 = "com.alibaba.fastjson2.adapter.jackson.databind.JsonSerializer$None";
            final String noneClassName2 = "com.fasterxml.jackson.databind.JsonSerializer$None";
            if (!noneClassName0.equals(usingName) && !noneClassName2.equals(usingName) && ObjectWriter.class.isAssignableFrom(result)) {
                return result;
            }
            if ("com.fasterxml.jackson.databind.ser.std.ToStringSerializer".equals(usingName)) {
                return ObjectWriterImplToString.class;
            }
            return null;
        }
        
        private void processJacksonJsonTypeInfo(final BeanInfo beanInfo, final Annotation annotation) {
            final Class<? extends Annotation> annotationClass = annotation.getClass();
            final String name;
            Object result;
            String value;
            BeanUtils.annotationMethods(annotationClass, m -> {
                name = m.getName();
                try {
                    result = m.invoke(annotation, new Object[0]);
                    if (name.equals("property")) {
                        value = (String)result;
                        if (!value.isEmpty()) {
                            beanInfo.typeKey = value;
                            beanInfo.writerFeatures |= JSONWriter.Feature.WriteClassName.mask;
                        }
                    }
                }
                catch (Throwable t) {}
            });
        }
        
        private void processJacksonJsonPropertyOrder(final BeanInfo beanInfo, final Annotation annotation) {
            final Class<? extends Annotation> annotationClass = annotation.getClass();
            final String name;
            Object result;
            String[] value;
            BeanUtils.annotationMethods(annotationClass, m -> {
                name = m.getName();
                try {
                    result = m.invoke(annotation, new Object[0]);
                    if (name.equals("value")) {
                        value = (String[])result;
                        if (value.length != 0) {
                            beanInfo.orders = value;
                        }
                    }
                }
                catch (Throwable t) {}
            });
        }
        
        private void processJacksonJsonSerialize(final FieldInfo fieldInfo, final Annotation annotation) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
            //     4: astore_3        /* annotationClass */
            //     5: aload_3         /* annotationClass */
            //     6: aload_0         /* this */
            //     7: aload_2         /* annotation */
            //     8: aload_1         /* fieldInfo */
            //     9: invokedynamic   BootstrapMethod #5, accept:(Lcom/alibaba/fastjson2/writer/ObjectWriterBaseModule$WriterAnnotationProcessor;Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/FieldInfo;)Ljava/util/function/Consumer;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        private void processJacksonJsonProperty(final FieldInfo fieldInfo, final Annotation annotation) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
            //     4: astore_3        /* annotationClass */
            //     5: aload_3         /* annotationClass */
            //     6: aload_2         /* annotation */
            //     7: aload_1         /* fieldInfo */
            //     8: invokedynamic   BootstrapMethod #6, accept:(Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/FieldInfo;)Ljava/util/function/Consumer;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        private void processJacksonJsonIgnoreProperties(final BeanInfo beanInfo, final Annotation annotation) {
            final Class<? extends Annotation> annotationClass = annotation.getClass();
            final String name;
            Object result;
            String[] value;
            BeanUtils.annotationMethods(annotationClass, m -> {
                name = m.getName();
                try {
                    result = m.invoke(annotation, new Object[0]);
                    if (name.equals("value")) {
                        value = (String[])result;
                        if (value.length != 0) {
                            beanInfo.ignores = value;
                        }
                    }
                }
                catch (Throwable t) {}
            });
        }
        
        private void processJSONField1x(final FieldInfo fieldInfo, final Annotation annotation) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
            //     4: astore_3        /* annotationClass */
            //     5: aload_3         /* annotationClass */
            //     6: aload_0         /* this */
            //     7: aload_2         /* annotation */
            //     8: aload_1         /* fieldInfo */
            //     9: invokedynamic   BootstrapMethod #8, accept:(Lcom/alibaba/fastjson2/writer/ObjectWriterBaseModule$WriterAnnotationProcessor;Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/FieldInfo;)Ljava/util/function/Consumer;
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        private void applyFeatures(final FieldInfo fieldInfo, final Enum[] features) {
            for (final Enum feature : features) {
                final String name = feature.name();
                switch (name) {
                    case "UseISO8601DateFormat": {
                        fieldInfo.format = "iso8601";
                        break;
                    }
                    case "WriteMapNullValue": {
                        fieldInfo.features |= JSONWriter.Feature.WriteNulls.mask;
                        break;
                    }
                    case "WriteNullListAsEmpty": {
                        fieldInfo.features |= JSONWriter.Feature.WriteNullListAsEmpty.mask;
                        break;
                    }
                    case "WriteNullStringAsEmpty": {
                        fieldInfo.features |= JSONWriter.Feature.WriteNullStringAsEmpty.mask;
                        break;
                    }
                    case "WriteNullNumberAsZero": {
                        fieldInfo.features |= JSONWriter.Feature.WriteNullNumberAsZero.mask;
                        break;
                    }
                    case "WriteNullBooleanAsFalse": {
                        fieldInfo.features |= JSONWriter.Feature.WriteNullBooleanAsFalse.mask;
                        break;
                    }
                    case "BrowserCompatible": {
                        fieldInfo.features |= JSONWriter.Feature.BrowserCompatible.mask;
                        break;
                    }
                    case "WriteClassName": {
                        fieldInfo.features |= JSONWriter.Feature.WriteClassName.mask;
                        break;
                    }
                    case "WriteNonStringValueAsString": {
                        fieldInfo.features |= JSONWriter.Feature.WriteNonStringValueAsString.mask;
                        break;
                    }
                    case "WriteEnumUsingToString": {
                        fieldInfo.features |= JSONWriter.Feature.WriteEnumUsingToString.mask;
                        break;
                    }
                    case "NotWriteRootClassName": {
                        fieldInfo.features |= JSONWriter.Feature.NotWriteRootClassName.mask;
                        break;
                    }
                    case "IgnoreErrorGetter": {
                        fieldInfo.features |= JSONWriter.Feature.IgnoreErrorGetter.mask;
                        break;
                    }
                    case "WriteBigDecimalAsPlain": {
                        fieldInfo.features |= JSONWriter.Feature.WriteBigDecimalAsPlain.mask;
                        break;
                    }
                }
            }
        }
        
        @Override
        public void getFieldInfo(final BeanInfo beanInfo, final FieldInfo fieldInfo, final Class objectClass, final Method method) {
            final Class mixInSource = ObjectWriterBaseModule.this.provider.mixInCache.get(objectClass);
            final String methodName = method.getName();
            if (methodName.equals("getTargetSql") && objectClass != null && objectClass.getName().startsWith("com.baomidou.mybatisplus.")) {
                fieldInfo.features |= JSONWriter.Feature.IgnoreErrorGetter.mask;
            }
            if (mixInSource != null && mixInSource != objectClass) {
                Method mixInMethod = null;
                try {
                    mixInMethod = mixInSource.getDeclaredMethod(methodName, (Class[])method.getParameterTypes());
                }
                catch (Exception ex) {}
                if (mixInMethod != null) {
                    this.getFieldInfo(beanInfo, fieldInfo, mixInSource, mixInMethod);
                }
            }
            final Class fieldClassMixInSource = ObjectWriterBaseModule.this.provider.mixInCache.get(method.getReturnType());
            if (fieldClassMixInSource != null) {
                fieldInfo.fieldClassMixIn = true;
            }
            if (JDKUtils.CLASS_TRANSIENT != null && method.getAnnotation((Class<Annotation>)JDKUtils.CLASS_TRANSIENT) != null) {
                fieldInfo.ignore = true;
            }
            if (objectClass != null) {
                final Class superclass = objectClass.getSuperclass();
                final Method supperMethod = BeanUtils.getMethod(superclass, method);
                final boolean ignore = fieldInfo.ignore;
                if (supperMethod != null) {
                    this.getFieldInfo(beanInfo, fieldInfo, superclass, supperMethod);
                    final int supperMethodModifiers = supperMethod.getModifiers();
                    if (ignore != fieldInfo.ignore && !Modifier.isAbstract(supperMethodModifiers) && !supperMethod.equals(method)) {
                        fieldInfo.ignore = ignore;
                    }
                }
                final Class[] interfaces2;
                final Class[] interfaces = interfaces2 = objectClass.getInterfaces();
                for (final Class anInterface : interfaces2) {
                    final Method interfaceMethod = BeanUtils.getMethod(anInterface, method);
                    if (interfaceMethod != null) {
                        this.getFieldInfo(beanInfo, fieldInfo, superclass, interfaceMethod);
                    }
                }
            }
            final Annotation[] annotations = BeanUtils.getAnnotations(method);
            this.processAnnotations(fieldInfo, annotations);
            if (!objectClass.getName().startsWith("java.lang") && !BeanUtils.isRecord(objectClass)) {
                final Field methodField = BeanUtils.getField(objectClass, method);
                if (methodField != null) {
                    fieldInfo.features |= 0x10000000000000L;
                    this.getFieldInfo(beanInfo, fieldInfo, objectClass, methodField);
                }
            }
            if (beanInfo.kotlin && beanInfo.creatorConstructor != null && beanInfo.createParameterNames != null) {
                final String fieldName = BeanUtils.getterName(method, null);
                for (int i = 0; i < beanInfo.createParameterNames.length; ++i) {
                    if (fieldName.equals(beanInfo.createParameterNames[i])) {
                        final Annotation[][] creatorConsParamAnnotations = beanInfo.creatorConstructor.getParameterAnnotations();
                        if (i < creatorConsParamAnnotations.length) {
                            final Annotation[] parameterAnnotations = creatorConsParamAnnotations[i];
                            this.processAnnotations(fieldInfo, parameterAnnotations);
                            break;
                        }
                    }
                }
            }
        }
        
        private void processAnnotations(final FieldInfo fieldInfo, final Annotation[] annotations) {
            for (final Annotation annotation : annotations) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                final JSONField jsonField = BeanUtils.findAnnotation(annotation, JSONField.class);
                if (Objects.nonNull(jsonField)) {
                    this.loadFieldInfo(fieldInfo, jsonField);
                }
                else {
                    if (annotationType == JSONCompiler.class) {
                        final JSONCompiler compiler = (JSONCompiler)annotation;
                        if (compiler.value() == JSONCompiler.CompilerOption.LAMBDA) {
                            fieldInfo.features |= 0x40000000000000L;
                        }
                    }
                    final boolean useJacksonAnnotation = JSONFactory.isUseJacksonAnnotation();
                    final String name;
                    final String annotationTypeName = name = annotationType.getName();
                    switch (name) {
                        case "com.fasterxml.jackson.annotation.JsonIgnore":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonIgnore": {
                            if (useJacksonAnnotation) {
                                BeanUtils.processJacksonJsonIgnore(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonAnyGetter":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonAnyGetter": {
                            if (useJacksonAnnotation) {
                                fieldInfo.features |= 0x2000000000000L;
                                break;
                            }
                            break;
                        }
                        case "com.alibaba.fastjson.annotation.JSONField": {
                            this.processJSONField1x(fieldInfo, annotation);
                            break;
                        }
                        case "java.beans.Transient": {
                            fieldInfo.ignore = true;
                            fieldInfo.isTransient = true;
                            break;
                        }
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonProperty":
                        case "com.fasterxml.jackson.annotation.JsonProperty": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonProperty(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonFormat":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonFormat": {
                            if (useJacksonAnnotation) {
                                BeanUtils.processJacksonJsonFormat(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonValue":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonValue": {
                            if (useJacksonAnnotation) {
                                fieldInfo.features |= 0x1000000000000L;
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonRawValue":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonRawValue": {
                            if (useJacksonAnnotation) {
                                fieldInfo.features |= 0x4000000000000L;
                                break;
                            }
                            break;
                        }
                        case "com.alibaba.fastjson2.adapter.jackson.databind.annotation.JsonSerialize":
                        case "com.fasterxml.jackson.databind.annotation.JsonSerialize": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonSerialize(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        private void loadFieldInfo(final FieldInfo fieldInfo, final JSONField jsonField) {
            final String jsonFieldName = jsonField.name();
            if (!jsonFieldName.isEmpty()) {
                fieldInfo.fieldName = jsonFieldName;
            }
            final String defaultValue = jsonField.defaultValue();
            if (!defaultValue.isEmpty()) {
                fieldInfo.defaultValue = defaultValue;
            }
            this.loadJsonFieldFormat(fieldInfo, jsonField.format());
            final String label = jsonField.label();
            if (!label.isEmpty()) {
                fieldInfo.label = label;
            }
            if (!fieldInfo.ignore) {
                fieldInfo.ignore = !jsonField.serialize();
            }
            if (jsonField.unwrapped()) {
                fieldInfo.features |= 0x2000000000000L;
            }
            for (final JSONWriter.Feature feature : jsonField.serializeFeatures()) {
                fieldInfo.features |= feature.mask;
            }
            final int ordinal = jsonField.ordinal();
            if (ordinal != 0) {
                fieldInfo.ordinal = ordinal;
            }
            if (jsonField.value()) {
                fieldInfo.features |= 0x1000000000000L;
            }
            if (jsonField.jsonDirect()) {
                fieldInfo.features |= 0x4000000000000L;
            }
            final Class serializeUsing = jsonField.serializeUsing();
            if (ObjectWriter.class.isAssignableFrom(serializeUsing)) {
                fieldInfo.writeUsing = (Class<?>)serializeUsing;
            }
        }
        
        private void loadJsonFieldFormat(final FieldInfo fieldInfo, String jsonFieldFormat) {
            if (!jsonFieldFormat.isEmpty()) {
                jsonFieldFormat = jsonFieldFormat.trim();
                if (jsonFieldFormat.indexOf(84) != -1 && !jsonFieldFormat.contains("'T'")) {
                    jsonFieldFormat = jsonFieldFormat.replaceAll("T", "'T'");
                }
                if (!jsonFieldFormat.isEmpty()) {
                    fieldInfo.format = jsonFieldFormat;
                }
            }
        }
    }
    
    static class VoidObjectWriter implements ObjectWriter
    {
        public static final VoidObjectWriter INSTANCE;
        
        @Override
        public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        }
        
        static {
            INSTANCE = new VoidObjectWriter();
        }
    }
}
