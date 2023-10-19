// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class DoubleConversion extends ObjectConversion<Double>
{
    public DoubleConversion() {
    }
    
    public DoubleConversion(final Double valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected Double fromString(final String input) {
        return Double.valueOf(input);
    }
}
