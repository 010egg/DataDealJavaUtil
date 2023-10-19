// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input;

public interface CharAppender extends CharSequence
{
    void appendIgnoringWhitespace(final char p0);
    
    void appendIgnoringPadding(final char p0, final char p1);
    
    void appendIgnoringWhitespaceAndPadding(final char p0, final char p1);
    
    void append(final char p0);
    
    void append(final int p0);
    
    int length();
    
    int whitespaceCount();
    
    void resetWhitespaceCount();
    
    String getAndReset();
    
    void reset();
    
    char[] getCharsAndReset();
    
    char[] getChars();
    
    void fill(final char p0, final int p1);
    
    void prepend(final char p0);
    
    void prepend(final char p0, final char p1);
    
    void prepend(final char[] p0);
    
    void updateWhitespace();
    
    char appendUntil(final char p0, final CharInput p1, final char p2);
    
    char appendUntil(final char p0, final CharInput p1, final char p2, final char p3);
    
    char appendUntil(final char p0, final CharInput p1, final char p2, final char p3, final char p4);
    
    void append(final char[] p0, final int p1, final int p2);
    
    void append(final char[] p0);
    
    void append(final int[] p0);
    
    void append(final String p0);
    
    void append(final String p0, final int p1, final int p2);
}
