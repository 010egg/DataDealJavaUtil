// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class RegexConversion implements Conversion<String, String>
{
    private final String replaceRegex;
    private final String replacement;
    
    public RegexConversion(final String replaceRegex, final String replacement) {
        this.replaceRegex = replaceRegex;
        this.replacement = replacement;
    }
    
    @Override
    public String execute(final String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll(this.replaceRegex, this.replacement);
    }
    
    @Override
    public String revert(final String input) {
        return this.execute(input);
    }
}
