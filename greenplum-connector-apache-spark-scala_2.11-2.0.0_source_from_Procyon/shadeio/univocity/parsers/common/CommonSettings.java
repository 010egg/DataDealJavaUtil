// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import shadeio.univocity.parsers.common.fields.ExcludeFieldEnumSelector;
import shadeio.univocity.parsers.common.fields.FieldEnumSelector;
import shadeio.univocity.parsers.common.fields.ExcludeFieldIndexSelector;
import shadeio.univocity.parsers.common.fields.FieldIndexSelector;
import shadeio.univocity.parsers.common.fields.ExcludeFieldNameSelector;
import shadeio.univocity.parsers.common.fields.FieldNameSelector;
import shadeio.univocity.parsers.common.fields.FieldSet;
import shadeio.univocity.parsers.common.fields.FieldSelector;

public abstract class CommonSettings<F extends Format> implements Cloneable
{
    private F format;
    private String nullValue;
    private int maxCharsPerColumn;
    private int maxColumns;
    private boolean skipEmptyLines;
    private boolean ignoreTrailingWhitespaces;
    private boolean ignoreLeadingWhitespaces;
    private FieldSelector fieldSelector;
    private boolean autoConfigurationEnabled;
    private ProcessorErrorHandler<? extends Context> errorHandler;
    private int errorContentLength;
    private boolean skipBitsAsWhitespace;
    private String[] headers;
    Class<?> headerSourceClass;
    
    public CommonSettings() {
        this.nullValue = null;
        this.maxCharsPerColumn = 4096;
        this.maxColumns = 512;
        this.skipEmptyLines = true;
        this.ignoreTrailingWhitespaces = true;
        this.ignoreLeadingWhitespaces = true;
        this.fieldSelector = null;
        this.autoConfigurationEnabled = true;
        this.errorContentLength = -1;
        this.skipBitsAsWhitespace = true;
        this.setFormat(this.createDefaultFormat());
    }
    
    public String getNullValue() {
        return this.nullValue;
    }
    
    public void setNullValue(final String emptyValue) {
        this.nullValue = emptyValue;
    }
    
    public int getMaxCharsPerColumn() {
        return this.maxCharsPerColumn;
    }
    
    public void setMaxCharsPerColumn(final int maxCharsPerColumn) {
        this.maxCharsPerColumn = maxCharsPerColumn;
    }
    
    public boolean getSkipEmptyLines() {
        return this.skipEmptyLines;
    }
    
    public void setSkipEmptyLines(final boolean skipEmptyLines) {
        this.skipEmptyLines = skipEmptyLines;
    }
    
    public boolean getIgnoreTrailingWhitespaces() {
        return this.ignoreTrailingWhitespaces;
    }
    
    public void setIgnoreTrailingWhitespaces(final boolean ignoreTrailingWhitespaces) {
        this.ignoreTrailingWhitespaces = ignoreTrailingWhitespaces;
    }
    
    public boolean getIgnoreLeadingWhitespaces() {
        return this.ignoreLeadingWhitespaces;
    }
    
    public void setIgnoreLeadingWhitespaces(final boolean ignoreLeadingWhitespaces) {
        this.ignoreLeadingWhitespaces = ignoreLeadingWhitespaces;
    }
    
    public void setHeaders(final String... headers) {
        if (headers == null || headers.length == 0) {
            this.headers = null;
        }
        else {
            this.headers = headers;
        }
    }
    
    void setHeadersDerivedFromClass(final Class<?> headerSourceClass, final String... headers) {
        this.headerSourceClass = headerSourceClass;
        this.setHeaders(headers);
    }
    
    boolean deriveHeadersFrom(final Class<?> beanClass) {
        if (this.headerSourceClass != null) {
            if (this.headerSourceClass == beanClass) {
                return false;
            }
            this.setHeaders((String[])null);
        }
        return true;
    }
    
    public String[] getHeaders() {
        return this.headers;
    }
    
    public int getMaxColumns() {
        return this.maxColumns;
    }
    
    public void setMaxColumns(final int maxColumns) {
        this.maxColumns = maxColumns;
    }
    
    public F getFormat() {
        return this.format;
    }
    
    public void setFormat(final F format) {
        if (format == null) {
            throw new IllegalArgumentException("Format cannot be null");
        }
        this.format = format;
    }
    
    public FieldSet<String> selectFields(final String... fieldNames) {
        return this.setFieldSet(new FieldNameSelector(), fieldNames);
    }
    
    public FieldSet<String> excludeFields(final String... fieldNames) {
        return this.setFieldSet(new ExcludeFieldNameSelector(), fieldNames);
    }
    
    public FieldSet<Integer> selectIndexes(final Integer... fieldIndexes) {
        return this.setFieldSet(new FieldIndexSelector(), fieldIndexes);
    }
    
    public FieldSet<Integer> excludeIndexes(final Integer... fieldIndexes) {
        return this.setFieldSet(new ExcludeFieldIndexSelector(), fieldIndexes);
    }
    
