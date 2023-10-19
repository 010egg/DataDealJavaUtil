// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableCoalescePartition extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLExpr count;
    
    public SQLExpr getCount() {
        return this.count;
    }
    
    public void setCount(final SQLExpr count) {
        if (count != null) {
            count.setParent(this);
        }
        this.count = count;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.count);
        }
        visitor.endVisit(this);
    }
}
