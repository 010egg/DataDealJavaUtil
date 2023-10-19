// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.routine;

import shadeio.univocity.parsers.common.processor.AbstractRowProcessor;
import java.util.Iterator;
import shadeio.univocity.parsers.common.ResultIterator;
import shadeio.univocity.parsers.common.processor.BeanProcessor;
import shadeio.univocity.parsers.common.IterableResult;
import shadeio.univocity.parsers.common.processor.BeanListProcessor;
import java.io.InputStream;
import java.util.List;
import shadeio.univocity.parsers.common.processor.BeanWriterProcessor;
import shadeio.univocity.parsers.common.Context;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.RowProcessor;
import shadeio.univocity.parsers.common.processor.RowWriterProcessor;
import java.io.Reader;
import java.sql.ResultSetMetaData;
import shadeio.univocity.parsers.common.TextWritingException;
import java.io.OutputStream;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.nio.charset.Charset;
import java.io.File;
import java.sql.ResultSet;
import shadeio.univocity.parsers.common.AbstractWriter;
import shadeio.univocity.parsers.common.AbstractParser;
import java.io.Writer;
import shadeio.univocity.parsers.common.CommonWriterSettings;
import shadeio.univocity.parsers.common.CommonParserSettings;

public abstract class AbstractRoutines<P extends CommonParserSettings<?>, W extends CommonWriterSettings<?>>
{
    private boolean keepResourcesOpen;
    private Writer previousOutput;
    private final String routineDescription;
    private P parserSettings;
    private W writerSettings;
    
    protected abstract AbstractParser<P> createParser(final P p0);
    
    protected abstract AbstractWriter<W> createWriter(final Writer p0, final W p1);
    
    protected abstract P createDefaultParserSettings();
    
    protected abstract W createDefaultWriterSettings();
    
    public AbstractRoutines(final String routineDescription) {
        this(routineDescription, null, null);
    }
    
    public AbstractRoutines(final String routineDescription, final P parserSettings) {
        this(routineDescription, parserSettings, null);
    }
    
    public AbstractRoutines(final String routineDescription, final W writerSettings) {
        this(routineDescription, null, writerSettings);
    }
    
    public AbstractRoutines(final String routineDescription, final P parserSettings, final W writerSettings) {
        this.keepResourcesOpen = false;
        this.routineDescription = routineDescription;
        this.parserSettings = parserSettings;
        this.writerSettings = writerSettings;
    }
    
    private void validateWriterSettings() {
        if (this.writerSettings == null) {
            this.writerSettings = this.createDefaultWriterSettings();
        }
    }
    
    private void validateParserSettings() {
        if (this.parserSettings == null) {
            this.parserSettings = this.createDefaultParserSettings();
        }
    }
    
    public final P getParserSettings() {
        this.validateParserSettings();
        return this.parserSettings;
    }
    
    public final void setParserSettings(final P parserSettings) {
        this.parserSettings = parserSettings;
    }
    
    public final W getWriterSettings() {
        this.validateWriterSettings();
        return this.writerSettings;
    }
    
    public final void setWriterSettings(final W writerSettings) {
        this.writerSettings = writerSettings;
    }
    
    protected void adjustColumnLengths(final String[] headers, final int[] lengths) {
    }
    
    public final void write(final ResultSet rs, final File output) {
        this.write(rs, output, (Charset)null);
    }
    
    public final void write(final ResultSet rs, final File output, final String encoding) {
        this.write(rs, output, Charset.forName(encoding));
    }
    
    public final void write(final ResultSet rs, final File output, final Charset encoding) {
        final Writer writer = ArgumentUtils.newWriter(output, encoding);
        try {
            this.write(rs, writer);
        }
        finally {
            try {
                writer.close();
            }
            catch (Exception e) {
                throw new IllegalStateException("Error closing file: '" + output.getAbsolutePath() + "'", e);
            }
        }
    }
    
    public final void write(final ResultSet rs, final OutputStream output) {
        this.write(rs, ArgumentUtils.newWriter(output));
    }
    
    public final void write(final ResultSet rs, final OutputStream output, final String encoding) {
        this.write(rs, ArgumentUtils.newWriter(output, encoding));
    }
    
