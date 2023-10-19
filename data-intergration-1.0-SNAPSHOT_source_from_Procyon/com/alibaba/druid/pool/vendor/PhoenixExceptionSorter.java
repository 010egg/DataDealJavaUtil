// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Properties;
import java.sql.SQLException;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.pool.ExceptionSorter;

public class PhoenixExceptionSorter implements ExceptionSorter
{
    private static final Log LOG;
    
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        if (e.getMessage().contains("Connection is null or closed")) {
            PhoenixExceptionSorter.LOG.error("\u5254\u9664phoenix\u4e0d\u53ef\u7528\u7684\u8fde\u63a5", e);
            return true;
        }
        return false;
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
    
    static {
        LOG = LogFactory.getLog(OracleExceptionSorter.class);
    }
}
