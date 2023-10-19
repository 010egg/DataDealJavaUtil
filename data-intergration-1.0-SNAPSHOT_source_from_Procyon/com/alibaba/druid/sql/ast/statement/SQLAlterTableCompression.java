// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableCompression extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLCharExpr name;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public SQLCharExpr getName() {
        return this.name;
    }
    
    public void setName(final SQLCharExpr name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
}
