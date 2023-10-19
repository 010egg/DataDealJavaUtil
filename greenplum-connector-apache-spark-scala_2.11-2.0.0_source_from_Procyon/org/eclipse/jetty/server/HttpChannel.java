// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.io.ChannelEndPoint;
import org.eclipse.jetty.util.thread.Scheduler;
import org.eclipse.jetty.util.SharedBlockingCallback;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.http.HttpField;
import java.nio.charset.StandardCharsets;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpHeaderValue;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.server.handler.ContextHandler;
import java.util.Iterator;
import java.util.List;
import java.nio.channels.ClosedChannelException;
import org.eclipse.jetty.io.EofException;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.handler.ErrorHandler;
import javax.servlet.DispatcherType;
import java.nio.ByteBuffer;
import org.eclipse.jetty.http.HttpGenerator;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.URIUtil;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.io.EndPoint;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.http.HttpParser;

public class HttpChannel<T> implements HttpParser.RequestHandler<T>, Runnable, HttpParser.ProxyHandler
{
    private static final Logger LOG;
    private static final ThreadLocal<HttpChannel<?>> __currentChannel;
    private final AtomicBoolean _committed;
    private final AtomicInteger _requests;
    private final Connector _connector;
    private final HttpConfiguration _configuration;
    private final EndPoint _endPoint;
    private final HttpTransport _transport;
    private final HttpURI _uri;
    private final HttpChannelState _state;
    private final Request _request;
    private final Response _response;
    private HttpVersion _version;
    private boolean _expect;
    private boolean _expect100Continue;
    private boolean _expect102Processing;
    
    public static HttpChannel<?> getCurrentHttpChannel() {
        return HttpChannel.__currentChannel.get();
    }
    
    protected static HttpChannel<?> setCurrentHttpChannel(final HttpChannel<?> channel) {
        final HttpChannel<?> last = HttpChannel.__currentChannel.get();
        HttpChannel.__currentChannel.set(channel);
        return last;
    }
    
    public HttpChannel(final Connector connector, final HttpConfiguration configuration, final EndPoint endPoint, final HttpTransport transport, final HttpInput<T> input) {
        this._committed = new AtomicBoolean();
        this._requests = new AtomicInteger();
        this._version = HttpVersion.HTTP_1_1;
        this._expect = false;
        this._expect100Continue = false;
        this._expect102Processing = false;
        this._connector = connector;
        this._configuration = configuration;
        this._endPoint = endPoint;
        this._transport = transport;
        this._uri = new HttpURI(URIUtil.__CHARSET);
        input.init(this._state = new HttpChannelState(this));
        this._request = new Request(this, input);
        this._response = new Response(this, new HttpOutput(this));
        if (HttpChannel.LOG.isDebugEnabled()) {
            HttpChannel.LOG.debug("new {} -> {},{},{}", this, this._endPoint, this._endPoint.getConnection(), this._state);
        }
    }
    
    public HttpChannelState getState() {
        return this._state;
    }
    
    public HttpVersion getHttpVersion() {
        return this._version;
    }
    
    public int getRequests() {
        return this._requests.get();
    }
    
    public Connector getConnector() {
        return this._connector;
    }
    
    public HttpTransport getHttpTransport() {
        return this._transport;
    }
    
    public long getIdleTimeout() {
        return this._endPoint.getIdleTimeout();
    }
    
    public void setIdleTimeout(final long timeoutMs) {
        this._endPoint.setIdleTimeout(timeoutMs);
    }
    
    public ByteBufferPool getByteBufferPool() {
        return this._connector.getByteBufferPool();
    }
    
    public HttpConfiguration getHttpConfiguration() {
        return this._configuration;
    }
    
    public Server getServer() {
        return this._connector.getServer();
    }
    
    public Request getRequest() {
        return this._request;
    }
    
    public Response getResponse() {
        return this._response;
    }
    
    public EndPoint getEndPoint() {
        return this._endPoint;
    }
    
    public InetSocketAddress getLocalAddress() {
        return this._endPoint.getLocalAddress();
    }
    
    public InetSocketAddress getRemoteAddress() {
        return this._endPoint.getRemoteAddress();
    }
    
    @Override
    public int getHeaderCacheSize() {
        return this._configuration.getHeaderCacheSize();
    }
    
