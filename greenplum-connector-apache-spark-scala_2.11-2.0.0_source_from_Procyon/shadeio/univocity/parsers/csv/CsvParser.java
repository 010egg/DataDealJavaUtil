// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

import shadeio.univocity.parsers.common.CommonParserSettings;
import shadeio.univocity.parsers.common.input.InputAnalysisProcess;
import shadeio.univocity.parsers.common.TextParsingException;
import shadeio.univocity.parsers.common.input.NoopCharAppender;
import shadeio.univocity.parsers.common.input.CharInput;
import shadeio.univocity.parsers.common.input.EOFException;
import shadeio.univocity.parsers.common.input.ExpandingCharAppender;
import shadeio.univocity.parsers.common.input.DefaultCharAppender;
import shadeio.univocity.parsers.common.AbstractParser;

public final class CsvParser extends AbstractParser<CsvParserSettings>
{
    private final boolean ignoreTrailingWhitespace;
    private final boolean ignoreLeadingWhitespace;
    private boolean parseUnescapedQuotes;
    private boolean parseUnescapedQuotesUntilDelimiter;
    private final boolean doNotEscapeUnquotedValues;
    private final boolean keepEscape;
    private final boolean keepQuotes;
    private boolean unescaped;
    private char prev;
    private char delimiter;
    private char quote;
    private char quoteEscape;
    private char escapeEscape;
    private char newLine;
    private final DefaultCharAppender whitespaceAppender;
    private final boolean normalizeLineEndingsInQuotes;
    private UnescapedQuoteHandling quoteHandling;
    private final String nullValue;
    private final int maxColumnLength;
    private final String emptyValue;
    private final boolean trimQuotedLeading;
    private final boolean trimQuotedTrailing;
    
    public CsvParser(final CsvParserSettings settings) {
        super(settings);
        this.ignoreTrailingWhitespace = settings.getIgnoreTrailingWhitespaces();
        this.ignoreLeadingWhitespace = settings.getIgnoreLeadingWhitespaces();
        this.parseUnescapedQuotes = settings.isParseUnescapedQuotes();
        this.parseUnescapedQuotesUntilDelimiter = settings.isParseUnescapedQuotesUntilDelimiter();
        this.doNotEscapeUnquotedValues = !settings.isEscapeUnquotedValues();
        this.keepEscape = settings.isKeepEscapeSequences();
        this.keepQuotes = settings.getKeepQuotes();
        this.normalizeLineEndingsInQuotes = settings.isNormalizeLineEndingsWithinQuotes();
        this.nullValue = settings.getNullValue();
        this.emptyValue = settings.getEmptyValue();
        this.maxColumnLength = settings.getMaxCharsPerColumn();
        this.trimQuotedTrailing = settings.getIgnoreTrailingWhitespacesInQuotes();
        this.trimQuotedLeading = settings.getIgnoreLeadingWhitespacesInQuotes();
        this.updateFormat(settings.getFormat());
        this.whitespaceAppender = new ExpandingCharAppender(10, "", this.whitespaceRangeStart);
        this.quoteHandling = settings.getUnescapedQuoteHandling();
        if (this.quoteHandling == null) {
            if (this.parseUnescapedQuotes) {
                if (this.parseUnescapedQuotesUntilDelimiter) {
                    this.quoteHandling = UnescapedQuoteHandling.STOP_AT_DELIMITER;
                }
                else {
                    this.quoteHandling = UnescapedQuoteHandling.STOP_AT_CLOSING_QUOTE;
                }
            }
            else {
                this.quoteHandling = UnescapedQuoteHandling.RAISE_ERROR;
            }
        }
        else {
            this.parseUnescapedQuotesUntilDelimiter = (this.quoteHandling == UnescapedQuoteHandling.STOP_AT_DELIMITER || this.quoteHandling == UnescapedQuoteHandling.SKIP_VALUE);
            this.parseUnescapedQuotes = (this.quoteHandling != UnescapedQuoteHandling.RAISE_ERROR);
        }
    }
    
