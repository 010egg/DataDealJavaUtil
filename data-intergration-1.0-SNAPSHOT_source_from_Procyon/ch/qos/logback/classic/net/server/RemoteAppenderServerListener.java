// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.net.server;

import ch.qos.logback.core.net.server.Client;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import ch.qos.logback.core.net.server.ServerSocketListener;

class RemoteAppenderServerListener extends ServerSocketListener<RemoteAppenderClient>
{
    public RemoteAppenderServerListener(final ServerSocket serverSocket) {
        super(serverSocket);
    }
    
    @Override
    protected RemoteAppenderClient createClient(final String id, final Socket socket) throws IOException {
        return new RemoteAppenderStreamClient(id, socket);
    }
}
