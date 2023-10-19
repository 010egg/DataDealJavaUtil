// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class SQLCheck extends SQLConstraintImpl implements SQLTableElement, SQLTableConstraint, SQLReplaceable
{
    private SQLExpr expr;
    private Boolean enforced;
    
    public SQLCheck() {
    }
    
    public SQLCheck(final SQLExpr expr) {
        this.setExpr(expr);
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.expr = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.getName() != null) {
                this.getName().accept(visitor);
            }
            if (this.expr != null) {
                this.expr.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    public void cloneTo(final SQLCheck x) {
        super.cloneTo(x);
        if (this.expr != null) {
            this.expr = this.expr.clone();
        }
        x.enforced = this.enforced;
    }
    
    @Override
    public SQLCheck clone() {
        final SQLCheck x = new SQLCheck();
        this.cloneTo(x);
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
    
    public Boolean getEnforced() {
        return this.enforced;
    }
    
    public void setEnforced(final Boolean enforced) {
        this.enforced = enforced;
    }
}
