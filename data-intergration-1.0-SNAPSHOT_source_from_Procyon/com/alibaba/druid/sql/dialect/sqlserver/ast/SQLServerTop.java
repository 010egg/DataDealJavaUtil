// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast;

import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;

public class SQLServerTop extends SQLServerObjectImpl
{
    private SQLExpr expr;
    private boolean percent;
    private boolean withTies;
    
    public SQLServerTop() {
    }
    
    public SQLServerTop(final SQLExpr expr) {
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
    
    public void setExpr(final int expr) {
        this.setExpr(new SQLIntegerExpr(expr));
    }
    
    public boolean isPercent() {
        return this.percent;
    }
    
    public void setPercent(final boolean percent) {
        this.percent = percent;
    }
    
    public boolean isWithTies() {
        return this.withTies;
    }
    
    public void setWithTies(final boolean withTies) {
        this.withTies = withTies;
    }
    
    @Override
    public void accept0(final SQLServerASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public SQLServerTop clone() {
        final SQLServerTop x = new SQLServerTop();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        x.percent = this.percent;
        x.withTies = this.withTies;
        return x;
    }
}