    @Override
    protected final void parseRecord() {
        if (this.ch <= ' ' && this.ignoreLeadingWhitespace && this.whitespaceRangeStart < this.ch) {
            this.ch = this.input.skipWhitespace(this.ch, this.delimiter, this.quote);
        }
        while (this.ch != this.newLine) {
            if (this.ch <= ' ' && this.ignoreLeadingWhitespace && this.whitespaceRangeStart < this.ch) {
                this.ch = this.input.skipWhitespace(this.ch, this.delimiter, this.quote);
            }
            if (this.ch == this.delimiter || this.ch == this.newLine) {
                this.output.emptyParsed();
            }
            else {
                this.unescaped = false;
                this.prev = '\0';
                if (this.ch == this.quote) {
                    this.input.enableNormalizeLineEndings(this.normalizeLineEndingsInQuotes);
                    final int len = this.output.appender.length();
                    if (len == 0) {
                        final String value = this.input.getQuotedString(this.quote, this.quoteEscape, this.escapeEscape, this.maxColumnLength, this.delimiter, this.newLine, this.keepQuotes, this.keepEscape, this.trimQuotedLeading, this.trimQuotedTrailing);
                        if (value != null) {
                            this.output.valueParsed((value == "") ? this.emptyValue : value);
                            try {
                                this.ch = this.input.nextChar();
                                if (this.ch != this.delimiter) {
                                    continue;
                                }
                                try {
                                    this.ch = this.input.nextChar();
                                    if (this.ch != this.newLine) {
                                        continue;
                                    }
                                    this.output.emptyParsed();
                                }
                                catch (EOFException e) {
                                    this.output.emptyParsed();
                                    return;
                                }
                                continue;
                            }
                            catch (EOFException e) {
                                return;
                            }
                        }
                    }
                    else if (len == -1 && this.input.skipQuotedString(this.quote, this.quoteEscape, this.delimiter, this.newLine)) {
                        this.output.valueParsed();
                        try {
                            this.ch = this.input.nextChar();
                            if (this.ch != this.delimiter) {
                                continue;
                            }
                            try {
                                this.ch = this.input.nextChar();
                                if (this.ch != this.newLine) {
                                    continue;
                                }
                                this.output.emptyParsed();
                            }
                            catch (EOFException e2) {
                                this.output.emptyParsed();
                                return;
                            }
                            continue;
                        }
                        catch (EOFException e2) {
                            return;
                        }
                    }
                    this.output.trim = this.trimQuotedTrailing;
                    this.parseQuotedValue();
                    this.input.enableNormalizeLineEndings(true);
                    this.output.valueParsed();
                }
                else if (this.doNotEscapeUnquotedValues) {
                    String value2 = null;
                    final int len2 = this.output.appender.length();
                    if (len2 == 0) {
                        value2 = this.input.getString(this.ch, this.delimiter, this.ignoreTrailingWhitespace, this.nullValue, this.maxColumnLength);
                    }
                    if (value2 != null) {
                        this.output.valueParsed(value2);
                        this.ch = this.input.getChar();
                    }
                    else {
                        if (len2 != -1) {
                            this.output.trim = this.ignoreTrailingWhitespace;
                            this.ch = this.output.appender.appendUntil(this.ch, this.input, this.delimiter, this.newLine);
                        }
                        else if (this.input.skipString(this.ch, this.delimiter)) {
                            this.ch = this.input.getChar();
                        }
                        else {
                            this.ch = this.output.appender.appendUntil(this.ch, this.input, this.delimiter, this.newLine);
                        }
                        this.output.valueParsed();
                    }
                }
                else {
                    this.output.trim = this.ignoreTrailingWhitespace;
                    this.parseValueProcessingEscape();
                    this.output.valueParsed();
                }
            }
            if (this.ch != this.newLine) {
                this.ch = this.input.nextChar();
                if (this.ch != this.newLine) {
                    continue;
                }
                this.output.emptyParsed();
            }
        }
    }
    
    private void skipValue() {
        this.output.appender.reset();
        this.ch = NoopCharAppender.getInstance().appendUntil(this.ch, this.input, this.delimiter, this.newLine);
    }
    
    private void handleValueSkipping(final boolean quoted) {
        switch (this.quoteHandling) {
            case SKIP_VALUE: {
                this.skipValue();
                break;
            }
            case RAISE_ERROR: {
                throw new TextParsingException(this.context, "Unescaped quote character '" + this.quote + "' inside " + (quoted ? "quoted" : "") + " value of CSV field. To allow unescaped quotes, set 'parseUnescapedQuotes' to 'true' in the CSV parser settings. Cannot parse CSV input.");
            }
        }
    }
    
