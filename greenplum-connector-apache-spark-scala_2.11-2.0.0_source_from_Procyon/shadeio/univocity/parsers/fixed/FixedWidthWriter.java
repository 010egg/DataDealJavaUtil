// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

import shadeio.univocity.parsers.common.CommonWriterSettings;
import shadeio.univocity.parsers.common.TextWritingException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.io.File;
import java.io.Writer;
import shadeio.univocity.parsers.common.AbstractWriter;

public class FixedWidthWriter extends AbstractWriter<FixedWidthWriterSettings>
{
    private boolean ignoreLeading;
    private boolean ignoreTrailing;
    private int[] fieldLengths;
    private FieldAlignment[] fieldAlignments;
    private char[] fieldPaddings;
    private char padding;
    private char defaultPadding;
    private int length;
    private FieldAlignment alignment;
    private Lookup[] lookaheadFormats;
    private Lookup[] lookbehindFormats;
    private char[] lookupChars;
    private Lookup lookbehindFormat;
    private int[] rootLengths;
    private FieldAlignment[] rootAlignments;
    private boolean[] ignore;
    private boolean[] rootIgnore;
    private int ignoreCount;
    private char[] rootPaddings;
    private boolean defaultHeaderPadding;
    private FieldAlignment defaultHeaderAlignment;
    
    public FixedWidthWriter(final FixedWidthWriterSettings settings) {
        this((Writer)null, settings);
    }
    
    public FixedWidthWriter(final Writer writer, final FixedWidthWriterSettings settings) {
        super(writer, settings);
    }
    
    public FixedWidthWriter(final File file, final FixedWidthWriterSettings settings) {
        super(file, settings);
    }
    
    public FixedWidthWriter(final File file, final String encoding, final FixedWidthWriterSettings settings) {
        super(file, encoding, settings);
    }
    
    public FixedWidthWriter(final File file, final Charset encoding, final FixedWidthWriterSettings settings) {
        super(file, encoding, settings);
    }
    
    public FixedWidthWriter(final OutputStream output, final FixedWidthWriterSettings settings) {
        super(output, settings);
    }
    
    public FixedWidthWriter(final OutputStream output, final String encoding, final FixedWidthWriterSettings settings) {
        super(output, encoding, settings);
    }
    
    public FixedWidthWriter(final OutputStream output, final Charset encoding, final FixedWidthWriterSettings settings) {
        super(output, encoding, settings);
    }
    
    @Override
    protected final void initialize(final FixedWidthWriterSettings settings) {
        final FixedWidthFormat format = settings.getFormat();
        this.padding = format.getPadding();
        this.defaultPadding = this.padding;
        this.ignoreLeading = settings.getIgnoreLeadingWhitespaces();
        this.ignoreTrailing = settings.getIgnoreTrailingWhitespaces();
        this.fieldLengths = settings.getAllLengths();
        this.fieldAlignments = settings.getFieldAlignments();
        this.fieldPaddings = settings.getFieldPaddings();
        this.ignore = settings.getFieldsToIgnore();
        if (this.ignore != null) {
            for (int i = 0; i < this.ignore.length; ++i) {
                if (this.ignore[i]) {
                    ++this.ignoreCount;
                }
            }
        }
        this.lookaheadFormats = settings.getLookaheadFormats();
        this.lookbehindFormats = settings.getLookbehindFormats();
        this.defaultHeaderPadding = settings.getUseDefaultPaddingForHeaders();
        this.defaultHeaderAlignment = settings.getDefaultAlignmentForHeaders();
        super.enableNewlineAfterRecord(settings.getWriteLineSeparatorAfterRecord());
        if (this.lookaheadFormats != null || this.lookbehindFormats != null) {
            this.lookupChars = new char[Lookup.calculateMaxLookupLength(new Lookup[][] { this.lookaheadFormats, this.lookbehindFormats })];
            this.rootLengths = this.fieldLengths;
            this.rootAlignments = this.fieldAlignments;
            this.rootPaddings = this.fieldPaddings;
            this.rootIgnore = this.ignore;
        }
        else {
            this.lookupChars = null;
            this.rootLengths = null;
            this.rootAlignments = null;
            this.rootPaddings = null;
            this.rootIgnore = null;
        }
    }
    
