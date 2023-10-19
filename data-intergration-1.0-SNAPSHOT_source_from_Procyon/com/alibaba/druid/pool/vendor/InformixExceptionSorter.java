// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import java.util.Properties;
import java.sql.SQLRecoverableException;
import java.sql.SQLException;
import java.io.Serializable;
import com.alibaba.druid.pool.ExceptionSorter;

public class InformixExceptionSorter implements ExceptionSorter, Serializable
{
    private static final long serialVersionUID = -5175884111768095263L;
    
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        if (e instanceof SQLRecoverableException) {
            return true;
        }
        switch (e.getErrorCode()) {
            case -79879:
            case -79837:
            case -79836:
            case -79812:
            case -79811:
            case -79788:
            case -79760:
            case -79759:
            case -79758:
            case -79757:
            case -79756:
            case -79736:
            case -79735:
            case -79734:
            case -79730:
            case -79716:
            case -710: {
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