    public final void write(final ResultSet rs, final OutputStream output, final Charset encoding) {
        this.write(rs, ArgumentUtils.newWriter(output, encoding));
    }
    
    public final void write(final ResultSet rs, final Writer output) {
        this.validateWriterSettings();
        final boolean hasWriterProcessor = this.writerSettings.getRowWriterProcessor() != null;
        AbstractWriter<W> writer = null;
        long rowCount = 0L;
        Object[] row = null;
        try {
            try {
                final ResultSetMetaData md = rs.getMetaData();
                final int columns = md.getColumnCount();
                String[] headers = new String[columns];
                final int[] lengths = new int[columns];
                for (int i = 1; i <= columns; ++i) {
                    headers[i - 1] = md.getColumnLabel(i);
                    final int precision = md.getPrecision(i);
                    final int scale = md.getScale(i);
                    int length;
                    if (precision != 0 && scale != 0) {
                        length = precision + scale + 2;
                    }
                    else {
                        length = precision + scale;
                    }
                    lengths[i - 1] = length;
                }
                final String[] userProvidedHeaders = this.writerSettings.getHeaders();
                if (userProvidedHeaders == null) {
                    this.writerSettings.setHeaders(headers);
                }
                else {
                    headers = userProvidedHeaders;
                }
                this.adjustColumnLengths(headers, lengths);
                writer = this.createWriter(output, this.writerSettings);
                if (this.writerSettings.isHeaderWritingEnabled()) {
                    writer.writeHeaders();
                }
                row = new Object[columns];
                while (rs.next()) {
                    for (int j = 1; j <= columns; ++j) {
                        row[j - 1] = rs.getObject(j);
                    }
                    if (hasWriterProcessor) {
                        writer.processRecord(row);
                    }
                    else {
                        writer.writeRow(row);
                    }
                    ++rowCount;
                }
            }
            finally {
                if (!this.keepResourcesOpen) {
                    rs.close();
                }
            }
        }
        catch (Exception e) {
            throw new TextWritingException("Error writing data from result set", rowCount, row, e);
        }
        finally {
            this.close(writer);
        }
    }
    
    public final void parseAndWrite(final Reader input, final Writer output) {
        this.setRowWriterProcessor(null);
        this.setRowProcessor(this.createWritingRowProcessor(output));
        try {
            final AbstractParser<P> parser = this.createParser(this.parserSettings);
            parser.parse(input);
        }
        finally {
            this.parserSettings.setRowProcessor(null);
        }
    }
    
    private void setRowWriterProcessor(final RowWriterProcessor rowWriterProcessor) {
        this.validateWriterSettings();
        this.writerSettings.setRowWriterProcessor(rowWriterProcessor);
    }
    
    private void setRowProcessor(final RowProcessor rowProcessor) {
        this.validateParserSettings();
        this.parserSettings.setRowProcessor(rowProcessor);
    }
    
    private RowProcessor createWritingRowProcessor(final Writer output) {
        return new RowProcessor() {
            private AbstractWriter<W> writer;
            
            @Override
            public void processStarted(final ParsingContext context) {
                this.writer = AbstractRoutines.this.createWriter(output, AbstractRoutines.this.writerSettings);
            }
            
            @Override
            public void rowProcessed(final String[] row, final ParsingContext context) {
                this.writer.writeRow(row);
            }
            
            @Override
            public void processEnded(final ParsingContext context) {
                AbstractRoutines.this.close(this.writer);
            }
        };
    }
    
    private void close(final AbstractWriter writer) {
        if (writer != null) {
            if (!this.keepResourcesOpen) {
                writer.close();
            }
            else {
                writer.flush();
            }
        }
    }
    
    public <T> void writeAll(final Iterable<T> elements, final Class<T> beanType, final File output, final String... headers) {
        this.writeAll(elements, beanType, ArgumentUtils.newWriter(output), headers);
    }
    
    public <T> void writeAll(final Iterable<T> elements, final Class<T> beanType, final File output, final String encoding, final String[] headers) {
        this.writeAll(elements, beanType, ArgumentUtils.newWriter(output, encoding), headers);
    }
    
