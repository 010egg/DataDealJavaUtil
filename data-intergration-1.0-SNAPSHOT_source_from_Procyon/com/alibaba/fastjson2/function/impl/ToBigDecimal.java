// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function.impl;

import com.alibaba.fastjson2.JSONException;
import java.math.BigInteger;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigDecimal;
import java.util.function.Function;

public class ToBigDecimal implements Function
{
    @Override
    public Object apply(final Object o) {
        if (o == null || o instanceof BigDecimal) {
            return o;
        }
        if (o instanceof Boolean) {
            return o ? BigDecimal.ONE : BigDecimal.ZERO;
        }
        if (o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long || o instanceof AtomicInteger || o instanceof AtomicLong) {
            return BigDecimal.valueOf(((Number)o).longValue());
        }
        if (o instanceof Float || o instanceof Double) {
            final double doubleValue = ((Number)o).doubleValue();
            return TypeUtils.toBigDecimal(doubleValue);
        }
        if (o instanceof BigInteger) {
            return new BigDecimal((BigInteger)o);
        }
        if (o instanceof String) {
            return new BigDecimal((String)o);
        }
        throw new JSONException("can not cast to BigDecimal " + o.getClass());
    }
}
