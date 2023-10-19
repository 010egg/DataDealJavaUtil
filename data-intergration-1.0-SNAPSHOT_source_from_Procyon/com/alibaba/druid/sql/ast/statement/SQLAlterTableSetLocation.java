// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableSetLocation extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLExpr location;
    
    public SQLExpr getLocation() {
        return this.location;
    }
    
    public void setLocation(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.location = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.location);
        }
        visitor.endVisit(this);
    }
}
