// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.sql.SQLException;

public class WallSQLException extends SQLException
{
    private static final long serialVersionUID = 1L;
    
    public WallSQLException(final String reason, final Throwable cause) {
        super(reason, cause);
    }
    
    public WallSQLException(final String reason) {
        super(reason);
    }
}
