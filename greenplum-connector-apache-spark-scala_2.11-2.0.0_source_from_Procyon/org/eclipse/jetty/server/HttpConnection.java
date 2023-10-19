// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import java.nio.channels.WritePendingException;
import org.eclipse.jetty.util.IteratingCallback;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpHeaderValue;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.Callback;
import java.util.concurrent.RejectedExecutionException;
import java.io.IOException;
import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.io.EndPoint;
import java.nio.ByteBuffer;
import org.eclipse.jetty.http.HttpParser;
import org.eclipse.jetty.http.HttpGenerator;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.AbstractConnection;

public class HttpConnection extends AbstractConnection implements Runnable, HttpTransport, Connection.UpgradeFrom
{
    public static final String UPGRADE_CONNECTION_ATTRIBUTE = "org.eclipse.jetty.server.HttpConnection.UPGRADE";
    private static final boolean REQUEST_BUFFER_DIRECT = false;
    private static final boolean HEADER_BUFFER_DIRECT = false;
    private static final boolean CHUNK_BUFFER_DIRECT = false;
    private static final Logger LOG;
    private static final ThreadLocal<HttpConnection> __currentConnection;
    private final HttpConfiguration _config;
    private final Connector _connector;
    private final ByteBufferPool _bufferPool;
    private final HttpGenerator _generator;
    private final HttpChannelOverHttp _channel;
    private final HttpParser _parser;
    private volatile ByteBuffer _requestBuffer;
    private volatile ByteBuffer _chunk;
    private final SendCallback _sendCallback;
    
    public static HttpConnection getCurrentConnection() {
        return HttpConnection.__currentConnection.get();
    }
    
    protected static HttpConnection setCurrentConnection(final HttpConnection connection) {
        final HttpConnection last = HttpConnection.__currentConnection.get();
        HttpConnection.__currentConnection.set(connection);
        return last;
    }
    
    public HttpConfiguration getHttpConfiguration() {
        return this._config;
    }
    
    public HttpConnection(final HttpConfiguration config, final Connector connector, final EndPoint endPoint) {
        super(endPoint, connector.getExecutor(), true);
        this._requestBuffer = null;
        this._chunk = null;
        this._sendCallback = new SendCallback();
        this._config = config;
        this._connector = connector;
        this._bufferPool = this._connector.getByteBufferPool();
        this._generator = this.newHttpGenerator();
        final HttpInput<ByteBuffer> input = this.newHttpInput();
        this._channel = this.newHttpChannel(input);
        this._parser = this.newHttpParser();
        if (HttpConnection.LOG.isDebugEnabled()) {
            HttpConnection.LOG.debug("New HTTP Connection {}", this);
        }
    }
    
    protected HttpGenerator newHttpGenerator() {
        return new HttpGenerator(this._config.getSendServerVersion(), this._config.getSendXPoweredBy());
    }
    
    protected HttpInput<ByteBuffer> newHttpInput() {
        return new HttpInputOverHTTP(this);
    }
    
    protected HttpChannelOverHttp newHttpChannel(final HttpInput<ByteBuffer> httpInput) {
        return new HttpChannelOverHttp(this._connector, this._config, this.getEndPoint(), this, httpInput);
    }
    
    protected HttpParser newHttpParser() {
        return new HttpParser(this.newRequestHandler(), this.getHttpConfiguration().getRequestHeaderSize());
    }
    
    protected HttpParser.RequestHandler<ByteBuffer> newRequestHandler() {
        return this._channel;
    }
    
    public Server getServer() {
        return this._connector.getServer();
    }
    
    public Connector getConnector() {
        return this._connector;
    }
    
    public HttpChannel<?> getHttpChannel() {
        return this._channel;
    }
    
    public HttpParser getParser() {
        return this._parser;
    }
    
    @Override
    public int getMessagesIn() {
        return this.getHttpChannel().getRequests();
    }
    
    @Override
    public int getMessagesOut() {
        return this.getHttpChannel().getRequests();
    }
    
    @Override
    public ByteBuffer onUpgradeFrom() {
        if (BufferUtil.hasContent(this._requestBuffer)) {
            final ByteBuffer buffer = this._requestBuffer;
            this._requestBuffer = null;
            return buffer;
        }
        return null;
    }
    
    void releaseRequestBuffer() {
        if (this._requestBuffer != null && !this._requestBuffer.hasRemaining()) {
            final ByteBuffer buffer = this._requestBuffer;
            this._requestBuffer = null;
            this._bufferPool.release(buffer);
        }
    }
    
