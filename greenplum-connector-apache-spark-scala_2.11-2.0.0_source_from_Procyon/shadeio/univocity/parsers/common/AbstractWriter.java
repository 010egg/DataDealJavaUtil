// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collection;
import shadeio.univocity.parsers.common.input.DefaultCharAppender;
import java.util.Iterator;
import shadeio.univocity.parsers.common.fields.FieldSelector;
import shadeio.univocity.parsers.common.fields.FieldIndexSelector;
import shadeio.univocity.parsers.common.fields.ExcludeFieldNameSelector;
import shadeio.univocity.parsers.common.fields.FieldNameSelector;
import shadeio.univocity.parsers.common.processor.RowWriterProcessorSwitch;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.io.File;
import java.util.Map;
import shadeio.univocity.parsers.common.input.WriterCharAppender;
import java.io.Writer;
import shadeio.univocity.parsers.common.processor.RowWriterProcessor;

public abstract class AbstractWriter<S extends CommonWriterSettings<?>>
{
    private final RowWriterProcessor writerProcessor;
    private Writer writer;
    private final boolean skipEmptyLines;
    private final char comment;
    private final WriterCharAppender rowAppender;
    private final boolean isHeaderWritingEnabled;
    private Object[] outputRow;
    private int[] indexesToWrite;
    private final char[] lineSeparator;
    protected String[] headers;
    protected long recordCount;
    protected final String nullValue;
    protected final String emptyValue;
    protected final WriterCharAppender appender;
    private final Object[] partialLine;
    private int partialLineIndex;
    private Map<String[], Map<String, Integer>> headerIndexes;
    private int largestRowLength;
    protected boolean writingHeaders;
    private String[] dummyHeaderRow;
    protected boolean expandRows;
    private boolean usingSwitch;
    private boolean enableNewlineAfterRecord;
    protected boolean usingNullOrEmptyValue;
    protected final int whitespaceRangeStart;
    private final boolean columnReorderingEnabled;
    private final CommonSettings<DummyFormat> internalSettings;
    private final int errorContentLength;
    
    public AbstractWriter(final S settings) {
        this((Writer)null, settings);
    }
    
    public AbstractWriter(final File file, final S settings) {
        this(ArgumentUtils.newWriter(file), settings);
    }
    
    public AbstractWriter(final File file, final String encoding, final S settings) {
        this(ArgumentUtils.newWriter(file, encoding), settings);
    }
    
    public AbstractWriter(final File file, final Charset encoding, final S settings) {
        this(ArgumentUtils.newWriter(file, encoding), settings);
    }
    
    public AbstractWriter(final OutputStream output, final S settings) {
        this(ArgumentUtils.newWriter(output), settings);
    }
    
    public AbstractWriter(final OutputStream output, final String encoding, final S settings) {
        this(ArgumentUtils.newWriter(output, encoding), settings);
    }
    
    public AbstractWriter(final OutputStream output, final Charset encoding, final S settings) {
        this(ArgumentUtils.newWriter(output, encoding), settings);
    }
    
    public AbstractWriter(final Writer writer, final S settings) {
        this.recordCount = 0L;
        this.partialLineIndex = 0;
        this.largestRowLength = -1;
        this.writingHeaders = false;
        this.enableNewlineAfterRecord = true;
        this.internalSettings = new CommonSettings<DummyFormat>() {
            @Override
            protected DummyFormat createDefaultFormat() {
                return DummyFormat.instance;
            }
        };
        settings.autoConfigure();
        this.internalSettings.setMaxColumns(settings.getMaxColumns());
        this.errorContentLength = settings.getErrorContentLength();
        this.nullValue = settings.getNullValue();
        this.emptyValue = settings.getEmptyValue();
        this.lineSeparator = ((CommonSettings<Format>)settings).getFormat().getLineSeparator();
        this.comment = ((CommonSettings<Format>)settings).getFormat().getComment();
        this.skipEmptyLines = settings.getSkipEmptyLines();
        this.writerProcessor = settings.getRowWriterProcessor();
        this.usingSwitch = (this.writerProcessor instanceof RowWriterProcessorSwitch);
        this.expandRows = settings.getExpandIncompleteRows();
        this.columnReorderingEnabled = settings.isColumnReorderingEnabled();
        this.whitespaceRangeStart = settings.getWhitespaceRangeStart();
        this.appender = new WriterCharAppender(settings.getMaxCharsPerColumn(), "", this.whitespaceRangeStart, ((CommonSettings<Format>)settings).getFormat());
        this.rowAppender = new WriterCharAppender(settings.getMaxCharsPerColumn(), "", this.whitespaceRangeStart, ((CommonSettings<Format>)settings).getFormat());
        this.writer = writer;
        this.headers = settings.getHeaders();
        this.updateIndexesToWrite(settings);
        this.partialLine = new Object[settings.getMaxColumns()];
        this.isHeaderWritingEnabled = settings.isHeaderWritingEnabled();
        if (this.writerProcessor instanceof DefaultConversionProcessor) {
            final DefaultConversionProcessor conversionProcessor = (DefaultConversionProcessor)this.writerProcessor;
            conversionProcessor.context = null;
            conversionProcessor.errorHandler = ((CommonSettings<Format>)settings).getProcessorErrorHandler();
        }
        this.initialize(settings);
    }
    
