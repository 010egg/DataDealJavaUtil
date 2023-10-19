// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTablePartitionCount extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLIntegerExpr count;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.count);
        }
        visitor.endVisit(this);
    }
    
    public SQLIntegerExpr getCount() {
        return this.count;
    }
    
    public void setCount(final SQLIntegerExpr count) {
        if (count != null) {
            count.setParent(this);
        }
        this.count = count;
    }
}
