// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.http;

import javax.servlet.ServletContextEvent;
import com.alibaba.druid.support.monitor.MonitorClient;
import javax.servlet.ServletContextListener;

public class MonitorClientContextListener implements ServletContextListener
{
    private MonitorClient client;
    
    public void contextInitialized(final ServletContextEvent event) {
        (this.client = new MonitorClient()).start();
    }
    
    public void contextDestroyed(final ServletContextEvent event) {
        if (this.client != null) {
            this.client.stop();
            this.client = null;
        }
    }
}
