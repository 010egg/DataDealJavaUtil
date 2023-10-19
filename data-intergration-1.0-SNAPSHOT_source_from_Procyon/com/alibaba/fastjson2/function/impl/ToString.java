// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function.impl;

import java.util.function.Function;

public class ToString implements Function
{
    @Override
    public Object apply(final Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }
}
