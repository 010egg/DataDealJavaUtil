// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.util;

import java.util.Iterator;
import java.util.ServiceLoader;
import ch.qos.logback.core.util.Loader;

public class EnvUtil
{
    static ClassLoader testServiceLoaderClassLoader;
    
    static {
        EnvUtil.testServiceLoaderClassLoader = null;
    }
    
    public static boolean isGroovyAvailable() {
        final ClassLoader classLoader = Loader.getClassLoaderOfClass(EnvUtil.class);
        try {
            final Class<?> bindingClass = classLoader.loadClass("groovy.lang.Binding");
            return bindingClass != null;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    private static ClassLoader getServiceLoaderClassLoader() {
        return (EnvUtil.testServiceLoaderClassLoader == null) ? Loader.getClassLoaderOfClass(EnvUtil.class) : EnvUtil.testServiceLoaderClassLoader;
    }
    
    public static <T> T loadFromServiceLoader(final Class<T> c) {
        final ServiceLoader<T> loader = ServiceLoader.load(c, getServiceLoaderClassLoader());
        final Iterator<T> it = loader.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }
}
