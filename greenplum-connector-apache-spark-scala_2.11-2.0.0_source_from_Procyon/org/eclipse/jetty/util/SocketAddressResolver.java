// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

import org.eclipse.jetty.util.log.Log;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.util.thread.Scheduler;
import java.util.concurrent.Executor;
import org.eclipse.jetty.util.log.Logger;
import java.nio.channels.UnresolvedAddressException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public interface SocketAddressResolver
{
    void resolve(final String p0, final int p1, final Promise<SocketAddress> p2);
    
    public static class Sync implements SocketAddressResolver
    {
        @Override
        public void resolve(final String host, final int port, final Promise<SocketAddress> promise) {
            try {
                final InetSocketAddress result = new InetSocketAddress(host, port);
                if (result.isUnresolved()) {
                    promise.failed(new UnresolvedAddressException());
                }
                else {
                    promise.succeeded(result);
                }
            }
            catch (Throwable x) {
                promise.failed(x);
            }
        }
    }
    
    public static class Async implements SocketAddressResolver
    {
        private static final Logger LOG;
        private final Executor executor;
        private final Scheduler scheduler;
        private final long timeout;
        
        public Async(final Executor executor, final Scheduler scheduler, final long timeout) {
            this.executor = executor;
            this.scheduler = scheduler;
            this.timeout = timeout;
        }
        
        public Executor getExecutor() {
            return this.executor;
        }
        
        public Scheduler getScheduler() {
            return this.scheduler;
        }
        
        public long getTimeout() {
            return this.timeout;
        }
        
        @Override
        public void resolve(final String host, final int port, final Promise<SocketAddress> promise) {
            this.executor.execute(new Runnable() {
                @Override
                public void run() {
                    Scheduler.Task task = null;
                    final AtomicBoolean complete = new AtomicBoolean();
                    if (Async.this.timeout > 0L) {
                        final Thread thread = Thread.currentThread();
                        task = Async.this.scheduler.schedule(new Runnable() {
                            @Override
                            public void run() {
                                if (complete.compareAndSet(false, true)) {
                                    promise.failed(new TimeoutException());
                                    thread.interrupt();
                                }
                            }
                        }, Async.this.timeout, TimeUnit.MILLISECONDS);
                    }
                    try {
                        final long start = System.nanoTime();
                        final InetSocketAddress result = new InetSocketAddress(host, port);
                        final long elapsed = System.nanoTime() - start;
                        if (Async.LOG.isDebugEnabled()) {
                            Async.LOG.debug("Resolved {} in {} ms", host, TimeUnit.NANOSECONDS.toMillis(elapsed));
                        }
                        if (complete.compareAndSet(false, true)) {
                            if (result.isUnresolved()) {
                                promise.failed(new UnresolvedAddressException());
                            }
                            else {
                                promise.succeeded(result);
                            }
                        }
                    }
                    catch (Throwable x) {
                        if (complete.compareAndSet(false, true)) {
                            promise.failed(x);
                        }
                    }
                    finally {
                        if (task != null) {
                            task.cancel();
                        }
                        Thread.interrupted();
                    }
                }
            });
        }
        
        static {
            LOG = Log.getLogger(SocketAddressResolver.class);
        }
    }
}
