// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.HashMap;
import java.util.Map;

public class DataProcessingException extends TextParsingException
{
    private static final long serialVersionUID = 1410975527141918215L;
    private String columnName;
    private int columnIndex;
    private Object[] row;
    private Object value;
    private Map<String, Object> values;
    private boolean fatal;
    private boolean handled;
    
    public DataProcessingException(final String message) {
        this(message, -1, null, null);
    }
    
    public DataProcessingException(final String message, final Throwable cause) {
        this(message, -1, null, cause);
    }
    
    public DataProcessingException(final String message, final Object[] row) {
        this(message, -1, row, null);
    }
    
    public DataProcessingException(final String message, final Object[] row, final Throwable cause) {
        this(message, -1, row, cause);
    }
    
    public DataProcessingException(final String message, final int columnIndex) {
        this(message, columnIndex, null, null);
    }
    
    public DataProcessingException(final String message, final int columnIndex, final Object[] row, final Throwable cause) {
        super(null, message, cause);
        this.values = new HashMap<String, Object>();
        this.fatal = true;
        this.handled = false;
        this.setColumnIndex(columnIndex);
        this.row = row;
    }
    
    @Override
    protected String getErrorDescription() {
        return "Error processing parsed input";
    }
    
    @Override
    protected String getDetails() {
        String details = super.getDetails();
        Object[] row = this.getRow();
        if (row != null) {
            row = row.clone();
            for (int i = 0; i < row.length; ++i) {
                row[i] = this.restrictContent(row[i]);
            }
        }
        details = AbstractException.printIfNotEmpty(details, "row", row);
        details = AbstractException.printIfNotEmpty(details, "value", this.restrictContent(this.getValue()));
        details = AbstractException.printIfNotEmpty(details, "columnName", this.getColumnName());
        details = AbstractException.printIfNotEmpty(details, "columnIndex", this.getColumnIndex());
        return details;
    }
    
    public String getColumnName() {
        if (this.columnName != null) {
            return this.columnName;
        }
        final String[] headers = this.getHeaders();
        if (headers != null && this.getExtractedColumnIndex() != -1 && this.getExtractedColumnIndex() < headers.length) {
            return headers[this.getExtractedColumnIndex()];
        }
        return null;
    }
    
    @Override
    public final int getColumnIndex() {
        return this.columnIndex;
    }
    
    public final Object[] getRow() {
        return this.restrictContent(this.row);
    }
    
    public final void setValue(Object value) {
        if (this.errorContentLength == 0) {
            value = null;
        }
        if (value == null) {
            value = "null";
        }
        this.value = value;
    }
    
    public final void setValue(final String label, Object value) {
        if (this.errorContentLength == 0) {
            value = null;
        }
        this.values.put(label, value);
    }
    
    public final Object getValue() {
        if (this.errorContentLength == 0) {
            return null;
        }
        if (this.value != null) {
            return this.value;
        }
        if (this.row != null && this.columnIndex != -1 && this.columnIndex < this.row.length) {
            return this.row[this.columnIndex];
        }
        return null;
    }
    
    public final void setColumnIndex(final int columnIndex) {
        this.columnIndex = columnIndex;
    }
    
    private int getExtractedColumnIndex() {
        if (this.extractedIndexes != null && this.columnIndex < this.extractedIndexes.length && this.columnIndex > -1) {
            return this.extractedIndexes[this.columnIndex];
        }
        return this.columnIndex;
    }
    
    public final void setColumnName(final String columnName) {
        this.columnName = columnName;
    }
    
    public final void setRow(Object[] row) {
        if (this.errorContentLength == 0) {
            row = null;
        }
        this.row = row;
    }
    
    final boolean isFatal() {
        return this.fatal;
    }
    
    public final void markAsNonFatal() {
        this.fatal = false;
    }
    
    public final void markAsHandled(final ProcessorErrorHandler handler) {
        this.handled = (handler != null && !(handler instanceof NoopProcessorErrorHandler) && !(handler instanceof NoopRowProcessorErrorHandler));
    }
    
    public boolean isHandled() {
        return this.handled;
    }
    
    @Override
    protected final String updateMessage(final String msg) {
        if (this.errorContentLength == 0 || msg == null) {
            return msg;
        }
        final StringBuilder out = new StringBuilder(msg.length());
        int previous = 0;
        int start = 0;
        while (true) {
            start = msg.indexOf(123, start);
            if (start == -1) {
                break;
            }
            final int end = msg.indexOf(125, start);
            if (end == -1) {
                break;
            }
            final String label = msg.substring(start + 1, end);
            Object value = null;
            if ("value".equals(label)) {
                value = this.value;
            }
            else if (this.values.containsKey(label)) {
                value = this.values.get(label);
            }
            if (value != null) {
                final String content = this.restrictContent(value);
                out.append(msg, previous, start);
                out.append(content);
                previous = end;
            }
            start = end;
        }
        out.append(msg, (previous == 0) ? 0 : (previous + 1), msg.length());
        return out.toString();
    }
}
