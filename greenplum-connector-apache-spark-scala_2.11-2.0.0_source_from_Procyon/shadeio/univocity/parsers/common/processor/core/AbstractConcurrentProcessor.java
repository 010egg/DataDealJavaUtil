// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.concurrent.ExecutionException;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractConcurrentProcessor<T extends Context> implements Processor<T>
{
    private final Processor processor;
    private boolean ended;
    private final ExecutorService executor;
    private volatile long rowCount;
    private Future<Void> process;
    private T currentContext;
    private Node<T> inputQueue;
    private volatile Node<T> outputQueue;
    private final int limit;
    private volatile long input;
    private volatile long output;
    private final Object lock;
    private boolean contextCopyingEnabled;
    
    public AbstractConcurrentProcessor(final Processor<T> processor) {
        this(processor, -1);
    }
    
    public AbstractConcurrentProcessor(final Processor<T> processor, final int limit) {
        this.ended = false;
        this.executor = Executors.newSingleThreadExecutor();
        this.contextCopyingEnabled = false;
        if (processor == null) {
            throw new IllegalArgumentException("Row processor cannot be null");
        }
        this.processor = processor;
        this.input = 0L;
        this.output = 0L;
        this.lock = new Object();
        this.limit = limit;
    }
    
    public boolean isContextCopyingEnabled() {
        return this.contextCopyingEnabled;
    }
    
    public void setContextCopyingEnabled(final boolean contextCopyingEnabled) {
        this.contextCopyingEnabled = contextCopyingEnabled;
    }
    
    @Override
    public final void processStarted(final T context) {
        this.currentContext = this.wrapContext(context);
        this.processor.processStarted(this.currentContext);
        this.startProcess();
    }
    
    private void startProcess() {
        this.ended = false;
        this.rowCount = 0L;
        this.process = this.executor.submit((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() {
                while (AbstractConcurrentProcessor.this.outputQueue == null && !AbstractConcurrentProcessor.this.ended) {
                    Thread.yield();
                }
                while (!AbstractConcurrentProcessor.this.ended) {
                    AbstractConcurrentProcessor.this.rowCount++;
                    AbstractConcurrentProcessor.this.processor.rowProcessed(AbstractConcurrentProcessor.this.outputQueue.row, AbstractConcurrentProcessor.this.outputQueue.context);
                    while (AbstractConcurrentProcessor.this.outputQueue.next == null) {
                        if (AbstractConcurrentProcessor.this.ended && AbstractConcurrentProcessor.this.outputQueue.next == null) {
                            return null;
                        }
                        Thread.yield();
                    }
                    AbstractConcurrentProcessor.this.outputQueue = AbstractConcurrentProcessor.this.outputQueue.next;
                    AbstractConcurrentProcessor.this.output++;
                    if (AbstractConcurrentProcessor.this.limit > 1) {
                        synchronized (AbstractConcurrentProcessor.this.lock) {
                            AbstractConcurrentProcessor.this.lock.notify();
                        }
                    }
                }
                while (AbstractConcurrentProcessor.this.outputQueue != null) {
                    AbstractConcurrentProcessor.this.rowCount++;
                    AbstractConcurrentProcessor.this.processor.rowProcessed(AbstractConcurrentProcessor.this.outputQueue.row, AbstractConcurrentProcessor.this.outputQueue.context);
                    AbstractConcurrentProcessor.this.outputQueue = AbstractConcurrentProcessor.this.outputQueue.next;
                }
                return null;
            }
        });
    }
    
    @Override
    public final void rowProcessed(final String[] row, final T context) {
        if (this.inputQueue == null) {
            this.inputQueue = new Node<T>(row, (T)this.grabContext(context));
            this.outputQueue = this.inputQueue;
        }
        else {
            if (this.limit > 1) {
                synchronized (this.lock) {
                    try {
                        if (this.input - this.output >= this.limit) {
                            this.lock.wait();
                        }
                    }
                    catch (InterruptedException e) {
                        this.ended = true;
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            this.inputQueue.next = (Node<T>)new Node(row, this.grabContext((T)context));
            this.inputQueue = this.inputQueue.next;
        }
        ++this.input;
    }
    
    @Override
    public final void processEnded(final T context) {
        this.ended = true;
        while (true) {
            if (this.limit > 1) {
                synchronized (this.lock) {
                    this.lock.notify();
                }
                try {
                    this.process.get();
                }
                catch (ExecutionException e) {
                    throw new DataProcessingException("Error executing process", e);
                }
                catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                }
                finally {
                    try {
                        this.processor.processEnded(this.grabContext(context));
                    }
                    finally {
                        this.executor.shutdown();
                    }
                }
                return;
            }
            continue;
        }
    }
    
    private T grabContext(final T context) {
        if (this.contextCopyingEnabled) {
            return this.copyContext(context);
        }
        return this.currentContext;
    }
    
    protected final long getRowCount() {
        return this.rowCount;
    }
    
    protected abstract T copyContext(final T p0);
    
    protected abstract T wrapContext(final T p0);
    
    private static class Node<T>
    {
        public final T context;
        public final String[] row;
        public Node next;
        
        public Node(final String[] row, final T context) {
            this.row = row;
            this.context = context;
        }
    }
}