    public void continue100(final int available) throws IOException {
        if (this.isExpecting100Continue()) {
            this._expect100Continue = false;
            if (available == 0) {
                if (this._response.isCommitted()) {
                    throw new IOException("Committed before 100 Continues");
                }
                final boolean committed = this.sendResponse(HttpGenerator.CONTINUE_100_INFO, null, false);
                if (!committed) {
                    throw new IOException("Concurrent commit while trying to send 100-Continue");
                }
            }
        }
    }
    
    public void reset() {
        this._committed.set(false);
        this._expect = false;
        this._expect100Continue = false;
        this._expect102Processing = false;
        this._request.recycle();
        this._response.recycle();
        this._uri.clear();
    }
    
    @Override
    public void run() {
        this.handle();
    }
    
    public boolean handle() {
        if (HttpChannel.LOG.isDebugEnabled()) {
            HttpChannel.LOG.debug("{} handle enter", this);
        }
        final HttpChannel<?> last = setCurrentHttpChannel(this);
        String threadName = null;
        if (HttpChannel.LOG.isDebugEnabled()) {
            threadName = Thread.currentThread().getName();
            Thread.currentThread().setName(threadName + " - " + this._uri);
        }
        HttpChannelState.Action action = this._state.handling();
        try {
        Label_0939:
            while (action.ordinal() < HttpChannelState.Action.WAIT.ordinal() && this.getServer().isRunning()) {
                boolean error = false;
                try {
                    if (HttpChannel.LOG.isDebugEnabled()) {
                        HttpChannel.LOG.debug("{} action {}", this, action);
                    }
                    switch (action) {
                        case REQUEST_DISPATCH: {
                            this._request.setHandled(false);
                            this._response.getHttpOutput().reopen();
                            this._request.setDispatcherType(DispatcherType.REQUEST);
                            final List<HttpConfiguration.Customizer> customizers = this._configuration.getCustomizers();
                            if (!customizers.isEmpty()) {
                                for (final HttpConfiguration.Customizer customizer : customizers) {
                                    customizer.customize(this.getConnector(), this._configuration, this._request);
                                }
                            }
                            this.getServer().handle(this);
                            break;
                        }
                        case ASYNC_DISPATCH: {
                            this._request.setHandled(false);
                            this._response.getHttpOutput().reopen();
                            this._request.setDispatcherType(DispatcherType.ASYNC);
                            this.getServer().handleAsync(this);
                            break;
                        }
                        case ASYNC_EXPIRED: {
                            this._request.setHandled(false);
                            this._response.getHttpOutput().reopen();
                            this._request.setDispatcherType(DispatcherType.ERROR);
                            final Throwable ex = this._state.getAsyncContextEvent().getThrowable();
                            String reason = "Async Timeout";
                            if (ex != null) {
                                reason = "Async Exception";
                                this._request.setAttribute("javax.servlet.error.exception", ex);
                            }
                            this._request.setAttribute("javax.servlet.error.status_code", new Integer(500));
                            this._request.setAttribute("javax.servlet.error.message", reason);
                            this._request.setAttribute("javax.servlet.error.request_uri", this._request.getRequestURI());
                            this._response.setStatusWithReason(500, reason);
                            final ErrorHandler eh = ErrorHandler.getErrorHandler(this.getServer(), this._state.getContextHandler());
                            if (eh instanceof ErrorHandler.ErrorPageMapper) {
                                final String error_page = ((ErrorHandler.ErrorPageMapper)eh).getErrorPage((HttpServletRequest)this._state.getAsyncContextEvent().getSuppliedRequest());
                                if (error_page != null) {
                                    this._state.getAsyncContextEvent().setDispatchPath(error_page);
                                }
                            }
                            this.getServer().handleAsync(this);
                            break;
                        }
                        case READ_CALLBACK: {
                            final ContextHandler handler = this._state.getContextHandler();
                            if (handler != null) {
                                handler.handle(this._request.getHttpInput());
                                break;
                            }
                            this._request.getHttpInput().run();
                            break;
                        }
                        case WRITE_CALLBACK: {
                            final ContextHandler handler = this._state.getContextHandler();
                            if (handler != null) {
                                handler.handle(this._response.getHttpOutput());
                                break;
                            }
                            this._response.getHttpOutput().run();
                            break;
                        }
                        default: {
                            break Label_0939;
                        }
                    }
                }
                catch (Error e) {
                    if ("ContinuationThrowable".equals(e.getClass().getSimpleName())) {
                        HttpChannel.LOG.ignore(e);
                    }
                    else {
                        error = true;
                        HttpChannel.LOG.warn(String.valueOf(this._uri), e);
                        this._state.error(e);
                        this._request.setHandled(true);
                        this.handleException(e);
                    }
                }
                catch (Exception e2) {
                    error = true;
                    if (e2 instanceof EofException) {
                        HttpChannel.LOG.debug(e2);
                    }
                    else {
                        HttpChannel.LOG.warn(String.valueOf(this._uri), e2);
                    }
                    this._state.error(e2);
                    this._request.setHandled(true);
                    this.handleException(e2);
                }
                finally {
                    if (error && this._state.isAsyncStarted()) {
                        this._state.errorComplete();
                    }
                    action = this._state.unhandle();
                }
            }
            if (action == HttpChannelState.Action.COMPLETE) {
                try {
                    this._state.completed();
                    if (!this._response.isCommitted() && !this._request.isHandled()) {
                        this._response.sendError(404);
                    }
                    else {
                        this._response.closeOutput();
                    }
                }
                catch (EofException | ClosedChannelException ex3) {
                    final IOException ex2;
                    final IOException e3 = ex2;
                    HttpChannel.LOG.debug(e3);
                }
                catch (Exception e4) {
                    HttpChannel.LOG.warn("complete failed", e4);
                }
                finally {
                    this._request.setHandled(true);
                    this._transport.completed();
                }
            }
        }
        finally {
            setCurrentHttpChannel(last);
            if (threadName != null && HttpChannel.LOG.isDebugEnabled()) {
                Thread.currentThread().setName(threadName);
            }
        }
        if (HttpChannel.LOG.isDebugEnabled()) {
            HttpChannel.LOG.debug("{} handle exit, result {}", this, action);
        }
        return action != HttpChannelState.Action.WAIT;
    }
    