    protected void enableNewlineAfterRecord(final boolean enableNewlineAfterRecord) {
        this.enableNewlineAfterRecord = enableNewlineAfterRecord;
    }
    
    protected abstract void initialize(final S p0);
    
    private void updateIndexesToWrite(final CommonSettings<?> settings) {
        final FieldSelector selector = settings.getFieldSelector();
        if (selector != null) {
            if (this.headers != null && this.headers.length > 0) {
                this.indexesToWrite = selector.getFieldIndexes(this.headers);
                if (this.columnReorderingEnabled) {
                    this.outputRow = new Object[this.indexesToWrite.length];
                }
                else {
                    this.outputRow = new Object[this.headers.length];
                }
            }
            else {
                if (selector instanceof FieldNameSelector || selector instanceof ExcludeFieldNameSelector) {
                    throw new IllegalStateException("Cannot select fields by name with no headers defined");
                }
                int rowLength = this.largestRowLength;
                if (selector instanceof FieldIndexSelector) {
                    boolean gotLengthFromSelection = false;
                    for (final Integer index : ((FieldIndexSelector)selector).get()) {
                        if (rowLength <= index) {
                            rowLength = index;
                            gotLengthFromSelection = true;
                        }
                    }
                    if (gotLengthFromSelection) {
                        ++rowLength;
                    }
                    if (rowLength < this.largestRowLength) {
                        rowLength = this.largestRowLength;
                    }
                }
                else {
                    rowLength = settings.getMaxColumns();
                }
                this.indexesToWrite = selector.getFieldIndexes(new String[rowLength]);
                if (this.columnReorderingEnabled) {
                    this.outputRow = new Object[this.indexesToWrite.length];
                }
                else {
                    this.outputRow = new Object[rowLength];
                }
            }
        }
        else {
            this.outputRow = null;
            this.indexesToWrite = null;
        }
    }
    
    public void updateFieldSelection(final String... newFieldSelection) {
        if (this.headers == null) {
            throw new IllegalStateException("Cannot select fields by name. Headers not defined.");
        }
        this.internalSettings.selectFields(newFieldSelection);
        this.updateIndexesToWrite(this.internalSettings);
    }
    
    public void updateFieldSelection(final Integer... newFieldSelectionByIndex) {
        this.internalSettings.selectIndexes(newFieldSelectionByIndex);
        this.updateIndexesToWrite(this.internalSettings);
    }
    
    public void updateFieldExclusion(final String... fieldsToExclude) {
        if (this.headers == null) {
            throw new IllegalStateException("Cannot de-select fields by name. Headers not defined.");
        }
        this.internalSettings.excludeFields(fieldsToExclude);
        this.updateIndexesToWrite(this.internalSettings);
    }
    
    public void updateFieldExclusion(final Integer... fieldIndexesToExclude) {
        this.internalSettings.excludeIndexes(fieldIndexesToExclude);
        this.updateIndexesToWrite(this.internalSettings);
    }
    
    private void submitRow(final Object[] row) {
        if (this.largestRowLength < row.length) {
            this.largestRowLength = row.length;
        }
        this.processRow(row);
    }
    
    protected abstract void processRow(final Object[] p0);
    
    protected final void appendValueToRow() {
        this.rowAppender.append(this.appender);
    }
    
    protected final void appendToRow(final char ch) {
        this.rowAppender.append(ch);
    }
    
    public final void writeHeaders() {
        this.writeHeaders(this.headers);
    }
    
    public final void writeHeaders(final Collection<?> headers) {
        if (headers != null && headers.size() > 0) {
            this.writeHeaders((String[])headers.toArray(new String[headers.size()]));
            return;
        }
        throw this.throwExceptionAndClose("No headers defined.");
    }
    
    public final void writeHeaders(String... headers) {
        if (this.recordCount > 0L) {
            throw this.throwExceptionAndClose("Cannot write headers after records have been written.", headers, null);
        }
        if (headers != null && headers.length > 0) {
            this.writingHeaders = true;
            if (this.columnReorderingEnabled && this.outputRow != null) {
                this.fillOutputRow(headers);
                headers = Arrays.copyOf(this.outputRow, this.outputRow.length, (Class<? extends String[]>)String[].class);
            }
            this.submitRow(headers);
            this.headers = headers;
            this.internalWriteRow();
            this.writingHeaders = false;
            return;
        }
        throw this.throwExceptionAndClose("No headers defined.", headers, null);
    }
    
    public final void processRecordsAndClose(final Iterable<?> allRecords) {
        try {
            this.processRecords(allRecords);
        }
        finally {
            this.close();
        }
    }
    
    public final void processRecordsAndClose(final Object[] allRecords) {
        try {
            this.processRecords(allRecords);
        }
        finally {
            this.close();
        }
    }
    
    public final void processRecords(final Iterable<?> records) {
        for (final Object record : records) {
            this.processRecord(record);
        }
    }
    
    public final void processRecords(final Object[] records) {
        for (final Object record : records) {
            this.processRecord(record);
        }
    }
    
    public final void processRecord(final Object... record) {
        this.processRecord((Object)record);
    }
    
