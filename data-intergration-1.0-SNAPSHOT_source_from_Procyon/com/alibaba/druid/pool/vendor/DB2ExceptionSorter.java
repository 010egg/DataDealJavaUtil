// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.util.Properties;
import java.sql.SQLRecoverableException;
import java.sql.SQLException;
import com.alibaba.druid.pool.ExceptionSorter;

public class DB2ExceptionSorter implements ExceptionSorter
{
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        if (e instanceof SQLRecoverableException) {
            return true;
        }
        final String sqlState = e.getSQLState();
        if (sqlState != null && sqlState.startsWith("08")) {
            return true;
        }
        final int errorCode = e.getErrorCode();
        switch (errorCode) {
            case -924:
            case -918:
            case -909:
            case -525:
            case -518:
            case -516:
            case -514:
            case -512: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
}
