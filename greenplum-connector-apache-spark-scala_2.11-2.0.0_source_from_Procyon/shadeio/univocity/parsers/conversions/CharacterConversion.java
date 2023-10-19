// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import shadeio.univocity.parsers.common.DataProcessingException;

public class CharacterConversion extends ObjectConversion<Character>
{
    public CharacterConversion() {
    }
    
    public CharacterConversion(final Character valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    @Override
    protected Character fromString(final String input) {
        if (input.length() != 1) {
            final DataProcessingException exception = new DataProcessingException("'{value}' is not a character");
            exception.setValue(input);
            throw exception;
        }
        return input.charAt(0);
    }
}
