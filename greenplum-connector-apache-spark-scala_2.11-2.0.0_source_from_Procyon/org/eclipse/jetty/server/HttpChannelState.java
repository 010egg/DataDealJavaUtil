// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.log.Log;
import javax.servlet.ServletResponse;
import org.eclipse.jetty.util.thread.Scheduler;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.server.handler.ContextHandler;
import javax.servlet.ServletContext;
import java.util.Iterator;
import javax.servlet.AsyncEvent;
import java.util.ArrayList;
import javax.servlet.AsyncListener;
import java.util.List;
import org.eclipse.jetty.util.log.Logger;

public class HttpChannelState
{
    private static final Logger LOG;
    private static final long DEFAULT_TIMEOUT;
    private final boolean DEBUG;
    private final HttpChannel<?> _channel;
    private List<AsyncListener> _asyncListeners;
    private State _state;
    private Async _async;
    private boolean _initial;
    private boolean _asyncRead;
    private boolean _asyncWrite;
    private long _timeoutMs;
    private AsyncContextEvent _event;
    
    protected HttpChannelState(final HttpChannel<?> channel) {
        this.DEBUG = HttpChannelState.LOG.isDebugEnabled();
        this._timeoutMs = HttpChannelState.DEFAULT_TIMEOUT;
        this._channel = channel;
        this._state = State.IDLE;
        this._async = null;
        this._initial = true;
    }
    
    public State getState() {
        synchronized (this) {
            return this._state;
        }
    }
    
    public void addListener(final AsyncListener listener) {
        synchronized (this) {
            if (this._asyncListeners == null) {
                this._asyncListeners = new ArrayList<AsyncListener>();
            }
            this._asyncListeners.add(listener);
        }
    }
    
    public void setTimeout(final long ms) {
        synchronized (this) {
            this._timeoutMs = ms;
        }
    }
    
    public long getTimeout() {
        synchronized (this) {
            return this._timeoutMs;
        }
    }
    
    public AsyncContextEvent getAsyncContextEvent() {
        synchronized (this) {
            return this._event;
        }
    }
    
    @Override
    public String toString() {
        synchronized (this) {
            return String.format("%s@%x{s=%s i=%b a=%s}", this.getClass().getSimpleName(), this.hashCode(), this._state, this._initial, this._async);
        }
    }
    
    public String getStatusString() {
        synchronized (this) {
            return String.format("s=%s i=%b a=%s", this._state, this._initial, this._async);
        }
    }
    
    protected Action handling() {
        synchronized (this) {
            if (this.DEBUG) {
                HttpChannelState.LOG.debug("{} handling {}", this, this._state);
            }
            switch (this._state) {
                case IDLE: {
                    this._initial = true;
                    this._state = State.DISPATCHED;
                    return Action.REQUEST_DISPATCH;
                }
                case COMPLETING: {
                    return Action.COMPLETE;
                }
                case COMPLETED: {
                    return Action.WAIT;
                }
                case ASYNC_WOKEN: {
                    if (this._asyncRead) {
                        this._state = State.ASYNC_IO;
                        this._asyncRead = false;
                        return Action.READ_CALLBACK;
                    }
                    if (this._asyncWrite) {
                        this._state = State.ASYNC_IO;
                        this._asyncWrite = false;
                        return Action.WRITE_CALLBACK;
                    }
                    if (this._async != null) {
                        final Async async = this._async;
                        switch (async) {
                            case COMPLETE: {
                                this._state = State.COMPLETING;
                                return Action.COMPLETE;
                            }
                            case DISPATCH: {
                                this._state = State.DISPATCHED;
                                this._async = null;
                                return Action.ASYNC_DISPATCH;
                            }
                            case EXPIRED: {
                                this._state = State.DISPATCHED;
                                this._async = null;
                                return Action.ASYNC_EXPIRED;
                            }
                            case STARTED: {
                                if (this.DEBUG) {
                                    HttpChannelState.LOG.debug("TODO Fix this double dispatch", new IllegalStateException(this.getStatusString()));
                                }
                                return Action.WAIT;
                            }
                        }
                    }
                    return Action.WAIT;
                }
                default: {
                    throw new IllegalStateException(this.getStatusString());
                }
            }
        }
    }
    
