// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io.ssl;

import org.eclipse.jetty.io.EofException;
import java.nio.channels.ClosedChannelException;
import java.util.Arrays;
import org.eclipse.jetty.io.Connection;
import java.io.IOException;
import javax.net.ssl.SSLEngineResult;
import org.eclipse.jetty.io.WriteFlusher;
import org.eclipse.jetty.io.FillInterest;
import org.eclipse.jetty.util.thread.Scheduler;
import org.eclipse.jetty.io.AbstractEndPoint;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.Callback;
import javax.net.ssl.SSLException;
import org.eclipse.jetty.io.RuntimeIOException;
import org.eclipse.jetty.io.EndPoint;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLEngine;
import org.eclipse.jetty.io.ByteBufferPool;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.io.AbstractConnection;

public class SslConnection extends AbstractConnection
{
    private static final Logger LOG;
    private static final boolean DEBUG;
    private static final ByteBuffer __FILL_CALLED_FLUSH;
    private static final ByteBuffer __FLUSH_CALLED_FILL;
    private final ByteBufferPool _bufferPool;
    private final SSLEngine _sslEngine;
    private final DecryptedEndPoint _decryptedEndPoint;
    private ByteBuffer _decryptedInput;
    private ByteBuffer _encryptedInput;
    private ByteBuffer _encryptedOutput;
    private final boolean _encryptedDirectBuffers = false;
    private final boolean _decryptedDirectBuffers = false;
    private final Runnable _runCompletWrite;
    private boolean _renegotiationAllowed;
    
    public SslConnection(final ByteBufferPool byteBufferPool, final Executor executor, final EndPoint endPoint, final SSLEngine sslEngine) {
        super(endPoint, executor, false);
        this._runCompletWrite = new Runnable() {
            @Override
            public void run() {
                SslConnection.this._decryptedEndPoint.getWriteFlusher().completeWrite();
            }
        };
        this._bufferPool = byteBufferPool;
        this._sslEngine = sslEngine;
        this._decryptedEndPoint = this.newDecryptedEndPoint();
    }
    
    protected DecryptedEndPoint newDecryptedEndPoint() {
        return new DecryptedEndPoint();
    }
    
    public SSLEngine getSSLEngine() {
        return this._sslEngine;
    }
    
    public DecryptedEndPoint getDecryptedEndPoint() {
        return this._decryptedEndPoint;
    }
    
    public boolean isRenegotiationAllowed() {
        return this._renegotiationAllowed;
    }
    
    public void setRenegotiationAllowed(final boolean renegotiationAllowed) {
        this._renegotiationAllowed = renegotiationAllowed;
    }
    
    @Override
    public void onOpen() {
        try {
            this._sslEngine.beginHandshake();
            super.onOpen();
            this.getDecryptedEndPoint().getConnection().onOpen();
        }
        catch (SSLException x) {
            this.getEndPoint().close();
            throw new RuntimeIOException(x);
        }
    }
    
    @Override
    public void onClose() {
        this._decryptedEndPoint.getConnection().onClose();
        super.onClose();
    }
    
    @Override
    public void close() {
        this.getDecryptedEndPoint().getConnection().close();
    }
    
    @Override
    public void onFillable() {
        if (SslConnection.DEBUG) {
            SslConnection.LOG.debug("onFillable enter {}", this._decryptedEndPoint);
        }
        if (this._decryptedEndPoint.isInputShutdown()) {
            this._decryptedEndPoint.close();
        }
        this._decryptedEndPoint.getFillInterest().fillable();
        synchronized (this._decryptedEndPoint) {
            if (this._decryptedEndPoint._flushRequiresFillToProgress) {
                this._decryptedEndPoint._flushRequiresFillToProgress = false;
                this.getExecutor().execute(this._runCompletWrite);
            }
        }
        if (SslConnection.DEBUG) {
            SslConnection.LOG.debug("onFillable exit {}", this._decryptedEndPoint);
        }
    }
    
