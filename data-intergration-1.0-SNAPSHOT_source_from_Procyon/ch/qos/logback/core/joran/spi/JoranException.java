// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.spi;

public class JoranException extends Exception
{
    private static final long serialVersionUID = 1112493363728774021L;
    
    public JoranException(final String msg) {
        super(msg);
    }
    
    public JoranException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