    public void startAsync(final AsyncContextEvent event) {
        final List<AsyncListener> lastAsyncListeners;
        synchronized (this) {
            if (this._state != State.DISPATCHED || this._async != null) {
                throw new IllegalStateException(this.getStatusString());
            }
            this._async = Async.STARTED;
            this._event = event;
            lastAsyncListeners = this._asyncListeners;
            this._asyncListeners = null;
        }
        if (lastAsyncListeners != null) {
            for (final AsyncListener listener : lastAsyncListeners) {
                try {
                    listener.onStartAsync(event);
                }
                catch (Exception e) {
                    HttpChannelState.LOG.warn(e);
                }
            }
        }
    }
    
    protected void error(final Throwable th) {
        synchronized (this) {
            if (this._event != null) {
                this._event.setThrowable(th);
            }
        }
    }
    
    protected Action unhandle() {
        synchronized (this) {
            if (this.DEBUG) {
                HttpChannelState.LOG.debug("{} unhandle {}", this, this._state);
            }
            switch (this._state) {
                case DISPATCHED:
                case ASYNC_IO: {
                    if (this._asyncRead) {
                        this._state = State.ASYNC_IO;
                        this._asyncRead = false;
                        return Action.READ_CALLBACK;
                    }
                    if (this._asyncWrite) {
                        this._asyncWrite = false;
                        this._state = State.ASYNC_IO;
                        return Action.WRITE_CALLBACK;
                    }
                    if (this._async != null) {
                        this._initial = false;
                        switch (this._async) {
                            case COMPLETE: {
                                this._state = State.COMPLETING;
                                this._async = null;
                                return Action.COMPLETE;
                            }
                            case DISPATCH: {
                                this._state = State.DISPATCHED;
                                this._async = null;
                                return Action.ASYNC_DISPATCH;
                            }
                            case EXPIRED: {
                                this._state = State.DISPATCHED;
                                this._async = null;
                                return Action.ASYNC_EXPIRED;
                            }
                            case EXPIRING:
                            case STARTED: {
                                this.scheduleTimeout();
                                this._state = State.ASYNC_WAIT;
                                return Action.WAIT;
                            }
                        }
                    }
                    this._state = State.COMPLETING;
                    return Action.COMPLETE;
                }
                default: {
                    throw new IllegalStateException(this.getStatusString());
                }
            }
        }
    }
    
    public void dispatch(final ServletContext context, final String path) {
        boolean dispatch = false;
        synchronized (this) {
            if (this._async != Async.STARTED && this._async != Async.EXPIRING) {
                throw new IllegalStateException("AsyncContext#dispath " + this.getStatusString());
            }
            this._async = Async.DISPATCH;
            if (context != null) {
                this._event.setDispatchContext(context);
            }
            if (path != null) {
                this._event.setDispatchPath(path);
            }
            switch (this._state) {
                case DISPATCHED:
                case ASYNC_IO: {
                    dispatch = false;
                    break;
                }
                case ASYNC_WAIT: {
                    this._state = State.ASYNC_WOKEN;
                    dispatch = true;
                    break;
                }
                case ASYNC_WOKEN: {
                    dispatch = false;
                    break;
                }
                default: {
                    HttpChannelState.LOG.warn("async dispatched when complete {}", this);
                    dispatch = false;
                    break;
                }
            }
        }
        this.cancelTimeout();
        if (dispatch) {
            this.scheduleDispatch();
        }
    }
    
