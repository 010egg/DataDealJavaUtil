// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableAddConstraint extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLConstraint constraint;
    private boolean withNoCheck;
    
    public SQLAlterTableAddConstraint() {
        this.withNoCheck = false;
    }
    
    public SQLAlterTableAddConstraint(final SQLConstraint constraint) {
        this.withNoCheck = false;
        this.setConstraint(constraint);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.constraint);
        }
        visitor.endVisit(this);
    }
    
    public SQLConstraint getConstraint() {
        return this.constraint;
    }
    
    public void setConstraint(final SQLConstraint constraint) {
        if (constraint != null) {
            constraint.setParent(this);
        }
        this.constraint = constraint;
    }
    
    public boolean isWithNoCheck() {
        return this.withNoCheck;
    }
    
    public void setWithNoCheck(final boolean withNoCheck) {
        this.withNoCheck = withNoCheck;
    }
}
