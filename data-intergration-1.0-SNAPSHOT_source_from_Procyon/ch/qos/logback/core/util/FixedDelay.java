// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

public class FixedDelay implements DelayStrategy
{
    private final long subsequentDelay;
    private long nextDelay;
    
    public FixedDelay(final long initialDelay, final long subsequentDelay) {
        final String s = new String();
        this.nextDelay = initialDelay;
        this.subsequentDelay = subsequentDelay;
    }
    
    public FixedDelay(final int delay) {
        this(delay, delay);
    }
    
    @Override
    public long nextDelay() {
        final long delay = this.nextDelay;
        this.nextDelay = this.subsequentDelay;
        return delay;
    }
}
