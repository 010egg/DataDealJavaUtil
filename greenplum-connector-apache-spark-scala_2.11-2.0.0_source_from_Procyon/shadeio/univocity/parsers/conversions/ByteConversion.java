// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class ByteConversion extends ObjectConversion<Byte>
{
    public ByteConversion() {
    }
    
    public ByteConversion(final Byte valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected Byte fromString(final String input) {
        return Byte.valueOf(input);
    }
}
