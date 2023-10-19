// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;

class ProxyLeakTask implements Runnable
{
    private static final Logger LOGGER;
    static final ProxyLeakTask NO_LEAK;
    private ScheduledFuture<?> scheduledFuture;
    private String connectionName;
    private Exception exception;
    private String threadName;
    private boolean isLeaked;
    
    ProxyLeakTask(final PoolEntry poolEntry) {
        this.exception = new Exception("Apparent connection leak detected");
        this.threadName = Thread.currentThread().getName();
        this.connectionName = poolEntry.connection.toString();
    }
    
    private ProxyLeakTask() {
    }
    
    void schedule(final ScheduledExecutorService executorService, final long leakDetectionThreshold) {
        this.scheduledFuture = executorService.schedule(this, leakDetectionThreshold, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void run() {
        this.isLeaked = true;
        final StackTraceElement[] stackTrace = this.exception.getStackTrace();
        final StackTraceElement[] trace = new StackTraceElement[stackTrace.length - 5];
        System.arraycopy(stackTrace, 5, trace, 0, trace.length);
        this.exception.setStackTrace(trace);
        ProxyLeakTask.LOGGER.warn("Connection leak detection triggered for {} on thread {}, stack trace follows", this.connectionName, this.threadName, this.exception);
    }
    
    void cancel() {
        this.scheduledFuture.cancel(false);
        if (this.isLeaked) {
            ProxyLeakTask.LOGGER.info("Previously reported leaked connection {} on thread {} was returned to the pool (unleaked)", this.connectionName, this.threadName);
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ProxyLeakTask.class);
        NO_LEAK = new ProxyLeakTask() {
            @Override
            void schedule(final ScheduledExecutorService executorService, final long leakDetectionThreshold) {
            }
            
            @Override
            public void run() {
            }
            
            public void cancel() {
            }
        };
    }
}
