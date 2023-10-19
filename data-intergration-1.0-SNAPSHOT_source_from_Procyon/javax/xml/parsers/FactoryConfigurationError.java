// 
// Decompiled by Procyon v0.5.36
// 

package javax.xml.parsers;

public class FactoryConfigurationError extends Error
{
    private Exception exception;
    
    public FactoryConfigurationError() {
        this.exception = null;
    }
    
    public FactoryConfigurationError(final String message) {
        super(message);
        this.exception = null;
    }
    
    public FactoryConfigurationError(final Exception exception) {
        super(exception.toString());
        this.exception = exception;
    }
    
    public FactoryConfigurationError(final Exception exception, final String message) {
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
