// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.util.beans;

import java.lang.reflect.Method;

public class BeanUtil
{
    public static final BeanUtil INSTANCE;
    public static final String PREFIX_GETTER_IS = "is";
    public static final String PREFIX_GETTER_GET = "get";
    public static final String PREFIX_SETTER = "set";
    public static final String PREFIX_ADDER = "add";
    
    public boolean isAdder(final Method method) {
        final int parameterCount = this.getParameterCount(method);
        if (parameterCount != 1) {
            return false;
        }
        final Class<?> returnType = method.getReturnType();
        if (returnType != Void.TYPE) {
            return false;
        }
        final String methodName = method.getName();
        return methodName.startsWith("add");
    }
    
    public boolean isGetter(final Method method) {
        final int parameterCount = this.getParameterCount(method);
        if (parameterCount > 0) {
            return false;
        }
        final Class<?> returnType = method.getReturnType();
        if (returnType == Void.TYPE) {
            return false;
        }
        final String methodName = method.getName();
        return (methodName.startsWith("get") || methodName.startsWith("is")) && (!methodName.startsWith("is") || returnType.equals(Boolean.TYPE) || returnType.equals(Boolean.class));
    }
    
    private int getParameterCount(final Method method) {
        return method.getParameterTypes().length;
    }
    
    public boolean isSetter(final Method method) {
        final int parameterCount = this.getParameterCount(method);
        if (parameterCount != 1) {
            return false;
        }
        final Class<?> returnType = method.getReturnType();
        if (returnType != Void.TYPE) {
            return false;
        }
        final String methodName = method.getName();
        return methodName.startsWith("set");
    }
    
    public String getPropertyName(final Method method) {
        final String methodName = method.getName();
        String rawPropertyName = this.getSubstringIfPrefixMatches(methodName, "get");
        if (rawPropertyName == null) {
            rawPropertyName = this.getSubstringIfPrefixMatches(methodName, "set");
        }
        if (rawPropertyName == null) {
            rawPropertyName = this.getSubstringIfPrefixMatches(methodName, "is");
        }
        if (rawPropertyName == null) {
            rawPropertyName = this.getSubstringIfPrefixMatches(methodName, "add");
        }
        return this.toLowerCamelCase(rawPropertyName);
    }
    
    public String toLowerCamelCase(final String string) {
        if (string == null) {
            return null;
        }
        if (string.isEmpty()) {
            return string;
        }
        if (string.length() > 1 && Character.isUpperCase(string.charAt(1)) && Character.isUpperCase(string.charAt(0))) {
            return string;
        }
        final char[] chars = string.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
    
    private String getSubstringIfPrefixMatches(final String wholeString, final String prefix) {
        if (wholeString.startsWith(prefix)) {
            return wholeString.substring(prefix.length());
        }
        return null;
    }
    
    static {
        INSTANCE = new BeanUtil();
    }
}
