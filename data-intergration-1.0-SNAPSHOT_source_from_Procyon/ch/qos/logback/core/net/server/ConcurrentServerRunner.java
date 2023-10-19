// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.server;

import java.util.concurrent.RejectedExecutionException;
import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Executor;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class ConcurrentServerRunner<T extends Client> extends ContextAwareBase implements Runnable, ServerRunner<T>
{
    private final Lock clientsLock;
    private final Collection<T> clients;
    private final ServerListener<T> listener;
    private final Executor executor;
    private boolean running;
    
    public ConcurrentServerRunner(final ServerListener<T> listener, final Executor executor) {
        this.clientsLock = new ReentrantLock();
        this.clients = new ArrayList<T>();
        this.listener = listener;
        this.executor = executor;
    }
    
    @Override
    public boolean isRunning() {
        return this.running;
    }
    
    protected void setRunning(final boolean running) {
        this.running = running;
    }
    
    @Override
    public void stop() throws IOException {
        this.listener.close();
        this.accept(new ClientVisitor<T>() {
            @Override
            public void visit(final T client) {
                client.close();
            }
        });
    }
    
    @Override
    public void accept(final ClientVisitor<T> visitor) {
        final Collection<T> clients = this.copyClients();
        for (final T client : clients) {
            try {
                visitor.visit(client);
            }
            catch (RuntimeException ex) {
                this.addError(client + ": " + ex);
            }
        }
    }
    
    private Collection<T> copyClients() {
        this.clientsLock.lock();
        try {
            final Collection<T> copy = new ArrayList<T>((Collection<? extends T>)this.clients);
            return copy;
        }
        finally {
            this.clientsLock.unlock();
        }
    }
    
    @Override
    public void run() {
        this.setRunning(true);
        try {
            this.addInfo("listening on " + this.listener);
            while (!Thread.currentThread().isInterrupted()) {
                final T client = this.listener.acceptClient();
                if (!this.configureClient(client)) {
                    this.addError(client + ": connection dropped");
                    client.close();
                }
                else {
                    try {
                        this.executor.execute(new ClientWrapper(client));
                    }
                    catch (RejectedExecutionException ex2) {
                        this.addError(client + ": connection dropped");
                        client.close();
                    }
                }
            }
        }
        catch (InterruptedException ex3) {}
        catch (Exception ex) {
            this.addError("listener: " + ex);
        }
        this.setRunning(false);
        this.addInfo("shutting down");
        this.listener.close();
    }
    
    protected abstract boolean configureClient(final T p0);
    
    private void addClient(final T client) {
        this.clientsLock.lock();
        try {
            this.clients.add(client);
        }
        finally {
            this.clientsLock.unlock();
        }
    }
    
    private void removeClient(final T client) {
        this.clientsLock.lock();
        try {
            this.clients.remove(client);
        }
        finally {
            this.clientsLock.unlock();
        }
    }
    
    private class ClientWrapper implements Client
    {
        private final T delegate;
        
        public ClientWrapper(final T client) {
            this.delegate = client;
        }
        
        @Override
        public void run() {
            ConcurrentServerRunner.this.addClient(this.delegate);
            try {
                this.delegate.run();
            }
            finally {
                ConcurrentServerRunner.this.removeClient(this.delegate);
            }
        }
        
        @Override
        public void close() {
            this.delegate.close();
        }
    }
}
