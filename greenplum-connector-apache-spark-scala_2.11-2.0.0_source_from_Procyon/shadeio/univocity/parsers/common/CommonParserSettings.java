// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.annotations.Headers;
import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.common.processor.core.AbstractMultiBeanProcessor;
import shadeio.univocity.parsers.common.processor.core.AbstractBeanProcessor;
import shadeio.univocity.parsers.common.processor.core.ColumnOrderDependent;
import java.util.Map;
import shadeio.univocity.parsers.common.input.ExpandingCharAppender;
import shadeio.univocity.parsers.common.input.DefaultCharAppender;
import shadeio.univocity.parsers.common.input.CharAppender;
import shadeio.univocity.parsers.common.fields.FieldSelector;
import shadeio.univocity.parsers.common.fields.FieldSet;
import shadeio.univocity.parsers.common.input.DefaultCharInputReader;
import shadeio.univocity.parsers.common.input.concurrent.ConcurrentCharInputReader;
import shadeio.univocity.parsers.common.input.CharInputReader;
import shadeio.univocity.parsers.common.processor.core.NoopProcessor;
import shadeio.univocity.parsers.common.processor.NoopRowProcessor;
import shadeio.univocity.parsers.common.processor.RowProcessor;
import shadeio.univocity.parsers.common.processor.core.Processor;

public abstract class CommonParserSettings<F extends Format> extends CommonSettings<F>
{
    protected Boolean headerExtractionEnabled;
    private Processor<? extends Context> processor;
    private boolean columnReorderingEnabled;
    private int inputBufferSize;
    private boolean readInputOnSeparateThread;
    private long numberOfRecordsToRead;
    private boolean lineSeparatorDetectionEnabled;
    private long numberOfRowsToSkip;
    private boolean commentCollectionEnabled;
    
    public CommonParserSettings() {
        this.headerExtractionEnabled = null;
        this.columnReorderingEnabled = true;
        this.inputBufferSize = 1048576;
        this.readInputOnSeparateThread = (Runtime.getRuntime().availableProcessors() > 1);
        this.numberOfRecordsToRead = -1L;
        this.lineSeparatorDetectionEnabled = false;
        this.numberOfRowsToSkip = 0L;
        this.commentCollectionEnabled = false;
    }
    
    public boolean getReadInputOnSeparateThread() {
        return this.readInputOnSeparateThread;
    }
    
    public void setReadInputOnSeparateThread(final boolean readInputOnSeparateThread) {
        this.readInputOnSeparateThread = readInputOnSeparateThread;
    }
    
    public boolean isHeaderExtractionEnabled() {
        return this.headerExtractionEnabled != null && this.headerExtractionEnabled;
    }
    
    public void setHeaderExtractionEnabled(final boolean headerExtractionEnabled) {
        this.headerExtractionEnabled = headerExtractionEnabled;
    }
    
    @Deprecated
    public RowProcessor getRowProcessor() {
        if (this.processor == null) {
            return NoopRowProcessor.instance;
        }
        return (RowProcessor)this.processor;
    }
    
    @Deprecated
    public void setRowProcessor(final RowProcessor processor) {
        this.processor = processor;
    }
    
    public <T extends Context> Processor<T> getProcessor() {
        if (this.processor == null) {
            return (Processor<T>)NoopProcessor.instance;
        }
        return (Processor<T>)this.processor;
    }
    
    public void setProcessor(final Processor<? extends Context> processor) {
        this.processor = processor;
    }
    
    protected CharInputReader newCharInputReader(final int whitespaceRangeStart) {
        if (this.readInputOnSeparateThread) {
            if (this.lineSeparatorDetectionEnabled) {
                return new ConcurrentCharInputReader(this.getFormat().getNormalizedNewline(), this.getInputBufferSize(), 10, whitespaceRangeStart);
            }
            return new ConcurrentCharInputReader(this.getFormat().getLineSeparator(), this.getFormat().getNormalizedNewline(), this.getInputBufferSize(), 10, whitespaceRangeStart);
        }
        else {
            if (this.lineSeparatorDetectionEnabled) {
                return new DefaultCharInputReader(this.getFormat().getNormalizedNewline(), this.getInputBufferSize(), whitespaceRangeStart);
            }
            return new DefaultCharInputReader(this.getFormat().getLineSeparator(), this.getFormat().getNormalizedNewline(), this.getInputBufferSize(), whitespaceRangeStart);
        }
    }
    
    public long getNumberOfRecordsToRead() {
        return this.numberOfRecordsToRead;
    }
    
    public void setNumberOfRecordsToRead(final long numberOfRecordsToRead) {
        this.numberOfRecordsToRead = numberOfRecordsToRead;
    }
    
    public boolean isColumnReorderingEnabled() {
        return !this.preventReordering() && this.columnReorderingEnabled;
    }
    
    @Override
    FieldSet<?> getFieldSet() {
        return this.preventReordering() ? null : super.getFieldSet();
    }
    
    @Override
    FieldSelector getFieldSelector() {
        return this.preventReordering() ? null : super.getFieldSelector();
    }
    
