// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server.handler;

import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.AsyncContextState;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.util.log.Logger;

public class RequestLogHandler extends HandlerWrapper
{
    private static final Logger LOG;
    private RequestLog _requestLog;
    private final AsyncListener _listener;
    
    public RequestLogHandler() {
        this._listener = new AsyncListener() {
            @Override
            public void onTimeout(final AsyncEvent event) throws IOException {
            }
            
            @Override
            public void onStartAsync(final AsyncEvent event) throws IOException {
                event.getAsyncContext().addListener(this);
            }
            
            @Override
            public void onError(final AsyncEvent event) throws IOException {
                final HttpServletResponse response = (HttpServletResponse)event.getAsyncContext().getResponse();
                if (!response.isCommitted()) {
                    response.setStatus(500);
                }
            }
            
            @Override
            public void onComplete(final AsyncEvent event) throws IOException {
                final AsyncContextState context = (AsyncContextState)event.getAsyncContext();
                final Request request = context.getHttpChannelState().getBaseRequest();
                final Response response = request.getResponse();
                RequestLogHandler.this._requestLog.log(request, response);
            }
        };
    }
    
    @Override
    public void handle(final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        try {
            super.handle(target, baseRequest, request, response);
        }
        catch (Error error) {}
        catch (IOException ex) {}
        catch (ServletException ex2) {}
        catch (RuntimeException e) {
            if (!response.isCommitted() && !baseRequest.getHttpChannelState().isAsync()) {
                response.setStatus(500);
            }
            throw e;
        }
        finally {
            if (this._requestLog != null && baseRequest.getDispatcherType().equals(DispatcherType.REQUEST)) {
                if (baseRequest.getHttpChannelState().isAsync()) {
                    if (baseRequest.getHttpChannelState().isInitial()) {
                        baseRequest.getAsyncContext().addListener(this._listener);
                    }
                }
                else {
                    this._requestLog.log(baseRequest, (Response)response);
                }
            }
        }
    }
    
    public void setRequestLog(final RequestLog requestLog) {
        this.updateBean(this._requestLog, requestLog);
        this._requestLog = requestLog;
    }
    
    public RequestLog getRequestLog() {
        return this._requestLog;
    }
    
    @Override
    protected void doStart() throws Exception {
        if (this._requestLog == null) {
            RequestLogHandler.LOG.warn("!RequestLog", new Object[0]);
            this._requestLog = new NullRequestLog();
        }
        super.doStart();
    }
    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if (this._requestLog instanceof NullRequestLog) {
            this._requestLog = null;
        }
    }
    
    static {
        LOG = Log.getLogger(RequestLogHandler.class);
    }
    
    private static class NullRequestLog extends AbstractLifeCycle implements RequestLog
    {
        @Override
        public void log(final Request request, final Response response) {
        }
    }
}
