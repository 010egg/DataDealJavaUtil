// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.common.fields.FieldIndexSelector;
import shadeio.univocity.parsers.common.input.NoopCharAppender;
import shadeio.univocity.parsers.common.fields.FieldSelector;
import java.util.Arrays;
import shadeio.univocity.parsers.common.input.CharAppender;

public class ParserOutput
{
    protected int column;
    protected final String[] parsedValues;
    private final CharAppender[] appenders;
    protected final CommonParserSettings<?> settings;
    private final boolean skipEmptyLines;
    private final String nullValue;
    public CharAppender appender;
    private final CharAppender appenderInstance;
    private boolean columnsToExtractInitialized;
    private boolean columnsReordered;
    private boolean columnReorderingEnabledSetting;
    private String[] headers;
    private int[] selectedIndexes;
    private long currentRecord;
    public boolean trim;
    String[] parsedHeaders;
    private final AbstractParser<?> parser;
    
    public ParserOutput(final CommonParserSettings<?> settings) {
        this(null, settings);
    }
    
    public ParserOutput(final AbstractParser<?> parser, final CommonParserSettings<?> settings) {
        this.column = 0;
        this.trim = false;
        this.parser = parser;
        this.appenderInstance = settings.newCharAppender();
        this.appender = this.appenderInstance;
        this.parsedValues = new String[settings.getMaxColumns()];
        Arrays.fill(this.appenders = new CharAppender[settings.getMaxColumns() + 1], this.appender);
        this.settings = settings;
        this.skipEmptyLines = settings.getSkipEmptyLines();
        this.nullValue = settings.getNullValue();
        this.columnsToExtractInitialized = false;
        this.currentRecord = 0L;
        if (settings.getHeaders() != null) {
            this.initializeHeaders();
        }
        this.columnReorderingEnabledSetting = settings.isColumnReorderingEnabled();
    }
    
    protected void initializeHeaders() {
        this.columnsReordered = false;
        this.selectedIndexes = null;
        this.appender = this.appenderInstance;
        Arrays.fill(this.appenders, this.appender);
        if (this.column > 0) {
            this.parsedHeaders = new String[this.column];
            System.arraycopy(this.parsedValues, 0, this.parsedHeaders, 0, this.column);
        }
        this.headers = this.settings.getHeaders();
        if (this.headers != null) {
            this.headers = this.headers.clone();
        }
        else if (this.column > 0) {
            this.headers = this.parsedHeaders.clone();
        }
        if (this.headers != null) {
            this.columnsToExtractInitialized = true;
            this.initializeColumnsToExtract(this.headers);
        }
    }
    
    public String[] rowParsed() {
        if (this.column > 0) {
            if (!this.columnsToExtractInitialized) {
                this.initializeHeaders();
                if (this.settings.isHeaderExtractionEnabled()) {
                    Arrays.fill(this.parsedValues, null);
                    this.column = 0;
                    this.appender = this.appenders[0];
                    return null;
                }
                if (!this.columnsReordered && this.selectedIndexes != null) {
                    final String[] out = new String[this.column];
                    for (int i = 0; i < this.selectedIndexes.length; ++i) {
                        final int index = this.selectedIndexes[i];
                        if (index < this.column) {
                            out[index] = this.parsedValues[index];
                        }
                    }
                    this.column = 0;
                    return out;
                }
            }
            ++this.currentRecord;
            if (!this.columnsReordered) {
                final int last = this.columnReorderingEnabledSetting ? this.column : ((this.column < this.headers.length) ? this.headers.length : this.column);
                final String[] out2 = new String[last];
                System.arraycopy(this.parsedValues, 0, out2, 0, this.column);
                this.column = 0;
                this.appender = this.appenders[0];
                return out2;
            }
            if (this.selectedIndexes.length == 0) {
                this.column = 0;
                return ArgumentUtils.EMPTY_STRING_ARRAY;
            }
            final String[] reorderedValues = new String[this.selectedIndexes.length];
            for (int i = 0; i < this.selectedIndexes.length; ++i) {
                final int index = this.selectedIndexes[i];
                if (index >= this.column || index == -1) {
                    reorderedValues[i] = this.nullValue;
                }
                else {
                    reorderedValues[i] = this.parsedValues[index];
                }
            }
            this.column = 0;
            this.appender = this.appenders[0];
            return reorderedValues;
        }
        else {
            if (this.skipEmptyLines) {
                return null;
            }
            if (!this.columnsToExtractInitialized) {
                this.initializeHeaders();
            }
            ++this.currentRecord;
            if (!this.columnsReordered) {
                return ArgumentUtils.EMPTY_STRING_ARRAY;
            }
            if (this.selectedIndexes.length == 0) {
                return ArgumentUtils.EMPTY_STRING_ARRAY;
            }
            final String[] out = new String[this.selectedIndexes.length];
            Arrays.fill(out, this.nullValue);
            return out;
        }
    }
    
    FieldSelector getFieldSelector() {
        return this.settings.getFieldSelector();
    }
    
    private void initializeColumnsToExtract(final String[] values) {
        final FieldSelector selector = this.settings.getFieldSelector();
        if (selector != null) {
            this.selectedIndexes = selector.getFieldIndexes(values);
            if (this.selectedIndexes != null) {
                Arrays.fill(this.appenders, NoopCharAppender.getInstance());
                for (int i = 0; i < this.selectedIndexes.length; ++i) {
                    final int index = this.selectedIndexes[i];
                    if (index != -1) {
                        this.appenders[index] = this.appender;
                    }
                }
                this.columnsReordered = this.settings.isColumnReorderingEnabled();
                if (!this.columnsReordered && values.length < this.appenders.length && !(selector instanceof FieldIndexSelector)) {
                    Arrays.fill(this.appenders, values.length, this.appenders.length, this.appender);
                }
                this.appender = this.appenders[0];
            }
        }
    }
    
    public String[] getHeaders() {
        if (this.parser != null) {
            this.parser.extractHeadersIfRequired();
        }
        return this.headers;
    }
    
    public int[] getSelectedIndexes() {
        return this.selectedIndexes;
    }
    
    public boolean isColumnReorderingEnabled() {
        return this.columnsReordered;
    }
    
    public int getCurrentColumn() {
        return this.column;
    }
    
    public void emptyParsed() {
        this.parsedValues[this.column++] = this.nullValue;
        this.appender = this.appenders[this.column];
    }
    
    public void valueParsed() {
        if (this.trim) {
            this.appender.updateWhitespace();
        }
        this.parsedValues[this.column++] = this.appender.getAndReset();
        this.appender = this.appenders[this.column];
    }
    
    public void valueParsed(final String value) {
        this.parsedValues[this.column++] = value;
        this.appender = this.appenders[this.column];
    }
    
    public long getCurrentRecord() {
        return this.currentRecord;
    }
    
    public final void discardValues() {
        this.column = 0;
        this.appender = this.appenders[0];
    }
    
    final void reset() {
        this.columnsToExtractInitialized = false;
        this.currentRecord = 0L;
        this.column = 0;
        this.headers = null;
    }
}
