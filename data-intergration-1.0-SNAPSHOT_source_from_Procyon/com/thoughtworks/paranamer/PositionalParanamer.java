// 
// Decompiled by Procyon v0.5.36
// 

package com.thoughtworks.paranamer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.AccessibleObject;

public class PositionalParanamer implements Paranamer
{
    private final String prefix;
    public static final String __PARANAMER_DATA = "<init> java.lang.String prefix \nlookupParameterNames java.lang.reflect.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.reflect.AccessibleObject,boolean methodOrCtor,throwExceptionIfMissing \n";
    
    public PositionalParanamer() {
        this("arg");
    }
    
    public PositionalParanamer(final String prefix) {
        this.prefix = prefix;
    }
    
    public String[] lookupParameterNames(final AccessibleObject methodOrConstructor) {
        return this.lookupParameterNames(methodOrConstructor, true);
    }
    
    public String[] lookupParameterNames(final AccessibleObject methodOrCtor, final boolean throwExceptionIfMissing) {
        final int count = this.count(methodOrCtor);
        final String[] result = new String[count];
        for (int i = 0; i < result.length; ++i) {
            result[i] = this.prefix + i;
        }
        return result;
    }
    
    private int count(final AccessibleObject methodOrCtor) {
        if (methodOrCtor instanceof Method) {
            final Method method = (Method)methodOrCtor;
            return method.getParameterTypes().length;
        }
        final Constructor<?> constructor = (Constructor<?>)methodOrCtor;
        return constructor.getParameterTypes().length;
    }
}
