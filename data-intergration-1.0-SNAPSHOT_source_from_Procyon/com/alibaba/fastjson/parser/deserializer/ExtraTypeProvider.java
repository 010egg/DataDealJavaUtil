// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

public interface ExtraTypeProvider extends ParseProcess
{
    Type getExtraType(final Object p0, final String p1);
}
