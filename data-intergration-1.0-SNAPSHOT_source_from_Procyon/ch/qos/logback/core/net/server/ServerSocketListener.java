// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.net.SocketAddress;
import ch.qos.logback.core.util.CloseUtil;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public abstract class ServerSocketListener<T extends Client> implements ServerListener<T>
{
    private final ServerSocket serverSocket;
    
    public ServerSocketListener(final ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    @Override
    public T acceptClient() throws IOException {
        final Socket socket = this.serverSocket.accept();
        return this.createClient(this.socketAddressToString(socket.getRemoteSocketAddress()), socket);
    }
    
    protected abstract T createClient(final String p0, final Socket p1) throws IOException;
    
    @Override
    public void close() {
        CloseUtil.closeQuietly(this.serverSocket);
    }
    
    @Override
    public String toString() {
        return this.socketAddressToString(this.serverSocket.getLocalSocketAddress());
    }
    
    private String socketAddressToString(final SocketAddress address) {
        String addr = address.toString();
        final int i = addr.indexOf("/");
        if (i >= 0) {
            addr = addr.substring(i + 1);
        }
        return addr;
    }
}
