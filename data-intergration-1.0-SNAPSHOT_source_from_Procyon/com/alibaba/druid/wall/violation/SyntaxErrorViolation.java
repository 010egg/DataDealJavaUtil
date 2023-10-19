// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.violation;

import com.alibaba.druid.wall.Violation;

public class SyntaxErrorViolation implements Violation
{
    private final Exception exception;
    private final String sql;
    
    public SyntaxErrorViolation(final Exception exception, final String sql) {
        this.exception = exception;
        this.sql = sql;
    }
    
    @Override
    public String toString() {
        return this.sql;
    }
    
    public Exception getException() {
        return this.exception;
    }
    
    public String getSql() {
        return this.sql;
    }
    
    @Override
    public String getMessage() {
        if (this.exception == null) {
            return "syntax error";
        }
        return "syntax error: " + this.exception.getMessage();
    }
    
    @Override
    public int getErrorCode() {
        return 1001;
    }
}
