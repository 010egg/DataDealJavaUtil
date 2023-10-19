// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import java.util.concurrent.ScheduledFuture;
import ch.qos.logback.core.spi.LifeCycle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.Map;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.spi.PropertyContainer;

public interface Context extends PropertyContainer
{
    StatusManager getStatusManager();
    
    Object getObject(final String p0);
    
    void putObject(final String p0, final Object p1);
    
    String getProperty(final String p0);
    
    void putProperty(final String p0, final String p1);
    
    Map<String, String> getCopyOfPropertyMap();
    
    String getName();
    
    void setName(final String p0);
    
    long getBirthTime();
    
    Object getConfigurationLock();
    
    ScheduledExecutorService getScheduledExecutorService();
    
    @Deprecated
    ExecutorService getExecutorService();
    
    void register(final LifeCycle p0);
    
    void addScheduledFuture(final ScheduledFuture<?> p0);
}
