// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.boolex;

public class EvaluationException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public EvaluationException(final String msg) {
        super(msg);
    }
    
    public EvaluationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
    public EvaluationException(final Throwable cause) {
        super(cause);
    }
}