    private void handleUnescapedQuoteInValue() {
        switch (this.quoteHandling) {
            case STOP_AT_CLOSING_QUOTE:
            case STOP_AT_DELIMITER: {
                this.output.appender.append(this.quote);
                this.prev = this.ch;
                this.parseValueProcessingEscape();
                break;
            }
            default: {
                this.handleValueSkipping(false);
                break;
            }
        }
    }
    
    private boolean handleUnescapedQuote() {
        this.unescaped = true;
        switch (this.quoteHandling) {
            case STOP_AT_CLOSING_QUOTE:
            case STOP_AT_DELIMITER: {
                this.output.appender.append(this.quote);
                this.output.appender.append(this.ch);
                this.prev = this.ch;
                this.parseQuotedValue();
                return true;
            }
            default: {
                this.handleValueSkipping(true);
                return false;
            }
        }
    }
    
    private void processQuoteEscape() {
        if (this.ch == this.quoteEscape && this.prev == this.escapeEscape && this.escapeEscape != '\0') {
            if (this.keepEscape) {
                this.output.appender.append(this.escapeEscape);
            }
            this.output.appender.append(this.quoteEscape);
            this.ch = '\0';
        }
        else if (this.prev == this.quoteEscape) {
            if (this.ch == this.quote) {
                if (this.keepEscape) {
                    this.output.appender.append(this.quoteEscape);
                }
                this.output.appender.append(this.quote);
                this.ch = '\0';
            }
            else {
                this.output.appender.append(this.prev);
            }
        }
        else if (this.ch == this.quote && this.prev == this.quote) {
            this.output.appender.append(this.quote);
        }
        else if (this.prev == this.quote) {
            this.handleUnescapedQuoteInValue();
        }
    }
    
    private void parseValueProcessingEscape() {
        while (this.ch != this.delimiter && this.ch != this.newLine) {
            if (this.ch != this.quote && this.ch != this.quoteEscape) {
                if (this.prev == this.quote) {
                    this.handleUnescapedQuoteInValue();
                    return;
                }
                this.output.appender.append(this.ch);
            }
            else {
                this.processQuoteEscape();
            }
            this.prev = this.ch;
            this.ch = this.input.nextChar();
        }
    }
    
    private void parseQuotedValue() {
        if (this.prev != '\0' && this.parseUnescapedQuotesUntilDelimiter) {
            if (this.quoteHandling == UnescapedQuoteHandling.SKIP_VALUE) {
                this.skipValue();
                return;
            }
            if (!this.keepQuotes) {
                this.output.appender.prepend(this.quote);
            }
            this.ch = this.input.nextChar();
            this.output.trim = this.ignoreTrailingWhitespace;
            this.ch = this.output.appender.appendUntil(this.ch, this.input, this.delimiter, this.newLine);
        }
        else {
            if (this.keepQuotes && this.prev == '\0') {
                this.output.appender.append(this.quote);
            }
            this.ch = this.input.nextChar();
            if (this.trimQuotedLeading && this.ch <= ' ' && this.output.appender.length() == 0) {
                while ((this.ch = this.input.nextChar()) <= ' ') {}
            }
            while (true) {
                if (this.prev == this.quote) {
                    if ((this.ch <= ' ' && this.whitespaceRangeStart < this.ch) || this.ch == this.delimiter) {
                        break;
                    }
                    if (this.ch == this.newLine) {
                        break;
                    }
                }
                if (this.ch != this.quote && this.ch != this.quoteEscape) {
                    if (this.prev == this.quote) {
                        if (!this.handleUnescapedQuote()) {
                            return;
                        }
                        if (this.quoteHandling == UnescapedQuoteHandling.SKIP_VALUE) {
                            break;
                        }
                        return;
                    }
                    else {
                        if (this.prev == this.quoteEscape && this.quoteEscape != '\0') {
                            this.output.appender.append(this.quoteEscape);
                        }
                        this.ch = this.output.appender.appendUntil(this.ch, this.input, this.quote, this.quoteEscape, this.escapeEscape);
                        this.prev = this.ch;
                        this.ch = this.input.nextChar();
                    }
                }
                else {
                    this.processQuoteEscape();
                    this.prev = this.ch;
                    this.ch = this.input.nextChar();
                    if (this.unescaped && (this.ch == this.delimiter || this.ch == this.newLine)) {
                        return;
                    }
                    continue;
                }
            }
            if (this.ch != this.delimiter && this.ch != this.newLine && this.ch <= ' ' && this.whitespaceRangeStart < this.ch) {
                this.whitespaceAppender.reset();
                do {
                    this.whitespaceAppender.append(this.ch);
                    this.ch = this.input.nextChar();
                    if (this.ch == this.newLine) {
                        if (this.keepQuotes) {
                            this.output.appender.append(this.quote);
                        }
                        return;
                    }
                } while (this.ch <= ' ' && this.whitespaceRangeStart < this.ch);
                if (this.ch != this.delimiter && this.parseUnescapedQuotes) {
                    if (this.output.appender instanceof DefaultCharAppender) {
                        this.output.appender.append(this.quote);
                        ((DefaultCharAppender)this.output.appender).append(this.whitespaceAppender);
                    }
                    if (this.parseUnescapedQuotesUntilDelimiter || (this.ch != this.quote && this.ch != this.quoteEscape)) {
                        this.output.appender.append(this.ch);
                    }
                    this.prev = this.ch;
                    this.parseQuotedValue();
                }
                else if (this.keepQuotes) {
                    this.output.appender.append(this.quote);
                }
            }
            else if (this.keepQuotes) {
                this.output.appender.append(this.quote);
            }
            if (this.ch != this.delimiter && this.ch != this.newLine) {
                throw new TextParsingException(this.context, "Unexpected character '" + this.ch + "' following quoted value of CSV field. Expecting '" + this.delimiter + "'. Cannot parse CSV input.");
            }
        }
    }
    
