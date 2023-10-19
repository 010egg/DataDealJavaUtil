// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.monitor;

import java.util.HashMap;
import java.util.Date;
import java.util.Map;

public class MonitorContext
{
    public static String DEFAULT_DOMAIN;
    private final Map<String, Object> attributes;
    private String domain;
    private String app;
    private String cluster;
    private String host;
    private int pid;
    private Date collectTime;
    private Date startTime;
    
    public MonitorContext() {
        this.attributes = new HashMap<String, Object>();
        this.domain = "default";
        this.app = "default";
        this.cluster = "default";
    }
    
    public Date getCollectTime() {
        return this.collectTime;
    }
    
    public void setCollectTime(final Date collectTime) {
        this.collectTime = collectTime;
    }
    
    public Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }
    
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
    
    public int getPID() {
        return this.pid;
    }
    
    public void setPID(final int pid) {
        this.pid = pid;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public int getPid() {
        return this.pid;
    }
    
    public void setPid(final int pid) {
        this.pid = pid;
    }
    
    public String getDomain() {
        return this.domain;
    }
    
    public void setDomain(final String domain) {
        this.domain = domain;
    }
    
    public String getApp() {
        return this.app;
    }
    
    public void setApp(final String app) {
        this.app = app;
    }
    
    public String getCluster() {
        return this.cluster;
    }
    
    public void setCluster(final String cluster) {
        this.cluster = cluster;
    }
}
