// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLValuesExpr extends SQLExprImpl implements SQLReplaceable
{
    private List<SQLListExpr> values;
    
    public SQLValuesExpr() {
        this.values = new ArrayList<SQLListExpr>();
    }
    
    public List<SQLListExpr> getValues() {
        return this.values;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.values);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLValuesExpr that = (SQLValuesExpr)o;
        return this.values.equals(that.values);
    }
    
    @Override
    public int hashCode() {
        return this.values.hashCode();
    }
    
    @Override
    public SQLExpr clone() {
        final SQLValuesExpr x = new SQLValuesExpr();
        for (final SQLListExpr value : this.values) {
            final SQLListExpr value2 = value.clone();
            value2.setParent(x);
            x.values.add(value2);
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = 0; i < this.values.size(); ++i) {
            if (this.values.get(i) == expr) {
                target.setParent(this);
                this.values.set(i, (SQLListExpr)target);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List getChildren() {
        return this.values;
    }
}
