// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLJSONValueExpr extends SQLExprImpl
{
    private SQLExpr json;
    private SQLExpr path;
    
    @Override
    public boolean equals(final Object o) {
        return false;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
    }
    
    @Override
    public SQLExpr clone() {
        return null;
    }
}
