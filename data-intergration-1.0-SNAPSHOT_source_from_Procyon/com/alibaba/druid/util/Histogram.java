// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongArray;

public class Histogram
{
    private final long[] ranges;
    private final AtomicLongArray rangeCounters;
    
    public Histogram(final long... ranges) {
        this.ranges = ranges;
        this.rangeCounters = new AtomicLongArray(ranges.length + 1);
    }
    
    public static Histogram makeHistogram(final int rangeCount) {
        final long[] rangeValues = new long[rangeCount];
        for (int i = 0; i < rangeValues.length; ++i) {
            rangeValues[i] = (long)Math.pow(10.0, i);
        }
        return new Histogram(rangeValues);
    }
    
    public Histogram(final TimeUnit timeUnit, final long... ranges) {
        this.ranges = new long[ranges.length];
        for (int i = 0; i < ranges.length; ++i) {
            this.ranges[i] = TimeUnit.MILLISECONDS.convert(ranges[i], timeUnit);
        }
        this.rangeCounters = new AtomicLongArray(ranges.length + 1);
    }
    
    public void reset() {
        for (int i = 0; i < this.rangeCounters.length(); ++i) {
            this.rangeCounters.set(i, 0L);
        }
    }
    
    public void record(final long millis) {
        int index = this.rangeCounters.length() - 1;
        for (int i = 0; i < this.ranges.length; ++i) {
            if (millis < this.ranges[i]) {
                index = i;
                break;
            }
        }
        this.rangeCounters.incrementAndGet(index);
    }
    
    public long get(final int index) {
        return this.rangeCounters.get(index);
    }
    
    public long[] toArray() {
        final long[] array = new long[this.rangeCounters.length()];
        for (int i = 0; i < this.rangeCounters.length(); ++i) {
            array[i] = this.rangeCounters.get(i);
        }
        return array;
    }
    
    public long[] toArrayAndReset() {
        final long[] array = new long[this.rangeCounters.length()];
        for (int i = 0; i < this.rangeCounters.length(); ++i) {
            array[i] = this.rangeCounters.getAndSet(i, 0L);
        }
        return array;
    }
    
    public long[] getRanges() {
        return this.ranges;
    }
    
    public long getValue(final int index) {
        return this.rangeCounters.get(index);
    }
    
    public long getSum() {
        long sum = 0L;
        for (int i = 0; i < this.rangeCounters.length(); ++i) {
            sum += this.rangeCounters.get(i);
        }
        return sum;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append('[');
        for (int i = 0; i < this.rangeCounters.length(); ++i) {
            if (i != 0) {
                buf.append(',');
            }
            buf.append(this.rangeCounters.get(i));
        }
        buf.append(']');
        return buf.toString();
    }
}
