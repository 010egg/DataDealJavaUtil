// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.util.concurrent.BlockingQueue;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

class RemoteReceiverServerRunner extends ConcurrentServerRunner<RemoteReceiverClient>
{
    private final int clientQueueSize;
    
    public RemoteReceiverServerRunner(final ServerListener<RemoteReceiverClient> listener, final Executor executor, final int clientQueueSize) {
        super(listener, executor);
        this.clientQueueSize = clientQueueSize;
    }
    
    @Override
    protected boolean configureClient(final RemoteReceiverClient client) {
        client.setContext(this.getContext());
        client.setQueue(new ArrayBlockingQueue<Serializable>(this.clientQueueSize));
        return true;
    }
}
