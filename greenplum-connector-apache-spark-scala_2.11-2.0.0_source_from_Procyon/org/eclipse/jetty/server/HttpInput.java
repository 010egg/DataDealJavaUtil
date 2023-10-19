// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.util.log.Log;
import java.util.Objects;
import java.io.IOException;
import org.eclipse.jetty.io.RuntimeIOException;
import javax.servlet.ReadListener;
import org.eclipse.jetty.util.log.Logger;
import javax.servlet.ServletInputStream;

public abstract class HttpInput<T> extends ServletInputStream implements Runnable
{
    private static final Logger LOG;
    private final byte[] _oneByteBuffer;
    private final Object _lock;
    private HttpChannelState _channelState;
    private ReadListener _listener;
    private Throwable _onError;
    private boolean _notReady;
    private State _contentState;
    private State _eofState;
    private long _contentRead;
    protected static final State STREAM;
    protected static final State ASYNC;
    protected static final State EARLY_EOF;
    protected static final State EOF;
    
    protected HttpInput() {
        this(null);
    }
    
    protected HttpInput(final Object lock) {
        this._oneByteBuffer = new byte[1];
        this._contentState = HttpInput.STREAM;
        this._lock = ((lock == null) ? this : lock);
    }
    
    public void init(final HttpChannelState state) {
        synchronized (this.lock()) {
            this._channelState = state;
        }
    }
    
    public final Object lock() {
        return this._lock;
    }
    
    public void recycle() {
        synchronized (this.lock()) {
            this._listener = null;
            this._onError = null;
            this._notReady = false;
            this._contentState = HttpInput.STREAM;
            this._eofState = null;
            this._contentRead = 0L;
        }
    }
    
    @Override
    public int available() {
        try {
            synchronized (this.lock()) {
                final T item = this.getNextContent();
                return (item == null) ? 0 : this.remaining(item);
            }
        }
        catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.read(this._oneByteBuffer, 0, 1);
        return (read < 0) ? -1 : (this._oneByteBuffer[0] & 0xFF);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        synchronized (this.lock()) {
            T item = this.getNextContent();
            if (item == null) {
                this._contentState.waitForContent(this);
                item = this.getNextContent();
                if (item == null) {
                    return this._contentState.noContent();
                }
            }
            final int l = this.get(item, b, off, len);
            this._contentRead += l;
            return l;
        }
    }
    
    protected T getNextContent() throws IOException {
        final T content = this.nextContent();
        if (content == null) {
            synchronized (this.lock()) {
                if (this._eofState != null) {
                    if (HttpInput.LOG.isDebugEnabled()) {
                        HttpInput.LOG.debug("{} eof {}", this, this._eofState);
                    }
                    this._contentState = this._eofState;
                }
            }
        }
        return content;
    }
    
    protected abstract T nextContent() throws IOException;
    
    protected abstract int remaining(final T p0);
    
    protected abstract int get(final T p0, final byte[] p1, final int p2, final int p3);
    
    protected abstract void consume(final T p0, final int p1);
    
    protected abstract void blockForContent() throws IOException;
    
    public abstract void content(final T p0);
    
    protected boolean onAsyncRead() {
        synchronized (this.lock()) {
            if (this._listener == null) {
                return false;
            }
        }
        this._channelState.onReadPossible();
        return true;
    }
    
    public long getContentRead() {
        synchronized (this.lock()) {
            return this._contentRead;
        }
    }
    
    public void earlyEOF() {
        synchronized (this.lock()) {
            if (!this.isEOF()) {
                if (HttpInput.LOG.isDebugEnabled()) {
                    HttpInput.LOG.debug("{} early EOF", this);
                }
                this._eofState = HttpInput.EARLY_EOF;
                if (this._listener == null) {
                    return;
                }
            }
        }
        this._channelState.onReadPossible();
    }
    
    public boolean isEarlyEOF() {
        synchronized (this.lock()) {
            return this._contentState == HttpInput.EARLY_EOF;
        }
    }
    
    public void messageComplete() {
        synchronized (this.lock()) {
            if (!this.isEOF()) {
                if (HttpInput.LOG.isDebugEnabled()) {
                    HttpInput.LOG.debug("{} EOF", this);
                }
                this._eofState = HttpInput.EOF;
                if (this._listener == null) {
                    return;
                }
            }
        }
        this._channelState.onReadPossible();
    }
    