    public <T> void writeAll(final Iterable<T> elements, final Class<T> beanType, final File output, final Charset encoding, final String... headers) {
        this.writeAll(elements, beanType, ArgumentUtils.newWriter(output, encoding), headers);
    }
    
    public <T> void writeAll(final Iterable<T> elements, final Class<T> beanType, final OutputStream output, final String... headers) {
        this.writeAll(elements, beanType, ArgumentUtils.newWriter(output), headers);
    }
    
    public <T> void writeAll(final Iterable<T> elements, final Class<T> beanType, final OutputStream output, final String encoding, final String[] headers) {
        this.writeAll(elements, beanType, ArgumentUtils.newWriter(output, encoding), headers);
    }
    
    public <T> void writeAll(final Iterable<T> elements, final Class<T> beanType, final OutputStream output, final Charset encoding, final String... headers) {
        this.writeAll(elements, beanType, ArgumentUtils.newWriter(output, encoding), headers);
    }
    
    public <T> void writeAll(final Iterable<T> elements, final Class<T> beanType, final Writer output, final String... headers) {
        this.setRowWriterProcessor(new BeanWriterProcessor(beanType));
        try {
            if (headers.length > 0) {
                this.writerSettings.setHeaders(headers);
                this.writerSettings.setHeaderWritingEnabled(true);
            }
            if (this.keepResourcesOpen && this.previousOutput == output) {
                this.writerSettings.setHeaderWritingEnabled(false);
            }
            final AbstractWriter<W> writer = this.createWriter(output, this.writerSettings);
            if (this.keepResourcesOpen) {
                writer.processRecords(elements);
                this.previousOutput = output;
            }
            else {
                writer.processRecordsAndClose(elements);
            }
        }
        finally {
            this.writerSettings.setRowWriterProcessor(null);
        }
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final File input, final int expectedBeanCount) {
        return this.parseAll(beanType, ArgumentUtils.newReader(input), expectedBeanCount);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final File input, final String encoding, final int expectedBeanCount) {
        return this.parseAll(beanType, ArgumentUtils.newReader(input, encoding), expectedBeanCount);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final File input, final Charset encoding, final int expectedBeanCount) {
        return this.parseAll(beanType, ArgumentUtils.newReader(input, encoding), expectedBeanCount);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final InputStream input, final int expectedBeanCount) {
        return this.parseAll(beanType, ArgumentUtils.newReader(input), expectedBeanCount);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final InputStream input, final String encoding, final int expectedBeanCount) {
        return this.parseAll(beanType, ArgumentUtils.newReader(input, encoding), expectedBeanCount);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final InputStream input, final Charset encoding, final int expectedBeanCount) {
        return this.parseAll(beanType, ArgumentUtils.newReader(input, encoding), expectedBeanCount);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final Reader input, final int expectedBeanCount) {
        final BeanListProcessor processor = new BeanListProcessor((Class<T>)beanType, expectedBeanCount);
        this.setRowProcessor(processor);
        try {
            this.createParser(this.parserSettings).parse(input);
            return (List<T>)processor.getBeans();
        }
        finally {
            this.parserSettings.setRowProcessor(null);
        }
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final File input) {
        return this.parseAll(beanType, input, 0);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final File input, final String encoding) {
        return this.parseAll(beanType, input, encoding, 0);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final File input, final Charset encoding) {
        return this.parseAll(beanType, input, encoding, 0);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final InputStream input) {
        return this.parseAll(beanType, input, 0);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final InputStream input, final String encoding) {
        return this.parseAll(beanType, input, encoding, 0);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final InputStream input, final Charset encoding) {
        return this.parseAll(beanType, input, encoding, 0);
    }
    
    public <T> List<T> parseAll(final Class<T> beanType, final Reader input) {
        return this.parseAll(beanType, input, 0);
    }
    
    public <T> IterableResult<T, ParsingContext> iterate(final Class<T> beanType, final File input) {
        return this.iterate(beanType, ArgumentUtils.newReader(input));
    }
    
    public <T> IterableResult<T, ParsingContext> iterate(final Class<T> beanType, final File input, final String encoding) {
        return this.iterate(beanType, ArgumentUtils.newReader(input, encoding));
    }
    
