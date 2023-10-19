// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder.impl;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.builder.SQLSelectBuilder;

public class SQLSelectBuilderImpl implements SQLSelectBuilder
{
    private SQLSelectStatement stmt;
    private DbType dbType;
    
    public SQLSelectBuilderImpl(final DbType dbType) {
        this(new SQLSelectStatement(), dbType);
    }
    
    public SQLSelectBuilderImpl(final String sql, final DbType dbType) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + sql);
        }
        if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + sql);
        }
        final SQLSelectStatement stmt = stmtList.get(0);
        this.stmt = stmt;
        this.dbType = dbType;
    }
    
    public SQLSelectBuilderImpl(final SQLSelectStatement stmt, final DbType dbType) {
        this.stmt = stmt;
        this.dbType = dbType;
    }
    
    public SQLSelect getSQLSelect() {
        if (this.stmt.getSelect() == null) {
            this.stmt.setSelect(this.createSelect());
        }
        return this.stmt.getSelect();
    }
    
    @Override
    public SQLSelectStatement getSQLSelectStatement() {
        return this.stmt;
    }
    
    @Override
    public SQLSelectBuilderImpl select(final String... columns) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        for (final String column : columns) {
            final SQLSelectItem selectItem = SQLUtils.toSelectItem(column, this.dbType);
            queryBlock.addSelectItem(selectItem);
        }
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl selectWithAlias(final String column, final String alias) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        final SQLExpr columnExpr = SQLUtils.toSQLExpr(column, this.dbType);
        final SQLSelectItem selectItem = new SQLSelectItem(columnExpr, alias);
        queryBlock.addSelectItem(selectItem);
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl from(final String table) {
        return this.from(table, (String)null);
    }
    
    @Override
    public SQLSelectBuilderImpl from(final String table, final String alias) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        final SQLExprTableSource from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias);
        queryBlock.setFrom(from);
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl orderBy(final String... columns) {
        final SQLSelect select = this.getSQLSelect();
        SQLOrderBy orderBy = select.getOrderBy();
        if (orderBy == null) {
            orderBy = this.createOrderBy();
            select.setOrderBy(orderBy);
        }
        for (final String column : columns) {
            final SQLSelectOrderByItem orderByItem = SQLUtils.toOrderByItem(column, this.dbType);
            orderBy.addItem(orderByItem);
        }
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl groupBy(final String expr) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        SQLSelectGroupByClause groupBy = queryBlock.getGroupBy();
        if (groupBy == null) {
            groupBy = this.createGroupBy();
            queryBlock.setGroupBy(groupBy);
        }
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        groupBy.addItem(exprObj);
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl having(final String expr) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        SQLSelectGroupByClause groupBy = queryBlock.getGroupBy();
        if (groupBy == null) {
            groupBy = this.createGroupBy();
            queryBlock.setGroupBy(groupBy);
        }
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        groupBy.setHaving(exprObj);
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl into(final String expr) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        queryBlock.setInto(exprObj);
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl where(final String expr) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        queryBlock.setWhere(exprObj);
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl whereAnd(final String expr) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        queryBlock.addWhere(SQLUtils.toSQLExpr(expr, this.dbType));
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl whereOr(final String expr) {
        final SQLSelectQueryBlock queryBlock = this.getQueryBlock();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        final SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, queryBlock.getWhere());
        queryBlock.setWhere(newCondition);
        return this;
    }
    
    @Override
    public SQLSelectBuilderImpl limit(final int rowCount) {
        return this.limit(rowCount, 0);
    }
    
    @Override
    public SQLSelectBuilderImpl limit(final int rowCount, final int offset) {
        this.getQueryBlock().limit(rowCount, offset);
        return this;
    }
    
    protected SQLSelectQueryBlock getQueryBlock() {
        final SQLSelect select = this.getSQLSelect();
        SQLSelectQuery query = select.getQuery();
        if (query == null) {
            query = SQLParserUtils.createSelectQueryBlock(this.dbType);
            select.setQuery(query);
        }
        if (!(query instanceof SQLSelectQueryBlock)) {
            throw new IllegalStateException("not support from, class : " + query.getClass().getName());
        }
        final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)query;
        return queryBlock;
    }
    
    protected SQLSelect createSelect() {
        return new SQLSelect();
    }
    
    protected SQLSelectQuery createSelectQueryBlock() {
        return SQLParserUtils.createSelectQueryBlock(this.dbType);
    }
    
    protected SQLOrderBy createOrderBy() {
        return new SQLOrderBy();
    }
    
    protected SQLSelectGroupByClause createGroupBy() {
        return new SQLSelectGroupByClause();
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this.stmt, this.dbType);
    }
}
