// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Collection;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import java.io.Serializable;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLCaseExpr extends SQLExprImpl implements SQLReplaceable, Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<Item> items;
    private SQLExpr valueExpr;
    private SQLExpr elseExpr;
    
    public SQLCaseExpr() {
        this.items = new ArrayList<Item>();
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
    
    public SQLExpr getElseExpr() {
        return this.elseExpr;
    }
    
    public void setElseExpr(final SQLExpr elseExpr) {
        if (elseExpr != null) {
            elseExpr.setParent(this);
        }
        this.elseExpr = elseExpr;
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
    
    public void addItem(final SQLExpr condition, final SQLExpr value) {
        this.addItem(new Item(condition, value));
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.valueExpr != null) {
                this.valueExpr.accept(visitor);
            }
            for (final Item item : this.items) {
                if (item != null) {
                    item.accept(visitor);
                }
            }
            if (this.elseExpr != null) {
                this.elseExpr.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.valueExpr != null) {
            children.add(this.valueExpr);
        }
        children.addAll(this.items);
        if (this.elseExpr != null) {
            children.add(this.elseExpr);
        }
        return children;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.valueExpr == expr) {
            this.setValueExpr(target);
            return true;
        }
        if (this.elseExpr == expr) {
            this.setElseExpr(target);
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.elseExpr == null) ? 0 : this.elseExpr.hashCode());
        result = 31 * result + this.items.hashCode();
        result = 31 * result + ((this.valueExpr == null) ? 0 : this.valueExpr.hashCode());
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
        final SQLCaseExpr other = (SQLCaseExpr)obj;
        if (this.elseExpr == null) {
            if (other.elseExpr != null) {
                return false;
            }
        }
        else if (!this.elseExpr.equals(other.elseExpr)) {
            return false;
        }
        if (!this.items.equals(other.items)) {
            return false;
        }
        if (this.valueExpr == null) {
            if (other.valueExpr != null) {
                return false;
            }
        }
        else if (!this.valueExpr.equals(other.valueExpr)) {
            return false;
        }
        return true;
    }
    
    @Override
    public SQLCaseExpr clone() {
        final SQLCaseExpr x = new SQLCaseExpr();
        for (final Item item : this.items) {
            x.addItem(item.clone());
        }
        if (this.valueExpr != null) {
            x.setValueExpr(this.valueExpr.clone());
        }
        if (this.elseExpr != null) {
            x.setElseExpr(this.elseExpr.clone());
        }
        return x;
    }
    
    @Override
    public SQLDataType computeDataType() {
        for (final Item item : this.items) {
            final SQLExpr expr = item.getValueExpr();
            if (expr != null) {
                final SQLDataType dataType = expr.computeDataType();
                if (dataType != null) {
                    return dataType;
                }
                continue;
            }
        }
        if (this.elseExpr != null) {
            return this.elseExpr.computeDataType();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, (DbType)null);
    }
    
    public static class Item extends SQLObjectImpl implements SQLReplaceable, Serializable
    {
        private static final long serialVersionUID = 1L;
        private SQLExpr conditionExpr;
        private SQLExpr valueExpr;
        
        public Item() {
        }
        
        public Item(final SQLExpr conditionExpr, final SQLExpr valueExpr) {
            this.setConditionExpr(conditionExpr);
            this.setValueExpr(valueExpr);
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
        
        public SQLExpr getValueExpr() {
            return this.valueExpr;
        }
        
        public void setValueExpr(final SQLExpr valueExpr) {
            if (valueExpr != null) {
                valueExpr.setParent(this);
            }
            this.valueExpr = valueExpr;
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                if (this.conditionExpr != null) {
                    this.conditionExpr.accept(visitor);
                }
                if (this.valueExpr != null) {
                    this.valueExpr.accept(visitor);
                }
            }
            visitor.endVisit(this);
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + ((this.conditionExpr == null) ? 0 : this.conditionExpr.hashCode());
            result = 31 * result + ((this.valueExpr == null) ? 0 : this.valueExpr.hashCode());
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
            if (this.valueExpr == null) {
                if (other.valueExpr != null) {
                    return false;
                }
            }
            else if (!this.valueExpr.equals(other.valueExpr)) {
                return false;
            }
            return true;
        }
        
        @Override
        public Item clone() {
            final Item x = new Item();
            if (this.conditionExpr != null) {
                x.setConditionExpr(this.conditionExpr.clone());
            }
            if (this.valueExpr != null) {
                x.setValueExpr(this.valueExpr.clone());
            }
            return x;
        }
        
        @Override
        public boolean replace(final SQLExpr expr, final SQLExpr target) {
            if (this.valueExpr == expr) {
                this.setValueExpr(target);
                return true;
            }
            if (this.conditionExpr == expr) {
                this.setConditionExpr(target);
                return true;
            }
            return false;
        }
    }
}
