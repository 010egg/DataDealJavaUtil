// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.IteratingNestedCallback;
import org.eclipse.jetty.util.IteratingCallback;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.http.HttpContent;
import java.nio.channels.ReadableByteChannel;
import java.io.InputStream;
import java.nio.channels.WritePendingException;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.util.Callback;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.WriteListener;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.SharedBlockingCallback;
import org.eclipse.jetty.util.log.Logger;
import javax.servlet.ServletOutputStream;

public class HttpOutput extends ServletOutputStream implements Runnable
{
    private static Logger LOG;
    private final HttpChannel<?> _channel;
    private final SharedBlockingCallback _writeblock;
    private long _written;
    private ByteBuffer _aggregate;
    private int _bufferSize;
    private int _commitSize;
    private WriteListener _writeListener;
    private volatile Throwable _onError;
    private final AtomicReference<OutputState> _state;
    
    public HttpOutput(final HttpChannel<?> channel) {
        this._writeblock = new SharedBlockingCallback() {
            @Override
            protected long getIdleTimeout() {
                return HttpOutput.this._channel.getIdleTimeout();
            }
        };
        this._state = new AtomicReference<OutputState>(OutputState.OPEN);
        this._channel = channel;
        final HttpConfiguration config = channel.getHttpConfiguration();
        this._bufferSize = config.getOutputBufferSize();
        this._commitSize = config.getOutputAggregationSize();
        if (this._commitSize > this._bufferSize) {
            HttpOutput.LOG.warn("OutputAggregationSize {} exceeds bufferSize {}", this._commitSize, this._bufferSize);
            this._commitSize = this._bufferSize;
        }
    }
    
    public HttpChannel<?> getHttpChannel() {
        return this._channel;
    }
    
    public boolean isWritten() {
        return this._written > 0L;
    }
    
    public long getWritten() {
        return this._written;
    }
    
    public void reset() {
        this._written = 0L;
        this.reopen();
    }
    
    public void reopen() {
        this._state.set(OutputState.OPEN);
    }
    
    public boolean isAllContentWritten() {
        return this._channel.getResponse().isAllContentWritten(this._written);
    }
    
    protected SharedBlockingCallback.Blocker acquireWriteBlockingCallback() throws IOException {
        return this._writeblock.acquire();
    }
    
    protected void write(final ByteBuffer content, final boolean complete) throws IOException {
        try (final SharedBlockingCallback.Blocker blocker = this._writeblock.acquire()) {
            this.write(content, complete, blocker);
            blocker.block();
        }
    }
    
    protected void write(final ByteBuffer content, final boolean complete, final Callback callback) {
        this._channel.write(content, complete, callback);
    }
    
    @Override
    public void close() {
        while (true) {
            final OutputState state = this._state.get();
            switch (state) {
                case CLOSED: {}
                case UNREADY: {
                    if (this._state.compareAndSet(state, OutputState.ERROR)) {
                        this._writeListener.onError((this._onError == null) ? new EofException("Async close") : this._onError);
                        continue;
                    }
                    continue;
                }
                default: {
                    if (this._state.compareAndSet(state, OutputState.CLOSED)) {
                        try {
                            this.write(BufferUtil.hasContent(this._aggregate) ? this._aggregate : BufferUtil.EMPTY_BUFFER, !this._channel.getResponse().isIncluding());
                        }
                        catch (IOException e) {
                            HttpOutput.LOG.debug(e);
                            this._channel.abort();
                        }
                        this.releaseBuffer();
                        return;
                    }
                    continue;
                }
            }
        }
    }
    
    void closed() {
        while (true) {
            final OutputState state = this._state.get();
            switch (state) {
                case CLOSED: {}
                case UNREADY: {
                    if (this._state.compareAndSet(state, OutputState.ERROR)) {
                        this._writeListener.onError((this._onError == null) ? new EofException("Async closed") : this._onError);
                        continue;
                    }
                    continue;
                }
                default: {
                    if (this._state.compareAndSet(state, OutputState.CLOSED)) {
                        try {
                            this._channel.getResponse().closeOutput();
                        }
                        catch (IOException e) {
                            HttpOutput.LOG.debug(e);
                            this._channel.abort();
                        }
                        this.releaseBuffer();
                        return;
                    }
                    continue;
                }
            }
        }
    }
    