    public boolean consumeAll() {
        synchronized (this.lock()) {
            if (this._onError != null) {
                return false;
            }
            try {
                while (!this.isFinished()) {
                    final T item = this.getNextContent();
                    if (item == null) {
                        this._contentState.waitForContent(this);
                    }
                    else {
                        this.consume(item, this.remaining(item));
                    }
                }
                return true;
            }
            catch (IOException e) {
                HttpInput.LOG.debug(e);
                return false;
            }
        }
    }
    
    public boolean isAsync() {
        synchronized (this.lock()) {
            return this._contentState == HttpInput.ASYNC;
        }
    }
    
    public boolean isEOF() {
        synchronized (this.lock()) {
            return this._eofState != null && this._eofState.isEOF();
        }
    }
    
    @Override
    public boolean isFinished() {
        synchronized (this.lock()) {
            return this._contentState.isEOF();
        }
    }
    
    @Override
    public boolean isReady() {
        final boolean finished;
        synchronized (this.lock()) {
            if (this._contentState.isEOF()) {
                return true;
            }
            if (this._listener == null) {
                return true;
            }
            if (this.available() > 0) {
                return true;
            }
            if (this._notReady) {
                return false;
            }
            this._notReady = true;
            finished = this.isFinished();
        }
        if (finished) {
            this._channelState.onReadPossible();
        }
        else {
            this.unready();
        }
        return false;
    }
    
    protected void unready() {
    }
    
    @Override
    public void setReadListener(ReadListener readListener) {
        try {
            readListener = Objects.requireNonNull(readListener);
            final boolean content;
            synchronized (this.lock()) {
                if (this._contentState != HttpInput.STREAM) {
                    throw new IllegalStateException("state=" + this._contentState);
                }
                this._contentState = HttpInput.ASYNC;
                this._listener = readListener;
                this._notReady = true;
                content = (this.getNextContent() != null || this.isEOF());
            }
            if (content) {
                this._channelState.onReadPossible();
            }
            else {
                this.unready();
            }
        }
        catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }
    
    public void failed(final Throwable x) {
        synchronized (this.lock()) {
            if (this._onError != null) {
                HttpInput.LOG.warn(x);
            }
            else {
                this._onError = x;
            }
        }
    }
    
    @Override
    public void run() {
        boolean available = false;
        final Throwable error;
        final ReadListener listener;
        final boolean eof;
        synchronized (this.lock()) {
            if (!this._notReady || this._listener == null) {
                return;
            }
            error = this._onError;
            listener = this._listener;
            try {
                final T item = this.getNextContent();
                available = (item != null && this.remaining(item) > 0);
            }
            catch (Exception e) {
                this.failed(e);
            }
            eof = (!available && this.isFinished());
            this._notReady = (!available && !eof);
        }
        try {
            if (error != null) {
                listener.onError(error);
            }
            else if (available) {
                listener.onDataAvailable();
            }
            else if (eof) {
                listener.onAllDataRead();
            }
            else {
                this.unready();
            }
        }
        catch (Throwable e2) {
            HttpInput.LOG.warn(e2.toString(), new Object[0]);
            HttpInput.LOG.debug(e2);
            listener.onError(e2);
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x[r=%d,s=%s,e=%s,f=%s]", this.getClass().getSimpleName(), this.hashCode(), this._contentRead, this._contentState, this._eofState, this._onError);
    }
    
    static {
        LOG = Log.getLogger(HttpInput.class);
        STREAM = new State() {
            @Override
            public void waitForContent(final HttpInput<?> input) throws IOException {
                input.blockForContent();
            }
            
            @Override
            public String toString() {
                return "STREAM";
            }
        };
        ASYNC = new State() {
            @Override
            public int noContent() throws IOException {
                return 0;
            }
            
            @Override
            public String toString() {
                return "ASYNC";
            }
        };
        EARLY_EOF = new State() {
            @Override
            public int noContent() throws IOException {
                throw new EofException("Early EOF");
            }
            
            @Override
            public boolean isEOF() {
                return true;
            }
            
            @Override
            public String toString() {
                return "EARLY_EOF";
            }
        };
        EOF = new State() {
            @Override
            public boolean isEOF() {
                return true;
            }
            
            @Override
            public String toString() {
                return "EOF";
            }
        };
    }
    
    protected abstract static class State
    {
        public void waitForContent(final HttpInput<?> in) throws IOException {
        }
        
        public int noContent() throws IOException {
            return -1;
        }
        
        public boolean isEOF() {
            return false;
        }
    }
}
