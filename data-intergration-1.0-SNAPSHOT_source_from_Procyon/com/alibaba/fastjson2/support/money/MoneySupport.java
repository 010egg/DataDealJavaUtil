// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.support.money;

import com.alibaba.fastjson2.writer.ObjectWriters;
import com.alibaba.fastjson2.writer.ObjectWriterAdapter;
import java.util.Arrays;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriterCreator;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import com.alibaba.fastjson2.writer.ObjectWriter;
import java.util.Map;
import java.util.List;
import com.alibaba.fastjson2.reader.ObjectReaderNoneDefaultConstructor;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.reader.ObjectReaderCreator;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import com.alibaba.fastjson2.reader.ObjectReaderImplValue;
import com.alibaba.fastjson2.JSONException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodType;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.function.Function;

public class MoneySupport
{
    static Class CLASS_MONETARY;
    static Class CLASS_MONETARY_AMOUNT;
    static Class CLASS_MONETARY_AMOUNT_FACTORY;
    static Class CLASS_DEFAULT_NUMBER_VALUE;
    static Class CLASS_NUMBER_VALUE;
    static Class CLASS_CURRENCY_UNIT;
    static Function<Object, Object> FUNC_CREATE;
    static Supplier<Object> FUNC_GET_DEFAULT_AMOUNT_FACTORY;
    static BiFunction<Object, Object, Object> FUNC_SET_CURRENCY;
    static BiFunction<Object, Object, Number> FUNC_SET_NUMBER;
    static Function<String, Object> FUNC_GET_CURRENCY;
    static Function<Object, BigDecimal> FUNC_NUMBER_VALUE;
    static Method METHOD_NUMBER_VALUE_OF;
    