    private void releaseBuffer() {
        if (this._aggregate != null) {
            this._channel.getConnector().getByteBufferPool().release(this._aggregate);
            this._aggregate = null;
        }
    }
    
    public boolean isClosed() {
        return this._state.get() == OutputState.CLOSED;
    }
    
    @Override
    public void flush() throws IOException {
        while (true) {
            switch (this._state.get()) {
                case OPEN: {
                    this.write(BufferUtil.hasContent(this._aggregate) ? this._aggregate : BufferUtil.EMPTY_BUFFER, false);
                }
                case ASYNC: {
                    throw new IllegalStateException("isReady() not called");
                }
                case READY: {
                    if (!this._state.compareAndSet(OutputState.READY, OutputState.PENDING)) {
                        continue;
                    }
                    new AsyncFlush().iterate();
                }
                case UNREADY:
                case PENDING: {
                    throw new WritePendingException();
                }
                case ERROR: {
                    throw new EofException(this._onError);
                }
                case CLOSED: {}
                default: {}
            }
        }
    }
    
    @Override
    public void write(final byte[] b, int off, int len) throws IOException {
        this._written += len;
        final boolean complete = this._channel.getResponse().isAllContentWritten(this._written);
    Label_0271:
        while (true) {
            switch (this._state.get()) {
                case ASYNC: {
                    throw new IllegalStateException("isReady() not called");
                }
                case READY: {
                    if (!this._state.compareAndSet(OutputState.READY, OutputState.PENDING)) {
                        continue;
                    }
                    if (!complete && len <= this._commitSize) {
                        if (this._aggregate == null) {
                            this._aggregate = this._channel.getByteBufferPool().acquire(this.getBufferSize(), false);
                        }
                        final int filled = BufferUtil.fill(this._aggregate, b, off, len);
                        if (filled == len && !BufferUtil.isFull(this._aggregate)) {
                            if (!this._state.compareAndSet(OutputState.PENDING, OutputState.ASYNC)) {
                                throw new IllegalStateException();
                            }
                            return;
                        }
                        else {
                            off += filled;
                            len -= filled;
                        }
                    }
                    new AsyncWrite(b, off, len, complete).iterate();
                    return;
                }
                case UNREADY:
                case PENDING: {
                    throw new WritePendingException();
                }
                case ERROR: {
                    throw new EofException(this._onError);
                }
                case CLOSED: {
                    throw new EofException("Closed");
                }
                default: {
                    break Label_0271;
                }
                case OPEN: {
                    break Label_0271;
                }
            }
        }
        final int capacity = this.getBufferSize();
        if (!complete && len <= this._commitSize) {
            if (this._aggregate == null) {
                this._aggregate = this._channel.getByteBufferPool().acquire(capacity, false);
            }
            final int filled2 = BufferUtil.fill(this._aggregate, b, off, len);
            if (filled2 == len && !BufferUtil.isFull(this._aggregate)) {
                return;
            }
            off += filled2;
            len -= filled2;
        }
        if (BufferUtil.hasContent(this._aggregate)) {
            this.write(this._aggregate, complete && len == 0);
            if (len > 0 && !complete && len <= this._commitSize && len <= BufferUtil.space(this._aggregate)) {
                BufferUtil.append(this._aggregate, b, off, len);
                return;
            }
        }
        if (len > 0) {
            final ByteBuffer wrap = ByteBuffer.wrap(b, off, len);
            final ByteBuffer view = wrap.duplicate();
            while (len > this.getBufferSize()) {
                final int p = view.position();
                final int l = p + this.getBufferSize();
                view.limit(p + this.getBufferSize());
                this.write(view, false);
                len -= this.getBufferSize();
                view.limit(l + Math.min(len, this.getBufferSize()));
                view.position(l);
            }
            this.write(view, complete);
        }
        else if (complete) {
            this.write(BufferUtil.EMPTY_BUFFER, complete);
        }
        if (complete) {
            this.closed();
        }
    }
    
