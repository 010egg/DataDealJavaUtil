// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net;

import ch.qos.logback.core.spi.PreSerializationTransformer;
import javax.net.SocketFactory;
import java.net.ConnectException;
import java.io.Serializable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import ch.qos.logback.core.util.CloseUtil;
import java.net.UnknownHostException;
import java.net.Socket;
import java.util.concurrent.Future;
import java.util.concurrent.BlockingDeque;
import ch.qos.logback.core.util.Duration;
import java.net.InetAddress;
import ch.qos.logback.core.AppenderBase;

public abstract class AbstractSocketAppender<E> extends AppenderBase<E> implements SocketConnector.ExceptionHandler
{
    public static final int DEFAULT_PORT = 4560;
    public static final int DEFAULT_RECONNECTION_DELAY = 30000;
    public static final int DEFAULT_QUEUE_SIZE = 128;
    private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
    private static final int DEFAULT_EVENT_DELAY_TIMEOUT = 100;
    private final ObjectWriterFactory objectWriterFactory;
    private final QueueFactory queueFactory;
    private String remoteHost;
    private int port;
    private InetAddress address;
    private Duration reconnectionDelay;
    private int queueSize;
    private int acceptConnectionTimeout;
    private Duration eventDelayLimit;
    private BlockingDeque<E> deque;
    private String peerId;
    private SocketConnector connector;
    private Future<?> task;
    private volatile Socket socket;
    
    protected AbstractSocketAppender() {
        this(new QueueFactory(), new ObjectWriterFactory());
    }
    
    AbstractSocketAppender(final QueueFactory queueFactory, final ObjectWriterFactory objectWriterFactory) {
        this.port = 4560;
        this.reconnectionDelay = new Duration(30000L);
        this.queueSize = 128;
        this.acceptConnectionTimeout = 5000;
        this.eventDelayLimit = new Duration(100L);
        this.objectWriterFactory = objectWriterFactory;
        this.queueFactory = queueFactory;
    }
    
    @Override
    public void start() {
        if (this.isStarted()) {
            return;
        }
        int errorCount = 0;
        if (this.port <= 0) {
            ++errorCount;
            this.addError("No port was configured for appender" + this.name + " For more information, please visit http://logback.qos.ch/codes.html#socket_no_port");
        }
        if (this.remoteHost == null) {
            ++errorCount;
            this.addError("No remote host was configured for appender" + this.name + " For more information, please visit http://logback.qos.ch/codes.html#socket_no_host");
        }
        if (this.queueSize == 0) {
            this.addWarn("Queue size of zero is deprecated, use a size of one to indicate synchronous processing");
        }
        if (this.queueSize < 0) {
            ++errorCount;
            this.addError("Queue size must be greater than zero");
        }
        if (errorCount == 0) {
            try {
                this.address = InetAddress.getByName(this.remoteHost);
            }
            catch (UnknownHostException ex) {
                this.addError("unknown host: " + this.remoteHost);
                ++errorCount;
            }
        }
        if (errorCount == 0) {
            this.deque = (BlockingDeque<E>)this.queueFactory.newLinkedBlockingDeque(this.queueSize);
            this.peerId = "remote peer " + this.remoteHost + ":" + this.port + ": ";
            this.connector = this.createConnector(this.address, this.port, 0, this.reconnectionDelay.getMilliseconds());
            this.task = this.getContext().getExecutorService().submit(new Runnable() {
                @Override
                public void run() {
                    AbstractSocketAppender.this.connectSocketAndDispatchEvents();
                }
            });
            super.start();
        }
    }
    
    @Override
    public void stop() {
        if (!this.isStarted()) {
            return;
        }
        CloseUtil.closeQuietly(this.socket);
        this.task.cancel(true);
        super.stop();
    }
    
    @Override
    protected void append(final E event) {
        if (event == null || !this.isStarted()) {
            return;
        }
        try {
            final boolean inserted = this.deque.offer(event, this.eventDelayLimit.getMilliseconds(), TimeUnit.MILLISECONDS);
            if (!inserted) {
                this.addInfo("Dropping event due to timeout limit of [" + this.eventDelayLimit + "] being exceeded");
            }
        }
        catch (InterruptedException e) {
            this.addError("Interrupted while appending event to SocketAppender", e);
        }
    }
    
