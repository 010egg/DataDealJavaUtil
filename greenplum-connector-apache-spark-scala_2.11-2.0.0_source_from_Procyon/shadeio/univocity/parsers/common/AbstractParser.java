// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.common.iterators.RecordIterator;
import shadeio.univocity.parsers.common.iterators.RowIterator;
import shadeio.univocity.parsers.common.record.RecordMetaData;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.io.File;
import shadeio.univocity.parsers.common.record.Record;
import java.util.ArrayList;
import java.util.List;
import shadeio.univocity.parsers.common.input.InputAnalysisProcess;
import shadeio.univocity.parsers.common.input.AbstractCharInputReader;
import shadeio.univocity.parsers.common.input.DefaultCharInputReader;
import shadeio.univocity.parsers.common.input.EOFException;
import shadeio.univocity.parsers.common.processor.core.NoopProcessor;
import java.io.Reader;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;
import shadeio.univocity.parsers.common.input.CharInputReader;
import shadeio.univocity.parsers.common.processor.core.Processor;

public abstract class AbstractParser<T extends CommonParserSettings<?>>
{
    protected final T settings;
    protected final ParserOutput output;
    private final long recordsToRead;
    private final char comment;
    private final LineReader lineReader;
    protected ParsingContext context;
    protected Processor processor;
    protected CharInputReader input;
    protected char ch;
    private final ProcessorErrorHandler errorHandler;
    private final long rowsToSkip;
    protected final Map<Long, String> comments;
    protected String lastComment;
    private final boolean collectComments;
    private final int errorContentLength;
    private boolean extractingHeaders;
    private final boolean extractHeaders;
    protected final int whitespaceRangeStart;
    
    public AbstractParser(final T settings) {
        this.lineReader = new LineReader();
        this.extractingHeaders = false;
        settings.autoConfigure();
        this.settings = settings;
        this.errorContentLength = settings.getErrorContentLength();
        this.output = new ParserOutput(this, settings);
        this.processor = ((CommonParserSettings<Format>)settings).getProcessor();
        this.recordsToRead = settings.getNumberOfRecordsToRead();
        this.comment = ((CommonSettings<Format>)settings).getFormat().getComment();
        this.errorHandler = ((CommonSettings<Format>)settings).getProcessorErrorHandler();
        this.rowsToSkip = settings.getNumberOfRowsToSkip();
        this.collectComments = settings.isCommentCollectionEnabled();
        this.comments = (this.collectComments ? new TreeMap<Long, String>() : Collections.emptyMap());
        this.extractHeaders = settings.isHeaderExtractionEnabled();
        this.whitespaceRangeStart = settings.getWhitespaceRangeStart();
    }
    
    protected void processComment() {
        if (this.collectComments) {
            final long line = this.input.lineCount();
            final String comment = this.input.readComment();
            if (comment != null) {
                this.lastComment = comment;
                this.comments.put(line, this.lastComment);
            }
        }
        else {
            try {
                this.input.skipLines(1L);
            }
            catch (IllegalArgumentException ex) {}
        }
    }
    
    public final void parse(final Reader reader) {
        this.beginParsing(reader);
        try {
            while (!this.context.isStopped()) {
                this.input.markRecordStart();
                this.ch = this.input.nextChar();
                if (this.inComment()) {
                    this.processComment();
                }
                else {
                    this.parseRecord();
                    final String[] row = this.output.rowParsed();
                    if (row == null) {
                        continue;
                    }
                    if (this.recordsToRead >= 0L && this.context.currentRecord() >= this.recordsToRead) {
                        this.context.stop();
                        if (this.recordsToRead == 0L) {
                            this.stopParsing();
                            return;
                        }
                    }
                    if (this.processor == NoopProcessor.instance) {
                        continue;
                    }
                    this.rowProcessed(row);
                }
            }
            this.stopParsing();
        }
        catch (EOFException ex2) {
            try {
                this.handleEOF();
            }
            finally {
                this.stopParsing();
            }
        }
        catch (Throwable ex) {
            try {
                ex = this.handleException(ex);
            }
            finally {
                this.stopParsing(ex);
            }
        }
    }
    
    protected abstract void parseRecord();
    
    protected boolean consumeValueOnEOF() {
        return false;
    }
    
