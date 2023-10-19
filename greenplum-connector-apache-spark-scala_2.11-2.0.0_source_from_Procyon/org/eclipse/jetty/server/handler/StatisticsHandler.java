// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server.handler;

import java.util.concurrent.Future;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import java.util.concurrent.TimeoutException;
import org.eclipse.jetty.server.Response;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.util.annotation.ManagedOperation;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.HttpChannelState;
import org.eclipse.jetty.server.AsyncContextEvent;
import java.io.IOException;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import org.eclipse.jetty.util.FutureCallback;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.jetty.util.statistic.SampleStatistic;
import org.eclipse.jetty.util.statistic.CounterStatistic;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.component.Graceful;

@ManagedObject("Request Statistics Gathering")
public class StatisticsHandler extends HandlerWrapper implements Graceful
{
    private final AtomicLong _statsStartedAt;
    private final CounterStatistic _requestStats;
    private final SampleStatistic _requestTimeStats;
    private final CounterStatistic _dispatchedStats;
    private final SampleStatistic _dispatchedTimeStats;
    private final CounterStatistic _asyncWaitStats;
    private final AtomicInteger _asyncDispatches;
    private final AtomicInteger _expires;
    private final AtomicInteger _responses1xx;
    private final AtomicInteger _responses2xx;
    private final AtomicInteger _responses3xx;
    private final AtomicInteger _responses4xx;
    private final AtomicInteger _responses5xx;
    private final AtomicLong _responsesTotalBytes;
    private final AtomicReference<FutureCallback> _shutdown;
    private final AsyncListener _onCompletion;
    
    public StatisticsHandler() {
        this._statsStartedAt = new AtomicLong();
        this._requestStats = new CounterStatistic();
        this._requestTimeStats = new SampleStatistic();
        this._dispatchedStats = new CounterStatistic();
        this._dispatchedTimeStats = new SampleStatistic();
        this._asyncWaitStats = new CounterStatistic();
        this._asyncDispatches = new AtomicInteger();
        this._expires = new AtomicInteger();
        this._responses1xx = new AtomicInteger();
        this._responses2xx = new AtomicInteger();
        this._responses3xx = new AtomicInteger();
        this._responses4xx = new AtomicInteger();
        this._responses5xx = new AtomicInteger();
        this._responsesTotalBytes = new AtomicLong();
        this._shutdown = new AtomicReference<FutureCallback>();
        this._onCompletion = new AsyncListener() {
            @Override
            public void onTimeout(final AsyncEvent event) throws IOException {
                StatisticsHandler.this._expires.incrementAndGet();
            }
            
            @Override
            public void onStartAsync(final AsyncEvent event) throws IOException {
                event.getAsyncContext().addListener(this);
            }
            
            @Override
            public void onError(final AsyncEvent event) throws IOException {
            }
            
            @Override
            public void onComplete(final AsyncEvent event) throws IOException {
                final HttpChannelState state = ((AsyncContextEvent)event).getHttpChannelState();
                final Request request = state.getBaseRequest();
                final long elapsed = System.currentTimeMillis() - request.getTimeStamp();
                final long d = StatisticsHandler.this._requestStats.decrement();
                StatisticsHandler.this._requestTimeStats.set(elapsed);
                StatisticsHandler.this.updateResponse(request);
                StatisticsHandler.this._asyncWaitStats.decrement();
                if (d == 0L) {
                    final FutureCallback shutdown = StatisticsHandler.this._shutdown.get();
                    if (shutdown != null) {
                        shutdown.succeeded();
                    }
                }
            }
        };
    }
    
    @ManagedOperation(value = "resets statistics", impact = "ACTION")
    public void statsReset() {
        this._statsStartedAt.set(System.currentTimeMillis());
        this._requestStats.reset();
        this._requestTimeStats.reset();
        this._dispatchedStats.reset();
        this._dispatchedTimeStats.reset();
        this._asyncWaitStats.reset();
        this._asyncDispatches.set(0);
        this._expires.set(0);
        this._responses1xx.set(0);
        this._responses2xx.set(0);
        this._responses3xx.set(0);
        this._responses4xx.set(0);
        this._responses5xx.set(0);
        this._responsesTotalBytes.set(0L);
    }
    
