// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLNullConstraint extends SQLConstraintImpl implements SQLColumnConstraint
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public SQLNullConstraint clone() {
        final SQLNullConstraint x = new SQLNullConstraint();
        super.cloneTo(x);
        return x;
    }
}
