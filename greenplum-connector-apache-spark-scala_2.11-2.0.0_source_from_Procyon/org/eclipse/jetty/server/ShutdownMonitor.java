// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.io.OutputStream;
import org.eclipse.jetty.util.component.Destroyable;
import org.eclipse.jetty.util.thread.ShutdownThread;
import java.io.Reader;
import java.io.LineNumberReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Collection;
import java.util.Arrays;
import java.net.ServerSocket;
import org.eclipse.jetty.util.component.LifeCycle;
import java.util.Set;

public class ShutdownMonitor
{
    private final Set<LifeCycle> _lifeCycles;
    private boolean DEBUG;
    private String host;
    private int port;
    private String key;
    private boolean exitVm;
    private ServerSocket serverSocket;
    private Thread thread;
    
    public static ShutdownMonitor getInstance() {
        return Holder.instance;
    }
    
    public static synchronized void register(final LifeCycle... lifeCycles) {
        getInstance()._lifeCycles.addAll(Arrays.asList(lifeCycles));
    }
    
    public static synchronized void deregister(final LifeCycle lifeCycle) {
        getInstance()._lifeCycles.remove(lifeCycle);
    }
    
    public static synchronized boolean isRegistered(final LifeCycle lifeCycle) {
        return getInstance()._lifeCycles.contains(lifeCycle);
    }
    
    private ShutdownMonitor() {
        this._lifeCycles = new CopyOnWriteArraySet<LifeCycle>();
        this.DEBUG = (System.getProperty("DEBUG") != null);
        this.host = System.getProperty("STOP.HOST", "127.0.0.1");
        this.port = Integer.parseInt(System.getProperty("STOP.PORT", "-1"));
        this.key = System.getProperty("STOP.KEY", null);
        this.exitVm = true;
    }
    
    private void close(final ServerSocket server) {
        if (server == null) {
            return;
        }
        try {
            server.close();
        }
        catch (IOException ignore) {
            this.debug(ignore);
        }
    }
    
