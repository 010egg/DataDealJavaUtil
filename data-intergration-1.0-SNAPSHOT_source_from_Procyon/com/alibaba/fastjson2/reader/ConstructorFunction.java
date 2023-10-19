// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.HashSet;
import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.codec.FieldInfo;
import java.lang.reflect.AccessibleObject;
import com.alibaba.fastjson2.internal.asm.ASMUtils;
import java.util.HashMap;
import com.alibaba.fastjson2.util.Fnv;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.List;
import java.lang.reflect.Parameter;
import java.util.function.BiFunction;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.function.Function;

final class ConstructorFunction<T> implements Function<Map<Long, Object>, T>
{
    final Constructor constructor;
    final Function function;
    final BiFunction biFunction;
    final Parameter[] parameters;
    final String[] paramNames;
    final boolean marker;
    final long[] hashCodes;
    final List<Constructor> alternateConstructors;
    Map<Set<Long>, Constructor> alternateConstructorMap;
    Map<Set<Long>, String[]> alternateConstructorNames;
    Map<Set<Long>, long[]> alternateConstructorNameHashCodes;
    Map<Set<Long>, Type[]> alternateConstructorArgTypes;
    
    ConstructorFunction(final List<Constructor> alternateConstructors, final Constructor constructor, final Function function, final BiFunction biFunction, final Constructor markerConstructor, final String... paramNames) {
        this.function = function;
        this.biFunction = biFunction;
        this.marker = (markerConstructor != null);
        this.constructor = (this.marker ? markerConstructor : constructor);
        this.parameters = constructor.getParameters();
        this.paramNames = paramNames;
        this.hashCodes = new long[this.parameters.length];
        for (int i = 0; i < this.parameters.length; ++i) {
            String name;
            if (i < paramNames.length) {
                name = paramNames[i];
            }
            else {
                name = this.parameters[i].getName();
            }
            if (name == null) {
                name = "arg" + i;
            }
            this.hashCodes[i] = Fnv.hashCode64(name);
        }
        if ((this.alternateConstructors = alternateConstructors) != null) {
            this.alternateConstructorMap = new HashMap<Set<Long>, Constructor>(alternateConstructors.size());
            this.alternateConstructorNames = new HashMap<Set<Long>, String[]>(alternateConstructors.size());
            this.alternateConstructorArgTypes = new HashMap<Set<Long>, Type[]>(alternateConstructors.size());
            this.alternateConstructorNameHashCodes = new HashMap<Set<Long>, long[]>(alternateConstructors.size());
            for (int i = 0; i < alternateConstructors.size(); ++i) {
                final Constructor alternateConstructor = alternateConstructors.get(i);
                alternateConstructor.setAccessible(true);
                final String[] parameterNames = ASMUtils.lookupParameterNames(alternateConstructor);
                final Parameter[] parameters = alternateConstructor.getParameters();
                final FieldInfo fieldInfo = new FieldInfo();
                final ObjectReaderProvider provider = JSONFactory.getDefaultObjectReaderProvider();
                for (int j = 0; j < parameters.length && j < parameterNames.length; ++j) {
                    fieldInfo.init();
                    final Parameter parameter = parameters[j];
                    provider.getFieldInfo(fieldInfo, alternateConstructor.getDeclaringClass(), alternateConstructor, j, parameter);
                    if (fieldInfo.fieldName != null) {
                        parameterNames[j] = fieldInfo.fieldName;
                    }
                }
                final long[] parameterNameHashCodes = new long[parameterNames.length];
                final Type[] parameterTypes = alternateConstructor.getGenericParameterTypes();
                final Set<Long> paramHashCodes = new HashSet<Long>(parameterNames.length);
                for (int k = 0; k < parameterNames.length; ++k) {
                    final long hashCode64 = Fnv.hashCode64(parameterNames[k]);
                    parameterNameHashCodes[k] = hashCode64;
                    paramHashCodes.add(hashCode64);
                }
                this.alternateConstructorMap.put(paramHashCodes, alternateConstructor);
                this.alternateConstructorNames.put(paramHashCodes, parameterNames);
                this.alternateConstructorNameHashCodes.put(paramHashCodes, parameterNameHashCodes);
                this.alternateConstructorArgTypes.put(paramHashCodes, parameterTypes);
            }
        }
    }
    
