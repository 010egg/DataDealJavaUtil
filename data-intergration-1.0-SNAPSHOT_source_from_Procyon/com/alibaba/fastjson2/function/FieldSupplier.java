// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function;

@FunctionalInterface
public interface FieldSupplier<T>
{
    Object get(final T p0, final int p1);
}
