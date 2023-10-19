// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.schema;

import java.util.HashMap;
import java.math.BigInteger;
import com.alibaba.fastjson2.JSONObject;
import java.math.BigDecimal;

public final class NumberSchema extends JSONSchema
{
    final BigDecimal minimum;
    final long minimumLongValue;
    final boolean exclusiveMinimum;
    final BigDecimal maximum;
    final long maximumLongValue;
    final boolean exclusiveMaximum;
    final BigDecimal multipleOf;
    final long multipleOfLongValue;
    final boolean typed;
    
    NumberSchema(final JSONObject input) {
        super(input);
        this.typed = "number".equals(input.get("type"));
        final Object exclusiveMinimum = input.get("exclusiveMinimum");
        final BigDecimal minimum = input.getBigDecimal("minimum");
        if (exclusiveMinimum == Boolean.TRUE) {
            this.minimum = minimum;
            this.exclusiveMinimum = true;
        }
        else if (exclusiveMinimum instanceof Number) {
            this.minimum = input.getBigDecimal("exclusiveMinimum");
            this.exclusiveMinimum = true;
        }
        else {
            this.minimum = minimum;
            this.exclusiveMinimum = false;
        }
        if (this.minimum == null || this.minimum.compareTo(BigDecimal.valueOf(this.minimum.longValue())) != 0) {
            this.minimumLongValue = Long.MIN_VALUE;
        }
        else {
            this.minimumLongValue = this.minimum.longValue();
        }
        final BigDecimal maximum = input.getBigDecimal("maximum");
        final Object exclusiveMaximum = input.get("exclusiveMaximum");
        if (exclusiveMaximum == Boolean.TRUE) {
            this.maximum = maximum;
            this.exclusiveMaximum = true;
        }
        else if (exclusiveMaximum instanceof Number) {
            this.maximum = input.getBigDecimal("exclusiveMaximum");
            this.exclusiveMaximum = true;
        }
        else {
            this.maximum = maximum;
            this.exclusiveMaximum = false;
        }
        if (this.maximum == null || this.maximum.compareTo(BigDecimal.valueOf(this.maximum.longValue())) != 0) {
            this.maximumLongValue = Long.MIN_VALUE;
        }
        else {
            this.maximumLongValue = this.maximum.longValue();
        }
        this.multipleOf = input.getBigDecimal("multipleOf");
        if (this.multipleOf == null) {
            this.multipleOfLongValue = Long.MIN_VALUE;
        }
        else {
            final long longValue = this.multipleOf.longValue();
            if (this.multipleOf.compareTo(BigDecimal.valueOf(longValue)) == 0) {
                this.multipleOfLongValue = longValue;
            }
            else {
                this.multipleOfLongValue = Long.MIN_VALUE;
            }
        }
    }
    
    @Override
    public Type getType() {
        return Type.Number;
    }
    
