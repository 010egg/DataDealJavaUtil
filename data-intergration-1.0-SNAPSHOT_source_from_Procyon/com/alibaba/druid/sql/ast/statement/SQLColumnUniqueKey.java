// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class SQLColumnUniqueKey extends SQLConstraintImpl implements SQLColumnConstraint, SQLReplaceable
{
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLColumnUniqueKey clone() {
        final SQLColumnUniqueKey x = new SQLColumnUniqueKey();
        super.cloneTo(x);
        return x;
    }
}
