// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.io.IOException;
import java.io.Closeable;

public interface ServerListener<T extends Client> extends Closeable
{
    T acceptClient() throws IOException, InterruptedException;
    
    void close();
}
