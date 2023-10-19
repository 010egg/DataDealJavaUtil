// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function;

@FunctionalInterface
public interface FieldConsumer<T>
{
    void accept(final T p0, final int p1, final Object p2);
}
