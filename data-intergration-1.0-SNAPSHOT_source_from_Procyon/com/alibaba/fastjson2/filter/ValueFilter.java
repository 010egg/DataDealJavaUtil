// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import java.util.function.Predicate;
import java.util.Map;
import java.util.function.Function;

public interface ValueFilter extends Filter
{
    Object apply(final Object p0, final String p1, final Object p2);
    
    default ValueFilter compose(final ValueFilter before, final ValueFilter after) {
        return (object, name, value) -> after.apply(object, name, before.apply(object, name, value));
    }
    
    default ValueFilter of(final String name, final Function function) {
        return (object, fieldName, fieldValue) -> (name == null || name.equals(fieldName)) ? function.apply(fieldValue) : fieldValue;
    }
    
    default ValueFilter of(final String name, final Map map) {
        Object o;
        return (object, fieldName, fieldValue) -> {
            if (name == null || name.equals(fieldName)) {
                o = map.get(fieldValue);
                if (o != null || map.containsKey(fieldValue)) {
                    return o;
                }
            }
            return fieldValue;
        };
    }
    
    default ValueFilter of(final Predicate<String> nameMatcher, final Function function) {
        return (object, fieldName, fieldValue) -> (nameMatcher == null || nameMatcher.test(fieldName)) ? function.apply(fieldValue) : fieldValue;
    }
}
