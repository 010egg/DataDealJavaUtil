// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.annotation.JSONBuilder;
import java.util.Arrays;
import com.alibaba.fastjson2.annotation.JSONField;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import com.alibaba.fastjson2.annotation.JSONCompiler;
import com.alibaba.fastjson2.annotation.JSONType;
import com.alibaba.fastjson2.util.KotlinUtils;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.modules.ObjectReaderAnnotationProcessor;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.net.UnknownHostException;
import java.net.InetAddress;
import com.alibaba.fastjson2.support.money.MoneySupport;
import com.alibaba.fastjson2.util.JodaSupport;
import com.alibaba.fastjson2.util.JdbcSupport;
import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;
import com.alibaba.fastjson2.util.ApacheLang3Support;
import com.alibaba.fastjson2.util.GuavaSupport;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.lang.reflect.ParameterizedType;
import com.alibaba.fastjson2.JSONPObject;
import java.io.Serializable;
import java.io.Closeable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.SortedSet;
import java.util.NavigableSet;
import java.util.EnumSet;
import java.util.AbstractSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.AbstractSequentialList;
import java.util.Deque;
import java.util.Queue;
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.AbstractCollection;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.util.MapMultiValueType;
import com.alibaba.fastjson2.util.MultiType;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.LongFunction;
import java.util.function.IntFunction;
import java.util.TimeZone;
import java.util.Currency;
import java.util.Locale;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.Date;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.AbstractMap;
import java.util.Map;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.regex.Pattern;
import java.net.MalformedURLException;
import com.alibaba.fastjson2.JSONException;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;
import java.nio.charset.Charset;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;
import java.time.Duration;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.OptionalInt;
import java.util.BitSet;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.codec.FieldInfo;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.annotation.JSONCreator;
import java.lang.reflect.AnnotatedElement;
import com.alibaba.fastjson2.util.BeanUtils;
import java.lang.reflect.Constructor;
import java.lang.annotation.Annotation;
import com.alibaba.fastjson2.codec.BeanInfo;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;
import com.alibaba.fastjson2.JSONArray;
import java.util.List;
import java.util.Collection;
import com.alibaba.fastjson2.function.impl.StringToAny;
import com.alibaba.fastjson2.function.impl.ToNumber;
import com.alibaba.fastjson2.function.impl.ToDouble;
import com.alibaba.fastjson2.function.impl.ToFloat;
import com.alibaba.fastjson2.function.impl.ToLong;
import com.alibaba.fastjson2.function.impl.ToInteger;
import com.alibaba.fastjson2.function.impl.ToShort;
import com.alibaba.fastjson2.function.impl.ToByte;
import com.alibaba.fastjson2.function.impl.ToBigInteger;
import com.alibaba.fastjson2.function.impl.ToBigDecimal;
import com.alibaba.fastjson2.function.impl.ToString;
import java.util.function.Function;
import com.alibaba.fastjson2.function.impl.ToBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.modules.ObjectReaderModule;

public class ObjectReaderBaseModule implements ObjectReaderModule
{
    final ObjectReaderProvider provider;
    final ReaderAnnotationProcessor annotationProcessor;
    
    public ObjectReaderBaseModule(final ObjectReaderProvider provider) {
        this.provider = provider;
        this.annotationProcessor = new ReaderAnnotationProcessor();
    }
    
    @Override
    public ObjectReaderProvider getProvider() {
        return this.provider;
    }
    
