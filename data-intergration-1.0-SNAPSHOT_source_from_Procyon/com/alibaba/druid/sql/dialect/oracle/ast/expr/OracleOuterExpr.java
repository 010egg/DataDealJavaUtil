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
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class OracleOuterExpr extends SQLExprImpl implements OracleExpr, SQLReplaceable
{
    private SQLExpr expr;
    
    public OracleOuterExpr() {
    }
    
    public OracleOuterExpr(final SQLExpr expr) {
        this.expr = expr;
    }
    
    @Override
    public OracleOuterExpr clone() {
        final OracleOuterExpr x = new OracleOuterExpr();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        return x;
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        this.expr = expr;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        return false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor)visitor);
        }
        else {
            this.acceptChild(visitor, this.expr);
        }
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.expr);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final OracleOuterExpr other = (OracleOuterExpr)obj;
        if (this.expr == null) {
            if (other.expr != null) {
                return false;
            }
        }
        else if (!this.expr.equals(other.expr)) {
            return false;
        }
        return true;
    }
}
