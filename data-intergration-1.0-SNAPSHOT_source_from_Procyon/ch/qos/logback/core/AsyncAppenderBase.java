// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import ch.qos.logback.core.spi.AppenderAttachable;

public class AsyncAppenderBase<E> extends UnsynchronizedAppenderBase<E> implements AppenderAttachable<E>
{
    AppenderAttachableImpl<E> aai;
    BlockingQueue<E> blockingQueue;
    public static final int DEFAULT_QUEUE_SIZE = 256;
    int queueSize;
    int appenderCount;
    static final int UNDEFINED = -1;
    int discardingThreshold;
    boolean neverBlock;
    Worker worker;
    public static final int DEFAULT_MAX_FLUSH_TIME = 1000;
    int maxFlushTime;
    
    public AsyncAppenderBase() {
        this.aai = new AppenderAttachableImpl<E>();
        this.queueSize = 256;
        this.appenderCount = 0;
        this.discardingThreshold = -1;
        this.neverBlock = false;
        this.worker = new Worker();
        this.maxFlushTime = 1000;
    }
    
    protected boolean isDiscardable(final E eventObject) {
        return false;
    }
    
    protected void preprocess(final E eventObject) {
    }
    
    @Override
    public void start() {
        if (this.isStarted()) {
            return;
        }
        if (this.appenderCount == 0) {
            this.addError("No attached appenders found.");
            return;
        }
        if (this.queueSize < 1) {
            this.addError("Invalid queue size [" + this.queueSize + "]");
            return;
        }
        this.blockingQueue = new ArrayBlockingQueue<E>(this.queueSize);
        if (this.discardingThreshold == -1) {
            this.discardingThreshold = this.queueSize / 5;
        }
        this.addInfo("Setting discardingThreshold to " + this.discardingThreshold);
        this.worker.setDaemon(true);
        this.worker.setName("AsyncAppender-Worker-" + this.getName());
        super.start();
        this.worker.start();
    }
    
    @Override
    public void stop() {
        if (!this.isStarted()) {
            return;
        }
        super.stop();
        this.worker.interrupt();
        try {
            this.worker.join(this.maxFlushTime);
            if (this.worker.isAlive()) {
                this.addWarn("Max queue flush timeout (" + this.maxFlushTime + " ms) exceeded. Approximately " + this.blockingQueue.size() + " queued events were possibly discarded.");
            }
            else {
                this.addInfo("Queue flush finished successfully within timeout.");
            }
        }
        catch (InterruptedException e) {
            this.addError("Failed to join worker thread. " + this.blockingQueue.size() + " queued events may be discarded.", e);
        }
    }
    
    @Override
    protected void append(final E eventObject) {
        if (this.isQueueBelowDiscardingThreshold() && this.isDiscardable(eventObject)) {
            return;
        }
        this.preprocess(eventObject);
        this.put(eventObject);
    }
    
    private boolean isQueueBelowDiscardingThreshold() {
        return this.blockingQueue.remainingCapacity() < this.discardingThreshold;
    }
    
    private void put(final E eventObject) {
        if (this.neverBlock) {
            this.blockingQueue.offer(eventObject);
        }
        else {
            try {
                this.blockingQueue.put(eventObject);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public int getQueueSize() {
        return this.queueSize;
    }
    
    public void setQueueSize(final int queueSize) {
        this.queueSize = queueSize;
    }
    
    public int getDiscardingThreshold() {
        return this.discardingThreshold;
    }
    
    public void setDiscardingThreshold(final int discardingThreshold) {
        this.discardingThreshold = discardingThreshold;
    }
    
    public int getMaxFlushTime() {
        return this.maxFlushTime;
    }
    
    public void setMaxFlushTime(final int maxFlushTime) {
        this.maxFlushTime = maxFlushTime;
    }
    
    public int getNumberOfElementsInQueue() {
        return this.blockingQueue.size();
    }
    
    public void setNeverBlock(final boolean neverBlock) {
        this.neverBlock = neverBlock;
    }
    
    public boolean isNeverBlock() {
        return this.neverBlock;
    }
    
    public int getRemainingCapacity() {
        return this.blockingQueue.remainingCapacity();
    }
    
    @Override
    public void addAppender(final Appender<E> newAppender) {
        if (this.appenderCount == 0) {
            ++this.appenderCount;
            this.addInfo("Attaching appender named [" + newAppender.getName() + "] to AsyncAppender.");
            this.aai.addAppender(newAppender);
        }
        else {
            this.addWarn("One and only one appender may be attached to AsyncAppender.");
            this.addWarn("Ignoring additional appender named [" + newAppender.getName() + "]");
        }
    }
    
    @Override
    public Iterator<Appender<E>> iteratorForAppenders() {
        return this.aai.iteratorForAppenders();
    }
    
    @Override
    public Appender<E> getAppender(final String name) {
        return this.aai.getAppender(name);
    }
    
    @Override
    public boolean isAttached(final Appender<E> eAppender) {
        return this.aai.isAttached(eAppender);
    }
    
    @Override
    public void detachAndStopAllAppenders() {
        this.aai.detachAndStopAllAppenders();
    }
    
    @Override
    public boolean detachAppender(final Appender<E> eAppender) {
        return this.aai.detachAppender(eAppender);
    }
    
    @Override
    public boolean detachAppender(final String name) {
        return this.aai.detachAppender(name);
    }
    
    class Worker extends Thread
    {
        @Override
        public void run() {
            final AsyncAppenderBase<E> parent = AsyncAppenderBase.this;
            final AppenderAttachableImpl<E> aai = parent.aai;
            while (parent.isStarted()) {
                try {
                    final E e = parent.blockingQueue.take();
                    aai.appendLoopOnAppenders(e);
                    continue;
                }
                catch (InterruptedException ie) {}
                break;
            }
            AsyncAppenderBase.this.addInfo("Worker thread will flush remaining events before exiting. ");
            for (final E e2 : parent.blockingQueue) {
                aai.appendLoopOnAppenders(e2);
                parent.blockingQueue.remove(e2);
            }
            aai.detachAndStopAllAppenders();
        }
    }
}
