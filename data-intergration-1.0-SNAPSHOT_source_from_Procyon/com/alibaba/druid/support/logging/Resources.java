// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

public final class Resources
{
    private static ClassLoader defaultClassLoader;
    
    private Resources() {
    }
    
    public static ClassLoader getDefaultClassLoader() {
        return Resources.defaultClassLoader;
    }
    
    public static void setDefaultClassLoader(final ClassLoader defaultClassLoader) {
        Resources.defaultClassLoader = defaultClassLoader;
    }
    
    public static Class<?> classForName(final String className) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            clazz = getClassLoader().loadClass(className);
        }
        catch (Exception ex) {}
        if (clazz == null) {
            clazz = Class.forName(className);
        }
        return clazz;
    }
    
    private static ClassLoader getClassLoader() {
        if (Resources.defaultClassLoader != null) {
            return Resources.defaultClassLoader;
        }
        return Thread.currentThread().getContextClassLoader();
    }
}
