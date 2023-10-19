// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.util.Properties;
import java.sql.SQLRecoverableException;
import java.sql.SQLException;
import com.alibaba.druid.pool.ExceptionSorter;

public class PGExceptionSorter implements ExceptionSorter
{
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        if (e instanceof SQLRecoverableException) {
            return true;
        }
        final String sqlState = e.getSQLState();
        return sqlState != null && sqlState.startsWith("08");
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
}
