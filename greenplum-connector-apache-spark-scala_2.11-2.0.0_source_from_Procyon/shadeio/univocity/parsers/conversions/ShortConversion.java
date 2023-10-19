// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class ShortConversion extends ObjectConversion<Short>
{
    public ShortConversion() {
    }
    
    public ShortConversion(final Short valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected Short fromString(final String input) {
        return Short.valueOf(input);
    }
}