    private String[] handleEOF() {
        String[] row = null;
        try {
            final boolean consumeValueOnEOF = this.consumeValueOnEOF();
            if (this.output.column != 0 || consumeValueOnEOF) {
                if (this.output.appender.length() > 0 || consumeValueOnEOF) {
                    this.output.valueParsed();
                }
                else {
                    this.output.emptyParsed();
                }
                row = this.output.rowParsed();
            }
            else if (this.output.appender.length() > 0 || this.input.currentParsedContentLength() > 0) {
                this.output.valueParsed();
                row = this.output.rowParsed();
            }
        }
        catch (Throwable e) {
            throw this.handleException(e);
        }
        if (row != null && this.processor != NoopProcessor.instance) {
            this.rowProcessed(row);
        }
        return row;
    }
    
    public final void beginParsing(final Reader reader) {
        this.output.reset();
        if (reader instanceof LineReader) {
            this.input = new DefaultCharInputReader(((CommonSettings<Format>)this.settings).getFormat().getLineSeparator(), ((CommonSettings<Format>)this.settings).getFormat().getNormalizedNewline(), this.settings.getInputBufferSize(), this.whitespaceRangeStart);
        }
        else {
            this.input = this.settings.newCharInputReader(this.whitespaceRangeStart);
        }
        this.input.enableNormalizeLineEndings(true);
        this.context = this.createParsingContext();
        if (this.processor instanceof DefaultConversionProcessor) {
            final DefaultConversionProcessor conversionProcessor = (DefaultConversionProcessor)this.processor;
            conversionProcessor.errorHandler = this.errorHandler;
            conversionProcessor.context = this.context;
        }
        if (this.input instanceof AbstractCharInputReader) {
            ((AbstractCharInputReader)this.input).addInputAnalysisProcess(this.getInputAnalysisProcess());
        }
        try {
            this.input.start(reader);
        }
        catch (Throwable t) {
            throw this.handleException(t);
        }
        this.input.skipLines(this.rowsToSkip);
        this.initialize();
        this.processor.processStarted(this.context);
    }
    
    void extractHeadersIfRequired() {
        while (this.extractHeaders && this.output.parsedHeaders == null && !this.context.isStopped() && !this.extractingHeaders) {
            final Processor userProvidedProcessor = this.processor;
            try {
                this.processor = NoopProcessor.instance;
                this.extractingHeaders = true;
                this.parseNext();
            }
            finally {
                this.extractingHeaders = false;
                this.processor = userProvidedProcessor;
            }
        }
    }
    
    protected ParsingContext createParsingContext() {
        final DefaultParsingContext out = new DefaultParsingContext(this, this.errorContentLength);
        out.stopped = false;
        return out;
    }
    
    protected void initialize() {
    }
    
    protected InputAnalysisProcess getInputAnalysisProcess() {
        return null;
    }
    
    private String getParsedContent(final CharSequence tmp) {
        return "Parsed content: " + AbstractException.restrictContent(this.errorContentLength, tmp);
    }
    
