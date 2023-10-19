// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;

public class SQLSubqueryTableSource extends SQLTableSourceImpl
{
    protected SQLSelect select;
    protected List<SQLName> columns;
    
    public SQLSubqueryTableSource() {
        this.columns = new ArrayList<SQLName>();
    }
    
    public SQLSubqueryTableSource(final String alias) {
        super(alias);
        this.columns = new ArrayList<SQLName>();
    }
    
    public SQLSubqueryTableSource(final SQLSelect select, final String alias) {
        super(alias);
        this.columns = new ArrayList<SQLName>();
        this.setSelect(select);
    }
    
    public SQLSubqueryTableSource(final SQLSelect select) {
        this.columns = new ArrayList<SQLName>();
        this.setSelect(select);
    }
    
    public SQLSubqueryTableSource(final SQLSelectQuery query) {
        this(new SQLSelect(query));
    }
    
    public SQLSubqueryTableSource(final SQLSelectQuery query, final String alias) {
        this(new SQLSelect(query), alias);
    }
    
    public SQLSelect getSelect() {
        return this.select;
    }
    
    public void setSelect(final SQLSelect x) {
        if (x != null) {
            x.setParent(this);
        }
        this.select = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.select != null) {
            this.select.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    public void cloneTo(final SQLSubqueryTableSource x) {
        x.alias = this.alias;
        if (this.select != null) {
            (x.select = this.select.clone()).setParent(x);
        }
        for (final SQLName column : this.columns) {
            final SQLName c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
    }
    
    @Override
    public SQLSubqueryTableSource clone() {
        final SQLSubqueryTableSource x = new SQLSubqueryTableSource();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final String columnName) {
        if (this.select == null) {
            return null;
        }
        final SQLSelectQueryBlock queryBlock = this.select.getFirstQueryBlock();
        if (queryBlock == null) {
            return null;
        }
        if (queryBlock.findSelectItem(columnName) != null) {
            return this;
        }
        return null;
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final long columnNameHash, final String columnName, final int option) {
        if (this.select == null) {
            return null;
        }
        final SQLSelectQueryBlock queryBlock = this.select.getFirstQueryBlock();
        if (queryBlock == null) {
            return null;
        }
        if (queryBlock.findSelectItem(columnNameHash) != null) {
            return this;
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLSubqueryTableSource that = (SQLSubqueryTableSource)o;
        return (this.select != null) ? this.select.equals(that.select) : (that.select == null);
    }
    
    @Override
    public int hashCode() {
        return (this.select != null) ? this.select.hashCode() : 0;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    public void addColumn(final SQLName column) {
        column.setParent(this);
        this.columns.add(column);
    }
    
    @Override
    public SQLColumnDefinition findColumn(final long columnNameHash) {
        final SQLSelectQueryBlock queryBlock = this.select.getFirstQueryBlock();
        if (queryBlock != null) {
            return queryBlock.findColumn(columnNameHash);
        }
        if (this.select.getQuery() instanceof SQLUnionQuery && ((SQLUnionQuery)this.select.getQuery()).getFirstQueryBlock() instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock left = ((SQLUnionQuery)this.select.getQuery()).getFirstQueryBlock();
            return left.findColumn(columnNameHash);
        }
        return null;
    }
}
