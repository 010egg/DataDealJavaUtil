// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;

public interface ConnectionFactory
{
    String getProtocol();
    
    Connection newConnection(final Connector p0, final EndPoint p1);
}
