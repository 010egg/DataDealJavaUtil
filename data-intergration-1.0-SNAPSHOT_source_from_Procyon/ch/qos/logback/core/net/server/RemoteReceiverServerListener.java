// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

class RemoteReceiverServerListener extends ServerSocketListener<RemoteReceiverClient>
{
    public RemoteReceiverServerListener(final ServerSocket serverSocket) {
        super(serverSocket);
    }
    
    @Override
    protected RemoteReceiverClient createClient(final String id, final Socket socket) throws IOException {
        return new RemoteReceiverStreamClient(id, socket);
    }
}
