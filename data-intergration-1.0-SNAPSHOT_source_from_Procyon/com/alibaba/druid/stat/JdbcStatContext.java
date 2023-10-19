// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.stat;

public class JdbcStatContext
{
    private String name;
    private String file;
    private String sql;
    private String requestId;
    private boolean traceEnable;
    
    public boolean isTraceEnable() {
        return this.traceEnable;
    }
    
    public void setTraceEnable(final boolean traceEnable) {
        this.traceEnable = traceEnable;
    }
    
    public String getRequestId() {
        return this.requestId;
    }
    
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getFile() {
        return this.file;
    }
    
    public void setFile(final String file) {
        this.file = file;
    }
    
    public String getSql() {
        return this.sql;
    }
    
    public void setSql(final String sql) {
        this.sql = sql;
    }
}
