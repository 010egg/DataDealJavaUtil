// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONArray;
import java.util.List;
import java.util.function.Function;

public class ToDouble implements Function
{
    final Double defaultValue;
    
    public ToDouble(final Double defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Object apply(final Object o) {
        if (o == null) {
            return this.defaultValue;
        }
        if (o instanceof Boolean) {
            return o ? 1.0 : 0.0;
        }
        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }
        if (o instanceof String) {
            final String str = (String)o;
            if (str.isEmpty()) {
                return this.defaultValue;
            }
            return Double.parseDouble(str);
        }
        else {
            if (o instanceof List) {
                final List list = (List)o;
                final JSONArray array = new JSONArray(list.size());
                for (int i = 0; i < list.size(); ++i) {
                    final Object item = list.get(i);
                    array.add(this.apply(item));
                }
                return array;
            }
            throw new JSONException("can not cast to Double " + o.getClass());
        }
    }
}
