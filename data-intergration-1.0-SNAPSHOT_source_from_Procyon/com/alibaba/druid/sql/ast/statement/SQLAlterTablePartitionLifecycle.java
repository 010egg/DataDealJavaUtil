// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTablePartitionLifecycle extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLIntegerExpr lifecycle;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.lifecycle);
        }
        visitor.endVisit(this);
    }
    
    public SQLIntegerExpr getLifecycle() {
        return this.lifecycle;
    }
    
    public void setLifecycle(final SQLIntegerExpr lifecycle) {
        if (lifecycle != null) {
            lifecycle.setParent(this);
        }
        this.lifecycle = lifecycle;
    }
}