    @Override
    public T apply(final Map<Long, Object> values) {
        boolean containsAll = true;
        for (final long hashCode : this.hashCodes) {
            if (!values.containsKey(hashCode)) {
                containsAll = false;
                break;
            }
        }
        if (!containsAll && this.alternateConstructorMap != null) {
            final Set<Long> key = values.keySet();
            final Constructor constructor = this.alternateConstructorMap.get(key);
            if (constructor != null) {
                final long[] hashCodes = this.alternateConstructorNameHashCodes.get(key);
                final Type[] paramTypes = this.alternateConstructorArgTypes.get(key);
                final Object[] args = new Object[hashCodes.length];
                for (int i = 0; i < hashCodes.length; ++i) {
                    Object arg = values.get(hashCodes[i]);
                    final Type paramType = paramTypes[i];
                    if (arg == null) {
                        arg = TypeUtils.getDefaultValue(paramType);
                    }
                    args[i] = arg;
                }
                try {
                    return constructor.newInstance(args);
                }
                catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex3) {
                    final Exception ex;
                    final Exception e = ex;
                    throw new JSONException("invoke constructor error, " + constructor, e);
                }
            }
        }
        if (this.function != null && this.parameters.length == 1) {
            final Parameter param = this.parameters[0];
            Object arg2 = values.get(this.hashCodes[0]);
            final Class<?> paramType2 = param.getType();
            if (arg2 == null) {
                arg2 = TypeUtils.getDefaultValue(paramType2);
            }
            else if (!paramType2.isInstance(arg2)) {
                arg2 = TypeUtils.cast(arg2, paramType2);
            }
            return this.function.apply(arg2);
        }
        if (this.biFunction != null && this.parameters.length == 2) {
            Object arg3 = values.get(this.hashCodes[0]);
            final Parameter param2 = this.parameters[0];
            final Class<?> param0Type = param2.getType();
            if (arg3 == null) {
                arg3 = TypeUtils.getDefaultValue(param0Type);
            }
            else if (!param0Type.isInstance(arg3)) {
                arg3 = TypeUtils.cast(arg3, param0Type);
            }
            Object arg4 = values.get(this.hashCodes[1]);
            final Parameter param3 = this.parameters[1];
            final Class<?> param1Type = param3.getType();
            if (arg4 == null) {
                arg4 = TypeUtils.getDefaultValue(param1Type);
            }
            else if (!param1Type.isInstance(arg4)) {
                arg4 = TypeUtils.cast(arg4, param1Type);
            }
            return this.biFunction.apply(arg3, arg4);
        }
        final int size = this.parameters.length;
        final Object[] args2 = new Object[this.constructor.getParameterCount()];
        if (this.marker) {
            int j = 0;
            int flag = 0;
            while (j < size) {
                final Object arg5 = values.get(this.hashCodes[j]);
                if (arg5 != null) {
                    args2[j] = arg5;
                }
                else {
                    flag |= 1 << j;
                    final Class<?> paramType3 = this.parameters[j].getType();
                    if (paramType3.isPrimitive()) {
                        args2[j] = TypeUtils.getDefaultValue(paramType3);
                    }
                }
                final int n = j + 1;
                if (n % 32 == 0 || n == size) {
                    args2[size + j / 32] = flag;
                    flag = 0;
                }
                j = n;
            }
        }
        else {
            for (int j = 0; j < size; ++j) {
                final Class<?> paramType4 = this.parameters[j].getType();
                Object arg6 = values.get(this.hashCodes[j]);
                if (arg6 == null) {
                    arg6 = TypeUtils.getDefaultValue(paramType4);
                }
                else if (!paramType4.isInstance(arg6)) {
                    arg6 = TypeUtils.cast(arg6, paramType4);
                }
                args2[j] = arg6;
            }
        }
        try {
            return this.constructor.newInstance(args2);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex4) {
            final Exception ex2;
            final Exception e2 = ex2;
            throw new JSONException("invoke constructor error, " + this.constructor, e2);
        }
    }
}
