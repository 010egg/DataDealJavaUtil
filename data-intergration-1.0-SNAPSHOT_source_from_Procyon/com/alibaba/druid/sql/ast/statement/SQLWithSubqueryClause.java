// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLWithSubqueryClause extends SQLObjectImpl
{
    private Boolean recursive;
    private final List<Entry> entries;
    
    public SQLWithSubqueryClause() {
        this.entries = new ArrayList<Entry>();
    }
    
    @Override
    public SQLWithSubqueryClause clone() {
        final SQLWithSubqueryClause x = new SQLWithSubqueryClause();
        x.recursive = this.recursive;
        for (final Entry entry : this.entries) {
            final Entry entry2 = entry.clone();
            entry2.setParent(x);
            x.entries.add(entry2);
        }
        return x;
    }
    
    public List<Entry> getEntries() {
        return this.entries;
    }
    
    public void addEntry(final Entry entry) {
        if (entry != null) {
            entry.setParent(this);
        }
        this.entries.add(entry);
    }
    
    public Boolean getRecursive() {
        return this.recursive;
    }
    
    public void setRecursive(final Boolean recursive) {
        this.recursive = recursive;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.entries.size(); ++i) {
                final Entry entry = this.entries.get(i);
                if (entry != null) {
                    entry.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    public Entry findEntry(final long alias_hash) {
        if (alias_hash == 0L) {
            return null;
        }
        for (final Entry entry : this.entries) {
            if (entry.aliasHashCode64() == alias_hash) {
                return entry;
            }
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
        final SQLWithSubqueryClause that = (SQLWithSubqueryClause)o;
        if (this.recursive != null) {
            if (this.recursive.equals(that.recursive)) {
                return this.entries.equals(that.entries);
            }
        }
        else if (that.recursive == null) {
            return this.entries.equals(that.entries);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.recursive != null) ? this.recursive.hashCode() : 0;
        result = 31 * result + this.entries.hashCode();
        return result;
    }
    
    public static class Entry extends SQLTableSourceImpl implements SQLReplaceable
    {
        protected final List<SQLName> columns;
        protected SQLSelect subQuery;
        protected SQLStatement returningStatement;
        protected SQLExpr expr;
        
        public Entry() {
            this.columns = new ArrayList<SQLName>();
        }
        
        public Entry(final String alias, final SQLSelect select) {
            this.columns = new ArrayList<SQLName>();
            this.setAlias(alias);
            this.setSubQuery(select);
        }
        
        public Entry(final String alias, final SQLExpr expr) {
            this.columns = new ArrayList<SQLName>();
            this.setAlias(alias);
            this.setExpr(expr);
        }
        
        public void cloneTo(final Entry x) {
            for (final SQLName column : this.columns) {
                final SQLName column2 = column.clone();
                column2.setParent(x);
                x.columns.add(column2);
            }
            if (this.subQuery != null) {
                x.setSubQuery(this.subQuery.clone());
            }
            if (this.returningStatement != null) {
                this.setReturningStatement(this.returningStatement.clone());
            }
            x.alias = this.alias;
            x.expr = this.expr;
        }
        
        @Override
        public boolean replace(final SQLExpr expr, final SQLExpr target) {
            if (this.flashback == expr) {
                this.setFlashback(target);
                return true;
            }
            for (int i = 0; i < this.columns.size(); ++i) {
                if (this.columns.get(i) == expr) {
                    target.setParent(this);
                    this.columns.set(i, (SQLName)expr);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public Entry clone() {
            final Entry x = new Entry();
            this.cloneTo(x);
            return x;
        }
        
        public SQLExpr getExpr() {
            return this.expr;
        }
        
        public void setExpr(final SQLExpr expr) {
            this.expr = expr;
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                for (int i = 0; i < this.columns.size(); ++i) {
                    final SQLExpr column = this.columns.get(i);
                    if (column != null) {
                        column.accept(visitor);
                    }
                }
                if (this.subQuery != null) {
                    this.subQuery.accept(visitor);
                }
                if (this.returningStatement != null) {
                    this.returningStatement.accept(visitor);
                }
                if (this.expr != null) {
                    this.expr.accept(visitor);
                }
            }
            visitor.endVisit(this);
        }
        
        public SQLSelect getSubQuery() {
            return this.subQuery;
        }
        
        public void setSubQuery(final SQLSelect subQuery) {
            if (subQuery != null) {
                subQuery.setParent(this);
            }
            this.subQuery = subQuery;
        }
        
        public SQLStatement getReturningStatement() {
            return this.returningStatement;
        }
        
        public void setReturningStatement(final SQLStatement returningStatement) {
            if (returningStatement != null) {
                returningStatement.setParent(this);
            }
            this.returningStatement = returningStatement;
        }
        
        public List<SQLName> getColumns() {
            return this.columns;
        }
        
        @Override
        public SQLTableSource findTableSourceWithColumn(final long columnNameHash, final String columnName, final int option) {
            for (final SQLName column : this.columns) {
                if (column.nameHashCode64() == columnNameHash) {
                    return this;
                }
            }
            if (this.subQuery != null) {
                final SQLSelectQueryBlock queryBlock = this.subQuery.getFirstQueryBlock();
                if (queryBlock != null && queryBlock.findSelectItem(columnNameHash) != null) {
                    return this;
                }
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
            if (!super.equals(o)) {
                return false;
            }
            final Entry entry = (Entry)o;
            if (!this.columns.equals(entry.columns)) {
                return false;
            }
            if (this.subQuery != null) {
                if (this.subQuery.equals(entry.subQuery)) {
                    return (this.returningStatement != null) ? this.returningStatement.equals(entry.returningStatement) : (entry.returningStatement == null);
                }
            }
            else if (entry.subQuery == null) {
                return (this.returningStatement != null) ? this.returningStatement.equals(entry.returningStatement) : (entry.returningStatement == null);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.columns != null) ? this.columns.hashCode() : 0);
            result = 31 * result + ((this.subQuery != null) ? this.subQuery.hashCode() : 0);
            result = 31 * result + ((this.returningStatement != null) ? this.returningStatement.hashCode() : 0);
            return result;
        }
    }
}
