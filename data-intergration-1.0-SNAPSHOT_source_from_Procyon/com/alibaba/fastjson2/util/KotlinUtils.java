// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import java.util.Iterator;
import kotlin.reflect.KClass;
import java.util.List;
import java.lang.reflect.Constructor;
import kotlin.reflect.KParameter;
import kotlin.reflect.KFunction;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.DefaultConstructorMarker;
import com.alibaba.fastjson2.codec.BeanInfo;

public class KotlinUtils
{
    public static final int STATE;
    
    private KotlinUtils() {
        throw new IllegalStateException();
    }
    
    public static void getConstructor(final Class<?> clazz, final BeanInfo beanInfo) {
        int creatorParams = 0;
        Constructor<?> creatorConstructor = null;
        final String[] paramNames = beanInfo.createParameterNames;
        final Constructor[] constructor2;
        final Constructor<?>[] constructors = (Constructor<?>[])(constructor2 = BeanUtils.getConstructor(clazz));
        for (final Constructor<?> constructor : constructor2) {
            final int paramCount = constructor.getParameterCount();
            Label_0128: {
                if (paramNames == null || paramCount == paramNames.length) {
                    if (paramCount > 2) {
                        final Class<?>[] parameterTypes = constructor.getParameterTypes();
                        if (parameterTypes[paramCount - 2] == Integer.TYPE && parameterTypes[paramCount - 1] == DefaultConstructorMarker.class) {
                            beanInfo.markerConstructor = constructor;
                            break Label_0128;
                        }
                    }
                    if (creatorConstructor == null || creatorParams < paramCount) {
                        creatorParams = paramCount;
                        creatorConstructor = constructor;
                    }
                }
            }
        }
        if (creatorParams != 0 && KotlinUtils.STATE == 2) {
            try {
                List<KParameter> params = null;
                final KClass<?> kClass = (KClass<?>)Reflection.getOrCreateKotlinClass((Class)clazz);
                for (final KFunction<?> function : kClass.getConstructors()) {
                    final List<KParameter> parameters = (List<KParameter>)function.getParameters();
                    if (params == null || creatorParams == parameters.size()) {
                        params = parameters;
                    }
                }
                if (params != null) {
                    final String[] names = new String[params.size()];
                    for (int i = 0, m = names.length; i < m; ++i) {
                        names[i] = params.get(i).getName();
                    }
                    beanInfo.createParameterNames = names;
                }
            }
            catch (Throwable t) {}
        }
        beanInfo.creatorConstructor = creatorConstructor;
    }
    
    static {
        int state = 0;
        try {
            Class.forName("kotlin.Metadata");
            ++state;
            Class.forName("kotlin.reflect.jvm.ReflectJvmMapping");
            ++state;
        }
        catch (Throwable t) {}
        STATE = state;
    }
}
