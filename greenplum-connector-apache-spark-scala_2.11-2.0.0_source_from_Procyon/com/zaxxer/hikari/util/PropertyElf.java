// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.util;

import org.slf4j.LoggerFactory;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Method;
import java.util.List;
import com.zaxxer.hikari.HikariConfig;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;
import org.slf4j.Logger;

public final class PropertyElf
{
    private static final Logger LOGGER;
    private static final Pattern GETTER_PATTERN;
    
    public static void setTargetFromProperties(final Object target, final Properties properties) {
        if (target == null || properties == null) {
            return;
        }
        final List<Method> methods = Arrays.asList(target.getClass().getMethods());
        final List<Method> methods2;
        properties.forEach((key, value) -> {
            if (target instanceof HikariConfig && key.toString().startsWith("dataSource.")) {
                ((HikariConfig)target).addDataSourceProperty(key.toString().substring("dataSource.".length()), value);
            }
            else {
                setProperty(target, key.toString(), value, methods2);
            }
        });
    }
    
    public static Set<String> getPropertyNames(final Class<?> targetClass) {
        final HashSet<String> set = new HashSet<String>();
        final Matcher matcher = PropertyElf.GETTER_PATTERN.matcher("");
        for (final Method method : targetClass.getMethods()) {
            String name = method.getName();
            if (method.getParameterTypes().length == 0 && matcher.reset(name).matches()) {
                name = name.replaceFirst("(get|is)", "");
                try {
                    if (targetClass.getMethod("set" + name, method.getReturnType()) != null) {
                        name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
                        set.add(name);
                    }
                }
                catch (Exception e) {}
            }
        }
        return set;
    }
    
    public static Object getProperty(final String propName, final Object target) {
        try {
            final String capitalized = "get" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
            final Method method = target.getClass().getMethod(capitalized, (Class<?>[])new Class[0]);
            return method.invoke(target, new Object[0]);
        }
        catch (Exception e) {
            try {
                final String capitalized2 = "is" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
                final Method method2 = target.getClass().getMethod(capitalized2, (Class<?>[])new Class[0]);
                return method2.invoke(target, new Object[0]);
            }
            catch (Exception e2) {
                return null;
            }
        }
    }
    
    public static Properties copyProperties(final Properties props) {
        final Properties copy = new Properties();
        props.forEach((key, value) -> copy.setProperty(key.toString(), value.toString()));
        return copy;
    }
    
    private static void setProperty(final Object target, final String propName, final Object propValue, final List<Method> methods) {
        final String methodName = "set" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
        Method writeMethod = methods.stream().filter(m -> m.getName().equals(methodName) && m.getParameterCount() == 1).findFirst().orElse(null);
        if (writeMethod == null) {
            final String methodName2 = "set" + propName.toUpperCase(Locale.ENGLISH);
            writeMethod = methods.stream().filter(m -> m.getName().equals(methodName2) && m.getParameterCount() == 1).findFirst().orElse(null);
        }
        if (writeMethod == null) {
            PropertyElf.LOGGER.error("Property {} does not exist on target {}", propName, target.getClass());
            throw new RuntimeException(String.format("Property %s does not exist on target %s", propName, target.getClass()));
        }
        try {
            final Class<?> paramClass = writeMethod.getParameterTypes()[0];
            if (paramClass == Integer.TYPE) {
                writeMethod.invoke(target, Integer.parseInt(propValue.toString()));
            }
            else if (paramClass == Long.TYPE) {
                writeMethod.invoke(target, Long.parseLong(propValue.toString()));
            }
            else if (paramClass == Boolean.TYPE || paramClass == Boolean.class) {
                writeMethod.invoke(target, Boolean.parseBoolean(propValue.toString()));
            }
            else if (paramClass == String.class) {
                writeMethod.invoke(target, propValue.toString());
            }
            else {
                writeMethod.invoke(target, propValue);
            }
        }
        catch (Exception e) {
            PropertyElf.LOGGER.error("Failed to set property {} on target {}", propName, target.getClass(), e);
            throw new RuntimeException(e);
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(PropertyElf.class);
        GETTER_PATTERN = Pattern.compile("(get|is)[A-Z].+");
    }
}
