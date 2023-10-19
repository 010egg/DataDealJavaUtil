// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.util.Properties;
import java.net.SocketTimeoutException;
import java.sql.SQLRecoverableException;
import java.sql.SQLException;
import com.alibaba.druid.pool.ExceptionSorter;

public class MySqlExceptionSorter implements ExceptionSorter
{
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        if (e instanceof SQLRecoverableException) {
            return true;
        }
        final String sqlState = e.getSQLState();
        final int errorCode = e.getErrorCode();
        if (sqlState != null && sqlState.startsWith("08")) {
            return true;
        }
        switch (errorCode) {
            case 1004:
            case 1005:
            case 1015:
            case 1021:
            case 1023:
            case 1037:
            case 1038:
            case 1040:
            case 1041:
            case 1042:
            case 1043:
            case 1045:
            case 1047:
            case 1081:
            case 1129:
            case 1130:
            case 1142:
            case 1227:
            case 1290: {
                return true;
            }
            default: {
                if (errorCode >= -9000 && errorCode <= -8000) {
                    return true;
                }
                String className = e.getClass().getName();
                if (className.endsWith(".CommunicationsException")) {
                    return true;
                }
                final String message = e.getMessage();
                if (message != null && message.length() > 0) {
                    if (message.startsWith("Streaming result set com.mysql.jdbc.RowDataDynamic") && message.endsWith("is still active. No statements may be issued when any streaming result sets are open and in use on a given connection. Ensure that you have called .close() on any active streaming result sets before attempting more queries.")) {
                        return true;
                    }
                    final String errorText = message.toUpperCase();
                    if ((errorCode == 0 && errorText.contains("COMMUNICATIONS LINK FAILURE")) || errorText.contains("COULD NOT CREATE CONNECTION") || errorText.contains("NO DATASOURCE") || errorText.contains("NO ALIVE DATASOURCE")) {
                        return true;
                    }
                }
                Throwable cause = e.getCause();
                for (int i = 0; i < 5 && cause != null; cause = cause.getCause(), ++i) {
                    if (cause instanceof SocketTimeoutException) {
                        return true;
                    }
                    className = cause.getClass().getName();
                    if (className.endsWith(".CommunicationsException")) {
                        return true;
                    }
                }
                return false;
            }
        }
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
}
