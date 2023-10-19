// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import org.eclipse.jetty.util.log.Log;
import java.nio.channels.ClosedChannelException;
import java.io.IOException;
import java.nio.channels.ReadPendingException;
import org.eclipse.jetty.util.Callback;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.jetty.util.log.Logger;

public abstract class FillInterest
{
    private static final Logger LOG;
    private final AtomicReference<Callback> _interested;
    
    protected FillInterest() {
        this._interested = new AtomicReference<Callback>(null);
    }
    
    public <C> void register(final Callback callback) throws ReadPendingException {
        if (callback == null) {
            throw new IllegalArgumentException();
        }
        if (!this._interested.compareAndSet(null, callback)) {
            FillInterest.LOG.warn("Read pending for " + this._interested.get() + " prevented " + callback, new Object[0]);
            throw new ReadPendingException();
        }
        try {
            if (this.needsFill()) {
                this.fillable();
            }
        }
        catch (IOException e) {
            this.onFail(e);
        }
    }
    
    public void fillable() {
        final Callback callback = this._interested.get();
        if (callback != null && this._interested.compareAndSet(callback, null)) {
            callback.succeeded();
        }
    }
    
    public boolean isInterested() {
        return this._interested.get() != null;
    }
    
    public boolean onFail(final Throwable cause) {
        final Callback callback = this._interested.get();
        if (callback != null && this._interested.compareAndSet(callback, null)) {
            callback.failed(cause);
            return true;
        }
        return false;
    }
    
    public void onClose() {
        final Callback callback = this._interested.get();
        if (callback != null && this._interested.compareAndSet(callback, null)) {
            callback.failed(new ClosedChannelException());
        }
    }
    
    @Override
    public String toString() {
        return String.format("FillInterest@%x{%b,%s}", this.hashCode(), this._interested.get(), this._interested.get());
    }
    
    protected abstract boolean needsFill() throws IOException;
    
    static {
        LOG = Log.getLogger(FillInterest.class);
    }
}