    @Override
    public void handle(final String path, final Request request, final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws IOException, ServletException {
        this._dispatchedStats.increment();
        final HttpChannelState state = request.getHttpChannelState();
        long start;
        if (state.isInitial()) {
            this._requestStats.increment();
            start = request.getTimeStamp();
        }
        else {
            start = System.currentTimeMillis();
            this._asyncDispatches.incrementAndGet();
        }
        try {
            super.handle(path, request, httpRequest, httpResponse);
        }
        finally {
            final long now = System.currentTimeMillis();
            final long dispatched = now - start;
            this._dispatchedStats.decrement();
            this._dispatchedTimeStats.set(dispatched);
            if (state.isSuspended()) {
                if (state.isInitial()) {
                    state.addListener(this._onCompletion);
                    this._asyncWaitStats.increment();
                }
            }
            else if (state.isInitial()) {
                final long d = this._requestStats.decrement();
                this._requestTimeStats.set(dispatched);
                this.updateResponse(request);
                final FutureCallback shutdown = this._shutdown.get();
                if (shutdown != null) {
                    httpResponse.flushBuffer();
                    if (d == 0L) {
                        shutdown.succeeded();
                    }
                }
            }
        }
    }
    
    protected void updateResponse(final Request request) {
        final Response response = request.getResponse();
        if (request.isHandled()) {
            switch (response.getStatus() / 100) {
                case 1: {
                    this._responses1xx.incrementAndGet();
                    break;
                }
                case 2: {
                    this._responses2xx.incrementAndGet();
                    break;
                }
                case 3: {
                    this._responses3xx.incrementAndGet();
                    break;
                }
                case 4: {
                    this._responses4xx.incrementAndGet();
                    break;
                }
                case 5: {
                    this._responses5xx.incrementAndGet();
                    break;
                }
            }
        }
        else {
            this._responses4xx.incrementAndGet();
        }
        this._responsesTotalBytes.addAndGet(response.getContentCount());
    }
    
    @Override
    protected void doStart() throws Exception {
        this._shutdown.set(null);
        super.doStart();
        this.statsReset();
    }
    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        final FutureCallback shutdown = this._shutdown.get();
        if (shutdown != null && !shutdown.isDone()) {
            shutdown.failed(new TimeoutException());
        }
    }
    
    @ManagedAttribute("number of requests")
    public int getRequests() {
        return (int)this._requestStats.getTotal();
    }
    
    @ManagedAttribute("number of requests currently active")
    public int getRequestsActive() {
        return (int)this._requestStats.getCurrent();
    }
    
    @ManagedAttribute("maximum number of active requests")
    public int getRequestsActiveMax() {
        return (int)this._requestStats.getMax();
    }
    
    @ManagedAttribute("maximum time spend handling requests (in ms)")
    public long getRequestTimeMax() {
        return this._requestTimeStats.getMax();
    }
    
    @ManagedAttribute("total time spend in all request handling (in ms)")
    public long getRequestTimeTotal() {
        return this._requestTimeStats.getTotal();
    }
    
    @ManagedAttribute("mean time spent handling requests (in ms)")
    public double getRequestTimeMean() {
        return this._requestTimeStats.getMean();
    }
    
    @ManagedAttribute("standard deviation for request handling (in ms)")
    public double getRequestTimeStdDev() {
        return this._requestTimeStats.getStdDev();
    }
    
    @ManagedAttribute("number of dispatches")
    public int getDispatched() {
        return (int)this._dispatchedStats.getTotal();
    }
    
    @ManagedAttribute("number of dispatches currently active")
    public int getDispatchedActive() {
        return (int)this._dispatchedStats.getCurrent();
    }
    
    @ManagedAttribute("maximum number of active dispatches being handled")
    public int getDispatchedActiveMax() {
        return (int)this._dispatchedStats.getMax();
    }
    
    @ManagedAttribute("maximum time spend in dispatch handling")
    public long getDispatchedTimeMax() {
        return this._dispatchedTimeStats.getMax();
    }
    
    @ManagedAttribute("total time spent in dispatch handling (in ms)")
    public long getDispatchedTimeTotal() {
        return this._dispatchedTimeStats.getTotal();
    }
    
    @ManagedAttribute("mean time spent in dispatch handling (in ms)")
    public double getDispatchedTimeMean() {
        return this._dispatchedTimeStats.getMean();
    }
    
    @ManagedAttribute("standard deviation for dispatch handling (in ms)")
    public double getDispatchedTimeStdDev() {
        return this._dispatchedTimeStats.getStdDev();
    }
    
    @ManagedAttribute("total number of async requests")
    public int getAsyncRequests() {
        return (int)this._asyncWaitStats.getTotal();
    }
    
    @ManagedAttribute("currently waiting async requests")
    public int getAsyncRequestsWaiting() {
        return (int)this._asyncWaitStats.getCurrent();
    }
    
