// 
// Decompiled by Procyon v0.5.36
// 

package javax.xml.transform;

public class TransformerFactoryConfigurationError extends Error
{
    private Exception exception;
    
    public TransformerFactoryConfigurationError() {
        this.exception = null;
    }
    
    public TransformerFactoryConfigurationError(final String message) {
        super(message);
        this.exception = null;
    }
    
    public TransformerFactoryConfigurationError(final Exception exception) {
        super(exception.toString());
        this.exception = exception;
    }
    
    public TransformerFactoryConfigurationError(final Exception exception, final String message) {
        super(message);
        this.exception = exception;
    }
    
    public String getMessage() {
        final String message = super.getMessage();
        if (message == null && this.exception != null) {
            return this.exception.getMessage();
        }
        return message;
    }
    
    public Exception getException() {
        return this.exception;
    }
}
