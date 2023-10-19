// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

public class ScanException extends Exception
{
    private static final long serialVersionUID = -3132040414328475658L;
    Throwable cause;
    
    public ScanException(final String msg) {
        super(msg);
    }
    
    public ScanException(final String msg, final Throwable rootCause) {
        super(msg);
        this.cause = rootCause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
