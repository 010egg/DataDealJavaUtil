// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class OracleIsOfTypeExpr extends SQLExprImpl implements OracleExpr, SQLReplaceable
{
    private SQLExpr expr;
    private List<SQLExpr> types;
    
    public OracleIsOfTypeExpr() {
        this.types = new ArrayList<SQLExpr>();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final OracleIsOfTypeExpr that = (OracleIsOfTypeExpr)o;
        if (this.expr != null) {
            if (this.expr.equals(that.expr)) {
                return (this.types != null) ? this.types.equals(that.types) : (that.types == null);
            }
        }
        else if (that.expr == null) {
            return (this.types != null) ? this.types.equals(that.types) : (that.types == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.expr != null) ? this.expr.hashCode() : 0;
        result = 31 * result + ((this.types != null) ? this.types.hashCode() : 0);
        return result;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.types);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        for (int i = 0; i < this.types.size(); ++i) {
            if (this.types.get(i) == expr) {
                target.setParent(this);
                this.types.set(i, target);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public SQLExpr clone() {
        final OracleIsOfTypeExpr x = new OracleIsOfTypeExpr();
        if (this.expr != null) {
            x.setExpr(this.expr);
        }
        return null;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List children = new ArrayList();
        if (this.expr != null) {
            children.add(this.expr);
        }
        children.addAll(this.types);
        return (List<SQLObject>)children;
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
    
    public List<SQLExpr> getTypes() {
        return this.types;
    }
}
