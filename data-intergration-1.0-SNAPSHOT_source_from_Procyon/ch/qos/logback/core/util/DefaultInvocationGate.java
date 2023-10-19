// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

public class DefaultInvocationGate implements InvocationGate
{
    static final int MASK_DECREASE_RIGHT_SHIFT_COUNT = 2;
    private static final int MAX_MASK = 65535;
    static final int DEFAULT_MASK = 15;
    private volatile long mask;
    private long invocationCounter;
    private static final long MASK_INCREASE_THRESHOLD = 100L;
    private static final long MASK_DECREASE_THRESHOLD = 800L;
    private long minDelayThreshold;
    private long maxDelayThreshold;
    long lowerLimitForMaskMatch;
    long upperLimitForNoMaskMatch;
    
    public DefaultInvocationGate() {
        this(100L, 800L, System.currentTimeMillis());
    }
    
    public DefaultInvocationGate(final long minDelayThreshold, final long maxDelayThreshold, final long currentTime) {
        this.mask = 15L;
        this.invocationCounter = 0L;
        this.minDelayThreshold = minDelayThreshold;
        this.maxDelayThreshold = maxDelayThreshold;
        this.lowerLimitForMaskMatch = currentTime + minDelayThreshold;
        this.upperLimitForNoMaskMatch = currentTime + maxDelayThreshold;
    }
    
    @Override
    public final boolean isTooSoon(final long currentTime) {
        final boolean maskMatch = (this.invocationCounter++ & this.mask) == this.mask;
        if (maskMatch) {
            if (currentTime < this.lowerLimitForMaskMatch) {
                this.increaseMask();
            }
            this.updateLimits(currentTime);
        }
        else if (currentTime > this.upperLimitForNoMaskMatch) {
            this.decreaseMask();
            this.updateLimits(currentTime);
            return false;
        }
        return !maskMatch;
    }
    
    private void updateLimits(final long currentTime) {
        this.lowerLimitForMaskMatch = currentTime + this.minDelayThreshold;
        this.upperLimitForNoMaskMatch = currentTime + this.maxDelayThreshold;
    }
    
    long getMask() {
        return this.mask;
    }
    
    private void increaseMask() {
        if (this.mask >= 65535L) {
            return;
        }
        this.mask = (this.mask << 1 | 0x1L);
    }
    
    private void decreaseMask() {
        this.mask >>>= 2;
    }
    
    public long getInvocationCounter() {
        return this.invocationCounter;
    }
}
