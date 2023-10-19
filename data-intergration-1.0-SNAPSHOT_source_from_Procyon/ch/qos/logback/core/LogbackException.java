// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

public class LogbackException extends RuntimeException
{
    private static final long serialVersionUID = -799956346239073266L;
    
    public LogbackException(final String msg) {
        super(msg);
    }
    
    public LogbackException(final String msg, final Throwable nested) {
        super(msg, nested);
    }
}
