// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLExprHint extends SQLObjectImpl implements SQLHint
{
    private SQLExpr expr;
    
    public SQLExprHint() {
    }
    
    public SQLExprHint(final SQLExpr expr) {
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
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLExprHint clone() {
        final SQLExprHint x = new SQLExprHint();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        return x;
    }
}
