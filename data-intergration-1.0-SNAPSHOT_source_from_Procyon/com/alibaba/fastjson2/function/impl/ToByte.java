// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONException;
import java.util.function.Function;

public class ToByte implements Function
{
    final Byte defaultValue;
    
    public ToByte(final Byte defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Object apply(final Object o) {
        if (o == null) {
            return this.defaultValue;
        }
        if (o instanceof Boolean) {
            return (byte)(((boolean)o) ? 1 : 0);
        }
        if (o instanceof Number) {
            return ((Number)o).byteValue();
        }
        throw new JSONException("can not cast to Byte " + o.getClass());
    }
}
