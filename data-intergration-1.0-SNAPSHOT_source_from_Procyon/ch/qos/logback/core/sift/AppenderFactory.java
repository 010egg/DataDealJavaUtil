// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.sift;

import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;

public interface AppenderFactory<E>
{
    Appender<E> buildAppender(final Context p0, final String p1) throws JoranException;
}
