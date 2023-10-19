// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.io.Closeable;

public interface Client extends Runnable, Closeable
{
    void close();
}
