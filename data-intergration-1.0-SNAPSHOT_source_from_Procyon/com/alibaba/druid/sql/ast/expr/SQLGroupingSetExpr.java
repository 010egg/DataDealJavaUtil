// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLGroupingSetExpr extends SQLExprImpl implements SQLReplaceable
{
    private final List<SQLExpr> parameters;
    
    public SQLGroupingSetExpr() {
        this.parameters = new ArrayList<SQLExpr>();
    }
    
    @Override
    public SQLGroupingSetExpr clone() {
        final SQLGroupingSetExpr x = new SQLGroupingSetExpr();
        for (final SQLExpr p : this.parameters) {
            final SQLExpr p2 = p.clone();
            p2.setParent(x);
            x.parameters.add(p2);
        }
        return x;
    }
    
    public List<SQLExpr> getParameters() {
        return this.parameters;
    }
    
    public void addParameter(final SQLExpr parameter) {
        if (parameter != null) {
            parameter.setParent(this);
        }
        this.parameters.add(parameter);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.parameters);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return this.parameters;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.parameters.hashCode();
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
        if (!(obj instanceof SQLGroupingSetExpr)) {
            return false;
        }
        final SQLGroupingSetExpr other = (SQLGroupingSetExpr)obj;
        return this.parameters.equals(other.parameters);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = 0; i < this.parameters.size(); ++i) {
            if (this.parameters.get(i) == expr) {
                target.setParent(this);
                this.parameters.set(i, target);
                return true;
            }
        }
        return false;
    }
}
