// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.lang.reflect.Constructor;

public final class UtilityElf
{
    private static final int SQL_SERVER_SNAPSHOT_ISOLATION_LEVEL = 4096;
    
    public static String getNullIfEmpty(final String text) {
        return (text == null) ? null : (text.trim().isEmpty() ? null : text.trim());
    }
    
    public static void quietlySleep(final long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static <T> T createInstance(final String className, final Class<T> clazz, final Object... args) {
        if (className == null) {
            return null;
        }
        try {
            final Class<?> loaded = UtilityElf.class.getClassLoader().loadClass(className);
            if (args.length == 0) {
                return clazz.cast(loaded.newInstance());
            }
            final Class<?>[] argClasses = (Class<?>[])new Class[args.length];
            for (int i = 0; i < args.length; ++i) {
                argClasses[i] = args[i].getClass();
            }
            final Constructor<?> constructor = loaded.getConstructor(argClasses);
            return clazz.cast(constructor.newInstance(args));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static ThreadPoolExecutor createThreadPoolExecutor(final int queueSize, final String threadName, ThreadFactory threadFactory, final RejectedExecutionHandler policy) {
        if (threadFactory == null) {
            threadFactory = new DefaultThreadFactory(threadName, true);
        }
        final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(queueSize);
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, queue, threadFactory, policy);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
    
    public static ThreadPoolExecutor createThreadPoolExecutor(final BlockingQueue<Runnable> queue, final String threadName, ThreadFactory threadFactory, final RejectedExecutionHandler policy) {
        if (threadFactory == null) {
            threadFactory = new DefaultThreadFactory(threadName, true);
        }
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, queue, threadFactory, policy);
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
    
    public static int getTransactionIsolation(final String transactionIsolationName) {
        if (transactionIsolationName != null) {
            try {
                final String upperName = transactionIsolationName.toUpperCase(Locale.ENGLISH);
                if (upperName.startsWith("TRANSACTION_")) {
                    final Field field = Connection.class.getField(upperName);
                    return field.getInt(null);
                }
                final int level = Integer.parseInt(transactionIsolationName);
                switch (level) {
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                    case 8:
                    case 4096: {
                        return level;
                    }
                    default: {
                        throw new IllegalArgumentException();
                    }
                }
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName);
            }
        }
        return -1;
    }
    
    public static final class DefaultThreadFactory implements ThreadFactory
    {
        private final String threadName;
        private final boolean daemon;
        
        public DefaultThreadFactory(final String threadName, final boolean daemon) {
            this.threadName = threadName;
            this.daemon = daemon;
        }
        
        @Override
        public Thread newThread(final Runnable r) {
            final Thread thread = new Thread(r, this.threadName);
            thread.setDaemon(this.daemon);
            return thread;
        }
    }
}
