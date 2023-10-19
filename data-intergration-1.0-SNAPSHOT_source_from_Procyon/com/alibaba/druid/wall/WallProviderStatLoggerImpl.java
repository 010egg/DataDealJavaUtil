// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.Map;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.support.logging.LogFactory;
import java.util.Properties;
import com.alibaba.druid.support.logging.Log;

public class WallProviderStatLoggerImpl extends WallProviderStatLoggerAdapter implements WallProviderStatLogger
{
    private static Log LOG;
    private Log logger;
    
    public WallProviderStatLoggerImpl() {
        this.logger = WallProviderStatLoggerImpl.LOG;
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
        if (properties == null) {
            return;
        }
        final String property = properties.getProperty("druid.stat.loggerName");
        if (property != null && property.length() > 0) {
            this.setLoggerName(property);
        }
    }
    
    public void setLoggerName(final String loggerName) {
        this.logger = LogFactory.getLog(loggerName);
    }
    
    public void setLogger(final Log logger) {
        if (logger == null) {
            throw new IllegalArgumentException("logger can not be null");
        }
        this.logger = logger;
    }
    
    public boolean isLogEnable() {
        return this.logger.isInfoEnabled();
    }
    
    public void log(final String value) {
        this.logger.info(value);
    }
    
    @Override
    public void log(final WallProviderStatValue statValue) {
        if (!this.isLogEnable()) {
            return;
        }
        final Map<String, Object> map = statValue.toMap();
        final String text = JSONUtils.toJSONString(map);
        this.log(text);
    }
    
    static {
        WallProviderStatLoggerImpl.LOG = LogFactory.getLog(WallProviderStatLoggerImpl.class);
    }
}
