// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.sift;

import ch.qos.logback.core.helpers.NOPAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.ContextAwareImpl;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AbstractComponentTracker;

public class AppenderTracker<E> extends AbstractComponentTracker<Appender<E>>
{
    int nopaWarningCount;
    final Context context;
    final AppenderFactory<E> appenderFactory;
    final ContextAwareImpl contextAware;
    
    public AppenderTracker(final Context context, final AppenderFactory<E> appenderFactory) {
        this.nopaWarningCount = 0;
        this.context = context;
        this.appenderFactory = appenderFactory;
        this.contextAware = new ContextAwareImpl(context, this);
    }
    
    @Override
    protected void processPriorToRemoval(final Appender<E> component) {
        component.stop();
    }
    
    @Override
    protected Appender<E> buildComponent(final String key) {
        Appender<E> appender = null;
        try {
            appender = this.appenderFactory.buildAppender(this.context, key);
        }
        catch (JoranException je) {
            this.contextAware.addError("Error while building appender with discriminating value [" + key + "]");
        }
        if (appender == null) {
            appender = this.buildNOPAppender(key);
        }
        return appender;
    }
    
    private NOPAppender<E> buildNOPAppender(final String key) {
        if (this.nopaWarningCount < 4) {
            ++this.nopaWarningCount;
            this.contextAware.addError("Building NOPAppender for discriminating value [" + key + "]");
        }
        final NOPAppender<E> nopa = new NOPAppender<E>();
        nopa.setContext(this.context);
        nopa.start();
        return nopa;
    }
    
    @Override
    protected boolean isComponentStale(final Appender<E> appender) {
        return !appender.isStarted();
    }
}
