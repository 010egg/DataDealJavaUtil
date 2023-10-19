// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.sift;

import java.util.Iterator;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.util.Duration;
import ch.qos.logback.core.AppenderBase;

public abstract class SiftingAppenderBase<E> extends AppenderBase<E>
{
    protected AppenderTracker<E> appenderTracker;
    AppenderFactory<E> appenderFactory;
    Duration timeout;
    int maxAppenderCount;
    Discriminator<E> discriminator;
    
    public SiftingAppenderBase() {
        this.timeout = new Duration(1800000L);
        this.maxAppenderCount = Integer.MAX_VALUE;
    }
    
    public Duration getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final Duration timeout) {
        this.timeout = timeout;
    }
    
    public int getMaxAppenderCount() {
        return this.maxAppenderCount;
    }
    
    public void setMaxAppenderCount(final int maxAppenderCount) {
        this.maxAppenderCount = maxAppenderCount;
    }
    
    public void setAppenderFactory(final AppenderFactory<E> appenderFactory) {
        this.appenderFactory = appenderFactory;
    }
    
    @Override
    public void start() {
        int errors = 0;
        if (this.discriminator == null) {
            this.addError("Missing discriminator. Aborting");
            ++errors;
        }
        if (!this.discriminator.isStarted()) {
            this.addError("Discriminator has not started successfully. Aborting");
            ++errors;
        }
        if (this.appenderFactory == null) {
            this.addError("AppenderFactory has not been set. Aborting");
            ++errors;
        }
        else {
            (this.appenderTracker = new AppenderTracker<E>(this.context, this.appenderFactory)).setMaxComponents(this.maxAppenderCount);
            this.appenderTracker.setTimeout(this.timeout.getMilliseconds());
        }
        if (errors == 0) {
            super.start();
        }
    }
    
    @Override
    public void stop() {
        for (final Appender<E> appender : this.appenderTracker.allComponents()) {
            appender.stop();
        }
    }
    
    protected abstract long getTimestamp(final E p0);
    
    @Override
    protected void append(final E event) {
        if (!this.isStarted()) {
            return;
        }
        final String discriminatingValue = this.discriminator.getDiscriminatingValue(event);
        final long timestamp = this.getTimestamp(event);
        final Appender<E> appender = (Appender<E>)this.appenderTracker.getOrCreate(discriminatingValue, timestamp);
        if (this.eventMarksEndOfLife(event)) {
            this.appenderTracker.endOfLife(discriminatingValue);
        }
        this.appenderTracker.removeStaleComponents(timestamp);
        appender.doAppend(event);
    }
    
    protected abstract boolean eventMarksEndOfLife(final E p0);
    
    public Discriminator<E> getDiscriminator() {
        return this.discriminator;
    }
    
    public void setDiscriminator(final Discriminator<E> discriminator) {
        this.discriminator = discriminator;
    }
    
    public AppenderTracker<E> getAppenderTracker() {
        return this.appenderTracker;
    }
    
    public String getDiscriminatorKey() {
        if (this.discriminator != null) {
            return this.discriminator.getKey();
        }
        return null;
    }
}
