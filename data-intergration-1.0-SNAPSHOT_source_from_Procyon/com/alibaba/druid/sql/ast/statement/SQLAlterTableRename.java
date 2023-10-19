// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableRename extends SQLObjectImpl implements SQLAlterTableItem
{
    protected SQLExprTableSource to;
    
    public SQLAlterTableRename() {
    }
    
    public SQLAlterTableRename(final SQLExpr to) {
        this.setTo(to);
    }
    
    public SQLExprTableSource getTo() {
        return this.to;
    }
    
    public SQLName getToName() {
        if (this.to == null) {
            return null;
        }
        final SQLExpr expr = this.to.expr;
        if (expr instanceof SQLName) {
            return (SQLName)expr;
        }
        return null;
    }
    
    public void setTo(final SQLExprTableSource to) {
        if (to != null) {
            to.setParent(this);
        }
        this.to = to;
    }
    
    public void setTo(final SQLExpr to) {
        this.setTo(new SQLExprTableSource(to));
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.to);
        }
        visitor.endVisit(this);
    }
}
