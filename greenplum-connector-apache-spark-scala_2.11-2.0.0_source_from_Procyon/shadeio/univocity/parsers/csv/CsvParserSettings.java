// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

import shadeio.univocity.parsers.common.Format;
import shadeio.univocity.parsers.common.CommonSettings;
import java.util.Arrays;
import java.util.Map;
import shadeio.univocity.parsers.common.input.ExpandingCharAppender;
import shadeio.univocity.parsers.common.input.DefaultCharAppender;
import shadeio.univocity.parsers.common.input.CharAppender;
import shadeio.univocity.parsers.common.CommonParserSettings;

public class CsvParserSettings extends CommonParserSettings<CsvFormat>
{
    private String emptyValue;
    private boolean parseUnescapedQuotes;
    private boolean parseUnescapedQuotesUntilDelimiter;
    private boolean escapeUnquotedValues;
    private boolean keepEscapeSequences;
    private boolean keepQuotes;
    private boolean normalizeLineEndingsWithinQuotes;
    private boolean ignoreTrailingWhitespacesInQuotes;
    private boolean ignoreLeadingWhitespacesInQuotes;
    private boolean delimiterDetectionEnabled;
    private boolean quoteDetectionEnabled;
    private UnescapedQuoteHandling unescapedQuoteHandling;
    private char[] delimitersForDetection;
    
    public CsvParserSettings() {
        this.emptyValue = null;
        this.parseUnescapedQuotes = true;
        this.parseUnescapedQuotesUntilDelimiter = true;
        this.escapeUnquotedValues = false;
        this.keepEscapeSequences = false;
        this.keepQuotes = false;
        this.normalizeLineEndingsWithinQuotes = true;
        this.ignoreTrailingWhitespacesInQuotes = false;
        this.ignoreLeadingWhitespacesInQuotes = false;
        this.delimiterDetectionEnabled = false;
        this.quoteDetectionEnabled = false;
        this.unescapedQuoteHandling = null;
        this.delimitersForDetection = null;
    }
    
    public String getEmptyValue() {
        return this.emptyValue;
    }
    
    public void setEmptyValue(final String emptyValue) {
        this.emptyValue = emptyValue;
    }
    
    @Override
    protected CharAppender newCharAppender() {
        final int chars = this.getMaxCharsPerColumn();
        if (chars != -1) {
            return new DefaultCharAppender(chars, this.emptyValue, this.getWhitespaceRangeStart());
        }
        return new ExpandingCharAppender(this.emptyValue, this.getWhitespaceRangeStart());
    }
    
    @Override
    protected CsvFormat createDefaultFormat() {
        return new CsvFormat();
    }
    
    @Deprecated
    public boolean isParseUnescapedQuotes() {
        return this.parseUnescapedQuotes || (this.unescapedQuoteHandling != null && this.unescapedQuoteHandling != UnescapedQuoteHandling.RAISE_ERROR);
    }
    
    @Deprecated
    public void setParseUnescapedQuotes(final boolean parseUnescapedQuotes) {
        this.parseUnescapedQuotes = parseUnescapedQuotes;
    }
    
    @Deprecated
    public void setParseUnescapedQuotesUntilDelimiter(final boolean parseUnescapedQuotesUntilDelimiter) {
        if (parseUnescapedQuotesUntilDelimiter) {
            this.parseUnescapedQuotes = true;
        }
        this.parseUnescapedQuotesUntilDelimiter = parseUnescapedQuotesUntilDelimiter;
    }
    
    @Deprecated
    public boolean isParseUnescapedQuotesUntilDelimiter() {
        return (this.parseUnescapedQuotesUntilDelimiter && this.isParseUnescapedQuotes()) || this.unescapedQuoteHandling == UnescapedQuoteHandling.STOP_AT_DELIMITER || this.unescapedQuoteHandling == UnescapedQuoteHandling.SKIP_VALUE;
    }
    
    public boolean isEscapeUnquotedValues() {
        return this.escapeUnquotedValues;
    }
    
    public void setEscapeUnquotedValues(final boolean escapeUnquotedValues) {
        this.escapeUnquotedValues = escapeUnquotedValues;
    }
    
    public final boolean isKeepEscapeSequences() {
        return this.keepEscapeSequences;
    }
    
