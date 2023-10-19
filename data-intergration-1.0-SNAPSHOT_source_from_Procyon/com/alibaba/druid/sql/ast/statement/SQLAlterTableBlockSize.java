// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableBlockSize extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLIntegerExpr size;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.size);
        }
        visitor.endVisit(this);
    }
    
    public SQLIntegerExpr getSize() {
        return this.size;
    }
    
    public void setSize(final SQLIntegerExpr size) {
        if (size != null) {
            size.setParent(this);
        }
        this.size = size;
    }
}
