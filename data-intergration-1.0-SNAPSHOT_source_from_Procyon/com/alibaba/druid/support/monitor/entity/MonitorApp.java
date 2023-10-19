// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.monitor.entity;

public class MonitorApp
{
    private long id;
    private String domain;
    private String app;
    
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
}
