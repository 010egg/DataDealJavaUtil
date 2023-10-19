// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import java.util.HashMap;
import com.alibaba.fastjson2.util.TypeUtils;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigInteger;
import com.alibaba.fastjson2.JSONObject;

public final class IntegerSchema extends JSONSchema
{
    final boolean typed;
    final long minimum;
    final boolean exclusiveMinimum;
    final long maximum;
    final boolean exclusiveMaximum;
    final long multipleOf;
    final Long constValue;
    
    IntegerSchema(final JSONObject input) {
        super(input);
        this.typed = ("integer".equalsIgnoreCase(input.getString("type")) || input.getBooleanValue("required"));
        final Object exclusiveMinimum = input.get("exclusiveMinimum");
        final long minimum = input.getLongValue("minimum", Long.MIN_VALUE);
        if (exclusiveMinimum == Boolean.TRUE) {
            this.exclusiveMinimum = true;
            this.minimum = minimum;
        }
        else if (exclusiveMinimum instanceof Number) {
            this.exclusiveMinimum = true;
            this.minimum = input.getLongValue("exclusiveMinimum");
        }
        else {
            this.minimum = minimum;
            this.exclusiveMinimum = false;
        }
        final long maximum = input.getLongValue("maximum", Long.MIN_VALUE);
        final Object exclusiveMaximum = input.get("exclusiveMaximum");
        if (exclusiveMaximum == Boolean.TRUE) {
            this.exclusiveMaximum = true;
            this.maximum = maximum;
        }
        else if (exclusiveMaximum instanceof Number) {
            this.exclusiveMaximum = true;
            this.maximum = input.getLongValue("exclusiveMaximum");
        }
        else {
            this.exclusiveMaximum = false;
            this.maximum = maximum;
        }
        this.multipleOf = input.getLongValue("multipleOf", 0L);
        this.constValue = input.getLong("const");
    }
    
