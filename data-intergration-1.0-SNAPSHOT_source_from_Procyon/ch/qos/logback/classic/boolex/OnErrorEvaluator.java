// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.boolex;

import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EventEvaluatorBase;

public class OnErrorEvaluator extends EventEvaluatorBase<ILoggingEvent>
{
    @Override
    public boolean evaluate(final ILoggingEvent event) throws NullPointerException, EvaluationException {
        return event.getLevel().levelInt >= 40000;
    }
}
