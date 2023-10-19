// 
// Decompiled by Procyon v0.5.36
// 

package org.xml.sax.helpers;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class NewInstance
{
    static Object newInstance(final ClassLoader classLoader, final String s) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz;
        if (classLoader == null) {
            clazz = Class.forName(s);
        }
        else {
            clazz = classLoader.loadClass(s);
        }
        return clazz.newInstance();
    }
    
    static ClassLoader getClassLoader() {
        Method method;
        try {
            method = Thread.class.getMethod("getContextClassLoader", (Class[])null);
        }
        catch (NoSuchMethodException ex3) {
            return NewInstance.class.getClassLoader();
        }
        try {
            return (ClassLoader)method.invoke(Thread.currentThread(), (Object[])null);
        }
        catch (IllegalAccessException ex) {
            throw new UnknownError(ex.getMessage());
        }
        catch (InvocationTargetException ex2) {
            throw new UnknownError(ex2.getMessage());
        }
    }
}