    public void write(final ByteBuffer buffer) throws IOException {
        this._written += buffer.remaining();
        final boolean complete = this._channel.getResponse().isAllContentWritten(this._written);
    Label_0164:
        while (true) {
            switch (this._state.get()) {
                case ASYNC: {
                    throw new IllegalStateException("isReady() not called");
                }
                case READY: {
                    if (!this._state.compareAndSet(OutputState.READY, OutputState.PENDING)) {
                        continue;
                    }
                    new AsyncWrite(buffer, complete).iterate();
                    return;
                }
                case UNREADY:
                case PENDING: {
                    throw new WritePendingException();
                }
                case ERROR: {
                    throw new EofException(this._onError);
                }
                case CLOSED: {
                    throw new EofException("Closed");
                }
                default: {
                    break Label_0164;
                }
                case OPEN: {
                    break Label_0164;
                }
            }
        }
        final int len = BufferUtil.length(buffer);
        if (BufferUtil.hasContent(this._aggregate)) {
            this.write(this._aggregate, complete && len == 0);
        }
        if (len > 0) {
            this.write(buffer, complete);
        }
        else if (complete) {
            this.write(BufferUtil.EMPTY_BUFFER, complete);
        }
        if (complete) {
            this.closed();
        }
    }
    
    @Override
    public void write(final int b) throws IOException {
        ++this._written;
        final boolean complete = this._channel.getResponse().isAllContentWritten(this._written);
    Label_0399:
        while (true) {
            switch (this._state.get()) {
                case OPEN: {
                    if (this._aggregate == null) {
                        this._aggregate = this._channel.getByteBufferPool().acquire(this.getBufferSize(), false);
                    }
                    BufferUtil.append(this._aggregate, (byte)b);
                    if (!complete && !BufferUtil.isFull(this._aggregate)) {
                        break Label_0399;
                    }
                    try (final SharedBlockingCallback.Blocker blocker = this._writeblock.acquire()) {
                        this.write(this._aggregate, complete, blocker);
                        blocker.block();
                    }
                    if (complete) {
                        this.closed();
                        break Label_0399;
                    }
                    break Label_0399;
                }
                case ASYNC: {
                    throw new IllegalStateException("isReady() not called");
                }
                case READY: {
                    if (!this._state.compareAndSet(OutputState.READY, OutputState.PENDING)) {
                        continue;
                    }
                    if (this._aggregate == null) {
                        this._aggregate = this._channel.getByteBufferPool().acquire(this.getBufferSize(), false);
                    }
                    BufferUtil.append(this._aggregate, (byte)b);
                    if (complete || BufferUtil.isFull(this._aggregate)) {
                        new AsyncFlush().iterate();
                        return;
                    }
                    if (!this._state.compareAndSet(OutputState.PENDING, OutputState.ASYNC)) {
                        throw new IllegalStateException();
                    }
                    return;
                }
                case UNREADY:
                case PENDING: {
                    throw new WritePendingException();
                }
                case ERROR: {
                    throw new EofException(this._onError);
                }
                case CLOSED: {
                    throw new EofException("Closed");
                }
                default: {
                    break Label_0399;
                }
            }
        }
    }
    
    @Override
    public void print(final String s) throws IOException {
        if (this.isClosed()) {
            throw new IOException("Closed");
        }
        this.write(s.getBytes(this._channel.getResponse().getCharacterEncoding()));
    }
    
    public void sendContent(final ByteBuffer content) throws IOException {
        try (final SharedBlockingCallback.Blocker blocker = this._writeblock.acquire()) {
            this.write(content, true, blocker);
            blocker.block();
        }
    }
    
    public void sendContent(final InputStream in) throws IOException {
        try (final SharedBlockingCallback.Blocker blocker = this._writeblock.acquire()) {
            new InputStreamWritingCB(in, blocker).iterate();
            blocker.block();
        }
    }
    
    public void sendContent(final ReadableByteChannel in) throws IOException {
        try (final SharedBlockingCallback.Blocker blocker = this._writeblock.acquire()) {
            new ReadableByteChannelWritingCB(in, blocker).iterate();
            blocker.block();
        }
    }
    
    public void sendContent(final HttpContent content) throws IOException {
        try (final SharedBlockingCallback.Blocker blocker = this._writeblock.acquire()) {
            this.sendContent(content, blocker);
            blocker.block();
        }
    }
    
    public void sendContent(final ByteBuffer content, final Callback callback) {
        this.write(content, true, new Callback() {
            @Override
            public void succeeded() {
                HttpOutput.this.closed();
                callback.succeeded();
            }
            
            @Override
            public void failed(final Throwable x) {
                callback.failed(x);
            }
        });
    }
    
