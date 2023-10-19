// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.net.SocketTimeoutException;
import java.net.ConnectException;
import java.nio.channels.CancelledKeyException;
import java.io.Closeable;
import java.util.Iterator;
import java.util.Set;
import org.eclipse.jetty.util.ConcurrentArrayQueue;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.TypeUtil;
import java.util.Collection;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import java.nio.channels.SelectionKey;
import org.eclipse.jetty.util.thread.NonBlockingThread;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.thread.Scheduler;
import java.util.concurrent.Executor;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.component.Dumpable;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

public abstract class SelectorManager extends AbstractLifeCycle implements Dumpable
{
    public static final String SUBMIT_KEY_UPDATES = "org.eclipse.jetty.io.SelectorManager.submitKeyUpdates";
    public static final int DEFAULT_CONNECT_TIMEOUT = 15000;
    protected static final Logger LOG;
    private static final boolean __submitKeyUpdates;
    private final Executor executor;
    private final Scheduler scheduler;
    private final ManagedSelector[] _selectors;
    private long _connectTimeout;
    private long _selectorIndex;
    private int _priorityDelta;
    
    protected SelectorManager(final Executor executor, final Scheduler scheduler) {
        this(executor, scheduler, (Runtime.getRuntime().availableProcessors() + 1) / 2);
    }
    
    protected SelectorManager(final Executor executor, final Scheduler scheduler, final int selectors) {
        this._connectTimeout = 15000L;
        if (selectors <= 0) {
            throw new IllegalArgumentException("No selectors");
        }
        this.executor = executor;
        this.scheduler = scheduler;
        this._selectors = new ManagedSelector[selectors];
    }
    
    public Executor getExecutor() {
        return this.executor;
    }
    
    public Scheduler getScheduler() {
        return this.scheduler;
    }
    
    public long getConnectTimeout() {
        return this._connectTimeout;
    }
    
    public void setConnectTimeout(final long milliseconds) {
        this._connectTimeout = milliseconds;
    }
    
    @ManagedAttribute("The priority delta to apply to selector threads")
    public int getSelectorPriorityDelta() {
        return this._priorityDelta;
    }
    
    public void setSelectorPriorityDelta(final int selectorPriorityDelta) {
        final int oldDelta = this._priorityDelta;
        this._priorityDelta = selectorPriorityDelta;
        if (oldDelta != selectorPriorityDelta && this.isStarted()) {
            for (final ManagedSelector selector : this._selectors) {
                final Thread thread = selector._thread;
                if (thread != null) {
                    final int deltaDiff = selectorPriorityDelta - oldDelta;
                    thread.setPriority(Math.max(1, Math.min(10, thread.getPriority() - deltaDiff)));
                }
            }
        }
    }
    
    protected void execute(final Runnable task) {
        this.executor.execute(task);
    }
    
    public int getSelectorCount() {
        return this._selectors.length;
    }
    
    private ManagedSelector chooseSelector() {
        final long s = this._selectorIndex++;
        final int index = (int)(s % this.getSelectorCount());
        return this._selectors[index];
    }
    
    public void connect(final SocketChannel channel, final Object attachment) {
        final ManagedSelector set = this.chooseSelector();
        set.submit(new ManagedSelector.Connect(channel, attachment));
    }
    
    public void accept(final SocketChannel channel) {
        this.accept(channel, null);
    }
    
    public void accept(final SocketChannel channel, final Object attachment) {
        final ManagedSelector selector = this.chooseSelector();
        selector.submit(new ManagedSelector.Accept(channel, attachment));
    }
    
    public void acceptor(final ServerSocketChannel server) {
        final ManagedSelector selector = this.chooseSelector();
        selector.submit(selector.new Acceptor(server));
    }
    
