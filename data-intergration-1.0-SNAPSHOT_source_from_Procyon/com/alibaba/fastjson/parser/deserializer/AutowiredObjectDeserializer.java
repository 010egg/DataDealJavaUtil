// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Set;

public interface AutowiredObjectDeserializer extends ObjectDeserializer
{
    Set<Type> getAutowiredFor();
}