    @ManagedAttribute("maximum number of waiting async requests")
    public int getAsyncRequestsWaitingMax() {
        return (int)this._asyncWaitStats.getMax();
    }
    
    @ManagedAttribute("number of requested that have been asynchronously dispatched")
    public int getAsyncDispatches() {
        return this._asyncDispatches.get();
    }
    
    @ManagedAttribute("number of async requests requests that have expired")
    public int getExpires() {
        return this._expires.get();
    }
    
    @ManagedAttribute("number of requests with 1xx response status")
    public int getResponses1xx() {
        return this._responses1xx.get();
    }
    
    @ManagedAttribute("number of requests with 2xx response status")
    public int getResponses2xx() {
        return this._responses2xx.get();
    }
    
    @ManagedAttribute("number of requests with 3xx response status")
    public int getResponses3xx() {
        return this._responses3xx.get();
    }
    
    @ManagedAttribute("number of requests with 4xx response status")
    public int getResponses4xx() {
        return this._responses4xx.get();
    }
    
    @ManagedAttribute("number of requests with 5xx response status")
    public int getResponses5xx() {
        return this._responses5xx.get();
    }
    
    @ManagedAttribute("time in milliseconds stats have been collected for")
    public long getStatsOnMs() {
        return System.currentTimeMillis() - this._statsStartedAt.get();
    }
    
    @ManagedAttribute("total number of bytes across all responses")
    public long getResponsesBytesTotal() {
        return this._responsesTotalBytes.get();
    }
    
    public String toStatsHTML() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<h1>Statistics:</h1>\n");
        sb.append("Statistics gathering started ").append(this.getStatsOnMs()).append("ms ago").append("<br />\n");
        sb.append("<h2>Requests:</h2>\n");
        sb.append("Total requests: ").append(this.getRequests()).append("<br />\n");
        sb.append("Active requests: ").append(this.getRequestsActive()).append("<br />\n");
        sb.append("Max active requests: ").append(this.getRequestsActiveMax()).append("<br />\n");
        sb.append("Total requests time: ").append(this.getRequestTimeTotal()).append("<br />\n");
        sb.append("Mean request time: ").append(this.getRequestTimeMean()).append("<br />\n");
        sb.append("Max request time: ").append(this.getRequestTimeMax()).append("<br />\n");
        sb.append("Request time standard deviation: ").append(this.getRequestTimeStdDev()).append("<br />\n");
        sb.append("<h2>Dispatches:</h2>\n");
        sb.append("Total dispatched: ").append(this.getDispatched()).append("<br />\n");
        sb.append("Active dispatched: ").append(this.getDispatchedActive()).append("<br />\n");
        sb.append("Max active dispatched: ").append(this.getDispatchedActiveMax()).append("<br />\n");
        sb.append("Total dispatched time: ").append(this.getDispatchedTimeTotal()).append("<br />\n");
        sb.append("Mean dispatched time: ").append(this.getDispatchedTimeMean()).append("<br />\n");
        sb.append("Max dispatched time: ").append(this.getDispatchedTimeMax()).append("<br />\n");
        sb.append("Dispatched time standard deviation: ").append(this.getDispatchedTimeStdDev()).append("<br />\n");
        sb.append("Total requests suspended: ").append(this.getAsyncRequests()).append("<br />\n");
        sb.append("Total requests expired: ").append(this.getExpires()).append("<br />\n");
        sb.append("Total requests resumed: ").append(this.getAsyncDispatches()).append("<br />\n");
        sb.append("<h2>Responses:</h2>\n");
        sb.append("1xx responses: ").append(this.getResponses1xx()).append("<br />\n");
        sb.append("2xx responses: ").append(this.getResponses2xx()).append("<br />\n");
        sb.append("3xx responses: ").append(this.getResponses3xx()).append("<br />\n");
        sb.append("4xx responses: ").append(this.getResponses4xx()).append("<br />\n");
        sb.append("5xx responses: ").append(this.getResponses5xx()).append("<br />\n");
        sb.append("Bytes sent total: ").append(this.getResponsesBytesTotal()).append("<br />\n");
        return sb.toString();
    }
    
    @Override
    public Future<Void> shutdown() {
        FutureCallback shutdown = new FutureCallback(false);
        this._shutdown.compareAndSet(null, shutdown);
        shutdown = this._shutdown.get();
        if (this._dispatchedStats.getCurrent() == 0L) {
            shutdown.succeeded();
        }
        return shutdown;
    }
}