    private TextParsingException handleException(final Throwable ex) {
        if (this.context != null) {
            this.context.stop();
        }
        if (ex instanceof DataProcessingException) {
            final DataProcessingException error = (DataProcessingException)ex;
            error.restrictContent(this.errorContentLength);
            error.setContext(this.context);
            throw error;
        }
        String message = ex.getClass().getName() + " - " + ex.getMessage();
        final char[] chars = this.output.appender.getChars();
        if (chars != null) {
            int length = this.output.appender.length();
            if (length > chars.length) {
                message = "Length of parsed input (" + length + ") exceeds the maximum number of characters defined in" + " your parser settings (" + this.settings.getMaxCharsPerColumn() + "). ";
                length = chars.length;
            }
            String tmp = new String(chars);
            if (tmp.contains("\n") || tmp.contains("\r")) {
                tmp = displayLineSeparators(tmp, true);
                final String lineSeparator = displayLineSeparators(((CommonSettings<Format>)this.settings).getFormat().getLineSeparatorString(), false);
                message = message + "\nIdentified line separator characters in the parsed content. This may be the cause of the error. The line separator in your parser settings is set to '" + lineSeparator + "'. " + this.getParsedContent(tmp);
            }
            int nullCharacterCount = 0;
            final int maxLength = (length > 1073741823) ? 1073741822 : length;
            final StringBuilder s = new StringBuilder(maxLength);
            for (int i = 0; i < maxLength; ++i) {
                if (chars[i] == '\0') {
                    s.append('\\');
                    s.append('0');
                    ++nullCharacterCount;
                }
                else {
                    s.append(chars[i]);
                }
            }
            tmp = s.toString();
            if (nullCharacterCount > 0) {
                message = message + "\nIdentified " + nullCharacterCount + " null characters ('\u0000') on parsed content. This may " + "indicate the data is corrupt or its encoding is invalid. Parsed content:\n\t" + this.getParsedContent(tmp);
            }
        }
        if (ex instanceof ArrayIndexOutOfBoundsException) {
            try {
                final int index = Integer.parseInt(ex.getMessage());
                if (index == this.settings.getMaxCharsPerColumn()) {
                    message = message + "\nHint: Number of characters processed may have exceeded limit of " + index + " characters per column. Use settings.setMaxCharsPerColumn(int) to define the maximum number of characters a column can have";
                }
                if (index == this.settings.getMaxColumns()) {
                    message = message + "\nHint: Number of columns processed may have exceeded limit of " + index + " columns. Use settings.setMaxColumns(int) to define the maximum number of columns your input can have";
                }
                message += "\nEnsure your configuration is correct, with delimiters, quotes and escape sequences that match the input format you are trying to parse";
            }
            catch (Throwable t) {}
        }
        try {
            if (!message.isEmpty()) {
                message += "\n";
            }
            message = message + "Parser Configuration: " + this.settings.toString();
        }
        catch (Exception ex2) {}
        if (this.errorContentLength == 0) {
            this.output.appender.reset();
        }
        final TextParsingException out = new TextParsingException(this.context, message, ex);
        out.setErrorContentLength(this.errorContentLength);
        return out;
    }
    
    private static String displayLineSeparators(String str, final boolean addNewLine) {
        if (addNewLine) {
            if (str.contains("\r\n")) {
                str = str.replaceAll("\\r\\n", "[\\\\r\\\\n]\r\n\t");
            }
            else if (str.contains("\n")) {
                str = str.replaceAll("\\n", "[\\\\n]\n\t");
            }
            else {
                str = str.replaceAll("\\r", "[\\\\r]\r\t");
            }
        }
        else {
            str = str.replaceAll("\\n", "\\\\n");
            str = str.replaceAll("\\r", "\\\\r");
        }
        return str;
    }
    
    private void stopParsing(final Throwable error) {
        if (error == null) {
            this.stopParsing();
            return;
        }
        try {
            this.stopParsing();
        }
        catch (Throwable t) {}
        if (error instanceof DataProcessingException) {
            final DataProcessingException ex = (DataProcessingException)error;
            ex.setContext(this.context);
            throw ex;
        }
        if (error instanceof RuntimeException) {
            throw (RuntimeException)error;
        }
        if (error instanceof Error) {
            throw (Error)error;
        }
        throw new IllegalStateException(error.getMessage(), error);
    }
    
    public final void stopParsing() {
        try {
            try {
                if (this.context != null) {
                    this.context.stop();
                }
            }
            finally {
                try {
                    if (this.processor != null) {
                        this.processor.processEnded(this.context);
                    }
                }
                finally {
                    if (this.output != null) {
                        this.output.appender.reset();
                    }
                    if (this.input != null) {
                        this.input.stop();
                    }
                }
            }
        }
        catch (Throwable error) {
            throw this.handleException(error);
        }
    }
    
    private <T> List<T> beginParseAll(final boolean validateReader, final Reader reader, final int expectedRowCount) {
        if (reader == null) {
            if (validateReader) {
                throw new IllegalStateException("Input reader must not be null");
            }
            if (this.context == null) {
                throw new IllegalStateException("Input not defined. Please call method 'beginParsing()' with a valid input.");
            }
            if (this.context.isStopped()) {
                return Collections.emptyList();
            }
        }
        final List<T> out = new ArrayList<T>((expectedRowCount <= 0) ? 10000 : expectedRowCount);
        if (reader != null) {
            this.beginParsing(reader);
        }
        return out;
    }
    
    public List<String[]> parseAll(final int expectedRowCount) {
        return this.internalParseAll(false, null, expectedRowCount);
    }
    
    public List<String[]> parseAll() {
        return this.internalParseAll(false, null, -1);
    }
    
    public List<Record> parseAllRecords(final int expectedRowCount) {
        return this.internalParseAllRecords(false, null, expectedRowCount);
    }
    
    public List<Record> parseAllRecords() {
        return this.internalParseAllRecords(false, null, -1);
    }
    
