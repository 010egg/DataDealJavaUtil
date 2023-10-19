// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

public interface InvocationGate
{
    public static final long TIME_UNAVAILABLE = -1L;
    
    boolean isTooSoon(final long p0);
}
