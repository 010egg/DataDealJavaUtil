// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import org.eclipse.jetty.util.log.Log;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.RejectedExecutionException;
import org.eclipse.jetty.util.thread.NonBlockingThread;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.jetty.util.Callback;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.List;
import org.eclipse.jetty.util.log.Logger;

public abstract class AbstractConnection implements Connection
{
    private static final Logger LOG;
    public static final boolean EXECUTE_ONFILLABLE = true;
    private final List<Listener> listeners;
    private final AtomicReference<State> _state;
    private final long _created;
    private final EndPoint _endPoint;
    private final Executor _executor;
    private final Callback _readCallback;
    private final boolean _executeOnfillable;
    private int _inputBufferSize;
    public static final State IDLE;
    public static final State FILL_INTERESTED;
    public static final State FILLING;
    public static final State REFILLING;
    public static final State FILLING_FILL_INTERESTED;
    private final Runnable _runOnFillable;
    
    protected AbstractConnection(final EndPoint endp, final Executor executor) {
        this(endp, executor, true);
    }
    
    protected AbstractConnection(final EndPoint endp, final Executor executor, final boolean executeOnfillable) {
        this.listeners = new CopyOnWriteArrayList<Listener>();
        this._state = new AtomicReference<State>(AbstractConnection.IDLE);
        this._created = System.currentTimeMillis();
        this._inputBufferSize = 2048;
        this._runOnFillable = new Runnable() {
            @Override
            public void run() {
                try {
                    AbstractConnection.this.onFillable();
                }
                finally {
                    State state;
                    do {
                        state = AbstractConnection.this._state.get();
                    } while (!AbstractConnection.this.next(state, state.onFilled()));
                }
            }
        };
        if (executor == null) {
            throw new IllegalArgumentException("Executor must not be null!");
        }
        this._endPoint = endp;
        this._executor = executor;
        this._readCallback = new ReadCallback();
        this._executeOnfillable = executeOnfillable;
        this._state.set(AbstractConnection.IDLE);
    }
    
    @Override
    public void addListener(final Listener listener) {
        this.listeners.add(listener);
    }
    
    public int getInputBufferSize() {
        return this._inputBufferSize;
    }
    
    public void setInputBufferSize(final int inputBufferSize) {
        this._inputBufferSize = inputBufferSize;
    }
    
    protected Executor getExecutor() {
        return this._executor;
    }
    