    public final void processRecord(final Object record) {
        if (this.writerProcessor == null) {
            String recordDescription;
            if (record instanceof Object[]) {
                recordDescription = Arrays.toString((Object[])record);
            }
            else {
                recordDescription = String.valueOf(record);
            }
            final String message = "Cannot process record '" + recordDescription + "' without a writer processor. Please define a writer processor instance in the settings or use the 'writeRow' methods.";
            this.throwExceptionAndClose(message);
        }
        Object[] row;
        try {
            if (this.usingSwitch) {
                this.dummyHeaderRow = ((RowWriterProcessorSwitch)this.writerProcessor).getHeaders(record);
                if (this.dummyHeaderRow == null) {
                    this.dummyHeaderRow = this.headers;
                }
                row = this.writerProcessor.write(record, this.dummyHeaderRow, this.indexesToWrite);
            }
            else {
                row = this.writerProcessor.write(record, this.getRowProcessorHeaders(), this.indexesToWrite);
            }
        }
        catch (DataProcessingException e) {
            e.setErrorContentLength(this.errorContentLength);
            throw e;
        }
        if (row != null) {
            this.writeRow(row);
        }
    }
    
    private String[] getRowProcessorHeaders() {
        if (this.headers == null && this.indexesToWrite == null) {
            return null;
        }
        return this.headers;
    }
    
    public final <C extends Collection<?>> void writeRowsAndClose(final Iterable<C> allRows) {
        try {
            this.writeRows((Iterable<Collection>)allRows);
        }
        finally {
            this.close();
        }
    }
    
    public final void writeRowsAndClose(final Collection<Object[]> allRows) {
        try {
            this.writeRows(allRows);
        }
        finally {
            this.close();
        }
    }
    
    public final void writeStringRowsAndClose(final Collection<String[]> allRows) {
        try {
            this.writeStringRows(allRows);
        }
        finally {
            this.close();
        }
    }
    
    public final void writeRowsAndClose(final Object[][] allRows) {
        try {
            this.writeRows(allRows);
        }
        finally {
            this.close();
        }
    }
    
    public final void writeRows(final Object[][] rows) {
        for (final Object[] row : rows) {
            this.writeRow(row);
        }
    }
    
    public final <C extends Collection<?>> void writeRows(final Iterable<C> rows) {
        for (final Collection<?> row : rows) {
            this.writeRow(row);
        }
    }
    
    public final void writeStringRows(final Collection<String[]> rows) {
        for (final String[] row : rows) {
            this.writeRow(row);
        }
    }
    
    public final <C extends Collection<?>> void writeStringRows(final Iterable<C> rows) {
        for (final Collection<?> row : rows) {
            this.writeRow(row.toArray());
        }
    }
    
    public final void writeRows(final Collection<Object[]> rows) {
        for (final Object[] row : rows) {
            this.writeRow(row);
        }
    }
    
    public final void writeRow(final Collection<?> row) {
        if (row == null) {
            return;
        }
        this.writeRow(row.toArray());
    }
    
    public final void writeRow(final String[] row) {
        this.writeRow((Object[])row);
    }
    
    public final void writeRow(Object... row) {
        try {
            if (this.recordCount == 0L && this.isHeaderWritingEnabled && this.headers != null) {
                this.writeHeaders();
            }
            if (row == null || (row.length == 0 && !this.expandRows)) {
                if (this.skipEmptyLines) {
                    return;
                }
                this.writeEmptyRow();
            }
            else {
                row = this.adjustRowLength(row);
                this.submitRow(row);
                this.internalWriteRow();
            }
        }
        catch (Throwable ex) {
            throw this.throwExceptionAndClose("Error writing row.", row, ex);
        }
    }
    
    protected Object[] expand(final Object[] row, final int length, final String[] h2) {
        if (row.length < length) {
            return Arrays.copyOf(row, length);
        }
        if (h2 != null && row.length < h2.length) {
            return Arrays.copyOf(row, h2.length);
        }
        if (length == -1 && h2 == null && row.length < this.largestRowLength) {
            return Arrays.copyOf(row, this.largestRowLength);
        }
        return row;
    }
    
    public final void writeRow(final String row) {
        try {
            this.writer.write(row);
            if (this.enableNewlineAfterRecord) {
                this.writer.write(this.lineSeparator);
            }
        }
        catch (Throwable ex) {
            throw this.throwExceptionAndClose("Error writing row.", row, ex);
        }
    }
    
    public final void writeEmptyRow() {
        try {
            if (this.enableNewlineAfterRecord) {
                this.writer.write(this.lineSeparator);
            }
        }
        catch (Throwable ex) {
            throw this.throwExceptionAndClose("Error writing empty row.", Arrays.toString(this.lineSeparator), ex);
        }
    }
    
    public final void commentRow(final String comment) {
        this.writeRow(this.comment + comment);
    }
    
