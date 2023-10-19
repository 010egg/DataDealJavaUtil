// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class SQLColumnCheck extends SQLConstraintImpl implements SQLColumnConstraint, SQLReplaceable
{
    private SQLExpr expr;
    protected Boolean enforced;
    
    public SQLColumnCheck() {
    }
    
    public SQLColumnCheck(final SQLExpr expr) {
        this.setExpr(expr);
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }
    
    public Boolean getEnforced() {
        return this.enforced;
    }
    
    public void setEnforced(final Boolean enforced) {
        this.enforced = enforced;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getName());
            this.acceptChild(visitor, this.getExpr());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLColumnCheck clone() {
        final SQLColumnCheck x = new SQLColumnCheck();
        super.cloneTo(x);
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        if (this.getName() == expr) {
            this.setName((SQLName)target);
            return true;
        }
        if (this.getComment() == expr) {
            this.setComment(target);
            return true;
        }
        return false;
    }
}