    protected void expired() {
        final AsyncContextEvent event;
        final List<AsyncListener> aListeners;
        synchronized (this) {
            if (this._async != Async.STARTED) {
                return;
            }
            this._async = Async.EXPIRING;
            event = this._event;
            aListeners = this._asyncListeners;
        }
        if (aListeners != null) {
            for (final AsyncListener listener : aListeners) {
                try {
                    listener.onTimeout(event);
                }
                catch (Exception e) {
                    HttpChannelState.LOG.debug(e);
                    event.setThrowable(e);
                    this._channel.getRequest().setAttribute("javax.servlet.error.exception", e);
                    break;
                }
            }
        }
        boolean dispatch = false;
        synchronized (this) {
            if (this._async == Async.EXPIRING) {
                this._async = Async.EXPIRED;
                if (this._state == State.ASYNC_WAIT) {
                    this._state = State.ASYNC_WOKEN;
                    dispatch = true;
                }
            }
        }
        if (dispatch) {
            this.scheduleDispatch();
        }
    }
    
    public void complete() {
        boolean handle = false;
        synchronized (this) {
            if (this._async != Async.STARTED && this._async != Async.EXPIRING) {
                throw new IllegalStateException(this.getStatusString());
            }
            this._async = Async.COMPLETE;
            if (this._state == State.ASYNC_WAIT) {
                handle = true;
                this._state = State.ASYNC_WOKEN;
            }
        }
        this.cancelTimeout();
        if (handle) {
            final ContextHandler handler = this.getContextHandler();
            if (handler != null) {
                handler.handle(this._channel);
            }
            else {
                this._channel.handle();
            }
        }
    }
    
    public void errorComplete() {
        synchronized (this) {
            this._async = Async.COMPLETE;
            this._event.setDispatchContext(null);
            this._event.setDispatchPath(null);
        }
        this.cancelTimeout();
    }
    
    protected void completed() {
        final List<AsyncListener> aListeners;
        final AsyncContextEvent event;
        synchronized (this) {
            switch (this._state) {
                case COMPLETING: {
                    this._state = State.COMPLETED;
                    aListeners = this._asyncListeners;
                    event = this._event;
                    break;
                }
                default: {
                    throw new IllegalStateException(this.getStatusString());
                }
            }
        }
        if (event != null) {
            if (aListeners != null) {
                if (event.getThrowable() != null) {
                    event.getSuppliedRequest().setAttribute("javax.servlet.error.exception", event.getThrowable());
                    event.getSuppliedRequest().setAttribute("javax.servlet.error.message", event.getThrowable().getMessage());
                }
                for (final AsyncListener listener : aListeners) {
                    try {
                        if (event.getThrowable() != null) {
                            listener.onError(event);
                        }
                        else {
                            listener.onComplete(event);
                        }
                    }
                    catch (Exception e) {
                        HttpChannelState.LOG.warn(e);
                    }
                }
            }
            event.completed();
        }
    }
    
    protected void recycle() {
        synchronized (this) {
            switch (this._state) {
                case DISPATCHED:
                case ASYNC_IO: {
                    throw new IllegalStateException(this.getStatusString());
                }
                case UPGRADED: {}
                default: {
                    this._asyncListeners = null;
                    this._state = State.IDLE;
                    this._async = null;
                    this._initial = true;
                    this._asyncRead = false;
                    this._asyncWrite = false;
                    this._timeoutMs = HttpChannelState.DEFAULT_TIMEOUT;
                    this.cancelTimeout();
                    this._event = null;
                    break;
                }
            }
        }
    }
    
    public void upgrade() {
        synchronized (this) {
            switch (this._state) {
                case IDLE:
                case COMPLETED: {
                    this._asyncListeners = null;
                    this._state = State.UPGRADED;
                    this._async = null;
                    this._initial = true;
                    this._asyncRead = false;
                    this._asyncWrite = false;
                    this._timeoutMs = HttpChannelState.DEFAULT_TIMEOUT;
                    this.cancelTimeout();
                    this._event = null;
                    break;
                }
                default: {
                    throw new IllegalStateException(this.getStatusString());
                }
            }
        }
    }
    
    protected void scheduleDispatch() {
        this._channel.execute(this._channel);
    }
    
    protected void scheduleTimeout() {
        final Scheduler scheduler = this._channel.getScheduler();
        if (scheduler != null && this._timeoutMs > 0L) {
            this._event.setTimeoutTask(scheduler.schedule(this._event, this._timeoutMs, TimeUnit.MILLISECONDS));
        }
    }
    
