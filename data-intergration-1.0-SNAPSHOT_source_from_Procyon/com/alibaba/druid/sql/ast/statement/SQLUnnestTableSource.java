// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;

public class SQLUnnestTableSource extends SQLTableSourceImpl
{
    private final List<SQLExpr> items;
    protected List<SQLName> columns;
    private boolean ordinality;
    
    public SQLUnnestTableSource() {
        this.items = new ArrayList<SQLExpr>();
        this.columns = new ArrayList<SQLName>();
        this.ordinality = false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.items);
            this.acceptChild(v, this.columns);
        }
        v.endVisit(this);
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLName column) {
        column.setParent(this);
        this.columns.add(column);
    }
    
    public boolean isOrdinality() {
        return this.ordinality;
    }
    
    public void setOrdinality(final boolean ordinality) {
        this.ordinality = ordinality;
    }
    
    public List<SQLExpr> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLExpr item) {
        item.setParent(this);
        this.items.add(item);
    }
    
    @Override
    public SQLUnnestTableSource clone() {
        final SQLUnnestTableSource x = new SQLUnnestTableSource();
        for (final SQLExpr item : this.items) {
            final SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        for (final SQLName column : this.columns) {
            final SQLName c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        x.alias = this.alias;
        return x;
    }
}
