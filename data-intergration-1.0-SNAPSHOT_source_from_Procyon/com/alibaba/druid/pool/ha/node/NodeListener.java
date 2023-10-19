// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.node;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.DruidRuntimeException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.Observer;
import java.util.Date;
import java.util.Properties;
import com.alibaba.druid.support.logging.Log;
import java.util.Observable;

public abstract class NodeListener extends Observable
{
    private static final Log LOG;
    private String prefix;
    private Properties properties;
    private Date lastUpdateTime;
    private Observer observer;
    private Lock lock;
    
    public NodeListener() {
        this.prefix = "";
        this.properties = new Properties();
        this.lastUpdateTime = null;
        this.observer = null;
        this.lock = new ReentrantLock();
    }
    
    public abstract List<NodeEvent> refresh();
    
    public abstract void destroy();
    
    public void init() {
        if (this.observer == null) {
            throw new DruidRuntimeException("No Observer(such as PoolUpdater) specified.");
        }
        this.addObserver(this.observer);
    }
    
    public void update() {
        this.update(this.refresh());
    }
    
    public void update(final List<NodeEvent> events) {
        if (events != null && !events.isEmpty()) {
            this.lastUpdateTime = new Date();
            final NodeEvent[] arr = new NodeEvent[events.size()];
            for (int i = 0; i < events.size(); ++i) {
                arr[i] = events.get(i);
            }
            this.setChanged();
            this.notifyObservers(arr);
        }
    }
    
    public Observer getObserver() {
        return this.observer;
    }
    
    public void setObserver(final Observer observer) {
        this.observer = observer;
    }
    
    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    
    public void setLastUpdateTime(final Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    public Properties getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Properties properties) {
        this.properties = properties;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
    
    static {
        LOG = LogFactory.getLog(NodeListener.class);
    }
}
