// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.BeanUtils;
import java.lang.reflect.Field;
import com.alibaba.fastjson2.TypeReference;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.function.BiConsumer;
import java.util.function.ObjDoubleConsumer;
import com.alibaba.fastjson2.function.ObjFloatConsumer;
import com.alibaba.fastjson2.function.ObjCharConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ObjIntConsumer;
import java.util.Locale;
import com.alibaba.fastjson2.function.ObjShortConsumer;
import com.alibaba.fastjson2.function.ObjByteConsumer;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.function.ObjBoolConsumer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.math.BigDecimal;
import java.util.function.LongFunction;
import java.util.function.IntFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ObjectReaders
{
    public static <T> ObjectReader<T> of(final Supplier<T> defaultCreator, final FieldReader... fieldReaders) {
        return ObjectReaderCreator.INSTANCE.createObjectReader(null, defaultCreator, fieldReaders);
    }
    
    public static <T> ObjectReader<T> of(final Class<T> objectClass, final Supplier<T> defaultCreator, final FieldReader... fieldReaders) {
        return ObjectReaderCreator.INSTANCE.createObjectReader(objectClass, defaultCreator, fieldReaders);
    }
    
    public static <T> ObjectReader<T> ofString(final Function<String, T> function) {
        return new ObjectReaderImplFromString<T>(null, function);
    }
    
    public static <T> ObjectReader<T> ofInt(final IntFunction<T> function) {
        return new ObjectReaderImplFromInt<T>(null, function);
    }
    
    public static <T> ObjectReader<T> ofLong(final LongFunction<T> function) {
        return new ObjectReaderImplFromLong<T>(null, function);
    }
    
    public static <T> ObjectReader<T> fromCharArray(final Function<char[], Object> function) {
        return (ObjectReader<T>)new ObjectReaderImplCharValueArray(function);
    }
    
    public static <T> ObjectReader<T> fromByteArray(final Function<byte[], Object> function) {
        return (ObjectReader<T>)new ObjectReaderImplInt8ValueArray(function, "base64");
    }
    
    public static <T> ObjectReader<T> fromShortArray(final Function<short[], Object> function) {
        return (ObjectReader<T>)new ObjectReaderImplInt16ValueArray(function);
    }
    
    public static <T> ObjectReader<T> fromIntArray(final Function<int[], Object> function) {
        return (ObjectReader<T>)new ObjectReaderImplInt32ValueArray(null, function);
    }
    
    public static <T> ObjectReader<T> fromLongArray(final Function<long[], Object> function) {
        return (ObjectReader<T>)new ObjectReaderImplInt64ValueArray(null, function);
    }
    
    public static <T> ObjectReader<T> fromFloatArray(final Function<float[], Object> function) {
        return (ObjectReader<T>)new ObjectReaderImplFloatValueArray(function);
    }
    
    public static <T> ObjectReader<T> fromDoubleArray(final Function<double[], Object> function) {
        return (ObjectReader<T>)new ObjectReaderImplDoubleValueArray(function);
    }
    
    public static <T> ObjectReader<T> fromBigDecimal(final Function<BigDecimal, Object> function) {
        return (ObjectReader<T>)new ObjectReaderImplBigDecimal(function);
    }
    
    public static <T> ObjectReader<T> objectReader(final Class<T> objectClass, final Supplier<T> defaultCreator, final FieldReader... fieldReaders) {
        return ObjectReaderCreator.INSTANCE.createObjectReader(objectClass, defaultCreator, fieldReaders);
    }
    
    public static <T> ObjectReader<T> ofReflect(final Class<T> objectType) {
        return ObjectReaderCreator.INSTANCE.createObjectReader(objectType);
    }
    
    public static <T> ObjectReader<T> of(final Class<T> objectType) {
        return ObjectReaderCreator.INSTANCE.createObjectReader(objectType);
    }
    
    public static <T> ObjectReader<T> objectReader(final Function<Map<Long, Object>, T> creator, final FieldReader... fieldReaders) {
        return ObjectReaderCreator.INSTANCE.createObjectReaderNoneDefaultConstructor(null, creator, fieldReaders);
    }
    
    public static FieldReader fieldReader(final String fieldName, final Class fieldClass) {
        return ObjectReaderCreator.INSTANCE.createFieldReader((Class<Object>)null, fieldName, fieldClass, fieldClass, null);
    }
    
    public static FieldReader fieldReader(final String fieldName, final Type fieldType, final Class fieldClass) {
        return ObjectReaderCreator.INSTANCE.createFieldReader((Class<Object>)null, fieldName, fieldType, fieldClass, null);
    }
    
    public static <T> FieldReader fieldReaderBool(final String fieldName, final ObjBoolConsumer<T> function) {
        return new FieldReaderBoolValFunc(fieldName, 0, null, null, function);
    }
    
    public static <T> FieldReader fieldReaderByte(final String fieldName, final ObjByteConsumer<T> function) {
        return new FieldReaderInt8ValueFunc(fieldName, 0, null, null, function);
    }
    
    public static <T> FieldReader fieldReaderShort(final String fieldName, final ObjShortConsumer<T> function) {
        return new FieldReaderInt16ValueFunc(fieldName, 0, 0L, null, null, null, null, null, function);
    }
    
    public static <T> FieldReader fieldReaderInt(final String fieldName, final ObjIntConsumer<T> function) {
        return new FieldReaderInt32ValueFunc(fieldName, 0, null, null, null, function);
    }
    
    public static <T> FieldReader fieldReaderLong(final String fieldName, final ObjLongConsumer<T> function) {
        return new FieldReaderInt64ValueFunc(fieldName, 0, null, null, null, function);
    }
    
    public static <T> FieldReader fieldReaderChar(final String fieldName, final ObjCharConsumer<T> function) {
        return new FieldReaderCharValueFunc(fieldName, 0, null, null, null, null, function);
    }
    
    public static <T> FieldReader fieldReaderFloat(final String fieldName, final ObjFloatConsumer<T> function) {
        return new FieldReaderFloatValueFunc(fieldName, 0, null, null, null, function);
    }
    
    public static <T> FieldReader fieldReaderDouble(final String fieldName, final ObjDoubleConsumer<T> function) {
        return new FieldReaderDoubleValueFunc(fieldName, 0, null, null, null, function);
    }
    
    public static <T> FieldReader fieldReaderString(final String fieldName, final BiConsumer<T, String> function) {
        return ObjectReaderCreator.INSTANCE.createFieldReader(fieldName, String.class, String.class, null, function);
    }
    
    public static <T, V> FieldReader fieldReader(final String fieldName, final Class<V> fieldClass, final BiConsumer<T, V> function) {
        return ObjectReaderCreator.INSTANCE.createFieldReader(fieldName, fieldClass, fieldClass, null, function);
    }
    
    public static <T, V> FieldReader fieldReader(final String fieldName, final Type fieldType, final Class<V> fieldClass, final BiConsumer<T, V> function) {
        return ObjectReaderCreator.INSTANCE.createFieldReader(fieldName, fieldType, fieldClass, null, function);
    }
    
    public static <T, V> FieldReader fieldReader(final String fieldName, final Type fieldType, final BiConsumer<T, V> consumer, final ObjectReader<V> fieldObjectReader) {
        return new FieldReaderObjectFunc(fieldName, fieldType, TypeUtils.getClass(fieldType), 0, 0L, null, null, null, null, null, consumer, fieldObjectReader);
    }
    
    public static <T, V> FieldReader fieldReaderList(final String fieldName, final Type itemType, final Supplier<List<V>> listCreator, final BiConsumer<T, List<V>> function, final ObjectReader<V> itemObjectReader) {
        return new FieldReaderListFuncImpl(listCreator, itemObjectReader, function, itemType, fieldName);
    }
    
    public static <T, V> FieldReader fieldReaderList(final String fieldName, final Type itemType, final Supplier<List<V>> listCreator, final BiConsumer<T, List<V>> function) {
        return new FieldReaderListFuncImpl(listCreator, null, function, itemType, fieldName);
    }
    
    public static <T> FieldReader fieldReaderListStr(final String fieldName, final BiConsumer<T, List<String>> function) {
        return new FieldReaderListFuncImpl(ArrayList::new, null, function, String.class, fieldName);
    }
    
    public static <T, V> FieldReader fieldReaderList(final String fieldName, final Type itemType, final BiConsumer<T, List<V>> function) {
        return fieldReaderList(fieldName, itemType, (Supplier<List<V>>)ArrayList::new, function);
    }
    
    public static <T, M extends Map> FieldReader fieldReaderMap(final String fieldName, final Class<M> mapClass, final Type keyType, final Type valueType, final BiConsumer<T, M> function) {
        return new FieldReaderObject(fieldName, TypeReference.parametricType(mapClass, new Type[] { keyType, valueType }), mapClass, 0, 0L, null, null, null, null, null, null, function);
    }
    
    public static FieldReader fieldReaderWithField(final String fieldName, final Class objectClass) {
        final Field field = BeanUtils.getDeclaredField(objectClass, fieldName);
        return ObjectReaderCreator.INSTANCE.createFieldReader(fieldName, field);
    }
    
    public static FieldReader fieldReaderWithField(final String name, final Class objectClass, final String fieldName) {
        final Field field = BeanUtils.getDeclaredField(objectClass, fieldName);
        return ObjectReaderCreator.INSTANCE.createFieldReader(name, field);
    }
    
    public static FieldReader fieldReaderWithMethod(final String name, final Class objectClass, final String methodName) {
        final Method method = BeanUtils.getSetter(objectClass, methodName);
        return ObjectReaderCreator.INSTANCE.createFieldReader(name, method);
    }
}
