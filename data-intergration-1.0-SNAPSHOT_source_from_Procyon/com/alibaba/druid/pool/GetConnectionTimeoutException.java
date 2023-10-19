// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.sql.SQLException;

public class GetConnectionTimeoutException extends SQLException
{
    private static final long serialVersionUID = 1L;
    
    public GetConnectionTimeoutException(final String reason) {
        super(reason);
    }
    
    public GetConnectionTimeoutException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