    public final List<String[]> parseAll(final Reader reader) {
        return this.parseAll(reader, 0);
    }
    
    public final List<String[]> parseAll(final Reader reader, final int expectedRowCount) {
        return this.internalParseAll(true, reader, expectedRowCount);
    }
    
    private final List<String[]> internalParseAll(final boolean validateReader, final Reader reader, final int expectedRowCount) {
        final List<String[]> out = this.beginParseAll(validateReader, reader, expectedRowCount);
        String[] row;
        while ((row = this.parseNext()) != null) {
            out.add(row);
        }
        return out;
    }
    
    protected boolean inComment() {
        return this.ch == this.comment;
    }
    
    public final String[] parseNext() {
        try {
            while (!this.context.isStopped()) {
                this.input.markRecordStart();
                this.ch = this.input.nextChar();
                if (this.inComment()) {
                    this.processComment();
                }
                else {
                    this.parseRecord();
                    final String[] row = this.output.rowParsed();
                    if (row != null) {
                        if (this.recordsToRead >= 0L && this.context.currentRecord() >= this.recordsToRead) {
                            this.context.stop();
                            if (this.recordsToRead == 0L) {
                                this.stopParsing();
                                return null;
                            }
                        }
                        if (this.processor != NoopProcessor.instance) {
                            this.rowProcessed(row);
                        }
                        return row;
                    }
                    if (this.extractingHeaders) {
                        return null;
                    }
                    continue;
                }
            }
            this.stopParsing();
            return null;
        }
        catch (EOFException ex3) {
            final String[] row2 = this.handleEOF();
            this.stopParsing();
            return row2;
        }
        catch (NullPointerException ex) {
            if (this.context == null) {
                throw new IllegalStateException("Cannot parse without invoking method beginParsing(Reader) first");
            }
            if (this.input != null) {
                this.stopParsing();
            }
            throw new IllegalStateException("Error parsing next record.", ex);
        }
        catch (Throwable ex2) {
            try {
                ex2 = this.handleException(ex2);
            }
            finally {
                this.stopParsing(ex2);
            }
            return null;
        }
    }
    
    protected final void reloadHeaders() {
        this.output.initializeHeaders();
        if (this.context instanceof DefaultParsingContext) {
            ((DefaultParsingContext)this.context).reset();
        }
    }
    
    public final Record parseRecord(final String line) {
        final String[] values = this.parseLine(line);
        if (values == null) {
            return null;
        }
        return this.context.toRecord(values);
    }
    
    public final String[] parseLine(final String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        this.lineReader.setLine(line);
        if (this.context == null || this.context.isStopped()) {
            this.beginParsing(this.lineReader);
        }
        else {
            ((DefaultCharInputReader)this.input).reloadBuffer();
        }
        try {
            while (!this.context.isStopped()) {
                this.input.markRecordStart();
                this.ch = this.input.nextChar();
                if (this.inComment()) {
                    this.processComment();
                    return null;
                }
                this.parseRecord();
                final String[] row = this.output.rowParsed();
                if (row != null) {
                    if (this.processor != NoopProcessor.instance) {
                        this.rowProcessed(row);
                    }
                    return row;
                }
            }
            return null;
        }
        catch (EOFException ex3) {
            return this.handleEOF();
        }
        catch (NullPointerException ex) {
            if (this.input != null) {
                this.stopParsing(null);
            }
            throw new IllegalStateException("Error parsing next record.", ex);
        }
        catch (Throwable ex2) {
            try {
                ex2 = this.handleException(ex2);
            }
            finally {
                this.stopParsing(ex2);
            }
            return null;
        }
    }
    
    private void rowProcessed(final String[] row) {
        Internal.process(row, this.processor, this.context, this.errorHandler);
    }
    
    public final void parse(final File file) {
        this.parse(ArgumentUtils.newReader(file));
    }
    
    public final void parse(final File file, final String encoding) {
        this.parse(ArgumentUtils.newReader(file, encoding));
    }
    
    public final void parse(final File file, final Charset encoding) {
        this.parse(ArgumentUtils.newReader(file, encoding));
    }
    
    public final void parse(final InputStream input) {
        this.parse(ArgumentUtils.newReader(input));
    }
    
    public final void parse(final InputStream input, final String encoding) {
        this.parse(ArgumentUtils.newReader(input, encoding));
    }
    
