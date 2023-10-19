// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import java.math.BigDecimal;

public class BigDecimalConversion extends ObjectConversion<BigDecimal>
{
    public BigDecimalConversion() {
    }
    
    public BigDecimalConversion(final BigDecimal valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected BigDecimal fromString(final String input) {
        return new BigDecimal(input);
    }
}
