// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.PropertyNamingStrategy;

public class PascalNameFilter implements NameFilter
{
    @Override
    public String process(final Object source, final String name, final Object value) {
        return BeanUtils.fieldName(name, PropertyNamingStrategy.PascalCase.name());
    }
}
