// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import java.io.Serializable;
import com.alibaba.druid.FastsqlException;

public class ParserException extends FastsqlException implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int line;
    private int column;
    
    public ParserException() {
    }
    
    public ParserException(final String message) {
        super(message);
    }
    
    public ParserException(final String message, final Throwable e) {
        super(message, e);
    }
    
    public ParserException(final String message, final int line, final int column) {
        super(message);
        this.line = line;
        this.column = column;
    }
    
    public ParserException(final Throwable ex, final String ksql) {
        super("parse error. detail message is :\n" + ex.getMessage() + "\nsource sql is : \n" + ksql, ex);
    }
}
