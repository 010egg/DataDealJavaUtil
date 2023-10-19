// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.util.Callback;
import java.nio.ByteBuffer;
import org.eclipse.jetty.http.HttpGenerator;

public interface HttpTransport
{
    void send(final HttpGenerator.ResponseInfo p0, final ByteBuffer p1, final boolean p2, final Callback p3);
    
    void send(final ByteBuffer p0, final boolean p1, final Callback p2);
    
    void completed();
    
    void abort();
}
