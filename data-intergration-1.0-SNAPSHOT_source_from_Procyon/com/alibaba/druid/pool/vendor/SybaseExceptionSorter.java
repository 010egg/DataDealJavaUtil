// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.util.Properties;
import java.sql.SQLRecoverableException;
import java.sql.SQLException;
import java.io.Serializable;
import com.alibaba.druid.pool.ExceptionSorter;

public class SybaseExceptionSorter implements ExceptionSorter, Serializable
{
    private static final long serialVersionUID = 2742592563671255116L;
    
    public SybaseExceptionSorter() {
        this.configFromProperties(System.getProperties());
    }
    
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        if (e instanceof SQLRecoverableException) {
            return true;
        }
        boolean result = false;
        String errorText = e.getMessage();
        if (errorText == null) {
            return false;
        }
        errorText = errorText.toUpperCase();
        if (errorText.contains("JZ0C0") || errorText.contains("JZ0C1")) {
            result = true;
        }
        return result;
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
    }
}