    protected void handleException(final Throwable x) {
        try {
            this._request.setAttribute("javax.servlet.error.exception", x);
            this._request.setAttribute("javax.servlet.error.exception_type", x.getClass());
            if (this._state.isSuspended()) {
                final HttpFields fields = new HttpFields();
                fields.add(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE);
                final HttpGenerator.ResponseInfo info = new HttpGenerator.ResponseInfo(this._request.getHttpVersion(), fields, 0L, 500, null, this._request.isHead());
                final boolean committed = this.sendResponse(info, null, true);
                if (!committed) {
                    HttpChannel.LOG.warn("Could not send response error 500: " + x, new Object[0]);
                }
                this._request.getAsyncContext().complete();
            }
            else if (this.isCommitted()) {
                this.abort();
                if (!(x instanceof EofException)) {
                    HttpChannel.LOG.warn("Could not send response error 500: " + x, new Object[0]);
                }
            }
            else {
                this._response.setHeader(HttpHeader.CONNECTION.asString(), HttpHeaderValue.CLOSE.asString());
                this._response.sendError(500, x.getMessage());
            }
        }
        catch (IOException e) {
            HttpChannel.LOG.debug("Could not commit response error 500", e);
        }
    }
    
    public boolean isExpecting100Continue() {
        return this._expect100Continue;
    }
    
    public boolean isExpecting102Processing() {
        return this._expect102Processing;
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x{r=%s,c=%b,a=%s,uri=%s}", this.getClass().getSimpleName(), this.hashCode(), this._requests, this._committed.get(), this._state.getState(), this._uri);
    }
    
    @Override
    public void proxied(final String protocol, final String sAddr, final String dAddr, final int sPort, final int dPort) {
        this._request.setAttribute("PROXY", protocol);
        this._request.setServerName(sAddr);
        this._request.setServerPort(dPort);
        this._request.setRemoteAddr(InetSocketAddress.createUnresolved(sAddr, sPort));
    }
    