    public void onFillInterestedFailed(final Throwable cause) {
        this._decryptedEndPoint.getFillInterest().onFail(cause);
        boolean failFlusher = false;
        synchronized (this._decryptedEndPoint) {
            if (this._decryptedEndPoint._flushRequiresFillToProgress) {
                this._decryptedEndPoint._flushRequiresFillToProgress = false;
                failFlusher = true;
            }
        }
        if (failFlusher) {
            this._decryptedEndPoint.getWriteFlusher().onFail(cause);
        }
    }
    
    @Override
    public String toString() {
        ByteBuffer b = this._encryptedInput;
        final int ei = (b == null) ? -1 : b.remaining();
        b = this._encryptedOutput;
        final int eo = (b == null) ? -1 : b.remaining();
        b = this._decryptedInput;
        final int di = (b == null) ? -1 : b.remaining();
        return String.format("SslConnection@%x{%s,eio=%d/%d,di=%d} -> %s", this.hashCode(), this._sslEngine.getHandshakeStatus(), ei, eo, di, this._decryptedEndPoint.getConnection());
    }
    
    static {
        LOG = Log.getLogger(SslConnection.class);
        DEBUG = SslConnection.LOG.isDebugEnabled();
        __FILL_CALLED_FLUSH = BufferUtil.allocate(0);
        __FLUSH_CALLED_FILL = BufferUtil.allocate(0);
    }
    
    public class DecryptedEndPoint extends AbstractEndPoint
    {
        private boolean _fillRequiresFlushToProgress;
        private boolean _flushRequiresFillToProgress;
        private boolean _cannotAcceptMoreAppDataToFlush;
        private boolean _handshaken;
        private boolean _underFlown;
        private final Callback _writeCallback;
        
        public DecryptedEndPoint() {
            super(null, SslConnection.this.getEndPoint().getLocalAddress(), SslConnection.this.getEndPoint().getRemoteAddress());
            this._writeCallback = new Callback() {
                @Override
                public void succeeded() {
                    boolean fillable = false;
                    synchronized (DecryptedEndPoint.this) {
                        if (SslConnection.DEBUG) {
                            SslConnection.LOG.debug("write.complete {}", SslConnection.this.getEndPoint());
                        }
                        DecryptedEndPoint.this.releaseEncryptedOutputBuffer();
                        DecryptedEndPoint.this._cannotAcceptMoreAppDataToFlush = false;
                        if (DecryptedEndPoint.this._fillRequiresFlushToProgress) {
                            DecryptedEndPoint.this._fillRequiresFlushToProgress = false;
                            fillable = true;
                        }
                    }
                    if (fillable) {
                        DecryptedEndPoint.this.getFillInterest().fillable();
                    }
                    AbstractConnection.this.getExecutor().execute(SslConnection.this._runCompletWrite);
                }
                
                @Override
                public void failed(final Throwable x) {
                    boolean fail_filler = false;
                    synchronized (DecryptedEndPoint.this) {
                        if (SslConnection.DEBUG) {
                            SslConnection.LOG.debug("{} write.failed", SslConnection.this, x);
                        }
                        BufferUtil.clear(SslConnection.this._encryptedOutput);
                        DecryptedEndPoint.this.releaseEncryptedOutputBuffer();
                        DecryptedEndPoint.this._cannotAcceptMoreAppDataToFlush = false;
                        if (DecryptedEndPoint.this._fillRequiresFlushToProgress) {
                            DecryptedEndPoint.this._fillRequiresFlushToProgress = false;
                            fail_filler = true;
                        }
                    }
                    final boolean filler_failed = fail_filler;
                    AbstractConnection.this.failedCallback(new Callback() {
                        @Override
                        public void succeeded() {
                        }
                        
                        @Override
                        public void failed(final Throwable x) {
                            if (filler_failed) {
                                DecryptedEndPoint.this.getFillInterest().onFail(x);
                            }
                            DecryptedEndPoint.this.getWriteFlusher().onFail(x);
                        }
                    }, x);
                }
            };
            this.setIdleTimeout(SslConnection.this.getEndPoint().getIdleTimeout());
        }
        
        @Override
        protected FillInterest getFillInterest() {
            return super.getFillInterest();
        }
        
        @Override
        public void setIdleTimeout(final long idleTimeout) {
            super.setIdleTimeout(idleTimeout);
            SslConnection.this.getEndPoint().setIdleTimeout(idleTimeout);
        }
        