    protected void cancelTimeout() {
        final AsyncContextEvent event;
        synchronized (this) {
            event = this._event;
        }
        if (event != null) {
            event.cancelTimeoutTask();
        }
    }
    
    public boolean isExpired() {
        synchronized (this) {
            return this._async == Async.EXPIRED;
        }
    }
    
    public boolean isInitial() {
        synchronized (this) {
            return this._initial;
        }
    }
    
    public boolean isSuspended() {
        synchronized (this) {
            return this._state == State.ASYNC_WAIT || (this._state == State.DISPATCHED && this._async == Async.STARTED);
        }
    }
    
    boolean isCompleting() {
        synchronized (this) {
            return this._state == State.COMPLETING;
        }
    }
    
    boolean isCompleted() {
        synchronized (this) {
            return this._state == State.COMPLETED;
        }
    }
    
    public boolean isAsyncStarted() {
        synchronized (this) {
            if (this._state == State.DISPATCHED) {
                return this._async != null;
            }
            return this._async == Async.STARTED || this._async == Async.EXPIRING;
        }
    }
    
    public boolean isAsync() {
        synchronized (this) {
            return !this._initial || this._async != null;
        }
    }
    
    public Request getBaseRequest() {
        return this._channel.getRequest();
    }
    
    public HttpChannel<?> getHttpChannel() {
        return this._channel;
    }
    
    public ContextHandler getContextHandler() {
        final AsyncContextEvent event;
        synchronized (this) {
            event = this._event;
        }
        if (event != null) {
            final ContextHandler.Context context = (ContextHandler.Context)event.getServletContext();
            if (context != null) {
                return context.getContextHandler();
            }
        }
        return null;
    }
    
    public ServletResponse getServletResponse() {
        final AsyncContextEvent event;
        synchronized (this) {
            event = this._event;
        }
        if (event != null && event.getSuppliedResponse() != null) {
            return event.getSuppliedResponse();
        }
        return this._channel.getResponse();
    }
    
    public Object getAttribute(final String name) {
        return this._channel.getRequest().getAttribute(name);
    }
    
    public void removeAttribute(final String name) {
        this._channel.getRequest().removeAttribute(name);
    }
    
    public void setAttribute(final String name, final Object attribute) {
        this._channel.getRequest().setAttribute(name, attribute);
    }
    
    public void onReadPossible() {
        boolean handle = false;
        synchronized (this) {
            this._asyncRead = true;
            if (this._state == State.ASYNC_WAIT) {
                this._state = State.ASYNC_WOKEN;
                handle = true;
            }
        }
        if (handle) {
            this._channel.execute(this._channel);
        }
    }
    
    public void onWritePossible() {
        boolean handle = false;
        synchronized (this) {
            this._asyncWrite = true;
            if (this._state == State.ASYNC_WAIT) {
                this._state = State.ASYNC_WOKEN;
                handle = true;
            }
        }
        if (handle) {
            this._channel.execute(this._channel);
        }
    }
    
    static {
        LOG = Log.getLogger(HttpChannelState.class);
        DEFAULT_TIMEOUT = Long.getLong("org.eclipse.jetty.server.HttpChannelState.DEFAULT_TIMEOUT", 30000L);
    }
    
    public enum State
    {
        IDLE, 
        DISPATCHED, 
        ASYNC_WAIT, 
        ASYNC_WOKEN, 
        ASYNC_IO, 
        COMPLETING, 
        COMPLETED, 
        UPGRADED;
    }
    
    public enum Action
    {
        REQUEST_DISPATCH, 
        ASYNC_DISPATCH, 
        ASYNC_EXPIRED, 
        WRITE_CALLBACK, 
        READ_CALLBACK, 
        WAIT, 
        COMPLETE;
    }
    
    public enum Async
    {
        STARTED, 
        DISPATCH, 
        COMPLETE, 
        EXPIRING, 
        EXPIRED;
    }
}
