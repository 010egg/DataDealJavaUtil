// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import org.eclipse.jetty.util.log.Log;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.thread.Scheduler;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import org.eclipse.jetty.util.log.Logger;

public class ByteArrayEndPoint extends AbstractEndPoint
{
    static final Logger LOG;
    public static final InetSocketAddress NOIP;
    protected volatile ByteBuffer _in;
    protected volatile ByteBuffer _out;
    protected volatile boolean _ishut;
    protected volatile boolean _oshut;
    protected volatile boolean _closed;
    protected volatile boolean _growOutput;
    
    public ByteArrayEndPoint() {
        this(null, 0L, null, null);
    }
    
    public ByteArrayEndPoint(final byte[] input, final int outputSize) {
        this(null, 0L, (input != null) ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
    }
    
    public ByteArrayEndPoint(final String input, final int outputSize) {
        this(null, 0L, (input != null) ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
    }
    
    public ByteArrayEndPoint(final Scheduler scheduler, final long idleTimeoutMs) {
        this(scheduler, idleTimeoutMs, null, null);
    }
    
    public ByteArrayEndPoint(final Scheduler timer, final long idleTimeoutMs, final byte[] input, final int outputSize) {
        this(timer, idleTimeoutMs, (input != null) ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
    }
    
    public ByteArrayEndPoint(final Scheduler timer, final long idleTimeoutMs, final String input, final int outputSize) {
        this(timer, idleTimeoutMs, (input != null) ? BufferUtil.toBuffer(input) : null, BufferUtil.allocate(outputSize));
    }
    
    public ByteArrayEndPoint(final Scheduler timer, final long idleTimeoutMs, final ByteBuffer input, final ByteBuffer output) {
        super(timer, ByteArrayEndPoint.NOIP, ByteArrayEndPoint.NOIP);
        this._in = ((input == null) ? BufferUtil.EMPTY_BUFFER : input);
        this._out = ((output == null) ? BufferUtil.allocate(1024) : output);
        this.setIdleTimeout(idleTimeoutMs);
    }
    
    @Override
    protected void onIncompleteFlush() {
    }
    
    @Override
    protected boolean needsFill() throws IOException {
        if (this._closed) {
            throw new ClosedChannelException();
        }
        return this._in == null || BufferUtil.hasContent(this._in);
    }
    
    public ByteBuffer getIn() {
        return this._in;
    }
    
    public void setInputEOF() {
        this._in = null;
    }
    
    public void setInput(final ByteBuffer in) {
        this._in = in;
        if (in == null || BufferUtil.hasContent(in)) {
            this.getFillInterest().fillable();
        }
    }
    
    public void setInput(final String s) {
        this.setInput(BufferUtil.toBuffer(s, StandardCharsets.UTF_8));
    }
    
    public void setInput(final String s, final Charset charset) {
        this.setInput(BufferUtil.toBuffer(s, charset));
    }
    
    public ByteBuffer getOutput() {
        return this._out;
    }
    
    public String getOutputString() {
        return this.getOutputString(StandardCharsets.UTF_8);
    }
    
    public String getOutputString(final Charset charset) {
        return BufferUtil.toString(this._out, charset);
    }
    
    public ByteBuffer takeOutput() {
        final ByteBuffer b = this._out;
        this._out = BufferUtil.allocate(b.capacity());
        this.getWriteFlusher().completeWrite();
        return b;
    }
    
    public String takeOutputString() {
        return this.takeOutputString(StandardCharsets.UTF_8);
    }
    
    public String takeOutputString(final Charset charset) {
        final ByteBuffer buffer = this.takeOutput();
        return BufferUtil.toString(buffer, charset);
    }
    
    public void setOutput(final ByteBuffer out) {
        this._out = out;
        this.getWriteFlusher().completeWrite();
    }
    
    @Override
    public boolean isOpen() {
        return !this._closed;
    }
    
    @Override
    public boolean isInputShutdown() {
        return this._ishut || this._closed;
    }
    
    @Override
    public boolean isOutputShutdown() {
        return this._oshut || this._closed;
    }
    
    private void shutdownInput() {
        this._ishut = true;
        if (this._oshut) {
            this.close();
        }
    }
    
    @Override
    public void shutdownOutput() {
        this._oshut = true;
        if (this._ishut) {
            this.close();
        }
    }
    
    @Override
    public void close() {
        super.close();
        this._closed = true;
    }
    
    public boolean hasMore() {
        return this.getOutput().position() > 0;
    }
    
    @Override
    public int fill(final ByteBuffer buffer) throws IOException {
        if (this._closed) {
            throw new EofException("CLOSED");
        }
        if (this._in == null) {
            this.shutdownInput();
        }
        if (this._ishut) {
            return -1;
        }
        final int filled = BufferUtil.append(buffer, this._in);
        if (filled > 0) {
            this.notIdle();
        }
        return filled;
    }
    
    @Override
    public boolean flush(final ByteBuffer... buffers) throws IOException {
        if (this._closed) {
            throw new IOException("CLOSED");
        }
        if (this._oshut) {
            throw new IOException("OSHUT");
        }
        boolean flushed = true;
        boolean idle = true;
        for (final ByteBuffer b : buffers) {
            if (BufferUtil.hasContent(b)) {
                if (this._growOutput && b.remaining() > BufferUtil.space(this._out)) {
                    BufferUtil.compact(this._out);
                    if (b.remaining() > BufferUtil.space(this._out)) {
                        final ByteBuffer n = BufferUtil.allocate(this._out.capacity() + b.remaining() * 2);
                        BufferUtil.append(n, this._out);
                        this._out = n;
                    }
                }
                if (BufferUtil.append(this._out, b) > 0) {
                    idle = false;
                }
                if (BufferUtil.hasContent(b)) {
                    flushed = false;
                    break;
                }
            }
        }
        if (!idle) {
            this.notIdle();
        }
        return flushed;
    }
    
    public void reset() {
        this.getFillInterest().onClose();
        this.getWriteFlusher().onClose();
        this._ishut = false;
        this._oshut = false;
        this._closed = false;
        this._in = null;
        BufferUtil.clear(this._out);
    }
    
    @Override
    public Object getTransport() {
        return null;
    }
    
    public boolean isGrowOutput() {
        return this._growOutput;
    }
    
    public void setGrowOutput(final boolean growOutput) {
        this._growOutput = growOutput;
    }
    
    static {
        LOG = Log.getLogger(ByteArrayEndPoint.class);
        NOIP = new InetSocketAddress(0);
    }
}
