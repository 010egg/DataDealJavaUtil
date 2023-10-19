// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public abstract class SQLSelectQueryBase extends SQLObjectImpl implements SQLSelectQuery
{
    protected boolean parenthesized;
    
    @Override
    public boolean isParenthesized() {
        return this.parenthesized;
    }
    
    @Override
    public void setParenthesized(final boolean parenthesized) {
        this.parenthesized = parenthesized;
    }
    
    @Override
    public abstract SQLSelectQueryBase clone();
}
