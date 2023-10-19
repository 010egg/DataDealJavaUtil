// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid;

public class DruidRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public DruidRuntimeException() {
    }
    
    public DruidRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DruidRuntimeException(final String message) {
        super(message);
    }
    
    public DruidRuntimeException(final Throwable cause) {
        super(cause);
    }
}
