// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

import java.util.HashMap;
import java.lang.reflect.Modifier;
import java.util.Map;

public class TypeCollector
{
    static final Map<String, String> PRIMITIVES;
    final String methodName;
    final Class<?>[] parameterTypes;
    protected MethodCollector collector;
    
    public TypeCollector(final String methodName, final Class<?>[] parameterTypes) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.collector = null;
    }
    
    protected MethodCollector visitMethod(final int access, final String name, final String desc) {
        if (this.collector != null) {
            return null;
        }
        if (!name.equals(this.methodName)) {
            return null;
        }
        final Type[] argTypes = Type.getArgumentTypes(desc);
        int longOrDoubleQuantity = 0;
        for (int i = 0; i < argTypes.length; ++i) {
            final Type t = argTypes[i];
            final String className = t.getClassName();
            if ("long".equals(className) || "double".equals(className)) {
                ++longOrDoubleQuantity;
            }
        }
        if (argTypes.length != this.parameterTypes.length) {
            return null;
        }
        for (int i = 0; i < argTypes.length; ++i) {
            if (!this.correctTypeName(argTypes[i], this.parameterTypes[i].getName())) {
                return null;
            }
        }
        return this.collector = new MethodCollector(Modifier.isStatic(access) ? 0 : 1, argTypes.length + longOrDoubleQuantity);
    }
    
    private boolean correctTypeName(final Type type, final String paramTypeName) {
        String s = type.getClassName();
        final StringBuilder braces = new StringBuilder();
        while (s.endsWith("[]")) {
            braces.append('[');
            s = s.substring(0, s.length() - 2);
        }
        if (braces.length() != 0) {
            if (TypeCollector.PRIMITIVES.containsKey(s)) {
                s = braces.append(TypeCollector.PRIMITIVES.get(s)).toString();
            }
            else {
                s = braces.append('L').append(s).append(';').toString();
            }
        }
        return s.equals(paramTypeName);
    }
    
    public String[] getParameterNamesForMethod() {
        if (this.collector == null || !this.collector.debugInfoPresent) {
            return new String[0];
        }
        return this.collector.getResult().split(",");
    }
    
    static {
        final HashMap map = new HashMap();
        map.put("int", "I");
        map.put("boolean", "Z");
        map.put("byte", "B");
        map.put("char", "C");
        map.put("short", "S");
        map.put("float", "F");
        map.put("long", "J");
        map.put("double", "D");
        PRIMITIVES = map;
    }
}
