// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONArray;
import java.util.List;
import java.util.Collections;
import java.math.BigInteger;
import java.math.BigDecimal;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.IOUtils;
import java.util.function.Function;

public class StringToAny implements Function
{
    final Object defaultValue;
    final Class targetClass;
    
    public StringToAny(final Class targetClass, final Object defaultValue) {
        this.targetClass = targetClass;
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Object apply(final Object from) {
        final String str = (String)from;
        if (str == null || "null".equals(str) || "".equals(str)) {
            return this.defaultValue;
        }
        if (this.targetClass == Byte.TYPE || this.targetClass == Byte.class) {
            return Byte.parseByte(str);
        }
        if (this.targetClass == Short.TYPE || this.targetClass == Short.class) {
            return Short.parseShort(str);
        }
        if (this.targetClass == Integer.TYPE || this.targetClass == Integer.class) {
            return Integer.parseInt(str);
        }
        if (this.targetClass == Long.TYPE || this.targetClass == Long.class) {
            if (!IOUtils.isNumber(str) && str.length() == 19) {
                return DateUtils.parseMillis(str, DateUtils.DEFAULT_ZONE_ID);
            }
            return Long.parseLong(str);
        }
        else {
            if (this.targetClass == Float.TYPE || this.targetClass == Float.class) {
                return Float.parseFloat(str);
            }
            if (this.targetClass == Double.TYPE || this.targetClass == Double.class) {
                return Double.parseDouble(str);
            }
            if (this.targetClass == Character.TYPE || this.targetClass == Character.class) {
                return str.charAt(0);
            }
            if (this.targetClass == Boolean.TYPE || this.targetClass == Boolean.class) {
                return "true".equals(str);
            }
            if (this.targetClass == BigDecimal.class) {
                return new BigDecimal(str);
            }
            if (this.targetClass == BigInteger.class) {
                return new BigInteger(str);
            }
            if (this.targetClass != Collections.class && this.targetClass != List.class && this.targetClass != JSONArray.class) {
                throw new JSONException("can not convert to " + this.targetClass + ", value : " + str);
            }
            if ("[]".equals(str)) {
                return new JSONArray();
            }
            return JSON.parseObject(str, (Class<Object>)this.targetClass);
        }
    }
}
