// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.Closeable;

public class CloseUtil
{
    public static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {}
    }
    
    public static void closeQuietly(final Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        }
        catch (IOException ex) {}
    }
    
    public static void closeQuietly(final ServerSocket serverSocket) {
        if (serverSocket == null) {
            return;
        }
        try {
            serverSocket.close();
        }
        catch (IOException ex) {}
    }
}
