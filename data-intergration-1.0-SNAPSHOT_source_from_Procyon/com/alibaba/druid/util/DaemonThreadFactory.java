// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory
{
    private AtomicInteger threadNo;
    private final String nameStart;
    private final String nameEnd = "]";
    
    public DaemonThreadFactory(final String poolName) {
        this.threadNo = new AtomicInteger(1);
        this.nameStart = "[" + poolName + "-";
    }
    
    @Override
    public Thread newThread(final Runnable r) {
        final String threadName = this.nameStart + this.threadNo.getAndIncrement() + "]";
        final Thread newThread = new Thread(r, threadName);
        newThread.setDaemon(true);
        if (newThread.getPriority() != 5) {
            newThread.setPriority(5);
        }
        return newThread;
    }
}
