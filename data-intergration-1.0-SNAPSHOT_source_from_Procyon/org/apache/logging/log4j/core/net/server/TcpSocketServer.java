// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.server;

import java.io.OptionalDataException;
import java.io.EOFException;
import org.apache.logging.log4j.core.LogEventListener;
import com.beust.jcommander.validators.PositiveInteger;
import com.beust.jcommander.Parameter;
import java.util.Iterator;
import java.net.Socket;
import org.apache.logging.log4j.message.EntryMessage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.net.InetAddress;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentMap;
import java.io.InputStream;

public class TcpSocketServer<T extends InputStream> extends AbstractSocketServer<T>
{
    private final ConcurrentMap<Long, SocketHandler> handlers;
    private final ServerSocket serverSocket;
    
    public static TcpSocketServer<InputStream> createJsonSocketServer(final int port) throws IOException {
        TcpSocketServer.LOGGER.entry(new Object[] { "createJsonSocketServer", port });
        final TcpSocketServer<InputStream> socketServer = new TcpSocketServer<InputStream>(port, new JsonInputStreamLogEventBridge());
        return TcpSocketServer.LOGGER.exit(socketServer);
    }
    
    public static TcpSocketServer<ObjectInputStream> createSerializedSocketServer(final int port) throws IOException {
        TcpSocketServer.LOGGER.entry(port);
        final TcpSocketServer<ObjectInputStream> socketServer = new TcpSocketServer<ObjectInputStream>(port, new ObjectInputStreamLogEventBridge());
        return TcpSocketServer.LOGGER.exit(socketServer);
    }
    
    public static TcpSocketServer<ObjectInputStream> createSerializedSocketServer(final int port, final int backlog, final InetAddress localBindAddress) throws IOException {
        TcpSocketServer.LOGGER.entry(port);
        final TcpSocketServer<ObjectInputStream> socketServer = new TcpSocketServer<ObjectInputStream>(port, backlog, localBindAddress, new ObjectInputStreamLogEventBridge());
        return TcpSocketServer.LOGGER.exit(socketServer);
    }
    
    public static TcpSocketServer<InputStream> createXmlSocketServer(final int port) throws IOException {
        TcpSocketServer.LOGGER.entry(port);
        final TcpSocketServer<InputStream> socketServer = new TcpSocketServer<InputStream>(port, new XmlInputStreamLogEventBridge());
        return TcpSocketServer.LOGGER.exit(socketServer);
    }
    
    public static void main(final String[] args) throws Exception {
        final CommandLineArguments cla = AbstractSocketServer.parseCommandLine(args, TcpSocketServer.class, new CommandLineArguments());
        if (cla.isHelp()) {
            return;
        }
        if (cla.getConfigLocation() != null) {
            ConfigurationFactory.setConfigurationFactory(new ServerConfigurationFactory(cla.getConfigLocation()));
        }
        final TcpSocketServer<ObjectInputStream> socketServer = createSerializedSocketServer(cla.getPort(), cla.getBacklog(), cla.getLocalBindAddress());
        final Thread serverThread = new Log4jThread(socketServer);
        serverThread.start();
        if (cla.isInteractive()) {
            final Charset enc = Charset.defaultCharset();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, enc));
            String line;
            do {
                line = reader.readLine();
            } while (line != null && !line.equalsIgnoreCase("Quit") && !line.equalsIgnoreCase("Stop") && !line.equalsIgnoreCase("Exit"));
            socketServer.shutdown();
            serverThread.join();
        }
    }
    
    public TcpSocketServer(final int port, final int backlog, final InetAddress localBindAddress, final LogEventBridge<T> logEventInput) throws IOException {
        this(port, logEventInput, new ServerSocket(port, backlog, localBindAddress));
    }
    
    public TcpSocketServer(final int port, final LogEventBridge<T> logEventInput) throws IOException {
        this(port, logEventInput, extracted(port));
    }
    
    private static ServerSocket extracted(final int port) throws IOException {
        return new ServerSocket(port);
    }
    
    public TcpSocketServer(final int port, final LogEventBridge<T> logEventInput, final ServerSocket serverSocket) throws IOException {
        super(port, logEventInput);
        this.handlers = new ConcurrentHashMap<Long, SocketHandler>();
        this.serverSocket = serverSocket;
    }
    
    @Override
    public void run() {
        final EntryMessage entry = this.logger.traceEntry();
        while (this.isActive()) {
            if (this.serverSocket.isClosed()) {
                return;
            }
            try {
                this.logger.debug("Listening for a connection {}...", this.serverSocket);
                final Socket clientSocket = this.serverSocket.accept();
                this.logger.debug("Acepted connection on {}...", this.serverSocket);
                this.logger.debug("Socket accepted: {}", clientSocket);
                clientSocket.setSoLinger(true, 0);
                final SocketHandler handler = new SocketHandler(clientSocket);
                this.handlers.put(handler.getId(), handler);
                handler.start();
            }
            catch (IOException e) {
                if (this.serverSocket.isClosed()) {
                    this.logger.traceExit(entry);
                    return;
                }
                this.logger.error("Exception encountered on accept. Ignoring. Stack trace :", e);
            }
        }
        for (final Map.Entry<Long, SocketHandler> handlerEntry : this.handlers.entrySet()) {
            final SocketHandler handler2 = handlerEntry.getValue();
            handler2.shutdown();
            try {
                handler2.join();
            }
            catch (InterruptedException ex) {}
        }
        this.logger.traceExit(entry);
    }
    
    public void shutdown() throws IOException {
        final EntryMessage entry = this.logger.traceEntry();
        this.setActive(false);
        Thread.currentThread().interrupt();
        this.serverSocket.close();
        this.logger.traceExit(entry);
    }
    
    protected static class CommandLineArguments extends AbstractSocketServer.CommandLineArguments
    {
        @Parameter(names = { "--backlog", "-b" }, validateWith = PositiveInteger.class, description = "Server socket backlog.")
        private int backlog;
        
        protected CommandLineArguments() {
            this.backlog = 50;
        }
        
        int getBacklog() {
            return this.backlog;
        }
        
        void setBacklog(final int backlog) {
            this.backlog = backlog;
        }
    }
    
    private class SocketHandler extends Log4jThread
    {
        private final T inputStream;
        private volatile boolean shutdown;
        
        public SocketHandler(final Socket socket) throws IOException {
            this.shutdown = false;
            this.inputStream = (T)TcpSocketServer.this.logEventInput.wrapStream(socket.getInputStream());
        }
        
        @Override
        public void run() {
            final EntryMessage entry = TcpSocketServer.this.logger.traceEntry();
            boolean closed = false;
            try {
                try {
                    while (!this.shutdown) {
                        TcpSocketServer.this.logEventInput.logEvents((T)this.inputStream, TcpSocketServer.this);
                    }
                }
                catch (EOFException e3) {
                    closed = true;
                }
                catch (OptionalDataException e) {
                    TcpSocketServer.this.logger.error("OptionalDataException eof=" + e.eof + " length=" + e.length, e);
                }
                catch (IOException e2) {
                    TcpSocketServer.this.logger.error("IOException encountered while reading from socket", e2);
                }
                if (!closed) {
                    try {
                        this.inputStream.close();
                    }
                    catch (Exception ex) {}
                }
            }
            finally {
                TcpSocketServer.this.handlers.remove(this.getId());
            }
            TcpSocketServer.this.logger.traceExit(entry);
        }
        
        public void shutdown() {
            this.shutdown = true;
            this.interrupt();
        }
    }
}
