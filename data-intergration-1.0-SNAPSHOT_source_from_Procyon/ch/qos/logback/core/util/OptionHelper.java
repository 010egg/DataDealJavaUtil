// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import java.util.Iterator;
import java.util.Properties;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.subst.NodeToStringTransformer;
import ch.qos.logback.core.spi.PropertyContainer;
import java.lang.reflect.Constructor;
import ch.qos.logback.core.Context;

public class OptionHelper
{
    static final String DELIM_START = "${";
    static final char DELIM_STOP = '}';
    static final String DELIM_DEFAULT = ":-";
    static final int DELIM_START_LEN = 2;
    static final int DELIM_STOP_LEN = 1;
    static final int DELIM_DEFAULT_LEN = 2;
    static final String _IS_UNDEFINED = "_IS_UNDEFINED";
    
    public static Object instantiateByClassName(final String className, final Class<?> superClass, final Context context) throws IncompatibleClassException, DynamicClassLoadingException {
        final ClassLoader classLoader = Loader.getClassLoaderOfObject(context);
        return instantiateByClassName(className, superClass, classLoader);
    }
    
    public static Object instantiateByClassNameAndParameter(final String className, final Class<?> superClass, final Context context, final Class<?> type, final Object param) throws IncompatibleClassException, DynamicClassLoadingException {
        final ClassLoader classLoader = Loader.getClassLoaderOfObject(context);
        return instantiateByClassNameAndParameter(className, superClass, classLoader, type, param);
    }
    
    public static Object instantiateByClassName(final String className, final Class<?> superClass, final ClassLoader classLoader) throws IncompatibleClassException, DynamicClassLoadingException {
        return instantiateByClassNameAndParameter(className, superClass, classLoader, null, null);
    }
    
    public static Object instantiateByClassNameAndParameter(final String className, final Class<?> superClass, final ClassLoader classLoader, final Class<?> type, final Object parameter) throws IncompatibleClassException, DynamicClassLoadingException {
        if (className == null) {
            throw new NullPointerException();
        }
        try {
            Class<?> classObj = null;
            classObj = classLoader.loadClass(className);
            if (!superClass.isAssignableFrom(classObj)) {
                throw new IncompatibleClassException(superClass, classObj);
            }
            if (type == null) {
                return classObj.newInstance();
            }
            final Constructor<?> constructor = classObj.getConstructor(type);
            return constructor.newInstance(parameter);
        }
        catch (IncompatibleClassException ice) {
            throw ice;
        }
        catch (Throwable t) {
            throw new DynamicClassLoadingException("Failed to instantiate type " + className, t);
        }
    }
    
    public static String substVars(final String val, final PropertyContainer pc1) {
        return substVars(val, pc1, null);
    }
    
    public static String substVars(final String input, final PropertyContainer pc0, final PropertyContainer pc1) {
        try {
            return NodeToStringTransformer.substituteVariable(input, pc0, pc1);
        }
        catch (ScanException e) {
            throw new IllegalArgumentException("Failed to parse input [" + input + "]", e);
        }
    }
    
    public static String propertyLookup(final String key, final PropertyContainer pc1, final PropertyContainer pc2) {
        String value = null;
        value = pc1.getProperty(key);
        if (value == null && pc2 != null) {
            value = pc2.getProperty(key);
        }
        if (value == null) {
            value = getSystemProperty(key, null);
        }
        if (value == null) {
            value = getEnv(key);
        }
        return value;
    }
    
    public static String getSystemProperty(final String key, final String def) {
        try {
            return System.getProperty(key, def);
        }
        catch (SecurityException e) {
            return def;
        }
    }
    
    public static String getEnv(final String key) {
        try {
            return System.getenv(key);
        }
        catch (SecurityException e) {
            return null;
        }
    }
    
    public static String getSystemProperty(final String key) {
        try {
            return System.getProperty(key);
        }
        catch (SecurityException e) {
            return null;
        }
    }
    
    public static void setSystemProperties(final ContextAware contextAware, final Properties props) {
        for (final Object o : props.keySet()) {
            final String key = (String)o;
            final String value = props.getProperty(key);
            setSystemProperty(contextAware, key, value);
        }
    }
    
    public static void setSystemProperty(final ContextAware contextAware, final String key, final String value) {
        try {
            System.setProperty(key, value);
        }
        catch (SecurityException e) {
            contextAware.addError("Failed to set system property [" + key + "]", e);
        }
    }
    
    public static Properties getSystemProperties() {
        try {
            return System.getProperties();
        }
        catch (SecurityException e) {
            return new Properties();
        }
    }
    
    public static String[] extractDefaultReplacement(final String key) {
        final String[] result = new String[2];
        if (key == null) {
            return result;
        }
        result[0] = key;
        final int d = key.indexOf(":-");
        if (d != -1) {
            result[0] = key.substring(0, d);
            result[1] = key.substring(d + 2);
        }
        return result;
    }
    
    public static boolean toBoolean(final String value, final boolean dEfault) {
        if (value == null) {
            return dEfault;
        }
        final String trimmedVal = value.trim();
        return "true".equalsIgnoreCase(trimmedVal) || (!"false".equalsIgnoreCase(trimmedVal) && dEfault);
    }
    
    public static boolean isEmpty(final String str) {
        return str == null || "".equals(str);
    }
}