    protected void failedCallback(final Callback callback, final Throwable x) {
        if (NonBlockingThread.isNonBlockingThread()) {
            try {
                this.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.failed(x);
                    }
                });
            }
            catch (RejectedExecutionException e) {
                AbstractConnection.LOG.debug(e);
                callback.failed(x);
            }
        }
        else {
            callback.failed(x);
        }
    }
    
    public void fillInterested() {
        if (AbstractConnection.LOG.isDebugEnabled()) {
            AbstractConnection.LOG.debug("fillInterested {}", this);
        }
        State state;
        do {
            state = this._state.get();
        } while (!this.next(state, state.fillInterested()));
    }
    
    public void fillInterested(final Callback callback) {
        if (AbstractConnection.LOG.isDebugEnabled()) {
            AbstractConnection.LOG.debug("fillInterested {}", this);
        }
        State state;
        State next;
        do {
            state = this._state.get();
            if (state instanceof FillingInterestedCallback && ((FillingInterestedCallback)state)._callback == callback) {
                break;
            }
            next = new FillingInterestedCallback(callback, state);
        } while (!this.next(state, next));
    }
    
    public abstract void onFillable();
    
    protected void onFillInterestedFailed(final Throwable cause) {
        if (AbstractConnection.LOG.isDebugEnabled()) {
            AbstractConnection.LOG.debug("{} onFillInterestedFailed {}", this, cause);
        }
        if (this._endPoint.isOpen()) {
            boolean close = true;
            if (cause instanceof TimeoutException) {
                close = this.onReadTimeout();
            }
            if (close) {
                if (this._endPoint.isOutputShutdown()) {
                    this._endPoint.close();
                }
                else {
                    this._endPoint.shutdownOutput();
                }
            }
        }
        if (this._endPoint.isOpen()) {
            this.fillInterested();
        }
    }
    
    protected boolean onReadTimeout() {
        return true;
    }
    
    @Override
    public void onOpen() {
        if (AbstractConnection.LOG.isDebugEnabled()) {
            AbstractConnection.LOG.debug("onOpen {}", this);
        }
        for (final Listener listener : this.listeners) {
            listener.onOpened(this);
        }
    }
    
    @Override
    public void onClose() {
        if (AbstractConnection.LOG.isDebugEnabled()) {
            AbstractConnection.LOG.debug("onClose {}", this);
        }
        for (final Listener listener : this.listeners) {
            listener.onClosed(this);
        }
    }
    
    @Override
    public EndPoint getEndPoint() {
        return this._endPoint;
    }
    
    @Override
    public void close() {
        this.getEndPoint().close();
    }
    
    @Override
    public int getMessagesIn() {
        return -1;
    }
    
    @Override
    public int getMessagesOut() {
        return -1;
    }
    
    @Override
    public long getBytesIn() {
        return -1L;
    }
    
    @Override
    public long getBytesOut() {
        return -1L;
    }
    
    @Override
    public long getCreatedTimeStamp() {
        return this._created;
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x[%s,%s]", this.getClass().getSimpleName(), this.hashCode(), this._state.get(), this._endPoint);
    }
    
    public boolean next(final State state, final State next) {
        if (next == null) {
            return true;
        }
        if (this._state.compareAndSet(state, next)) {
            if (AbstractConnection.LOG.isDebugEnabled()) {
                AbstractConnection.LOG.debug("{}-->{} {}", state, next, this);
            }
            if (next != state) {
                next.onEnter(this);
            }
            return true;
        }
        return false;
    }
    
    static {
        LOG = Log.getLogger(AbstractConnection.class);
        IDLE = new IdleState();
        FILL_INTERESTED = new FillInterestedState();
        FILLING = new FillingState();
        REFILLING = new RefillingState();
        FILLING_FILL_INTERESTED = new FillingFillInterestedState("FILLING_FILL_INTERESTED");
    }
    
    private static final class IdleState extends State
    {
        private IdleState() {
            super("IDLE");
        }
        
        @Override
        State fillInterested() {
            return AbstractConnection.FILL_INTERESTED;
        }
    }
    
    private static final class FillInterestedState extends State
    {
        private FillInterestedState() {
            super("FILL_INTERESTED");
        }
        
        public void onEnter(final AbstractConnection connection) {
            connection.getEndPoint().fillInterested(connection._readCallback);
        }
        
        @Override
        State fillInterested() {
            return this;
        }
        
        public State onFillable() {
            return AbstractConnection.FILLING;
        }
        
        @Override
        State onFailed() {
            return AbstractConnection.IDLE;
        }
    }
    
    private static final class RefillingState extends State
    {
        private RefillingState() {
            super("REFILLING");
        }
        
        @Override
        State fillInterested() {
            return AbstractConnection.FILLING_FILL_INTERESTED;
        }
        
        public State onFilled() {
            return AbstractConnection.IDLE;
        }
    }
    
    private static final class FillingFillInterestedState extends State
    {
        private FillingFillInterestedState(final String name) {
            super(name);
        }
        
        @Override
        State fillInterested() {
            return this;
        }
        
        @Override
        State onFilled() {
            return AbstractConnection.FILL_INTERESTED;
        }
    }
    
    private static final class FillingState extends State
    {
        private FillingState() {
            super("FILLING");
        }
        
        public void onEnter(final AbstractConnection connection) {
            if (connection._executeOnfillable) {
                connection.getExecutor().execute(connection._runOnFillable);
            }
            else {
                connection._runOnFillable.run();
            }
        }
        
        @Override
        State fillInterested() {
            return AbstractConnection.FILLING_FILL_INTERESTED;
        }
        
        public State onFilled() {
            return AbstractConnection.IDLE;
        }
    }
    
    public static class State
    {
        private final String _name;
        
        State(final String name) {
            this._name = name;
        }
        
        @Override
        public String toString() {
            return this._name;
        }
        
        void onEnter(final AbstractConnection connection) {
        }
        
        State fillInterested() {
            throw new IllegalStateException(this.toString());
        }
        
        State onFillable() {
            throw new IllegalStateException(this.toString());
        }
        
        State onFilled() {
            throw new IllegalStateException(this.toString());
        }
        
        State onFailed() {
            throw new IllegalStateException(this.toString());
        }
    }
    
    public class NestedState extends State
    {
        private final State _nested;
        
        NestedState(final State nested) {
            super("NESTED(" + nested + ")");
            this._nested = nested;
        }
        
        NestedState(final String name, final State nested) {
            super(name + "(" + nested + ")");
            this._nested = nested;
        }
        
        @Override
        State fillInterested() {
            return new NestedState(this._nested.fillInterested());
        }
        
        @Override
        State onFillable() {
            return new NestedState(this._nested.onFillable());
        }
        
        @Override
        State onFilled() {
            return new NestedState(this._nested.onFilled());
        }
    }
    
    public class FillingInterestedCallback extends NestedState
    {
        private final Callback _callback;
        
        FillingInterestedCallback(final Callback callback, final State nested) {
            super("FILLING_INTERESTED_CALLBACK", (nested == AbstractConnection.FILLING) ? AbstractConnection.REFILLING : nested);
            this._callback = callback;
        }
        
        @Override
        void onEnter(final AbstractConnection connection) {
            final Callback callback = new Callback() {
                @Override
                public void succeeded() {
                    State state;
                    State nested;
                    do {
                        state = connection._state.get();
                        if (!(state instanceof NestedState)) {
                            break;
                        }
                        nested = ((NestedState)state)._nested;
                    } while (!connection.next(state, nested));
                    FillingInterestedCallback.this._callback.succeeded();
                }
                
                @Override
                public void failed(final Throwable x) {
                    State state;
                    State nested;
                    do {
                        state = connection._state.get();
                        if (!(state instanceof NestedState)) {
                            break;
                        }
                        nested = ((NestedState)state)._nested;
                    } while (!connection.next(state, nested));
                    FillingInterestedCallback.this._callback.failed(x);
                }
            };
            connection.getEndPoint().fillInterested(callback);
        }
    }
    
    private class ReadCallback implements Callback
    {
        @Override
        public void succeeded() {
            State state;
            do {
                state = AbstractConnection.this._state.get();
            } while (!AbstractConnection.this.next(state, state.onFillable()));
        }
        
        @Override
        public void failed(final Throwable x) {
            AbstractConnection.this._executor.execute(new Runnable() {
                @Override
                public void run() {
                    State state;
                    do {
                        state = AbstractConnection.this._state.get();
                    } while (!AbstractConnection.this.next(state, state.onFailed()));
                    AbstractConnection.this.onFillInterestedFailed(x);
                }
            });
        }
        
        @Override
        public String toString() {
            return String.format("AC.ReadCB@%x{%s}", AbstractConnection.this.hashCode(), AbstractConnection.this);
        }
    }
}
