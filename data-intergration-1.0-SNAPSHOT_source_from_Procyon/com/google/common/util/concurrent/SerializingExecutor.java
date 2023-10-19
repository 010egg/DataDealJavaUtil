// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.logging.Level;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import javax.annotation.concurrent.GuardedBy;
import java.util.Deque;
import java.util.logging.Logger;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.Executor;

@GwtIncompatible
final class SerializingExecutor implements Executor
{
    private static final Logger log;
    private final Executor executor;
    @GuardedBy("queue")
    private final Deque<Runnable> queue;
    @GuardedBy("queue")
    private boolean isWorkerRunning;
    @GuardedBy("queue")
    private int suspensions;
    private final QueueWorker worker;
    
    public SerializingExecutor(final Executor executor) {
        this.queue = new ArrayDeque<Runnable>();
        this.isWorkerRunning = false;
        this.suspensions = 0;
        this.worker = new QueueWorker();
        this.executor = Preconditions.checkNotNull(executor);
    }
    
    @Override
    public void execute(final Runnable task) {
        synchronized (this.queue) {
            this.queue.addLast(task);
            if (this.isWorkerRunning || this.suspensions > 0) {
                return;
            }
            this.isWorkerRunning = true;
        }
        this.startQueueWorker();
    }
    
    public void executeFirst(final Runnable task) {
        synchronized (this.queue) {
            this.queue.addFirst(task);
            if (this.isWorkerRunning || this.suspensions > 0) {
                return;
            }
            this.isWorkerRunning = true;
        }
        this.startQueueWorker();
    }
    
    public void suspend() {
        synchronized (this.queue) {
            ++this.suspensions;
        }
    }
    
    public void resume() {
        synchronized (this.queue) {
            Preconditions.checkState(this.suspensions > 0);
            --this.suspensions;
            if (this.isWorkerRunning || this.suspensions > 0 || this.queue.isEmpty()) {
                return;
            }
            this.isWorkerRunning = true;
        }
        this.startQueueWorker();
    }
    
    private void startQueueWorker() {
        boolean executionRejected = true;
        try {
            this.executor.execute(this.worker);
            executionRejected = false;
        }
        finally {
            if (executionRejected) {
                synchronized (this.queue) {
                    this.isWorkerRunning = false;
                }
            }
        }
    }
    
    static {
        log = Logger.getLogger(SerializingExecutor.class.getName());
    }
    
    private final class QueueWorker implements Runnable
    {
        @Override
        public void run() {
            try {
                this.workOnQueue();
            }
            catch (Error e) {
                synchronized (SerializingExecutor.this.queue) {
                    SerializingExecutor.this.isWorkerRunning = false;
                }
                throw e;
            }
        }
        
        private void workOnQueue() {
            while (true) {
                Runnable task = null;
                synchronized (SerializingExecutor.this.queue) {
                    if (SerializingExecutor.this.suspensions == 0) {
                        task = SerializingExecutor.this.queue.pollFirst();
                    }
                    if (task == null) {
                        SerializingExecutor.this.isWorkerRunning = false;
                        return;
                    }
                }
                try {
                    task.run();
                }
                catch (RuntimeException e) {
                    SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + task, e);
                }
            }
        }
    }
}
