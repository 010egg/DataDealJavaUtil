// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.RunnableFuture;

@GwtCompatible
class TrustedListenableFutureTask<V> extends TrustedFuture<V> implements RunnableFuture<V>
{
    private InterruptibleTask task;
    
    static <V> TrustedListenableFutureTask<V> create(final AsyncCallable<V> callable) {
        return new TrustedListenableFutureTask<V>(callable);
    }
    
    static <V> TrustedListenableFutureTask<V> create(final Callable<V> callable) {
        return new TrustedListenableFutureTask<V>(callable);
    }
    
    static <V> TrustedListenableFutureTask<V> create(final Runnable runnable, @Nullable final V result) {
        return new TrustedListenableFutureTask<V>(Executors.callable(runnable, result));
    }
    
    TrustedListenableFutureTask(final Callable<V> callable) {
        this.task = new TrustedFutureInterruptibleTask(callable);
    }
    
    TrustedListenableFutureTask(final AsyncCallable<V> callable) {
        this.task = new TrustedFutureInterruptibleAsyncTask(callable);
    }
    
    @Override
    public void run() {
        final InterruptibleTask localTask = this.task;
        if (localTask != null) {
            localTask.run();
        }
    }
    
    @Override
    protected void afterDone() {
        super.afterDone();
        if (this.wasInterrupted()) {
            final InterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.interruptTask();
            }
        }
        this.task = null;
    }
    
    @Override
    protected String pendingToString() {
        final InterruptibleTask localTask = this.task;
        if (localTask != null) {
            return "task=[" + localTask + "]";
        }
        return null;
    }
    
    private final class TrustedFutureInterruptibleTask extends InterruptibleTask
    {
        private final Callable<V> callable;
        
        TrustedFutureInterruptibleTask(final Callable<V> callable) {
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @Override
        void runInterruptibly() {
            if (!TrustedListenableFutureTask.this.isDone()) {
                try {
                    TrustedListenableFutureTask.this.set(this.callable.call());
                }
                catch (Throwable t) {
                    TrustedListenableFutureTask.this.setException(t);
                }
            }
        }
        
        @Override
        boolean wasInterrupted() {
            return TrustedListenableFutureTask.this.wasInterrupted();
        }
        
        @Override
        public String toString() {
            return this.callable.toString();
        }
    }
    
    private final class TrustedFutureInterruptibleAsyncTask extends InterruptibleTask
    {
        private final AsyncCallable<V> callable;
        
        TrustedFutureInterruptibleAsyncTask(final AsyncCallable<V> callable) {
            this.callable = Preconditions.checkNotNull(callable);
        }
        
        @Override
        void runInterruptibly() {
            if (!TrustedListenableFutureTask.this.isDone()) {
                try {
                    final ListenableFuture<V> result = this.callable.call();
                    Preconditions.checkNotNull(result, (Object)"AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)?");
                    TrustedListenableFutureTask.this.setFuture(result);
                }
                catch (Throwable t) {
                    TrustedListenableFutureTask.this.setException(t);
                }
            }
        }
        
        @Override
        boolean wasInterrupted() {
            return TrustedListenableFutureTask.this.wasInterrupted();
        }
        
        @Override
        public String toString() {
            return this.callable.toString();
        }
    }
}