    public FieldSet<Enum> selectFields(final Enum... columns) {
        return (FieldSet<Enum>)this.setFieldSet(new FieldEnumSelector(), (Enum[])columns);
    }
    
    public FieldSet<Enum> excludeFields(final Enum... columns) {
        return (FieldSet<Enum>)this.setFieldSet(new ExcludeFieldEnumSelector(), (Enum[])columns);
    }
    
    private <T> FieldSet<T> setFieldSet(final FieldSet<T> fieldSet, final T... values) {
        this.fieldSelector = (FieldSelector)fieldSet;
        fieldSet.add(values);
        return fieldSet;
    }
    
    FieldSet<?> getFieldSet() {
        return (FieldSet<?>)this.fieldSelector;
    }
    
    FieldSelector getFieldSelector() {
        return this.fieldSelector;
    }
    
    public final boolean isAutoConfigurationEnabled() {
        return this.autoConfigurationEnabled;
    }
    
    public final void setAutoConfigurationEnabled(final boolean autoConfigurationEnabled) {
        this.autoConfigurationEnabled = autoConfigurationEnabled;
    }
    
    @Deprecated
    public RowProcessorErrorHandler getRowProcessorErrorHandler() {
        return (RowProcessorErrorHandler)((this.errorHandler == null) ? NoopRowProcessorErrorHandler.instance : this.errorHandler);
    }
    
    @Deprecated
    public void setRowProcessorErrorHandler(final RowProcessorErrorHandler rowProcessorErrorHandler) {
        this.errorHandler = rowProcessorErrorHandler;
    }
    
    public <T extends Context> ProcessorErrorHandler<T> getProcessorErrorHandler() {
        return (ProcessorErrorHandler<T>)((this.errorHandler == null) ? NoopProcessorErrorHandler.instance : this.errorHandler);
    }
    
    public void setProcessorErrorHandler(final ProcessorErrorHandler<? extends Context> processorErrorHandler) {
        this.errorHandler = processorErrorHandler;
    }
    
    public boolean isProcessorErrorHandlerDefined() {
        return this.errorHandler != null;
    }
    
    protected abstract F createDefaultFormat();
    
    final void autoConfigure() {
        if (!this.autoConfigurationEnabled) {
            return;
        }
        this.runAutomaticConfiguration();
    }
    
    public final void trimValues(final boolean trim) {
        this.setIgnoreLeadingWhitespaces(trim);
        this.setIgnoreTrailingWhitespaces(trim);
    }
    
    public int getErrorContentLength() {
        return this.errorContentLength;
    }
    
    public void setErrorContentLength(final int errorContentLength) {
        this.errorContentLength = errorContentLength;
    }
    
    void runAutomaticConfiguration() {
    }
    
    public final boolean getSkipBitsAsWhitespace() {
        return this.skipBitsAsWhitespace;
    }
    
    public final void setSkipBitsAsWhitespace(final boolean skipBitsAsWhitespace) {
        this.skipBitsAsWhitespace = skipBitsAsWhitespace;
    }
    
    protected final int getWhitespaceRangeStart() {
        return this.skipBitsAsWhitespace ? -1 : 1;
    }
    
    @Override
    public final String toString() {
        final StringBuilder out = new StringBuilder();
        out.append(this.getClass().getSimpleName()).append(':');
        final TreeMap<String, Object> config = new TreeMap<String, Object>();
        this.addConfiguration(config);
        for (final Map.Entry<String, Object> e : config.entrySet()) {
            out.append("\n\t");
            out.append(e.getKey()).append('=').append(e.getValue());
        }
        out.append("Format configuration:\n\t").append(this.getFormat().toString());
        return out.toString();
    }
    
    protected void addConfiguration(final Map<String, Object> out) {
        out.put("Null value", this.nullValue);
        out.put("Maximum number of characters per column", this.maxCharsPerColumn);
        out.put("Maximum number of columns", this.maxColumns);
        out.put("Skip empty lines", this.skipEmptyLines);
        out.put("Ignore trailing whitespaces", this.ignoreTrailingWhitespaces);
        out.put("Ignore leading whitespaces", this.ignoreLeadingWhitespaces);
        out.put("Selected fields", (this.fieldSelector == null) ? "none" : this.fieldSelector.describe());
        out.put("Headers", Arrays.toString(this.headers));
        out.put("Auto configuration enabled", this.autoConfigurationEnabled);
        out.put("RowProcessor error handler", this.errorHandler);
        out.put("Length of content displayed on error", this.errorContentLength);
        out.put("Restricting data in exceptions", this.errorContentLength == 0);
        out.put("Skip bits as whitespace", this.skipBitsAsWhitespace);
    }
    
    protected CommonSettings clone(final boolean clearInputSpecificSettings) {
        try {
            final CommonSettings out = (CommonSettings)super.clone();
            if (out.format != null) {
                out.format = (F)out.format.clone();
            }
            if (clearInputSpecificSettings) {
                out.clearInputSpecificSettings();
            }
            return out;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    protected CommonSettings clone() {
        return this.clone(false);
    }
    
    protected void clearInputSpecificSettings() {
        this.fieldSelector = null;
        this.headers = null;
    }
}