    public void sendContent(final InputStream in, final Callback callback) {
        new InputStreamWritingCB(in, callback).iterate();
    }
    
    public void sendContent(final ReadableByteChannel in, final Callback callback) {
        new ReadableByteChannelWritingCB(in, callback).iterate();
    }
    
    public void sendContent(final HttpContent httpContent, final Callback callback) {
        if (BufferUtil.hasContent(this._aggregate)) {
            callback.failed(new IOException("cannot sendContent() after write()"));
            return;
        }
        if (this._channel.isCommitted()) {
            callback.failed(new IOException("committed"));
            return;
        }
        while (true) {
            switch (this._state.get()) {
                case OPEN: {
                    if (!this._state.compareAndSet(OutputState.OPEN, OutputState.PENDING)) {
                        continue;
                    }
                    ByteBuffer buffer = this._channel.useDirectBuffers() ? httpContent.getDirectBuffer() : null;
                    if (buffer == null) {
                        buffer = httpContent.getIndirectBuffer();
                    }
                    if (buffer != null) {
                        if (HttpOutput.LOG.isDebugEnabled()) {
                            HttpOutput.LOG.debug("sendContent({}=={},{},direct={})", httpContent, BufferUtil.toDetailString(buffer), callback, this._channel.useDirectBuffers());
                        }
                        this.sendContent(buffer, callback);
                        return;
                    }
                    try {
                        final ReadableByteChannel rbc = httpContent.getReadableByteChannel();
                        if (rbc != null) {
                            if (HttpOutput.LOG.isDebugEnabled()) {
                                HttpOutput.LOG.debug("sendContent({}=={},{},direct={})", httpContent, rbc, callback, this._channel.useDirectBuffers());
                            }
                            this.sendContent(rbc, callback);
                            return;
                        }
                        final InputStream in = httpContent.getInputStream();
                        if (in != null) {
                            if (HttpOutput.LOG.isDebugEnabled()) {
                                HttpOutput.LOG.debug("sendContent({}=={},{},direct={})", httpContent, in, callback, this._channel.useDirectBuffers());
                            }
                            this.sendContent(in, callback);
                            return;
                        }
                    }
                    catch (Throwable th) {
                        callback.failed(th);
                        return;
                    }
                    callback.failed(new IllegalArgumentException("unknown content for " + httpContent));
                }
                case ERROR: {
                    callback.failed(new EofException(this._onError));
                }
                case CLOSED: {
                    callback.failed(new EofException("Closed"));
                }
                default: {
                    throw new IllegalStateException();
                }
            }
        }
    }
    
    public int getBufferSize() {
        return this._bufferSize;
    }
    
    public void setBufferSize(final int size) {
        this._bufferSize = size;
        this._commitSize = size;
    }
    
    public void resetBuffer() {
        if (BufferUtil.hasContent(this._aggregate)) {
            BufferUtil.clear(this._aggregate);
        }
    }
    
