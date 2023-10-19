// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.SQLException;

public class DataSourceClosedException extends SQLException
{
    private static final long serialVersionUID = 1L;
    
    public DataSourceClosedException(final String reason) {
        super(reason);
    }
}
