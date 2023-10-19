// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.io.ByteArrayEndPoint;
import java.io.IOException;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.BufferUtil;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import java.util.concurrent.LinkedBlockingQueue;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.thread.Scheduler;
import java.util.concurrent.Executor;
import java.util.concurrent.BlockingQueue;

public class LocalConnector extends AbstractConnector
{
    private final BlockingQueue<LocalEndPoint> _connects;
    
    public LocalConnector(final Server server, final Executor executor, final Scheduler scheduler, final ByteBufferPool pool, final int acceptors, final ConnectionFactory... factories) {
        super(server, executor, scheduler, pool, acceptors, factories);
        this._connects = new LinkedBlockingQueue<LocalEndPoint>();
        this.setIdleTimeout(30000L);
    }
    
    public LocalConnector(final Server server) {
        this(server, null, null, null, -1, new ConnectionFactory[] { new HttpConnectionFactory() });
    }
    
    public LocalConnector(final Server server, final SslContextFactory sslContextFactory) {
        this(server, null, null, null, -1, AbstractConnectionFactory.getFactories(sslContextFactory, new HttpConnectionFactory()));
    }
    
    public LocalConnector(final Server server, final ConnectionFactory connectionFactory) {
        this(server, null, null, null, -1, new ConnectionFactory[] { connectionFactory });
    }
    
    public LocalConnector(final Server server, final ConnectionFactory connectionFactory, final SslContextFactory sslContextFactory) {
        this(server, null, null, null, -1, AbstractConnectionFactory.getFactories(sslContextFactory, connectionFactory));
    }
    
    @Override
    public Object getTransport() {
        return this;
    }
    
    public String getResponses(final String requests) throws Exception {
        return this.getResponses(requests, 5L, TimeUnit.SECONDS);
    }
    
    public String getResponses(final String requests, final long idleFor, final TimeUnit units) throws Exception {
        final ByteBuffer result = this.getResponses(BufferUtil.toBuffer(requests, StandardCharsets.UTF_8), idleFor, units);
        return (result == null) ? null : BufferUtil.toString(result, StandardCharsets.UTF_8);
    }
    
    public ByteBuffer getResponses(final ByteBuffer requestsBuffer) throws Exception {
        return this.getResponses(requestsBuffer, 5L, TimeUnit.SECONDS);
    }
    
    public ByteBuffer getResponses(final ByteBuffer requestsBuffer, final long idleFor, final TimeUnit units) throws Exception {
        if (this.LOG.isDebugEnabled()) {
            this.LOG.debug("requests {}", BufferUtil.toUTF8String(requestsBuffer));
        }
        final LocalEndPoint endp = this.executeRequest(requestsBuffer);
        endp.waitUntilClosedOrIdleFor(idleFor, units);
        final ByteBuffer responses = endp.takeOutput();
        if (endp.isOutputShutdown()) {
            endp.close();
        }
        if (this.LOG.isDebugEnabled()) {
            this.LOG.debug("responses {}", BufferUtil.toUTF8String(responses));
        }
        return responses;
    }
    
    public LocalEndPoint executeRequest(final String rawRequest) {
        return this.executeRequest(BufferUtil.toBuffer(rawRequest, StandardCharsets.UTF_8));
    }
    
    private LocalEndPoint executeRequest(final ByteBuffer rawRequest) {
        if (!this.isStarted()) {
            throw new IllegalStateException("!STARTED");
        }
        final LocalEndPoint endp = new LocalEndPoint();
        endp.setInput(rawRequest);
        this._connects.add(endp);
        return endp;
    }
    
    @Override
    protected void accept(final int acceptorID) throws IOException, InterruptedException {
        if (this.LOG.isDebugEnabled()) {
            this.LOG.debug("accepting {}", acceptorID);
        }
        final LocalEndPoint endPoint = this._connects.take();
        endPoint.onOpen();
        this.onEndPointOpened(endPoint);
        final Connection connection = this.getDefaultConnectionFactory().newConnection(this, endPoint);
        endPoint.setConnection(connection);
        connection.onOpen();
    }
    
    public class LocalEndPoint extends ByteArrayEndPoint
    {
        private final CountDownLatch _closed;
        
        public LocalEndPoint() {
            super(LocalConnector.this.getScheduler(), LocalConnector.this.getIdleTimeout());
            this._closed = new CountDownLatch(1);
            this.setGrowOutput(true);
        }
        
        public void addInput(final String s) {
            while (this.getIn() == null || BufferUtil.hasContent(this.getIn())) {
                Thread.yield();
            }
            this.setInput(BufferUtil.toBuffer(s, StandardCharsets.UTF_8));
        }
        
        @Override
        public void close() {
            final boolean wasOpen = this.isOpen();
            super.close();
            if (wasOpen) {
                this.getConnection().onClose();
                this.onClose();
            }
        }
        
        @Override
        public void onClose() {
            LocalConnector.this.onEndPointClosed(this);
            super.onClose();
            this._closed.countDown();
        }
        
        @Override
        public void shutdownOutput() {
            super.shutdownOutput();
            this.close();
        }
        
        public void waitUntilClosed() {
            while (this.isOpen()) {
                try {
                    if (!this._closed.await(10L, TimeUnit.SECONDS)) {
                        break;
                    }
                    continue;
                }
                catch (Exception e) {
                    LocalConnector.this.LOG.warn(e);
                }
            }
        }
        
        public void waitUntilClosedOrIdleFor(final long idleFor, final TimeUnit units) {
            Thread.yield();
            int size = this.getOutput().remaining();
            while (this.isOpen()) {
                try {
                    if (this._closed.await(idleFor, units)) {
                        continue;
                    }
                    if (size == this.getOutput().remaining()) {
                        if (LocalConnector.this.LOG.isDebugEnabled()) {
                            LocalConnector.this.LOG.debug("idle for {} {}", idleFor, units);
                        }
                        return;
                    }
                    size = this.getOutput().remaining();
                }
                catch (Exception e) {
                    LocalConnector.this.LOG.warn(e);
                }
            }
        }
    }
}
