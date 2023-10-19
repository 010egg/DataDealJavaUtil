// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.SQLException;

public class DataSourceDisableException extends SQLException
{
    private static final long serialVersionUID = 1L;
    
    public DataSourceDisableException() {
    }
    
    public DataSourceDisableException(final String reason) {
        super(reason);
    }
    
    public DataSourceDisableException(final Throwable cause) {
        super(cause);
    }
}
