// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import java.util.function.Function;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.PropertyNamingStrategy;

public interface NameFilter extends Filter
{
    String process(final Object p0, final String p1, final Object p2);
    
    default NameFilter of(final PropertyNamingStrategy namingStrategy) {
        return (object, name, value) -> BeanUtils.fieldName(name, namingStrategy.name());
    }
    
    default NameFilter compose(final NameFilter before, final NameFilter after) {
        return (object, name, value) -> after.process(object, before.process(object, name, value), value);
    }
    
    default NameFilter of(final Function<String, String> function) {
        return (object, name, value) -> function.apply(name);
    }
}
