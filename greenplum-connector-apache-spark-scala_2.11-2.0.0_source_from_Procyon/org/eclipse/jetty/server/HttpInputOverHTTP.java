// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.BufferUtil;
import java.io.IOException;
import org.eclipse.jetty.util.SharedBlockingCallback;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.Callback;
import java.nio.ByteBuffer;

public class HttpInputOverHTTP extends HttpInput<ByteBuffer> implements Callback
{
    private static final Logger LOG;
    private final SharedBlockingCallback _readBlocker;
    private final HttpConnection _httpConnection;
    private ByteBuffer _content;
    
    public HttpInputOverHTTP(final HttpConnection httpConnection) {
        this._readBlocker = new SharedBlockingCallback();
        this._httpConnection = httpConnection;
    }
    
    @Override
    public void recycle() {
        synchronized (this.lock()) {
            super.recycle();
            this._content = null;
        }
    }
    
    @Override
    protected void blockForContent() throws IOException {
        Object content;
        do {
            try (final SharedBlockingCallback.Blocker blocker = this._readBlocker.acquire()) {
                this._httpConnection.fillInterested(blocker);
                if (HttpInputOverHTTP.LOG.isDebugEnabled()) {
                    HttpInputOverHTTP.LOG.debug("{} block readable on {}", this, blocker);
                }
                blocker.block();
            }
            content = ((HttpInput<Object>)this).getNextContent();
        } while (content == null && !this.isFinished());
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x", this.getClass().getSimpleName(), this.hashCode());
    }
    
    @Override
    protected ByteBuffer nextContent() throws IOException {
        if (BufferUtil.hasContent(this._content)) {
            return this._content;
        }
        this._content = null;
        this._httpConnection.parseContent();
        if (BufferUtil.hasContent(this._content)) {
            return this._content;
        }
        return null;
    }
    
    @Override
    protected int remaining(final ByteBuffer item) {
        return item.remaining();
    }
    
    @Override
    protected int get(final ByteBuffer item, final byte[] buffer, final int offset, final int length) {
        final int l = Math.min(item.remaining(), length);
        item.get(buffer, offset, l);
        return l;
    }
    
    @Override
    protected void consume(final ByteBuffer item, final int length) {
        item.position(item.position() + length);
    }
    
    @Override
    public void content(final ByteBuffer item) {
        if (BufferUtil.hasContent(this._content)) {
            throw new IllegalStateException();
        }
        this._content = item;
    }
    
    @Override
    protected void unready() {
        this._httpConnection.fillInterested(this);
    }
    
    @Override
    public void succeeded() {
        this._httpConnection.getHttpChannel().getState().onReadPossible();
    }
    
    @Override
    public void failed(final Throwable x) {
        super.failed(x);
        this._httpConnection.getHttpChannel().getState().onReadPossible();
    }
    
    static {
        LOG = Log.getLogger(HttpInputOverHTTP.class);
    }
}
