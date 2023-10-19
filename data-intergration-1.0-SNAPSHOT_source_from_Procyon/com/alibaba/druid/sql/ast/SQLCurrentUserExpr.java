// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLCurrentUserExpr extends SQLExprImpl
{
    @Override
    protected void accept0(final SQLASTVisitor v) {
        v.visit(this);
        v.endVisit(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && (this == o || this.getClass() == o.getClass());
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public SQLCurrentUserExpr clone() {
        return new SQLCurrentUserExpr();
    }
}
