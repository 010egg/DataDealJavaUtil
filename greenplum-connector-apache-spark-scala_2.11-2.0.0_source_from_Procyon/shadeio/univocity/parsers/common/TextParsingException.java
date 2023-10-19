// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

public class TextParsingException extends AbstractException
{
    private static final long serialVersionUID = 1410975527141918214L;
    private long lineIndex;
    private long charIndex;
    private long recordNumber;
    private int columnIndex;
    private String content;
    private String[] headers;
    protected int[] extractedIndexes;
    
    public TextParsingException(final Context context, final String message, final Throwable cause) {
        super(message, cause);
        this.setContext(context);
    }
    
    protected void setContext(final Context context) {
        if (context instanceof ParsingContext) {
            this.setParsingContext((ParsingContext)context);
        }
        else {
            this.setParsingContext(null);
        }
        this.columnIndex = ((context == null) ? -1 : context.currentColumn());
        this.recordNumber = ((context == null) ? -1L : context.currentRecord());
        if (this.headers == null) {
            this.headers = (String[])((context == null) ? null : context.headers());
        }
        this.extractedIndexes = (int[])((context == null) ? null : context.extractedFieldIndexes());
    }
    
    private void setParsingContext(final ParsingContext parsingContext) {
        this.lineIndex = ((parsingContext == null) ? -1L : parsingContext.currentLine());
        this.charIndex = ((parsingContext == null) ? 0L : parsingContext.currentChar());
        this.content = ((parsingContext == null) ? null : parsingContext.fieldContentOnError());
    }
    
    public TextParsingException(final ParsingContext context, final String message) {
        this(context, message, null);
    }
    
    public TextParsingException(final ParsingContext context, final Throwable cause) {
        this(context, (cause != null) ? cause.getMessage() : null, cause);
    }
    
    public TextParsingException(final ParsingContext context) {
        this(context, null, null);
    }
    
    @Override
    protected String getErrorDescription() {
        return "Error parsing input";
    }
    
    @Override
    protected String getDetails() {
        String details = "";
        details = AbstractException.printIfNotEmpty(details, "line", this.lineIndex);
        details = AbstractException.printIfNotEmpty(details, "column", this.columnIndex);
        details = AbstractException.printIfNotEmpty(details, "record", this.recordNumber);
        details = ((this.charIndex == 0L) ? details : AbstractException.printIfNotEmpty(details, "charIndex", this.charIndex));
        details = AbstractException.printIfNotEmpty(details, "headers", this.headers);
        details = AbstractException.printIfNotEmpty(details, "content parsed", this.restrictContent(this.content));
        return details;
    }
    
    public long getRecordNumber() {
        return this.lineIndex;
    }
    
    public int getColumnIndex() {
        return this.columnIndex;
    }
    
    public long getLineIndex() {
        return this.lineIndex;
    }
    
    public long getCharIndex() {
        return this.charIndex;
    }
    
    public final String getParsedContent() {
        if (this.errorContentLength == 0) {
            return null;
        }
        return this.content;
    }
    
    public final String[] getHeaders() {
        return this.headers;
    }
}
