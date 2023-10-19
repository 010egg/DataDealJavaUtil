// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.log.Log;
import java.io.InterruptedIOException;
import java.io.IOException;
import org.eclipse.jetty.util.ArrayQueue;
import org.eclipse.jetty.util.log.Logger;

public abstract class QueuedHttpInput<T> extends HttpInput<T>
{
    private static final Logger LOG;
    private final ArrayQueue<T> _inputQ;
    
    public QueuedHttpInput() {
        this._inputQ = new ArrayQueue<T>(this.lock());
    }
    
    @Override
    public void content(final T item) {
        synchronized (this.lock()) {
            final boolean wasEmpty = this._inputQ.isEmpty();
            this._inputQ.add(item);
            if (QueuedHttpInput.LOG.isDebugEnabled()) {
                QueuedHttpInput.LOG.debug("{} queued {}", this, item);
            }
            if (wasEmpty && !this.onAsyncRead()) {
                this.lock().notify();
            }
        }
    }
    
    @Override
    public void recycle() {
        synchronized (this.lock()) {
            for (T item = this._inputQ.pollUnsafe(); item != null; item = this._inputQ.pollUnsafe()) {
                this.onContentConsumed(item);
            }
            super.recycle();
        }
    }
    
    @Override
    protected T nextContent() {
        synchronized (this.lock()) {
            T item;
            for (item = this._inputQ.peekUnsafe(); item != null && this.remaining(item) == 0; item = this._inputQ.peekUnsafe()) {
                this._inputQ.pollUnsafe();
                this.onContentConsumed(item);
                if (QueuedHttpInput.LOG.isDebugEnabled()) {
                    QueuedHttpInput.LOG.debug("{} consumed {}", this, item);
                }
            }
            return item;
        }
    }
    
    @Override
    protected void blockForContent() throws IOException {
        synchronized (this.lock()) {
            while (this._inputQ.isEmpty() && !this.isFinished() && !this.isEOF()) {
                try {
                    if (QueuedHttpInput.LOG.isDebugEnabled()) {
                        QueuedHttpInput.LOG.debug("{} waiting for content", this);
                    }
                    this.lock().wait();
                    continue;
                }
                catch (InterruptedException e) {
                    throw (IOException)new InterruptedIOException().initCause(e);
                }
                break;
            }
        }
    }
    
    protected abstract void onContentConsumed(final T p0);
    
    @Override
    public void earlyEOF() {
        synchronized (this.lock()) {
            super.earlyEOF();
            this.lock().notify();
        }
    }
    
    @Override
    public void messageComplete() {
        synchronized (this.lock()) {
            super.messageComplete();
            this.lock().notify();
        }
    }
    
    static {
        LOG = Log.getLogger(QueuedHttpInput.class);
    }
}
