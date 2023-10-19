// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import java.io.InputStream;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import org.slf4j.Logger;

public class HttpConnectionConfig
{
    private static final Logger LOGGER;
    private Integer maxConnection;
    private Integer connectTimeout;
    private Long defaultKeepliveTimeout;
    private static final Properties PROPERTIES;
    private static HttpConnectionConfig connectionPool;
    
    private HttpConnectionConfig() {
    }
    
    public void loadProp() {
        this.maxConnection = Integer.valueOf(HttpConnectionConfig.PROPERTIES.getProperty("httpclient.maxConnection"));
        this.connectTimeout = Integer.valueOf(HttpConnectionConfig.PROPERTIES.getProperty("httpclient.connectTimeout"));
        this.defaultKeepliveTimeout = Long.valueOf(HttpConnectionConfig.PROPERTIES.getProperty("httpclient.defaultKeepliveTimeout"));
    }
    
    public static HttpConnectionConfig getInstance() {
        if (HttpConnectionConfig.connectionPool == null) {
            (HttpConnectionConfig.connectionPool = new HttpConnectionConfig()).loadProp();
        }
        return HttpConnectionConfig.connectionPool;
    }
    
    public Integer getMaxConnection() {
        return this.maxConnection;
    }
    
    public void setMaxConnection(final Integer maxConnection) {
        this.maxConnection = maxConnection;
    }
    
    public Integer getConnectTimeout() {
        return this.connectTimeout;
    }
    
    public void setConnectTimeout(final Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    
    public Long getDefaultKeepliveTimeout() {
        return this.defaultKeepliveTimeout;
    }
    
    public void setDefaultKeepliveTimeout(final Long defaultKeepliveTimeout) {
        this.defaultKeepliveTimeout = defaultKeepliveTimeout;
    }
    
    public static HttpConnectionConfig getConnectionPool() {
        return HttpConnectionConfig.connectionPool;
    }
    
    public static void setConnectionPool(final HttpConnectionConfig connectionPool) {
        HttpConnectionConfig.connectionPool = connectionPool;
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(HttpConnectionConfig.class);
        PROPERTIES = new Properties();
        HttpConnectionConfig.connectionPool = null;
        try {
            final InputStream is = HttpConnectionConfig.class.getClassLoader().getResourceAsStream("data-intergration.properties");
            HttpConnectionConfig.PROPERTIES.load(is);
        }
        catch (IOException e) {
            HttpConnectionConfig.LOGGER.error(e.getMessage());
        }
    }
}
