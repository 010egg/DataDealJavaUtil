// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.monitor.entity;

import java.util.Date;

public class MonitorInstance
{
    private long id;
    private String domain;
    private String app;
    private String cluster;
    private String host;
    private String ip;
    private Date lastActiveTime;
    private Long lastPID;
    
    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
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
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public String getIp() {
        return this.ip;
    }
    
    public void setIp(final String ip) {
        this.ip = ip;
    }
    
    public Date getLastActiveTime() {
        return this.lastActiveTime;
    }
    
    public void setLastActiveTime(final Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }
    
    public Long getLastPID() {
        return this.lastPID;
    }
    
    public void setLastPID(final Long lastPID) {
        this.lastPID = lastPID;
    }
}
