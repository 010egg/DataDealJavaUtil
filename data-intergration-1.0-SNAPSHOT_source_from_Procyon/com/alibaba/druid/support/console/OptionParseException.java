// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.console;

public class OptionParseException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public OptionParseException(final String msg) {
        super(msg);
    }
    
    public OptionParseException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
    public OptionParseException(final Throwable cause) {
        super(cause);
    }
}
