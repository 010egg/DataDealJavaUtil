// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLNotNullConstraint extends SQLConstraintImpl implements SQLColumnConstraint
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public SQLNotNullConstraint clone() {
        final SQLNotNullConstraint x = new SQLNotNullConstraint();
        super.cloneTo(x);
        return x;
    }
}
