// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input;

public class DefaultCharAppender implements CharAppender
{
    final int whitespaceRangeStart;
    final char[] emptyChars;
    char[] chars;
    int index;
    final String emptyValue;
    int whitespaceCount;
    
    public DefaultCharAppender(final int maxLength, final String emptyValue, final int whitespaceRangeStart) {
        this.whitespaceRangeStart = whitespaceRangeStart;
        this.chars = new char[maxLength];
        this.emptyValue = emptyValue;
        if (emptyValue == null) {
            this.emptyChars = null;
        }
        else {
            this.emptyChars = emptyValue.toCharArray();
        }
    }
    
    @Override
    public void appendIgnoringPadding(final char ch, final char padding) {
        this.chars[this.index++] = ch;
        if (ch == padding) {
            ++this.whitespaceCount;
        }
        else {
            this.whitespaceCount = 0;
        }
    }
    
    @Override
    public void appendIgnoringWhitespaceAndPadding(final char ch, final char padding) {
        this.chars[this.index++] = ch;
        if (ch == padding || (ch <= ' ' && this.whitespaceRangeStart < ch)) {
            ++this.whitespaceCount;
        }
        else {
            this.whitespaceCount = 0;
        }
    }
    
    @Override
    public void appendIgnoringWhitespace(final char ch) {
        this.chars[this.index++] = ch;
        if (ch <= ' ' && this.whitespaceRangeStart < ch) {
            ++this.whitespaceCount;
        }
        else {
            this.whitespaceCount = 0;
        }
    }
    
    @Override
    public void append(final char ch) {
        this.chars[this.index++] = ch;
    }
    
    @Override
    public final void append(final int ch) {
        if (ch < 65536) {
            this.append((char)ch);
        }
        else {
            final int off = ch - 65536;
            this.append((char)((off >>> 10) + 55296));
            this.append((char)((off & 0x3FF) + 56320));
        }
    }
    
    @Override
    public final void append(final int[] ch) {
        for (int i = 0; i < ch.length; ++i) {
            this.append(ch[i]);
        }
    }
    
    @Override
    public String getAndReset() {
        String out = this.emptyValue;
        if (this.index > this.whitespaceCount) {
            out = new String(this.chars, 0, this.index - this.whitespaceCount);
        }
        this.index = 0;
        this.whitespaceCount = 0;
        return out;
    }
    
    @Override
    public final String toString() {
        if (this.index <= this.whitespaceCount) {
            return this.emptyValue;
        }
        return new String(this.chars, 0, this.index - this.whitespaceCount);
    }
    
    @Override
    public final int length() {
        return this.index - this.whitespaceCount;
    }
    
    @Override
    public char[] getCharsAndReset() {
        char[] out = this.emptyChars;
        if (this.index > this.whitespaceCount) {
            final int length = this.index - this.whitespaceCount;
            out = new char[length];
            System.arraycopy(this.chars, 0, out, 0, length);
        }
        this.index = 0;
        this.whitespaceCount = 0;
        return out;
    }
    
    @Override
    public final int whitespaceCount() {
        return this.whitespaceCount;
    }
    
    @Override
    public void reset() {
        this.index = 0;
        this.whitespaceCount = 0;
    }
    
    public void append(final DefaultCharAppender appender) {
        System.arraycopy(appender.chars, 0, this.chars, this.index, appender.index - appender.whitespaceCount);
        this.index += appender.index - appender.whitespaceCount;
        appender.reset();
    }
    
    @Override
    public final void resetWhitespaceCount() {
        this.whitespaceCount = 0;
    }
    
    @Override
    public final char[] getChars() {
        return this.chars;
    }
    
    @Override
    public void fill(final char ch, final int length) {
        for (int i = 0; i < length; ++i) {
            this.chars[this.index++] = ch;
        }
    }
    
    @Override
    public void prepend(final char ch) {
        System.arraycopy(this.chars, 0, this.chars, 1, this.index);
        this.chars[0] = ch;
        ++this.index;
    }
    
    @Override
    public void prepend(final char ch1, final char ch2) {
        System.arraycopy(this.chars, 0, this.chars, 2, this.index);
        this.chars[0] = ch1;
        this.chars[1] = ch2;
        this.index += 2;
    }
    
    @Override
    public void prepend(final char[] chars) {
        System.arraycopy(this.chars, 0, this.chars, chars.length, this.index);
        System.arraycopy(chars, 0, this.chars, 0, chars.length);
        this.index += chars.length;
    }
    
    @Override
    public final void updateWhitespace() {
        this.whitespaceCount = 0;
        for (int i = this.index - 1; i >= 0 && this.chars[i] <= ' ' && this.whitespaceRangeStart < this.chars[i]; --i, ++this.whitespaceCount) {}
    }
    
    @Override
    public char appendUntil(char ch, final CharInput input, final char stop) {
        while (ch != stop) {
            this.chars[this.index++] = ch;
            ch = input.nextChar();
        }
        return ch;
    }
    
    @Override
    public char appendUntil(char ch, final CharInput input, final char stop1, final char stop2) {
        while (ch != stop1 && ch != stop2) {
            this.chars[this.index++] = ch;
            ch = input.nextChar();
        }
        return ch;
    }
    
    @Override
    public char appendUntil(char ch, final CharInput input, final char stop1, final char stop2, final char stop3) {
        while (ch != stop1 && ch != stop2 && ch != stop3) {
            this.chars[this.index++] = ch;
            ch = input.nextChar();
        }
        return ch;
    }
    
    @Override
    public void append(final char[] ch, final int from, final int length) {
        System.arraycopy(ch, from, this.chars, this.index, length);
        this.index += length;
    }
    
    @Override
    public final void append(final char[] ch) {
        this.append(ch, 0, ch.length);
    }
    
    @Override
    public void append(final String string, final int from, final int to) {
        string.getChars(from, to, this.chars, this.index);
        this.index += to - from;
    }
    
    @Override
    public final void append(final String string) {
        this.append(string, 0, string.length());
    }
    
    @Override
    public final char charAt(final int i) {
        return this.chars[i];
    }
    
    @Override
    public final String subSequence(final int from, final int to) {
        return new String(this.chars, from, to - from);
    }
}
