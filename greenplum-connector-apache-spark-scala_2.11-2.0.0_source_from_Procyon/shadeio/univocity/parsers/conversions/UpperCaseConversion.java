// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class UpperCaseConversion implements Conversion<String, String>
{
    @Override
    public String execute(final String input) {
        if (input == null) {
            return null;
        }
        return input.toUpperCase();
    }
    
    @Override
    public String revert(final String input) {
        return this.execute(input);
    }
}
