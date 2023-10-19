// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap2;
import org.apache.logging.log4j.spi.CopyOnWrite;
import org.apache.logging.log4j.ThreadContextAccess;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.ContextDataInjector;

public class ContextDataInjectorFactory
{
    public static ContextDataInjector createInjector() {
        final String className = PropertiesUtil.getProperties().getStringProperty("log4j2.ContextDataInjector");
        if (className == null) {
            return createDefaultInjector();
        }
        try {
            final Class<? extends ContextDataInjector> cls = LoaderUtil.loadClass(className).asSubclass(ContextDataInjector.class);
            return (ContextDataInjector)cls.newInstance();
        }
        catch (Exception dynamicFailed) {
            final ContextDataInjector result = createDefaultInjector();
            StatusLogger.getLogger().warn("Could not create ContextDataInjector for '{}', using default {}: {}", className, result.getClass().getName(), dynamicFailed);
            return result;
        }
    }
    
    private static ContextDataInjector createDefaultInjector() {
        final ThreadContextMap threadContextMap = ThreadContextAccess.getThreadContextMap();
        if (threadContextMap instanceof CopyOnWrite && threadContextMap instanceof ThreadContextMap2) {
            return new ThreadContextDataInjector.ForCopyOnWriteThreadContextMap();
        }
        if (threadContextMap instanceof ThreadContextMap2) {
            return new ThreadContextDataInjector.ForGarbageFreeThreadContextMap();
        }
        return new ThreadContextDataInjector.ForDefaultThreadContextMap();
    }
}
