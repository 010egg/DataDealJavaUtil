// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import java.math.BigInteger;
import com.alibaba.fastjson2.util.TypeUtils;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

public final class EnumSchema extends JSONSchema
{
    final Set<Object> items;
    
    EnumSchema(final Object... items) {
        super(null, null);
        this.items = new LinkedHashSet<Object>(items.length);
        for (Object item : items) {
            if (item instanceof BigDecimal) {
                final BigDecimal decimal = ((BigDecimal)item).stripTrailingZeros();
                if (decimal.scale() == 0) {
                    final BigInteger bigInt = decimal.toBigInteger();
                    if (bigInt.compareTo(TypeUtils.BIGINT_INT32_MIN) >= 0 && bigInt.compareTo(TypeUtils.BIGINT_INT32_MAX) <= 0) {
                        item = bigInt.intValue();
                    }
                    else if (bigInt.compareTo(TypeUtils.BIGINT_INT64_MIN) >= 0 && bigInt.compareTo(TypeUtils.BIGINT_INT64_MAX) <= 0) {
                        item = bigInt.longValue();
                    }
                    else {
                        item = bigInt;
                    }
                }
                else {
                    item = decimal;
                }
            }
            this.items.add(item);
        }
    }
    
    @Override
    public Type getType() {
        return Type.Enum;
    }
    
    @Override
    public ValidateResult validate(Object value) {
        if (value instanceof BigDecimal) {
            final BigDecimal decimal = (BigDecimal)value;
            value = decimal.stripTrailingZeros();
            final long longValue = decimal.longValue();
            if (decimal.compareTo(BigDecimal.valueOf(longValue)) == 0) {
                value = longValue;
            }
            else if (decimal.scale() == 0) {
                value = decimal.unscaledValue();
            }
        }
        else if (value instanceof BigInteger) {
            final BigInteger bigInt = (BigInteger)value;
            if (bigInt.compareTo(TypeUtils.BIGINT_INT64_MIN) >= 0 && bigInt.compareTo(TypeUtils.BIGINT_INT64_MAX) <= 0) {
                value = bigInt.longValue();
            }
        }
        if (value instanceof Long) {
            final long longValue2 = (long)value;
            if (longValue2 >= -2147483648L && longValue2 <= 2147483647L) {
                value = (int)longValue2;
            }
        }
        if (this.items.contains(value)) {
            return EnumSchema.SUCCESS;
        }
        if (value == null) {
            return EnumSchema.FAIL_INPUT_NULL;
        }
        return new ValidateResult(false, "expect type %s, but %s", new Object[] { Type.Enum, value.getClass() });
    }
}
