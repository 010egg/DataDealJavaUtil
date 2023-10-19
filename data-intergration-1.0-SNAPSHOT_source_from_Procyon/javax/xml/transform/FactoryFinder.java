// 
// Decompiled by Procyon v0.5.36
// 

package javax.xml.transform;

import java.io.UnsupportedEncodingException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class FactoryFinder
{
    private static final boolean debug = false;
    
    private static void debugPrintln(final String s) {
    }
    
    private static ClassLoader findClassLoader() throws ConfigurationError {
        Method method;
        try {
            method = Thread.class.getMethod("getContextClassLoader", (Class[])null);
        }
        catch (NoSuchMethodException ex3) {
            debugPrintln("assuming JDK 1.1");
            return FactoryFinder.class.getClassLoader();
        }
        try {
            return (ClassLoader)method.invoke(Thread.currentThread(), (Object[])null);
        }
        catch (IllegalAccessException ex) {
            throw new ConfigurationError("Unexpected IllegalAccessException", ex);
        }
        catch (InvocationTargetException ex2) {
            throw new ConfigurationError("Unexpected InvocationTargetException", ex2);
        }
    }
    
    private static Object newInstance(final String s, final ClassLoader classLoader) throws ConfigurationError {
        try {
            Class<?> clazz;
            if (classLoader == null) {
                clazz = Class.forName(s);
            }
            else {
                clazz = classLoader.loadClass(s);
            }
            return clazz.newInstance();
        }
        catch (ClassNotFoundException ex) {
            throw new ConfigurationError("Provider " + s + " not found", ex);
        }
        catch (Exception obj) {
            throw new ConfigurationError("Provider " + s + " could not be instantiated: " + obj, obj);
        }
    }
    
    static Object find(final String s, final String str) throws ConfigurationError {
        debugPrintln("debug is on");
        final ClassLoader classLoader = findClassLoader();
        try {
            final String property = System.getProperty(s);
            if (property != null) {
                debugPrintln("found system property " + property);
                return newInstance(property, classLoader);
            }
        }
        catch (SecurityException ex) {}
        try {
            final File file = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties");
            if (file.exists()) {
                final Properties properties = new Properties();
                properties.load(new FileInputStream(file));
                final String property2 = properties.getProperty(s);
                debugPrintln("found java.home property " + property2);
                return newInstance(property2, classLoader);
            }
        }
        catch (Exception ex2) {}
        final String string = "META-INF/services/" + s;
        try {
            InputStream inputStream;
            if (classLoader == null) {
                inputStream = ClassLoader.getSystemResourceAsStream(string);
            }
            else {
                inputStream = classLoader.getResourceAsStream(string);
            }
            if (inputStream != null) {
                debugPrintln("found " + string);
                BufferedReader bufferedReader;
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                }
                catch (UnsupportedEncodingException ex3) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                }
                final String line = bufferedReader.readLine();
                bufferedReader.close();
                if (line != null && !"".equals(line)) {
                    debugPrintln("loaded from services: " + line);
                    return newInstance(line, classLoader);
                }
            }
        }
        catch (Exception ex4) {}
        if (str == null) {
            throw new ConfigurationError("Provider for " + s + " cannot be found", null);
        }
        debugPrintln("loaded from fallback value: " + str);
        return newInstance(str, classLoader);
    }
    
    static class ConfigurationError extends Error
    {
        private Exception exception;
        
        ConfigurationError(final String message, final Exception exception) {
            super(message);
            this.exception = exception;
        }
        
        Exception getException() {
            return this.exception;
        }
    }
}