    @Override
    public boolean startRequest(final HttpMethod httpMethod, final String method, final ByteBuffer uri, final HttpVersion version) {
        this._expect = false;
        this._expect100Continue = false;
        this._expect102Processing = false;
        this._request.setTimeStamp(System.currentTimeMillis());
        this._request.setMethod(httpMethod, method);
        if (httpMethod == HttpMethod.CONNECT) {
            this._uri.parseConnect(uri.array(), uri.arrayOffset() + uri.position(), uri.remaining());
        }
        else {
            this._uri.parse(uri.array(), uri.arrayOffset() + uri.position(), uri.remaining());
        }
        this._request.setUri(this._uri);
        String path;
        try {
            path = this._uri.getDecodedPath();
        }
        catch (Exception e) {
            HttpChannel.LOG.warn("Failed UTF-8 decode for request path, trying ISO-8859-1", new Object[0]);
            HttpChannel.LOG.ignore(e);
            path = this._uri.getDecodedPath(StandardCharsets.ISO_8859_1);
        }
        String info = URIUtil.canonicalPath(path);
        if (info == null) {
            if (path != null || this._uri.getScheme() == null || this._uri.getHost() == null) {
                this.badMessage(400, null);
                return true;
            }
            info = "/";
            this._request.setRequestURI("");
        }
        this._request.setPathInfo(info);
        this._version = ((version == null) ? HttpVersion.HTTP_0_9 : version);
        this._request.setHttpVersion(this._version);
        return false;
    }
    
