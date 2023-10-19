// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.SQLUtils;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDeleteStatement extends SQLStatementImpl implements SQLReplaceable
{
    protected SQLWithSubqueryClause with;
    protected SQLTableSource tableSource;
    protected SQLExpr where;
    protected SQLTableSource from;
    protected SQLTableSource using;
    protected boolean only;
    
    public SQLDeleteStatement() {
        this.only = false;
    }
    
    public SQLDeleteStatement(final DbType dbType) {
        super(dbType);
        this.only = false;
    }
    
    protected void cloneTo(final SQLDeleteStatement x) {
        if (this.headHints != null) {
            for (final SQLCommentHint h : this.headHints) {
                final SQLCommentHint h2 = h.clone();
                h2.setParent(x);
                x.headHints.add(h2);
            }
        }
        if (this.with != null) {
            x.setWith(this.with.clone());
        }
        if (this.tableSource != null) {
            x.setTableSource(this.tableSource.clone());
        }
        if (this.where != null) {
            x.setWhere(this.where.clone());
        }
        if (this.from != null) {
            x.setFrom(this.from.clone());
        }
        if (this.using != null) {
            x.setUsing(this.using.clone());
        }
        x.only = this.only;
    }
    
    @Override
    public SQLDeleteStatement clone() {
        final SQLDeleteStatement x = new SQLDeleteStatement();
        this.cloneTo(x);
        return x;
    }
    
    public SQLTableSource getTableSource() {
        return this.tableSource;
    }
    
    public SQLExprTableSource getExprTableSource() {
        return (SQLExprTableSource)this.getTableSource();
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
            final SQLExprTableSource exprTableSource = (SQLExprTableSource)this.tableSource;
            return (SQLName)exprTableSource.getExpr();
        }
        if (this.tableSource instanceof SQLSubqueryTableSource) {
            final SQLSelectQuery selectQuery = ((SQLSubqueryTableSource)this.tableSource).getSelect().getQuery();
            if (selectQuery instanceof SQLSelectQueryBlock) {
                final SQLTableSource subQueryTableSource = ((SQLSelectQueryBlock)selectQuery).getFrom();
                if (subQueryTableSource instanceof SQLExprTableSource) {
                    final SQLExpr subQueryTableSourceExpr = ((SQLExprTableSource)subQueryTableSource).getExpr();
                    return (SQLName)subQueryTableSourceExpr;
                }
            }
        }
        return null;
    }
    
    public void setTableName(final SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }
    
    public void setTableName(final String name) {
        this.setTableName(new SQLIdentifierExpr(name));
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
    
    public String getAlias() {
        return this.tableSource.getAlias();
    }
    
    public void setAlias(final String alias) {
        this.tableSource.setAlias(alias);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.with != null) {
                this.with.accept(visitor);
            }
            if (this.tableSource != null) {
                this.tableSource.accept(visitor);
            }
            if (this.where != null) {
                this.where.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.with != null) {
            children.add(this.with);
        }
        children.add(this.tableSource);
        if (this.where != null) {
            children.add(this.where);
        }
        return children;
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
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.where == expr) {
            this.setWhere(target);
            return true;
        }
        return false;
    }
    
    public boolean isOnly() {
        return this.only;
    }
    
    public void setOnly(final boolean only) {
        this.only = only;
    }
    
    public SQLTableSource getUsing() {
        return this.using;
    }
    
    public void setUsing(final SQLTableSource using) {
        this.using = using;
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
    
    public boolean addWhere(final SQLExpr where) {
        if (where == null) {
            return false;
        }
        this.addCondition(where);
        return true;
    }
}
