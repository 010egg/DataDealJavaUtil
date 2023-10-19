// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.ArrayList;
import com.alibaba.druid.filter.Filter;
import java.util.List;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;

public class DataSourceProxyConfig
{
    private String rawUrl;
    private String url;
    private String rawDriverClassName;
    private String name;
    private boolean jmx;
    private PasswordCallback passwordCallback;
    private NameCallback userCallback;
    private final List<Filter> filters;
    
    public DataSourceProxyConfig() {
        this.filters = new ArrayList<Filter>();
    }
    
    public boolean isJmxOption() {
        return this.jmx;
    }
    
    public void setJmxOption(final boolean jmx) {
        this.jmx = jmx;
    }
    
    public void setJmxOption(final String jmx) {
        this.jmx = Boolean.parseBoolean(jmx);
    }
    
    public PasswordCallback getPasswordCallback() {
        return this.passwordCallback;
    }
    
    public void setPasswordCallback(final PasswordCallback passwordCallback) {
        this.passwordCallback = passwordCallback;
    }
    
    public NameCallback getUserCallback() {
        return this.userCallback;
    }
    
    public void setUserCallback(final NameCallback userCallback) {
        this.userCallback = userCallback;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public List<Filter> getFilters() {
        return this.filters;
    }
    
    public String getRawUrl() {
        return this.rawUrl;
    }
    
    public void setRawUrl(final String rawUrl) {
        this.rawUrl = rawUrl;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public String getRawDriverClassName() {
        return this.rawDriverClassName;
    }
    
    public void setRawDriverClassName(final String driverClassName) {
        this.rawDriverClassName = driverClassName;
    }
}