    private void close(final Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.close();
        }
        catch (IOException ignore) {
            this.debug(ignore);
        }
    }
    
    private void shutdownInput(final Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.shutdownInput();
        }
        catch (IOException ignore) {
            this.debug(ignore);
        }
    }
    
    private void debug(final String format, final Object... args) {
        if (this.DEBUG) {
            System.err.printf("[ShutdownMonitor] " + format + "%n", args);
        }
    }
    
    private void debug(final Throwable t) {
        if (this.DEBUG) {
            t.printStackTrace(System.err);
        }
    }
    
    public String getKey() {
        return this.key;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }
    
    public boolean isExitVm() {
        return this.exitVm;
    }
    
    public void setDebug(final boolean flag) {
        this.DEBUG = flag;
    }
    
    public void setExitVm(final boolean exitVm) {
        synchronized (this) {
            if (this.thread != null && this.thread.isAlive()) {
                throw new IllegalStateException("ShutdownMonitorThread already started");
            }
            this.exitVm = exitVm;
        }
    }
    
    public void setKey(final String key) {
        synchronized (this) {
            if (this.thread != null && this.thread.isAlive()) {
                throw new IllegalStateException("ShutdownMonitorThread already started");
            }
            this.key = key;
        }
    }
    
    public void setPort(final int port) {
        synchronized (this) {
            if (this.thread != null && this.thread.isAlive()) {
                throw new IllegalStateException("ShutdownMonitorThread already started");
            }
            this.port = port;
        }
    }
    
    protected void start() throws Exception {
        Thread t = null;
        synchronized (this) {
            if (this.thread != null && this.thread.isAlive()) {
                if (this.DEBUG) {
                    System.err.printf("ShutdownMonitorThread already started", new Object[0]);
                }
                return;
            }
            (this.thread = new Thread(new ShutdownMonitorRunnable())).setDaemon(true);
            this.thread.setName("ShutdownMonitor");
            t = this.thread;
        }
        if (t != null) {
            t.start();
        }
    }
    
    protected boolean isAlive() {
        boolean result = false;
        synchronized (this) {
            result = (this.thread != null && this.thread.isAlive());
        }
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("%s[port=%d]", this.getClass().getName(), this.port);
    }
    
    static class Holder
    {
        static ShutdownMonitor instance;
        
        static {
            Holder.instance = new ShutdownMonitor(null);
        }
    }
    
    private class ShutdownMonitorRunnable implements Runnable
    {
        public ShutdownMonitorRunnable() {
            this.startListenSocket();
        }
        
        @Override
        public void run() {
            if (ShutdownMonitor.this.serverSocket == null) {
                return;
            }
            while (ShutdownMonitor.this.serverSocket != null) {
                Socket socket = null;
                try {
                    socket = ShutdownMonitor.this.serverSocket.accept();
                    final LineNumberReader lin = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
                    final String receivedKey = lin.readLine();
                    if (!ShutdownMonitor.this.key.equals(receivedKey)) {
                        System.err.println("Ignoring command with incorrect key");
                    }
                    else {
                        final OutputStream out = socket.getOutputStream();
                        final String cmd = lin.readLine();
                        ShutdownMonitor.this.debug("command=%s", cmd);
                        if ("stop".equalsIgnoreCase(cmd)) {
                            ShutdownMonitor.this.debug("Issuing stop...", new Object[0]);
                            for (final LifeCycle l : ShutdownMonitor.this._lifeCycles) {
                                try {
                                    if (l.isStarted() && ShutdownThread.isRegistered(l)) {
                                        l.stop();
                                    }
                                    if (!(l instanceof Destroyable) || !ShutdownMonitor.this.exitVm) {
                                        continue;
                                    }
                                    ((Destroyable)l).destroy();
                                }
                                catch (Exception e) {
                                    ShutdownMonitor.this.debug(e);
                                }
                            }
                            this.stopInput(socket);
                            ShutdownMonitor.this.debug("Informing client that we are stopped.", new Object[0]);
                            this.informClient(out, "Stopped\r\n");
                            this.stopOutput(socket);
                            if (!ShutdownMonitor.this.exitVm) {
                                continue;
                            }
                            ShutdownMonitor.this.debug("Killing JVM", new Object[0]);
                            System.exit(0);
                        }
                        else if ("forcestop".equalsIgnoreCase(cmd)) {
                            ShutdownMonitor.this.debug("Issuing force stop...", new Object[0]);
                            this.stopLifeCycles(ShutdownMonitor.this.exitVm);
                            this.stopInput(socket);
                            ShutdownMonitor.this.debug("Informing client that we are stopped.", new Object[0]);
                            this.informClient(out, "Stopped\r\n");
                            this.stopOutput(socket);
                            if (!ShutdownMonitor.this.exitVm) {
                                continue;
                            }
                            ShutdownMonitor.this.debug("Killing JVM", new Object[0]);
                            System.exit(0);
                        }
                        else if ("stopexit".equalsIgnoreCase(cmd)) {
                            ShutdownMonitor.this.debug("Issuing stop and exit...", new Object[0]);
                            this.stopLifeCycles(true);
                            this.stopInput(socket);
                            ShutdownMonitor.this.debug("Informing client that we are stopped.", new Object[0]);
                            this.informClient(out, "Stopped\r\n");
                            this.stopOutput(socket);
                            ShutdownMonitor.this.debug("Killing JVM", new Object[0]);
                            System.exit(0);
                        }
                        else if ("exit".equalsIgnoreCase(cmd)) {
                            ShutdownMonitor.this.debug("Killing JVM", new Object[0]);
                            System.exit(0);
                        }
                        else {
                            if (!"status".equalsIgnoreCase(cmd)) {
                                continue;
                            }
                            this.informClient(out, "OK\r\n");
                        }
                    }
                }
                catch (Exception e2) {
                    ShutdownMonitor.this.debug(e2);
                    System.err.println(e2.toString());
                }
                finally {
                    ShutdownMonitor.this.close(socket);
                    socket = null;
                }
            }
        }
        
        public void stopInput(final Socket socket) {
            ShutdownMonitor.this.close(ShutdownMonitor.this.serverSocket);
            ShutdownMonitor.this.serverSocket = null;
            ShutdownMonitor.this.shutdownInput(socket);
        }
        
        public void stopOutput(Socket socket) throws IOException {
            socket.shutdownOutput();
            ShutdownMonitor.this.close(socket);
            socket = null;
            ShutdownMonitor.this.debug("Shutting down monitor", new Object[0]);
            ShutdownMonitor.this.serverSocket = null;
        }
        
        public void informClient(final OutputStream out, final String message) throws IOException {
            out.write(message.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
        
        public void stopLifeCycles(final boolean destroy) {
            for (final LifeCycle l : ShutdownMonitor.this._lifeCycles) {
                try {
                    if (l.isStarted()) {
                        l.stop();
                    }
                    if (!(l instanceof Destroyable) || !destroy) {
                        continue;
                    }
                    ((Destroyable)l).destroy();
                }
                catch (Exception e) {
                    ShutdownMonitor.this.debug(e);
                }
            }
        }
        
        public void startListenSocket() {
            if (ShutdownMonitor.this.port < 0) {
                if (ShutdownMonitor.this.DEBUG) {
                    System.err.println("ShutdownMonitor not in use (port < 0): " + ShutdownMonitor.this.port);
                }
                return;
            }
            try {
                ShutdownMonitor.this.serverSocket = new ServerSocket();
                ShutdownMonitor.this.serverSocket.setReuseAddress(true);
                ShutdownMonitor.this.serverSocket.bind(new InetSocketAddress(InetAddress.getByName(ShutdownMonitor.this.host), ShutdownMonitor.this.port), 1);
                if (ShutdownMonitor.this.port == 0) {
                    ShutdownMonitor.this.port = ShutdownMonitor.this.serverSocket.getLocalPort();
                    System.out.printf("STOP.PORT=%d%n", ShutdownMonitor.this.port);
                }
                if (ShutdownMonitor.this.key == null) {
                    ShutdownMonitor.this.key = Long.toString((long)(9.223372036854776E18 * Math.random() + this.hashCode() + System.currentTimeMillis()), 36);
                    System.out.printf("STOP.KEY=%s%n", ShutdownMonitor.this.key);
                }
            }
            catch (Exception e) {
                ShutdownMonitor.this.debug(e);
                System.err.println("Error binding monitor port " + ShutdownMonitor.this.port + ": " + e.toString());
                ShutdownMonitor.this.serverSocket = null;
            }
            finally {
                ShutdownMonitor.this.debug("STOP.PORT=%d", ShutdownMonitor.this.port);
                ShutdownMonitor.this.debug("STOP.KEY=%s", ShutdownMonitor.this.key);
                ShutdownMonitor.this.debug("%s", ShutdownMonitor.this.serverSocket);
            }
        }
    }
}
