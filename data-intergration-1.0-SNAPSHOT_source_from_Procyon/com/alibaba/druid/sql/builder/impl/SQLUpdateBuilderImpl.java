// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder.impl;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import java.util.Iterator;
import java.util.Map;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.builder.SQLUpdateBuilder;

public class SQLUpdateBuilderImpl extends SQLBuilderImpl implements SQLUpdateBuilder
{
    private SQLUpdateStatement stmt;
    private DbType dbType;
    
    public SQLUpdateBuilderImpl(final DbType dbType) {
        this.dbType = dbType;
    }
    
    public SQLUpdateBuilderImpl(final String sql, final DbType dbType) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + sql);
        }
        if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + sql);
        }
        final SQLUpdateStatement stmt = stmtList.get(0);
        this.stmt = stmt;
        this.dbType = dbType;
    }
    
    public SQLUpdateBuilderImpl(final SQLUpdateStatement stmt, final DbType dbType) {
        this.stmt = stmt;
        this.dbType = dbType;
    }
    
    @Override
    public SQLUpdateBuilderImpl limit(final int rowCount) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SQLUpdateBuilderImpl limit(final int rowCount, final int offset) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SQLUpdateBuilderImpl from(final String table) {
        return this.from(table, null);
    }
    
    @Override
    public SQLUpdateBuilderImpl from(final String table, final String alias) {
        final SQLUpdateStatement update = this.getSQLUpdateStatement();
        final SQLExprTableSource from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias);
        update.setTableSource(from);
        return this;
    }
    
    @Override
    public SQLUpdateBuilderImpl where(final String expr) {
        final SQLUpdateStatement update = this.getSQLUpdateStatement();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        update.setWhere(exprObj);
        return this;
    }
    
    @Override
    public SQLUpdateBuilderImpl whereAnd(final String expr) {
        final SQLUpdateStatement update = this.getSQLUpdateStatement();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        final SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, update.getWhere());
        update.setWhere(newCondition);
        return this;
    }
    
    @Override
    public SQLUpdateBuilderImpl whereOr(final String expr) {
        final SQLUpdateStatement update = this.getSQLUpdateStatement();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        final SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, update.getWhere());
        update.setWhere(newCondition);
        return this;
    }
    
    @Override
    public SQLUpdateBuilderImpl set(final String... items) {
        final SQLUpdateStatement update = this.getSQLUpdateStatement();
        for (final String item : items) {
            final SQLUpdateSetItem updateSetItem = SQLUtils.toUpdateSetItem(item, this.dbType);
            update.addItem(updateSetItem);
        }
        return this;
    }
    
    public SQLUpdateBuilderImpl setValue(final Map<String, Object> values) {
        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            this.setValue(entry.getKey(), entry.getValue());
        }
        return this;
    }
    
    public SQLUpdateBuilderImpl setValue(final String column, final Object value) {
        final SQLUpdateStatement update = this.getSQLUpdateStatement();
        final SQLExpr columnExpr = SQLUtils.toSQLExpr(column, this.dbType);
        final SQLExpr valueExpr = SQLBuilderImpl.toSQLExpr(value, this.dbType);
        final SQLUpdateSetItem item = new SQLUpdateSetItem();
        item.setColumn(columnExpr);
        item.setValue(valueExpr);
        update.addItem(item);
        return this;
    }
    
    public SQLUpdateStatement getSQLUpdateStatement() {
        if (this.stmt == null) {
            this.stmt = this.createSQLUpdateStatement();
        }
        return this.stmt;
    }
    
    public SQLUpdateStatement createSQLUpdateStatement() {
        switch (this.dbType) {
            case mysql:
            case mariadb: {
                return new MySqlUpdateStatement();
            }
            case oracle: {
                return new OracleUpdateStatement();
            }
            case postgresql: {
                return new PGUpdateStatement();
            }
            case sqlserver: {
                return new SQLServerUpdateStatement();
            }
            default: {
                return new SQLUpdateStatement();
            }
        }
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this.stmt, this.dbType);
    }
}
