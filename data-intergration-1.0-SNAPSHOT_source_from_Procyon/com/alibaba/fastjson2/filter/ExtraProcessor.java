// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import java.lang.reflect.Type;

public interface ExtraProcessor extends Filter
{
    default Type getType(final String fieldName) {
        return Object.class;
    }
    
    void processExtra(final Object p0, final String p1, final Object p2);
}