    public final void setKeepEscapeSequences(final boolean keepEscapeSequences) {
        this.keepEscapeSequences = keepEscapeSequences;
    }
    
    public final boolean isDelimiterDetectionEnabled() {
        return this.delimiterDetectionEnabled;
    }
    
    public final void setDelimiterDetectionEnabled(final boolean separatorDetectionEnabled, final char... delimitersForDetection) {
        this.delimiterDetectionEnabled = separatorDetectionEnabled;
        this.delimitersForDetection = delimitersForDetection;
    }
    
    public final boolean isQuoteDetectionEnabled() {
        return this.quoteDetectionEnabled;
    }
    
    public final void setQuoteDetectionEnabled(final boolean quoteDetectionEnabled) {
        this.quoteDetectionEnabled = quoteDetectionEnabled;
    }
    
    public final void detectFormatAutomatically(final char... delimitersForDetection) {
        this.setDelimiterDetectionEnabled(true, delimitersForDetection);
        this.setQuoteDetectionEnabled(true);
        this.setLineSeparatorDetectionEnabled(true);
    }
    
    public boolean isNormalizeLineEndingsWithinQuotes() {
        return this.normalizeLineEndingsWithinQuotes;
    }
    
    public void setNormalizeLineEndingsWithinQuotes(final boolean normalizeLineEndingsWithinQuotes) {
        this.normalizeLineEndingsWithinQuotes = normalizeLineEndingsWithinQuotes;
    }
    
    public void setUnescapedQuoteHandling(final UnescapedQuoteHandling unescapedQuoteHandling) {
        this.unescapedQuoteHandling = unescapedQuoteHandling;
    }
    
    public UnescapedQuoteHandling getUnescapedQuoteHandling() {
        return this.unescapedQuoteHandling;
    }
    
    public boolean getKeepQuotes() {
        return this.keepQuotes;
    }
    
    public void setKeepQuotes(final boolean keepQuotes) {
        this.keepQuotes = keepQuotes;
    }
    
    @Override
    protected void addConfiguration(final Map<String, Object> out) {
        super.addConfiguration(out);
        out.put("Empty value", this.emptyValue);
        out.put("Unescaped quote handling", this.unescapedQuoteHandling);
        out.put("Escape unquoted values", this.escapeUnquotedValues);
        out.put("Keep escape sequences", this.keepEscapeSequences);
        out.put("Keep quotes", this.keepQuotes);
        out.put("Normalize escaped line separators", this.normalizeLineEndingsWithinQuotes);
        out.put("Autodetect column delimiter", this.delimiterDetectionEnabled);
        out.put("Autodetect quotes", this.quoteDetectionEnabled);
        out.put("Delimiters for detection", Arrays.toString(this.delimitersForDetection));
        out.put("Ignore leading whitespaces in quotes", this.ignoreLeadingWhitespacesInQuotes);
        out.put("Ignore trailing whitespaces in quotes", this.ignoreTrailingWhitespacesInQuotes);
    }
    
    public final CsvParserSettings clone() {
        return (CsvParserSettings)super.clone();
    }
    
    public final CsvParserSettings clone(final boolean clearInputSpecificSettings) {
        return (CsvParserSettings)super.clone(clearInputSpecificSettings);
    }
    
    public final char[] getDelimitersForDetection() {
        return this.delimitersForDetection;
    }
    
    public boolean getIgnoreTrailingWhitespacesInQuotes() {
        return this.ignoreTrailingWhitespacesInQuotes;
    }
    
    public void setIgnoreTrailingWhitespacesInQuotes(final boolean ignoreTrailingWhitespacesInQuotes) {
        this.ignoreTrailingWhitespacesInQuotes = ignoreTrailingWhitespacesInQuotes;
    }
    
    public boolean getIgnoreLeadingWhitespacesInQuotes() {
        return this.ignoreLeadingWhitespacesInQuotes;
    }
    
    public void setIgnoreLeadingWhitespacesInQuotes(final boolean ignoreLeadingWhitespacesInQuotes) {
        this.ignoreLeadingWhitespacesInQuotes = ignoreLeadingWhitespacesInQuotes;
    }
    
    public final void trimQuotedValues(final boolean trim) {
        this.setIgnoreTrailingWhitespacesInQuotes(trim);
        this.setIgnoreLeadingWhitespacesInQuotes(trim);
    }
}
