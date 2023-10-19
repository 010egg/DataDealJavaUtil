// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLArgument extends SQLObjectImpl
{
    private SQLParameter.ParameterType type;
    private SQLExpr expr;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLArgument clone() {
        final SQLArgument x = new SQLArgument();
        x.type = this.type;
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        return x;
    }
    
    public SQLParameter.ParameterType getType() {
        return this.type;
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setType(final SQLParameter.ParameterType type) {
        this.type = type;
    }
    
    public void setExpr(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.expr = x;
    }
}