    public <T> IterableResult<T, ParsingContext> iterate(final Class<T> beanType, final File input, final Charset encoding) {
        return this.iterate(beanType, ArgumentUtils.newReader(input, encoding));
    }
    
    public <T> IterableResult<T, ParsingContext> iterate(final Class<T> beanType, final InputStream input) {
        return this.iterate(beanType, ArgumentUtils.newReader(input));
    }
    
    public <T> IterableResult<T, ParsingContext> iterate(final Class<T> beanType, final InputStream input, final String encoding) {
        return this.iterate(beanType, ArgumentUtils.newReader(input, encoding));
    }
    
    public <T> IterableResult<T, ParsingContext> iterate(final Class<T> beanType, final InputStream input, final Charset encoding) {
        return this.iterate(beanType, ArgumentUtils.newReader(input, encoding));
    }
    
    public <T> IterableResult<T, ParsingContext> iterate(final Class<T> beanType, final Reader input) {
        final Object[] beanHolder = { null };
        this.setRowProcessor(new BeanProcessor<T>(beanType) {
            @Override
            public void beanProcessed(final T bean, final ParsingContext context) {
                beanHolder[0] = bean;
            }
            
            @Override
            public void processEnded(final ParsingContext context) {
                super.processEnded(context);
                AbstractRoutines.this.parserSettings.setRowProcessor(null);
            }
        });
        return new IterableResult<T, ParsingContext>() {
            private ParsingContext context;
            
            @Override
            public ParsingContext getContext() {
                return this.context;
            }
            
            @Override
            public ResultIterator<T, ParsingContext> iterator() {
                final AbstractParser<P> parser = AbstractRoutines.this.createParser(AbstractRoutines.this.parserSettings);
                parser.beginParsing(input);
                this.context = parser.getContext();
                return new ResultIterator<T, ParsingContext>() {
                    String[] row;
                    
                    @Override
                    public boolean hasNext() {
                        return beanHolder[0] != null || this.row != null || (this.row = parser.parseNext()) != null;
                    }
                    
                    @Override
                    public T next() {
                        T out = (T)beanHolder[0];
                        if (out == null && this.hasNext()) {
                            out = (T)beanHolder[0];
                        }
                        beanHolder[0] = null;
                        this.row = null;
                        return out;
                    }
                    
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Can't remove beans");
                    }
                    
                    @Override
                    public ParsingContext getContext() {
                        return IterableResult.this.context;
                    }
                };
            }
        };
    }
    
    @Override
    public String toString() {
        return this.routineDescription;
    }
    
    public InputDimension getInputDimension(final File input) {
        return this.getInputDimension(ArgumentUtils.newReader(input));
    }
    
    public InputDimension getInputDimension(final File input, final String encoding) {
        return this.getInputDimension(ArgumentUtils.newReader(input, encoding));
    }
    
    public InputDimension getInputDimension(final InputStream input) {
        return this.getInputDimension(ArgumentUtils.newReader(input));
    }
    
    public InputDimension getInputDimension(final InputStream input, final String encoding) {
        return this.getInputDimension(ArgumentUtils.newReader(input, encoding));
    }
    
    public InputDimension getInputDimension(final Reader input) {
        final InputDimension out = new InputDimension();
        this.setRowProcessor(new AbstractRowProcessor() {
            int lastColumn;
            
            @Override
            public void rowProcessed(final String[] row, final ParsingContext context) {
                if (this.lastColumn < row.length) {
                    this.lastColumn = row.length;
                }
            }
            
            @Override
            public void processEnded(final ParsingContext context) {
                out.rows = context.currentRecord() + 1L;
                out.columns = this.lastColumn;
            }
        });
        final P settings = this.getParserSettings();
        settings.setMaxCharsPerColumn(-1);
        if (settings.getMaxColumns() < 1000000) {
            settings.setMaxColumns(1000000);
        }
        settings.selectIndexes(new Integer[0]);
        settings.setColumnReorderingEnabled(false);
        this.createParser(settings).parse(input);
        return out;
    }
    
    public boolean getKeepResourcesOpen() {
        return this.keepResourcesOpen;
    }
    
    public void setKeepResourcesOpen(final boolean keepResourcesOpen) {
        this.keepResourcesOpen = keepResourcesOpen;
    }
}
