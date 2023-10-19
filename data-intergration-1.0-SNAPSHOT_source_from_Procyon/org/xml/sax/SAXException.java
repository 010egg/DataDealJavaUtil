// 
// Decompiled by Procyon v0.5.36
// 

package org.xml.sax;

public class SAXException extends Exception
{
    private Exception exception;
    
    public SAXException() {
        this.exception = null;
    }
    
    public SAXException(final String message) {
        super(message);
        this.exception = null;
    }
    
    public SAXException(final Exception exception) {
        this.exception = exception;
    }
    
    public SAXException(final String message, final Exception exception) {
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
    
    public String toString() {
        if (this.exception != null) {
            return this.exception.toString();
        }
        return super.toString();
    }
}
