// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.ParameterizedType;
import com.alibaba.fastjson2.util.ParameterizedTypeImpl;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import com.alibaba.fastjson2.function.ToFloatFunction;
import com.alibaba.fastjson2.function.ToByteFunction;
import com.alibaba.fastjson2.function.ToShortFunction;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.text.DecimalFormat;
import java.util.function.ToLongFunction;
import java.lang.reflect.Field;
import java.util.function.ToIntFunction;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Function;

public class ObjectWriters
{
    static final ObjectWriterCreator INSTANCE;
    
    public static ObjectWriter ofReflect(final Class objectType) {
        return ObjectWriterCreator.INSTANCE.createObjectWriter(objectType);
    }
    
    public static ObjectWriter objectWriter(final Class objectType) {
        return ObjectWriters.INSTANCE.createObjectWriter(objectType);
    }
    
    public static ObjectWriter objectWriter(final Class objectType, final FieldWriter... fieldWriters) {
        return ObjectWriters.INSTANCE.createObjectWriter(objectType, fieldWriters);
    }
    
    public static <T> ObjectWriter<T> of(final Class<T> objectType, final FieldWriter... fieldWriters) {
        return (ObjectWriter<T>)ObjectWriters.INSTANCE.createObjectWriter(objectType, fieldWriters);
    }
    
    public static ObjectWriter objectWriter(final Class objectType, final long features, final FieldWriter... fieldWriters) {
        return ObjectWriters.INSTANCE.createObjectWriter(objectType, features, fieldWriters);
    }
    
    public static ObjectWriter objectWriter(final FieldWriter... fieldWriters) {
        return ObjectWriters.INSTANCE.createObjectWriter(fieldWriters);
    }
    
    public static <T> ObjectWriter ofToString(final Function<T, String> function) {
        return ObjectWriters.INSTANCE.createObjectWriter(ObjectWriters.INSTANCE.createFieldWriter(null, null, "toString", 0, 281474976710656L, null, null, String.class, String.class, null, function));
    }
    
    public static <T> ObjectWriter ofToInt(final ToIntFunction function) {
        return ObjectWriters.INSTANCE.createObjectWriter(new FieldWriterInt32ValFunc("toInt", 0, 281474976710656L, null, null, null, null, function));
    }
    
    public static <T> ObjectWriter ofToLong(final ToLongFunction function) {
        return ObjectWriters.INSTANCE.createObjectWriter(new FieldWriterInt64ValFunc("toLong", 0, 281474976710656L, null, null, null, null, function));
    }
    
    public static <T> ObjectWriter ofToByteArray(final Function<Object, byte[]> function) {
        return new ObjectWriterImplInt8ValueArray(function);
    }
    
    public static <T> ObjectWriter ofToShortArray(final Function<Object, short[]> function) {
        return new ObjectWriterImplInt16ValueArray(function);
    }
    
    public static <T> ObjectWriter ofToIntArray(final Function<Object, int[]> function) {
        return new ObjectWriterImplInt32ValueArray(function);
    }
    
    public static <T> ObjectWriter ofToLongArray(final Function<Object, long[]> function) {
        return new ObjectWriterImplInt64ValueArray(function);
    }
    
    public static <T> ObjectWriter ofToCharArray(final Function<Object, char[]> function) {
        return new ObjectWriterImplCharValueArray(function);
    }
    
    public static <T> ObjectWriter ofToFloatArray(final Function<Object, float[]> function) {
        return new ObjectWriterImplFloatValueArray(function, null);
    }
    
    public static <T> ObjectWriter ofToDoubleArray(final Function<Object, double[]> function) {
        return new ObjectWriterImplDoubleValueArray(function, null);
    }
    
    public static <T> ObjectWriter ofToBooleanArray(final Function<Object, boolean[]> function) {
        return new ObjectWriterImplBoolValueArray(function);
    }
    
    public static <T> ObjectWriter ofToBooleanArray(final ToIntFunction functionSize, final BiFunction<Object, Integer, Boolean> functionGet) {
        return new ObjectWriterImplBoolValueArrayLambda(functionSize, functionGet);
    }
    
    public static <T> ObjectWriter ofToBigDecimal(final Function<Object, BigDecimal> function) {
        return new ObjectWriterImplBigDecimal(null, function);
    }
    
    public static <T> ObjectWriter ofToBooleanArray(final ToLongFunction functionSize, final BiFunction<Object, Integer, Boolean> functionGet) {
        final ToIntFunction functionSizeInt = o -> (int)functionSize.applyAsLong(o);
        return new ObjectWriterImplBoolValueArrayLambda(functionSizeInt, functionGet);
    }
    
    public static <T> FieldWriter fieldWriter(final String fieldName, final ToLongFunction<T> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, function);
    }
    
    public static <T> FieldWriter fieldWriter(final String fieldName, final ToIntFunction<T> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, function);
    }
    
    public static <T> FieldWriter fieldWriter(final String fieldName, final ToShortFunction<T> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, function);
    }
    
    public static <T> FieldWriter fieldWriter(final String fieldName, final ToByteFunction<T> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, function);
    }
    
    public static <T> FieldWriter fieldWriter(final String fieldName, final ToFloatFunction<T> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, function);
    }
    
    public static <T> FieldWriter fieldWriter(final String fieldName, final ToDoubleFunction<T> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, function);
    }
    
    public static <T> FieldWriter fieldWriter(final String fieldName, final Predicate<T> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, function);
    }
    
    public static <T> FieldWriter fieldWriter(final String fieldName, final Function<T, String> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, String.class, function);
    }
    
    public static <T, V> FieldWriter fieldWriter(final String fieldName, final Class<V> fieldClass, final Function<T, V> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, fieldClass, function);
    }
    
    public static <T, V> FieldWriter fieldWriter(final String fieldName, final Type fieldType, final Class<V> fieldClass, final Function<T, V> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, fieldType, fieldClass, function);
    }
    
    public static <T, V> FieldWriter fieldWriterList(final String fieldName, final Class<V> itemType, final Function<T, List<V>> function) {
        ParameterizedType listType;
        if (itemType == String.class) {
            listType = TypeUtils.PARAM_TYPE_LIST_STR;
        }
        else {
            listType = new ParameterizedTypeImpl(List.class, new Type[] { itemType });
        }
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, listType, List.class, function);
    }
    
    public static <T> FieldWriter fieldWriterListString(final String fieldName, final Function<T, List<String>> function) {
        return ObjectWriters.INSTANCE.createFieldWriter(fieldName, TypeUtils.PARAM_TYPE_LIST_STR, List.class, function);
    }
    
    static {
        INSTANCE = ObjectWriterCreator.INSTANCE;
    }
}
