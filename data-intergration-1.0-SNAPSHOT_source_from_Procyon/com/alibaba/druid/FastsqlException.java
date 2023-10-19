// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid;

public class FastsqlException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public FastsqlException() {
    }
    
    public FastsqlException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public FastsqlException(final String message) {
        super(message);
    }
    
    public FastsqlException(final Throwable cause) {
        super(cause);
    }
}
