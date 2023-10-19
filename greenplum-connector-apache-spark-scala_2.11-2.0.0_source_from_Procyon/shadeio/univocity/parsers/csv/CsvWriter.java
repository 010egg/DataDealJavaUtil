// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

import shadeio.univocity.parsers.common.CommonWriterSettings;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collections;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.io.File;
import java.io.Writer;
import shadeio.univocity.parsers.common.fields.FieldSelector;
import java.util.Set;
import shadeio.univocity.parsers.common.AbstractWriter;

public class CsvWriter extends AbstractWriter<CsvWriterSettings>
{
    private char separator;
    private char quoteChar;
    private char escapeChar;
    private char escapeEscape;
    private boolean ignoreLeading;
    private boolean ignoreTrailing;
    private boolean quoteAllFields;
    private boolean escapeUnquoted;
    private boolean inputNotEscaped;
    private char newLine;
    private boolean dontProcessNormalizedNewLines;
    private boolean[] quotationTriggers;
    private char maxTrigger;
    private Set<Integer> quotedColumns;
    private FieldSelector quotedFieldSelector;
    
    public CsvWriter(final CsvWriterSettings settings) {
        this((Writer)null, settings);
    }
    
    public CsvWriter(final Writer writer, final CsvWriterSettings settings) {
        super(writer, settings);
    }
    
    public CsvWriter(final File file, final CsvWriterSettings settings) {
        super(file, settings);
    }
    
    public CsvWriter(final File file, final String encoding, final CsvWriterSettings settings) {
        super(file, encoding, settings);
    }
    
    public CsvWriter(final File file, final Charset encoding, final CsvWriterSettings settings) {
        super(file, encoding, settings);
    }
    
    public CsvWriter(final OutputStream output, final CsvWriterSettings settings) {
        super(output, settings);
    }
    
    public CsvWriter(final OutputStream output, final String encoding, final CsvWriterSettings settings) {
        super(output, encoding, settings);
    }
    
    public CsvWriter(final OutputStream output, final Charset encoding, final CsvWriterSettings settings) {
        super(output, encoding, settings);
    }
    
    @Override
    protected final void initialize(final CsvWriterSettings settings) {
        final CsvFormat format = settings.getFormat();
        this.separator = format.getDelimiter();
        this.quoteChar = format.getQuote();
        this.escapeChar = format.getQuoteEscape();
        this.escapeEscape = settings.getFormat().getCharToEscapeQuoteEscaping();
        this.newLine = format.getNormalizedNewline();
        this.quoteAllFields = settings.getQuoteAllFields();
        this.ignoreLeading = settings.getIgnoreLeadingWhitespaces();
        this.ignoreTrailing = settings.getIgnoreTrailingWhitespaces();
        this.escapeUnquoted = settings.isEscapeUnquotedValues();
        this.inputNotEscaped = !settings.isInputEscaped();
        this.dontProcessNormalizedNewLines = !settings.isNormalizeLineEndingsWithinQuotes();
        this.quotationTriggers = null;
        this.quotedColumns = null;
        this.maxTrigger = '\0';
        this.quotedColumns = Collections.emptySet();
        this.quotedFieldSelector = settings.getQuotedFieldSelector();
        final int triggerCount = settings.getQuotationTriggers().length;
        final int offset = settings.isQuoteEscapingEnabled() ? 1 : 0;
        final char[] tmp = Arrays.copyOf(settings.getQuotationTriggers(), triggerCount + offset);
        if (offset == 1) {
            tmp[triggerCount] = this.quoteChar;
        }
        for (int i = 0; i < tmp.length; ++i) {
            if (this.maxTrigger < tmp[i]) {
                this.maxTrigger = tmp[i];
            }
        }
        if (this.maxTrigger != '\0') {
            ++this.maxTrigger;
            Arrays.fill(this.quotationTriggers = new boolean[this.maxTrigger], false);
            for (int i = 0; i < tmp.length; ++i) {
                this.quotationTriggers[tmp[i]] = true;
            }
        }
    }
    
