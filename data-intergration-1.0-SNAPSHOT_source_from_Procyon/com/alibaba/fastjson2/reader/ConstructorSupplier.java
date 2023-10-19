// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.util.function.Supplier;

final class ConstructorSupplier implements Supplier
{
    final Constructor constructor;
    final Class objectClass;
    final boolean useClassNewInstance;
    
    public ConstructorSupplier(final Constructor constructor) {
        constructor.setAccessible(true);
        this.constructor = constructor;
        this.objectClass = this.constructor.getDeclaringClass();
        this.useClassNewInstance = (constructor.getParameterCount() == 0 && Modifier.isPublic(constructor.getModifiers()) && Modifier.isPublic(this.objectClass.getModifiers()));
    }
    
    @Override
    public Object get() {
        try {
            if (this.useClassNewInstance) {
                return this.objectClass.newInstance();
            }
            if (this.constructor.getParameterCount() == 1) {
                return this.constructor.newInstance(new Object[1]);
            }
            return this.constructor.newInstance(new Object[0]);
        }
        catch (Throwable e) {
            throw new JSONException("create instance error", e);
        }
    }
}