    @Override
    public ValidateResult validate(final Object value) {
        if (value == null) {
            return this.typed ? NumberSchema.FAIL_INPUT_NULL : NumberSchema.SUCCESS;
        }
        if (!(value instanceof Number)) {
            return this.typed ? NumberSchema.FAIL_TYPE_NOT_MATCH : NumberSchema.SUCCESS;
        }
        final Number number = (Number)value;
        if (number instanceof Byte || number instanceof Short || number instanceof Integer || number instanceof Long) {
            return this.validate(number.longValue());
        }
        if (number instanceof Float || number instanceof Double) {
            return this.validate(number.doubleValue());
        }
        BigDecimal decimalValue;
        if (number instanceof BigInteger) {
            decimalValue = new BigDecimal((BigInteger)number);
        }
        else {
            if (!(number instanceof BigDecimal)) {
                return new ValidateResult(false, "expect type %s, but %s", new Object[] { Type.Number, value.getClass() });
            }
            decimalValue = (BigDecimal)number;
        }
        Label_0235: {
            if (this.minimum != null) {
                if (this.exclusiveMinimum) {
                    if (this.minimum.compareTo(decimalValue) < 0) {
                        break Label_0235;
                    }
                }
                else if (this.minimum.compareTo(decimalValue) <= 0) {
                    break Label_0235;
                }
                return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, value });
            }
        }
        Label_0312: {
            if (this.maximum != null) {
                if (this.exclusiveMaximum) {
                    if (this.maximum.compareTo(decimalValue) > 0) {
                        break Label_0312;
                    }
                }
                else if (this.maximum.compareTo(decimalValue) >= 0) {
                    break Label_0312;
                }
                return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, value });
            }
        }
        if (this.multipleOf != null && decimalValue.divideAndRemainder(this.multipleOf)[1].abs().compareTo(BigDecimal.ZERO) > 0) {
            return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, decimalValue });
        }
        return NumberSchema.SUCCESS;
    }
    
    @Override
    public ValidateResult validate(final Integer value) {
        if (value == null) {
            return NumberSchema.SUCCESS;
        }
        return this.validate((long)value);
    }
    
    @Override
    public ValidateResult validate(final Float value) {
        if (value == null) {
            return NumberSchema.SUCCESS;
        }
        return this.validate((double)value);
    }
    
    @Override
    public ValidateResult validate(final Double value) {
        if (value == null) {
            return NumberSchema.SUCCESS;
        }
        return this.validate((double)value);
    }
    
    @Override
    public ValidateResult validate(final Long value) {
        if (value == null) {
            return NumberSchema.SUCCESS;
        }
        return this.validate((long)value);
    }
    
    @Override
    public ValidateResult validate(final long value) {
        BigDecimal decimalValue = null;
        Label_0167: {
            if (this.minimum != null) {
                if (this.minimumLongValue != Long.MIN_VALUE) {
                    if (this.exclusiveMinimum) {
                        if (value > this.minimumLongValue) {
                            break Label_0167;
                        }
                    }
                    else if (value >= this.minimumLongValue) {
                        break Label_0167;
                    }
                    return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, value });
                }
                decimalValue = BigDecimal.valueOf(value);
                if (this.exclusiveMinimum) {
                    if (this.minimum.compareTo(decimalValue) < 0) {
                        break Label_0167;
                    }
                }
                else if (this.minimum.compareTo(decimalValue) <= 0) {
                    break Label_0167;
                }
                return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, value });
            }
        }
        Label_0336: {
            if (this.maximum != null) {
                if (this.maximumLongValue != Long.MIN_VALUE) {
                    if (this.exclusiveMaximum) {
                        if (value < this.maximumLongValue) {
                            break Label_0336;
                        }
                    }
                    else if (value <= this.maximumLongValue) {
                        break Label_0336;
                    }
                    return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, value });
                }
                if (decimalValue == null) {
                    decimalValue = BigDecimal.valueOf(value);
                }
                if (this.exclusiveMaximum) {
                    if (this.maximum.compareTo(decimalValue) > 0) {
                        break Label_0336;
                    }
                }
                else if (this.maximum.compareTo(decimalValue) >= 0) {
                    break Label_0336;
                }
                return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, value });
            }
        }
        if (this.multipleOf != null) {
            if (this.multipleOfLongValue != Long.MIN_VALUE && value % this.multipleOfLongValue != 0L) {
                return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, decimalValue });
            }
            if (decimalValue == null) {
                decimalValue = BigDecimal.valueOf(value);
            }
            if (decimalValue.divideAndRemainder(this.multipleOf)[1].abs().compareTo(BigDecimal.ZERO) > 0) {
                return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, value });
            }
        }
        return NumberSchema.SUCCESS;
    }
    
    @Override
    public ValidateResult validate(final double value) {
        Label_0160: {
            if (this.minimum != null) {
                if (this.minimumLongValue != Long.MIN_VALUE) {
                    if (this.exclusiveMinimum) {
                        if (value > this.minimumLongValue) {
                            break Label_0160;
                        }
                    }
                    else if (value >= this.minimumLongValue) {
                        break Label_0160;
                    }
                    return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, value });
                }
                final double minimumDoubleValue = this.minimum.doubleValue();
                if (this.exclusiveMinimum) {
                    if (value > minimumDoubleValue) {
                        break Label_0160;
                    }
                }
                else if (value >= minimumDoubleValue) {
                    break Label_0160;
                }
                return new ValidateResult(false, this.exclusiveMinimum ? "exclusiveMinimum not match, expect >= %s, but %s" : "minimum not match, expect >= %s, but %s", new Object[] { this.minimum, value });
            }
        }
        Label_0320: {
            if (this.maximum != null) {
                if (this.maximumLongValue != Long.MIN_VALUE) {
                    if (this.exclusiveMaximum) {
                        if (value < this.maximumLongValue) {
                            break Label_0320;
                        }
                    }
                    else if (value <= this.maximumLongValue) {
                        break Label_0320;
                    }
                    return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, value });
                }
                final double maximumDoubleValue = this.maximum.doubleValue();
                if (this.exclusiveMaximum) {
                    if (value < maximumDoubleValue) {
                        break Label_0320;
                    }
                }
                else if (value <= maximumDoubleValue) {
                    break Label_0320;
                }
                return new ValidateResult(false, this.exclusiveMaximum ? "exclusiveMaximum not match, expect >= %s, but %s" : "maximum not match, expect >= %s, but %s", new Object[] { this.maximum, value });
            }
        }
        if (this.multipleOf != null) {
            if (this.multipleOfLongValue != Long.MIN_VALUE && value % this.multipleOfLongValue != 0.0) {
                return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, value });
            }
            final BigDecimal decimalValue = BigDecimal.valueOf(value);
            if (decimalValue.divideAndRemainder(this.multipleOf)[1].abs().compareTo(BigDecimal.ZERO) > 0) {
                return new ValidateResult(false, "multipleOf not match, expect multipleOf %s, but %s", new Object[] { this.multipleOf, decimalValue });
            }
        }
        return NumberSchema.SUCCESS;
    }
    
    @Override
    public JSONObject toJSONObject() {
        final JSONObject object = JSONObject.of("type", (Object)"number");
        if (this.minimumLongValue != Long.MIN_VALUE) {
            ((HashMap<String, Long>)object).put(this.exclusiveMinimum ? "exclusiveMinimum" : "minimum", this.minimumLongValue);
        }
        else if (this.minimum != null) {
            ((HashMap<String, BigDecimal>)object).put(this.exclusiveMinimum ? "exclusiveMinimum" : "minimum", this.minimum);
        }
        if (this.maximumLongValue != Long.MIN_VALUE) {
            ((HashMap<String, Long>)object).put(this.exclusiveMaximum ? "exclusiveMaximum" : "maximum", this.minimumLongValue);
        }
        else if (this.maximum != null) {
            ((HashMap<String, BigDecimal>)object).put(this.exclusiveMaximum ? "exclusiveMaximum" : "maximum", this.maximum);
        }
        if (this.multipleOfLongValue != Long.MIN_VALUE) {
            ((HashMap<String, Long>)object).put("multipleOf", this.multipleOfLongValue);
        }
        else if (this.multipleOf != null) {
            ((HashMap<String, BigDecimal>)object).put("multipleOf", this.multipleOf);
        }
        return object;
    }
}
