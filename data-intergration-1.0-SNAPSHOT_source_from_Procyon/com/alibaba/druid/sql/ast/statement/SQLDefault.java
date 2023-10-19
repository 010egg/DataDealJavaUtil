// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class SQLDefault extends SQLConstraintImpl implements SQLTableElement, SQLTableConstraint, SQLReplaceable
{
    private SQLExpr expr;
    private SQLExpr column;
    private boolean withValues;
    
    public SQLDefault() {
        this.withValues = false;
    }
    
    public SQLDefault(final SQLExpr expr, final SQLExpr column) {
        this.withValues = false;
        this.setExpr(expr);
        this.setColumn(column);
    }
    
    public SQLExpr getColumn() {
        return this.column;
    }
    
    public void setColumn(final SQLExpr column) {
        this.column = column;
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
    
    public void cloneTo(final SQLDefault x) {
        super.cloneTo(x);
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        if (this.column != null) {
            x.setColumn(this.column.clone());
        }
        x.setWithValues(x.isWithValues());
    }
    
    @Override
    public SQLDefault clone() {
        final SQLDefault x = new SQLDefault();
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
    
    public boolean isWithValues() {
        return this.withValues;
    }
    
    public void setWithValues(final boolean withValues) {
        this.withValues = withValues;
    }
}
