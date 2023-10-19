// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.ha.node;

public class ZookeeperNodeInfo
{
    private String prefix;
    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;
    
    public ZookeeperNodeInfo() {
        this.prefix = "";
    }
    
    public void setPrefix(final String prefix) {
        if (prefix != null && !prefix.trim().isEmpty()) {
            this.prefix = prefix;
            if (!prefix.endsWith(".")) {
                this.prefix = prefix + ".";
            }
        }
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public Integer getPort() {
        return this.port;
    }
    
    public void setPort(final Integer port) {
        this.port = port;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final String database) {
        this.database = database;
    }
}