    private <T> void fillOutputRow(final T[] row) {
        if (this.columnReorderingEnabled) {
            for (int i = 0; i < this.indexesToWrite.length; ++i) {
                if (this.indexesToWrite[i] < row.length) {
                    this.outputRow[i] = row[this.indexesToWrite[i]];
                }
                else {
                    this.outputRow[i] = null;
                }
            }
        }
        else if (row.length > this.outputRow.length) {
            this.outputRow = row;
        }
        else if (row.length > this.indexesToWrite.length) {
            for (int i = 0; i < this.indexesToWrite.length; ++i) {
                this.outputRow[this.indexesToWrite[i]] = row[this.indexesToWrite[i]];
            }
        }
        else {
            for (int i = 0; i < this.indexesToWrite.length && i < row.length; ++i) {
                this.outputRow[this.indexesToWrite[i]] = row[i];
            }
        }
    }
    
    private void internalWriteRow() {
        try {
            if (this.skipEmptyLines && this.rowAppender.length() == 0) {
                return;
            }
            if (this.enableNewlineAfterRecord) {
                this.rowAppender.appendNewLine();
            }
            this.rowAppender.writeCharsAndReset(this.writer);
            ++this.recordCount;
        }
        catch (Throwable ex) {
            throw this.throwExceptionAndClose("Error writing row.", this.rowAppender.getAndReset(), ex);
        }
    }
    
    protected static int skipLeadingWhitespace(final int whitespaceRangeStart, final String element) {
        if (element.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < element.length(); ++i) {
            final char nextChar = element.charAt(i);
            if (nextChar > ' ' || whitespaceRangeStart >= nextChar) {
                return i;
            }
        }
        return element.length();
    }
    
    public final void flush() {
        try {
            this.writer.flush();
        }
        catch (Throwable ex) {
            throw this.throwExceptionAndClose("Error flushing output.", this.rowAppender.getAndReset(), ex);
        }
    }
    
    public final void close() {
        try {
            this.headerIndexes = null;
            if (this.writer != null) {
                this.writer.close();
                this.writer = null;
            }
        }
        catch (Throwable ex) {
            throw new IllegalStateException("Error closing the output.", ex);
        }
        if (this.partialLineIndex != 0) {
            throw new TextWritingException("Not all values associated with the last record have been written to the output. \n\tHint: use 'writeValuesToRow()' or 'writeValuesToString()' to flush the partially written values to a row.", this.recordCount, this.getContent(Arrays.copyOf(this.partialLine, this.partialLineIndex)));
        }
    }
    
    private TextWritingException throwExceptionAndClose(final String message) {
        return this.throwExceptionAndClose(message, (Object[])null, null);
    }
    
    private TextWritingException throwExceptionAndClose(final String message, final Throwable cause) {
        return this.throwExceptionAndClose(message, (Object[])null, cause);
    }
    
    private TextWritingException throwExceptionAndClose(String message, final String recordCharacters, final Throwable cause) {
        try {
            if (cause instanceof NullPointerException && this.writer == null) {
                message = message + " No writer provided in the constructor of " + this.getClass().getName() + ". You can only use operations that write to Strings.";
            }
            throw new TextWritingException(message, this.recordCount, this.getContent(recordCharacters), cause);
        }
        finally {
            this.close();
        }
    }
    
    private TextWritingException throwExceptionAndClose(final String message, final Object[] recordValues, final Throwable cause) {
        try {
            throw new TextWritingException(message, this.recordCount, this.getContent(recordValues), cause);
        }
        finally {
            try {
                this.close();
            }
            catch (Throwable t) {}
        }
    }
    
    protected String getStringValue(final Object element) {
        this.usingNullOrEmptyValue = false;
        if (element == null) {
            this.usingNullOrEmptyValue = true;
            return this.nullValue;
        }
        final String string = String.valueOf(element);
        if (string.isEmpty()) {
            this.usingNullOrEmptyValue = true;
            return this.emptyValue;
        }
        return string;
    }
    
    public final void addValues(final Object... values) {
        try {
            System.arraycopy(values, 0, this.partialLine, this.partialLineIndex, values.length);
            this.partialLineIndex += values.length;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error adding values to in-memory row", values, t);
        }
    }
    
    public final void addStringValues(final Collection<String> values) {
        if (values != null) {
            try {
                for (final String o : values) {
                    this.partialLine[this.partialLineIndex++] = o;
                }
            }
            catch (Throwable t) {
                throw this.throwExceptionAndClose("Error adding values to in-memory row", values.toArray(), t);
            }
        }
    }
    
    public final void addValues(final Collection<?> values) {
        if (values != null) {
            try {
                for (final Object o : values) {
                    this.partialLine[this.partialLineIndex++] = o;
                }
            }
            catch (Throwable t) {
                throw this.throwExceptionAndClose("Error adding values to in-memory row", values.toArray(), t);
            }
        }
    }
    
    public final void addValue(final Object value) {
        try {
            this.partialLine[this.partialLineIndex++] = value;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error adding value to in-memory row", new Object[] { value }, t);
        }
    }
    
    private void fillPartialLineToMatchHeaders() {
        if (this.headers != null && this.partialLineIndex < this.headers.length) {
            while (this.partialLineIndex < this.headers.length) {
                this.partialLine[this.partialLineIndex++] = null;
            }
        }
    }
    
    public final void writeValuesToRow() {
        this.fillPartialLineToMatchHeaders();
        this.writeRow(Arrays.copyOf(this.partialLine, this.partialLineIndex));
        this.discardValues();
    }
    
