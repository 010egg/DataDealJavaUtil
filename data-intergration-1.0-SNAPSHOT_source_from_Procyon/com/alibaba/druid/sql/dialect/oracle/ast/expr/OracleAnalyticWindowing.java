// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class OracleAnalyticWindowing extends SQLObjectImpl implements OracleExpr
{
    private Type type;
    private SQLExpr expr;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
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
    
    public Type getType() {
        return this.type;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }
    
    @Override
    public OracleAnalyticWindowing clone() {
        final OracleAnalyticWindowing x = new OracleAnalyticWindowing();
        x.type = this.type;
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        return x;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.expr);
    }
    
    public enum Type
    {
        ROWS, 
        RANGE;
    }
}
