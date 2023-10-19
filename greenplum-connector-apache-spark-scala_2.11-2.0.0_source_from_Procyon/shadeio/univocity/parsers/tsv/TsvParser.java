// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.tsv;

import shadeio.univocity.parsers.common.input.CharInput;
import shadeio.univocity.parsers.common.AbstractParser;

public class TsvParser extends AbstractParser<TsvParserSettings>
{
    private final boolean ignoreTrailingWhitespace;
    private final boolean ignoreLeadingWhitespace;
    private final boolean joinLines;
    private final char newLine;
    private final char escapeChar;
    private final char escapedTabChar;
    
    public TsvParser(final TsvParserSettings settings) {
        super(settings);
        this.ignoreTrailingWhitespace = settings.getIgnoreTrailingWhitespaces();
        this.ignoreLeadingWhitespace = settings.getIgnoreLeadingWhitespaces();
        this.joinLines = settings.isLineJoiningEnabled();
        final TsvFormat format = settings.getFormat();
        this.newLine = format.getNormalizedNewline();
        this.escapeChar = settings.getFormat().getEscapeChar();
        this.escapedTabChar = format.getEscapedTabChar();
    }
    
    @Override
    protected void initialize() {
        this.output.trim = this.ignoreTrailingWhitespace;
    }
    
    @Override
    protected void parseRecord() {
        if (this.ignoreLeadingWhitespace && this.ch != '\t' && this.ch <= ' ' && this.whitespaceRangeStart < this.ch) {
            this.ch = this.input.skipWhitespace(this.ch, '\t', this.escapeChar);
        }
        while (this.ch != this.newLine) {
            this.parseField();
            if (this.ch != this.newLine) {
                this.ch = this.input.nextChar();
                if (this.ch != this.newLine) {
                    continue;
                }
                this.output.emptyParsed();
            }
        }
    }
    
    private void parseField() {
        if (this.ignoreLeadingWhitespace && this.ch != '\t' && this.ch <= ' ' && this.whitespaceRangeStart < this.ch) {
            this.ch = this.input.skipWhitespace(this.ch, '\t', this.escapeChar);
        }
        if (this.ch == '\t') {
            this.output.emptyParsed();
        }
        else {
            while (this.ch != '\t' && this.ch != this.newLine) {
                if (this.ch == this.escapeChar) {
                    this.ch = this.input.nextChar();
                    if (this.ch == 't' || this.ch == this.escapedTabChar) {
                        this.output.appender.append('\t');
                    }
                    else if (this.ch == 'n') {
                        this.output.appender.append('\n');
                    }
                    else if (this.ch == '\\') {
                        this.output.appender.append('\\');
                    }
                    else if (this.ch == 'r') {
                        this.output.appender.append('\r');
                    }
                    else if (this.ch == this.newLine && this.joinLines) {
                        this.output.appender.append(this.newLine);
                    }
                    else {
                        this.output.appender.append(this.escapeChar);
                        if (this.ch == this.newLine) {
                            break;
                        }
                        if (this.ch == '\t') {
                            break;
                        }
                        this.output.appender.append(this.ch);
                    }
                    this.ch = this.input.nextChar();
                }
                else {
                    this.ch = this.output.appender.appendUntil(this.ch, this.input, '\t', this.escapeChar, this.newLine);
                }
            }
            this.output.valueParsed();
        }
    }
}
