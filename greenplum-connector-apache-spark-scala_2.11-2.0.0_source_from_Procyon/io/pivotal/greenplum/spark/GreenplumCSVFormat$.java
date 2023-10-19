// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import shadeio.univocity.parsers.csv.UnescapedQuoteHandling;
import shadeio.univocity.parsers.csv.CsvFormat;
import scala.runtime.BoxedUnit;
import shadeio.univocity.parsers.csv.CsvParserSettings;
import scala.runtime.Null$;

public final class GreenplumCSVFormat$
{
    public static final GreenplumCSVFormat$ MODULE$;
    private final char ESCAPE;
    private final char CHAR_DELIMITER;
    private final String NEWLINE;
    private final char QUOTE;
    private final String QUOTE_STRING;
    private final String VALUE_OF_NULL;
    private final String EMPTY_VALUE;
    private final Null$ NULL_VALUE;
    private final String DEFAULT_ENCODING;
    private final boolean STRICT_QUOTES;
    private final boolean IGNORE_WHITESPACE;
    private final String ESCAPED_QUOTE;
    private final int inputBufferSize;
    private final int maxCharsPerColumn;
    private final int maxErrorContentLength;
    private CsvParserSettings DEFAULT;
    private volatile boolean bitmap$0;
    
    static {
        new GreenplumCSVFormat$();
    }
    
    private CsvParserSettings DEFAULT$lzycompute() {
        synchronized (this) {
            if (!this.bitmap$0) {
                this.DEFAULT = this.getDefault();
                this.bitmap$0 = true;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.DEFAULT;
        }
    }
    
    public char ESCAPE() {
        return this.ESCAPE;
    }
    
    public char CHAR_DELIMITER() {
        return this.CHAR_DELIMITER;
    }
    
    public String NEWLINE() {
        return this.NEWLINE;
    }
    
    public char QUOTE() {
        return this.QUOTE;
    }
    
    public String QUOTE_STRING() {
        return this.QUOTE_STRING;
    }
    
    public String VALUE_OF_NULL() {
        return this.VALUE_OF_NULL;
    }
    
    public String EMPTY_VALUE() {
        return this.EMPTY_VALUE;
    }
    
    public Null$ NULL_VALUE() {
        final Null$ null_VALUE = this.NULL_VALUE;
        return null;
    }
    
    public String DEFAULT_ENCODING() {
        return this.DEFAULT_ENCODING;
    }
    
    public boolean STRICT_QUOTES() {
        return this.STRICT_QUOTES;
    }
    
    public boolean IGNORE_WHITESPACE() {
        return this.IGNORE_WHITESPACE;
    }
    
    public String ESCAPED_QUOTE() {
        return this.ESCAPED_QUOTE;
    }
    
    public int inputBufferSize() {
        return this.inputBufferSize;
    }
    
    public int maxCharsPerColumn() {
        return this.maxCharsPerColumn;
    }
    
    public int maxErrorContentLength() {
        return this.maxErrorContentLength;
    }
    
    public CsvParserSettings DEFAULT() {
        return this.bitmap$0 ? this.DEFAULT : this.DEFAULT$lzycompute();
    }
    
    private CsvParserSettings getDefault() {
        final CsvParserSettings settings = new CsvParserSettings();
        final CsvFormat format = settings.getFormat();
        final CsvParserSettings csvParserSettings = settings;
        this.NULL_VALUE();
        csvParserSettings.setNullValue(null);
        settings.setEmptyValue(this.EMPTY_VALUE());
        format.setDelimiter(this.CHAR_DELIMITER());
        format.setQuote(this.QUOTE());
        format.setQuoteEscape(this.QUOTE());
        settings.setIgnoreLeadingWhitespacesInQuotes(this.IGNORE_WHITESPACE());
        settings.setIgnoreTrailingWhitespacesInQuotes(this.IGNORE_WHITESPACE());
        settings.setIgnoreLeadingWhitespaces(this.IGNORE_WHITESPACE());
        settings.setIgnoreTrailingWhitespaces(this.IGNORE_WHITESPACE());
        settings.setReadInputOnSeparateThread(false);
        settings.setInputBufferSize(this.inputBufferSize());
        settings.setMaxCharsPerColumn(this.maxCharsPerColumn());
        settings.setUnescapedQuoteHandling(UnescapedQuoteHandling.STOP_AT_DELIMITER);
        settings.setLineSeparatorDetectionEnabled(true);
        return settings;
    }
    
    private GreenplumCSVFormat$() {
        MODULE$ = this;
        this.ESCAPE = '\\';
        this.CHAR_DELIMITER = '|';
        this.NEWLINE = "\n";
        this.QUOTE = '\"';
        this.QUOTE_STRING = "\"";
        this.VALUE_OF_NULL = "";
        this.EMPTY_VALUE = "";
        this.NULL_VALUE = null;
        this.DEFAULT_ENCODING = "UTF-8";
        this.STRICT_QUOTES = true;
        this.IGNORE_WHITESPACE = false;
        this.ESCAPED_QUOTE = "\"\"";
        this.inputBufferSize = 128;
        this.maxCharsPerColumn = -1;
        this.maxErrorContentLength = 1000;
    }
}
