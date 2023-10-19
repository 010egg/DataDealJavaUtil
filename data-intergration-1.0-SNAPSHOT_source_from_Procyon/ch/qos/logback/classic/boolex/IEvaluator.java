// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.boolex;

import ch.qos.logback.classic.spi.ILoggingEvent;

public interface IEvaluator
{
    boolean doEvaluate(final ILoggingEvent p0);
}
