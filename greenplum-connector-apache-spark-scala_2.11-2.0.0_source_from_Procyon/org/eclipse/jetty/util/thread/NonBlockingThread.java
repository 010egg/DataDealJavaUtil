// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.thread;

public class NonBlockingThread implements Runnable
{
    private static final ThreadLocal<Boolean> __nonBlockingThread;
    private final Runnable delegate;
    
    public static boolean isNonBlockingThread() {
        return Boolean.TRUE.equals(NonBlockingThread.__nonBlockingThread.get());
    }
    
    public NonBlockingThread(final Runnable delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void run() {
        try {
            NonBlockingThread.__nonBlockingThread.set(Boolean.TRUE);
            this.delegate.run();
        }
        finally {
            NonBlockingThread.__nonBlockingThread.set(Boolean.FALSE);
        }
    }
    
    static {
        __nonBlockingThread = new ThreadLocal<Boolean>();
    }
}
