// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.expr;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;

public class PGTypeCastExpr extends SQLCastExpr implements PGExpr
{
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.dataType);
        }
        visitor.endVisit(this);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            this.accept0((PGASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    @Override
    public String toString() {
        return SQLUtils.toPGString(this);
    }
}
