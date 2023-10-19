// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class LongConversion extends ObjectConversion<Long>
{
    public LongConversion() {
    }
    
    public LongConversion(final Long valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected Long fromString(final String input) {
        return Long.valueOf(input);
    }
}
