// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net.server;

import ch.qos.logback.core.net.server.Client;
import ch.qos.logback.classic.LoggerContext;
import java.util.concurrent.Executor;
import ch.qos.logback.core.net.server.ServerListener;
import ch.qos.logback.core.net.server.ConcurrentServerRunner;

class RemoteAppenderServerRunner extends ConcurrentServerRunner<RemoteAppenderClient>
{
    public RemoteAppenderServerRunner(final ServerListener<RemoteAppenderClient> listener, final Executor executor) {
        super(listener, executor);
    }
    
    @Override
    protected boolean configureClient(final RemoteAppenderClient client) {
        client.setLoggerContext((LoggerContext)this.getContext());
        return true;
    }
}