    @Override
    protected void processRow(final Object[] row) {
        if (this.recordCount == 0L && this.quotedFieldSelector != null) {
            final int[] quotedIndexes = this.quotedFieldSelector.getFieldIndexes(this.headers);
            if (quotedIndexes.length > 0) {
                this.quotedColumns = new HashSet<Integer>();
                for (final int idx : quotedIndexes) {
                    this.quotedColumns.add(idx);
                }
            }
        }
        for (int i = 0; i < row.length; ++i) {
            if (i != 0) {
                this.appendToRow(this.separator);
            }
            if (this.dontProcessNormalizedNewLines) {
                this.appender.enableDenormalizedLineEndings(false);
            }
            final String nextElement = this.getStringValue(row[i]);
            final int originalLength = this.appender.length();
            final boolean isElementQuoted = this.append(this.quoteAllFields || this.quotedColumns.contains(i), nextElement);
            if (this.appender.length() == originalLength && !this.usingNullOrEmptyValue) {
                if (isElementQuoted) {
                    if (nextElement == null) {
                        this.append(false, this.nullValue);
                    }
                    else {
                        this.append(true, this.emptyValue);
                    }
                }
                else if (nextElement == null) {
                    this.append(false, this.nullValue);
                }
                else {
                    this.append(false, this.emptyValue);
                }
            }
            if (isElementQuoted) {
                this.appendToRow(this.quoteChar);
                this.appendValueToRow();
                this.appendToRow(this.quoteChar);
                if (this.dontProcessNormalizedNewLines) {
                    this.appender.enableDenormalizedLineEndings(true);
                }
            }
            else {
                this.appendValueToRow();
            }
        }
    }
    
    private boolean quoteElement(final int start, final String element) {
        final int length = element.length();
        if (this.maxTrigger == '\0') {
            for (int i = start; i < length; ++i) {
                final char nextChar = element.charAt(i);
                if (nextChar == this.separator || nextChar == this.newLine) {
                    return true;
                }
            }
        }
        else {
            for (int i = start; i < length; ++i) {
                final char nextChar = element.charAt(i);
                if (nextChar == this.separator || nextChar == this.newLine || (nextChar < this.maxTrigger && this.quotationTriggers[nextChar])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean append(boolean isElementQuoted, String element) {
        if (element == null) {
            if (this.nullValue == null) {
                return isElementQuoted;
            }
            element = this.nullValue;
        }
        int start = 0;
        if (this.ignoreLeading) {
            start = AbstractWriter.skipLeadingWhitespace(this.whitespaceRangeStart, element);
        }
        final int length = element.length();
        if (start < length && element.charAt(start) == this.quoteChar) {
            isElementQuoted = true;
        }
        if (!isElementQuoted) {
            int i = start;
            char ch = '\0';
            while (i < length) {
                ch = element.charAt(i);
                if (ch == this.quoteChar || ch == this.separator || ch == this.newLine || ch == this.escapeChar || (ch < this.maxTrigger && this.quotationTriggers[ch])) {
                    this.appender.append(element, start, i);
                    start = i + 1;
                    if (ch == this.quoteChar || ch == this.escapeChar) {
                        if (this.quoteElement(i, element)) {
                            this.appendQuoted(i, element);
                            return true;
                        }
                        if (this.escapeUnquoted) {
                            this.appendQuoted(i, element);
                        }
                        else {
                            this.appender.append(element, i, length);
                            if (this.ignoreTrailing && element.charAt(length - 1) <= ' ' && this.whitespaceRangeStart < element.charAt(length - 1)) {
                                this.appender.updateWhitespace();
                            }
                        }
                        return isElementQuoted;
                    }
                    else {
                        if (ch == this.escapeChar && this.inputNotEscaped && this.escapeEscape != '\0' && this.escapeUnquoted) {
                            this.appender.append(this.escapeEscape);
                        }
                        else if (ch == this.separator || ch == this.newLine || (ch < this.maxTrigger && this.quotationTriggers[ch])) {
                            this.appendQuoted(i, element);
                            return true;
                        }
                        this.appender.append(ch);
                    }
                }
                ++i;
            }
            this.appender.append(element, start, i);
            if (ch <= ' ' && this.ignoreTrailing && this.whitespaceRangeStart < ch) {
                this.appender.updateWhitespace();
            }
            return isElementQuoted;
        }
        if (!this.usingNullOrEmptyValue || length < 2) {
            this.appendQuoted(start, element);
            return true;
        }
        if (element.charAt(0) == this.quoteChar && element.charAt(length - 1) == this.quoteChar) {
            this.appender.append(element);
            return false;
        }
        this.appendQuoted(start, element);
        return true;
    }
    
    private void appendQuoted(int start, final String element) {
        final int length = element.length();
        int i = start;
        char ch = '\0';
        while (i < length) {
            ch = element.charAt(i);
            if (ch == this.quoteChar || ch == this.newLine || ch == this.escapeChar) {
                this.appender.append(element, start, i);
                start = i + 1;
                if (ch == this.quoteChar && this.inputNotEscaped) {
                    this.appender.append(this.escapeChar);
                }
                else if (ch == this.escapeChar && this.inputNotEscaped && this.escapeEscape != '\0') {
                    this.appender.append(this.escapeEscape);
                }
                this.appender.append(ch);
            }
            ++i;
        }
        this.appender.append(element, start, i);
        if (ch <= ' ' && this.ignoreTrailing && this.whitespaceRangeStart < ch) {
            this.appender.updateWhitespace();
        }
    }
}
