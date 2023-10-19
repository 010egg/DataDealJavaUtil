// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableDisableConstraint extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLName constraintName;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.constraintName);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getConstraintName() {
        return this.constraintName;
    }
    
    public void setConstraintName(final SQLName constraintName) {
        this.constraintName = constraintName;
    }
}
