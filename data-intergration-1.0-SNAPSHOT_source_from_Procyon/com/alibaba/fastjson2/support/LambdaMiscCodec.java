// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.support;

import java.lang.reflect.InvocationTargetException;
import java.util.function.ObjIntConsumer;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.function.LongFunction;
import java.lang.reflect.Constructor;
import com.alibaba.fastjson2.reader.ObjectReaders;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.util.function.ToLongFunction;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.lang.invoke.LambdaMetafactory;
import java.util.function.BiFunction;
import java.lang.invoke.MethodType;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.JSONException;
import java.util.function.Function;
import com.alibaba.fastjson2.writer.ObjectWriters;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.lang.reflect.Type;

public class LambdaMiscCodec
{
    static volatile boolean hppcError;
    static volatile Throwable errorLast;
    
    public static ObjectWriter getObjectWriter(final Type objectType, final Class objectClass) {
        if (LambdaMiscCodec.hppcError) {
            return null;
        }
        final String name;
        final String className = name = objectClass.getName();
        switch (name) {
            case "gnu.trove.set.hash.TByteHashSet":
            case "gnu.trove.stack.array.TByteArrayStack":
            case "gnu.trove.list.array.TByteArrayList":
            case "com.carrotsearch.hppc.ByteArrayList": {
                try {
                    return ObjectWriters.ofToByteArray(createFunction(objectClass.getMethod("toArray", (Class[])new Class[0])));
                }
                catch (NoSuchMethodException | SecurityException ex9) {
                    final Exception ex;
                    final Exception e = ex;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.set.hash.TShortHashSet":
            case "gnu.trove.list.array.TShortArrayList":
            case "com.carrotsearch.hppc.ShortArrayList": {
                try {
                    return ObjectWriters.ofToShortArray(createFunction(objectClass.getMethod("toArray", (Class[])new Class[0])));
                }
                catch (NoSuchMethodException | SecurityException ex10) {
                    final Exception ex2;
                    final Exception e = ex2;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.list.array.TIntArrayList":
            case "gnu.trove.set.hash.TIntHashSet":
            case "com.carrotsearch.hppc.IntArrayList":
            case "com.carrotsearch.hppc.IntHashSet": {
                try {
                    return ObjectWriters.ofToIntArray(createFunction(objectClass.getMethod("toArray", (Class[])new Class[0])));
                }
                catch (NoSuchMethodException | SecurityException ex11) {
                    final Exception ex3;
                    final Exception e = ex3;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.list.array.TLongArrayList":
            case "gnu.trove.set.hash.TLongHashSet":
            case "com.carrotsearch.hppc.LongArrayList":
            case "com.carrotsearch.hppc.LongHashSet": {
                try {
                    return ObjectWriters.ofToLongArray(createFunction(objectClass.getMethod("toArray", (Class[])new Class[0])));
                }
                catch (NoSuchMethodException | SecurityException ex12) {
                    final Exception ex4;
                    final Exception e = ex4;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.list.array.TCharArrayList":
            case "com.carrotsearch.hppc.CharArrayList":
            case "com.carrotsearch.hppc.CharHashSet": {
                try {
                    return ObjectWriters.ofToCharArray(createFunction(objectClass.getMethod("toArray", (Class[])new Class[0])));
                }
                catch (NoSuchMethodException | SecurityException ex13) {
                    final Exception ex5;
                    final Exception e = ex5;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.list.array.TFloatArrayList":
            case "com.carrotsearch.hppc.FloatArrayList": {
                try {
                    return ObjectWriters.ofToFloatArray(createFunction(objectClass.getMethod("toArray", (Class[])new Class[0])));
                }
                catch (NoSuchMethodException | SecurityException ex14) {
                    final Exception ex6;
                    final Exception e = ex6;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.list.array.TDoubleArrayList":
            case "com.carrotsearch.hppc.DoubleArrayList": {
                try {
                    return ObjectWriters.ofToDoubleArray(createFunction(objectClass.getMethod("toArray", (Class[])new Class[0])));
                }
                catch (NoSuchMethodException | SecurityException ex15) {
                    final Exception ex7;
                    final Exception e = ex7;
                    throw new JSONException("illegal state", e);
                }
            }
            case "com.carrotsearch.hppc.BitSet": {
                final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(objectClass);
                try {
                    final ToLongFunction functionSize = createToLongFunction(objectClass.getMethod("size", (Class[])new Class[0]));
                    final MethodHandle getHandler = lookup.findVirtual(objectClass, "get", MethodType.methodType(Boolean.TYPE, Integer.TYPE));
                    final CallSite getCallSite = LambdaMetafactory.metafactory(lookup, "apply", MethodType.methodType(BiFunction.class), MethodType.methodType(Object.class, Object.class, Object.class), getHandler, MethodType.methodType(Boolean.class, objectClass, Integer.class));
                    final BiFunction<Object, Integer, Boolean> functionGet = (BiFunction<Object, Integer, Boolean>)getCallSite.getTarget().invokeExact();
                    return ObjectWriters.ofToBooleanArray(functionSize, functionGet);
                }
                catch (Throwable ignored) {
                    LambdaMiscCodec.hppcError = true;
                    break;
                }
            }
            case "org.bson.types.Decimal128": {
                try {
                    return ObjectWriters.ofToBigDecimal(createFunction(objectClass.getMethod("bigDecimalValue", (Class[])new Class[0])));
                }
                catch (NoSuchMethodException | SecurityException ex16) {
                    final Exception ex8;
                    final Exception e = ex8;
                    throw new JSONException("illegal state", e);
                }
                break;
            }
        }
        return null;
    }
    
    public static ObjectReader getObjectReader(final Class objectClass) {
        if (LambdaMiscCodec.hppcError) {
            return null;
        }
        final String name;
        final String className = name = objectClass.getName();
        switch (name) {
            case "com.carrotsearch.hppc.ByteArrayList": {
                try {
                    return ObjectReaders.fromByteArray(createFunction(objectClass.getMethod("from", byte[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex16) {
                    final Exception ex;
                    final Exception e = ex;
                    throw new JSONException("illegal state", e);
                }
            }
            case "com.carrotsearch.hppc.ShortArrayList": {
                try {
                    return ObjectReaders.fromShortArray(createFunction(objectClass.getMethod("from", short[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex17) {
                    final Exception ex2;
                    final Exception e = ex2;
                    throw new JSONException("illegal state", e);
                }
            }
            case "com.carrotsearch.hppc.IntArrayList":
            case "com.carrotsearch.hppc.IntHashSet": {
                try {
                    return ObjectReaders.fromIntArray(createFunction(objectClass.getMethod("from", int[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex18) {
                    final Exception ex3;
                    final Exception e = ex3;
                    throw new JSONException("illegal state", e);
                }
            }
            case "com.carrotsearch.hppc.LongArrayList":
            case "com.carrotsearch.hppc.LongHashSet": {
                try {
                    return ObjectReaders.fromLongArray(createFunction(objectClass.getMethod("from", long[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex19) {
                    final Exception ex4;
                    final Exception e = ex4;
                    throw new JSONException("illegal state", e);
                }
            }
            case "com.carrotsearch.hppc.CharArrayList":
            case "com.carrotsearch.hppc.CharHashSet": {
                try {
                    return ObjectReaders.fromCharArray(createFunction(objectClass.getMethod("from", char[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex20) {
                    final Exception ex5;
                    final Exception e = ex5;
                    throw new JSONException("illegal state", e);
                }
            }
            case "com.carrotsearch.hppc.FloatArrayList": {
                try {
                    return ObjectReaders.fromFloatArray(createFunction(objectClass.getMethod("from", float[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex21) {
                    final Exception ex6;
                    final Exception e = ex6;
                    throw new JSONException("illegal state", e);
                }
            }
            case "com.carrotsearch.hppc.DoubleArrayList": {
                try {
                    return ObjectReaders.fromDoubleArray(createFunction(objectClass.getMethod("from", double[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex22) {
                    final Exception ex7;
                    final Exception e = ex7;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.set.hash.TByteHashSet":
            case "gnu.trove.stack.array.TByteArrayStack":
            case "gnu.trove.list.array.TByteArrayList": {
                try {
                    return ObjectReaders.fromByteArray(createFunction(objectClass.getConstructor(byte[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex23) {
                    final Exception ex8;
                    final Exception e = ex8;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.list.array.TCharArrayList": {
                try {
                    return ObjectReaders.fromCharArray(createFunction(objectClass.getConstructor(char[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex24) {
                    final Exception ex9;
                    final Exception e = ex9;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.set.hash.TShortHashSet":
            case "gnu.trove.list.array.TShortArrayList": {
                try {
                    return ObjectReaders.fromShortArray(createFunction(objectClass.getConstructor(short[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex25) {
                    final Exception ex10;
                    final Exception e = ex10;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.set.hash.TIntHashSet":
            case "gnu.trove.list.array.TIntArrayList": {
                try {
                    return ObjectReaders.fromIntArray(createFunction(objectClass.getConstructor(int[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex26) {
                    final Exception ex11;
                    final Exception e = ex11;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.set.hash.TLongHashSet":
            case "gnu.trove.list.array.TLongArrayList": {
                try {
                    return ObjectReaders.fromLongArray(createFunction(objectClass.getConstructor(long[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex27) {
                    final Exception ex12;
                    final Exception e = ex12;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.list.array.TFloatArrayList": {
                try {
                    return ObjectReaders.fromFloatArray(createFunction(objectClass.getConstructor(float[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex28) {
                    final Exception ex13;
                    final Exception e = ex13;
                    throw new JSONException("illegal state", e);
                }
            }
            case "gnu.trove.list.array.TDoubleArrayList": {
                try {
                    return ObjectReaders.fromDoubleArray(createFunction(objectClass.getConstructor(double[].class)));
                }
                catch (NoSuchMethodException | SecurityException ex29) {
                    final Exception ex14;
                    final Exception e = ex14;
                    throw new JSONException("illegal state", e);
                }
            }
            case "org.bson.types.Decimal128": {
                try {
                    return ObjectReaders.fromBigDecimal(createFunction(objectClass.getConstructor(BigDecimal.class)));
                }
                catch (NoSuchMethodException | SecurityException ex30) {
                    final Exception ex15;
                    final Exception e = ex15;
                    throw new JSONException("illegal state", e);
                }
                break;
            }
        }
        return null;
    }
    
    public static LongFunction createLongFunction(final Constructor constructor) {
        try {
            final Class objectClass = constructor.getDeclaringClass();
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(objectClass);
            final MethodHandle methodHandle = lookup.findConstructor(objectClass, TypeUtils.METHOD_TYPE_VOID_LONG);
            final MethodType invokedType = MethodType.methodType(objectClass, Long.TYPE);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_LONG_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_LONG, methodHandle, invokedType);
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ReflectLongFunction(constructor);
        }
    }
    
    public static ToIntFunction createToIntFunction(final Method method) {
        final Class<?> objectClass = method.getDeclaringClass();
        try {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(objectClass);
            final MethodType methodType = MethodType.methodType(Integer.TYPE);
            final MethodHandle methodHandle = lookup.findVirtual(objectClass, method.getName(), methodType);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "applyAsInt", TypeUtils.METHOD_TYPE_TO_INT_FUNCTION, TypeUtils.METHOD_TYPE_INT_OBJECT, methodHandle, MethodType.methodType(Integer.TYPE, objectClass));
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ReflectToIntFunction(method);
        }
    }
    
    public static ToLongFunction createToLongFunction(final Method method) {
        final Class<?> objectClass = method.getDeclaringClass();
        try {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(objectClass);
            final MethodType methodType = MethodType.methodType(Long.TYPE);
            final MethodHandle methodHandle = lookup.findVirtual(objectClass, method.getName(), methodType);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "applyAsLong", TypeUtils.METHOD_TYPE_TO_LONG_FUNCTION, TypeUtils.METHOD_TYPE_LONG_OBJECT, methodHandle, MethodType.methodType(Long.TYPE, objectClass));
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ReflectToLongFunction(method);
        }
    }
    
    public static Function createFunction(final Constructor constructor) {
        try {
            final Class<?> declaringClass = constructor.getDeclaringClass();
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
            final Class<?>[] parameterTypes = (Class<?>[])constructor.getParameterTypes();
            final Class<?> param0 = parameterTypes[0];
            final MethodHandle methodHandle = lookup.findConstructor(declaringClass, MethodType.methodType(Void.TYPE, param0));
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, methodHandle, MethodType.methodType(declaringClass, param0));
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ConstructorFunction(constructor);
        }
    }
    
    public static Supplier createSupplier(final Constructor constructor) {
        try {
            final Class<?> declaringClass = constructor.getDeclaringClass();
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
            final MethodHandle methodHandle = lookup.findConstructor(declaringClass, MethodType.methodType(Void.TYPE));
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "get", TypeUtils.METHOD_TYPE_SUPPLIER, TypeUtils.METHOD_TYPE_OBJECT, methodHandle, MethodType.methodType(declaringClass));
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ConstructorSupplier(constructor);
        }
    }
    
    public static Supplier createSupplier(final Method method) {
        try {
            final Class<?> declaringClass = method.getDeclaringClass();
            final Class objectClass = method.getReturnType();
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
            final MethodHandle methodHandle = lookup.findStatic(declaringClass, method.getName(), MethodType.methodType(objectClass));
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "get", TypeUtils.METHOD_TYPE_SUPPLIER, TypeUtils.METHOD_TYPE_OBJECT, methodHandle, MethodType.methodType(objectClass));
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ReflectSupplier(method);
        }
    }
    
    public static BiFunction createBiFunction(final Method method) {
        try {
            final Class<?> declaringClass = method.getDeclaringClass();
            final Class objectClass = method.getReturnType();
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Class<?> param0 = parameterTypes[0];
            MethodHandle methodHandle;
            MethodType methodType;
            if (Modifier.isStatic(method.getModifiers())) {
                final Class<?> param2 = parameterTypes[1];
                methodHandle = lookup.findStatic(declaringClass, method.getName(), MethodType.methodType(objectClass, param0, param2));
                methodType = MethodType.methodType(objectClass, param0, param2);
            }
            else {
                methodHandle = lookup.findVirtual(declaringClass, method.getName(), MethodType.methodType(objectClass, param0));
                methodType = MethodType.methodType(objectClass, declaringClass, param0);
            }
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_BI_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT_OBJECT, methodHandle, methodType);
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ReflectBiFunction(method);
        }
    }
    
    public static BiFunction createBiFunction(final Constructor constructor) {
        try {
            final Class<?> declaringClass = constructor.getDeclaringClass();
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
            final Class<?>[] parameterTypes = (Class<?>[])constructor.getParameterTypes();
            final Class<?> param0 = parameterTypes[0];
            final Class<?> param2 = parameterTypes[1];
            final MethodHandle methodHandle = lookup.findConstructor(declaringClass, MethodType.methodType(Void.TYPE, param0, param2));
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_BI_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT_OBJECT, methodHandle, MethodType.methodType(declaringClass, param0, param2));
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ConstructorBiFunction(constructor);
        }
    }
    
    public static Function createFunction(final Method method) {
        final Class<?> declaringClass = method.getDeclaringClass();
        final int modifiers = method.getModifiers();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final boolean isStatic = Modifier.isStatic(modifiers);
        final Class objectClass = method.getReturnType();
        Class paramClass;
        if (parameterTypes.length == 1 && isStatic) {
            paramClass = parameterTypes[0];
        }
        else {
            if (parameterTypes.length != 0 || isStatic) {
                throw new JSONException("not support parameters " + method);
            }
            paramClass = declaringClass;
        }
        try {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
            MethodHandle methodHandle;
            if (isStatic) {
                methodHandle = lookup.findStatic(declaringClass, method.getName(), MethodType.methodType(objectClass, paramClass));
            }
            else {
                methodHandle = lookup.findVirtual(declaringClass, method.getName(), MethodType.methodType(objectClass));
            }
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, methodHandle, MethodType.methodType(objectClass, paramClass));
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            if (!Modifier.isStatic(method.getModifiers())) {
                return new GetterFunction(method);
            }
            return new FactoryFunction(method);
        }
    }
    
    public static ObjIntConsumer createObjIntConsumer(final Method method) {
        final Class<?> declaringClass = method.getDeclaringClass();
        try {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(declaringClass);
            final MethodType methodType = MethodType.methodType(Void.TYPE, Integer.TYPE);
            final MethodHandle methodHandle = lookup.findVirtual(declaringClass, method.getName(), methodType);
            final CallSite callSite = LambdaMetafactory.metafactory(lookup, "accept", TypeUtils.METHOD_TYPE_OBJECT_INT_CONSUMER, TypeUtils.METHOD_TYPE_VOID_OBJECT_INT, methodHandle, MethodType.methodType(Void.TYPE, declaringClass, Integer.TYPE));
            return callSite.getTarget().invokeExact();
        }
        catch (Throwable ignored) {
            LambdaMiscCodec.errorLast = ignored;
            return new ReflectObjIntConsumer(method);
        }
    }
    
    static final class ConstructorSupplier implements Supplier
    {
        final Constructor constructor;
        
        ConstructorSupplier(final Constructor constructor) {
            this.constructor = constructor;
        }
        
        @Override
        public Object get() {
            try {
                return this.constructor.newInstance(new Object[0]);
            }
            catch (IllegalAccessException | InvocationTargetException | InstantiationException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("invoke error", e);
            }
        }
    }
    
    static final class ConstructorFunction implements Function
    {
        final Constructor constructor;
        
        ConstructorFunction(final Constructor constructor) {
            this.constructor = constructor;
        }
        
        @Override
        public Object apply(final Object arg0) {
            try {
                return this.constructor.newInstance(arg0);
            }
            catch (IllegalAccessException | InvocationTargetException | InstantiationException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("invoke error", e);
            }
        }
    }
    
    static final class ConstructorBiFunction implements BiFunction
    {
        final Constructor constructor;
        
        ConstructorBiFunction(final Constructor constructor) {
            this.constructor = constructor;
        }
        
        @Override
        public Object apply(final Object arg0, final Object arg1) {
            try {
                return this.constructor.newInstance(arg0, arg1);
            }
            catch (IllegalAccessException | InvocationTargetException | InstantiationException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("invoke error", e);
            }
        }
    }
    
    static final class ReflectBiFunction implements BiFunction
    {
        final Method method;
        
        ReflectBiFunction(final Method method) {
            this.method = method;
        }
        
        @Override
        public Object apply(final Object arg0, final Object arg1) {
            try {
                if (Modifier.isStatic(this.method.getModifiers())) {
                    return this.method.invoke(null, arg0, arg1);
                }
                return this.method.invoke(arg0, arg1);
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("invoke error", e);
            }
        }
    }
    
    static final class ReflectSupplier implements Supplier
    {
        final Method method;
        
        ReflectSupplier(final Method method) {
            this.method = method;
        }
        
        @Override
        public Object get() {
            try {
                return this.method.invoke(null, new Object[0]);
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("invoke error", e);
            }
        }
    }
    
    static final class ReflectObjIntConsumer implements ObjIntConsumer
    {
        final Method method;
        
        public ReflectObjIntConsumer(final Method method) {
            this.method = method;
        }
        
        @Override
        public void accept(final Object object, final int value) {
            try {
                this.method.invoke(object, value);
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("invoke error", e);
            }
        }
    }
    
    static final class FactoryFunction implements Function
    {
        final Method method;
        
        FactoryFunction(final Method method) {
            this.method = method;
        }
        
        @Override
        public Object apply(final Object arg) {
            try {
                return this.method.invoke(null, arg);
            }
            catch (Exception e) {
                throw new JSONException("createInstance error", e);
            }
        }
    }
    
    static final class GetterFunction implements Function
    {
        final Method method;
        
        GetterFunction(final Method method) {
            this.method = method;
        }
        
        @Override
        public Object apply(final Object arg) {
            try {
                return this.method.invoke(arg, new Object[0]);
            }
            catch (Exception e) {
                throw new JSONException("createInstance error", e);
            }
        }
    }
    
    static final class ReflectLongFunction implements LongFunction
    {
        final Constructor constructor;
        
        public ReflectLongFunction(final Constructor constructor) {
            this.constructor = constructor;
        }
        
        @Override
        public Object apply(final long value) {
            try {
                return this.constructor.newInstance(value);
            }
            catch (Exception e) {
                throw new JSONException("createInstance error", e);
            }
        }
    }
    
    static final class ReflectToIntFunction implements ToIntFunction
    {
        final Method method;
        
        public ReflectToIntFunction(final Method method) {
            this.method = method;
        }
        
        @Override
        public int applyAsInt(final Object object) {
            try {
                return (int)this.method.invoke(object, new Object[0]);
            }
            catch (Exception e) {
                throw new JSONException("applyAsInt error", e);
            }
        }
    }
    
    static final class ReflectToLongFunction implements ToLongFunction
    {
        final Method method;
        
        public ReflectToLongFunction(final Method method) {
            this.method = method;
        }
        
        @Override
        public long applyAsLong(final Object object) {
            try {
                return (long)this.method.invoke(object, new Object[0]);
            }
            catch (Exception e) {
                throw new JSONException("applyAsLong error", e);
            }
        }
    }
}
