// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableSetLifecycle extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLExpr lifecycle;
    
    public SQLExpr getLifecycle() {
        return this.lifecycle;
    }
    
    public void setLifecycle(final SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.lifecycle = comment;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.lifecycle);
        }
        visitor.endVisit(this);
    }
}
