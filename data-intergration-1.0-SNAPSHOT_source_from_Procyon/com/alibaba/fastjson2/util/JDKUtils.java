// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.lang.invoke.CallSite;
import java.lang.reflect.Method;
import java.lang.invoke.LambdaMetafactory;
import java.util.List;
import java.nio.ByteOrder;
import java.math.BigDecimal;
import java.lang.invoke.MethodType;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.invoke.MethodHandles;
import java.util.function.Predicate;
import java.lang.invoke.MethodHandle;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.BiFunction;
import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class JDKUtils
{
    public static final Unsafe UNSAFE;
    public static final long ARRAY_BYTE_BASE_OFFSET;
    public static final long ARRAY_CHAR_BASE_OFFSET;
    public static final int JVM_VERSION;
    public static final Byte LATIN1;
    public static final Byte UTF16;
    public static final Field FIELD_STRING_VALUE;
    public static final long FIELD_STRING_VALUE_OFFSET;
    public static volatile boolean FIELD_STRING_VALUE_ERROR;
    public static final long FIELD_DECIMAL_INT_COMPACT_OFFSET;
    public static final Field FIELD_STRING_CODER;
    public static final long FIELD_STRING_CODER_OFFSET;
    public static volatile boolean FIELD_STRING_CODER_ERROR;
    static final Class<?> CLASS_SQL_DATASOURCE;
    static final Class<?> CLASS_SQL_ROW_SET;
    public static final boolean HAS_SQL;
    public static final boolean ANDROID;
    public static final boolean GRAAL;
    public static final boolean OPENJ9;
    public static final Class CLASS_TRANSIENT;
    public static final boolean BIG_ENDIAN;
    public static final boolean UNSAFE_SUPPORT = true;
    public static final boolean VECTOR_SUPPORT;
    public static final int VECTOR_BIT_LENGTH;
    public static final BiFunction<char[], Boolean, String> STRING_CREATOR_JDK8;
    public static final BiFunction<byte[], Byte, String> STRING_CREATOR_JDK11;
    public static final ToIntFunction<String> STRING_CODER;
    public static final Function<String, byte[]> STRING_VALUE;
    public static final MethodHandle METHOD_HANDLE_HAS_NEGATIVE;
    public static final Predicate<byte[]> PREDICATE_IS_ASCII;
    static final MethodHandles.Lookup IMPL_LOOKUP;
    static volatile MethodHandle CONSTRUCTOR_LOOKUP;
    static volatile boolean CONSTRUCTOR_LOOKUP_ERROR;
    static volatile Throwable initErrorLast;
    static volatile Throwable reflectErrorLast;
    static final AtomicInteger reflectErrorCount;
    
    public static boolean isSQLDataSourceOrRowSet(final Class<?> type) {
        return (JDKUtils.CLASS_SQL_DATASOURCE != null && JDKUtils.CLASS_SQL_DATASOURCE.isAssignableFrom(type)) || (JDKUtils.CLASS_SQL_ROW_SET != null && JDKUtils.CLASS_SQL_ROW_SET.isAssignableFrom(type));
    }
    
    public static void setReflectErrorLast(final Throwable error) {
        JDKUtils.reflectErrorCount.incrementAndGet();
        JDKUtils.reflectErrorLast = error;
    }
    
    public static char[] getCharArray(final String str) {
        if (!JDKUtils.FIELD_STRING_VALUE_ERROR) {
            try {
                return (char[])JDKUtils.UNSAFE.getObject(str, JDKUtils.FIELD_STRING_VALUE_OFFSET);
            }
            catch (Exception ignored) {
                JDKUtils.FIELD_STRING_VALUE_ERROR = true;
            }
        }
        return str.toCharArray();
    }
    
    public static MethodHandles.Lookup trustedLookup(final Class objectClass) {
        if (!JDKUtils.CONSTRUCTOR_LOOKUP_ERROR) {
            try {
                final int TRUSTED = -1;
                MethodHandle constructor = JDKUtils.CONSTRUCTOR_LOOKUP;
                if (JDKUtils.JVM_VERSION < 15) {
                    if (constructor == null) {
                        constructor = (JDKUtils.CONSTRUCTOR_LOOKUP = JDKUtils.IMPL_LOOKUP.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(Void.TYPE, Class.class, Integer.TYPE)));
                    }
                    final int FULL_ACCESS_MASK = 31;
                    return constructor.invoke(objectClass, JDKUtils.OPENJ9 ? FULL_ACCESS_MASK : TRUSTED);
                }
                if (constructor == null) {
                    constructor = (JDKUtils.CONSTRUCTOR_LOOKUP = JDKUtils.IMPL_LOOKUP.findConstructor(MethodHandles.Lookup.class, MethodType.methodType(Void.TYPE, Class.class, Class.class, Integer.TYPE)));
                }
                return constructor.invoke(objectClass, (Void)null, TRUSTED);
            }
            catch (Throwable ignored) {
                JDKUtils.CONSTRUCTOR_LOOKUP_ERROR = true;
            }
        }
        return JDKUtils.IMPL_LOOKUP.in(objectClass);
    }
    
    static {
        LATIN1 = 0;
        UTF16 = 1;
        reflectErrorCount = new AtomicInteger();
        Unsafe unsafe = null;
        long offset = -1L;
        long charOffset = -1L;
        try {
            final Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            unsafe = (Unsafe)theUnsafeField.get(null);
            offset = Unsafe.ARRAY_BYTE_BASE_OFFSET;
            charOffset = Unsafe.ARRAY_CHAR_BASE_OFFSET;
        }
        catch (Throwable t) {}
        UNSAFE = unsafe;
        ARRAY_BYTE_BASE_OFFSET = offset;
        ARRAY_CHAR_BASE_OFFSET = charOffset;
        int jvmVersion = -1;
        boolean openj9 = false;
        boolean android = false;
        boolean graal = false;
        try {
            final String jmvName = System.getProperty("java.vm.name");
            openj9 = jmvName.contains("OpenJ9");
            android = jmvName.equals("Dalvik");
            graal = (System.getProperty("org.graalvm.nativeimage.imagecode") != null);
            if (openj9 || android || graal) {
                JDKUtils.FIELD_STRING_VALUE_ERROR = true;
            }
            String javaSpecVer = System.getProperty("java.specification.version");
            if (javaSpecVer.startsWith("1.")) {
                javaSpecVer = javaSpecVer.substring(2);
            }
            if (javaSpecVer.indexOf(46) == -1) {
                jvmVersion = Integer.parseInt(javaSpecVer);
            }
        }
        catch (Throwable e) {
            JDKUtils.initErrorLast = e;
        }
        OPENJ9 = openj9;
        ANDROID = android;
        GRAAL = graal;
        boolean hasJavaSql = true;
        Class dataSourceClass = null;
        Class rowSetClass = null;
        try {
            dataSourceClass = Class.forName("javax.sql.DataSource");
            rowSetClass = Class.forName("javax.sql.RowSet");
        }
        catch (Throwable ignored) {
            hasJavaSql = false;
        }
        CLASS_SQL_DATASOURCE = dataSourceClass;
        CLASS_SQL_ROW_SET = rowSetClass;
        HAS_SQL = hasJavaSql;
        Class transientClass = null;
        if (!android) {
            try {
                transientClass = Class.forName("java.beans.Transient");
            }
            catch (Throwable t2) {}
        }
        CLASS_TRANSIENT = transientClass;
        JVM_VERSION = jvmVersion;
        if (JDKUtils.JVM_VERSION == 8) {
            Field field = null;
            long fieldOffset = -1L;
            try {
                field = String.class.getDeclaredField("value");
                field.setAccessible(true);
                fieldOffset = JDKUtils.UNSAFE.objectFieldOffset(field);
            }
            catch (Exception ignored2) {
                JDKUtils.FIELD_STRING_VALUE_ERROR = true;
            }
            FIELD_STRING_VALUE = field;
            FIELD_STRING_VALUE_OFFSET = fieldOffset;
            FIELD_STRING_CODER = null;
            FIELD_STRING_CODER_OFFSET = -1L;
            JDKUtils.FIELD_STRING_CODER_ERROR = true;
        }
        else {
            Field fieldValue = null;
            long fieldValueOffset = -1L;
            try {
                fieldValue = String.class.getDeclaredField("value");
                fieldValueOffset = JDKUtils.UNSAFE.objectFieldOffset(fieldValue);
            }
            catch (Exception ignored2) {
                JDKUtils.FIELD_STRING_VALUE_ERROR = true;
            }
            FIELD_STRING_VALUE_OFFSET = fieldValueOffset;
            FIELD_STRING_VALUE = fieldValue;
            Field fieldCode = null;
            long fieldCodeOffset = -1L;
            try {
                fieldCode = String.class.getDeclaredField("coder");
                fieldCodeOffset = JDKUtils.UNSAFE.objectFieldOffset(fieldCode);
            }
            catch (Exception ignored3) {
                JDKUtils.FIELD_STRING_CODER_ERROR = true;
            }
            FIELD_STRING_CODER_OFFSET = fieldCodeOffset;
            FIELD_STRING_CODER = fieldCode;
        }
        long fieldOffset2 = -1L;
        try {
            final Field field2 = BigDecimal.class.getDeclaredField("intCompact");
            fieldOffset2 = JDKUtils.UNSAFE.objectFieldOffset(field2);
        }
        catch (Throwable t3) {}
        FIELD_DECIMAL_INT_COMPACT_OFFSET = fieldOffset2;
        BIG_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
        BiFunction<char[], Boolean, String> stringCreatorJDK8 = null;
        BiFunction<byte[], Byte, String> stringCreatorJDK9 = null;
        ToIntFunction<String> stringCoder = null;
        Function<String, byte[]> stringValue = null;
        MethodHandles.Lookup trustedLookup = null;
        try {
            final Class lookupClass = MethodHandles.Lookup.class;
            final Field implLookup = lookupClass.getDeclaredField("IMPL_LOOKUP");
            final long fieldOffset3 = JDKUtils.UNSAFE.staticFieldOffset(implLookup);
            trustedLookup = (MethodHandles.Lookup)JDKUtils.UNSAFE.getObject(lookupClass, fieldOffset3);
        }
        catch (Throwable t4) {}
        if (trustedLookup == null) {
            trustedLookup = MethodHandles.lookup();
        }
        IMPL_LOOKUP = trustedLookup;
        int vector_bit_length = -1;
        boolean vector_support = false;
        try {
            if (JDKUtils.JVM_VERSION >= 11) {
                final Class<?> factorClass = Class.forName("java.lang.management.ManagementFactory");
                final Class<?> runtimeMXBeanClass = Class.forName("java.lang.management.RuntimeMXBean");
                final Method getRuntimeMXBean = factorClass.getMethod("getRuntimeMXBean", (Class<?>[])new Class[0]);
                final Object runtimeMXBean = getRuntimeMXBean.invoke(null, new Object[0]);
                final Method getInputArguments = runtimeMXBeanClass.getMethod("getInputArguments", (Class<?>[])new Class[0]);
                final List<String> inputArguments = (List<String>)getInputArguments.invoke(runtimeMXBean, new Object[0]);
                vector_support = inputArguments.contains("--add-modules=jdk.incubator.vector");
                if (vector_support) {
                    final Class<?> byteVectorClass = Class.forName("jdk.incubator.vector.ByteVector");
                    final Class<?> vectorSpeciesClass = Class.forName("jdk.incubator.vector.VectorSpecies");
                    final Field speciesMax = byteVectorClass.getField("SPECIES_MAX");
                    final Object species = speciesMax.get(null);
                    final Method lengthMethod = vectorSpeciesClass.getMethod("length", (Class<?>[])new Class[0]);
                    final int length = (int)lengthMethod.invoke(species, new Object[0]);
                    vector_bit_length = length * 8;
                }
            }
        }
        catch (Throwable e2) {
            JDKUtils.initErrorLast = e2;
        }
        VECTOR_SUPPORT = vector_support;
        VECTOR_BIT_LENGTH = vector_bit_length;
        Predicate<byte[]> isAscii = null;
        MethodHandle handle = null;
        Class<?> classStringCoding = null;
        if (JDKUtils.JVM_VERSION >= 17) {
            try {
                handle = trustedLookup.findStatic(classStringCoding = String.class, "isASCII", MethodType.methodType(Boolean.TYPE, byte[].class));
            }
            catch (Throwable e3) {
                JDKUtils.initErrorLast = e3;
            }
        }
        if (handle == null && JDKUtils.JVM_VERSION >= 11) {
            try {
                classStringCoding = Class.forName("java.lang.StringCoding");
                handle = trustedLookup.findStatic(classStringCoding, "isASCII", MethodType.methodType(Boolean.TYPE, byte[].class));
            }
            catch (Throwable e3) {
                JDKUtils.initErrorLast = e3;
            }
        }
        if (handle != null) {
            try {
                final MethodHandles.Lookup lookup = trustedLookup(classStringCoding);
                final CallSite callSite = LambdaMetafactory.metafactory(lookup, "test", MethodType.methodType(Predicate.class), MethodType.methodType(Boolean.TYPE, Object.class), handle, MethodType.methodType(Boolean.TYPE, byte[].class));
                isAscii = (Predicate<byte[]>)callSite.getTarget().invokeExact();
            }
            catch (Throwable e3) {
                JDKUtils.initErrorLast = e3;
            }
        }
        PREDICATE_IS_ASCII = isAscii;
        MethodHandle handle2 = null;
        if (JDKUtils.JVM_VERSION >= 11) {
            try {
                final Class<?> classStringCoding2 = Class.forName("java.lang.StringCoding");
                handle2 = trustedLookup.findStatic(classStringCoding2, "hasNegatives", MethodType.methodType(Boolean.TYPE, byte[].class, Integer.TYPE, Integer.TYPE));
            }
            catch (Throwable e4) {
                JDKUtils.initErrorLast = e4;
            }
        }
        METHOD_HANDLE_HAS_NEGATIVE = handle2;
        Boolean compact_strings = null;
        try {
            if (JDKUtils.JVM_VERSION == 8) {
                final MethodHandles.Lookup lookup2 = trustedLookup(String.class);
                final MethodHandle handle3 = lookup2.findConstructor(String.class, MethodType.methodType(Void.TYPE, char[].class, Boolean.TYPE));
                final CallSite callSite2 = LambdaMetafactory.metafactory(lookup2, "apply", MethodType.methodType(BiFunction.class), MethodType.methodType(Object.class, Object.class, Object.class), handle3, MethodType.methodType(String.class, char[].class, Boolean.TYPE));
                stringCreatorJDK8 = (BiFunction<char[], Boolean, String>)callSite2.getTarget().invokeExact();
            }
            boolean lookupLambda = false;
            if (JDKUtils.JVM_VERSION > 8 && !android) {
                try {
                    final Field compact_strings_field = String.class.getDeclaredField("COMPACT_STRINGS");
                    final long fieldOffset4 = JDKUtils.UNSAFE.staticFieldOffset(compact_strings_field);
                    compact_strings = JDKUtils.UNSAFE.getBoolean(String.class, fieldOffset4);
                }
                catch (Throwable e5) {
                    JDKUtils.initErrorLast = e5;
                }
                lookupLambda = (compact_strings != null && compact_strings);
            }
            if (lookupLambda) {
                final MethodHandles.Lookup lookup3 = trustedLookup.in(String.class);
                final MethodHandle handle4 = lookup3.findConstructor(String.class, MethodType.methodType(Void.TYPE, byte[].class, Byte.TYPE));
                final CallSite callSite = LambdaMetafactory.metafactory(lookup3, "apply", MethodType.methodType(BiFunction.class), MethodType.methodType(Object.class, Object.class, Object.class), handle4, MethodType.methodType(String.class, byte[].class, Byte.class));
                stringCreatorJDK9 = (BiFunction<byte[], Byte, String>)callSite.getTarget().invokeExact();
                final MethodHandle coder = lookup3.findSpecial(String.class, "coder", MethodType.methodType(Byte.TYPE), String.class);
                final CallSite applyAsInt = LambdaMetafactory.metafactory(lookup3, "applyAsInt", MethodType.methodType(ToIntFunction.class), MethodType.methodType(Integer.TYPE, Object.class), coder, MethodType.methodType(Byte.TYPE, String.class));
                stringCoder = (ToIntFunction<String>)applyAsInt.getTarget().invokeExact();
                final MethodHandle value = lookup3.findSpecial(String.class, "value", MethodType.methodType(byte[].class), String.class);
                final CallSite apply = LambdaMetafactory.metafactory(lookup3, "apply", MethodType.methodType(Function.class), MethodType.methodType(Object.class, Object.class), value, MethodType.methodType(byte[].class, String.class));
                stringValue = (Function<String, byte[]>)apply.getTarget().invokeExact();
            }
        }
        catch (Throwable e4) {
            JDKUtils.initErrorLast = e4;
        }
        if (stringCoder == null) {
            stringCoder = (str -> 1);
        }
        STRING_CREATOR_JDK8 = stringCreatorJDK8;
        STRING_CREATOR_JDK11 = stringCreatorJDK9;
        STRING_CODER = stringCoder;
        STRING_VALUE = stringValue;
    }
}
