// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface TimeLimiter
{
     <T> T newProxy(final T p0, final Class<T> p1, final long p2, final TimeUnit p3);
    
     <T> T callWithTimeout(final Callable<T> p0, final long p1, final TimeUnit p2) throws TimeoutException, InterruptedException, ExecutionException;
    
     <T> T callUninterruptiblyWithTimeout(final Callable<T> p0, final long p1, final TimeUnit p2) throws TimeoutException, ExecutionException;
    
    void runWithTimeout(final Runnable p0, final long p1, final TimeUnit p2) throws TimeoutException, InterruptedException;
    
    void runUninterruptiblyWithTimeout(final Runnable p0, final long p1, final TimeUnit p2) throws TimeoutException;
}
