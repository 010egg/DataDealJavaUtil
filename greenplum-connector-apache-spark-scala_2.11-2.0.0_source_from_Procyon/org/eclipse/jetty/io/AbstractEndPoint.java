// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.BufferUtil;
import java.util.concurrent.TimeoutException;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.Callback;
import java.io.IOException;
import org.eclipse.jetty.util.thread.Scheduler;
import java.net.InetSocketAddress;
import org.eclipse.jetty.util.log.Logger;

public abstract class AbstractEndPoint extends IdleTimeout implements EndPoint
{
    private static final Logger LOG;
    private final long _created;
    private final InetSocketAddress _local;
    private final InetSocketAddress _remote;
    private volatile Connection _connection;
    private final FillInterest _fillInterest;
    private final WriteFlusher _writeFlusher;
    
    protected AbstractEndPoint(final Scheduler scheduler, final InetSocketAddress local, final InetSocketAddress remote) {
        super(scheduler);
        this._created = System.currentTimeMillis();
        this._fillInterest = new FillInterest() {
            @Override
            protected boolean needsFill() throws IOException {
                return AbstractEndPoint.this.needsFill();
            }
        };
        this._writeFlusher = new WriteFlusher((EndPoint)this) {
            @Override
            protected void onIncompleteFlushed() {
                AbstractEndPoint.this.onIncompleteFlush();
            }
        };
        this._local = local;
        this._remote = remote;
    }
    
    @Override
    public long getCreatedTimeStamp() {
        return this._created;
    }
    
    @Override
    public InetSocketAddress getLocalAddress() {
        return this._local;
    }
    
    @Override
    public InetSocketAddress getRemoteAddress() {
        return this._remote;
    }
    
    @Override
    public Connection getConnection() {
        return this._connection;
    }
    
    @Override
    public void setConnection(final Connection connection) {
        this._connection = connection;
    }
    
    @Override
    public void onOpen() {
        if (AbstractEndPoint.LOG.isDebugEnabled()) {
            AbstractEndPoint.LOG.debug("onOpen {}", this);
        }
        super.onOpen();
    }
    
    @Override
    public void onClose() {
        super.onClose();
        if (AbstractEndPoint.LOG.isDebugEnabled()) {
            AbstractEndPoint.LOG.debug("onClose {}", this);
        }
        this._writeFlusher.onClose();
        this._fillInterest.onClose();
    }
    
    @Override
    public void close() {
        this.onClose();
    }
    
    @Override
    public void fillInterested(final Callback callback) throws IllegalStateException {
        this.notIdle();
        this._fillInterest.register(callback);
    }
    
    @Override
    public void write(final Callback callback, final ByteBuffer... buffers) throws IllegalStateException {
        this._writeFlusher.write(callback, buffers);
    }
    
    protected abstract void onIncompleteFlush();
    
    protected abstract boolean needsFill() throws IOException;
    
    protected FillInterest getFillInterest() {
        return this._fillInterest;
    }
    
    protected WriteFlusher getWriteFlusher() {
        return this._writeFlusher;
    }
    
    @Override
    protected void onIdleExpired(final TimeoutException timeout) {
        final boolean output_shutdown = this.isOutputShutdown();
        final boolean input_shutdown = this.isInputShutdown();
        final boolean fillFailed = this._fillInterest.onFail(timeout);
        final boolean writeFailed = this._writeFlusher.onFail(timeout);
        if (this.isOpen() && (output_shutdown || input_shutdown) && !fillFailed && !writeFailed) {
            this.close();
        }
        else {
            AbstractEndPoint.LOG.debug("Ignored idle endpoint {}", this);
        }
    }
    
    @Override
    public void upgrade(final Connection newConnection) {
        final Connection old_connection = this.getConnection();
        if (AbstractEndPoint.LOG.isDebugEnabled()) {
            AbstractEndPoint.LOG.debug("{} upgrading from {} to {}", this, old_connection, newConnection);
        }
        final ByteBuffer prefilled = (old_connection instanceof Connection.UpgradeFrom) ? ((Connection.UpgradeFrom)old_connection).onUpgradeFrom() : null;
        old_connection.onClose();
        old_connection.getEndPoint().setConnection(newConnection);
        if (newConnection instanceof Connection.UpgradeTo) {
            ((Connection.UpgradeTo)newConnection).onUpgradeTo(prefilled);
        }
        else if (BufferUtil.hasContent(prefilled)) {
            throw new IllegalStateException();
        }
        newConnection.onOpen();
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x{%s<->%d,%s,%s,%s,%s,%s,%d/%d,%s}", this.getClass().getSimpleName(), this.hashCode(), this.getRemoteAddress(), this.getLocalAddress().getPort(), this.isOpen() ? "Open" : "CLOSED", this.isInputShutdown() ? "ISHUT" : "in", this.isOutputShutdown() ? "OSHUT" : "out", this._fillInterest.isInterested() ? "R" : "-", this._writeFlusher.isInProgress() ? "W" : "-", this.getIdleFor(), this.getIdleTimeout(), (this.getConnection() == null) ? null : this.getConnection().getClass().getSimpleName());
    }
    
    static {
        LOG = Log.getLogger(AbstractEndPoint.class);
    }
}