    @Override
    public void init(final ObjectReaderProvider provider) {
        provider.registerTypeConvert(Character.class, Character.TYPE, o -> o);
        final Class[] numberTypes = { Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Number.class, Float.class, Double.class, BigInteger.class, BigDecimal.class, AtomicInteger.class, AtomicLong.class };
        final Function<Object, Boolean> TO_BOOLEAN = (Function<Object, Boolean>)new ToBoolean(null);
        for (final Class type : numberTypes) {
            provider.registerTypeConvert(type, Boolean.class, TO_BOOLEAN);
        }
        final Function<Object, Boolean> TO_BOOLEAN_VALUE = (Function<Object, Boolean>)new ToBoolean(Boolean.FALSE);
        for (final Class type2 : numberTypes) {
            provider.registerTypeConvert(type2, Boolean.TYPE, TO_BOOLEAN_VALUE);
        }
        final Function<Object, String> TO_STRING = (Function<Object, String>)new ToString();
        for (final Class type3 : numberTypes) {
            provider.registerTypeConvert(type3, String.class, TO_STRING);
        }
        final Function<Object, BigDecimal> TO_DECIMAL = (Function<Object, BigDecimal>)new ToBigDecimal();
        for (final Class type4 : numberTypes) {
            provider.registerTypeConvert(type4, BigDecimal.class, TO_DECIMAL);
        }
        final Function<Object, BigInteger> TO_BIGINT = (Function<Object, BigInteger>)new ToBigInteger();
        for (final Class type5 : numberTypes) {
            provider.registerTypeConvert(type5, BigInteger.class, TO_BIGINT);
        }
        final Function<Object, Byte> TO_BYTE = (Function<Object, Byte>)new ToByte(null);
        for (final Class type6 : numberTypes) {
            provider.registerTypeConvert(type6, Byte.class, TO_BYTE);
        }
        final Function<Object, Byte> TO_BYTE_VALUE = (Function<Object, Byte>)new ToByte((Byte)0);
        for (final Class type7 : numberTypes) {
            provider.registerTypeConvert(type7, Byte.TYPE, TO_BYTE_VALUE);
        }
        final Function<Object, Short> TO_SHORT = (Function<Object, Short>)new ToShort(null);
        for (final Class type8 : numberTypes) {
            provider.registerTypeConvert(type8, Short.class, TO_SHORT);
        }
        final Function<Object, Short> TO_SHORT_VALUE = (Function<Object, Short>)new ToShort((Short)0);
        for (final Class type9 : numberTypes) {
            provider.registerTypeConvert(type9, Short.TYPE, TO_SHORT_VALUE);
        }
        final Function<Object, Integer> TO_INTEGER = (Function<Object, Integer>)new ToInteger(null);
        for (final Class type10 : numberTypes) {
            provider.registerTypeConvert(type10, Integer.class, TO_INTEGER);
        }
        final Function<Object, Integer> TO_INT = (Function<Object, Integer>)new ToInteger(0);
        for (final Class type11 : numberTypes) {
            provider.registerTypeConvert(type11, Integer.TYPE, TO_INT);
        }
        final Function<Object, Long> TO_LONG = (Function<Object, Long>)new ToLong(null);
        for (final Class type12 : numberTypes) {
            provider.registerTypeConvert(type12, Long.class, TO_LONG);
        }
        final Function<Object, Long> TO_LONG_VALUE = (Function<Object, Long>)new ToLong(0L);
        for (final Class type13 : numberTypes) {
            provider.registerTypeConvert(type13, Long.TYPE, TO_LONG_VALUE);
        }
        final Function<Object, Float> TO_FLOAT = (Function<Object, Float>)new ToFloat(null);
        for (final Class type14 : numberTypes) {
            provider.registerTypeConvert(type14, Float.class, TO_FLOAT);
        }
        final Function<Object, Float> TO_FLOAT_VALUE = (Function<Object, Float>)new ToFloat(0.0f);
        for (final Class type15 : numberTypes) {
            provider.registerTypeConvert(type15, Float.TYPE, TO_FLOAT_VALUE);
        }
        final Function<Object, Double> TO_DOUBLE = (Function<Object, Double>)new ToDouble(null);
        for (final Class type16 : numberTypes) {
            provider.registerTypeConvert(type16, Double.class, TO_DOUBLE);
        }
        final Function<Object, Double> TO_DOUBLE_VALUE = (Function<Object, Double>)new ToDouble(0.0);
        for (final Class type17 : numberTypes) {
            provider.registerTypeConvert(type17, Double.TYPE, TO_DOUBLE_VALUE);
        }
        final Function<Object, Number> TO_NUMBER = (Function<Object, Number>)new ToNumber(0.0);
        for (final Class type18 : numberTypes) {
            provider.registerTypeConvert(type18, Number.class, TO_NUMBER);
        }
        provider.registerTypeConvert(String.class, Character.TYPE, new StringToAny(Character.TYPE, '0'));
        provider.registerTypeConvert(String.class, Boolean.TYPE, new StringToAny(Boolean.TYPE, false));
        provider.registerTypeConvert(String.class, Float.TYPE, new StringToAny(Float.TYPE, 0.0f));
        provider.registerTypeConvert(String.class, Double.TYPE, new StringToAny(Double.TYPE, 0.0));
        provider.registerTypeConvert(String.class, Byte.TYPE, new StringToAny(Byte.TYPE, 0));
        provider.registerTypeConvert(String.class, Short.TYPE, new StringToAny(Short.TYPE, 0));
        provider.registerTypeConvert(String.class, Integer.TYPE, new StringToAny(Integer.TYPE, 0));
        provider.registerTypeConvert(String.class, Long.TYPE, new StringToAny(Long.TYPE, 0L));
        provider.registerTypeConvert(String.class, Character.class, new StringToAny(Character.class, null));
        provider.registerTypeConvert(String.class, Boolean.class, new StringToAny(Boolean.class, null));
        provider.registerTypeConvert(String.class, Double.class, new StringToAny(Double.class, null));
        provider.registerTypeConvert(String.class, Float.class, new StringToAny(Float.class, null));
        provider.registerTypeConvert(String.class, Byte.class, new StringToAny(Byte.class, null));
        provider.registerTypeConvert(String.class, Short.class, new StringToAny(Short.class, null));
        provider.registerTypeConvert(String.class, Integer.class, new StringToAny(Integer.class, null));
        provider.registerTypeConvert(String.class, Long.class, new StringToAny(Long.class, null));
        provider.registerTypeConvert(String.class, BigDecimal.class, new StringToAny(BigDecimal.class, null));
        provider.registerTypeConvert(String.class, BigInteger.class, new StringToAny(BigInteger.class, null));
        provider.registerTypeConvert(String.class, Number.class, new StringToAny(BigDecimal.class, null));
        provider.registerTypeConvert(String.class, Collection.class, new StringToAny(Collection.class, null));
        provider.registerTypeConvert(String.class, List.class, new StringToAny(List.class, null));
        provider.registerTypeConvert(String.class, JSONArray.class, new StringToAny(JSONArray.class, null));
        provider.registerTypeConvert(Boolean.class, Boolean.TYPE, o -> o);
        Function function = o -> (o == null || "null".equals(o) || o.equals(0L)) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(o), ZoneId.systemDefault());
        provider.registerTypeConvert(Long.class, LocalDateTime.class, function);
        function = (o -> (o == null || "null".equals(o) || "".equals(o)) ? null : UUID.fromString(o));
        provider.registerTypeConvert(String.class, UUID.class, function);
    }
    
    private void getBeanInfo1xJSONPOJOBuilder(final BeanInfo beanInfo, final Class<?> builderClass, final Annotation builderAnnatation, final Class<? extends Annotation> builderAnnatationClass) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: aload_3         /* builderAnnatation */
        //     3: aload_1         /* beanInfo */
        //     4: aload_2         /* builderClass */
        //     5: invokedynamic   BootstrapMethod #4, accept:(Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/BeanInfo;Ljava/lang/Class;)Ljava/util/function/Consumer;
        //    10: invokestatic    com/alibaba/fastjson2/util/BeanUtils.annotationMethods:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
        //    13: return         
        //    Signature:
        //  (Lcom/alibaba/fastjson2/codec/BeanInfo;Ljava/lang/Class<*>;Ljava/lang/annotation/Annotation;Ljava/lang/Class<+Ljava/lang/annotation/Annotation;>;)V
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
    
    private void getCreator(final BeanInfo beanInfo, final Class<?> objectClass, final Constructor constructor) {
        if (objectClass.isEnum()) {
            return;
        }
        final Annotation[] annotations = BeanUtils.getAnnotations(constructor);
        boolean creatorMethod = false;
        for (final Annotation annotation : annotations) {
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            final JSONCreator jsonCreator = BeanUtils.findAnnotation(annotation, JSONCreator.class);
            Label_0255: {
                if (jsonCreator != null) {
                    final String[] createParameterNames = jsonCreator.parameterNames();
                    if (createParameterNames.length != 0) {
                        beanInfo.createParameterNames = createParameterNames;
                    }
                    creatorMethod = true;
                    if (jsonCreator == annotation) {
                        break Label_0255;
                    }
                }
                final String name = annotationType.getName();
                switch (name) {
                    case "com.alibaba.fastjson.annotation.JSONCreator": {
                        creatorMethod = true;
                        final Object obj;
                        String[] createParameterNames2;
                        BeanUtils.annotationMethods(annotationType, m1 -> {
                            try {
                                if (m1.getName().equals("parameterNames")) {
                                    createParameterNames2 = (String[])m1.invoke(obj, new Object[0]);
                                    if (createParameterNames2.length != 0) {
                                        beanInfo.createParameterNames = createParameterNames2;
                                    }
                                }
                            }
                            catch (Throwable t) {}
                            return;
                        });
                        break;
                    }
                    case "com.fasterxml.jackson.annotation.JsonCreator":
                    case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonCreator": {
                        if (JSONFactory.isUseJacksonAnnotation()) {
                            creatorMethod = true;
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (!creatorMethod) {
            return;
        }
        Constructor<?> targetConstructor = null;
        try {
            targetConstructor = objectClass.getDeclaredConstructor((Class<?>[])constructor.getParameterTypes());
        }
        catch (NoSuchMethodException ex) {}
        if (targetConstructor != null) {
            beanInfo.creatorConstructor = targetConstructor;
        }
    }
    
    private void getCreator(final BeanInfo beanInfo, final Class<?> objectClass, final Method method) {
        if (method.getDeclaringClass() == Enum.class) {
            return;
        }
        final String methodName = method.getName();
        if (objectClass.isEnum() && methodName.equals("values")) {
            return;
        }
        final Annotation[] annotations = BeanUtils.getAnnotations(method);
        boolean creatorMethod = false;
        JSONCreator jsonCreator = null;
        for (final Annotation annotation : annotations) {
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            jsonCreator = BeanUtils.findAnnotation(annotation, JSONCreator.class);
            if (jsonCreator != annotation) {
                final String name = annotationType.getName();
                switch (name) {
                    case "com.alibaba.fastjson.annotation.JSONCreator": {
                        creatorMethod = true;
                        final Object obj;
                        String[] createParameterNames;
                        BeanUtils.annotationMethods(annotationType, m1 -> {
                            try {
                                if (m1.getName().equals("parameterNames")) {
                                    createParameterNames = (String[])m1.invoke(obj, new Object[0]);
                                    if (createParameterNames.length != 0) {
                                        beanInfo.createParameterNames = createParameterNames;
                                    }
                                }
                            }
                            catch (Throwable t) {}
                            return;
                        });
                        break;
                    }
                    case "com.fasterxml.jackson.annotation.JsonCreator":
                    case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonCreator": {
                        if (JSONFactory.isUseJacksonAnnotation()) {
                            creatorMethod = true;
                            final Object obj2;
                            String[] createParameterNames2;
                            BeanUtils.annotationMethods(annotationType, m1 -> {
                                try {
                                    if (m1.getName().equals("parameterNames")) {
                                        createParameterNames2 = (String[])m1.invoke(obj2, new Object[0]);
                                        if (createParameterNames2.length != 0) {
                                            beanInfo.createParameterNames = createParameterNames2;
                                        }
                                    }
                                }
                                catch (Throwable t2) {}
                                return;
                            });
                            break;
                        }
                        break;
                    }
                }
            }
        }
        if (jsonCreator != null) {
            final String[] createParameterNames3 = jsonCreator.parameterNames();
            if (createParameterNames3.length != 0) {
                beanInfo.createParameterNames = createParameterNames3;
            }
            creatorMethod = true;
        }
        if (!creatorMethod) {
            return;
        }
        Method targetMethod = null;
        try {
            targetMethod = objectClass.getDeclaredMethod(methodName, method.getParameterTypes());
        }
        catch (NoSuchMethodException ex) {}
        if (targetMethod != null) {
            beanInfo.createMethod = targetMethod;
        }
    }
    
    @Override
    public ReaderAnnotationProcessor getAnnotationProcessor() {
        return this.annotationProcessor;
    }
    
    @Override
    public void getBeanInfo(final BeanInfo beanInfo, final Class<?> objectClass) {
        if (this.annotationProcessor != null) {
            this.annotationProcessor.getBeanInfo(beanInfo, objectClass);
        }
    }
    
    @Override
    public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Field field) {
        if (this.annotationProcessor != null) {
            this.annotationProcessor.getFieldInfo(fieldInfo, objectClass, field);
        }
    }
    
    @Override
    public ObjectReader getObjectReader(final ObjectReaderProvider provider, final Type type) {
        if (type == String.class || type == CharSequence.class) {
            return ObjectReaderImplString.INSTANCE;
        }
        if (type == Character.TYPE || type == Character.class) {
            return ObjectReaderImplCharacter.INSTANCE;
        }
        if (type == Boolean.TYPE || type == Boolean.class) {
            return ObjectReaderImplBoolean.INSTANCE;
        }
        if (type == Byte.TYPE || type == Byte.class) {
            return ObjectReaderImplByte.INSTANCE;
        }
        if (type == Short.TYPE || type == Short.class) {
            return ObjectReaderImplShort.INSTANCE;
        }
        if (type == Integer.TYPE || type == Integer.class) {
            return ObjectReaderImplInteger.INSTANCE;
        }
        if (type == Long.TYPE || type == Long.class) {
            return ObjectReaderImplInt64.INSTANCE;
        }
        if (type == Float.TYPE || type == Float.class) {
            return ObjectReaderImplFloat.INSTANCE;
        }
        if (type == Double.TYPE || type == Double.class) {
            return ObjectReaderImplDouble.INSTANCE;
        }
        if (type == BigInteger.class) {
            return ObjectReaderImplBigInteger.INSTANCE;
        }
        if (type == BigDecimal.class) {
            return ObjectReaderImplBigDecimal.INSTANCE;
        }
        if (type == Number.class) {
            return ObjectReaderImplNumber.INSTANCE;
        }
        if (type == BitSet.class) {
            return ObjectReaderImplBitSet.INSTANCE;
        }
        if (type == OptionalInt.class) {
            return ObjectReaderImplOptionalInt.INSTANCE;
        }
        if (type == OptionalLong.class) {
            return ObjectReaderImplOptionalLong.INSTANCE;
        }
        if (type == OptionalDouble.class) {
            return ObjectReaderImplOptionalDouble.INSTANCE;
        }
        if (type == Optional.class) {
            return ObjectReaderImplOptional.INSTANCE;
        }
        if (type == UUID.class) {
            return ObjectReaderImplUUID.INSTANCE;
        }
        if (type == Duration.class) {
            return new ObjectReaderImplFromString(Duration.class, Duration::parse);
        }
        if (type == AtomicBoolean.class) {
            return new ObjectReaderImplFromBoolean(AtomicBoolean.class, AtomicBoolean::new);
        }
        if (type == URI.class) {
            return new ObjectReaderImplFromString(URI.class, URI::create);
        }
        if (type == Charset.class) {
            return new ObjectReaderImplFromString(Charset.class, Charset::forName);
        }
        if (type == File.class) {
            return new ObjectReaderImplFromString(File.class, File::new);
        }
        if (type == Path.class) {
            return new ObjectReaderImplFromString(Path.class, x$0 -> Paths.get(x$0, new String[0]));
        }
        if (type == URL.class) {
            return new ObjectReaderImplFromString(URL.class, e -> {
                try {
                    return new URL(e);
                }
                catch (MalformedURLException ex) {
                    throw new JSONException("read URL error", ex);
                }
            });
        }
        if (type == Pattern.class) {
            return new ObjectReaderImplFromString(Pattern.class, Pattern::compile);
        }
        if (type == Class.class) {
            return ObjectReaderImplClass.INSTANCE;
        }
        if (type == Method.class) {
            return new ObjectReaderImplMethod();
        }
        if (type == Field.class) {
            return new ObjectReaderImplField();
        }
        if (type == Type.class) {
            return ObjectReaderImplClass.INSTANCE;
        }
        String internalMixin = null;
        final String typeName2;
        final String typeName = typeName2 = type.getTypeName();
        switch (typeName2) {
            case "com.google.common.collect.AbstractMapBasedMultimap$WrappedSet": {
                return null;
            }
            case "org.springframework.util.LinkedMultiValueMap": {
                return ObjectReaderImplMap.of(type, (Class)type, 0L);
            }
            case "org.springframework.security.core.authority.RememberMeAuthenticationToken": {
                internalMixin = "org.springframework.security.jackson2.AnonymousAuthenticationTokenMixin";
                break;
            }
            case "org.springframework.security.core.authority.AnonymousAuthenticationToken": {
                internalMixin = "org.springframework.security.jackson2.RememberMeAuthenticationTokenMixin";
                break;
            }
            case "org.springframework.security.core.authority.SimpleGrantedAuthority": {
                internalMixin = "org.springframework.security.jackson2.SimpleGrantedAuthorityMixin";
                break;
            }
            case "org.springframework.security.core.userdetails.User": {
                internalMixin = "org.springframework.security.jackson2.UserMixin";
                break;
            }
            case "org.springframework.security.authentication.UsernamePasswordAuthenticationToken": {
                internalMixin = "org.springframework.security.jackson2.UsernamePasswordAuthenticationTokenMixin";
                break;
            }
            case "org.springframework.security.authentication.BadCredentialsException": {
                internalMixin = "org.springframework.security.jackson2.BadCredentialsExceptionMixin";
                break;
            }
            case "org.springframework.security.web.csrf.DefaultCsrfToken": {
                internalMixin = "org.springframework.security.web.jackson2.DefaultCsrfTokenMixin";
                break;
            }
            case "org.springframework.security.web.savedrequest.SavedCookie": {
                internalMixin = "org.springframework.security.web.jackson2.SavedCookieMixin";
                break;
            }
            case "org.springframework.security.web.authentication.WebAuthenticationDetails": {
                internalMixin = "org.springframework.security.web.jackson2.WebAuthenticationDetailsMixin";
                break;
            }
        }
        if (internalMixin != null) {
            Class mixin = provider.mixInCache.get(type);
            if (mixin == null) {
                mixin = TypeUtils.loadClass(internalMixin);
                if (mixin == null && internalMixin.equals("org.springframework.security.jackson2.SimpleGrantedAuthorityMixin")) {
                    mixin = TypeUtils.loadClass("com.alibaba.fastjson2.internal.mixin.spring.SimpleGrantedAuthorityMixin");
                }
                if (mixin != null) {
                    provider.mixInCache.putIfAbsent((Class)type, mixin);
                }
            }
        }
        if (type == Map.class || type == AbstractMap.class) {
            return ObjectReaderImplMap.of(null, (Class)type, 0L);
        }
        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return typedMap((Class)type, ConcurrentHashMap.class, null, Object.class);
        }
        if (type == ConcurrentNavigableMap.class || type == ConcurrentSkipListMap.class) {
            return typedMap((Class)type, ConcurrentSkipListMap.class, null, Object.class);
        }
        if (type == SortedMap.class || type == NavigableMap.class || type == TreeMap.class) {
            return typedMap((Class)type, TreeMap.class, null, Object.class);
        }
        if (type == Calendar.class || "javax.xml.datatype.XMLGregorianCalendar".equals(typeName)) {
            return ObjectReaderImplCalendar.INSTANCE;
        }
        if (type == Date.class) {
            return ObjectReaderImplDate.INSTANCE;
        }
        if (type == LocalDate.class) {
            return ObjectReaderImplLocalDate.INSTANCE;
        }
        if (type == LocalTime.class) {
            return ObjectReaderImplLocalTime.INSTANCE;
        }
        if (type == LocalDateTime.class) {
            return ObjectReaderImplLocalDateTime.INSTANCE;
        }
        if (type == ZonedDateTime.class) {
            return ObjectReaderImplZonedDateTime.INSTANCE;
        }
        if (type == OffsetDateTime.class) {
            return ObjectReaderImplOffsetDateTime.INSTANCE;
        }
        if (type == Instant.class) {
            return ObjectReaderImplInstant.INSTANCE;
        }
        if (type == Locale.class) {
            return ObjectReaderImplLocale.INSTANCE;
        }
        if (type == Currency.class) {
            return ObjectReaderImplCurrency.INSTANCE;
        }
        if (type == ZoneId.class) {
            return new ObjectReaderImplFromString(ZoneId.class, ZoneId::of);
        }
        if (type == TimeZone.class) {
            return new ObjectReaderImplFromString(TimeZone.class, TimeZone::getTimeZone);
        }
        if (type == char[].class) {
            return ObjectReaderImplCharValueArray.INSTANCE;
        }
        if (type == float[].class) {
            return ObjectReaderImplFloatValueArray.INSTANCE;
        }
        if (type == double[].class) {
            return ObjectReaderImplDoubleValueArray.INSTANCE;
        }
        if (type == boolean[].class) {
            return ObjectReaderImplBoolValueArray.INSTANCE;
        }
        if (type == byte[].class) {
            return ObjectReaderImplInt8ValueArray.INSTANCE;
        }
        if (type == short[].class) {
            return ObjectReaderImplInt16ValueArray.INSTANCE;
        }
        if (type == int[].class) {
            return ObjectReaderImplInt32ValueArray.INSTANCE;
        }
        if (type == long[].class) {
            return ObjectReaderImplInt64ValueArray.INSTANCE;
        }
        if (type == Byte[].class) {
            return ObjectReaderImplInt8Array.INSTANCE;
        }
        if (type == Short[].class) {
            return ObjectReaderImplInt16Array.INSTANCE;
        }
        if (type == Integer[].class) {
            return ObjectReaderImplInt32Array.INSTANCE;
        }
        if (type == Long[].class) {
            return ObjectReaderImplInt64Array.INSTANCE;
        }
        if (type == Float[].class) {
            return ObjectReaderImplFloatArray.INSTANCE;
        }
        if (type == Double[].class) {
            return ObjectReaderImplDoubleArray.INSTANCE;
        }
        if (type == Number[].class) {
            return ObjectReaderImplNumberArray.INSTANCE;
        }
        if (type == String[].class) {
            return ObjectReaderImplStringArray.INSTANCE;
        }
        if (type == AtomicInteger.class) {
            return new ObjectReaderImplFromInt(AtomicInteger.class, AtomicInteger::new);
        }
        if (type == AtomicLong.class) {
            return new ObjectReaderImplFromLong(AtomicLong.class, AtomicLong::new);
        }
        if (type == AtomicIntegerArray.class) {
            return new ObjectReaderImplInt32ValueArray(AtomicIntegerArray.class, (Function<int[], Object>)AtomicIntegerArray::new);
        }
        if (type == AtomicLongArray.class) {
            return new ObjectReaderImplInt64ValueArray(AtomicLongArray.class, (Function<long[], Object>)AtomicLongArray::new);
        }
        if (type == AtomicReference.class) {
            return ObjectReaderImplAtomicReference.INSTANCE;
        }
        if (type instanceof MultiType) {
            return new ObjectArrayReaderMultiType((MultiType)type);
        }
        if (type instanceof MapMultiValueType) {
            return new ObjectReaderImplMapMultiValueType((MapMultiValueType)type);
        }
        Label_1658: {
            if (type != StringBuffer.class) {
                if (type != StringBuilder.class) {
                    break Label_1658;
                }
            }
            try {
                final Class objectClass = (Class)type;
                return new ObjectReaderImplValue(objectClass, String.class, String.class, 0L, null, null, null, objectClass.getConstructor(String.class), null, null);
            }
            catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            }
        }
        if (type == Iterable.class || type == Collection.class || type == List.class || type == AbstractCollection.class || type == AbstractList.class || type == ArrayList.class) {
            return ObjectReaderImplList.of(type, null, 0L);
        }
        if (type == Queue.class || type == Deque.class || type == AbstractSequentialList.class || type == LinkedList.class) {
            return ObjectReaderImplList.of(type, null, 0L);
        }
        if (type == Set.class || type == AbstractSet.class || type == EnumSet.class) {
            return ObjectReaderImplList.of(type, null, 0L);
        }
        if (type == NavigableSet.class || type == SortedSet.class) {
            return ObjectReaderImplList.of(type, null, 0L);
        }
        if (type == ConcurrentLinkedDeque.class || type == ConcurrentLinkedQueue.class || type == ConcurrentSkipListSet.class || type == LinkedHashSet.class || type == HashSet.class || type == TreeSet.class || type == CopyOnWriteArrayList.class) {
            return ObjectReaderImplList.of(type, null, 0L);
        }
        if (type == ObjectReaderImplList.CLASS_EMPTY_SET || type == ObjectReaderImplList.CLASS_EMPTY_LIST || type == ObjectReaderImplList.CLASS_SINGLETON || type == ObjectReaderImplList.CLASS_SINGLETON_LIST || type == ObjectReaderImplList.CLASS_ARRAYS_LIST || type == ObjectReaderImplList.CLASS_UNMODIFIABLE_COLLECTION || type == ObjectReaderImplList.CLASS_UNMODIFIABLE_LIST || type == ObjectReaderImplList.CLASS_UNMODIFIABLE_SET || type == ObjectReaderImplList.CLASS_UNMODIFIABLE_SORTED_SET || type == ObjectReaderImplList.CLASS_UNMODIFIABLE_NAVIGABLE_SET) {
            return ObjectReaderImplList.of(type, null, 0L);
        }
        if (type == TypeUtils.CLASS_SINGLE_SET) {
            return ObjectReaderImplList.of(type, null, 0L);
        }
        if (type == Object.class || type == Cloneable.class || type == Closeable.class || type == Serializable.class || type == Comparable.class) {
            return ObjectReaderImplObject.INSTANCE;
        }
        if (type == Map.Entry.class) {
            return new ObjectReaderImplMapEntry(null, null);
        }
        if (type instanceof Class) {
            final Class objectClass = (Class)type;
            if (BeanUtils.isExtendedMap(objectClass)) {
                return null;
            }
            if (Map.class.isAssignableFrom(objectClass)) {
                return ObjectReaderImplMap.of(null, objectClass, 0L);
            }
            if (Collection.class.isAssignableFrom(objectClass)) {
                return ObjectReaderImplList.of(objectClass, objectClass, 0L);
            }
            if (objectClass.isArray()) {
                final Class componentType = objectClass.getComponentType();
                if (componentType == Object.class) {
                    return ObjectArrayReader.INSTANCE;
                }
                return new ObjectArrayTypedReader(objectClass);
            }
            else {
                if (JSONPObject.class.isAssignableFrom(objectClass)) {
                    return new ObjectReaderImplJSONP(objectClass);
                }
                final ObjectReaderCreator creator = JSONFactory.getDefaultObjectReaderProvider().getCreator();
                if (objectClass == StackTraceElement.class) {
                    try {
                        final Constructor constructor = objectClass.getConstructor(String.class, String.class, String.class, Integer.TYPE);
                        return creator.createObjectReaderNoneDefaultConstructor(constructor, "className", "methodName", "fileName", "lineNumber");
                    }
                    catch (Throwable t) {}
                }
            }
        }
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final Type rawType = parameterizedType.getRawType();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 2) {
                final Type actualTypeParam0 = actualTypeArguments[0];
                final Type actualTypeParam2 = actualTypeArguments[1];
                if (rawType == Map.class || rawType == AbstractMap.class || rawType == HashMap.class) {
                    return typedMap((Class)rawType, HashMap.class, actualTypeParam0, actualTypeParam2);
                }
                if (rawType == ConcurrentMap.class || rawType == ConcurrentHashMap.class) {
                    return typedMap((Class)rawType, ConcurrentHashMap.class, actualTypeParam0, actualTypeParam2);
                }
                if (rawType == ConcurrentNavigableMap.class || rawType == ConcurrentSkipListMap.class) {
                    return typedMap((Class)rawType, ConcurrentSkipListMap.class, actualTypeParam0, actualTypeParam2);
                }
                if (rawType == LinkedHashMap.class || rawType == TreeMap.class || rawType == Hashtable.class) {
                    return typedMap((Class)rawType, (Class)rawType, actualTypeParam0, actualTypeParam2);
                }
                if (rawType == Map.Entry.class) {
                    return new ObjectReaderImplMapEntry(actualTypeParam0, actualTypeParam2);
                }
                final String typeName3 = rawType.getTypeName();
                switch (typeName3) {
                    case "com.google.common.collect.ImmutableMap":
                    case "com.google.common.collect.RegularImmutableMap": {
                        return new ObjectReaderImplMapTyped((Class)rawType, HashMap.class, actualTypeParam0, actualTypeParam2, 0L, GuavaSupport.immutableMapConverter());
                    }
                    case "com.google.common.collect.SingletonImmutableBiMap": {
                        return new ObjectReaderImplMapTyped((Class)rawType, HashMap.class, actualTypeParam0, actualTypeParam2, 0L, GuavaSupport.singletonBiMapConverter());
                    }
                    case "org.springframework.util.LinkedMultiValueMap": {
                        return ObjectReaderImplMap.of(type, (Class)rawType, 0L);
                    }
                    case "org.apache.commons.lang3.tuple.Pair":
                    case "org.apache.commons.lang3.tuple.ImmutablePair": {
                        return new ApacheLang3Support.PairReader((Class)rawType, actualTypeParam0, actualTypeParam2);
                    }
                }
            }
            else if (actualTypeArguments.length == 1) {
                final Type itemType = actualTypeArguments[0];
                final Class itemClass = TypeUtils.getMapping(itemType);
                if (rawType == Iterable.class || rawType == Collection.class || rawType == List.class || rawType == AbstractCollection.class || rawType == AbstractList.class || rawType == ArrayList.class) {
                    if (itemClass == String.class) {
                        return new ObjectReaderImplListStr((Class)rawType, ArrayList.class);
                    }
                    if (itemClass == Long.class) {
                        return new ObjectReaderImplListInt64((Class)rawType, ArrayList.class);
                    }
                    return ObjectReaderImplList.of(type, null, 0L);
                }
                else if (rawType == Queue.class || rawType == Deque.class || rawType == AbstractSequentialList.class || rawType == LinkedList.class) {
                    if (itemClass == String.class) {
                        return new ObjectReaderImplListStr((Class)rawType, LinkedList.class);
                    }
                    if (itemClass == Long.class) {
                        return new ObjectReaderImplListInt64((Class)rawType, LinkedList.class);
                    }
                    return ObjectReaderImplList.of(type, null, 0L);
                }
                else if (rawType == Set.class || rawType == AbstractSet.class || rawType == EnumSet.class) {
                    if (itemClass == String.class) {
                        return new ObjectReaderImplListStr((Class)rawType, HashSet.class);
                    }
                    if (itemClass == Long.class) {
                        return new ObjectReaderImplListInt64((Class)rawType, HashSet.class);
                    }
                    return ObjectReaderImplList.of(type, null, 0L);
                }
                else if (rawType == NavigableSet.class || rawType == SortedSet.class) {
                    if (itemType == String.class) {
                        return new ObjectReaderImplListStr((Class)rawType, TreeSet.class);
                    }
                    if (itemClass == Long.class) {
                        return new ObjectReaderImplListInt64((Class)rawType, TreeSet.class);
                    }
                    return ObjectReaderImplList.of(type, null, 0L);
                }
                else if (rawType == ConcurrentLinkedDeque.class || rawType == ConcurrentLinkedQueue.class || rawType == ConcurrentSkipListSet.class || rawType == LinkedHashSet.class || rawType == HashSet.class || rawType == TreeSet.class || rawType == CopyOnWriteArrayList.class) {
                    if (itemType == String.class) {
                        return new ObjectReaderImplListStr((Class)rawType, (Class)rawType);
                    }
                    if (itemClass == Long.class) {
                        return new ObjectReaderImplListInt64((Class)rawType, (Class)rawType);
                    }
                    return ObjectReaderImplList.of(type, null, 0L);
                }
                else {
                    final String typeName4 = rawType.getTypeName();
                    switch (typeName4) {
                        case "com.google.common.collect.ImmutableList":
                        case "com.google.common.collect.ImmutableSet":
                        case "com.google.common.collect.SingletonImmutableSet": {
                            return ObjectReaderImplList.of(type, null, 0L);
                        }
                        default: {
                            if (rawType == Optional.class) {
                                return ObjectReaderImplOptional.of(type, null, null);
                            }
                            if (rawType == AtomicReference.class) {
                                return new ObjectReaderImplAtomicReference(itemType);
                            }
                            if (itemType instanceof WildcardType) {
                                return this.getObjectReader(provider, rawType);
                            }
                            break;
                        }
                    }
                }
            }
            return null;
        }
        if (type instanceof GenericArrayType) {
            return new ObjectReaderImplGenericArray((GenericArrayType)type);
        }
        if (type instanceof WildcardType) {
            final Type[] upperBounds = ((WildcardType)type).getUpperBounds();
            if (upperBounds.length == 1) {
                return this.getObjectReader(provider, upperBounds[0]);
            }
        }
        if (type == ParameterizedType.class) {
            return ObjectReaders.ofReflect(ParameterizedTypeImpl.class);
        }
        final String s = typeName;
        switch (s) {
            case "java.sql.Time": {
                return JdbcSupport.createTimeReader((Class)type, null, null);
            }
            case "java.sql.Timestamp": {
                return JdbcSupport.createTimestampReader((Class)type, null, null);
            }
            case "java.sql.Date": {
                return JdbcSupport.createDateReader((Class)type, null, null);
            }
            case "java.util.RegularEnumSet":
            case "java.util.JumboEnumSet": {
                return ObjectReaderImplList.of(type, TypeUtils.getClass(type), 0L);
            }
            case "org.joda.time.Chronology": {
                return JodaSupport.createChronologyReader((Class)type);
            }
            case "org.joda.time.LocalDate": {
                return JodaSupport.createLocalDateReader((Class)type);
            }
            case "org.joda.time.LocalDateTime": {
                return JodaSupport.createLocalDateTimeReader((Class)type);
            }
            case "org.joda.time.Instant": {
                return JodaSupport.createInstantReader((Class)type);
            }
            case "org.joda.time.DateTime": {
                return new ObjectReaderImplZonedDateTime(new JodaSupport.DateTimeFromZDT());
            }
            case "javax.money.CurrencyUnit": {
                return MoneySupport.createCurrencyUnitReader();
            }
            case "javax.money.MonetaryAmount":
            case "javax.money.Money": {
                return MoneySupport.createMonetaryAmountReader();
            }
            case "javax.money.NumberValue": {
                return MoneySupport.createNumberValueReader();
            }
            case "java.net.InetSocketAddress": {
                return new ObjectReaderMisc((Class)type);
            }
            case "java.net.InetAddress": {
                return ObjectReaderImplValue.of((Class<InetAddress>)type, String.class, address -> {
                    try {
                        return InetAddress.getByName(address);
                    }
                    catch (UnknownHostException e3) {
                        throw new JSONException("create address error", e3);
                    }
                });
            }
            case "java.text.SimpleDateFormat": {
                return ObjectReaderImplValue.of((Class<SimpleDateFormat>)type, String.class, SimpleDateFormat::new);
            }
            case "java.lang.Throwable":
            case "java.lang.Exception":
            case "java.lang.IllegalStateException":
            case "java.lang.RuntimeException":
            case "java.io.IOException":
            case "java.io.UncheckedIOException": {
                return new ObjectReaderException((Class)type);
            }
            case "java.nio.HeapByteBuffer":
            case "java.nio.ByteBuffer": {
                return new ObjectReaderImplInt8ValueArray((Function<byte[], Object>)ByteBuffer::wrap, null);
            }
            case "org.apache.commons.lang3.tuple.Pair":
            case "org.apache.commons.lang3.tuple.ImmutablePair": {
                return new ApacheLang3Support.PairReader((Class)type, Object.class, Object.class);
            }
            case "com.google.common.collect.ImmutableList":
            case "com.google.common.collect.ImmutableSet":
            case "com.google.common.collect.SingletonImmutableSet":
            case "com.google.common.collect.RegularImmutableSet":
            case "com.google.common.collect.AbstractMapBasedMultimap$RandomAccessWrappedList": {
                return ObjectReaderImplList.of(type, null, 0L);
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
            case "org.bson.types.Decimal128": {
                return LambdaMiscCodec.getObjectReader((Class)type);
            }
            default: {
                return null;
            }
        }
    }
    
    public static ObjectReader typedMap(final Class mapType, final Class instanceType, final Type keyType, final Type valueType) {
        if ((keyType == null || keyType == String.class) && valueType == String.class) {
            return new ObjectReaderImplMapString(mapType, instanceType, 0L);
        }
        return new ObjectReaderImplMapTyped(mapType, instanceType, keyType, valueType, 0L, null);
    }
    
    public class ReaderAnnotationProcessor implements ObjectReaderAnnotationProcessor
    {
        @Override
        public void getBeanInfo(final BeanInfo beanInfo, final Class<?> objectClass) {
            Class mixInSource = ObjectReaderBaseModule.this.provider.mixInCache.get(objectClass);
            if (mixInSource == null) {
                final String typeName = objectClass.getName();
                if (typeName.equals("org.apache.commons.lang3.tuple.Triple")) {
                    ObjectReaderBaseModule.this.provider.mixIn(objectClass, mixInSource = ApacheLang3Support.TripleMixIn.class);
                }
            }
            if (mixInSource != null && mixInSource != objectClass) {
                beanInfo.mixIn = true;
                this.getBeanInfo(beanInfo, BeanUtils.getAnnotations(mixInSource));
                BeanUtils.staticMethod(mixInSource, method -> ObjectReaderBaseModule.this.getCreator(beanInfo, objectClass, method));
                BeanUtils.constructor(mixInSource, constructor -> ObjectReaderBaseModule.this.getCreator(beanInfo, objectClass, constructor));
            }
            Class seeAlsoClass = null;
            for (Class superClass = objectClass.getSuperclass(); superClass != null && superClass != Object.class && superClass != Enum.class; superClass = superClass.getSuperclass()) {
                final BeanInfo superBeanInfo = new BeanInfo();
                this.getBeanInfo(superBeanInfo, superClass);
                if (superBeanInfo.seeAlso != null) {
                    boolean inSeeAlso = false;
                    for (final Class seeAlsoItem : superBeanInfo.seeAlso) {
                        if (seeAlsoItem == objectClass) {
                            inSeeAlso = true;
                            break;
                        }
                    }
                    if (!inSeeAlso) {
                        seeAlsoClass = superClass;
                    }
                }
            }
            if (seeAlsoClass != null) {
                this.getBeanInfo(beanInfo, seeAlsoClass);
            }
            final Annotation[] annotations = BeanUtils.getAnnotations(objectClass);
            this.getBeanInfo(beanInfo, annotations);
            for (final Annotation annotation : annotations) {
                final boolean useJacksonAnnotation = JSONFactory.isUseJacksonAnnotation();
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                final String name;
                final String annotationTypeName = name = annotationType.getName();
                switch (name) {
                    case "com.alibaba.fastjson.annotation.JSONType": {
                        this.getBeanInfo1x(beanInfo, annotation);
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
                    case "com.fasterxml.jackson.databind.annotation.JsonDeserialize":
                    case "com.alibaba.fastjson2.adapter.jackson.databind.annotation.JsonDeserialize": {
                        if (useJacksonAnnotation) {
                            this.processJacksonJsonDeserializer(beanInfo, annotation);
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
                        break;
                    }
                }
            }
            BeanUtils.staticMethod(objectClass, method -> ObjectReaderBaseModule.this.getCreator(beanInfo, objectClass, method));
            BeanUtils.constructor(objectClass, constructor -> ObjectReaderBaseModule.this.getCreator(beanInfo, objectClass, constructor));
            if (beanInfo.creatorConstructor == null && (beanInfo.readerFeatures & JSONReader.Feature.FieldBased.mask) == 0x0L && beanInfo.kotlin) {
                KotlinUtils.getConstructor(objectClass, beanInfo);
            }
        }
        
        private void processJacksonJsonSubTypes(final BeanInfo beanInfo, final Annotation annotation) {
            final Class<? extends Annotation> annotationClass = annotation.getClass();
            final String name;
            Object result;
            Object[] value;
            int i;
            Annotation subTypeAnn;
            BeanUtils.annotationMethods(annotationClass, m -> {
                name = m.getName();
                try {
                    result = m.invoke(annotation, new Object[0]);
                    if (name.equals("value")) {
                        value = (Object[])result;
                        if (value.length != 0) {
                            beanInfo.seeAlso = new Class[value.length];
                            beanInfo.seeAlsoNames = new String[value.length];
                            for (i = 0; i < value.length; ++i) {
                                subTypeAnn = (Annotation)value[i];
                                BeanUtils.processJacksonJsonSubTypesType(beanInfo, i, subTypeAnn);
                            }
                        }
                    }
                }
                catch (Throwable t) {}
            });
        }
        
        private void processJacksonJsonDeserializer(final BeanInfo beanInfo, final Annotation annotation) {
            final Class<? extends Annotation> annotationClass = annotation.getClass();
            final String name;
            Object result;
            Class using;
            BeanUtils.annotationMethods(annotationClass, m -> {
                name = m.getName();
                try {
                    result = m.invoke(annotation, new Object[0]);
                    if (name.equals("using")) {
                        using = this.processUsing((Class)result);
                        if (using != null) {
                            beanInfo.deserializer = using;
                        }
                    }
                }
                catch (Throwable t) {}
            });
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
                            beanInfo.readerFeatures |= JSONReader.Feature.SupportAutoType.mask;
                        }
                    }
                }
                catch (Throwable t) {}
            });
        }
        
        private void getBeanInfo(final BeanInfo beanInfo, final Annotation[] annotations) {
            for (final Annotation annotation : annotations) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                final JSONType jsonType = BeanUtils.findAnnotation(annotation, JSONType.class);
                Label_0104: {
                    if (jsonType != null) {
                        this.getBeanInfo1x(beanInfo, annotation);
                        if (jsonType == annotation) {
                            break Label_0104;
                        }
                    }
                    if (annotationType == JSONCompiler.class) {
                        final JSONCompiler compiler = (JSONCompiler)annotation;
                        if (compiler.value() == JSONCompiler.CompilerOption.LAMBDA) {
                            beanInfo.readerFeatures |= 0x40000000000000L;
                        }
                    }
                }
            }
        }
        
        void getBeanInfo1x(final BeanInfo beanInfo, final Annotation annotation) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     4: astore_3        /* annotationClass */
            //     5: aload_3         /* annotationClass */
            //     6: aload_0         /* this */
            //     7: aload_2         /* annotation */
            //     8: aload_1         /* beanInfo */
            //     9: invokedynamic   BootstrapMethod #7, accept:(Lcom/alibaba/fastjson2/reader/ObjectReaderBaseModule$ReaderAnnotationProcessor;Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/BeanInfo;)Ljava/util/function/Consumer;
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
        
        private void processSeeAlsoAnnotation(final BeanInfo beanInfo, final Class<?> objectClass) {
            Class mixInSource = ObjectReaderBaseModule.this.provider.mixInCache.get(objectClass);
            if (mixInSource == null) {
                final String typeName = objectClass.getName();
                if (typeName.equals("org.apache.commons.lang3.tuple.Triple")) {
                    ObjectReaderBaseModule.this.provider.mixIn(objectClass, mixInSource = ApacheLang3Support.TripleMixIn.class);
                }
            }
            if (mixInSource != null && mixInSource != objectClass) {
                beanInfo.mixIn = true;
                this.processSeeAlsoAnnotation(beanInfo, BeanUtils.getAnnotations(mixInSource));
            }
            this.processSeeAlsoAnnotation(beanInfo, BeanUtils.getAnnotations(objectClass));
        }
        
        private void processSeeAlsoAnnotation(final BeanInfo beanInfo, final Annotation[] annotations) {
            for (final Annotation annotation : annotations) {
                final Class<? extends Annotation> itemAnnotationType = annotation.annotationType();
                final String name;
                final Object obj;
                Object result;
                String typeName;
                BeanUtils.annotationMethods(itemAnnotationType, m -> {
                    name = m.getName();
                    try {
                        result = m.invoke(obj, new Object[0]);
                        if (name.equals("typeName")) {
                            typeName = (String)result;
                            if (!typeName.isEmpty()) {
                                beanInfo.typeName = typeName;
                            }
                        }
                    }
                    catch (Throwable t) {}
                    return;
                });
            }
        }
        
        @Override
        public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Constructor constructor, final int paramIndex, final Parameter parameter) {
            if (objectClass != null) {
                final Class mixInSource = ObjectReaderBaseModule.this.provider.mixInCache.get(objectClass);
                if (mixInSource != null && mixInSource != objectClass) {
                    Constructor mixInConstructor = null;
                    try {
                        mixInConstructor = mixInSource.getDeclaredConstructor(constructor.getParameterTypes());
                    }
                    catch (NoSuchMethodException ex) {}
                    if (mixInConstructor != null) {
                        final Parameter mixInParam = mixInConstructor.getParameters()[paramIndex];
                        this.processAnnotation(fieldInfo, BeanUtils.getAnnotations(mixInParam));
                    }
                }
            }
            final boolean staticClass = Modifier.isStatic(constructor.getDeclaringClass().getModifiers());
            Annotation[] annotations = null;
            if (staticClass) {
                try {
                    annotations = BeanUtils.getAnnotations(parameter);
                }
                catch (ArrayIndexOutOfBoundsException ex2) {}
            }
            else {
                final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
                int paIndex;
                if (parameterAnnotations.length == constructor.getParameterCount()) {
                    paIndex = paramIndex;
                }
                else {
                    paIndex = paramIndex - 1;
                }
                if (paIndex >= 0 && paIndex < parameterAnnotations.length) {
                    annotations = parameterAnnotations[paIndex];
                }
            }
            if (annotations != null && annotations.length > 0) {
                this.processAnnotation(fieldInfo, annotations);
            }
        }
        
        @Override
        public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Method method, final int paramIndex, final Parameter parameter) {
            if (objectClass != null) {
                final Class mixInSource = ObjectReaderBaseModule.this.provider.mixInCache.get(objectClass);
                if (mixInSource != null && mixInSource != objectClass) {
                    Method mixInMethod = null;
                    try {
                        mixInMethod = mixInSource.getMethod(method.getName(), (Class[])method.getParameterTypes());
                    }
                    catch (NoSuchMethodException ex) {}
                    if (mixInMethod != null) {
                        final Parameter mixInParam = mixInMethod.getParameters()[paramIndex];
                        this.processAnnotation(fieldInfo, BeanUtils.getAnnotations(mixInParam));
                    }
                }
            }
            this.processAnnotation(fieldInfo, BeanUtils.getAnnotations(parameter));
        }
        
        @Override
        public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Field field) {
            if (objectClass != null) {
                final Class mixInSource = ObjectReaderBaseModule.this.provider.mixInCache.get(objectClass);
                if (mixInSource != null && mixInSource != objectClass) {
                    Field mixInField = null;
                    try {
                        mixInField = mixInSource.getDeclaredField(field.getName());
                    }
                    catch (Exception ex) {}
                    if (mixInField != null) {
                        this.getFieldInfo(fieldInfo, mixInSource, mixInField);
                    }
                }
            }
            final Annotation[] annotations = BeanUtils.getAnnotations(field);
            this.processAnnotation(fieldInfo, annotations);
        }
        
        @Override
        public void getFieldInfo(final FieldInfo fieldInfo, final Class objectClass, final Method method) {
            final String methodName = method.getName();
            if (objectClass != null) {
                final Class superclass = objectClass.getSuperclass();
                final Method supperMethod = BeanUtils.getMethod(superclass, method);
                if (supperMethod != null) {
                    this.getFieldInfo(fieldInfo, superclass, supperMethod);
                }
                final Class[] interfaces2;
                final Class[] interfaces = interfaces2 = objectClass.getInterfaces();
                for (final Class i : interfaces2) {
                    if (i != Serializable.class) {
                        final Method interfaceMethod = BeanUtils.getMethod(i, method);
                        if (interfaceMethod != null) {
                            this.getFieldInfo(fieldInfo, superclass, interfaceMethod);
                        }
                    }
                }
                final Class mixInSource = ObjectReaderBaseModule.this.provider.mixInCache.get(objectClass);
                if (mixInSource != null && mixInSource != objectClass) {
                    Method mixInMethod = null;
                    try {
                        mixInMethod = mixInSource.getDeclaredMethod(methodName, (Class[])method.getParameterTypes());
                    }
                    catch (Exception ex) {}
                    if (mixInMethod != null) {
                        this.getFieldInfo(fieldInfo, mixInSource, mixInMethod);
                    }
                }
            }
            String jsonFieldName = null;
            final Annotation[] annotations2;
            final Annotation[] annotations = annotations2 = BeanUtils.getAnnotations(method);
            for (final Annotation annotation : annotations2) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                final JSONField jsonField = BeanUtils.findAnnotation(annotation, JSONField.class);
                Label_0872: {
                    if (jsonField != null) {
                        this.getFieldInfo(fieldInfo, jsonField);
                        jsonFieldName = jsonField.name();
                        if (jsonField == annotation) {
                            break Label_0872;
                        }
                    }
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
                        case "com.alibaba.fastjson2.adapter.jackson.databind.annotation.JsonDeserialize":
                        case "com.fasterxml.jackson.databind.annotation.JsonDeserialize": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonDeserialize(fieldInfo, annotation);
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
                        case "com.fasterxml.jackson.annotation.JsonAnySetter":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonAnySetter": {
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
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonProperty":
                        case "com.fasterxml.jackson.annotation.JsonProperty": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonProperty(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonAlias":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonAlias": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonAlias(fieldInfo, annotation);
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
            String fieldName;
            if (methodName.startsWith("set")) {
                fieldName = BeanUtils.setterName(methodName, null);
            }
            else {
                fieldName = BeanUtils.getterName(methodName, null);
            }
            final char c0;
            final char c2;
            String fieldName2;
            String fieldName3;
            if (fieldName.length() > 1 && (c0 = fieldName.charAt(0)) >= 'A' && c0 <= 'Z' && (c2 = fieldName.charAt(1)) >= 'A' && c2 <= 'Z' && (jsonFieldName == null || jsonFieldName.isEmpty())) {
                final char[] chars = fieldName.toCharArray();
                chars[0] += ' ';
                fieldName2 = new String(chars);
                chars[1] += ' ';
                fieldName3 = new String(chars);
            }
            else {
                fieldName2 = null;
                fieldName3 = null;
            }
            final Object anObject;
            int modifiers;
            final Object anObject2;
            int modifiers2;
            final Object anObject3;
            int modifiers3;
            BeanUtils.declaredFields(objectClass, field -> {
                if (field.getName().equals(anObject)) {
                    modifiers = field.getModifiers();
                    if (!Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                        this.getFieldInfo(fieldInfo, objectClass, field);
                    }
                    fieldInfo.features |= 0x10000000000000L;
                }
                else if (field.getName().equals(anObject2)) {
                    modifiers2 = field.getModifiers();
                    if (!Modifier.isPublic(modifiers2) && !Modifier.isStatic(modifiers2)) {
                        this.getFieldInfo(fieldInfo, objectClass, field);
                    }
                    fieldInfo.features |= 0x10000000000000L;
                }
                else if (field.getName().equals(anObject3)) {
                    modifiers3 = field.getModifiers();
                    if (!Modifier.isPublic(modifiers3) && !Modifier.isStatic(modifiers3)) {
                        this.getFieldInfo(fieldInfo, objectClass, field);
                    }
                    fieldInfo.features |= 0x10000000000000L;
                }
                return;
            });
            if (fieldName2 != null && fieldInfo.fieldName == null && fieldInfo.alternateNames == null) {
                fieldInfo.alternateNames = new String[] { fieldName2, fieldName3 };
            }
        }
        
        private void processAnnotation(final FieldInfo fieldInfo, final Annotation[] annotations) {
            for (final Annotation annotation : annotations) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                final JSONField jsonField = BeanUtils.findAnnotation(annotation, JSONField.class);
                Label_0676: {
                    if (jsonField != null) {
                        this.getFieldInfo(fieldInfo, jsonField);
                        if (jsonField == annotation) {
                            break Label_0676;
                        }
                    }
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
                        case "com.fasterxml.jackson.databind.annotation.JsonDeserialize":
                        case "com.alibaba.fastjson2.adapter.jackson.databind.annotation.JsonDeserialize": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonDeserialize(fieldInfo, annotation);
                                break;
                            }
                            break;
                        }
                        case "com.fasterxml.jackson.annotation.JsonAlias":
                        case "com.alibaba.fastjson2.adapter.jackson.annotation.JsonAlias": {
                            if (useJacksonAnnotation) {
                                this.processJacksonJsonAlias(fieldInfo, annotation);
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
        }
        
        private void processJacksonJsonDeserialize(final FieldInfo fieldInfo, final Annotation annotation) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: ifne            7
            //     6: return         
            //     7: aload_2         /* annotation */
            //     8: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
            //    11: astore_3        /* annotationClass */
            //    12: aload_3         /* annotationClass */
            //    13: aload_0         /* this */
            //    14: aload_2         /* annotation */
            //    15: aload_1         /* fieldInfo */
            //    16: invokedynamic   BootstrapMethod #10, accept:(Lcom/alibaba/fastjson2/reader/ObjectReaderBaseModule$ReaderAnnotationProcessor;Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/FieldInfo;)Ljava/util/function/Consumer;
            //    21: invokestatic    com/alibaba/fastjson2/util/BeanUtils.annotationMethods:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
            //    24: return         
            //    StackMapTable: 00 01 07
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
        
        private Class processUsing(final Class using) {
            final String usingName = using.getName();
            final String noneClassName0 = "com.fasterxml.jackson.databind.JsonDeserializer$None";
            final String noneClassName2 = "com.alibaba.fastjson2.adapter.jackson.databind.JsonDeserializer$None";
            if (!noneClassName0.equals(usingName) && !noneClassName2.equals(usingName) && ObjectReader.class.isAssignableFrom(using)) {
                return using;
            }
            return null;
        }
        
        private void processJacksonJsonProperty(final FieldInfo fieldInfo, final Annotation annotation) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: ifne            7
            //     6: return         
            //     7: aload_2         /* annotation */
            //     8: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
            //    11: astore_3        /* annotationClass */
            //    12: aload_3         /* annotationClass */
            //    13: aload_2         /* annotation */
            //    14: aload_1         /* fieldInfo */
            //    15: invokedynamic   BootstrapMethod #11, accept:(Ljava/lang/annotation/Annotation;Lcom/alibaba/fastjson2/codec/FieldInfo;)Ljava/util/function/Consumer;
            //    20: invokestatic    com/alibaba/fastjson2/util/BeanUtils.annotationMethods:(Ljava/lang/Class;Ljava/util/function/Consumer;)V
            //    23: return         
            //    StackMapTable: 00 01 07
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
        
        private void processJacksonJsonAlias(final FieldInfo fieldInfo, final Annotation annotation) {
            final Class<? extends Annotation> annotationClass = annotation.getClass();
            final String name;
            Object result;
            String[] values;
            BeanUtils.annotationMethods(annotationClass, m -> {
                name = m.getName();
                try {
                    result = m.invoke(annotation, new Object[0]);
                    if (name.equals("value")) {
                        values = (String[])result;
                        if (values.length != 0) {
                            fieldInfo.alternateNames = values;
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
            //     6: aload_2         /* annotation */
            //     7: aload_1         /* fieldInfo */
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
        
        private void getFieldInfo(final FieldInfo fieldInfo, final JSONField jsonField) {
            if (jsonField == null) {
                return;
            }
            final String jsonFieldName = jsonField.name();
            if (!jsonFieldName.isEmpty()) {
                fieldInfo.fieldName = jsonFieldName;
            }
            String jsonFieldFormat = jsonField.format();
            if (!jsonFieldFormat.isEmpty()) {
                jsonFieldFormat = jsonFieldFormat.trim();
                if (jsonFieldFormat.indexOf(84) != -1 && !jsonFieldFormat.contains("'T'")) {
                    jsonFieldFormat = jsonFieldFormat.replaceAll("T", "'T'");
                }
                fieldInfo.format = jsonFieldFormat;
            }
            String label = jsonField.label();
            if (!label.isEmpty()) {
                label = label.trim();
                fieldInfo.label = label;
            }
            final String defaultValue = jsonField.defaultValue();
            if (!defaultValue.isEmpty()) {
                fieldInfo.defaultValue = defaultValue;
            }
            final String locale = jsonField.locale();
            if (!locale.isEmpty()) {
                final String[] parts = locale.split("_");
                if (parts.length == 2) {
                    fieldInfo.locale = new Locale(parts[0], parts[1]);
                }
            }
            final String[] alternateNames = jsonField.alternateNames();
            if (alternateNames.length != 0) {
                if (fieldInfo.alternateNames == null) {
                    fieldInfo.alternateNames = alternateNames;
                }
                else {
                    final Set<String> nameSet = new LinkedHashSet<String>();
                    nameSet.addAll(Arrays.asList(alternateNames));
                    nameSet.addAll(Arrays.asList(fieldInfo.alternateNames));
                    fieldInfo.alternateNames = nameSet.toArray(new String[nameSet.size()]);
                }
            }
            if (!fieldInfo.ignore) {
                fieldInfo.ignore = !jsonField.deserialize();
            }
            for (final JSONReader.Feature feature : jsonField.deserializeFeatures()) {
                fieldInfo.features |= feature.mask;
            }
            final int ordinal = jsonField.ordinal();
            if (ordinal != 0) {
                fieldInfo.ordinal = ordinal;
            }
            final boolean value = jsonField.value();
            if (value) {
                fieldInfo.features |= 0x1000000000000L;
            }
            if (jsonField.unwrapped()) {
                fieldInfo.features |= 0x2000000000000L;
            }
            if (jsonField.required()) {
                fieldInfo.required = true;
            }
            final String schema = jsonField.schema().trim();
            if (!schema.isEmpty()) {
                fieldInfo.schema = schema;
            }
            final Class deserializeUsing = jsonField.deserializeUsing();
            if (ObjectReader.class.isAssignableFrom(deserializeUsing)) {
                fieldInfo.readUsing = (Class<?>)deserializeUsing;
            }
        }
    }
}
