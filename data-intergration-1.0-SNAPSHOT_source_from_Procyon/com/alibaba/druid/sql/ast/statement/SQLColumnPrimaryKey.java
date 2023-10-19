// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLColumnPrimaryKey extends SQLConstraintImpl implements SQLColumnConstraint
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLColumnPrimaryKey clone() {
        final SQLColumnPrimaryKey x = new SQLColumnPrimaryKey();
        super.cloneTo(x);
        return x;
    }
}