    @Override
    protected void processRow(Object[] row) {
        if ((row.length > 0 && this.lookaheadFormats != null) || this.lookbehindFormats != null) {
            int dstBegin = 0;
            int len;
            for (int i = 0; i < row.length && dstBegin < this.lookupChars.length; dstBegin += len, ++i) {
                final String value = String.valueOf(row[i]);
                len = value.length();
                if (dstBegin + len > this.lookupChars.length) {
                    len = this.lookupChars.length - dstBegin;
                }
                value.getChars(0, len, this.lookupChars, dstBegin);
            }
            for (int i = this.lookupChars.length - 1; i > dstBegin; --i) {
                this.lookupChars[i] = '\0';
            }
            boolean matched = false;
            if (this.lookaheadFormats != null) {
                for (int j = 0; j < this.lookaheadFormats.length; ++j) {
                    if (this.lookaheadFormats[j].matches(this.lookupChars)) {
                        this.fieldLengths = this.lookaheadFormats[j].lengths;
                        this.fieldAlignments = this.lookaheadFormats[j].alignments;
                        this.fieldPaddings = this.lookaheadFormats[j].paddings;
                        this.ignore = this.lookaheadFormats[j].ignore;
                        matched = true;
                        break;
                    }
                }
                if (this.lookbehindFormats != null && matched) {
                    this.lookbehindFormat = null;
                    for (int j = 0; j < this.lookbehindFormats.length; ++j) {
                        if (this.lookbehindFormats[j].matches(this.lookupChars)) {
                            this.lookbehindFormat = this.lookbehindFormats[j];
                            break;
                        }
                    }
                }
            }
            else {
                for (int j = 0; j < this.lookbehindFormats.length; ++j) {
                    if (this.lookbehindFormats[j].matches(this.lookupChars)) {
                        this.lookbehindFormat = this.lookbehindFormats[j];
                        matched = true;
                        this.fieldLengths = this.rootLengths;
                        this.fieldAlignments = this.rootAlignments;
                        this.fieldPaddings = this.rootPaddings;
                        this.ignore = this.rootIgnore;
                        break;
                    }
                }
            }
            if (!matched) {
                if (this.lookbehindFormat == null) {
                    if (this.rootLengths == null) {
                        throw new TextWritingException("Cannot write with the given configuration. No default field lengths defined and no lookahead/lookbehind value match '" + new String(this.lookupChars) + '\'', this.getRecordCount(), row);
                    }
                    this.fieldLengths = this.rootLengths;
                    this.fieldAlignments = this.rootAlignments;
                    this.fieldPaddings = this.rootPaddings;
                    this.ignore = this.rootIgnore;
                }
                else {
                    this.fieldLengths = this.lookbehindFormat.lengths;
                    this.fieldAlignments = this.lookbehindFormat.alignments;
                    this.fieldPaddings = this.lookbehindFormat.paddings;
                    this.ignore = this.lookbehindFormat.ignore;
                }
            }
        }
        if (this.expandRows) {
            row = this.expand(row, this.fieldLengths.length - this.ignoreCount, null);
        }
        for (int lastIndex = (this.fieldLengths.length < row.length) ? this.fieldLengths.length : row.length, off = 0, j = 0; j < lastIndex + off; ++j) {
            this.length = this.fieldLengths[j];
            if (this.ignore[j]) {
                ++off;
                this.appender.fill(' ', this.length);
            }
            else {
                this.alignment = this.fieldAlignments[j];
                this.padding = this.fieldPaddings[j];
                if (this.writingHeaders) {
                    if (this.defaultHeaderPadding) {
                        this.padding = this.defaultPadding;
                    }
                    if (this.defaultHeaderAlignment != null) {
                        this.alignment = this.defaultHeaderAlignment;
                    }
                }
                final String nextElement = this.getStringValue(row[j - off]);
                this.processElement(nextElement);
                this.appendValueToRow();
            }
        }
    }
    
    private void append(final String element) {
        int start = 0;
        if (this.ignoreLeading) {
            start = AbstractWriter.skipLeadingWhitespace(this.whitespaceRangeStart, element);
        }
        final int padCount = this.alignment.calculatePadding(this.length, element.length() - start);
        this.length -= padCount;
        this.appender.fill(this.padding, padCount);
        if (this.ignoreTrailing) {
            int i = start;
            while (i < element.length() && this.length > 0) {
                while (i < element.length() && this.length-- > 0) {
                    final char nextChar = element.charAt(i);
                    this.appender.appendIgnoringWhitespace(nextChar);
                    ++i;
                }
                if (this.length == -1 && this.appender.whitespaceCount() > 0) {
                    for (int j = i; j < element.length(); ++j) {
                        if (element.charAt(j) > ' ') {
                            this.appender.resetWhitespaceCount();
                            break;
                        }
                    }
                    if (this.appender.whitespaceCount() > 0) {
                        this.length = 0;
                    }
                }
                this.length += this.appender.whitespaceCount();
                this.appendValueToRow();
            }
        }
        else {
            for (int i = start; i < element.length() && this.length-- > 0; ++i) {
                final char nextChar = element.charAt(i);
                this.appender.append(nextChar);
            }
        }
    }
    
    private void processElement(final String element) {
        if (element != null) {
            this.append(element);
        }
        this.appender.fill(this.padding, this.length);
    }
}
