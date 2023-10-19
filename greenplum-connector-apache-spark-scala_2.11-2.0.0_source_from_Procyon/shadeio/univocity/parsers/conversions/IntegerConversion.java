// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class IntegerConversion extends ObjectConversion<Integer>
{
    public IntegerConversion() {
    }
    
    public IntegerConversion(final Integer valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected Integer fromString(final String input) {
        return Integer.valueOf(input);
    }
}
