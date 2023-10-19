// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input;

public class NoopCharAppender implements CharAppender
{
    private static final NoopCharAppender instance;
    
    public static CharAppender getInstance() {
        return NoopCharAppender.instance;
    }
    
    private NoopCharAppender() {
    }
    
    @Override
    public int length() {
        return -1;
    }
    
    @Override
    public String getAndReset() {
        return null;
    }
    
    @Override
    public void appendIgnoringWhitespace(final char ch) {
    }
    
    @Override
    public void append(final char ch) {
    }
    
    @Override
    public char[] getCharsAndReset() {
        return null;
    }
    
    @Override
    public int whitespaceCount() {
        return 0;
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public void resetWhitespaceCount() {
    }
    
    @Override
    public char[] getChars() {
        return null;
    }
    
    @Override
    public void fill(final char ch, final int length) {
    }
    
    @Override
    public void appendIgnoringPadding(final char ch, final char padding) {
    }
    
    @Override
    public void appendIgnoringWhitespaceAndPadding(final char ch, final char padding) {
    }
    
    @Override
    public void prepend(final char ch) {
    }
    
    @Override
    public void updateWhitespace() {
    }
    
    @Override
    public char appendUntil(char ch, final CharInput input, final char stop) {
        while (ch != stop) {
            ch = input.nextChar();
        }
        return ch;
    }
    
    @Override
    public final char appendUntil(char ch, final CharInput input, final char stop1, final char stop2) {
        while (ch != stop1 && ch != stop2) {
            ch = input.nextChar();
        }
        return ch;
    }
    
    @Override
    public final char appendUntil(char ch, final CharInput input, final char stop1, final char stop2, final char stop3) {
        while (ch != stop1 && ch != stop2 && ch != stop3) {
            ch = input.nextChar();
        }
        return ch;
    }
    
    @Override
    public void append(final char[] ch, final int from, final int length) {
    }
    
    @Override
    public void prepend(final char ch1, final char ch2) {
    }
    
    @Override
    public void prepend(final char[] chars) {
    }
    
    @Override
    public void append(final char[] ch) {
    }
    
    @Override
    public void append(final String string) {
    }
    
    @Override
    public void append(final String string, final int from, final int to) {
    }
    
    @Override
    public char charAt(final int i) {
        return '\0';
    }
    
    @Override
    public CharSequence subSequence(final int i, final int i1) {
        return null;
    }
    
    @Override
    public void append(final int ch) {
    }
    
    @Override
    public void append(final int[] ch) {
    }
    
    static {
        instance = new NoopCharAppender();
    }
}
