// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import ch.qos.logback.core.spi.ContextAware;

interface RemoteReceiverClient extends Client, ContextAware
{
    void setQueue(final BlockingQueue<Serializable> p0);
    
    boolean offer(final Serializable p0);
}
