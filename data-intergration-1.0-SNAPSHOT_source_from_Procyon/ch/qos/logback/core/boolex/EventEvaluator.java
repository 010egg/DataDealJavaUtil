// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.boolex;

import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.ContextAware;

public interface EventEvaluator<E> extends ContextAware, LifeCycle
{
    boolean evaluate(final E p0) throws NullPointerException, EvaluationException;
    
    String getName();
    
    void setName(final String p0);
}