    public static ObjectReader createCurrencyUnitReader() {
        if (MoneySupport.CLASS_MONETARY == null) {
            MoneySupport.CLASS_MONETARY = TypeUtils.loadClass("javax.money.Monetary");
        }
        if (MoneySupport.CLASS_CURRENCY_UNIT == null) {
            MoneySupport.CLASS_CURRENCY_UNIT = TypeUtils.loadClass("javax.money.CurrencyUnit");
        }
        if (MoneySupport.FUNC_GET_CURRENCY == null) {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(MoneySupport.CLASS_MONETARY);
            try {
                final MethodHandle methodHandle = lookup.findStatic(MoneySupport.CLASS_MONETARY, "getCurrency", MethodType.methodType(MoneySupport.CLASS_CURRENCY_UNIT, String.class, String[].class));
                final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_BI_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT_OBJECT, methodHandle, MethodType.methodType(MoneySupport.CLASS_CURRENCY_UNIT, String.class, String[].class));
                final MethodHandle target = callSite.getTarget();
                final BiFunction<String, String[], Object> biFunctionGetCurrency = (BiFunction<String, String[], Object>)target.invokeExact();
                MoneySupport.FUNC_GET_CURRENCY = (Function<String, Object>)(s -> biFunctionGetCurrency.apply(s, new String[0]));
            }
            catch (Throwable e) {
                throw new JSONException("method not found : javax.money.Monetary.getCurrency", e);
            }
        }
        return ObjectReaderImplValue.of(MoneySupport.CLASS_CURRENCY_UNIT, String.class, MoneySupport.FUNC_GET_CURRENCY);
    }
    
    public static ObjectReader createMonetaryAmountReader() {
        if (MoneySupport.CLASS_NUMBER_VALUE == null) {
            MoneySupport.CLASS_NUMBER_VALUE = TypeUtils.loadClass("javax.money.NumberValue");
        }
        if (MoneySupport.CLASS_CURRENCY_UNIT == null) {
            MoneySupport.CLASS_CURRENCY_UNIT = TypeUtils.loadClass("javax.money.CurrencyUnit");
        }
        try {
            final Method factoryMethod = MoneySupport.class.getMethod("createMonetaryAmount", Object.class, Object.class);
            final String[] paramNames = { "currency", "number" };
            final Function<Map<Long, Object>, Object> factoryFunction = ObjectReaderCreator.INSTANCE.createFactoryFunction(factoryMethod, paramNames);
            final FieldReader fieldReader0 = ObjectReaderCreator.INSTANCE.createFieldReaderParam(MoneySupport.class, MoneySupport.class, "currency", 0, 0L, null, MoneySupport.CLASS_CURRENCY_UNIT, MoneySupport.CLASS_CURRENCY_UNIT, "currency", null, null, null);
            final FieldReader fieldReader2 = ObjectReaderCreator.INSTANCE.createFieldReaderParam(MoneySupport.class, MoneySupport.class, "number", 0, 0L, null, MoneySupport.CLASS_DEFAULT_NUMBER_VALUE, MoneySupport.CLASS_DEFAULT_NUMBER_VALUE, "number", null, null, null);
            final FieldReader[] fieldReaders = { fieldReader0, fieldReader2 };
            return new ObjectReaderNoneDefaultConstructor(null, null, null, 0L, factoryFunction, null, paramNames, fieldReaders, null, null, null);
        }
        catch (NoSuchMethodException e) {
            throw new JSONException("createMonetaryAmountReader error", e);
        }
    }
    
    public static ObjectReader createNumberValueReader() {
        if (MoneySupport.CLASS_DEFAULT_NUMBER_VALUE == null) {
            MoneySupport.CLASS_DEFAULT_NUMBER_VALUE = TypeUtils.loadClass("org.javamoney.moneta.spi.DefaultNumberValue");
        }
        if (MoneySupport.METHOD_NUMBER_VALUE_OF == null) {
            try {
                MoneySupport.METHOD_NUMBER_VALUE_OF = MoneySupport.CLASS_DEFAULT_NUMBER_VALUE.getMethod("of", Number.class);
            }
            catch (NoSuchMethodException e) {
                throw new JSONException("method not found : org.javamoney.moneta.spi.DefaultNumberValue.of", e);
            }
        }
        if (MoneySupport.CLASS_NUMBER_VALUE == null) {
            MoneySupport.CLASS_NUMBER_VALUE = TypeUtils.loadClass("javax.money.NumberValue");
        }
        return ObjectReaderImplValue.of((Class<Object>)MoneySupport.CLASS_NUMBER_VALUE, BigDecimal.class, MoneySupport.METHOD_NUMBER_VALUE_OF);
    }
    
    public static ObjectWriter createMonetaryAmountWriter() {
        if (MoneySupport.CLASS_MONETARY == null) {
            MoneySupport.CLASS_MONETARY = TypeUtils.loadClass("javax.money.Monetary");
        }
        if (MoneySupport.CLASS_MONETARY_AMOUNT == null) {
            MoneySupport.CLASS_MONETARY_AMOUNT = TypeUtils.loadClass("javax.money.MonetaryAmount");
        }
        if (MoneySupport.CLASS_NUMBER_VALUE == null) {
            MoneySupport.CLASS_NUMBER_VALUE = TypeUtils.loadClass("javax.money.NumberValue");
        }
        if (MoneySupport.CLASS_CURRENCY_UNIT == null) {
            MoneySupport.CLASS_CURRENCY_UNIT = TypeUtils.loadClass("javax.money.CurrencyUnit");
        }
        Function<Object, Object> FUNC_GET_CURRENCY;
        try {
            FUNC_GET_CURRENCY = (Function<Object, Object>)LambdaMiscCodec.createFunction(MoneySupport.CLASS_MONETARY_AMOUNT.getMethod("getCurrency", (Class[])new Class[0]));
        }
        catch (Throwable e) {
            throw new JSONException("method not found : javax.money.Monetary.getCurrency", e);
        }
        Function<Object, Object> FUNC_GET_NUMBER;
        try {
            FUNC_GET_NUMBER = (Function<Object, Object>)LambdaMiscCodec.createFunction(MoneySupport.CLASS_MONETARY_AMOUNT.getMethod("getNumber", (Class[])new Class[0]));
        }
        catch (Throwable e2) {
            throw new JSONException("method not found : javax.money.Monetary.getNumber", e2);
        }
        final FieldWriter fieldWriter0 = ObjectWriterCreator.INSTANCE.createFieldWriter("currency", MoneySupport.CLASS_CURRENCY_UNIT, MoneySupport.CLASS_CURRENCY_UNIT, FUNC_GET_CURRENCY);
        final FieldWriter fieldWriter2 = ObjectWriterCreator.INSTANCE.createFieldWriter("number", MoneySupport.CLASS_NUMBER_VALUE, MoneySupport.CLASS_NUMBER_VALUE, FUNC_GET_NUMBER);
        return new ObjectWriterAdapter(MoneySupport.CLASS_MONETARY_AMOUNT, null, null, 0L, Arrays.asList(fieldWriter0, fieldWriter2));
    }
    
    public static ObjectWriter createNumberValueWriter() {
        if (MoneySupport.CLASS_NUMBER_VALUE == null) {
            MoneySupport.CLASS_NUMBER_VALUE = TypeUtils.loadClass("javax.money.NumberValue");
        }
        if (MoneySupport.FUNC_NUMBER_VALUE == null) {
            try {
                final BiFunction<Object, Class, Number> biFunctionNumberValue = (BiFunction<Object, Class, Number>)LambdaMiscCodec.createBiFunction(MoneySupport.CLASS_NUMBER_VALUE.getMethod("numberValue", Class.class));
                MoneySupport.FUNC_NUMBER_VALUE = (o -> (BigDecimal)biFunctionNumberValue.apply(o, BigDecimal.class));
            }
            catch (Throwable e) {
                throw new JSONException("method not found : javax.money.NumberValue.numberValue", e);
            }
        }
        return ObjectWriters.ofToBigDecimal(MoneySupport.FUNC_NUMBER_VALUE);
    }
    
    public static Object createMonetaryAmount(final Object currency, final Object number) {
        if (MoneySupport.CLASS_NUMBER_VALUE == null) {
            MoneySupport.CLASS_NUMBER_VALUE = TypeUtils.loadClass("javax.money.NumberValue");
        }
        if (MoneySupport.CLASS_CURRENCY_UNIT == null) {
            MoneySupport.CLASS_CURRENCY_UNIT = TypeUtils.loadClass("javax.money.CurrencyUnit");
        }
        if (MoneySupport.CLASS_MONETARY == null) {
            MoneySupport.CLASS_MONETARY = TypeUtils.loadClass("javax.money.Monetary");
        }
        if (MoneySupport.CLASS_MONETARY_AMOUNT == null) {
            MoneySupport.CLASS_MONETARY_AMOUNT = TypeUtils.loadClass("javax.money.MonetaryAmount");
        }
        if (MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY == null) {
            MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY = TypeUtils.loadClass("javax.money.MonetaryAmountFactory");
        }
        if (MoneySupport.FUNC_GET_DEFAULT_AMOUNT_FACTORY == null) {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(MoneySupport.CLASS_MONETARY);
            try {
                final MethodHandle methodHandle = lookup.findStatic(MoneySupport.CLASS_MONETARY, "getDefaultAmountFactory", MethodType.methodType(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY));
                final CallSite callSite = LambdaMetafactory.metafactory(lookup, "get", TypeUtils.METHOD_TYPE_SUPPLIER, TypeUtils.METHOD_TYPE_OBJECT, methodHandle, MethodType.methodType(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY));
                final MethodHandle target = callSite.getTarget();
                MoneySupport.FUNC_GET_DEFAULT_AMOUNT_FACTORY = (Supplier<Object>)target.invokeExact();
            }
            catch (Throwable e) {
                throw new JSONException("method not found : javax.money.Monetary.getDefaultAmountFactory", e);
            }
        }
        if (MoneySupport.FUNC_SET_CURRENCY == null) {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY);
            try {
                final MethodHandle methodHandle = lookup.findVirtual(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, "setCurrency", MethodType.methodType(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, MoneySupport.CLASS_CURRENCY_UNIT));
                final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_BI_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT_OBJECT, methodHandle, MethodType.methodType(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, MoneySupport.CLASS_CURRENCY_UNIT));
                final MethodHandle target = callSite.getTarget();
                MoneySupport.FUNC_SET_CURRENCY = (BiFunction<Object, Object, Object>)target.invokeExact();
            }
            catch (Throwable e) {
                throw new JSONException("method not found : javax.money.NumberValue.numberValue", e);
            }
        }
        if (MoneySupport.FUNC_SET_NUMBER == null) {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY);
            try {
                final MethodHandle methodHandle = lookup.findVirtual(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, "setNumber", MethodType.methodType(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, Number.class));
                final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_BI_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT_OBJECT, methodHandle, MethodType.methodType(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, Number.class));
                final MethodHandle target = callSite.getTarget();
                MoneySupport.FUNC_SET_NUMBER = (BiFunction<Object, Object, Number>)target.invokeExact();
            }
            catch (Throwable e) {
                throw new JSONException("method not found : javax.money.NumberValue.numberValue", e);
            }
        }
        if (MoneySupport.FUNC_CREATE == null) {
            final MethodHandles.Lookup lookup = JDKUtils.trustedLookup(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY);
            try {
                final MethodHandle methodHandle = lookup.findVirtual(MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY, "create", MethodType.methodType(MoneySupport.CLASS_MONETARY_AMOUNT));
                final CallSite callSite = LambdaMetafactory.metafactory(lookup, "apply", TypeUtils.METHOD_TYPE_FUNCTION, TypeUtils.METHOD_TYPE_OBJECT_OBJECT, methodHandle, MethodType.methodType(MoneySupport.CLASS_MONETARY_AMOUNT, MoneySupport.CLASS_MONETARY_AMOUNT_FACTORY));
                final MethodHandle target = callSite.getTarget();
                MoneySupport.FUNC_CREATE = (Function<Object, Object>)target.invokeExact();
            }
            catch (Throwable e) {
                throw new JSONException("method not found : javax.money.NumberValue.numberValue", e);
            }
        }
        final Object factoryObject = MoneySupport.FUNC_GET_DEFAULT_AMOUNT_FACTORY.get();
        if (currency != null) {
            MoneySupport.FUNC_SET_CURRENCY.apply(factoryObject, currency);
        }
        if (number != null) {
            MoneySupport.FUNC_SET_NUMBER.apply(factoryObject, number);
        }
        return MoneySupport.FUNC_CREATE.apply(factoryObject);
    }
}