    @Override
    protected final InputAnalysisProcess getInputAnalysisProcess() {
        if (((CsvParserSettings)this.settings).isDelimiterDetectionEnabled() || ((CsvParserSettings)this.settings).isQuoteDetectionEnabled()) {
            return new CsvFormatDetector(20, (CsvParserSettings)this.settings, this.whitespaceRangeStart) {
                @Override
                void apply(final char delimiter, final char quote, final char quoteEscape) {
                    if (((CsvParserSettings)CsvParser.this.settings).isDelimiterDetectionEnabled()) {
                        CsvParser.this.delimiter = delimiter;
                    }
                    if (((CsvParserSettings)CsvParser.this.settings).isQuoteDetectionEnabled()) {
                        CsvParser.this.quote = quote;
                        CsvParser.this.quoteEscape = quoteEscape;
                    }
                }
            };
        }
        return null;
    }
    
    public final CsvFormat getDetectedFormat() {
        CsvFormat out = null;
        if (((CsvParserSettings)this.settings).isDelimiterDetectionEnabled()) {
            out = ((CsvParserSettings)this.settings).getFormat().clone();
            out.setDelimiter(this.delimiter);
        }
        if (((CsvParserSettings)this.settings).isQuoteDetectionEnabled()) {
            out = ((out == null) ? ((CsvParserSettings)this.settings).getFormat().clone() : out);
            out.setQuote(this.quote);
            out.setQuoteEscape(this.quoteEscape);
        }
        if (((CsvParserSettings)this.settings).isLineSeparatorDetectionEnabled()) {
            out = ((out == null) ? ((CsvParserSettings)this.settings).getFormat().clone() : out);
            out.setLineSeparator(this.input.getLineSeparator());
        }
        return out;
    }
    
    @Override
    protected final boolean consumeValueOnEOF() {
        if (this.ch == this.quote) {
            if (this.prev == this.quote) {
                if (this.keepQuotes) {
                    this.output.appender.append(this.quote);
                }
                return true;
            }
            if (!this.unescaped) {
                this.output.appender.append(this.quote);
            }
        }
        return this.prev != '\0' && this.ch != this.delimiter && this.ch != this.newLine;
    }
    
    public final void updateFormat(final CsvFormat format) {
        this.delimiter = format.getDelimiter();
        this.quote = format.getQuote();
        this.quoteEscape = format.getQuoteEscape();
        this.escapeEscape = format.getCharToEscapeQuoteEscaping();
        this.newLine = format.getNormalizedNewline();
    }
}
