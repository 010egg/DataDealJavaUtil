// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public abstract class PGExprImpl extends SQLExprImpl implements PGExpr
{
    @Override
    public abstract void accept0(final PGASTVisitor p0);
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor)visitor);
    }
    
    @Override
    public String toString() {
        return SQLUtils.toPGString(this);
    }
}
