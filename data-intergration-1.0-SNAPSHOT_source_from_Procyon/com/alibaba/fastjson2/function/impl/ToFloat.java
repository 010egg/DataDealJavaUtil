// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONException;
import java.util.function.Function;

public class ToFloat implements Function
{
    final Float defaultValue;
    
    public ToFloat(final Float defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Object apply(final Object o) {
        if (o == null) {
            return this.defaultValue;
        }
        if (o instanceof Boolean) {
            return o ? 1.0f : 0.0f;
        }
        if (o instanceof Number) {
            return ((Number)o).floatValue();
        }
        throw new JSONException("can not cast to Float " + o.getClass());
    }
}
