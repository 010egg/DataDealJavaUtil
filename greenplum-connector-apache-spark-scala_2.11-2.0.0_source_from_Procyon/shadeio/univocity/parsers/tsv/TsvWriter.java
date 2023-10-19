// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.tsv;

import shadeio.univocity.parsers.common.CommonWriterSettings;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.io.File;
import java.io.Writer;
import shadeio.univocity.parsers.common.AbstractWriter;

public class TsvWriter extends AbstractWriter<TsvWriterSettings>
{
    private boolean ignoreLeading;
    private boolean ignoreTrailing;
    private boolean joinLines;
    private char escapeChar;
    private char escapedTabChar;
    private char newLine;
    
    public TsvWriter(final TsvWriterSettings settings) {
        this((Writer)null, settings);
    }
    
    public TsvWriter(final Writer writer, final TsvWriterSettings settings) {
        super(writer, settings);
    }
    
    public TsvWriter(final File file, final TsvWriterSettings settings) {
        super(file, settings);
    }
    
    public TsvWriter(final File file, final String encoding, final TsvWriterSettings settings) {
        super(file, encoding, settings);
    }
    
    public TsvWriter(final File file, final Charset encoding, final TsvWriterSettings settings) {
        super(file, encoding, settings);
    }
    
    public TsvWriter(final OutputStream output, final TsvWriterSettings settings) {
        super(output, settings);
    }
    
    public TsvWriter(final OutputStream output, final String encoding, final TsvWriterSettings settings) {
        super(output, encoding, settings);
    }
    
    public TsvWriter(final OutputStream output, final Charset encoding, final TsvWriterSettings settings) {
        super(output, encoding, settings);
    }
    
    @Override
    protected final void initialize(final TsvWriterSettings settings) {
        this.escapeChar = settings.getFormat().getEscapeChar();
        this.escapedTabChar = settings.getFormat().getEscapedTabChar();
        this.ignoreLeading = settings.getIgnoreLeadingWhitespaces();
        this.ignoreTrailing = settings.getIgnoreTrailingWhitespaces();
        this.joinLines = settings.isLineJoiningEnabled();
        this.newLine = settings.getFormat().getNormalizedNewline();
    }
    
    @Override
    protected void processRow(final Object[] row) {
        for (int i = 0; i < row.length; ++i) {
            if (i != 0) {
                this.appendToRow('\t');
            }
            final String nextElement = this.getStringValue(row[i]);
            final int originalLength = this.appender.length();
            this.append(nextElement);
            if (this.appender.length() == originalLength && this.nullValue != null && !this.nullValue.isEmpty()) {
                this.append(this.nullValue);
            }
            this.appendValueToRow();
        }
    }
    
    private void append(String element) {
        if (element == null) {
            element = this.nullValue;
        }
        if (element == null) {
            return;
        }
        int start = 0;
        if (this.ignoreLeading) {
            start = AbstractWriter.skipLeadingWhitespace(this.whitespaceRangeStart, element);
        }
        final int length = element.length();
        int i = start;
        char ch = '\0';
        while (i < length) {
            ch = element.charAt(i);
            if (ch == '\t' || ch == '\n' || ch == '\r' || ch == '\\') {
                this.appender.append(element, start, i);
                start = i + 1;
                this.appender.append(this.escapeChar);
                if (ch == '\t') {
                    this.appender.append(this.escapedTabChar);
                }
                else if (ch == '\n') {
                    this.appender.append(this.joinLines ? this.newLine : 'n');
                }
                else if (ch == '\\') {
                    this.appender.append('\\');
                }
                else {
                    this.appender.append(this.joinLines ? this.newLine : 'r');
                }
            }
            ++i;
        }
        this.appender.append(element, start, i);
        if (ch <= ' ' && this.ignoreTrailing && this.whitespaceRangeStart < ch) {
            this.appender.updateWhitespace();
        }
    }
}