    public final void addValue(final int index, final Object value) {
        if (index >= this.partialLine.length) {
            throw this.throwExceptionAndClose("Cannot write '" + value + "' to index '" + index + "'. Maximum number of columns (" + this.partialLine.length + ") exceeded.", new Object[] { value }, null);
        }
        this.partialLine[index] = value;
        if (this.partialLineIndex <= index) {
            this.partialLineIndex = index + 1;
        }
    }
    
    public final void addValue(final String headerName, final Object value) {
        this.addValue(this.getFieldIndex(this.headers, headerName, false), value);
    }
    
    private final void addValue(final String[] headersInContext, final String headerName, final boolean ignoreOnMismatch, final Object value) {
        final int index = this.getFieldIndex(headersInContext, headerName, ignoreOnMismatch);
        if (index != -1) {
            this.addValue(index, value);
        }
    }
    
    private int getFieldIndex(final String[] headersInContext, final String headerName, final boolean ignoreOnMismatch) {
        if (this.headerIndexes == null) {
            this.headerIndexes = new HashMap<String[], Map<String, Integer>>();
        }
        Map<String, Integer> indexes = this.headerIndexes.get(headersInContext);
        if (indexes == null) {
            indexes = new HashMap<String, Integer>();
            this.headerIndexes.put(headersInContext, indexes);
        }
        Integer index = indexes.get(headerName);
        if (index == null) {
            if (headersInContext == null) {
                throw this.throwExceptionAndClose("Cannot calculate position of header '" + headerName + "' as no headers were defined.", null);
            }
            index = ArgumentUtils.indexOf(ArgumentUtils.normalize(headersInContext), ArgumentUtils.normalize(headerName));
            if (index == -1 && !ignoreOnMismatch) {
                throw this.throwExceptionAndClose("Header '" + headerName + "' could not be found. Defined headers are: " + Arrays.toString(headersInContext) + '.', null);
            }
            indexes.put(headerName, index);
        }
        return index;
    }
    
    public final void discardValues() {
        Arrays.fill(this.partialLine, 0, this.partialLineIndex, null);
        this.partialLineIndex = 0;
    }
    
    public final String writeHeadersToString() {
        return this.writeHeadersToString(this.headers);
    }
    
    public final String writeHeadersToString(final Collection<?> headers) {
        if (headers != null && headers.size() > 0) {
            return this.writeHeadersToString((String[])headers.toArray(new String[headers.size()]));
        }
        throw this.throwExceptionAndClose("No headers defined");
    }
    
    public final String writeHeadersToString(final String... headers) {
        if (headers != null && headers.length > 0) {
            this.writingHeaders = true;
            this.submitRow(headers);
            this.writingHeaders = false;
            this.headers = headers;
            return this.internalWriteRowToString();
        }
        throw this.throwExceptionAndClose("No headers defined.");
    }
    
    public final List<String> processRecordsToString(final Iterable<?> records) {
        try {
            final List<String> out = new ArrayList<String>(1000);
            for (final Object record : records) {
                out.add(this.processRecordToString(record));
            }
            return out;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Unable process input records", t);
        }
    }
    
    public final List<String> processRecordsToString(final Object[] records) {
        try {
            final List<String> out = new ArrayList<String>(1000);
            for (final Object record : records) {
                out.add(this.processRecordToString(record));
            }
            return out;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Unable process input records", records, t);
        }
    }
    
    public final String processRecordToString(final Object... record) {
        return this.processRecordToString((Object)record);
    }
    
    public final String processRecordToString(final Object record) {
        if (this.writerProcessor == null) {
            throw this.throwExceptionAndClose("Cannot process record '" + record + "' without a writer processor. Please define a writer processor instance in the settings or use the 'writeRow' methods.");
        }
        try {
            final Object[] row = this.writerProcessor.write(record, this.getRowProcessorHeaders(), this.indexesToWrite);
            if (row != null) {
                return this.writeRowToString(row);
            }
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Could not process record '" + record + "'", t);
        }
        return null;
    }
    
    public final List<String> writeRowsToString(final Object[][] rows) {
        try {
            final List<String> out = new ArrayList<String>(rows.length);
            for (final Object[] row : rows) {
                final String string = this.writeRowToString(row);
                if (string != null) {
                    out.add(string);
                }
            }
            return out;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error writing input rows", t);
        }
    }
    
    public final <C extends Collection<?>> List<String> writeRowsToString(final Iterable<C> rows) {
        try {
            final List<String> out = new ArrayList<String>(1000);
            for (final Collection<?> row : rows) {
                out.add(this.writeRowToString(row));
            }
            return out;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error writing input rows", t);
        }
    }
    
    public final <C extends Collection<?>> List<String> writeStringRowsToString(final Iterable<C> rows) {
        try {
            final List<String> out = new ArrayList<String>(1000);
            for (final Collection<?> row : rows) {
                final String string = this.writeRowToString(row);
                if (string != null) {
                    out.add(string);
                }
            }
            return out;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error writing input rows", t);
        }
    }
    
    public final List<String> writeRowsToString(final Collection<Object[]> rows) {
        try {
            final List<String> out = new ArrayList<String>(rows.size());
            for (final Object[] row : rows) {
                out.add(this.writeRowToString(row));
            }
            return out;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error writing input rows", t);
        }
    }
    
