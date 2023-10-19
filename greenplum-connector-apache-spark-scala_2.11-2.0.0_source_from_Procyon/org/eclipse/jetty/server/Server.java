// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.log.Log;
import java.util.Collections;
import java.net.InetAddress;
import org.eclipse.jetty.server.handler.ContextHandler;
import java.net.URI;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.util.URIUtil;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.http.HttpMethod;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import org.eclipse.jetty.util.component.Graceful;
import java.util.concurrent.Future;
import java.util.Iterator;
import org.eclipse.jetty.util.Uptime;
import org.eclipse.jetty.util.MultiException;
import org.eclipse.jetty.http.HttpGenerator;
import org.eclipse.jetty.http.DateGenerator;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpField;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import org.eclipse.jetty.util.thread.ShutdownThread;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import java.util.concurrent.CopyOnWriteArrayList;
import java.net.InetSocketAddress;
import org.eclipse.jetty.util.annotation.Name;
import java.util.List;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.util.AttributesMap;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.Attributes;
import org.eclipse.jetty.server.handler.HandlerWrapper;

@ManagedObject("Jetty HTTP Servlet server")
public class Server extends HandlerWrapper implements Attributes
{
    private static final Logger LOG;
    private final AttributesMap _attributes;
    private final ThreadPool _threadPool;
    private final List<Connector> _connectors;
    private SessionIdManager _sessionIdManager;
    private boolean _stopAtShutdown;
    private boolean _dumpAfterStart;
    private boolean _dumpBeforeStop;
    private volatile DateField _dateField;
    
    public Server() {
        this((ThreadPool)null);
    }
    
    public Server(@Name("port") final int port) {
        this((ThreadPool)null);
        final ServerConnector connector = new ServerConnector(this);
        connector.setPort(port);
        this.setConnectors(new Connector[] { connector });
    }
    
    public Server(@Name("address") final InetSocketAddress addr) {
        this((ThreadPool)null);
        final ServerConnector connector = new ServerConnector(this);
        connector.setHost(addr.getHostName());
        connector.setPort(addr.getPort());
        this.setConnectors(new Connector[] { connector });
    }
    
    public Server(@Name("threadpool") final ThreadPool pool) {
        this._attributes = new AttributesMap();
        this._connectors = new CopyOnWriteArrayList<Connector>();
        this._dumpAfterStart = false;
        this._dumpBeforeStop = false;
        this.addBean(this._threadPool = ((pool != null) ? pool : new QueuedThreadPool()));
        this.setServer(this);
    }
    
    @ManagedAttribute("version of this server")
    public static String getVersion() {
        return Jetty.VERSION;
    }
    
    public boolean getStopAtShutdown() {
        return this._stopAtShutdown;
    }
    
    @Override
    public void setStopTimeout(final long stopTimeout) {
        super.setStopTimeout(stopTimeout);
    }
    
    public void setStopAtShutdown(final boolean stop) {
        if (stop) {
            if (!this._stopAtShutdown && this.isStarted()) {
                ShutdownThread.register(this);
            }
        }
        else {
            ShutdownThread.deregister(this);
        }
        this._stopAtShutdown = stop;
    }
    
    @ManagedAttribute(value = "connectors for this server", readonly = true)
    public Connector[] getConnectors() {
        final List<Connector> connectors = new ArrayList<Connector>(this._connectors);
        return connectors.toArray(new Connector[connectors.size()]);
    }
    
    public void addConnector(final Connector connector) {
        if (connector.getServer() != this) {
            throw new IllegalArgumentException("Connector " + connector + " cannot be shared among server " + connector.getServer() + " and server " + this);
        }
        if (this._connectors.add(connector)) {
            this.addBean(connector);
        }
    }
    
    public void removeConnector(final Connector connector) {
        if (this._connectors.remove(connector)) {
            this.removeBean(connector);
        }
    }
    
    public void setConnectors(final Connector[] connectors) {
        if (connectors != null) {
            for (final Connector connector : connectors) {
                if (connector.getServer() != this) {
                    throw new IllegalArgumentException("Connector " + connector + " cannot be shared among server " + connector.getServer() + " and server " + this);
                }
            }
        }
        final Connector[] oldConnectors = this.getConnectors();
        this.updateBeans(oldConnectors, connectors);
        this._connectors.removeAll(Arrays.asList(oldConnectors));
        if (connectors != null) {
            this._connectors.addAll(Arrays.asList(connectors));
        }
    }
    
    @ManagedAttribute("the server thread pool")
    public ThreadPool getThreadPool() {
        return this._threadPool;
    }
    
    @ManagedAttribute("dump state to stderr after start")
    public boolean isDumpAfterStart() {
        return this._dumpAfterStart;
    }
    
    public void setDumpAfterStart(final boolean dumpAfterStart) {
        this._dumpAfterStart = dumpAfterStart;
    }
    
    @ManagedAttribute("dump state to stderr before stop")
    public boolean isDumpBeforeStop() {
        return this._dumpBeforeStop;
    }
    
