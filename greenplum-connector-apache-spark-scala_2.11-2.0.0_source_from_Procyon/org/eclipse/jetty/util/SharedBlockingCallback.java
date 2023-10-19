// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

import java.util.concurrent.CancellationException;
import org.eclipse.jetty.util.thread.NonBlockingThread;
import java.io.Closeable;
import org.eclipse.jetty.util.log.Log;
import java.io.InterruptedIOException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.eclipse.jetty.util.log.Logger;

public class SharedBlockingCallback
{
    static final Logger LOG;
    final ReentrantLock _lock;
    final Condition _idle;
    final Condition _complete;
    private static Throwable IDLE;
    private static Throwable SUCCEEDED;
    private static Throwable FAILED;
    Blocker _blocker;
    
    public SharedBlockingCallback() {
        this._lock = new ReentrantLock();
        this._idle = this._lock.newCondition();
        this._complete = this._lock.newCondition();
        this._blocker = new Blocker();
    }
    
    protected long getIdleTimeout() {
        return -1L;
    }
    
    public Blocker acquire() throws IOException {
        this._lock.lock();
        final long idle = this.getIdleTimeout();
        try {
            while (this._blocker._state != SharedBlockingCallback.IDLE) {
                if (idle > 0L && idle < 4611686018427387903L) {
                    if (!this._idle.await(idle * 2L, TimeUnit.MILLISECONDS)) {
                        throw new IOException(new TimeoutException());
                    }
                    continue;
                }
                else {
                    this._idle.await();
                }
            }
            this._blocker._state = null;
        }
        catch (InterruptedException e) {
            throw new InterruptedIOException();
        }
        finally {
            this._lock.unlock();
        }
        return this._blocker;
    }
    
    protected void notComplete(final Blocker blocker) {
        SharedBlockingCallback.LOG.warn("Blocker not complete {}", blocker);
        if (SharedBlockingCallback.LOG.isDebugEnabled()) {
            SharedBlockingCallback.LOG.debug(new Throwable());
        }
    }
    
    static {
        LOG = Log.getLogger(SharedBlockingCallback.class);
        SharedBlockingCallback.IDLE = new Throwable() {
            @Override
            public String toString() {
                return "IDLE";
            }
        };
        SharedBlockingCallback.SUCCEEDED = new Throwable() {
            @Override
            public String toString() {
                return "SUCCEEDED";
            }
        };
        SharedBlockingCallback.FAILED = new Throwable() {
            @Override
            public String toString() {
                return "FAILED";
            }
        };
    }
    
    public class Blocker implements Callback, Closeable
    {
        Throwable _state;
        
        protected Blocker() {
            this._state = SharedBlockingCallback.IDLE;
        }
        
        @Override
        public void succeeded() {
            SharedBlockingCallback.this._lock.lock();
            try {
                if (this._state != null) {
                    throw new IllegalStateException(this._state);
                }
                this._state = SharedBlockingCallback.SUCCEEDED;
                SharedBlockingCallback.this._complete.signalAll();
            }
            finally {
                SharedBlockingCallback.this._lock.unlock();
            }
        }
        
        @Override
        public void failed(final Throwable cause) {
            SharedBlockingCallback.this._lock.lock();
            try {
                if (this._state != null) {
                    throw new IllegalStateException(this._state);
                }
                if (cause == null) {
                    this._state = SharedBlockingCallback.FAILED;
                }
                else if (cause instanceof BlockerTimeoutException) {
                    this._state = new IOException(cause);
                }
                else {
                    this._state = cause;
                }
                SharedBlockingCallback.this._complete.signalAll();
            }
            finally {
                SharedBlockingCallback.this._lock.unlock();
            }
        }
        
        public void block() throws IOException {
            if (NonBlockingThread.isNonBlockingThread()) {
                SharedBlockingCallback.LOG.warn("Blocking a NonBlockingThread: ", new Throwable());
            }
            SharedBlockingCallback.this._lock.lock();
            final long idle = SharedBlockingCallback.this.getIdleTimeout();
            try {
                while (this._state == null) {
                    if (idle > 0L && idle < 4611686018427387903L) {
                        if (SharedBlockingCallback.this._complete.await(idle + idle / 2L, TimeUnit.MILLISECONDS)) {
                            continue;
                        }
                        this._state = new BlockerTimeoutException();
                    }
                    else {
                        SharedBlockingCallback.this._complete.await();
                    }
                }
                if (this._state == SharedBlockingCallback.SUCCEEDED) {
                    return;
                }
                if (this._state == SharedBlockingCallback.IDLE) {
                    throw new IllegalStateException("IDLE");
                }
                if (this._state instanceof IOException) {
                    throw (IOException)this._state;
                }
                if (this._state instanceof CancellationException) {
                    throw (CancellationException)this._state;
                }
                if (this._state instanceof RuntimeException) {
                    throw (RuntimeException)this._state;
                }
                if (this._state instanceof Error) {
                    throw (Error)this._state;
                }
                throw new IOException(this._state);
            }
            catch (InterruptedException e) {
                throw new InterruptedIOException();
            }
            finally {
                SharedBlockingCallback.this._lock.unlock();
            }
        }
        
        @Override
        public void close() throws IOException {
            SharedBlockingCallback.this._lock.lock();
            try {
                if (this._state == SharedBlockingCallback.IDLE) {
                    throw new IllegalStateException("IDLE");
                }
                if (this._state == null) {
                    SharedBlockingCallback.this.notComplete(this);
                }
            }
            finally {
                try {
                    if (this._state instanceof BlockerTimeoutException) {
                        SharedBlockingCallback.this._blocker = new Blocker();
                    }
                    else {
                        this._state = SharedBlockingCallback.IDLE;
                    }
                    SharedBlockingCallback.this._idle.signalAll();
                    SharedBlockingCallback.this._complete.signalAll();
                }
                finally {
                    SharedBlockingCallback.this._lock.unlock();
                }
            }
        }
        
        @Override
        public String toString() {
            SharedBlockingCallback.this._lock.lock();
            try {
                return String.format("%s@%x{%s}", Blocker.class.getSimpleName(), this.hashCode(), this._state);
            }
            finally {
                SharedBlockingCallback.this._lock.unlock();
            }
        }
    }
    
    private static class BlockerTimeoutException extends TimeoutException
    {
    }
}
