// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.hook;

import ch.qos.logback.core.util.Duration;

public class DelayingShutdownHook extends ShutdownHookBase
{
    public static final Duration DEFAULT_DELAY;
    private Duration delay;
    
    public DelayingShutdownHook() {
        this.delay = DelayingShutdownHook.DEFAULT_DELAY;
    }
    
    public Duration getDelay() {
        return this.delay;
    }
    
    public void setDelay(final Duration delay) {
        this.delay = delay;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(this.delay.getMilliseconds());
        }
        catch (InterruptedException ex) {}
        super.stop();
    }
    
    static {
        DEFAULT_DELAY = Duration.buildByMilliseconds(0.0);
    }
}