    public void setDumpBeforeStop(final boolean dumpBeforeStop) {
        this._dumpBeforeStop = dumpBeforeStop;
    }
    
    public HttpField getDateField() {
        final long now = System.currentTimeMillis();
        final long seconds = now / 1000L;
        DateField df = this._dateField;
        if (df == null || df._seconds != seconds) {
            synchronized (this) {
                df = this._dateField;
                if (df == null || df._seconds != seconds) {
                    final HttpField field = new HttpGenerator.CachedHttpField(HttpHeader.DATE, DateGenerator.formatDate(now));
                    this._dateField = new DateField(seconds, field);
                    return field;
                }
            }
        }
        return df._dateField;
    }
    
    @Override
    protected void doStart() throws Exception {
        if (this.getStopAtShutdown()) {
            ShutdownThread.register(this);
        }
        ShutdownMonitor.register(this);
        ShutdownMonitor.getInstance().start();
        Server.LOG.info("jetty-" + getVersion(), new Object[0]);
        HttpGenerator.setJettyVersion(HttpConfiguration.SERVER_VERSION);
        final MultiException mex = new MultiException();
        final ThreadPool.SizedThreadPool pool = this.getBean(ThreadPool.SizedThreadPool.class);
        final int max = (pool == null) ? -1 : pool.getMaxThreads();
        int selectors = 0;
        int acceptors = 0;
        if (mex.size() == 0) {
            for (final Connector connector : this._connectors) {
                if (connector instanceof AbstractConnector) {
                    acceptors += ((AbstractConnector)connector).getAcceptors();
                }
                if (connector instanceof ServerConnector) {
                    selectors += ((ServerConnector)connector).getSelectorManager().getSelectorCount();
                }
            }
        }
        final int needed = 1 + selectors + acceptors;
        if (max > 0 && needed > max) {
            throw new IllegalStateException(String.format("Insufficient threads: max=%d < needed(acceptors=%d + selectors=%d + request=1)", max, acceptors, selectors));
        }
        try {
            super.doStart();
        }
        catch (Throwable e) {
            mex.add(e);
        }
        for (final Connector connector2 : this._connectors) {
            try {
                connector2.start();
            }
            catch (Throwable e2) {
                mex.add(e2);
            }
        }
        if (this.isDumpAfterStart()) {
            this.dumpStdErr();
        }
        mex.ifExceptionThrow();
        Server.LOG.info(String.format("Started @%dms", Uptime.getUptime()), new Object[0]);
    }
    
    @Override
    protected void start(final LifeCycle l) throws Exception {
        if (!(l instanceof Connector)) {
            super.start(l);
        }
    }
    
