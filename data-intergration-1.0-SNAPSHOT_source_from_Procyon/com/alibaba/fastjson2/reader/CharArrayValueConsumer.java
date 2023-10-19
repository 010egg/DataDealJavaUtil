// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

public interface CharArrayValueConsumer<T>
{
    default void start() {
    }
    
    default void beforeRow(final int row) {
    }
    
    void accept(final int p0, final int p1, final char[] p2, final int p3, final int p4);
    
    default void afterRow(final int row) {
    }
    
    default void end() {
    }
}