    @Override
    public Type getType() {
        return Type.Integer;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        if (value == null) {
            return this.typed ? IntegerSchema.FAIL_INPUT_NULL : IntegerSchema.SUCCESS;
        }
        final Class valueClass = value.getClass();
        if (valueClass != Byte.class && valueClass != Short.class && valueClass != Integer.class && valueClass != Long.class && valueClass != BigInteger.class && valueClass != AtomicInteger.class && valueClass != AtomicLong.class) {
            if (value instanceof BigDecimal) {
                final BigDecimal decimal = (BigDecimal)value;
                final boolean integer = TypeUtils.isInteger(decimal);
                if (integer) {
                    final BigInteger unscaleValue = decimal.toBigInteger();
                    if (this.constValue != null) {
                        boolean equals = false;
                        if (TypeUtils.isInt64(unscaleValue)) {
                            equals = (this.constValue == unscaleValue.longValue());
                        }
                        if (!equals) {
                            return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, value });
                        }
                    }
                    return IntegerSchema.SUCCESS;
                }
                if (this.constValue != null) {
                    return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, value });
                }
            }
            if (this.constValue != null) {
                if (value instanceof Float) {
                    final float floatValue = (float)value;
                    if (this.constValue != floatValue) {
                        return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, value });
                    }
                }
                else if (value instanceof Double) {
                    final double doubleValue = (double)value;
                    if (this.constValue != doubleValue) {
                        return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, value });
                    }
                }
                else if (value instanceof String) {
                    final String str = (String)value;
                    boolean equals2 = false;
                    if (TypeUtils.isInteger(str) && str.length() < 21) {
                        try {
                            final long longValue = Long.parseLong(str);
                            equals2 = (this.constValue == longValue);
                        }
                        catch (NumberFormatException ex) {}
                    }
                    if (!equals2) {
                        return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, value });
                    }
                }
            }
            return this.typed ? new ValidateResult(false, "expect type %s, but %s", new Object[] { Type.Integer, valueClass }) : IntegerSchema.SUCCESS;
        }
        boolean isInt64 = true;
        if (valueClass == BigInteger.class) {
            isInt64 = TypeUtils.isInt64((BigInteger)value);
        }
        final long longValue2 = ((Number)value).longValue();
        Label_0175: {
            if (this.minimum != Long.MIN_VALUE) {
                if (this.exclusiveMinimum) {
                    if (longValue2 > this.minimum) {
                        break Label_0175;
                    }
                }
                else if (longValue2 >= this.minimum) {
                    break Label_0175;
                }
                return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, value });
            }
        }
        Label_0257: {
            if (this.maximum != Long.MIN_VALUE) {
                if (this.exclusiveMaximum) {
                    if (longValue2 < this.maximum) {
                        break Label_0257;
                    }
                }
                else if (longValue2 <= this.maximum) {
                    break Label_0257;
                }
                return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, value });
            }
        }
        if (this.multipleOf != 0L && longValue2 % this.multipleOf != 0L) {
            return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, value });
        }
        if (this.constValue != null && (this.constValue != longValue2 || !isInt64)) {
            return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, value });
        }
        return IntegerSchema.SUCCESS;
    }
    
    @Override
    public ValidateResult validate(final long longValue) {
        Label_0083: {
            if (this.minimum != Long.MIN_VALUE) {
                if (this.exclusiveMinimum) {
                    if (longValue > this.minimum) {
                        break Label_0083;
                    }
                }
                else if (longValue >= this.minimum) {
                    break Label_0083;
                }
                return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, longValue });
            }
        }
        Label_0166: {
            if (this.maximum != Long.MIN_VALUE) {
                if (this.exclusiveMaximum) {
                    if (longValue < this.maximum) {
                        break Label_0166;
                    }
                }
                else if (longValue <= this.maximum) {
                    break Label_0166;
                }
                return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, longValue });
            }
        }
        if (this.multipleOf != 0L && longValue % this.multipleOf != 0L) {
            return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, longValue });
        }
        if (this.constValue != null && this.constValue != longValue) {
            return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, longValue });
        }
        return IntegerSchema.SUCCESS;
    }
    
    @Override
    public ValidateResult validate(final Long value) {
        if (value == null) {
            return this.typed ? IntegerSchema.FAIL_INPUT_NULL : IntegerSchema.SUCCESS;
        }
        final long longValue = value;
        Label_0106: {
            if (this.minimum != Long.MIN_VALUE) {
                if (this.exclusiveMinimum) {
                    if (longValue > this.minimum) {
                        break Label_0106;
                    }
                }
                else if (longValue >= this.minimum) {
                    break Label_0106;
                }
                return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, value });
            }
        }
        Label_0186: {
            if (this.maximum != Long.MIN_VALUE) {
                if (this.exclusiveMaximum) {
                    if (longValue < this.maximum) {
                        break Label_0186;
                    }
                }
                else if (longValue <= this.maximum) {
                    break Label_0186;
                }
                return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, value });
            }
        }
        if (this.multipleOf != 0L && longValue % this.multipleOf != 0L) {
            return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, longValue });
        }
        if (this.constValue != null && this.constValue != longValue) {
            return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, value });
        }
        return IntegerSchema.SUCCESS;
    }
    
    @Override
    public ValidateResult validate(final Integer value) {
        if (value == null) {
            return this.typed ? IntegerSchema.FAIL_INPUT_NULL : IntegerSchema.SUCCESS;
        }
        final long longValue = value;
        Label_0106: {
            if (this.minimum != Long.MIN_VALUE) {
                if (this.exclusiveMinimum) {
                    if (longValue > this.minimum) {
                        break Label_0106;
                    }
                }
                else if (longValue >= this.minimum) {
                    break Label_0106;
                }
                return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, value });
            }
        }
        Label_0186: {
            if (this.maximum != Long.MIN_VALUE) {
                if (this.exclusiveMaximum) {
                    if (longValue < this.maximum) {
                        break Label_0186;
                    }
                }
                else if (longValue <= this.maximum) {
                    break Label_0186;
                }
                return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, value });
            }
        }
        if (this.multipleOf != 0L && longValue % this.multipleOf != 0L) {
            return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, longValue });
        }
        if (this.constValue != null && this.constValue != longValue) {
            return new ValidateResult(false, "const not match, expect %s, but %s", new Object[] { this.constValue, value });
        }
        return IntegerSchema.SUCCESS;
    }
    
    @Override
    public JSONObject toJSONObject() {
        final JSONObject object = new JSONObject();
        ((HashMap<String, String>)object).put("type", "integer");
        if (this.minimum != Long.MIN_VALUE) {
            ((HashMap<String, Long>)object).put(this.exclusiveMinimum ? "exclusiveMinimum" : "minimum", this.minimum);
        }
        if (this.maximum != Long.MIN_VALUE) {
            ((HashMap<String, Long>)object).put(this.exclusiveMaximum ? "exclusiveMaximum" : "maximum", this.maximum);
        }
        if (this.multipleOf != 0L) {
            ((HashMap<String, Long>)object).put("multipleOf", this.multipleOf);
        }
        if (this.constValue != null) {
            ((HashMap<String, Long>)object).put("const", this.constValue);
        }
        return object;
    }
}
