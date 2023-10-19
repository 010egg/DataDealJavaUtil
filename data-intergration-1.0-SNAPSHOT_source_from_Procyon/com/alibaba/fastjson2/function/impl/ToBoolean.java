// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONException;
import java.util.function.Function;

public class ToBoolean implements Function
{
    final Boolean defaultValue;
    
    public ToBoolean(final Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Object apply(final Object o) {
        if (o == null) {
            return this.defaultValue;
        }
        if (o instanceof Boolean) {
            return o;
        }
        if (o instanceof Number) {
            return ((Number)o).intValue() == 1;
        }
        if (o instanceof String) {
            final String s;
            final String str = s = (String)o;
            switch (s) {
                case "true":
                case "TRUE":
                case "True":
                case "T":
                case "Y":
                case "YES":
                case "Yes":
                case "yes": {
                    return Boolean.TRUE;
                }
                case "false":
                case "FALSE":
                case "False":
                case "F":
                case "N":
                case "NO":
                case "no":
                case "No": {
                    return Boolean.FALSE;
                }
                case "null": {
                    return this.defaultValue;
                }
            }
        }
        throw new JSONException("can not cast to Byte " + o.getClass());
    }
}
