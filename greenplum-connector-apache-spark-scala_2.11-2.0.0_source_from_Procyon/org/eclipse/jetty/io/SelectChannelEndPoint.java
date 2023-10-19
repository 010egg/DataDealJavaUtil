// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import org.eclipse.jetty.util.log.Log;
import java.nio.channels.CancelledKeyException;
import org.eclipse.jetty.util.thread.Scheduler;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.channels.SelectionKey;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.util.log.Logger;

public class SelectChannelEndPoint extends ChannelEndPoint implements SelectorManager.SelectableEndPoint
{
    public static final Logger LOG;
    private final Runnable _updateTask;
    private final AtomicBoolean _open;
    private final SelectorManager.ManagedSelector _selector;
    private final SelectionKey _key;
    private final AtomicInteger _interestOps;
    
    public SelectChannelEndPoint(final SocketChannel channel, final SelectorManager.ManagedSelector selector, final SelectionKey key, final Scheduler scheduler, final long idleTimeout) {
        super(scheduler, channel);
        this._updateTask = new Runnable() {
            @Override
            public void run() {
                try {
                    if (SelectChannelEndPoint.this.getChannel().isOpen()) {
                        final int oldInterestOps = SelectChannelEndPoint.this._key.interestOps();
                        final int newInterestOps = SelectChannelEndPoint.this._interestOps.get();
                        if (newInterestOps != oldInterestOps) {
                            SelectChannelEndPoint.this.setKeyInterests(oldInterestOps, newInterestOps);
                        }
                    }
                }
                catch (CancelledKeyException x2) {
                    SelectChannelEndPoint.LOG.debug("Ignoring key update for concurrently closed channel {}", this);
                    SelectChannelEndPoint.this.close();
                }
                catch (Exception x) {
                    SelectChannelEndPoint.LOG.warn("Ignoring key update for " + this, x);
                    SelectChannelEndPoint.this.close();
                }
            }
        };
        this._open = new AtomicBoolean();
        this._interestOps = new AtomicInteger();
        this._selector = selector;
        this._key = key;
        this.setIdleTimeout(idleTimeout);
    }
    
    @Override
    protected boolean needsFill() {
        this.updateLocalInterests(1, true);
        return false;
    }
    
    @Override
    protected void onIncompleteFlush() {
        this.updateLocalInterests(4, true);
    }
    
    @Override
    public void onSelected() {
        assert this._selector.isSelectorThread();
        final int oldInterestOps = this._key.interestOps();
        final int readyOps = this._key.readyOps();
        final int newInterestOps = oldInterestOps & ~readyOps;
        this.setKeyInterests(oldInterestOps, newInterestOps);
        this.updateLocalInterests(readyOps, false);
        if (this._key.isReadable()) {
            this.getFillInterest().fillable();
        }
        if (this._key.isWritable()) {
            this.getWriteFlusher().completeWrite();
        }
    }
    
    private void updateLocalInterests(final int operation, final boolean add) {
        while (true) {
            final int oldInterestOps = this._interestOps.get();
            int newInterestOps;
            if (add) {
                newInterestOps = (oldInterestOps | operation);
            }
            else {
                newInterestOps = (oldInterestOps & ~operation);
            }
            if (this.isInputShutdown()) {
                newInterestOps &= 0xFFFFFFFE;
            }
            if (this.isOutputShutdown()) {
                newInterestOps &= 0xFFFFFFFB;
            }
            if (newInterestOps != oldInterestOps) {
                if (this._interestOps.compareAndSet(oldInterestOps, newInterestOps)) {
                    if (SelectChannelEndPoint.LOG.isDebugEnabled()) {
                        SelectChannelEndPoint.LOG.debug("Local interests updating {} -> {} for {}", oldInterestOps, newInterestOps, this);
                    }
                    this._selector.updateKey(this._updateTask);
                    break;
                }
                if (!SelectChannelEndPoint.LOG.isDebugEnabled()) {
                    continue;
                }
                SelectChannelEndPoint.LOG.debug("Local interests update conflict: now {}, was {}, attempted {} for {}", this._interestOps.get(), oldInterestOps, newInterestOps, this);
            }
            else {
                if (SelectChannelEndPoint.LOG.isDebugEnabled()) {
                    SelectChannelEndPoint.LOG.debug("Ignoring local interests update {} -> {} for {}", oldInterestOps, newInterestOps, this);
                    break;
                }
                break;
            }
        }
    }
    
    private void setKeyInterests(final int oldInterestOps, final int newInterestOps) {
        this._key.interestOps(newInterestOps);
        if (SelectChannelEndPoint.LOG.isDebugEnabled()) {
            SelectChannelEndPoint.LOG.debug("Key interests updated {} -> {} on {}", oldInterestOps, newInterestOps, this);
        }
    }
    
    @Override
    public void close() {
        if (this._open.compareAndSet(true, false)) {
            super.close();
            this._selector.destroyEndPoint(this);
        }
    }
    
    @Override
    public boolean isOpen() {
        return this._open.get();
    }
    
    @Override
    public void onOpen() {
        if (this._open.compareAndSet(false, true)) {
            super.onOpen();
        }
    }
    
    @Override
    public String toString() {
        try {
            final boolean valid = this._key != null && this._key.isValid();
            final int keyInterests = valid ? this._key.interestOps() : -1;
            final int keyReadiness = valid ? this._key.readyOps() : -1;
            return String.format("%s{io=%d,kio=%d,kro=%d}", super.toString(), this._interestOps.get(), keyInterests, keyReadiness);
        }
        catch (CancelledKeyException x) {
            return String.format("%s{io=%s,kio=-2,kro=-2}", super.toString(), this._interestOps.get());
        }
    }
    
    static {
        LOG = Log.getLogger(SelectChannelEndPoint.class);
    }
}
