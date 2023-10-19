// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.util;

import com.alibaba.fastjson2.JSON;
import java.lang.reflect.Type;

public class MultiType implements Type
{
    private final Type[] types;
    
    public MultiType(final Type... types) {
        this.types = types;
    }
    
    public int size() {
        return this.types.length;
    }
    
    public Type getType(final int index) {
        return this.types[index];
    }
    
    @Override
    public String toString() {
        return JSON.toJSONString(this.types);
    }
}