    public ByteBuffer getRequestBuffer() {
        if (this._requestBuffer == null) {
            this._requestBuffer = this._bufferPool.acquire(this.getInputBufferSize(), false);
        }
        return this._requestBuffer;
    }
    
    @Override
    public void onFillable() {
        if (HttpConnection.LOG.isDebugEnabled()) {
            HttpConnection.LOG.debug("{} onFillable {}", this, this._channel.getState());
        }
        final HttpConnection last = setCurrentConnection(this);
        int filled = Integer.MAX_VALUE;
        boolean suspended = false;
        try {
            while (!suspended && this.getEndPoint().getConnection() == this) {
                if (BufferUtil.isEmpty(this._requestBuffer)) {
                    if (filled <= 0) {
                        break;
                    }
                    if (this.getEndPoint().isInputShutdown()) {
                        filled = -1;
                        this._parser.atEOF();
                    }
                    else {
                        this._requestBuffer = this.getRequestBuffer();
                        filled = this.getEndPoint().fill(this._requestBuffer);
                        if (filled == 0) {
                            filled = this.getEndPoint().fill(this._requestBuffer);
                        }
                        if (filled < 0) {
                            this._parser.atEOF();
                        }
                    }
                }
                if (this._parser.parseNext((this._requestBuffer == null) ? BufferUtil.EMPTY_BUFFER : this._requestBuffer)) {
                    suspended = !this._channel.handle();
                }
                else {
                    this.releaseRequestBuffer();
                }
            }
        }
        catch (EofException e) {
            HttpConnection.LOG.debug(e);
        }
        catch (Exception e2) {
            if (this._parser.isIdle()) {
                HttpConnection.LOG.debug(e2);
            }
            else {
                HttpConnection.LOG.warn(this.toString(), e2);
            }
            this.close();
        }
        finally {
            setCurrentConnection(last);
            if (!suspended && this.getEndPoint().isOpen() && this.getEndPoint().getConnection() == this) {
                this.fillInterested();
            }
        }
    }
    
    protected void parseContent() throws IOException {
        final ByteBuffer requestBuffer = this.getRequestBuffer();
        while (this._parser.inContentState()) {
            final boolean parsed = this._parser.parseNext((requestBuffer == null) ? BufferUtil.EMPTY_BUFFER : requestBuffer);
            if (BufferUtil.isEmpty(requestBuffer) && this.getEndPoint().isInputShutdown()) {
                this._parser.atEOF();
                if (parsed) {
                    break;
                }
                continue;
            }
            else {
                if (parsed) {
                    break;
                }
                final int filled = this.getEndPoint().fill(requestBuffer);
                if (HttpConnection.LOG.isDebugEnabled()) {
                    HttpConnection.LOG.debug("{} filled {}", this, filled);
                }
                if (filled > 0) {
                    continue;
                }
                if (filled >= 0) {
                    break;
                }
                this._parser.atEOF();
            }
        }
    }
    
    @Override
    public void completed() {
        if (this._channel.getResponse().getStatus() == 101) {
            final Connection connection = (Connection)this._channel.getRequest().getAttribute("org.eclipse.jetty.server.HttpConnection.UPGRADE");
            if (connection != null) {
                this._channel.getState().upgrade();
                this.getEndPoint().upgrade(connection);
                this._channel.reset();
                this._parser.reset();
                this._generator.reset();
                this.releaseRequestBuffer();
                return;
            }
        }
        if (this._channel.isExpecting100Continue()) {
            this._parser.close();
        }
        else if (this._parser.inContentState() && this._generator.isPersistent()) {
            if (this._channel.getRequest().getHttpInput().isAsync()) {
                if (HttpConnection.LOG.isDebugEnabled()) {
                    HttpConnection.LOG.debug("unconsumed async input {}", this);
                }
                this._channel.abort();
            }
            else {
                if (HttpConnection.LOG.isDebugEnabled()) {
                    HttpConnection.LOG.debug("unconsumed input {}", this);
                }
                if (!this._channel.getRequest().getHttpInput().consumeAll()) {
                    this._channel.abort();
                }
            }
        }
        this._channel.reset();
        if (this._generator.isPersistent() && !this._parser.isClosed()) {
            this._parser.reset();
        }
        else {
            this._parser.close();
        }
        this.releaseRequestBuffer();
        if (this._chunk != null) {
            this._bufferPool.release(this._chunk);
        }
        this._chunk = null;
        this._generator.reset();
        if (getCurrentConnection() != this) {
            if (this._parser.isStart()) {
                if (BufferUtil.isEmpty(this._requestBuffer)) {
                    this.fillInterested();
                }
                else if (this.getConnector().isRunning()) {
                    try {
                        this.getExecutor().execute(this);
                    }
                    catch (RejectedExecutionException e) {
                        if (this.getConnector().isRunning()) {
                            HttpConnection.LOG.warn(e);
                        }
                        else {
                            HttpConnection.LOG.ignore(e);
                        }
                        this.getEndPoint().close();
                    }
                }
                else {
                    this.getEndPoint().close();
                }
            }
            else if (this.getEndPoint().isOpen()) {
                this.fillInterested();
            }
        }
    }
    
