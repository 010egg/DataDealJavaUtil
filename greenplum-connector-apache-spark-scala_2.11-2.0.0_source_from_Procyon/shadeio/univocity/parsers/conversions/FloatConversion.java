// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class FloatConversion extends ObjectConversion<Float>
{
    public FloatConversion() {
    }
    
    public FloatConversion(final Float valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected Float fromString(final String input) {
        return Float.valueOf(input);
    }
}
