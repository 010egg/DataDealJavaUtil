// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;

public interface SQLSelectQuery extends SQLObject
{
    boolean isParenthesized();
    
    void setParenthesized(final boolean p0);
    
    SQLSelectQuery clone();
}
