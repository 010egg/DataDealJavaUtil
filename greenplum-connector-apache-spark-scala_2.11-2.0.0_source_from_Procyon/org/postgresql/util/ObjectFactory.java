// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class ObjectFactory
{
    public static Object instantiate(final String classname, final Properties info, boolean tryString, final String stringarg) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object[] args = { info };
        Constructor<?> ctor = null;
        final Class<?> cls = Class.forName(classname);
        try {
            ctor = cls.getConstructor(Properties.class);
        }
        catch (NoSuchMethodException nsme) {
            if (tryString) {
                try {
                    ctor = cls.getConstructor(String.class);
                    args = new String[] { stringarg };
                }
                catch (NoSuchMethodException nsme2) {
                    tryString = false;
                }
            }
            if (!tryString) {
                ctor = cls.getConstructor((Class<?>[])null);
                args = null;
            }
        }
        return ctor.newInstance(args);
    }
}