    private void connectSocketAndDispatchEvents() {
        try {
            while (this.socketConnectionCouldBeEstablished()) {
                try {
                    final ObjectWriter objectWriter = this.createObjectWriterForSocket();
                    this.addInfo(this.peerId + "connection established");
                    this.dispatchEvents(objectWriter);
                }
                catch (IOException ex) {
                    this.addInfo(this.peerId + "connection failed: " + ex);
                }
                finally {
                    CloseUtil.closeQuietly(this.socket);
                    this.socket = null;
                    this.addInfo(this.peerId + "connection closed");
                }
            }
        }
        catch (InterruptedException ex2) {}
        this.addInfo("shutting down");
    }
    
    private boolean socketConnectionCouldBeEstablished() throws InterruptedException {
        final Socket call = this.connector.call();
        this.socket = call;
        return call != null;
    }
    
    private ObjectWriter createObjectWriterForSocket() throws IOException {
        this.socket.setSoTimeout(this.acceptConnectionTimeout);
        final ObjectWriter objectWriter = this.objectWriterFactory.newAutoFlushingObjectWriter(this.socket.getOutputStream());
        this.socket.setSoTimeout(0);
        return objectWriter;
    }
    
    private SocketConnector createConnector(final InetAddress address, final int port, final int initialDelay, final long retryDelay) {
        final SocketConnector connector = this.newConnector(address, port, initialDelay, retryDelay);
        connector.setExceptionHandler(this);
        connector.setSocketFactory(this.getSocketFactory());
        return connector;
    }
    
    private void dispatchEvents(final ObjectWriter objectWriter) throws InterruptedException, IOException {
        while (true) {
            final E event = this.deque.takeFirst();
            this.postProcessEvent(event);
            final Serializable serializableEvent = this.getPST().transform(event);
            try {
                objectWriter.write(serializableEvent);
            }
            catch (IOException e) {
                this.tryReAddingEventToFrontOfQueue(event);
                throw e;
            }
        }
    }
    
    private void tryReAddingEventToFrontOfQueue(final E event) {
        final boolean wasInserted = this.deque.offerFirst(event);
        if (!wasInserted) {
            this.addInfo("Dropping event due to socket connection error and maxed out deque capacity");
        }
    }
    
    @Override
    public void connectionFailed(final SocketConnector connector, final Exception ex) {
        if (ex instanceof InterruptedException) {
            this.addInfo("connector interrupted");
        }
        else if (ex instanceof ConnectException) {
            this.addInfo(this.peerId + "connection refused");
        }
        else {
            this.addInfo(this.peerId + ex);
        }
    }
    
    protected SocketConnector newConnector(final InetAddress address, final int port, final long initialDelay, final long retryDelay) {
        return new DefaultSocketConnector(address, port, initialDelay, retryDelay);
    }
    
    protected SocketFactory getSocketFactory() {
        return SocketFactory.getDefault();
    }
    
    protected abstract void postProcessEvent(final E p0);
    
    protected abstract PreSerializationTransformer<E> getPST();
    
    public void setRemoteHost(final String host) {
        this.remoteHost = host;
    }
    
    public String getRemoteHost() {
        return this.remoteHost;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setReconnectionDelay(final Duration delay) {
        this.reconnectionDelay = delay;
    }
    
    public Duration getReconnectionDelay() {
        return this.reconnectionDelay;
    }
    
    public void setQueueSize(final int queueSize) {
        this.queueSize = queueSize;
    }
    
    public int getQueueSize() {
        return this.queueSize;
    }
    
    public void setEventDelayLimit(final Duration eventDelayLimit) {
        this.eventDelayLimit = eventDelayLimit;
    }
    
    public Duration getEventDelayLimit() {
        return this.eventDelayLimit;
    }
    
    void setAcceptConnectionTimeout(final int acceptConnectionTimeout) {
        this.acceptConnectionTimeout = acceptConnectionTimeout;
    }
}