    protected void accepted(final SocketChannel channel) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
        for (int i = 0; i < this._selectors.length; ++i) {
            final ManagedSelector selector = this.newSelector(i);
            (this._selectors[i] = selector).start();
            this.execute(new NonBlockingThread(selector));
        }
    }
    
    protected ManagedSelector newSelector(final int id) {
        return new ManagedSelector(id);
    }
    
    @Override
    protected void doStop() throws Exception {
        for (final ManagedSelector selector : this._selectors) {
            selector.stop();
        }
        super.doStop();
    }
    
    protected void endPointOpened(final EndPoint endpoint) {
        endpoint.onOpen();
    }
    
    protected void endPointClosed(final EndPoint endpoint) {
        endpoint.onClose();
    }
    
    public void connectionOpened(final Connection connection) {
        try {
            connection.onOpen();
        }
        catch (Throwable x) {
            if (this.isRunning()) {
                SelectorManager.LOG.warn("Exception while notifying connection " + connection, x);
            }
            else {
                SelectorManager.LOG.debug("Exception while notifying connection " + connection, x);
            }
            throw x;
        }
    }
    
    public void connectionClosed(final Connection connection) {
        try {
            connection.onClose();
        }
        catch (Throwable x) {
            SelectorManager.LOG.debug("Exception while notifying connection " + connection, x);
        }
    }
    
    protected boolean finishConnect(final SocketChannel channel) throws IOException {
        return channel.finishConnect();
    }
    
    protected void connectionFailed(final SocketChannel channel, final Throwable ex, final Object attachment) {
        SelectorManager.LOG.warn(String.format("%s - %s", channel, attachment), ex);
    }
    
    protected abstract EndPoint newEndPoint(final SocketChannel p0, final ManagedSelector p1, final SelectionKey p2) throws IOException;
    
    public abstract Connection newConnection(final SocketChannel p0, final EndPoint p1, final Object p2) throws IOException;
    
    @Override
    public String dump() {
        return ContainerLifeCycle.dump(this);
    }
    
    @Override
    public void dump(final Appendable out, final String indent) throws IOException {
        ContainerLifeCycle.dumpObject(out, this);
        ContainerLifeCycle.dump(out, indent, TypeUtil.asList(this._selectors));
    }
    
    static {
        LOG = Log.getLogger(SelectorManager.class);
        __submitKeyUpdates = Boolean.valueOf(System.getProperty("org.eclipse.jetty.io.SelectorManager.submitKeyUpdates", "true"));
    }
    
    private enum State
    {
        CHANGES, 
        MORE_CHANGES, 
        SELECT, 
        WAKEUP, 
        PROCESS;
    }
    
    public class ManagedSelector extends AbstractLifeCycle implements Runnable, Dumpable
    {
        private final AtomicReference<State> _state;
        private final Queue<Runnable> _changes;
        private final int _id;
        private Selector _selector;
        private volatile Thread _thread;
        final /* synthetic */ SelectorManager this$0;
        
        public ManagedSelector(final int id) {
            this._state = new AtomicReference<State>(State.PROCESS);
            this._changes = new ConcurrentArrayQueue<Runnable>();
            this._id = id;
            this.setStopTimeout(5000L);
        }
        
        @Override
        protected void doStart() throws Exception {
            super.doStart();
            this._selector = Selector.open();
            this._state.set(State.PROCESS);
        }
        
        @Override
        protected void doStop() throws Exception {
            if (SelectorManager.LOG.isDebugEnabled()) {
                SelectorManager.LOG.debug("Stopping {}", this);
            }
            final Stop stop = new Stop();
            this.submit(stop);
            stop.await(this.getStopTimeout());
            if (SelectorManager.LOG.isDebugEnabled()) {
                SelectorManager.LOG.debug("Stopped {}", this);
            }
        }
        
        public void updateKey(final Runnable update) {
            if (SelectorManager.__submitKeyUpdates) {
                this.submit(update);
            }
            else {
                synchronized (this) {
                    this.runChange(update);
                }
                if (this._state.compareAndSet(State.SELECT, State.WAKEUP)) {
                    this.wakeup();
                }
            }
        }
        
        public void submit(final Runnable change) {
            this._changes.offer(change);
            if (SelectorManager.LOG.isDebugEnabled()) {
                SelectorManager.LOG.debug("Queued change {}", change);
            }
        Label_0154:
            while (true) {
                switch (this._state.get()) {
                    case SELECT: {
                        if (!this._state.compareAndSet(State.SELECT, State.WAKEUP)) {
                            continue;
                        }
                        this.wakeup();
                        break Label_0154;
                    }
                    case CHANGES: {
                        if (this._state.compareAndSet(State.CHANGES, State.MORE_CHANGES)) {
                            break Label_0154;
                        }
                        continue;
                    }
                    case WAKEUP: {
                        break Label_0154;
                    }
                    case MORE_CHANGES: {
                        break Label_0154;
                    }
                    case PROCESS: {
                        break Label_0154;
                    }
                    default: {
                        throw new IllegalStateException();
                    }
                }
            }
        }
        
        private void runChanges() {
            Runnable change;
            while ((change = this._changes.poll()) != null) {
                this.runChange(change);
            }
        }
        
        protected void runChange(final Runnable change) {
            try {
                if (SelectorManager.LOG.isDebugEnabled()) {
                    SelectorManager.LOG.debug("Running change {}", change);
                }
                change.run();
            }
            catch (Throwable x) {
                SelectorManager.LOG.debug("Could not run change " + change, x);
            }
        }
        
        @Override
        public void run() {
            this._thread = Thread.currentThread();
            final String name = this._thread.getName();
            final int priority = this._thread.getPriority();
            try {
                if (SelectorManager.this._priorityDelta != 0) {
                    this._thread.setPriority(Math.max(1, Math.min(10, priority + SelectorManager.this._priorityDelta)));
                }
                this._thread.setName(String.format("%s-selector-%s@%h/%d", name, SelectorManager.this.getClass().getSimpleName(), SelectorManager.this.hashCode(), this._id));
                if (SelectorManager.LOG.isDebugEnabled()) {
                    SelectorManager.LOG.debug("Starting {} on {}", this._thread, this);
                }
                while (this.isRunning()) {
                    this.select();
                }
                while (this.isStopping()) {
                    this.runChanges();
                }
            }
            finally {
                if (SelectorManager.LOG.isDebugEnabled()) {
                    SelectorManager.LOG.debug("Stopped {} on {}", this._thread, this);
                }
                this._thread.setName(name);
                if (SelectorManager.this._priorityDelta != 0) {
                    this._thread.setPriority(priority);
                }
            }
        }
        
        public void select() {
            final boolean debug = SelectorManager.LOG.isDebugEnabled();
            Label_0378: {
                try {
                    this._state.set(State.CHANGES);
                    while (true) {
                        switch (this._state.get()) {
                            case CHANGES: {
                                this.runChanges();
                                if (!this._state.compareAndSet(State.CHANGES, State.SELECT)) {
                                    continue;
                                }
                                assert this._state.get() == State.WAKEUP;
                                if (debug) {
                                    SelectorManager.LOG.debug("Selector loop waiting on select", new Object[0]);
                                }
                                final int selected = this._selector.select();
                                if (debug) {
                                    SelectorManager.LOG.debug("Selector loop woken up from select, {}/{} selected", selected, this._selector.keys().size());
                                }
                                this._state.set(State.PROCESS);
                                final Set<SelectionKey> selectedKeys = this._selector.selectedKeys();
                                for (final SelectionKey key : selectedKeys) {
                                    if (key.isValid()) {
                                        this.processKey(key);
                                    }
                                    else {
                                        if (debug) {
                                            SelectorManager.LOG.debug("Selector loop ignoring invalid key for channel {}", key.channel());
                                        }
                                        final Object attachment = key.attachment();
                                        if (!(attachment instanceof EndPoint)) {
                                            continue;
                                        }
                                        ((EndPoint)attachment).close();
                                    }
                                }
                                selectedKeys.clear();
                                break Label_0378;
                            }
                            case MORE_CHANGES: {
                                this.runChanges();
                                this._state.set(State.CHANGES);
                                continue;
                            }
                            default: {
                                throw new IllegalStateException();
                            }
                        }
                    }
                }
                catch (Throwable x) {
                    if (this.isRunning()) {
                        SelectorManager.LOG.warn(x);
                    }
                    else {
                        SelectorManager.LOG.ignore(x);
                    }
                }
            }
        }
        
        private void processKey(final SelectionKey key) {
            final Object attachment = key.attachment();
            try {
                if (attachment instanceof SelectableEndPoint) {
                    ((SelectableEndPoint)attachment).onSelected();
                }
                else if (key.isConnectable()) {
                    this.processConnect(key, (Connect)attachment);
                }
                else {
                    if (!key.isAcceptable()) {
                        throw new IllegalStateException();
                    }
                    this.processAccept(key);
                }
            }
            catch (CancelledKeyException x2) {
                SelectorManager.LOG.debug("Ignoring cancelled key for channel {}", key.channel());
                if (attachment instanceof EndPoint) {
                    this.closeNoExceptions((Closeable)attachment);
                }
            }
            catch (Throwable x) {
                SelectorManager.LOG.warn("Could not process key for channel " + key.channel(), x);
                if (attachment instanceof EndPoint) {
                    this.closeNoExceptions((Closeable)attachment);
                }
            }
        }
        
        private void processConnect(final SelectionKey key, final Connect connect) {
            final SocketChannel channel = (SocketChannel)key.channel();
            try {
                key.attach(connect.attachment);
                final boolean connected = SelectorManager.this.finishConnect(channel);
                if (!connected) {
                    throw new ConnectException();
                }
                if (!connect.timeout.cancel()) {
                    throw new SocketTimeoutException("Concurrent Connect Timeout");
                }
                key.interestOps(0);
                final EndPoint endpoint = this.createEndPoint(channel, key);
                key.attach(endpoint);
            }
            catch (Throwable x) {
                connect.failed(x);
            }
        }
        
        private void processAccept(final SelectionKey key) {
            final ServerSocketChannel server = (ServerSocketChannel)key.channel();
            SocketChannel channel = null;
            try {
                while ((channel = server.accept()) != null) {
                    SelectorManager.this.accepted(channel);
                }
            }
            catch (Throwable x) {
                this.closeNoExceptions(channel);
                SelectorManager.LOG.warn("Accept failed for channel " + channel, x);
            }
        }
        
        private void closeNoExceptions(final Closeable closeable) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            }
            catch (Throwable x) {
                SelectorManager.LOG.ignore(x);
            }
        }
        
        public void wakeup() {
            this._selector.wakeup();
        }
        
        public boolean isSelectorThread() {
            return Thread.currentThread() == this._thread;
        }
        
        private EndPoint createEndPoint(final SocketChannel channel, final SelectionKey selectionKey) throws IOException {
            final EndPoint endPoint = SelectorManager.this.newEndPoint(channel, this, selectionKey);
            SelectorManager.this.endPointOpened(endPoint);
            final Connection connection = SelectorManager.this.newConnection(channel, endPoint, selectionKey.attachment());
            endPoint.setConnection(connection);
            SelectorManager.this.connectionOpened(connection);
            if (SelectorManager.LOG.isDebugEnabled()) {
                SelectorManager.LOG.debug("Created {}", endPoint);
            }
            return endPoint;
        }
        
        public void destroyEndPoint(final EndPoint endPoint) {
            if (SelectorManager.LOG.isDebugEnabled()) {
                SelectorManager.LOG.debug("Destroyed {}", endPoint);
            }
            final Connection connection = endPoint.getConnection();
            if (connection != null) {
                SelectorManager.this.connectionClosed(connection);
            }
            SelectorManager.this.endPointClosed(endPoint);
        }
        
        @Override
        public String dump() {
            return ContainerLifeCycle.dump(this);
        }
        
        @Override
        public void dump(final Appendable out, final String indent) throws IOException {
            out.append(String.valueOf(this)).append(" id=").append(String.valueOf(this._id)).append("\n");
            final Thread selecting = this._thread;
            Object where = "not selecting";
            final StackTraceElement[] trace = (StackTraceElement[])((selecting == null) ? null : selecting.getStackTrace());
            if (trace != null) {
                for (final StackTraceElement t : trace) {
                    if (t.getClassName().startsWith("org.eclipse.jetty.")) {
                        where = t;
                        break;
                    }
                }
            }
            final Selector selector = this._selector;
            if (selector != null && selector.isOpen()) {
                final ArrayList<Object> dump = new ArrayList<Object>(selector.keys().size() * 2);
                dump.add(where);
                final DumpKeys dumpKeys = new DumpKeys((List)dump);
                this.submit(dumpKeys);
                dumpKeys.await(5L, TimeUnit.SECONDS);
                ContainerLifeCycle.dump(out, indent, dump);
            }
        }
        
        public void dumpKeysState(final List<Object> dumps) {
            final Selector selector = this._selector;
            final Set<SelectionKey> keys = selector.keys();
            dumps.add(selector + " keys=" + keys.size());
            for (final SelectionKey key : keys) {
                if (key.isValid()) {
                    dumps.add(key.attachment() + " iOps=" + key.interestOps() + " rOps=" + key.readyOps());
                }
                else {
                    dumps.add(key.attachment() + " iOps=-1 rOps=-1");
                }
            }
        }
        
        @Override
        public String toString() {
            final Selector selector = this._selector;
            return String.format("%s keys=%d selected=%d", super.toString(), (selector != null && selector.isOpen()) ? selector.keys().size() : -1, (selector != null && selector.isOpen()) ? selector.selectedKeys().size() : -1);
        }
        
        private class DumpKeys implements Runnable
        {
            private final CountDownLatch latch;
            private final List<Object> _dumps;
            
            private DumpKeys(final List<Object> dumps) {
                this.latch = new CountDownLatch(1);
                this._dumps = dumps;
            }
            
            @Override
            public void run() {
                ManagedSelector.this.dumpKeysState(this._dumps);
                this.latch.countDown();
            }
            
            public boolean await(final long timeout, final TimeUnit unit) {
                try {
                    return this.latch.await(timeout, unit);
                }
                catch (InterruptedException x) {
                    return false;
                }
            }
        }
        
        private class Acceptor implements Runnable
        {
            private final ServerSocketChannel _channel;
            
            public Acceptor(final ServerSocketChannel channel) {
                this._channel = channel;
            }
            
            @Override
            public void run() {
                try {
                    final SelectionKey key = this._channel.register(ManagedSelector.this._selector, 16, null);
                    if (SelectorManager.LOG.isDebugEnabled()) {
                        SelectorManager.LOG.debug("{} acceptor={}", this, key);
                    }
                }
                catch (Throwable x) {
                    ManagedSelector.this.closeNoExceptions(this._channel);
                    SelectorManager.LOG.warn(x);
                }
            }
        }
        
        private class Accept implements Runnable
        {
            private final SocketChannel channel;
            private final Object attachment;
            
            private Accept(final SocketChannel channel, final Object attachment) {
                this.channel = channel;
                this.attachment = attachment;
            }
            
            @Override
            public void run() {
                try {
                    final SelectionKey key = this.channel.register(ManagedSelector.this._selector, 0, this.attachment);
                    final EndPoint endpoint = ManagedSelector.this.createEndPoint(this.channel, key);
                    key.attach(endpoint);
                }
                catch (Throwable x) {
                    ManagedSelector.this.closeNoExceptions(this.channel);
                    SelectorManager.LOG.debug(x);
                }
            }
        }
        
        private class Connect implements Runnable
        {
            private final AtomicBoolean failed;
            private final SocketChannel channel;
            private final Object attachment;
            private final Scheduler.Task timeout;
            
            private Connect(final SocketChannel channel, final Object attachment) {
                this.failed = new AtomicBoolean();
                this.channel = channel;
                this.attachment = attachment;
                this.timeout = ManagedSelector.this.this$0.scheduler.schedule(new ConnectTimeout(this), ManagedSelector.this.this$0.getConnectTimeout(), TimeUnit.MILLISECONDS);
            }
            
            @Override
            public void run() {
                try {
                    this.channel.register(ManagedSelector.this._selector, 8, this);
                }
                catch (Throwable x) {
                    this.failed(x);
                }
            }
            
            private void failed(final Throwable failure) {
                if (this.failed.compareAndSet(false, true)) {
                    this.timeout.cancel();
                    ManagedSelector.this.closeNoExceptions(this.channel);
                    SelectorManager.this.connectionFailed(this.channel, failure, this.attachment);
                }
            }
        }
        
        private class ConnectTimeout implements Runnable
        {
            private final Connect connect;
            
            private ConnectTimeout(final Connect connect) {
                this.connect = connect;
            }
            
            @Override
            public void run() {
                final SocketChannel channel = this.connect.channel;
                if (channel.isConnectionPending()) {
                    if (SelectorManager.LOG.isDebugEnabled()) {
                        SelectorManager.LOG.debug("Channel {} timed out while connecting, closing it", channel);
                    }
                    this.connect.failed(new SocketTimeoutException("Connect Timeout"));
                }
            }
        }
        
        private class Stop implements Runnable
        {
            private final CountDownLatch latch;
            
            private Stop() {
                this.latch = new CountDownLatch(1);
            }
            
            @Override
            public void run() {
                try {
                    for (final SelectionKey key : ManagedSelector.this._selector.keys()) {
                        final Object attachment = key.attachment();
                        if (attachment instanceof EndPoint) {
                            final EndPointCloser closer = new EndPointCloser((EndPoint)attachment);
                            SelectorManager.this.execute(closer);
                            closer.await(ManagedSelector.this.getStopTimeout());
                        }
                    }
                    ManagedSelector.this.closeNoExceptions(ManagedSelector.this._selector);
                }
                finally {
                    this.latch.countDown();
                }
            }
            
            public boolean await(final long timeout) {
                try {
                    return this.latch.await(timeout, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException x) {
                    return false;
                }
            }
        }
        
        private class EndPointCloser implements Runnable
        {
            private final CountDownLatch latch;
            private final EndPoint endPoint;
            
            private EndPointCloser(final EndPoint endPoint) {
                this.latch = new CountDownLatch(1);
                this.endPoint = endPoint;
            }
            
            @Override
            public void run() {
                try {
                    ManagedSelector.this.closeNoExceptions(this.endPoint.getConnection());
                }
                finally {
                    this.latch.countDown();
                }
            }
            
            private boolean await(final long timeout) {
                try {
                    return this.latch.await(timeout, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException x) {
                    return false;
                }
            }
        }
    }
    
    public interface SelectableEndPoint extends EndPoint
    {
        void onSelected();
    }
}