    @Override
    public boolean parsedHeader(final HttpField field) {
        final HttpHeader header = field.getHeader();
        String value = field.getValue();
        if (value == null) {
            value = "";
        }
        if (header != null) {
            switch (header) {
                case EXPECT: {
                    if (this._version.getVersion() >= HttpVersion.HTTP_1_1.getVersion()) {
                        HttpHeaderValue expect = HttpHeaderValue.CACHE.get(value);
                        switch ((expect == null) ? HttpHeaderValue.UNKNOWN : expect) {
                            case CONTINUE: {
                                this._expect100Continue = true;
                                break;
                            }
                            case PROCESSING: {
                                this._expect102Processing = true;
                                break;
                            }
                            default: {
                                final String[] values = value.split(",");
                                for (int i = 0; i < values.length; ++i) {
                                    expect = HttpHeaderValue.CACHE.get(values[i].trim());
                                    if (expect == null) {
                                        this._expect = true;
                                    }
                                    else {
                                        switch (expect) {
                                            case CONTINUE: {
                                                this._expect100Continue = true;
                                                break;
                                            }
                                            case PROCESSING: {
                                                this._expect102Processing = true;
                                                break;
                                            }
                                            default: {
                                                this._expect = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
                case CONTENT_TYPE: {
                    final MimeTypes.Type mime = MimeTypes.CACHE.get(value);
                    final String charset = (mime == null || mime.getCharset() == null) ? MimeTypes.getCharsetFromContentType(value) : mime.getCharset().toString();
                    if (charset != null) {
                        this._request.setCharacterEncodingUnchecked(charset);
                        break;
                    }
                    break;
                }
            }
        }
        if (field.getName() != null) {
            this._request.getHttpFields().add(field);
        }
        return false;
    }
    
    @Override
    public boolean parsedHostHeader(final String host, final int port) {
        if (this._uri.getHost() == null) {
            this._request.setServerName(host);
            this._request.setServerPort(port);
        }
        return false;
    }
    
    @Override
    public boolean headerComplete() {
        this._requests.incrementAndGet();
        final HttpFields fields = this._response.getHttpFields();
        switch (this._version) {
            case HTTP_0_9: {
                break;
            }
            case HTTP_1_0: {
                if (this._configuration.getSendDateHeader() && !fields.contains(HttpHeader.DATE)) {
                    this._response.getHttpFields().add(this._connector.getServer().getDateField());
                    break;
                }
                break;
            }
            case HTTP_1_1: {
                if (this._configuration.getSendDateHeader() && !fields.contains(HttpHeader.DATE)) {
                    this._response.getHttpFields().add(this._connector.getServer().getDateField());
                }
                if (this._expect) {
                    this.badMessage(417, null);
                    return true;
                }
                break;
            }
            default: {
                throw new IllegalStateException();
            }
        }
        return true;
    }
    
    @Override
    public boolean content(final T item) {
        if (HttpChannel.LOG.isDebugEnabled()) {
            HttpChannel.LOG.debug("{} content {}", this, item);
        }
        final HttpInput<T> input = (HttpInput<T>)this._request.getHttpInput();
        input.content(item);
        return false;
    }
    
    @Override
    public boolean messageComplete() {
        if (HttpChannel.LOG.isDebugEnabled()) {
            HttpChannel.LOG.debug("{} messageComplete", this);
        }
        this._request.getHttpInput().messageComplete();
        return true;
    }
    
    @Override
    public void earlyEOF() {
        this._request.getHttpInput().earlyEOF();
    }
    
    @Override
    public void badMessage(int status, final String reason) {
        if (status < 400 || status > 599) {
            status = 400;
        }
        try {
            if (this._state.handling() == HttpChannelState.Action.REQUEST_DISPATCH) {
                ByteBuffer content = null;
                final HttpFields fields = new HttpFields();
                final ErrorHandler handler = this.getServer().getBean(ErrorHandler.class);
                if (handler != null) {
                    content = handler.badMessageError(status, reason, fields);
                }
                this.sendResponse(new HttpGenerator.ResponseInfo(HttpVersion.HTTP_1_1, fields, 0L, status, reason, false), content, true);
            }
        }
        catch (IOException e) {
            HttpChannel.LOG.debug(e);
        }
        finally {
            if (this._state.unhandle() != HttpChannelState.Action.COMPLETE) {
                throw new IllegalStateException();
            }
            this._state.completed();
        }
    }
    
    protected boolean sendResponse(HttpGenerator.ResponseInfo info, final ByteBuffer content, final boolean complete, final Callback callback) {
        final boolean committing = this._committed.compareAndSet(false, true);
        if (committing) {
            if (info == null) {
                info = this._response.newResponseInfo();
            }
            final int status = info.getStatus();
            final Callback committed = (status < 200 && status >= 100) ? new Commit100Callback(callback) : new CommitCallback(callback);
            this._transport.send(info, content, complete, committed);
        }
        else if (info == null) {
            this._transport.send(content, complete, callback);
        }
        else {
            callback.failed(new IllegalStateException("committed"));
        }
        return committing;
    }
    
    protected boolean sendResponse(final HttpGenerator.ResponseInfo info, final ByteBuffer content, final boolean complete) throws IOException {
        try (final SharedBlockingCallback.Blocker blocker = this._response.getHttpOutput().acquireWriteBlockingCallback()) {
            final boolean committing = this.sendResponse(info, content, complete, blocker);
            blocker.block();
            return committing;
        }
    }
    
    public boolean isCommitted() {
        return this._committed.get();
    }
    
    protected void write(final ByteBuffer content, final boolean complete, final Callback callback) {
        this.sendResponse(null, content, complete, callback);
    }
    
    protected void execute(final Runnable task) {
        this._connector.getExecutor().execute(task);
    }
    
    public Scheduler getScheduler() {
        return this._connector.getScheduler();
    }
    
    public boolean useDirectBuffers() {
        return this.getEndPoint() instanceof ChannelEndPoint;
    }
    
    public void abort() {
        this._transport.abort();
    }
    
    static {
        LOG = Log.getLogger(HttpChannel.class);
        __currentChannel = new ThreadLocal<HttpChannel<?>>();
    }
    
    private class CommitCallback implements Callback
    {
        private final Callback _callback;
        
        private CommitCallback(final Callback callback) {
            this._callback = callback;
        }
        
        @Override
        public void succeeded() {
            this._callback.succeeded();
        }
        
        @Override
        public void failed(final Throwable x) {
            if (x instanceof EofException || x instanceof ClosedChannelException) {
                HttpChannel.LOG.debug(x);
                this._callback.failed(x);
                HttpChannel.this._response.getHttpOutput().closed();
            }
            else {
                HttpChannel.LOG.warn("Commit failed", x);
                HttpChannel.this._transport.send(HttpGenerator.RESPONSE_500_INFO, null, true, new Callback() {
                    @Override
                    public void succeeded() {
                        CommitCallback.this._callback.failed(x);
                        HttpChannel.this._response.getHttpOutput().closed();
                    }
                    
                    @Override
                    public void failed(final Throwable th) {
                        HttpChannel.LOG.ignore(th);
                        CommitCallback.this._callback.failed(x);
                        HttpChannel.this._response.getHttpOutput().closed();
                    }
                });
            }
        }
    }
    
    private class Commit100Callback extends CommitCallback
    {
        private Commit100Callback(final Callback callback) {
            super(callback);
        }
        
        @Override
        public void succeeded() {
            if (HttpChannel.this._committed.compareAndSet(true, false)) {
                super.succeeded();
            }
            else {
                super.failed(new IllegalStateException());
            }
        }
    }
}
