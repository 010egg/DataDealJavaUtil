// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import com.alibaba.fastjson2.JSONWriter;

public interface PropertyPreFilter extends Filter
{
    boolean process(final JSONWriter p0, final Object p1, final String p2);
}
