// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

public class TrimConversion implements Conversion<String, String>
{
    private final int length;
    
    public TrimConversion() {
        this.length = -1;
    }
    
    public TrimConversion(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Maximum trim length must be positive");
        }
        this.length = length;
    }
    
    @Override
    public String execute(final String input) {
        if (input == null) {
            return null;
        }
        if (input.length() == 0) {
            return input;
        }
        if (this.length == -1) {
            return input.trim();
        }
        int begin;
        for (begin = 0; begin < input.length() && input.charAt(begin) <= ' '; ++begin) {}
        if (begin == input.length()) {
            return "";
        }
        int end = begin + ((this.length < input.length()) ? this.length : input.length()) - 1;
        if (end >= input.length()) {
            end = input.length() - 1;
        }
        while (input.charAt(end) <= ' ') {
            --end;
        }
        return input.substring(begin, end + 1);
    }
    
    @Override
    public String revert(final String input) {
        return this.execute(input);
    }
}
