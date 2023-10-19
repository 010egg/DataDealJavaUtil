// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net.server;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.net.server.Client;

interface RemoteAppenderClient extends Client
{
    void setLoggerContext(final LoggerContext p0);
}