    public void setColumnReorderingEnabled(final boolean columnReorderingEnabled) {
        if (columnReorderingEnabled && this.preventReordering()) {
            throw new IllegalArgumentException("Cannot reorder columns when using a row processor that manipulates nested rows.");
        }
        this.columnReorderingEnabled = columnReorderingEnabled;
    }
    
    public int getInputBufferSize() {
        return this.inputBufferSize;
    }
    
    public void setInputBufferSize(final int inputBufferSize) {
        this.inputBufferSize = inputBufferSize;
    }
    
    protected CharAppender newCharAppender() {
        final int chars = this.getMaxCharsPerColumn();
        if (chars != -1) {
            return new DefaultCharAppender(chars, this.getNullValue(), this.getWhitespaceRangeStart());
        }
        return new ExpandingCharAppender(this.getNullValue(), this.getWhitespaceRangeStart());
    }
    
    public final boolean isLineSeparatorDetectionEnabled() {
        return this.lineSeparatorDetectionEnabled;
    }
    
    public final void setLineSeparatorDetectionEnabled(final boolean lineSeparatorDetectionEnabled) {
        this.lineSeparatorDetectionEnabled = lineSeparatorDetectionEnabled;
    }
    
    public final long getNumberOfRowsToSkip() {
        return this.numberOfRowsToSkip;
    }
    
    public final void setNumberOfRowsToSkip(final long numberOfRowsToSkip) {
        if (numberOfRowsToSkip < 0L) {
            throw new IllegalArgumentException("Number of rows to skip from the input must be 0 or greater");
        }
        this.numberOfRowsToSkip = numberOfRowsToSkip;
    }
    
    @Override
    protected void addConfiguration(final Map<String, Object> out) {
        super.addConfiguration(out);
        out.put("Header extraction enabled", this.headerExtractionEnabled);
        out.put("Processor", (this.processor == null) ? "none" : this.processor.getClass().getName());
        out.put("Column reordering enabled", this.columnReorderingEnabled);
        out.put("Input buffer size", this.inputBufferSize);
        out.put("Input reading on separate thread", this.readInputOnSeparateThread);
        out.put("Number of records to read", (this.numberOfRecordsToRead == -1L) ? "all" : Long.valueOf(this.numberOfRecordsToRead));
        out.put("Line separator detection enabled", this.lineSeparatorDetectionEnabled);
    }
    
    private boolean preventReordering() {
        return this.processor instanceof ColumnOrderDependent && ((ColumnOrderDependent)this.processor).preventColumnReordering();
    }
    
    public boolean isCommentCollectionEnabled() {
        return this.commentCollectionEnabled;
    }
    
    public void setCommentCollectionEnabled(final boolean commentCollectionEnabled) {
        this.commentCollectionEnabled = commentCollectionEnabled;
    }
    
    @Override
    final void runAutomaticConfiguration() {
        Class<?> beanClass = null;
        if (this.processor instanceof AbstractBeanProcessor) {
            beanClass = (Class<?>)((AbstractBeanProcessor)this.processor).getBeanClass();
        }
        else if (this.processor instanceof AbstractMultiBeanProcessor) {
            final Class[] classes = ((AbstractMultiBeanProcessor)this.processor).getBeanClasses();
            if (classes.length > 0) {
                beanClass = (Class<?>)classes[0];
            }
        }
        if (beanClass != null) {
            this.configureFromAnnotations(beanClass);
        }
    }
    
    protected synchronized void configureFromAnnotations(final Class<?> beanClass) {
        if (!this.deriveHeadersFrom(beanClass)) {
            return;
        }
        final Headers headerAnnotation = AnnotationHelper.findHeadersAnnotation(beanClass);
        String[] headersFromBean = ArgumentUtils.EMPTY_STRING_ARRAY;
        final boolean allFieldsIndexBased = AnnotationHelper.allFieldsIndexBasedForParsing(beanClass);
        boolean extractHeaders = !allFieldsIndexBased;
        if (headerAnnotation != null) {
            if (headerAnnotation.sequence().length > 0) {
                headersFromBean = headerAnnotation.sequence();
            }
            extractHeaders = headerAnnotation.extract();
        }
        if (this.headerExtractionEnabled == null) {
            this.setHeaderExtractionEnabled(extractHeaders);
        }
        if (this.getHeaders() == null && headersFromBean.length > 0 && !this.headerExtractionEnabled) {
            this.setHeadersDerivedFromClass(beanClass, headersFromBean);
        }
        if (this.getFieldSet() == null) {
            if (allFieldsIndexBased) {
                this.selectIndexes(AnnotationHelper.getSelectedIndexes(beanClass, MethodFilter.ONLY_SETTERS));
            }
            else if (headersFromBean.length > 0 && AnnotationHelper.allFieldsNameBasedForParsing(beanClass)) {
                this.selectFields(headersFromBean);
            }
        }
    }
    
    @Override
    protected CommonParserSettings clone(final boolean clearInputSpecificSettings) {
        return (CommonParserSettings)super.clone(clearInputSpecificSettings);
    }
    
    @Override
    protected CommonParserSettings clone() {
        return (CommonParserSettings)super.clone();
    }
    
    @Override
    protected void clearInputSpecificSettings() {
        super.clearInputSpecificSettings();
        this.processor = null;
        this.numberOfRecordsToRead = -1L;
        this.numberOfRowsToSkip = 0L;
    }
}
