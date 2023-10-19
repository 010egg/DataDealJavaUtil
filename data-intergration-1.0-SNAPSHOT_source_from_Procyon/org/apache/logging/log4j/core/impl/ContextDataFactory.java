// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.util.LoaderUtil;
import java.lang.reflect.Constructor;

public class ContextDataFactory
{
    private static final String CLASS_NAME;
    private static final Class<?> CACHED_CLASS;
    private static final Constructor<?> CACHED_CONSTRUCTOR;
    
    private static Class<?> createCachedClass(final String className) {
        if (className == null) {
            return null;
        }
        try {
            return LoaderUtil.loadClass(className);
        }
        catch (Exception any) {
            return null;
        }
    }
    
    private static Constructor<?> createCachedConstructor(final Class<?> cachedClass) {
        if (cachedClass == null) {
            return null;
        }
        try {
            return cachedClass.getDeclaredConstructor(Integer.TYPE);
        }
        catch (Exception any) {
            return null;
        }
    }
    
    public static StringMap createContextData() {
        if (ContextDataFactory.CACHED_CLASS == null) {
            return new SortedArrayStringMap();
        }
        try {
            return (StringMap)ContextDataFactory.CACHED_CLASS.newInstance();
        }
        catch (Exception any) {
            return new SortedArrayStringMap();
        }
    }
    
    public static StringMap createContextData(final int initialCapacity) {
        if (ContextDataFactory.CACHED_CONSTRUCTOR == null) {
            return new SortedArrayStringMap(initialCapacity);
        }
        try {
            return (StringMap)ContextDataFactory.CACHED_CONSTRUCTOR.newInstance(initialCapacity);
        }
        catch (Exception any) {
            return new SortedArrayStringMap(initialCapacity);
        }
    }
    
    static {
        CLASS_NAME = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextData");
        CACHED_CLASS = createCachedClass(ContextDataFactory.CLASS_NAME);
        CACHED_CONSTRUCTOR = createCachedConstructor(ContextDataFactory.CACHED_CLASS);
    }
}
