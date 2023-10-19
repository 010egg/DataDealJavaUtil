// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server.handler;

import java.util.List;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;

public class HotSwapHandler extends AbstractHandlerContainer
{
    private volatile Handler _handler;
    
    public Handler getHandler() {
        return this._handler;
    }
    
    @Override
    public Handler[] getHandlers() {
        return new Handler[] { this._handler };
    }
    
    public void setHandler(final Handler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Parameter handler is null.");
        }
        try {
            this.updateBean(this._handler, handler);
            this._handler = handler;
            final Server server = this.getServer();
            handler.setServer(server);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }
    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }
    
    @Override
    public void handle(final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        if (this._handler != null && this.isStarted()) {
            this._handler.handle(target, baseRequest, request, response);
        }
    }
    
    @Override
    public void setServer(final Server server) {
        if (this.isRunning()) {
            throw new IllegalStateException("RUNNING");
        }
        super.setServer(server);
        final Handler h = this.getHandler();
        if (h != null) {
            h.setServer(server);
        }
    }
    
    @Override
    protected void expandChildren(final List<Handler> list, final Class<?> byClass) {
        this.expandHandler(this._handler, list, byClass);
    }
    
    @Override
    public void destroy() {
        if (!this.isStopped()) {
            throw new IllegalStateException("!STOPPED");
        }
        final Handler child = this.getHandler();
        if (child != null) {
            this.setHandler(null);
            child.destroy();
        }
        super.destroy();
    }
}