    @Override
    protected void onFillInterestedFailed(final Throwable cause) {
        this._parser.close();
        super.onFillInterestedFailed(cause);
    }
    
    @Override
    public void onOpen() {
        super.onOpen();
        this.fillInterested();
    }
    
    @Override
    public void onClose() {
        this._sendCallback.close();
        super.onClose();
    }
    
    @Override
    public void run() {
        this.onFillable();
    }
    
    @Override
    public void send(final HttpGenerator.ResponseInfo info, final ByteBuffer content, final boolean lastContent, final Callback callback) {
        if (info != null && this._channel.isExpecting100Continue()) {
            this._generator.setPersistent(false);
        }
        if (this._sendCallback.reset(info, content, lastContent, callback)) {
            this._sendCallback.iterate();
        }
    }
    
    @Override
    public void send(final ByteBuffer content, final boolean lastContent, final Callback callback) {
        if (!lastContent && BufferUtil.isEmpty(content)) {
            callback.succeeded();
        }
        else if (this._sendCallback.reset(null, content, lastContent, callback)) {
            this._sendCallback.iterate();
        }
    }
    
    @Override
    public void abort() {
        this.getEndPoint().close();
    }
    
    @Override
    public String toString() {
        return String.format("%s[p=%s,g=%s,c=%s]", super.toString(), this._parser, this._generator, this._channel);
    }
    
    static {
        LOG = Log.getLogger(HttpConnection.class);
        __currentConnection = new ThreadLocal<HttpConnection>();
    }
    
    protected class HttpChannelOverHttp extends HttpChannel<ByteBuffer>
    {
        public HttpChannelOverHttp(final Connector connector, final HttpConfiguration config, final EndPoint endPoint, final HttpTransport transport, final HttpInput<ByteBuffer> input) {
            super(connector, config, endPoint, transport, input);
        }
        
        @Override
        public void earlyEOF() {
            if (this.getRequest().getMethod() == null) {
                HttpConnection.this.close();
            }
            else {
                super.earlyEOF();
            }
        }
        
        @Override
        public boolean content(final ByteBuffer item) {
            super.content(item);
            return true;
        }
        
        @Override
        public void badMessage(final int status, final String reason) {
            HttpConnection.this._generator.setPersistent(false);
            super.badMessage(status, reason);
        }
        
