// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.builder.impl;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.builder.SQLDeleteBuilder;

public class SQLDeleteBuilderImpl implements SQLDeleteBuilder
{
    private SQLDeleteStatement stmt;
    private DbType dbType;
    
    public SQLDeleteBuilderImpl(final DbType dbType) {
        this.dbType = dbType;
    }
    
    public SQLDeleteBuilderImpl(final String sql, final DbType dbType) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + sql);
        }
        if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + sql);
        }
        final SQLDeleteStatement stmt = stmtList.get(0);
        this.stmt = stmt;
        this.dbType = dbType;
    }
    
    public SQLDeleteBuilderImpl(final SQLDeleteStatement stmt, final DbType dbType) {
        this.stmt = stmt;
        this.dbType = dbType;
    }
    
    @Override
    public SQLDeleteBuilderImpl limit(final int rowCount) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SQLDeleteBuilderImpl limit(final int rowCount, final int offset) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SQLDeleteBuilder from(final String table) {
        return this.from(table, null);
    }
    
    @Override
    public SQLDeleteBuilder from(final String table, final String alias) {
        final SQLDeleteStatement delete = this.getSQLDeleteStatement();
        final SQLExprTableSource from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias);
        delete.setTableSource(from);
        return this;
    }
    
    @Override
    public SQLDeleteBuilder where(final String expr) {
        final SQLDeleteStatement delete = this.getSQLDeleteStatement();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        delete.setWhere(exprObj);
        return this;
    }
    
    @Override
    public SQLDeleteBuilder whereAnd(final String expr) {
        final SQLDeleteStatement delete = this.getSQLDeleteStatement();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        final SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, delete.getWhere());
        delete.setWhere(newCondition);
        return this;
    }
    
    @Override
    public SQLDeleteBuilder whereOr(final String expr) {
        final SQLDeleteStatement delete = this.getSQLDeleteStatement();
        final SQLExpr exprObj = SQLUtils.toSQLExpr(expr, this.dbType);
        final SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, delete.getWhere());
        delete.setWhere(newCondition);
        return this;
    }
    
    public SQLDeleteStatement getSQLDeleteStatement() {
        if (this.stmt == null) {
            this.stmt = this.createSQLDeleteStatement();
        }
        return this.stmt;
    }
    
    public SQLDeleteStatement createSQLDeleteStatement() {
        switch (this.dbType) {
            case oracle: {
                return new OracleDeleteStatement();
            }
            case mysql: {
                return new MySqlDeleteStatement();
            }
            case postgresql: {
                return new PGDeleteStatement();
            }
            default: {
                return new SQLDeleteStatement();
            }
        }
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this.stmt, this.dbType);
    }
}
