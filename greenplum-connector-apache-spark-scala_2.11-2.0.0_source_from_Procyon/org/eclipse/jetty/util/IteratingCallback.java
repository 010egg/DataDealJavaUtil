// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class IteratingCallback implements Callback
{
    private final AtomicReference<State> _state;
    private boolean _iterate;
    
    protected IteratingCallback() {
        this._state = new AtomicReference<State>(State.IDLE);
    }
    
    protected IteratingCallback(final boolean needReset) {
        this._state = new AtomicReference<State>(needReset ? State.SUCCEEDED : State.IDLE);
    }
    
    protected abstract Action process() throws Exception;
    
    @Deprecated
    protected void completed() {
    }
    
    protected void onCompleteSuccess() {
        this.completed();
    }
    
    protected void onCompleteFailure(final Throwable x) {
    }
    
    public void iterate() {
    Label_0162:
        while (true) {
            final State state = this._state.get();
            switch (state) {
                case PENDING:
                case CALLED: {
                    break Label_0162;
                }
                case IDLE: {
                    if (!this._state.compareAndSet(state, State.PROCESSING)) {
                        continue;
                    }
                    this.processing();
                    break Label_0162;
                }
                case PROCESSING: {
                    if (!this._state.compareAndSet(state, State.LOCKED)) {
                        continue;
                    }
                    this._iterate = true;
                    this._state.set(State.PROCESSING);
                    break Label_0162;
                }
                case LOCKED: {
                    Thread.yield();
                    continue;
                }
                case FAILED:
                case SUCCEEDED: {
                    break Label_0162;
                }
                default: {
                    throw new IllegalStateException("state=" + state);
                }
            }
        }
    }
    
    private void processing() {
    Label_0000_Outer:
        while (true) {
        Label_0000:
            while (true) {
                Action action;
                try {
                    action = this.process();
                }
                catch (Throwable x) {
                    this.failed(x);
                    break;
                }
                while (true) {
                    final State state = this._state.get();
                    switch (state) {
                        case PROCESSING: {
                            switch (action) {
                                case IDLE: {
                                    if (!this._state.compareAndSet(state, State.LOCKED)) {
                                        continue Label_0000_Outer;
                                    }
                                    if (this._iterate) {
                                        this._iterate = false;
                                        this._state.set(State.PROCESSING);
                                        continue Label_0000;
                                    }
                                    this._state.set(State.IDLE);
                                    break Label_0000_Outer;
                                }
                                case SCHEDULED: {
                                    if (!this._state.compareAndSet(state, State.PENDING)) {
                                        continue Label_0000_Outer;
                                    }
                                    break Label_0000_Outer;
                                }
                                case SUCCEEDED: {
                                    if (!this._state.compareAndSet(state, State.LOCKED)) {
                                        continue Label_0000_Outer;
                                    }
                                    this._iterate = false;
                                    this._state.set(State.SUCCEEDED);
                                    this.onCompleteSuccess();
                                    break Label_0000_Outer;
                                }
                                default: {
                                    throw new IllegalStateException("state=" + state + " action=" + action);
                                }
                            }
                            break;
                        }
                        case CALLED: {
                            switch (action) {
                                case SCHEDULED: {
                                    if (!this._state.compareAndSet(state, State.PROCESSING)) {
                                        continue Label_0000_Outer;
                                    }
                                    continue Label_0000;
                                }
                                default: {
                                    throw new IllegalStateException("state=" + state + " action=" + action);
                                }
                            }
                            break;
                        }
                        case LOCKED: {
                            Thread.yield();
                            continue Label_0000_Outer;
                        }
                        case FAILED:
                        case SUCCEEDED:
                        case CLOSED: {
                            break Label_0000_Outer;
                        }
                        default: {
                            throw new IllegalStateException("state=" + state + " action=" + action);
                        }
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public void succeeded() {
    Label_0141:
        while (true) {
            final State state = this._state.get();
            switch (state) {
                case PROCESSING: {
                    if (!this._state.compareAndSet(state, State.CALLED)) {
                        continue;
                    }
                    break Label_0141;
                }
                case PENDING: {
                    if (!this._state.compareAndSet(state, State.PROCESSING)) {
                        continue;
                    }
                    this.processing();
                    break Label_0141;
                }
                case FAILED:
                case CLOSED: {
                    break Label_0141;
                }
                case LOCKED: {
                    Thread.yield();
                    continue;
                }
                default: {
                    throw new IllegalStateException("state=" + state);
                }
            }
        }
    }
    
    @Override
    public void failed(final Throwable x) {
    Label_0125:
        while (true) {
            final State state = this._state.get();
            switch (state) {
                case CALLED:
                case IDLE:
                case FAILED:
                case SUCCEEDED:
                case CLOSED: {
                    break Label_0125;
                }
                case LOCKED: {
                    Thread.yield();
                    continue;
                }
                case PENDING:
                case PROCESSING: {
                    if (!this._state.compareAndSet(state, State.FAILED)) {
                        continue;
                    }
                    this.onCompleteFailure(x);
                    break Label_0125;
                }
                default: {
                    throw new IllegalStateException("state=" + state);
                }
            }
        }
    }
    
    public void close() {
    Label_0113:
        while (true) {
            final State state = this._state.get();
            switch (state) {
                case IDLE:
                case FAILED:
                case SUCCEEDED: {
                    if (!this._state.compareAndSet(state, State.CLOSED)) {
                        continue;
                    }
                    break Label_0113;
                }
                case CLOSED: {
                    break Label_0113;
                }
                case LOCKED: {
                    Thread.yield();
                    continue;
                }
                default: {
                    if (!this._state.compareAndSet(state, State.CLOSED)) {
                        continue;
                    }
                    this.onCompleteFailure(new ClosedChannelException());
                    break Label_0113;
                }
            }
        }
    }
    
    boolean isIdle() {
        return this._state.get() == State.IDLE;
    }
    
    public boolean isClosed() {
        return this._state.get() == State.CLOSED;
    }
    
    public boolean isFailed() {
        return this._state.get() == State.FAILED;
    }
    
    public boolean isSucceeded() {
        return this._state.get() == State.SUCCEEDED;
    }
    
    public boolean reset() {
        while (true) {
            final State state = this._state.get();
            switch (state) {
                case IDLE: {
                    return true;
                }
                case SUCCEEDED: {
                    if (!this._state.compareAndSet(state, State.LOCKED)) {
                        continue;
                    }
                    this._iterate = false;
                    this._state.set(State.IDLE);
                    return true;
                }
                case FAILED: {
                    if (!this._state.compareAndSet(state, State.LOCKED)) {
                        continue;
                    }
                    this._iterate = false;
                    this._state.set(State.IDLE);
                    return true;
                }
                case LOCKED: {
                    Thread.yield();
                    continue;
                }
                default: {
                    return false;
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s[%s]", super.toString(), this._state);
    }
    
    private enum State
    {
        IDLE, 
        PROCESSING, 
        PENDING, 
        CALLED, 
        SUCCEEDED, 
        FAILED, 
        CLOSED, 
        LOCKED;
    }
    
    protected enum Action
    {
        IDLE, 
        SCHEDULED, 
        SUCCEEDED;
    }
}