    public final List<String> writeStringRowsToString(final Collection<String[]> rows) {
        try {
            final List<String> out = new ArrayList<String>(rows.size());
            for (final String[] row : rows) {
                out.add(this.writeRowToString(row));
            }
            return out;
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error writing input rows", t);
        }
    }
    
    public final String writeRowToString(final Collection<?> row) {
        try {
            if (row == null) {
                return null;
            }
            return this.writeRowToString(row.toArray());
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error writing input row ", t);
        }
    }
    
    public final String writeRowToString(final String[] row) {
        return this.writeRowToString((Object[])row);
    }
    
    public final String writeRowToString(Object... row) {
        try {
            if ((row == null || (row.length == 0 && !this.expandRows)) && this.skipEmptyLines) {
                return null;
            }
            row = this.adjustRowLength(row);
            this.submitRow(row);
            return this.internalWriteRowToString();
        }
        catch (Throwable ex) {
            throw this.throwExceptionAndClose("Error writing row.", row, ex);
        }
    }
    
    private Object[] adjustRowLength(Object[] row) {
        if (this.outputRow != null) {
            this.fillOutputRow(row);
            row = this.outputRow;
        }
        else if (this.expandRows) {
            if (this.usingSwitch) {
                row = this.expand(row, (this.dummyHeaderRow == null) ? -1 : this.dummyHeaderRow.length, this.headers);
                this.dummyHeaderRow = null;
            }
            else {
                row = this.expand(row, (this.headers == null) ? -1 : this.headers.length, null);
            }
        }
        return row;
    }
    
    public final String commentRowToString(final String comment) {
        return this.writeRowToString(this.comment + comment);
    }
    
    private String internalWriteRowToString() {
        if (this.skipEmptyLines && this.rowAppender.length() == 0) {
            return null;
        }
        final String out = this.rowAppender.getAndReset();
        ++this.recordCount;
        return out;
    }
    
    public final String writeValuesToString() {
        this.fillPartialLineToMatchHeaders();
        final String out = this.writeRowToString(Arrays.copyOf(this.partialLine, this.partialLineIndex));
        this.discardValues();
        return out;
    }
    
    public final void processValuesToRow() {
        this.fillPartialLineToMatchHeaders();
        this.processRecord(Arrays.copyOf(this.partialLine, this.partialLineIndex));
        this.discardValues();
    }
    
    public final String processValuesToString() {
        this.fillPartialLineToMatchHeaders();
        final String out = this.processRecordToString(Arrays.copyOf(this.partialLine, this.partialLineIndex));
        this.discardValues();
        return out;
    }
    
    public final long getRecordCount() {
        return this.recordCount;
    }
    
    private <K> void writeValuesFromMap(final Map<K, String> headerMapping, final Map<K, ?> rowData) {
        try {
            if (rowData != null && !rowData.isEmpty()) {
                this.dummyHeaderRow = this.headers;
                if (this.usingSwitch) {
                    this.dummyHeaderRow = ((RowWriterProcessorSwitch)this.writerProcessor).getHeaders(headerMapping, rowData);
                    if (this.dummyHeaderRow == null) {
                        this.dummyHeaderRow = this.headers;
                    }
                }
                if (this.dummyHeaderRow != null) {
                    if (headerMapping == null) {
                        for (final Map.Entry<?, ?> e : rowData.entrySet()) {
                            this.addValue(this.dummyHeaderRow, String.valueOf(e.getKey()), true, e.getValue());
                        }
                    }
                    else {
                        for (final Map.Entry<?, ?> e : rowData.entrySet()) {
                            final String header = headerMapping.get(e.getKey());
                            if (header != null) {
                                this.addValue(this.dummyHeaderRow, header, true, e.getValue());
                            }
                        }
                    }
                }
                else if (headerMapping != null) {
                    this.setHeadersFromMap(headerMapping, false);
                    this.writeValuesFromMap((Map<Object, String>)headerMapping, (Map<Object, ?>)rowData);
                }
                else {
                    this.setHeadersFromMap(rowData, true);
                    this.writeValuesFromMap(null, (Map<Object, ?>)rowData);
                }
            }
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error processing data from input map", t);
        }
    }
    
    private void setHeadersFromMap(final Map<?, ?> map, final boolean keys) {
        this.headers = new String[map.size()];
        int i = 0;
        for (final Object header : keys ? map.keySet() : map.values()) {
            this.headers[i++] = String.valueOf(header);
        }
    }
    
    public final String writeRowToString(final Map<?, ?> rowData) {
        return this.writeRowToString(null, rowData);
    }
    
    public final void writeRow(final Map<?, ?> rowData) {
        this.writeRow(null, rowData);
    }
    
    public final <K> String writeRowToString(final Map<K, String> headerMapping, final Map<K, ?> rowData) {
        this.writeValuesFromMap(headerMapping, rowData);
        return this.writeValuesToString();
    }
    
    public final <K> void writeRow(final Map<K, String> headerMapping, final Map<K, ?> rowData) {
        this.writeValuesFromMap((Map<Object, String>)headerMapping, (Map<Object, ?>)rowData);
        this.writeValuesToRow();
    }
    
