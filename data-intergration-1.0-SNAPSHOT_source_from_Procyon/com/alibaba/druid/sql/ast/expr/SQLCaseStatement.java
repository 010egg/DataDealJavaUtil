// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObjectImpl;
import java.util.Collection;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import java.io.Serializable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCaseStatement extends SQLStatementImpl implements Serializable
{
    private final List<Item> items;
    private SQLExpr valueExpr;
    private List<SQLStatement> elseStatements;
    
    public SQLCaseStatement() {
        this.items = new ArrayList<Item>();
        this.elseStatements = new ArrayList<SQLStatement>();
    }
    
    public SQLExpr getValueExpr() {
        return this.valueExpr;
    }
    
    public void setValueExpr(final SQLExpr valueExpr) {
        if (valueExpr != null) {
            valueExpr.setParent(this);
        }
        this.valueExpr = valueExpr;
    }
    
    public List<SQLStatement> getElseStatements() {
        return this.elseStatements;
    }
    
    public List<Item> getItems() {
        return this.items;
    }
    
    public void addItem(final Item item) {
        if (item != null) {
            item.setParent(this);
            this.items.add(item);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.valueExpr != null) {
                this.valueExpr.accept(visitor);
            }
            if (this.items != null) {
                for (final Item item : this.items) {
                    if (item != null) {
                        item.accept(visitor);
                    }
                }
            }
            if (this.elseStatements != null) {
                for (final SQLStatement item2 : this.elseStatements) {
                    if (item2 != null) {
                        item2.accept(visitor);
                    }
                }
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.valueExpr != null) {
            children.add(this.valueExpr);
        }
        children.addAll(this.items);
        children.addAll(this.elseStatements);
        return children;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLCaseStatement that = (SQLCaseStatement)o;
        if (!this.items.equals(that.items)) {
            return false;
        }
        if (this.valueExpr != null) {
            if (this.valueExpr.equals(that.valueExpr)) {
                return (this.elseStatements != null) ? this.elseStatements.equals(that.elseStatements) : (that.elseStatements == null);
            }
        }
        else if (that.valueExpr == null) {
            return (this.elseStatements != null) ? this.elseStatements.equals(that.elseStatements) : (that.elseStatements == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.items.hashCode();
        result = 31 * result + ((this.valueExpr != null) ? this.valueExpr.hashCode() : 0);
        result = 31 * result + ((this.elseStatements != null) ? this.elseStatements.hashCode() : 0);
        return result;
    }
    
    public static class Item extends SQLObjectImpl implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private SQLExpr conditionExpr;
        private SQLStatement statement;
        
        public Item() {
        }
        
        public Item(final SQLExpr conditionExpr, final SQLStatement statement) {
            this.setConditionExpr(conditionExpr);
            this.setStatement(statement);
        }
        
        public SQLExpr getConditionExpr() {
            return this.conditionExpr;
        }
        
        public void setConditionExpr(final SQLExpr conditionExpr) {
            if (conditionExpr != null) {
                conditionExpr.setParent(this);
            }
            this.conditionExpr = conditionExpr;
        }
        
        public SQLStatement getStatement() {
            return this.statement;
        }
        
        public void setStatement(final SQLStatement statement) {
            if (statement != null) {
                statement.setParent(this);
            }
            this.statement = statement;
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                if (this.conditionExpr != null) {
                    this.conditionExpr.accept(visitor);
                }
                if (this.statement != null) {
                    this.statement.accept(visitor);
                }
            }
            visitor.endVisit(this);
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.conditionExpr == null) ? 0 : this.conditionExpr.hashCode());
            result = 31 * result + ((this.statement == null) ? 0 : this.statement.hashCode());
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
            final Item other = (Item)obj;
            if (this.conditionExpr == null) {
                if (other.conditionExpr != null) {
                    return false;
                }
            }
            else if (!this.conditionExpr.equals(other.conditionExpr)) {
                return false;
            }
            if (this.statement == null) {
                if (other.statement != null) {
                    return false;
                }
            }
            else if (!this.statement.equals(other.statement)) {
                return false;
            }
            return true;
        }
    }
}