    @Override
    public void setWriteListener(final WriteListener writeListener) {
        if (!this._channel.getState().isAsync()) {
            throw new IllegalStateException("!ASYNC");
        }
        if (this._state.compareAndSet(OutputState.OPEN, OutputState.READY)) {
            this._writeListener = writeListener;
            this._channel.getState().onWritePossible();
            return;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public boolean isReady() {
        while (true) {
            switch (this._state.get()) {
                case OPEN: {
                    return true;
                }
                case ASYNC: {
                    if (!this._state.compareAndSet(OutputState.ASYNC, OutputState.READY)) {
                        continue;
                    }
                    return true;
                }
                case READY: {
                    return true;
                }
                case PENDING: {
                    if (!this._state.compareAndSet(OutputState.PENDING, OutputState.UNREADY)) {
                        continue;
                    }
                    return false;
                }
                case UNREADY: {
                    return false;
                }
                case ERROR: {
                    return true;
                }
                case CLOSED: {
                    return true;
                }
                default: {
                    continue;
                }
            }
        }
    }
    
    @Override
    public void run() {
    Label_0228:
        while (true) {
            final OutputState state = this._state.get();
            if (this._onError != null) {
                switch (state) {
                    case CLOSED:
                    case ERROR: {
                        this._onError = null;
                        break Label_0228;
                    }
                    default: {
                        if (this._state.compareAndSet(state, OutputState.ERROR)) {
                            final Throwable th = this._onError;
                            this._onError = null;
                            if (HttpOutput.LOG.isDebugEnabled()) {
                                HttpOutput.LOG.debug("onError", th);
                            }
                            this._writeListener.onError(th);
                            this.close();
                            break Label_0228;
                        }
                        continue;
                    }
                }
            }
            else {
                switch (this._state.get()) {
                    case CLOSED:
                    case READY: {
                        try {
                            this._writeListener.onWritePossible();
                            break Label_0228;
                        }
                        catch (Throwable e) {
                            this._onError = e;
                            continue;
                        }
                        break;
                    }
                }
                this._onError = new IllegalStateException("state=" + this._state.get());
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x{%s}", this.getClass().getSimpleName(), this.hashCode(), this._state.get());
    }
    
    static {
        HttpOutput.LOG = Log.getLogger(HttpOutput.class);
    }
    
    enum OutputState
    {
        OPEN, 
        ASYNC, 
        READY, 
        PENDING, 
        UNREADY, 
        ERROR, 
        CLOSED;
    }
    
    private abstract class AsyncICB extends IteratingCallback
    {
        @Override
        protected void onCompleteSuccess() {
        Label_0127:
            while (true) {
                final OutputState last = HttpOutput.this._state.get();
                switch (last) {
                    case PENDING: {
                        if (!HttpOutput.this._state.compareAndSet(OutputState.PENDING, OutputState.ASYNC)) {
                            continue;
                        }
                        break Label_0127;
                    }
                    case UNREADY: {
                        if (!HttpOutput.this._state.compareAndSet(OutputState.UNREADY, OutputState.READY)) {
                            continue;
                        }
                        HttpOutput.this._channel.getState().onWritePossible();
                        break Label_0127;
                    }
                    case CLOSED: {
                        break Label_0127;
                    }
                    default: {
                        throw new IllegalStateException();
                    }
                }
            }
        }
        
        public void onCompleteFailure(final Throwable e) {
            HttpOutput.this._onError = ((e == null) ? new IOException() : e);
            HttpOutput.this._channel.getState().onWritePossible();
        }
    }
    
    private class AsyncFlush extends AsyncICB
    {
        protected volatile boolean _flushed;
        
        public AsyncFlush() {
        }
        
        @Override
        protected Action process() {
            if (BufferUtil.hasContent(HttpOutput.this._aggregate)) {
                this._flushed = true;
                HttpOutput.this.write(HttpOutput.this._aggregate, false, this);
                return Action.SCHEDULED;
            }
            if (!this._flushed) {
                this._flushed = true;
                HttpOutput.this.write(BufferUtil.EMPTY_BUFFER, false, this);
                return Action.SCHEDULED;
            }
            return Action.SUCCEEDED;
        }
    }
    
    private class AsyncWrite extends AsyncICB
    {
        private final ByteBuffer _buffer;
        private final ByteBuffer _slice;
        private final boolean _complete;
        private final int _len;
        protected volatile boolean _completed;
        
        public AsyncWrite(final byte[] b, final int off, final int len, final boolean complete) {
            this._buffer = ByteBuffer.wrap(b, off, len);
            this._len = len;
            this._slice = ((this._len < HttpOutput.this.getBufferSize()) ? null : this._buffer.duplicate());
            this._complete = complete;
        }
        
        public AsyncWrite(final ByteBuffer buffer, final boolean complete) {
            this._buffer = buffer;
            this._len = buffer.remaining();
            this._slice = ((this._buffer.isDirect() || this._len < HttpOutput.this.getBufferSize()) ? null : this._buffer.duplicate());
            this._complete = complete;
        }
        
        @Override
        protected Action process() {
            if (BufferUtil.hasContent(HttpOutput.this._aggregate)) {
                this._completed = (this._len == 0);
                HttpOutput.this.write(HttpOutput.this._aggregate, this._complete && this._completed, this);
                return Action.SCHEDULED;
            }
            if (!this._complete && this._len < BufferUtil.space(HttpOutput.this._aggregate) && this._len < HttpOutput.this._commitSize) {
                final int position = BufferUtil.flipToFill(HttpOutput.this._aggregate);
                BufferUtil.put(this._buffer, HttpOutput.this._aggregate);
                BufferUtil.flipToFlush(HttpOutput.this._aggregate, position);
                return Action.SUCCEEDED;
            }
            if (this._buffer.hasRemaining()) {
                if (this._slice == null) {
                    this._completed = true;
                    HttpOutput.this.write(this._buffer, this._complete, this);
                    return Action.SCHEDULED;
                }
                final int p = this._buffer.position();
                final int l = Math.min(HttpOutput.this.getBufferSize(), this._buffer.remaining());
                final int pl = p + l;
                this._slice.limit(pl);
                this._buffer.position(pl);
                this._slice.position(p);
                this._completed = !this._buffer.hasRemaining();
                HttpOutput.this.write(this._slice, this._complete && this._completed, this);
                return Action.SCHEDULED;
            }
            else {
                if (this._complete && !this._completed) {
                    this._completed = true;
                    HttpOutput.this.write(BufferUtil.EMPTY_BUFFER, this._complete, this);
                    return Action.SCHEDULED;
                }
                return Action.SUCCEEDED;
            }
        }
        
        @Override
        protected void onCompleteSuccess() {
            super.onCompleteSuccess();
            if (this._complete) {
                HttpOutput.this.closed();
            }
        }
    }
    
    private class InputStreamWritingCB extends IteratingNestedCallback
    {
        private final InputStream _in;
        private final ByteBuffer _buffer;
        private boolean _eof;
        
        public InputStreamWritingCB(final InputStream in, final Callback callback) {
            super(callback);
            this._in = in;
            this._buffer = HttpOutput.this._channel.getByteBufferPool().acquire(HttpOutput.this.getBufferSize(), false);
        }
        
        @Override
        protected Action process() throws Exception {
            if (this._eof) {
                this._in.close();
                HttpOutput.this.closed();
                HttpOutput.this._channel.getByteBufferPool().release(this._buffer);
                return Action.SUCCEEDED;
            }
            int len = 0;
            while (len < this._buffer.capacity() && !this._eof) {
                final int r = this._in.read(this._buffer.array(), this._buffer.arrayOffset() + len, this._buffer.capacity() - len);
                if (r < 0) {
                    this._eof = true;
                }
                else {
                    len += r;
                }
            }
            this._buffer.position(0);
            this._buffer.limit(len);
            HttpOutput.this.write(this._buffer, this._eof, this);
            return Action.SCHEDULED;
        }
        
        public void onCompleteFailure(final Throwable x) {
            super.onCompleteFailure(x);
            HttpOutput.this._channel.getByteBufferPool().release(this._buffer);
            try {
                this._in.close();
            }
            catch (IOException e) {
                HttpOutput.LOG.ignore(e);
            }
        }
    }
    
    private class ReadableByteChannelWritingCB extends IteratingNestedCallback
    {
        private final ReadableByteChannel _in;
        private final ByteBuffer _buffer;
        private boolean _eof;
        
        public ReadableByteChannelWritingCB(final ReadableByteChannel in, final Callback callback) {
            super(callback);
            this._in = in;
            this._buffer = HttpOutput.this._channel.getByteBufferPool().acquire(HttpOutput.this.getBufferSize(), HttpOutput.this._channel.useDirectBuffers());
        }
        
        @Override
        protected Action process() throws Exception {
            if (this._eof) {
                this._in.close();
                HttpOutput.this.closed();
                HttpOutput.this._channel.getByteBufferPool().release(this._buffer);
                return Action.SUCCEEDED;
            }
            this._buffer.clear();
            while (this._buffer.hasRemaining() && !this._eof) {
                this._eof = (this._in.read(this._buffer) < 0);
            }
            this._buffer.flip();
            HttpOutput.this.write(this._buffer, this._eof, this);
            return Action.SCHEDULED;
        }
        
        public void onCompleteFailure(final Throwable x) {
            super.onCompleteFailure(x);
            HttpOutput.this._channel.getByteBufferPool().release(this._buffer);
            try {
                this._in.close();
            }
            catch (IOException e) {
                HttpOutput.LOG.ignore(e);
            }
        }
    }
}
