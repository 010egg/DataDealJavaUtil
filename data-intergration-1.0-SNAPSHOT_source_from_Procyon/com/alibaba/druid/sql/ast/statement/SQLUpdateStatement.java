// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.SQLUtils;
import java.util.Collection;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.DbType;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLUpdateStatement extends SQLStatementImpl implements SQLReplaceable
{
    protected SQLWithSubqueryClause with;
    protected final List<SQLUpdateSetItem> items;
    protected SQLExpr where;
    protected SQLTableSource from;
    protected SQLTableSource tableSource;
    protected List<SQLExpr> returning;
    protected List<SQLAssignItem> partitions;
    protected SQLOrderBy orderBy;
    
    public SQLUpdateStatement() {
        this.items = new ArrayList<SQLUpdateSetItem>();
    }
    
    public void cloneTo(final SQLUpdateStatement x) {
        x.dbType = this.dbType;
        x.afterSemi = this.afterSemi;
        if (this.with != null) {
            x.setWith(this.with.clone());
            x.with.setParent(x);
        }
        if (this.where != null) {
            (x.where = this.where.clone()).setParent(x);
        }
        if (this.tableSource != null) {
            x.setTableSource(this.tableSource.clone());
            x.tableSource.setParent(x);
        }
        for (final SQLUpdateSetItem item : this.items) {
            final SQLUpdateSetItem clone = item.clone();
            clone.setParent(x);
            x.getItems().add(clone);
        }
        if (this.returning != null) {
            for (final SQLExpr item2 : this.returning) {
                final SQLExpr clone2 = item2.clone();
                clone2.setParent(x);
                x.getReturning().add(clone2);
            }
        }
        if (this.orderBy != null) {
            (x.orderBy = this.orderBy.clone()).setParent(x);
        }
    }
    
    @Override
    public SQLUpdateStatement clone() {
        final SQLUpdateStatement x = new SQLUpdateStatement();
        this.cloneTo(x);
        return x;
    }
    
    public SQLUpdateStatement(final DbType dbType) {
        super(dbType);
        this.items = new ArrayList<SQLUpdateSetItem>();
    }
    
    public SQLTableSource getTableSource() {
        return this.tableSource;
    }
    
    public void setTableSource(final SQLExpr expr) {
        this.setTableSource(new SQLExprTableSource(expr));
    }
    
    public void setTableSource(final SQLTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }
    
    public SQLName getTableName() {
        if (this.tableSource instanceof SQLExprTableSource) {
            return ((SQLExprTableSource)this.tableSource).getName();
        }
        if (this.tableSource instanceof SQLJoinTableSource) {
            final SQLTableSource left = ((SQLJoinTableSource)this.tableSource).getLeft();
            if (left instanceof SQLExprTableSource) {
                return ((SQLExprTableSource)left).getName();
            }
        }
        return null;
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
    
    public void setPartitions(final List<SQLAssignItem> partitions) {
        this.partitions = partitions;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr where) {
        if (where != null) {
            where.setParent(this);
        }
        this.where = where;
    }
    
    public List<SQLUpdateSetItem> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLUpdateSetItem item) {
        this.items.add(item);
        item.setParent(this);
    }
    
    public List<SQLExpr> getReturning() {
        if (this.returning == null) {
            this.returning = new ArrayList<SQLExpr>(2);
        }
        return this.returning;
    }
    
    public SQLTableSource getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLTableSource from) {
        if (from != null) {
            from.setParent(this);
        }
        this.from = from;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor);
        }
        visitor.endVisit(this);
    }
    
    protected void acceptChild(final SQLASTVisitor visitor) {
        if (this.with != null) {
            this.with.accept(visitor);
        }
        if (this.tableSource != null) {
            this.tableSource.accept(visitor);
        }
        if (this.from != null) {
            this.from.accept(visitor);
        }
        for (int i = 0; i < this.items.size(); ++i) {
            final SQLUpdateSetItem item = this.items.get(i);
            if (item != null) {
                item.accept(visitor);
            }
        }
        if (this.where != null) {
            this.where.accept(visitor);
        }
        if (this.orderBy != null) {
            this.orderBy.accept(visitor);
        }
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.tableSource != null) {
            children.add(this.tableSource);
        }
        if (this.from != null) {
            children.add(this.from);
        }
        children.addAll(this.items);
        if (this.where != null) {
            children.add(this.where);
        }
        if (this.orderBy != null) {
            children.add(this.orderBy);
        }
        return children;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.where == expr) {
            this.setWhere(target);
            return true;
        }
        if (this.returning != null) {
            for (int i = 0; i < this.returning.size(); ++i) {
                if (this.returning.get(i) == expr) {
                    target.setParent(this);
                    this.returning.set(i, target);
                    return true;
                }
            }
        }
        return false;
    }
    
    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }
        this.orderBy = orderBy;
    }
    
    public SQLWithSubqueryClause getWith() {
        return this.with;
    }
    
    public void setWith(final SQLWithSubqueryClause with) {
        if (with != null) {
            with.setParent(this);
        }
        this.with = with;
    }
    
    public void addCondition(final String conditionSql) {
        if (conditionSql == null || conditionSql.length() == 0) {
            return;
        }
        final SQLExpr condition = SQLUtils.toSQLExpr(conditionSql, this.dbType);
        this.addCondition(condition);
    }
    
    public void addCondition(final SQLExpr expr) {
        if (expr == null) {
            return;
        }
        this.setWhere(SQLBinaryOpExpr.and(this.where, expr));
    }
    
    public boolean removeCondition(final String conditionSql) {
        if (conditionSql == null || conditionSql.length() == 0) {
            return false;
        }
        final SQLExpr condition = SQLUtils.toSQLExpr(conditionSql, this.dbType);
        return this.removeCondition(condition);
    }
    
    public boolean removeCondition(final SQLExpr condition) {
        if (condition == null) {
            return false;
        }
        if (this.where instanceof SQLBinaryOpExprGroup) {
            final SQLBinaryOpExprGroup group = (SQLBinaryOpExprGroup)this.where;
            int removedCount = 0;
            final List<SQLExpr> items = group.getItems();
            for (int i = items.size() - 1; i >= 0; --i) {
                if (items.get(i).equals(condition)) {
                    items.remove(i);
                    ++removedCount;
                }
            }
            if (items.size() == 0) {
                this.where = null;
            }
            return removedCount > 0;
        }
        if (this.where instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr binaryOpWhere = (SQLBinaryOpExpr)this.where;
            final SQLBinaryOperator operator = binaryOpWhere.getOperator();
            if (operator == SQLBinaryOperator.BooleanAnd || operator == SQLBinaryOperator.BooleanOr) {
                final List<SQLExpr> items = SQLBinaryOpExpr.split(binaryOpWhere);
                int removedCount2 = 0;
                for (int j = items.size() - 1; j >= 0; --j) {
                    final SQLExpr item = items.get(j);
                    if (item.equals(condition) && SQLUtils.replaceInParent(item, null)) {
                        ++removedCount2;
                    }
                }
                return removedCount2 > 0;
            }
        }
        if (condition.equals(this.where)) {
            this.where = null;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLUpdateStatement that = (SQLUpdateStatement)o;
        Label_0062: {
            if (this.with != null) {
                if (this.with.equals(that.with)) {
                    break Label_0062;
                }
            }
            else if (that.with == null) {
                break Label_0062;
            }
            return false;
        }
        if (!this.items.equals(that.items)) {
            return false;
        }
        Label_0113: {
            if (this.where != null) {
                if (this.where.equals(that.where)) {
                    break Label_0113;
                }
            }
            else if (that.where == null) {
                break Label_0113;
            }
            return false;
        }
        Label_0146: {
            if (this.from != null) {
                if (this.from.equals(that.from)) {
                    break Label_0146;
                }
            }
            else if (that.from == null) {
                break Label_0146;
            }
            return false;
        }
        Label_0179: {
            if (this.tableSource != null) {
                if (this.tableSource.equals(that.tableSource)) {
                    break Label_0179;
                }
            }
            else if (that.tableSource == null) {
                break Label_0179;
            }
            return false;
        }
        if (this.returning != null) {
            if (this.returning.equals(that.returning)) {
                return (this.orderBy != null) ? this.orderBy.equals(that.orderBy) : (that.orderBy == null);
            }
        }
        else if (that.returning == null) {
            return (this.orderBy != null) ? this.orderBy.equals(that.orderBy) : (that.orderBy == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.with != null) ? this.with.hashCode() : 0;
        result = 31 * result + this.items.hashCode();
        result = 31 * result + ((this.where != null) ? this.where.hashCode() : 0);
        result = 31 * result + ((this.from != null) ? this.from.hashCode() : 0);
        result = 31 * result + ((this.tableSource != null) ? this.tableSource.hashCode() : 0);
        result = 31 * result + ((this.returning != null) ? this.returning.hashCode() : 0);
        result = 31 * result + ((this.orderBy != null) ? this.orderBy.hashCode() : 0);
        return result;
    }
    
    public boolean addWhere(final SQLExpr where) {
        if (where == null) {
            return false;
        }
        this.addCondition(where);
        return true;
    }
}
