// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.thread;

import java.util.concurrent.atomic.AtomicReference;

@Deprecated
public class SpinLock
{
    private final AtomicReference<Thread> _lock;
    private final Lock _unlock;
    
    public SpinLock() {
        this._lock = new AtomicReference<Thread>(null);
        this._unlock = new Lock();
    }
    
    public Lock lock() {
        final Thread thread = Thread.currentThread();
        while (!this._lock.compareAndSet(null, thread)) {
            if (this._lock.get() == thread) {
                throw new IllegalStateException("SpinLock is not reentrant");
            }
        }
        return this._unlock;
    }
    
    public boolean isLocked() {
        return this._lock.get() != null;
    }
    
    public boolean isLockedThread() {
        return this._lock.get() == Thread.currentThread();
    }
    
    public class Lock implements AutoCloseable
    {
        @Override
        public void close() {
            SpinLock.this._lock.set(null);
        }
    }
}
