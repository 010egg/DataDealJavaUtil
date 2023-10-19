// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public interface ValueConsumer
{
    default void accept(final byte[] bytes, final int off, final int len) {
        this.accept(new String(bytes, off, len, StandardCharsets.UTF_8));
    }
    
    default void acceptNull() {
    }
    
    default void accept(final boolean val) {
    }
    
    default void accept(final int val) {
        this.accept((Number)val);
    }
    
    default void accept(final long val) {
        this.accept((Number)val);
    }
    
    default void accept(final Number val) {
    }
    
    default void accept(final String val) {
    }
    
    default void accept(final Map object) {
    }
    
    default void accept(final List array) {
    }
}
