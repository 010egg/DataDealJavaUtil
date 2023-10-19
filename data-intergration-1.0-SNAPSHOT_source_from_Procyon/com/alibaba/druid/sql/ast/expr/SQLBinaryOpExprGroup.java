// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Set;
import java.util.LinkedHashSet;
import com.alibaba.druid.sql.SQLUtils;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLBinaryOpExprGroup extends SQLExprImpl implements SQLReplaceable
{
    private final SQLBinaryOperator operator;
    private final List<SQLExpr> items;
    private DbType dbType;
    
    public SQLBinaryOpExprGroup(final SQLBinaryOperator operator) {
        this.items = new ArrayList<SQLExpr>();
        this.operator = operator;
    }
    
    public SQLBinaryOpExprGroup(final SQLBinaryOperator operator, final DbType dbType) {
        this.items = new ArrayList<SQLExpr>();
        this.operator = operator;
        this.dbType = dbType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLBinaryOpExprGroup that = (SQLBinaryOpExprGroup)o;
        return this.operator == that.operator && this.items.equals(that.items);
    }
    
    @Override
    public int hashCode() {
        int result = (this.operator != null) ? this.operator.hashCode() : 0;
        result = 31 * result + this.items.hashCode();
        return result;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.items.size(); ++i) {
                final SQLExpr item = this.items.get(i);
                item.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLExpr clone() {
        final SQLBinaryOpExprGroup x = new SQLBinaryOpExprGroup(this.operator);
        for (final SQLExpr item : this.items) {
            final SQLExpr item2 = item.clone();
            item2.setParent(this);
            x.items.add(item2);
        }
        return x;
    }
    
    @Override
    public List getChildren() {
        return this.items;
    }
    
    public void add(final SQLExpr item) {
        this.add(this.items.size(), item);
    }
    
    public void add(final int index, final SQLExpr item) {
        if (item instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)item;
            if (binaryOpExpr.getOperator() == this.operator) {
                this.add(binaryOpExpr.getLeft());
                this.add(binaryOpExpr.getRight());
                return;
            }
        }
        else if (item instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)item;
            if (group.operator == this.operator) {
                for (final SQLExpr sqlExpr : group.getItems()) {
                    this.add(sqlExpr);
                }
                return;
            }
        }
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(index, item);
    }
    
    public List<SQLExpr> getItems() {
        return this.items;
    }
    
    public SQLBinaryOperator getOperator() {
        return this.operator;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, this.dbType);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        boolean replaced = false;
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i) == expr) {
                if (target == null) {
                    this.items.remove(i);
                }
                else if (target instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)target).getOperator() == this.operator) {
                    this.items.remove(i);
                    final List<SQLExpr> list = SQLBinaryOpExpr.split(target, this.operator);
                    for (int j = 0; j < list.size(); ++j) {
                        final SQLExpr o = list.get(j);
                        o.setParent(this);
                        this.items.add(i + j, o);
                    }
                }
                else {
                    target.setParent(this);
                    this.items.set(i, target);
                }
                replaced = true;
            }
        }
        if (this.items.size() == 1 && replaced) {
            SQLUtils.replaceInParent(this, this.items.get(0));
        }
        if (this.items.size() == 0) {
            SQLUtils.replaceInParent(this, null);
        }
        return replaced;
    }
    
    public void optimize() {
        List<Integer> dupIndexList = null;
        final Set<SQLExpr> itemSet = new LinkedHashSet<SQLExpr>();
        for (int i = 0; i < this.items.size(); ++i) {
            if (!itemSet.add(this.items.get(i))) {
                if (dupIndexList == null) {
                    dupIndexList = new ArrayList<Integer>();
                }
                dupIndexList.add(i);
            }
        }
        if (dupIndexList != null) {
            for (int i = dupIndexList.size() - 1; i >= 0; --i) {
                final int index = dupIndexList.get(i);
                this.items.remove(index);
            }
        }
    }
}