        @Override
        protected WriteFlusher getWriteFlusher() {
            return super.getWriteFlusher();
        }
        
        @Override
        protected void onIncompleteFlush() {
            boolean try_again = false;
            synchronized (this) {
                if (SslConnection.DEBUG) {
                    SslConnection.LOG.debug("onIncompleteFlush {}", SslConnection.this.getEndPoint());
                }
                if (BufferUtil.hasContent(SslConnection.this._encryptedOutput)) {
                    this._cannotAcceptMoreAppDataToFlush = true;
                    SslConnection.this.getEndPoint().write(this._writeCallback, SslConnection.this._encryptedOutput);
                }
                else if (SslConnection.this._sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                    this._flushRequiresFillToProgress = true;
                    SslConnection.this.fillInterested();
                }
                else {
                    try_again = true;
                }
            }
            if (try_again) {
                if (this.isOutputShutdown()) {
                    this.getWriteFlusher().onClose();
                }
                else {
                    AbstractConnection.this.getExecutor().execute(SslConnection.this._runCompletWrite);
                }
            }
        }
        
        @Override
        protected boolean needsFill() throws IOException {
            synchronized (this) {
                if (BufferUtil.hasContent(SslConnection.this._decryptedInput)) {
                    return true;
                }
                if (BufferUtil.isEmpty(SslConnection.this._encryptedInput) || this._underFlown) {
                    if (this._fillRequiresFlushToProgress) {
                        if (!BufferUtil.hasContent(SslConnection.this._encryptedOutput)) {
                            this._fillRequiresFlushToProgress = false;
                            return true;
                        }
                        this._cannotAcceptMoreAppDataToFlush = true;
                        SslConnection.this.getEndPoint().write(this._writeCallback, SslConnection.this._encryptedOutput);
                    }
                    else {
                        SslConnection.this.fillInterested();
                    }
                    return false;
                }
                return true;
            }
        }
        
        @Override
        public void setConnection(final Connection connection) {
            if (connection instanceof AbstractConnection) {
                final AbstractConnection a = (AbstractConnection)connection;
                if (a.getInputBufferSize() < SslConnection.this._sslEngine.getSession().getApplicationBufferSize()) {
                    a.setInputBufferSize(SslConnection.this._sslEngine.getSession().getApplicationBufferSize());
                }
            }
            super.setConnection(connection);
        }
        
        public SslConnection getSslConnection() {
            return SslConnection.this;
        }
        
