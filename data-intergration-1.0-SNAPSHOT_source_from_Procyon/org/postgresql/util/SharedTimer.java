// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.util.Timer;
import org.postgresql.core.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedTimer
{
    private static AtomicInteger timerCount;
    private Logger log;
    private volatile Timer timer;
    private AtomicInteger refCount;
    
    public SharedTimer(final Logger log) {
        this.timer = null;
        this.refCount = new AtomicInteger(0);
        this.log = log;
    }
    
    public int getRefCount() {
        return this.refCount.get();
    }
    
    public synchronized Timer getTimer() {
        if (this.timer == null) {
            final int index = SharedTimer.timerCount.incrementAndGet();
            this.timer = new Timer("PostgreSQL-JDBC-SharedTimer-" + index, true);
        }
        this.refCount.incrementAndGet();
        return this.timer;
    }
    
    public synchronized void releaseTimer() {
        final int count = this.refCount.decrementAndGet();
        if (count > 0) {
            this.log.debug("Outstanding references still exist so not closing shared Timer");
        }
        else if (count == 0) {
            this.log.debug("No outstanding references to shared Timer, will cancel and close it");
            if (this.timer != null) {
                this.timer.cancel();
                this.timer = null;
            }
        }
        else {
            this.log.debug("releaseTimer() called too many times; there is probably a bug in the calling code");
            this.refCount.set(0);
        }
    }
    
    static {
        SharedTimer.timerCount = new AtomicInteger(0);
    }
}
