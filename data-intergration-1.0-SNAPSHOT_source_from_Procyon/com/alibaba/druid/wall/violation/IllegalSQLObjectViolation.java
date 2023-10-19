// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall.violation;

import com.alibaba.druid.wall.Violation;

public class IllegalSQLObjectViolation implements Violation
{
    private final String message;
    private final String sqlPart;
    private final int errorCode;
    
    public IllegalSQLObjectViolation(final int errorCode, final String message, final String sqlPart) {
        this.errorCode = errorCode;
        this.message = message;
        this.sqlPart = sqlPart;
    }
    
    public String getSqlPart() {
        return this.sqlPart;
    }
    
    @Override
    public String toString() {
        return this.sqlPart;
    }
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public int getErrorCode() {
        return this.errorCode;
    }
}
