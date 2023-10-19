// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.List;

public final class SQLOrderBy extends SQLObjectImpl implements SQLReplaceable
{
    protected final List<SQLSelectOrderByItem> items;
    private boolean sibings;
    
    public SQLOrderBy() {
        this.items = new ArrayList<SQLSelectOrderByItem>();
    }
    
    public SQLOrderBy(final SQLExpr expr) {
        this.items = new ArrayList<SQLSelectOrderByItem>();
        final SQLSelectOrderByItem item = new SQLSelectOrderByItem(expr);
        this.addItem(item);
    }
    
    public SQLOrderBy(final SQLExpr expr, final SQLOrderingSpecification type) {
        this.items = new ArrayList<SQLSelectOrderByItem>();
        final SQLSelectOrderByItem item = new SQLSelectOrderByItem(expr, type);
        this.addItem(item);
    }
    
    public void addItem(final SQLSelectOrderByItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    public void addItem(final SQLExpr item) {
        this.addItem(new SQLSelectOrderByItem(item));
    }
    
    public List<SQLSelectOrderByItem> getItems() {
        return this.items;
    }
    
    public boolean isSibings() {
        return this.sibings;
    }
    
    public void setSibings(final boolean sibings) {
        this.sibings = sibings;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            for (int i = 0; i < this.items.size(); ++i) {
                final SQLSelectOrderByItem item = this.items.get(i);
                item.accept(v);
            }
        }
        v.endVisit(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLOrderBy order = (SQLOrderBy)o;
        return this.sibings == order.sibings && this.items.equals(order.items);
    }
    
    @Override
    public int hashCode() {
        int result = this.items.hashCode();
        result = 31 * result + (this.sibings ? 1 : 0);
        return result;
    }
    
    public void addItem(final SQLExpr expr, final SQLOrderingSpecification type) {
        final SQLSelectOrderByItem item = this.createItem();
        item.setExpr(expr);
        item.setType(type);
        this.addItem(item);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        for (final SQLSelectOrderByItem item : this.items) {
            if (item.replace(expr, target)) {
                return true;
            }
        }
        return false;
    }
    
    protected SQLSelectOrderByItem createItem() {
        return new SQLSelectOrderByItem();
    }
    
    @Override
    public SQLOrderBy clone() {
        final SQLOrderBy x = new SQLOrderBy();
        for (final SQLSelectOrderByItem item : this.items) {
            final SQLSelectOrderByItem item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        x.sibings = this.sibings;
        return x;
    }
}
