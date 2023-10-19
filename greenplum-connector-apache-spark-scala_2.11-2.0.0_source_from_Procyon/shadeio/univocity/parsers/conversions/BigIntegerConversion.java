// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import java.math.BigInteger;

public class BigIntegerConversion extends ObjectConversion<BigInteger>
{
    public BigIntegerConversion() {
    }
    
    public BigIntegerConversion(final BigInteger valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected BigInteger fromString(final String input) {
        return new BigInteger(input);
    }
}
