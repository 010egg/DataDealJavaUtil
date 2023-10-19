// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.status;

public class InfoStatus extends StatusBase
{
    public InfoStatus(final String msg, final Object origin) {
        super(0, msg, origin);
    }
    
    public InfoStatus(final String msg, final Object origin, final Throwable t) {
        super(0, msg, origin, t);
    }
}
