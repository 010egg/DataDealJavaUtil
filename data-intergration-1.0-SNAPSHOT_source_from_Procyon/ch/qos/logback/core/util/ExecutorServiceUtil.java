// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class ExecutorServiceUtil
{
    private static final ThreadFactory THREAD_FACTORY;
    
    public static ScheduledExecutorService newScheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(2, ExecutorServiceUtil.THREAD_FACTORY);
    }
    
    public static ExecutorService newExecutorService() {
        return new ThreadPoolExecutor(0, 32, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), ExecutorServiceUtil.THREAD_FACTORY);
    }
    
    public static void shutdown(final ExecutorService executorService) {
        executorService.shutdownNow();
    }
    
    static {
        THREAD_FACTORY = new ThreadFactory() {
            private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            
            @Override
            public Thread newThread(final Runnable r) {
                final Thread thread = this.defaultFactory.newThread(r);
                if (!thread.isDaemon()) {
                    thread.setDaemon(true);
                }
                thread.setName("logback-" + this.threadNumber.getAndIncrement());
                return thread;
            }
        };
    }
}
