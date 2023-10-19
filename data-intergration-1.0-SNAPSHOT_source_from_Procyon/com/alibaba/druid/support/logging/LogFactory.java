// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.logging;

import java.lang.reflect.Constructor;

public class LogFactory
{
    private static Constructor logConstructor;
    
    private static void tryImplementation(final String testClassName, final String implClassName) {
        if (LogFactory.logConstructor != null) {
            return;
        }
        try {
            Resources.classForName(testClassName);
            final Class implClass = Resources.classForName(implClassName);
            LogFactory.logConstructor = implClass.getConstructor(String.class);
            final Class<?> declareClass = LogFactory.logConstructor.getDeclaringClass();
            if (!Log.class.isAssignableFrom(declareClass)) {
                LogFactory.logConstructor = null;
            }
            try {
                if (null != LogFactory.logConstructor) {
                    LogFactory.logConstructor.newInstance(LogFactory.class.getName());
                }
            }
            catch (Throwable t) {
                LogFactory.logConstructor = null;
            }
        }
        catch (Throwable t2) {}
    }
    
    public static Log getLog(final Class clazz) {
        return getLog(clazz.getName());
    }
    
    public static Log getLog(final String loggerName) {
        try {
            return LogFactory.logConstructor.newInstance(loggerName);
        }
        catch (Throwable t) {
            throw new RuntimeException("Error creating logger for logger '" + loggerName + "'.  Cause: " + t, t);
        }
    }
    
    public static synchronized void selectLog4JLogging() {
        try {
            Resources.classForName("org.apache.log4j.Logger");
            final Class implClass = Resources.classForName("com.alibaba.druid.support.logging.Log4jImpl");
            LogFactory.logConstructor = implClass.getConstructor(String.class);
        }
        catch (Throwable t) {}
    }
    
    public static synchronized void selectJavaLogging() {
        try {
            Resources.classForName("java.util.logging.Logger");
            final Class implClass = Resources.classForName("com.alibaba.druid.support.logging.Jdk14LoggingImpl");
            LogFactory.logConstructor = implClass.getConstructor(String.class);
        }
        catch (Throwable t) {}
    }
    
    static {
        final String logType = System.getProperty("druid.logType");
        if (logType != null) {
            if (logType.equalsIgnoreCase("slf4j")) {
                tryImplementation("org.slf4j.Logger", "com.alibaba.druid.support.logging.SLF4JImpl");
            }
            else if (logType.equalsIgnoreCase("log4j")) {
                tryImplementation("org.apache.log4j.Logger", "com.alibaba.druid.support.logging.Log4jImpl");
            }
            else if (logType.equalsIgnoreCase("log4j2")) {
                tryImplementation("org.apache.logging.log4j.Logger", "com.alibaba.druid.support.logging.Log4j2Impl");
            }
            else if (logType.equalsIgnoreCase("commonsLog")) {
                tryImplementation("org.apache.commons.logging.LogFactory", "com.alibaba.druid.support.logging.JakartaCommonsLoggingImpl");
            }
            else if (logType.equalsIgnoreCase("jdkLog")) {
                tryImplementation("java.util.logging.Logger", "com.alibaba.druid.support.logging.Jdk14LoggingImpl");
            }
        }
        tryImplementation("org.slf4j.Logger", "com.alibaba.druid.support.logging.SLF4JImpl");
        tryImplementation("org.apache.log4j.Logger", "com.alibaba.druid.support.logging.Log4jImpl");
        tryImplementation("org.apache.logging.log4j.Logger", "com.alibaba.druid.support.logging.Log4j2Impl");
        tryImplementation("org.apache.commons.logging.LogFactory", "com.alibaba.druid.support.logging.JakartaCommonsLoggingImpl");
        tryImplementation("java.util.logging.Logger", "com.alibaba.druid.support.logging.Jdk14LoggingImpl");
        if (LogFactory.logConstructor == null) {
            try {
                LogFactory.logConstructor = NoLoggingImpl.class.getConstructor(String.class);
            }
            catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }
}
