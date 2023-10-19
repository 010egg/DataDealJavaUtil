// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import ch.qos.logback.core.LogbackException;

public class RolloverFailure extends LogbackException
{
    private static final long serialVersionUID = -4407533730831239458L;
    
    public RolloverFailure(final String msg) {
        super(msg);
    }
    
    public RolloverFailure(final String message, final Throwable cause) {
        super(message, cause);
    }
}