    public final <K, I extends Iterable<?>> List<String> writeRowsToString(final Map<K, I> rowData) {
        return this.writeRowsToString(null, rowData);
    }
    
    public final <K, I extends Iterable<?>> void writeRows(final Map<K, I> rowData) {
        this.writeRows(null, rowData, null, false);
    }
    
    public final <K, I extends Iterable<?>> List<String> writeRowsToString(final Map<K, String> headerMapping, final Map<K, I> rowData) {
        final List<String> writtenRows = new ArrayList<String>();
        this.writeRows(headerMapping, rowData, writtenRows, false);
        return writtenRows;
    }
    
    public final <K, I extends Iterable<?>> void writeRows(final Map<K, String> headerMapping, final Map<K, I> rowData) {
        this.writeRows(headerMapping, rowData, null, false);
    }
    
    private <K, I extends Iterable<?>> void writeRows(final Map<K, String> headerMapping, final Map<K, I> rowData, final List<String> outputList, final boolean useRowProcessor) {
        try {
            final Iterator[] iterators = new Iterator[rowData.size()];
            final Object[] keys = new Object[rowData.size()];
            final Map<Object, Object> rowValues = new LinkedHashMap<Object, Object>(rowData.size());
            if (outputList != null && this.headers == null) {
                if (headerMapping != null) {
                    this.setHeadersFromMap(headerMapping, true);
                }
                else {
                    this.setHeadersFromMap(rowData, true);
                }
                if (this.isHeaderWritingEnabled && this.recordCount == 0L) {
                    outputList.add(this.writeHeadersToString());
                }
            }
            int length = 0;
            for (final Map.Entry<K, I> rowEntry : rowData.entrySet()) {
                iterators[length] = ((rowEntry.getValue() == null) ? null : rowEntry.getValue().iterator());
                keys[length] = rowEntry.getKey();
                rowValues.put(rowEntry.getKey(), null);
                ++length;
            }
            boolean nullsOnly;
            do {
                nullsOnly = true;
                for (int i = 0; i < length; ++i) {
                    final Iterator<?> iterator = (Iterator<?>)iterators[i];
                    final boolean isNull = iterator == null || !iterator.hasNext();
                    nullsOnly &= isNull;
                    if (isNull) {
                        rowValues.put(keys[i], null);
                    }
                    else {
                        rowValues.put(keys[i], iterator.next());
                    }
                }
                if (!nullsOnly) {
                    if (outputList == null) {
                        if (useRowProcessor) {
                            this.processRecord((Map<Object, String>)headerMapping, rowValues);
                        }
                        else {
                            this.writeRow((Map<Object, String>)headerMapping, rowValues);
                        }
                    }
                    else if (useRowProcessor) {
                        outputList.add(this.processRecordToString((Map<Object, String>)headerMapping, rowValues));
                    }
                    else {
                        outputList.add(this.writeRowToString((Map<Object, String>)headerMapping, rowValues));
                    }
                }
            } while (!nullsOnly);
        }
        catch (Throwable t) {
            throw this.throwExceptionAndClose("Error processing input rows from map", t);
        }
    }
    
    public final <K> List<String> writeStringRowsToString(final Map<K, String> headerMapping, final Map<K, String[]> rowData) {
        final List<String> writtenRows = new ArrayList<String>();
        this.writeRows(headerMapping, (Map<K, Iterable<String>>)this.wrapStringArray((Map<K, String[]>)rowData), writtenRows, false);
        return writtenRows;
    }
    
    public final <K> void writeStringRows(final Map<K, String> headerMapping, final Map<K, String[]> rowData) {
        this.writeRows(headerMapping, (Map<K, Iterable<String>>)this.wrapStringArray((Map<K, String[]>)rowData), null, false);
    }
    
    public final <K> List<String> writeObjectRowsToString(final Map<K, String> headerMapping, final Map<K, Object[]> rowData) {
        final List<String> writtenRows = new ArrayList<String>();
        this.writeRows(headerMapping, (Map<K, Iterable<Object>>)this.wrapObjectArray((Map<K, Object[]>)rowData), writtenRows, false);
        return writtenRows;
    }
    
    public final <K> void writeObjectRows(final Map<K, String> headerMapping, final Map<K, Object[]> rowData) {
        this.writeRows(headerMapping, (Map<K, Iterable<Object>>)this.wrapObjectArray((Map<K, Object[]>)rowData), null, false);
    }
    
    private <K> Map<K, Iterable<Object>> wrapObjectArray(final Map<K, Object[]> rowData) {
        final Map<K, Iterable<Object>> out = new LinkedHashMap<K, Iterable<Object>>(rowData.size());
        for (final Map.Entry<K, Object[]> e : rowData.entrySet()) {
            if (e.getValue() == null) {
                out.put(e.getKey(), Collections.emptyList());
            }
            else {
                out.put(e.getKey(), Arrays.asList((Object[])e.getValue()));
            }
        }
        return out;
    }
    
    private <K> Map<K, Iterable<String>> wrapStringArray(final Map<K, String[]> rowData) {
        final Map<K, Iterable<String>> out = new LinkedHashMap<K, Iterable<String>>(rowData.size());
        for (final Map.Entry<K, String[]> e : rowData.entrySet()) {
            if (e.getValue() == null) {
                out.put(e.getKey(), (Iterable<String>)Collections.emptyList());
            }
            else {
                out.put(e.getKey(), Arrays.asList((String[])e.getValue()));
            }
        }
        return out;
    }
    
