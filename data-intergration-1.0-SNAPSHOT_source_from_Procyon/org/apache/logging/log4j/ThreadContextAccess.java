// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import org.apache.logging.log4j.spi.ThreadContextMap2;
import org.apache.logging.log4j.spi.ThreadContextMap;

public final class ThreadContextAccess
{
    private ThreadContextAccess() {
    }
    
    public static ThreadContextMap getThreadContextMap() {
        return ThreadContext.getThreadContextMap();
    }
    
    public static ThreadContextMap2 getThreadContextMap2() {
        return (ThreadContextMap2)ThreadContext.getThreadContextMap();
    }
}
