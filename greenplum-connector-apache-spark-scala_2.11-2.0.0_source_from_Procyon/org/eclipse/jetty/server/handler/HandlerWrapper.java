// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server.handler;

import java.util.List;
import org.eclipse.jetty.server.Server;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.annotation.ManagedObject;

@ManagedObject("Handler wrapping another Handler")
public class HandlerWrapper extends AbstractHandlerContainer
{
    protected Handler _handler;
    
    @ManagedAttribute(value = "Wrapped Handler", readonly = true)
    public Handler getHandler() {
        return this._handler;
    }
    
    @Override
    public Handler[] getHandlers() {
        if (this._handler == null) {
            return new Handler[0];
        }
        return new Handler[] { this._handler };
    }
    
    public void setHandler(final Handler handler) {
        if (this.isStarted()) {
            throw new IllegalStateException("STARTED");
        }
        if (handler != null) {
            handler.setServer(this.getServer());
        }
        final Handler old = this._handler;
        this.updateBean(old, this._handler = handler);
    }
    
    @Override
    public void handle(final String target, final Request baseRequest, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        if (this._handler != null && this.isStarted()) {
            this._handler.handle(target, baseRequest, request, response);
        }
    }
    
    @Override
    public void setServer(final Server server) {
        if (server == this.getServer()) {
            return;
        }
        if (this.isStarted()) {
            throw new IllegalStateException("STARTED");
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