        @Override
        public boolean headerComplete() {
            final HttpVersion version = this.getHttpVersion();
            boolean persistent = false;
            switch (version) {
                case HTTP_0_9: {
                    persistent = false;
                    break;
                }
                case HTTP_1_0: {
                    persistent = this.getRequest().getHttpFields().contains(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE.asString());
                    if (!persistent) {
                        persistent = HttpMethod.CONNECT.is(this.getRequest().getMethod());
                    }
                    if (persistent) {
                        this.getResponse().getHttpFields().add(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE);
                        break;
                    }
                    break;
                }
                case HTTP_1_1: {
                    persistent = !this.getRequest().getHttpFields().contains(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.asString());
                    if (!persistent) {
                        persistent = HttpMethod.CONNECT.is(this.getRequest().getMethod());
                    }
                    if (!persistent) {
                        this.getResponse().getHttpFields().add(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE);
                        break;
                    }
                    break;
                }
                case HTTP_2: {
                    persistent = false;
                    this.badMessage(400, null);
                    return true;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
            if (!persistent) {
                HttpConnection.this._generator.setPersistent(false);
            }
            return super.headerComplete() && (!this.getHttpConfiguration().isDelayDispatchUntilContent() || HttpConnection.this._parser.getContentLength() <= 0L || this.isExpecting100Continue() || this.isCommitted() || !BufferUtil.isEmpty(HttpConnection.this._requestBuffer));
        }
        
        @Override
        protected void handleException(final Throwable x) {
            HttpConnection.this._generator.setPersistent(false);
            super.handleException(x);
        }
        
        @Override
        public void abort() {
            super.abort();
            HttpConnection.this._generator.setPersistent(false);
        }
        
        @Override
        public boolean messageComplete() {
            super.messageComplete();
            return false;
        }
    }
    
    private class SendCallback extends IteratingCallback
    {
        private HttpGenerator.ResponseInfo _info;
        private ByteBuffer _content;
        private boolean _lastContent;
        private Callback _callback;
        private ByteBuffer _header;
        private boolean _shutdownOut;
        
        private SendCallback() {
            super(true);
        }
        
        private boolean reset(final HttpGenerator.ResponseInfo info, final ByteBuffer content, final boolean last, final Callback callback) {
            if (this.reset()) {
                this._info = info;
                this._content = content;
                this._lastContent = last;
                this._callback = callback;
                this._header = null;
                this._shutdownOut = false;
                return true;
            }
            if (this.isClosed()) {
                callback.failed(new EofException());
            }
            else {
                callback.failed(new WritePendingException());
            }
            return false;
        }
        
        public Action process() throws Exception {
            if (this._callback == null) {
                throw new IllegalStateException();
            }
            ByteBuffer chunk = HttpConnection.this._chunk;
            while (true) {
                final HttpGenerator.Result result = HttpConnection.this._generator.generateResponse(this._info, this._header, chunk, this._content, this._lastContent);
                if (HttpConnection.LOG.isDebugEnabled()) {
                    HttpConnection.LOG.debug("{} generate: {} ({},{},{})@{}", this, result, BufferUtil.toSummaryString(this._header), BufferUtil.toSummaryString(this._content), this._lastContent, HttpConnection.this._generator.getState());
                }
                switch (result) {
                    case NEED_HEADER: {
                        this._header = HttpConnection.this._bufferPool.acquire(HttpConnection.this._config.getResponseHeaderSize(), false);
                        continue;
                    }
                    case NEED_CHUNK: {
                        chunk = (HttpConnection.this._chunk = HttpConnection.this._bufferPool.acquire(12, false));
                        continue;
                    }
                    case FLUSH: {
                        if (HttpConnection.this._channel.getRequest().isHead() || HttpConnection.this._generator.isNoContent()) {
                            BufferUtil.clear(chunk);
                            BufferUtil.clear(this._content);
                        }
                        if (BufferUtil.hasContent(this._header)) {
                            if (BufferUtil.hasContent(this._content)) {
                                if (BufferUtil.hasContent(chunk)) {
                                    HttpConnection.this.getEndPoint().write(this, this._header, chunk, this._content);
                                }
                                else {
                                    HttpConnection.this.getEndPoint().write(this, this._header, this._content);
                                }
                            }
                            else {
                                HttpConnection.this.getEndPoint().write(this, this._header);
                            }
                        }
                        else if (BufferUtil.hasContent(chunk)) {
                            if (BufferUtil.hasContent(this._content)) {
                                HttpConnection.this.getEndPoint().write(this, chunk, this._content);
                            }
                            else {
                                HttpConnection.this.getEndPoint().write(this, chunk);
                            }
                        }
                        else if (BufferUtil.hasContent(this._content)) {
                            HttpConnection.this.getEndPoint().write(this, this._content);
                        }
                        else {
                            this.succeeded();
                        }
                        return Action.SCHEDULED;
                    }
                    case SHUTDOWN_OUT: {
                        this._shutdownOut = true;
                        continue;
                    }
                    case DONE: {
                        return Action.SUCCEEDED;
                    }
                    case CONTINUE: {
                        continue;
                    }
                    default: {
                        throw new IllegalStateException("generateResponse=" + result);
                    }
                }
            }
        }
        
        private void releaseHeader() {
            final ByteBuffer h = this._header;
            this._header = null;
            if (h != null) {
                HttpConnection.this._bufferPool.release(h);
            }
        }
        
        @Override
        protected void onCompleteSuccess() {
            this.releaseHeader();
            this._callback.succeeded();
            if (this._shutdownOut) {
                HttpConnection.this.getEndPoint().shutdownOutput();
            }
        }
        
        public void onCompleteFailure(final Throwable x) {
            this.releaseHeader();
            AbstractConnection.this.failedCallback(this._callback, x);
            if (this._shutdownOut) {
                HttpConnection.this.getEndPoint().shutdownOutput();
            }
        }
        
        @Override
        public String toString() {
            return String.format("%s[i=%s,cb=%s]", super.toString(), this._info, this._callback);
        }
    }
}
