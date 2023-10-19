// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.node;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Iterator;
import java.util.Properties;
import com.alibaba.druid.pool.ha.PropertiesUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import com.alibaba.druid.support.logging.Log;

public class FileNodeListener extends NodeListener
{
    private static final Log LOG;
    private Lock lock;
    private String file;
    private int intervalSeconds;
    private ScheduledExecutorService executor;
    
    public FileNodeListener() {
        this.lock = new ReentrantLock();
        this.file = null;
        this.intervalSeconds = 60;
    }
    
    @Override
    public void init() {
        super.init();
        if (this.intervalSeconds <= 0) {
            this.intervalSeconds = 60;
        }
        (this.executor = Executors.newScheduledThreadPool(1)).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                FileNodeListener.LOG.debug("Checking file " + FileNodeListener.this.file + " every " + FileNodeListener.this.intervalSeconds + "s.");
                if (!FileNodeListener.this.lock.tryLock()) {
                    FileNodeListener.LOG.info("Can not acquire the lock, skip this time.");
                    return;
                }
                try {
                    FileNodeListener.this.update();
                }
                catch (Exception e) {
                    FileNodeListener.LOG.error("Can NOT update the node list.", e);
                }
                finally {
                    FileNodeListener.this.lock.unlock();
                }
            }
        }, this.intervalSeconds, this.intervalSeconds, TimeUnit.SECONDS);
    }
    
    @Override
    public List<NodeEvent> refresh() {
        final Properties originalProperties = PropertiesUtils.loadProperties(this.file);
        final List<String> nameList = PropertiesUtils.loadNameList(originalProperties, this.getPrefix());
        final Properties properties = new Properties();
        for (final String n : nameList) {
            final String url = originalProperties.getProperty(n + ".url");
            final String username = originalProperties.getProperty(n + ".username");
            final String password = originalProperties.getProperty(n + ".password");
            if (url == null || url.isEmpty()) {
                FileNodeListener.LOG.warn(n + ".url is EMPTY! IGNORE!");
            }
            else {
                properties.setProperty(n + ".url", url);
                if (username == null || username.isEmpty()) {
                    FileNodeListener.LOG.debug(n + ".username is EMPTY. Maybe you should check the config.");
                }
                else {
                    properties.setProperty(n + ".username", username);
                }
                if (password == null || password.isEmpty()) {
                    FileNodeListener.LOG.debug(n + ".password is EMPTY. Maybe you should check the config.");
                }
                else {
                    properties.setProperty(n + ".password", password);
                }
            }
        }
        final List<NodeEvent> events = NodeEvent.getEventsByDiffProperties(this.getProperties(), properties);
        if (events != null && !events.isEmpty()) {
            FileNodeListener.LOG.info(events.size() + " different(s) detected.");
            this.setProperties(properties);
        }
        return events;
    }
    
    @Override
    public void destroy() {
        if (this.executor == null || this.executor.isShutdown()) {
            return;
        }
        try {
            this.executor.shutdown();
        }
        catch (Exception e) {
            FileNodeListener.LOG.error("Can NOT shutdown the ScheduledExecutorService.", e);
        }
    }
    
    public int getIntervalSeconds() {
        return this.intervalSeconds;
    }
    
    public void setIntervalSeconds(final int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }
    
    public String getFile() {
        return this.file;
    }
    
    public void setFile(final String file) {
        this.file = file;
    }
    
    static {
        LOG = LogFactory.getLog(FileNodeListener.class);
    }
}
