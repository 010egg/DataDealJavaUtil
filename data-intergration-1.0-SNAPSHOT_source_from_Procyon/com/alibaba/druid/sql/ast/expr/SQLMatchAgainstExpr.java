// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Collection;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLMatchAgainstExpr extends SQLExprImpl implements SQLReplaceable
{
    private List<SQLExpr> columns;
    private SQLExpr against;
    private SearchModifier searchModifier;
    
    public SQLMatchAgainstExpr() {
        this.columns = new ArrayList<SQLExpr>();
    }
    
    @Override
    public SQLMatchAgainstExpr clone() {
        final SQLMatchAgainstExpr x = new SQLMatchAgainstExpr();
        for (final SQLExpr column : this.columns) {
            final SQLExpr column2 = column.clone();
            column2.setParent(x);
            x.columns.add(column2);
        }
        if (this.against != null) {
            x.setAgainst(this.against.clone());
        }
        x.searchModifier = this.searchModifier;
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.against == expr) {
            this.setAgainst(target);
            return true;
        }
        for (int i = 0; i < this.columns.size(); ++i) {
            if (this.columns.get(i) == expr) {
                target.setParent(this);
                this.columns.set(i, target);
                return true;
            }
        }
        return false;
    }
    
    public List<SQLExpr> getColumns() {
        return this.columns;
    }
    
    public void setColumns(final List<SQLExpr> columns) {
        this.columns = columns;
    }
    
    public SQLExpr getAgainst() {
        return this.against;
    }
    
    public void setAgainst(final SQLExpr against) {
        if (against != null) {
            against.setParent(this);
        }
        this.against = against;
    }
    
    public SearchModifier getSearchModifier() {
        return this.searchModifier;
    }
    
    public void setSearchModifier(final SearchModifier searchModifier) {
        this.searchModifier = searchModifier;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            if (this.columns != null) {
                for (final SQLExpr column : this.columns) {
                    if (column != null) {
                        column.accept(v);
                    }
                }
            }
            if (this.against != null) {
                this.against.accept(v);
            }
        }
        v.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.addAll(this.columns);
        children.add(this.against);
        return children;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.against == null) ? 0 : this.against.hashCode());
        result = 31 * result + ((this.columns == null) ? 0 : this.columns.hashCode());
        result = 31 * result + ((this.searchModifier == null) ? 0 : this.searchModifier.hashCode());
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
        final SQLMatchAgainstExpr other = (SQLMatchAgainstExpr)obj;
        if (this.against == null) {
            if (other.against != null) {
                return false;
            }
        }
        else if (!this.against.equals(other.against)) {
            return false;
        }
        if (this.columns == null) {
            if (other.columns != null) {
                return false;
            }
        }
        else if (!this.columns.equals(other.columns)) {
            return false;
        }
        return this.searchModifier == other.searchModifier;
    }
    
    public enum SearchModifier
    {
        IN_BOOLEAN_MODE("IN BOOLEAN MODE"), 
        IN_NATURAL_LANGUAGE_MODE("IN NATURAL LANGUAGE MODE"), 
        IN_NATURAL_LANGUAGE_MODE_WITH_QUERY_EXPANSION("IN NATURAL LANGUAGE MODE WITH QUERY EXPANSION"), 
        WITH_QUERY_EXPANSION("WITH QUERY EXPANSION");
        
        public final String name;
        public final String name_lcase;
        
        private SearchModifier() {
            this(null);
        }
        
        private SearchModifier(final String name) {
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }
}
