// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool.vendor;

import com.alibaba.druid.support.logging.LogFactory;
import java.sql.SQLRecoverableException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.HashSet;
import java.util.Set;
import com.alibaba.druid.support.logging.Log;
import java.io.Serializable;
import com.alibaba.druid.pool.ExceptionSorter;

public class OracleExceptionSorter implements ExceptionSorter, Serializable
{
    private static final Log LOG;
    private static final long serialVersionUID = -9146226891418913174L;
    private Set<Integer> fatalErrorCodes;
    
    public OracleExceptionSorter() {
        this.fatalErrorCodes = new HashSet<Integer>();
        this.configFromProperties(System.getProperties());
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
        if (properties == null) {
            return;
        }
        final String property = properties.getProperty("druid.oracle.fatalErrorCodes");
        if (property != null) {
            final String[] split;
            final String[] items = split = property.split("\\,");
            for (final String item : split) {
                if (item != null && item.length() > 0) {
                    try {
                        final int code = Integer.parseInt(item);
                        this.fatalErrorCodes.add(code);
                    }
                    catch (NumberFormatException e) {
                        OracleExceptionSorter.LOG.error("parse druid.oracle.fatalErrorCodes error", e);
                    }
                }
            }
        }
    }
    
    public Set<Integer> getFatalErrorCodes() {
        return this.fatalErrorCodes;
    }
    
    public void setFatalErrorCodes(final Set<Integer> fatalErrorCodes) {
        this.fatalErrorCodes = fatalErrorCodes;
    }
    
    @Override
    public boolean isExceptionFatal(final SQLException e) {
        if (e instanceof SQLRecoverableException) {
            return true;
        }
        final int error_code = Math.abs(e.getErrorCode());
        switch (error_code) {
            case 28:
            case 600:
            case 1012:
            case 1014:
            case 1033:
            case 1034:
            case 1035:
            case 1089:
            case 1090:
            case 1092:
            case 1094:
            case 2396:
            case 3106:
            case 3111:
            case 3113:
            case 3114:
            case 3134:
            case 3135:
            case 3136:
            case 3138:
            case 3142:
            case 3143:
            case 3144:
            case 3145:
            case 3149:
            case 6801:
            case 6802:
            case 6805:
            case 9918:
            case 9920:
            case 9921:
            case 17001:
            case 17002:
            case 17008:
            case 17009:
            case 17024:
            case 17089:
            case 17401:
            case 17409:
            case 17410:
            case 17416:
            case 17438:
            case 17442:
            case 25407:
            case 25408:
            case 25409:
            case 25425:
            case 29276:
            case 30676: {
                return true;
            }
            default: {
                if (error_code >= 12100 && error_code <= 12299) {
                    return true;
                }
                final String error_text = e.getMessage().toUpperCase();
                return ((error_code < 20000 || error_code >= 21000) && (error_text.contains("SOCKET") || error_text.contains("\u5957\u63a5\u5b57") || error_text.contains("CONNECTION HAS ALREADY BEEN CLOSED") || error_text.contains("BROKEN PIPE") || error_text.contains("\u7ba1\u9053\u5df2\u7ed3\u675f"))) || this.fatalErrorCodes.contains(error_code);
            }
        }
    }
    
    static {
        LOG = LogFactory.getLog(OracleExceptionSorter.class);
    }
}