    public final void parse(final InputStream input, final Charset encoding) {
        this.parse(ArgumentUtils.newReader(input, encoding));
    }
    
    public final void beginParsing(final File file) {
        this.beginParsing(ArgumentUtils.newReader(file));
    }
    
    public final void beginParsing(final File file, final String encoding) {
        this.beginParsing(ArgumentUtils.newReader(file, encoding));
    }
    
    public final void beginParsing(final File file, final Charset encoding) {
        this.beginParsing(ArgumentUtils.newReader(file, encoding));
    }
    
    public final void beginParsing(final InputStream input) {
        this.beginParsing(ArgumentUtils.newReader(input));
    }
    
    public final void beginParsing(final InputStream input, final String encoding) {
        this.beginParsing(ArgumentUtils.newReader(input, encoding));
    }
    
    public final void beginParsing(final InputStream input, final Charset encoding) {
        this.beginParsing(ArgumentUtils.newReader(input, encoding));
    }
    
    public final List<String[]> parseAll(final File file, final int expectedRowCount) {
        return this.parseAll(ArgumentUtils.newReader(file), expectedRowCount);
    }
    
    public final List<String[]> parseAll(final File file, final String encoding, final int expectedRowCount) {
        return this.parseAll(ArgumentUtils.newReader(file, encoding), expectedRowCount);
    }
    
    public final List<String[]> parseAll(final File file, final Charset encoding, final int expectedRowCount) {
        return this.parseAll(ArgumentUtils.newReader(file, encoding), expectedRowCount);
    }
    
    public final List<String[]> parseAll(final InputStream input, final int expectedRowCount) {
        return this.parseAll(ArgumentUtils.newReader(input), expectedRowCount);
    }
    
    public final List<String[]> parseAll(final InputStream input, final String encoding, final int expectedRowCount) {
        return this.parseAll(ArgumentUtils.newReader(input, encoding), expectedRowCount);
    }
    
    public final List<String[]> parseAll(final InputStream input, final Charset encoding, final int expectedRowCount) {
        return this.parseAll(ArgumentUtils.newReader(input, encoding), expectedRowCount);
    }
    
    public final List<String[]> parseAll(final File file) {
        return this.parseAll(ArgumentUtils.newReader(file));
    }
    
    public final List<String[]> parseAll(final File file, final String encoding) {
        return this.parseAll(ArgumentUtils.newReader(file, encoding));
    }
    
    public final List<String[]> parseAll(final File file, final Charset encoding) {
        return this.parseAll(ArgumentUtils.newReader(file, encoding));
    }
    
    public final List<String[]> parseAll(final InputStream input) {
        return this.parseAll(ArgumentUtils.newReader(input));
    }
    
    public final List<String[]> parseAll(final InputStream input, final String encoding) {
        return this.parseAll(ArgumentUtils.newReader(input, encoding));
    }
    
    public final List<String[]> parseAll(final InputStream input, final Charset encoding) {
        return this.parseAll(ArgumentUtils.newReader(input, encoding));
    }
    
    public final List<Record> parseAllRecords(final File file, final int expectedRowCount) {
        return this.parseAllRecords(ArgumentUtils.newReader(file), expectedRowCount);
    }
    
    public final List<Record> parseAllRecords(final File file, final String encoding, final int expectedRowCount) {
        return this.parseAllRecords(ArgumentUtils.newReader(file, encoding), expectedRowCount);
    }
    
    public final List<Record> parseAllRecords(final File file, final Charset encoding, final int expectedRowCount) {
        return this.parseAllRecords(ArgumentUtils.newReader(file, encoding), expectedRowCount);
    }
    
    public final List<Record> parseAllRecords(final InputStream input, final int expectedRowCount) {
        return this.parseAllRecords(ArgumentUtils.newReader(input), expectedRowCount);
    }
    
    public final List<Record> parseAllRecords(final InputStream input, final String encoding, final int expectedRowCount) {
        return this.parseAllRecords(ArgumentUtils.newReader(input, encoding), expectedRowCount);
    }
    
    public final List<Record> parseAllRecords(final InputStream input, final Charset encoding, final int expectedRowCount) {
        return this.parseAllRecords(ArgumentUtils.newReader(input, encoding), expectedRowCount);
    }
    
    public final List<Record> parseAllRecords(final File file) {
        return this.parseAllRecords(ArgumentUtils.newReader(file));
    }
    
