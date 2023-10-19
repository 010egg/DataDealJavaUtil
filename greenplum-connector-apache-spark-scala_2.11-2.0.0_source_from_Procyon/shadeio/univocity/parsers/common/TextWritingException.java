// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

public class TextWritingException extends AbstractException
{
    private static final long serialVersionUID = 7198462597717255519L;
    private final long recordCount;
    private final Object[] recordData;
    private final String recordCharacters;
    
    private TextWritingException(final String message, final long recordCount, final Object[] row, final String recordCharacters, final Throwable cause) {
        super(message, cause);
        this.recordCount = recordCount;
        this.recordData = row;
        this.recordCharacters = recordCharacters;
    }
    
    public TextWritingException(final String message, final long recordCount, final String recordCharacters, final Throwable cause) {
        this(message, recordCount, null, recordCharacters, cause);
    }
    
    public TextWritingException(final String message, final long recordCount, final Object[] row, final Throwable cause) {
        this(message, recordCount, row, null, cause);
    }
    
    public TextWritingException(final String message) {
        this(message, 0L, null, null, null);
    }
    
    public TextWritingException(final Throwable cause) {
        this((cause != null) ? cause.getMessage() : null, 0L, null, null, cause);
    }
    
    public TextWritingException(final String message, final long line, final Object[] row) {
        this(message, line, row, null);
    }
    
    public TextWritingException(final String message, final long line, final String recordCharacters) {
        this(message, line, null, recordCharacters, null);
    }
    
    public long getRecordCount() {
        return this.recordCount;
    }
    
    public Object[] getRecordData() {
        return this.restrictContent(this.recordData);
    }
    
    public String getRecordCharacters() {
        if (this.errorContentLength == 0) {
            return null;
        }
        return this.recordCharacters;
    }
    
    @Override
    protected String getDetails() {
        String details = "";
        details = AbstractException.printIfNotEmpty(details, "recordCount", this.recordCount);
        details = AbstractException.printIfNotEmpty(details, "recordData", this.restrictContent(this.recordData));
        details = AbstractException.printIfNotEmpty(details, "recordCharacters", this.restrictContent(this.recordCharacters));
        return details;
    }
    
    @Override
    protected String getErrorDescription() {
        return "Error writing data";
    }
}
