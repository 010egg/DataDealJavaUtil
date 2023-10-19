// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import com.alibaba.druid.support.logging.Log;
import java.util.Properties;

public interface DruidDataSourceStatLogger
{
    void log(final DruidDataSourceStatValue p0);
    
    void configFromProperties(final Properties p0);
    
    void setLogger(final Log p0);
    
    void setLoggerName(final String p0);
}
