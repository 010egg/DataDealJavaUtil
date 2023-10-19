// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Map;
import shadeio.univocity.parsers.common.input.CharInputReader;

public class DefaultParsingContext extends DefaultContext implements ParsingContext
{
    private final CharInputReader input;
    private final AbstractParser<?> parser;
    
    public DefaultParsingContext(final AbstractParser<?> parser, final int errorContentLength) {
        super((parser == null) ? null : parser.output, errorContentLength);
        this.parser = parser;
        this.input = ((parser == null) ? null : parser.input);
    }
    
    @Override
    public long currentLine() {
        return this.input.lineCount();
    }
    
    @Override
    public long currentChar() {
        return this.input.charCount();
    }
    
    @Override
    public void skipLines(final long lines) {
        this.input.skipLines(lines);
    }
    
    @Override
    public String fieldContentOnError() {
        final char[] chars = this.output.appender.getChars();
        if (chars != null) {
            int length = this.output.appender.length();
            if (length > chars.length) {
                length = chars.length;
            }
            if (length > 0) {
                return new String(chars, 0, length);
            }
        }
        return null;
    }
    
    @Override
    public String currentParsedContent() {
        if (this.input != null) {
            return this.input.currentParsedContent();
        }
        return null;
    }
    
    @Override
    public int currentParsedContentLength() {
        if (this.input != null) {
            return this.input.currentParsedContentLength();
        }
        return 0;
    }
    
    @Override
    public Map<Long, String> comments() {
        return this.parser.getComments();
    }
    
    @Override
    public String lastComment() {
        return this.parser.getLastComment();
    }
    
    @Override
    public String[] parsedHeaders() {
        return this.parser.getParsedHeaders();
    }
    
    @Override
    public char[] lineSeparator() {
        return this.input.getLineSeparator();
    }
}