        @Override
        public synchronized int fill(final ByteBuffer buffer) throws IOException {
            if (SslConnection.DEBUG) {
                SslConnection.LOG.debug("{} fill enter", SslConnection.this);
            }
            try {
                if (BufferUtil.hasContent(SslConnection.this._decryptedInput)) {
                    return BufferUtil.append(buffer, SslConnection.this._decryptedInput);
                }
                if (SslConnection.this._encryptedInput == null) {
                    SslConnection.this._encryptedInput = SslConnection.this._bufferPool.acquire(SslConnection.this._sslEngine.getSession().getPacketBufferSize(), false);
                }
                else {
                    BufferUtil.compact(SslConnection.this._encryptedInput);
                }
                ByteBuffer app_in;
                if (BufferUtil.space(buffer) > SslConnection.this._sslEngine.getSession().getApplicationBufferSize()) {
                    app_in = buffer;
                }
                else if (SslConnection.this._decryptedInput == null) {
                    app_in = (SslConnection.this._decryptedInput = SslConnection.this._bufferPool.acquire(SslConnection.this._sslEngine.getSession().getApplicationBufferSize(), false));
                }
                else {
                    app_in = SslConnection.this._decryptedInput;
                }
            Label_2466:
                while (true) {
                    final int net_filled = SslConnection.this.getEndPoint().fill(SslConnection.this._encryptedInput);
                    if (SslConnection.DEBUG) {
                        SslConnection.LOG.debug("{} filled {} encrypted bytes", SslConnection.this, net_filled);
                    }
                    while (true) {
                        final int pos = BufferUtil.flipToFill(app_in);
                        SSLEngineResult unwrapResult;
                        try {
                            unwrapResult = SslConnection.this._sslEngine.unwrap(SslConnection.this._encryptedInput, app_in);
                        }
                        finally {
                            BufferUtil.flipToFlush(app_in, pos);
                        }
                        if (SslConnection.DEBUG) {
                            SslConnection.LOG.debug("{} unwrap {}", SslConnection.this, unwrapResult);
                        }
                        final SSLEngineResult.HandshakeStatus handshakeStatus = SslConnection.this._sslEngine.getHandshakeStatus();
                        final SSLEngineResult.HandshakeStatus unwrapHandshakeStatus = unwrapResult.getHandshakeStatus();
                        final SSLEngineResult.Status unwrapResultStatus = unwrapResult.getStatus();
                        this._underFlown = (unwrapResultStatus == SSLEngineResult.Status.BUFFER_UNDERFLOW || (unwrapResultStatus == SSLEngineResult.Status.OK && unwrapResult.bytesConsumed() == 0 && unwrapResult.bytesProduced() == 0));
                        if (this._underFlown) {
                            if (net_filled < 0) {
                                this.closeInbound();
                            }
                            if (net_filled <= 0) {
                                return net_filled;
                            }
                        }
                        switch (unwrapResultStatus) {
                            case CLOSED: {
                                switch (handshakeStatus) {
                                    case NOT_HANDSHAKING: {
                                        return -1;
                                    }
                                    case NEED_TASK: {
                                        SslConnection.this._sslEngine.getDelegatedTask().run();
                                        continue;
                                    }
                                    case NEED_WRAP: {
                                        return -1;
                                    }
                                    case NEED_UNWRAP: {
                                        return -1;
                                    }
                                    default: {
                                        throw new IllegalStateException();
                                    }
                                }
                                break;
                            }
                            case BUFFER_UNDERFLOW:
                            case OK: {
                                if (unwrapHandshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED && !this._handshaken) {
                                    this._handshaken = true;
                                    if (SslConnection.DEBUG) {
                                        SslConnection.LOG.debug("{} {} handshake completed", SslConnection.this, SslConnection.this._sslEngine.getUseClientMode() ? "client-side" : "resumed session server-side");
                                    }
                                }
                                if (this._handshaken && handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING && !SslConnection.this.isRenegotiationAllowed()) {
                                    if (SslConnection.DEBUG) {
                                        SslConnection.LOG.debug("{} renegotiation denied", SslConnection.this);
                                    }
                                    this.closeInbound();
                                    return -1;
                                }
                                if (unwrapResult.bytesProduced() > 0) {
                                    if (app_in == buffer) {
                                        return unwrapResult.bytesProduced();
                                    }
                                    return BufferUtil.append(buffer, SslConnection.this._decryptedInput);
                                }
                                else {
                                    switch (handshakeStatus) {
                                        case NOT_HANDSHAKING: {
                                            if (this._underFlown) {
                                                continue Label_2466;
                                            }
                                            continue;
                                        }
                                        case NEED_TASK: {
                                            SslConnection.this._sslEngine.getDelegatedTask().run();
                                            continue;
                                        }
                                        case NEED_WRAP: {
                                            if (buffer == SslConnection.__FLUSH_CALLED_FILL) {
                                                return 0;
                                            }
                                            this._fillRequiresFlushToProgress = true;
                                            this.flush(SslConnection.__FILL_CALLED_FLUSH);
                                            if (BufferUtil.isEmpty(SslConnection.this._encryptedOutput)) {
                                                this._fillRequiresFlushToProgress = false;
                                                continue;
                                            }
                                            return 0;
                                        }
                                        case NEED_UNWRAP: {
                                            if (this._underFlown) {
                                                continue Label_2466;
                                            }
                                            continue;
                                        }
                                        default: {
                                            throw new IllegalStateException();
                                        }
                                    }
                                }
                                break;
                            }
                            default: {
                                throw new IllegalStateException();
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                this.close();
                throw e;
            }
            finally {
                if (this._flushRequiresFillToProgress) {
                    this._flushRequiresFillToProgress = false;
                    AbstractConnection.this.getExecutor().execute(SslConnection.this._runCompletWrite);
                }
                if (SslConnection.this._encryptedInput != null && !SslConnection.this._encryptedInput.hasRemaining()) {
                    SslConnection.this._bufferPool.release(SslConnection.this._encryptedInput);
                    SslConnection.this._encryptedInput = null;
                }
                if (SslConnection.this._decryptedInput != null && !SslConnection.this._decryptedInput.hasRemaining()) {
                    SslConnection.this._bufferPool.release(SslConnection.this._decryptedInput);
                    SslConnection.this._decryptedInput = null;
                }
                if (SslConnection.DEBUG) {
                    SslConnection.LOG.debug("{} fill exit", SslConnection.this);
                }
            }
        }
        
        private void closeInbound() {
            try {
                SslConnection.this._sslEngine.closeInbound();
            }
            catch (SSLException x) {
                SslConnection.LOG.ignore(x);
            }
        }
        
        @Override
        public synchronized boolean flush(final ByteBuffer... appOuts) throws IOException {
            if (SslConnection.DEBUG) {
                SslConnection.LOG.debug("{} flush enter {}", SslConnection.this, Arrays.toString(appOuts));
            }
            int consumed = 0;
            try {
                if (!this._cannotAcceptMoreAppDataToFlush) {
                    if (SslConnection.this._encryptedOutput == null) {
                        SslConnection.this._encryptedOutput = SslConnection.this._bufferPool.acquire(SslConnection.this._sslEngine.getSession().getPacketBufferSize(), false);
                    }
                    boolean allConsumed = false;
                Label_1038:
                    while (true) {
                        BufferUtil.compact(SslConnection.this._encryptedOutput);
                        final int pos = BufferUtil.flipToFill(SslConnection.this._encryptedOutput);
                        SSLEngineResult wrapResult;
                        try {
                            wrapResult = SslConnection.this._sslEngine.wrap(appOuts, SslConnection.this._encryptedOutput);
                        }
                        finally {
                            BufferUtil.flipToFlush(SslConnection.this._encryptedOutput, pos);
                        }
                        if (SslConnection.DEBUG) {
                            SslConnection.LOG.debug("{} wrap {}", SslConnection.this, wrapResult);
                        }
                        if (wrapResult.bytesConsumed() > 0) {
                            consumed += wrapResult.bytesConsumed();
                        }
                        final SSLEngineResult.Status wrapResultStatus = wrapResult.getStatus();
                        allConsumed = true;
                        for (final ByteBuffer b : appOuts) {
                            if (BufferUtil.hasContent(b)) {
                                allConsumed = false;
                            }
                        }
                        switch (wrapResultStatus) {
                            case CLOSED: {
                                if (BufferUtil.hasContent(SslConnection.this._encryptedOutput)) {
                                    this._cannotAcceptMoreAppDataToFlush = true;
                                    SslConnection.this.getEndPoint().flush(SslConnection.this._encryptedOutput);
                                    SslConnection.this.getEndPoint().shutdownOutput();
                                    if (BufferUtil.hasContent(SslConnection.this._encryptedOutput)) {
                                        return false;
                                    }
                                }
                                else {
                                    SslConnection.this.getEndPoint().shutdownOutput();
                                }
                                return allConsumed;
                            }
                            case BUFFER_UNDERFLOW: {
                                throw new IllegalStateException();
                            }
                            default: {
                                if (SslConnection.DEBUG) {
                                    SslConnection.LOG.debug("{} {} {}", this, wrapResultStatus, BufferUtil.toDetailString(SslConnection.this._encryptedOutput));
                                }
                                if (wrapResult.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED && !this._handshaken) {
                                    this._handshaken = true;
                                    if (SslConnection.DEBUG) {
                                        SslConnection.LOG.debug("{} {} handshake completed", SslConnection.this, "server-side");
                                    }
                                }
                                final SSLEngineResult.HandshakeStatus handshakeStatus = SslConnection.this._sslEngine.getHandshakeStatus();
                                if (this._handshaken && handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING && !SslConnection.this.isRenegotiationAllowed()) {
                                    if (SslConnection.DEBUG) {
                                        SslConnection.LOG.debug("{} renegotiation denied", SslConnection.this);
                                    }
                                    SslConnection.this.getEndPoint().shutdownOutput();
                                    return allConsumed;
                                }
                                if (BufferUtil.hasContent(SslConnection.this._encryptedOutput) && !SslConnection.this.getEndPoint().flush(SslConnection.this._encryptedOutput)) {
                                    SslConnection.this.getEndPoint().flush(SslConnection.this._encryptedOutput);
                                }
                                switch (handshakeStatus) {
                                    case NOT_HANDSHAKING: {
                                        if (!allConsumed && wrapResult.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED && BufferUtil.isEmpty(SslConnection.this._encryptedOutput)) {
                                            continue;
                                        }
                                        return allConsumed && BufferUtil.isEmpty(SslConnection.this._encryptedOutput);
                                    }
                                    case NEED_TASK: {
                                        SslConnection.this._sslEngine.getDelegatedTask().run();
                                        continue;
                                    }
                                    case NEED_WRAP: {
                                        continue;
                                    }
                                    case NEED_UNWRAP: {
                                        if (appOuts[0] == SslConnection.__FILL_CALLED_FLUSH || this.getFillInterest().isInterested()) {
                                            break Label_1038;
                                        }
                                        this._flushRequiresFillToProgress = true;
                                        this.fill(SslConnection.__FLUSH_CALLED_FILL);
                                        if (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                                            continue;
                                        }
                                        break Label_1038;
                                    }
                                    case FINISHED: {
                                        throw new IllegalStateException();
                                    }
                                    default: {
                                        continue;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    return allConsumed && BufferUtil.isEmpty(SslConnection.this._encryptedOutput);
                }
                if (SslConnection.this._sslEngine.isOutboundDone()) {
                    throw new EofException(new ClosedChannelException());
                }
                return false;
            }
            finally {
                if (SslConnection.DEBUG) {
                    SslConnection.LOG.debug("{} flush exit, consumed {}", SslConnection.this, consumed);
                }
                this.releaseEncryptedOutputBuffer();
            }
        }
        
        private void releaseEncryptedOutputBuffer() {
            if (!Thread.holdsLock(this)) {
                throw new IllegalStateException();
            }
            if (SslConnection.this._encryptedOutput != null && !SslConnection.this._encryptedOutput.hasRemaining()) {
                SslConnection.this._bufferPool.release(SslConnection.this._encryptedOutput);
                SslConnection.this._encryptedOutput = null;
            }
        }
        
        @Override
        public void shutdownOutput() {
            final boolean ishut = this.isInputShutdown();
            final boolean oshut = this.isOutputShutdown();
            if (SslConnection.DEBUG) {
                SslConnection.LOG.debug("{} shutdownOutput: oshut={}, ishut={}", SslConnection.this, oshut, ishut);
            }
            if (ishut) {
                SslConnection.this.getEndPoint().close();
            }
            else if (!oshut) {
                try {
                    SslConnection.this._sslEngine.closeOutbound();
                    this.flush(BufferUtil.EMPTY_BUFFER);
                    SslConnection.this.fillInterested();
                }
                catch (Exception e) {
                    SslConnection.LOG.ignore(e);
                    SslConnection.this.getEndPoint().close();
                }
            }
        }
        
        @Override
        public boolean isOutputShutdown() {
            return SslConnection.this._sslEngine.isOutboundDone() || SslConnection.this.getEndPoint().isOutputShutdown();
        }
        
        @Override
        public void close() {
            super.close();
            this.shutdownOutput();
            SslConnection.this.getEndPoint().close();
        }
        
        @Override
        public boolean isOpen() {
            return SslConnection.this.getEndPoint().isOpen();
        }
        
        @Override
        public Object getTransport() {
            return SslConnection.this.getEndPoint();
        }
        
        @Override
        public boolean isInputShutdown() {
            return SslConnection.this._sslEngine.isInboundDone();
        }
        
        @Override
        public String toString() {
            return super.toString() + "->" + SslConnection.this.getEndPoint().toString();
        }
    }
}
