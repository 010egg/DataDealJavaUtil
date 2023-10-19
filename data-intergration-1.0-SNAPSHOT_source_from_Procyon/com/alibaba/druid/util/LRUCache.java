// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.Map;
import java.util.LinkedHashMap;

public class LRUCache<K, V> extends LinkedHashMap<K, V>
{
    private static final long serialVersionUID = 1L;
    private final int maxSize;
    
    public LRUCache(final int maxSize) {
        this(maxSize, 16, 0.75f, false);
    }
    
    public LRUCache(final int maxSize, final int initialCapacity, final float loadFactor, final boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        this.maxSize = maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
        return this.size() > this.maxSize;
    }
}
