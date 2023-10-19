// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Collection;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLArrayExpr extends SQLExprImpl implements SQLReplaceable
{
    private SQLExpr expr;
    private List<SQLExpr> values;
    
    public SQLArrayExpr() {
        this.values = new ArrayList<SQLExpr>();
    }
    
    @Override
    public SQLArrayExpr clone() {
        final SQLArrayExpr x = new SQLArrayExpr();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        for (final SQLExpr value : this.values) {
            final SQLExpr value2 = value.clone();
            value2.setParent(x);
            x.values.add(value2);
        }
        return x;
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        this.expr = expr;
    }
    
    public List<SQLExpr> getValues() {
        return this.values;
    }
    
    public void setValues(final List<SQLExpr> values) {
        this.values = values;
        if (values != null) {
            for (final SQLExpr value : values) {
                value.setParent(this);
            }
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.expr != null) {
                this.expr.accept(visitor);
            }
            if (this.values != null) {
                for (final SQLExpr value : this.values) {
                    if (value != null) {
                        value.accept(visitor);
                    }
                }
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.add(this.expr);
        children.addAll(this.values);
        return children;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
        result = 31 * result + ((this.values == null) ? 0 : this.values.hashCode());
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
        final SQLArrayExpr other = (SQLArrayExpr)obj;
        if (this.expr == null) {
            if (other.expr != null) {
                return false;
            }
        }
        else if (!this.expr.equals(other.expr)) {
            return false;
        }
        if (this.values == null) {
            if (other.values != null) {
                return false;
            }
        }
        else if (!this.values.equals(other.values)) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = 0; i < this.values.size(); ++i) {
            if (this.values.get(i) == expr) {
                target.setParent(this);
                this.values.set(i, target);
                return true;
            }
        }
        return false;
    }
}