    @Override
    protected void doStop() throws Exception {
        if (this.isDumpBeforeStop()) {
            this.dumpStdErr();
        }
        final MultiException mex = new MultiException();
        final List<Future<Void>> futures = new ArrayList<Future<Void>>();
        for (final Connector connector : this._connectors) {
            futures.add(connector.shutdown());
        }
        final Handler[] arr$;
        final Handler[] gracefuls = arr$ = this.getChildHandlersByClass(Graceful.class);
        for (final Handler graceful : arr$) {
            futures.add(((Graceful)graceful).shutdown());
        }
        final long stopTimeout = this.getStopTimeout();
        if (stopTimeout > 0L) {
            final long stop_by = System.currentTimeMillis() + stopTimeout;
            if (Server.LOG.isDebugEnabled()) {
                Server.LOG.debug("Graceful shutdown {} by ", this, new Date(stop_by));
            }
            for (final Future<Void> future : futures) {
                try {
                    if (future.isDone()) {
                        continue;
                    }
                    future.get(Math.max(1L, stop_by - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
                }
                catch (Exception e) {
                    mex.add(e);
                }
            }
        }
        for (final Future<Void> future2 : futures) {
            if (!future2.isDone()) {
                future2.cancel(true);
            }
        }
        for (final Connector connector2 : this._connectors) {
            try {
                connector2.stop();
            }
            catch (Throwable e2) {
                mex.add(e2);
            }
        }
        try {
            super.doStop();
        }
        catch (Throwable e3) {
            mex.add(e3);
        }
        if (this.getStopAtShutdown()) {
            ShutdownThread.deregister(this);
        }
        ShutdownMonitor.deregister(this);
        mex.ifExceptionThrow();
    }
    
    public void handle(final HttpChannel<?> connection) throws IOException, ServletException {
        final String target = connection.getRequest().getPathInfo();
        final Request request = connection.getRequest();
        final Response response = connection.getResponse();
        if (Server.LOG.isDebugEnabled()) {
            Server.LOG.debug(request.getDispatcherType() + " " + request.getMethod() + " " + target + " on " + connection, new Object[0]);
        }
        if (HttpMethod.OPTIONS.is(request.getMethod()) || "*".equals(target)) {
            if (!HttpMethod.OPTIONS.is(request.getMethod())) {
                response.sendError(400);
            }
            this.handleOptions(request, response);
            if (!request.isHandled()) {
                this.handle(target, request, request, response);
            }
        }
        else {
            this.handle(target, request, request, response);
        }
        if (Server.LOG.isDebugEnabled()) {
            Server.LOG.debug("RESPONSE " + target + "  " + connection.getResponse().getStatus() + " handled=" + request.isHandled(), new Object[0]);
        }
    }
    
    protected void handleOptions(final Request request, final Response response) throws IOException {
    }
    
    public void handleAsync(final HttpChannel<?> connection) throws IOException, ServletException {
        final HttpChannelState state = connection.getRequest().getHttpChannelState();
        final AsyncContextEvent event = state.getAsyncContextEvent();
        final Request baseRequest = connection.getRequest();
        final String path = event.getPath();
        if (path != null) {
            final ServletContext context = event.getServletContext();
            final HttpURI uri = new HttpURI(URIUtil.addPaths((context == null) ? null : context.getContextPath(), path));
            baseRequest.setUri(uri);
            baseRequest.setRequestURI(null);
            baseRequest.setPathInfo(uri.getDecodedPath());
            if (uri.getQuery() != null) {
                baseRequest.mergeQueryParameters(uri.getQuery(), true);
            }
        }
        final String target = baseRequest.getPathInfo();
        final HttpServletRequest request = (HttpServletRequest)event.getSuppliedRequest();
        final HttpServletResponse response = (HttpServletResponse)event.getSuppliedResponse();
        if (Server.LOG.isDebugEnabled()) {
            Server.LOG.debug(request.getDispatcherType() + " " + request.getMethod() + " " + target + " on " + connection, new Object[0]);
            this.handle(target, baseRequest, request, response);
            Server.LOG.debug("RESPONSE " + target + "  " + connection.getResponse().getStatus(), new Object[0]);
        }
        else {
            this.handle(target, baseRequest, request, response);
        }
    }
    
    public void join() throws InterruptedException {
        this.getThreadPool().join();
    }
    
    public SessionIdManager getSessionIdManager() {
        return this._sessionIdManager;
    }
    
    public void setSessionIdManager(final SessionIdManager sessionIdManager) {
        this.updateBean(this._sessionIdManager, sessionIdManager);
        this._sessionIdManager = sessionIdManager;
    }
    
    @Override
    public void clearAttributes() {
        final Enumeration<String> names = this._attributes.getAttributeNames();
        while (names.hasMoreElements()) {
            this.removeBean(this._attributes.getAttribute(names.nextElement()));
        }
        this._attributes.clearAttributes();
    }
    
    @Override
    public Object getAttribute(final String name) {
        return this._attributes.getAttribute(name);
    }
    
    @Override
    public Enumeration<String> getAttributeNames() {
        return AttributesMap.getAttributeNamesCopy(this._attributes);
    }
    
    @Override
    public void removeAttribute(final String name) {
        final Object bean = this._attributes.getAttribute(name);
        if (bean != null) {
            this.removeBean(bean);
        }
        this._attributes.removeAttribute(name);
    }
    
    @Override
    public void setAttribute(final String name, final Object attribute) {
        this.addBean(attribute);
        this._attributes.setAttribute(name, attribute);
    }
    
    public URI getURI() {
        NetworkConnector connector = null;
        for (final Connector c : this._connectors) {
            if (c instanceof NetworkConnector) {
                connector = (NetworkConnector)c;
                break;
            }
        }
        if (connector == null) {
            return null;
        }
        final ContextHandler context = this.getChildHandlerByClass(ContextHandler.class);
        try {
            final String scheme = connector.getDefaultConnectionFactory().getProtocol().startsWith("SSL-") ? "https" : "http";
            String host = connector.getHost();
            if (context != null && context.getVirtualHosts() != null && context.getVirtualHosts().length > 0) {
                host = context.getVirtualHosts()[0];
            }
            if (host == null) {
                host = InetAddress.getLocalHost().getHostAddress();
            }
            String path = (context == null) ? null : context.getContextPath();
            if (path == null) {
                path = "/";
            }
            return new URI(scheme, null, host, connector.getLocalPort(), path, null, null);
        }
        catch (Exception e) {
            Server.LOG.warn(e);
            return null;
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + "@" + Integer.toHexString(this.hashCode());
    }
    
    @Override
    public void dump(final Appendable out, final String indent) throws IOException {
        this.dumpBeans(out, indent, Collections.singleton(new ClassLoaderDump(this.getClass().getClassLoader())));
    }
    
    public static void main(final String... args) throws Exception {
        System.err.println(getVersion());
    }
    
    static {
        LOG = Log.getLogger(Server.class);
    }
    
    private static class DateField
    {
        final long _seconds;
        final HttpField _dateField;
        
        public DateField(final long seconds, final HttpField dateField) {
            this._seconds = seconds;
            this._dateField = dateField;
        }
    }
}
