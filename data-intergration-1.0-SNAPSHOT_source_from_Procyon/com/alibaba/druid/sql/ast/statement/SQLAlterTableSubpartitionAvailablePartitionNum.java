// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableSubpartitionAvailablePartitionNum extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLIntegerExpr number;
    
    public SQLIntegerExpr getNumber() {
        return this.number;
    }
    
    public void setNumber(final SQLIntegerExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.number = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.number);
        }
        visitor.endVisit(this);
    }
}
