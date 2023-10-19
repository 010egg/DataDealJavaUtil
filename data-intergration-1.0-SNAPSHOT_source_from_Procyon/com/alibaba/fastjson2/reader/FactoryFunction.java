// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Parameter;
import com.alibaba.fastjson2.support.LambdaMiscCodec;
import com.alibaba.fastjson2.util.Fnv;
import java.util.function.BiFunction;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

final class FactoryFunction<T> implements Function<Map<Long, Object>, T>
{
    final Method factoryMethod;
    final Function function;
    final BiFunction biFunction;
    final String[] paramNames;
    final long[] hashCodes;
    
    FactoryFunction(final Method factoryMethod, final String... paramNames) {
        this.factoryMethod = factoryMethod;
        final Parameter[] parameters = factoryMethod.getParameters();
        this.paramNames = new String[parameters.length];
        this.hashCodes = new long[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            String name;
            if (i < paramNames.length) {
                name = paramNames[i];
            }
            else {
                name = parameters[i].getName();
            }
            paramNames[i] = name;
            this.hashCodes[i] = Fnv.hashCode64(name);
        }
        Function function = null;
        BiFunction biFunction = null;
        if (ObjectReaderCreator.JIT) {
            final int parameterCount = factoryMethod.getParameterCount();
            if (parameterCount == 1) {
                function = LambdaMiscCodec.createFunction(factoryMethod);
            }
            else if (parameterCount == 2) {
                biFunction = LambdaMiscCodec.createBiFunction(factoryMethod);
            }
        }
        this.function = function;
        this.biFunction = biFunction;
    }
    
    @Override
    public T apply(final Map<Long, Object> values) {
        if (this.function != null) {
            final Object arg = values.get(this.hashCodes[0]);
            return this.function.apply(arg);
        }
        if (this.biFunction != null) {
            final Object arg2 = values.get(this.hashCodes[0]);
            final Object arg3 = values.get(this.hashCodes[1]);
            return this.biFunction.apply(arg2, arg3);
        }
        final Object[] args = new Object[this.hashCodes.length];
        for (int i = 0; i < args.length; ++i) {
            args[i] = values.get(this.hashCodes[i]);
        }
        try {
            return (T)this.factoryMethod.invoke(null, args);
        }
        catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new JSONException("invoke factoryMethod error", e);
        }
    }
}