    public final <K> void writeObjectRowsAndClose(final Map<K, String> headerMapping, final Map<K, Object[]> rowData) {
        try {
            this.writeObjectRows((Map<Object, String>)headerMapping, (Map<Object, Object[]>)rowData);
        }
        finally {
            this.close();
        }
    }
    
    public final <K> void writeStringRowsAndClose(final Map<K, String> headerMapping, final Map<K, String[]> rowData) {
        try {
            this.writeStringRows((Map<Object, String>)headerMapping, (Map<Object, String[]>)rowData);
        }
        finally {
            this.close();
        }
    }
    
    public final <K> void writeObjectRowsAndClose(final Map<K, Object[]> rowData) {
        this.writeObjectRowsAndClose(null, rowData);
    }
    
    public final <K> void writeStringRowsAndClose(final Map<K, String[]> rowData) {
        this.writeStringRowsAndClose(null, rowData);
    }
    
    public final <K, I extends Iterable<?>> void writeRowsAndClose(final Map<K, String> headerMapping, final Map<K, I> rowData) {
        try {
            this.writeRows((Map<Object, String>)headerMapping, (Map<Object, Iterable>)rowData);
        }
        finally {
            this.close();
        }
    }
    
    public final <K, I extends Iterable<?>> void writeRowsAndClose(final Map<K, I> rowData) {
        this.writeRowsAndClose(null, rowData);
    }
    
    public final String processRecordToString(final Map<?, ?> rowData) {
        return this.processRecordToString(null, rowData);
    }
    
    public final void processRecord(final Map<?, ?> rowData) {
        this.processRecord(null, rowData);
    }
    
    public final <K> String processRecordToString(final Map<K, String> headerMapping, final Map<K, ?> rowData) {
        this.writeValuesFromMap(headerMapping, rowData);
        return this.processValuesToString();
    }
    
    public final <K> void processRecord(final Map<K, String> headerMapping, final Map<K, ?> rowData) {
        this.writeValuesFromMap((Map<Object, String>)headerMapping, (Map<Object, ?>)rowData);
        this.processValuesToRow();
    }
    
    public final <K, I extends Iterable<?>> List<String> processRecordsToString(final Map<K, I> rowData) {
        return this.processRecordsToString(null, rowData);
    }
    
    public final <K, I extends Iterable<?>> void processRecords(final Map<K, I> rowData) {
        this.writeRows(null, rowData, null, true);
    }
    
    public final <K, I extends Iterable<?>> List<String> processRecordsToString(final Map<K, String> headerMapping, final Map<K, I> rowData) {
        final List<String> writtenRows = new ArrayList<String>();
        this.writeRows(headerMapping, rowData, writtenRows, true);
        return writtenRows;
    }
    
    public final <K, I extends Iterable<?>> void processRecords(final Map<K, String> headerMapping, final Map<K, I> rowData) {
        this.writeRows(headerMapping, rowData, null, true);
    }
    
    public final <K> List<String> processObjectRecordsToString(final Map<K, Object[]> rowData) {
        return this.processObjectRecordsToString(null, rowData);
    }
    
    public final <K> List<String> processObjectRecordsToString(final Map<K, String> headerMapping, final Map<K, Object[]> rowData) {
        final List<String> writtenRows = new ArrayList<String>();
        this.writeRows(headerMapping, (Map<K, Iterable<Object>>)this.wrapObjectArray((Map<K, Object[]>)rowData), writtenRows, true);
        return writtenRows;
    }
    
    public final <K> void processObjectRecords(final Map<K, String> headerMapping, final Map<K, Object[]> rowData) {
        this.writeRows(headerMapping, (Map<K, Iterable<Object>>)this.wrapObjectArray((Map<K, Object[]>)rowData), null, true);
    }
    
    public final <K> void processObjectRecordsAndClose(final Map<K, String> headerMapping, final Map<K, Object[]> rowData) {
        try {
            this.processObjectRecords((Map<Object, String>)headerMapping, (Map<Object, Object[]>)rowData);
        }
        finally {
            this.close();
        }
    }
    
    public final <K> void processObjectRecordsAndClose(final Map<K, Object[]> rowData) {
        this.processRecordsAndClose(null, (Map<Object, Iterable<Object>>)this.wrapObjectArray((Map<K, Object[]>)rowData));
    }
    
    public final <K, I extends Iterable<?>> void processRecordsAndClose(final Map<K, String> headerMapping, final Map<K, I> rowData) {
        try {
            this.processRecords((Map<Object, String>)headerMapping, (Map<Object, Iterable>)rowData);
        }
        finally {
            this.close();
        }
    }
    
    public final <K, I extends Iterable<?>> void processRecordsAndClose(final Map<K, I> rowData) {
        this.processRecordsAndClose(null, rowData);
    }
    
    private Object[] getContent(final Object[] tmp) {
        return AbstractException.restrictContent(this.errorContentLength, tmp);
    }
    
    private String getContent(final CharSequence tmp) {
        return AbstractException.restrictContent(this.errorContentLength, tmp);
    }
}
