// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import ch.qos.logback.core.spi.FilterAttachable;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

public interface Appender<E> extends LifeCycle, ContextAware, FilterAttachable<E>
{
    String getName();
    
    void doAppend(final E p0) throws LogbackException;
    
    void setName(final String p0);
}
