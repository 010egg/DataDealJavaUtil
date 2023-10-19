// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.statistic;

import org.eclipse.jetty.util.Atomics;
import java.util.concurrent.atomic.AtomicLong;

public class CounterStatistic
{
    protected final AtomicLong _max;
    protected final AtomicLong _curr;
    protected final AtomicLong _total;
    
    public CounterStatistic() {
        this._max = new AtomicLong();
        this._curr = new AtomicLong();
        this._total = new AtomicLong();
    }
    
    public void reset() {
        this.reset(0L);
    }
    
    public void reset(final long value) {
        this._max.set(value);
        this._curr.set(value);
        this._total.set(0L);
    }
    
    public long add(final long delta) {
        final long value = this._curr.addAndGet(delta);
        if (delta > 0L) {
            this._total.addAndGet(delta);
            Atomics.updateMax(this._max, value);
        }
        return value;
    }
    
    public long increment() {
        return this.add(1L);
    }
    
    public long decrement() {
        return this.add(-1L);
    }
    
    public long getMax() {
        return this._max.get();
    }
    
    public long getCurrent() {
        return this._curr.get();
    }
    
    public long getTotal() {
        return this._total.get();
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x{c=%d,m=%d,t=%d}", this.getClass().getSimpleName(), this.hashCode(), this._curr.get(), this._max.get(), this._total.get());
    }
}
