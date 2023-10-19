// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input;

import java.io.Reader;
import java.util.Arrays;

public class LookaheadCharInputReader implements CharInputReader
{
    private final CharInputReader reader;
    private char[] lookahead;
    private int length;
    private int start;
    private final char newLine;
    private char delimiter;
    private final int whitespaceRangeStart;
    
    public LookaheadCharInputReader(final CharInputReader reader, final char newLine, final int whitespaceRangeStart) {
        this.lookahead = new char[0];
        this.length = 0;
        this.start = 0;
        this.reader = reader;
        this.newLine = newLine;
        this.whitespaceRangeStart = whitespaceRangeStart;
    }
    
    public boolean matches(final char current, final char[] sequence, final char wildcard) {
        if (sequence.length > this.length - this.start) {
            return false;
        }
        if (sequence[0] != current && sequence[0] != wildcard) {
            return false;
        }
        for (int i = 1; i < sequence.length; ++i) {
            final char ch = sequence[i];
            if (ch != wildcard && ch != this.lookahead[i - 1 + this.start]) {
                return false;
            }
        }
        return true;
    }
    
    public boolean matches(final char[] sequence, final char wildcard) {
        if (sequence.length > this.length - this.start) {
            return false;
        }
        for (int i = 0; i < sequence.length; ++i) {
            final char ch = sequence[i];
            if (ch != wildcard && sequence[i] != this.lookahead[i + this.start]) {
                return false;
            }
        }
        return true;
    }
    
    public String getLookahead() {
        if (this.start >= this.length) {
            return "";
        }
        return new String(this.lookahead, this.start, this.length);
    }
    
    public String getLookahead(final char current) {
        if (this.start >= this.length) {
            return String.valueOf(current);
        }
        return current + new String(this.lookahead, this.start, this.length - 1);
    }
    
    public void lookahead(int numberOfCharacters) {
        numberOfCharacters += this.length - this.start;
        if (this.lookahead.length < numberOfCharacters) {
            this.lookahead = Arrays.copyOf(this.lookahead, numberOfCharacters);
        }
        if (this.start >= this.length) {
            this.start = 0;
            this.length = 0;
        }
        try {
            numberOfCharacters -= this.length;
            while (numberOfCharacters-- > 0) {
                this.lookahead[this.length] = this.reader.nextChar();
                ++this.length;
            }
        }
        catch (EOFException ex) {}
    }
    
    @Override
    public void start(final Reader reader) {
        this.reader.start(reader);
    }
    
    @Override
    public void stop() {
        this.reader.stop();
    }
    
    @Override
    public char nextChar() {
        if (this.start >= this.length) {
            return this.reader.nextChar();
        }
        return this.lookahead[this.start++];
    }
    
    @Override
    public long charCount() {
        return this.reader.charCount();
    }
    
    @Override
    public long lineCount() {
        return this.reader.lineCount();
    }
    
    @Override
    public void skipLines(final long lineCount) {
        this.reader.skipLines(lineCount);
    }
    
    @Override
    public void enableNormalizeLineEndings(final boolean escaping) {
        this.reader.enableNormalizeLineEndings(escaping);
    }
    
    @Override
    public String readComment() {
        return this.reader.readComment();
    }
    
    @Override
    public char[] getLineSeparator() {
        return this.reader.getLineSeparator();
    }
    
    @Override
    public final char getChar() {
        if (this.start != 0 && this.start >= this.length) {
            return this.reader.getChar();
        }
        return this.lookahead[this.start - 1];
    }
    
    @Override
    public char skipWhitespace(char ch, final char stopChar1, final char stopChar2) {
        while (this.start < this.length && ch <= ' ' && ch != stopChar1 && ch != this.newLine && ch != stopChar2 && this.whitespaceRangeStart < ch) {
            ch = this.lookahead[this.start++];
        }
        return this.reader.skipWhitespace(ch, stopChar1, stopChar2);
    }
    
    @Override
    public String currentParsedContent() {
        return this.reader.currentParsedContent();
    }
    
    @Override
    public void markRecordStart() {
        this.reader.markRecordStart();
    }
    
    @Override
    public String getString(final char ch, final char stop, final boolean trim, final String nullValue, final int maxLength) {
        return this.reader.getString(ch, stop, trim, nullValue, maxLength);
    }
    
    @Override
    public String getQuotedString(final char quote, final char escape, final char escapeEscape, final int maxLength, final char stop1, final char stop2, final boolean keepQuotes, final boolean keepEscape, final boolean trimLeading, final boolean trimTrailing) {
        return this.reader.getQuotedString(quote, escape, escapeEscape, maxLength, stop1, stop2, keepQuotes, keepEscape, trimLeading, trimTrailing);
    }
    
    @Override
    public int currentParsedContentLength() {
        return this.reader.currentParsedContentLength();
    }
    
    @Override
    public boolean skipString(final char ch, final char stop) {
        return this.reader.skipString(ch, stop);
    }
    
    @Override
    public boolean skipQuotedString(final char quote, final char escape, final char stop1, final char stop2) {
        return this.reader.skipQuotedString(quote, escape, stop1, stop2);
    }
}