    public final List<Record> parseAllRecords(final File file, final String encoding) {
        return this.parseAllRecords(ArgumentUtils.newReader(file, encoding));
    }
    
    public final List<Record> parseAllRecords(final File file, final Charset encoding) {
        return this.parseAllRecords(ArgumentUtils.newReader(file, encoding));
    }
    
    public final List<Record> parseAllRecords(final InputStream input) {
        return this.parseAllRecords(ArgumentUtils.newReader(input));
    }
    
    public final List<Record> parseAllRecords(final InputStream input, final String encoding) {
        return this.parseAllRecords(ArgumentUtils.newReader(input, encoding));
    }
    
    public final List<Record> parseAllRecords(final InputStream input, final Charset encoding) {
        return this.parseAllRecords(ArgumentUtils.newReader(input, encoding));
    }
    
    public final List<Record> parseAllRecords(final Reader reader, final int expectedRowCount) {
        return this.internalParseAllRecords(true, reader, expectedRowCount);
    }
    
    private List<Record> internalParseAllRecords(final boolean validateReader, final Reader reader, final int expectedRowCount) {
        final List<Record> out = this.beginParseAll(validateReader, reader, expectedRowCount);
        if (this.context.isStopped()) {
            return out;
        }
        Record record;
        while ((record = this.parseNextRecord()) != null) {
            out.add(record);
        }
        return out;
    }
    
    public final List<Record> parseAllRecords(final Reader reader) {
        return this.parseAllRecords(reader, 0);
    }
    
    public final Record parseNextRecord() {
        final String[] row = this.parseNext();
        if (row != null) {
            return this.context.toRecord(row);
        }
        return null;
    }
    
    final Map<Long, String> getComments() {
        return this.comments;
    }
    
    final String getLastComment() {
        return this.lastComment;
    }
    
    final String[] getParsedHeaders() {
        this.extractHeadersIfRequired();
        return this.output.parsedHeaders;
    }
    
    public final ParsingContext getContext() {
        return this.context;
    }
    
    public final RecordMetaData getRecordMetadata() {
        if (this.context == null) {
            throw new IllegalStateException("Record metadata not available. The parser has not been started.");
        }
        return this.context.recordMetaData();
    }
    
    public final IterableResult<String[], ParsingContext> iterate(final File input, final String encoding) {
        return this.iterate(input, Charset.forName(encoding));
    }
    
    public final IterableResult<String[], ParsingContext> iterate(final File input, final Charset encoding) {
        return new RowIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input, encoding);
            }
        };
    }
    
    public final IterableResult<String[], ParsingContext> iterate(final File input) {
        return new RowIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input);
            }
        };
    }
    
    public final IterableResult<String[], ParsingContext> iterate(final Reader input) {
        return new RowIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input);
            }
        };
    }
    
    public final IterableResult<String[], ParsingContext> iterate(final InputStream input, final String encoding) {
        return this.iterate(input, Charset.forName(encoding));
    }
    
    public final IterableResult<String[], ParsingContext> iterate(final InputStream input, final Charset encoding) {
        return new RowIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input, encoding);
            }
        };
    }
    
    public final IterableResult<String[], ParsingContext> iterate(final InputStream input) {
        return new RowIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input);
            }
        };
    }
    
    public final IterableResult<Record, ParsingContext> iterateRecords(final File input, final String encoding) {
        return this.iterateRecords(input, Charset.forName(encoding));
    }
    
    public final IterableResult<Record, ParsingContext> iterateRecords(final File input, final Charset encoding) {
        return new RecordIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input, encoding);
            }
        };
    }
    
    public final IterableResult<Record, ParsingContext> iterateRecords(final File input) {
        return new RecordIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input);
            }
        };
    }
    
    public final IterableResult<Record, ParsingContext> iterateRecords(final Reader input) {
        return new RecordIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input);
            }
        };
    }
    
    public final IterableResult<Record, ParsingContext> iterateRecords(final InputStream input, final String encoding) {
        return this.iterateRecords(input, Charset.forName(encoding));
    }
    
    public final IterableResult<Record, ParsingContext> iterateRecords(final InputStream input, final Charset encoding) {
        return new RecordIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input, encoding);
            }
        };
    }
    
    public final IterableResult<Record, ParsingContext> iterateRecords(final InputStream input) {
        return new RecordIterator(this) {
            @Override
            protected void beginParsing() {
                this.parser.beginParsing(input);
            }
        };
    }
}
