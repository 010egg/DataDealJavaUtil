// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.simplejndi;

import com.alibaba.druid.support.logging.LogFactory;
import java.sql.SQLException;
import java.util.Map;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidDataSource;
import java.util.Properties;
import com.alibaba.druid.support.logging.Log;
import org.osjava.sj.loader.convert.Converter;

public class DruidDataSourceConverter implements Converter
{
    private static final Log LOG;
    
    public Object convert(final Properties properties, final String type) {
        try {
            final DruidDataSource dataSource = new DruidDataSource();
            DruidDataSourceFactory.config(dataSource, properties);
            return dataSource;
        }
        catch (SQLException e) {
            DruidDataSourceConverter.LOG.error("properties:" + properties, e);
            return null;
        }
    }
    
    static {
        LOG = LogFactory.getLog(DruidDataSourceConverter.class);
    }
}
