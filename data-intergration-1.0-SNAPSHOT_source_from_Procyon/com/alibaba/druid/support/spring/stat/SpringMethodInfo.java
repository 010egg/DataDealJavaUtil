// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.spring.stat;

import java.lang.reflect.Method;

public class SpringMethodInfo
{
    private String signature;
    private Class<?> instanceClass;
    private Method method;
    
    public SpringMethodInfo(final Class<?> instanceClass, final Method method) {
        this.instanceClass = instanceClass;
        this.method = method;
    }
    
    public String getClassName() {
        return this.instanceClass.getName();
    }
    
    public String getMethodName() {
        return this.method.getName();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.instanceClass.getName().hashCode();
        result = 31 * result + this.method.getName().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof SpringMethodInfo)) {
            return false;
        }
        final SpringMethodInfo other = (SpringMethodInfo)obj;
        if (!this.instanceClass.getName().equals(other.instanceClass.getName())) {
            return false;
        }
        if (!this.method.getName().equals(other.method.getName())) {
            return false;
        }
        if (this.method.getParameterTypes().length != other.method.getParameterTypes().length) {
            return false;
        }
        for (int i = 0; i < this.method.getParameterTypes().length; ++i) {
            if (!this.method.getParameterTypes()[i].getName().equals(other.method.getParameterTypes()[i].getName())) {
                return false;
            }
        }
        return true;
    }
    
    public static String getMethodSignature(final Method method) {
        final StringBuilder sb = new StringBuilder();
        sb.append(method.getName());
        sb.append('(');
        final Class<?>[] params = method.getParameterTypes();
        for (int j = 0; j < params.length; ++j) {
            sb.append(params[j].getName());
            if (j < params.length - 1) {
                sb.append(',');
            }
        }
        sb.append(')');
        return sb.toString();
    }
    
    public String getSignature() {
        if (this.signature == null) {
            this.signature = getMethodSignature(this.method);
        }
        return this.signature;
    }
}
