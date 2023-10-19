// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.Properties;
import javax.security.auth.callback.PasswordCallback;

public class DruidPasswordCallback extends PasswordCallback
{
    private static final long serialVersionUID = 1L;
    private String url;
    private Properties properties;
    
    public DruidPasswordCallback() {
        this("druidDataSource password", false);
    }
    
    public DruidPasswordCallback(final String prompt, final boolean echoOn) {
        super(prompt, echoOn);
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public Properties getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Properties properties) {
        this.properties = properties;
    }
}
