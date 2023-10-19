// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLListExpr extends SQLExprImpl implements SQLReplaceable
{
    private final List<SQLExpr> items;
    
    public SQLListExpr() {
        this.items = new ArrayList<SQLExpr>();
    }
    
    public SQLListExpr(final SQLExpr... items) {
        this.items = new ArrayList<SQLExpr>(items.length);
        for (final SQLExpr item : items) {
            item.setParent(this);
            this.items.add(item);
        }
    }
    
    public List<SQLExpr> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.items.size(); ++i) {
                final SQLExpr item = this.items.get(i);
                if (item != null) {
                    item.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.items.hashCode();
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
        final SQLListExpr other = (SQLListExpr)obj;
        return this.items.equals(other.items);
    }
    
    @Override
    public SQLListExpr clone() {
        final SQLListExpr x = new SQLListExpr();
        for (final SQLExpr item : this.items) {
            final SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        return x;
    }
    
    @Override
    public List getChildren() {
        return this.items;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i) == expr) {
                target.setParent(this);
                this.items.set(i, target);
                return true;
            }
        }
        return false;
    }
}
