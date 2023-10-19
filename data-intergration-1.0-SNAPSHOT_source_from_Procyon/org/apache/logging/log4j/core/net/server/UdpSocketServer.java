// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.server;

import java.io.EOFException;
import java.io.OptionalDataException;
import org.apache.logging.log4j.core.LogEventListener;
import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.logging.log4j.core.util.Log4jThread;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.io.InputStream;

public class UdpSocketServer<T extends InputStream> extends AbstractSocketServer<T>
{
    private final DatagramSocket datagramSocket;
    private final int maxBufferSize = 67584;
    
    public static UdpSocketServer<InputStream> createJsonSocketServer(final int port) throws IOException {
        return new UdpSocketServer<InputStream>(port, new JsonInputStreamLogEventBridge());
    }
    
    public static UdpSocketServer<ObjectInputStream> createSerializedSocketServer(final int port) throws IOException {
        return new UdpSocketServer<ObjectInputStream>(port, new ObjectInputStreamLogEventBridge());
    }
    
    public static UdpSocketServer<InputStream> createXmlSocketServer(final int port) throws IOException {
        return new UdpSocketServer<InputStream>(port, new XmlInputStreamLogEventBridge());
    }
    
    public static void main(final String[] args) throws Exception {
        final CommandLineArguments cla = AbstractSocketServer.parseCommandLine(args, UdpSocketServer.class, new CommandLineArguments());
        if (cla.isHelp()) {
            return;
        }
        if (cla.getConfigLocation() != null) {
            ConfigurationFactory.setConfigurationFactory(new ServerConfigurationFactory(cla.getConfigLocation()));
        }
        final UdpSocketServer<ObjectInputStream> socketServer = createSerializedSocketServer(cla.getPort());
        final Thread serverThread = new Log4jThread(socketServer);
        serverThread.start();
        if (cla.isInteractive()) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            do {
                line = reader.readLine();
            } while (line != null && !line.equalsIgnoreCase("Quit") && !line.equalsIgnoreCase("Stop") && !line.equalsIgnoreCase("Exit"));
            socketServer.shutdown();
            serverThread.join();
        }
    }
    
    public UdpSocketServer(final int port, final LogEventBridge<T> logEventInput) throws IOException {
        super(port, logEventInput);
        this.datagramSocket = new DatagramSocket(port);
    }
    
    @Override
    public void run() {
        while (this.isActive()) {
            if (this.datagramSocket.isClosed()) {
                return;
            }
            try {
                final byte[] buf = new byte[67584];
                final DatagramPacket packet = new DatagramPacket(buf, buf.length);
                this.datagramSocket.receive(packet);
                final ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength());
                this.logEventInput.logEvents(this.logEventInput.wrapStream(bais), this);
            }
            catch (OptionalDataException e) {
                if (this.datagramSocket.isClosed()) {
                    return;
                }
                this.logger.error("OptionalDataException eof=" + e.eof + " length=" + e.length, e);
            }
            catch (EOFException e3) {
                if (this.datagramSocket.isClosed()) {
                    return;
                }
                this.logger.info("EOF encountered");
            }
            catch (IOException e2) {
                if (this.datagramSocket.isClosed()) {
                    return;
                }
                this.logger.error("Exception encountered on accept. Ignoring. Stack Trace :", e2);
            }
        }
    }
    
    public void shutdown() {
        this.setActive(false);
        Thread.currentThread().interrupt();
        this.datagramSocket.close();
    }
}
