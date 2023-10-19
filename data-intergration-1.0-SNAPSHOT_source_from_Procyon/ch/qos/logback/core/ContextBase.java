// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import java.util.Collection;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ExecutorService;
import ch.qos.logback.core.spi.LogbackLock;
import java.util.Map;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.spi.LifeCycle;

public class ContextBase implements Context, LifeCycle
{
    private long birthTime;
    private String name;
    private StatusManager sm;
    Map<String, String> propertyMap;
    Map<String, Object> objectMap;
    LogbackLock configurationLock;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;
    protected List<ScheduledFuture<?>> scheduledFutures;
    private LifeCycleManager lifeCycleManager;
    private boolean started;
    
    public ContextBase() {
        this.birthTime = System.currentTimeMillis();
        this.sm = new BasicStatusManager();
        this.propertyMap = new HashMap<String, String>();
        this.objectMap = new HashMap<String, Object>();
        this.configurationLock = new LogbackLock();
        this.scheduledFutures = new ArrayList<ScheduledFuture<?>>(1);
        this.initCollisionMaps();
    }
    
    @Override
    public StatusManager getStatusManager() {
        return this.sm;
    }
    
    public void setStatusManager(final StatusManager statusManager) {
        if (statusManager == null) {
            throw new IllegalArgumentException("null StatusManager not allowed");
        }
        this.sm = statusManager;
    }
    
    @Override
    public Map<String, String> getCopyOfPropertyMap() {
        return new HashMap<String, String>(this.propertyMap);
    }
    
    @Override
    public void putProperty(final String key, final String val) {
        this.propertyMap.put(key, val);
    }
    
    protected void initCollisionMaps() {
        this.putObject("RFA_FILENAME_COLLISION_MAP", new HashMap());
        this.putObject("RFA_FILENAME_PATTERN_COLLISION_MAP", new HashMap());
    }
    
    @Override
    public String getProperty(final String key) {
        if ("CONTEXT_NAME".equals(key)) {
            return this.getName();
        }
        return this.propertyMap.get(key);
    }
    
    @Override
    public Object getObject(final String key) {
        return this.objectMap.get(key);
    }
    
    @Override
    public void putObject(final String key, final Object value) {
        this.objectMap.put(key, value);
    }
    
    public void removeObject(final String key) {
        this.objectMap.remove(key);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void start() {
        this.started = true;
    }
    
    @Override
    public void stop() {
        this.stopExecutorServices();
        this.started = false;
    }
    
    @Override
    public boolean isStarted() {
        return this.started;
    }
    
    public void reset() {
        this.removeShutdownHook();
        this.getLifeCycleManager().reset();
        this.propertyMap.clear();
        this.objectMap.clear();
    }
    
    @Override
    public void setName(final String name) throws IllegalStateException {
        if (name != null && name.equals(this.name)) {
            return;
        }
        if (this.name == null || "default".equals(this.name)) {
            this.name = name;
            return;
        }
        throw new IllegalStateException("Context has been already given a name");
    }
    
    @Override
    public long getBirthTime() {
        return this.birthTime;
    }
    
    @Override
    public Object getConfigurationLock() {
        return this.configurationLock;
    }
    
    @Override
    public synchronized ExecutorService getExecutorService() {
        return this.getScheduledExecutorService();
    }
    
    @Override
    public synchronized ScheduledExecutorService getScheduledExecutorService() {
        if (this.scheduledExecutorService == null) {
            this.scheduledExecutorService = ExecutorServiceUtil.newScheduledExecutorService();
        }
        return this.scheduledExecutorService;
    }
    
    private synchronized void stopExecutorServices() {
        if (this.executorService != null) {
            ExecutorServiceUtil.shutdown(this.executorService);
            this.executorService = null;
        }
        if (this.scheduledExecutorService != null) {
            ExecutorServiceUtil.shutdown(this.scheduledExecutorService);
            this.scheduledExecutorService = null;
        }
    }
    
    private void removeShutdownHook() {
        final Thread hook = (Thread)this.getObject("SHUTDOWN_HOOK");
        if (hook != null) {
            this.removeObject("SHUTDOWN_HOOK");
            try {
                Runtime.getRuntime().removeShutdownHook(hook);
            }
            catch (IllegalStateException ex) {}
        }
    }
    
    @Override
    public void register(final LifeCycle component) {
        this.getLifeCycleManager().register(component);
    }
    
    synchronized LifeCycleManager getLifeCycleManager() {
        if (this.lifeCycleManager == null) {
            this.lifeCycleManager = new LifeCycleManager();
        }
        return this.lifeCycleManager;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public void addScheduledFuture(final ScheduledFuture<?> scheduledFuture) {
        this.scheduledFutures.add(scheduledFuture);
    }
    
    public List<ScheduledFuture<?>> getScheduledFutures() {
        return new ArrayList<ScheduledFuture<?>>(this.scheduledFutures);
    }
}
