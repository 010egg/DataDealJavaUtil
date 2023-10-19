// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.annotations.Headers;
import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.common.processor.BeanWriterProcessor;
import java.util.Map;
import shadeio.univocity.parsers.common.processor.RowWriterProcessor;

public abstract class CommonWriterSettings<F extends Format> extends CommonSettings<F>
{
    private RowWriterProcessor<?> rowWriterProcessor;
    private Boolean headerWritingEnabled;
    private String emptyValue;
    private boolean expandIncompleteRows;
    private boolean columnReorderingEnabled;
    
    public CommonWriterSettings() {
        this.headerWritingEnabled = null;
        this.emptyValue = "";
        this.expandIncompleteRows = false;
        this.columnReorderingEnabled = false;
    }
    
    public String getEmptyValue() {
        return this.emptyValue;
    }
    
    public void setEmptyValue(final String emptyValue) {
        this.emptyValue = emptyValue;
    }
    
    public RowWriterProcessor<?> getRowWriterProcessor() {
        return this.rowWriterProcessor;
    }
    
    public void setRowWriterProcessor(final RowWriterProcessor<?> rowWriterProcessor) {
        this.rowWriterProcessor = rowWriterProcessor;
    }
    
    public final boolean isHeaderWritingEnabled() {
        return this.headerWritingEnabled != null && this.headerWritingEnabled;
    }
    
    public final void setHeaderWritingEnabled(final boolean headerWritingEnabled) {
        this.headerWritingEnabled = headerWritingEnabled;
    }
    
    public final boolean getExpandIncompleteRows() {
        return this.expandIncompleteRows;
    }
    
    public final void setExpandIncompleteRows(final boolean expandIncompleteRows) {
        this.expandIncompleteRows = expandIncompleteRows;
    }
    
    @Override
    protected void addConfiguration(final Map<String, Object> out) {
        super.addConfiguration(out);
        out.put("Empty value", this.emptyValue);
        out.put("Header writing enabled", this.headerWritingEnabled);
        out.put("Row processor", (this.rowWriterProcessor == null) ? "none" : this.rowWriterProcessor.getClass().getName());
    }
    
    @Override
    final void runAutomaticConfiguration() {
        if (this.rowWriterProcessor instanceof BeanWriterProcessor) {
            final Class<?> beanClass = (Class<?>)((BeanWriterProcessor)this.rowWriterProcessor).getBeanClass();
            this.configureFromAnnotations(beanClass);
        }
    }
    
    protected void configureFromAnnotations(final Class<?> beanClass) {
        if (!this.deriveHeadersFrom(beanClass)) {
            return;
        }
        final Headers headerAnnotation = AnnotationHelper.findHeadersAnnotation(beanClass);
        String[] headersFromBean = AnnotationHelper.deriveHeaderNamesFromFields(beanClass, MethodFilter.ONLY_GETTERS);
        boolean writeHeaders = false;
        if (headerAnnotation != null) {
            if (headerAnnotation.sequence().length > 0) {
                headersFromBean = headerAnnotation.sequence();
            }
            writeHeaders = headerAnnotation.write();
        }
        if (this.headerWritingEnabled == null) {
            this.headerWritingEnabled = writeHeaders;
        }
        if (this.getHeaders() == null && headersFromBean.length > 0) {
            this.setHeadersDerivedFromClass(beanClass, headersFromBean);
        }
    }
    
    @Override
    protected CommonWriterSettings clone(final boolean clearInputSpecificSettings) {
        return (CommonWriterSettings)super.clone(clearInputSpecificSettings);
    }
    
    @Override
    protected CommonWriterSettings clone() {
        return (CommonWriterSettings)super.clone();
    }
    
    @Override
    protected void clearInputSpecificSettings() {
        super.clearInputSpecificSettings();
        this.rowWriterProcessor = null;
    }
    
    public boolean isColumnReorderingEnabled() {
        return this.columnReorderingEnabled;
    }
    
    public void setColumnReorderingEnabled(final boolean columnReorderingEnabled) {
        this.columnReorderingEnabled = columnReorderingEnabled;
    }
}
