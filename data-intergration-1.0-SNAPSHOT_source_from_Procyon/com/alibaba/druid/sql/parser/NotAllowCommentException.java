// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

public class NotAllowCommentException extends ParserException
{
    private static final long serialVersionUID = 1L;
    
    public NotAllowCommentException() {
        this("comment not allow");
    }
    
    public NotAllowCommentException(final String message, final Throwable e) {
        super(message, e);
    }
    
    public NotAllowCommentException(final String message) {
        super(message);
    }
}
